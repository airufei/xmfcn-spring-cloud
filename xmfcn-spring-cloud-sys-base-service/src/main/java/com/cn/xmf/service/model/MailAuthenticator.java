package com.cn.xmf.service.model;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 密码验证
 */
public class MailAuthenticator extends Authenticator {
    String userName = null;
    String password = null;

    public MailAuthenticator() {
    }

    public MailAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}
