package com.cn.xmf.api.user.controller;


import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * UserController(用户信息)
 *
 * @author rufei.cn
 * @version 2018-09-16
 */

@RestController
@RequestMapping(value = "/log")
@SuppressWarnings("all")
public class LogController {
    private static Logger logger = LoggerFactory.getLogger(LogController.class);

    /**
     * getList:(获取用户信息分页查询接口)
     *
     * @param request
     * @param parms
     * @return
     * @author rufei.cn
     */
    @RequestMapping("upLog")
    public RetData upLog(HttpServletRequest request) {
        RetData retData = new RetData();
        String logContent = request.getParameter("logContent");
        logger.info("<====================wechat_log=================>={}",logContent);
        retData.setCode(ResultCodeMessage.SUCCESS);
        retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        return retData;
    }
}
