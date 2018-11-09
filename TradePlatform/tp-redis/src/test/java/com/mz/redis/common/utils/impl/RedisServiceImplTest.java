package com.mz.redis.common.utils.impl;

import com.mz.redis.common.JedisConfiguration;
import com.mz.redis.common.utils.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 2018/8/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JedisConfiguration.class, RedisServiceImpl.class})
public class RedisServiceImplTest {

    @Autowired
    RedisService redisService;

    @Test
    public void setList() {
        List<String> ls = new ArrayList();
        ls.add("test2");
        redisService.setList("1", ls);
    }

    @Test
    public void getList1() {
        System.out.println(redisService.getList1("1"));
    }
}