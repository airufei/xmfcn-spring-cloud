package com.cn.xmf.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cn.xmf.base.model.RetCode;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.util.StringUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author airufei
 * @date 2017-11-12 21:43
 * <p>Title: $TITLE</p>
 * <p>Description: 授权过滤器</p>
 */
@Component
public class AppTokenFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(AppTokenFilter.class);


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
       /* RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        logger.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        String ipAddr = getIpAddr(request);
        String ua = request.getHeader("User-Agent");
        logger.info("=================>请求IP:" + ipAddr + " =========>浏览器信息" + ua);
        // 记录入参
        Enumeration<String> enu = request.getParameterNames();
        String requestURI = request.getRequestURI();
        while (enu.hasMoreElements()) {
            String paraName = enu.nextElement();
            logger.info("In parameter: " + requestURI + "  " + paraName + ": " + request.getParameter(paraName));
        }
        String sessionId = request.getRequestedSessionId();
        if (StringUtil.isBlank(sessionId)) {
            request.getSession(true);
        }
        boolean shouldFilter = true;//需要拦截
        // ios导航版静态资源过滤
        if (requestURI.contains("/pc/") || requestURI.contains("/daohangImage/")||requestURI.contains("/api/channel/")) {
            shouldFilter = false;//不拦截
            return shouldFilter;
        }
        String dictValue =null;// dictService.getDictValue(ConstantUtil.DICT_TYPE_ALLOW_URL, requestURI);
        if (StringUtil.isNotBlank(dictValue)) {
            shouldFilter = false;
        }*/
        return false;
    }


    /**
     * 获取访问者IP
     * <p>
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * <p>
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     *
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
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
        Object accessToken = request.getParameter("token");
        // 根据token信息进行相关校验
        logger.info("accessToken: " + accessToken);
        boolean checkToken = checkToken(request, response);
        // token校验不通过，不放行。
        if (!checkToken) {
            requestContext.setSendZuulResponse(false);// 对该请求不路由
            requestContext.set("isSuccess", false);// 设值，让下一个Filter看到上一个Filter的状态
            // 构建返回信息
            RetData dataReturn = new RetData();
            dataReturn.setCode(RetCode.NO_LOGIN);
            dataReturn.setMessage("您的账号已在其它手机登录，请重新登录");
            String jsonString = JSON.toJSONString(dataReturn, SerializerFeature.WriteMapNullValue);
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
        String token = request.getParameter("token");
        if (StringUtil.isBlank(token)) {
            return false;
        }
        String userId = request.getParameter("userId");
        String uuid = request.getParameter("uuid");
        logger.info("访问地址：" + request.getRequestURI() + ",token:" + token + ",userId:" + userId + ",uuid:" + uuid);
        if ((StringUtils.isNotBlank(token) && StringUtils.isNotBlank(uuid))
                || (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(userId))) {
            // 匹配token与uuid
            logger.info("token:" + token + ",userId:" + userId + ",uuid:" + uuid);
            JSONObject param = new JSONObject();
            param.put("userId", userId);
            param.put("token", token);
            param.put("uuid", uuid);
        }
        logger.info("token验证不通过。token:" + token + ",userId:" + userId + ",uuid:" + uuid);
        return false;
    }

}
