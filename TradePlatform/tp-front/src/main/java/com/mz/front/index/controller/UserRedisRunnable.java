package com.mz.front.index.controller;

import com.mz.manage.remote.RemoteManageService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.util.sys.SpringContextUtil;
import com.mz.front.redis.model.UserRedis;
import com.mz.trade.redis.model.EntrustTrade;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * 登录后更新用户账户id保存到redis中
 *
 * @author CHINA_LSL
 */
@Component
public class UserRedisRunnable {

  private final Logger log = Logger.getLogger(UserRedisRunnable.class);

  public void process(String id) {
    UserRedis userRedis = new UserRedis();
    userRedis.setId(id);

    RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
        .getBean("remoteManageService");
    Map<String, Long> map = remoteManageService.findAllAccountId(id);
    userRedis.setAccountId(map.get("accountId") == null ? null : map.get("accountId"));
    //获取完后，移除
    map.remove("accountId");
    userRedis.setDmAccountId(map);

    log.info("初始化账户id到redis------------->>>");
    RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
    redisUtil.put(userRedis, userRedis.getId());
    //缓存委托历史记录和当前记录
    RedisUtil<com.mz.trade.redis.model.EntrustByUser> redisUtilEntrustByUser = new RedisUtil<com.mz.trade.redis.model.EntrustByUser>(
        com.mz.trade.redis.model.EntrustByUser.class);
    List<Map<String, List<EntrustTrade>>> listml = remoteManageService
        .findExEntrustBycust(Long.valueOf(id));
    com.mz.trade.redis.model.EntrustByUser ebu = new com.mz.trade.redis.model.EntrustByUser();
    ebu.setCustomerId(Long.valueOf(id));
    ebu.setEntrustedmap(listml.get(0));
    ebu.setEntrustingmap(listml.get(1));
    redisUtilEntrustByUser.put(ebu, id);
  }

}
