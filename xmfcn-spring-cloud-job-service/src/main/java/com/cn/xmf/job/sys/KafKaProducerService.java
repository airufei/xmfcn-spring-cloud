package com.cn.xmf.job.sys;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * kafka生产者
 *
 * @Author rufei.cn
 * @version 2019-05-09
 */
@FeignClient(value = "${base-service:kafka-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/kafka/")// 配置远程服务路径
@SuppressWarnings("all")
public interface KafKaProducerService {

    /**
     * 发送kafka数据
     *
     * topic 主题 必填
     * key 数据key 选填
     * value 数据主体 必填
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "sendKafka", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean sendKafka(@RequestBody JSONObject jsonObject);
}
