package com.cn.xmf.util;


import com.google.common.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ListenableFutureTest {

    private static Logger logger = LoggerFactory.getLogger(TreadPoolUtil.class);
    private static ThreadPoolExecutor cachedThreadPool = TreadPoolUtil.getCommonThreadPool();//获取公共线程池
    private static ListeningExecutorService pool = MoreExecutors.listeningDecorator(cachedThreadPool);

    public static int call(int mun) {
        StringUtil.threadSleep(2000);
        return mun * 2;
    }

    /**
     * 同步调用计算两次调用返回的结果
     *
     * @return
     */
    public static int synchroSum(int num) {
        int sum = 0;
        int call01 = call(num);
        int call02 = call(num);
        sum = call01 + call02;
        return sum;
    }

    /**
     * 异步调用计算两次调用返回的结果
     *
     * @return
     */
    public static int asyncSum(int mun) {
        int sum = 0;
        ListenableFuture<Integer> future01 = pool.submit(() -> {
            return call(mun);
        });
        ListenableFuture<Integer> future02 = pool.submit(() -> {
            return call(mun);
        });
        try {
            Integer s01 = future01.get();
            Integer s02 = future02.get();
            sum = s01 + s02;
            logger.info("s01={}", s01);
            logger.info("s02={}", s02);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        return sum;
    }

    public static void main(String[] args) {
        long startTimeMillis = System.currentTimeMillis();
        int sum = asyncSum(30);
        logger.info("sum={}", sum);
        long endTimeMillis = System.currentTimeMillis();
        long retTime = (endTimeMillis - startTimeMillis) / 1000;
        logger.info("花费时间未retTime={}", retTime);
    }
}
