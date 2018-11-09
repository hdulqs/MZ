/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午5:55:59
 */
package com.mz.trade.account.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.exchange.account.model.ExDmColdAccountRecord;
import java.util.List;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午5:55:59
 */
public interface ExDmColdAccountRecordDao extends
		BaseDao<ExDmColdAccountRecord, Long> {
	 public  void insertRecord(List<ExDmColdAccountRecord> list);

}
