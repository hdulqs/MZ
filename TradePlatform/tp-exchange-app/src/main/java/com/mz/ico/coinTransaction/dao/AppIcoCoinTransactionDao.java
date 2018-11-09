/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-08-18 14:08:35
 */
package com.mz.ico.coinTransaction.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.ico.coinTransaction.model.AppIcoCoinTransaction;
import java.util.List;
import java.util.Map;

/**
 * <p> AppIcoCoinTransactionDao </p>
 * @author: shangxl
 * @Date :          2017-08-18 14:08:35  
 */
public interface AppIcoCoinTransactionDao extends BaseDao<AppIcoCoinTransaction, Long> {

  List<AppIcoCoinTransaction> findIcotransaction(Map<String, String> params);
}
