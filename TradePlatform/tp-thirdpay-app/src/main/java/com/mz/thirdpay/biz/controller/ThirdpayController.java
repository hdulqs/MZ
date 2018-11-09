/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Zhang Xiaofang
 * @version: V1.0
 * @Date: 2016年7月5日 下午3:14:31
 */
package com.mz.thirdpay.biz.controller;

import com.mz.ThirdPayInterfaceService;
import com.mz.account.fund.model.AppTransaction;
import com.mz.account.remote.RemoteAppTransactionService;
import com.mz.core.annotation.NoLogin;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.remote.RemoteThirdPayInterfaceService;
import com.mz.thirdpay.utils.ThirdPayUtil;
import com.mz.util.message.MessageConstant;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.utils.CommonRequest;
import com.mz.gopay.impl.GopayServiceImpl;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Zhang Xiaofang
 * @Date : 2016年7月5日 下午3:14:31
 */
@Controller
@RequestMapping("/pay/thirdpayconfig")
public class ThirdpayController {


  @MethodName(name = "现代金控充值回调方法(微信端)")
  @RequestMapping("/recharge_jinkongpay_weixin")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_jinkongpay_weixin(HttpServletResponse response,
      HttpServletRequest request) {
    System.out.println("接收到现代金控微信端充值回调");
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    map.put("thirdPayConfig", "jinkongpay_weixin");// 用于区别哪个第三方
    return callRemoteRechargeCallBack(map);
  }

  @MethodName(name = "银湾付充值回调方法")
  @RequestMapping("/notify_yinwanpay")
  @NoLogin
  @ResponseBody
  public ModelAndView notify_yinwanpay(HttpServletResponse response, HttpServletRequest request) {
    ModelAndView m = new ModelAndView();
    m.setViewName("front/user/rmbdeposit");
    return m;
  }

  @MethodName(name = "银湾付充值回调方法")
  @RequestMapping("/recharge_yinwanpay")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_yinwanpay(HttpServletResponse response, HttpServletRequest request) {
    System.out.println("接收到银湾付充值回调");
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    System.out.println(map);
    map.put("thirdPayConfig", "yinwanpay");// 用于区别哪个第三方
    JsonResult result = callRemoteRechargeCallBack(map);
    System.out.println(result.toString());
    return result;
  }

  @MethodName(name = "现代金控充值回调方法(支付宝)")
  @RequestMapping("/recharge_jinkongpay_zhifubao")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_jinkongpay_zhifubao(HttpServletResponse response,
      HttpServletRequest request) {
    System.out.println("接收到现代金控支付宝端充值回调");
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    map.put("thirdPayConfig", "jinkongpay_zhifubao");// 用于区别哪个第三方
    return callRemoteRechargeCallBack(map);
  }

  @MethodName(name = "现代金控充值回调方法(网银)")
  @RequestMapping("/recharge_jinkongpay_wangyin")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_jinkongpay_wangyin(HttpServletResponse response,
      HttpServletRequest request) {
    System.out.println("接收到现代金控网银端充值回调");
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    map.put("thirdPayConfig", "jinkongpay_wangyin");// 用于区别哪个第三方
    return callRemoteRechargeCallBack(map);
  }

  @MethodName(name = "现代金控充值回调方法(有收银台)")
  @RequestMapping("/recharge_jinkongpay")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_jinkongpay(HttpServletResponse response, HttpServletRequest request) {
    System.out.println("接收到现代金控充值回调(有收银台)");
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    map.put("thirdPayConfig", "jinkongpay_havepage");// 用于区别哪个第三方
    return callRemoteRechargeCallBack(map);
  }

  @MethodName(name = "现代金控充值回调同步方法(有收银台)")
  @RequestMapping("/recharge_jinkongpay_tbhd")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_jinkongpay_tbhd(HttpServletResponse response,
      HttpServletRequest request) {
    StringBuffer sb = new StringBuffer();
    sb.append("<html><body>");
    sb.append("支付成功 <a href='http://testfront.100btcoin.com/'>返回到系统</a>");
    sb.append("</body></html>");

    response.setContentType("text/html; charset=utf-8");
    OutputStream outputStream;
    try {
      outputStream = response.getOutputStream();
      byte[] dataByteArr = sb.toString().getBytes("UTF-8");// 将字符转换成字节数组，指定以UTF-8编码进行转换
      outputStream.write(dataByteArr);// 使用OutputStream流向客户端输出字节数组
      outputStream.flush();
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @MethodName(name = "环迅充值回调方法")
  @RequestMapping("/recharge_ipspay")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_ipspay(HttpServletResponse response, HttpServletRequest request) {
    System.out.println("接收到环迅充值回调");
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    map.put("thirdPayConfig", "ipspay");// 用于区别哪个第三方
    return callRemoteRechargeCallBack(map);
  }

  @MethodName(name = "闪付充值回调异步方法")
  @RequestMapping("/recharge_shanfupay")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_shanfupay(HttpServletResponse response, HttpServletRequest request) {
    System.out.println("接收到闪付充值回调");
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    map.put("thirdPayConfig", "shanfupay");// 用于区别哪个第三方
    return callRemoteRechargeCallBack(map);
  }

  @MethodName(name = "闪付充值回调同步方法")
  @RequestMapping("/recharge_shanfupay_tbhd")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_shanfupay_tbhd(HttpServletResponse response,
      HttpServletRequest request) {
    StringBuffer sb = new StringBuffer();
    sb.append("<html><body>");
    sb.append("支付成功 <a href='http://www.longyin.trade'>返回到系统</a>");
    sb.append("</body></html>");

    response.setContentType("text/html; charset=utf-8");
    OutputStream outputStream;
    try {
      outputStream = response.getOutputStream();
      byte[] dataByteArr = sb.toString().getBytes("UTF-8");// 将字符转换成字节数组，指定以UTF-8编码进行转换
      outputStream.write(dataByteArr);// 使用OutputStream流向客户端输出字节数组
      outputStream.flush();
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @MethodName(name = "国付宝充值回调方法")
  @RequestMapping("/recharge_gopay")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_gopay(HttpServletResponse response, HttpServletRequest request) {
    System.out.println("接收到国付宝充值回调");
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    map.put("thirdPayConfig", "gopay");// 用于区别哪个第三方
    return callRemoteRechargeCallBack(map);
  }

  @MethodName(name = "智付充值回调方法")
  @RequestMapping("/recharge_dinpay")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_dinpay(HttpServletResponse response, HttpServletRequest request) {
    System.out.println("接收到智付充值回调");
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    map.put("thirdPayConfig", "dinpay");// 用于区别哪个第三方
    return callRemoteRechargeCallBack(map);
  }

  @MethodName(name = "huicao充值回调方法")
  @RequestMapping("/recharge_huicaopay")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_huicao(HttpServletResponse response, HttpServletRequest request) {
    System.out.println("接收到huicao充值回调");
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    map.put("thirdPayConfig", "huicaopay");// 用于区别哪个第三方
    return callRemoteRechargeCallBack(map);
  }

  @MethodName(name = "创新支付充值回调方法(网银)")
  @RequestMapping("/recharge_inpay_wangyin")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_inpay_wangyin(HttpServletResponse response,
      HttpServletRequest request) {
    System.out.println("接收到创新支付网银端充值回调");
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    map.put("thirdPayConfig", "inpay");// 用于区别哪个第三方
    return callRemoteRechargeCallBack(map);
  }

  @MethodName(name = "创新支付充值回调方法(支付宝)")
  @RequestMapping("/recharge_inpay_zhifubao")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_inpay_zhifubao(HttpServletResponse response,
      HttpServletRequest request) {
    System.out.println("接收到创新支付支付宝端充值回调");
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    map.put("thirdPayConfig", "inpay_zhifubao");// 用于区别哪个第三方
    return callRemoteRechargeCallBack(map);
  }

  @MethodName(name = "创新支付充值回调方法(微信端)")
  @RequestMapping("/recharge_inpay_weixin")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_inpay_weixin(HttpServletResponse response,
      HttpServletRequest request) {
    System.out.println("接收到创新支付微信端充值回调");
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    map.put("thirdPayConfig", "inpay_weixin");// 用于区别哪个第三方
    return callRemoteRechargeCallBack(map);
  }

  @MethodName(name = "创新支付充值回调方法(快捷)")
  @RequestMapping("/recharge_inpay_quick")
  @NoLogin
  @ResponseBody
  public JsonResult recharge_inpay_quick(HttpServletResponse response, HttpServletRequest request) {
    System.out.println("接收到创新支付快捷充值回调");
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    map.put("thirdPayConfig", "inpay_kuaijie");// 用于区别哪个第三方
    return callRemoteRechargeCallBack(map);
  }

  @MethodName(name = "请求到远程充值回调")
  @NoLogin
  public JsonResult callRemoteRechargeCallBack(Map<String, Object> map) {
    JsonResult jsonResult = new JsonResult();
    RemoteThirdPayInterfaceService remoteThirdPayInterfaceService = (RemoteThirdPayInterfaceService) ContextUtil
        .getBean("remoteThirdPayInterfaceService");
    com.mz.utils.CommonRequest result = remoteThirdPayInterfaceService.rechargeCallBack(map);
    RemoteAppTransactionService remoteAppTransactionService = (RemoteAppTransactionService) ContextUtil
        .getBean("remoteAppTransactionService");
    if (result.getResponseCode().equals(MessageConstant.SUCCESS)) {
      remoteAppTransactionService.passOrder(result.getRequestNo(), "online", Integer.valueOf(1));
      jsonResult.setObj(map);
      jsonResult.setSuccess(true);
      jsonResult.setMsg("充值回调成功---业务处理完成");
    } else {
      remoteAppTransactionService.passOrder(result.getRequestNo(), "online", Integer.valueOf(0));
      jsonResult.setObj(map);
      jsonResult.setSuccess(false);
      jsonResult.setMsg("充值回调失败---业务处理完成，" + result.getResponseMsg());
    }
    return jsonResult;
  }

  // =====================================================================================

  @MethodName(name = "提现回调方法")
  @RequestMapping("/withdraw")
  @NoLogin
  @ResponseBody
  public JsonResult withdraw(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    RemoteThirdPayInterfaceService remoteThirdPayInterfaceService = (RemoteThirdPayInterfaceService) ContextUtil
        .getBean("remoteThirdPayInterfaceService");
    String msg = request.getParameter("notifyMsg");
    map.put("notifyMsg", msg);
    com.mz.utils.CommonRequest result = remoteThirdPayInterfaceService.withdrawCallBack(map);
    RemoteAppTransactionService remoteAppTransactionService = (RemoteAppTransactionService) ContextUtil
        .getBean("remoteAppTransactionService");

    if (null != result.getResponseCode() && !"".equals(result.getResponseCode())) {
      if (result.getResponseCode().equals(MessageConstant.SUCCESS)) {
        remoteAppTransactionService
            .withdraw(Long.valueOf(result.getRequestNo()), "online", Integer.valueOf(1));
        jsonResult.setObj(map);
        jsonResult.setSuccess(true);
        jsonResult.setMsg("提现回调成功---业务处理完成--提现成功");
      } else if (result.getResponseCode().equals("faile")) {
        remoteAppTransactionService
            .withdraw(Long.valueOf(result.getRequestNo()), "online", Integer.valueOf(0));
        jsonResult.setObj(map);
        jsonResult.setSuccess(false);
        jsonResult.setMsg("提现回调成功---业务处理完成");
      } else {
        jsonResult.setMsg(result.getResponseMsg());
        jsonResult.setObj(result.getResponseCode());
      }
      System.out.println("提现回调---" + result.getResponseMsg());
    } else {
      jsonResult.setMsg("信息为空");
      jsonResult.setObj("null");
    }

    return jsonResult;
  }

  @MethodName(name = "查询方法")
  @RequestMapping("/queryOrder")
  @NoLogin
  public String query(HttpServletResponse response) {
    System.out.println("查询方法");

    ThirdPayInterfaceService thirdPayInterfaceService = new GopayServiceImpl();

    CommonRequest req = new CommonRequest();
    req.setAmount("100.00");
    req.setRequestNo("01160714172038061694");
    thirdPayInterfaceService.queryOrder(req);
    return null;

  }

  @MethodName(name = "第三方支付回调")
  @RequestMapping("/html")
  @NoLogin
  public String html(HttpServletRequest request, HttpServletResponse response) {

    Map<String, Object> map = ThirdPayUtil.createResponseMap(request);
    System.out.println("request.getParameter(BillNo).toString()===" + map.toString());
    RemoteThirdPayInterfaceService remoteThirdPayInterfaceService = (RemoteThirdPayInterfaceService) ContextUtil
        .getBean("remoteThirdPayInterfaceService");
    com.mz.utils.CommonRequest result = remoteThirdPayInterfaceService.rechargeCallBack(map);

    String billNo = (String) map.get("BillNo");
    System.out.println("billNo=" + billNo);
    RemoteAppTransactionService remoteAppTransactionService = (RemoteAppTransactionService) ContextUtil
        .getBean("remoteAppTransactionService");
    AppTransaction transaction = remoteAppTransactionService.getByTransactionNum(billNo);
    String frontweb = "";
    if (transaction != null && "en".equals(transaction.getWebsite())) {
      frontweb = PropertiesUtils.APP.getProperty("app.emailurl_en");
    } else {
      frontweb = PropertiesUtils.APP.getProperty("app.emailurl_cn");
    }

    return "redirect:" + frontweb.replace("exmain", "") + "static/pay/pay.html?order="
        + request.getParameter("BillNo").toString() + "&Amount=" + request.getParameter("Amount")
        .toString()
        + "&Succeed=" + result.getResponseCode() + "&Result=" + result.getResponseMsg();
  }
}
