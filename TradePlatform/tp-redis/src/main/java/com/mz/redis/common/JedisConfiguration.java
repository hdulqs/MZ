package com.mz.redis.common;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by Frank on 2018/8/13.
 */
@Configuration
public class JedisConfiguration {

  @Value("${spring.redis.host}")
  private String host;
  @Value("${spring.redis.port}")
  private String port;
  @Value("${spring.redis.timeout}")
  private String timeout;
  @Value("${spring.redis.pool.max-idle}")
  private String maxIdle;
  @Value("${spring.redis.pool.max-wait}")
  private String maxWaitMillis;
  @Value("${spring.redis.password}")
  private String password;

  @Value("${spring.redis1.host}")
  private String host1;
  @Value("${spring.redis1.port}")
  private String port1;
  @Value("${spring.redis1.timeout}")
  private String timeout1;
  @Value("${spring.redis1.pool.max-idle}")
  private String maxIdle1;
  @Value("${spring.redis1.pool.max-wait}")
  private String maxWaitMillis1;
  @Value("${spring.redis1.password}")
  private String password1;

  @Bean
  public JedisPool jedisPool() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxIdle(Integer.parseInt(maxIdle));
    jedisPoolConfig.setMaxWaitMillis(Long.parseLong(maxWaitMillis));
    JedisPool jedisPool = new JedisPool(jedisPoolConfig, host,
        Integer.parseInt(port), Integer.parseInt(timeout), password);
    return jedisPool;
  }


  @Bean
  public ShardedJedisPool shardedJedisPool() {
    List<JedisShardInfo> shardList = new ArrayList<>();
    JedisShardInfo jedisShardInfo = new JedisShardInfo(host, Integer.parseInt(port), 0, "");
    if (!StringUtils.isEmpty(password)) {
      jedisShardInfo.setPassword(password);
    }
    jedisShardInfo.setConnectionTimeout(Integer.parseInt(timeout));
    shardList.add(jedisShardInfo);
    // JedisShardInfo jedisShardInfo1 = new JedisShardInfo(host1, Integer.parseInt(port1), 0, "");
    // if (!StringUtils.isEmpty(password1)) {
    //   jedisShardInfo1.setPassword(password1);
    // }
    // shardList.add(jedisShardInfo1);
    // jedisShardInfo1.setConnectionTimeout(Integer.parseInt(timeout1));
    return new ShardedJedisPool(new JedisPoolConfig(), shardList);
  }
}
