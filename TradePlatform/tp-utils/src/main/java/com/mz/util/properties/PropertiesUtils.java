/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年11月4日 下午1:22:49
 */
package com.mz.util.properties;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

  public final static String SSOKEY = "app.sso";

  public final static String LOADWEBKEY = "app.loadweb";

  static {
    APP = new Properties();
    try {
      InputStream inputStream = new FileInputStream(
          PropertiesUtils.class.getClassLoader().getResource(
              ("app.properties")).getPath());
      APP.load(inputStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
    MULTIPLTEMAIL = new Properties();
    try {
      InputStream inputStream = new FileInputStream(
          PropertiesUtils.class.getClassLoader().getResource(
              ("multipleEmail.properties")).getPath());
      MULTIPLTEMAIL.load(inputStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
    SHIRO_PROPERTIES = new Properties();
    try {
      URL url = PropertiesUtils.class.getClassLoader().getResource(
          ("shiro-client.properties"));
      if (url != null) {
        InputStream inputStream = new FileInputStream(
            url.getPath());
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
}
