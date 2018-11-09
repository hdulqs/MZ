/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-08-18 14:08:35
 */
package com.mz.ico.coinTransaction.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.ico.coinTransaction.model.AppIcoCoinTransaction;
import com.mz.manage.remote.model.base.FrontPage;
import java.util.Map;


/**
 * <p> AppIcoCoinTransactionService </p>
 * @author: shangxl
 * @Date :          2017-08-18 14:08:35  
 */
public interface AppIcoCoinTransactionService extends BaseService<AppIcoCoinTransaction, Long> {

  /**
   * 查询充币 分页
   * @param params
   * @return
   */
  public FrontPage findIcotransaction(Map<String, String> params);
}
