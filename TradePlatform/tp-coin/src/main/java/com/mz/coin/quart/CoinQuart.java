package com.mz.coin.quart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.Constant;
import com.mz.coin.Tvtx;
import com.mz.coin.TvtxAmount;
import com.mz.coin.TvtxLedgerEntries;
import com.mz.coin.bds.BdsRpcHttpClient;
import com.mz.coin.bds.BdsServerImpl;
import com.mz.coin.bds.BtsServerImpl;
import com.mz.coin.bds.GxsServerImpl;
import com.mz.coin.eth.service.impl.EtcService;
import com.mz.coin.eth.service.impl.EtherService;
import com.mz.coin.eth.service.impl.EtzService;
import com.mz.coin.htl.HtlEntity;
import com.mz.coin.htl.service.HtlServiceImpl;
import com.mz.coin.neo.NeoEntity;
import com.mz.coin.neo.NeoServiceImpl;
import com.mz.coin.transaction.model.AppCoinTransactionTv;
import com.mz.coin.transaction.service.AppCoinTransactionTvService;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.ex.digitalmoneyAccount.service.ExDigitalmoneyAccountService;
import com.mz.util.QueryFilter;
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import com.mz.utils.CommonUtil;
import com.mz.utils.Properties;
import com.mz.coin.coin.model.AppCoinTransaction;
import com.mz.coin.coin.model.CoinTransaction;
import com.mz.coin.coin.service.AppCoinTransactionService;
import com.mz.coin.coin.service.CoinTransactionService;
import com.mz.coin.tea.service.TEAService;
import com.mz.coin.tv.TvUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: shangxl
 * @Date : 2017年7月26日 上午11:23:33
 */
@Component
public class CoinQuart {

  @Autowired
  TEAService teaService;

  /**
   * <p>
   * TODO
   * </p>
   *
   * @author: shangxl
   * @param:
   * @return: void
   * @Date : 2017年3月15日 下午5:05:22
   * @throws:
   */
  public void productionTx() {
    String[] arrcoin = Properties.listCoinBasedBtc();// 基于比特币币种类型
    CoinTransactionService txService = (CoinTransactionService) ContextUtil
        .getBean("coinTransactionService");
    if (arrcoin != null) {
      for (String type : arrcoin) {
        if (type.equalsIgnoreCase(Constant.USDT)) {
          txService.omniRecordTransaction(type, null, 0, 0, 0, 0);
        } else {
          txService.recordTransaction(type, "*", Integer.valueOf(20));
        }
      }
    }
  }

  /**
   * <p>
   * TODO
   * </p>
   *
   * @author: shangxl
   * @param:
   * @return: void
   * @Date : 2017年3月15日 下午5:06:41
   * @throws:
   */
  public void consumeTx() {
    CoinTransactionService txService = (CoinTransactionService) ContextUtil
        .getBean("coinTransactionService");
    txService.consumeTransaction();
  }

  /**
   * <p>
   * TODO tv币充币记录生产
   * </p>
   *
   * @author: shangxl
   * @param:
   * @return: void
   * @Date : 2017年12月1日 下午3:25:21
   * @throws:
   */
  public void tvProduceTx() {
    System.out.println("TV produce tx start");
//		Long count = 30L;
    Long endblock = TvUtil.getBlockCount();
    Long startblock = 0L;
    String coinType = Constant.TV.toUpperCase();
    //区块链数据
    JSONArray arr = TvUtil.getTransactionDetail(startblock, endblock, null);
    if (arr != null && arr.size() != 0) {
      ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService) ContextUtil
          .getBean("exDigitalmoneyAccountService");
      // 查询出TV所有用户的accountNumber
      List<String> listAccountNumber = exDigitalmoneyAccountService
          .listAccountNumByCoinCode(Constant.TV.toUpperCase());
      // 公钥list不能为空
      if (listAccountNumber != null && listAccountNumber.size() > 0) {
//				StringBuffer accountNumbers = CommonUtil.list2Stringbuffer(listAccountNumber, ":");
        StringBuffer accountNumbers = new StringBuffer();
        // 区块链数据转化为对象list
        List<Tvtx> list = JSON.parseArray(arr.toJSONString(), Tvtx.class);
        if (list != null && list.size() > 0) {
          AppCoinTransactionTvService transactiontvservice = (AppCoinTransactionTvService) ContextUtil
              .getBean("appCoinTransactionTvService");
          // 获取已经确认的txid
          List<String> listTxId = transactiontvservice.listTxIdByIsUse(1);
          if (listTxId != null && listTxId.size() > 0) {
//						StringBuffer txIds = CommonUtil.list2Stringbuffer(listTxId, ":");
            StringBuffer txIds = new StringBuffer();
            for (Tvtx l : list) {
              String ledger = l.getLedger_entries();
              //是否区块确认
              String isconfirmed = l.getIs_confirmed();
              String trxId = l.getTrx_id();
              String blockNum = l.getBlock_num();
              //非空、txid未保存过
              if (Boolean.valueOf(isconfirmed) && StringUtils.isNotEmpty(ledger)
                  && txIds.indexOf(trxId) < 0) {
                // System.out.println(Json.toJson(ledger));
                List<TvtxLedgerEntries> txs = JSON.parseArray(ledger, TvtxLedgerEntries.class);
                if (txs != null && txs.size() > 0 && txs.get(0) != null) {
                  TvtxLedgerEntries tx = txs.get(0);
                  String amountjson = tx.getAmount();
                  String fromAddress = tx.getFrom_account();
                  String toAccount = tx.getTo_account();
                  //手续费
                  BigDecimal fee = BigDecimal.ZERO;
                  String feestr = l.getFee();
                  JSONObject feeObj = JSON.parseObject(feestr);
                  String feeamountstr = feeObj.get("amount").toString();
                  if (StringUtils.isNotEmpty(feeamountstr)) {
                    fee = new BigDecimal(feeamountstr).divide(TvUtil.RATE);
                  }
                  // 非空、并且属于平台上AccountNumber
                  if (StringUtils.isNotEmpty(amountjson) && StringUtils.isNotEmpty(fromAddress)
                      && StringUtils.isNotEmpty(toAccount)
                      && accountNumbers.indexOf(toAccount.toUpperCase()) >= 0) {
                    TvtxAmount am = JSON.parseObject(amountjson, TvtxAmount.class);
                    String amountNum = am.getAmount();
                    if (StringUtils.isNotEmpty(amountNum)) {
                      BigDecimal amount = new BigDecimal(amountNum).divide(TvUtil.RATE);
                      AppCoinTransactionTv tv = new AppCoinTransactionTv();
                      tv.setType(coinType);
                      tv.setBlockNum(Integer.valueOf(blockNum));
                      tv.setTrxId(trxId);
                      tv.setFromAddress(fromAddress);
                      tv.setToAccount(toAccount);
                      tv.setAmount(amount);
                      tv.setIsUse(0);
                      tv.setFee(fee);
                      tv.setIsconfirmed(1);//区块链已确认
                      transactiontvservice.save(tv);
                    } else {
                      // System.out.println("TV充币数量为空:amountNum"
                      // +
                      // JSON.toJSON(amountNum));
                    }
                  }
                } else {
                  // System.out.println("分类项目为空" +
                  // JSON.toJSON("账目数量"+txs.size()));
                }
              } else {
                // System.out.println("该数据已被保存" +
                // JSON.toJSON("trxId："+trxId));
              }
            }
            System.out.println("TV produce tx end");
          }
        }
      } else {
        System.out.println("TV accountNumber get result is null");
      }
    }
  }

  /**
   * 消费tv充币数据
   * <p>
   * TODO
   * </p>
   *
   * @author: shangxl
   * @param:
   * @return: void
   * @Date : 2018年4月11日 下午3:33:59
   * @throws:
   */
  public void tvConsumeTx() {
    LogFactory.info("TV consume tx start");
    AppCoinTransactionTvService transactiontvservice = (AppCoinTransactionTvService) ContextUtil
        .getBean("appCoinTransactionTvService");
    //获取未消费的记录
    List<AppCoinTransactionTv> listTv = transactiontvservice.listTxByIsUse(0);
    if (listTv != null && listTv.size() > 0) {
      CoinTransactionService coinTransactionService = (CoinTransactionService) ContextUtil
          .getBean("coinTransactionService");
      for (AppCoinTransactionTv tv : listTv) {
        // 将tv转化为transatcion类型
        CoinTransaction transaction = new CoinTransaction();
        String time = System.currentTimeMillis() + "";
        time = time.substring(0, time.length() - 3);
        String address = TvUtil.getAccountPublicAddress(tv.getToAccount());
        transaction.setTime(time);
        transaction.setTimeReceived(time);
        transaction.setBlockTime(time);
        transaction.setAddress(address);
        transaction.setCoinType(Constant.TV.toUpperCase());
        transaction.setConfirmations(1);
        transaction.setTxId(tv.getTrxId());
        transaction.setFee(TvUtil.getTxFee(tv.getAmount()).doubleValue());
        transaction.setAmount(tv.getAmount().doubleValue());
        JsonResult result = coinTransactionService.rechargecoin(transaction);
        if (result.getSuccess()) {
          QueryFilter filter = new QueryFilter(AppCoinTransactionTv.class);
          filter.addFilter("trxId=", tv.getTrxId());
          AppCoinTransactionTv t = transactiontvservice.get(filter);
          t.setIsUse(1);
          transactiontvservice.update(t);
        } else {
          LogFactory.info("充币返回结果：" + JSON.toJSONString(result));
        }
      }
    }

    LogFactory.info("TV consume tx start");
  }

  /**
   * tv币币归集提币账户
   * <p>
   * TODO
   * </p>
   *
   * @author: shangxl
   * @param:
   * @return: void
   * @Date : 2017年12月5日 下午6:50:25
   * @throws:
   */
  public void tvSend2coinbase() {
    ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService) ContextUtil
        .getBean("exDigitalmoneyAccountService");
    String wtAccount = TvUtil.withdAccount;
    String wtAddress = TvUtil.getAccountPublicAddress(wtAccount);
    String type = Constant.TV.toUpperCase();
    List<String> list = exDigitalmoneyAccountService.listAccountNumByCoinCode(type);
    // LogFactory.info("用户地址list="+JSON.toJSON(list));
    for (String l : list) {
      l = l.toLowerCase();
      BigDecimal hotMoney = TvUtil.getbalance(l);
      BigDecimal fee = TvUtil.getTxFee(hotMoney);
      // LogFactory.info("归集明细1： hotMoney="+hotMoney+" l="+l);
      if (hotMoney != null && hotMoney.compareTo(fee) > 0) {
        String amount = hotMoney.subtract(fee).toString();
        amount = CommonUtil.strRoundDown(amount, TvUtil.PRECISION);
        // LogFactory.info("归集明细2： amount="+amount+" 提币公钥="+wtAddress);
        TvUtil.sendCoinToAddr(l, type, amount, wtAddress, "userAccount2wtAccount");
      }
    }
  }

  /**
   * 以太坊及代币、以太经典充币记录刷新
   * <p>
   * TODO
   * </p>
   *
   * @author: Shangxl
   * @param:
   * @return: void
   * @Date : 2017年9月12日 下午8:30:17
   * @throws:
   */
  public void gethConsumeTx() {
    AppCoinTransactionService txService = (AppCoinTransactionService) ContextUtil
        .getBean("appCoinTransactionService");
    List<AppCoinTransaction> list = txService.consumeTx();
    if (list != null && list.size() > 0) {
      CoinTransactionService coinTransactionService = (CoinTransactionService) ContextUtil
          .getBean("coinTransactionService");
      for (AppCoinTransaction l : list) {
        CoinTransaction tx = new CoinTransaction();
        tx.setAddress(l.getTo_());
        tx.setAmount(l.getAmount().doubleValue());
        tx.setCoinType(l.getCoinType());
        tx.setFee(0);
        tx.setTxId(l.getHash_());
        JsonResult result = coinTransactionService.rechargecoin(tx);
        if (result.getSuccess()) {
          l.setIsconsume(1);
          txService.update(l);
        }
      }
    }
  }

  /**
   * <p>
   * bds充币定时器
   * </p>
   *
   * @author: shangxl
   * @param:
   * @return: void
   * @Date : 2018年1月10日 下午3:51:30
   * @throws:
   */
  public void bdsConsumeTx() {
    LogFactory.info("bds充币定时器执行");
    BdsServerImpl bdsServerImpl = new BdsServerImpl();
    String accountName = BdsRpcHttpClient.CHARGE_ACCOUNT;
    String count = "99999";
    String password = BdsRpcHttpClient.WALLET_PASSWORD;
    String id = BdsRpcHttpClient.id;
    // 记录到数据库
    bdsServerImpl.getAccountHistory(accountName, count, id, password);
  }

  /**
   * bts充币定时器
   * <p>
   * TODO
   * </p>
   *
   * @author: shangxl
   * @param:
   * @return: void
   * @Date : 2018年3月5日 下午2:00:16
   * @throws:
   */
  public void btsConsumeTx() {
    BtsServerImpl btServerImpl = new BtsServerImpl();
    btServerImpl.consumeTx();
  }

  /**
   * gxs充币定时器
   * <p>
   * TODO
   * </p>
   *
   * @author: shangxl
   * @param:
   * @return: void
   * @Date : 2018年3月5日 下午2:00:16
   * @throws:
   */
  public void gxsConsumeTx() {
    GxsServerImpl gxsServerImpl = new GxsServerImpl();
    gxsServerImpl.consumeTx();
  }

  /**
   * neo充值业务
   * <p>
   * TODO
   * </p>
   *
   * @author: shangxl
   * @param:
   * @return: void
   * @Date : 2018年1月22日 下午4:22:11
   * @throws:
   */
  public void neoConsumeTx() {
    NeoServiceImpl neoServer = new NeoServiceImpl();
    int endBlockNumber = neoServer.getblockcount() - 1;
    int startBlockNumber = endBlockNumber - 10;
    List<CoinTransaction> listTransaction = new ArrayList<>();
    for (int i = endBlockNumber; i >= startBlockNumber; i--) {
      List<NeoEntity> list = neoServer.getblock(i + "");
      for (NeoEntity l : list) {
        CoinTransaction transaction = new CoinTransaction();
        transaction.setTime("");
        transaction.setTimeReceived("");
        transaction.setBlockTime("");
        transaction.setAddress(l.getAddress());
        transaction.setCoinType(Constant.BDS);
        transaction.setConfirmations(1);
        transaction.setTxId(l.getTxId());
        transaction.setTxIdType(Constant.NEO);
        transaction.setFee(0d);
        transaction.setAmount(Double.valueOf(l.getValue()));
        transaction.setBlockIndex(Integer.valueOf(l.getN()));
        listTransaction.add(transaction);
      }
    }
  }

  /**
   * htl交易记录产生
   * <p>
   * TODO
   * </p>
   */
  public void htlConsumeTx() {
    HtlServiceImpl htlService = new HtlServiceImpl();
    int endBlockNumber = htlService.getblockcount() - 1;
    int startBlockNumber = endBlockNumber - 10;
    List<CoinTransaction> listTransaction = new ArrayList<>();
    for (int i = endBlockNumber; i >= startBlockNumber; i--) {
      List<HtlEntity> list = htlService.getblock(i + "");
      for (HtlEntity l : list) {
        CoinTransaction transaction = new CoinTransaction();
        transaction.setTime("");
        transaction.setTimeReceived("");
        transaction.setBlockTime("");
        transaction.setAddress(l.getAddress());
        transaction.setCoinType(Constant.BDS);
        transaction.setConfirmations(1);
        transaction.setTxId(l.getTxId());
        transaction.setTxIdType(com.mz.coin.utils.Constant.HTL);
        transaction.setFee(0d);
        transaction.setAmount(Double.valueOf(l.getValue()));
        transaction.setBlockIndex(Integer.valueOf(l.getN()));
        listTransaction.add(transaction);
      }
    }
  }

  /**
   * 以太坊、代币交易记录生产
   * <p>
   * TODO
   * </p>
   *
   * @author: shangxl
   * @param:
   * @return: void
   * @Date : 2018年1月26日 下午8:15:00
   * @throws:
   */
  public void etherProductionTx() {
    EtherService.etherProductionTx();
  }

  /**
   * 以太经典交易记录生产
   * <p>
   * TODO
   * </p>
   *
   * @author: shangxl
   * @param:
   * @return: void
   * @Date : 2018年1月26日 下午8:15:00
   * @throws:
   */
  public void etcproductionTx() {
    EtcService.productionTx();
  }

  /**
   * 以太经典交易记录生产
   * <p>
   * TODO
   * </p>
   *
   * @author: shangxl
   * @param:
   * @return: void
   * @Date : 2018年1月26日 下午8:15:00
   * @throws:
   */
  public void etzProductionTx() {
    EtzService.productionTx();
  }

  /**
   * 记录钱包信息到redis中
   */
  public void recordWalletInfoToRedis() {
    CoinTransactionService txService = (CoinTransactionService) ContextUtil
        .getBean("coinTransactionService");
    txService.listWalletByredis();
  }

  /**
   * TEA归集
   */
  public void teaCollection() {
    teaService.collection();
  }

  /**
   * TEA交易记录同步
   */
  public void teaCoinSync() {
    teaService.coinSync();
  }
}
