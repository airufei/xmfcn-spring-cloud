package com.cn.xmf.service.dingtalk;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.enums.DingMessageType;
import com.cn.xmf.model.ding.*;
import com.cn.xmf.service.model.SendResult;
import com.cn.xmf.service.model.TextMessage;
import com.cn.xmf.service.util.DingTalkUtil;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        logger.info("sendTextMessageToDingTalk(发送文本信息到钉钉群) 开始 parms={}", parms);
        if (parms == null || parms.isEmpty()) {
            return;
        }
        String webhook = parms.getString("webhook");
        String message = parms.getString("message");
        int len = 0;
        if (message != null) {
            len = message.length();
        }
        if (StringUtil.isBlank(webhook)) {
            //webhook = dictService.getDictValue(ConstantUtil.DICT_TYPE_DINGTALK_URL, dingTalkType);
        }
        Message messageText = new TextMessage(message);
        try {
            SendResult send = DingTalkUtil.send(webhook, messageText);
            if (send != null && send.isSuccess()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("key", webhook);
                jsonObject.put("value", len);
                jsonObject.put("expTime", 30);//30秒
                //ssdbService.setStrByKey(jsonObject);
            }
        } catch (IOException e) {
            logger.info("钉钉发送提醒消息失败：" + e + "=======webhook:" + webhook + "=======message:" + message);

        }
    }

    /**
     * sendMessageToDingTalk(发送不定格式信息到钉钉群)
     *
     * @param parms
     * @return
     */
    public RetData sendMessageToDingTalk(DingMessage dingMessage) {
        RetData result = new RetData();
        result.setCode(ResultCodeMessage.FAILURE);
        result.setMessage(ResultCodeMessage.FAILURE_MESSAGE);
        if (dingMessage == null) {
            result.setMessage(RetCodeAndMessage.PARMS_ERROR_MESSAGE);
            return result;
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
            result.setMessage("钉钉发送提醒消息失败 webhook 为空");
            return result;
        }
        try {
            SendResult send = DingTalkUtil.send(webhook, message);
            if (send != null && send.isSuccess()) {
                result.setMessage(RetCodeAndMessage.SUCCESS_MESSAGE);
                result.setCode(RetCodeAndMessage.SUCCESS);
            }
            logger.info("钉钉发送提醒发送结果: " + send);
        } catch (Exception e) {
            logger.error("钉钉发送提醒消息失败：异常={}", StringUtil.getExceptionMsg(e));
        }
        return result;
    }
}
