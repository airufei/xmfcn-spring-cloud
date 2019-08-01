package com.cn.xmf.service.common;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.Interface.SysCommon;
import com.cn.xmf.model.ding.DingMessage;
import com.cn.xmf.service.sys.DictService;
import com.cn.xmf.service.sys.DingTalkService;
import com.cn.xmf.service.sys.KafKaProducerService;
import com.cn.xmf.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 公共处理方法模块
 *
 * @author rufei.cn
 */
@Service
@SuppressWarnings("all")
public class SysCommonService implements SysCommon {

    private static ThreadPoolExecutor cachedThreadPool = ThreadPoolUtil.getCommonThreadPool();//获取公共线程池
    private static Logger logger = LoggerFactory.getLogger(SysCommonService.class);

    @Autowired
    private DingTalkService dingTalkService;
    @Autowired
    private Environment environment;
    @Autowired
    private DictService dictService;
    @Autowired
    private KafKaProducerService kafKaProducerService;

    /**
     * setDingMessage(组织钉钉消息)
     *
     * @param method
     * @param parms
     * @return
     */
    @Override
    public void sendDingMessage(String method, Object parms, Object retData, Object msg, Class t) {
        try {
            if (msg == null) {
                return;
            }
            String currentThreadClass = t.getSimpleName();
            String subSysName = StringUtil.getSubSysName();
            DingMessage dingMessage = MessageUtil.getDingTalkMessage(parms, retData, msg, subSysName, currentThreadClass, method);
            if (dingMessage == null) {
                return;
            }
            String classMethod = this.getClass().getName() + ".sendDingMessage()";
            ThreadPoolUtil.getThreadPoolIsNext(cachedThreadPool, classMethod);
            cachedThreadPool.execute(() -> {
                dingTalkService.sendMessageToDingTalk(dingMessage);
            });
        } catch (Exception e) {
            logger.error("setDingMessage(发送钉钉消息) 异常={}", StringUtil.getExceptionMsg(e));
        }
    }

    /**
     * sendKafka（发送数据到kafka）
     *
     * @param topic
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean sendKafka(String topic, String key, Object value) {
        boolean result = false;
        if (StringUtil.isBlank(topic)) {
            logger.info("topic不能为空");
        }
        if (value == null) {
            logger.info("value不能为空");
        }
        if (kafKaProducerService == null) {
            return result;
        }
        JSONObject sendJson = new JSONObject();
        sendJson.put("topic", topic);
        sendJson.put("key", key);
        sendJson.put("value", value);
        try {
            result = kafKaProducerService.sendKafka(sendJson);
        } catch (Exception e) {
            logger.error("sendKafka（发送数据到kafka）异常={}:", StringUtil.getExceptionMsg(e));
        }
        return result;
    }


    /**
     * getDictValue(获取字典数据)
     *
     * @param dictType
     * @param dictKey
     * @return
     */
    @Override
    public String getDictValue(String dictType, String dictKey) {
        String dictValue = null;
        String key = ConstantUtil.CACHE_SYS_BASE_DATA_ + dictType + dictKey;
        try {
            dictValue = LocalCacheUtil.getCache(key);
            if (StringUtil.isNotBlank(dictValue)) {
                dictValue = dictValue.replace("@0", "");
                return dictValue;
            }
            dictValue = dictService.getDictValue(dictType, dictKey);
            if (StringUtil.isBlank(dictValue)) {
                LocalCacheUtil.saveCache(key, "@0", 60);
            } else {
                LocalCacheUtil.saveCache(key, dictValue, 60);
            }
        } catch (Exception e) {
            logger.error("getDictValue(获取字典数据) 异常={}", StringUtil.getExceptionMsg(e));
        }
        return dictValue;
    }
}
