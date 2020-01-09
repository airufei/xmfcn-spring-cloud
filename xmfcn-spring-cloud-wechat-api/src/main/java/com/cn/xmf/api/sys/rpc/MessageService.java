package com.cn.xmf.api.sys.rpc;


import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.msg.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * MessageService(综合消息发送服务)
 * @author rufei.cn
 * @version 2017-12-21
 */
@SuppressWarnings("all")

@FeignClient(value = "${base-service.sys-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/message/")// 配置远程服务路径
public interface MessageService {
    /**
     * sendMessage（消息发送（包括邮件、短信、钉钉等）
     *
     * @param message
     * @return
     */
    @RequestMapping(value = "sendMessage", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RetData sendMessage(@RequestBody Message message);
}
