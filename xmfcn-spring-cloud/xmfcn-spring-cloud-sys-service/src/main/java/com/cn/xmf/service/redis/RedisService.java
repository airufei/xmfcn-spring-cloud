package com.cn.xmf.service.redis;

import com.cn.xmf.util.StringUtil;
import org.apache.commons.httpclient.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
@SuppressWarnings("all")
@RestController
@RequestMapping("/server/redis/")
public class RedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    private Jedis getJedis() {
        Jedis jedis = null;
        if (jedisConnectionFactory != null) {
            jedis = jedisConnectionFactory.getShardInfo().createResource();
        }
        return jedis;
    }

    /**
     * getQueueLength（获取队列长度)key 是消息频道
     * @param key
     * @return
     */
    @RequestMapping("getQueueLength")
    public Long getQueueLength(String key) {
        logger.info("getQueueLength（获取队列长度) 开始 " + key);
        Long result = (long) -1;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = jedis.llen(key);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        logger.info("getQueueLength（获取队列长度)redis 结束 " + result);
        return result;
    }

    /**
     * putToQueue（写入消息队列）入队key 是消息频道 ，value 消息内容
     *
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("putToQueue")
    public Long putToQueue(final String key, final String value) {
        logger.info("putToQueue（写入消息队列）redis 开始 " + key + " value:" + value);
        Long result = (long) -1;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        if (StringUtil.isBlank(value)) {
            return result;
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
            broken = true;
        }
        logger.info("putToQueue（写入消息队列）redis 结束 " + result);
        return result;
    }

    /**
     * getFromQueue（读取队列消息） （读过队列中消息就没有了）key 是消息频道
     *
     * @param key
     * @return
     */
    @RequestMapping("getFromQueue")
    public String getFromQueue(final String key) {
        logger.info("getFromQueue（读取队列消息）redis 开始 " + key);
        String result = null;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.rpop(key);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        logger.info("getFromQueue（读取队列消息）redis 结束 " + result);
        return result;
    }

    /**
     * 设置缓存-默认缓存5分钟
     *
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("setCache")
    public String setCache(String key, String value) {
        String result = null;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        if (StringUtil.isBlank(value)) {
            return result;
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            int expDate = 60 * 5;
            saveCache(key, value, expDate);
        } catch (Exception e) {
            logger.error("setCache:" + StringUtil.getExceptionMsg(e));
        }
        return result;
    }


    /**
     * getCache（获取缓存数据）
     *
     * @param key
     * @return
     */
    @RequestMapping("getCache")
    public String getCache(String key) {
        logger.info("getCache（获取缓存数据）redis 开始 " + key);
        String result = null;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.get(key);
        } catch (Exception e) {
            logger.error("getCache:" + StringUtil.getExceptionMsg(e));
        }
        return result;
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    @RequestMapping("exists")
    public Boolean exists(String key) {
        Boolean result = false;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.exists(key);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        return result;
    }

    /**
     * 在某段时间后失效
     *
     * @param key
     * @param unixTime
     * @return
     */
    @RequestMapping("expire")
    public Long expire(String key, int seconds) {
        Long result = (long) -1;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.expire(key, seconds);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        return result;
    }

    /**
     * saveCache:(设置缓存-带有效期)
     *
     * @param key
     * @param value
     * @param seconds 有效时间 单位（秒）
     * @return
     * @Author airufei
     */
    @RequestMapping("saveCache")
    public String saveCache(String key, String value, int seconds) {
        logger.info("saveRedis:(设置缓存-带有效期) 开始 key={},seconds={}",key,seconds);
        String result = null;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.setex(key, seconds, value);
        } catch (Exception e) {
            logger.error("saveCache:" + StringUtil.getExceptionMsg(e));
        }
        logger.info("saveCache:(设置缓存-带有效期) 结束 key={},result={}", key,result);
        return result;
    }

    /**
     * delCache（将 key 缓存数据删除）
     *
     * @param key
     * @return
     */
    @RequestMapping("delCache")
    public Long delCache(String key) {
        logger.info("delCache（将 key 缓存数据删除） 开始 key={}",key);
        Long result = null;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.del(key);
        } catch (Exception e) {
            logger.error("delCache_error:" + StringUtil.getExceptionMsg(e));
        }
        logger.info("delCache（将 key 缓存数据删除）结束 key={},result={}", key,result);
        return result;
    }

    /**
     * 将 key 中储存的数字值减一
     *
     * @param key
     * @return
     */
    @RequestMapping("decr")
    public Long decr(String key) {
        Long result = null;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.decr(key);
        } catch (Exception e) {
            logger.error("decr_error:" + StringUtil.getExceptionMsg(e));
        }
        return result;
    }

    /**
     * 将 key 中储存的数字值加N
     *
     * @param key
     * @param integer
     * @return
     */
    @RequestMapping("incrBy")
    public Long incrBy(String key, long integer) {
        Long result = null;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.incrBy(key, integer);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        return result;
    }


    /**
     * 根据通配符删除所有key
     *
     * @param key
     * @param integer
     * @return
     */
    @RequestMapping("delCaches")
    public long delCaches(String pattern) {
        long result = -1;
        if (StringUtil.isBlank(pattern)) {
            return result;
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            Set<String> list = getKeyList(pattern);
            if (list == null || list.size() < 1) {
                return result;
            }
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                if (StringUtil.isBlank(next)) {
                    continue;
                }
                delCache(next);
            }
            result = 1;
        } catch (Exception e) {
            logger.error("delCaches:" + StringUtil.getExceptionMsg(e));
        }
        return result;
    }

    /**
     * 根据通配符获取key集合
     *
     * @param key
     * @param integer
     * @return
     */
    @RequestMapping("getKeyList")
    public Set<String> getKeyList(String pattern) {
        Set<String> result = null;
        if (StringUtil.isBlank(pattern)) {
            return result;
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.keys(pattern);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        return result;
    }

    /**
     * getLock(获取分布式锁)
     *
     * @param key
     * @param integer
     * @return
     */
    @RequestMapping("getLock")
    public Long getLock(String key) {
        logger.info("getLock(获取分布式锁) 开始：" + key);
        Long result = null;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.incr(key);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        int maxLockCount=500;
        if(result>=maxLockCount)//清除死锁
        {
            delCache(key);
        }
        logger.info("getLock(获取分布式锁) 结束：" + result);
        return result;
    }

    /**
     * getOnlyNo(获取唯一编号)
     *
     * @param type
     * @return
     */
    @RequestMapping("getOnlyNo")
    public String getOnlyNo(String prefix) {
        logger.info("getOnlyNo(获取唯一编号) 开始：" + prefix);
        String onlyNo = null;
        if (StringUtil.isBlank(prefix)) {
            return onlyNo;
        }
        String key = DateUtil.formatDate(new Date(), "yyyyMMdd");
        if (!exists(key)) {
            saveCache(key, "100000", 60 * 60 * 24);
        }
        Jedis jedis = getJedis();
        if (jedis == null) {
            return onlyNo;
        }
        long number=-1;
        try {
            number = jedis.incr(key);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        onlyNo = prefix + DateUtil.formatDate(new Date(), "yyyyMMddHHmmss") + number;
        logger.info("getOnlyNo(获取唯一编号) 结束  ：" + onlyNo);
        return onlyNo;
    }

}
