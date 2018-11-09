/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月28日 下午7:14:22
 */
package com.mz.exchange.transaction.controller;

import com.alibaba.fastjson.JSONObject;
import com.mz.account.fund.model.AppAccountRecord;
import com.mz.account.fund.model.AppOurAccount;
import com.mz.account.remote.RemoteAppOurAccountService;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.customer.user.model.AppCustomer;
import com.mz.coin.Wallet;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.account.service.ExAmineOrderService;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.product.service.ExProductService;
import com.mz.exchange.purse.CoinInterfaceUtil;
import com.mz.exchange.transaction.CoinQuart;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import com.mz.redis.common.utils.RedisService;
import com.mz.remote.settlement.RemoteAppReportSettlementCheckService;
import com.mz.util.QueryFilter;
import com.mz.util.StringUtil;
import com.mz.util.http.HttpConnectionUtil;
import com.mz.util.idgenerate.IdGenerate;
import com.mz.util.idgenerate.NumConstant;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.account.fund.service.AppTransactionService;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.remote.RemoteAppCustomerService;
import com.mz.customer.user.service.AppCustomerService;
import com.mz.exchange.account.service.ExDmColdAccountRecordService;
import com.mz.exchange.account.service.ExDmHotAccountRecordService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午7:14:22
 */
@Controller
@RequestMapping("/transaction/exdmtransaction")
public class ExDmTransactionColler extends BaseController<ExDmTransaction, Long> {

  @Resource
  public ExAmineOrderService examineOrderService;

  @Resource(name = "exDmTransactionService")
  public void setService(BaseService<ExDmTransaction, Long> service) {
    super.service = service;
  }

  @Resource(name = "exDmColdAccountRecordService")
  public ExDmColdAccountRecordService exDmColdAccountRecordService;

  @Resource(name = "exDmHotAccountRecordService")
  public ExDmHotAccountRecordService exDmHotAccountRecordService;

  @Resource(name = "exDmTransactionService")
  public ExDmTransactionService exDmTransactionService;

  @Resource(name = "exDigitalmoneyAccountService")
  public ExDigitalmoneyAccountService exDigitalmoneyAccountService;


  @Resource(name = "exProductService")
  public ExProductService exProductService;
  @Resource
  public AppCustomerService appCustomerService;
  @Autowired
  private RedisService redisService;
  @Resource
  public RemoteAppReportSettlementCheckService remoteAppReportSettlementCheckService;

  @RequestMapping("/list")
  @MethodName(name = "分页查询ExDmTransaction")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(ExDmTransaction.class, request);

    String transactionType = request.getParameter("transactionType_EQ");
    String status = request.getParameter("status_EQ");
    if (!org.springframework.util.StringUtils.isEmpty(transactionType)
        && !org.springframework.util.StringUtils.isEmpty(status)
        && Integer.valueOf(transactionType).intValue() == 2
        && Integer.valueOf(status).intValue() == 1
    ) {
      filter.addFilter("remark!=", "C2C卖币");
    }

    PageResult page = null;
    String type = request.getParameter("transactionType_EQ");
    if (type.equals("1")) {
      filter.setOrderby("created desc");
      page = super.findPage(filter);
    } else {
      filter.setOrderby("created desc");
      page = super.findPage(filter);
    }

    return page;
  }

  /**
   * 提现现金或者提现币的之前对用户进行校验，是否有错误数据，对前台进行提现
   *
   * @return 2017年7月28日 tzw
   */
  @MethodName(name = "通过一个提币订单前核算")
  @RequestMapping("/checkWithdraw/{ids}")
  @ResponseBody
  public JsonResult checkWithdraw(@PathVariable String[] ids) {/*
		JsonResult jsonResult = new JsonResult();
		String[] customerIds = new String[ids.length];
		// 根据前台传过来的id数组查询用户的错误信息，然后根据id查询人的名字
		for (int i = 0; i < ids.length; i++) {
			ExDmTransaction transaction = exDmTransactionService.get(Long.valueOf(ids[i]));
			if (transaction != null) {
				customerIds[i] = transaction.getCustomerId().toString();
			}
		}
		if (customerIds[0] != null && !customerIds[0].equals("")) {
			RemoteAppReportSettlementCheckService remoteAppReportSettlementCheckService = (RemoteAppReportSettlementCheckService) ContextUtil.getBean("remoteAppReportSettlementCheckService");
			List<Map<String, Object>> list = remoteAppReportSettlementCheckService.culAccountByCustomersErrorInfosureold(customerIds, false);
			// 拼接返回字符串
			StringBuffer sb = new StringBuffer("用户名为：");
			if (list != null && list.size() > 0) {
				AppCustomer appCustomer;
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					// 获取用户名返回
					appCustomer = appCustomerService.get((Long) map.get("customerId"));
					if (appCustomer != null) {
						sb.append(appCustomer.getUserName());
					}
					if (i != list.size() - 1) {
						sb.append(",");
					}
				}
				jsonResult.setMsg(sb.append("在余额中有错误数据，是否确认取现？").toString());
				jsonResult.setSuccess(false);
			} else {
				jsonResult.setSuccess(true);
			}
		} else {
			jsonResult.setSuccess(true);
		}
		return jsonResult;
	*/

    JsonResult jsonResult = new JsonResult();
    jsonResult.setSuccess(true);
    return jsonResult;
  }

  @MethodName(name = "通过一个提币订单前判断提币地址是否足够并核账")
  @RequestMapping("/checkWallet")
  @ResponseBody
  public JsonResult checkWallet(HttpServletRequest request) {
    String coinCode = request.getParameter("coinCode");
    String transMoney = request.getParameter("transMoney");
    String customerId = request.getParameter("customerId");
    BigDecimal money = new BigDecimal(transMoney);
    String allwalletlist = redisService.get("AllWalletList");
    JsonResult jsonResult = new JsonResult();
    if (allwalletlist != null) {
      List<Wallet> l = com.alibaba.fastjson.JSON.parseArray(allwalletlist, Wallet.class);
      for (Wallet wallet : l) {
        String walletCoinCode = wallet.getCoinCode();
        if (walletCoinCode != null && walletCoinCode.equals(coinCode)) {
          String walletTotalMoney = wallet.getWithdrawalsAddressMoney();
          BigDecimal walletMoney = new BigDecimal(walletTotalMoney);
          //钱包地址的总币数>提币数量
          if (walletMoney.compareTo(money) == 1) {
            jsonResult.setSuccess(true);
          } else {
            jsonResult.setSuccess(false);
            jsonResult.setMsg("提币地址数量不足");
          }
        }
      }
    }
    String ids[] = {String.valueOf(customerId)};
    List<Map<String, Object>> list = remoteAppReportSettlementCheckService
        .culAccountByCustomersErrorInfosureold(ids, false);
    if (list.size() > 0) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("账户核算有误,请到资产核算核对客户账户");
    } else {
      jsonResult.setSuccess(true);
    }
    return jsonResult;
  }

  @RequestMapping(value = "/post/{ids}", method = RequestMethod.GET)
  @MethodName(name = "通过一个提币订单")
  @ResponseBody
  public synchronized JsonResult see(@PathVariable String ids) {
    JsonResult jsonResult = new JsonResult();
    if (!"".equals(ids) && ids != null) {
      String[] list = ids.split(",");
      Long id = null;
      for (String l : list) {
        id = Long.valueOf(l);
        try {
          JsonResult js = super.get(id);
          ExDmTransaction transaction = (ExDmTransaction) js.getObj();
          // transactionType=2提币、status=1待审核
          if (transaction.getTransactionType().intValue() == 2
              && transaction.getStatus().intValue() == 1) {
            String coinCode = transaction.getCoinCode();
            Long customerId = transaction.getCustomerId();

            RemoteAppCustomerService appCustomerService = (RemoteAppCustomerService) ContextUtil
                .getBean("remoteAppCustomerService");
            AppCustomer customer = appCustomerService.getById(customerId);

            jsonResult = sendTo(transaction);
            if (jsonResult.getSuccess()) {
              // 发送提币短信通知

              if (StringUtils.isEmpty(customer.getPhone())) {
                jsonResult.setMsg("客户手机号码为空，不能发送短信提示");
              } else {
                SmsParam smsParam = new SmsParam();
                smsParam.setHryMobilephone(customer.getPhone());
                smsParam.setHrySmstype("sms_withdraw_rmbOrCoin");
                smsParam.setHryCode(coinCode);
                Map<String, Object> map = new HashMap<>();
                map.put("hryCode", coinCode);
                map.put("state", "通过");
                SmsSendUtil.sendSmsCode(smsParam, map);
              }

            } else {
              return jsonResult;
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          jsonResult.setSuccess(false);
          jsonResult.setMsg("提币操作后台处理异常");
          return jsonResult;
        }
      }
    }
    jsonResult.setSuccess(true);
    return jsonResult;

  }

  @RequestMapping(value = "/stop/{ids}")
  @MethodName(name = "驳回一个订单")
  @ResponseBody
  public synchronized JsonResult stop(@PathVariable String ids, HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    if (!"".equals(ids) && ids != null) {
      String[] list = ids.split(",");
      String reason = "批量驳回";
      String str = request.getParameter("reason");
//			if (list.length == 1 && str != null && !"".equals(str)) {// 确认不是通过批量操作的
//				reason = str;
//			}
      reason = str;
      Long id = null;
      for (String l : list) {
        id = Long.valueOf(l);
        try {

          ExDmTransaction exDmTransaction = exDmTransactionService.get(id);
          if (exDmTransaction.getStatus().intValue() == 1) {
            exDmTransaction.setRejectionReason(reason);

            Long customerId = exDmTransaction.getCustomerId();
            RemoteAppCustomerService appCustomerService = (RemoteAppCustomerService) ContextUtil
                .getBean("remoteAppCustomerService");
            AppCustomer customer = appCustomerService.getById(customerId);

            AppTransactionService appTransactionService = (AppTransactionService) ContextUtil
                .getBean("appTransactionService");
            boolean flag = appTransactionService.dmWithdrawReject(exDmTransaction);
            if (flag) {
              // 发送提现驳回短信通知(提币驳回)
              SmsParam smsParam = new SmsParam();
              smsParam.setHryMobilephone(customer.getUserName());
              smsParam.setHrySmstype("sms_coinwithdraw_invalid");
              smsParam.setHryCode(exDmTransaction.getCoinCode());
              SmsSendUtil.sendSmsCode(smsParam, null);
            }
          }

        } catch (Exception e) {
          jsonResult.setSuccess(false);
          e.printStackTrace();
          return jsonResult;
        }
      }
    }
    jsonResult.setSuccess(true);
    return jsonResult;
  }

  @RequestMapping(value = "/record")
  @MethodName(name = "钱包转入记录刷新")
  @ResponseBody
  public JsonResult record() {
    JsonResult jsonResult = new JsonResult();
    try {

      CoinQuart coinQuart = new CoinQuart();
      coinQuart.recordAll();

      jsonResult.setSuccess(true);
      return jsonResult;
    } catch (Exception e) {
      e.printStackTrace();
      jsonResult.setSuccess(false);
    }

    return jsonResult;
  }

  /**
   * 调用钱包接口转出币
   *
   * @author: Zhang Xiaofang
   * @param: @param account 我方币种账户(转出币的账户)
   * @param: @param address 提币账户(转入币的地址)
   * @param: @param amount 数量
   * @param: @param coinCode 币种类型
   * @param: @param id
   * @param: @return
   * @return: String
   * @Date : 2016年9月3日 下午3:59:00
   * @throws:
   */
  public JsonResult sendTo(ExDmTransaction t) {
    String fromAddress = t.getOurAccountNumber();
    String toAddress = t.getInAddress();
    String amount = t.getTransactionMoney().subtract(t.getFee())
        .setScale(8, BigDecimal.ROUND_HALF_DOWN).toString();
    String coinCode = t.getCoinCode();
    String transactionNum = t.getTransactionNum();
    Long id = t.getId();
    String userName = t.getCustomerName();
    JsonResult jsonResult = new JsonResult();
    try {
      String coinInterFace = PropertiesUtils.APP.getProperty("app.coinInterFace");
      String txStr = null;

      QueryFilter queryFilter = new QueryFilter(ExDigitalmoneyAccount.class);
      queryFilter.addFilter("customerId=", t.getCustomerId());
      queryFilter.addFilter("coinCode=", coinCode);
      ExDigitalmoneyAccount moneyAccount = exDigitalmoneyAccountService.get(queryFilter);

      //判断转出地址是否为本地生成的钱包地址，如果是则内部记账，不走区块链
      QueryFilter filter = new QueryFilter(ExDigitalmoneyAccount.class);
      filter.addFilter("publicKey=", toAddress);
      filter.addFilter("coinCode=", t.getCoinCode());
      ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService.get(filter);
      //如果查出为平台生成的钱包地址
      if (exDigitalmoneyAccount != null) {
        //判断币种类型是否匹配
        if (!exDigitalmoneyAccount.getCoinCode().equals(t.getCoinCode())) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("地址有误，币种不匹配");
          return jsonResult;
        }
        boolean result = exDmTransactionService.sendOurCustomer(t, exDigitalmoneyAccount);
        if (result == false) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("订单重复");
          return jsonResult;
        }

        jsonResult.setSuccess(true);
        jsonResult.setMsg("提币申请成功!");
      } else {
        // 调用钱包接口转出币
        if ("LMC".equals(coinInterFace)) {// 邻萌宝提币接口
          txStr = CoinInterfaceUtil
              .lmcSendTo(fromAddress, toAddress, amount, coinCode, transactionNum, userName);
        } else {// 默认提币接口
          txStr = CoinInterfaceUtil
              .sendTo(t.getCustomerName(), toAddress, amount, coinCode, transactionNum);
        }
        if (StringUtils.isNotEmpty(txStr)) {
          LogFactory.info("提币接口调用返回结果：" + txStr);
          JsonResult result = com.alibaba.fastjson.JSON.parseObject(txStr, JsonResult.class);
          //成功调用
          if (result.getSuccess()) {
            ExDmTransaction transaction = exDmTransactionService.get(id);
            transaction.setOrderNo(result.getMsg() + "_send");
            exDmTransactionService.update(transaction);
            examineOrderService.pasePutOrder(id);
            jsonResult.setSuccess(true);
            jsonResult.setMsg("提币申请成功!");
          } else {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(result.getMsg());
          }
        } else {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("接口调用错误!");
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
      jsonResult.setSuccess(false);
      jsonResult.setMsg("后台处理异常");
      return jsonResult;
    }
    return jsonResult;
  }


  @RequestMapping("/allList")
  @MethodName(name = "分页查询ExDmTransaction")
  @ResponseBody
  @MyRequiresPermissions
  public PageResult allList(HttpServletRequest request) {

    QueryFilter filter = new QueryFilter(ExDmTransaction.class, request);
    PageResult page = null;
    page = exDmTransactionService.findPageBySql(filter);

    return page;
  }

  @MethodName(name = "钱包转币到我方账户")
  @RequestMapping(value = "/transfer")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult transfer(HttpServletRequest request, AppOurAccount appOurAccount) {
    JsonResult JsonResult = new JsonResult();
    JsonResult = exDmTransactionService.sendToOurRecharge();
    return JsonResult;
  }

  /**
   * 查询订单详情
   * <p>
   * TODO
   * </p>
   *
   * @author: Zhang Xiaofang
   * @param: @param exDmTransaction
   * @param: @return
   * @return: boolean
   * @Date : 2016年9月23日 下午4:02:35
   * @throws:
   */
  public boolean queryOrder(ExDmTransaction exDmTransaction) {

    String txStr = CoinInterfaceUtil
        .row(exDmTransaction.getOrderNo(), exDmTransaction.getCoinCode());
    if (null != txStr && !"".equals(txStr)) {
      txStr = txStr.replace(" ", "");
      Map<String, Object> tx2map = StringUtil.str2map(txStr);
      // String confirmations= tx2map.get("confirmations").toString();
      Object feeobj = tx2map.get("fee");
      String fee = "";
      if (null != feeobj) {
        fee = String.valueOf(feeobj);
        fee = fee.substring(1, fee.length());
      }
      RemoteAppOurAccountService remoteAppOurAccountService = (RemoteAppOurAccountService) ContextUtil
          .getBean("remoteAppOurAccountService");

      // 查询我方提币账户
      AppOurAccount our = remoteAppOurAccountService
          .findAppOurAccount(exDmTransaction.getWebsite(), exDmTransaction.getCurrencyType(), 1);
      // 保存流水并更新我方提币账户余额(手续费)
      AppAccountRecord withdrawRecord = new AppAccountRecord();
      withdrawRecord.setAppAccountId(our.getId());
      withdrawRecord.setAppAccountNum(our.getAccountNumber());
      withdrawRecord.setRecordType(1);
      withdrawRecord.setSource(0);
      withdrawRecord.setTransactionMoney(new BigDecimal(fee));
      withdrawRecord.setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
      withdrawRecord.setStatus(5);
      withdrawRecord.setRemark("钱包收取的提币手续费");
      withdrawRecord.setCurrencyName(exDmTransaction.getCurrencyType());
      withdrawRecord.setCurrencyType(exDmTransaction.getCurrencyType());
      withdrawRecord.setRemark(exDmTransaction.getOrderNo() + "-purseFee");
      boolean c = remoteAppOurAccountService.updateAccountBalance(our, withdrawRecord);

      return c;
    }
    return false;
  }

  /**
   * 充币成功后驳回功能
   */
  @MethodName(name = "撤销成功记录")
  @RequestMapping(value = "/cancelTransaction")
  @ResponseBody
  public JsonResult cancelTransaction(HttpServletRequest req) {
    ExDmTransactionService exDmTransactionService = (ExDmTransactionService) service;
    String id = req.getParameter("id");
    if (null != id) {
      JsonResult result = exDmTransactionService.cancelTransaction(Long.valueOf(id));
      return result;
    }
    return null;
  }


  @MethodName(name = "撤销成功记录")
  @RequestMapping(value = "/test")
  @ResponseBody
  public JsonResult test(HttpServletRequest req) {
    String url = "http://localhost:9090/coin/coin/test";
    String result = HttpConnectionUtil.getSend(url, "name=1");
    JsonResult obj = JSONObject.parseObject(result, JsonResult.class);
    System.out.println(com.alibaba.fastjson.JSON.toJSON(obj));
    return null;
  }

  public static void main(String[] args) {
    String s = "{\"code\":\"\",\"msg\":\"hello\",\"success\":true}";
    JsonResult result = JSONObject.parseObject(s, JsonResult.class);
    System.out.println(com.alibaba.fastjson.JSON.toJSONString(result));
  }
}
