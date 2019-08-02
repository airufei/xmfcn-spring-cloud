package com.cn.xmf.service.msg;

import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.enums.MessageType;
import com.cn.xmf.model.msg.Message;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MessageService(消息发送服务)
 *
 * @author 数据字典表
 * @version 2017-11-23
 */
@RestController
@RequestMapping(value = "/server/message/")
@SuppressWarnings("all")
public class MessageService {

    private static Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageHelperService messageHelperService;

    /**
     * sendMessage（消息发送（包括邮件、短信、钉钉等）
     *
     * @param message
     * @return
     */
    @RequestMapping("sendMessage")
    public RetData sendMessage(@RequestBody Message message) {
        RetData retData = new RetData();
        retData.setCode(ResultCodeMessage.FAILURE);
        retData.setMessage(ResultCodeMessage.FAILURE_MESSAGE);
        if (message == null) {
            retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
            return retData;
        }
        MessageType messageType = message.getMessageType();
        String messageContent = message.getMessageContent();
        if (messageType == null) {
            retData.setMessage("入参 messageType 为空");
            return retData;
        }
        if (StringUtil.isBlank(messageContent)) {
            retData.setMessage("入参 messageContent 为空");
            return retData;
        }
        logger.info("sendMessage（消息发送（包括邮件、短信、钉钉等）开始 messageType={}", messageType);
        try {
            switch (messageType) {
                case SMSMESSAGE:
                    break;
                case MAILMESSAGE:
                    retData = messageHelperService.sendMailMessageToDingTalk(messageContent);
                    break;
                case DINGMESSAGE:
                    retData = messageHelperService.sendDingMessageToDingTalk(messageContent);
                    break;
                default:
                    throw new RuntimeException("消息类型错误");
            }
        } catch (Exception e) {
            logger.error("sendMessage（消息发送（包括邮件、短信、钉钉等）异常={}", StringUtil.getExceptionMsg(e));
        }
        return retData;
    }
}
