/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月18日 上午10:24:56
 */
package com.mz.shiro.dao;


import com.mz.oauth.user.model.AppUser;
import com.mz.core.mvc.dao.base.BaseDao;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年9月18日 上午10:24:56 
 */
public interface AppUserDao extends BaseDao<AppUser, Long> {

	/**
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @return
	 * @return: List<AppUser> 
	 * @Date :          2016年3月16日 下午5:36:40   
	 * @throws:
	 */
	List<AppUser> findPage(Map<String, Object> map);


	/**
	 * 查询全部权限
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @return
	 * @return: String
	 * @Date :          2016年6月17日 下午3:25:58
	 * @throws:
	 */
	Set<String> findAllShiroUrl();


	/**
	 * 查询全部权限
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @return
	 * @return: String
	 * @Date :          2016年6月17日 下午3:25:58
	 * @throws:
	 */
	Set<String> findUserShiroUrl(Map<String, Object> map);
}
