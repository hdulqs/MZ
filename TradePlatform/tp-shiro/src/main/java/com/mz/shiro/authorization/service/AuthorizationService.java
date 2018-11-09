package com.mz.shiro.authorization.service;


import com.mz.shiro.authorization.model.Authorization;
import com.mz.oauth.user.model.AppUser;
import java.util.List;
import java.util.Set;


/**
 * client 端访问调用权限查询service
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年11月4日 上午10:11:35
 */
public interface AuthorizationService {


    public Authorization createAuthorization(Authorization authorization);
    public Authorization updateAuthorization(Authorization authorization);
    public void deleteAuthorization(Long authorizationId);

    public Authorization findOne(Long authorizationId);
    public List<Authorization> findAll();

    /**
     * 根据AppKey和用户名查找其角色
     * @param username
     * @return
     */
    public Set<String> findRoles(String saasId, String appKey, AppUser user);

    /**
     * 根据AppKey和用户名查找权限字符串
     * @param username
     * @return
     */
    public Set<String> findPermissions(String saasId, String appKey, AppUser user);


}
