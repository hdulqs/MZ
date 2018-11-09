/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: menwei
 * @version: V1.0
 * @Date: 2017-11-28 15:30:38
 */
package com.mz.customer.commend.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.commend.model.AppCommendUser;
import com.mz.customer.user.model.AppCustomer;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p> AppCommendUserService </p>
 * @author: menwei
 * @Date :          2017-11-28 15:30:38  
 */
public interface AppCommendUserService extends BaseService<AppCommendUser, Long> {

  void saveObj(AppCustomer customer);

  List<AppCommendUser> saveAppTradeFactor(AppCustomer buyCustomer, BigDecimal transactionBuyFee,
      String fixPriceCoinCode, Integer fixPriceType);

  int findLen(String id);

  JsonResult forzen(String ids);

  JsonResult noforzen(String ids);


  List<AppCommendUser> findLikeBySid(String pid);

  List<AppCommendUser> findByAloneMoneyIsNotZero(AppCommendUser appCommendUser);

  //新增方法
  int findLen2(String sid);
}
