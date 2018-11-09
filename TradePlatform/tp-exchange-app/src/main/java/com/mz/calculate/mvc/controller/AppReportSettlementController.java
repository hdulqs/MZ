/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年9月6日 下午6:52:40
 */
package com.mz.calculate.mvc.controller;

import com.alibaba.fastjson.JSON;
import com.mz.calculate.mvc.AppReportSettlementNorTrService;
import com.mz.calculate.mvc.po.AccountFundInfo;
import com.mz.calculate.mvc.po.OperationAccountFundInfoLog;
import com.mz.calculate.settlement.model.AppReportSettlement;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.calculate.mvc.service.AppReportSettlementService;
import com.mz.calculate.mvc.service.AppReportSettlementcoinService;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.remote.RemoteAppCustomerService;
import com.mz.trade.entrust.DifCustomer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.util.StringUtil;

/**
 * @author Wu shuiming
 * @date 2016年9月6日 下午6:52:40
 */
@Controller
@RequestMapping(value = "/appReportSettlement1111")
public class AppReportSettlementController extends BaseController<AppReportSettlement, Long> {

  @Resource(name = "appReportSettlementService")
  @Override
  public void setService(BaseService<AppReportSettlement, Long> service) {
    super.service = service;
  }

  @Resource
  private RedisService redisService;
  @Resource
  public AppReportSettlementcoinService appReportSettlementcoinService;
  @Resource(name = "remoteAppCustomerService")
  public RemoteAppCustomerService appCustomerService;

  @Resource
  public AppReportSettlementNorTrService appReportSettlementNorTrService;

  @MethodName(name = "查询AppReportSettlement所有的")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppReportSettlement.class, request);
    filter.setOrderby("settleDate desc,customerId asc");
    PageResult findPage = super.findPage(filter);
    return findPage;
  }

  @MethodName(name = "是否结算按钮可用")
  @RequestMapping(value = "/getIsShowExSettlemenButton", method = RequestMethod.GET)
  @ResponseBody
  public Integer getIsShowExSettlementFinance(HttpServletRequest req) {
    if (DifCustomer.isInClosePlateAndClose()) {
      return 1;
    } else {
      return 0;
    }
  }


  @MethodName(name = "结算")
  @RequestMapping(value = "/postAudit", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult postAudit(HttpServletRequest req) {

    JsonResult jsonResult = new JsonResult();
    jsonResult.setSuccess(true);

    AppReportSettlementService appReportSettlementService = (AppReportSettlementService) service;
    String website = ContextUtil.getWebsite();
    String currencyType = ContextUtil.getCurrencyType();

    Integer i = appReportSettlementService.platformconfirmExSettlement(currencyType, website);

    if (1 == i) {
      jsonResult.setMsg("成功");
      return jsonResult;
    } else {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("失败");
      return jsonResult;
    }

  }

  @MethodName(name = "确认单个结算单")
  @RequestMapping(value = "/postAuditByCustomer/{ids}")
  @ResponseBody
  public JsonResult postAuditByCustomer(@PathVariable String[] ids) {
    AppReportSettlementService appReportSettlementService = (AppReportSettlementService) service;
    JsonResult jsonResult = new JsonResult();
    appReportSettlementService.postAuditByCustomer(ids);

    jsonResult.setSuccess(true);

    return jsonResult;

  }

  @MethodName(name = "对单个客户重新计算结算单")
  @RequestMapping(value = "/settlementByCustomerId/{ids}")
  @ResponseBody
  public JsonResult settlementByCustomerId(@PathVariable String[] ids) {
    AppReportSettlementService appReportSettlementService = (AppReportSettlementService) service;
    JsonResult jsonResult = new JsonResult();
    appReportSettlementService.settlementByCustomerIds(ids);

    jsonResult.setSuccess(true);

    return jsonResult;

  }


  @MethodName(name = "闭盘结算重新核算列表")
  @RequestMapping(value = "/findSettlement")
  @ResponseBody
  public PageResult findSettlement(HttpServletRequest req) {
    String userName = null;
    QueryFilter filter = new QueryFilter(AppReportSettlement.class, req);
    String name = req.getParameter("userName_like");
    AppReportSettlementService appReportSettlementService = (AppReportSettlementService) service;
    if (name != null) {
      userName = name + "%";
    }
    PageResult pageResult = appReportSettlementService.findPageByStates(filter, null, userName);
    return pageResult;
  }
	

/*
	
	
	@MethodName(name="基于sureold全部余额核算并保存到数据库")
	@RequestMapping(value="/culAccountAllCustomersureold")
	@ResponseBody	
	public JsonResult culAccountAllCustomersureold(HttpServletRequest request){
		JsonResult jsonResult = new JsonResult();
		appReportSettlementNorTrService.culAccountAllCustomer();
		jsonResult.setSuccess(true);
		return jsonResult;
	}
	
	public String erewra(){
		return "";
	}
	
	@MethodName(name="基于sureold全部余额核算有错误的客户信息ToMongo")
	@RequestMapping(value="/culSureOldAccountAllCustomerErrorInfoToMongo")
	@ResponseBody	
	public JsonResult culSureOldAccountAllCustomerErrorInfoToMongo(HttpServletRequest request) {
		String daysstr = request.getParameter("days");
		List<Map<String, Object>> list = appReportSettlementNorTrService
				.culSureOldAccountAllCustomerErrorInfo(StringUtil.isEmpty(daysstr) ? null : Integer.valueOf(daysstr));
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		jsonResult.setMsg("计算完成已经保存到mongo，一共" + list.size() + "条");
		return jsonResult;
	}


	@MethodName(name="对单个客户余额核算展示有错误的客户信息")
	@RequestMapping(value="/culAccountByCustomerErrorInfo/{ids}")
	@ResponseBody	
	public List<Map<String,Object>> culAccountByCustomerErrorInfo(@PathVariable String[] ids){
		AppReportSettlementService appReportSettlementService = (AppReportSettlementService)service;
		List<Map<String,Object>> list=appReportSettlementService.culAccountByCustomersErrorInfo(ids);
		
		return list;
		
	}
	@MethodName(name="对单个客户余额核算并保存到数据库")
	@RequestMapping(value="/culAccountByCustomer/{ids}")
	@ResponseBody	
	public JsonResult culAccountByCustomer(@PathVariable String[] ids){
		AppReportSettlementService appReportSettlementService = (AppReportSettlementService)service;
		JsonResult jsonResult = new JsonResult();
		appReportSettlementService.culAccountByCustomers(ids);
		
		jsonResult.setSuccess(true);
		
		return jsonResult;
		
	}
	@MethodName(name="全部余额核算有错误的客户信息ToMongo")
	@RequestMapping(value="/culAccountAllCustomerErrorInfoToMongo")
	@ResponseBody	
	public JsonResult culAccountAllCustomerErrorInfoToMongo(HttpServletRequest request){
		AppReportSettlementService appReportSettlementService = (AppReportSettlementService)service;
		String daysstr=request.getParameter("days");
		List<Map<String,Object>> list = appReportSettlementService.culAccountAllCustomerErrorInfo(StringUtil.isEmpty(daysstr)?null:Integer.valueOf(daysstr));
	       JsonResult jsonResult = new JsonResult();
	       jsonResult.setSuccess(true);
			jsonResult.setMsg("计算完成已经保存到mongo，一共"+list.size()+"条");
			return jsonResult;
	}

	@MethodName(name="全部余额核算并保存到数据库")
	@RequestMapping(value="/culAccountAllCustomer")
	@ResponseBody	
	public JsonResult culAccountAllCustomer(HttpServletRequest request){
		JsonResult jsonResult = new JsonResult();
		appReportSettlementNorTrService.culAccountAllCustomer();
		jsonResult.setSuccess(true);
		return jsonResult;
	}*/

  //====================start核算 用redis======================
  @MethodName(name = "基于sureold全部余额核算有错误的客户信息ToRedis")
  @RequestMapping(value = "/culSureOldAccountAllCustomerErrorInfoToRedis")
  @ResponseBody
  public JsonResult culSureOldAccountAllCustomerErrorInfoToRedis(HttpServletRequest request) {
    String daysstr = request.getParameter("days");
    List<Map<String, Object>> list = appReportSettlementNorTrService
        .culSureOldAccountAllCustomerErrorInfo(
            StringUtil.isEmpty(daysstr) ? null : Integer.valueOf(daysstr));
    JsonResult jsonResult = new JsonResult();
    jsonResult.setSuccess(true);
    jsonResult.setMsg("计算完成已经保存到Redis，一共" + list.size() + "条");
    return jsonResult;
  }

  @MethodName(name = "全部余额核算展示有错误的客户信息Redis")
  @RequestMapping(value = "/culAccountAllCustomerErrorInfo")
  @ResponseBody
  public List<Map<String, List<AccountFundInfo>>> culAccountAllCustomerErrorInfo(
      HttpServletRequest request) {
    List<Map<String, List<AccountFundInfo>>> list = redisService.getList1("user_fund_check_all");

    return list;
  }

  @MethodName(name = "全部余额核算并保存到数据库")
  @RequestMapping(value = "/culAccountAllCustomer")
  @ResponseBody
  public JsonResult culAccountAllCustomer(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    appReportSettlementNorTrService.culAccountAllCustomer();
    jsonResult.setSuccess(true);
    return jsonResult;
  }

  @MethodName(name = "基于sureold对单个客户余额核算展示有错误的客户信息")
  @RequestMapping(value = "/culAccountByCustomerErrorInfosureold/{ids}")
  @ResponseBody
  public List<Map<String, Object>> culAccountByCustomerErrorInfosureold(
      @PathVariable String[] ids) {
    AppReportSettlementService appReportSettlementService = (AppReportSettlementService) service;
    List<Map<String, Object>> list = appReportSettlementService
        .culAccountByCustomersErrorInfosureold(ids, false);

    return list;

  }

  @MethodName(name = "基于sureold对单个客户余额核算展示有错误正确的的客户信息")
  @RequestMapping(value = "/culAccountByCustomerErrorAndRightInfosureold/{ids}")
  @ResponseBody
  public List<Map<String, Object>> culAccountByCustomerErrorAndRightInfosureold(
      @PathVariable String[] ids) {
    AppReportSettlementService appReportSettlementService = (AppReportSettlementService) service;
    List<Map<String, Object>> list = appReportSettlementService
        .culAccountByCustomersErrorInfosureold(ids, true);

    return list;

  }

  @MethodName(name = "基于sureold对单个客户余额核算并保存到数据库")
  @RequestMapping(value = "/culAccountByCustomersureold/{ids}")
  @ResponseBody
  public JsonResult culAccountByCustomersureold(@PathVariable String[] ids) {
    AppReportSettlementService appReportSettlementService = (AppReportSettlementService) service;
    JsonResult jsonResult = new JsonResult();
    appReportSettlementService.culAccountByCustomerssureold(ids);

    jsonResult.setSuccess(true);

    return jsonResult;

  }
  //====================end核算=================================

  @MethodName(name = "核算记录列表")
  @RequestMapping(value = "/operationAccountFundInfoLogList")
  @ResponseBody
  public PageResult operationAccountFundInfoLogList() {
    List<OperationAccountFundInfoLog> list = new ArrayList<OperationAccountFundInfoLog>();
    list = JSON.parseArray(redisService.get("operation_accountfundinfo_log"),
        OperationAccountFundInfoLog.class);
    if (null == list) {
      list = new ArrayList<OperationAccountFundInfoLog>();
    }
    PageResult pageResult = new PageResult();
    pageResult.setRows(list);
    pageResult.setDraw(1);
    pageResult.setPage(1);
    //	pageResult.setTotalPage(Long.valueOf(Integer.valueOf(list.size()).toString()));
    pageResult.setPageSize(list.size());

    // 设置分页数据
    // 设置总记录数
    pageResult.setRecordsTotal(Long.valueOf(Integer.valueOf(list.size()).toString()));

    return pageResult;
  }
}
