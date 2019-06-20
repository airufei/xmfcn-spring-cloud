package com.cn.xmf.util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 本地缓存工具类
 */
public class LocalCacheUtil {

    public static ConcurrentMap<String, Object> cacheMap = new ConcurrentHashMap();//数据本机缓存，减少rpc 调用
    private static ExecutorService cachedThreadPool = Executors.newFixedThreadPool(15);//线程池

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @param expTime
     */
    public static void saveCache(String key, String value, long expTime) {
        cacheMap.put(key, value);
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
                public void run() {
                    cacheMap.remove(key);
                    timer.cancel();
                }
            }, expTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
