/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年12月9日 下午3:14:21
 */
package com.mz.oauth.remote.company;

import com.mz.tenant.user.model.SaasUser;
import com.mz.util.RemoteQueryFilter;
import com.mz.oauth.user.model.AppOrganization;
import com.mz.oauth.user.model.AppUserOrganization;
import java.util.List;

/**  组织结构远程调用
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年12月9日 下午3:14:21 
 */
public interface RemoteAppOrganizationService {
	
	/**
	 * 初始化组织结构根数据
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2015年12月9日 下午3:16:04   
	 * @throws:
	 */
	public boolean initRootOrganization(SaasUser saasUser);

	/**
	 * 还原组织结构根数据
	 * <p> TODO</p>  
	 * @author:         Liu Shilei
	 * @param:    @param saasUser
	 * @return: void 
	 * @Date :          2015年12月23日 下午4:53:55   
	 * @throws:
	 */
	public boolean restoreRootOrganization(SaasUser saasUser);
	
	
	/**
	 * 查询用户组织结构表对象
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param userId
	 * @param:    @return
	 * @return: List<AppUserOrganization> 
	 * @Date :          2016年6月21日 上午9:45:02   
	 * @throws:
	 */
	public List<AppUserOrganization>  findUserOrgByUid(Long userId);

	/**
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param queryFilter
	 * @return: void 
	 * @Date :          2016年6月21日 上午10:06:20   
	 * @throws:
	 */
	public AppOrganization get(RemoteQueryFilter queryFilter);
	
}
