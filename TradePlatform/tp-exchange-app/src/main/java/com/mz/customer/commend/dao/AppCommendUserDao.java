/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 15:30:37 
 */
package com.mz.customer.commend.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.customer.commend.model.AppCommendUser;
import java.util.List;
import java.util.Map;


/**
 * <p> AppCommendUserDao </p>
 * @author:         menwei
 * @Date :          2017-11-28 15:30:37  
 */
public interface AppCommendUserDao extends  BaseDao<AppCommendUser, Long> {


	int findLen(String id);

	JsonResult forzen(String ids);

	JsonResult noforzen(String ids);

    List<AppCommendUser> findLikeBySid(Map pids);

    List<AppCommendUser> findByAloneMoneyIsNotZero(Map map);
    
    int findLen2(String sid);
}
