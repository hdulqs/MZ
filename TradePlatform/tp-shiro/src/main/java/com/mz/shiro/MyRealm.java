package com.mz.shiro;

import com.mz.oauth.user.model.AppUser;
import com.mz.shiro.authorization.service.AuthorizationService;
import com.mz.shiro.service.AppUserService;
import com.mz.util.QueryFilter;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 认证授权域
 * <p>
 * TODO
 * </p>
 *
 * @author: Liu Shilei
 * @Date : 2015年9月25日 上午10:38:46
 */
public class MyRealm extends AuthorizingRealm {

  @Resource
  private AppUserService appUserService;
  @Resource
  private AuthorizationService authorizationService;

  private static final String OR_OPERATOR = " or ";
  private static final String AND_OPERATOR = " and ";
  private static final String NOT_OPERATOR = " not ";

  /**
   * 授权--查询登录用户所拥有的权限资源
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

    String saasId = ContextUtil.getSaasId();
    String appKey = PropertiesUtils.APP.getProperty("app.key");
    AppUser user = ContextUtil.getCurrentUser();
    // 登录后查询权限
    LogFactory.info("查询权限.......................................................");
    authorizationInfo.setRoles(authorizationService.findRoles(saasId, appKey, user));
    Set<String> findPermissions = authorizationService.findPermissions(saasId, appKey, user);
    authorizationInfo.setStringPermissions(findPermissions);

    return authorizationInfo;
  }

  /**
   * 认证--登录认证
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
      throws AuthenticationException {
    UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
    // 获得用户名前缀
    QueryFilter queryFilter = new QueryFilter(AppUser.class);
    queryFilter.addFilter("username=", token.getUsername());
    queryFilter.addFilter("isDelete!=", "1");
    AppUser appUser = appUserService.get(queryFilter.setNosaas());
    if (appUser == null) {
      throw new UnknownAccountException();
    }
    SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
        appUser.getUsername(),
        appUser.getPassword(),
        new MySimpleByteSource((appUser.getSalt() + appUser.getAppuserprefix()).getBytes()),
        getName()
    );
    //-------------------------------------登录成功---------------------------------------------
    //设置session,saasId
    SecurityUtils.getSubject().getSession().setAttribute("user", appUser);
    SecurityUtils.getSubject().getSession().setAttribute("userName", appUser.getUsername());
    SecurityUtils.getSubject().getSession().setAttribute("saasId", appUser.getSaasId());
    return authenticationInfo;
  }

  /**
   * 支持or and not 关键词 不支持and or混用
   */
  public boolean isPermitted(PrincipalCollection principals, String permission) {
    if (permission.contains(OR_OPERATOR)) {
      String[] permissions = permission.split(OR_OPERATOR);
      for (String orPermission : permissions) {
        if (isPermittedWithNotOperator(principals, orPermission)) {
          return true;
        }
      }
      return false;
    } else if (permission.contains(AND_OPERATOR)) {
      String[] permissions = permission.split(AND_OPERATOR);
      for (String orPermission : permissions) {
        if (!isPermittedWithNotOperator(principals, orPermission)) {
          return false;
        }
      }
      return true;
    } else {
      return isPermittedWithNotOperator(principals, permission);
    }
  }

  private boolean isPermittedWithNotOperator(PrincipalCollection principals, String permission) {
    if (permission.startsWith(NOT_OPERATOR)) {
      return !super.isPermitted(principals, permission.substring(NOT_OPERATOR.length()));
    } else {
      return super.isPermitted(principals, permission);
    }
  }

}