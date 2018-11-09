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
import com.mz.customer.businessman.model.C2cTransaction;
import com.mz.oauth.user.model.AppUser;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.manage.remote.model.c2c.C2cOrder;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;


/**
 * <p> C2cTransactionService </p>
 * @author: liushilei
 * @Date :          2017-12-07 14:06:38  
 */
public interface C2cTransactionService extends BaseService<C2cTransaction, Long> {


  /**
   * 生成与商户匹配的订单
   * @param c2cOrder
   */
  JsonResult createC2cOrder(C2cOrder c2cOrder);

  /**
   * 获得订单汇款信息
   * @param transactionNum
   * @return
   */
  String getC2cOrderDetail(String transactionNum);

  /**
   * 确认订单
   * @param valueOf
   * @param appUser
   * @return
   */
  JsonResult confirm(Long valueOf, AppUser appUser, HttpServletRequest request);

  /**
   * 关闭订单
   * @param valueOf
   * @param appUser 管理人
   * @return
   */
  JsonResult close(Long valueOf, AppUser appUser);

  /**
   * 个人中心查询c2c订单
   * @param params
   * @return
   */
  FrontPage c2clist(Map<String, String> params);

  /**
   * 订单超时
   * @param valueOf
   * @return
   */
  void timeout();


  JsonResult checkAccount(Long id, AppUser appUser, HttpServletRequest request);
}
