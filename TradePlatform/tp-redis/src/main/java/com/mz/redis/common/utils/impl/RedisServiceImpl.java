package com.mz.redis.common.utils.impl;

import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.SerializeUtil;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.SortingParams;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年3月18日 上午10:10:43
 */
@Service("redisService")
public class RedisServiceImpl implements RedisService {

  @Autowired
  ShardedJedisPool shardedJedisPool;

  @Autowired
  JedisPool jedisPool;

  public Long publish(String room, String users) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.publish(room, users);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0L;

  }

  @Override
  public <T> void setList(String key, List<T> list) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      shardedJedis.set(key.getBytes(), SerializeUtil.serialize(list));
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * 获取list
   *
   * @return list
   */
  @Override
  public <T> List<T> getList1(String key) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      try {
        byte[] in = shardedJedis.get(key.getBytes());
        List<T> list = (List<T>) SerializeUtil.unserialize(in);
        return list;
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;

    }
  }

  public String save(String key, String value) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      return shardedJedis.set(key, value);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  public void save(String key, String value, int second) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      shardedJedis.set(key, value);
      shardedJedis.expire(key, second);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String get(String key) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      return shardedJedis.get(key);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public Long delete(String key) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      return shardedJedis.del(key);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void setTime(String key, int second) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      shardedJedis.expire(key, second);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // -------------------- map操作 ------------------------------------

  /**
   * 以map形式保存在缓存中，传一个map。
   */
  public void saveMap(String key, Map<String, String> map) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      shardedJedis.hmset(key, map);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 直接获取一个map类型的key返回一个map集合。
   */
  public Map<String, String> getMap(String key) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      Map<String, String> map = shardedJedis.hgetAll(key);
      return map;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 可以传一个数组(数组里都是map里面的key),然后返回这些key里所对应的value。
   */
  public List<String> getMapKeys(String key, String[] str) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      List<String> list = shardedJedis.hmget(key, str);
      return list;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 保存一个单个键值对进到map里.如果mKey已经存在于map里将会覆盖之前的值。
   */
  public void saveMap(String key, String mKey, String value) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      shardedJedis.hset(key, mKey, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 通过键来获取值。
   */
  public String getMap(String key, String mKey) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      String ss = shardedJedis.hget(key, mKey);
      return ss;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 删除某个key 所对应的value。
   */
  public String delMapKey(String key, String mKey) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      String ss = shardedJedis.hget(key, mKey);
      return ss;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // 返回所有的map中所有的key

  /**
   * 返回所有的keys.只针对map类型的key.
   */
  public Set<String> findMapKeys(String key) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      Set<String> ss = shardedJedis.hkeys(key);
      return ss;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // 返回所有的map中所有的value

  /**
   * 返回所有的value.只正对map类型的key.
   */
  public List<String> findMapValue(String key) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      List<String> ss = shardedJedis.hvals(key);
      return ss;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // ------------ list操作 ----------------------------------

  /**
   * 保存一个值以list形式存储
   */
  public void saveList(String key, String lstValue) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      shardedJedis.lpush(key, lstValue);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // list之查询所有

  /**
   * 查询所有
   */
  public List<String> getList(String key) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      List<String> list = shardedJedis.lrange(key, 0, -1);
      return list;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 将这个传入两个索影将获取任意一段索影的值。(注意： 索影从0开始)
   */
  public List<String> getListByIndex(String key, int sta, int end) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      List<String> list = shardedJedis.lrange(key, sta, end);
      return list;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 排序方法 。第二个参数值是"ASC","DESC"。分别表示升序或者降序。传入其他参数返回null.
   */
  public List<String> sortList(String key, String sortName) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      SortingParams sortingParameters = new SortingParams();
      SortingParams alpha = sortingParameters.alpha();
      if (sortName.equals("DESC")) {
        SortingParams desc = alpha.desc();
        List<String> sortList = shardedJedis.sort(key, desc);
        return sortList;
      }
      if (sortName.equals("ASC")) {
        List<String> sort = shardedJedis.sort(key, alpha);
        return sort;
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // 修改某一个索影下的值 (索影从0开始算起)

  /**
   * 修改某个索影下的值。(索影从0开始)
   */
  public void updateList(String key, int index, String value) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      shardedJedis.lset(key, index, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 通过索影来获取list里面的值(索影从0开始)
   */
  public String getListIndex(String key, int index) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      String s = shardedJedis.lindex(key, index);
      return s;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // 删除list集合中某个特定的值 , 由于list是可重复的所以需要下面的方法来批量删除

  /**
   * 删除list集合中某个特定的值 .
   */
  public void delListValue(String key, String value) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      shardedJedis.lrem(key, 1, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 删除一定数目的value 值

  /**
   * 删除一定数目指定的值。
   */
  public void delListValueNum(String key, int count, String value) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      shardedJedis.lrem(key, count, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // ---------------- set方法的使用 ---------------------------------------

  /**
   * 保存一个key以set形式保存
   */
  public void saveSet(String key, String value) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      shardedJedis.sadd(key, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取指定key下set的所有元素。
   */
  public Set<String> getSet(String key) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      Set<String> s = shardedJedis.smembers(key);
      return s;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // 删除制定值

  /**
   * 删除指定的值
   */
  public void delSet(String key, String value) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      shardedJedis.srem(key, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 判断set中有没有这个值

  /**
   * 判断一个key 下有没有这个值。
   */
  public Boolean ynSetValue(String key, String value) {
    Boolean b = false;
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      b = shardedJedis.sismember(key, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return b;
  }

  // ------------------- SoretedSet方法的使用 --------------------------
  // SoretedSet 需要传一个Double型的参数 作为索影 ，redis并且会根据这个参数的大小来排序。

  /**
   * SoretedSet 是set与list的结合。中间的Double值可以看做是动态的索引。而且redis将会自动按照按照升序排序。
   */
  public void SaveSoretSet(String key, Double dob, String member) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      shardedJedis.zadd(key, dob, member);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取所有的值
   */
  public Set<String> getSoretSet(String key) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      Set<String> set = shardedJedis.zrange(key, 0, -1);
      return set;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // 删除指定key的value

  /**
   * 删除某个值
   */
  public void delSoretSet(String key, String value) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      shardedJedis.zrem(key, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 删除从最小索影到最大索影的值

  /**
   * 删除某个区间内的值
   */
  public void delSoretSetforIndex(String key, Double min, Double max) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      shardedJedis.zcount(key, min, max);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 返回当前值的索影

  /**
   * 返回当前的索影
   */
  public Double getSortSetIndex(String key, String value) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      Double dob = shardedJedis.zscore(key, value);
      return dob;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 999.9;
  }

  // 返回在索影范围内的所有值

  /**
   * 返回一个指定Score范围之内的所有值
   */
  public Set<String> getSortSetIndex(String key, Double min, Double max) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      Set<String> set = shardedJedis.zrangeByScore(key, min, max);
      return set;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // ----------------------- 工具方法 -----------------------------------

  /**
   * @author: Wu Shuiming
   * @Date : 日期：2016年3月9日 时间 ：下午5:58:03
   */
  public Long getKeyTime(String key) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      Long l = shardedJedis.ttl(key);
      return l;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @author: Wu Shuiming
   * @Date : 日期：2016年3月9日 时间 ：下午5:57:56
   */
  public String saveStringAndTime(String key, int seconds, String value) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      String setex = shardedJedis.setex(key, seconds, value);
      if (setex.equals("OK")) {
        return setex;
      } else {
        return "NO";
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @author: Wu Shuiming
   * @Date : 日期：2016年3月9日 时间 ：下午5:57:50
   */
  public Boolean ynKeys(String key) {
    Boolean b = false;
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      b = shardedJedis.exists(key);
      return b;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return b;
  }

  /**
   * 此方法用于给某个key加锁。适合在分布式负载情况下给 key加锁 ，实现跨虚拟机的的锁机制。
   * <p>
   * 一个锁只能存活30秒, 并且有效期是两秒。所以在调用这个方法的时候必须手动的释放锁。下面的方法是解锁。
   */
  public boolean lock(String key) {
    boolean lockSuccess = false;
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      // long start = new Date().getTime();
      String lockKey = "lock_" + key;
      long result = shardedJedis
          .setnx(lockKey, String.valueOf(new Date().getTime() + 2 * 1000 + 1));
      if (result == 1) {
        // 设置锁的存活时间为30秒，
        shardedJedis.expire(lockKey, 30);
        lockSuccess = true;
        return lockSuccess;
      } else {
        String lockTimeStr = shardedJedis.get(lockKey);
        if (StringUtils.isNumeric(lockTimeStr)) {
          // 如果key存在，锁存在
          long lockTime = Long.valueOf(lockTimeStr);
          if (lockTime < new Date().getTime()) {
            // 锁已过期
            String originStr = shardedJedis.getSet(lockKey,
                String.valueOf(new Date().getTime() + 2 * 1000 + 1));
            shardedJedis.expire(lockKey, 3);
            if (StringUtils.isNoneBlank(originStr) && originStr.equals(lockTimeStr)) {
              // 表明锁由该线程获得
              lockSuccess = true;
              return lockSuccess;
            }
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return lockSuccess;
  }

  /**
   * 释放锁
   */
  public void unLock(String key) {
    try (ShardedJedis shardedJedis = shardedJedisPool.getResource()) {
      String lockKey = "lock_" + key;
      shardedJedis.del(lockKey);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /***
   *
   * @param patt
   * @return
   */
  @Override
  public Set<String> keys(String patt) {
    try (Jedis jedis = jedisPool.getResource()) {
      Set<String> keys = jedis.keys("*" + patt + "*");
			    /*Iterator<String> iterator = keys.iterator();
				while (iterator.hasNext()) {
					jedis.del(iterator.next());
				}
				*/
      return keys;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Set<String> delkeys(String patt) {
    try (Jedis jedis = jedisPool.getResource()) {
      Set<String> keys = jedis.keys("*" + patt + "*");
      Iterator<String> iterator = keys.iterator();
      while (iterator.hasNext()) {
        jedis.del(iterator.next());
      }

      return keys;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Set<String> noPerkeys(String patt) {
    try (Jedis jedis = jedisPool.getResource()) {
      Set<String> keys = jedis.keys(patt + "*");
			/*	Iterator<String> iterator = keys.iterator();
				while (iterator.hasNext()) {
					jedis.del(iterator.next());
				}
				*/
      return keys;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
