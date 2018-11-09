/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年10月29日 下午2:35:41
 */
package com.mz.oauth.oauth.core;

import com.alibaba.dubbo.rpc.RpcContext;
import com.mz.oauth.remote.core.PermissionContext;
import com.mz.shiro.RedisSessionDAO;
import com.mz.shiro.authorization.service.AuthorizationService;
import com.mz.util.SerializableUtils;
import com.mz.util.sys.SpringContextUtil;
import com.mz.oauth.user.model.AppUser;
import java.io.Serializable;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p> TODO</p>   session 权限资源接口   远程调用访问权限认证系统service
 *
 * @author: Liu Shilei
 * @Date :          2015年10月29日 下午2:35:41
 */
@Service
public class RemoteOauthServiceImpl implements RemoteOauthService {

  @Autowired
  private AuthorizationService authorizationService;

  @Override
  public String getSession(String appKey, Serializable sessionId) {
    try {
      RedisSessionDAO redisSessionDAO = SpringContextUtil.getBean(RedisSessionDAO.class);
      Session readSession = redisSessionDAO.readSession(sessionId);
      return SerializableUtils.serialize(readSession);
    } catch (UnknownSessionException e) {
      return null;
    }
  }

  @Override
  public Serializable createSession(Session session) {
    RedisSessionDAO redisSessionDAO = SpringContextUtil.getBean(RedisSessionDAO.class);
    return redisSessionDAO.create(session);
  }

  @Override
  public void updateSession(String appKey, Session session) {
    RedisSessionDAO redisSessionDAO = SpringContextUtil.getBean(RedisSessionDAO.class);
    redisSessionDAO.update(session);
  }

  @Override
  public void deleteSession(String appKey, Session session) {
    RedisSessionDAO redisSessionDAO = SpringContextUtil.getBean(RedisSessionDAO.class);
    redisSessionDAO.delete(session);
  }

  @Override
  public PermissionContext getPermissions(String saasId, String appKey, AppUser user) {
    PermissionContext permissionContext = new PermissionContext();
    permissionContext.setRoles(authorizationService.findRoles(saasId, appKey, user));
    permissionContext.setPermissions(authorizationService.findPermissions(saasId, appKey, user));
    return permissionContext;
  }

  @Override
  public String getSaasId() {
    String saasId = RpcContext.getContext().getAttachment("saasId");

    System.out.println("提供者：SAASID:" + saasId);
    System.out.println("aaa");
    return "hello";
  }

  @Override
  public String getSessions(String appKey, Serializable sessionId) {
    RedisSessionDAO redisSessionDAO = SpringContextUtil.getBean(RedisSessionDAO.class);
    Session readSession = redisSessionDAO.readSession(sessionId);
    return SerializableUtils.serialize(readSession);
  }
}
