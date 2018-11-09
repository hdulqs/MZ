/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: menwei
 * @version: V1.0
 * @Date: 2017-11-28 17:40:59
 */
package com.mz.customer.trade.service.impl;

import com.github.pagehelper.util.StringUtil;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.commend.model.AppCommendUser;
import com.mz.customer.commend.service.AppCommendUserService;
import com.mz.customer.deploy.model.AppCommendDeploy;
import com.mz.customer.money.model.AppCommendMoney;
import com.mz.customer.money.service.AppCommendMoneyService;
import com.mz.customer.rank.model.AppCommendRank;
import com.mz.customer.trade.model.AppCommendTrade;
import com.mz.customer.user.model.AppCustomer;
import com.mz.redis.common.utils.RedisService;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.change.remote.exEntrust.RemoteExExOrderService;
import com.mz.customer.deploy.service.AppCommendDeployService;
import com.mz.customer.rank.service.AppCommendRankService;
import com.mz.customer.trade.dao.AppCommendTradeDao;
import com.mz.customer.trade.service.AppCommendTradeService;
import com.mz.customer.user.service.AppCustomerService;
import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.web.remote.RemoteAppConfigService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> AppCommendTradeService </p>
 *
 * @author: menwei
 * @Date :          2017-11-28 17:40:59
 */
@Service("appCommendTradeService")
public class AppCommendTradeServiceImpl extends BaseServiceImpl<AppCommendTrade, Long> implements
    AppCommendTradeService {

  @Resource(name = "appCommendTradeDao")
  @Override
  public void setDao(BaseDao<AppCommendTrade, Long> dao) {
    super.dao = dao;
  }

  @Resource(name = "appCommendTradeDao")
  public AppCommendTradeDao appCommendTradeDao;

  @Resource(name = "appCustomerService")
  private AppCustomerService appCustomerService;

  @Resource(name = "appCommendTradeService")
  private AppCommendTradeService appCommendTradeService;

  @Resource(name = "appCommendUserService")
  public AppCommendUserService appCommendUserService;

  @Resource(name = "appCommendDeployService")
  public AppCommendDeployService appCommendDeployService;

  @Resource(name = "appCommendMoneyService")
  public AppCommendMoneyService appCommendMoneyService;

  @Resource(name = "exOrderInfoService")
  public ExOrderInfoService exOrderInfoService;

  @Resource(name = "appCommendRankService")
  public AppCommendRankService appCommendRankService;

  @Autowired
  private RedisService redisService;

  private static final Logger log = LoggerFactory.getLogger(AppCommendTradeServiceImpl.class);

  @Override
  public Boolean dealCommission(String orderNum) {

    RemoteExExOrderService remoteOrderServiceService = (RemoteExExOrderService) ContextUtil
        .getBean("remoteExExOrderService");
    //订单
    List<ExOrderInfo> orderList = remoteOrderServiceService.findByOrderNum(orderNum);
    for (int i = 0; i < orderList.size(); i++) {
      if (null != orderList && orderList.size() > 0) {
        ExOrderInfo order = orderList.get(0);
        //系统配置 买方
        QueryFilter depBuyFilter = new QueryFilter(AppCommendDeploy.class);
        depBuyFilter.addFilter("states=", "1");
        List<AppCommendDeploy> buyDeploys = appCommendDeployService.find(depBuyFilter);

        /**
         * 买单用户
         */
        AppCommendDeploy buyDeploy = null;
        try {
          Long buyCustomId = order.getBuyCustomId();
          if (buyDeploys != null && buyDeploys.size() > 0) {
            if (order.getTransactionBuyFee().compareTo(BigDecimal.ZERO) > 0) {
              buyDeploy = buyDeploys.get(0);
              QueryFilter buyUserFilter = new QueryFilter(AppCommendUser.class);
              buyUserFilter.addFilter("uid=", buyCustomId);
              AppCommendUser buyUser = appCommendUserService.get(buyUserFilter);
              //参数配置校验
              if (buyUser != null) {
                if (buyUser.getIsFrozen() == 0) {
                  String[] checkr = checkSeachUser(remoteOrderServiceService, order, buyDeploy,
                      buyCustomId, buyUser, order.getBuyUserName());
                  if (checkr[1] == null) {
                    String sid = checkr[0];
                    Integer minHierarchy = buyDeploy.getMinHierarchy();
                    Integer maxHierarchy = buyDeploy.getMaxHierarchy();
                    if (StringUtils.isEmpty(minHierarchy)) {
                      minHierarchy = 0;
                    }
                    if (StringUtils.isEmpty(maxHierarchy)) {
                      maxHierarchy = 0;
                    }
                    //如果最大奖励层级大于当前最大层级，则最大奖励层级等于当前最大层级
                    String[] split = sid.split(",");
                    if (split.length < maxHierarchy) {
                      maxHierarchy = split.length;
                    }
                    int num = 0;
                    if (minHierarchy == 0 || minHierarchy == 1) {
                      minHierarchy = 1;
                      num = 1;
                    } else if (minHierarchy > 0) {
                      num = minHierarchy;
                    }
                    //基本参数
                    BigDecimal transactionBuyFee = order.getTransactionBuyFee();
                    BigDecimal transactionCharge = transactionBuyFee;
                    String fixPriceCoinCode = order.getFixPriceCoinCode();
                    Integer fixPriceType = order.getFixPriceType();
                    String coinCode = order.getCoinCode();
                    BigDecimal transactionPrice = order.getTransactionPrice();
                    //开始轮询
                    excuteSaveCommission(i, order, buyDeploy, minHierarchy, maxHierarchy, split,
                        num, transactionBuyFee, transactionCharge, coinCode, fixPriceType,
                        fixPriceCoinCode, 1, buyDeploy, transactionPrice);
                  }
                } else {
                  log.error("该用户已被冻结：{}，订单号：{}", order.getBuyUserName(), order.getOrderNum());
                }
              } else {
                log.error("找不到买单用户：{}，订单号：{}", order.getBuyUserName(), order.getOrderNum());
              }
            } else {
              log.warn("买方没有产生交易手续费");
            }
          } else {
            log.warn("没有买方推荐佣金的相关配置");
          }
        } catch (Exception e) {
          log.error("买单用户佣金计算异常：{}，订单号：{} ，错误信息：", order.getBuyUserName(), order.getOrderNum(), e);
          return false;
        }

        /**
         * 卖单用户
         */
        //卖方
        QueryFilter depSellFilter = new QueryFilter(AppCommendDeploy.class);
        depSellFilter.addFilter("states=", "2");
        List<AppCommendDeploy> sellDeploys = appCommendDeployService.find(depSellFilter);
        AppCommendDeploy sellDeploy = null;
        try {
          Long sellCustomId = order.getSellCustomId();
          if (sellDeploys != null && sellDeploys.size() > 0) {
            if (order.getTransactionSellFee().compareTo(BigDecimal.ZERO) > 0) {
              sellDeploy = sellDeploys.get(0);
              QueryFilter sellUserFilter = new QueryFilter(AppCommendUser.class);
              sellUserFilter.addFilter("uid=", sellCustomId);
              AppCommendUser sellUser = appCommendUserService.get(sellUserFilter);

              //参数配置校验
              if (sellUser != null) {
                if (sellUser.getIsFrozen() == 0) {
                  String[] checkr = checkSeachUser(remoteOrderServiceService, order, sellDeploy,
                      sellCustomId, sellUser, order.getSellUserName());
                  if (checkr[1] == null) {
                    String sid = checkr[0];
                    Integer minHierarchy = sellDeploy.getMinHierarchy();
                    Integer maxHierarchy = sellDeploy.getMaxHierarchy();
                    if (StringUtils.isEmpty(minHierarchy)) {
                      minHierarchy = 0;
                    }
                    if (StringUtils.isEmpty(maxHierarchy)) {
                      maxHierarchy = 0;
                    }
                    //如果最大奖励层级大于当前最大层级，则最大奖励层级等于当前最大层级
                    String[] split = sid.split(",");
                    if (split.length < maxHierarchy) {
                      maxHierarchy = split.length;
                    }
                    int num = 0;
                    if (minHierarchy == 0 || minHierarchy == 1) {
                      num = 1;
                      minHierarchy = 1;
                    } else if (minHierarchy > 0) {
                      num = minHierarchy;
                    }
                    //基本参数
                    BigDecimal transactionSellFee = order.getTransactionSellFee();
                    BigDecimal transactionCharge = transactionSellFee;
                    String fixPriceCoinCode = order.getFixPriceCoinCode();
                    Integer fixPriceType = order.getFixPriceType();
                    String coinCode = order.getCoinCode();
                    BigDecimal transactionPrice = order.getTransactionPrice();
                    //开始轮询
                    excuteSaveCommission(i, order, sellDeploy, minHierarchy, maxHierarchy, split,
                        num, transactionSellFee, transactionCharge, fixPriceCoinCode, fixPriceType,
                        coinCode, 2, sellDeploy, transactionPrice);
                  }
                } else {
                  log.error("卖该用户已被冻结：{}，订单号：{}", order.getSellUserName(), order.getOrderNum());
                }
              } else {
                log.error("找不到卖单用户：{}，订单号：{}", order.getSellUserName(), order.getOrderNum());
              }
            } else {
              log.warn("卖方没有产生交易手续费");
            }
          } else {
            log.warn("没有卖方推荐佣金的相关配置");
          }
        } catch (Exception e) {
          log.error("卖单用户佣金计算异常：{}，订单号：{} ，错误信息：", order.getSellUserName(), order.getOrderNum(), e);
          return false;
        }
      }
    }
    return true;
  }

  private String[] checkSeachUser(RemoteExExOrderService remoteOrderServiceService,
      ExOrderInfo order, AppCommendDeploy buyDeploy, Long buyCustomId, AppCommendUser buyUser,
      String buyUserName) {
    String sid = buyUser.getSid();
    String[] result = new String[2];
    result[0] = sid;
    if (StringUtils.isEmpty(sid)) {
      log.info("查找不到用户：{} 的推荐人，订单号：{}", buyUserName, order.getOrderNum());
      result[1] = "0";
      return result;
    }
    Long buyOrderCount = remoteOrderServiceService.ListCount(buyCustomId);
    if (buyOrderCount > Long.valueOf(buyDeploy.getTransactionNumber())) {
      log.info("买单用户：{} 超出交易笔数限制，订单号：{}", buyUserName, order.getOrderNum());
      result[1] = "0";
      return result;
    }
    return result;
  }

  private void excuteSaveCommission(int i, ExOrderInfo order, AppCommendDeploy buyDeploy,
      Integer minHierarchy, Integer maxHierarchy, String[] split, int num,
      BigDecimal transactionBuyFee, BigDecimal transactionCharge, String fixPriceCoinCode,
      Integer fixPriceType, String coinCode, Integer type, AppCommendDeploy appCommendDeploy,
      BigDecimal transactionPrice) {
    boolean vflag = false;
    for (int j = 0; j < maxHierarchy; j++) {
      if (minHierarchy <= 1) {
        vflag = true;
      }
      int numIndex = j;
      //交易笔数限制split
      AppCustomer customer = appCustomerService.get(Long.valueOf(split[j]));
      //保存佣金表以及邀请排行榜
      List<AppCommendUser> list2 = this
          .saveAppTradeFactor(customer, transactionBuyFee, fixPriceCoinCode, fixPriceType, coinCode,
              numIndex, transactionCharge, appCommendDeploy, transactionPrice, type, vflag);
      minHierarchy--;
      if (list2.get(0) != null && list2.size() > 0) {
        transactionBuyFee = list2.get(0).getMoneydic();
        if (vflag) {
          if (list2.size() > 0 && buyDeploy.getMaxHierarchy() > i) {
            //存明细
            appCommendTradeService
                .saveCommissionDetailForOrder(order, list2.get(0), num, type, transactionPrice);
          }
          num++;
        }
      }
    }
  }


  private List<AppCommendUser> saveAppTradeFactor(AppCustomer customer, BigDecimal commissionMoney,
      String fixPriceCoinCode, Integer fixPriceType, String coinCode, int numIndex,
      BigDecimal transactionCharge, AppCommendDeploy appCommendDeploy, BigDecimal transactionPrice,
      Integer type, boolean flag) {
    AppCommendMoney appCommendMoney = new AppCommendMoney();
    AppCommendRank appCommendRank = null;
    List<AppCommendUser> list2 = new ArrayList<AppCommendUser>();
    QueryFilter qfdd = new QueryFilter(AppCommendUser.class);
    qfdd.addFilter("uid=", customer.getId());
    List<AppCommendUser> list = appCommendUserService.find(qfdd);
    RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil
        .getBean("remoteAppConfigService");
    if (list.size() > 0) {
      BigDecimal bigDecimalOneRate = new BigDecimal(0);
      if (null != appCommendDeploy) {
        String Ratetype = "";
        BigDecimal aloneMoney = list.get(0).getAloneMoney();
        if (aloneMoney == null) {
          aloneMoney = new BigDecimal(0);
        }
        long longValue = aloneMoney.longValue();
        if (longValue != 0) {
          //个人返佣比例
          Ratetype = "1";
        } else {
          Ratetype = "2";
        }
        //平台返佣比例
        bigDecimalOneRate = appCommendDeploy.getRankRatio().divide(new BigDecimal("100"));

        if (appCommendDeploy.getReserveMoney() == null) {
          appCommendDeploy.setReserveMoney(new BigDecimal(0));
        }
        //平台扣除比例
        BigDecimal ReserveMoney = appCommendDeploy.getReserveMoney().divide(new BigDecimal("100"));

        BigDecimal Moneypa = commissionMoney.multiply(bigDecimalOneRate);
        //平台扣除书续费
        BigDecimal Moneypaone = commissionMoney.multiply(ReserveMoney);

        BigDecimal Reserve = Moneypa.subtract(Moneypaone);

        // 判断用户的手续费是否大于后台配置的佣金标准
        if (appCommendDeploy.getStandardValue().compareTo(Reserve) <= 0) {

          QueryFilter qfrank = new QueryFilter(AppCommendRank.class);
          qfrank.addFilter("userId=", list.get(0).getUid());
          List<AppCommendRank> findrank = appCommendRankService.find(qfrank);
          QueryFilter qfdc = new QueryFilter(AppCommendUser.class);
          qfdc.addFilter("username=", customer.getUserName());
          AppCommendUser appCommendUser2 = appCommendUserService.get(qfdc);
          //一个币种只允许有一个账户
          if (findrank.size() == 0) {
            AppCommendRank appCommendRankone = new AppCommendRank();
            appCommendRankone.setUserId(list.get(0).getUid());
            appCommendRankone.setUserName(list.get(0).getUsername());
            appCommendRankone.setCoinCode(fixPriceCoinCode);
            appCommendRankone.setFixCoin(fixPriceCoinCode);
            appCommendRankone.setFixMoney(BigDecimal.ZERO);
            if (flag) {
              appCommendRankService.save(appCommendRankone);
            }
            appCommendRank = appCommendRankone;
          } else if (findrank.size() > 0) {
            appCommendRank = findrank.get(0);
          }

          if (null == commissionMoney) {
            commissionMoney = new BigDecimal(1);    //轮询手续费
          }
          if (null == ReserveMoney) {
            ReserveMoney = new BigDecimal(0);          //平台扣除比例
          }
          if (null == bigDecimalOneRate) {
            bigDecimalOneRate = new BigDecimal(1);//推荐奖励比例
          }
          if (null == transactionCharge) {
            transactionCharge = new BigDecimal(0);//初始手续费
          }
          BigDecimal oneMoney;    //平台奖励
          BigDecimal userMoney = new BigDecimal(0);   //个人奖励
          BigDecimal totalMoney;  //总奖励
          //判断是否第一次接收
          if (numIndex > 0) {
            oneMoney = commissionMoney.multiply(bigDecimalOneRate);
          } else {
            oneMoney = commissionMoney.multiply((new BigDecimal(1).subtract(ReserveMoney)))
                .multiply(bigDecimalOneRate);
          }
          //判断是否有开启单独奖励
          if (appCommendUser2.getAloneMoney() != null
              && appCommendUser2.getAloneMoney().compareTo(new BigDecimal(0)) > 0) {
            BigDecimal addRate = appCommendUser2.getAloneMoney().divide(new BigDecimal(100));
            userMoney = transactionCharge.multiply(addRate);
          }
          totalMoney = oneMoney.add(userMoney);
          //安全校验
          if (oneMoney.compareTo(new BigDecimal(0)) <= 0) {
            oneMoney = new BigDecimal(0);
          }
          if (totalMoney.compareTo(new BigDecimal(0)) <= 0) {
            totalMoney = new BigDecimal(0);
          }
          //邀请排行计算
          appCommendMoney.setFixPriceCoinCode(fixPriceCoinCode);
          String coinMoneyKey = appCommendMoney.getFixPriceCoinCode() + "_USDT:currentExchangPrice";
          String coinMoney = redisService.get(coinMoneyKey);
          if (StringUtil.isEmpty(coinMoney)) {
            coinMoney = "0";
          }
          BigDecimal multiply = totalMoney.multiply(new BigDecimal(coinMoney));//手续费转换为USDT
          appCommendRank.setFixMoney(appCommendRank.getFixMoney().add(multiply));
          appCommendRank.setModified(new Date());
          appCommendRank.setCoinCode(appCommendMoney.getFixPriceCoinCode());
          //推荐派发管理
          appCommendMoney.setMoneyNum(totalMoney);
          appCommendMoney.setRankRatio(bigDecimalOneRate);
          appCommendMoney.setCustromerId(appCommendUser2.getUid());
          appCommendMoney.setCustromerName(appCommendUser2.getUsername());
          appCommendMoney.setFixPriceType(fixPriceType);
          appCommendMoney.setCoinCode(coinCode);
          appCommendMoney.setPaidMoney(BigDecimal.ZERO);
          BigDecimal currentmoney = new BigDecimal(0);
          if (!coinCode.equals("USDT")) {
            currentmoney = remoteAppConfigService.getCurrentCoinMoney(coinCode, "USDT");
          } else {
            currentmoney = transactionPrice;
          }
          appCommendMoney.setCurrentMoney(currentmoney);
          appCommendMoney.setDealType(type);
          //推荐人管理
          AppCommendUser appCommendUser = new AppCommendUser();
          appCommendUser.setMoneydic(oneMoney);
          appCommendUser.setPid(list.get(0).getPid());
          appCommendUser.setUid(list.get(0).getUid());
          appCommendUser.setPname(list.get(0).getPname());
          appCommendUser.setUsername(list.get(0).getUsername());
          appCommendUser.setRatetype(Ratetype);
          appCommendUser.setUserMoney(userMoney);

          list.set(0, appCommendUser);
          list2.add(list.get(0));
          if (flag) {
            appCommendMoneyService.save(appCommendMoney);
            appCommendRankService.update(appCommendRank);
          }
        }
      }
    } else {
      System.out.println("代理商为空");
    }
    System.out.println("size=" + list2.size());
    return list2;
  }


  @Override
  public void saveCommissionDetailForOrder(ExOrderInfo orderInfo, AppCommendUser appCommendUser,
      int hierarchy, Integer type, BigDecimal transactionPrice) {
    // TODO Auto-generated method stub
    if (null != appCommendUser) {
      AppCommendTrade appCommendTrade = new AppCommendTrade();
      appCommendTrade.setFixPriceCoinCode(orderInfo.getFixPriceCoinCode());
      appCommendTrade.setFixPriceType(orderInfo.getFixPriceType());

      if (type == 1) {
        appCommendTrade.setCoinCode(orderInfo.getCoinCode());
        appCommendTrade.setDeliveryId(Integer.valueOf(orderInfo.getBuyCustomId().toString()));
        appCommendTrade.setBasemoney(orderInfo.getTransactionBuyFee());
        appCommendTrade.setPersonType(1);
        appCommendTrade.setDeliveryName(orderInfo.getBuyUserName());
      } else if (type == 2) {
        appCommendTrade.setCoinCode(orderInfo.getFixPriceCoinCode());
        appCommendTrade.setDeliveryId(Integer.valueOf(orderInfo.getSellCustomId().toString()));
        appCommendTrade.setBasemoney(orderInfo.getTransactionSellFee());
        appCommendTrade.setPersonType(2);
        appCommendTrade.setDeliveryName(orderInfo.getSellUserName());
      }
      appCommendTrade.setCurrentMoney(transactionPrice);
      appCommendTrade.setOrdertNum(orderInfo.getOrderNum());
      appCommendTrade.setCustromerName(appCommendUser.getUsername());
      appCommendTrade.setCustromerId(appCommendUser.getUid());
      appCommendTrade.setUserMoney(appCommendUser.getUserMoney());
      // 设置买方总交的手续费
      QueryFilter qfd = new QueryFilter(AppCommendDeploy.class);
      AppCommendDeploy appCommendDeploy = appCommendDeployService.get(qfd);
      appCommendTrade.setFeeamout(orderInfo.getTransactionCount());
      if (appCommendUser.getOnecommendMoney() != null) {
        appCommendTrade.setRewardmoney(appCommendUser.getOnecommendMoney());
      } else {
        appCommendTrade.setRewardmoney(appCommendUser.getMoneydic());
      }
      appCommendTrade.setHierarchy(hierarchy);
      appCommendTrade.setTransactionTime(orderInfo.getTransactionTime());
      appCommendTrade.setRatetype(appCommendUser.getRatetype());
      String coin = orderInfo.getCoinCode() + "_" + "USDT";
      BigDecimal price = this.newTransactionPrice(coin);
      BigDecimal multiply = price.multiply(appCommendUser.getMoneydic());
      appCommendTrade.setChangeMoney(multiply);
      super.save(appCommendTrade);
    } else {

      System.out.println("代理商为空");
    }
  }


  @Override
  public BigDecimal selectCommissionByMoney(BigDecimal money) {
    QueryFilter qf = new QueryFilter(AppCommendTrade.class);
    qf.addFilter("states=", 1);
    AppCommendDeploy appCommendDeploy = appCommendDeployService.get(qf);

    if (null != appCommendDeploy.getRankRatio()) {

      BigDecimal multiply = money.multiply(appCommendDeploy.getRankRatio());
      return multiply;
    }
    return BigDecimal.ZERO;
  }

  public static void main(String[] args) {
        /*int minHierarchy = 1;
        int num = 0;
        if (minHierarchy == 0) {
            num = 1;
            System.out.println(num);
        } else if (minHierarchy > 0) {
            minHierarchy = minHierarchy - 1;
            System.out.println(minHierarchy);
        }*/
    int i = 0 / 1;
    System.out.println(i);
    int j = 1 / 0;
  }


  @Override
  public BigDecimal findOne(String userName, String fixPriceCoinCode) {
    // TODO Auto-generated method stub
    return appCommendTradeDao.findOne(userName, fixPriceCoinCode);
  }


  @Override
  public BigDecimal findTwo(String userName, String fixPriceCoinCode) {
    // TODO Auto-generated method stub
    return appCommendTradeDao.findTwo(userName, fixPriceCoinCode);

  }


  @Override
  public BigDecimal findThree(String userName, String fixPriceCoinCode) {
    // TODO Auto-generated method stub
    return appCommendTradeDao.findThree(userName, fixPriceCoinCode);
  }


  @Override
  public BigDecimal findLater(String userName, String fixPriceCoinCode) {
    // TODO Auto-generated method stub
    return appCommendTradeDao.findLater(userName, fixPriceCoinCode);
  }


  @RequestMapping(value = "/newTransactionPrice", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
  @ResponseBody
  public BigDecimal newTransactionPrice(String coinCode) {
    System.out.println(coinCode);
    BigDecimal price = new BigDecimal(0);
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    if (!StringUtils.isEmpty(coinCode)) {
      try {
        String coin = redisService.get(coinCode + ":currentExchangPrice");
        System.out.println("最新价=0" + coin);
        if (!"".equals(coin) && coin != null) {
          price = new BigDecimal(coin);
        } else {
          price = new BigDecimal(1);
        }
      } catch (Exception e) {
        price = new BigDecimal(1);
      }

    }
    System.out.println(price);
    return price;

  }


  @Override
  public List<AppCommendTrade> selectCommendTrade(String custromerName) {
    // TODO Auto-generated method stub
    return appCommendTradeDao.selectCommendTrade(custromerName);
  }

  @Override
  public List<AppCommendTrade> findByUids(List<Long> pids) {
    return appCommendTradeDao.findByUids(pids);
  }

  @Override
  public List<AppCommendTrade> findByUsername(String username) {
    return appCommendTradeDao.findByUsername(username);
  }

  @Override
  public AppCommendTrade findList(String id) {
    // TODO Auto-generated method stub
    return appCommendTradeDao.findList(id);
  }


}
