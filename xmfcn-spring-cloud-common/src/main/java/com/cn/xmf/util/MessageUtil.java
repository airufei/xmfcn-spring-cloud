package com.cn.xmf.util;

import com.cn.xmf.enums.DingMessageType;
import com.cn.xmf.model.ding.DingMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
    public static DingMessage getDingTalkMessage(Object parms, Object retData, Object msg, String sysName, String className, String method) {
        if (msg == null) {
            return null;
        }
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
        return dingMessage;
    }
}
