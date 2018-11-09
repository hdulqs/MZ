/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月28日 下午7:14:22
 */
package com.mz.exchange.transaction.controller;

import com.mz.account.fund.model.AppOurAccount;
import com.mz.account.remote.RemoteAppOurAccountService;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.account.service.ExAmineOrderService;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.product.service.ExProductService;
import com.mz.exchange.purse.CoinInterfaceUtil;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import com.mz.util.QueryFilter;
import com.mz.util.StringUtil;
import com.mz.util.sys.ContextUtil;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.exchange.account.service.ExDmColdAccountRecordService;
import com.mz.exchange.account.service.ExDmHotAccountRecordService;
import java.math.BigDecimal;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/transaction/hxbexdmtransaction")
public class HxbExDmTransactionColler extends
    BaseController<ExDmTransaction, Long> {

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

  @RequestMapping("/list")
  @MethodName(name = "分页查询ExDmTransaction")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    String ss = request.getParameter("trueName_like");
    QueryFilter filter = new QueryFilter(ExDmTransaction.class, request);
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

  @RequestMapping(value = "/post/{id}", method = RequestMethod.GET)
  @MethodName(name = "通过一个订单")
  @ResponseBody
  public JsonResult see(@PathVariable Long id) {

    JsonResult jsonResult = new JsonResult();
    try {
      JsonResult js = super.get(id);
      ExDmTransaction transaction = (ExDmTransaction) js.getObj();
      if (transaction.getTransactionType() == 1) {
        if (transaction.getStatus() == 1) {
          String confirm = transaction.getConfirmations();
          if (null == confirm || "".equals(confirm)) {
            confirm = "0";
          }
          Long num = Long.valueOf(confirm);
          //确认节点数至少是2个的时候才可以通过
          if (num > 1) {
            String s = examineOrderService.pasePutOrder(id);
            if (s.equals("OK")) {
              jsonResult.setSuccess(true);
              return jsonResult;
            } else {
              jsonResult.setSuccess(false);
              jsonResult.setMsg("后台操作失败");
              return jsonResult;
            }
          } else {
            //重新查询确认节点数
            String txStr = CoinInterfaceUtil
                .row(transaction.getOrderNo(), transaction.getCoinCode());
            if (null != txStr && !"".equals(txStr)) {
              txStr = txStr.replace(" ", "");
              Map<String, Object> tx2map = StringUtil.str2map(txStr);
              String confirmations = tx2map.get("confirmations").toString();
              Long n = Long.valueOf(confirmations);
              if (n > 1) {
                String ss = examineOrderService.pasePutOrder(id);
                if (ss.equals("OK")) {
                  jsonResult.setSuccess(true);
                  return jsonResult;
                } else {
                  jsonResult.setSuccess(false);
                  jsonResult.setMsg("确认节点数至少为2才可以通过审核");
                  return jsonResult;
                }
              } else {
                jsonResult.setSuccess(false);
                jsonResult.setMsg("确认节点数至少为2才可以通过审核");
                return jsonResult;

              }
            }
          }
        } else {
          jsonResult.setSuccess(true);
          jsonResult.setMsg("已经确认过了");
          return jsonResult;
        }
      } else if (transaction.getTransactionType() == 2) {
        String address = transaction.getInAddress();
        BigDecimal money = transaction.getTransactionMoney().subtract(transaction.getFee());
        String coinCode = transaction.getCoinCode();
        RemoteAppOurAccountService remoteAppOurAccountService = (RemoteAppOurAccountService) ContextUtil
            .getBean("remoteAppOurAccountService");
        AppOurAccount ourAccount = remoteAppOurAccountService
            .findAppOurAccount(ContextUtil.getWebsite(), coinCode, Integer.valueOf("1"));
        //平台记录的我方账户的余额
        //if(ourAccount.getAccountMoney().setScale(4, BigDecimal.ROUND_HALF_UP).compareTo(money)>0){
        //查询用户钱包余额
        String balance = CoinInterfaceUtil.balance("", transaction.getCoinCode());
        if (null != balance && !"".equals(balance)) {
          BigDecimal purseBalance = new BigDecimal(balance);
          System.out.println("purseBalance=" + purseBalance);
          System.out.println("findAppOurAccount=" + ourAccount.getAccountMoney());
          if (purseBalance
              .compareTo(ourAccount.getAccountMoney().setScale(4, BigDecimal.ROUND_HALF_UP)) > 0) {
            //查询提币账户余额
            String withdrawBalance = CoinInterfaceUtil
                .balance(ourAccount.getAccountName(), transaction.getCoinCode());

            BigDecimal pursewithdrawBalance = new BigDecimal(withdrawBalance);
            if (pursewithdrawBalance.compareTo(
                transaction.getTransactionMoney().subtract(transaction.getFee())
                    .setScale(4, BigDecimal.ROUND_HALF_UP)) > 0) {
              jsonResult = sendTo(transaction.getOurAccountNumber(), address, money.toString(),
                  coinCode, id);
            } else {
              jsonResult.setMsg("提币账户余额不足");
              jsonResult.setSuccess(false);
            }


          } else {
            jsonResult.setMsg("钱包账户余额不足1");
            jsonResult.setSuccess(false);
          }
        } else {
          jsonResult.setMsg("钱包账户余额不足2");
          jsonResult.setSuccess(false);
        }
			
			 /* }else{
				  jsonResult.setMsg("我方账户余额不足");
				  jsonResult.setSuccess(false);
			  }*/

        return jsonResult;

      }


    } catch (Exception e) {
      e.printStackTrace();
      jsonResult.setSuccess(false);
      return jsonResult;
    }
    return jsonResult;

  }

  @RequestMapping(value = "/stop/{id}")
  @MethodName(name = "驳回一个订单")
  @ResponseBody
  public JsonResult stop(@PathVariable Long id, HttpServletRequest request) {

    JsonResult jsonResult = new JsonResult();
    String reason = request.getParameter("reason");
    try {
      String s = examineOrderService.paseStopeOrderToAppAccount(id);
      if (s.equals("OK")) {
        ExDmTransaction exDmTransaction = exDmTransactionService.get(id);
        exDmTransaction.setRejectionReason(reason);
        exDmTransactionService.update(exDmTransaction);
        jsonResult.setSuccess(true);
        return jsonResult;
      } else {
        jsonResult.setSuccess(false);
        return jsonResult;
      }
    } catch (Exception e) {
      jsonResult.setSuccess(false);
      return jsonResult;
    }

  }

  /**
   *
   * 调用钱包接口转出币
   * @author: Zhang Xiaofang
   * @param:    @param account  我方币种账户(转出币的账户)
   * @param:    @param address  提币账户(转入币的地址)
   * @param:    @param amount   数量
   * @param:    @param coinCode 币种类型
   * @param:    @param id
   * @param:    @return
   * @return: String
   * @Date :          2016年9月3日 下午3:59:00
   * @throws:
   */
  public JsonResult sendTo(String ourAccount, String address, String amount, String coinCode,
      Long id) {
    return null;
  }

}
