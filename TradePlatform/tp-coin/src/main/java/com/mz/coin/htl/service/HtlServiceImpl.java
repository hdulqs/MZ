package com.mz.coin.htl.service;

import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.coin.BtsServer;
import com.mz.coin.Wallet;
import com.mz.coin.coin.model.CoinTransaction;
import com.mz.coin.htl.HtlEntity;
import com.mz.coin.htl.HtlRpcHttpClient;
import com.mz.coin.htl.service.HtlService;
import com.mz.coin.utils.Constant;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.ex.digitalmoneyAccount.service.ExDigitalmoneyAccountService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import com.mz.util.sys.SpringContextUtil;
import com.mz.utils.Properties;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class HtlServiceImpl implements BtsServer, HtlService {

  private static Logger logger = LoggerFactory.getLogger(HtlServiceImpl.class);

  private static String htl_assetId = Properties
      .appcoinMap().get(Constant.HTL.toLowerCase() + "_assetid");

  /**
   * 根据指定的资产编号，返回钱包中对应资产的余额信息
   */
  @Override
  public BigDecimal getBalance(String accountName) {
    BigDecimal balance = BigDecimal.ZERO;
    String methodname = "getbalance";
    Object result = "";
    String amount_ = "";
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List paramlist = new ArrayList<>();
      Map addressMap = new HashMap(16);
      String id = "";
      addressMap = getaccountstate(accountName);
      if (null == addressMap) {
        LogFactory.info("未查询到账户资产信息");
        return balance;
      } else {
        id = addressMap.get("asset") + "";
      }
      if ("0".equals(id)) {
        LogFactory.info("未查询到账户资产信息");
        return balance;
      }
      paramlist.add('"' + id + '"');
      result = HtlRpcHttpClient.getHttpClient(methodname, paramlist);
      LogFactory.info(result + "");
      JSONObject json = JSONObject.parseObject(result + "");
      if (json == null) {

      } else {
        map = (Map<String, Object>) json.get("result");
        amount_ = map.get("balance") + "";
        balance = BigDecimal.valueOf(Double.valueOf(amount_));
      }
    } catch (Throwable e) {
      e.printStackTrace();
      LogFactory.error("查询HTL币余额有误");
    }

    return balance;
  }

  /**
   * <p>
   * 生成地址、备注
   * </p>
   *
   * @author: zzz
   * @param: @param userName
   * @param: @return
   * @return: String
   * @Date : 2018年1月9日 下午2:57:08
   * @throws:
   */
  @Override
  public String getPublicKey(String userName) {
    String methodname = "getnewaddress";
    Object result = "";
    String address = "";
    List list = new ArrayList<>();
    try {
      List paramlist = new ArrayList<>();
      result = HtlRpcHttpClient.getHttpClient(methodname, paramlist);
      LogFactory.info(result + "");
      JSONObject json = JSONObject.parseObject(result + "");
      address = json.get("result") + "";
    } catch (Throwable e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      LogFactory.error("生成HTL地址失败！");
    }
    return address;
  }

  @Override
  public boolean unlock(String password) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean lock() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void getAccountHistory(String accountName, String count, String id, String password) {
    // TODO Auto-generated method stub
  }

  @Override
  public String transfer(String fromAccount, String toAccount, String amount, String symbol,
      String memo) {
    return null;
  }

  /**
   * 根据账户地址，查询账户资产信息
   */
  @Override
  public Map getaccountstate(String address) {
    Map resultMap = new HashMap();
    String asset = "";
    String methodname = "getaccountstate";
    Object result = "";
    List list = new ArrayList<>();
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List paramlist = new ArrayList<>();
      paramlist.add('"' + address + '"');
      result = HtlRpcHttpClient.getHttpClient(methodname, paramlist);
      LogFactory.info(result + "");
      JSONObject json = JSONObject.parseObject(result + "");
      map = (Map<String, Object>) json.get("result");
      List<Map> listresult = new <Map>ArrayList();
      listresult = (List<Map>) map.get("balances");
      if (listresult == null || listresult.size() == 0) {
        return resultMap;
      } else {
        resultMap = listresult.get(0);
      }
    } catch (Throwable e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      LogFactory.error("查询账户资产信息失败！");
    }
    return resultMap;
  }

  /**
   * 根据指定的索引，返回对应的区块信息
   *
   * @param index 区块索引（区块高度） = 区块数 - 1。
   */
  @Override
  public List<HtlEntity> getblock(String index) {
    List<HtlEntity> listNeo = new ArrayList<HtlEntity>();
    List<Map> listtx = new ArrayList<Map>();
    String methodname = "getblock";
    Object result = "";
    String asset = "";
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List paramlist = new ArrayList<>();
      paramlist.add(index);
      paramlist.add(1);
      result = HtlRpcHttpClient.getHttpClient(methodname, paramlist);
      JSONObject json = JSONObject.parseObject(result + "");
      if (json != null) {
        map = (Map<String, Object>) json.get("result");
        listtx = (List<Map>) map.get("tx");
        if (null == listtx) {
          return listNeo;
        } else {
          HtlEntity htlEntity;
          for (int i = 1; i < listtx.size(); i++) {
            if ("ContractTransaction".equals(listtx.get(i).get("type"))) {
              //System.out.println("listtx.get(i)====================="+listtx.get(i));
              //LogFactory.info("htl查询区块信息返回-------------------"+result);
              Map mapVin = new HashMap();
              JSONArray txJson = (JSONArray) listtx.get(i).get("vout");
              String txId = (String) listtx.get(i).get("txid");
              //System.out.println("txId====================="+txId);
              mapVin = JSON.toJavaObject((JSONObject) txJson.get(0), Map.class);
              if (null == mapVin) {

              } else {
                htlEntity = new HtlEntity();
                htlEntity.setTxId(txId);
                htlEntity.setN(mapVin.get("n") + "");
                htlEntity.setAsset(mapVin.get("asset") + "");
                htlEntity.setValue(mapVin.get("value") + "");
                htlEntity.setAddress(mapVin.get("address") + "");
                listNeo.add(htlEntity);
              }

            }
          }
        }
      }
    } catch (Throwable e) {
      //LogFactory.error("查询HTL区块信息有误！");
      e.printStackTrace();
    }

    return listNeo;
  }

  @Override
  public boolean validateAddress(String validateaddress) {
    boolean flag = false;
    String methodname = "validateaddress";
    Object result = "";
    List list = new ArrayList<>();
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List paramlist = new ArrayList<>();
      paramlist.add('"' + validateaddress + '"');
      result = HtlRpcHttpClient.getHttpClient(methodname, paramlist);
      LogFactory.info(result + "");
      JSONObject json = JSONObject.parseObject(result + "");
      map = (Map<String, Object>) json.get("result");
      if ((boolean) map.get("isvalid")) {
        flag = true;
      }
    } catch (Throwable e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      LogFactory.error("HTL地址有误！");
    }
    return flag;
  }

  @Override
  public Map sendfrom(String asset_id, String to, String value, String fee) {
    Map mapresult = new HashMap();
    // 手续费fee
    if (null == fee || "".equals(fee)) {
      fee = "0";
    } else {

    }
    String asset = "";
    BigDecimal balance = BigDecimal.ZERO;
    String methodname = "sendtoaddress";
    Object result = "";
    String amount_ = "";
    Map<String, Object> map = new HashMap<String, Object>();
    List list = new ArrayList<>();
    try {
      List paramlist = new ArrayList<>();
      paramlist.add('"' + asset_id + '"');
      paramlist.add('"' + to + '"');
      paramlist.add(value);

      if (fee.equals("0")) {

      } else {
        paramlist.add(fee);
      }
      result = HtlRpcHttpClient.getHttpClient(methodname, paramlist);
      LogFactory.info(result + "");
      JSONObject json = JSONObject.parseObject(result + "");
      map = (Map<String, Object>) json.get("result");
      String txid = map.get("txid").toString();
      if (txid != null) {
        mapresult.put("txid", txid);
        mapresult.put("result", "true");
      } else {
        mapresult.put("result", "false");
      }
      // amount_=map.get("balance")+"";
      // balance = new BigDecimal(amount_).divide(BigDecimal.valueOf(100000l), 8,
      // BigDecimal.ROUND_DOWN);
    } catch (Throwable e) {
      e.printStackTrace();
      LogFactory.error("转币HTL失败");
    }

    return mapresult;
  }

  /**
   * 获取主链中区块的数量
   */
  @Override
  public int getblockcount() {
    int index = 0;
    List<HtlEntity> listNeo = new ArrayList<HtlEntity>();
    String methodname = "getblockcount";
    Object result = "";
    String asset = "";
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List paramlist = new ArrayList<>();
      result = HtlRpcHttpClient.getHttpClient(methodname, paramlist);
      LogFactory.info(result + "");
      JSONObject json = JSONObject.parseObject(result + "");
      if (json != null && json.get("result") != null) {
        index = Integer.parseInt(json.get("result") + "");
      }
    } catch (Throwable e) {
      e.printStackTrace();
      LogFactory.error("获取主链中区块的数量失败！");
    }

    return index;
  }

  @Override
  public JsonResult send2ColdAddress(String toAddress, String amount) {
    Map map = sendfrom(htl_assetId, toAddress, amount, "0");
    String re = map.get("result").toString();

    JsonResult result = new JsonResult();
    if ("true".equals(re)) {
      result.setSuccess(true);
      String txid = map.get("txid").toString();
      result.setMsg(txid);
    } else {
      result.setSuccess(false);
    }
    return result;
  }

  @Override
  public JsonResult sendFrom(String amount, String toAddress, String memo) {
    Map map = sendfrom(htl_assetId.trim(), toAddress, amount, "0");
    String re = map.get("result").toString();

    JsonResult result = new JsonResult();
    if ("true".equals(re)) {
      result.setSuccess(true);
      String txid = map.get("txid").toString();
      result.setMsg(txid);
    } else {
      result.setSuccess(false);
    }
    return result;
  }

  @Override
  public Wallet getWalletInfo() {
    Wallet wallet = new Wallet();
    //设置币种类型
    wallet.setCoinCode(Constant.HTL);
    //设置币种冷钱包地址
    String coldwalletAddres = Properties.appcoinMap()
        .get(Constant.HTL.toLowerCase() + Properties.COLDADDERSS);
    wallet.setColdwalletAddress(coldwalletAddres == null ? "" : coldwalletAddres);
    //设置提币地址
    wallet.setWithdrawalsAddress("-");

    List list = new ArrayList<>();
    list.add('"' + htl_assetId.trim() + '"');
    //System.out.println(list);
    String methodname = "getbalance";
    //查询钱包总余额
    Object htlwallet = HtlRpcHttpClient.getHttpClient(methodname, list);
    //System.out.println(htlwallet);
    JSONObject json = JSONObject.parseObject(htlwallet + "");
//        LogFactory.info(htlwallet + "");
    Map<String, Object> map = new HashMap<String, Object>();
    if (json != null) {
      map = (Map<String, Object>) json.get("result");
      wallet.setTotalMoney(map.get("balance").toString());
      wallet.setWithdrawalsAddressMoney(map.get("balance").toString());
    }
    return wallet;
  }

  @Override
  public void getRlativeAccountHistory(String accountName, String startnum, String count,
      String endnum) {
    // TODO Auto-generated method stub
  }

  @Transactional
  public JsonResult htlrecordTransaction() {
    try {
      RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
      ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService) ContextUtil
          .getBean("exDigitalmoneyAccountService");
      HtlServiceImpl htlService = new HtlServiceImpl();
      int endBlockNumber = htlService.getblockcount() - 1;
      String htl_nowBlock = redisService.get("HTL_NowBlock");
      List<String> li = exDigitalmoneyAccountService
          .listPublicKeyByCoinCode(Constant.HTL);
      if (htl_nowBlock == null) {
        redisService.save("HTL_NowBlock", "1");
        htl_nowBlock = "1";
      }
      int startBlockNumber = Integer.valueOf(htl_nowBlock);
      List<CoinTransaction> listTransaction = new ArrayList<>();
      logger.info("HTL同步数据开始。。。。。。。。。。。。。。。。。。。。。。。");
      for (int i = endBlockNumber; i >= startBlockNumber; i--) {
        List<HtlEntity> list = htlService.getblock(i + "");
        for (HtlEntity l : list) {
          String add = l.getAddress();
          //LogFactory.info(("区块地址"+li+"/核态地址"+add));
          if (li.contains(add)) {
            CoinTransaction transaction = new CoinTransaction();
            String time = System.currentTimeMillis() + "";
            time = time.substring(0, time.length() - 3);
            transaction.setTime(time);
            transaction.setTimeReceived(time);
            transaction.setBlockTime(time);
            transaction.setAddress(l.getAddress());
            transaction.setCoinType(Constant.HTL);
            transaction.setConfirmations(1);
            transaction.setTxId(l.getTxId());
            transaction.setTxIdType(l.getTxId());
            transaction.setFee(0d);
            transaction.setAmount(Double.valueOf(l.getValue()));
            transaction.setBlockIndex(Integer.valueOf(l.getN()));
            listTransaction.add(transaction);
          }
        }
      }
      logger.info("保存HTL数据" + listTransaction.size() + "条。。。。。。。。。。。。。。。。。。。。。。。");
      this.saveTrans(listTransaction);
      redisService.save("HTL_NowBlock", String.valueOf(endBlockNumber));
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return null;
  }

  private void saveTrans(List<CoinTransaction> transactionsList) throws RemotingException {
    JdbcTemplate jdbcTemplate = SpringContextUtil.getBean("jdbcTemplate");
    if (jdbcTemplate != null) {
      final String sql =
          "insert into coin_transaction(time,address,amount,blockHash,txId,coinType,fee," +
              "confirmations,isCreateOrder,txIdType,timeReceived,blockTime) " +
              "values(?,?,?,?,?,?,?,?,?,?,?,?)";
      jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
        @Override
        public int getBatchSize() {
          return transactionsList.size();
        }

        @Override
        public void setValues(PreparedStatement preparedStatement, int i)
            throws SQLException {
          CoinTransaction transactions = transactionsList.get(i);
          System.out.println("保存htl数据" + JSON.toJSONString(transactions));
          String time = System.currentTimeMillis() + "";
          time = time.substring(0, time.length() - 3);
          preparedStatement.setString(1, time);
          String addresses = transactions.getAddress();
          BigDecimal amount =
              transactions != null ? BigDecimal.valueOf(transactions.getAmount()) : BigDecimal.ZERO;
          preparedStatement.setString(2, addresses);
          preparedStatement.setBigDecimal(3, amount);
          String blockHash = transactions.getBlockHash();
          preparedStatement.setString(4, blockHash);
          String txId = transactions.getTxId();
          preparedStatement.setString(5, txId);
          preparedStatement.setString(6, "HTL");
          preparedStatement.setBigDecimal(7, BigDecimal.valueOf(transactions.getFee()));
          BigDecimal confirmations = BigDecimal.valueOf(transactions.getConfirmations());
          preparedStatement.setBigDecimal(8, confirmations);
          preparedStatement.setInt(9, 0);
          preparedStatement.setString(10, txId);
          preparedStatement.setString(11, time);
          preparedStatement.setString(12, time);
        }
      });
    }
  }
}
