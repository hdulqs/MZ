/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年7月5日 上午10:42:10
 */
package com.mz.calculate.mvc;

import com.alibaba.fastjson.JSON;
import com.mz.calculate.mvc.po.OperationAccountFundInfoLog;
import com.mz.calculate.mvc.service.AppReportSettlementCulService;
import com.mz.customer.user.model.AppCustomer;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.date.DateUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.serialize.Mapper;
import com.mz.util.sys.ContextUtil;
import com.mz.core.constant.StringConstant;
import com.mz.customer.user.service.AppCustomerService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author: Wu Shuiming
 * @Date :          2016年7月5日 上午10:42:10 
 */
@Service("appReportSettlementNoTrService")
public class AppReportSettlementNorTrService {


  @Resource
  public AppCustomerService appCustomerService;
  @Resource
  public AppReportSettlementCulService appReportSettlementCulService;
  @Resource
  public RedisService redisService;

  //=======================start核算====================
  public void timeingCulSureOldAccountAllCustomerErrorInfo() {
    //定时计算数据库和核算
    culSureOldAccountAllCustomerErrorInfo(1);
  }

  public void timeingCulRedis() {
    //定时计算数据库与缓存
    culRedisAndSqlSureOldAccountAllCustomerErrorInfo(1);
  }

  public List<Map<String, Object>> culSureOldAccountAllCustomerErrorInfo(Integer days) {
    long start = System.currentTimeMillis();
    System.out.println(start);
    Map<String, Object> mapp = new HashMap<String, Object>();
    mapp.put("endTime", DateUtil.dateToString(new Date(), StringConstant.DATE_FORMAT_FULL));
    if (days == null) {
      mapp.put("beginTime", "2016-01-01 18:24:48");
    } else {
      mapp.put("beginTime", DateUtil
          .dateToString(DateUtil.addDay(new Date(), (0 - days)), StringConstant.DATE_FORMAT_FULL));
    }

    List<AppCustomer> list = appCustomerService.getFundChangeCustomers(mapp);
    System.out.println("计算条数==" + (null != list ? list.size() : "0"));
    Map<String, String> mapLoadWeb = PropertiesUtils.getLoadWeb();
    List<Map<String, Object>> listErrorInfo = new ArrayList<Map<String, Object>>();
    int i = 1;

    for (AppCustomer l : list) {
      System.out.println("到第" + i + "条了");
      i++;
      long start1 = System.currentTimeMillis();
      Map<String, Object> map = appReportSettlementCulService
          .culAccountByCustomer(l.getId(), null, null, false, false);
      long start2 = System.currentTimeMillis();
      System.out.println("一人耗时==" + (start2 - start1));
      if (null != map) {
        map.put("customerId", l.getId());
        map.put("createTime", new Date());
        listErrorInfo.add(map);
      }


    }

    long end = System.currentTimeMillis();
    System.out.println(end);
    System.out.println("余额核算时间==" + (end - start));
    System.out
        .println("核算的日期==" + (DateUtil.dateToString(new Date(), StringConstant.DATE_FORMAT_FULL)));
    //List<String> list1=new ArrayList<String>();
    redisService.setList("user_fund_check_all", listErrorInfo);
    //	mongoTemplate.insert(listErrorInfo, "user_fund_check_all");

    return listErrorInfo;
  }

  public void culAccountAllCustomer() {
    List<Map<String, Object>> listErrorInfo = new ArrayList<Map<String, Object>>();
    List<AppCustomer> list = new ArrayList<AppCustomer>();
    List<Map<String, Object>> listcheckall = redisService.getList1("user_fund_check_all");
    for (Map<String, Object> a : listcheckall) {
      String customerId = (String) a.get("customerId");
      AppCustomer appCustomer = new AppCustomer();
      appCustomer.setId(Long.valueOf(customerId));
      list.add(appCustomer);
    }
    Map<String, String> mapLoadWeb = PropertiesUtils.getLoadWeb();
    for (String website : mapLoadWeb.keySet()) {
      String currencyType = mapLoadWeb.get(website);
      for (AppCustomer l : list) {
        Map<String, Object> map = appReportSettlementCulService
            .culAccountByCustomer(l.getId(), currencyType, website, true, false);
        if (null != map) {
          listErrorInfo.add(map);
        }
      }
    }
    if (listErrorInfo.size() > 0) {
      //保存操作日志
      OperationAccountFundInfoLog operationAccountFundInfoLog = new OperationAccountFundInfoLog();
      operationAccountFundInfoLog.setWebsite("cn");
      operationAccountFundInfoLog.setCurrencyType("cny");
      operationAccountFundInfoLog.setOperatorName(ContextUtil.getCurrentUser().getUsername());
      operationAccountFundInfoLog.setUserName("all");
      operationAccountFundInfoLog.setContext(Mapper.objectToJson(listErrorInfo));
      operationAccountFundInfoLog.setCreatDate(new Date());
      List<OperationAccountFundInfoLog> operationAccountFundInfoLoglist = JSON
          .parseArray(redisService.get("operation_accountfundinfo_log"),
              OperationAccountFundInfoLog.class);
      if (operationAccountFundInfoLoglist == null) {
        operationAccountFundInfoLoglist = new ArrayList<OperationAccountFundInfoLog>();
      }
      operationAccountFundInfoLoglist.add(0, operationAccountFundInfoLog);
      redisService.save("operation_accountfundinfo_log",
          JSON.toJSONString(operationAccountFundInfoLoglist));
    }

    long end = System.currentTimeMillis();
    System.out.println(end);
  }


  public List<Map<String, Object>> culRedisAndSqlSureOldAccountAllCustomerErrorInfo(Integer days) {
    long start = System.currentTimeMillis();
    System.out.println(start);
    Map<String, Object> mapp = new HashMap<String, Object>();
    mapp.put("endTime", DateUtil.dateToString(new Date(), StringConstant.DATE_FORMAT_FULL));
    if (days == null) {
      mapp.put("beginTime", "2016-01-01 18:24:48");
    } else {
      mapp.put("beginTime", DateUtil
          .dateToString(DateUtil.addDay(new Date(), (0 - days)), StringConstant.DATE_FORMAT_FULL));
    }

    List<AppCustomer> list = appCustomerService.getFundChangeCustomers(mapp);
    System.out.println("计算条数==" + (null != list ? list.size() : "0"));
    Map<String, String> mapLoadWeb = PropertiesUtils.getLoadWeb();
    List<Map<String, Object>> listErrorInfo = new ArrayList<Map<String, Object>>();
    int i = 1;

    for (AppCustomer l : list) {
      System.out.println("到第" + i + "条了");
      i++;
      long start1 = System.currentTimeMillis();
      Map<String, Object> map = appReportSettlementCulService
          .culRedisAndSqlAccountByCustomer(l.getId(), null, null, false, false);
      long start2 = System.currentTimeMillis();
      System.out.println("一人耗时==" + (start2 - start1));
      if (null != map) {
        map.put("customerId", l.getId());
        map.put("createTime", new Date());
        listErrorInfo.add(map);
      }


    }

    long end = System.currentTimeMillis();
    System.out.println(end);
    System.out.println("余额核算时间==" + (end - start));
    redisService.setList("user_fund_check_all_redisansql", listErrorInfo);

    return listErrorInfo;
  }
  //========================end核算=======================


}
