package com.mz.front.mobile.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.customer.user.model.AppCustomer;
import com.mz.front.index.controller.UserRedisRunnable;
import com.mz.front.listener.SessionListener;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.Coin;
import com.mz.manage.remote.model.CoinAccount;
import com.mz.manage.remote.model.ExDigitalmoneyAccountManage;
import com.mz.manage.remote.model.MyAccountTO;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.UserInfo;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.manage.remote.model.commendCode;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.FileType;
import com.mz.util.FileUpload;
import com.mz.util.GoogleAuthenticatorUtil;
import com.mz.util.SessionUtils;
import com.mz.util.httpRequest.HttpUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.SpringContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Controller
@RequestMapping(value = "/mobile/user/apppersondetail")
@Api(value = "App操作类", description = "实名认证，修改密码，手机认证，谷歌验证，推荐佣金页面信息信息")
public class AppPersonDetailController {

  @Autowired
  UserRedisRunnable userRedisRunnable;

  /**
   * 生成谷歌认证码
   */
  @RequestMapping("/sendgoogle")
  @ApiOperation(value = "生成谷歌认证码（JsonResult + obj）", httpMethod = "POST", response = AppCustomer.class, notes = "")
  @ResponseBody
  public JsonResult sendgoogle(HttpServletRequest request) {
    String secret = GoogleAuthenticatorUtil.generateSecretKey();
    User user = SessionUtils.getUser(request);
    System.out.println("ssss=" + secret);
    String qrBarcodeURL = GoogleAuthenticatorUtil.getQRBarcodeURL("testuser",
        "testhost", secret);
    System.out.println("ffff=" + qrBarcodeURL);
    String company = PropertiesUtils.APP.getProperty("app.googleCompany");
    AppCustomer a = new AppCustomer();
    a.setGoogleKey(secret);
    a.setCompany(company);
    a.setUserName(user.getUsername());
    return new JsonResult().setSuccess(true).setObj(a);

  }


  /**
   *
   * @param codes
   * @param
   * @param request
   * @return
   */

  @RequestMapping("/jcgoogle")
  @ApiOperation(value = "解除谷歌认证", httpMethod = "POST", response = JsonResult.class, notes = "用户密码PassWord，谷歌验证码codes")
  @ResponseBody
  public JsonResult jcgoogle(@RequestParam String codes, HttpServletRequest request) {
    // enter the code shown on device. Edit this and run it fast before the
    // code expires!
    //String GoogleKey = (String) request.getSession().getAttribute("googleKey");
    User user = SessionUtils.getUser(request);
    String GoogleKey = user.getGoogleKey();
    String PassWord = request.getParameter("PassWord");
    if (StringUtils.isEmpty(PassWord)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("loginpwd_no_null"));
    }
    //查看密码
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    RemoteResult result = remoteManageService.setvail(user.getMobile(), PassWord);

    if (!result.getSuccess()) {
      return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("mimacuowu"));

    }
    long code = 0;
    try {
      code = Long.parseLong(codes);
    } catch (NumberFormatException e) {
      return new JsonResult().setSuccess(false)
          .setMsg(SpringContextUtil.diff("gugeyanzhengubuzhengque", "zh_CN"));
    }
    long t = System.currentTimeMillis();
    GoogleAuthenticatorUtil ga = new GoogleAuthenticatorUtil();
    // ga.setWindowSize(0); // should give 5 * 30 seconds of grace...
    boolean r = ga.check_code(GoogleKey, code, t);
    if (!r) {
      return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("gugejiechuerror"));

    } else {
      RemoteResult jcgoogle = remoteManageService.jcgoogle(user.getMobile(), GoogleKey);

      if (jcgoogle.getSuccess()) {
        user.setGoogleState(0);
        user.setPassDate(new Date());
        user.setGoogleKey(GoogleKey);
        SessionUtils.updateRedis(user);
      }

      return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("gugejiechusuccess"));
    }
  }


  @RequestMapping("/PhoneAuth")
  @ApiOperation(value = "手机二次认证", httpMethod = "POST", response = JsonResult.class, notes = "verifyCode 验证码, username 用户名 password 密码")
  @ResponseBody
  public JsonResult PhoneAuth(@RequestParam String verifyCode, @RequestParam String username,
      @RequestParam String password, HttpServletRequest request, HttpServletResponse response) {

    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      return new JsonResult().setMsg("用户或密码不能为空!");
    }
    if (StringUtils.isEmpty(verifyCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_is_not_null", "zh_CN"));
    }
    try {

      UUID uuid = UUID.randomUUID();
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult login = remoteManageService.login(username, password, uuid.toString());
      if (login != null && login.getSuccess()) {
        User user = (User) login.getObj();
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String session_pwSmsCode = redisService.get("SMS:smsphone:" + user.getPhone());
        //String session_pwSmsCode = (String) request.getSession().getAttribute("accountpwSmsCode");
        if (!verifyCode.equals(session_pwSmsCode)) {
          return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
        }

        userRedisRunnable.process(user.getCustomerId().toString());

        log.info(username + "|登录成功!");
        // 存入redis 给手机端提供token

        user.setTokenId(uuid.toString());
        redisService.save("mobile:" + uuid,
            "{\"mobile\":\"" + username + "\",\"user\":" + JSON.toJSON(login.getObj()).toString()
                + "}",
            1800);
        log.info("UUID=" + uuid);
        Cookie cookie = new Cookie("tokenId", uuid.toString());
        cookie.setMaxAge(1800);
        cookie.setPath("/");
        response.addCookie(cookie);
        if (user != null) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("user", login.getObj());
          map.put("UUID", uuid);
          return new JsonResult().setSuccess(true).setObj(map);
        } else {
          return new JsonResult().setSuccess(false).setObj(user);

        }
      }
      return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff(login.getMsg(), "zh_CN"));
    } catch (Exception e) {
      e.printStackTrace();
      log.error("登录方法远程调用出错");
    }
    return new JsonResult().setSuccess(false).setMsg("用户或密码错误!");
  }

  /**
   *
   * @param codes
   * @param
   * @param request
   * @return
   */

  @RequestMapping("/yzgoogle")
  @ApiOperation(value = "谷歌认证", httpMethod = "POST", response = JsonResult.class, notes = "codes 验证码 , 谷歌密文savedSecret")
  @ResponseBody
  public JsonResult yzgoogle(String codes, String savedSecret, HttpServletRequest request) {
    // enter the code shown on device. Edit this and run it fast before the
    // code expires!
    long code = 0;
    try {
      code = Long.parseLong(codes);
    } catch (NumberFormatException e) {
      return new JsonResult().setSuccess(false)
          .setMsg(SpringContextUtil.diff("gugeyanzhengubuzhengque", "zh_CN"));
    }
    System.out.println("cococooc=" + code);
    System.out.println("savedSecret=" + savedSecret);

    long t = System.currentTimeMillis();
    GoogleAuthenticatorUtil ga = new GoogleAuthenticatorUtil();
    //  ga.setWindowSize(0); // should give 5 * 30 seconds of grace...
    boolean r = ga.check_code(savedSecret, code, t);
    System.out.println("rrrr=" + r);
    User user = SessionUtils.getUser(request);
    if (!r) {
      return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("gugeyanzhengshibai"));
    } else {
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      System.out.println("user" + user.getMobile());
      String usname = "";
      if (user.getMobile() != null) {
        usname = user.getMobile();
      } else {
        usname = user.getUsername();
      }
      RemoteResult result = remoteManageService.sendgoogle(usname, savedSecret);
      if (result.getSuccess()) {
        user.setGoogleState(1);
        user.setGoogleKey(savedSecret);
        SessionUtils.updateRedis(user);
        UUID uuid = UUID.randomUUID();
        RedisService redisService = SpringContextUtil.getBean("redisService");
        user.setTokenId(uuid.toString());
        redisService.save("mobile:" + uuid,
            "{\"mobile\":\"" + user.getUsername() + "\",\"user\":" + JSON.toJSON(user).toString()
                + "}", 1800);
      }

      return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("gugeyanzhengsuccess"));
    }
  }

  @RequestMapping("/googleAuth")
  @ApiOperation(value = "google二次认证", httpMethod = "POST", response = JsonResult.class, notes = "verifyCode 验证码 , username 用户名")
  @ResponseBody
  public JsonResult googleAuth(String password, String verifyCode, String username,
      HttpServletRequest request, HttpServletResponse response) {
    //String GoogleKey = (String) request.getSession().getAttribute("googleKey");
    String languages = request.getParameter("languages");
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    if (languages == null || languages.equals("")) {
      languages = "zh_CN";
    }

    try {

      UUID uuid = UUID.randomUUID();
      RemoteResult login = remoteManageService.login(username, password, uuid.toString());
      if (login != null && login.getSuccess()) {
        User user = (User) login.getObj();
        long code = Long.parseLong(verifyCode);
        long t = System.currentTimeMillis();
        GoogleAuthenticatorUtil ga = new GoogleAuthenticatorUtil();
        //ga.setWindowSize(0); // should give 5 * 30 seconds of grace...
        boolean r = ga.check_code(user.getGoogleKey(), code, t);
        if (!r) {
          return new JsonResult().setSuccess(false)
              .setMsg(SpringContextUtil.diff("gugeyanzhengshibai", languages));
        }
        RedisService redisService = SpringContextUtil.getBean("redisService");

        userRedisRunnable.process(user.getCustomerId().toString());

        log.info(username + "|登录成功!");
        // 存入redis 给手机端提供token

        user.setTokenId(uuid.toString());
        redisService.save("mobile:" + uuid,
            "{\"mobile\":\"" + username + "\",\"user\":" + JSON.toJSON(login.getObj()).toString()
                + "}",
            1800);
        log.info("UUID=" + uuid);
        Cookie cookie = new Cookie("tokenId", uuid.toString());
        cookie.setMaxAge(1800);
        cookie.setPath("/");
        response.addCookie(cookie);
        if (user != null) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("user", login.getObj());
          map.put("UUID", uuid);
          return new JsonResult().setSuccess(true).setObj(map);
        } else {
          return new JsonResult().setSuccess(false).setObj(user);

        }
      }
      return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff(login.getMsg(), "zh_CN"));
    } catch (Exception e) {
      e.printStackTrace();
      log.error("登录方法远程调用出错");
    }
    return new JsonResult().setSuccess(false).setMsg("用户或密码错误!");
  }


  @RequestMapping("/offPhone")
  @ApiOperation(value = "关闭手机认证", httpMethod = "POST", response = JsonResult.class, notes = "verifyCode 验证码")
  @ResponseBody
  public JsonResult offPhone(@RequestParam String verifyCode, HttpServletRequest request) {
    String languages = request.getParameter("languages");
    if (languages == null || languages.equals("")) {
      languages = "zh_CN";
    }
    if (StringUtils.isEmpty(verifyCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_is_not_null", languages));
    }
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        //String session_pwSmsCode = (String) request.getSession().getAttribute("accountpwSmsCode");
        String phone = user.getPhone();
        int i = phone.indexOf("+");
        if (i == -1) {
          String[] strings = phone.split(" ");
          String b = strings[1];
          String c = strings[2];
          phone = "+" + b + " " + c;

        }

        String session_pwSmsCode = redisService.get("SMS:smsphone:" + phone);
        if (!verifyCode.equals(session_pwSmsCode)) {
          return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error", languages));
        }
        RemoteResult offPhone = remoteManageService.offPhone(user.getPhone(), user.getUsername());
        if (offPhone.getSuccess()) {
          user.setPhoneState(0);
          user.setPassDate(new Date());
          SessionUtils.updateRedis(user);
          return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("offphonesu", languages));

        } else {
          return new JsonResult().setSuccess(false)
              .setMsg(SpringContextUtil.diff("offphoneer", languages));

        }
      }


    }
    return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_is_not_null"));
  }


  @RequestMapping("/appSetPhone")
  @ApiOperation(value = "收到验证码后确认", httpMethod = "POST", response = JsonResult.class, notes = "mobile 手机号,  pwSmsCode 验证码")
  @ResponseBody
  public JsonResult setPhone(@RequestParam String mobile, @RequestParam String verifyCode,
      HttpServletRequest request) {
    if (StringUtils.isEmpty(verifyCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_is_not_null"));
    }
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {

        RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
        RemoteResult regist = manageService.regphone(mobile);

        if (!regist.getSuccess()) {
          return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("phonechongfu"));

        } else {
          if (regist.getSuccess()) {
            String session_pwSmsCode = redisService.get("SMS:smsphone:" + mobile);
            if (!verifyCode.equals(session_pwSmsCode)) {
              return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
            }

            RemoteResult setPhone = remoteManageService.setPhone(mobile, user.getUsername());
            if (setPhone != null && setPhone.getSuccess()) {
              user.setPhoneState(1);
              user.setPhone(mobile);
              SessionUtils.updateRedis(user);
              return new JsonResult().setSuccess(true)
                  .setMsg(SpringContextUtil.diff("phonesuccess", request.getParameter("languages")));
            }
          }
        }
      }
    }

    return new JsonResult().setSuccess(true)
        .setMsg(SpringContextUtil.diff("请登录或重新登录", request.getParameter("languages")));
  }


  @RequestMapping(value = "/sendMsg", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "手机认证发送验证码", httpMethod = "POST", response = JsonResult.class, notes = "mobile 手机号")
  @ResponseBody
  public JsonResult sendMsg(HttpServletRequest request, @RequestParam String mobile) {
    String languages = request.getParameter("languages");
    mobile = mobile.replaceAll(" ", "");
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);

    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);

      if (user != null) {
        String address = mobile.substring(0, mobile.indexOf(",")).trim();
        String phone = mobile.substring(mobile.lastIndexOf(",") + 1);

        // 设置短信验证码到session中
        SmsParam smsParam = new SmsParam();
        smsParam.setHryMobilephone(mobile);
        smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
        if (address.equals("86")) {
          smsParam.setHrySmstype(SmsSendUtil.TAKE_PHONE);
          String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, address, phone);
          redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);
        } else {
          smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
          String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, address, phone);
          redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);

        }
        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(true);
        jsonResult.setMsg(SpringContextUtil.diff("sms_success", languages));
        return jsonResult;
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");

  }


  @RequestMapping(value = "/getRecommend", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "推荐佣金页面信息信息", httpMethod = "POST", response = JsonResult.class, notes = "登陆后即可调用")
  @ResponseBody
  public JsonResult getRecommend(HttpServletRequest request) {
    JsonResult result = new JsonResult();
    JSONObject json = new JSONObject();
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    String type = request.getParameter("transactionType");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String tokenId = request.getParameter("tokenId");
    String value = redisService.get("mobile:" + tokenId);
    if(value !=null){
      String tel = JSONObject.parseObject(value).getString("mobile");
      User user = remoteManageService.selectByTel(tel);
      Map<String, String> params = HttpServletRequestUtils.getParams(request);
      params.put("username", user.getMobile().toString());
      if ("0".equals(type)) {// 0查全部
        params.put("transactionType", null);
      }
      String property = HttpUtil.getAppUrl(request);
      RemoteResult selectCommend = remoteManageService.selectCommend(user.getUsername(),property);
      FrontPage findTrades = remoteManageService.rakebake(params);
      json.put("findTrades", findTrades);
      json.put("CommendInfo", selectCommend);
      return result.setSuccess(true).setObj(json);
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }


  /**
   * 获得手机认证短信
   */
  @RequestMapping(value = "/getPhone", method = RequestMethod.POST)
  @ApiOperation(value = "获得手机认证短信", httpMethod = "POST", response = JsonResult.class, notes = " 格式 +86 手机号  +86是国家区号后面有一个空格加手机号")
  @ResponseBody
  public JsonResult getPhone(HttpServletRequest request) {

    String language = null;
    String mobi = null;
    ;
    //手机号
    String phone = null;
    //地区截取
    String address = null;
    String addressqu = null;
    User user = null;
    JsonResult jsonResult = new JsonResult();
    String mobile = null;
    try {
      mobile = request.getParameter("mobile");

      RedisService redisService = SpringContextUtil.getBean("redisService");
      String tokenId = request.getParameter("tokenId");

      String value = redisService.get("mobile:" + tokenId);
      if (tokenId != null) {
        if (value == null || value.equals("")) {
          return jsonResult.setSuccess(false).setMsg("请登录或重新登录");
        }
      }
      if (value != null && !value.equals("")) {
        String tel = JSONObject.parseObject(value).getString("mobile");
        RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
            .getBean("remoteManageService");
        user = remoteManageService.selectByTel(tel);
        if (mobile == null || mobile.equals("")) {
          mobile = user.getPhone();
        }
      } else {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UUID uuid = UUID.randomUUID();
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        RemoteResult login = remoteManageService.login(username, password, uuid.toString());
        if (login != null && login.getSuccess()) {
          user = (User) login.getObj();
        } else {
          if (user != null || !user.equals("")) {

            return jsonResult.setSuccess(false).setMsg("请登录或重新登录");
          }
        }
      }

      int i = mobile.indexOf("+");
      if (i == -1) {
        String[] strings = mobile.split(" ");
        String b = strings[1];
        String c = strings[2];
        mobile = "+" + b + " " + c;
      }
      language = (String) request.getAttribute("language");
      mobi = mobile.replace(" ", "");
      ;
      //手机号
      phone = mobile.substring(mobile.lastIndexOf(" ") + 1);
      //地区截取
      address = mobile.substring(0, mobile.indexOf(" "));
      addressqu = address.substring(address.lastIndexOf("+") + 1);


    } catch (Exception e) {
      e.printStackTrace();
      jsonResult.setSuccess(false);
      jsonResult.setMsg("200");
      return jsonResult;
    }
    if (user != null) {

      // 设置短信验证码到session中
      SmsParam smsParam = new SmsParam();
      smsParam.setHryMobilephone(mobile);
      smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
      if (addressqu.equals("86")) {
        smsParam.setHrySmstype(SmsSendUtil.TAKE_PHONE);
        String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, addressqu, phone);
        RedisService redisService = SpringContextUtil.getBean("redisService");
        redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);
      } else {
        smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
        String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, addressqu, mobi);
        RedisService redisService = SpringContextUtil.getBean("redisService");
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
   * @return authcode
   */

  @RequestMapping("/setPhone")
  @ApiOperation(value = "手机认证", httpMethod = "POST", response = JsonResult.class, notes = " verifyCode短信验证码   mobile： 手机号（格式 +86 手机号  +86是国家区号后面有一个空格加手机号）")
  @ResponseBody
  public JsonResult setPhone(HttpServletRequest request) {
    String pwSmsCode = request.getParameter("verifyCode");//短信验证码
    String mobile = "";
    RedisService redisService = SpringContextUtil.getBean("redisService");
    mobile = request.getParameter("mobile");
    String tokenId = request.getParameter("tokenId"); //modify by zongwei
    String value = redisService.get("mobile:" + tokenId); //modify by zongwei
    if (value == null) {
      return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
    }
    String tel = JSONObject.parseObject(value).getString("mobile");

    int i = mobile.indexOf("+");
    if (i == -1) {
      String[] strings = mobile.split(" ");
      String b = strings[1];
      String c = strings[2];
      mobile = "+" + b + " " + c;
    }
    String phone = mobile.substring(mobile.lastIndexOf(" ") + 1);
    //User user = SessionUtils.getUser(request); //modify by zongwei
    RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
        .getBean("remoteManageService"); //modify by zongwei
    User user = remoteManageService.selectByTel(tel); //modify by zongwei
    //中文英文
    String languages = request.getParameter("languages");
    if (languages == null || languages.equals("")) {
      languages = "zh_CH";
    }
    //地区截取
    if (mobile == null && mobile.equals("")) {
      mobile = user.getPhone();
    }
    //request.getSession().setAttribute("phone", mobile);
    if (StringUtils.isEmpty(pwSmsCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_is_not_null", languages));
    }
    RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
    RemoteResult regist = manageService.regphone(mobile);

    if (!regist.getSuccess()) {
      return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("phonechongfu", languages));

    } else {
      if (regist.getSuccess()) {
        String session_pwSmsCode = redisService.get("SMS:smsphone:" + mobile);
        if (!pwSmsCode.equals(session_pwSmsCode)) {
          return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error", languages));
        }

        RemoteResult setPhone = remoteManageService.setPhone(mobile, user.getUsername());
        if (setPhone != null && setPhone.getSuccess()) {
          user.setPhoneState(1);
          user.setPhone(mobile);
          SessionUtils.updateRedis(user);
        }
      }
    }
    return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("phonesuccess", languages));
  }


  /**
   * 实名认证 提交
   */
  @RequestMapping(value = "/apprealname", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "实名认证 提交", httpMethod = "POST", response = JsonResult.class, notes = " 海内海外类型type(海内 ：0,海外:1 ),姓 surname，名 trueName，性别 sex，国家country，证件类型cardType ，证件号码 cardId ,三张图片 正面  ，反面 ，手持， img1,img2img3")
  @ResponseBody
  public JsonResult apprealname(HttpServletRequest request,
      @RequestParam(required = false) String type, @RequestParam(required = false) String surname,
      @RequestParam(required = false) String trueName,
      @RequestParam(required = false) String cardId, @RequestParam(required = false) String sex,
      @RequestParam(required = false) String country,
      @RequestParam(required = false) String cardType) {
    System.out.println(123);
    try {
      CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
          request.getSession().getServletContext());
      if (multipartResolver.isMultipart(request)) {
        System.out.println("ss");
      }
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

      MultipartFile img1 = multipartRequest.getFile("img1");
      MultipartFile img2 = multipartRequest.getFile("img2");
      MultipartFile img3 = multipartRequest.getFile("img3");
      MultipartFile[] files = {img1, img2, img3};
      for (int i = 0; i < files.length; i++) {
        MultipartFile file = files[i];
        String names = file.getOriginalFilename();
        if (names != null && (names.endsWith("jpg") || names.endsWith("png") || names
            .endsWith("gif") || names.endsWith("bmp"))) {
        } else {
          return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("picture_error"));
        }
        InputStream inputStream = file.getInputStream();
        String fileType = FileType.getFileType(inputStream);
        if (fileType != null && (fileType.equals("jpg") || fileType.equals("png") || fileType
            .equals("gif") || fileType.equals("bmp"))) {
        } else {
          return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("picture_error"));
        }
      }

      String tokenId = request.getParameter("tokenId");
      RedisService redisService = SpringContextUtil.getBean("redisService");
      String value = redisService.get("mobile:" + tokenId);
      if (value != null) {
        String tel = JSONObject.parseObject(value).getString("mobile");
        RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
            .getBean("remoteManageService");
        User user = remoteManageService.selectByTel(tel);
        if (user != null) {
          //String[] pathImg = this.upload(files);
          String[] pathImg = FileUpload.POSTFileQiniu(request, files);
          String config = redisService.get("configCache:all");
          JSONObject parseObject = JSONObject.parseObject(config);
          RemoteResult realname = remoteManageService
              .xstar(user.getUsername(), trueName, sex, surname, country, cardType, cardId, pathImg,
                  type, parseObject.get("language_code").toString());
          if (realname != null) {
            if (realname.getSuccess()) {
              user.setStates(1);
              SessionUtils.updateRedis(user);
              return new JsonResult().setSuccess(true).setMsg("实名认证提交成功");
              //更新session状态
							/*user.setIsReal(1);
							user.setTruename(trueName);
							request.getSession().setAttribute("states",1);
							request.getSession().setAttribute("user",user);*/
            } else {
              return new JsonResult().setSuccess(false)
                  .setMsg(SpringContextUtil.diff(realname.getMsg(), "zh_CN"));
            }
          }
        }
      }

    } catch (Exception e) {
      log.error("实名认证远程调用出错");
      e.printStackTrace();
    }

    return new JsonResult().setSuccess(false).setMsg("提交失败1");

  }

  /**
   * wap实名认证 提交
   */
  @RequestMapping(value = "/waprealname", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "wap实名认证 提交", httpMethod = "POST", response = JsonResult.class, notes = " 海内海外类型type(海内 ：0,海外:1 ),姓surname，名 trueName，性别 sex，国家country，证件类型cardType ，证件号码 cardId ,图片地址 MultipartFile[] file")
  @ResponseBody
  public JsonResult waprealname(@RequestParam("file") MultipartFile[] files,
      HttpServletRequest request) {
    if (files.length <= 0) {
      return new JsonResult().setSuccess(false).setMsg("图片不能为空");
    }

    String type = request.getParameter("type");
    String trueName = request.getParameter("trueName");
    String sex = request.getParameter("sex");
    String surname = request.getParameter("surname");
    String country = request.getParameter("country");
    String cardType = request.getParameter("cardType");
    String cardId = request.getParameter("cardId");

    User user = SessionUtils.getUser(request);
    if (country.equals("") || country == null) {
      country = "+86";
    }
    try {
      // 上传图片
      //String[] pathImg = this.upload(files);
      String[] pathImg = FileUpload.POSTFileQiniu(request, files);
      RedisService redisService = SpringContextUtil.getBean("redisService");
      String config = redisService.get("configCache:all");
      JSONObject parseObject = JSONObject.parseObject(config);
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult realname = remoteManageService
          .xstar(user.getUsername(), trueName, sex, surname, country,
              cardType, cardId, pathImg, type, parseObject.get("language_code").toString());
      if (realname != null) {
        if (realname.getSuccess()) {
          user.setStates(1);
          SessionUtils.updateRedis(user);
          // 更新session状态
          /*
           * user.setIsReal(1); user.setTruename(trueName);
           * request.getSession().setAttribute("states",1);
           * request.getSession().setAttribute("user",user);
           */
        } else {
          return new JsonResult().setSuccess(false)
              .setMsg(SpringContextUtil.diff(realname.getMsg(), "zh_CN"));
        }
      }
    } catch (Exception e) {
      log.error("实名认证远程调用出错");
      e.printStackTrace();
      return new JsonResult().setSuccess(false).setMsg("实名认证远程调用出错");

    }
    return new JsonResult().setSuccess(true)
        .setMsg(SpringContextUtil.diff("tijiaochenggongshenhe", "zh_CN"));

  }


  public static HttpServletRequest getRequest() {
    try {
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
          .getRequestAttributes())
          .getRequest();
      return request;
    } catch (Exception e) {
    }
    return null;

  }

  /**
   * 上传图片
   */
  public String[] upload(@RequestParam("file") MultipartFile[] files) {
    String[] pathImg = new String[3];
    try {
      if (files != null && files.length > 0) {
        for (int i = 0; i < files.length; i++) {
          MultipartFile file = files[i];
          // 获取文件名
          String filename = file.getOriginalFilename();
          // 上传图片
          if (file != null && filename != null && filename.length() > 0) {
            // 上传路径

            String realPath = this.getRequest().getRealPath("/");
            // 生成hryfile路径
            String rootPath = realPath.substring(0,
                org.apache.commons.lang3.StringUtils.lastOrdinalIndexOf(realPath, File.separator, 2)
                    + 1);
            System.out.println("rootPath" + rootPath);
            // 新图片名称
            String newFileName = UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));
            pathImg[i] = "hryfilefront" + File.separator + newFileName;
            File secondFolder = new File(rootPath + "hryfilefront");
            // 存入本地
            if (!secondFolder.exists()) {
              secondFolder.mkdirs();
            }
            File newFile = new File(rootPath + "hryfilefront" + File.separator + newFileName);
            file.transferTo(newFile);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return pathImg;
  }

  private final static Logger log = Logger.getLogger(AppPersonDetailController.class);

  @RequestMapping(value = "/getrealname", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "已实名后所需要的信息", httpMethod = "POST", response = UserInfo.class, notes = "已实名后所需要的信息")
  @ResponseBody
  public JsonResult getrealname(HttpServletRequest request) {
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        RemoteResult result = remoteManageService.getPersonInfo(user.getUserCode());
        return new JsonResult().setSuccess(true).setObj(result.getObj());
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping(value = "/appjypwd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "设置交易密码提交", httpMethod = "POST", response = JsonResult.class, notes = "新密码accountPassWord,确认密码reaccountPassWord,短信验证码accountpwSmsCode")
  @ResponseBody
  public JsonResult appjypwd(HttpServletRequest request, @RequestParam String accountPassWord,
      @RequestParam String reaccountPassWord, @RequestParam String accountpwSmsCode) {

    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        try {
          if (StringUtils.isEmpty(accountPassWord)) {
            return new JsonResult().setSuccess(false).setMsg("交易密码不能为空");
          }
          if (StringUtils.isEmpty(reaccountPassWord)) {
            return new JsonResult().setSuccess(false).setMsg("确认密码不能为空");
          }
          if (!accountPassWord.equals(reaccountPassWord)) {
            return new JsonResult().setSuccess(false).setMsg("两个密码不一致");
          }
          if (StringUtils.isEmpty(accountpwSmsCode)) {
            return new JsonResult().setSuccess(false).setMsg("短信验证码不能为空");
          }
          String session_accountpwSmsCode = redisService.get("appjypwdcode:" + tel);
          if (session_accountpwSmsCode == null) {
            return new JsonResult().setSuccess(false).setMsg("验证码已失效");
          }
          if (!accountpwSmsCode.equals(session_accountpwSmsCode)) {
            return new JsonResult().setSuccess(false).setMsg("短信验证码不正确");
          }

          RemoteResult result = remoteManageService.appresetapw(user.getMobile(), accountPassWord);
          if (result != null) {
            if (result.getSuccess()) {
              return new JsonResult().setSuccess(true).setMsg("修改成功");
            } else {
              return new JsonResult().setSuccess(false).setMsg(result.getMsg());
            }
          }
        } catch (Exception e) {
          log.error("重置交易密码远程调用出错");
        }
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  /**
   * 重置交易密码提交
   */
  @RequestMapping("resetapwsubmit")
  @ApiOperation(value = "重置交易密码提交", httpMethod = "POST", response = JsonResult.class, notes = "passWord登陆密码，新密码accountPassWord,确认密码reaccountPassWord,短信验证码accountpwSmsCode")
  @ResponseBody
  public JsonResult resetapwsubmit(HttpServletRequest request) {
    String language = (String) request.getAttribute("language");

    String passWord = request.getParameter("passWord");
    String accountPassWord = request.getParameter("accountPassWord");
    String reaccountPassWord = request.getParameter("reaccountPassWord");
    String accountpwSmsCode = request.getParameter("accountpwSmsCode");

    String tokenId = request.getParameter("tokenId");

    RedisService redisService = SpringContextUtil.getBean("redisService");

    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {

        if (StringUtils.isEmpty(passWord)) {
          return new JsonResult().setMsg(SpringContextUtil.diff("loginpwd_no_null"));
        }
        if (StringUtils.isEmpty(accountPassWord)) {
          return new JsonResult().setMsg(SpringContextUtil.diff("jypwd_no_null"));
        }
        if (StringUtils.isEmpty(reaccountPassWord)) {
          return new JsonResult().setMsg(SpringContextUtil.diff("okpwd_no_null"));
        }
        if (!accountPassWord.equals(reaccountPassWord)) {
          return new JsonResult().setMsg(SpringContextUtil.diff("twopwd_is_diff"));
        }
        if (StringUtils.isEmpty(accountpwSmsCode)) {
          return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_is_not_null"));
        }
        String session_accountpwSmsCode = redisService.get("appjypwdcode:" + tel);
        if (!accountpwSmsCode.equals(session_accountpwSmsCode)) {
          return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
        }

        try {
          RemoteResult result = remoteManageService
              .resetapw(user.getMobile(), passWord, accountPassWord);
          if (result != null) {
            if (result.getSuccess()) {
              return new JsonResult().setSuccess(true);
            } else {
              return new JsonResult().setMsg(SpringContextUtil.diff(result.getMsg()));
            }
          }
        } catch (Exception e) {
          log.error("重置交易密码远程调用出错");
          return new JsonResult().setMsg(SpringContextUtil.diff("yichang"));
        }
        return new JsonResult();
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping(value = "/appjypwdcode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "发送重置交易密码验证码", httpMethod = "POST", response = JsonResult.class, notes = "新密码accountPassWord,确认密码reaccountPassWord,短信验证码accountpwSmsCode")
  @ResponseBody
  public JsonResult appjypwdcode(HttpServletRequest request, @RequestParam String accountPassWord,
      @RequestParam String reaccountPassWord) {
    JsonResult jsonResult = new JsonResult();
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user.getPhoneState() != 1) {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("请先绑定手机！");
        return jsonResult;
      }
      if (user != null) {
        if (StringUtils.isEmpty(accountPassWord)) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("新密码不能为空");
        } else if (!accountPassWord.equals(reaccountPassWord)) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("两次密码不一致");
        } else {
          // 设置短信验证码到session中
          SmsParam smsParam = new SmsParam();
          smsParam.setHryMobilephone(user.getPhone());
          smsParam.setHrySmstype(SmsSendUtil.RESET_CHANGE_PW);

          redisService
              .save("appjypwdcode:" + tel, SmsSendUtil.sendSmsCode(smsParam, null, null), 120);

          jsonResult.setSuccess(true);
          jsonResult.setMsg("短信发送成功!");
        }
        return jsonResult;
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping(value = "/appdlpwd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "修改登录密码提交(JsonResult + obj)", httpMethod = "POST", response = User.class, notes = "原始密码oldPassWord,新密码newPassWord,再次新密码reNewPassWord")
  @ResponseBody
  public JsonResult appdlpwd(HttpServletRequest request, @RequestParam String oldPassWord,
      @RequestParam String newPassWord, @RequestParam String reNewPassWord) {

    String tokenId = request.getParameter("tokenId");
    String valicode = request.getParameter("valicode");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        if (StringUtils.isEmpty(oldPassWord)) {
          return new JsonResult().setSuccess(false).setMsg("原始密码不能为空");
        }
        if (StringUtils.isEmpty(newPassWord)) {
          return new JsonResult().setSuccess(false).setMsg("新密码不能为空");
        }
        if (oldPassWord.equals(newPassWord)) {
          return new JsonResult().setSuccess(false).setMsg("新登录密码不能和原始登录密码一致");
        }
        if (!newPassWord.equals(reNewPassWord)) {
          return new JsonResult().setSuccess(false).setMsg("两次密码不一致");
        }

        boolean Phone_condition = true;
        boolean Google_condition = true;
        Integer phonestate = user.getPhoneState();
        //手机验证
//				if(user.getPhoneState()==1&&(valicode==null||("").equals(valicode))){
//					return new JsonResult().setSuccess(false).setObj(user);
//				}
//				if(user.getPhoneState()==1){
//					String session_pwSmsCode = redisService.get("SMS:smsphone:"+user.getPhone());
//					//String session_pwSmsCode = (String) request.getSession().getAttribute("accountpwSmsCode");
//					if(!valicode.equals(session_pwSmsCode)){
//						Phone_condition=false;
//						if(user.getPhone_googleState()!=1){
//						return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error","zh_CN"));}
//					}
//				}
        //谷歌验证
        if (user.getGoogleState() == 1 && (valicode == null || ("").equals(valicode))) {
          return new JsonResult().setSuccess(false).setObj(user);
        }
        if (user.getGoogleState() == 1) {
          long code = 0;
          try {
            code = Long.parseLong(valicode);
          } catch (Exception e) {
            if (user.getPhone_googleState() != 1) {
              return new JsonResult().setSuccess(false)
                  .setMsg(SpringContextUtil.diff("gugeyanzhengshibai", "zh_CN"));
            } else {
              Google_condition = false;
            }
          }

          long t = System.currentTimeMillis();
          GoogleAuthenticatorUtil ga = new GoogleAuthenticatorUtil();
          //ga.setWindowSize(0); // should give 5 * 30 seconds of grace...
          boolean r = ga.check_code(user.getGoogleKey(), code, t);
          if (!r) {
            if (user.getPhone_googleState() != 1) {
              return new JsonResult().setSuccess(false)
                  .setMsg(SpringContextUtil.diff("gugeyanzhengshibai", "zh_CN"));
            } else {
              Google_condition = false;
            }
          }
        }
        //如果有一个是ture就代表其中的一种通过了
        if (!Phone_condition && !Google_condition) {
          return new JsonResult().setMsg("验证码错误");


        }

        try {
          RemoteResult result = remoteManageService
              .setpw(user.getMobile(), oldPassWord, newPassWord);
          if (result != null) {
            if (result.getSuccess()) {
              logout(request);
              return new JsonResult().setSuccess(true).setMsg("修改成功");
            } else {
              return new JsonResult().setMsg(SpringContextUtil.diff(result.getMsg(), "zh_CN"));

            }
          }
        } catch (Exception e) {
          log.error("修改登录密码远程调用出错");
          return new JsonResult().setMsg("修改登录密码远程调用出错");
        }
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping(value = "/appdlpwdcode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "发送修改登录密码验证码", httpMethod = "POST", response = JsonResult.class, notes = "原始密码oldPassWord,新密码newPassWord,重复新密码repwd")
  @ResponseBody
  public JsonResult appdlpwdcode(HttpServletRequest request, @RequestParam String oldPassWord,
      @RequestParam String newPassWord, @RequestParam String repwd) {
    JsonResult jsonResult = new JsonResult();

    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      RemoteResult remoteResult = remoteManageService
          .login(user.getMobile(), oldPassWord, user.getUuid());
      if (user != null) {
        if (StringUtils.isEmpty(oldPassWord)) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("原始密码不能为空");
          return jsonResult;
        } else if (remoteResult.getSuccess() == false) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("登录密码错误");
          return jsonResult;
        } else if (StringUtils.isEmpty(newPassWord)) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("交易密码不能为空");
          return jsonResult;
        } else if (!newPassWord.equals(repwd)) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("两次密码不一致");
          return jsonResult;
        } else {
          // 设置短信验证码到session中
          SmsParam smsParam = new SmsParam();
          smsParam.setHryMobilephone(user.getMobile());
          smsParam.setHrySmstype(SmsSendUtil.MODIFY_LOGIN_PW);

          redisService
              .save("appdlpwdcode:" + tel, SmsSendUtil.sendSmsCode(smsParam, null, null), 120);

          jsonResult.setSuccess(true);
          jsonResult.setMsg("短信发送成功!");
        }
        return jsonResult;
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping(value = "/isrealandpwd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "是否实名和密码设置", httpMethod = "POST", response = JsonResult.class, notes = "是否实名和密码设置")
  @ResponseBody
  public JsonResult isrealandpwd(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();

    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 查询账户金额
        MyAccountTO myAccount = remoteManageService.myAccount(user.getCustomerId());

        List<CoinAccount> list = new ArrayList<CoinAccount>();

        // 查询后台参数配置
        String config = redisService.get("configCache:all");
        if (!StringUtils.isEmpty(config)) {
          JSONObject parseObject = JSONObject.parseObject(config);
          map.put("maxWithdrawMoney", parseObject.get("maxWithdrawMoney"));// 当天最多提现金额
          map.put("maxWithdrawMoneyOneTime",
              parseObject.get("maxWithdrawMoneyOneTime"));// 单笔最多提现金额(元)
          String languageCode = (String) parseObject.get("language_code");// 法币类型
          map.put("languageCode", languageCode);
					/*// 查询法币账户
					CoinAccount coina = remoteManageService.getAppaccount(user.getCustomerId());
					if (coina != null) {
						coina.setCoinCode(languageCode);
						coina.setName(languageCode);
						coina.setCurrencyType(languageCode);
						list.add(coina);
					}*/
        }
        // 查询币账户
        List<CoinAccount> findCoinAccount = remoteManageService
            .findCoinAccount(user.getCustomerId());

        // 从redis查图片路径
        if (findCoinAccount != null && findCoinAccount.size() > 0) {
          String string = redisService.get("cn:coinInfoList");
          List<Coin> coins = JSONArray.parseArray(string, Coin.class);
          for (Coin coin : coins) {
            for (CoinAccount coinAccount : findCoinAccount) {
              if (coin.getCoinCode().equals(coinAccount.getCoinCode())) {
                coinAccount.setPicturePath(coin.getPicturePath());
              }
            }
          }
        }
        list.addAll(findCoinAccount);
        // 获取提现手续费费率
        RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil
            .getBean("remoteAppTransactionManageService");
        BigDecimal witfee = remoteAppTransactionManageService.witfee();

        JSONObject parseObject = JSONObject.parseObject(config);
        List<ExDigitalmoneyAccountManage> exlist = remoteAppTransactionManageService
            .listexd(user.getCustomerId(), parseObject.get("language_code").toString());

        for (CoinAccount coinAccount : list) {
          coinAccount.setTokenId(user.getUuid());
          String CoinCode1 = coinAccount.getCoinCode();
          for (ExDigitalmoneyAccountManage exDigitalmoneyAccountManage : exlist) {
            String coinCode = exDigitalmoneyAccountManage.getCoinCode();
            if (coinCode.equals(CoinCode1)) {
              Long id = exDigitalmoneyAccountManage.getId();
              coinAccount.setId(id);
            }
          }
        }
        //手续费率
        String isTrade = parseObject.get("isTrade").toString();

        String isChongbi = parseObject.get("isChongbi").toString();
        String isTibi = parseObject.get("isTibi").toString();
        //获取国家等信息
        RemoteResult result = remoteManageService.getPersonInfo(user.getUserCode());
        if (result != null && result.getSuccess()) {
          UserInfo userInfo = (UserInfo) result.getObj();
          if ("1".equals(userInfo.getType())) {
						/*
						if("en".equals(locale.toString())){
							userInfo.setPapersType("ID Card");
						}*/
          } else if ("2".equals(userInfo.getType())) {
						/*if("en".equals(locale.toString())){
							userInfo.setPapersType("Passport");
						}*/
          }
          map.put("info", result.getObj());
        }

        map.put("isTibi", isTibi);
        map.put("isChongbi", isChongbi);
        map.put("isTrade", isTrade);
        map.put("myAccount", myAccount);
        map.put("user", user);
        map.put("coinAccount", list);
        map.put("witfee", witfee);
        return jsonResult.setSuccess(true).setObj(map);
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping(value = "/regreg", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "注册协议,获取邮箱和客服电话", httpMethod = "POST", response = JsonResult.class, notes = "语言language")
  @ResponseBody
  public JsonResult regreg(HttpServletRequest request, @RequestParam String language) {
    // 查询后台参数配置
    JsonResult jsonResult = new JsonResult();
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String config = redisService.get("configCache:all");
    Map<String, Object> map = new HashMap<String, Object>();
    if (!StringUtils.isEmpty(config)) {
      JSONObject parseObject = JSONObject.parseObject(config);
      if ("zh_CN".equals(language.toString())) {
        map.put("regreg", parseObject.get("reg_cn"));
      } else if ("en".equals(language.toString())) {
        map.put("regreg", parseObject.get("reg_en"));
      }
      map.put("servicePhone", parseObject.get("servicePhone"));
      map.put("serviceEmail", parseObject.get("serviceEmail"));

    }
    return jsonResult.setSuccess(true).setObj(map);
  }

  @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "退出", httpMethod = "POST", response = JsonResult.class, notes = "退出")
  @ResponseBody
  public JsonResult logout(HttpServletRequest request) {
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      redisService.delete("mobile:" + tokenId);
      request.getSession().removeAttribute("user");
      String username = JSONObject.parseObject(value).getString("mobile");
      SessionListener.SESSIONID_USER.remove(request.getSession().getId());
      SessionListener.ONLINE_USER.remove(username);
      redisService.delete("online:username:" + username);
      return new JsonResult().setSuccess(true);
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping(value = "/setphonenext", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "重新绑定手机下一步", httpMethod = "POST", response = JsonResult.class, notes = "tokenId   verifyCode ：验证码")
  @ResponseBody
  public JsonResult setphonenext(HttpServletRequest request) {
    String tokenId = request.getParameter("tokenId");
    String verifyCode = request.getParameter("verifyCode");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);

      if (user == null) {
        return new JsonResult().setSuccess(false).setMsg("请登录或重新登录！");
      }
      String session_verifyCode = redisService.get("SMS:smsphone:" + user.getPhone());
      if (!verifyCode.equals(session_verifyCode)) {
        return new JsonResult().setMsg(SpringContextUtil.diff("短信验证错误或已失效！"));
      }
      return new JsonResult().setSuccess(true);
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }


}
