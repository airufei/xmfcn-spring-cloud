package com.cn.xmf.job.admin.job.controller;

import com.cn.xmf.base.model.RetCode;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.job.admin.controller.interceptor.PermissionInterceptor;
import com.cn.xmf.job.admin.core.util.I18nUtil;
import com.cn.xmf.job.admin.job.service.XxlJobService;
import com.cn.xmf.job.admin.user.service.UserHelperService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.model.user.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
public class IndexController {
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private XxlJobService xxlJobService;
    @Autowired
    private UserHelperService userHelperService;

    @RequestMapping("/")
    public String index(Model model) {
        Map<String, Object> dashboardMap = xxlJobService.dashboardInfo();
        model.addAllAttributes(dashboardMap);
        logger.info("-----------------------------------1");
        return "index";
    }

    @RequestMapping("/chartInfo")
    @ResponseBody
    public ReturnT<Map<String, Object>> chartInfo(Date startDate, Date endDate) {
        logger.info("-----------------------------------1");
        return xxlJobService.chartInfo(startDate, endDate);
    }

    @RequestMapping("/toLogin")
    public String toLogin(Model model, HttpServletRequest request, HttpServletResponse response) {
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<String> loginDo(HttpServletRequest request, HttpServletResponse response, String userName, String password, String ifRemember) {
        // param

        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return new ReturnT<String>(500, I18nUtil.getString("login_param_empty"));
        }
        HttpSession session = request.getSession();
        Object us = session.getAttribute("user");
        if (us != null) {
            return ReturnT.SUCCESS;
        }
        // do login
        RetData retData = userHelperService.login(userName, password);
        int code = retData.getCode();
        Object data = retData.getData();
        if (code == RetCode.SYS_ERROR) {
            return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
        }
        User user = (User) data;
        if (user == null) {
            return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
        }
        boolean ifRem = StringUtils.isNotBlank(ifRemember) && "on".equals(ifRemember);
        PermissionInterceptor.login(response, userName, password, ifRem);
        logger.info("-----------------------------------5");
        request.getSession().setAttribute("user", user);
        return ReturnT.SUCCESS;
    }

    @RequestMapping(value = "logout")
    @ResponseBody
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        PermissionInterceptor.logout(request, response);
        return "login";
    }

    @RequestMapping("/help")
    public String help() {
        return "help";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
