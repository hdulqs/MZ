/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-29 10:05:55 
 */
package com.mz.customer.money.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.customer.money.model.AppCommendMoney;
import java.util.List;


/**
 * <p> AppCommendMoneyDao </p>
 * @author:         menwei
 * @Date :          2017-11-29 10:05:55  
 */
public interface AppCommendMoneyDao extends  BaseDao<AppCommendMoney, Long> {

	List<AppCommendMoney> findAgentsForMoney(Long custromerId, String fixPriceCoinCode);
	
    //修改用户前台返佣状态
	List<AppCommendMoney> selectMoneyCommendAll(String username);

	//查询要返佣的对象
	List<AppCommendMoney> getAll();

}
