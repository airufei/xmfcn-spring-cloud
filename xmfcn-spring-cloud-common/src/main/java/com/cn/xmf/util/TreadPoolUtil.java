package com.cn.xmf.util;

import com.cn.xmf.base.Interface.SysCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类
 */
public class TreadPoolUtil {

    private static Logger logger = LoggerFactory.getLogger(TreadPoolUtil.class);
    private static int maxQueueSize = 20;//默认最大队列数量
    private static int corePoolSize = 30;//默认核心线程数
    private static int maxPoolSize = 200;//最大线程数，超过此值改为默认值
    private static int keepAliveTime = 3000;//空闲线程等待时间（秒）
    /**
     * int corePoolSize, 指定了线程池中的线程数量，它的数量决定了添加的任务是开辟新的线程去执行，还是放到workQueue任务队列中去；
     * int maximumPoolSize, 指定了线程池中的最大线程数量，这个参数会根据你使用的workQueue任务队列的类型，决定线程池会开辟的最大线程数量；
     * long keepAliveTime, 当线程池中空闲线程数量超过corePoolSize时，多余的线程会在多长时间内被销毁；
     * TimeUnit unit, keepAliveTime的单位
     * BlockingQueue<Runnable> workQueue 任务队列，被添加到线程池中，但尚未被执行的任务；它一般分为直接提交队列、有界任务队列、无界任务队列、优先任务队列几种；
     */
    private static ThreadPoolExecutor cachedThreadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(maxQueueSize));

    /**
     * 判断激活的线程数量与最大线程的比列 如果大于80% 则暂停1秒
     *
     * @param cachedThreadPool
     */
    public static void getThreadPoolIsNext(ThreadPoolExecutor cachedThreadPool, Class t) {
        boolean isNext = true;
        try {
            if (cachedThreadPool == null) {
                return;
            }
            if (maxPoolSize <= 0) {
                return;
            }
            int maxPoolSize=cachedThreadPool.getMaximumPoolSize();
            int activeCount = cachedThreadPool.getActiveCount();
            int poolSize = cachedThreadPool.getPoolSize();
            if (maxPoolSize <= 0) {
                return;
            }
            if (poolSize <= 0) {
                return;
            }
            if (activeCount <= 0) {
                return;
            }
            double activeNum = activeCount / maxPoolSize;
            double poolNum = poolSize / maxPoolSize;
            getCountThreadPool(cachedThreadPool, t);//发送监控数据
            if (poolNum > 0.8 || activeNum > 0.8) {
                StringUtil.threadSleep(2000);
            }
        } catch (Exception e) {
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            logger.error(exceptionMsg);
        }
    }

    /**
     * 发送监控数据
     *
     * @param cachedThreadPool
     */
    public static void getCountThreadPool(ThreadPoolExecutor cachedThreadPool, Class t) {
        if (cachedThreadPool == null) {
            return;
        }
        int activeCount = cachedThreadPool.getActiveCount();
        long completedTaskCount = cachedThreadPool.getCompletedTaskCount();
        int corePoolSize = cachedThreadPool.getCorePoolSize();
        int poolSize = cachedThreadPool.getPoolSize();
        long taskCount = cachedThreadPool.getTaskCount();
        int maximumPoolSize = cachedThreadPool.getMaximumPoolSize();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【线程池监控】");
        stringBuilder.append("\n\n 配置最大线程数=").append(maximumPoolSize);
        stringBuilder.append("\n\n 当前激活线程数=").append(activeCount);
        stringBuilder.append("\n\n 提交的任务数=").append(taskCount);
        stringBuilder.append("\n\n 已经完成的任务数量=").append(completedTaskCount);
        stringBuilder.append("\n\n 核心线程数=").append(corePoolSize);
        stringBuilder.append("\n\n 当前运行线程数=").append(poolSize);
        logger.info("==============================================》" + stringBuilder.toString());
        SysCommon sysCommonService = null;
        try {
            sysCommonService = (SysCommon) SpringUtil.getBean("sysCommonService");
        } catch (Exception e) {
        }
        if (sysCommonService == null) {
            return;
        }
        sysCommonService.sendDingMessage("getCountThreadPool", null, null, stringBuilder.toString(), t);//发送钉钉消息
    }


    public static void getData(int num) {
        for (int i = 0; i < 50; i++) {
            continue;
        }
    }

    /**
     * 线程池测试
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10000; i++) {
            getThreadPoolIsNext(cachedThreadPool, LocalCacheUtil.class);//判断激活的线程数量与最大线程的比列 如果大于80% 则暂停N秒
            try {
                final int num = i;
                cachedThreadPool.execute(() -> {
                    getData(num);//定时清除缓存
                });
            } catch (Exception e) {
                String exceptionMsg = StringUtil.getExceptionMsg(e);
                logger.error(exceptionMsg);
                throw new Exception("线程池异常：" + exceptionMsg);
            }
        }
        cachedThreadPool.shutdown();
    }
}
