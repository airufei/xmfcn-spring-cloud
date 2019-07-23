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
public class LocalCacheUtil {

    /**
     * int corePoolSize, 指定了线程池中的线程数量，它的数量决定了添加的任务是开辟新的线程去执行，还是放到workQueue任务队列中去；
     * int maximumPoolSize, 指定了线程池中的最大线程数量，这个参数会根据你使用的workQueue任务队列的类型，决定线程池会开辟的最大线程数量；
     * long keepAliveTime, 当线程池中空闲线程数量超过corePoolSize时，多余的线程会在多长时间内被销毁；
     * TimeUnit unit, keepAliveTime的单位
     * BlockingQueue<Runnable> workQueue 任务队列，被添加到线程池中，但尚未被执行的任务；它一般分为直接提交队列、有界任务队列、无界任务队列、优先任务队列几种；
     */
    private static int maxPoolSize = 300;//最大队列
    private static ThreadPoolExecutor cachedThreadPool = new ThreadPoolExecutor(300, maxPoolSize, 3000, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(200));

    public static ConcurrentMap<String, Object> cacheMap = new ConcurrentHashMap();//数据本机缓存，减少rpc 调用
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
        StringUtil.getThreadPoolIsNext(cachedThreadPool, maxPoolSize, LocalCacheUtil.class);//判断激活的线程数量与最大线程的比列 如果大于80% 则暂停1秒
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
            logger.error(StringUtil.getExceptionMsg(e));
        }
    }

    public static void getData() {
        for (int i = 0; i < 50; i++) {
            StringUtil.threadSleep(20);
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10000; i++) {
            logger.info("start i={}", i);
            StringUtil.getThreadPoolIsNext(cachedThreadPool, maxPoolSize, LocalCacheUtil.class);//判断激活的线程数量与最大线程的比列 如果大于80% 则暂停1秒
            try {
                cachedThreadPool.execute(() -> {
                    getData();//定时清除缓存
                });
            } catch (Exception e) {
                String exceptionMsg = StringUtil.getExceptionMsg(e);
                logger.error(exceptionMsg);
                throw  new Exception("线程池异常："+exceptionMsg);
            }
            logger.info("main i={}", i);
        }
    }

}
