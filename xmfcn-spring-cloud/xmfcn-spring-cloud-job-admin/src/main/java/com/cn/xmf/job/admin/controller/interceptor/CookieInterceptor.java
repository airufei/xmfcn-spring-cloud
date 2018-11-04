package com.cn.xmf.job.admin.controller.interceptor;

import com.cn.xmf.job.admin.core.util.FtlUtil;
import com.cn.xmf.job.admin.core.util.I18nUtil;
import com.cn.xmf.util.SpringUtil;
import com.cn.xmf.util.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * push cookies to model as cookieMap
 *
 * @author xuxueli 2015-12-12 18:09:04
 */
public class CookieInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(CookieInterceptor.class);

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("I18nUtil", FtlUtil.generateStaticModel(I18nUtil.class.getName()));
        }
        super.postHandle(request, response, handler, modelAndView);
        String webRoot = getWebRootUrl();
        if (StringUtil.isBlank(webRoot)) {
            return;
        }
        String loginUrl = webRoot + "/jobadmin/toLogin";
        String strUrl = request.getRequestURI();
        if(strUrl!=null&&(strUrl.contains("/toLogin")||strUrl.contains("/login")||strUrl.contains("/logout")||strUrl.contains("/api")))
        {
            return;
        }
        logger.info("请求地址============================" + strUrl);
        logger.info("登录地址============================" + loginUrl);
        HttpSession session = null;
        try {
            session = request.getSession();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (session == null) {
            return;
        }
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            StringUtil.redirect(response, loginUrl);
            return;
        }
    }

    /**
     * 获取当前运行的系统名称
     *
     * @return
     */
    public String getWebRootUrl() {
        Environment environment = (Environment) SpringUtil.getBean("environment");
        if (environment == null) {
            return null;
        }
        return environment.getProperty("xmf.job.login.webRoot");
    }
}
