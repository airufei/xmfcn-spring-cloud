package com.cn.xmf.job.admin.job.controller;

import com.cn.xmf.base.model.RetCodeAndMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.job.admin.common.PermissionInterceptor;
import com.cn.xmf.job.admin.core.util.I18nUtil;
import com.cn.xmf.job.admin.job.service.XxlJobService;
import com.cn.xmf.job.admin.user.model.JobUser;
import com.cn.xmf.job.admin.user.service.JobUserHelperService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.util.StringUtil;
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
    private JobUserHelperService jobUserHelperService;

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
    public ReturnT<String> loginDo(HttpServletRequest request, HttpServletResponse response, String phone, String password, String ifRemember) {
        // param
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(password)) {
            return new ReturnT<String>(500, I18nUtil.getString("login_param_empty"));
        }
        HttpSession session = request.getSession();
        Object us = session.getAttribute("user");
        if (us != null) {
            return ReturnT.SUCCESS;
        }
        // do login
        RetData retData = null;
        try {
            retData = jobUserHelperService.login(phone, password);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        int code = retData.getCode();
        String message = retData.getMessage();
        Object data = retData.getData();
        if (code == RetCodeAndMessage.FAILURE || code == RetCodeAndMessage.PARMS_ERROR) {
            return new ReturnT<String>(500, message);
        }
        JobUser user = (JobUser) data;
        if (user == null) {
            return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
        }
        boolean ifRem = StringUtils.isNotBlank(ifRemember) && "on".equals(ifRemember);
        PermissionInterceptor.login(response, ifRem);
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

    @RequestMapping("/sysError")
    public String error(String errorMsg, Model model) {
        model.addAttribute("errorMsg", errorMsg);
        return "common/common-error";
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
