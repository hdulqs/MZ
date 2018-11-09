/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2017-12-07 14:06:38 
 */
package com.mz.customer.businessman.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.customer.businessman.model.OtcTransaction;
import java.util.List;
import java.util.Map;


/**
 * <p> C2cTransactionDao </p>
 * @author:         zongwei
 * @Date :          20180508
 */
public interface OtcTransactionDao extends  BaseDao<OtcTransaction, Long> {

	/**
	 * 查询成交的一个币的 订单数量，按商户分组
	 * @param map
	 * @return
	 */
	List<OtcTransaction> groupByBusinessmanId(Map<String,Object> map);

	/**
	 * 查询前台订单清单
	 * @param params
	 * @return
	 */
	List<OtcTransaction> otclist(Map<String, String> params);

	/**
	 * 查询前台订单清单
	 * @param params
	 * @return
	 */
	List<OtcTransaction> otclists(Map<String, String> params);

}
