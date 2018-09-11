package com.cn.xmf.api.sys;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 日志信息
 */
@FeignClient(value = "${base-service.sys-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/log/")// 配置远程服务路径
@SuppressWarnings("all")
public interface EsLogService {


    /**
     * 发送日志信息
     *
     * @param parms
     * @return
     */
    @RequestMapping(value = "saveLog", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveLog(@RequestParam("parms") String parms, @RequestParam("index") String index, @RequestParam("type") String type);

    /**
     * getLogById (获取日志)
     *
     * @param interfaceInterceptor
     * @return
     */
    @RequestMapping("getLogById")
    public JSONObject getLogById(@RequestParam("id") String id, @RequestParam("index") String  index, @RequestParam("type") String  type);
}
