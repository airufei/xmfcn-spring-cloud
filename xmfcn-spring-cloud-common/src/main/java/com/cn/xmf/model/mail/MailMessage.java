package com.cn.xmf.model.mail;

import com.cn.xmf.enums.MailContentType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * 邮箱服务器信息
 */
@SuppressWarnings("all")
public class MailMessage {

    /**
     * 主题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    /**
     * 类型
     */
    private MailContentType mailContentType;

    /**
     * 发件人账户
     */
    private String senderAddress;
    /**
     * 收件人
     */
    private List<String> addresseeList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MailContentType getMailContentType() {
        return mailContentType;
    }

    public void setMailContentType(MailContentType mailContentType) {
        this.mailContentType = mailContentType;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public List<String> getAddresseeList() {
        return addresseeList;
    }

    public void setAddresseeList(List<String> addresseeList) {
        this.addresseeList = addresseeList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .append("content", content)
                .append("mailContentType", mailContentType)
                .append("senderAddress", senderAddress)
                .append("addresseeList", addresseeList)
                .toString();
    }
}
