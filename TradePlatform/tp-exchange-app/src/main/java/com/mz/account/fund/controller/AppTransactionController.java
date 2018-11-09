/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年3月31日 下午6:55:57
 */
package com.mz.account.fund.controller;

import com.mz.account.fund.model.AppBankCard;
import com.mz.account.fund.model.AppTransaction;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.user.model.AppCustomer;
import com.mz.oauth.user.model.AppUser;
import com.mz.remote.RemoteThirdPayInterfaceService;
import com.mz.util.QueryFilter;
import com.mz.util.date.DateUtil;
import com.mz.util.httpRequest.IpUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.utils.CommonRequest;
import com.mz.account.fund.service.AppAccountService;
import com.mz.account.fund.service.AppBankCardService;
import com.mz.account.fund.service.AppTransactionService;
import com.mz.calculate.mvc.service.AppReportSettlementService;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.remote.RemoteAppPersonInfoService;
import com.mz.customer.user.service.AppCustomerService;
import com.mz.web.remote.RemoteAppConfigService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2016年3月31日 下午6:55:57
 */
@Controller
@RequestMapping("/fund/apptransaction")
public class AppTransactionController extends BaseController<AppTransaction, Long> {

  @Resource(name = "appTransactionService")
  @Override
  public void setService(BaseService<AppTransaction, Long> service) {
    super.service = service;
  }

  @Resource(name = "appBankCardService")
  private AppBankCardService appBankCardService;

  @Resource
  private AppAccountService appAccountService;

  @Resource
  AppReportSettlementService appReportSettlementService;

  @MethodName(name = "查询账户list")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppTransaction.class, request);
    filter.setOrderby("created desc");
    PageResult findPage = super.findPage(filter);
    @SuppressWarnings("unchecked")
    List<AppTransaction> rows = findPage.getRows();
    if (rows != null && rows.size() > 0) {
      for (AppTransaction at : rows) {
        QueryFilter queryFilter = new QueryFilter(AppBankCard.class);
        queryFilter.addFilter("cardNumber=", at.getCustromerAccountNumber());
        AppBankCard appBankCard = appBankCardService.get(queryFilter);
        if (appBankCard != null) {
          at.setBankName(appBankCard.getCardBank());
          at.setBankProvince(appBankCard.getBankProvince());
          at.setBankAddress(appBankCard.getBankAddress());
          at.setSubBank(appBankCard.getSubBank());
          at.setSubBankNum(appBankCard.getSubBankNum());
        }
      }
    }

    return findPage;
  }


  @MethodName(name = "确认充值")
  @RequestMapping("/confirm")
  @ResponseBody
  @MyRequiresPermissions
  public JsonResult confirm(HttpServletRequest request) {

    String id = request.getParameter("id");
    AppUser user = ContextUtil.getCurrentUser();
    JsonResult jsonResult = new JsonResult();
    if (!StringUtils.isEmpty(id)) {
      boolean confirm = ((AppTransactionService) service).confirm(Long.valueOf(id), user.getName());
      if (confirm) {
        jsonResult.setSuccess(true);
        jsonResult.setMsg("确认成功");
      } else {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("确认失败");
      }
    } else {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("请选择确认数据");
    }
    return jsonResult;
  }

  @MethodName(name = "充值,提现无效处理")
  @RequestMapping("/invalid")
  @ResponseBody
  @MyRequiresPermissions(value = {"/winvalid", "/dinvalid"})
  public synchronized JsonResult invalid(HttpServletRequest request) {
    String id = request.getParameter("id");
    String reason = request.getParameter("reason");
    JsonResult jsonResult = new JsonResult();
    if (!StringUtils.isEmpty(id)) {
      AppTransaction appTransaction = service.get(Long.valueOf(id));
      RemoteAppPersonInfoService appPersonInfoService = (RemoteAppPersonInfoService) ContextUtil
          .getBean("remoteAppPersonInfoService");
      AppPersonInfo personInfo = appPersonInfoService
          .getByCustomerId(appTransaction.getCustomerId());
      AppCustomer appCustomer = appCustomerService.get(appTransaction.getCustomerId());

      if (appTransaction.getStatus() == 2 || appTransaction.getStatus() == 3) {
        jsonResult.setSuccess(true);
        jsonResult.setMsg("已经处理过了");
        return jsonResult;
      }
      appTransaction.setStatus(3);
      appTransaction.setRejectionReason(reason);
      //线上提现，线下提现
      if (appTransaction.getTransactionType() == 2 || appTransaction.getTransactionType() == 4) {
        //String[] arr = appAccountService.unfreezeAccountThem(appTransaction.getAccountId(), appTransaction.getTransactionMoney(), appTransaction.getTransactionNum()+"-reject", "人民币提现驳回 冷账户减少金额",null,null);

        boolean flag = appTransactionService.withdrawReject(appTransaction);

        if (flag) {
        				/*AppAccount  appAccount=appAccountService.get(appTransaction.getAccountId());
        				appAccountService.updateAccount(appAccount);
        				String[] str =	appAccountService.inComeToHotAccount(appTransaction.getAccountId(), appTransaction.getTransactionMoney(), appTransaction.getTransactionNum()+"-reject", "人民币提现驳回热账户增加金额", null,null);
        				if(CodeConstant.CODE_SUCCESS.equals(str[0])){
        					AppAccount  app=appAccountService.get(appTransaction.getAccountId());
            				appAccountService.updateAccount(app);
        				}*/

          if (!"".equals(appCustomer.getPhone()) && appCustomer.getPhone() != null) {
            //发送提现驳回短信通知(提现驳回)
            SmsParam smsParam = new SmsParam();
            smsParam.setHryMobilephone(appCustomer.getPhone());
            smsParam.setHrySmstype("sms_rmbwithdraw_invalid");
            smsParam.setHryCode("RMB");
            SmsSendUtil.sendSmsCode(smsParam, null, null);
          }

          jsonResult.setSuccess(true);
          jsonResult.setMsg("处理成功");
        } else {
          jsonResult.setSuccess(true);
          jsonResult.setMsg("解冻金额失败");
        }
      } else {
        if (!"".equals(appCustomer.getPhone()) && appCustomer.getPhone() != null) {
          //发送提现驳回短信通知(充值驳回)
          SmsParam smsParam = new SmsParam();
          smsParam.setHryMobilephone(appCustomer.getPhone());
          smsParam.setHrySmstype("sms_rmbdeposit_invalid");
          smsParam.setHryCode("RMB");
          SmsSendUtil.sendSmsCode(smsParam, null, null);
        }
        service.update(appTransaction);
        jsonResult.setSuccess(true);
        jsonResult.setMsg("处理成功");
      }

    } else {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("请选择数据");
    }
    return jsonResult;
  }

  @Resource
  private AppTransactionService appTransactionService;
  @Resource
  private AppCustomerService appCustomerService;

  /**
   * 提现现金或者提现币的之前对用户进行校验，是否有错误数据，对前台进行提现
   *
   * @return 2017年7月28日 tzw
   */
  @MethodName(name = "取现确认前核算")
  @RequestMapping("/checkWithdraw/{ids}")
  @ResponseBody

  public JsonResult checkWithdraw(@PathVariable String[] ids) {/*
    	JsonResult jsonResult = new JsonResult();
    	String[] customerIds = new String[ids.length];
    	//根据前台传过来的id数组查询用户的错误信息，然后根据id查询人的名字
    	for (int i = 0; i < ids.length; i++) {
    		AppTransaction transaction = appTransactionService.get(Long.valueOf(ids[i]));
    		if (transaction != null) {
    			customerIds[i] = transaction.getCustomerId().toString();
			}
		}	
    	if(customerIds[0]!=null&&!customerIds[0].equals("")){
    		RemoteAppReportSettlementCheckService remoteAppReportSettlementCheckService = (RemoteAppReportSettlementCheckService) ContextUtil.getBean("remoteAppReportSettlementCheckService");
			List<Map<String, Object>> list = remoteAppReportSettlementCheckService.culAccountByCustomersErrorInfosureold(customerIds, false);
		//拼接返回字符串
    	StringBuffer sb = new StringBuffer("用户名为：");
    	if (list != null && list.size() > 0) {
    		AppCustomer appCustomer;
        	for (int i = 0; i < list.size(); i++) {
        		Map<String, Object> map = list.get(i);
        		//获取用户名返回
        		appCustomer = appCustomerService.get((Long)map.get("customerId"));
        		if (appCustomer != null) {
        			sb.append(appCustomer.getUserName());
				}
        		if (i != list.size()-1) {
        			sb.append(",");
    			}
    		}
        	jsonResult.setMsg(sb.append("在余额中有错误数据，是否确认取现？").toString());
    		jsonResult.setSuccess(false);
		}else {
			jsonResult.setSuccess(true);
		}
    	}else{
    		jsonResult.setSuccess(true);
    	}
    	return jsonResult;
    */
    JsonResult jsonResult = new JsonResult();
    jsonResult.setSuccess(true);
    return jsonResult;
  }


  @MethodName(name = "确认取现")
  @RequestMapping("/confirmwithdraw")
  @ResponseBody
  @MyRequiresPermissions
  public synchronized JsonResult confirmwithdraw(HttpServletRequest request) {
    String id = request.getParameter("id");

    JsonResult jsonResult = new JsonResult();
    RemoteAppPersonInfoService appPersonInfoService = (RemoteAppPersonInfoService) ContextUtil
        .getBean("remoteAppPersonInfoService");

    AppTransaction appTransaction = service.get(Long.valueOf(id));
    QueryFilter queryFilter = new QueryFilter(AppBankCard.class);
    queryFilter.addFilter("cardNumber=", appTransaction.getCustromerAccountNumber());
    AppBankCard bankCard = appBankCardService.get(queryFilter);

    AppPersonInfo personInfo = appPersonInfoService.getByCustomerId(appTransaction.getCustomerId());
    System.out.println("确认提现：" + personInfo.toString());
    System.out.println("确认提现身份证：" + personInfo.getCardId());
    boolean confirm;
    //如果没有开启提现接口，那么默认走线下提现
    //是否开启提现接口功能，0：开启  1：关闭
    boolean isOpenIithdraw = false;
    String isOpenIithdrawValue = getCnfigValue("withdrawInterface");
    if (isOpenIithdrawValue != null && "0".equals(isOpenIithdrawValue)) {
      isOpenIithdraw = true;
    }

    //判断提现金额是否超过该用户的提现审核额度
    //当提现接口没有开启或者接口开启但提现超过审核额度都进入后台审核
    if (!isOpenIithdraw) {
      //进行审核 状态 待审核
      // 操作个人的账户流水 以及
      confirm = ((AppTransactionService) service).confirmwithdraw(Long.valueOf(id), "online");
    } else {
      //不进行审核，直接提现
      //这里直接调用第三方
      //成功,处理金额,保存成功流水
      //失败就流水失败就行了
      String baseUrl = PropertiesUtils.APP.getProperty("app.thirdpayUrl");
      CommonRequest req = new CommonRequest();
      req.setRequestNo(appTransaction.getTransactionNum());
      req.setBankAccNum(bankCard.getCardNumber());
      req.setRequestUser(personInfo.getTrueName());
      req.setBankName(bankCard.getCardBank());
      req.setBankBranchName(bankCard.getSubBank()); //开户分行
      req.setAmount((appTransaction.getTransactionMoney().subtract(appTransaction.getFee()))
          .toString());//提现金额
      req.setBankProvince(bankCard.getBankProvince());//省名称
      req.setBankCity(bankCard.getBankAddress());//城市名称
      req.setIdCard(personInfo.getCardId());
      req.setUserBrowerIP(IpUtil.getIp());
      req.setBaseUrl(baseUrl);//回调的url
      String dateTime = DateUtil.dateToString(new Date(), "yyyyMMddHHmmss");
      req.setTransactionDateTime(dateTime);

      RemoteThirdPayInterfaceService remoteThirdPayInterfaceService = (RemoteThirdPayInterfaceService) ContextUtil
          .getBean("remoteThirdPayInterfaceService");
      CommonRequest ret = remoteThirdPayInterfaceService.withdraw(req);
      System.out.println("提现结果:" + ret.toString());
      if (ret != null && "success".equals(ret.getResponseCode())) {
        //保存第三方的流水号
        String thirdPayNo = ret.getRequestNo();
        appTransaction.setRemark(thirdPayNo);
        appTransaction.setStatus(4);
        ((AppTransactionService) service).update(appTransaction);
        confirm = true;
      } else {
        //这里需要直接返回了
        confirm = false;
      }


    }
    if (confirm) {
      //发送提币短信通知
      SmsParam smsParam = new SmsParam();
      smsParam.setHryMobilephone(personInfo.getMobilePhone());
      smsParam.setHrySmstype("sms_withdraw_rmbOrCoin");
      smsParam.setHryCode("RMB");
      SmsSendUtil.sendSmsCode(smsParam, null, null);

      jsonResult.setSuccess(true);
      jsonResult.setMsg("确认成功");
    } else {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("确认失败");
    }

    return jsonResult;
  }


  @MethodName(name = "查询订单状态")
  @RequestMapping("/queryOrder")
  @ResponseBody
  public JsonResult queryOrdeer(HttpServletRequest request) {
    String id = request.getParameter("id");

    JsonResult jsonResult = new JsonResult();
    if (!StringUtils.isEmpty(id)) {
      AppTransaction appTransaction = service.get(Long.valueOf(id));
      CommonRequest commonRequest = new CommonRequest();
      commonRequest.setQueryOrderDate("20160707145055");
      commonRequest.setQueryOrderNo(appTransaction.getTransactionNum());
      ;
      commonRequest.setRequestNo(appTransaction.getTransactionNum());
      //	RemoteThirdPayInterfaceService  remoteThirdPayInterfaceService=(RemoteThirdPayInterfaceService)ContextUtil.getBean("remoteThirdPayInterfaceService");
      //	remoteThirdPayInterfaceService.queryOrder(commonRequest);
      jsonResult.setSuccess(true);
      jsonResult.setMsg("订单成功");

    } else {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("请选择确认数据");
    }

    return jsonResult;

  }

  @MethodName(name = "查询资金充值及资金提现")
  @RequestMapping("/report")
  @ResponseBody
  public PageResult report(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppCustomer.class, request);
    PageResult findPageBySql = ((AppTransactionService) service).listPageBySql(filter);
    return findPageBySql;
  }


  @MethodName(name = "撤销")
  @RequestMapping("/undo")
  @ResponseBody
  public JsonResult undo(HttpServletRequest request) {

    String id = request.getParameter("id");
    AppUser user = ContextUtil.getCurrentUser();
    JsonResult jsonResult = new JsonResult();
    if (!StringUtils.isEmpty(id)) {

      boolean confirm = ((AppTransactionService) service).undo(Long.valueOf(id), user.getName());
      if (confirm) {
        jsonResult.setSuccess(true);
        jsonResult.setMsg("撤销成功");
      } else {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("撤销失败");
      }
    } else {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("请选择撤销数据");
    }
    return jsonResult;
  }

  /**
   * 获取app_config配置
   * <p> TODO</p>
   *
   * @author: Zhang Lei
   * @param: @param type
   * @param: @return
   * @return: String
   * @Date :          2016年12月8日 上午10:50:01
   * @throws:
   */
  public static String getCnfigValue(String type) {
    RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil
        .getBean("remoteAppConfigService");
    String value = remoteAppConfigService.getValueByKey(type);
    return value;
  }


}
