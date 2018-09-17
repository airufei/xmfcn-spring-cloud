/*
package com.cn.xmf.service.common;

import com.cn.xmf.enums.DingMessageType;
import com.cn.xmf.model.ding.DingMessage;
//import com.cn.xmf.service.sys.DingTalkService;
//import com.cn.xmf.service.sys.RedisService;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

*/
/**
 * @author airufei
 * <p>公共处理方法模块 $DESCRIPTION</p>
 *//*

@Service
@SuppressWarnings("all")
public class SysCommonService {
    private static Logger logger = LoggerFactory.getLogger(SysCommonService.class);

    //@Autowired
   // private DingTalkService dingTalkService;
    //@Autowired
    //private RedisService redisService;
    @Autowired
    private Environment environment;

    */
/**
     * 获取当前运行的系统名称
     *
     * @return
     *//*

    public String getSysName() {
        return environment.getProperty("spring.application.name");
    }

    */
/**
     * setDingMessage(组织钉钉消息)
     *
     * @param method
     * @param parms
     * @return
     *//*

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
            //dingTalkService.sendMessageToDingTalk(dingMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    */
/**
     * saveCache(保存redis)
     *
     * @param key        缓存key
     * @param value      缓存值
     * @param expireTime 缓时间（秒）
     * @author airuei
     *//*

    public void saveCache(String key, String value, int expireTime) {
        try {
            if (StringUtil.isBlank(key)) {
                return;
            }
            //redisService.saveCache(key, value, expireTime);
        } catch (Exception e) {
            logger.error("saveRedis_error:" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
    }

    */
/**
     * getCache（获取Redis缓存数据）
     *
     * @param key
     * @return
     * @author airuei
     *//*

    public String getCache(String key) {
        String cache = null;
        if (StringUtil.isBlank(key)) {
            return cache;
        }
        try {
            //cache = redisService.getCache(key);
        } catch (Exception e) {
            logger.error("getCache（获取Redis缓存数据）:" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        return cache;
    }

    */
/**
     * delCache(删除redis缓存)
     *
     * @param key
     * @return
     * @author airuei
     *//*

    public long delCache(String key) {
        logger.info("delCache(删除redis缓存) 开始：" + key);
        long num = -1;
        if (StringUtil.isBlank(key)) {
            return num;
        }
        try {
            Long ret = null;//redisService.delCache(key);
            if (ret != null) {
                num = ret;
            }
        } catch (Exception e) {
            logger.error("delCache(删除redis缓存):" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        logger.info("delCache(删除redis缓存) 结束：" + num);
        return num;
    }

    */
/**
     * getQueueLength（获取队列长度)key 是消息频道
     *
     * @param key
     * @return
     *//*

    public long getQueueLength(String key) {
        logger.info("getQueueLength（获取队列长度) 开始 " + key);
        long result = -1;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        boolean broken = false;
        try {
            Long queueLength =null;// redisService.getQueueLength(key);
            if (queueLength != null) {
                result = queueLength;
            }
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        logger.info("getQueueLength（获取队列长度)redis 结束 " + result);
        return result;
    }

    */
/**
     * delCaches(根据通配符删除所有key)
     *
     * @param key
     * @return
     * @author airuei
     *//*

    public long delCaches(String pattern) {
        long num = -1;
        if (StringUtil.isBlank(pattern)) {
            return num;
        }
        try {
            Long ret =null;// redisService.delCaches(pattern);
            if (ret != null) {
                num = ret;
            }
        } catch (Exception e) {
            logger.error("delCaches(根据通配符删除所有key):" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        return num;
    }

    */
/**
     * getLock（获取分布式锁）
     *
     * @param key
     * @return
     * @author airuei
     *//*

    public long getLock(String key) {
        long lock = -1;
        if (StringUtil.isBlank(key)) {
            return lock;
        }
        try {
            Long aLong = null;// redisService.getLock(key);
            if (aLong != null) {
                lock = aLong;
            }
        } catch (Exception e) {
            logger.error("getLock（获取分布式锁）:" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        return lock;
    }

    */
/**
     * getQueue（获取Redis队列数据）
     *
     * @param key
     * @return
     * @author airuei
     *//*

    public String getQueue(String key) {
        String queue = null;
        if (StringUtil.isBlank(key)) {
            return queue;
        }
        try {
            queue =null;//  redisService.getFromQueue(key);
        } catch (Exception e) {
            logger.error("getQueue（获取Redis对列数据）:" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        return queue;
    }

    */
/**
     * saveQueue（保存Redis队列数据）
     *
     * @param key
     * @return
     * @author airuei
     *//*

    public long saveQueue(String key, String value) {
        long queueCount = -1;
        if (StringUtil.isBlank(key)) {
            return queueCount;
        }
        if (StringUtil.isBlank(value)) {
            return queueCount;
        }
        try {
            Long aLong =null;//  redisService.putToQueue(key, value);
            if (aLong != null) {
                queueCount = aLong;
            }
        } catch (Exception e) {
            logger.error("saveQueue（保存Redis队列数据）:" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        return queueCount;
    }

    */
/**
     * getOnlyNo(米族产生24位唯一单号)
     *
     * @param type
     * @return
     *//*

    public String getOnlyNo(String prefix) {
        String onlyNo = null;
        logger.info("getOnlyNo(获取唯一编号) 开始：" + prefix);
        if (StringUtil.isBlank(prefix)) {
            return onlyNo;
        }
        // 前缀必须为四位字符
        try {
            onlyNo = null;// redisService.getOnlyNo(prefix);

        } catch (Exception e) {
            logger.error("getOnlyNo(获取唯一编号):" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        if (StringUtil.isBlank(onlyNo))//如果获取单号失败，则自动生成一个
        {
            String s = StringUtil.randomCodeUtil();//20位
            onlyNo = prefix + s;
        }
        logger.info("getOnlyNo(获取唯一编号) 结束 onlyNo ：" + onlyNo);
        return onlyNo;
    }
}
*/
