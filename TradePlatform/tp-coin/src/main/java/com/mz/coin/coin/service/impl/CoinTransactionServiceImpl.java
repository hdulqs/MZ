/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年5月17日 上午11:21:53
 */
package com.mz.coin.coin.service.impl;

import com.azazar.krotjson.JSON;
import com.mz.Constant;
import com.mz.app.ourAccount.service.AppOurAccountService;
import com.mz.coin.CoinService;
import com.mz.coin.Wallet;
import com.mz.coin.bds.BdsServerImpl;
import com.mz.coin.bds.BtsServerImpl;
import com.mz.coin.bds.GxsServerImpl;
import com.mz.coin.coin.dao.CoinTransactionDao;
import com.mz.coin.coin.model.CoinTransaction;
import com.mz.coin.coin.service.CoinTransactionService;
import com.mz.coin.eth.service.impl.EtcService;
import com.mz.coin.eth.service.impl.EtherService;
import com.mz.coin.eth.service.impl.EtzService;
import com.mz.coin.htl.service.HtlServiceImpl;
import com.mz.coin.tea.service.TEAService;
import com.mz.coin.tv.TvUtil;
import com.mz.coin.utils.CoinEmpty;
import com.mz.coin.utils.CoinServiceImpl;
import com.mz.coin.utils.JsonrpcClient;
import com.mz.coin.utils.MyCoinService;
import com.mz.coin.utils.RedisUtil;
import com.mz.core.constant.StringConstant;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.ex.digitalmoneyAccount.model.ExDigitalmoneyAccount;
import com.mz.ex.digitalmoneyAccount.service.ExDigitalmoneyAccountService;
import com.mz.ex.dmTransaction.service.ExDmTransactionService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.StringUtil;
import com.mz.util.http.Httpclient;
import com.mz.util.log.LogFactory;
import com.mz.util.security.Check;
import com.mz.utils.Properties;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Yuan Zhicheng
 * @Date : 2016年5月17日 上午11:21:53
 */
@Service("coinTransactionService")
public class CoinTransactionServiceImpl extends BaseServiceImpl<CoinTransaction, Long> implements
    CoinTransactionService {

  @Resource(name = "appOurAccountService")
  private AppOurAccountService appOurAccountService;
  @Resource(name = "exDigitalmoneyAccountService")
  private ExDigitalmoneyAccountService exDigitalmoneyAccountService;
  @Resource(name = "exDmTransactionService")
  private ExDmTransactionService exDmTransactionService;
  @Resource(name = "redisService")
  private RedisService redisService;
  @Autowired
  TEAService teaService;

  @Resource(name = "coinTransactionDao")
  @Override
  public void setDao(BaseDao<CoinTransaction, Long> dao) {
    super.dao = dao;
  }

  @Override
  public int isExists(String txid) {
    return ((CoinTransactionDao) dao).isExists(txid);
  }

  @Override
  public int countByParams(String txidType, String account) {
    return ((CoinTransactionDao) dao).countByParams(txidType, account);
  }

  @Override
  public JsonResult recordTransaction(String type, String account, Integer count) {
    // 区块确认数
    int confirmations = 2;
    JsonResult result = new JsonResult();

    CoinService coinService = new CoinServiceImpl(type);
    List<com.azazar.bitcoin.jsonrpcclient.Bitcoin.Transaction> list = coinService
        .listTransactions(account, count);
    if (list != null && list.size() > 0) {
      String txStr = JSON.stringify(list);
      if (StringUtils.isNotEmpty(txStr) && txStr.startsWith("[")) {
        List<String> txList = (List<String>) JSON.parse(txStr);
        if (txList.size() != 0) {
          for (String txs : txList) {
            Map<String, Object> tx2map = StringUtil.str2map(txs);
            String json = com.alibaba.fastjson.JSON.toJSONString(tx2map);
            json = json.replaceAll(" ", "");
            CoinTransaction tx = com.alibaba.fastjson.JSON.parseObject(json, CoinTransaction.class);
            String txidtype = tx.getTxId() + "_" + tx.getCategory();
            if (countByParams(txidtype, tx.getAddress()) == 0
                && tx.getConfirmations() >= confirmations && "receive".equals(tx.getCategory())) {
              tx.setTxIdType(txidtype);
              tx.setCoinType(type);
              tx.setIsCreateOrder(0);
              save(tx);
            }
          }
          result.setCode(StringConstant.SUCCESS);
          result.setMsg("成功");
          result.setSuccess(true);
        } else {
          result.setCode(StringConstant.SUCCESS);
          result.setMsg("未查询到记录");
        }
      } else {
        result.setCode(StringConstant.FAIL);
        result.setMsg("钱包接口查询数据异常：" + txStr);
      }
    } else {
      LogFactory.info("未查询到充币记录      type=" + type);
    }
    return result;
  }

  @Override
  public JsonResult rechargecoin(CoinTransaction tx) {
    JsonResult jsonResult = new JsonResult();
    String url = Properties.appMap().get("notify_recharge_url");
    if (StringUtils.isNotEmpty(url)) {
      // 查询币账户
      String publicKey = tx.getAddress();
      ExDigitalmoneyAccount coinAccount = exDigitalmoneyAccountService
          .getExDigitalmoneyAccountByPublicKey(publicKey);
      if (coinAccount != null) {
        String coinCode = tx.getCoinType();
        Double txamount = tx.getAmount();
        Double txfee = tx.getFee();
        String transactionMoney = new BigDecimal(txamount.toString()).toPlainString();
        String fee = new BigDecimal(txfee != null ? txfee.toString() : "0").toPlainString();
        String userName = coinAccount.getUserName();
        String surname = coinAccount.getSurname();
        String trueName = coinAccount.getTrueName();
        String CurrencyType = coinAccount.getCurrencyType();
        String inAddress = coinAccount.getPublicKey();
        String orderNo = tx.getTxId();
        String remark = "Receive";

        Map<String, String> map = new HashMap<String, String>(16);
        map.put("transactionMoney", transactionMoney);
        map.put("fee", fee);
        map.put("username", userName);
        map.put("surname", surname);
        map.put("trueName", trueName);
        map.put("coinCode", coinCode);
        map.put("CurrencyType", CurrencyType);
        map.put("inAddress", inAddress);
        map.put("orderNo", orderNo);
        map.put("remark", remark);
        //安全校验
        String[] params = {transactionMoney, fee, userName, surname, trueName, coinCode,
            CurrencyType, inAddress, orderNo, remark};
        String authcode = Check.authCode(params);
        //校验码
        System.out.println("充币authcode为" + authcode);
        map.put("authcode", authcode);
        String result = Httpclient.post(url, map);
        if (StringUtils.isNotEmpty(result)) {
          return com.alibaba.fastjson.JSON.parseObject(result, JsonResult.class);
        }
      } else {
        LogFactory.info("publicKey=" + publicKey);
        LogFactory.info("coinAccount none existent");
        jsonResult.setMsg("coinAccount none existent");
      }
    } else {
      LogFactory.info("url=" + url);
      jsonResult.setMsg("app.properties file url not found");
    }
    return jsonResult;
  }

  @Override
  public List<CoinTransaction> findTransactionListIsRollOut(String coinType, int istIsRollOut,
      String category) {
    return ((CoinTransactionDao) dao)
        .findTransactionListIsRollOut(coinType, istIsRollOut, category);
  }

  @Override
  public List<CoinTransaction> findTransactionListByconfirm(String coinType, int isCreateOrder,
      String category) {
    return ((CoinTransactionDao) dao)
        .findTransactionListByconfirm(coinType, isCreateOrder, category);
  }

  @Override
  public boolean isexistHash(String hash) {
    BigDecimal count = ((CoinTransactionDao) dao).isexistHash(hash);
    return count.compareTo(BigDecimal.ZERO) > 0;
  }

  @Override
  public List<CoinTransaction> listUnconfirmed(String coinCode) {
    return ((CoinTransactionDao) dao).listUnconfirmed(coinCode);
  }


  @Override
  public JsonResult omniRecordTransaction(String coinType, String address, int count, int skip,
      int startblock, int endblock) {
    JsonResult result = new JsonResult();
    //获取USDT区块当前处理到的高度（即数据库已经匹配到多少区块的记录高度）
    String usdt_nowBlock = redisService.get("USDT_NowBlock");
    if (usdt_nowBlock == null) {
      redisService.save("USDT_NowBlock", "1");
      usdt_nowBlock = "1";
    }
    startblock = Integer.parseInt(usdt_nowBlock);

    //查找出数据库中不为空的USDT地址
    List<String> digitalAddressList = exDigitalmoneyAccountService
        .listPublicKeyByCoinCode(Constant.USDT);

    LogFactory.info("omni钱包充币记录刷新");
    //兼容omni的其他币种，维护coinType和propertyid对应
    Map<String, String> omnisMap = new HashMap<>();
    omnisMap.put(Constant.USDT, Constant.PROPERTYID_USDT.toString());
    int sysConfirmations = 2;//确认数
    JsonrpcClient client = MyCoinService.getClient(coinType);
    //获取当前USDT钱包同步的区块高度
    endblock = client.getBlockCount();

    if (client != null) {
      if (address == null) {
        address = "*";
      }
      count = 9999;
      skip = 0;
//            startblock = 0;
//            endblock = 999999;
      List<Map<String, Object>> list = client
          .omniListTransactions(address, count, skip, startblock, endblock);

      for (Map<String, Object> map : list) {
        //处理类型
        if (map.get("valid") != null) {
          String valid = map.get("valid").toString().toLowerCase();
          String propertyid = map.get("propertyid").toString();
          String txId = map.get("txid").toString();
          String txType = map.get("type").toString();
          String reviceaddress = map.get("referenceaddress").toString();
          for (String add : digitalAddressList) {
            if (add.equals(reviceaddress)) {
              Integer confirmations = Integer.valueOf(map.get("confirmations").toString());
              String txidtype = txId + "_" + txType;
              if (valid.equals("true")) {
                if (isExists(txidtype) == 0 && confirmations >= sysConfirmations && propertyid
                    .equals(omnisMap.get(coinType))) {
                  CoinTransaction t = new CoinTransaction();
                  t.setBlockHash(map.get("blockhash").toString());
                  t.setAddress(map.get("referenceaddress").toString());
                  t.setTxId(txId);
                  t.setCoinType(coinType);
                  t.setCategory(txType);
                  t.setAmount(Double.valueOf(map.get("amount").toString()));
                  t.setFee(Double.valueOf(map.get("fee").toString()));
                  t.setConfirmations(confirmations);
                  t.setTxIdType(txidtype);
                  t.setIsCreateOrder(0);
                  //保存数据库
                  save(t);
                }
              }
            }
          }
        }
      }
      redisService.save("USDT_NowBlock", String.valueOf(endblock));
    } else {
      LogFactory.info("获取usdt client为空");
    }
    return result;
  }

  @Override
  public JsonResult omniRecordTransactionByOneBlock(String coinType, String address, int count,
      int skip, int startblock, int endblock) {
    JsonResult result = new JsonResult();
    LogFactory.info("omni钱包定位区块刷新：" + startblock);
    //兼容omni的其他币种，维护coinType和propertyid对应
    Map<String, String> omnisMap = new HashMap<>();
    omnisMap.put(Constant.USDT, Constant.PROPERTYID_USDT.toString());
    int sysConfirmations = 2;//确认数
    JsonrpcClient client = MyCoinService.getClient(coinType);
    if (client != null) {
      if (address == null) {
        address = "*";
      }
      count = 10;
      skip = 0;
      List<Map<String, Object>> list = client
          .omniListTransactions(address, count, skip, startblock, endblock);
      for (Map<String, Object> map : list) {
        //处理类型
        String valid = map.get("valid").toString().toLowerCase();
        String propertyid = map.get("propertyid").toString();
        String txId = map.get("txid").toString();
        String txType = map.get("type").toString();
        Integer confirmations = Integer.valueOf(map.get("confirmations").toString());
        String txidtype = txId + "_" + txType;
        if (valid.equals("true")) {
          if (isExists(txidtype) == 0 && confirmations >= sysConfirmations && propertyid
              .equals(omnisMap.get(coinType))) {
            CoinTransaction t = new CoinTransaction();
            t.setBlockHash(map.get("blockhash").toString());
            t.setAddress(map.get("referenceaddress").toString());
            t.setTxId(txId);
            t.setCoinType(coinType);
            t.setCategory(txType);
            t.setAmount(Double.valueOf(map.get("amount").toString()));
            t.setFee(Double.valueOf(map.get("fee").toString()));
            t.setConfirmations(confirmations);
            t.setTxIdType(txidtype);
            t.setIsCreateOrder(0);
            //保存数据库
            save(t);
          }
        }
      }
    } else {
      LogFactory.info("获取usdt client为空");
    }
    return result;
  }


  @Override
  public void consumeTransaction() {
    //从数据库读取未处理的记录
    List<CoinTransaction> list = ((CoinTransactionDao) dao).listUnCreateOrder();
    if (list != null && list.size() > 0) {
      for (CoinTransaction tx : list) {
        JsonResult result = rechargecoin(tx);
        boolean isSuccess = result.getSuccess();
        if (isSuccess) {
          tx.setIsCreateOrder(1);
          this.update(tx);
        } else {
          LogFactory.info("充币接口返回结果：" + com.alibaba.fastjson.JSON.toJSONString(result));
        }
      }
    }
  }

  @Override
  public List<String> listAddressGroupByCoinType(String coinType) {
    // TODO Auto-generated method stub
    return ((CoinTransactionDao) dao).listAddressGroupByCoinType(coinType);
  }

  @Override
  public void listWalletByredis() {
    List<Wallet> list = new ArrayList<>();
    List<String> coins = RedisUtil.listcoin();
    int i = 0;
    for (String coinCode : coins) {
      coinCode = coinCode.toUpperCase();
      LogFactory.info("记录币名称到redis:" + coinCode);
      if (!CoinEmpty.coinisNotEmpty(coinCode)) {
        continue;
      }
      Wallet wallet = new Wallet();
      wallet.setCoinCode(coinCode);
      //以太坊代币
      if (EtherService.isSmartContractCoin(coinCode)) {
        wallet = EtherService.smartContractGetWalletInfo(coinCode);
        wallet.setId(++i);
        list.add(wallet);

        continue;
      }

      switch (coinCode) {
        case Constant.ETHER:
          wallet = EtherService.getEtherWalletInfo();
          wallet.setId(++i);
          list.add(wallet);
          continue;
        case Constant.ETC:
          wallet = EtcService.getEtherWalletInfo();
          wallet.setId(++i);
          list.add(wallet);
          continue;
        case Constant.ETZ:
          wallet = EtzService.getEtherWalletInfo();
          wallet.setId(++i);
          list.add(wallet);
          continue;
        case "TV":
          wallet = TvUtil.getWalletInfo();
          wallet.setId(++i);
          list.add(wallet);
          continue;
        case Constant.USDT:
          wallet = MyCoinService.omniGetWalletInfo(coinCode);
          wallet.setId(++i);
          list.add(wallet);
          continue;
        case Constant.BDS:
          BdsServerImpl bdsServerImpl = new BdsServerImpl();
          wallet = bdsServerImpl.getWalletInfo();
          wallet.setId(++i);
          list.add(wallet);
          continue;
        case Constant.BTS:
          BtsServerImpl btsServerImpl = new BtsServerImpl();
          wallet = btsServerImpl.getWalletInfo();
          wallet.setId(++i);
          list.add(wallet);
          continue;
        case Constant.GXS:
          GxsServerImpl gxsServerImpl = new GxsServerImpl();
          wallet = gxsServerImpl.getWalletInfo();
          wallet.setId(++i);
          list.add(wallet);
          continue;
        case Constant.NEO:
          CoinService coinService1 = new CoinServiceImpl(coinCode);
          wallet = coinService1.getWalletInfo(coinCode);
          wallet.setId(++i);
          list.add(wallet);
          continue;
        case "TEA":
          wallet = teaService.listWalletBalance();
          wallet.setId(++i);
          list.add(wallet);
          continue;
        case com.mz.coin.utils.Constant.HTL:
          HtlServiceImpl htl = new HtlServiceImpl();
          wallet = htl.getWalletInfo();
          wallet.setId(++i);
          list.add(wallet);
          continue;
        default:
          CoinService coinService = new CoinServiceImpl(coinCode);
          wallet = coinService.getWalletInfo(coinCode);
          wallet.setId(++i);
          list.add(wallet);
          continue;
      }

    }
    redisService.save("AllWalletList", com.alibaba.fastjson.JSON.toJSONString(list));
  }
}
