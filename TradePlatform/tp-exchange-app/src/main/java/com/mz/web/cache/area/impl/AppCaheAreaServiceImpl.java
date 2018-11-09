/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Zhang Xiaofang
 * @version: V1.0
 * @Date: 2016年8月1日 下午8:58:57
 */
package com.mz.web.cache.area.impl;

import com.alibaba.fastjson.JSON;
import com.mz.core.constant.StringConstant;
import com.mz.redis.common.utils.RedisService;
import com.mz.web.cache.CacheManageCallBack;
import com.mz.web.cache.CacheManageService;
import com.mz.web.dictionary.model.AppDicMultilevel;
import com.mz.web.dictionary.service.AppDicMultilevelService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;


/**
 * <p> TODO</p>
 * @author: Zhang Xiaofang
 * @Date :          2016年8月1日 下午8:58:57 
 */

public class AppCaheAreaServiceImpl implements CacheManageService {

  @Resource(name = "redisService")
  private RedisService redisService;

  @Resource(name = "appDicMultilevelService")
  private AppDicMultilevelService appDicMultilevelService;

  @Override
  public void initCache(CacheManageCallBack callback) {

    List<Map> l = new ArrayList<Map>();
    List<AppDicMultilevel> list = appDicMultilevelService.findListBypDicKey("area");
    for (AppDicMultilevel pdic : list) {
      Map<String, Object> pMap = new HashMap<String, Object>();
      pMap.put("key", pdic.getDicKey());
      pMap.put("province", pdic.getItemName());
      List<AppDicMultilevel> cityList = appDicMultilevelService.findListBypDicKey(pdic.getDicKey());

      List<Map> cl = new ArrayList<Map>();
      if (cityList.size() == 0) {
        pMap.put("cities", null);
      } else {
        for (AppDicMultilevel dic : cityList) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("city", dic.getItemName());
          cl.add(map);
        }
        pMap.put("cities", cl);
      }

      l.add(pMap);
    }

    String data = JSON.toJSONString(l);
    redisService.save(StringConstant.AREA_CACHE, data);
    callback.callback(AppCaheAreaServiceImpl.class, StringConstant.AREA_CACHE, "地区信息缓存");

    System.out.println("=====" + JSON.toJSONString(l));
    ;

  }

}
