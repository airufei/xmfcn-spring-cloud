package com.cn.xmf.job.admin.common;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.enums.DingMessageType;
import com.cn.xmf.job.admin.sys.RedisService;
import com.cn.xmf.model.ding.DingMessage;
import com.cn.xmf.util.StringUtil;
import com.cn.xmf.job.admin.sys.DingTalkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author rufei.cn
 * <p>公共处理方法模块 $DESCRIPTION</p>
 */
@Service
@SuppressWarnings("all")
public class SysCommonService {

    private static Logger logger = LoggerFactory.getLogger(SysCommonService.class);
    @Autowired
    private DingTalkService dingTalkService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private Environment environment;

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
            redisService.save(key, value, seconds);
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
     * putToQueue(入队列)
     *
     * @param key
     * @return
     */
    public void putToQueue(String key, String value) {
        try {
            if (StringUtil.isBlank(key)) {
                return;
            }
            if (StringUtil.isBlank(value)) {
                return;
            }
            redisService.putToQueue(key, value);
        } catch (Exception e) {
            logger.error("putToQueue_error:" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
    }

    /**
     * getFromQueue(获取队列)
     *
     * @param key
     * @return
     */
    public String getFromQueue(String key) {
        String value = null;
        try {
            if (StringUtil.isBlank(key)) {
                return value;
            }
            value = redisService.getFromQueue(key);
        } catch (Exception e) {
            logger.error("putToQueue_error:" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        return value;
    }

    /**
     * getLock（获取分布式锁）
     *
     * @param key
     * @return
     * @author airuei
     */
    public long getLock(String key) {
        long lock = -1;
        if (StringUtil.isBlank(key)) {
            return lock;
        }
        try {
            Long aLong = redisService.getLock(key);
            if (aLong != null) {
                lock = aLong;
            }
        } catch (Exception e) {
            logger.error("getLock（获取分布式锁）:" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        return lock;
    }

    /**
     * getQueueLength（获取队列长度)key 是消息频道
     *
     * @param key
     * @return
     */
    public long getQueueLength(String key) {
        long lock = -1;
        if (StringUtil.isBlank(key)) {
            return lock;
        }
        try {
            Long aLong = redisService.getQueueLength(key);
            if (aLong != null) {
                lock = aLong;
            }
        } catch (Exception e) {
            logger.error("getQueueLength（获取队列长度):" + StringUtil.getExceptionMsg(e));
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
}
