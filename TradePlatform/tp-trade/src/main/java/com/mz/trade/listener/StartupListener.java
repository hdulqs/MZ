/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Yuan Zhicheng
 * @version: V1.0
 * @Date: 2015年9月16日 上午11:04:39
 */
package com.mz.trade.listener;

import com.github.pagehelper.util.StringUtil;
import com.mz.core.quartz.QuartzJob;
import com.mz.core.quartz.QuartzManager;
import com.mz.core.quartz.ScheduleJob;
import com.mz.exchange.product.model.ExCointoCoin;
import com.mz.redis.common.utils.RedisTradeService;
import com.mz.trade.entrust.dao.CommonDao;
import com.mz.trade.entrust.service.ExEntrustService;
import com.mz.trade.model.TradeRedis;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author Administrator
 *
 */
@Component
public class StartupListener implements CommandLineRunner {

  @Override
  public void run(String... strings) {

    LogFactory
        .info("--------------------------------------------------------------------------------");
    LogFactory
        .info("---------------------------------加载应用app----------------------------------------");
    LogFactory.info("------------------" + PropertiesUtils.APP.getProperty("app.loadApp")
        + "-----------------");
    //加载每个应用的启动方法
    String isStartRobot = PropertiesUtils.APP.getProperty("app.isStartRobot");
    if (!StringUtil.isEmpty(isStartRobot) && isStartRobot.equals("true")) {
      // 刷新参考价格，参考价格可以为火币价格
      ScheduleJob refreshReferencePriceJob = new ScheduleJob();
      refreshReferencePriceJob.setSpringId("exEntrustService");
      refreshReferencePriceJob.setMethodName("refreshReferencePrice");
      QuartzManager.addJob("refreshReferencePrice", refreshReferencePriceJob, QuartzJob.class, "* 0/2 * * * ?");  // 2分钟刷新一次

      //机器人定时器
      ScheduleJob autoAddExEntrust = new ScheduleJob();
      autoAddExEntrust.setSpringId("exEntrustService");
      autoAddExEntrust.setMethodName("autoAddExEntrust");
      QuartzManager
          .addJob("autoAddExEntrust", autoAddExEntrust, QuartzJob.class, "0/5 * * * * ?");// 两秒

      ScheduleJob cancelAutoAddExEntrust = new ScheduleJob();
      cancelAutoAddExEntrust.setSpringId("exEntrustService");
      cancelAutoAddExEntrust.setMethodName("cancelAutoAddExEntrust");
      QuartzManager.addJob("cancelAutoAddExEntrust", cancelAutoAddExEntrust, QuartzJob.class,
          "0/30 * * * * ?");// 两秒

    }

    // 委托单入库
    ScheduleJob redisToMysql = new ScheduleJob();
    redisToMysql.setSpringId("exOrderInfoService");
    redisToMysql.setMethodName("redisToMysqlmq");
    QuartzManager.addJob("redisToMysql", redisToMysql, QuartzJob.class, "0/2 * * * * ?");// 两秒
    try {
      Thread.sleep(300);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // 资金变化入库
    ScheduleJob redisToredisLog = new ScheduleJob();
    redisToredisLog.setSpringId("exOrderInfoService");
    redisToredisLog.setMethodName("redisToredisLogmq");
    QuartzManager.addJob("redisToredisLog", redisToredisLog, QuartzJob.class,
        "0/2 * * * * ?");// 两秒 0/2 * * * * ?

    // 深度更新
    ScheduleJob jobRunTimepushMarket = new ScheduleJob();
    jobRunTimepushMarket.setSpringId("webSocketScheduleService");
    jobRunTimepushMarket.setMethodName("pushMarket");
    QuartzManager.addJob("jobRunTimepushMarket", jobRunTimepushMarket, QuartzJob.class,
        "0/1 * * * * ?");// 两秒

    ExEntrustService exEntrustService = (ExEntrustService) ContextUtil.getBean("exEntrustService");
    long start1 = System.currentTimeMillis();
    exEntrustService.tradeInit();
    long end = System.currentTimeMillis();
    System.out.println("初始化交易数据：");
    System.out.println(end - start1);

    //启动的时候要把这个设置成1，不然刚启动完之后，所有的数据都没了，必须下一单才会出来
    CommonDao commonDao = (CommonDao) ContextUtil.getBean("commonDao");
    List<ExCointoCoin> listExCointoCoin = commonDao.getExCointoCoinValid();
    RedisTradeService redisTradeService = (RedisTradeService) ContextUtil
        .getBean("redisTradeService");
    for (ExCointoCoin exCointoCoin : listExCointoCoin) {
      redisTradeService.save(TradeRedis
          .getEntrustTimeFlag(exCointoCoin.getCoinCode(), exCointoCoin.getFixPriceCoinCode()), "1");
    }
  }

}
