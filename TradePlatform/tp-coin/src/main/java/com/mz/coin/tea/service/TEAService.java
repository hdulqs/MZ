package com.mz.coin.tea.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.coin.Wallet;
import com.mz.coin.tea.Assets;
import com.mz.coin.tea.Method;
import com.mz.coin.tea.Response;
import com.mz.coin.tea.TEAClientProxy;
import com.mz.coin.tea.Transactions;
import com.mz.core.constant.StringConstant;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.redis.common.utils.RedisService;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import javax.transaction.Transactional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by Frank on 2018/9/3.
 *
 * TEAjie
 */
@Service("teaService")
public class TEAService {

  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  RedisService redisService;

  private static final Logger logger = LoggerFactory.getLogger(TEAService.class);

  private static final String ASSET_NAME = "teacoin";
  private static final String LAST_TIME_TANS_INDEX = "tea_lastTimeTransIndex";
  private static final int TRANS_COUNT = 1000000;

  /**
   * 生成地址
   */
  public String getnewaddress() {
    String response = new TEAClientProxy()
        .invoke(Method.GETNEWADDRESS.getValue(), new ArrayList<>());
    String address = JSON.parseObject(response).getString("result");
    return address;
  }

  /**
   * 查询余额
   */
  public BigDecimal getaddressbalances(String address) {
    List<Object> params = new LinkedList<>();
    params.add(address);
    String response = new TEAClientProxy().invoke(Method.GETADDRESSBALANCES.getValue(), params);
    JSONArray result = (JSONArray) (JSON.parseObject(response, Response.class).getResult());
    Assets assets = null;
    if (result != null && result.size() > 0) {
      assets = JSON.toJavaObject((JSONObject) result.get(0), Assets.class);
    }
    BigDecimal balance = assets != null ? assets.getQty() : BigDecimal.ZERO;
    return balance;
  }

  /**
   * 转账
   */
  public JsonResult sendassetfrom(String fromAddress, String toAddress, BigDecimal quantity) {
    List<Object> params = new LinkedList<>();
    fromAddress = StringUtils.isBlank(fromAddress) ? this.getaddresses().get(0) : fromAddress;
    params.add(fromAddress);
    params.add(toAddress);
    params.add(ASSET_NAME);
    params.add(quantity);
    String res = new TEAClientProxy().invoke(Method.SENDASSETFROM.getValue(), params);
    Response response = JSON.parseObject(res, Response.class);
    if (StringUtils.isNotBlank(String.valueOf(response.getResult()))) {
      return setJsonResult(StringConstant.SUCCESS, true, String.valueOf(response.getResult()));
    }
    return setJsonResult(StringConstant.FAIL, false, null);
  }

  /**
   * 查询一个地址的交易记录
   */
  public List<Transactions> listaddresstransactions(String address, Integer count, Integer skip) {
    List<Object> params = new ArrayList<>();
    params.add(address);
    params.add(count);
    params.add(skip);
    List<Transactions> transactions = JSON
        .toJavaObject((JSONArray) (JSON.parseObject(new TEAClientProxy().invoke(
            Method.LISTADDRESSTRANSACTIONS.getValue(), params), Response.class).getResult()),
            List.class);
    return transactions;
  }

  /**
   * 查询所有交易记录（不提供数量，弃用）
   */
  public List<Transactions> listwallettransactions(Integer count, Integer skip) {
    List<Object> params = new ArrayList<>();
    params.add(count);
    params.add(skip);
    List<Transactions> transactions = JSON
        .toJavaObject((JSONArray) (JSON.parseObject(new TEAClientProxy().invoke(
            Method.LISTWALLETTRANSACTIONS.getValue(), params), Response.class).getResult()),
            List.class);
    return transactions;
  }


  /**
   * 查询本地钱包的资产总额
   */
  public BigDecimal gettotalbalances() {
    String response = new TEAClientProxy().invoke(Method.GETTOTALBALANCES.getValue(),
        new ArrayList());
    Assets result = JSON.toJavaObject(
        (JSONObject) (((JSONArray) JSON.parseObject(response, Response.class).getResult()).get(0)),
        Assets.class);
    BigDecimal balance = result != null ? result.getQty() : BigDecimal.ZERO;
    return balance;
  }

  /**
   * 获取所有地址
   */
  public List<String> getaddresses() {
    String response = new TEAClientProxy().invoke(Method.GETADDRESSES.getValue(),
        new ArrayList());
    List<String> address = JSON.parseObject(response).getObject("result", List.class);
    return address;
  }

  /**
   * 查询提币余额及资产总额
   */
  public Wallet listWalletBalance() {
    Wallet wallet = new Wallet();
    wallet.setCoinCode("TEA");
    String address = getaddresses().get(0);
    wallet.setWithdrawalsAddress(address);
    wallet.setWithdrawalsAddressMoney(String.valueOf(getaddressbalances(address)));
    wallet.setTotalMoney(String.valueOf(gettotalbalances()));
    return wallet;
  }

  /**
   * 单个用户刷币
   */
  @Transactional
  public JsonResult refreshUserCoin(String address, Integer count, Integer skip) {
    logger.info("TEA" + address + "刷币，从" + skip + "起" + count + "条.....");
    List transactionsList = this.listaddresstransactions(address, TRANS_COUNT, 0);
    int size = transactionsList.size();
    if (CollectionUtils.isNotEmpty(transactionsList)) {
      transactionsList = this.filterTrans(transactionsList, skip);
      this.saveTrans(transactionsList);
      logger.info("TEA" + address + "刷币成功，获取数据" + (size - skip) + "条.....");
      return setJsonResult(StringConstant.SUCCESS, true, "TEA单个用户刷币成功");
    }
    return setJsonResult(StringConstant.FAIL, false, "TEA单个用户刷币失败或无数据");
  }

  /**
   * 归集
   */
  public void collection() {
    List<String> addresses = this.getaddresses();
    logger.info("TEA归集开始,收集" + addresses.size() + "个地址.....");
    final String toAddress = addresses.get(0);
    IntStream.range(0, addresses.size()).forEach(i -> {
      String fromAddress;
      if (i == 0) {
        return;
      } else {
        fromAddress = addresses.get(i);
      }
      BigDecimal balance = this.getaddressbalances(fromAddress);
      if (StringUtils.isNotBlank(fromAddress) && StringUtils.isNotBlank(toAddress)
          && balance.compareTo(BigDecimal.ZERO) > 0) {
        this.sendassetfrom(fromAddress, toAddress, balance);
        logger.info(fromAddress + "归集到" + toAddress + "币" + balance + "个.....");
      }
    });
  }

  /**
   * 交易记录同步
   */
  @Transactional
  public void coinSync() {
    String lastTimeIndex =
        StringUtils.isNotBlank(redisService.get(LAST_TIME_TANS_INDEX)) ? redisService
            .get(LAST_TIME_TANS_INDEX) : "0";
    logger.info("TEA交易同步，从" + lastTimeIndex + "起.....");
    List transactionsList = this.listwallettransactions(TRANS_COUNT, 0);
    if (CollectionUtils.isNotEmpty(transactionsList)) {
      redisService.save(LAST_TIME_TANS_INDEX, String.valueOf(transactionsList.size()));
      transactionsList = this.filterTrans(transactionsList, Integer.parseInt(lastTimeIndex));
      this.saveTrans(transactionsList);
      logger.info(
          "TEA交易同步成功，获取数据" + transactionsList.size() + "条.....");
    } else {
      logger.info("TEA交易记录同步失败或无数据.....");
    }
  }

  private JsonResult setJsonResult(String code, Boolean isSuccess, String msg) {
    JsonResult jsonResult = new JsonResult();
    jsonResult.setSuccess(isSuccess);
    if (StringUtils.isNotBlank(code)) {
      jsonResult.setCode(code);
    }
    if (StringUtils.isNotBlank(msg)) {
      jsonResult.setMsg(msg);
    }
    return jsonResult;
  }

  /**
   * 过滤transactions，删除已处理或无效数据
   */
  private List filterTrans(List transactionsList, int skip) {
    Iterator<JSONObject> it = transactionsList.iterator();
    int i = 0;
    while (it.hasNext()) {
      Transactions transactions = JSON.toJavaObject(it.next(), Transactions.class);
      List<Assets> assetsList = transactions.getBalance().getAssets();
      Assets assets = CollectionUtils.isNotEmpty(assetsList) ? assetsList.get(0) : new Assets();
      String myAddress = transactions.getMyaddresses().get(0);
      BigDecimal qty = assets.getQty() != null ? assets.getQty() : BigDecimal.ZERO;
      if (i < skip || StringUtils.isBlank(myAddress) || qty
          .compareTo(BigDecimal.ZERO) <= 0) {
        it.remove();
      }
      i++;
    }
    return transactionsList;
  }

  /**
   * 保存交易数据
   */
  private void saveTrans(List transactionsList) {
    final String sql =
        "insert into coin_transaction(time,address,amount,blockHash,txId,coinType,fee,"
            + "confirmations,isCreateOrder,txIdType) values(?,?,?,?,?,?,?,?,?,?)";
    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public int getBatchSize() {
        return transactionsList.size();
      }

      @Override
      public void setValues(PreparedStatement preparedStatement, int i)
          throws SQLException {
        Transactions transactions = JSON
            .toJavaObject((JSONObject) transactionsList.get(i), Transactions.class);
        preparedStatement.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(Long.parseLong(transactions.getTime() + "000")));
        String addresses = transactions.getMyaddresses().get(0);
        List<Assets> assetsList = transactions.getBalance().getAssets();
        Assets assets = CollectionUtils.isNotEmpty(assetsList) ? assetsList.get(0) : null;
        BigDecimal amount = assets != null ? assets.getQty() : BigDecimal.ZERO;
        preparedStatement.setString(2, addresses);
        preparedStatement.setBigDecimal(3, amount);
        String blockHash = transactions.getBlockhash();
        preparedStatement.setString(4, blockHash);
        String txId = transactions.getTxid();
        preparedStatement.setString(5, txId);
        preparedStatement.setString(6, "TEA");
        preparedStatement.setBigDecimal(7, BigDecimal.ZERO);
        BigDecimal confirmations = transactions.getConfirmations();
        preparedStatement.setBigDecimal(8, confirmations);
        preparedStatement.setInt(9, 0);
        preparedStatement.setString(10, txId);
      }
    });
  }
}
