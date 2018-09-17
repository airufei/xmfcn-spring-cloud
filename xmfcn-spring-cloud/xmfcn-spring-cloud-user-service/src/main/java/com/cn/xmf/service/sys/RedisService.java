
package com.cn.xmf.service.sys;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${base-service.sys-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/redis/")// 配置远程服务路径
public interface RedisService {

    /**
     * saveCache:(设置缓存-带有效期)
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */

    @RequestMapping(value = "saveCache", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String saveCache(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value, @RequestParam(value = "seconds") int seconds);


    /**
     * 获取单个值
     *
     * @param key
     * @return
     */

    @RequestMapping(value = "getCache", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String getCache(@RequestParam(value = "key") String key);


    /**
     * 将 key 缓存数据删除
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "delCache", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long delCache(@RequestParam(value = "key") String key);

}
