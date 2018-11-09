package com.mz.utils;

import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * <p> TODO</p>
 *
 * @author: shangxl
 * @Date :          2018年3月13日 下午7:38:27
 */
public class Properties {

  public static final String BASEBTC = "basebtc";

  public static final String COLDADDERSS = "_coldAddress";
  /**
   * coins.properties配置文件读取
   */
  private static Map<String, String> Appcoin = null;
  /**
   * app.properties配置文件读取
   */
  private static Map<String, String> App = null;

  private static String[] ListCoinBasedBtc = null;


  public static synchronized Map<String, String> appcoinMap() {
    if (Appcoin == null) {
      Appcoin = PropertiesUtil.printAll("coinConfig/coins.properties");
      // 以太坊代币处理
      if (!Appcoin.isEmpty()) {
        Map<String, String> map2 = new HashMap<>(200);
        map2.putAll(Appcoin);
        Set<Entry<String, String>> entrys = map2.entrySet();
        for (Entry<String, String> entry : entrys) {
          String key = entry.getKey().toString();
          String value = entry.getValue().toString();
          if (key.startsWith(CommonUtil.HEXPREFIX)) {
            Appcoin.put(value, key);
          }
        }
      }
    }
    return Appcoin;
  }

  public static synchronized Map<String, String> appMap() {
    if (App == null) {
      App = PropertiesUtil.printAll("app.properties");
    }
    return App;
  }

  /**
   * 获取基于比特币的币种
   * <p>
   * TODO
   * </p>
   *
   * @author: shangxl
   * @param: @return
   * @return: List<String>
   * @Date : 2018年1月26日 上午10:08:39
   * @throws:
   */
  public static String[] listCoinBasedBtc() {
    if (ListCoinBasedBtc == null || ListCoinBasedBtc.length == 0) {
      Map<String, String> map = appcoinMap();
      if (map != null) {
        String arrcoinstr = map.get(BASEBTC).toString();
        if (StringUtils.isNotEmpty(arrcoinstr)) {
          int subnum = 1;
          arrcoinstr = arrcoinstr.substring(subnum, arrcoinstr.length() - subnum);
          String[] arrcoin = arrcoinstr.split(":");
          if (arrcoin != null && arrcoin.length != 0) {
            for (int i = 0; i < arrcoin.length; i++) {
              arrcoin[i] = arrcoin[i].toUpperCase();
            }
            ListCoinBasedBtc = arrcoin;
          }
        } else {
          LogFactory.info("basebtc not configure,please check coins.properties");
        }
      } else {
        LogFactory.info("AppCoin is null");
      }
    }
    return ListCoinBasedBtc;
  }
}
