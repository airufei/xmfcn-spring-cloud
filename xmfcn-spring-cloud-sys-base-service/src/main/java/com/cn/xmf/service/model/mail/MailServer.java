package com.cn.xmf.service.model.mail;

import com.cn.xmf.service.enums.MailServerType;

import java.util.Properties;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 邮箱服务器信息
 */
@SuppressWarnings("all")
public class MailServer {

    /**
     * 获得邮件会话属性
     */
    public Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", this.serverHost);
        p.put("mail.smtp.port", this.serverPort);
        p.put("mail.smtp.auth", validate ? "true" : "false");
        return p;
    }

    private MailServerType mailServerType;
    /**
     * 主机地址
     */
    private String serverHost;

    /**
     * 端口号
     */
    private String serverPort;

    /**
     * 是否需要身份验证
     */
    private boolean validate = false;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;

    public MailServerType getMailServerType() {
        return mailServerType;
    }

    public void setMailServerType(MailServerType mailServerType) {
        this.mailServerType = mailServerType;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("mailServerType", mailServerType)
                .append("serverHost", serverHost)
                .append("serverPort", serverPort)
                .append("validate", validate)
                .append("userName", userName)
                .append("password", password)
                .toString();
    }
}
