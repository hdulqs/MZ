package com.mz.shiro;

import com.mz.redis.common.utils.RedisService;
import com.mz.util.SerializableUtils;
import com.mz.util.log.LogFactory;
import java.io.Serializable;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * session 持久化
 *
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2015年10月11日 下午6:41:02
 */
@Component
public class RedisSessionDAO extends CachingSessionDAO {

  @Autowired
  private RedisService redisService;

  @Override
  protected Serializable doCreate(Session session) {
    Serializable sessionId = generateSessionId(session);
    assignSessionId(session, sessionId);
    try {
      redisService
          .save("session:" + sessionId.toString(), SerializableUtils.serialize(session), 1800);
    } catch (Exception e) {
      LogFactory.info(
          "-------------------------------redis 异常  创建session--------------------------------------------");
    }
    return session.getId();
  }

  @Override
  protected void doUpdate(Session session) {
    if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
      return; //如果会话过期/停止 没必要再更新了
    }

    if (StringUtils.isEmpty(session.getId())) {
      return;
    }
    try {
      //开
      redisService
          .save("session:" + session.getId().toString(), SerializableUtils.serialize(session),
              1800);
      //关
    } catch (Exception e) {
      LogFactory.info(
          "-------------------------------redis 异常  更新session--------------------------------------------");
      e.printStackTrace();
    }
  }

  @Override
  protected void doDelete(Session session) {
    try {
      redisService.delete("session:" + session.getId().toString());
    } catch (Exception e) {
      LogFactory.info(
          "-------------------------------redis 异常  删除session--------------------------------------------");
    }
  }

  @Override
  protected Session doReadSession(Serializable sessionId) {
    String session = "";
    try {
      session = redisService.get("session:" + sessionId.toString());
      if (StringUtils.isEmpty(session)) {
        return null;
      } else {
        redisService.save("session:" + sessionId.toString(), session, 1800);
      }
    } catch (Exception e) {
      LogFactory.info(
          "-------------------------------redis 异常 读session--------------------------------------------");
    }
    return SerializableUtils.deserialize(session);
  }

}
