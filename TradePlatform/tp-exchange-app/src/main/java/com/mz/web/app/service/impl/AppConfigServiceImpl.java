/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Gao Mimi
 * @version: V1.0
 * @Date: 2015年10月10日  18:41:55
 */
package com.mz.web.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.AppConfig;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.QueryFilter;
import com.mz.web.cache.CacheManageCallBack;
import com.mz.web.cache.CacheManageService;
import com.mz.core.constant.StringConstant;
import com.mz.web.app.dao.AppConfigDao;
import com.mz.web.app.service.AppConfigService;
import com.mz.web.article.dao.AppBannerDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author: Gao Mimi
 * @Date :    2015年10月10日  18:41:55     
 */
@Service("appConfigService")
public class AppConfigServiceImpl extends BaseServiceImpl<AppConfig, Long> implements
    AppConfigService, CacheManageService {

  @Autowired
  private RedisService redisService;

  @Resource(name = "appBannerDao")
  private AppBannerDao appBannerDao;

  @Resource(name = "appConfigDao")
  @Override
  public void setDao(BaseDao<AppConfig, Long> dao) {
    super.dao = dao;
  }

  @Override
  public String getBykey(String key) {
    QueryFilter filter = new QueryFilter(AppConfig.class);
    if (key != null) {
      filter.addFilter("configkey=", key);
    }

    List<AppConfig> list = super.dao.selectByExample(filter.setNosaas().getExample());
    return list == null ? null : list.get(0).getValue();
  }


  @Override
  public List<AppConfig> findKey() {

    return ((AppConfigDao) dao).findKey();
  }


  @Override
  public void initCache(CacheManageCallBack cacheManageCallBack) {
    List<AppConfig> typeList = ((AppConfigDao) dao).findKey();

    for (AppConfig config : typeList) {
      Map<String, String> map = new HashMap<String, String>();
      map.put("typekey", config.getTypekey());
      List<AppConfig> list = ((AppConfigDao) dao).getConfig(map);

      String data = JSON.toJSONString(list);
      redisService.save(StringConstant.CONFIG_CACHE + ":" + config.getTypekey(), data);
    }
    List<AppConfig> list = ((AppConfigDao) dao).getConfig(null);
    Map<String, String> map = new HashMap<String, String>();
    for (AppConfig app : list) {
      if (null != app.getConfigkey() && !"".equals(app.getConfigkey())) {
        map.put(app.getConfigkey(), app.getValue());
      }
    }
    String data = JSON.toJSONString(map);
    redisService.save(StringConstant.CONFIG_CACHE + ":all", data);
    cacheManageCallBack
        .callback(AppConfigService.class, StringConstant.CONFIG_CACHE + ":all", "系统配置信息缓存");

  }

  @Override
  public List<AppConfig> getConfig(Map<String, String> map) {
    List<AppConfig> list = ((AppConfigDao) dao).getConfig(map);
    return list;
  }

  @Override
  public List<AppConfig> findLendKey() {
    return ((AppConfigDao) dao).findLendKey();
  }

  @Override
  public void setBykeyToDB(String key, String value) {
    QueryFilter filter = new QueryFilter(AppConfig.class);
    if (key != null) {
      filter.addFilter("configkey=", key);
    }
    AppConfig appConfig = this.get(filter);
    if (null != appConfig) {
      appConfig.setValue(value);
      this.update(appConfig);
    }
  }


  /**
   * 0 开启，1 不开启
   */
  @Override
  public String getBykeyfromDB(String key) {
    QueryFilter filter = new QueryFilter(AppConfig.class);
    if (key != null) {
      filter.addFilter("configkey=", key);
    }
    AppConfig appConfig = this.get(filter);
    if (null != appConfig) {
      return appConfig.getValue();
    }
    return null;
  }


}