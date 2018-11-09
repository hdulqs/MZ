package com.mz.coin.impl;

import com.alibaba.fastjson.JSON;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin.Transaction;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;
import com.mz.coin.CoinService;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.coin.Wallet;
import com.mz.util.StringUtil;
import com.mz.util.log.LogFactory;
import com.mz.utils.CommonUtil;
import com.mz.utils.Properties;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * <p> TODO</p>
 *
 * @author: shangxl
 * @Date :          2018年3月13日 下午8:13:18
 */
public class CoinServiceImpl implements CoinService {

  private volatile static Map<String, BitcoinJSONRPCClient> mapClient = null;

  private static BitcoinJSONRPCClient client = null;

  public CoinServiceImpl() {
    super();
  }

  /**
   * 获取rpc客户端
   * <p> TODO</p>
   *
   * @author: shangxl
   * @param: @param type
   */
  public CoinServiceImpl(String type) {
    client = getClient(type);
  }

  /**
   * 获取钱包客户端
   * <p>
   * TODO
   * </p>
   *
   * @author: shangxl
   * @param: @param type
   * @param: @return
   * @return: BitcoinJSONRPCClient
   * @Date : 2017年11月15日 上午11:43:55
   * @throws:
   */
  public static BitcoinJSONRPCClient getClient(String type) {
    type = type.toLowerCase();
    if (mapClient != null && mapClient.get(type) != null) {
      return mapClient.get(type);
    } else {
      if (mapClient == null || mapClient.get(type) == null) {
        if (mapClient == null) {
          mapClient = new HashMap<String, BitcoinJSONRPCClient>(16);
        }
        LogFactory.info("获取" + type + "的rpc连接");
        String protocol = Properties.appcoinMap().get(type + "_protocol");
        String ip = Properties.appcoinMap().get(type + "_ip");
        String port = Properties.appcoinMap().get(type + "_port");
        String rpcuser = Properties.appcoinMap().get(type + "_rpcuser");
        String rpcpassword = Properties.appcoinMap().get(type + "_rpcpassword");
        // 判断是否有空值
        String[] uri = {protocol, ip, port, rpcuser, rpcpassword};
        String rpcURI =
            protocol + "://" + rpcuser + ":" + rpcpassword + "@" + ip + ":" + port + "/";
        for (String l : uri) {
          if (StringUtils.isEmpty(l)) {
            mapClient.put(type, null);
            LogFactory.info(type + "币接口配置参数无效请检查-----" + rpcURI);
            return null;
          }
        }
        try {
          mapClient.put(type, new BitcoinJSONRPCClient(rpcURI));
        } catch (Exception e) {
          mapClient.put(type, null);
//					e.printStackTrace();
          LogFactory.info("建立连接异常-----" + rpcURI);
        }
      }
    }
    return mapClient.get(type);
  }

  /**
   * 创建币 账户
   */
  @Override
  public String createNewAddress(String userName) {
    try {
      if (StringUtils.isNotEmpty(userName)) {
        return client.getNewAddress(userName);
      } else {
        return client.getNewAddress();
      }
    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
      e.printStackTrace();
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public double getBalance(String account) {
    double balance = 0;
    try {
      if (account != null) {
        balance = client.getBalance(account);
      } else {
        balance = client.getBalance();
      }

    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
//			e.printStackTrace();
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
//			e.printStackTrace();
    }
    return balance;
  }


  /**
   * 查询一定数量的流水记录 account 交易所的用户账户
   */
  @Override
  public List<Transaction> listTransactions(String account, Integer count) {
    try {
      if (StringUtils.isNotEmpty(account) && count != null) {
        return client.listTransactions(account, count);
      } else if (StringUtils.isNotEmpty(account) && count == null) {
        return client.listTransactions(account);
      } else {
        return client.listTransactions();
      }
    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
//			e.printStackTrace();
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
//			e.printStackTrace();
    }
    return null;
  }

  /**
   * account 交易所的用户账户
   *
   * 查询流水记录 count 数量 from 从哪开始
   */
  @Override
  public List<Transaction> listTransactions(String account, int count, int from) {
    try {
      return client.listTransactions(account, count, from);
    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
//			e.printStackTrace();
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
//			e.printStackTrace();
    }
    return null;
  }

  @Override
  public String sendFrom(String fromAddress, String toBitcoinAddress, double amount) {
    JsonResult result = new JsonResult();
    String isvalid = "isvalid";
    String validate = null;
    try {
      validate = validateAddress(fromAddress);
      validate = validate.replace(" ", "");
      Map<String, Object> map = StringUtil.str2map(validate);
      if (map.get(isvalid).toString() != null && Boolean
          .parseBoolean(map.get(isvalid).toString())) {
        String r = client.sendFrom(fromAddress, toBitcoinAddress, amount);
        LogFactory.info("转币返回结果：===== : " + r);
        result.setSuccess(true);
        result.setMsg(r);
      } else {
        result.setMsg("地址无效");
      }
    } catch (BitcoinException e) {
      result.setMsg("钱包接口返回错误");
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
      result.setMsg("client为空");
    }
    return JSON.toJSONString(result);
  }

  /**
   * 查询余额
   */
  @Override
  public double getBalance() {
    try {
      return client.getBalance();
    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
//			e.printStackTrace();
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
//			e.printStackTrace();
    }
    return Double.valueOf("0");
  }

  /**
   * 查询交易信息 订单的信息 txid 币服务器中的订单号
   */
  @Override
  public String getRawTransaction(String txId) {
    String ret = "";
    try {
      ret = client.getRawTransaction(txId).toString();
    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
    }
    return ret;
  }

  @Override
  public String validateAddress(String address) {
    String ret = "";
    try {
      ret = client.validateAddress(address).toString();
    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
      e.printStackTrace();
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
      e.printStackTrace();
    }
    return ret;
  }

  @Override
  public Map<String, Number> listaccounts() {
    Map<String, Number> ret = null;
    try {
      ret = client.listAccounts();
    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
//			e.printStackTrace();
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
//			e.printStackTrace();
    }
    return ret;
  }

  @Override
  public List<String> getAddressesByAccount(String account) {
    List<String> ret = null;
    try {
      ret = client.getAddressesByAccount(account);

    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
//			e.printStackTrace();
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
//			e.printStackTrace();
    }
    return ret;
  }


  @Override
  public double getReceivedByAddress(String address) {
    double ret = 0.00;
    try {
      ret = client.getReceivedByAddress(address);
    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
      // e.printStackTrace();
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
      // e.printStackTrace();
    }
    return ret;
  }

  @Override
  public String sendFromByAccount(String account, String toBitcoinAddress, double amount) {
    String ret = "";
    try {
      ret = client.sendFrom(account, toBitcoinAddress, amount);
    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
    }
    System.out.println("转币操作：" + ret);
    return ret;
  }

  @Override
  public String getAccount(String address) {
    try {
      return client.getAccount(address);
    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
    }
    return null;
  }

  @Override
  public JsonResult sendtoAddress(String toaddress, Double amount) {
    JsonResult result = new JsonResult();
    try {
      String hash = client.sendToAddress(toaddress, amount);
      if (StringUtils.isNotEmpty(hash)) {
        result.setSuccess(true);
        result.setMsg(hash);
      }
    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
      result = CommonUtil.analysisBitcoinException(e.getMessage());
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
//			e.printStackTrace();
    }
    return result;
  }


  @Override
  public String sendfrom(String fromAddress, String toBitcoinAddress, double amount) {
    try {
      return client.sendFrom(fromAddress, toBitcoinAddress, amount);
    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
//			e.printStackTrace();
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
//			e.printStackTrace();
    }
    return null;
  }


  /**
   * 获取所有用户地址（未经过测试）
   * <p> TODO</p>
   *
   * @author: shangxl
   * @param: @return
   * @return: Map<String               ,               Number>
   * @Date :          2017年12月25日 下午4:32:59
   * @throws:
   */
  public Map<String, Number> listAccounts() {
    try {
      return client.listAccounts();
    } catch (BitcoinException e) {
      LogFactory.info("钱包接口返回错误");
    } catch (NullPointerException e) {
      LogFactory.info("client为空");
    }
    return null;
  }


  @Override
  public JsonResult sendFrom(String amount, String toAddress) {
    JsonResult result = new JsonResult();

    // 提币数量
    //btc旷工费预计需要这么多
    Double fee = new Double("0.0005");
    Double money = Double.valueOf(amount);
    Double totalMoney = this.getBalance();
    Double needfunds = money + fee;
    // 提币账号余额比较提币金额
    if (totalMoney.compareTo(needfunds) >= 0) {
      // jsonrpc接口调用
      result = this.sendtoAddress(toAddress, money);
    } else {
      result.setSuccess(false);
      result.setMsg("提币账户可用余额不足");
    }
    return result;
  }

  @Override
  public Wallet getWalletInfo(String coinCode) {
    Wallet wallet = new Wallet();
    // 提币账户余额为总账户余额
    Double balance = this.getBalance();
    if (balance != null) {
      BigDecimal money = BigDecimal.valueOf(balance);
      String total = money.toString();
      String toMoney = total;
      String withdrawalsAddress = "-";
      String coldwalletAddress = Properties.appcoinMap()
          .get(coinCode.toLowerCase() + Properties.COLDADDERSS);
      wallet.setCoinCode(coinCode);
      wallet.setColdwalletAddress(coldwalletAddress == null ? "" : coldwalletAddress);
      wallet.setWithdrawalsAddress(withdrawalsAddress);
      wallet.setWithdrawalsAddressMoney(toMoney);
      wallet.setTotalMoney(total);
    }
    return wallet;
  }
}
