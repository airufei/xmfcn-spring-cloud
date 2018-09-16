/*
package com.cn.xmf.service.sys;


import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.ding.DingMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


*/
/**
 * Service(钉钉提醒)
 * @author airufei
 * @version 2017-12-21
 *//*

@SuppressWarnings("all")

@FeignClient(value = "${base-service.sys-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/dingTalk/")// 配置远程服务路径
public interface DingTalkService {

    */
/**
     * sendTextMessageToDingTalk(发送文本信息到钉钉群)
     * @Author airufei
     * String message,String webhook
     * @return
     *//*

    @RequestMapping(value = "sendTextMessageToDingTalk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendTextMessageToDingTalk(@RequestBody JSONObject parms);

    */
/**
     * sendMessageToDingTalk(发送不定格式信息到钉钉群)
     * @param parms
     * @return
     *//*

    @RequestMapping(value = "sendMessageToDingTalk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendMessageToDingTalk(@RequestBody DingMessage dingMessage);
}
*/
