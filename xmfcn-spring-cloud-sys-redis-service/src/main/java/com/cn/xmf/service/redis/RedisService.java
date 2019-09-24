package com.cn.xmf.service.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.util.StringUtil;
import org.apache.commons.httpclient.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("all")
@RestController
@RequestMapping("/server/redis/")
public class RedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisService.class);
    private static final String UNLOCK_LUA;//释放锁的命令
    private static RedisConnection connection = null;//redis连接
    private final ReentrantLock lock = new ReentrantLock();

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;

    public RedisConnection getRedisConnection() {
        if (connection != null) {
            return connection;
        }
        lock.lock();
        logger.info("------------------------------redis 连接不存在");
        try {
            connection = lettuceConnectionFactory.getConnection();
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        } finally {
            lock.unlock();
        }
        if (connection != null) {
            logger.info("------------------------------已经获取redis 连接");
        }
        return connection;
    }

    /**
     * getRedisInfo（redis 运行健康信息)
     *
     * @param key
     * @return
     */
    @RequestMapping("getRedisInfo")
    public JSONObject getRedisInfo() {
        logger.info("getRedisInfo（redis 运行健康信息) 开始 ");
        JSONObject result = new JSONObject();
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        boolean broken = false;
        try {
            Hashtable info = conn.info();
            if (info == null) {
                return result;
            }
            String jsonString = JSON.toJSONString(info);
            if (StringUtil.isBlank(jsonString)) {
                return result;
            }
            result.put("info", jsonString);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        logger.info("getRedisInfo（redis 运行健康信息)redis 结束 " + result);
        return result;
    }

    /**
     * getQueueLength（获取队列长度)key 是消息频道
     *
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
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = conn.lLen(key.getBytes());
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
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = conn.lPush(key.getBytes(), value.getBytes());
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
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        try {
            byte[] bytes = conn.rPop(key.getBytes());
            if (bytes != null) {
                result = new String(bytes, "utf-8");
            }
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        logger.info("getFromQueue（读取队列消息）redis 结束 " + result);
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
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        try {
            byte[] bytes = conn.get(key.getBytes());
            if (bytes != null) {
                result = new String(bytes, "utf-8");
            }
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
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        try {
            result = conn.exists(key.getBytes());
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
    public Long expire(String key, long seconds) {
        Long result = (long) -1;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        try {
            boolean ret = conn.expire(key.getBytes(), seconds);
            if (ret) {
                result = (long) 1;
            }
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
     * @author rufei.cn
     */
    @RequestMapping("save")
    public long saveCache(String key, String value, long seconds) {
        logger.info("save:(设置缓存-带有效期) 开始 key={},seconds={}", key, seconds);
        long result = -1;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        try {
            Boolean ret = conn.set(key.getBytes(), value.getBytes(), Expiration.seconds(seconds), RedisStringCommands.SetOption.UPSERT);
            if (ret) {
                result = 1L;
            }
        } catch (Exception e) {
            logger.error("saveCache:" + StringUtil.getExceptionMsg(e));
        }
        logger.info("saveCache:(设置缓存-带有效期) 结束 key={},result={}", key, result);
        return result;
    }

    /**
     * delete（将 key 缓存数据删除）
     *
     * @param key
     * @return
     */
    @RequestMapping("delete")
    public Long delete(String key) {
        logger.info("delete（将 key 缓存数据删除） 开始 key={}", key);
        Long result = null;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        try {
            result = conn.del(key.getBytes());
        } catch (Exception e) {
            logger.error("delete_error:" + StringUtil.getExceptionMsg(e));
        }
        logger.info("delete（将 key 缓存数据删除）结束 key={},result={}", key, result);
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
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        try {
            result = conn.decr(key.getBytes());
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
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        try {
            result = conn.incrBy(key.getBytes(), integer);
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
        if ("*".equals(pattern)) {
            return result;
        }
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        try {
            Set<byte[]> list = getKeyList(pattern);
            if (list == null || list.size() < 1) {
                return result;
            }
            Iterator<byte[]> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() == null) {
                    continue;
                }
                String next = new String(iterator.next(), "utf-8");
                if (StringUtil.isBlank(next)) {
                    continue;
                }
                delete(next);
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
    public Set<byte[]> getKeyList(String pattern) {
        Set<byte[]> result = null;
        if (StringUtil.isBlank(pattern)) {
            return result;
        }
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        try {
            Set<byte[]> keys = conn.keys(pattern.getBytes());
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        return result;
    }

    /**
     * getLock（分布式锁-正确方式）
     *
     * @param key        锁标识
     * @param requestId  请求唯一标识，解锁需要带入
     * @param expireTime 自动释放锁的时间
     */
    @RequestMapping("getLock")
    public Long getLock(String key, String requestId, long expireTime) {
        Long result = -1L;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        if (StringUtil.isBlank(requestId)) {
            return result;
        }
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        try {
            boolean ret = conn.set(key.getBytes(Charset.forName("UTF-8")), requestId.getBytes(Charset.forName("UTF-8")), Expiration.milliseconds(expireTime), RedisStringCommands.SetOption.SET_IF_ABSENT);
            if (ret) {
                result = 1L;
            }
        } catch (Exception e) {
            logger.error("getRedisLock（取分布式锁-正确方式）异常={}", StringUtil.getExceptionMsg(e));
        }
        return result;
    }

    /**
     * unRedisLock（释放分布式锁）
     *
     * @param key       锁
     * @param requestId 请求唯一标识，解锁需要带入
     * @return 是否释放成功
     */
    @RequestMapping("unRedisLock")
    public int unRedisLock(String key, String requestId) {
        int result = -1;
        if (StringUtil.isBlank(key)) {
            return result;
        }
        if (StringUtil.isBlank(requestId)) {
            return result;
        }
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return result;
        }
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
            boolean ret = conn.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN, 1, key.getBytes(Charset.forName("UTF-8")), requestId.getBytes(Charset.forName("UTF-8")));
            if (ret) {
                result = 1;
            }
        } catch (Exception e) {
            logger.error("unRedisLock（释放分布式锁）异常={}", StringUtil.getExceptionMsg(e));
        }
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
        RedisConnection conn = getRedisConnection();
        if (conn == null) {
            return onlyNo;
        }
        long number = -1;
        try {
            number = conn.incr(key.getBytes());
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        onlyNo = prefix + DateUtil.formatDate(new Date(), "yyyyMMddHHmmss") + number;
        logger.info("getOnlyNo(获取唯一编号) 结束  ：" + onlyNo);
        return onlyNo;
    }

}
