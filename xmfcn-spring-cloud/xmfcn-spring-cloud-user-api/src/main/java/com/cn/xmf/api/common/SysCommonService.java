package com.cn.xmf.api.common;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.sys.DingTalkService;
import com.cn.xmf.enums.DingMessageType;
import com.cn.xmf.model.ding.DingMessage;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author airufei
 * <p>公共处理方法模块 $DESCRIPTION</p>
 */
@Service
public class SysCommonService {


    @Autowired
    private DingTalkService dingTalkService;
    private static Logger logger = LoggerFactory.getLogger(SysCommonService.class);
    @Autowired
    private Environment environment;

    /**
     * 获取当前运行的系统名称
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
     * saveSsdb(保存ssdb)
     *
     * @param key        缓存key
     * @param value      缓存值
     * @param expireTime 缓实际
     */
    public void saveSsdb(String key, String value, long expireTime) {
        if (StringUtil.isBlank(key)) {
            return;
        }
        if (StringUtil.isBlank(value)) {
            return;
        }
        JSONObject ssdbJsonObject = new JSONObject();
        ssdbJsonObject.put("key", key);
        ssdbJsonObject.put("value", value);
        ssdbJsonObject.put("expTime", expireTime);
        try {
            // ssdbService.setStrByKey(ssdbJsonObject);
        } catch (Exception e) {
            logger.error("saveSsdb_error:"+StringUtil.getExceptionInfo(e));
            e.printStackTrace();
        }
    }

    /**
     * saveSsdb(保存ssdb) 默认缓存10分钟
     *
     * @param key   缓存key
     * @param value 缓存值
     */
    public void saveSsdb(String key, String value) {
        if (StringUtil.isBlank(key)) {
            return;
        }
        if (StringUtil.isBlank(value)) {
            return;
        }
        JSONObject ssdbJsonObject = new JSONObject();
        ssdbJsonObject.put("key", key);
        ssdbJsonObject.put("value", value);
        ssdbJsonObject.put("expTime", 60 * 10);
        try {
            //ssdbService.setStrByKey(ssdbJsonObject);
        } catch (Exception e) {
            logger.error("saveSsdb_error:"+StringUtil.getExceptionInfo(e));
            e.printStackTrace();
        }
    }

    /**
     * getStr(获取缓存)
     * @param key
     * @return
     */
    public String getCache(String key) {
        String cache=null;
        if (StringUtil.isBlank(key)) {
            return null;
        }
        try {
            //cache=ssdbService.getStrByKey(key);
        } catch (Exception e) {
            logger.error("saveSsdb_error:"+StringUtil.getExceptionInfo(e));
            e.printStackTrace();
        }
        return cache;
    }

    /**
     * del(删除缓存)
     * @param key
     * @return
     */
    public void del(String key) {
        try {
            if (StringUtil.isBlank(key)) {
                return ;
            }
            //ssdbService.delStrByKey(key);
        } catch (Exception e) {
            logger.error("saveSsdb_error:"+StringUtil.getExceptionInfo(e));
            e.printStackTrace();
        }
    }
}
