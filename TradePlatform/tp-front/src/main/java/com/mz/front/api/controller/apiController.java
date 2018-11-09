package com.mz.front.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.front.annotation.ThirdInterFaceLog;
import com.mz.front.index.controller.EmailRunnable;
import com.mz.front.kline.model.MarketTrades;
import com.mz.front.kline.model.MarketTradesSub;
import com.mz.manage.remote.RemoteApiService;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.ApiExApiApply;
import com.mz.manage.remote.model.Coin;
import com.mz.manage.remote.model.Coin2;
import com.mz.manage.remote.model.ExDigitalmoneyAccountManage;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.util.DrawPictureUtil;
import com.mz.util.IpUtil;
import com.mz.core.thread.ThreadPool;
import com.mz.util.sys.SpringContextUtil;;
import io.swagger.annotations.ApiOperation;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
@Import(com.mz.redis.common.utils.impl.RedisServiceImpl.class)
public class apiController {

  private final static Logger log = Logger.getLogger(apiController.class);

  private String tel = "tel:";

  @Autowired
  RemoteApiService remoteApiService;

  @Resource
  RemoteAppTransactionManageService remoteAppTransactionManageService;

  @Autowired
  private RedisService redisService;

  /**
   * 注册类型属性编辑器
   */
  @InitBinder
  public void initBinder(ServletRequestDataBinder binder) {

    // 系统注入的只能是基本类型，如int，char，String

    /**
     * 自动转换日期类型的字段格式
     */
    binder.registerCustomEditor(Date.class, new DateTimePropertyEditorSupport());

    /**
     * 防止XSS攻击，并且带去左右空格功能
     */
    binder.registerCustomEditor(String.class, new StringPropertyEditorSupport(true, false));
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

  /*
   * zongwei
   * 第三方接口发短信认证
   * @Date 20180517
   * */
  @RequestMapping(value = "/sendSms", method = RequestMethod.POST)
  @ResponseBody
  @ThirdInterFaceLog
  public JsonResult sendSms(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    RedisService redisService = SpringContextUtil.getBean("redisService");
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }
    String username = "";
    String mobile = "";
    username = request.getParameter("username");
    String accesskey = request.getParameter("accesskey");//平台key

    ApiExApiApply apiExApiApply = remoteApiService.getExApiApply(accesskey);
    if (apiExApiApply == null) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("平台没有对接！");
      return jsonResult;
    }

    //
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    //获取用户信息
    User user = remoteManageService.selectByTel(username);
    if (user == null || user.getUsername() == null) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("账户不存在，请注册！");
      return jsonResult;
    }
    if (!("1").equals(user.getPhoneState().toString())) {
      jsonResult.setMsg("请到平台绑定手机，再绑定账号！");
      jsonResult.setSuccess(false);
      return jsonResult;
    }
    //获取手机号码
    mobile = user.getPhone();
    //手机号码去空格
    String mobi = mobile.replace(" ", "");
    // 手机号
    String phone = mobile.substring(mobile.lastIndexOf(" ") + 1);
    //获取地址
    String address = mobile.substring(0, mobile.indexOf(" "));

    SmsParam smsParam = new SmsParam();
    smsParam.setHryMobilephone(mobile);

    if (address.equals("+86")) {
      smsParam.setHrySmstype(SmsSendUtil.TAKE_PHONE);
      String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, address, phone);
      redisService.save("SMS:smsphone:" + username, sendSmsCodeHai, 120);
    } else {
      smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
      String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, address, mobi);
      redisService.save("SMS:smsphone:" + username, sendSmsCodeHai, 120);

    }
    jsonResult.setSuccess(true);
    jsonResult.setMsg(SpringContextUtil.diff("sms_success"));

    return jsonResult;
  }


  /*
   * zongwei
   * 第三方接口绑定账号
   * @Date 20180517
   * */
  @RequestMapping(value = "/binduser", method = RequestMethod.POST)
  @ResponseBody
  @ThirdInterFaceLog
  public JsonResult binduser(HttpServletRequest request) throws Exception {
    JsonResult jsonResult = new JsonResult();
    RedisService redisService = SpringContextUtil.getBean("redisService");
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }
    String username = request.getParameter("username");
    String thirdUserName = request.getParameter("thirdUserName");
    String verificationCode = request.getParameter("verificationCode");
    String accesskey = request.getParameter("accesskey");//平台key

    ApiExApiApply apiExApiApply = remoteApiService.getExApiApply(accesskey);
    if (apiExApiApply == null) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("平台没有对接！");
      return jsonResult;
    }

    if (verificationCode == null) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("手机验证码不能为空！");
      return jsonResult;
    }

    String verificationCodeT = redisService.get("SMS:smsphone:" + username);

    if (!verificationCode.equals(verificationCodeT)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("手机验证码错误！");
      return jsonResult;
    }
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    //获取用户信息
    User user = remoteManageService.selectByTel(username);
    if (user == null || user.getPhone() == null) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("账户不存在，请注册！");
      return jsonResult;
    }
    if (("1").equals(user.getPhoneState())) {
      jsonResult.setMsg("请到平台绑定手机，再绑定账号！");
      jsonResult.setSuccess(false);
      return jsonResult;
    }
    user.setPlatform(apiExApiApply.getDescription());
    user.setThirdUserName(thirdUserName);
    String code = RandomStringUtils.random(16, true, false);
    user.setThirdUserPw(code);
    RemoteResult remoteResult = remoteManageService.updateThird(user);
    //AppCustomer appCustomer = (AppCustomer) remoteResult.getObj();
    if (!remoteResult.getSuccess()) {
      return jsonResult.setSuccess(false).setMsg(remoteResult.getMsg());
    }
    String key1 = "hello";
    String key2 = "hello";
    String key3 = "moto";
    Des4JS desObj = new Des4JS();
//        String encrypt =  encryption.aesEncrypt(code,encryptkey);
//        String encrypt1 =  encryption.aesDecrypt(encrypt,encryptkey);
    String str = desObj.strEnc(code, key1, key2, key3);
    JSONObject result = new JSONObject();
    result.put("username", user.getUsername());
    result.put("UserPw", str);
    return jsonResult.setSuccess(true).setMsg("绑定成功！").setObj(result);
  }


  /*
   * zongwei
   * 获取用户信息及钱包信息
   * @Date 20180517
   * */
  @RequestMapping(value = "/getCoinInfo", method = RequestMethod.POST)
  @ResponseBody
  @ThirdInterFaceLog
  public JsonResult getCoinInfo(HttpServletRequest request) throws Exception {
    JsonResult jsonResult = new JsonResult();
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }
    String username = request.getParameter("username");
    String thirdUserPw = request.getParameter("UserPw");
    String coinCode = request.getParameter("coinCode");
    String accesskey = request.getParameter("accesskey");//平台key

    ApiExApiApply apiExApiApply = remoteApiService.getExApiApply(accesskey);
    if (apiExApiApply == null) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("平台没有对接！");
      return jsonResult;
    }
    if (thirdUserPw == null) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("用户密码不能为空！");
      return jsonResult;
    }

    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    //获取用户信息
    User user = remoteManageService.selectByTel(username);

    if (user == null || user.getPhone() == null) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("账户不存在，请注册！");
      return jsonResult;
    }
    if (!thirdUserPw.equals(user.getThirdUserPw())) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("用户密码错误！");
      return jsonResult;
    }

    List<ExDigitalmoneyAccountManage> list = new ArrayList<ExDigitalmoneyAccountManage>();
    if (coinCode == null) {
      list = remoteAppTransactionManageService.listexd(user.getCustomerId(), "zh_CN");
    } else {
      list = remoteAppTransactionManageService.listexdbycoinCode(user.getCustomerId(), coinCode);
    }
    List<ExDigitalmoneyAccountManage> lists = new ArrayList<ExDigitalmoneyAccountManage>();

    for (int i = 0; i < list.size(); i++) {
      //String encryptkey = "hello,moto";
      String key1 = "hello";
      String key2 = "hello";
      String key3 = "moto";
      Des4JS desObj = new Des4JS();
      //String encrypt =  encryption.aesEncrypt(list.get(i).getPublicKey(),encryptkey);
      String str = desObj.strEnc(list.get(i).getPublicKey(), key1, key2, key3);
      ExDigitalmoneyAccountManage exDigitalmoneyAccountManage = new ExDigitalmoneyAccountManage();
      exDigitalmoneyAccountManage.setPublicKey(str);
      exDigitalmoneyAccountManage.setCoinCode(list.get(i).getCoinCode());
      lists.add(exDigitalmoneyAccountManage);
    }
    jsonResult.setObj(lists);
    jsonResult.setMsg("成功！");
    jsonResult.setSuccess(true);
    return jsonResult;
  }

  @RequestMapping(value = "/mailreg", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "注册", httpMethod = "POST", response = JsonResult.class, notes =
      "用户名username,"
          + "密码password,图形验证码registCode,邀请码referralCode,邀请码可为空,语言language")
  @ResponseBody
  @ThirdInterFaceLog
  public JsonResult appreg(HttpServletRequest request, HttpServletResponse response,
      @RequestParam String username,
      @RequestParam String password, @RequestParam String registCode
  ) {
    String referralCode = null;

    String accesskey = request.getParameter("accesskey");//平台key

    ApiExApiApply apiExApiApply = remoteApiService.getExApiApply(accesskey);
    if (apiExApiApply == null) {
      JsonResult jsonResult = new JsonResult();
      jsonResult.setSuccess(false);
      jsonResult.setMsg("平台没有对接！");
      return jsonResult;
    }

    Locale locale = new Locale("zh_CN");

    if (StringUtils.isEmpty(username)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("emailisnull", "zh_CN"));
    }
    if (StringUtils.isEmpty(password)) {
      return new JsonResult().setMsg("密码不能为空");
    }

    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("regCode");

    if (!registCode.equalsIgnoreCase(value)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tx_error", "zh_CN"));
    }

    if (!"".equals(referralCode) && referralCode != null) {
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult selectPhone = remoteManageService.selectAgent(referralCode);
      if (!selectPhone.getSuccess()) {
        return new JsonResult().setSuccess(false)
            .setMsg(SpringContextUtil.diff("dailisbucunzai", "zh_CN"));
      }
    }
    try {
      RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");

      //后台配置的法币类型
      String language_code = null;
      String config = redisService.get("configCache:all");
      if (!StringUtils.isEmpty(config)) {
        JSONObject parseObject = JSONObject.parseObject(config);
        language_code = parseObject.get("language_code").toString();
      }

      RemoteResult regist = manageService.regist(username, password, referralCode, language_code);
      if (regist != null) {
        if (regist.getSuccess()) {
          String url =
              PropertiesUtils.APP.getProperty("app.url") + "/activation/" + username + "/" + regist
                  .getObj();
          // 发送邮件
          StringBuffer sb = new StringBuffer();
          sb.append(SpringContextUtil.diff("dear") + " " + username + "<br><br>" + SpringContextUtil
              .diff("Resetfirstemail") + "<br><br>" + SpringContextUtil.diff("Resettwoemail")
              + "<br><br>");
          sb.append(PropertiesUtils.APP.getProperty("app.url"));
          sb.append(
              "/resetPassword/" + "/" + regist.getObj() + "/" + locale.toString() + "<br><br>");

          String type = "3";
          ThreadPool.exe(new EmailRunnable(username, sb.toString(), type, locale));
          return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("reg_success", "zh_CN"));
        } else {
          return new JsonResult().setMsg(SpringContextUtil.diff(regist.getMsg(), "zh_CN"));
        }
      }

    } catch (Exception e) {
      log.error("注册方法远程调用出错");
    }
    return new JsonResult();
  }

  /**
   * 注册验证码
   */
  @RequestMapping(value = "/registcode")
  @ThirdInterFaceLog
  public void registcode(HttpServletResponse response, HttpServletRequest request) {
    String accesskey = request.getParameter("accesskey");//平台key
    ApiExApiApply apiExApiApply = remoteApiService.getExApiApply(accesskey);
    if (apiExApiApply == null) {
    } else {
      DrawPictureUtil drawPictureUtil = new DrawPictureUtil("registCode", 100, 30);
      drawPictureUtil.darw(request, response);
    }
  }

  /**
   * 注册获得短信认证码
   */
  @RequestMapping(value = "/smsPhone", method = RequestMethod.POST)
  @ResponseBody
  @ThirdInterFaceLog
  public JsonResult smsPhone(HttpServletRequest request) {
    String language = (String) request.getAttribute("language");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }
    JsonResult jsonResult = new JsonResult();
    String userName = request.getParameter("username"); // 手机号
    String country = request.getParameter("country");// 国家地区

    String accesskey = request.getParameter("accesskey");//平台key

    ApiExApiApply apiExApiApply = remoteApiService.getExApiApply(accesskey);
    if (apiExApiApply == null) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("平台没有对接！");
      return jsonResult;
    }

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

    if (StringUtils.isEmpty(registCode)) {// 验证码判空
      jsonResult.setSuccess(false);
      jsonResult.setMsg("图形验证码不能为空");
      return jsonResult;
    } else if (StringUtils.isEmpty(registCode) || !registCode
        .equalsIgnoreCase(sessionRegistCode)) {// 验证码判正确性
      jsonResult.setMsg("图形验证码不正确");
      jsonResult.setSuccess(false);
      return jsonResult;
    }
    if (StringUtils.isEmpty(userName)) {// 手机号判空
      jsonResult.setSuccess(false);
      jsonResult.setMsg(SpringContextUtil.diff("tel_is_not_null"));
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
      redisService.save("SMS:reg:smsphone:" + userName, sendSmsCodeHai, 120);
      jsonResult.setSuccess(true);
      jsonResult.setMsg(SpringContextUtil.diff("sms_success"));
    }
    return jsonResult;
  }

  /**
   * 手机注册ajax方法
   */
  @RequestMapping(value = "phonereg", method = RequestMethod.POST)
  @ResponseBody
  @ThirdInterFaceLog
  public JsonResult registService2(HttpServletRequest request, HttpServletResponse response) {
    String language = (String) request.getAttribute("language");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    Locale locale = LocaleContextHolder.getLocale();

    // 手机号
    String mobile = request.getParameter("username");
    // 国家
    String country = request.getParameter("country");
    // 密码
    String password = request.getParameter("password");
    // 图形验证码
    String registCode = request.getParameter("registCode");
    // 手机验证码
    String registSmsCode = request.getParameter("registSmsCode");
    // 邀请码
    // String referralCode = request.getParameter("referralCode");
    String referralCode = null;

    String accesskey = request.getParameter("accesskey");//平台key
    ApiExApiApply apiExApiApply = remoteApiService.getExApiApply(accesskey);
    if (apiExApiApply == null) {
      JsonResult jsonResult = new JsonResult();

      jsonResult.setSuccess(false);
      jsonResult.setMsg("平台没有对接！");
      return jsonResult;
    }

    if (StringUtils.isEmpty(mobile)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("telphone_is_not_null"));
    } else {// 去空格
      mobile = mobile.trim().replace(" ", "");
    }
    if (StringUtils.isEmpty(password)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
    }
    if (StringUtils.isEmpty(registCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tx_is_not_null"));
    }
    // session图形验证码
    String session_registcode = (String) request.getSession().getAttribute("registCode");
    if (!registCode.equalsIgnoreCase(session_registcode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tx_error"));
    }
    // session手机验证码
    String session_registSmsCode = redisService.get("SMS:reg:smsphone:" + mobile);
    if (!registSmsCode.equalsIgnoreCase(session_registSmsCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
    }

    if (!"".equals(referralCode) && referralCode != null) {
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult selectPhone = remoteManageService.selectAgent(referralCode);
      if (!selectPhone.getSuccess()) {
        return new JsonResult().setMsg(SpringContextUtil.diff("dailisbucunzai"));
      }
    }
    try {
      String config = redisService.get("configCache:all");
      JSONObject parseObject = JSONObject.parseObject(config);
      RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult regist = manageService.registMobile(mobile, password, referralCode, country,
          parseObject.get("language_code").toString());

      if (regist != null) {
        if (regist.getSuccess()) {
          return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("reg_success"));
        } else {
          return new JsonResult().setMsg(SpringContextUtil.diff(regist.getMsg()));
        }
      }
      return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("reg_success"));
    } catch (Exception e) {
      log.error("注册方法远程调用出错");
    }

    return new JsonResult();

  }


  @RequestMapping(value = "/newest/info", method = RequestMethod.POST)
  @ApiOperation(value = "首页成交记录v2", httpMethod = "POST", response = JsonResult.class, notes = "首页成交记录v1")
  @ResponseBody
  @ThirdInterFaceLog
  public JsonResult newest(HttpServletRequest request) {

    String accesskey = request.getParameter("accesskey");//平台key
    ApiExApiApply apiExApiApply = remoteApiService.getExApiApply(accesskey);
    if (apiExApiApply == null) {
      JsonResult jsonResult = new JsonResult();
      jsonResult.setSuccess(false);
      jsonResult.setMsg("平台没有对接！");
      return jsonResult;
    }

    /*获得usdt对人民币价格*/
    BigDecimal usdttormb = new BigDecimal(1);
    String financeConfig = redisService.get("configCache:financeConfig");
    if (!StringUtils.isEmpty(financeConfig)) {
      JSONArray parseArray = JSON.parseArray(financeConfig);

      if (parseArray != null) {
        for (int i = 0; i < parseArray.size(); i++) {
          JSONObject jsonObject = parseArray.getJSONObject(i);
          if ("usdttormb".equals(jsonObject.getString("configkey"))) {
            String value = jsonObject.getString("value");
            if (!StringUtils.isEmpty(value)) {
              usdttormb = new BigDecimal(value);
            }
          }
        }
      }

    }

    JSONArray jsonArray = new JSONArray();

    // 去redis查询产品数量
    String productListStr = redisService.get("cn:coinInfoList");
    if (!StringUtils.isEmpty(productListStr)) {
      List<Coin> productList = JSON.parseArray(productListStr, Coin.class);

      //统计交易区数量
      Set<String> coinarea = new LinkedHashSet<String>();
      //查询用户自选区
      User user = (User) request.getSession().getAttribute("user");

      //增加交易区
      for (Coin coin : productList) {

        if (("FWF").equals(coin.getCoinCode())) {
          coinarea.add(coin.getFixPriceCoinCode());
        }
      }
      if (coinarea.size() > 0) {
        Iterator<String> it = coinarea.iterator();
        while (it.hasNext()) {
          String areaname = it.next();

          JSONObject obj = new JSONObject();
          obj.put("areaname", areaname);
          obj.put("areanameview", areaname + "  " + SpringContextUtil.diff("jiaoyiqu"));

          //创建list
          ArrayList<JSONObject> list = new ArrayList<JSONObject>();
          //遍历
          for (Coin coin : productList) {
            if (areaname.equals(coin.getFixPriceCoinCode())) {
              JSONObject data = createData(coin);
              data.put("usdttormb", usdttormb);
              list.add(data);
            }
          }
          obj.put("data", list);
          //加入map中
          jsonArray.add(obj);
        }
      }


    }
    JsonResult jsonResult = new JsonResult();
    jsonResult.setSuccess(true).setObj(jsonArray);
    return jsonResult;
  }

  private JSONObject createData(Coin coin) {

    //交易币的位数
    Integer keepCoin = coin.getKeepDecimalForCoin();
    //定价币的位数
    Integer keepCurrency = coin.getKeepDecimalForCurrency();
    int zeroLength = 2;
    //交易币的格式化
    String keepCoinFormat = "0.00";
    //定价币的格式化
    String keepCurrencyFormat = "0.00";
    if (keepCoin > zeroLength) {
      keepCoinFormat = "0.";
      for (int i = 1; i <= keepCoin; i++) {
        keepCoinFormat = keepCoinFormat += "0";
      }
    }
    if (keepCurrency > zeroLength) {
      keepCurrencyFormat = "0.";
      for (int i = 1; i <= keepCurrency; i++) {
        keepCurrencyFormat = keepCurrencyFormat += "0";
      }
    }
    DecimalFormat decimalFormatCoin = new DecimalFormat(keepCoinFormat);
    DecimalFormat decimalFormatCurrency = new DecimalFormat(keepCurrencyFormat);

    JSONObject data = new JSONObject();
    data.put("coinCode", coin.getCoinCode() + "_" + coin.getFixPriceCoinCode());
    data.put("name", coin.getCoinCode() + "_" + coin.getFixPriceCoinCode());
    data.put("picturePath", coin.getPicturePath());

    String currentExchangPrice_str = redisService
        .get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode() + ":currentExchangPrice");
    if (!StringUtils.isEmpty(currentExchangPrice_str)) {
      data.put("currentExchangPrice",
          decimalFormatCurrency.format(new BigDecimal(currentExchangPrice_str)));
      if ("USDT".equals(coin.getFixPriceCoinCode())) {
        data.put("usdtcount", new BigDecimal(currentExchangPrice_str));
      } else {
        //如果当前币对usdt有价格
        String usdtprice = redisService
            .get(coin.getFixPriceCoinCode() + "_USDT" + ":currentExchangPrice");
        if (!StringUtils.isEmpty(usdtprice)) {
          data.put("usdtcount",
              new BigDecimal(currentExchangPrice_str).multiply(new BigDecimal(usdtprice)));
        } else {
          data.put("usdtcount", 0);
        }
      }
    } else {
      data.put("usdtcount", 0);
      data.put("currentExchangPrice", 0);
    }

    // 昨日收盘价
    String coinStr = redisService.get("cn:coinInfoList2");
    String coinCode = coin.getCoinCode() + "_" + coin.getFixPriceCoinCode();
    BigDecimal yesterdayPrice = new BigDecimal(0);
    if (!StringUtils.isEmpty(coinStr)) {
      List<Coin2> coins = JSON.parseArray(coinStr, Coin2.class);
      for (Coin2 c : coins) {
        if (coinCode.equals(c.getCoinCode() + "_" + c.getFixPriceCoinCode())) {
          if (!StringUtils.isEmpty(c.getYesterdayPrice())) {
            yesterdayPrice = new BigDecimal(c.getYesterdayPrice());
          }
        }
      }
    }
    data.put("yesterdayPrice", decimalFormatCurrency.format(yesterdayPrice));

    String str = redisService
        .get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode() + ":PeriodLastKLineList");
    if (str != null) {
      JSONArray jsonv = JSON.parseArray(str);
      //System.out.println(jsonv.getString(5));
      if (jsonv.getString(5) != null) {
        JSONObject jsonv_ = JSON.parseObject(jsonv.getString(5));
        if ("1day".equals(jsonv_.getString("period"))) {

          BigDecimal currentExchangPrice = new BigDecimal(0);
          //上一笔交易价格
          String orders = redisService.get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode()
              + ":pushNewListRecordMarketDesc");
          if (!StringUtils.isEmpty(orders)) {
            MarketTrades marketTrades = com.alibaba.fastjson.JSON
                .parseObject(orders, MarketTrades.class);
            // 最新价格
            if (marketTrades != null) {
              List<MarketTradesSub> trades = marketTrades.getTrades();
              if (trades != null) {
                if (trades.size() > 1) {
                  MarketTradesSub marketTradesSub0 = trades.get(0);
                  data.put("currentExchangPrice",
                      decimalFormatCurrency.format(marketTradesSub0.getPrice()));
                  currentExchangPrice = marketTradesSub0.getPrice();

                  MarketTradesSub marketTradesSub1 = trades.get(1);
                  data.put("lastExchangPrice",
                      decimalFormatCurrency.format(marketTradesSub1.getPrice()));
                } else {

                  MarketTradesSub marketTradesSub0 = trades.get(0);
                  data.put("currentExchangPrice",
                      decimalFormatCurrency.format(marketTradesSub0.getPrice()));
                  currentExchangPrice = marketTradesSub0.getPrice();

                  data.put("lastExchangPrice",
                      decimalFormatCurrency.format(marketTradesSub0.getPrice()));
                }
              } else {
                data.put("lastExchangPrice", "1");
              }
            } else {
              data.put("lastExchangPrice", "1");
            }
          } else {
            data.put("lastExchangPrice", "1");
          }
          //当日成交总量
          data.put("transactionSum",
              decimalFormatCoin.format(new BigDecimal(jsonv_.getString("amount"))));

          data.put("maxPrice", jsonv_.getString("priceHigh"));
          data.put("minPrice", jsonv_.getString("priceLow"));
          // 开盘价
          data.put("openPrice",
              decimalFormatCurrency.format(new BigDecimal(jsonv_.getString("priceOpen"))));

          if (BigDecimal.ZERO.compareTo(yesterdayPrice) != 0) {
            if (BigDecimal.ZERO.compareTo(currentExchangPrice) != 0) {
              BigDecimal divide = (currentExchangPrice.subtract(yesterdayPrice))
                  .divide(yesterdayPrice, 5, BigDecimal.ROUND_HALF_DOWN)
                  .multiply(new BigDecimal(100));
              data.put("RiseAndFall", divide);
            } else {
              data.put("RiseAndFall", 0);
            }
          } else {
            data.put("RiseAndFall", 0);
          }


        } else {
          data.put("lastExchangPrice", 0);
          data.put("transactionSum", 0);
          data.put("maxPrice", 0);
          data.put("minPrice", 0);
          // 开盘价
          data.put("openPrice", new BigDecimal(0));
          data.put("lastEndPrice", 0);
          data.put("RiseAndFall", 0);
        }
      } else {
        data.put("lastExchangPrice", 0);
        data.put("transactionSum", 0);
        data.put("maxPrice", 0);
        data.put("minPrice", 0);
        // 开盘价
        data.put("openPrice", new BigDecimal(0));

        data.put("lastEndPrice", 0);
        data.put("RiseAndFall", 0);
      }
    } else {
      data.put("lastExchangPrice", 0);
      data.put("transactionSum", 0);
      data.put("maxPrice", 0);
      data.put("minPrice", 0);
      // 开盘价
      data.put("openPrice", new BigDecimal(0));

      data.put("lastEndPrice", 0);
      data.put("RiseAndFall", 0);
    }

    return data;

  }


}
