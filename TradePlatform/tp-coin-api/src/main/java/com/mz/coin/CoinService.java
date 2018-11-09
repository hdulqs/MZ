/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Zhang Xiaofang
 * @version: V1.0
 * @Date: 2016年7月15日 上午11:30:27
 */
package com.mz.coin;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin.Transaction;
import com.mz.core.mvc.model.page.JsonResult;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Zhang Xiaofang
 * @Date : 2016年7月15日 上午11:30:27
 */
public interface CoinService {


  /**
   *
   * <p>
   * 生成一个新的钱包地址
   * </p>
   *
   * @author: Zhang Xiaofang
   * @param: @return
   * @return: String
   * @Date : 2016年7月15日 上午11:31:21
   * @throws:
   */

  public String createNewAddress(String string);


  /**
   * <p>
   * 获取账户总金额
   * </p>
   *
   * @author: Yuan Zhicheng
   * @param: @param account
   * @param: @return
   * @return: double
   * @Date : 2016年8月10日 上午11:19:13
   * @throws:
   */
  public double getBalance();

  /**
   * <p>
   * 获取用户 持币金额
   * </p>
   *
   * @author: Yuan Zhicheng
   * @param: @param account
   * @param: @return
   * @return: double
   * @Date : 2016年8月10日 上午11:19:13
   * @throws:
   */
  public double getBalance(String account);

  /**
   * <p>
   * 查询全部交易信息
   * </p>
   *
   * @author: Yuan Zhicheng
   * @param: @return
   * @return: List<Transaction>
   * @Date : 2016年8月10日 上午11:44:12
   * @throws:
   */
  public List<Transaction> listTransactions(String account, Integer count);

  /**
   * <p>
   * 查询具体用户全部交易信息
   * </p>
   *
   * @author: Yuan Zhicheng
   * @param: @return
   * @return: List<Transaction>
   * @Date : 2016年8月10日 上午11:44:12
   * @throws:
   */
  //public List<Transaction> listTransactions(String account);

  /**
   *
   * <p>
   * <p>
   * 查询具体用户 前几条交易信息
   * </p>
   * </p>
   *
   * @author: Yuan Zhicheng
   * @param: @param account 用户账号
   * @param: @param count 查询数量
   * @param: @return
   * @return: List<Transaction>
   * @Date : 2016年8月10日 上午11:47:02
   * @throws:
   */
  //public List<Transaction> listTransactions(String account, int count);

  /**
   *
   * <p>
   * 查询交易信息
   * </p>
   *
   * @author: Yuan Zhicheng
   * @param: @param account 用户账号
   * @param: @param count 数量
   * @param: @param from 开始于
   * @param: @return
   * @return: List<Transaction>
   * @Date : 2016年8月10日 上午11:47:27
   * @throws:
   */
  public List<Transaction> listTransactions(String account, int count, int from);

  /**
   * <p>
   * 查询交易信息
   * </p>
   *
   * @author: Yuan Zhicheng
   * @param: @param txId 订单号
   * @param: @return
   * @return: String
   * @Date : 2016年9月5日 下午1:51:26
   * @throws:
   */
  public String getRawTransaction(String txId);

  /**
   * <p>
   * 转账
   * </p>
   *
   * @author: Yuan Zhicheng
   * @param: @param address 转入地址
   * @param: @param amount 金额
   * @param: @return
   * @return: String 交易订单号
   * @Date : 2016年9月5日 下午3:07:26
   * @throws:
   */
  //public String sendToAddress(String address, double amount);

  /**
   *
   * <p>
   * 转币
   * </p>
   *
   * @author: Yuan Zhicheng
   * @param: @param fromAddress 转出地址
   * @param: @param toBitcoinAddress 转入地址
   * @param: @param amount 转入金额
   * @param: @return
   * @return: String
   * @Date : 2016年8月19日 下午1:51:12
   * @throws:
   */
  public String sendFrom(String fromAddress, String toBitcoinAddress, double amount);

  public JsonResult sendFrom(String amount, String toAddress);

  /**
   *
   * <p>
   * 转币
   * </p>
   *
   * @author: Yuan Zhicheng
   * @param: @param account 转出帐号
   * @param: @param toBitcoinAddress 转入地址
   * @param: @param amount 转入金额
   * @param: @return
   * @return: String
   * @Date : 2016年8月19日 下午1:51:12
   * @throws:
   */
  public String sendFromByAccount(String account, String toBitcoinAddress, double amount);

  /**
   * <p>
   * 校验地址
   * </p>
   *
   * @author: Yuan Zhicheng
   * @param: @param address
   * @param: @return
   * @return: String
   * @Date : 2016年9月6日 上午11:38:47
   * @throws:
   */
  public String validateAddress(String address);

  /**
   *
   * <p>
   * 账户列表
   * </p>
   *
   * @author: Yuan Zhicheng
   * @param: @return
   * @return: Map<String ,   Number>
   * @Date : 2016年9月27日 下午2:18:16
   * @throws:
   */
  public Map<String, Number> listaccounts();

  /**
   *
   * <p>
   * 获取用户地址
   * </p>
   *
   * @author: Yuan Zhicheng
   * @param: @param account 账号
   * @param: @return
   * @return: List<String>
   * @Date : 2016年9月27日 下午2:20:39
   * @throws:
   */
  public List<String> getAddressesByAccount(String account);

  /**
   * <p>
   * 获取总共充值金额
   * </p>
   *
   * @author: Yuan Zhicheng
   * @param: @param address
   * @param: @return
   * @return: Double
   * @Date : 2016年10月31日 下午3:22:57
   * @throws:
   */
  public double getReceivedByAddress(String address);

  //public String getOurAddress(String type);

  /**
   * 根据地址查询用户名
   * <p> TODO</p>
   * @author: shangxl
   * @param:    @param address
   * @param:    @return
   * @return: String
   * @Date :          2017年11月10日 下午5:20:39
   * @throws:
   */
  public String getAccount(String address);

  /**
   * 从钱包转出
   * <p> TODO</p>
   * @author: shangxl
   * @param:    @param toaddress
   * @param:    @param amount
   * @param:    @return
   * @return: String
   * @Date :          2017年11月13日 下午5:38:28
   * @throws:
   */
  public JsonResult sendtoAddress(String toaddress, Double amount);


  /**
   * 地址到地址转币，返回txHash
   * <p> TODO</p>
   * @author: shangxl
   * @param:    @param fromAddress
   * @param:    @param toBitcoinAddress
   * @param:    @param amount
   * @param:    @return
   * @return: String
   * @Date :          2017年11月14日 下午7:45:11
   * @throws:
   */
  public String sendfrom(String fromAddress, String toBitcoinAddress, double amount);

  public Wallet getWalletInfo(String coinCode);

}
