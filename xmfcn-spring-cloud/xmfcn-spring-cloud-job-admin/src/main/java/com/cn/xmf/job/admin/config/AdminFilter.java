package com.cn.xmf.job.admin.config;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.request.ResponeUtil;
import com.cn.xmf.base.request.WrapperedResponse;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*
 * @WebFilter将一个实现了javax.servlet.Filter接口的类定义为过滤器
 * 属性filterName声明过滤器的名称,可选
 * 属性urlPatterns指定要过滤 的URL模式,也可使用属性value来声明.(指定要过滤的URL模式是必选属性)
 */
@WebFilter(filterName = "adminFilter", urlPatterns = "/*")
@Component
@Order(999)
public class AdminFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(AdminFilter.class);

    /**
     * 执行过滤操作
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
        logger.info("执行过滤操作。。。222");
        //JSONObject json = ResponeUtil.getParms(req);
        // logger.info("请求参数============================" + json);
        // byte[] data = wrapResponse.getResponseData();
        //String responseBodyMw = new String(data, "utf-8");
        //logger.info("返回参数============================" + responseBodyMw);
        //ResponeUtil.writeResponse(response, responseBodyMw);
        //logger.info("执行过滤操作。。。");
    }


    /**
     * 过滤器初始化
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        logger.info("过滤器初始化。。。");
    }

    /**
     * 过滤器销毁
     */
    @Override
    public void destroy() {
        logger.info("过滤器销毁。。。");
    }
}
