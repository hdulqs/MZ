/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: menwei
 * @version: V1.0
 * @Date: 2017-11-28 17:40:59
 */
package com.mz.customer.trade.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.commend.model.AppCommendUser;
import com.mz.customer.trade.model.AppCommendTrade;
import com.mz.trade.entrust.model.ExOrderInfo;
import java.math.BigDecimal;
import java.util.List;


/**
 * <p> AppCommendTradeService </p>
 * @author: menwei
 * @Date :          2017-11-28 17:40:59  
 */
public interface AppCommendTradeService extends BaseService<AppCommendTrade, Long> {

  Boolean dealCommission(String orderNum);

  void saveCommissionDetailForOrder(ExOrderInfo orderInfo, AppCommendUser appCommendUser, int j,
      Integer type, BigDecimal transactionPrice);

  BigDecimal selectCommissionByMoney(BigDecimal transactionBuyFee);

  BigDecimal findOne(String userName, String fixPriceCoinCode);

  BigDecimal findTwo(String userName, String fixPriceCoinCode);

  BigDecimal findThree(String userName, String fixPriceCoinCode);

  BigDecimal findLater(String userName, String fixPriceCoinCode);

  List<AppCommendTrade> selectCommendTrade(String username);


  List<AppCommendTrade> findByUids(List<Long> pids);

  List<AppCommendTrade> findByUsername(String username);

  AppCommendTrade findList(String id);
}
