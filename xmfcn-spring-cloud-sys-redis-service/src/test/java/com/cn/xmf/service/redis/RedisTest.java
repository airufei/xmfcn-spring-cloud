package com.cn.xmf.service.redis;


import com.cn.xmf.util.StringUtil;
import com.cn.xmf.util.ThreadPoolUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ThreadPoolExecutor;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    private static Logger logger = LoggerFactory.getLogger(RedisTest.class);
    private static ThreadPoolExecutor cachedThreadPool = ThreadPoolUtil.getCommonThreadPool();//获取公共线程池
    @Autowired
    private RedisService redisService;

    //测试锁
    @Test
    public void testRedis() {
        for (int i = 0; i < 10000; i++) {
            testRedisLock();
            cachedThreadPool.execute(() -> {
                testRedisLock2();
            });

        }
    }

    private void testRedisLock() {
        logger.info("----------------------->测试锁开始");
        String requestId = StringUtil.getUuId();
        String lockKey = "lock_11111111111111111111111111111113332322";
        long redisLock = redisService.getLock(lockKey, requestId, 5000);
        if (redisLock != 1) {
            logger.info("==========================》锁被占用正在执行中");
        }
        logger.info("已经获取到锁");
        int lock = redisService.unRedisLock(lockKey, requestId);
        logger.info("----------------------->测试锁结束 lock={}",lock);
    }


    private void testRedisLock2() {
        logger.info("测试锁开始----------------------->");
        String requestId = StringUtil.getUuId();
        String lockKey = "lock_11111111111111111111111111111113332322";
        long redisLock = redisService.getLock(lockKey, requestId, 500);
        if (redisLock != 1) {
            logger.info("正在执行中");
        }
        logger.info("已经获取到锁");
        int lock = redisService.unRedisLock(lockKey, requestId);
        logger.info("已经解锁");
    }
}
