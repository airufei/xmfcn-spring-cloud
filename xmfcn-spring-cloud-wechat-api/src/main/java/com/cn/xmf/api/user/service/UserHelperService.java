package com.cn.xmf.api.user.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserHelperService {
    private static Logger logger = LoggerFactory.getLogger(UserHelperService.class);

    /**
     * 获取用户OpenID和sessionKey信息
     *
     * @param code
     * @return
     */
    public JSONObject getUserData(String code) {
        logger.info("获取用户OpenID和sessionKey信息 开始 code={}", code);
        JSONObject jsonObject = new JSONObject();
        String appid = "wxf25e90199003dbdb";
        String secret = "b32dc988ea15473d30234708ec801772";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        jsonObject = HttpClientUtil.httpGet(url);
        logger.info("获取用户OpenID和sessionKey信息 结束 jsonObject={}", jsonObject);
        return jsonObject;
    }
}
