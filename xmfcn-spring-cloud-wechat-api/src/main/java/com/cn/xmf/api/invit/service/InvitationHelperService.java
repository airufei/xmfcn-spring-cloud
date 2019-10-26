package com.cn.xmf.api.invit.service;

import com.cn.xmf.api.invit.rpc.InvitationService;
import com.cn.xmf.model.wx.Invitation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Invitation Service(邀请函辅助类)
 * @author rufei.cn
 * @version 2019-10-26
 */
@Service
@SuppressWarnings("all")
public class InvitationHelperService  {

    private static Logger logger = LoggerFactory.getLogger(InvitationHelperService.class);
    @Autowired
	private InvitationService invitationService;

    /**
	 * 获取单条数据
	 * @param invitation
	 * @author rufei.cn
	 * @return
	 */
	 public Invitation getSignleInvitation(Invitation invitation)
	 {
	      return invitationService.getInvitation(invitation);
	 }

}