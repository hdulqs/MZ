package com.mz.front.mobile.user;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.mz.core.util.shiro.PasswordHelper;
import com.mz.util.IpUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.ExDigitalmoneyAccountManage;
import com.mz.manage.remote.model.ExDmCustomerPublicKeyManage;
import com.mz.manage.remote.model.ExDmTransactionManage;
import com.mz.manage.remote.model.ExProductManage;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.GoogleAuthenticatorUtil;
import com.mz.util.SessionUtils;
import com.mz.util.sys.SpringContextUtil;;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/mobile/user/appbtc")
@Api(value = "App操作类", description = "我要充币、提币和添加币账户")
public class AppBtcController {

  @RequestMapping(value = "/selectByIdAccount", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "我要充币页面", httpMethod = "POST", response = ExDigitalmoneyAccountManage.class, notes =
      "跳转到我要充币页面要显示的一些数据(JsonResult+Obj)"
          + "paceFeeRate提币手续费率,leastPaceNum单次提币最小数量,oneDayPaceNum一天提币最大数量")
  @ResponseBody
  public JsonResult selectByIdAccount(HttpServletRequest request) {
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    String config = redisService.get("configCache:all");
    JSONObject parseObject = JSONObject.parseObject(config);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil
            .getBean("remoteAppTransactionManageService");
        List<ExDigitalmoneyAccountManage> list = remoteAppBankCardManageService
            .listexd(user.getCustomerId(),
                parseObject.get("language_code").toString());
        return new JsonResult().setSuccess(true).setObj(list);
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping(value = "/addBiAccount", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "添加币账户页面", httpMethod = "POST", response = ExProductManage.class, notes = "跳转到币账户页面要显示的一些数据(JsonResult+Obj)")
  @ResponseBody
  public JsonResult addBiAccount() {
    RemoteAppTransactionManageService remoteAppTransactionManageService = (RemoteAppTransactionManageService) SpringContextUtil
        .getBean("remoteAppTransactionManageService");
    List<ExProductManage> listProduct = remoteAppTransactionManageService.listProduct();
    return new JsonResult().setSuccess(true).setObj(listProduct);
  }

  @RequestMapping(value = "/appsave", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "新增币账户", httpMethod = "POST", response = ExDmCustomerPublicKeyManage.class, notes = "新增币账户")
  @ResponseBody
  public JsonResult appsave(HttpServletRequest request, @RequestParam String currencyType,
      @RequestParam String publicKey, @RequestParam String remark) {
    ExDmCustomerPublicKeyManage exDmCustomerPublicKeyManage = new ExDmCustomerPublicKeyManage();
    exDmCustomerPublicKeyManage.setCurrencyType(currencyType);
    exDmCustomerPublicKeyManage.setPublicKey(publicKey);
    exDmCustomerPublicKeyManage.setRemark(remark);

    JsonResult jsonresult = new JsonResult();
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
//                    String username = (String) request.getSession().getAttribute("username");
//////                    user.setUsername(username);
          RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil
              .getBean("remoteAppTransactionManageService");
          remoteAppBankCardManageService.save(exDmCustomerPublicKeyManage, user);
          return jsonresult.setSuccess(true).setObj(exDmCustomerPublicKeyManage).setMsg("保存成功");
        } catch (Exception e) {
          e.printStackTrace();
          return jsonresult.setSuccess(false).setMsg("远程调用失败");
        }
      }
    }
    return jsonresult.setSuccess(false).setMsg("请登录或重新登录！");
  }

  /**
   * 查询提现币的记录 交易类型(1充币 ，2提币)'
   */
  @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "查询提现币的记录(JsonResult+Obj)", httpMethod = "POST", response = ExDmTransactionManage.class, notes = "offset; limit; sortOrder = asc; transactionType : 交易类型(1充币 ，2提币); status: 1待审核 2已完成 3以否决; coinCode=USDT ; '")
  @ResponseBody
  public FrontPage list(HttpServletRequest request) {
    User user = SessionUtils.getUser(request);
    //add by zongwei 20180509 如果用户取不到通过 tokenId取   begin
    if (user == null) {
      String tokenId = request.getParameter("tokenId");
      RedisService redisService = SpringContextUtil.getBean("redisService");
      String value = redisService.get("mobile:" + tokenId);
      if (value != null) {
        String tel = JSONObject.parseObject(value).getString("mobile");
        RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
            .getBean("remoteManageService");
        user = remoteManageService.selectByTel(tel);
      }
    }
    //add by zongwei 20180509 如果用户取不到通过 tokenId取   end
    String status = request.getParameter("status");
    String coinCode = request.getParameter("coinCode");
    RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil
        .getBean("remoteAppTransactionManageService");
    Map<String, String> params = HttpServletRequestUtils.getParams(request);
    params.put("customerId", user.getCustomerId().toString());
    //modify by zongwei 20180510 begin
    String transactionType = request.getParameter("transactionType");
    if (transactionType == null) {
      transactionType = "2";
    }
    //modify by zongwei 20180510 end
    params.put("transactionType", transactionType);
    params.put("status", status);
    FrontPage page = remoteAppTransactionManageService.findExdmtransaction(params);
    List<ExDmTransactionManage> list = null;
//        if (coinCode == null) {
//            list = page.getRows();
//        } else {
//            for (ExDmTransactionManage object : list) {
//                if (object.getCoinCode() == coinCode) {
//                    list.add(object);
//                }
//            }
//        }
    list = page.getRows();
    page.setRows(list);

    return page;
  }

  @RequestMapping(value = "/jumpCoin", method = RequestMethod.POST, produces = "application/json; charset=utf-8")

  @ApiOperation(value = "我要提币页面", httpMethod = "POST", notes = "跳转我要提币页面所要显示的数据,list为所持有的币,list2为钱包地址(JsonResult+Obj(list,list2))")
  @ResponseBody
  public JsonResult jumpCoin(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    String config = redisService.get("configCache:all");
    JSONObject parseObject = JSONObject.parseObject(config);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);

      RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil
          .getBean("remoteAppTransactionManageService");
      List<ExDigitalmoneyAccountManage> list = remoteAppBankCardManageService
          .listexd(user.getCustomerId(),
              parseObject.get("language_code").toString());
      List<String> openTibiList = remoteManageService.findOpenTibi();
      map.put("list", list);
      //查询出禁止提币的币，并remove
      for (int i = 0; i < list.size(); i++) {
        if (!openTibiList.contains(list.get(i).getCoinCode())) {
          list.remove(i);
          i = i - 1;
          continue;
        }
      }
      List<ExDmCustomerPublicKeyManage> list2 = remoteAppBankCardManageService
          .getList(user.getCustomerId());
      map.put("list2", list2);
      return new JsonResult().setSuccess(true).setObj(map);
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录！");
  }

  @RequestMapping(value = "/getWithdrawCoinCode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "提币短信发送", httpMethod = "POST", response = JsonResult.class, notes = "提币数量inputNumWit,交易密码accountPassWord")
  @ResponseBody
  public JsonResult getWithdrawCoinCode(HttpServletRequest request,
      @RequestParam String inputNumWit,
      @RequestParam String accountPassWord) {
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
        String accountpwSmsCode = redisService.get("accounCoinCode:" + tel);
        if (StringUtils.isEmpty(inputNumWit)) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("提币数量不能为空");
        } else if (StringUtils.isEmpty(accountPassWord)) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("交易密码不能为空");
        } else {
          SmsParam smsParam = new SmsParam();
          smsParam.setHryMobilephone(user.getUsername());
          smsParam.setHrySmstype(SmsSendUtil.TAKE_COIN);

          redisService
              .save("accounCoinCode:" + tel, SmsSendUtil.sendSmsCode(smsParam, null, null), 120);
          jsonResult.setSuccess(true);
          jsonResult.setMsg("短信发送成功!");
        }
        return jsonResult;
      }
    }
    return jsonResult.setSuccess(false).setMsg("请登录或重新登录");
  }

  private JsonResult verificationOrder(HttpServletRequest request) {
    String tel = "tel:";
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
   * 提币短信获取
   */
  @RequestMapping(value = "/getbtcvalicode", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult getbtcvalicode(HttpServletRequest request) {

    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }
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

    if (StringUtils.isEmpty(mobile)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("请先绑定手机号！");
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
      redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);

    } else {
      smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
      String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, address, mobi);
      redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);

    }
    jsonResult.setSuccess(true);
    jsonResult.setMsg(SpringContextUtil.diff("sms_success"));

    return jsonResult;
  }

  @RequestMapping(value = "/getbtc", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "提币", httpMethod = "POST", response = JsonResult.class, notes = "提币类型coinType（币名字的缩写,例:HRC）,提币数量btcNum,提币地址btcKey,手续费pacecurrecy，短信认证码valicode 交易密码 accountPassWord")
  @ResponseBody
  public JsonResult addCoin(HttpServletRequest request, @RequestParam String coinType,
      @RequestParam String btcNum,
      @RequestParam String btcKey, @RequestParam String pacecurrecy) {
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    String valicode = request.getParameter("valicode");
    String passWord = request.getParameter("accountPassWord");
    try {
      if (new BigDecimal(pacecurrecy).compareTo(new BigDecimal(btcNum)) >= 0) {
        return new JsonResult().setSuccess(false).setMsg("提币手续费的不能大于或者等提币数量");
      }
    } catch (Exception e) {
      return new JsonResult().setSuccess(false).setMsg("请输入正确的数量！");
    }
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);

      if (user != null) {
        //add by zongwei 提币必须要绑定手机
        if (user.getPhoneState() != 1) {
          return new JsonResult().setSuccess(false).setObj(user).setMsg("请到个人中心绑定手机！");
        }
        //
        if (user.getAccountPassWord() == null || "".equals(user.getAccountPassWord())) {
          return new JsonResult().setSuccess(false).setObj(user).setMsg("请到个人中心设置交易密码！");
        }

        if (user.getStates() != 2) {
          if (user.getStates() == 0) {
            return new JsonResult().setSuccess(false).setMsg("请个人中心实名认证！");
          } else {
            return new JsonResult().setSuccess(false).setMsg("请等待实名认证审批完成！！");
          }
        }

        if (user.getAccountPassWord() != null && !"".equals(user.getAccountPassWord())) {
          PasswordHelper passwordHelper = new PasswordHelper();
          String pw = passwordHelper.encryString(passWord, user.getSalt());
          if (!pw.equals(user.getAccountPassWord())) {
            return new JsonResult().setMsg(SpringContextUtil.diff("mimacuowu"));
          }
        }

        boolean Phone_condition = true;
        boolean Google_condition = true;
        //提币验证，验证币是否被禁止交易
        List<String> openTibiList = remoteManageService.findOpenTibi();
        //查询出禁止提币的币，并remove
        if (!openTibiList.contains(coinType)) {
          return new JsonResult().setSuccess(false).setMsg("该币种已禁止交易");
        }
        //手机验证
        if (user.getPhoneState() == 1 && (valicode == null || valicode.equals(""))) {
          return new JsonResult().setSuccess(false).setObj(user).setMsg("短信验证码不能为空！");
        }
        if (user.getPhoneState() == 1) {
          String session_pwSmsCode = redisService.get("SMS:smsphone:" + user.getPhone());
          //String session_pwSmsCode = (String) request.getSession().getAttribute("accountpwSmsCode");
          if (!valicode.equals(session_pwSmsCode)) {
            Phone_condition = false;
            if (user.getPhone_googleState() != 1) {
              return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error", "zh_CN"));
            }
          }
        }
        //谷歌验证
        if (user.getGoogleState() == 1 && (valicode == null || valicode.equals(""))) {
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

        User selectByTel = remoteManageService.selectByTel(user.getUsername());
        if (selectByTel != null) {
          try {
            return addCoin1(redisService, user, selectByTel, coinType, btcNum, btcKey, pacecurrecy);

          } catch (Exception e) {
            return new JsonResult().setSuccess(false);
          }
        }
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  //提币方法
  public JsonResult addCoin1(RedisService redisService, User user, User selectByTel,
      String coinType, String btcNum, String btcKey, String pacecurrecy) {

    /*
     * String sessionAccountpwSmsCode =
     * redisService.get("accounCoinCode:"+tel);
     * if(sessionAccountpwSmsCode==null){ return new
     * JsonResult().setSuccess(false).setMsg("验证码失效,请重新发送");
     * }
     */

    String config = redisService.get("configCache:all");
    JSONObject parseObject = JSONObject.parseObject(config);
    String languageCode = (String) parseObject.get("language_code");
    String isChongbi = parseObject.get("isChongbi").toString();

    if (isChongbi != null && "1".equals(isChongbi)) {
      //已经实名和不需要实名可以交易
      String accountPW = user.getAccountPassWord();
      String username = selectByTel.getUsername();
      RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil
          .getBean("remoteAppTransactionManageService");
      RemoteResult remoteResult = remoteAppBankCardManageService.getOrdervail(user, coinType,
          null, null, btcNum, languageCode, username, btcKey);
      if (remoteResult.getSuccess()) {
        remoteResult = remoteAppBankCardManageService
            .getOrder(user, coinType, null, null, btcNum, languageCode, username, btcKey,
                pacecurrecy);
      }
      if (remoteResult.getSuccess()) {
        return new JsonResult().setSuccess(true)
            .setMsg(SpringContextUtil.diff(remoteResult.getMsg().toString(), "zh_CN"));
      } else {
        return new JsonResult().setSuccess(false)
            .setMsg(SpringContextUtil.diff(remoteResult.getMsg().toString(), "zh_CN"));
      }
    } else {
      //未实名和需要实名才可以交易且未实名
      if (user != null && user != null) {
        if (selectByTel.getStates() == 0) {
          return new JsonResult().setSuccess(false).setMsg("实名还未认证,请先实名");
        } else if (selectByTel.getStates() == 1) {
          return new JsonResult().setSuccess(false).setMsg("实名认证正在审核，请耐心等待");
        } else if (selectByTel.getStates() == 2) {

          //已经实名和不需要实名可以交易
          String accountPW = user.getAccountPassWord();
          String username = selectByTel.getUsername();
          RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil
              .getBean("remoteAppTransactionManageService");
          RemoteResult remoteResult = remoteAppBankCardManageService.getOrdervail(user, coinType,
              "123", "123", btcNum, languageCode, username, btcKey);
          //add by zongwei 20180514

          if (!remoteResult.getSuccess()) {
            return new JsonResult().setSuccess(false)
                .setMsg(SpringContextUtil.diff(remoteResult.getMsg().toString(), "zh_CN"));
          }
          remoteResult = remoteAppBankCardManageService
              .getOrder(user, coinType, null, null, btcNum, languageCode, username, btcKey,
                  pacecurrecy);

          if (remoteResult.getSuccess()) {
            return new JsonResult().setSuccess(true)
                .setMsg(SpringContextUtil.diff(remoteResult.getMsg().toString(), "zh_CN"));
          } else {
            return new JsonResult().setSuccess(false)
                .setMsg(SpringContextUtil.diff(remoteResult.getMsg().toString(), "zh_CN"));
          }
        } else if (selectByTel.getStates() == 3) {
          return new JsonResult().setSuccess(false).setMsg("实名认证正在审核，请耐心等待");
        }
      }

      return new JsonResult().setSuccess(false).setMsg("实名认证正在审核，请耐心等待");

    }
  }

  @RequestMapping(value = "/selectwallet", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "查询钱包地址", httpMethod = "POST", response = ExDmCustomerPublicKeyManage.class, notes = "查询钱包地址")
  @ResponseBody
  public JsonResult selectwallet(HttpServletRequest request) {
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        RemoteAppTransactionManageService remoteAppTransactionManageService = (RemoteAppTransactionManageService) SpringContextUtil
            .getBean("remoteAppTransactionManageService");
        List<ExDmCustomerPublicKeyManage> listPublic = remoteAppTransactionManageService
            .listPublic(user.getCustomerId());
        return new JsonResult().setSuccess(true).setObj(listPublic);
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping("/delete")
  @ApiOperation(value = "删除币账户", httpMethod = "POST", response = JsonResult.class, notes = "删除币账户")
  @ResponseBody
  public JsonResult delete(HttpServletRequest request, @RequestParam String id) {
    if (id != null && !id.equals("")) {
      try {
        RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil
            .getBean("remoteAppTransactionManageService");
        remoteAppBankCardManageService.deletePublieKey(Long.valueOf(id));
        return new JsonResult().setSuccess(true);
      } catch (Exception e) {
        // TODO: handle exception
        System.out.println("远程调用失败");
        e.printStackTrace();
        return new JsonResult().setSuccess(false);
      }
    } else {
      return new JsonResult().setSuccess(false);
    }
  }

  /**
   * 获得币地址
   */
  @RequestMapping("/getPublicKey")
  @ApiOperation(value = "获得币地址", httpMethod = "POST", response = JsonResult.class, notes = "accountId 币的id")
  @ResponseBody
  public JsonResult getPublicKey(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String accountId = request.getParameter("accountId");
    if (!StringUtils.isEmpty(accountId)) {
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult remoteResult = remoteManageService.getPublicKey(Long.valueOf(accountId));
      if (remoteResult != null && remoteResult.getSuccess()) {
        return jsonResult.setSuccess(true).setObj(remoteResult.getObj());
      } else {
        return jsonResult.setMsg((SpringContextUtil.diff(remoteResult.getMsg(), "zh_CN")));
      }
    }

    return new JsonResult().setMsg("accountId is null");
  }

  /**
   * 生成币地址
   */
  @RequestMapping("/createPublicKey")
  @ApiOperation(value = "生成币地址", httpMethod = "POST", response = JsonResult.class, notes = "accountId 币的id")
  @ResponseBody
  public JsonResult createPublicKey(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String accountId = request.getParameter("accountId");
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user.getStates() != 2) {
        if (user.getStates() == 0) {
          return new JsonResult().setSuccess(false).setMsg("请个人中心实名认证！");
        } else {
          return new JsonResult().setSuccess(false).setMsg("请等待实名认证审批完成！！");
        }
      }
      if (!StringUtils.isEmpty(accountId)) {
        RemoteResult remoteResult = remoteManageService.createPublicKey(Long.valueOf(accountId));
        if (remoteResult != null && remoteResult.getSuccess()) {
          return jsonResult.setSuccess(true).setObj(remoteResult.getObj());
        } else {
          return jsonResult.setMsg(SpringContextUtil.diff(remoteResult.getMsg(), "zh_CN"));
        }
      }

      return new JsonResult().setMsg("accountId is null");
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }


  /**
   * 查询提币费率
   */
  @RequestMapping("/findcurrcy")
  @ApiOperation(value = "查询提币的综合信息", httpMethod = "POST", response = ExDigitalmoneyAccountManage.class, notes = "查询提币费率  币种coinCode ;   返回值   paceFeeRate//提币手续费率;leastPaceNum;//单次提币最小数量oneDayPaceNum;//一天提币最大数量")
  @ResponseBody
  public JsonResult findcurrcy(@RequestParam String coinCode, @RequestParam String tokenId) {
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        String config = redisService.get("configCache:all");
        JSONObject parseObject = JSONObject.parseObject(config);
        RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil
            .getBean("remoteAppTransactionManageService");
        List<ExDigitalmoneyAccountManage> list = remoteAppBankCardManageService
            .listexd(user.getCustomerId(),
                parseObject.get("language_code").toString());
        String paceCurrecy = redisService.get("paceCurrecy:" + coinCode);
        String currecyType = redisService.get("currecyType:" + coinCode);

        List<Object> olist = new ArrayList<>();
        // 对比是否是需要的币的信息
        for (ExDigitalmoneyAccountManage exDigitalmoneyAccountManage : list) {
          if (exDigitalmoneyAccountManage.getCoinCode().equals(coinCode)) {

            exDigitalmoneyAccountManage.setPaceCyrrcey(paceCurrecy);
            exDigitalmoneyAccountManage.setCurrencyType(currecyType);
            olist.add(exDigitalmoneyAccountManage);
            olist.add(paceCurrecy);
            olist.add(currecyType);
            return new JsonResult().setSuccess(true).setObj(olist);
          }
        }
      }
    }

    return new JsonResult().setSuccess(false).setMsg("请重试");
  }


}
