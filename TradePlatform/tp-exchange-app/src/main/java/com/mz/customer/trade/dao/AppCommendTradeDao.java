/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 17:40:59 
 */
package com.mz.customer.trade.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.customer.trade.model.AppCommendTrade;
import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p> AppCommendTradeDao </p>
 * @author:         menwei
 * @Date :          2017-11-28 17:40:59  
 */
public interface AppCommendTradeDao extends  BaseDao<AppCommendTrade, Long> {

	BigDecimal findOne(String userName, String fixPriceCoinCode);

	BigDecimal findTwo(String userName, String fixPriceCoinCode);


	BigDecimal findThree(String userName, String fixPriceCoinCode);

	BigDecimal findLater( String userName, String fixPriceCoinCode);

	List<AppCommendTrade> selectCommendTrade(@Param("custromerName")String custromerName);

    List<AppCommendTrade> findByUids(List<Long> pids);

    List<AppCommendTrade> findByUsername(String username);

	AppCommendTrade findList(String id);
}
