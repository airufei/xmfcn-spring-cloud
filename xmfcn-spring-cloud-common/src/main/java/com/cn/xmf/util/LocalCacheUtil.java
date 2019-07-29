package com.cn.xmf.util;

import net.bytebuddy.implementation.bytecode.Throw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * 本地缓存工具类
 */
@SuppressWarnings("all")
public class LocalCacheUtil {


    private static ThreadPoolExecutor cachedThreadPool = TreadPoolUtil.getCommonThreadPool();
    private static ConcurrentMap<String, Object> cacheMap = new ConcurrentHashMap();//数据本机缓存，减少rpc 调用
    private static Logger logger = LoggerFactory.getLogger(LocalCacheUtil.class);

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @param expTime
     */
    public static void saveCache(String key, String value, long expTime) {
        cacheMap.put(key, value);
        String classMethod = LocalCacheUtil.class.getName() + ".saveCache()";
        TreadPoolUtil.getThreadPoolIsNext(cachedThreadPool, classMethod);//判断激活的线程数量与最大线程的比列 如果大于80% 则暂停N秒
        cachedThreadPool.execute(() -> {
            cleanLoadCache(key, expTime);//定时清除缓存
        });
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    public static void saveCache(String key, String value) {
        long expTime = 30;//默认30秒
        saveCache(key, value, expTime);
    }

    /**
     * 设置缓存
     *
     * @param key
     */
    public static String getCache(String key) {
        String result = null;
        Object o = cacheMap.get(key);
        if (o != null) {
            result = o.toString();
        }
        return result;
    }

    /**
     * 定时清除本地缓存
     *
     * @param key
     */
    private static void cleanLoadCache(String key, long expTime) {
        try {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    cacheMap.remove(key);
                    timer.cancel();
                }
            }, expTime);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
    }

}
