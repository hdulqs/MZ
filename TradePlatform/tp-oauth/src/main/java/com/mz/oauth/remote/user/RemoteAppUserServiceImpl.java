/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年9月18日 上午10:29:49
 */
package com.mz.oauth.remote.user;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.oauth.user.model.AppUser;
import com.mz.shiro.PasswordHelper;
import com.mz.shiro.dao.AppUserDao;
import com.mz.shiro.service.AppOrganizationService;
import com.mz.shiro.service.AppRoleService;
import com.mz.shiro.service.AppUserService;
import com.mz.spotchange.user.model.SpVipUser;
import com.mz.tenant.user.model.SaasUser;
import com.mz.util.QueryFilter;
import com.mz.util.RemoteQueryFilter;
import com.mz.util.properties.PropertiesUtils;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2015年9月18日 上午10:29:49 
 */
public class RemoteAppUserServiceImpl implements RemoteAppUserService {

  @Resource(name = "appUserDao")
  private AppUserDao appUserDao;

  @Autowired
  private AppUserService appUserService;

  @Autowired
  private AppRoleService appRoleService;

  @Autowired
  private AppOrganizationService appOrganizationService;


  @Override
  public boolean createAppUser(SaasUser saasUser) {
    try {
      AppUser appUser = new AppUser();
      appUser.setSaasId(saasUser.getSaasId());
      appUser.setAppuserprefix(saasUser.getAppuserprefix());
      /**
       * 用户名前缀加用户名
       */
      appUser.setUsername(PropertiesUtils.APP.getProperty("app.admin"));
      appUser.setPassword("admin");
      PasswordHelper passwordHelper = new PasswordHelper();
      passwordHelper.encryptPassword(appUser);
      //		appUserDao.save_NOSAAS(appUser);

      appUserDao.insertSelective(appUser);

      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public AppUser findBySaasId(String saasId) {

//		QueryFilter filter = new QueryFilter();
//		filter.addFilter("Q_t.saasId_=_String", saasId);
//		filter.addFilter("Q_t.username_=_String", "admin");
//		return appUserDao.get_NOSAAS(filter);

    QueryFilter filter = new QueryFilter(AppUser.class);
    filter.addFilter("saasId=", saasId);
    filter.addFilter("username=", "admin");

    return appUserDao.selectByExample(filter.setNosaas().getExample()).get(0);

  }

  @Override
  public List<AppUser> find() {

    return appUserDao.selectAll();

    //	return appUserDao.findAll_NOSAAS();
  }

  @Override
  public boolean deleteAppUser(SaasUser saasUser) {
    try {
//			QueryFilter filter  = new QueryFilter();
//			filter.addFilter("Q_t.saasId_=_String", saasUser.getSaasId());
//			filter.addFilter("Q_t.username_=_String", "amdin");

      QueryFilter filter = new QueryFilter(AppUser.class);
      filter.addFilter("saasId=", saasUser.getSaasId());

//			List<AppUser> find_NOSAAS = appUserDao.find_NOSAAS(filter);

      List<AppUser> find_NOSAAS = appUserDao.selectByExample(filter.setNosaas().getExample());

      if (find_NOSAAS != null) {
        for (AppUser appUser : find_NOSAAS) {
          appUserDao.delete(appUser);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }


  @Override
  public JsonResult opeanVipUser(SpVipUser spVipUser, Long roleId) {
    JsonResult jsonResult = new JsonResult();
    return jsonResult;
  }

  @Override
  public JsonResult deleteUser(Long appUserId) {
    JsonResult remove = appUserService.remove(new String[]{appUserId.toString()});
    return remove;
  }

  @Override
  public AppUser get(RemoteQueryFilter remoteQueryFilter) {
    return appUserService.get(remoteQueryFilter.getQueryFilter());
  }

  @Override
  public JsonResult resetpw(Long id, String passWord) {

    JsonResult jsonResult = new JsonResult();

    AppUser appUser = appUserService.get(id);
    appUser.setPassword(passWord);
    PasswordHelper passwordHelper = new PasswordHelper();
    passwordHelper.encryptPassword(appUser);
    appUserService.update(appUser);

    jsonResult.setSuccess(true);
    return jsonResult;


  }

  @Override
  public JsonResult logout(Long id) {
    JsonResult jsonResult = new JsonResult();

    AppUser appUser = appUserService.get(id);
    appUser.setIsDelete("1");
    appUserService.update(appUser);

    jsonResult.setSuccess(true);
    return jsonResult;
  }


}
