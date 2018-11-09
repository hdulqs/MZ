/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Wu Shuiming
 * @version:   V1.0 
 * @Date:      2015年11月06日  14:57:13
 */
package com.mz.trade.entrust.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.trade.entrust.model.EradeCheck;
import java.util.Map;

/**
 * 
 * <p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午1:34:18
 */
public interface ExEntrustDao extends BaseDao<ExEntrust, Long> {
	
	public EradeCheck  getcoldEntrustMoney(Map<String,Object> map);
	public EradeCheck  getbuyTransactionMoney(Map<String,Object> map);
	public EradeCheck  getsellTransactionMoney(Map<String,Object> map);

	public EradeCheck  getedcoldEntrustCount(Map<String,Object> map);
	public EradeCheck  getbuyTransactioncount(Map<String,Object> map);
	public EradeCheck  getsellTransactioncount(Map<String,Object> map);

	public EradeCheck  getcoldEntrustFixPrice(Map<String,Object> map);
	public EradeCheck  getbuyTransactionFixPrice(Map<String,Object> map);
	public EradeCheck  getsellTransactionFixPrice(Map<String,Object> map);

}
