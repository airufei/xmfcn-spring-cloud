package com.cn.xmf.base.Interface;

import com.cn.xmf.enums.MessageType;

import java.util.List;

public interface SysCommon {
    /**
     * sendDingTalkMessage(发送钉钉消息)
     *
     * @param method  发送消息的方法
     * @param parms   方法参数
     * @param retData 方法返回值
     * @param msg     消息内容
     * @param t       类信息
     */
    void sendDingTalkMessage(String method, Object parms, Object retData, Object msg, Class t);

    /**
     * sendMessage（综合消息发送）
     *
     * @param messageType 消息类型
     * @param title       标题
     * @param content     内容
     * @param list        收件人
     */
    void sendMessage(MessageType messageType, String title, Object content, List<String> list);

    /**
     * 获取字典数据
     *
     * @param dictType
     * @param dictKey
     * @return
     */
    String getDictValue(String dictType, String dictKey);

    /**
     * sendKafka（发送数据到kafka）
     *
     * @param topic
     * @param key
     * @param value
     * @return
     */
    boolean sendKafka(String topic, String key, Object value);
}
