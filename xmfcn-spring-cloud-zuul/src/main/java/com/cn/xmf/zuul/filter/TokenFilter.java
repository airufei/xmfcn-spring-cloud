package com.cn.xmf.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.StringUtil;
import com.cn.xmf.zuul.common.SysCommonService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author rufei.cn
 * @date 2017-11-12 21:43
 * <p>Title: $TITLE</p>
 * <p>Description: 授权过滤器</p>
 */
@Component
@SuppressWarnings("all")
public class TokenFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(TokenFilter.class);
    @Autowired
    private SysCommonService sysCommonService;

    /**
     * 下游服务之前进行处理
     *
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 过滤器顺序
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 判断哪些链接需要过滤
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        // 根据之前的过滤器结果处理，若前一个过滤器失败，则该过滤器直接跳过
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String requestURI = request.getRequestURI();
        boolean shouldFilter = false;
        if (requestURI.contains("/wechat/")) {
            shouldFilter = true;
        }
        return false;
    }

    /**
     * 处理逻辑
     *
     * @return
     */
    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        String sourceCode = request.getParameter("sourceCode");
        String dbCode = sysCommonService.getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "sourceCode");
        if (StringUtil.isBlank(dbCode)||!dbCode.equals(sourceCode)) {
            requestContext.setSendZuulResponse(false);// 对该请求不路由
            requestContext.set("isSuccess", false);// 设值，让下一个Filter看到上一个Filter的状态
            // 构建返回信息
            RetData retData = new RetData();
            retData.setCode(ResultCodeMessage.UNAUTHORIZED);
            retData.setMessage(ResultCodeMessage.UNAUTHORIZED_MESSAGE);
            String jsonString = JSON.toJSONString(retData, SerializerFeature.WriteMapNullValue);
            requestContext.setResponseBody(jsonString);
            requestContext.addZuulResponseHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
            return response;
        }
        requestContext.set("isSuccess", true);// 设值，让下一个Filter看到上一个Filter的状态
        return null;
    }

    /**
     * token校验方法
     *
     * @param request
     * @param response
     * @return true:校验通过；false:校验失败
     */
    private boolean checkToken(HttpServletRequest request, HttpServletResponse response) {

        return true;
    }

}
