/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月28日 下午6:57:19
 */
package com.mz.exchange.transaction.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.util.QueryFilter;
import com.mz.manage.remote.model.base.FrontPage;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午6:57:19
 */
public interface ExDmTransactionService extends
    BaseService<ExDmTransaction, Long> {

  PageResult findPageBySql(QueryFilter filter);


  public ExDmTransaction findLastTrasaction();

  /**
   * 使用用户名以及币的code 查询用户当天提现或充值的总数
   *
   * type  1表示充值  2 表示提现
   *
   * @author: Wu Shuiming
   * @version: V1.0
   * @date: 2016年8月31日 下午7:00:28
   */
  public BigDecimal findTransactionByCustomer(String customer, String coinCode, String type);


  /**
   * 查询钱包记录并保存流水以及更新币种账户余额
   *
   * @author: Zhang Xiaofang
   * @param: @return
   * @return: Map<String       ,       String>
   * @Date :          2016年9月5日 下午3:56:57
   * @throws:
   */
  public Map<String, String> record();

  /**
   * 把提币订单为审核状态的记录重新调用钱包查询接口提币结果。
   *
   * @author: Zhang Xiaofang
   * @param: @return
   * @return: Map<String       ,       String>
   * @Date :          2016年9月5日 下午4:37:23
   * @throws:
   */
  public Map<String, String> updateStatus();


  /**
   * 从钱包转币到我方充币账户
   * <p> TODO</p>
   *
   * @author: Zhang Xiaofang
   * @param: @return
   * @return: String
   * @Date :          2016年9月6日 上午11:11:57
   * @throws:
   */
  public JsonResult sendToOurRecharge();


  /**
   * 查询钱包记录并保存流水以及更新币种账户余额
   *
   * @author: Zhang Xiaofang
   * @param: @return
   * @return: Map<String       ,       String>
   * @Date :          2016年9月5日 下午3:56:57
   * @throws:
   */
  public Map<String, String> recordAll();


  /**
   * 查询钱包记录(提币记录)并保存流水以及更新币种账户余额
   *
   * @author: Zhang Xiaofang
   * @param: @return
   * @return: Map<String       ,       String>
   * @Date :          2016年9月18日 下午3:21:43
   * @throws:
   */
  public Map<String, String> recordAllWithdraw();

  /**
   * 撤销成功记录
   */
  public JsonResult cancelTransaction(Long id);

  public FrontPage findExdmtransaction(Map<String, String> params);

  /**
   * 内部转账
   */
  boolean sendOurCustomer(ExDmTransaction t, ExDigitalmoneyAccount exDigitalmoneyAccount);
}
