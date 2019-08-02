package com.cn.xmf.service.dingtalk;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.enums.DingMessageType;
import com.cn.xmf.model.ding.*;
import com.cn.xmf.service.model.dingtalk.SendResult;
import com.cn.xmf.service.model.dingtalk.TextMessage;
import com.cn.xmf.service.util.DingTalkUtil;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@SuppressWarnings("all")
public class DingTalkService {

    private static Logger logger = LoggerFactory.getLogger(DingTalkService.class);
    @Autowired
    private DingTalkHelperService dingTalkHelperService;

    /**
     * sendTextMessageToDingTalk(发送文本信息到钉钉群)
     *
     * @param parms
     * @return
     */
    public void sendTextMessageToDingTalk(JSONObject parms) {
        if (parms == null || parms.isEmpty()) {
            return;
        }
        String webhook = parms.getString("webhook");
        String message = parms.getString("message");
        int len = 0;
        if (message != null) {
            len = message.length();
        }
        Message messageText = new TextMessage(message);
        try {
            SendResult send = DingTalkUtil.send(webhook, messageText);

        } catch (IOException e) {
            logger.error("钉钉发送提醒消息失败：异常={}", StringUtil.getExceptionMsg(e));
        }
    }

    /**
     * sendDingMessageToDingTalk(发送不定格式信息到钉钉群)
     *
     * @param parms
     * @return
     */
    public RetData sendMessageToDingTalk(DingMessage dingMessage) {
        RetData retData = new RetData();
        retData.setCode(ResultCodeMessage.FAILURE);
        retData.setMessage(ResultCodeMessage.FAILURE_MESSAGE);
        if (dingMessage == null) {
            retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
            return retData;
        }
        String webhook = dingMessage.getWebhook();
        DingMessageType dingMessageType = dingMessage.getDingMessageType();
        Message message = null;
        if (dingMessageType == DingMessageType.MARKDWON) {
            message = dingTalkHelperService.setMarkdownMessage(dingMessage);
        } else {
            String msg = dingTalkHelperService.setTextMessage(dingMessage);
            message = new TextMessage(msg);
        }
        int len = 0;
        if (message != null) {
            len = message.toString().length();
        }
        if (StringUtil.isBlank(webhook)) {
            logger.info("钉钉发送提醒消息失败 webhook 为空");
            retData.setMessage("钉钉发送提醒消息失败 webhook 为空");
            return retData;
        }
        try {
            SendResult send = DingTalkUtil.send(webhook, message);
            logger.info("钉钉发送提醒发送结果: " + send);
            if (send != null && send.isSuccess()) {
                retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
                retData.setCode(ResultCodeMessage.SUCCESS);
            }
        } catch (Exception e) {
            retData.setMessage(ResultCodeMessage.EXCEPTION_MESSAGE);
            retData.setCode(ResultCodeMessage.FAILURE);
            logger.error("钉钉发送提醒消息失败：异常={}", StringUtil.getExceptionMsg(e));
        }
        return retData;
    }
}
