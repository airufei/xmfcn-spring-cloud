package com.cn.xmf.base.Interface;

public interface SysCommon {
    /**
     * setDingMessage(钉钉消息发送)
     *
     * @param method
     * @param parms
     * @return
     */
    public void sendDingMessage(String method, String parms, String retData, String msg, Class t);
}
