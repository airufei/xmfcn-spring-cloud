package com.cn.xmf.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 本地缓存工具类
 */
@SuppressWarnings("all")
public class LocalCacheUtil {

    private static ConcurrentMap<String, LocalCacheData> cacheMap = new ConcurrentHashMap();//数据本机缓存，减少rpc 调用
    private static Logger logger = LoggerFactory.getLogger(LocalCacheUtil.class);

    private static class LocalCacheData {
        private String key;
        private Object value;
        private long timeoutTime;

        public LocalCacheData() {
        }

        public LocalCacheData(String key, Object value, long timeoutTime) {
            this.key = key;
            this.value = value;
            this.timeoutTime = timeoutTime;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object val) {
            this.value = val;
        }

        public long getTimeoutTime() {
            return timeoutTime;
        }

        public void setTimeoutTime(long timeoutTime) {
            this.timeoutTime = timeoutTime;
        }
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @param expTime
     */
    public static void saveCache(String key, String value, long expTime) {
        cleanTimeutCache();
        if (key == null || key.trim().length() == 0) {
            return;
        }
        if (value == null) {
            remove(key);
        }
        if (expTime <= 0) {
            remove(key);
        }
        long timeoutTime = System.currentTimeMillis() + expTime;
        LocalCacheData localCacheData = new LocalCacheData(key, value, timeoutTime);
        cacheMap.put(localCacheData.getKey(), localCacheData);
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
        if (key == null || key.trim().length() == 0) {
            return result;
        }
        LocalCacheData localCacheData = cacheMap.get(key);
        Object obj = null;
        if (localCacheData != null && System.currentTimeMillis() < localCacheData.getTimeoutTime()) {
            obj = localCacheData.getValue();
        } else {
            remove(key);
        }
        if (obj != null) {
            result = obj.toString();
        }
        return result;
    }

    /**
     * remove cache
     *
     * @param key
     * @return
     */
    public static boolean remove(String key) {
        if (key == null || key.trim().length() == 0) {
            return false;
        }
        cacheMap.remove(key);
        return true;
    }
    /**
     * 定时清除本地缓存
     *
     * @param key
     */
    /**
     * clean timeout cache
     *
     * @return
     */
    public static boolean cleanTimeutCache() {
        if (!cacheMap.keySet().isEmpty()) {
            for (String key : cacheMap.keySet()) {
                LocalCacheData localCacheData = cacheMap.get(key);
                if (localCacheData != null && System.currentTimeMillis() >= localCacheData.getTimeoutTime()) {
                    cacheMap.remove(key);
                }
            }
        }
        return true;
    }

}
