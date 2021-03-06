/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午5:55:59
 */
package com.mz.exchange.account.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.exchange.account.model.ExDmHotAccountRecord;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午5:55:59
 */
public interface ExDmHotAccountRecordDao extends
		BaseDao<ExDmHotAccountRecord, Long> {
	 public  void insertRecord(List<ExDmHotAccountRecord> list);
	   public List<ExDmHotAccountRecord> find(Map<String,Object> map);
}
