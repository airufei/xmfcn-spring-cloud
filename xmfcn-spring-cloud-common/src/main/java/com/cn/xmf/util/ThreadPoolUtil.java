package com.cn.xmf.util;

import com.cn.xmf.base.Interface.SysCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工具类
 */
@SuppressWarnings("all")
public class ThreadPoolUtil {

    private static Logger logger = LoggerFactory.getLogger(ThreadPoolUtil.class);
    private static int maxQueueSize = 500;//默认最大队列数量
    private static int corePoolSize = 200;//默认核心线程数
    private static int maxPoolSize = 800;//最大线程数，超过此值改为默认值
    private static int keepAliveTime = 3000;//空闲线程等待时间（秒）
    /**
     * int corePoolSize, 指定了线程池中的线程数量，它的数量决定了添加的任务是开辟新的线程去执行，还是放到workQueue任务队列中去；
     * int maximumPoolSize, 指定了线程池中的最大线程数量，这个参数会根据你使用的workQueue任务队列的类型，决定线程池会开辟的最大线程数量；
     * long keepAliveTime, 当线程池中空闲线程数量超过corePoolSize时，多余的线程会在多长时间内被销毁；
     * TimeUnit unit, keepAliveTime的单位
     * BlockingQueue<Runnable> workQueue 任务队列，被添加到线程池中，但尚未被执行的任务；它一般分为直接提交队列、有界任务队列、无界任务队列、优先任务队列几种；
     * ThreadFactory threadFactory 线程工厂
     * RejectedExecutionHandler handler 队列满了之后的拒绝策略【AbortPolicy：表示拒绝任务并抛出异常 DiscardPolicy：表示拒绝任务但不做任何动作 CallerRunsPolicy：表示拒绝任务，并在调用者的线程中直接执行该任务
     * DiscardOldestPolicy：表示先丢弃任务队列中的第一个任务，然后把这个任务加进队列。】
     */
    private static ThreadPoolExecutor cachedThreadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(maxQueueSize), new XmfThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());


    /**
     * 线程工厂-设置线程前缀名称
     */
    private static class XmfThreadFactory implements ThreadFactory {
        private AtomicInteger count = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            String threadName = new StringBuilder().append("xmf_thread_pool_").append(ThreadPoolUtil.class.getSimpleName()).append(count.addAndGet(1)).toString();
            t.setName(threadName);
            return t;
        }
    }

    /**
     * 重写拒绝机制，改为阻塞提交。保证不抛弃一个任务
     */
    private static class XmfRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                // 核心改造点，由blockingqueue的offer改成put阻塞方法
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                logger.error(StringUtil.getExceptionMsg(e));
            }
        }
    }
    /**
     * 获取公共线程池
     *
     * @return
     */
    public static ThreadPoolExecutor getCommonThreadPool() {
        return cachedThreadPool;
    }

    /**
     * 判断激活的线程数量与最大线程的比列 如果大于80% 则暂停1秒
     *
     * @param cachedThreadPool
     */
    public static void getThreadPoolIsNext(ThreadPoolExecutor cachedThreadPool, String classMethod) {
        boolean isNext = true;
        if (cachedThreadPool == null) {
            return;
        }
        int waitCount = 0;//等待次数
        long minWaitTime = 200;//最小等待时间
        try {
            while (isNext) {
                int maxPoolSize = cachedThreadPool.getMaximumPoolSize();
                int poolSize = cachedThreadPool.getPoolSize();
                if (maxPoolSize <= 0) {
                    return;
                }
                if (poolSize <= 0) {
                    return;
                }
                double poolNum = poolSize / maxPoolSize;
                if (poolNum > 0.8) {
                    waitCount = waitCount + 1;
                    StringUtil.threadSleep(minWaitTime);
                } else {
                    isNext = false;
                }
            }
            if (waitCount > 0) {
                getMonitorThreadPoolInfo(cachedThreadPool, waitCount, minWaitTime, classMethod);//发送监控数据
            }
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
    }

    /**
     * 发送监控数据
     *
     * @param cachedThreadPool
     */
    public static void getMonitorThreadPoolInfo(ThreadPoolExecutor cachedThreadPool, int waitCount, long minWaitTime, String classMethod) {
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
        stringBuilder.append("\n\n【线程池监控】");
        stringBuilder.append("\n\n 执行位置：").append(classMethod);
        stringBuilder.append("\n\n 等待睡眠次数=").append(waitCount);
        stringBuilder.append("\n\n 每次睡眠时间(毫秒)=").append(minWaitTime);
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
        String threadPoolMessage = sysCommonService.getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "isSendMonitorThreadPoolMessage");
        boolean isSendMonitorThreadPoolMessage = StringUtil.strToBoolean(threadPoolMessage);
        if (!isSendMonitorThreadPoolMessage) {
            return;
        }
        sysCommonService.sendDingTalkMessage("getMonitorThreadPoolInfo", null, null, stringBuilder.toString(), LocalCacheUtil.class);//发送钉钉消息
    }


    public static void getData(int num) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程池测试
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        long startTimeMillis = System.currentTimeMillis();
        String classMethod = LocalCacheUtil.class.getName() + ".main()";
        for (int i = 0; i < 5000; i++) {
            int finalI = i;
            getThreadPoolIsNext(cachedThreadPool, classMethod);//判断激活的线程数量与最大线程的比列 如果大于80% 则暂停N秒
            cachedThreadPool.execute(() -> {
                getData(finalI);//定时清除缓存
            });
        }
        cachedThreadPool.shutdown();
        long endTimeMillis = System.currentTimeMillis();
        long retTime = (endTimeMillis - startTimeMillis) / 1000;
        logger.info("花费时间未retTime={}", retTime);
    }
}
