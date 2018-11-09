/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年7月5日 上午10:42:10
 */
package com.mz.calculate.mvc.service.impl;

import com.alibaba.fastjson.JSON;
import com.mz.calculate.settlement.model.AppReportSettlement;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.user.model.AppCustomer;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.serialize.Mapper;
import com.mz.calculate.mvc.po.OperationAccountFundInfoLog;
import com.mz.calculate.mvc.service.AppReportSettlementCulService;
import com.mz.calculate.mvc.service.AppReportSettlementService;
import com.mz.customer.user.service.AppCustomerService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author: Wu Shuiming
 * @Date :          2016年7月5日 上午10:42:10 
 */
@Service("appReportSettlementService")
public class AppReportSettlementImpl extends BaseServiceImpl<AppReportSettlement, Long> implements
    AppReportSettlementService {

  @Resource(name = "appReportSettlementDao")
  @Override
  public void setDao(BaseDao<AppReportSettlement, Long> dao) {
    super.dao = dao;
  }

  @Resource
  private RedisService redisService;
  @Resource
  public AppReportSettlementCulService appReportSettlementCulService;

  @Resource
  public AppCustomerService appCustomerService;

  //=====start核算============
  @Override
  public List<Map<String, Object>> culAccountByCustomersErrorInfosureold(
      String[] ids, Boolean iserrorright) {
    List<Map<String, Object>> listErrorInfo = new ArrayList<Map<String, Object>>();
    Map<String, String> mapLoadWeb = PropertiesUtils.getLoadWeb();
    for (String website : mapLoadWeb.keySet()) {
      String currencyType = mapLoadWeb.get(website);
      int i = 0;
      while (i < ids.length) {
        Long id = Long.valueOf(ids[i]);
        AppCustomer appCustomer = appCustomerService.get(id);
        Map<String, Object> map = appReportSettlementCulService
            .culAccountByCustomer(appCustomer.getId(), currencyType, website, false, iserrorright);
        if (null != map) {
          map.put("customerId", id);
          map.put("createTime", new Date());
          listErrorInfo.add(map);
        }
        i++;
      }
    }

    redisService.setList("user_fund_check", listErrorInfo);

    return listErrorInfo;

  }

  @Override
  public void culAccountByCustomerssureold(String[] ids) {
    List<Map<String, Object>> listErrorInfo = new ArrayList<Map<String, Object>>();
    int i = 0;
    StringBuffer userNames = new StringBuffer("");
    Map<String, String> mapLoadWeb = PropertiesUtils.getLoadWeb();
    for (String website : mapLoadWeb.keySet()) {
      String currencyType = mapLoadWeb.get(website);
      while (i < ids.length) {
        Long id = Long.valueOf(ids[i]);
        AppCustomer appCustomer = appCustomerService.get(id);

        Map<String, Object> map = appReportSettlementCulService
            .culAccountByCustomer(appCustomer.getId(), currencyType, website, true, false);

        if (null != map) {
          map.put("customerId", id);
          map.put("createTime", new Date());
          listErrorInfo.add(map);
        }
        //	 userNames.append(appCustomer.getUserName()+",");
        userNames.append(appCustomer.getUserName());
        i++;
      }
    }
    //保存操作日志
    if (listErrorInfo.size() > 0) {
      OperationAccountFundInfoLog operationAccountFundInfoLog = new OperationAccountFundInfoLog();
      operationAccountFundInfoLog.setWebsite("cn");
      operationAccountFundInfoLog.setCurrencyType("cny");
      //		operationAccountFundInfoLog.setOperatorName(ContextUtil.getCurrentUser().getUsername());
      operationAccountFundInfoLog.setUserName(userNames.toString());
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

  }

  @Override
  public List<Map<String, Object>> culRedisAndSqlAccountByCustomer(String[] ids,
      Boolean iserrorright) {

    List<Map<String, Object>> listErrorInfo = new ArrayList<Map<String, Object>>();
    Map<String, String> mapLoadWeb = PropertiesUtils.getLoadWeb();
    for (String website : mapLoadWeb.keySet()) {
      String currencyType = mapLoadWeb.get(website);
      int i = 0;
      while (i < ids.length) {
        Long id = Long.valueOf(ids[i]);
        AppCustomer appCustomer = appCustomerService.get(id);
        Map<String, Object> map = appReportSettlementCulService
            .culRedisAndSqlAccountByCustomer(appCustomer.getId(), currencyType, website, false,
                iserrorright);
        if (null != map) {
          map.put("customerId", id);
          map.put("createTime", new Date());
          listErrorInfo.add(map);
        }
        i++;
      }
    }

    redisService.setList("user_fund_check", listErrorInfo);

    return listErrorInfo;


  }

  @Override
  public void culRedisAndSqlToSqlAccountByCustomer(String[] ids) {

    List<Map<String, Object>> listErrorInfo = new ArrayList<Map<String, Object>>();
    int i = 0;
    StringBuffer userNames = new StringBuffer("");
    Map<String, String> mapLoadWeb = PropertiesUtils.getLoadWeb();
    for (String website : mapLoadWeb.keySet()) {
      String currencyType = mapLoadWeb.get(website);
      while (i < ids.length) {
        Long id = Long.valueOf(ids[i]);
        AppCustomer appCustomer = appCustomerService.get(id);

        appReportSettlementCulService
            .culRedisAndSqlToSqlAccountByCustomer(appCustomer.getId(), currencyType, website);

        i++;
      }
    }


  }

  //=====end核算============

}
