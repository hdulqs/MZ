/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年9月18日 上午10:32:03
 */
package com.mz.oauth.user.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.mz.oauth.user.model.AppOrganization;
import com.mz.oauth.user.model.AppRole;
import com.mz.oauth.user.model.AppUser;
import com.mz.shiro.PasswordHelper;
import com.mz.shiro.authorization.service.AuthorizationService;
import com.mz.shiro.service.AppUserService;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.SetPropertyEditorSupport;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 *
 * @author: Liu   Shilei
 * @Date :          2015年9月18日 上午10:32:03
 */
@Controller
@RequestMapping("/user/appuser")
public class AppUserController extends BaseController<AppUser, Long> {

  @Resource(name = "appUserService")
  @Override
  public void setService(BaseService<AppUser, Long> service) {
    super.service = service;
  }

  @Autowired
  private AuthorizationService authorizationService;


  /**
   * User自己的initBinder
   */
  @InitBinder
  public void initBinderDemoUser(ServletRequestDataBinder binder) {
    // 由于用户里面的角色信息是个Set类型，所以这地方要转一下才行，前台传递过来的是个一个字符串，类似"1,2,3"这种，后台要自动转成Set
    binder
        .registerCustomEditor(Set.class, "appRoleSet", new SetPropertyEditorSupport(AppRole.class));
    binder.registerCustomEditor(Set.class, "companySet",
        new SetPropertyEditorSupport(AppOrganization.class));
    binder.registerCustomEditor(Set.class, "shopSet",
        new SetPropertyEditorSupport(AppOrganization.class));
    binder.registerCustomEditor(Set.class, "departmentSet",
        new SetPropertyEditorSupport(AppOrganization.class));

  }

  @MethodName(name = "查看后台用户")
  @RequestMapping(value = "/get/{id}")
  @ResponseBody
  public AppUser get1(@PathVariable Long id) {

    AppUser appUser = service.get(id);
    DruidDataSource ds = (DruidDataSource) ContextUtil.getBean("dataSource");
    int poolingCount = ds.getPoolingCount();
    System.out.println(ds.dump());
    System.out.println(poolingCount);
    return appUser;
  }


  @MethodName(name = "查看后台用户")
  @RequestMapping(value = "/see/{id}", method = RequestMethod.GET)
//	@MyRequiresPermissions 
  @ResponseBody
  public AppUser see(@PathVariable Long id) {
    AppUser appUser = service.get(id);
    appUser.setAppRoleSet(((AppUserService) service).getAppRoleSet(appUser));
    appUser.setCompanySet(((AppUserService) service).getCompanySet(appUser));
    appUser.setShopSet(((AppUserService) service).getShopSet(appUser));
    appUser.setDepartmentSet(((AppUserService) service).getDepartmentSet(appUser));
    return appUser;
  }


  @MethodName(name = "增加后台用户")
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request, AppUser appUser) {
    return ((AppUserService) service).add(request, appUser);
  }

  @MethodName(name = "修改后台用户")
  @RequestMapping(value = "/modify", method = RequestMethod.POST)
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request, AppUser appUser) {
    return ((AppUserService) service).modify(request, appUser);
  }

  @MethodName(name = "个人中心——上传图片")
  @RequestMapping(value = "/uploadpicture", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult uploadpicture(Long id, String picturePath) {

    JsonResult jsonResult = new JsonResult();
    try {
      AppUser appUser = service.get(id);
      appUser.setPicturePath(picturePath);
      service.update(appUser);
      jsonResult.setSuccess(true);
    } catch (Exception e) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("上传图片失败");
      e.printStackTrace();
    }
    return jsonResult;

  }

  @MethodName(name = "个人中心——修改密码")
  @RequestMapping(value = "/modifypassword", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult modifypassword(Long id, String oldPassword, String newPassword) {

    JsonResult jsonResult = new JsonResult();
    try {
      AppUser appUser = service.get(id);
      PasswordHelper passwordHelper = new PasswordHelper();
      if (!passwordHelper.validatePassword(appUser, oldPassword)) {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("原密码不正确");
        return jsonResult;
      }
      //设置新密码
      appUser.setPassword(newPassword);
      //加密
      passwordHelper.encryptPassword(appUser);
      service.update(appUser);
      jsonResult.setSuccess(true);
    } catch (Exception e) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("修改失败");
      e.printStackTrace();
    }
    return jsonResult;
  }

  @MethodName(name = "重置密码")
  @RequestMapping(value = "/resetpw", method = RequestMethod.POST)
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult resetpw(Long id, String newPassword) {

    JsonResult jsonResult = new JsonResult();
    try {
      AppUser appUser = service.get(id);
      PasswordHelper passwordHelper = new PasswordHelper();
      //设置新密码
      appUser.setPassword(newPassword);
      //加密
      passwordHelper.encryptPassword(appUser);
      service.update(appUser);
      jsonResult.setSuccess(true);
    } catch (Exception e) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("修改失败");
      e.printStackTrace();
    }
    return jsonResult;
  }


  @MethodName(name = "删除")
  @RequestMapping(value = "/remove/{ids}", method = RequestMethod.DELETE)
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult remove(@PathVariable String[] ids) {
    return ((AppUserService) service).remove(ids);
  }


  @MethodName(name = "列表")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    PageResult findPage = ((AppUserService) service).findPage(request);
    return findPage;
  }


  @MethodName(name = "查找user拥有的角色和未拥有的角色")
  @RequestMapping("/findMyRolesAndNohasRoles")
  @ResponseBody
  public Map<String, List<AppRole>> findMyRolesAndNohasRoles(Long id) {
    AppUser appUser = service.get(id);

    List<AppRole> nohas = new ArrayList<AppRole>();
    List<AppRole> has = new ArrayList<AppRole>();

    if (appUser != null) {
			
	/*		List<AppRole> allRoles = appRoleService.find();
			Set<AppRole> myRoles = appUser.getAppRoleSet();
			Iterator<AppRole> iterator = myRoles.iterator();
			//迭代拥有的角色列表
			while(iterator.hasNext()){
				AppRole next = iterator.next();
				//myRoles角色列表
				has.add(appRoleService.get(next.getId()));
			}
			//取差集
			allRoles.removeAll(has);
			nohas= allRoles;*/

    }

    Map<String, List<AppRole>> map = new HashMap<String, List<AppRole>>();
    map.put("has", has);
    map.put("nohas", nohas);

    return map;

  }

  @MethodName(name = "查找当前用户的所有shiroURL")
  @RequestMapping("/findMyShiro")
  @ResponseBody
  public Set<String> findMyShiro() {
    AppUser user = ContextUtil.getCurrentUser();
    return authorizationService
        .findPermissions(user.getSaasId(), PropertiesUtils.APP.getProperty("app.key"), user);
  }


  @MethodName(name = "查找当前登录用户")
  @RequestMapping("/getLoginUser")
  @ResponseBody
  public AppUser getLoginUser(HttpServletResponse response) {

    AppUser user = ContextUtil.getCurrentUser();

    //登录成功后设置Cookie
    Cookie cookie = new Cookie("appuserprefix", user.getAppuserprefix());
    cookie.setMaxAge(60 * 60 * 24 * 30);
    //	cookie.setHttpOnly(false);
    cookie.setPath("/");
    cookie.setVersion(0);
    response.addCookie(cookie);
    return user;
  }

  //----------------------------------------------------

}
