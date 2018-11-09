package com.mz.exapi.sign.controller;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.exapi.util.EncryptUtil;
import com.mz.util.sys.SpringContextUtil;
import com.mz.manage.remote.RemoteApiService;
import com.mz.manage.remote.model.ApiExApiApply;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/sign")
public class GetSign {


  //2.5获取币种钱包地址sign
  @RequestMapping(value = "/getPurseAddress", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getPurseAddress(String accesskey, String coinCode) {

    JsonResult jsonResult = new JsonResult();
    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    String sign = EncryptUtil.hmacSign(coinCode, exApiApply.getAccessKey());
    jsonResult.setObj(sign);
    jsonResult.setSuccess(true);
    return jsonResult;

  }

  //2.1获取委托下单sign
  @RequestMapping(value = "/getOrder", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getOrder(String accesskey, String entrustPrice, String type,
      String coinCode, String entrustCount) {

    JsonResult jsonResult = new JsonResult();
    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    String sign = EncryptUtil
        .hmacSign(entrustPrice + type + coinCode + entrustCount, exApiApply.getAccessKey());
    jsonResult.setObj(sign);
    jsonResult.setSuccess(true);
    return jsonResult;

  }

  //2.2取消委托下单sign
  @RequestMapping(value = "/getcancelOrder", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getcancelOrder(String accesskey, String coinCode) {

    JsonResult jsonResult = new JsonResult();
    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    String sign = EncryptUtil.hmacSign(coinCode, exApiApply.getAccessKey());
    jsonResult.setObj(sign);
    jsonResult.setSuccess(true);
    return jsonResult;

  }

  //2.3获取我的委托sign
  @RequestMapping(value = "/getallExEntrust", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult gettallExEntrust(String accesskey, String coinCode, String limit, String offset,
      String sortOrder, String querypath) {

    JsonResult jsonResult = new JsonResult();
    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    String sign = EncryptUtil
        .hmacSign(limit + offset + sortOrder + querypath, exApiApply.getAccessKey());
    jsonResult.setObj(sign);
    jsonResult.setSuccess(true);
    return jsonResult;

  }


  //2.4获取用户信息sign
  @RequestMapping(value = "/getAccountInfo", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getAccountInfo(String accesskey) {

    JsonResult jsonResult = new JsonResult();
    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    String sign = EncryptUtil.hmacSign(accesskey, exApiApply.getAccessKey());
    jsonResult.setObj(sign);
    jsonResult.setSuccess(true);
    return jsonResult;

  }

  //2.6获取用户体现地址sign
  @RequestMapping(value = "/getWithdrawAddr", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getWithdrawAddr(String accesskey) {

    JsonResult jsonResult = new JsonResult();
    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    String sign = EncryptUtil.hmacSign(accesskey, exApiApply.getAccessKey());
    jsonResult.setObj(sign);
    jsonResult.setSuccess(true);
    return jsonResult;

  }


  //2.7获取数字资产体现记录
  @RequestMapping(value = "/getFindCoinInfo", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getFindCoinInfo(String accesskey, String limit, String offset, String type,
      String sortOrder) {

    JsonResult jsonResult = new JsonResult();
    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    String sign = EncryptUtil
        .hmacSign(limit + offset + sortOrder + type, exApiApply.getAccessKey());
    jsonResult.setObj(sign);
    jsonResult.setSuccess(true);
    return jsonResult;

  }

  //2.8体现数字资产
  @RequestMapping(value = "/getWithDraw", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getWithDraw(String accesskey, String coinType, String btcNum, String btcKey,
      String pacecurrecy) {

    JsonResult jsonResult = new JsonResult();
    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    String sign = EncryptUtil
        .hmacSign(btcKey + coinType + btcNum + pacecurrecy, exApiApply.getAccessKey());
    jsonResult.setObj(sign);
    jsonResult.setSuccess(true);
    return jsonResult;

  }
}
