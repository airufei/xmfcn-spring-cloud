package com.cn.xmf.model.ding;


import com.cn.xmf.enums.DingMessageType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@SuppressWarnings("all")
public class DingMessage implements Serializable {
    private String sysName;//系统名称
    private String sysIp;//系统IP
    private String sysHostName;//系统主机名
    private String moduleName;//模块名称
    private String methodName;//方法名称
    private String parms;//入参
    private String retData;//出参
    private String message;//异常信息
    private String time;//发生时间
    private String webhook;//钉钉接口地址
    private DingMessageType dingMessageType;//消息格式类型
    private String exceptionMessage;

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysIp() {
        return sysIp;
    }

    public void setSysIp(String sysIp) {
        this.sysIp = sysIp;
    }

    public String getSysHostName() {
        return sysHostName;
    }

    public void setSysHostName(String sysHostName) {
        this.sysHostName = sysHostName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParms() {
        return parms;
    }

    public void setParms(String parms) {
        this.parms = parms;
    }

    public String getRetData() {
        return retData;
    }

    public void setRetData(String retData) {
        this.retData = retData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public DingMessageType getDingMessageType() {
        return dingMessageType;
    }

    public void setDingMessageType(DingMessageType dingMessageType) {
        this.dingMessageType = dingMessageType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("sysName", sysName)
                .append("sysIp", sysIp)
                .append("sysHostName", sysHostName)
                .append("moduleName", moduleName)
                .append("methodName", methodName)
                .append("parms", parms)
                .append("retData", retData)
                .append("message", message)
                .append("time", time)
                .append("webhook", webhook)
                .append("dingMessageType", dingMessageType)
                .toString();
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}

