package com.cn.xmf.service.dingtalk;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetCodeAndMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.enums.DingMessageType;
import com.cn.xmf.model.ding.*;
import com.cn.xmf.util.DingTalkUtil;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/server/dingTalk/")
@SuppressWarnings("all")
public class DingTalkService {

    private static Logger logger = LoggerFactory.getLogger(DingTalkService.class);
    @Autowired
    private DingTalkHelperService dingTalkHelperService;
    @Value("${dingTalk.type}")
    private String dingTalkType;

    /**
     * sendTextMessageToDingTalk(发送文本信息到钉钉群)
     *
     * @param parms
     * @return
     */
    @RequestMapping("sendTextMessageToDingTalk")
    public void sendTextMessageToDingTalk(@RequestBody JSONObject parms) {
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
        logger.info("钉钉类型：" + dingTalkType + " 钉钉地址:" + webhook);

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
            e.printStackTrace();
        }
    }

    /**
     * sendMessageToDingTalk(发送不定格式信息到钉钉群)
     *
     * @param parms
     * @return
     */
    @RequestMapping("sendMessageToDingTalk")
    public RetData sendMessageToDingTalk(@RequestBody DingMessage dingMessage) {
        logger.info("sendMessageToDingTalk(发送不定格式信息到钉钉群) 开始");
        RetData result = new RetData();
        result.setCode(RetCodeAndMessage.SYS_ERROR);
        result.setMessage(RetCodeAndMessage.SYS_ERROR_MESSAGE);
        if (dingMessage == null) {
            result.setMessage("入参dingMessage为空");
            return result;
        }
        logger.info("sendMessageToDingTalk(发送不定格式信息到钉钉群)parms={}", dingMessage.toString());
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
        String key= dingMessageType+dingMessage.getSysName()+dingMessage.getModuleName()+dingMessage.getMethodName();
        String str = null;//ssdbService.getStrByKey(key);
        if (StringUtil.isNotEmpty(str)) {
            result.setMessage("相同消息30秒内只发送一次");
            return result;//相同消息30秒内只发送一次
        }
        if (StringUtil.isBlank(webhook)) {
            //webhook = dictService.getDictValue(ConstantUtil.DICT_TYPE_DINGTALK_URL, dingTalkType);
        }
        logger.info("钉钉类型：" + dingTalkType + " 钉钉地址:" + webhook);
        if (StringUtil.isBlank(webhook)) {
            logger.info("钉钉发送提醒消息失败 webhook 为空");
            result.setMessage("钉钉发送提醒消息失败 webhook 为空");
            return result;
        }
        try {
            SendResult send = DingTalkUtil.send(webhook, message);
            if (send != null && send.isSuccess()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("key", key);
                jsonObject.put("value", "00000");
                jsonObject.put("expTime", 50);//30秒
               // ssdbService.setStrByKey(jsonObject);
            }
            logger.info("钉钉发送提醒发送结果: " + send);
        } catch (IOException e) {
            logger.error("钉钉发送提醒消息失败：" + e + "=======webhook:" + webhook + "=======message:" + message);
            e.printStackTrace();
        }
        return result;
    }
}
