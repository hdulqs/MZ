package com.mz.front.mobile.user;

import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.User;
import com.mz.util.IpUtil;
import com.mz.util.sys.SpringContextUtil;;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Controller
@RequestMapping("/mobile/sms")
@Api(value = "短信发送操作类", description = "短信发送")
public class AppSmsController {

  private final static Logger log = Logger.getLogger(AppSmsController.class);

  private String tel = "tel:";

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
   *
   *
   * @param request
   * @return
   */
  @RequestMapping(value = "/smsPhone", method = RequestMethod.POST)
  @ApiOperation(value = "发送短信", httpMethod = "POST", response = JsonResult.class, notes = "发送短信")
  @ResponseBody
  public JsonResult updateBankSmsPhone(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String phone = "";
    phone = request.getParameter("phone");
    String tokenId = request.getParameter("tokenId");
    String type = request.getParameter("type");
    String country = request.getParameter("country");
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value == null) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("请登录或重新登录！");
      return jsonResult;
    }
    String tel = JSONObject.parseObject(value).getString("mobile");
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    User user = remoteManageService.selectByTel(tel);

    String mobile = user.getPhone();
    if (mobile == null) {
      mobile = country + " " + phone;
    }

    if (StringUtils.isEmpty(mobile)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("手机号码为空！");
      return jsonResult;
    }
    String mobi = mobile.replace(" ", "");
    // 手机号
    phone = mobile.substring(mobile.lastIndexOf(" ") + 1);

    // 地区截取
    String address = mobile.substring(0, mobile.indexOf(" "));

    // 设置短信验证码到session中
    SmsParam smsParam = new SmsParam();
    smsParam.setHryMobilephone(mobile);

    if (address.equals("+86")) {
      smsParam.setHrySmstype(SmsSendUtil.TAKE_PHONE);
      String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, address, phone);
      if (null != type && "change".equals(type)) {
        redisService.save("genghuan" + phone, sendSmsCodeHai, 120);
      }
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


}
