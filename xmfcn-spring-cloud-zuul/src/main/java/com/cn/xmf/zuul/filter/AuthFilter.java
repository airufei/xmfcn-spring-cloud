package com.cn.xmf.zuul.filter;

import com.cn.xmf.util.StringUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限验证filter
 *
 */
@Component
public class AuthFilter extends ZuulFilter {
    private Logger logger = LoggerFactory.getLogger(TokenFilter.class);
    @Value("${api.name}")
    String apiName;
    @Value("${api.password}")
    String apiPassword;

    @Value("${base-service.name}")
    String serviceName;
    @Value("${base-service.password}")
    String servicePassword;


    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        // 根据之前的过滤器结果处理，若前一个过滤器失败，则该过滤器直接跳过
        RequestContext requestContext = RequestContext.getCurrentContext();
        Boolean isSuccess = (Boolean) requestContext.get("isSuccess");
        if (isSuccess != null && !isSuccess) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String requestURI = request.getRequestURI();
        String url = StringUtil.getSystemUrl(request);
        logger.info("请求requestURI："+requestURI);
        logger.trace("请求url："+url);
        //添加Basic Auth认证信息
        if (requestURI.contains("/server/")) {
            requestContext.addZuulRequestHeader("Authorization", "Basic " + getBase64Credentials(serviceName, servicePassword));
        } else {
            requestContext.addZuulRequestHeader("Authorization", "Basic " + getBase64Credentials(apiName, apiPassword));
        }
        requestContext.set("isSuccess", true);// 设值，让下一个Filter看到上一个Filter的状态
        return null;
    }

    /**
     * 对用户名和密码进行Base64编码
     *
     * @param name
     * @param password
     * @return
     */
    private String getBase64Credentials(String name, String password) {
        String plainCreds = name + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        return new String(base64CredsBytes);
    }
}
