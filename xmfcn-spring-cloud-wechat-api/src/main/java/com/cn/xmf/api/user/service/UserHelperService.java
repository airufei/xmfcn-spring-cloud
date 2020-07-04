package com.cn.xmf.api.user.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.comment.service.CommentHelperService;
import com.cn.xmf.api.common.SysCommonService;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserHelperService {
    private static Logger logger = LoggerFactory.getLogger(UserHelperService.class);
    @Autowired
    private SysCommonService sysCommonService;

    /**
     * 获取用户OpenID和sessionKey信息
     *
     * @param code
     * @return
     */
    public JSONObject getUserData(String code) {
        logger.info("获取用户OpenID和sessionKey信息 开始 code={}", code);
        JSONObject jsonObject = new JSONObject();
        String configStr = sysCommonService.getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "wechat_config");
        JSONObject configJosn = JSONObject.parseObject(configStr);
        if (configJosn == null) {
            return jsonObject;
        }
        String appid = configJosn.getString("appId");
        String secret = configJosn.getString("secret");
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        logger.info("获取用户OpenID和sessionKey信息 url={}", url);
        JSONObject mp=new  JSONObject();
        jsonObject = HttpClientUtil.HttpsPost(url,mp);
        logger.info("获取用户OpenID和sessionKey信息 结束 jsonObject={}", jsonObject);
        return jsonObject;
    }
}
