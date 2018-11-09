package com.mz.exapi.api.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.mz.core.annotation.RequestLimit;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.exapi.constant.APICodeConstant;
import com.mz.exapi.util.EncryptUtil;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.manage.remote.RemoteManageService;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.trade.redis.model.EntrustByUser;
import com.mz.util.GoogleAuthenticatorUtil;
import com.mz.util.sys.SpringContextUtil;
import com.mz.front.redis.model.UserRedis;
import com.mz.manage.remote.RemoteApiService;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.model.ApiExApiApply;
import com.mz.manage.remote.model.AppBankCardManage;
import com.mz.manage.remote.model.AppTransactionManage;
import com.mz.manage.remote.model.Coin;
import com.mz.manage.remote.model.CoinAccount;
import com.mz.manage.remote.model.Entrust;
import com.mz.manage.remote.model.ExDigitalmoneyAccountManage;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.trade.redis.model.EntrustTrade;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/user")
public class ApiUserController {


  // 2.5获取用户币种钱包地址
  //@RequestLimit(count=2000 ,time=60)//拦截请求time秒，count次数
  @RequestMapping(value = "/getPurseAddress", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult getPurseAddress(HttpServletRequest request, String accesskey, String coinCode,
      String sign1) {

    JsonResult jsonResult = new JsonResult();
    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    // 校验k值
    String sign2 = EncryptUtil.hmacSign(coinCode, exApiApply.getAccessKey());
    if (sign1 == null || !sign2.equals(sign1)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("sign错误");
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      return jsonResult;
    }

    if (exApiApply == null || exApiApply.equals("")) {
      jsonResult.setSuccess(false);
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      jsonResult.setMsg("key无效");
      return jsonResult;
    }
    Long customerId = exApiApply.getCustomerId();
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    RemoteResult remoteResult = remoteManageService.getAddress(customerId, coinCode);
    ExDigitalmoneyAccount digitalmoneyAccount = (ExDigitalmoneyAccount) remoteResult.getObj();
			/*ExDigitalmoneyAccount exDigitalmoneyAccount = new ExDigitalmoneyAccount();
			exDigitalmoneyAccount.setCustomerId(customerId);
			exDigitalmoneyAccount.setUserName(digitalmoneyAccount.getUserName());
			exDigitalmoneyAccount.setPublicKey(digitalmoneyAccount.getPublicKey());
			exDigitalmoneyAccount.setSurname(digitalmoneyAccount.getSurname());
			exDigitalmoneyAccount.setTrueName(digitalmoneyAccount.getTrueName());*/
    return new JsonResult().setSuccess(true).setObj(digitalmoneyAccount)
        .setCode(APICodeConstant.CODE_SUCCESS);
  }


  //获取单个委托
  @RequestMapping(value = "/exEntrust", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult exEntrust(HttpServletRequest request, String sign1,
      String accesskey) {

    JsonResult jsonResult = new JsonResult();
    String entrustNum = request.getParameter("entrustNum");

    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    // 校验k值
    String sign2 = EncryptUtil.hmacSign(entrustNum, exApiApply.getAccessKey());
    if (sign1 == null || !sign2.equals(sign1)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("sign错误");
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      return jsonResult;
    }

    if (exApiApply == null || exApiApply.equals("")) {
      jsonResult.setSuccess(false);
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      jsonResult.setMsg("key无效");
      return jsonResult;
    }
    Long customerId = exApiApply.getCustomerId();
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    User user = remoteManageService.selectByustomerId(customerId);
    RemoteManageService service = SpringContextUtil.getBean("remoteManageService");
    Map<String, String> params = new HashMap<String, String>();
    params.put("limit", "9999999");
    params.put("offset", "0");
    params.put("sortOrder", "asc");
    params.put("type", "center");
    params.put("querypath", "center");
    params.put("typeone", null);
    params.put("customerId", user.getCustomerId().toString());
    FrontPage findEntrust = service.findEntrust(params);
    List<ExEntrust> rows = findEntrust.getRows();
    for (ExEntrust entrust : rows) {

      String entrustNum2 = entrust.getEntrustNum();
      if (entrustNum2 != null && !"".equals(entrustNum2) && entrustNum2.equals(entrustNum)) {

        return jsonResult.setSuccess(true).setObj(entrust).setCode(APICodeConstant.CODE_SUCCESS);
      }


    }
    return jsonResult.setSuccess(false).setMsg("未查询到该委托").setCode(APICodeConstant.CODE_FAILED);
  }

  //获取我的委托
  @RequestLimit(count = 2000, time = 60)//拦截请求time秒，count次数
  @RequestMapping(value = "/allExEntrust", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult cancelSingleExEntrust(HttpServletRequest request, String sign1,
      String accesskey, String limit, String offset, String sortOrder, String querypath) {

    JsonResult jsonResult = new JsonResult();

    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    // 校验k值
    String sign2 = EncryptUtil
        .hmacSign(limit + offset + sortOrder + querypath, exApiApply.getAccessKey());
    if (sign1 == null || !sign2.equals(sign1)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("sign错误");
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      return jsonResult;
    }

    if (exApiApply == null || exApiApply.equals("")) {
      jsonResult.setSuccess(false);
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      jsonResult.setMsg("key无效");
      return jsonResult;
    }
    Long customerId = exApiApply.getCustomerId();
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    User user = remoteManageService.selectByustomerId(customerId);
    RemoteManageService service = SpringContextUtil.getBean("remoteManageService");
    Map<String, String> params = new HashMap<String, String>();
    params.put("limit", limit);
    params.put("offset", offset);
    params.put("sortOrder", sortOrder);
    params.put("type", querypath);
    params.put("querypath", querypath);
    params.put("typeone", null);
    params.put("customerId", user.getCustomerId().toString());
    FrontPage findEntrust = service.findEntrust(params);
    return jsonResult.setSuccess(true).setObj(findEntrust).setCode(APICodeConstant.CODE_SUCCESS);
  }


  // 获取用户的信息
  @RequestLimit(count = 2000, time = 60)//拦截请求time秒，count次数
  @RequestMapping(value = "/getAccountInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult isrealandpwd(HttpServletRequest request, String sign1,
      String accesskey) {

    JsonResult jsonResult = new JsonResult();

    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    // 校验k值
    String sign2 = EncryptUtil.hmacSign(accesskey, exApiApply.getAccessKey());
    if (sign1 == null || !sign2.equals(sign1)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("sign错误");
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      return jsonResult;
    }

    if (exApiApply == null || exApiApply.equals("")) {
      jsonResult.setSuccess(false);
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      jsonResult.setMsg("key无效");
      return jsonResult;
    }
    Long customerId = exApiApply.getCustomerId();

    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");

    User user = remoteManageService.selectByustomerId(customerId);
    Map<String, Object> map = new HashMap<String, Object>();
    List<CoinAccount> list = new ArrayList<CoinAccount>();
    // 查询后台参数配置
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String config = redisService.get("configCache:all");
    if (!StringUtils.isEmpty(config)) {
      JSONObject parseObject = JSONObject.parseObject(config);
      map.put("maxWithdrawMoney", parseObject.get("maxWithdrawMoney"));// 当天最多提现金额
      map.put("maxWithdrawMoneyOneTime", parseObject.get("maxWithdrawMoneyOneTime"));// 单笔最多提现金额(元)
      String languageCode = (String) parseObject.get("language_code");// 法币类型
      map.put("languageCode", languageCode);
      // 查询法币账户
      CoinAccount coina = remoteManageService.getAppaccount(user.getCustomerId());
      if (coina != null) {
        coina.setCoinCode(languageCode);
        coina.setName(languageCode);
        coina.setCurrencyType(languageCode);
        // 法币类型为0
        coina.setMoneyAndCoin(0);
        list.add(coina);
      }
    }

    // 查询币账户
    List<CoinAccount> findCoinAccount = remoteManageService.findCoinAccount(user.getCustomerId());
    if (findCoinAccount.size() == 0) {
      UserRedisRunnable(customerId.toString());
      //生成tokenid
      UUID uuid = UUID.randomUUID();
      redisService.save("mobile:" + uuid,
          "{\"mobile\":\"" + user.getUsername() + "\",\"user\":" + JSON.toJSON(user).toString()
              + "}", 1800);
      findCoinAccount = remoteManageService.findCoinAccount(user.getCustomerId());
    }

    // 从redis查图片路径
    if (findCoinAccount != null && findCoinAccount.size() > 0) {
      String string = redisService.get("cn:coinInfoList");
      List<Coin> coins = JSONArray.parseArray(string, Coin.class);
      for (Coin coin : coins) {
        for (CoinAccount coinAccount : findCoinAccount) {
          // 虚拟货币类型为1
          coinAccount.setMoneyAndCoin(1);
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
        .listexd(user.getCustomerId(),
            parseObject.get("language_code").toString());

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
    // 手续费率
    String isTrade = parseObject.get("isTrade").toString();

    String isChongbi = parseObject.get("isChongbi").toString();
    String isTibi = parseObject.get("isTibi").toString();
    // 获取国家等信息
    RemoteResult result = remoteManageService.getPersonInfo(user.getUserCode());
    if (result != null && result.getSuccess()) {
      map.put("info", result.getObj());
    }

    map.put("isTibi", isTibi);
    map.put("isChongbi", isChongbi);
    map.put("isTrade", isTrade);
    map.put("user", user);
    map.put("coinAccount", list);
    map.put("witfee", witfee);
    return jsonResult.setSuccess(true).setObj(map).setCode(APICodeConstant.CODE_SUCCESS);
  }

  private void UserRedisRunnable(String customerId) {

    UserRedis userRedis = new UserRedis();
    userRedis.setId(customerId);

    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    Map<String, Long> map = remoteManageService.findAllAccountId(customerId);
    userRedis.setAccountId(map.get("accountId") == null ? null : map.get("accountId"));
    //获取完后，移除
    map.remove("accountId");
    userRedis.setDmAccountId(map);

    RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
    redisUtil.put(userRedis, userRedis.getId());
    //缓存委托历史记录和当前记录
    RedisUtil<EntrustByUser> redisUtilEntrustByUser = new RedisUtil<EntrustByUser>(
        EntrustByUser.class);
    List<Map<String, List<EntrustTrade>>> listml = remoteManageService
        .findExEntrustBycust(Long.valueOf(customerId));
    EntrustByUser ebu = new EntrustByUser();
    ebu.setCustomerId(Long.valueOf(customerId));
    ebu.setEntrustedmap(listml.get(0));
    ebu.setEntrustingmap(listml.get(1));
    redisUtilEntrustByUser.put(ebu, customerId);


  }


  //充值接口
  @RequestLimit(count = 2000, time = 60)//拦截请求time秒，count次数
  @RequestMapping(value = "/rmbdeposit", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult rmbdeposit(HttpServletRequest request, String surname, String remitter,
      String bankCode,
      String bankAmount, String bankName, String accesskey, String sign1) {

    JsonResult jsonResult = new JsonResult();
    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    // 校验k值
    String sign2 = EncryptUtil
        .hmacSign(surname + remitter + bankCode + bankAmount + bankName, exApiApply.getAccessKey());
    if (sign1 == null || !sign2.equals(sign1)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("sign错误");
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      return jsonResult;
    }

    if (exApiApply == null || exApiApply.equals("")) {
      jsonResult.setSuccess(false);
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      jsonResult.setMsg("key无效");
      return jsonResult;
    }
    Long customerId = exApiApply.getCustomerId();
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    User user = remoteManageService.selectByustomerId(customerId);

    AppTransactionManage appTransaction = new AppTransactionManage();
    RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil
        .getBean("remoteAppTransactionManageService");
    RemoteResult remoteResult = remoteAppTransactionManageService
        .generateRmbdeposit(surname, remitter, bankCode,
            bankAmount, bankName, appTransaction, user);
    return new JsonResult().setSuccess(true).setObj(remoteResult.getObj())
        .setCode(APICodeConstant.CODE_SUCCESS);
  }

  // 获取用户的提现地址
  @RequestLimit(count = 2000, time = 60)//拦截请求time秒，count次数
  @RequestMapping(value = "/getWithdrawAddress", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult findBankCard(HttpServletRequest request, String accesskey, String sign1) {

    JsonResult jsonResult = new JsonResult();
    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    // 校验k值
    String sign2 = EncryptUtil.hmacSign(accesskey, exApiApply.getAccessKey());
    if (sign1 == null || !sign2.equals(sign1)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("sign错误");
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      return jsonResult;
    }

    if (exApiApply == null || exApiApply.equals("")) {
      jsonResult.setSuccess(false);
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      jsonResult.setMsg("key无效");
      return jsonResult;
    }
    Long customerId = exApiApply.getCustomerId();
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    User user = remoteManageService.selectByustomerId(customerId);

    RemoteAppTransactionManageService remoteAppBankCardManageService = SpringContextUtil
        .getBean("remoteAppTransactionManageService");
    List<AppBankCardManage> list = remoteAppBankCardManageService
        .findByCustomerId(user.getCustomerId());
    return new JsonResult().setSuccess(true).setObj(list).setCode(APICodeConstant.CODE_SUCCESS);
  }


  /**
   * 查询提现币的记录 交易类型(1充币 ，2提币)'
   */
  @RequestLimit(count = 2000, time = 60)//拦截请求time秒，count次数
  @RequestMapping(value = "/findcoinInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult list(HttpServletRequest request, String accesskey, String sign1,
      String limit, String offset, String type, String sortOrder) {
    JsonResult jsonResult = new JsonResult();
    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    // 校验k值
    String sign2 = EncryptUtil
        .hmacSign(limit + offset + sortOrder + type, exApiApply.getAccessKey());
    if (sign1 == null || !sign2.equals(sign1)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("sign错误");
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      return jsonResult;
    }

    if (exApiApply == null || exApiApply.equals("")) {
      jsonResult.setSuccess(false);
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      jsonResult.setMsg("key无效");
      return jsonResult;
    }
    Long customerId = exApiApply.getCustomerId();
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    User user = remoteManageService.selectByustomerId(customerId);

    RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil
        .getBean("remoteAppTransactionManageService");
    Map<String, String> params = new HashMap<String, String>();
    params.put("customerId", user.getCustomerId().toString());
    params.put("transactionType", type);
    params.put("limit", limit);
    params.put("offset", offset);
    params.put("sortOrder", sortOrder);
    FrontPage page = remoteAppTransactionManageService.findExdmtransaction(params);
    return jsonResult.setCode(APICodeConstant.CODE_SUCCESS).setSuccess(true).setObj(page);
  }

  //用户提现数字资产
  @RequestLimit(count = 2000, time = 60)//拦截请求time秒，count次数
  @RequestMapping(value = "/withdraw", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult addCoin(HttpServletRequest request, String accesskey, String sign1,
      @RequestParam String coinType, @RequestParam String btcNum,
      @RequestParam String btcKey, @RequestParam String pacecurrecy) {
    RedisService redisService = SpringContextUtil.getBean("redisService");

    String valicode = request.getParameter("valicode");

    JsonResult jsonResult = new JsonResult();
    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    // 校验k值
    String sign2 = EncryptUtil
        .hmacSign(btcKey + coinType + btcNum + pacecurrecy, exApiApply.getAccessKey());
    if (sign1 == null || !sign2.equals(sign1)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("sign错误");
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      return jsonResult;
    }

    if (exApiApply == null || exApiApply.equals("")) {
      jsonResult.setSuccess(false);
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      jsonResult.setMsg("key无效");
      return jsonResult;
    }
    Long customerId = exApiApply.getCustomerId();
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    User user = remoteManageService.selectByustomerId(customerId);

    boolean Phone_condition = true;
    boolean Google_condition = true;
    //手机验证（北京那边沟通后，去掉短信验证码判断）
				/*if(user.getPhoneState()==1&&(valicode==null||valicode.equals(""))){
					return new JsonResult().setSuccess(false).setObj(user);
				}*/
				/*if(user.getPhoneState()==1){
					String session_pwSmsCode = redisService.get("SMS:smsphone:"+user.getPhone());
					//String session_pwSmsCode = (String) request.getSession().getAttribute("accountpwSmsCode");
					if(!valicode.equals(session_pwSmsCode)){
						Phone_condition=false;
						if(user.getPhone_googleState()!=1){
						return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error","zh_CN"));}
					}
				}*/
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
    return null;
  }

  //提币方法
  public JsonResult addCoin1(RedisService redisService, User user, User selectByTel,
      String coinType, String btcNum, String btcKey, String pacecurrecy) {

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
            .setMsg("申请提币成功").setCode(APICodeConstant.CODE_SUCCESS);
      } else {
        return new JsonResult().setSuccess(false)
            .setMsg(SpringContextUtil.diff(remoteResult.getMsg().toString(), "zh_CN"));
      }
    } else {
      //未实名和需要实名才可以交易且未实名
      if (user != null && user != null) {
        if (selectByTel.getStates() == 0) {
          return new JsonResult().setSuccess(false).setMsg("实名认证正在审核，请耐心等待");
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
          if (remoteResult.getSuccess()) {
            return new JsonResult().setSuccess(true)
                .setMsg("提币申请成功").setCode(APICodeConstant.CODE_SUCCESS);
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

  //转币接口
	/*@RequestMapping(value = "/appSendFrom", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public JsonResult TransferCurrency(String accesskey,String sign1,String fromUsername,String toUsername, BigDecimal count, String coinCode){
		
		JsonResult jsonResult = new JsonResult();
		RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
		ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
		// 校验k值
		String sign2 = EncryptUtil.hmacSign(fromUsername+toUsername+coinCode+count,exApiApply.getAccessKey());
		if (sign1 == null || !sign2.equals(sign1)) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg("sign错误");
			jsonResult.setCode(APICodeConstant.CODE_FAILED);
			return jsonResult;
		}
			
		if (exApiApply == null || exApiApply.equals("")) {
			jsonResult.setSuccess(false);
			jsonResult.setCode(APICodeConstant.CODE_FAILED);
			jsonResult.setMsg("key无效");
			return jsonResult;
		}
		
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		User toUser = remoteManageService.selectByTel(toUsername);
		User fromUser = remoteManageService.selectByTel(fromUsername);
		String string = remoteApiService.TransferCurrency(fromUser.getCustomerId(), toUser.getCustomerId(), count, coinCode, "123");
		jsonResult = JSONObject.parseObject(string, JsonResult.class);
		return jsonResult;
		
	
	}*/

  //指定撤销
  @RequestMapping(value = "/singleRevocation", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult singleRevocation(String accesskey, String sign1, HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();

    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    String entrustNum = request.getParameter("entrustNum");

    // 校验k值
    String sign2 = EncryptUtil.hmacSign(entrustNum, exApiApply.getAccessKey());
    if (sign1 == null || !sign2.equals(sign1)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("sign错误");
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      return jsonResult;
    }

    if (exApiApply == null || exApiApply.equals("")) {
      jsonResult.setSuccess(false);
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      jsonResult.setMsg("key无效");
      return jsonResult;
    }

    RemoteManageService remoteManageService = SpringContextUtil
        .getBean("remoteManageService");

    Entrust exEntrust = remoteApiService.selectExEntrust(entrustNum);
    if (exEntrust == null) {
      jsonResult.setMsg("委托单没有找到");
      jsonResult.setCode(APICodeConstant.CODE_Not_Found_Entrust);
      jsonResult.setSuccess(false);
      return jsonResult;
    }
    EntrustTrade entrustTrade = new EntrustTrade();
    entrustTrade.setEntrustNum(exEntrust.getEntrustNum());//委托单号
    entrustTrade.setCoinCode(exEntrust.getCoinCode());//交易对
    entrustTrade.setType(exEntrust.getType());//买卖类型
    entrustTrade.setFixPriceCoinCode(exEntrust.getFixPriceCoinCode());//法币
    entrustTrade.setEntrustPrice(exEntrust.getEntrustPrice());//交易价格
    remoteManageService.cancelExEntrust(entrustTrade);
    jsonResult.setSuccess(true);
    jsonResult.setMsg(SpringContextUtil.diff("revoke_success", "zh_CN"));
    jsonResult.setCode(APICodeConstant.CODE_SUCCESS);
    return jsonResult;
  }


  //全部撤销保留最新多少条
  @RequestMapping(value = "/cancelCustAllExEntrustKeepNewN")
  @ResponseBody
  public JsonResult cancelCustAllExEntrustKeepNewN(String accesskey, String sign1,
      HttpServletRequest request) {
    String language = (String) request.getAttribute("language");
    JsonResult jsonResult = new JsonResult();
    RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
    ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
    String coinCode = request.getParameter("coinCode");
    String cancelKeepN = request.getParameter("cancelKeepN");
    String type = request.getParameter("type");
    String[] split = coinCode.split("_");

    // 校验k值
    String sign2 = EncryptUtil.hmacSign(cancelKeepN + coinCode + type, exApiApply.getAccessKey());
    if (sign1 == null || !sign2.equals(sign1)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("sign错误");
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      return jsonResult;
    }

    if (exApiApply == null || exApiApply.equals("")) {
      jsonResult.setSuccess(false);
      jsonResult.setCode(APICodeConstant.CODE_FAILED);
      jsonResult.setMsg("key无效");
      return jsonResult;
    }

    Long fromCustomerId = exApiApply.getCustomerId();

    EntrustTrade entrustTrade = new EntrustTrade();
    entrustTrade.setCoinCode(split[0]);
    entrustTrade.setFixPriceCoinCode(split[1]);
    entrustTrade.setCustomerId(fromCustomerId);
    if (!StringUtil.isEmpty(type)) {
      entrustTrade.setType(Integer.valueOf(type));
    }
    if (!StringUtil.isEmpty(cancelKeepN)) {
      entrustTrade.setCancelKeepN(Integer.valueOf(cancelKeepN));
    }

    RemoteManageService remoteManageService = SpringContextUtil
        .getBean("remoteManageService");
    remoteManageService.cancelCustAllExEntrust(entrustTrade);
    jsonResult.setSuccess(true);
    jsonResult.setMsg("撤销成功");
    jsonResult.setCode(APICodeConstant.CODE_SUCCESS);
    return jsonResult;

  }
}
