package com.cn.xmf.service.dingtalk;

import com.cn.xmf.common.model.common.DingMessage;
import com.cn.xmf.common.model.common.MarkdownMessage;
import com.cn.xmf.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all")
public class DingTalkHelperService {

    private static Logger logger = LoggerFactory.getLogger(DingTalkHelperService.class);

    public String setTextMessage(DingMessage dingMessage) {
        MarkdownMessage message = new MarkdownMessage();
        message.setTitle("小蜜蜂消息提醒");
        String sysName = dingMessage.getSysName();
        String moduleName = dingMessage.getModuleName();
        String methodName = dingMessage.getMethodName();
        String parms = dingMessage.getParms();
        String retData = dingMessage.getRetData();
        String exceptionMessage = dingMessage.getExceptionMessage();
        String time = dingMessage.getTime();
        if(StringUtil.isBlank(time))
        {
           // time= DateUtil.getNowDateTime();
        }
        if(StringUtil.isBlank(sysName))
        {
            sysName="小蜜蜂微服务系统";
        }
        if(StringUtil.isBlank(moduleName))
        {
            moduleName="未知模块";
        }
        if(StringUtil.isBlank(methodName))
        {
            methodName="未知方法/接口";
        }
        if(StringUtil.isBlank(parms))
        {
            parms="无入参";
        }
        if(StringUtil.isBlank(retData))
        {
            retData="无出参";
        }
        if(StringUtil.isBlank(exceptionMessage))
        {
            exceptionMessage="未知异常";
        }
        String msg="系统名称："+sysName+"\n 模块名称："+moduleName+"\n 方法名称："+methodName
                +"\n 入参："+parms+"\n 出参："+retData+"\n 消息内容："+exceptionMessage+"\n 时间："+time;
        return  msg;
    }
    public MarkdownMessage setMarkdownMessage(DingMessage dingMessage) {
        MarkdownMessage message = new MarkdownMessage();
        message.setTitle("小蜜蜂消息提醒");
        String sysName = dingMessage.getSysName();
        String moduleName = dingMessage.getModuleName();
        String methodName = dingMessage.getMethodName();
        String parms = dingMessage.getParms();
        String retData = dingMessage.getRetData();
        String exceptionMessage = dingMessage.getExceptionMessage();
        String time = dingMessage.getTime();
        if(StringUtil.isBlank(time))
        {
            //time= DateUtil.getNowDateTime();
        }
        if(StringUtil.isBlank(sysName))
        {
            sysName="小蜜蜂微服务系统";
        }
        if(StringUtil.isBlank(moduleName))
        {
            moduleName="未知模块";
        }
        if(StringUtil.isBlank(methodName))
        {
            methodName="未知方法/接口";
        }
        if(StringUtil.isBlank(parms))
        {
            parms="无入参";
        }
        if(StringUtil.isBlank(retData))
        {
            retData="无出参";
        }
        if(StringUtil.isBlank(exceptionMessage))
        {
            exceptionMessage="未知异常";
        }
        message.add(MarkdownMessage.getHeaderText(3, "系统："));
        message.add(sysName);
        message.add("\n\n");
        message.add(MarkdownMessage.getHeaderText(3, "模块："));
        message.add(moduleName);
        message.add("\n\n");
        message.add(MarkdownMessage.getHeaderText(3, "方法："));
        message.add(methodName);
        message.add("\n\n");
        message.add(MarkdownMessage.getHeaderText(3, "入参："));
        message.add(parms);
        message.add("\n\n");
        message.add(MarkdownMessage.getHeaderText(3, "出参："));
        message.add(retData);
        message.add("\n\n");
        message.add(MarkdownMessage.getHeaderText(3, "时间："));
        message.add(time);
        message.add("\n\n");
        message.add(MarkdownMessage.getHeaderText(3, "消息内容："));
        message.add(exceptionMessage);
        message.add("\n\n");
        return  message;
    }
}
