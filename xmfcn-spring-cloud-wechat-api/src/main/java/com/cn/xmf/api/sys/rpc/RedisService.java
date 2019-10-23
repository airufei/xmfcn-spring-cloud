package com.cn.xmf.api.sys.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${base-service.sys-base-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/redis/")// 配置远程服务路径
public interface RedisService {

    /**
     * save:(设置缓存-带有效期)
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    @RequestMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String save(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value, @RequestParam(value = "seconds") int seconds);

    /**
     * 获取单个值
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "getCache", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String getCache(@RequestParam(value = "key") String key);

    /**
     * 入队key 是消息频道 ，value 消息内容
     *
     * @param key
     * @param value
     * @return
     */
    @RequestMapping(value = "putToQueue", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long putToQueue(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value);

    /**
     * 读取消息 （读过队列中消息就没有了）key 是消息频道
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "getFromQueue", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String getFromQueue(@RequestParam(value = "key") String key);


    /**
     * 将 key 缓存数据删除
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long delete(@RequestParam(value = "key") String key);

    /**
     * 根据通配符删除所有key
     *
     * @param pattern
     * @return
     */
    @RequestMapping(value = "deletes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public long deletes(@RequestParam(value = "pattern") String pattern);


    /**
     * 获取分布式锁
     *
     * @param key
     * @param key expireTime 自动释放锁的时间
     * @return
     */
    @RequestMapping(value = "getLock", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long getLock(@RequestParam(value = "key") String key,@RequestParam(value = "requestId") String requestId, @RequestParam(value = "expireTime") long expireTime);

    /**
     * unRedisLock（释放分布式锁）
     * @param key       锁
     * @return 是否释放成功
     */
    @RequestMapping(value ="unRedisLock", consumes = MediaType.APPLICATION_JSON_VALUE)
    public int unRedisLock(@RequestParam(value = "key") String key,@RequestParam(value = "requestId") String requestId);

}