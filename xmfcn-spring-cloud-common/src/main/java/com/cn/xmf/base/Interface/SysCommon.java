package com.cn.xmf.base.Interface;

public interface SysCommon {
    /**
     * setDingMessage(钉钉消息发送)
     *
     * @param method
     * @param parms
     * @return
     */
    void sendDingMessage(String method, Object parms, Object retData, Object msg, Class t);

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
