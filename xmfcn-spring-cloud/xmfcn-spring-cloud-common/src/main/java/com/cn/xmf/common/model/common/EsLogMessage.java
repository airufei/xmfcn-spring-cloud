package com.cn.xmf.common.model.common;

import java.io.Serializable;
import java.util.Date;
public class EsLogMessage implements Serializable {

    private String id;
    private String sysName="xmf";//系统名称 xmf
    private String subSysName;//子系统系统名称
    private String moduleName;//模块名称
    private String methodName;//方法名称
    private String parms="无入参";//入参
    private String retData="无出参";//出参
    private String message;//信息
    private String level;//日志级别
    private String time=new Date().toString() ;//发生时间
    private String stackMessage="";//堆栈信息 ERRROR级别才会有


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStackMessage() {
        return stackMessage;
    }

    public void setStackMessage(String stackMessage) {
        this.stackMessage = stackMessage;
    }

    public String getSubSysName() {
        return subSysName;
    }

    public void setSubSysName(String subSysName) {
        this.subSysName = subSysName;
    }

    @Override
    public String toString() {
        return "EsLogMessage{" +
                "sysName='" + sysName + '\'' +
                ", subSysName='" + subSysName + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parms='" + parms + '\'' +
                ", retData='" + retData + '\'' +
                ", message='" + message + '\'' +
                ", level='" + level + '\'' +
                ", time='" + time + '\'' +
                ", stackMessage='" + stackMessage + '\'' +
                '}';
    }
}
