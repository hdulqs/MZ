package com.mz.sms.sdk.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.redis.common.utils.RedisService;
import com.mz.sms.send.model.AppSmsSend;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.mz.sms.sdk.service.SdkService;
import com.mz.web.remote.RemoteAppConfigService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * TwilioServiceImpl.java
 *
 * @author denghf 2017年10月19日 下午6:44:51
 */
@Service("twilioServiceImpl")
public class TwilioServiceImpl implements SdkService {

  public static final String ACCOUNT_SID = "AC7c874671d1793d85f2e58b0bf6098ce1";
  public static final String AUTH_TOKEN = "73c336502b14c2502e1612e29242c1d6";

  @Override
  public boolean sendSms(AppSmsSend appSmsSend, SmsParam smsParam, String phone) {
    //根据短信类型获得短信模板
    RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil
        .getBean("remoteAppConfigService");
    String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
    if ("0".equals(smsOpen)) {//接口是否开启
      String value = remoteAppConfigService.getValueByKey(smsParam.getHrySmstype());
      String sendContent = SmsSendUtil.replaceKey(value, smsParam);
      String username = PropertiesUtils.APP.getProperty("app.sms.hxusername");
      String pwd = PropertiesUtils.APP.getProperty("app.sms.hxpassword");
      try {
        Twilio.init(username, pwd);

        Message message = Message
            .creator(new PhoneNumber("+86" + phone), // to +8615926553196
                new PhoneNumber("+12153302422"), // from +12153302422
                sendContent)
            .create();

        System.out.println("短信发送成功：" + message.getSid());
      } catch (Exception e) {
        e.printStackTrace();
        appSmsSend.setThirdPartyResult("【系统短信功能异常】");
      }
    } else {
      appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
      return false;
    }
    return false;
  }

  @Override
  public JsonResult checkCard(String name, String idCard) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean sendSmsHai(AppSmsSend appSmsSend, SmsParam smsParam, String Phone)
      throws IOException {
    //根据短信类型获得短信模板
    RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil
        .getBean("remoteAppConfigService");
    String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
    if ("0".equals(smsOpen)) {//接口是否开启
      String value = remoteAppConfigService.getValueByKey(smsParam.getHrySmstype());
      String sendContent = SmsSendUtil.replaceKey(value, smsParam);
      String username = PropertiesUtils.APP.getProperty("app.sms.hxusername");
      String pwd = PropertiesUtils.APP.getProperty("app.sms.hxpassword");

      try {
        Twilio.init(username, pwd);

        Message message = Message
            .creator(new PhoneNumber(Phone), // to +8615926553196
                new PhoneNumber("+12153302422"), // from +12153302422
                sendContent)
            .create();

        System.out.println(message.getSid());
      } catch (Exception e) {
        e.printStackTrace();
        appSmsSend.setThirdPartyResult("【系统短信功能异常】");
      }
    } else {
      appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
      return false;
    }
    return false;
  }


  public static void main(String[] args) {
    System.out.println("发短信开始啦");
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

    Message message = Message
        .creator(new PhoneNumber("+861371873073"), // to +8615926553196
            new PhoneNumber("+12153302422"), // from +12153302422
            "Where's Wallace?")
        .create();

    System.out.println(message.getSid());
    System.out.println("发短信结束了啦");
  }

  @Override
  public boolean sendSmsInfo(AppSmsSend appSmsSend, SmsParam smsParam, String Phone, String syscode,
      Map<String, Object> map) {
    //根据短信类型获得短信模板
    RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil
        .getBean("remoteAppConfigService");
    String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
    if ("0".equals(smsOpen)) {//接口是否开启
      RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
      String sysCodeValueLists = redisService.get("cn:SysCodeValueList");
      String value = null;
      if (!StringUtils.isEmpty(sysCodeValueLists)) {
        List<com.mz.spotchange.model.SysCodeValue> sysCodeValuelist = JSONArray
            .parseArray(sysCodeValueLists, com.mz.spotchange.model.SysCodeValue.class);
        for (com.mz.spotchange.model.SysCodeValue sysCodeValue : sysCodeValuelist) {
          if (sysCodeValue.getCode().equals(syscode)) {
            value = sysCodeValue.getValue();
          }
        }
      }
      if (value == null) {
        appSmsSend.setReceiveStatus("0");
        appSmsSend.setThirdPartyResult("系统代码获取不到短信模板！");
        return false;
      }
      String sendContent = this.replaceKey(value, map);
      String username = PropertiesUtils.APP.getProperty("app.sms.hxusername");
      String pwd = PropertiesUtils.APP.getProperty("app.sms.hxpassword");

      try {
        Twilio.init(username, pwd);

        Message message = Message
            .creator(new PhoneNumber(Phone), // to +8615926553196
                new PhoneNumber("+12153302422"), // from +12153302422
                sendContent)
            .create();

        System.out.println(message.getSid());
      } catch (Exception e) {
        e.printStackTrace();
        appSmsSend.setThirdPartyResult("【系统短信功能异常】");
      }
    } else {
      appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
      return false;
    }
    return false;
  }

  /**
   * 替换模板里面的值
   */
  public static String replaceKey(String value, Map<String, Object> map) {
    String[] split = value.split("#");
    String string = "";
    if (map != null) {
      for (int i = 0; i < split.length; i++) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
          if (split[i].equals(entry.getKey())) {
            split[i] = (String) entry.getValue();
          }

        }
        string += split[i];
      }
    }
    return string;
  }
}
