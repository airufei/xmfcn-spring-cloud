package com.cn.xmf.model.msg;

import com.cn.xmf.enums.MessageType;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 消息数据
 */
public class Message {

    private String title;
    private MessageType messageType;
    private String messageContent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .append("messageType", messageType)
                .append("messageContent", messageContent)
                .toString();
    }
}
