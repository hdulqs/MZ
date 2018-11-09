package com.mz.coin.impl;

import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;
import com.mz.Constant;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.coin.Wallet;
import com.mz.util.log.LogFactory;
import com.mz.utils.Properties;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

/**
 * 基于比特币rpc接口开发新的功能
 * <p>
 * TODO
 * </p>
 *
 * @author: shangxl
 * @Date : 2017年11月16日 下午6:10:02
 */
public class JsonrpcClient extends BitcoinJSONRPCClient {

  /**
   * <p>
   * TODO
   * </p>
   *
   * @author: shangxl
   * @param: @param rpcUrl
   * @param: @throws MalformedURLException
   */
  public JsonrpcClient(String rpcUrl) throws MalformedURLException {
    super(rpcUrl);
  }

  @Override
  public String getNewAddress(String accountName) {
    try {
      if (StringUtils.isNotEmpty(accountName)) {
        return super.getNewAddress(accountName);
      } else {
        return super.getNewAddress();
      }
    } catch (BitcoinException e) {
      LogFactory.info("创建新币账户出错：  accountName=" + accountName);
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 根据地址获取usdt的账户余额
   * <p>
   * TODO
   * </p>
   *
   * @author: shangxl
   * @param: @param address
   * @param: @return
   * @return: String
   * @Date : 2017年12月18日 下午5:39:04
   * @throws:
   */
  public String omniGetBalance(String address) {
    String balance = "0";
    try {
      Integer propertyid = Integer.valueOf(Constant.PROPERTYID_USDT);
      Map<String, Object> map = (Map<String, Object>) query("omni_getbalance",
          new Object[]{address, propertyid});
      if (map != null) {
        balance = map.get("balance").toString();
      }
    } catch (BitcoinException e) {
      e.printStackTrace();
    }
    return balance;
  }

  /**
   * <p> TODO</p>
   * usdt 查询充币记录
   *
   * @author: shangxl
   * @param: @param address
   * @param: @param count
   * @param: @param skip
   * @param: @param startblock
   * @param: @param endblock
   * @param: @return
   * @return: List<Map   <   String   ,   Object>>
   * @Date :          2017年12月19日 上午11:26:51
   * @throws:
   */
  public List<Map<String, Object>> omniListTransactions(String address, int count, int skip,
      int startblock, int endblock) {
    try {
      return (List<Map<String, Object>>) query("omni_listtransactions",
          new Object[]{address, count, skip, startblock, endblock});
    } catch (BitcoinException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * usdt 转币
   * <p> TODO</p>
   *
   * @author: shangxl
   * @param: @return
   * @return: String
   * @Date :          2017年12月19日 下午3:38:31
   * @throws:
   */
  public String omniSend(String fromaddress, String toaddress, String amount) {
    try {
      return query("omni_send",
          new Object[]{fromaddress, toaddress, Integer.valueOf(Constant.PROPERTYID_USDT), amount})
          .toString();
    } catch (BitcoinException e) {
      LogFactory.info("fromaddress=" + fromaddress + " toaddress=" + toaddress + " propertyid="
          + Constant.PROPERTYID_USDT + " amount=" + amount);
      e.printStackTrace();
      return null;
    }
  }


  /**
   * <p>查询钱包总币、提币地址余额</p>
   *
   * @author: shangxl
   * @param: @return
   * @return: Wallet
   * @Date :          2018年1月16日 下午3:43:16
   * @throws:
   */
  public Wallet getUsdtWalletInfo() {
    Wallet wallet = new Wallet();
    try {
      String withdrawalsAddress = this.getAddressesByAccount("").get(0);
      CoinServiceImpl coinServiceImpl = new CoinServiceImpl(Constant.USDT);
      Map<String, Number> map = coinServiceImpl.listaccounts();
      Double totalMoneyDouble = Double.valueOf(0d);
      for (Entry<String, Number> entry : map.entrySet()) {
        List<String> addrs = this.getAddressesByAccount(entry.getKey());
        for (String l : addrs) {
          totalMoneyDouble += Double.valueOf(this.omniGetBalance(l));
        }
      }
      BigDecimal totalMoney = BigDecimal.valueOf(totalMoneyDouble);
      String coldwalletAddres = Properties.appcoinMap()
          .get(Constant.USDT.toLowerCase() + Properties.COLDADDERSS);

      wallet.setCoinCode(Constant.USDT);
      wallet.setWithdrawalsAddress(withdrawalsAddress);
      wallet.setColdwalletAddress(coldwalletAddres);
      wallet.setTotalMoney(totalMoney.toString());
      wallet.setWithdrawalsAddressMoney(this.omniGetBalance(withdrawalsAddress));
    } catch (BitcoinException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return wallet;
  }


  public JsonResult omniSendFrom(String type, String amount, String toAddress) {
    JsonResult result = new JsonResult();
    //获取所有地址，然后去第一个地址作为提币地址
    List<String> list = new ArrayList<>();
    try {
      list = this.getAddressesByAccount("");
    } catch (BitcoinException e) {
      e.printStackTrace();
    }
    if (list != null && list.size() > 0) {
      //usdt转币需要btc和usdt旷工费、暂时设定旷工费
      double btcfee = Double.valueOf("0.0005");
      double usdtfee = Double.valueOf("0.001");
      String fromaddress = list.get(0);
      double btcmoney = Double.valueOf(0d);
      try {
        btcmoney = this.getBalance();
      } catch (BitcoinException e) {
        e.printStackTrace();
      }
      String usdtmoneyStr = this.omniGetBalance(fromaddress);
      //usdt提币地址币量
      double usdtmoney = Double.valueOf(usdtmoneyStr);
      double needmoney = Double.valueOf(amount) + usdtfee;
      if (btcmoney >= btcfee) {
        if (usdtmoney >= needmoney) {
          String txid = this.omniSend(fromaddress, toAddress, amount);
          if (StringUtils.isNotEmpty(txid)) {
            result.setSuccess(true);
            result.setMsg(txid);
          } else {
            String message = "usdt提币失败，返回为空";
            LogFactory.info(message);
            result.setSuccess(false);
            result.setMsg(message);
          }
        } else {
          String message = "usdt提币地址币量不足";
          LogFactory.info(message);
          result.setSuccess(false);
          result.setMsg(message);
        }
      } else {
        String message = "钱包btc不足";
        LogFactory.info(message);
        result.setSuccess(false);
        result.setMsg(message);
      }
    } else {
      String message = "未查询到提币地址";
      LogFactory.info(message);
      result.setSuccess(false);
      result.setMsg(message);
    }
    return result;
  }
}
