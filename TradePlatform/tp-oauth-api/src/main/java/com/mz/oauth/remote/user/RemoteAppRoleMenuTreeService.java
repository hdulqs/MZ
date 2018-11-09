/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年11月25日 下午4:49:39
 */
package com.mz.oauth.remote.user;

import java.util.List;


/**
 * 
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年6月16日 下午6:39:01
 */
public interface RemoteAppRoleMenuTreeService {
	
	/**
	 * 批量删除
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param ids
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2016年6月16日 下午6:42:47   
	 * @throws:
	 */
	public boolean delete(List<Long> ids);
	
	
	
	
}
