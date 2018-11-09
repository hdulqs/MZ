/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-08-17 18:26:08
 */
package com.mz.ico.coinAccount.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.ico.coinAccount.model.AppIcoCoinAccount;

/**
 * <p> AppIcoCoinAccountService </p>
 * @author: shangxl
 * @Date :          2017-08-17 18:26:08  
 */
public interface AppIcoCoinAccountService extends BaseService<AppIcoCoinAccount, Long> {

  /**
   * 查询币账户
   * <p> TODO</p>
   * @author: Shangxl
   * @param:    @param customerId
   * @param:    @param coinCode
   * @param:    @param currencyType
   * @param:    @param website
   * @param:    @return
   * @return: AppIcoCoinAccount
   * @Date :          2017年8月18日 下午7:28:01
   * @throws:
   */
  public AppIcoCoinAccount getByCustomerIdAndType(Long customerId, String coinCode,
      String currencyType, String website);

}
