package com.cn.xmf.service.mail;


import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.enums.MailContentType;
import com.cn.xmf.service.model.MailAuthenticator;
import com.cn.xmf.model.mail.MailMessage;
import com.cn.xmf.service.model.mail.MailServer;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 邮件服务
 */
@SuppressWarnings("all")
@Service
public class MailService {
    private static Logger logger = LoggerFactory.getLogger(MailService.class);


    /**
     * sendMessage（消息发送邮件）
     *
     * @param message
     * @return
     */
    public RetData sendMail(MailMessage message) {
        RetData retData = new RetData();
        retData.setCode(ResultCodeMessage.FAILURE);
        retData.setMessage(ResultCodeMessage.FAILURE_MESSAGE);
        if (message == null) {
            retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
            return retData;
        }
        MailContentType contentType = message.getMailContentType();
        if (contentType == null) {
            retData.setMessage("消息 contentType 为空");
            return retData;
        }
        String mailContent = message.getContent();
        if (StringUtil.isBlank(mailContent)) {
            retData.setMessage("消息 mailContent 为空");
            return retData;
        }
        List<String> addresseeList = message.getAddresseeList();
        if (addresseeList == null || addresseeList.size() <= 0) {
            retData.setMessage("消息 addresseeList 为空");
            return retData;
        }
        // 获取配置的邮件服务器信息
        MailServer mailServer = getMailServer();
        Properties pro = mailServer.getProperties();
        MailAuthenticator mailAuthenticator = null;
        if (mailServer.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            mailAuthenticator = new MailAuthenticator(mailServer.getUserName(), mailServer.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, mailAuthenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(message.getSenderAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(addresseeList.get(0));
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(MimeUtility.encodeText(message.getTitle(), "utf-8", "B"));
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());

            // 设置邮件消息的主要内容
            if (MailContentType.MAIL_TEXT.equals(contentType)) {
                mailMessage.setText(mailContent);
            } else {
                // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
                Multipart mainPart = new MimeMultipart();
                // 创建一个包含HTML内容的MimeBodyPart
                BodyPart html = new MimeBodyPart();
                // 设置HTML内容
                html.setContent(mailContent, "text/html; charset=utf-8");
                mainPart.addBodyPart(html);
                // 将MiniMultipart对象设置为邮件内容
                mailMessage.setContent(mainPart);
            }

            // 发送邮件
            Transport.send(mailMessage);
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        } catch (MessagingException ex) {
            retData.setCode(ResultCodeMessage.FAILURE);
            retData.setMessage(ResultCodeMessage.EXCEPTION_MESSAGE);
            logger.error("sendMessage（消息发送邮件）MessagingException={} ", StringUtil.getExceptionMsg(ex));
        } catch (UnsupportedEncodingException e) {
            retData.setCode(ResultCodeMessage.FAILURE);
            retData.setMessage(ResultCodeMessage.EXCEPTION_MESSAGE);
            logger.error("sendMessage（消息发送邮件）UnsupportedEncodingException={} ", StringUtil.getExceptionMsg(e));
        }
        return retData;
    }

    /**
     * 获取邮箱服务器信息，写死在本地或者配置，数据库自由决定
     *
     * @return
     */
    private MailServer getMailServer() {
        MailServer mailServer = new MailServer();
        return mailServer;
    }
}
