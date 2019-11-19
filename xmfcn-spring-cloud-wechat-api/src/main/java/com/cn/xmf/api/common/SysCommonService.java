package com.cn.xmf.api.common;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.sys.rpc.DictService;
import com.cn.xmf.api.sys.rpc.KafKaProducerService;
import com.cn.xmf.api.sys.rpc.MessageService;
import com.cn.xmf.api.sys.rpc.RedisService;
import com.cn.xmf.base.Interface.SysCommon;
import com.cn.xmf.enums.MessageType;
import com.cn.xmf.model.msg.Message;
import com.cn.xmf.util.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author rufei.cn
 * <p>公共处理方法模块 $DESCRIPTION</p>
 */
@SuppressWarnings("all")
@Service
public class SysCommonService implements SysCommon {

    private static ThreadPoolExecutor cachedThreadPool = ThreadPoolUtil.getCommonThreadPool();//获取公共线程池
    private static Logger logger = LoggerFactory.getLogger(SysCommonService.class);

    @Autowired
    private RedisService redisService;
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
            dictValue = getCache(key);
            if (StringUtil.isNotBlank(dictValue)) {
                dictValue = dictValue.replace("@0", "");
                return dictValue;
            }
            dictValue = dictService.getDictValue(dictType, dictKey);
            if (StringUtil.isBlank(dictValue)) {
                save(key, "@0", 60);
            } else {
                save(key, dictValue, 60);
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
    public long getLock(String key, String requestId, long expireTime) {
        long lock = -1;
        if (StringUtil.isBlank(key)) {
            return lock;
        }
        try {
            Long rLock = redisService.getLock(key, requestId, expireTime);
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
    public long unRedisLock(String key, String requestId) {
        long lock = -1;
        if (StringUtil.isBlank(key)) {
            return lock;
        }
        try {
            lock = redisService.unRedisLock(key, requestId);
        } catch (Exception e) {
            logger.error("getLock（获取分布式锁） 异常={}", StringUtil.getExceptionMsg(e));
        }
        return lock;
    }

    /**
     * 内容审核
     *
     * @param conten
     * @return
     */
    public boolean checkContent(String content, String openId) {
        boolean retData = false;
        if (StringUtil.isBlank(content)) {
            return retData;
        }
        String accessToken = getAccessToken();
        if (StringUtil.isBlank(accessToken)) {
            return retData;
        }
        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken;
        JSONObject param = new JSONObject();
        param.put("content", content);
        JSONObject jsonObject = HttpClientUtil.HttpPost(param, url);
        if (jsonObject == null || jsonObject.size() <= 0) {
            return retData;
        }
        int errcode = jsonObject.getIntValue("errcode");
        String errMsg = jsonObject.getString("errMsg");
        if (errcode == 0) {
            retData = true;
        } else {
            logger.error("内容含有违法违规内容：", errMsg);
            this.sendDingTalkMessage("checkContent", content, jsonObject, errMsg, this.getClass());
        }
        return retData;
    }

    /**
     * 获取用户AccessToken
     *
     * @param code
     * @return
     */
    private String getAccessToken() {
        logger.info("获取用户OpenID和sessionKey信息 开始");
        String key = ConstantUtil.CACHE_SYS_BASE_DATA_ + "access_token";
        String accessToken = null;
        accessToken = this.getCache(key);
        if (StringUtil.isNotBlank(accessToken)) {
            return accessToken;
        }
        String configStr = this.getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "wechat_config");
        JSONObject configJosn = JSONObject.parseObject(configStr);
        if (configJosn == null) {
            return accessToken;
        }
        String appid = configJosn.getString("appId");
        String secret = configJosn.getString("secret");
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret;
        JSONObject jsonObject = HttpClientUtil.httpGet(url);
        int expiresIn = 0;
        if (jsonObject != null) {
            accessToken = jsonObject.getString("access_token");
            expiresIn = jsonObject.getIntValue("expires_in");
        }
        if (StringUtil.isNotBlank(accessToken)) {
            this.save(key, accessToken, expiresIn);
        }
        logger.info("获取用户OpenID和sessionKey信息 结束 jsonObject={}", jsonObject);
        return accessToken;
    }
}
