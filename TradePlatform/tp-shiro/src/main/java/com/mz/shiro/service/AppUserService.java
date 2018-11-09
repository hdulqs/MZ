/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月18日 上午10:28:30
 */
package com.mz.shiro.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.oauth.user.model.AppOrganization;
import com.mz.oauth.user.model.AppResource;
import com.mz.oauth.user.model.AppRole;
import com.mz.oauth.user.model.AppUser;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年9月18日 上午10:28:30 
 */
public interface AppUserService  extends BaseService<AppUser, Long>{

	/**
	 * <p> TODO</p> 添加账号
	 * @author:         Liu Shilei
	 * @param:    @param request
	 * @param:    @param appUser
	 * @return: void 
	 * @Date :          2015年12月10日 下午5:41:59   
	 * @throws:
	 */
	JsonResult add(HttpServletRequest request, AppUser appUser);

	/**
	 * <p> TODO</p>  删除账号
	 * @author:         Liu Shilei
	 * @param:    @param ids
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2015年12月21日 上午11:40:05   
	 * @throws:
	 */
	JsonResult remove(String[] ids);
	
	/**
	 * <p> TODO</p>  修改用户
	 * @author:         Liu Shilei
	 * @param:    @param request
	 * @param:    @param appUser
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2015年12月21日 下午6:43:27   
	 * @throws:
	 */
	JsonResult modify(HttpServletRequest request, AppUser appUser);
	
	
	/**
	 * 查当前用户的所有权限
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param appuser
	 * @param:    @return
	 * @return: Set<AppResource> 
	 * @Date :          2015年12月22日 下午6:37:21   
	 * @throws:
	 */
	Set<AppResource>  getAppResourceSet(AppUser appuser);

	/**
	 * <p> TODO</p>  查当前用户的所有角色
	 * @author:         Liu Shilei
	 * @param:    @param appUser
	 * @return: void 
	 * @Date :          2015年12月21日 下午12:06:20   
	 * @throws:
	 */
	Set<AppRole> getAppRoleSet(AppUser appUser);

	/**
	 * <p> TODO</p>  查当前用户的所有公司
	 * @author:         Liu Shilei
	 * @param:    @param appUser
	 * @param:    @return
	 * @return: Set<AppOrganization> 
	 * @Date :          2015年12月21日 下午1:48:11   
	 * @throws:
	 */
	Set<AppOrganization> getCompanySet(AppUser appUser);

	/**
	 * <p> TODO</p> 查当前用户的所有门店
	 * @author:         Liu Shilei
	 * @param:    @param appUser
	 * @param:    @return
	 * @return: Set<AppOrganization> 
	 * @Date :          2015年12月21日 下午1:48:33   
	 * @throws:
	 */
	Set<AppOrganization> getShopSet(AppUser appUser);

	/**
	 * <p> TODO</p>  查当前用户的所有部门
	 * @author:         Liu Shilei
	 * @param:    @param appUser
	 * @param:    @return
	 * @return: Set<AppOrganization> 
	 * @Date :          2015年12月21日 下午1:48:52   
	 * @throws:
	 */
	Set<AppOrganization> getDepartmentSet(AppUser appUser);

	/**
	 * <p> TODO</p>   AppUser分页方法
	 * @author:         Liu Shilei
	 * @param:    @param request
	 * @param:    @return
	 * @return: PageResult 
	 * @Date :          2016年3月16日 下午5:23:43   
	 * @throws:
	 */
	PageResult findPage(HttpServletRequest request);

	/**
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param appUser
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2016年10月21日 上午10:51:01   
	 * @throws:
	 */
	JsonResult openVip(AppUser appUser);
	
	
	




}
