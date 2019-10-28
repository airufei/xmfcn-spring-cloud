package com.cn.xmf.service.msg;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.ding.DingMessage;
import com.cn.xmf.service.dingtalk.DingTalkService;
import com.cn.xmf.service.mail.MailService;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MessageService(消息发送辅助服务)
 *
 * @author rufei.cn
 * @version 2017-11-23
 */
@Service
@SuppressWarnings("all")
public class MessageHelperService {

    private static Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private DingTalkService dingTalkService;
    @Autowired
    private MailService mailService;


    /**
     * sendDingMessageToDingTalk(发送不定格式信息到钉钉群)
     *
     * @param parms
     * @return
     */
    public RetData sendDingMessageToDingTalk(String messageContent) {
        RetData retData = new RetData();
        retData.setCode(ResultCodeMessage.FAILURE);
        retData.setMessage(ResultCodeMessage.FAILURE_MESSAGE);
        if (StringUtil.isBlank(messageContent)) {
            retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
            return retData;
        }
        DingMessage dingMessage = JSONObject.parseObject(messageContent, DingMessage.class);
        if (dingMessage == null) {
            retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
            return retData;
        }
        retData = dingTalkService.sendMessageToDingTalk(dingMessage);
        return retData;
    }

    /**
     * sendMailMessageToDingTalk(发送邮件消息)
     *
     * @param parms
     * @return
     */
    public RetData sendMailMessageToDingTalk(String messageContent) {
        RetData retData = new RetData();
        retData.setCode(ResultCodeMessage.FAILURE);
        retData.setMessage(ResultCodeMessage.FAILURE_MESSAGE);
        if (StringUtil.isBlank(messageContent)) {
            retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
            return retData;
        }
        DingMessage dingMessage = JSONObject.parseObject(messageContent, DingMessage.class);
        if (dingMessage == null) {
            retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
            return retData;
        }
        retData = dingTalkService.sendMessageToDingTalk(dingMessage);
        return retData;
    }
}
