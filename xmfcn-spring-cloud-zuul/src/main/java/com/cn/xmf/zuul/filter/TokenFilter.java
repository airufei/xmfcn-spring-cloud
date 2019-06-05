package com.cn.xmf.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
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
public class TokenFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(TokenFilter.class);


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
