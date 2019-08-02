package com.cn.xmf.service.msg;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetCodeAndMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.ding.DingMessage;
import com.cn.xmf.model.ding.MarkdownMessage;
import com.cn.xmf.service.dingtalk.DingTalkHelperService;
import com.cn.xmf.service.dingtalk.DingTalkService;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MessageService(消息发送辅助服务)
 *
 * @author 数据字典表
 * @version 2017-11-23
 */
@Service
@SuppressWarnings("all")
public class MessageHelperService {

    private static Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private DingTalkService dingTalkService;

    /**
     * sendMessageToDingTalk(发送不定格式信息到钉钉群)
     *
     * @param parms
     * @return
     */
    public RetData sendMessageToDingTalk(String messageContent) {
        RetData retData = new RetData();
        retData.setCode(RetCodeAndMessage.FAILURE);
        retData.setMessage(RetCodeAndMessage.FAILURE_MESSAGE);
        if (StringUtil.isBlank(messageContent)) {
            retData.setMessage(RetCodeAndMessage.PARMS_ERROR_MESSAGE);
            return retData;
        }
        DingMessage dingMessage = JSONObject.parseObject(messageContent, DingMessage.class);
        if (dingMessage == null) {
            retData.setMessage(RetCodeAndMessage.PARMS_ERROR_MESSAGE);
            return retData;
        }
        retData = dingTalkService.sendMessageToDingTalk(dingMessage);
        return retData;
    }
}
