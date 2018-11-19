/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年11月4日 下午1:22:49
 */
package com.mz.util.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Liu Shilei
 * @Date : 2015年11月4日 下午1:22:49
 */
public class PropertiesUtils {

  public static Properties APP;
  public static Properties RMI;
  public static Properties SHIRO_PROPERTIES;
  public static Properties MULTIPLTEMAIL;
  public static Properties COIN;

  public final static String SSOKEY = "app.sso";

  public final static String LOADWEBKEY = "app.loadweb";

  static {
    APP = new Properties();
    try {
      InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(
          ("app.properties"));
      if (inputStream != null) {
        APP.load(inputStream);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    MULTIPLTEMAIL = new Properties();
    try {
      InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(
          ("multipleEmail.properties"));
      if (inputStream != null) {
        MULTIPLTEMAIL.load(inputStream);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    COIN = new Properties();
    try {
      InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(
          ("coinConfig/coins.properties"));
      if (inputStream != null) {
        COIN.load(inputStream);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    SHIRO_PROPERTIES = new Properties();
    try {
      InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(
          ("shiro-client.properties"));
      if (inputStream != null) {
        SHIRO_PROPERTIES.load(inputStream);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * front_oauth 获得sso同步地址
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @return
   * @return: ArrayList<String>
   * @Date :          2016年7月5日 上午11:15:57
   * @throws:
   */
  public static ArrayList<String> getAppSSO() {

    ArrayList<String> arrayList = new ArrayList<String>();
    Set<Object> keySet = APP.keySet();
    Iterator<Object> iterator = keySet.iterator();
    while (iterator.hasNext()) {
      String key = (String) iterator.next();
      if (key.contains(SSOKEY)) {
        arrayList.add(APP.getProperty(key));
      }
    }
    return arrayList;
  }


  /**
   * 获得加载了多少个站点
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @return
   * @return: ArrayList<String>
   * @Date :          2016年7月5日 上午11:15:57
   * @throws:
   */
  public static Map<String, String> getLoadWeb() {

    Map<String, String> map = new HashMap<String, String>();
    Set<Object> keySet = APP.keySet();
    Iterator<Object> iterator = keySet.iterator();
    while (iterator.hasNext()) {
      String key = (String) iterator.next();
      if (key.contains(LOADWEBKEY)) {
        String[] split = APP.getProperty(key).split("=");
        map.put(split[0], split[1]);
      }
    }
    return map;
  }

  /**
   * 遍历properties
   * <p> TODO</p>
   *
   * @author: Shangxl
   * @param: @param url
   * @param: @return
   * @return: Map<String   ,   String>
   * @Date :          2017年9月14日 上午11:29:07
   * @throws:
   */
  public static Map<String, String> printAll(String url) {
    Properties prop = new Properties();
    Map<String, String> map = null;
    InputStream input = null;
    try {
      input = new FileInputStream(
          PropertiesUtils.class.getClassLoader().getResource(url).getPath());
      if (input == null) {
        System.out.println("Sorry, unable to find " + url);
      }
      prop.load(input);
      Set<Entry<Object, Object>> entrys = prop.entrySet();// 返回的属性键值对实体
      map = new HashMap<String, String>();
      for (Entry<Object, Object> entry : entrys) {
        String key = entry.getKey().toString();
        String value = entry.getValue().toString();
        map.put(key, value);
      }
      return map;
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return map;
  }
}
