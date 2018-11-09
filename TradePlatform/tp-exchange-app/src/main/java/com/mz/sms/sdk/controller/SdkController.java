/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年3月28日 下午5:38:24
 */
package com.mz.sms.sdk.controller;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.mz.core.annotation.NoLogin;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.exchange.account.model.ExApiApply;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.sms.send.model.AppSmsSend;
import com.mz.util.QueryFilter;
import com.mz.util.idgenerate.IdGenerate;
import com.mz.util.idgenerate.NumConstant;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.security.Check;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.ContextUtil;
import com.mz.util.urlencode.URLEncodeUtils;
import com.mz.core.mvc.service.AppSmsSend.AppSmsSendService;
import com.mz.customer.person.service.AppPersonInfoService;
import com.mz.exchange.account.service.ExApiApplyService;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.mq.producer.service.MessageProducer;
import com.mz.sms.sdk.service.SdkService;
import com.mz.trade.redis.model.Accountadd;
import com.mz.trade.redis.model.EntrustTrade;
import com.mz.trade.redis.model.ExDigitalmoneyAccountRedis;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Liu Shilei
 * @Date : 2016年3月28日 下午5:38:24
 */
@Controller
@RequestMapping("/sdk")
public class SdkController {

  @Resource
  private MessageProducer messageProducer;

  @Resource
  private AppPersonInfoService appPersonInfoService;

  @InitBinder
  public void initBinder(ServletRequestDataBinder binder) {
    /**
     * 自动转换日期类型的字段格式
     */
    binder.registerCustomEditor(Date.class, new DateTimePropertyEditorSupport());

    /**
     * 防止XSS攻击，并且带去左右空格功能
     */
    binder.registerCustomEditor(String.class, new StringPropertyEditorSupport(true, true));
  }


  @MethodName(name = "发送短信")
  @RequestMapping(value = "/test1")
  @NoLogin
  @ResponseBody
  public JsonResult test1(HttpServletRequest request) {
    return null;
  }

  @MethodName(name = "验证身份证")
  @RequestMapping(value = "/checkCard")
  @NoLogin
  @ResponseBody
  public JsonResult checkCard(HttpServletRequest request) throws UnsupportedEncodingException {

    String name = request.getParameter("name");
    System.out.println("实名认证的name:" + name);
    String idCard = request.getParameter("idCard");
    String smsKey = request.getParameter("smsKey");
    JsonResult jsonResult = new JsonResult();
    try {
      if (!StringUtils.isEmpty(smsKey) && "hurongyuseen".equals(smsKey)) {

        SdkService sdkService;
        String serviceName = PropertiesUtils.APP.getProperty("app.smsServiceName1111");
        if (!StringUtils.isEmpty(serviceName)) {
          sdkService = (SdkService) ContextUtil.getBean(serviceName);
        } else {
          sdkService = (SdkService) ContextUtil.getBean("sdkService");
        }
        return sdkService.checkCard(URLEncodeUtils.Utf8URLdecode(name), idCard);
      } else {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("非法请求，密码不正确");
      }
      return jsonResult;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;

  }


  @MethodName(name = "发送短信")
  @RequestMapping(value = "/send")
  @NoLogin
  @ResponseBody
  public JsonResult send(HttpServletRequest request, String phone) {

    String param = request.getParameter("param");
    LogFactory.info("发送短信，接收到的请求参数：" + param);
    SmsParam smsParam = JSON.parseObject(param, SmsParam.class);

    if (SmsSendUtil.WITHDRAW_RMBORCOIN.equals(smsParam.getHrySmstype())
        || SmsSendUtil.WITHDRAW_RMBORCOIN_FRONT.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_RMBWITHDRAW_INVALID.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_COINWITHDRAW_INVALID.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_RMBDEPOSIT_INVALID.equals(smsParam.getHrySmstype())) {
      // 汉字需要转码
      String code = smsParam.getHryCode();
      LogFactory.info("发送短信，接收到的请求参数HryCode（提现）：" + code);

      if ("BTC".equals(code)) {
        smsParam.setHryCode("比特币");
      } else if ("LTC".equals(code)) {
        smsParam.setHryCode("莱特币");
      } else if ("CRTC".equals(code)) {
        smsParam.setHryCode("联合学分");
      }
    }

    // 内部验证密码
    String smsKey = smsParam.getSmsKey();
    // 获得sendId
    Long sendId = smsParam.getSendId();
    LogFactory.info("收到短信发送请求sendId=" + sendId);
    try {
      Thread.sleep(5000);// 休息5秒

      JsonResult jsonResult = new JsonResult();
//			MongoUtil<AppSmsSend, Long> mongoUtil = new MongoUtil<AppSmsSend, Long>(AppSmsSend.class);
      AppSmsSendService appSmsSendService = (AppSmsSendService) ContextUtil
          .getBean("appSmsSendService");
      //	AppSmsSend appSmsSend = appSmsSendService.get(sendId);
//			AppSmsSend appSmsSend = mongoUtil.get(sendId);
      //if (appSmsSend != null) {
      AppSmsSend appSmsSend = new AppSmsSend();
      // 标记为已接收到些条记录
      appSmsSend.setReceiveStatus("1");
      // 判断密钥
      if (!StringUtils.isEmpty(smsKey) && "hurongyuseen".equals(smsKey)) {
        String code = smsParam.getHryCode();
        LogFactory.info("短信验证码为:" + code);

        SdkService sdkService;
        String serviceName = PropertiesUtils.APP.getProperty("app.smsServiceName");
        if (!StringUtils.isEmpty(serviceName)) {
          sdkService = (SdkService) ContextUtil.getBean(serviceName);
        } else {
          sdkService = (SdkService) ContextUtil.getBean("sdkService");
        }

        boolean sendSms = sdkService.sendSms(appSmsSend, smsParam, phone);
        // 如果发送标记为成功,则标记为已发送
        if (sendSms) {
          appSmsSend.setSendStatus("1");
        }
        jsonResult.setSuccess(sendSms);
      } else {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("非法请求，密码不正确");
      }
      //.update(appSmsSend);
      appSmsSendService.save(appSmsSend);
//				mongoUtil.save(appSmsSend);
		/*	} else {
				LogFactory.info("没有查询到数据库发送记录");
				jsonResult.setSuccess(false);
				jsonResult.setMsg("非法请求，拒之门外");
			}*/
      return jsonResult;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;

  }

  /**
   * 给kk提供刷新redis币账户接口
   */
  @RequestMapping(value = "/getAccountKK", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult getAccountKK(HttpServletRequest request) {
    JsonResult j = new JsonResult();

    String id = request.getParameter("id");
    ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService) ContextUtil
        .getBean("exDigitalmoneyAccountService");
    ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService
        .get(Long.valueOf(id));

    if (exDigitalmoneyAccount != null) {
      ExDigitalmoneyAccountRedis exar = new ExDigitalmoneyAccountRedis();
      exar.setCoinCode(exDigitalmoneyAccount.getCoinCode());
      exar.setColdMoney(exDigitalmoneyAccount.getColdMoney());
      exar.setHotMoney(exDigitalmoneyAccount.getHotMoney());
      exar.setCustomerId(exDigitalmoneyAccount.getCustomerId());
      exar.setId(exDigitalmoneyAccount.getId());

      RedisUtil<ExDigitalmoneyAccountRedis> redisUtil = new RedisUtil<ExDigitalmoneyAccountRedis>(
          ExDigitalmoneyAccountRedis.class);
      redisUtil.put(exar, exDigitalmoneyAccount.getId().toString());
      j.setSuccess(true);
      return j;
    }
    j.setSuccess(false);
    return j;
  }

  /**
   * 充币
   */
  @RequestMapping(value = "/rechargeCodeKK", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult rechargeCodeKK(HttpServletRequest request) {
    JsonResult j = new JsonResult();
    try {
      ExDmTransactionService exDmTransactionService = (ExDmTransactionService) ContextUtil
          .getBean("exDmTransactionService");

      String username = request.getParameter("username");//用户名
      String transactionMoney = request.getParameter("transactionMoney");//提币数量
      String fee = request.getParameter("fee");//手续费
      String trueName = request.getParameter("trueName");//名
      String surname = request.getParameter("surname");//姓
      String coinCode = request.getParameter("coinCode");//币代码
      String CurrencyType = request.getParameter("CurrencyType");//法币代码
      String inAddress = request.getParameter("inAddress");//钱包转入地址
      String remark = request.getParameter("remark");//备注
      String orderNo = request.getParameter("orderNo");//txid
      String authcode = request.getParameter("authcode");//备注
      if (trueName != null && surname != null) {
        trueName = trueName.trim();
        surname = surname.trim();
      }

      String[] params = {transactionMoney, fee, username, surname, trueName, coinCode, CurrencyType,
          inAddress, orderNo, remark};
      String getcode = Check.authCode(params);
      System.out.print("authcode: " + authcode);
      System.out.print("getcode: " + getcode);
      //判断
      if (getcode.equals(authcode)) {
        QueryFilter queryFilter = new QueryFilter(ExDmTransaction.class);
        queryFilter.addFilter("customerName=", username);
        queryFilter.addFilter("orderNo=", orderNo);
        ExDmTransaction transaction = exDmTransactionService.get(queryFilter);
        if (null == transaction) {
          //币位数
          Integer keepDecimalForCoin = 8;
          RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
          String str = redisService.get("cn:productinfoListall");
          if (!StringUtils.isEmpty(str)) {
            JSONArray array = JSON.parseArray(str);
            if (array != null) {
              for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                if (coinCode.equals(jsonObject.getString("coinCode"))) {
                  keepDecimalForCoin = jsonObject.getInteger("keepDecimalForCoin");
                }
              }
            }
          }

          System.out.println("进入充币了");
          if (new BigDecimal(transactionMoney).compareTo(new BigDecimal(0)) > 0
              && new BigDecimal(fee).compareTo(new BigDecimal(0)) >= 0) {
            System.out.println("进入第一个if");
            if (StringUtil.isNotEmpty(username) && StringUtil.isNotEmpty(coinCode)) {
              System.out.println("进入第二个if");
              RemoteManageService remoteManageService = (RemoteManageService) ContextUtil
                  .getBean("remoteManageService");
              System.out.println("1==========userName=" + username + "    coinCode=" + coinCode);
              Map<String, Object> map = remoteManageService.selectRechargeCoin(username, coinCode);
              System.out.println(map.get("customerId") + " " + map.get("accountId"));
              if (map != null && map.size() > 0) {
                if (map.get("customerId") != null && map.get("accountId") != null) {
                  ExDmTransaction exDmTransaction = new ExDmTransaction();
                  exDmTransaction.setCustomerId(Long.valueOf(map.get("customerId").toString()));
                  String transactionNum = NumConstant.Ex_Dm_Transaction;
                  exDmTransaction.setTransactionNum(IdGenerate.transactionNum(transactionNum));
                  exDmTransaction.setAccountId(Long.valueOf(map.get("accountId").toString()));
                  exDmTransaction.setTransactionType(1);
                  exDmTransaction.setTransactionMoney(new BigDecimal(transactionMoney)
                      .setScale(keepDecimalForCoin, BigDecimal.ROUND_DOWN));
                  exDmTransaction.setCustomerName(username == null ? "" : username);
                  exDmTransaction.setTrueName(trueName == null ? "" : trueName);
                  exDmTransaction.setSurname(surname);

                  try {
                    QueryFilter filter = new QueryFilter(AppPersonInfo.class);
                    filter.addFilter("customerId=", Long.valueOf(map.get("customerId").toString()));
                    AppPersonInfo appPersonInfo = appPersonInfoService.get(filter);
                    if (appPersonInfo != null) {
                      exDmTransaction.setTrueName(appPersonInfo.getTrueName());
                      exDmTransaction.setSurname(appPersonInfo.getSurname());
                    }
                  } catch (Exception e) {
                    System.out.println(
                        "充值获得用户信息失败!customerId=" + Long.valueOf(map.get("customerId").toString()));
                    e.printStackTrace();
                  }

                  exDmTransaction.setStatus(2);
                  exDmTransaction.setCoinCode(coinCode);
                  exDmTransaction.setCurrencyType(CurrencyType);
                  exDmTransaction.setSaasId(RpcContext.getContext().getAttachment(
                      "saasId"));
                  exDmTransaction.setFee(BigDecimal.ZERO);
                  exDmTransaction.setInAddress(inAddress);
                  //exDmTransaction.setOrderNo(exDmTransaction.getTransactionNum());
                  exDmTransaction.setOrderNo(orderNo);
                  exDmTransaction.setRemark(remark);
                  // 保存订单
                  exDmTransactionService.save(exDmTransaction);

                  //热账户增加
                  Accountadd accountadd2 = new Accountadd();
                  accountadd2.setAccountId(exDmTransaction.getAccountId());
                  accountadd2.setMoney(
                      exDmTransaction.getTransactionMoney().subtract(exDmTransaction.getFee()));
                  accountadd2.setMonteyType(1);
                  accountadd2.setAcccountType(1);
                  accountadd2.setRemarks(31);
                  accountadd2.setTransactionNum(exDmTransaction.getTransactionNum());

                  List<Accountadd> list = new ArrayList<Accountadd>();
                  list.add(accountadd2);
                  messageProducer.toAccount(JSON.toJSONString(list));
                  j.setSuccess(true);
                  j.setMsg("充币成功");
                } else {
                  j.setSuccess(false);
                  j.setCode("1000");
                  j.setMsg("账户不存在");
                }
              } else {
                j.setSuccess(false);
                j.setCode("1000");
                j.setMsg("账户不存在");
                j.setObj(map.get("customerId") + " " + map.get("accountId"));
              }
            } else {
              j.setSuccess(false);
              j.setCode("1000");
              j.setMsg("参数不正确");
            }
          } else {
            j.setSuccess(false);
            j.setCode("1000");
            j.setMsg("参数不正确");
          }

        } else {
          j.setSuccess(false);
          j.setCode("9999");
          j.setMsg("冲币重复");
        }
      } else {
        j.setSuccess(false);
        j.setCode("9998");
        j.setMsg("参数加密不一致");
      }

    } catch (Exception e) {
      e.printStackTrace();
      j.setSuccess(false);
      j.setCode("1000");
      j.setMsg("充币异常");
    }
    return j;
  }


  /**
   * 提币
   */
  @RequestMapping(value = "/withdrawCodeKK", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult withdrawCodeKK(HttpServletRequest request) {
    JsonResult j = new JsonResult();

    ExDmTransactionService exDmTransactionService = (ExDmTransactionService) ContextUtil
        .getBean("exDmTransactionService");

    String customerId = request.getParameter("customerId");//用户ID
    String accountId = request.getParameter("accountId");//币账户ID
    String transactionMoney = request.getParameter("transactionMoney");//提币数量
    String fee = request.getParameter("fee");//用户ID
    String customerName = request.getParameter("customerName");//用户名
    String trueName = request.getParameter("trueName");//名
    String surname = request.getParameter("surname");//姓
    String coinCode = request.getParameter("coinCode");//币代码
    String CurrencyType = request.getParameter("CurrencyType");//法币代码
    String remark = request.getParameter("remark");//备注

    if (accountId != null && !"".equals(accountId)) {
      RedisUtil<ExDigitalmoneyAccountRedis> a = new RedisUtil<ExDigitalmoneyAccountRedis>(
          ExDigitalmoneyAccountRedis.class);
      ExDigitalmoneyAccountRedis exDigitalmoneyAccountRedis = a.get(accountId);

      if (exDigitalmoneyAccountRedis != null) {
        if (exDigitalmoneyAccountRedis.getHotMoney().compareTo(new BigDecimal(0)) > 0
            && exDigitalmoneyAccountRedis.getHotMoney().compareTo(new BigDecimal(transactionMoney))
            >= 0) {
          ExDmTransaction exDmTransaction = new ExDmTransaction();
          exDmTransaction.setCustomerId(Long.valueOf(customerId));
          String transactionNum = NumConstant.Ex_Dm_Transaction;
          exDmTransaction.setTransactionNum(IdGenerate.transactionNum(transactionNum));
          exDmTransaction.setAccountId(Long.valueOf(accountId));
          exDmTransaction.setTransactionType(2);
          exDmTransaction.setTransactionMoney(new BigDecimal(transactionMoney));
          exDmTransaction.setCustomerName(customerName);
          exDmTransaction.setTrueName(trueName);
          exDmTransaction.setSurname(surname);
          exDmTransaction.setStatus(2);
          exDmTransaction.setCoinCode(coinCode);
          exDmTransaction.setCurrencyType(CurrencyType);
          //exDmTransaction.setWebsite(order.getWebsite());
          exDmTransaction.setSaasId(RpcContext.getContext().getAttachment(
              "saasId"));
          exDmTransaction.setFee(new BigDecimal(fee));
          exDmTransaction.setRemark(remark);
          //exDmTransaction.setOurAccountNumber(order.getOurAccountNumber());
          //exDmTransaction.setInAddress(order.getCurrencyKey());
          //exDmTransaction.setInAddress(order.getInAddress());
          //exDmTransaction.setOutAddress(order.getOurAccountNumber());
          // 保存订单
          exDmTransactionService.save(exDmTransaction);

          //热账户减少
          Accountadd accountadd2 = new Accountadd();
          accountadd2.setAccountId(exDmTransaction.getAccountId());
          accountadd2.setMoney(
              exDmTransaction.getTransactionMoney().subtract(exDmTransaction.getFee())
                  .multiply(new BigDecimal(-1)));
          accountadd2.setMonteyType(1);
          accountadd2.setAcccountType(1);
          accountadd2.setRemarks(33);
          accountadd2.setTransactionNum(exDmTransaction.getTransactionNum());

          List<Accountadd> list = new ArrayList<Accountadd>();
          list.add(accountadd2);
          messageProducer.toAccount(JSON.toJSONString(list));
          j.setSuccess(true);
          j.setMsg("提币成功");
        } else {
          j.setSuccess(false);
          j.setCode("1000");
          j.setMsg("余额不足");
        }
      } else {
        j.setSuccess(false);
        j.setCode("1001");
        j.setMsg("币账户不存在");
      }
    } else {
      j.setSuccess(false);
      j.setCode("1001");
      j.setMsg("币账户不存在");
    }
    return j;
  }

  /**
   * 全部撤销
   */
  @RequestMapping(value = "/allcancelKK", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult allcancelKK(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();

    String accesskey = request.getParameter("accesskey");//用户key
    String coinCode = request.getParameter("coinCode");//币代码

    if (accesskey != null && !"".equals(accesskey)) {
      ExApiApplyService exApiApplyService = (ExApiApplyService) ContextUtil
          .getBean("exApiApplyService");

      QueryFilter qf = new QueryFilter(ExApiApply.class);
      qf.addFilter("accessKey=", accesskey);
      ExApiApply exApiApply = exApiApplyService.get(qf);
      if (exApiApply != null) {
        String[] split = coinCode.split("_");
        EntrustTrade entrustTrade = new EntrustTrade();
        entrustTrade.setCoinCode(split[0]);
        entrustTrade.setFixPriceCoinCode(split[1]);
        entrustTrade.setCustomerId(exApiApply.getCustomerId());
        RemoteManageService remoteManageService = (RemoteManageService) ContextUtil
            .getBean("remoteManageService");
        remoteManageService.cancelCustAllExEntrust(entrustTrade);
        jsonResult.setSuccess(true);
        jsonResult.setMsg("撤销成功");
      } else {
        jsonResult.setSuccess(false);
        jsonResult.setCode("1000");
        jsonResult.setMsg("key值不存在");
      }
    } else {
      jsonResult.setSuccess(false);
      jsonResult.setCode("1000");
      jsonResult.setMsg("key值不存在");
    }
    return jsonResult;
  }

  /**
   * 拉取交易对最新价格
   */
  @RequestMapping(value = "/newTransactionPrice", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult newTransactionPrice(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();

    String coinCode = request.getParameter("coinCode");//交易对
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    if (!StringUtils.isEmpty(coinCode)) {
      String price = redisService.get(coinCode + ":currentExchangPrice");
      jsonResult.setSuccess(true);
      jsonResult.setMsg(price);
    } else {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("交易对不存在");
    }
    return jsonResult;

  }
}
