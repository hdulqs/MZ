/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: liushilei
 * @version: V1.0
 * @Date: 2017-12-07 14:06:38
 */
package com.mz.customer.businessman.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.businessman.model.OtcTransaction;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.manage.remote.model.otc.OtcTransactionOrder;
import java.util.List;
import java.util.Map;

/**
 * <p> OtcTransactionService </p>
 * @author: liushilei
 * @Date :          2017-12-07 14:06:38  
 */
public interface OtcTransactionService extends BaseService<OtcTransaction, Long> {


  /**
   * 生成与商户匹配的订单
   * @param otcTransactionOrder
   */
  JsonResult createOtcOrder(OtcTransactionOrder otcTransactionOrder);

  /**
   * 获得订单汇款信息
   * @param transactionNum
   * @return
   */
  String getOtcOrderDetail(String transactionNum);

  /**
   * 确认订单
   * @param valueOf
   * @return
   */
  JsonResult confirm(Long valueOf);

  /**
   * 关闭委托订单
   * @param valueOf
   * @return
   */
  JsonResult OtcListclose(Long valueOf);


  /**
   * 获得委托订单
   * @param transactionType
   * @return
   */
  List<OtcTransaction> getOtclist(String transactionType, String coinCode, String OrderByClause);


  public FrontPage getOtclists(Map<String, String> params);


}
