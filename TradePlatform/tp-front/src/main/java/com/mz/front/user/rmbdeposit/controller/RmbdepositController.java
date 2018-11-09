package com.mz.front.user.rmbdeposit.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.AppConfig;
import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.AppTransactionManage;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.UserInfo;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.util.SessionUtils;
import com.mz.util.sys.SpringContextUtil;;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * RechargeController.java
 *
 * @author denghf 2017年7月10日 下午2:21:16
 */
@Controller
@RequestMapping(value = "/user/rmbdeposit")
public class RmbdepositController {

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

  //跳转充值页面
  @RequestMapping(value = "/selectRedisBank")
  @ResponseBody
  public JsonResult selectRedisBank() {
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String key = redisService.get("DIC:bank");
    if (key != null && !key.equals("")) {
      return new JsonResult().setSuccess(true).setObj(key);
    }
    return new JsonResult().setSuccess(false);
  }


  @RequestMapping("index")
  public ModelAndView index(HttpServletRequest request) {
    Locale locale = LocaleContextHolder.getLocale();
    ModelAndView mav = new ModelAndView();
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    User user = SessionUtils.getUser(request);
    User selectByTel = remoteManageService.selectByTel(user.getUsername());

    //查询后台参数配置
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String config = redisService.get("configCache:all");
    JSONObject parseObject = JSONObject.parseObject(config);
    String isChongbi = parseObject.get("isChongbi").toString();

    //判断是否开启强制手机认证
    String isOpenVerify = "";
    if (parseObject.get("isOpenVerify") != null) {
      isOpenVerify = parseObject.get("isOpenVerify").toString();
    }
    if (null != isOpenVerify && (!"".equals(isOpenVerify)) && isOpenVerify.equals("0")) {
      if (user.getGoogleState() == 0 && user.getPhoneState() == 0) {
        mav.setViewName("front/user/setphone");
        return mav;
      }
    }

    if (isChongbi != null && "1".equals(isChongbi)) {
      mav.setViewName("front/user/rmbdeposit");
      if (!StringUtils.isEmpty(config)) {
        mav.addObject("rechargeFeeRate", parseObject.get("rechargeFeeRate"));//充值手续费率
        mav.addObject("minRechargeMoney", parseObject.get("minRechargeMoney"));//最小充值金额
        mav.addObject("languageCode", parseObject.get("language_code"));//当前币种
      }
      //线上充值所需要的参数
      String randomStr = RandomStringUtils.random(2, false, true);
      mav.addObject("random", "0." + randomStr);
      mav.addObject("randomStr", "." + randomStr);

      //金如悦 查询后台配置 是否开启第三方充值
      RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil
          .getBean("remoteAppTransactionManageService");
      List<AppConfig> configInfo = remoteAppTransactionManageService.getConfigInfo("isOpenThird");
      if (configInfo.size() == 0) {
        mav.addObject("isOpenThird", "1");
      } else {
        String value = configInfo.get(0).getValue();
        mav.addObject("isOpenThird", value);
      }
      return mav;
    } else {
      //未实名，往实名页跳
      if (selectByTel != null) {
        if (selectByTel.getStates() == 0) {
          mav.setViewName("front/user/identity");
          return mav;
        } else if (selectByTel.getStates() == 1) {
          RemoteResult result = remoteManageService.getPersonInfo(user.getUserCode());
          if (result != null && result.getSuccess()) {
            UserInfo userInfo = (UserInfo) result.getObj();
            if ("1".equals(userInfo.getType())) {
              if ("en".equals(locale.toString())) {
                userInfo.setPapersType("ID Card");
              }
            } else if ("2".equals(userInfo.getType())) {
              if ("en".equals(locale.toString())) {
                userInfo.setPapersType("Passport");
              }
            }
            mav.addObject("info", result.getObj());
          }
          mav.setViewName("front/user/realinfo");
          return mav;
        } else if (selectByTel.getStates() == 2) {
          mav.setViewName("front/user/rmbdeposit");

          //查询后台参数配置
          if (!StringUtils.isEmpty(config)) {
            mav.addObject("rechargeFeeRate", parseObject.get("rechargeFeeRate"));//充值手续费率
            mav.addObject("minRechargeMoney", parseObject.get("minRechargeMoney"));//最小充值金额
            mav.addObject("languageCode", parseObject.get("language_code"));//当前币种
          }
          //线上充值所需要的参数
          String randomStr = RandomStringUtils.random(2, false, true);
          mav.addObject("random", "0." + randomStr);
          mav.addObject("randomStr", "." + randomStr);

          //金如悦 查询后台配置 是否开启第三方充值
          RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil
              .getBean("remoteAppTransactionManageService");
          List<AppConfig> configInfo = remoteAppTransactionManageService
              .getConfigInfo("isOpenThird");
          if (configInfo.size() == 0) {
            mav.addObject("isOpenThird", "1");
          } else {
            String value = configInfo.get(0).getValue();
            mav.addObject("isOpenThird", value);
          }
          return mav;
        } else if (selectByTel.getStates() == 3) {
          mav.setViewName("front/user/realinfono");
          return mav;
        }
      }
    }
    return mav;
  }


  //生成银行汇款单
  @RequestMapping(value = "/rmbdeposit")
  @ResponseBody
  public JsonResult rmbdeposit(String surname, String remitter, String bankCode, String bankAmount,
      String bankName, HttpServletRequest request) {
    String language = (String) request.getAttribute("language");

    bankName = (String) request.getParameter("selectedBankName");
    AppTransactionManage appTransaction = new AppTransactionManage();
    User user = SessionUtils.getUser(request);
    RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil
        .getBean("remoteAppTransactionManageService");

    if (user != null) {
      RemoteResult remoteResult = remoteAppTransactionManageService
          .generateRmbdeposit(surname, remitter, bankCode, bankAmount, bankName, appTransaction,
              user);
      if (remoteResult.getObj() != null) {

        return new JsonResult().setSuccess(true).setObj(remoteResult.getObj());
      }
      return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff(remoteResult.getMsg()));
    }
    return new JsonResult().setSuccess(false);
  }

  /**
   * 查询充值,提现记录
   */
  @RequestMapping("/list")
  @ResponseBody
  public FrontPage list(HttpServletRequest request) {
    User user = SessionUtils.getUser(request);

    String transactionType = request.getParameter("transactionType");
    String status = request.getParameter("status");

    RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil
        .getBean("remoteAppTransactionManageService");
    Map<String, String> params = HttpServletRequestUtils.getParams(request);
    params.put("customerId", user.getCustomerId().toString());
    if ("all".equals(transactionType)) {//all查全部
      params.put("transactionType", null);
    }
    if ("0".equals(status)) {//0查全部
      params.put("status", null);
    }
    return remoteAppTransactionManageService.findTransaction(params);
  }

  public static String transactionNum(String bussType) {
    SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssSSS");
    String time = format.format(new Date(System.currentTimeMillis()));
    String randomStr = RandomStringUtils.random(4, false, true);
    if (null == bussType) {
      bussType = "00";
    }
    return bussType + time + randomStr;
  }

  public static void main(String[] args) {
    String aa = "[{\"created\":1466480567000},{\"created\":146648056220},{\"created\":1466483333000}]";
    JSONArray jsona = JSONArray.parseArray(aa);
    for (int i = 0; i < jsona.size(); i++) {
      JSONObject jsono = jsona.getJSONObject(i);
      jsono.put(i + "", jsono.get("created"));
    }
  }
}
