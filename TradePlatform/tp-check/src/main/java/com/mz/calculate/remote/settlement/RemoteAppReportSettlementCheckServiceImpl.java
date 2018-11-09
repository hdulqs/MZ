/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月24日 下午2:04:29
 */
package com.mz.calculate.remote.settlement;

import com.mz.remote.settlement.RemoteAppReportSettlementCheckService;
import com.mz.calculate.mvc.AppReportSettlementNorTrService;
import com.mz.calculate.mvc.service.AppReportSettlementService;
import com.mz.trade.entrust.service.ExOrderInfoService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

/**
 *
 * <p> TODO</p>
 * @author: Gao Mimi
 * @Date :          2016年5月23日 下午6:44:50
 */

public class RemoteAppReportSettlementCheckServiceImpl implements
    RemoteAppReportSettlementCheckService {

  @Resource
  public AppReportSettlementNorTrService appReportSettlementNorTrService;
  @Resource
  public AppReportSettlementService appReportSettlementService;
  @Resource
  private ExOrderInfoService exOrderInfoService;

  @Override
  public List<Map<String, Object>> culSureOldAccountAllCustomerErrorInfo(Integer days) {
    // TODO Auto-generated method stub
    return appReportSettlementNorTrService.culSureOldAccountAllCustomerErrorInfo(days);
  }

  @Override
  public void culAccountAllCustomer() {
    appReportSettlementNorTrService.culAccountAllCustomer();

  }

  @Override
  public List<Map<String, Object>> culAccountByCustomersErrorInfosureold(String[] ids,
      Boolean iserrorright) {
    // TODO Auto-generated method stub
    return appReportSettlementService.culAccountByCustomersErrorInfosureold(ids, iserrorright);
  }

  @Override
  public void culAccountByCustomerssureold(String[] ids) {
    appReportSettlementService.culAccountByCustomerssureold(ids);

  }

  @Override
  public List<Map<String, Object>> culRedisAndSqlSureOldAccountAllCustomerErrorInfo(Integer days) {
    // TODO Auto-generated method stub
    return appReportSettlementNorTrService.culRedisAndSqlSureOldAccountAllCustomerErrorInfo(days);
  }

  @Override
  public List<Map<String, Object>> culRedisAndSqlAccountByCustomer(String[] ids,
      Boolean iserrorright) {
    // TODO Auto-generated method stub
    return appReportSettlementService.culRedisAndSqlAccountByCustomer(ids, iserrorright);
  }

  @Override
  public void culRedisAndSqlToSqlAccountByCustomer(String[] ids) {
    // TODO Auto-generated method stub
    appReportSettlementService.culRedisAndSqlToSqlAccountByCustomer(ids);
  }

  @Override
  public void removeEntrustRobt() {
    exOrderInfoService.removeEntrustRobt();

  }


}
