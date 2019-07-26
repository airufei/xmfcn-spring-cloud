package com.cn.xmf.service.common;

import com.cn.xmf.enums.DingMessageType;
import com.cn.xmf.model.ding.DingMessage;
import com.cn.xmf.service.sys.DictService;
import com.cn.xmf.service.sys.DingTalkService;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.LocalCacheUtil;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * 公共处理方法模块
 *
 * @author rufei.cn
 */
@Service
@SuppressWarnings("all")
public class SysCommonService {

    private static Logger logger = LoggerFactory.getLogger(SysCommonService.class);

    @Autowired
    private DingTalkService dingTalkService;
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
            logger.error("save_error:" + StringUtil.getExceptionMsg(e));
        }
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
