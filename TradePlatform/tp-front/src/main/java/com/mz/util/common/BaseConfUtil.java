package com.mz.util.common;

import com.alibaba.fastjson.JSON;
import com.mz.core.mvc.model.AppConfig;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.sys.SpringContextUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;

public class BaseConfUtil {

  // 查询查询系统基础配置
  public static Map<String, Object> getConfigByKey(String key) {
    Map<String, Object> map = new HashMap<String, Object>();
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String text = redisService.get(key);
    if (!StringUtils.isEmpty(text)) {
      List<AppConfig> list = JSON.parseArray(text, AppConfig.class);
      for (AppConfig l : list) {
        map.put(l.getConfigkey(), l.getValue());
      }
    }
    return map;
  }
}
