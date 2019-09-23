package com.cn.xmf.job.common;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.Interface.SysCommon;
import com.cn.xmf.enums.MessageType;
import com.cn.xmf.job.sys.DictService;
import com.cn.xmf.job.sys.KafKaProducerService;
import com.cn.xmf.job.sys.MessageService;
import com.cn.xmf.job.sys.RedisService;
import com.cn.xmf.model.msg.Message;
import com.cn.xmf.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author rufei.cn
 * <p>公共处理方法模块 $DESCRIPTION</p>
 */
@Service
@SuppressWarnings("all")
public class SysCommonService implements SysCommon {

    private static ThreadPoolExecutor cachedThreadPool = ThreadPoolUtil.getCommonThreadPool();//获取公共线程池
    private static Logger logger = LoggerFactory.getLogger(SysCommonService.class);

    @Autowired
    private RedisService redisService;
    @Autowired
    private Environment environment;
    @Autowired
    private DictService dictService;
    @Autowired
    private KafKaProducerService kafKaProducerService;
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

    /**
     * save(保持缓存)
     *
     * @param key
     * @return
     */
    public void save(String key, String value, int seconds) {
        try {
            if (StringUtil.isBlank(key)) {
                return;
            }
            redisService.save(key, value, seconds);
        } catch (Exception e) {
            logger.error("save(保持缓存) 异常={}", StringUtil.getExceptionMsg(e));
        }
    }

    /**
     * getCache(获取缓存)
     *
     * @param key
     * @return
     */
    public String getCache(String key) {
        String cache = null;
        if (StringUtil.isBlank(key)) {
            return null;
        }
        try {
            redisService.getCache(key);
        } catch (Exception e) {
            logger.error("getCache(获取缓存) 异常={}", StringUtil.getExceptionMsg(e));
        }
        return cache;
    }

    /**
     * delete(删除缓存)
     *
     * @param key
     * @return
     */
    public long delete(String key) {
        long result = -1;
        try {
            if (StringUtil.isBlank(key)) {
                return result;
            }
            result = redisService.delete(key);
        } catch (Exception e) {
            logger.error("delete(删除缓存) 异常={}", StringUtil.getExceptionMsg(e));
        }
        return result;
    }


    /**
     * getLock（获取分布式锁-暂不可用）
     *
     * @param key
     * @return
     * @author airuei
     */
    public long getLock(String key, String requestId,long expireTime) {
        long lock = -1;
        if (StringUtil.isBlank(key)) {
            return lock;
        }
        try {
            Long rLock = redisService.getLock(key,requestId, expireTime);
            if (rLock != null) {
                lock = rLock;
            }
        } catch (Exception e) {
            logger.error("getLock（获取分布式锁） 异常={}", StringUtil.getExceptionMsg(e));
        }
        return lock;
    }

    /**
     * unRedisLock（释放分布式锁）
     *
     * @param key 锁
     * @return 是否释放成功
     */
    public long unRedisLock(String key,String requestId) {
        long lock = -1;
        if (StringUtil.isBlank(key)) {
            return lock;
        }
        try {
            lock = redisService.unRedisLock(key,requestId);
        } catch (Exception e) {
            logger.error("getLock（获取分布式锁） 异常={}", StringUtil.getExceptionMsg(e));
        }
        return lock;
    }

    /**
     * getRedisInfo（redis 运行健康信息)
     *
     * @param key
     * @return
     */
    public JSONObject getRedisInfo() {
        JSONObject result = null;
        try {
            result = redisService.getRedisInfo();
        } catch (Exception e) {
            logger.error("getRedisInfo（redis 运行健康信息) 异常={}", StringUtil.getExceptionMsg(e));
        }
        return result;
    }
}
