package com.cn.xmf.model.sys;

import com.cn.xmf.util.DateUtil;
import com.cn.xmf.util.StringUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@SuppressWarnings("all")
public class LogMessage implements Serializable {

    private String id = StringUtil.getUuId();//日志ID
    private String sysName = "rufei.cn";//系统名称
    private String sysIp;//执行机器IP
    private String subSysName;//子系统系统名称 sys-service /base-zuul等
    private String moduleName;//模块名称
    private String methodName;//方法名称
    private String parms = "无入参";//入参
    private String retData = "无出参";//出参
    private String message;//信息
    private String level;//日志级别
    private String time = DateUtil.getTimeNow();//发生时间
    private String stackMessage = "";//堆栈信息 ERRROR级别才会有
    private String threadName;//线程名称
    private String traceId;//上下文追踪ID

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

    public String getSysIp() {
        return sysIp;
    }

    public void setSysIp(String sysIp) {
        this.sysIp = sysIp;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("sysName", sysName)
                .append("sysIp", sysIp)
                .append("subSysName", subSysName)
                .append("moduleName", moduleName)
                .append("methodName", methodName)
                .append("parms", parms)
                .append("retData", retData)
                .append("message", message)
                .append("level", level)
                .append("time", time)
                .append("stackMessage", stackMessage)
                .append("threadName", threadName)
                .append("traceId", traceId)
                .toString();
    }
}
