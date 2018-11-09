/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年8月25日 下午3:21:39
 */
package com.mz.front.sms.controller;

import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.util.DrawPictureUtil;
import com.mz.util.IpUtil;
import com.mz.util.SessionUtils;
import com.mz.util.sys.SpringContextUtil;;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
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
 * @Date : 2016年8月25日 下午3:21:39
 */
@Controller
@RequestMapping("/sms")
public class SmsController {

  private final static Logger log = Logger.getLogger(SmsController.class);

  private String tel = "tel:";

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

  /**
   * 发送修改登录密码时的短信验证码
   */
  @RequestMapping(value = "/getPwSmsCode", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getPwSmsCode(HttpServletRequest request) {
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }

    String language = (String) request.getAttribute("language");

    JsonResult jsonResult = new JsonResult();
    User user = SessionUtils.getUser(request);
    if (user != null) {
      String oldPassWord = request.getParameter("oldPassWord");
      String newPassWord = request.getParameter("newPassWord");
      String reNewPassWord = request.getParameter("reNewPassWord");
      if (StringUtils.isEmpty(oldPassWord)) {
        jsonResult.setSuccess(false);
        jsonResult.setMsg(SpringContextUtil.diff("oldpwd_no_null"));
      } else if (StringUtils.isEmpty(newPassWord)) {
        jsonResult.setSuccess(false);
        jsonResult.setMsg(SpringContextUtil.diff("jypwd_no_null"));
      } else if (!newPassWord.equals(reNewPassWord)) {
        jsonResult.setSuccess(false);
        jsonResult.setMsg(SpringContextUtil.diff("twopwd_is_diff"));
      } else {
        // 设置短信验证码到session中
        SmsParam smsParam = new SmsParam();
        smsParam.setHryMobilephone(user.getMobile());
        smsParam.setHrySmstype(SmsSendUtil.MODIFY_LOGIN_PW);
        request.getSession()
            .setAttribute("pwSmsCode", SmsSendUtil.sendSmsCode(smsParam, null, null));
        jsonResult.setSuccess(true);
        jsonResult.setMsg(SpringContextUtil.diff("sms_success"));
      }
    } else {
      jsonResult.setSuccess(false);
    }
    return jsonResult;
  }

  /**
   * 发送设置交易密码的短信短信验证码
   */
  @RequestMapping(value = "/getApwSmsCode", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getApwSmsCode(HttpServletRequest request) {
    String language = (String) request.getAttribute("language");
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }
    JsonResult jsonResult = new JsonResult();
    User user = SessionUtils.getUser(request);
    if (user.getPhoneState() == 0) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg(SpringContextUtil.diff("weiwanchengsjbd"));
      return jsonResult;
    }
    if (user != null) {
      String accountPassWord = request.getParameter("accountPassWord");
      String reaccountPassWord = request.getParameter("reaccountPassWord");
      if (StringUtils.isEmpty(accountPassWord)) {
        jsonResult.setSuccess(false);
        jsonResult.setMsg(SpringContextUtil.diff("jypwd_no_null"));
      } else if (!accountPassWord.equals(reaccountPassWord)) {
        jsonResult.setSuccess(false);
        jsonResult.setMsg(SpringContextUtil.diff("twopwd_is_diff"));
      } else {
        // 设置短信验证码到session中
        SmsParam smsParam = new SmsParam();
        smsParam.setHryMobilephone(user.getPhone());
        smsParam.setHrySmstype(SmsSendUtil.SET_CHANGE_PW);
        request.getSession()
            .setAttribute("accountpwSmsCode", SmsSendUtil.sendSmsCode(smsParam, null, null));
        jsonResult.setSuccess(true);
        jsonResult.setMsg(SpringContextUtil.diff("sms_success"));
      }
    } else {
      jsonResult.setSuccess(false);
    }

    return jsonResult;
  }

  /**
   * 发送重置交易密码的短信短信验证码
   */
  @RequestMapping(value = "/getReApwSmsCode", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getReApwSmsCode(HttpServletRequest request) {
    String language = (String) request.getAttribute("language");
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }
    JsonResult jsonResult = new JsonResult();
    User user = SessionUtils.getUser(request);
    if (user != null) {
      String passWord = request.getParameter("passWord");
      String accountPassWord = request.getParameter("accountPassWord");
      String reaccountPassWord = request.getParameter("reaccountPassWord");
      if (StringUtils.isEmpty(passWord)) {
        jsonResult.setSuccess(false);
        jsonResult.setMsg(SpringContextUtil.diff("loginpwd_no_null"));
      } else if (StringUtils.isEmpty(accountPassWord)) {
        jsonResult.setSuccess(false);
        jsonResult.setMsg(SpringContextUtil.diff("newpwd_no_null"));
      } else if (!accountPassWord.equals(reaccountPassWord)) {
        jsonResult.setSuccess(false);
        jsonResult.setMsg(SpringContextUtil.diff("twopwd_is_diff"));
      } else {
        // 设置短信验证码到session中
        SmsParam smsParam = new SmsParam();
        smsParam.setHryMobilephone(user.getMobile());
        smsParam.setHrySmstype(SmsSendUtil.RESET_CHANGE_PW);
        request.getSession()
            .setAttribute("accountpwSmsCode", SmsSendUtil.sendSmsCode(smsParam, null, null));
        jsonResult.setSuccess(true);
        jsonResult.setMsg(SpringContextUtil.diff("sms_success"));
      }
    } else {
      jsonResult.setSuccess(false);
    }

    return jsonResult;
  }

  /**
   * 用户找回密码发送验证码
   */
  @RequestMapping(value = "/forgetSmsCode", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult forgetSmsCode(HttpServletRequest request) {
    String language = (String) request.getAttribute("language");
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }
    JsonResult jsonResult = new JsonResult();
    String phoneNum = request.getParameter("phoneNum"); // 手机号
    // String forgetCode = request.getParameter("forgetCode");//获得客户传来的验证码
    // 获得图形验证码
    // String sessionRegistCode = (String)
    // request.getSession().getAttribute("registCode");
    if (StringUtils.isEmpty(phoneNum)) {// 手机号判空
      jsonResult.setSuccess(false);
      jsonResult.setMsg(SpringContextUtil.diff("tel_is_not_null"));
    } /*
     * else if(StringUtils.isEmpty(forgetCode)){//验证码判空
     * jsonResult.setSuccess(false);
     * jsonResult.setMsg(MessageUtil.getText(MessageConstant.REQUIRED_registCode));
     * }else if(StringUtils.isEmpty(forgetCode)||!forgetCode.equalsIgnoreCase(
     * sessionRegistCode)){//验证码判正确性
     * jsonResult.setMsg(MessageUtil.getText(MessageConstant.INCORRECT_CODE));
     * jsonResult.setSuccess(false); return jsonResult; }
     */ else {
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult remoteResult = remoteManageService.selectPhone(phoneNum);
      if (remoteResult.getSuccess()) {
        // 设置短信验证码到session中
        SmsParam smsParam = new SmsParam();
        smsParam.setHryMobilephone(phoneNum);
        smsParam.setHrySmstype(SmsSendUtil.FIND_LOGIN_PW);
        request.getSession()
            .setAttribute("smsfindloginpw" + phoneNum,
                SmsSendUtil.sendSmsCode(smsParam, null, null));
        jsonResult.setSuccess(true);
        jsonResult.setMsg(SpringContextUtil.diff("sms_success"));
      } else {
        jsonResult.setMsg(SpringContextUtil.diff("telcode_is_null"));
        jsonResult.setSuccess(false);
      }
    }
    return jsonResult;
  }

  /**
   * 用户注册发送验证码
   */
  @RequestMapping(value = "/registSmsCode", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult registSmsCode(HttpServletRequest request) {
    String language = (String) request.getAttribute("language");
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }
    JsonResult jsonResult = new JsonResult();
    String userName = request.getParameter("username"); // 手机号
    String country = request.getParameter("country");// 国家

    RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
    RemoteResult remoteResult = manageService.checknoPhone(userName, country);

    if (remoteResult.getSuccess() == false) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("本手机号已经存在！");
      return jsonResult;
    }

    String registCode = request.getParameter("registCode");// 获得客户传来的验证码
    // 获得验证码
    String sessionRegistCode = (String) request.getSession().getAttribute("registCode");
    if (StringUtils.isEmpty(userName)) {// 手机号判空
      jsonResult.setSuccess(false);
      jsonResult.setMsg(SpringContextUtil.diff("tel_is_not_null"));
//注释begin  modify  by zongwei 20180424
//		} else if (StringUtils.isEmpty(registCode)) {// 验证码判空
//			jsonResult.setSuccess(false);
//			jsonResult.setMsg(SpringContextUtil.diff("registCode_can_not_null"));
//		} else if (StringUtils.isEmpty(registCode) || !registCode.equalsIgnoreCase(sessionRegistCode)) {// 验证码判正确性
//			jsonResult.setMsg(SpringContextUtil.diff("registCode_error"));
//			jsonResult.setSuccess(false); 
// 注释end  modify  by zongwei 20180424			
    } else {
      // 设置短信验证码到session中
      SmsParam smsParam = new SmsParam();
      String sendSmsCodeHai;
      if (country.equals("+86")) {
        smsParam.setHrySmstype(SmsSendUtil.TAKE_PHONE);
        sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, country.split("/+")[0], userName);
      } else {
        smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
        smsParam.setHryMobilephone(country + " " + userName);
        sendSmsCodeHai = SmsSendUtil
            .sendSmsCode(smsParam, country.split("/+")[0], country + userName);
      }
      //String sendSmsCode = SmsSendUtil.sendSmsCode(smsParam);
      log.info("注册验证码：【" + sendSmsCodeHai + "】");
      request.getSession().setAttribute("registSmsCode" + userName, sendSmsCodeHai);
      jsonResult.setSuccess(true);
      jsonResult.setMsg(SpringContextUtil.diff("sms_success"));
    }
    return jsonResult;
  }

  /**
   * 注册验证码
   */
  @RequestMapping(value = "/registcode")
  public void registcode(HttpServletResponse response, HttpServletRequest request) {

    DrawPictureUtil drawPictureUtil = new DrawPictureUtil("registCode", 100, 30);
    drawPictureUtil.darw(request, response);
  }

  /**
   * 获得提现短信验证码
   */
  @RequestMapping(value = "/getRmbWithdrawCode", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getRmbWithdrawCode(HttpServletRequest request) {
    String language = (String) request.getAttribute("language");
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }
    JsonResult jsonResult = new JsonResult();
    User user = SessionUtils.getUser(request);
    if (user != null) {
      String transactionMoney = request.getParameter("transactionMoney");
      String accountPassWord = request.getParameter("accountPassWord");
      /*
       * if (StringUtils.isEmpty(transactionMoney)) { jsonResult.setSuccess(false);
       * jsonResult.setMsg(SpringContextUtil.diff("withdrawal_no_null")); } else if
       * (StringUtils.isEmpty(accountPassWord)) { jsonResult.setSuccess(false);
       * jsonResult.setMsg(SpringContextUtil.diff("jypwd_no_null")); } else {
       */
      // 设置短信验证码到session中
      SmsParam smsParam = new SmsParam();
      smsParam.setHryMobilephone(user.getMobile());
      smsParam.setHrySmstype(SmsSendUtil.TAKE_MONEY);
      request.getSession()
          .setAttribute("accountpwSmsCode", SmsSendUtil.sendSmsCode(smsParam, null, null));
      jsonResult.setSuccess(true);
      jsonResult.setMsg(SpringContextUtil.diff("sms_success"));
      // }
    } else {
      jsonResult.setSuccess(false);
    }

    return jsonResult;
  }

  /*
   */

  /**
   * 获得提币短信验证码
   */
  @RequestMapping(value = "/getWithdrawCoinCode", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getWithdrawCoinCode(HttpServletRequest request) {
    String language = (String) request.getAttribute("language");
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }
    JsonResult jsonResult = new JsonResult();
    User user = SessionUtils.getUser(request);
    if (user != null) {
      String inputNumWit = request.getParameter("inputNumWit");
      // String accountPassWord = request.getParameter("accountPassWord");
      String withdrawCode = request.getParameter("withdrawCode");
      String accountpwSmsCode = (String) request.getSession().getAttribute("accounCoinSmsCode");
      /*
       * if (StringUtils.isEmpty(inputNumWit)) { jsonResult.setSuccess(false);
       * jsonResult.setMsg(SpringContextUtil.diff("tb_no_null")); }
       */
      /*
       * else if (StringUtils.isEmpty(accountPassWord)) {
       * jsonResult.setSuccess(false);
       * jsonResult.setMsg(SpringContextUtil.diff("jypwd_no_null")); }
       */
      // else {
      SmsParam smsParam = new SmsParam();
      smsParam.setHryMobilephone(user.getMobile());
      smsParam.setHrySmstype(SmsSendUtil.TAKE_COIN);
      request.getSession()
          .setAttribute("accounCoinSmsCode", SmsSendUtil.sendSmsCode(smsParam, null, null));
      jsonResult.setSuccess(true);
      jsonResult.setMsg(SpringContextUtil.diff("sms_success"));
      // }
    } else {
      jsonResult.setSuccess(false);
    }

    return jsonResult;
  }

  private JsonResult verificationOrder(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String ip = IpUtil.getIp(request);
    RedisService redisService = SpringContextUtil.getBean("redisService");
    // Integer telTime =
    // Integer.valueOf(PropertiesUtils.APP.getProperty("telTime"));
    // Integer telCount =
    // Integer.valueOf(PropertiesUtils.APP.getProperty("telCount"));
    Integer telTime = 5;
    Integer telCount = 3;
    String telValue = redisService.get(tel + ip);
    if (telValue == null || "".equals(telValue)) {
      redisService.save(tel + ip, "1", telTime);
    } else {
      Integer num = Integer.valueOf(telValue);
      if (num >= telCount) {
        jsonResult.setCode("0000");
        jsonResult.setMsg(SpringContextUtil.diff("sms_tooMuch"));
        jsonResult.setSuccess(false);
        return jsonResult;
      }
      num++;
      redisService.save(tel + ip, String.valueOf(num), telTime);
    }

    return jsonResult.setSuccess(true);

  }

  /**
   * 获得手机认证短信
   */
  @RequestMapping(value = "/getPhone", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getPhone(HttpServletRequest request) {

    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }

    String language = null;
    String mobile = null;
    String mobi = null;
    ;
    // 手机号
    String phone = null;
    // 地区截取
    String address = null;
    String addressqu = null;
    User user = null;
    JsonResult jsonResult = new JsonResult();

    try {
      language = (String) request.getAttribute("language");
      mobile = request.getParameter("mobile");
      mobi = mobile.replace(" ", "");
      ;
      // 手机号
      phone = mobile.substring(mobile.lastIndexOf(" ") + 1);
      // 地区截取
      address = mobile.substring(0, mobile.indexOf(" "));
      addressqu = address.substring(address.lastIndexOf("+") + 1);
      user = SessionUtils.getUser(request);
    } catch (Exception e) {
      e.printStackTrace();
      jsonResult.setSuccess(false);
      jsonResult.setMsg("200");
      return jsonResult;
    }
    if (user != null) {
      RedisService redisService = SpringContextUtil.getBean("redisService");
      // 设置短信验证码到session中
      SmsParam smsParam = new SmsParam();
      smsParam.setHryMobilephone(mobile);
      smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
      if (addressqu.equals("86")) {
        smsParam.setHrySmstype(SmsSendUtil.TAKE_PHONE);
        String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, addressqu, phone);

        redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);
      } else {
        smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
        String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, addressqu, mobi);
        redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);

      }
      jsonResult.setSuccess(true);
      jsonResult.setMsg(SpringContextUtil.diff("sms_success"));

    } else {
      jsonResult.setSuccess(false);
    }

    return jsonResult;
  }

  /**
   * 取消手机认证获得手机认证短信
   */
  @RequestMapping(value = "/getPwdPhone", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getPwdPhone(HttpServletRequest request) {

    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }

    String language = null;
    String mobile = null;
    String mobi = null;
    ;
    // 手机号
    String phone = null;
    // 地区截取
    String address = null;
    String addressqu = null;
    User user = null;
    JsonResult jsonResult = new JsonResult();

    try {
      language = (String) request.getAttribute("language");
      //mobile = request.getParameter("mobile");

      user = SessionUtils.getUser(request);
      mobile = user.getPhone();

      mobi = mobile.replace(" ", "");

      // 手机号
      phone = mobile.substring(mobile.lastIndexOf(" ") + 1);
      // 地区截取
      address = mobile.substring(0, mobile.indexOf(" "));
      addressqu = address.substring(address.lastIndexOf("+") + 1);

    } catch (Exception e) {
      e.printStackTrace();
      jsonResult.setSuccess(false);
      jsonResult.setMsg("200");
      return jsonResult;
    }
    if (user != null) {
      RedisService redisService = SpringContextUtil.getBean("redisService");
      // 设置短信验证码到session中
      SmsParam smsParam = new SmsParam();
      smsParam.setHryMobilephone(mobile);
      smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
      if (addressqu.equals("86")) {
        smsParam.setHrySmstype(SmsSendUtil.TAKE_PHONE);
        String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, addressqu, phone);

        redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);
      } else {
        smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
        String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, addressqu, mobi);
        redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);

      }
      jsonResult.setSuccess(true);
      jsonResult.setMsg(SpringContextUtil.diff("sms_success"));

    } else {
      jsonResult.setSuccess(false);
    }

    return jsonResult;
  }

  /**
   * 忘记秘密获取验证码
   */
  @RequestMapping(value = "/sendForgetSmsCode", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult sendForgetSmsCode(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String username = "";
    username = request.getParameter("phoneNum");
    String type = request.getParameter("type");
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }

    String mobile = "";
    String addressqu = "";
    if (username != null) {
      String language = (String) request.getAttribute("language");
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      User user = remoteManageService.selectByTel(username);
      if (user.getUsername() == null) {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("账户不存在，请注册！");
        return jsonResult;
      }
      mobile = user.getPhone();
      addressqu = user.getCountry();
    } else {
      User user = SessionUtils.getUser(request);
      mobile = user.getPhone();
      username = user.getUsername();
      addressqu = user.getCountry();
    }

    if (StringUtils.isEmpty(mobile)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg(SpringContextUtil.diff("withdrawal_no_null"));
    }
    String mobi = mobile.replace(" ", "");
    // 手机号
    String phone = mobile.substring(mobile.lastIndexOf(" ") + 1);

    // 地区截取
    String address = mobile.substring(0, mobile.indexOf(" "));
    // addressqu = addressqu.substring(addressqu.lastIndexOf("+") + 1);

    // 设置短信验证码到session中
    SmsParam smsParam = new SmsParam();
    smsParam.setHryMobilephone(mobile);

    if (address.equals("+86")) {
      smsParam.setHrySmstype(SmsSendUtil.TAKE_PHONE);
      String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, address, phone);
      request.getSession().setAttribute("smsfindloginpw" + username, sendSmsCodeHai);
    } else {
      smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
      String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, address, mobi);
      request.getSession().setAttribute("smsfindloginpw" + username, sendSmsCodeHai);

    }
    jsonResult.setSuccess(true);
    jsonResult.setMsg(SpringContextUtil.diff("sms_success"));

    return jsonResult;
  }


  /**
   * 获得手机二次认证短信
   */
  @RequestMapping(value = "/smsPhone", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult smsPhone(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String username = "";
    username = request.getParameter("username");
    String type = request.getParameter("type");
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }

    String mobile = "";
    String addressqu = "";
    if (username != null) {
      String language = (String) request.getAttribute("language");
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      User user = remoteManageService.selectByTel(username);
      mobile = user.getPhone();
      addressqu = user.getCountry();
    } else {
      User user = SessionUtils.getUser(request);
      mobile = user.getPhone();
      username = user.getUsername();
      addressqu = user.getCountry();
    }

    if (StringUtils.isEmpty(mobile)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("手机号码为空！");
      return jsonResult;
    }
    String mobi = mobile.replace(" ", "");
    // 手机号
    String phone = mobile.substring(mobile.lastIndexOf(" ") + 1);

    // 地区截取
    String address = mobile.substring(0, mobile.indexOf(" "));
    //addressqu = addressqu.substring(addressqu.lastIndexOf("+") + 1);

    // 设置短信验证码到session中
    SmsParam smsParam = new SmsParam();
    smsParam.setHryMobilephone(mobile);

    if (address.equals("+86")) {
      smsParam.setHrySmstype(SmsSendUtil.TAKE_PHONE);
      String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, address, phone);
      RedisService redisService = SpringContextUtil.getBean("redisService");
      if (null != type && "change".equals(type)) {
        redisService.save("genghuan" + username, sendSmsCodeHai, 120);
      }

      redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);
      // request.getSession().setAttribute("accountpwSmsCode",
      // SmsSendUtil.sendSmsCodeHai(smsParam, addressqu,phone));
    } else {
      smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
      String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, address, mobi);
      RedisService redisService = SpringContextUtil.getBean("redisService");
      redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);
      // request.getSession().setAttribute("accountpwSmsCode",
      // SmsSendUtil.sendSmsCodeHai(smsParam, addressqu,mobi));

    }
    jsonResult.setSuccess(true);
    jsonResult.setMsg(SpringContextUtil.diff("sms_success"));

    return jsonResult;
  }

  /**
   * 获得修改银行卡手机认证
   */
  @RequestMapping(value = "/updateBankSmsPhone", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult updateBankSmsPhone(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    String mobile = "";
    String addressqu = "";
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        mobile = user.getPhone();
        addressqu = user.getCountry();
      } else {
        user = SessionUtils.getUser(request);
        mobile = user.getPhone();
        addressqu = user.getCountry();
      }
    } else {
      User user = SessionUtils.getUser(request);
      mobile = user.getPhone();
      addressqu = user.getCountry();
    }

    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }

    if (StringUtils.isEmpty(mobile)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("手机号码为空！");
      return jsonResult;
    }
    String mobi = mobile.replace(" ", "");
    // 手机号
    String phone = mobile.substring(mobile.lastIndexOf(" ") + 1);

    // 地区截取
    String address = mobile.substring(0, mobile.indexOf(" "));
    //addressqu = addressqu.substring(addressqu.lastIndexOf("+") + 1);

    // 设置短信验证码到session中
    SmsParam smsParam = new SmsParam();
    smsParam.setHryMobilephone(mobile);

    if (address.equals("+86")) {
      smsParam.setHrySmstype(SmsSendUtil.TAKE_PHONE);
      String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, address, phone);
      //RedisService redisService1 = SpringContextUtil.getBean("redisService");
      redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);
      // request.getSession().setAttribute("accountpwSmsCode",
      // SmsSendUtil.sendSmsCodeHai(smsParam, addressqu,phone));
    } else {
      smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
      String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, address, mobi);
      //RedisService redisService1 = SpringContextUtil.getBean("redisService");
      redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);
      // request.getSession().setAttribute("accountpwSmsCode",
      // SmsSendUtil.sendSmsCodeHai(smsParam, addressqu,mobi));

    }
    jsonResult.setSuccess(true);
    jsonResult.setMsg(SpringContextUtil.diff("sms_success"));

    return jsonResult;
  }

}
