package com.cn.xmf.util;

import com.alibaba.fastjson.JSON;
import com.cn.xmf.enums.DingMessageType;
import com.cn.xmf.enums.MailContentType;
import com.cn.xmf.enums.MessageType;
import com.cn.xmf.model.ding.DingMessage;
import com.cn.xmf.model.mail.MailMessage;
import com.cn.xmf.model.msg.Message;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * 消息相关的工具类
 */
@SuppressWarnings("all")
public class MessageUtil {

    /**
     * 组织钉钉提醒数据
     *
     * @param parms
     * @param retData
     * @param msg
     * @param className
     * @param method
     * @return
     */
    public static Message getDingTalkMessage(Object parms, Object retData, Object msg, String sysName, String className, String method) {
        if (msg == null) {
            return null;
        }
        Message message = new Message();
        message.setMessageType(MessageType.DINGMESSAGE);
        DingMessage dingMessage = new DingMessage();
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {

        }
        String ip = null;
        String hostName = null;
        if (addr != null) {
            //获取本机ip
            ip = addr.getHostAddress();
            //获取本机计算机名称
            hostName = addr.getHostName();
        }
        dingMessage.setDingMessageType(DingMessageType.MARKDWON);
        dingMessage.setSysName(sysName);
        dingMessage.setSysIp(ip);
        dingMessage.setSysHostName(hostName);
        dingMessage.setModuleName(className);
        dingMessage.setMethodName(method);
        if (parms != null) {
            dingMessage.setParms(parms.toString());
        }
        if (retData != null) {
            dingMessage.setRetData(retData.toString());
        }
        dingMessage.setMessage(msg.toString());
        message.setMessageContent(JSON.toJSONString(dingMessage));
        message.setTitle("钉钉消息");
        return message;
    }

    /**
     * 组织邮件数据
     *
     * @param messageType
     * @param title
     * @param msg
     * @param addresseeList
     * @return
     */
    public static Message getMailMessage(String title, Object msg, List<String> addresseeList) {
        if (msg == null) {
            return null;
        }
        Message message = new Message();
        message.setMessageType(MessageType.MAILMESSAGE);
        MailMessage mailMessage = new MailMessage();
        mailMessage.setAddresseeList(addresseeList);
        mailMessage.setContent(msg.toString());
        mailMessage.setMailContentType(MailContentType.MAIL_TEXT);
        mailMessage.setTitle(title);
        message.setMessageContent(JSON.toJSONString(mailMessage));
        message.setTitle("邮件信息消息");
        return message;
    }
}
