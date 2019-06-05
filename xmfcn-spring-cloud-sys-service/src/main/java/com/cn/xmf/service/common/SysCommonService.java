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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 公共处理方法模块
 *
 * @author rufei.cn
 */
@Service
@SuppressWarnings("all")
public class SysCommonService {

    private static Logger logger = LoggerFactory.getLogger(SysCommonService.class);
    private static ExecutorService cachedThreadPool = Executors.newFixedThreadPool(200);//线程池
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return dictValue;
    }
}
