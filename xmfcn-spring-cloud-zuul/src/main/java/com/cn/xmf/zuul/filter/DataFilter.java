package com.cn.xmf.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

/**
 * 出参全局处理过滤器
 *
 */
@Component
public class DataFilter extends ZuulFilter {

    /**
     * 服务完成之后进行处理
     *
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    /**
     * 过滤器顺序
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 999;
    }

    /**
     * 是否过滤
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        // 根据之前的过滤器结果处理，若前一个过滤器失败，则该过滤器直接跳过
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        return false;
    }

    /**
     * 处理逻辑
     *
     * @return
     */
    @Override
    public Object run() {
//        modifyResponseBodyDataStream();
//        modifyResponseBody();
        //modifyResponseHeader();
        return null;
    }

    /**
     * 修改返回头
     */
    private void modifyResponseHeader() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletResponse servletResponse = context.getResponse();
        servletResponse.addHeader("content-type","image/jpeg;charset=utf-8");
    }

    /**
     * 修改输出数据
     */
    private void modifyResponseBody() {
        try {
            RequestContext context = getCurrentContext();
            InputStream stream = context.getResponseDataStream();
            String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            context.setResponseBody("Modified via setResponseBody(): " + body);
        } catch (IOException e) {
            rethrowRuntimeException(e);
        }
    }

    /**
     * 修改输出数据流
     */
    private void modifyResponseBodyDataStream() {
        try {
            RequestContext context = getCurrentContext();
            InputStream stream = context.getResponseDataStream();
            String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            body = "Modified via setResponseDataStream(): " + body;
            context.setResponseDataStream(new ByteArrayInputStream(body.getBytes("UTF-8")));
        } catch (IOException e) {
            rethrowRuntimeException(e);
        }
    }
}
