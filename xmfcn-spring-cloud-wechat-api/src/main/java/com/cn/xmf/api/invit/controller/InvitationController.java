package com.cn.xmf.api.invit.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.invit.rpc.InvitationService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.wx.Invitation;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * InvitationController(邀请函)
 * Controller 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 *
 * @author rufei.cn
 * @version 2019-10-26
 */
@RestController
@RequestMapping("/invit/")
@SuppressWarnings("all")
public class InvitationController {

    private static Logger logger = LoggerFactory.getLogger(InvitationController.class);

    @Autowired
    private InvitationService invitationService;

    /**
     * getInvitation:(查询邀请函单条数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping("getInvitation")
    public RetData getInvitation(HttpServletRequest request) {
        RetData retData = new RetData();
        Invitation invitation = new Invitation();
        String idStr = request.getParameter("id");
        long id = StringUtil.stringToLong(idStr);
        if(id>0)
        {
            invitation.setId(id);
        }
        logger.info("getInvitation:(查询邀请函单条数据接口) 开始  invitation={}", invitation);
        Invitation retinvitation = invitationService.getInvitation(invitation);
        retData.setData(retinvitation);
        retData.setCode(ResultCodeMessage.SUCCESS);
        retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        logger.info("getInvitation:(查询邀请函单条数据接口) 结束");
        return retData;
    }

}