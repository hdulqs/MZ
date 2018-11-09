package com.mz.front.mobile.user;

import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.redis.common.utils.RedisService;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.AppBankCardManage;
import com.mz.manage.remote.model.AppTransactionManage;
import com.mz.manage.remote.model.MyAccountTO;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.util.GoogleAuthenticatorUtil;
import com.mz.util.sys.SpringContextUtil;;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mobile/user/apprmbwithdraw")
@Api(value = "App操作类", description = "人民币提现")
public class AppRmbwithdrawController {

  private final static Logger log = Logger.getLogger(AppRmbwithdrawController.class);

  @RequestMapping(value = "/selectAll", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "获取可用余额、银行卡", httpMethod = "POST", response = JsonResult.class, notes = "获取可用余额、银行卡")
  @ResponseBody
  public JsonResult selectAll(HttpServletRequest request) {
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        MyAccountTO myAccount = remoteManageService.myAccount(user.getCustomerId());

        RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil
            .getBean("remoteAppTransactionManageService");
        List<AppBankCardManage> list = remoteAppBankCardManageService
            .findByCustomerId(user.getCustomerId());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("telphone", user.getUsername());
        map.put("hotMoney", myAccount.getHotMoney());
        map.put("list", list);
        return new JsonResult().setSuccess(true).setObj(map);
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping(value = "/getRmbWithdrawCode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "提现短信验证码", httpMethod = "POST", response = JsonResult.class, notes = "提现短信验证码")
  @ResponseBody
  public JsonResult getRmbWithdrawCode(HttpServletRequest request,
      @RequestParam String transactionMoney,
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
        if (StringUtils.isEmpty(transactionMoney)) {
          jsonResult.setSuccess(false);
          jsonResult.setSuccess(false).setMsg("提现金额不能为空");
        } else if (StringUtils.isEmpty(accountPassWord)) {
          jsonResult.setSuccess(false);
          jsonResult.setSuccess(false).setMsg("交易密码不能为空");
        } else {
          // 设置短信验证码到session中
          SmsParam smsParam = new SmsParam();
          smsParam.setHryMobilephone(user.getUsername());
          smsParam.setHrySmstype(SmsSendUtil.TAKE_MONEY);

          redisService
              .save("withdrawCode:" + tel, SmsSendUtil.sendSmsCode(smsParam, null, null), 120);

          jsonResult.setSuccess(true);
          jsonResult.setMsg("短信发送成功!");
          return jsonResult;
        }
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping(value = "/rmbwithdraw", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "处理提现", httpMethod = "POST", response = JsonResult.class, notes = ""
      + "选中银行卡bankCardId传ID,交易金额transactionMoney,手续费 fee")
  @ResponseBody
  public JsonResult rmbwithdraw(HttpServletRequest request, HttpServletResponse response,
      @RequestParam String transactionMoney, @RequestParam String fee,
      @RequestParam String bankCardId) {
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    String valicode = request.getParameter("valicode");
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        boolean Phone_condition = true;
        boolean Google_condition = true;
        //手机验证
        if (user.getPhoneState() == 1 && (valicode == null || valicode.equals(""))) {
          return new JsonResult().setSuccess(false).setObj(user);
        }
        if (user.getPhoneState() == 1) {
          String session_pwSmsCode = redisService.get("SMS:smsphone:" + user.getPhone());
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
        AppTransactionManage appTransaction = new AppTransactionManage();
        // 交易金额
        appTransaction.setTransactionMoney(new BigDecimal(transactionMoney));
        // 手续费
        appTransaction.setFee(new BigDecimal(fee));
        /*
         * if(StringUtils.isEmpty(withdrawCode)){ return new
         * JsonResult().setMsg("短信验证码不正确!"); }
         * if(!withdrawCode.equals(accountpwSmsCode)){ return new
         * JsonResult().setMsg("短信验证码不正确!"); }
         */

        RemoteResult remoteResult = null;
        try {
          if (user != null) {
            RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil
                .getBean("remoteAppTransactionManageService");
            // 临时将验证码设置为1234
            String withdrawCode = "1234";
            String accountpwSmsCode = "1234";
            remoteResult = remoteAppTransactionManageService
                .rmbwithdraw("1234", bankCardId, withdrawCode,
                    accountpwSmsCode, user, "1234", appTransaction);
            return new JsonResult().setSuccess(remoteResult.getSuccess())
                .setMsg(SpringContextUtil.diff(remoteResult.getMsg(), "zh_CN"))
                .setObj(remoteResult.getObj());

          }
        } catch (Exception e) {
          log.error("远程调用出错");
          return new JsonResult().setMsg("远程调用出错");
        }
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping(value = "/witfee", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "提现手续费费率", httpMethod = "POST", response = JsonResult.class, notes = "提现手续费费率witfee,当天最多提现金额maxWithdrawMoney,单笔最多提现金额(元)maxWithdrawMoneyOneTime,法币类型languageCode")
  @ResponseBody
  public JsonResult witfee(HttpServletRequest request) {
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    Map<String, Object> map = new HashMap<String, Object>();
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        //查询后台参数配置
        String config = redisService.get("configCache:all");
        if (!StringUtils.isEmpty(config)) {
          JSONObject parseObject = JSONObject.parseObject(config);
          map.put("maxWithdrawMoney", parseObject.get("maxWithdrawMoney"));//当天最多提现金额
          map.put("maxWithdrawMoneyOneTime",
              parseObject.get("maxWithdrawMoneyOneTime"));//单笔最多提现金额(元)
          String languageCode = (String) parseObject.get("language_code");//法币类型
          map.put("languageCode", languageCode);
          //获取提现手续费费率
          RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil
              .getBean("remoteAppTransactionManageService");
          BigDecimal witfee = remoteAppTransactionManageService.witfee();
          map.put("witfee", witfee);
        }

        return new JsonResult().setSuccess(true).setObj(map);
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }
}
