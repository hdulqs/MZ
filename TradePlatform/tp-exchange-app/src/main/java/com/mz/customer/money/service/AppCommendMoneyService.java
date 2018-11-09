/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: menwei
 * @version: V1.0
 * @Date: 2017-11-29 10:05:55
 */
package com.mz.customer.money.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.money.model.AppCommendMoney;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p> AppCommendMoneyService </p>
 * @author: menwei
 * @Date :          2017-11-29 10:05:55  
 */
public interface AppCommendMoneyService extends BaseService<AppCommendMoney, Long> {

  List<AppCommendMoney> selectCommend();

  BigDecimal findOne(String userName, String fixPriceCoinCode);

  BigDecimal findTwo(String userName, String fixPriceCoinCode);

  BigDecimal findThree(String userName, String fixPriceCoinCode);

  BigDecimal findLater(String userName, String fixPriceCoinCode);

  JsonResult postMoneyById(Long id, BigDecimal money, String fixPriceCoinCode);

  //修改用户返佣状态
  List<AppCommendMoney> selectMoneyCommend(String username);

  //一键派发
  List<AppCommendMoney> selectAllCommend();


}
