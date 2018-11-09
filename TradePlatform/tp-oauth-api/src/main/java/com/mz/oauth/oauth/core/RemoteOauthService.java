/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月29日 下午1:23:41
 */
package com.mz.oauth.oauth.core;

import com.alibaba.dubbo.config.annotation.Service;
import com.mz.oauth.remote.core.PermissionContext;

import com.mz.oauth.user.model.AppUser;
import java.io.Serializable;

import org.apache.shiro.session.Session;

/**
 * <p> TODO</p>   session 权限资源接口   远程调用访问权限认证系统service
 * @author:         Liu Shilei 
 * @Date :          2015年10月29日 下午1:23:41 
 */
@Service
public interface RemoteOauthService {
	/**
	 * 获得session  序列化后字符串    
	 * 		由于使用dubbo  RPC协议传输Session为接口必须序列化为二进制数据，之前采用的httpinvoker走的http协议不适用了
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param appKey
	 * @param:    @param sessionId
	 * @param:    @return
	 * @return: String 
	 * @Date :          2016年1月26日 下午12:35:20   
	 * @throws:
	 */
	public String getSession(String appKey, Serializable sessionId);
	
	public Serializable createSession(Session session);
	public void updateSession(String appKey, Session session);
	public void deleteSession(String appKey, Session session);
	public PermissionContext getPermissions(String saasId,String appKey, AppUser user);
	
	public String getSaasId();
	
	
	public String  getSessions(String appKey ,Serializable sessionId);

}
