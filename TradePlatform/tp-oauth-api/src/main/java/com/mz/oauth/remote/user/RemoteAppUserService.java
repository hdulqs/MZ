/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年11月11日 下午5:07:22
 */
package com.mz.oauth.remote.user;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.oauth.user.model.AppUser;
import com.mz.spotchange.user.model.SpVipUser;
import com.mz.tenant.user.model.SaasUser;
import com.mz.util.RemoteQueryFilter;
import java.util.List;

/**
 * 
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年11月20日 下午2:57:05
 */
public interface RemoteAppUserService  {
	
	/**
	 * 开通会员账户
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param appUser
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2016年10月20日 下午2:38:30   
	 * @throws:
	 */
	public JsonResult opeanVipUser(SpVipUser spVipUser,Long roleId);
	
	/**
	 * 
	 * <p> 创建业务平台超级管理员账号</p>
	 * @author:         Liu Shilei
	 * @param:    @param saasUser
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2015年11月24日 下午1:54:35   
	 * @throws:
	 */
	public boolean createAppUser(SaasUser saasUser);
	
	/**
	 * <p> 根据saasId查询业务平台超级管理员账号 </p>
	 * @author:         Liu Shilei
	 * @param:    @param id
	 * @return: void 
	 * @Date :          2015年11月24日 下午2:47:41   
	 * @throws:
	 */
	public AppUser findBySaasId(String saasId);
	
	
	
	public List<AppUser> find();

	/**
	 * <p> TODO</p> 删除业务平台超级管理员账号
	 * @author:         Liu Shilei
	 * @param:    @param saasUser
	 * @return: void 
	 * @Date :          2015年12月23日 下午4:42:40   
	 * @throws:
	 */
	public boolean deleteAppUser(SaasUser saasUser);

	/**
	 * <p>根据ID删除一个用户，并删除所有的关联关系</p>
	 * @author:         Liu Shilei
	 * @param:    @param appUserId
	 * @return: JsonResult 
	 * @Date :          2016年11月24日 下午2:46:51   
	 * @throws:
	 */
	public JsonResult deleteUser(Long appUserId);

	/**
	 * <p>查询AppUser</p>
	 * @author:         Liu Shilei
	 * @param:    @param remoteQueryFilter
	 * @return: void 
	 * @Date :          2016年11月25日 下午6:54:56   
	 * @throws:
	 */
	public AppUser get(RemoteQueryFilter remoteQueryFilter);

	/**
	 * <p>重置密码</p>
	 * @author:         Liu Shilei
	 * @param:    @param id
	 * @param:    @param passWord
	 * @return: void 
	 * @Date :          2016年11月25日 下午6:57:15   
	 * @throws:
	 */
	public JsonResult resetpw(Long id, String passWord);

	/**
	 * <p>注销</p>
	 * @author:         Liu Shilei
	 * @param:    @param appUserId
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2016年11月28日 上午11:32:49   
	 * @throws:
	 */
	public JsonResult logout(Long id);
	
	/**
	 * 
	 * 设置会员编号
	 * @param _spVipUser
	 */
	//public JsonResult setVipNumber(SpVipUser _spVipUser);

	
	
	
	
	
}
