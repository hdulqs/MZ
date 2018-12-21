package com.mz.redis.common;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Frank on 2018/8/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JedisConfiguration.class, RedisUtils.class})
public class RedisUtilsTest {

    @Autowired
    RedisUtils redisUtils;

    @Before
    public void setUp() {
        redisUtils.set("test", "100");
    }

    @Test
    public void testLock() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                redisUtils.sync("test", 30, new RedisService());
            });
        }
    }

    @Service
    class RedisService implements RedisUtils.SyncLockCallback {
        @Override
        public Object callback() {
            try {
                String threadName = Thread.currentThread().getName();
                String test = redisUtils.get("test");
                System.out.println(threadName + "获取test=" + test);
                int a = (Integer.parseInt(test) - 1);
                redisUtils.set("test", a + "");
                System.out.println(threadName + "修改test=" + a);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return redisUtils.get("test");
        }
    }
}