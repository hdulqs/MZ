package com.mz.redis.common.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface RedisService {

  /**
   * 发布消息
   */
  public Long publish(String room, String users);

  // 给某个key加锁
  public boolean lock(String key);

  // 释放锁
  public void unLock(String key);

  /**
   * 根据key 获得一个value值
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @param key
   * @param: @return
   * @return: String
   * @Date :          2016年4月11日 下午6:28:54
   * @throws:
   */
  public String get(String key);

  /**
   * 根据key保存和更新value
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @param key
   * @param: @param value
   * @param: @return
   * @return: String
   * @Date :          2016年4月11日 下午6:29:16
   * @throws:
   */
  public String save(String key, String value);

  /**
   * 根据key保存和更新value，并设置一段时间后自动删除
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @param key
   * @param: @param value
   * @param: @param second   秒
   * @param: @return
   * @return: String
   * @Date :          2016年4月11日 下午6:36:23
   * @throws:
   */
  public void save(String key, String value, int second);

  /**
   * 根据key删除
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @param key
   * @param: @return
   * @return: Long
   * @Date :          2016年4月11日 下午6:29:42
   * @throws:
   */
  public Long delete(String key);

  /**
   * 根据key设置一条记录的过期时间 定时多少秒之后自动删除
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @param second  秒
   * @return: void
   * @Date :          2016年4月11日 下午6:31:14
   * @throws:
   */
  public void setTime(String key, int second);

  // -------------------- map操作 ------------------------------------

  /**
   * 以map形式保存在缓存中，传一个map。
   */
  public void saveMap(String key, Map<String, String> map);

  /**
   * 直接获取一个map类型的key返回一个map集合。
   */
  public Map<String, String> getMap(String key);

  /**
   * 可以传一个数组(数组里都是map里面的key),然后返回这些key里所对应的value。
   */
  public List<String> getMapKeys(String key, String[] str);

  /**
   * 保存一个单个键值对进到map里.如果mKey已经存在于map里将会覆盖之前的值。
   */
  public void saveMap(String key, String mKey, String value);

  /**
   * 通过键来获取值。
   */
  public String getMap(String key, String mKey);

  /**
   * 删除某个key 所对应的value。
   */
  public String delMapKey(String key, String mKey);

  // 返回所有的map中所有的key

  /**
   * 返回所有的keys.只针对map类型的key.
   */
  public Set<String> findMapKeys(String key);

  // 返回所有的map中所有的value

  /**
   * 返回所有的value.只正对map类型的key.
   */
  public List<String> findMapValue(String key);

  // ------------ list操作 ----------------------------------

  /**
   * 保存一个值以list形式存储
   */
  public void saveList(String key, String lstValue);

  // list之查询所有

  /**
   * 查询所有
   */
  public List<String> getList(String key);

  /**
   * 将这个传入两个索影将获取任意一段索影的值。(注意： 索影从0开始)
   */
  public List<String> getListByIndex(String key, int sta, int end);

  /**
   * 排序方法 。第二个参数值是"ASC","DESC"。分别表示升序或者降序。传入其他参数返回null.
   */
  public List<String> sortList(String key, String sortName);

  // 修改某一个索影下的值 (索影从0开始算起)

  /**
   * 修改某个索影下的值。(索影从0开始)
   */
  public void updateList(String key, int index, String value);

  /**
   * 通过索影来获取list里面的值(索影从0开始)
   */
  public String getListIndex(String key, int index);

  // 删除list集合中某个特定的值 , 由于list是可重复的所以需要下面的方法来批量删除

  /**
   * 删除list集合中某个特定的值 .
   */
  public void delListValue(String key, String value);

  // 删除一定数目的value 值

  /**
   * 删除一定数目指定的值。
   */
  public void delListValueNum(String key, int count, String value);

  // ---------------- set方法的使用 ---------------------------------------

  /**
   * 保存一个key以set形式保存
   */
  public void saveSet(String key, String value);

  /**
   * 获取指定key下set的所有元素。
   */
  public Set<String> getSet(String key);

  // 删除制定值

  /**
   * 删除指定的值
   */
  public void delSet(String key, String value);

  // 判断set中有没有这个值

  /**
   * 判断一个key 下有没有这个值。
   */
  public Boolean ynSetValue(String key, String value);

  // ------------------- SoretedSet方法的使用 --------------------------
  // SoretedSet 需要传一个Double型的参数 作为索影 ，redis并且会根据这个参数的大小来排序。

  /**
   * SoretedSet 是set与list的结合。中间的Double值可以看做是动态的索引。而且redis将会自动按照按照升序排序。
   */
  public void SaveSoretSet(String key, Double dob, String member);

  /**
   * 获取所有的值
   */
  public Set<String> getSoretSet(String key);

  // 删除指定key的value

  /**
   * 删除某个值
   */
  public void delSoretSet(String key, String value);

  // 删除从最小索影到最大索影的值

  /**
   * 删除某个区间内的值
   */
  public void delSoretSetforIndex(String key, Double min, Double max);

  // 返回当前值的索影

  /**
   * 返回当前的索影
   */
  public Double getSortSetIndex(String key, String value);

  // 返回在索影范围内的所有值

  /**
   * 返回一个指定Score范围之内的所有值
   */
  public Set<String> getSortSetIndex(String key, Double min, Double max);

  // ----------------------- 工具方法 -----------------------------------

  /**
   * @author: Wu Shuiming
   * @Date :  日期：2016年3月9日       时间 ：下午5:58:03
   */
  public Long getKeyTime(String key);

  /**
   * @author: Wu Shuiming
   * @Date :  日期：2016年3月9日       时间 ：下午5:57:56
   */
  public String saveStringAndTime(String key, int seconds, String value);

  /**
   * @author: Wu Shuiming
   * @Date :  日期：2016年3月9日       时间 ：下午5:57:50
   */
  public Boolean ynKeys(String key);

  public <T> List<T> getList1(String key);

  public <T> void setList(String key, List<T> list);

  public Set<String> keys(String patt);

  public Set<String> delkeys(String patt);

  public Set<String> noPerkeys(String patt);

}
