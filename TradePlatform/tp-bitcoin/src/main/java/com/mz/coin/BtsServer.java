package com.mz.coin;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.coin.Wallet;
import java.math.BigDecimal;


/**
 * 基于bts的币接口
 * <p> TODO</p>
 *
 * @author: shangxl
 * @Date :          2018年1月9日 下午1:51:05
 */
public interface BtsServer {

  /**
   * <p>查询余额</p>
   *
   * @author: shangxl
   * @param: @param accountName 用户名
   * @param: @return
   * @return: BigDecimal
   * @Date :          2018年1月9日 下午1:51:50
   * @throws:
   */
  public BigDecimal getBalance(String accountName);

  /**
   * <p>生成地址、备注</p>
   *
   * @author: shangxl
   * @param: @param userName
   * @param: @return
   * @return: String
   * @Date :          2018年1月9日 下午2:57:08
   * @throws:
   */
  public String getPublicKey(String userName);

  /**
   * <p>解锁钱包</p>
   *
   * @author: shangxl
   * @param: @param password
   * @param: @return
   * @return: boolean
   * @Date :          2018年1月10日 上午10:23:21
   * @throws:
   */
  public boolean unlock(String password);


  /**
   * <p>锁钱包</p>
   *
   * @author: shangxl
   * @param: @return
   * @return: boolean
   * @Date :          2018年1月10日 上午10:24:23
   * @throws:
   */
  public boolean lock();

  /**
   * 查询充币记录
   * <p> TODO</p>
   *
   * @author: shangxl
   * @param: @param accountName
   * @param: @param count
   * @param: @param id
   * @param: @param password
   * @param: @return
   * @return: List<Transaction>
   * @Date :          2018年1月10日 下午1:51:21
   * @throws:
   */
  public void getAccountHistory(String accountName, String count, String id, String password);

  /**
   * <p>转币</p>
   *
   * @author: shangxl
   * @param: @param fromAccount
   * @param: @param toAccount
   * @param: @param amount
   * @param: @param symbol
   * @param: @param memo
   * @param: @return
   * @return: String
   * @Date :          2018年1月10日 下午3:43:34
   * @throws:
   */
  public String transfer(String fromAccount, String toAccount, String amount, String symbol,
      String memo);

  /**
   * <p>转入冷钱包</p>
   *
   * @author: shangxl
   * @param: @param toAddress
   * @param: @param amount
   * @param: @return
   * @return: JsonResult
   * @Date :          2018年1月16日 下午2:32:07
   * @throws:
   */
  public JsonResult send2ColdAddress(String toAddress, String amount);


  /**
   * <p> TODO</p>
   *
   * @author: shangxl
   * @param: @return
   * @return: Wallet
   * @Date :          2018年1月16日 下午3:32:32
   * @throws:
   */
  public Wallet getWalletInfo();

  /**
   * <p> 提币</p>
   *
   * @author: shangxl
   * @param: @param amount
   * @param: @param toAddress
   * @param: @param memo
   * @param: @return
   * @return: JsonResult
   * @Date :          2018年1月16日 下午4:45:43
   * @throws:
   */
  public JsonResult sendFrom(String amount, String toAddress, String memo);

  /**
   * 查询账户的不可逆的历史交易数据
   * <p> TODO</p>
   *
   * @author: shangxl
   * @param: @param accountName
   * @param: @param startnum
   * @param: @param count
   * @param: @param endnum
   * @param: @return
   * @return: String
   * @Date :          2018年3月5日 上午11:28:45
   * @throws:
   */
  public void getRlativeAccountHistory(String accountName, String startnum, String count,
      String endnum);

}
