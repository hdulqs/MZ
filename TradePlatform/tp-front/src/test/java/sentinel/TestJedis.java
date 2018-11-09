package sentinel;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

public class TestJedis {
	public static void main(String[] args) {
		//Jedis jedis = new Jedis("172.16.10.128", 26379);
		
		// 使用HashSet添加多个sentinel
		Set<String> sentinels = new HashSet<String>();
		// 添加sentinel主机和端口
		sentinels.add("172.16.10.128:26379");
		
		// 创建config  
        JedisPoolConfig poolConfig = new JedisPoolConfig();  
        // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。  
        poolConfig.setMaxIdle(10);  
        // 控制一个pool最多有多少个jedis实例。  
        poolConfig.setMaxTotal(100);  
        // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；  
        poolConfig.setMaxWaitMillis(2000);  
        // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；  
        poolConfig.setTestOnBorrow(true);  
		
		JedisSentinelPool redisSentinelJedisPool = new JedisSentinelPool("mymaster", sentinels,poolConfig);
		
		HostAndPort currentHostMaster = redisSentinelJedisPool.getCurrentHostMaster();  
        System.out.println(currentHostMaster.getHost() + "--"+ currentHostMaster.getPort());
        
		Jedis jedis = null;
         try {
             jedis = redisSentinelJedisPool.getResource();
             System.out.println(jedis.get("k1"));
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             redisSentinelJedisPool.returnBrokenResource(jedis);
         }
 
         redisSentinelJedisPool.close();
	}
}
