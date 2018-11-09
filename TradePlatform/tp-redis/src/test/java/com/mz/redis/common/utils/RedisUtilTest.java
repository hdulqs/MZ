package com.mz.redis.common.utils;

import com.mz.redis.common.JedisConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Frank on 2018/8/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JedisConfiguration.class, RedisUtil.class})
public class RedisUtilTest {

    RedisUtil redisUtil = new RedisUtil(String.class);

    /*@Autowired
    ApplicationContext applicationContext;

    @Before
    public void setApplicationContext() {
        SpringContextUtil.setApplicationContext(applicationContext);
    }*/

    @Test
    public void get() {
        System.out.println(redisUtil.get("testId"));
    }

    @Test
    public void put() {
        redisUtil.put("test", "testId");
    }

    @Test
    public void delete() {
        redisUtil.delete("testId");
    }
}