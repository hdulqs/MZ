/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年4月11日 下午4:42:00
 */
package com.mz.redis.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nutz.lang.Mirror;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Liu Shilei
 * @Date : 2016年10月27日 上午10:53:39
 */
@Component
public class RedisUtil<T> implements ApplicationContextAware {


  private static ApplicationContext applicationContext;

  private static JedisPool jedisPool;

  private static volatile Jedis jedis;

  private Class<T> clazz;

  private String clazzName;

  private final String DB = "RedisDB:";

  private RuntimeSchema<T> schema;

  public RedisUtil() {

  }

  public RedisUtil(Class<T> clazz) {
    this.clazz = clazz;
    this.clazzName = DB + this.clazz.getName().replace(".", ":");
    this.schema = RuntimeSchema.createFrom(clazz);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    if (RedisUtil.applicationContext == null) {
      RedisUtil.applicationContext = applicationContext;
    }
  }

  public static JedisPool getJedisPool() {
    if (jedisPool == null) {
      synchronized (JedisPool.class) {
        if (jedisPool == null) {
          try {
            jedisPool = applicationContext.getBean(JedisPool.class);
          } catch (BeansException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return jedisPool;
  }

  public void hmset(String key, Map<String, String> map) {
    try (Jedis jedis = getJedisPool().getResource()) {
      jedis.hmset(key, map);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 获得一个对象
   * <p>
   * TODO
   * </p>
   *
   * @author: Liu Shilei
   * @param: @param id
   * @param: @return
   * @return: T
   * @Date : 2016年10月27日 上午11:21:49
   * @throws:
   */
  public T get(String id) {
    T t = null;
    try (Jedis jedis = getJedisPool().getResource()) {
      String key = this.clazzName + ":" + id;
      byte[] bytes = jedis.get(key.getBytes());
      if (bytes != null) {
        t = clazz.newInstance();
        ProtostuffIOUtil.mergeFrom(bytes, t, schema);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return t;
  }

  /**
   * 存入一个对象
   * <p>
   * TODO
   * </p>
   *
   * @author: Liu Shilei
   * @param: @param t
   * @param: @param id
   * @return: void
   * @Date : 2016年10月27日 上午11:14:10
   * @throws:
   */
  public void put(T t, String id) {
    String key;
    if (id == null) {
      Mirror<?> mirror = Mirror.me(t.getClass());
      //如果创建时间存在就不能再次修改
      Object obj = mirror.getValue(t, "id");
      key = this.clazzName + ":" + obj;
    } else {
      key = this.clazzName + ":" + id;
    }
    try (Jedis jedis = getJedisPool().getResource()) {
      jedis.del(key.getBytes());
      byte[] bytes = ProtostuffIOUtil
          .toByteArray(t, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
      jedis.set(key.getBytes(), bytes);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * <p>
   * 返回同类的所有对象List<T> list
   * </p>
   *
   * @author: Liu Shilei
   * @param:
   * @return: void
   * @Date : 2016年10月27日 上午11:20:00
   * @throws:
   */
  public List<T> list() {
    List<T> list = new ArrayList<T>();
    try (Jedis jedis = getJedisPool().getResource()) {
      Set<String> keys = jedis.keys(this.clazzName + "*");
      Iterator<String> iterator = keys.iterator();
      while (iterator.hasNext()) {
        byte[] bytes = jedis.get(iterator.next().getBytes());
        if (bytes != null) {
          T t = clazz.newInstance();
          ProtostuffIOUtil.mergeFrom(bytes, t, schema);
          list.add(t);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  /**
   * <p>
   * 清空同类的所有对象
   * </p>
   *
   * @author: Liu Shilei
   * @param:
   * @return: void
   * @Date : 2016年10月27日 上午11:20:00
   * @throws:
   */
  public void clear() {
    try (Jedis jedis = getJedisPool().getResource()) {
      Set<String> keys = jedis.keys(this.clazzName + "*");
      Iterator<String> iterator = keys.iterator();
      while (iterator.hasNext()) {
        jedis.del(iterator.next());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void delete(String id) {
    try (Jedis jedis = getJedisPool().getResource()) {
      String key = this.clazzName + ":" + id;
      Set<String> keys = jedis.keys(key);
      Iterator<String> iterator = keys.iterator();
      while (iterator.hasNext()) {
        jedis.del(iterator.next());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
