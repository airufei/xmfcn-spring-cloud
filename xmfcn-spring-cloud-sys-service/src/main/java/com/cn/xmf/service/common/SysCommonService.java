package com.cn.xmf.service.common;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.enums.DingMessageType;
import com.cn.xmf.model.ding.DingMessage;
import com.cn.xmf.service.dict.service.DictService;
import com.cn.xmf.service.dingtalk.DingTalkService;
import com.cn.xmf.service.redis.RedisService;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.LocalCacheUtil;
import com.cn.xmf.util.StringUtil;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 公共处理方法模块
 *
 * @author rufei.cn
 */
@Service
@SuppressWarnings("all")
public class SysCommonService {

    /**
     * int corePoolSize, 指定了线程池中的线程数量，它的数量决定了添加的任务是开辟新的线程去执行，还是放到workQueue任务队列中去；
     * int maximumPoolSize, 指定了线程池中的最大线程数量，这个参数会根据你使用的workQueue任务队列的类型，决定线程池会开辟的最大线程数量；
     * long keepAliveTime, 当线程池中空闲线程数量超过corePoolSize时，多余的线程会在多长时间内被销毁；
     * TimeUnit unit, keepAliveTime的单位
     * BlockingQueue<Runnable> workQueue 任务队列，被添加到线程池中，但尚未被执行的任务；它一般分为直接提交队列、有界任务队列、无界任务队列、优先任务队列几种；
     */
    private static ThreadPoolExecutor cachedThreadPool = new ThreadPoolExecutor(50, 100, 3000, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50));
    private static Logger logger = LoggerFactory.getLogger(SysCommonService.class);

    @Autowired
    private DingTalkService dingTalkService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private Environment environment;
    @Autowired
    private DictService dictService;


    /**
     * 获取当前运行的系统名称
     *
     * @return
     */
    public String getSysName() {
        return environment.getProperty("spring.application.name");
    }

    /**
     * setDingMessage(组织钉钉消息)
     *
     * @param method
     * @param parms
     * @return
     */
    public void sendDingMessage(String method, String parms, String retData, String msg, Class t) {
        try {
            DingMessage dingMessage = new DingMessage();
            dingMessage.setDingMessageType(DingMessageType.MARKDWON);
            dingMessage.setSysName(getSysName());
            dingMessage.setModuleName(t.getPackage().toString());
            dingMessage.setMethodName(method);
            dingMessage.setParms(parms);
            dingMessage.setExceptionMessage(msg);
            dingMessage.setRetData(retData);
            dingTalkService.sendMessageToDingTalk(dingMessage);
        } catch (Exception e) {

        }
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
            redisService.saveCache(key, value, seconds);
        } catch (Exception e) {
            logger.error("save_error:" + StringUtil.getExceptionMsg(e));

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
            logger.error("getCache_error:" + StringUtil.getExceptionMsg(e));

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
            logger.error("delete_error:" + StringUtil.getExceptionMsg(e));

        }
        return result;
    }


    /**
     * getLock（获取分布式锁）
     *
     * @param key
     * @return
     * @author airuei
     */
    public RLock getLock(String key) {
        RLock lock = null;
        if (StringUtil.isBlank(key)) {
            return lock;
        }
        try {
            //lock = redisService.getLock(key);
        } catch (Exception e) {
            logger.error("getLock（获取分布式锁）:" + StringUtil.getExceptionMsg(e));

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
            logger.error("getRedisInfo（redis 运行健康信息):" + StringUtil.getExceptionMsg(e));

        }
        return result;
    }

    /**
     * 获取字典数据
     *
     * @param dictType
     * @param dictKey
     * @return
     */
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
                LocalCacheUtil.saveCache(key, "@0");
            } else {
                LocalCacheUtil.saveCache(key, dictValue);
            }
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));

        }
        return dictValue;
    }
}
