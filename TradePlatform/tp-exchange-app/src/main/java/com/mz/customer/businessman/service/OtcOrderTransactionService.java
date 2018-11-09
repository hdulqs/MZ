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
import com.mz.customer.businessman.model.OtcOrderTransaction;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.manage.remote.model.otc.OtcOrderTransactionMange;
import java.math.BigDecimal;
import java.util.Map;


/**
 * <p> OtcTransactionService </p>
 * @author: liushilei
 * @Date :          2017-12-07 14:06:38  
 */
public interface OtcOrderTransactionService extends BaseService<OtcOrderTransaction, Long> {


  /**
   * 查询otc从清单中购买
   * @author zongwei
   * @param id  清单id
   * @param customerId  操作者id
   * @time 2018-05-08
   * @return
   */
  JsonResult createOrderTransaction(Long customerId, Long id, BigDecimal transactioncount);


  /**
   * 完成付款
   * @param otcOrderTransactionMange
   * @return
   */
  JsonResult otcPayment(OtcOrderTransactionMange otcOrderTransactionMange);


  public FrontPage otcorderlistall(Map<String, String> params);

  /**
   * 获卖交易订单列表
   * @param
   * @return
   */
  public FrontPage otcorderselllist(Map<String, String> params);

  /**
   * 获买交易订单列表
   * @param
   * @return
   */
  public FrontPage otcorderbuylist(Map<String, String> params);


  /**
   * otc申诉
   * @param otcOrderTransactionMange
   * @return
   */
  JsonResult confirmotcApplyArbitration(OtcOrderTransactionMange otcOrderTransactionMange);


  public JsonResult finishOtcOrder(OtcOrderTransactionMange otcOrderTransactionMange);

  /**
   * otc撤销
   * @param otcOrderTransactionMange
   * @return
   */
  public JsonResult otcUndo(OtcOrderTransactionMange otcOrderTransactionMange);


  /**
   * otc关闭
   * @param otcOrderTransactionMange
   * @return
   */
  public JsonResult otcColse(OtcOrderTransactionMange otcOrderTransactionMange);

  /**
   * otc拒绝
   * @param otcOrderTransactionMange
   * @return
   */
  public JsonResult otcrefuse(OtcOrderTransactionMange otcOrderTransactionMange);

  /**
   * 订单超时
   * @param
   * @return
   */
  void timeout();


}
