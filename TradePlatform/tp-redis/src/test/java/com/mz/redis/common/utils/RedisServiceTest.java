package com.mz.redis.common.utils;

import com.mz.redis.common.JedisConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Frank on 2018/8/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JedisConfiguration.class, RedisService.class})
public class RedisServiceTest {

    RedisUtil redisService = new RedisUtil(String.class);

    /*@Autowired
    ApplicationContext applicationContext;

    @Before
    public void setApplicationContext() {
        SpringContextUtil.setApplicationContext(applicationContext);
    }*/

    @Test
    public void get() {
        System.out.println(redisService.get("testId"));
    }

    @Test
    public void put() {
        redisService.put("test", "testId");
    }

    @Test
    public void delete() {
        redisService.delete("testId");
    }
}