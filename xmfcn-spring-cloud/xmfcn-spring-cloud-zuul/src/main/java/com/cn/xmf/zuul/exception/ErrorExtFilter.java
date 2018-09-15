package com.cn.xmf.zuul.exception;

import com.cn.xmf.enums.DingMessageType;
import com.cn.xmf.model.ding.DingMessage;
import com.cn.xmf.base.model.RetCode;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.util.StringUtil;
import com.cn.xmf.zuul.sys.DingTalkService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 全局异常处理
 *
 * @author airufei
 * @create 2017-11-24 10:54
 **/
@Component
public class ErrorExtFilter extends SendErrorFilter {
    private Logger logger = LoggerFactory.getLogger(ErrorExtFilter.class);

    @Value("${zuul.routes.user-api.serviceId}")
    String serviceName;

    @Autowired
    private DingTalkService dingTalkService;

    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return 5;    // 大于ErrorFilter的值
    }

    @Override
    public boolean shouldFilter() {
        // 判断：仅处理来自post过滤器引起的异常
        RequestContext ctx = RequestContext.getCurrentContext();
        ZuulFilter failedFilter = (ZuulFilter) ctx.get("failed.filter");
        if (failedFilter != null && ("post".equals(failedFilter.filterType()) || "get".equals(failedFilter.filterType()))) {
            return true;
        }
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        logger.error("全局异常处理过滤器 ErrorFilter : {}", throwable.getCause().getMessage());
        ctx.setSendZuulResponse(false);// 对该请求不路由
        ctx.set("isSuccess", false);// 设值，让下一个Filter看到上一个Filter的状态
        dingTalkMessage(ctx, throwable);
        throwable.printStackTrace();
        // 构建返回信息
        RetData dataReturn = new RetData();
        dataReturn.setCode(RetCode.FAILURE);
        dataReturn.setMessage("服务繁忙，请稍后再试");
        dataReturn.setData(new Object());
        return dataReturn;
    }

    /*
     * dingTalkMessage:(发送钉钉消息)
     * @author: airufei
     * @date:2018/1/3 18:08
     * @return:
     */
    private void dingTalkMessage(RequestContext ctx, Throwable throwable) {
        HttpServletRequest request = ctx.getRequest();
        Enumeration<String> enu = request.getParameterNames();
        String requestURI = request.getRequestURI();
        StringBuilder sb = new StringBuilder();
        while (enu.hasMoreElements()) {
            String paraName = enu.nextElement();
            sb.append(" ").append(paraName).append(":").append(request.getParameter(paraName));
        }
        String url = StringUtil.getSystemUrl(request) + requestURI;
        String stackMessage = StringUtil.getExceptionMsg(throwable);
        DingMessage dingMessage = new DingMessage();
        dingMessage.setDingMessageType(DingMessageType.MARKDWON);
        dingMessage.setSysName(serviceName);
        dingMessage.setModuleName(this.getClass().getSimpleName());
        dingMessage.setMethodName(url);
        dingMessage.setParms(sb.toString());
        dingMessage.setExceptionMessage(stackMessage);
        dingTalkService.sendMessageToDingTalk(dingMessage);
        logger.error(stackMessage);
    }


}
