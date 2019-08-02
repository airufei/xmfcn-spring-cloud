package com.cn.xmf.service.common;

import com.cn.xmf.base.Interface.SysCommon;
import com.cn.xmf.enums.MessageType;
import com.cn.xmf.model.msg.Message;
import com.cn.xmf.service.sys.DictService;
import com.cn.xmf.service.sys.MessageService;
import com.cn.xmf.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private Environment environment;
    @Autowired
    private DictService dictService;

    @Autowired
    private MessageService messageService;

    /**
     * sendDingTalkMessage(综合消息发送)
     *
     * @param messageType 消息类型
     * @param method      发送消息的方法
     * @param parms       方法参数
     * @param retData     方法返回值
     * @param msg         消息内容
     * @param t           类信息
     */
    @Override
    public void sendDingTalkMessage(String method, Object parms, Object retData, Object msg, Class t) {
        try {
            if (msg == null) {
                return;
            }
            String currentThreadClass = t.getSimpleName();
            String subSysName = StringUtil.getSubSysName();
            Message message = MessageUtil.getDingTalkMessage(parms, retData, msg, subSysName, currentThreadClass, method);
            if (message == null) {
                return;
            }
            String classMethod = this.getClass().getName() + ".sendDingTalkMessage()";
            ThreadPoolUtil.getThreadPoolIsNext(cachedThreadPool, classMethod);
            cachedThreadPool.execute(() -> {
                messageService.sendMessage(message);
            });
        } catch (Exception e) {
            logger.error("sendDingTalkMessage(发送钉钉消息) 异常={}", StringUtil.getExceptionMsg(e));
        }
    }

    /**
     * sendMessage（综合消息发送）
     *
     * @param messageType 消息类型
     * @param title       标题
     * @param content     内容
     * @param list        收件人
     */
    @Override
    public void sendMessage(MessageType messageType, String title, Object content, List<String> list) {
        try {
            if (content == null) {
                return;
            }
            Message message = MessageUtil.getMailMessage(title, content, list);
            if (message == null) {
                return;
            }
            message.setMessageType(messageType);
            String classMethod = this.getClass().getName() + ".sendMessage()";
            ThreadPoolUtil.getThreadPoolIsNext(cachedThreadPool, classMethod);
            cachedThreadPool.execute(() -> {
                messageService.sendMessage(message);
            });
        } catch (Exception e) {
            logger.error("sendMessage（综合消息发送） 异常={}", StringUtil.getExceptionMsg(e));
        }
    }


    /**
     * sendKafka（发送数据到kafka - 未实现功能）
     *
     * @param topic
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean sendKafka(String topic, String key, Object value) {
        boolean result = false;
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
