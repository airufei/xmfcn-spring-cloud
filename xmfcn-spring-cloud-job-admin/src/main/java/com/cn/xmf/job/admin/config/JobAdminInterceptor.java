package com.cn.xmf.job.admin.config;

import com.cn.xmf.job.admin.core.util.FtlUtil;
import com.cn.xmf.job.admin.core.util.I18nUtil;
import com.cn.xmf.job.admin.menu.model.JobMenu;
import com.cn.xmf.job.admin.menu.service.JobMenuService;
import com.cn.xmf.job.admin.user.model.JobUser;
import com.cn.xmf.util.SpringUtil;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.List;

/**
 * push cookies to model as cookieMap
 *
 * @author xuxueli 2015-12-12 18:09:04
 */
public class JobAdminInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(JobAdminInterceptor.class);


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("I18nUtil", FtlUtil.generateStaticModel(I18nUtil.class.getName()));
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String webRoot = getWebRootUrl();
        if (StringUtil.isBlank(webRoot)) {
            throw new Exception("未获取服务域名/IP");
        }
        String loginUrl = webRoot + "/job/toLogin";
        String strUrl = request.getRequestURI();
        if (strUrl != null && (strUrl.contains("/toLogin") || strUrl.contains("/login") || strUrl.contains("/api") || strUrl.contains("/logout"))) {
            return true;
        }
        if (strUrl != null && (strUrl.contains("/plugins/")||strUrl.contains("/adminlte/")||strUrl.contains("/js/")||strUrl.contains("/treeview/")||strUrl.contains("/fonts/")||strUrl.endsWith(".css") || strUrl.endsWith(".js")|| strUrl.endsWith(".jpg")|| strUrl.endsWith(".gif"))) {
            return true;
        }
        //logger.info("登录地址============================" + loginUrl);
        HttpSession session = null;
        try {
            session = request.getSession();
        } catch (Exception e) {
        }
        if (session == null) {
            StringUtil.redirect(response, loginUrl);
            return false;
        }
        JobUser user = (JobUser) request.getSession().getAttribute("user");
        if (user == null || user.getId() == null || user.getId() <= 0) {
            StringUtil.redirect(response, loginUrl);
            return false;
        }
        String roleCode = user.getRoleCode();
        boolean interceptUrl = true; //isInterceptUrl(roleCode, strUrl);//检查权限
        if (!interceptUrl) {
            logger.info("请求地址============================" + strUrl);
            String msg = "权限不足";
            msg = URLEncoder.encode(msg, "utf-8");
            String errorUrl = webRoot + "/job/sysError?errorMsg=" + msg;
            StringUtil.redirect(response, errorUrl);
            return false;
        }
        return true;
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

    /**
     * 判断当前节点是否需要选中
     *
     * @param roleCode
     * @param url
     * @return
     */
    public boolean isInterceptUrl(String roleCode, String url) {
        boolean isInterceptUrl = false;
        JobMenuService jobMenuService = (JobMenuService) SpringUtil.getBean("jobMenuService");
        if (jobMenuService == null) {
            return false;
        }
        if (StringUtil.isBlank(url)) {
            return true;
        }
        if (url.contains("sysError")) {
            return true;
        }
        if (url.contains("/adminlte/")) {
            return true;
        }
        if (url.contains("/plugins/")) {
            return true;
        }
        if (url.contains("/js/")) {
            return true;
        }
        if (url.contains("/treeview/")) {
            return true;
        }
        if (url.contains("/chartInfo")) {
            return true;
        }
        List<JobMenu> roleList = jobMenuService.getJobMenuRoles(0, roleCode);
        if (roleList == null || roleList.size() <= 0) {
            return isInterceptUrl;
        }
        int size = roleList.size();
        for (int i = 0; i < size; i++) {
            JobMenu menuRole = roleList.get(i);
            String roleUrl = menuRole.getUrl();
            if (StringUtil.isBlank(roleUrl)) {
                continue;
            }
            if (url.contains(roleUrl) || roleUrl.contains(url)) {
                isInterceptUrl = true;
                break;
            }
        }
        return isInterceptUrl;
    }
}
