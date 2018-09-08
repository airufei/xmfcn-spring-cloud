package com.cn.xmf.common.model.common;


import com.cn.xmf.common.enums.DingMessageType;

import java.io.Serializable;

public class DingMessage implements Serializable {
    private String sysName;//系统名称
    private String moduleName;//模块名称
    private String methodName;//方法名称
    private String parms;//入参
    private String retData;//出参
    private String exceptionMessage;//异常信息
    private String time;//发生时间
    private String webhook;//钉钉接口地址
    private DingMessageType dingMessageType;//消息格式类型

    public DingMessageType getDingMessageType() {
        return dingMessageType;
    }

    public void setDingMessageType(DingMessageType dingMessageType) {
        this.dingMessageType = dingMessageType;
    }
    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }



    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
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

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "DingMessage{" +
                "sysName='" + sysName + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parms='" + parms + '\'' +
                ", retData='" + retData + '\'' +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                ", time='" + time + '\'' +
                ", webhook='" + webhook + '\'' +
                ", dingMessageType=" + dingMessageType +
                '}';
    }
}

