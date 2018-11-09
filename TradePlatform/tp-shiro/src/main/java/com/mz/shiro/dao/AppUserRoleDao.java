/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年12月9日 下午7:19:24
 */
package com.mz.shiro.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.oauth.user.model.AppUserRole;
import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年12月9日 下午7:19:24 
 */
public interface AppUserRoleDao extends BaseDao<AppUserRole, Long> {

	/**
	 * <p> TODO</p>   通过角色查询所有用户角色关联信息
	 * @author:         Liu Shilei
	 * @param:    @param appRole
	 * @return: void 
	 * @Date :          2015年12月10日 下午6:59:11   
	 * @throws:
	 */
	List<AppUserRole> findByAppRole(Long roleId);
	
	/**
	 * <p> TODO</p>通过用户查询所有用户角色关联信息
	 * @author:         Liu Shilei
	 * @param:    @param appUser
	 * @return: void 
	 * @Date :          2015年12月21日 下午1:41:22   
	 * @throws:
	 */
	List<AppUserRole> findByAppUser(Long userId);
	

	Long  deleteUserId(Long userId);


}
