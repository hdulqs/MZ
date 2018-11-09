/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年2月17日 下午3:03:04
 */
package com.mz.shiro.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.oauth.user.model.AppUserLevel;
import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年2月17日 下午3:03:04 
 */
public interface AppUserLevelService extends BaseService<AppUserLevel, Long>{

	/**
	 * <p> 批量保存上下级关系</p>
	 * @author:         Liu Shilei
	 * @param:    @param users
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2016年2月18日 上午9:35:12   
	 * @throws:
	 */
	JsonResult addBatch(List<AppUserLevel> users);

	/**
	 * <p> 查询上下级结构树，并自动补填新增用户</p>
	 * @author:         Liu Shilei
	 * @param:    @return
	 * @return: List<AppUserLevel> 
	 * @Date :          2016年2月18日 上午10:19:35   
	 * @throws:
	 */
	List<AppUserLevel> saveForlist();
	

}
