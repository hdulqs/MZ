package com.mz.sms.remote;

import com.alibaba.fastjson.JSON;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.sms.send.model.AppSmsSend;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.core.mvc.service.AppSmsSend.AppSmsSendService;
import com.mz.manage.remote.RemoteSmsService;
import com.mz.sms.sdk.service.SdkService;
import com.mz.sms.sdk.service.impl.YunpianServiceImpl;
import java.io.IOException;
import java.util.Map;
import org.springframework.util.StringUtils;

public class RemoteSmsServiceImpl implements RemoteSmsService {

  @Override
  public void sendsmsHai(String params, String phoneType, String phone) {

    String param = params;
    LogFactory.info("发送短信，接收到的请求参数：" + param);
    SmsParam smsParam = JSON.parseObject(param, SmsParam.class);

    if (SmsSendUtil.WITHDRAW_RMBORCOIN.equals(smsParam.getHrySmstype())
        || SmsSendUtil.WITHDRAW_RMBORCOIN_FRONT.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_RMBWITHDRAW_INVALID.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_COINWITHDRAW_INVALID.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_RMBDEPOSIT_INVALID.equals(smsParam.getHrySmstype())) {
      // 汉字需要转码
      String code = smsParam.getHryCode();
      LogFactory.info("发送短信，接收到的请求参数HryCode（提现）：" + code);

      if ("BTC".equals(code)) {
        smsParam.setHryCode("比特币");
      } else if ("LTC".equals(code)) {
        smsParam.setHryCode("莱特币");
      } else if ("CRTC".equals(code)) {
        smsParam.setHryCode("联合学分");
      }
    }

    // 内部验证密码
    String smsKey = smsParam.getSmsKey();
    // 获得sendId
    Long sendId = smsParam.getSendId();
    LogFactory.info("收到短信发送请求sendId=" + sendId);
    try {
      Thread.sleep(5000);// 休息5秒

      JsonResult jsonResult = new JsonResult();
      AppSmsSendService appSmsSendService = (AppSmsSendService) ContextUtil
          .getBean("appSmsSendService");
      AppSmsSend appSmsSend = new AppSmsSend();

      // 标记为已接收到些条记录
      appSmsSend.setReceiveStatus("1");
      // 判断密钥
      if (!StringUtils.isEmpty(smsKey) && "hurongyuseen".equals(smsKey)) {
        String code = smsParam.getHryCode();
        LogFactory.info("短信验证码为:" + code);
        String serviceName = "";
        SdkService sdkService;
        if (phoneType.equals("86")) {
          serviceName = PropertiesUtils.APP.getProperty("app.smsServiceName");
        } else {
          serviceName = PropertiesUtils.APP.getProperty("app.smsHaiName");

        }
        if (!StringUtils.isEmpty(serviceName)) {
          sdkService = (SdkService) ContextUtil.getBean(serviceName);
        } else {
          sdkService = (SdkService) ContextUtil.getBean("sdkService");
        }

        //设置短信发送类型
        appSmsSend.setType(SmsSendUtil.getSendTypeValue(smsParam.getHrySmstype()));
        appSmsSend.setPostParam(JSON.toJSONString(smsParam));
        boolean sendSmsHai = sdkService.sendSmsHai(appSmsSend, smsParam, phone);
        //boolean sendSms = sdkService.sendSmsHai(appSmsSend, smsParam,phone);
        // 如果发送标记为成功,则标记为已发送
        if (sendSmsHai) {
          appSmsSend.setSendStatus("1");
        }
        jsonResult.setSuccess(sendSmsHai);
      } else {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("非法请求，密码不正确");
      }
      appSmsSendService.save(appSmsSend);


    } catch (InterruptedException | IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void sendsms(String params, String phone) {

    String param = params;
    LogFactory.info("发送短信，接收到的请求参数：" + param);
    SmsParam smsParam = JSON.parseObject(param, SmsParam.class);

    if (SmsSendUtil.WITHDRAW_RMBORCOIN.equals(smsParam.getHrySmstype())
        || SmsSendUtil.WITHDRAW_RMBORCOIN_FRONT.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_RMBWITHDRAW_INVALID.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_COINWITHDRAW_INVALID.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_RMBDEPOSIT_INVALID.equals(smsParam.getHrySmstype())) {
      // 汉字需要转码
      String code = smsParam.getHryCode();
      LogFactory.info("发送短信，接收到的请求参数HryCode（提现）：" + code);

      if ("BTC".equals(code)) {
        smsParam.setHryCode("比特币");
      } else if ("LTC".equals(code)) {
        smsParam.setHryCode("莱特币");
      } else if ("CRTC".equals(code)) {
        smsParam.setHryCode("联合学分");
      }
    }

    // 内部验证密码
    String smsKey = smsParam.getSmsKey();
    String hrySmstype = smsParam.getHrySmstype();
    // 获得sendId
    Long sendId = smsParam.getSendId();
    LogFactory.info("收到短信发送请求sendId=" + sendId);
    try {
      Thread.sleep(5000);// 休息5秒

      JsonResult jsonResult = new JsonResult();
      AppSmsSendService appSmsSendService = (AppSmsSendService) ContextUtil
          .getBean("appSmsSendService");
      AppSmsSend appSmsSend = new AppSmsSend();

      // 标记为已接收到些条记录
      appSmsSend.setReceiveStatus("1");
      // 判断密钥
      if (!StringUtils.isEmpty(smsKey) && "hurongyuseen".equals(smsKey)) {
        String code = smsParam.getHryCode();
        LogFactory.info("短信验证码为:" + code);
        String serviceName = "";
        SdkService sdkService;

        serviceName = PropertiesUtils.APP.getProperty("app.smsServiceName");

        if (!StringUtils.isEmpty(serviceName)) {
          sdkService = (SdkService) ContextUtil.getBean(serviceName);
        } else {
          sdkService = (SdkService) ContextUtil.getBean("sdkService");
        }

        //设置短信发送类型
        appSmsSend.setType(SmsSendUtil.getSendTypeValue(hrySmstype));
        appSmsSend.setPostParam(JSON.toJSONString(smsParam));
        boolean sendSms = sdkService.sendSms(appSmsSend, smsParam, phone);
        // 如果发送标记为成功,则标记为已发送
        if (sendSms) {
          appSmsSend.setSendStatus("1");
        }
        jsonResult.setSuccess(sendSms);
      } else {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("非法请求，密码不正确");
      }
      appSmsSendService.save(appSmsSend);


    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


  public void sendsms(String params, String phone, Map<String, Object> map) {
    String param = params;
    LogFactory.info("发送短信，接收到的请求参数：" + param);
    SmsParam smsParam = JSON.parseObject(param, SmsParam.class);

    if (SmsSendUtil.WITHDRAW_RMBORCOIN.equals(smsParam.getHrySmstype())
        || SmsSendUtil.WITHDRAW_RMBORCOIN_FRONT.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_RMBWITHDRAW_INVALID.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_COINWITHDRAW_INVALID.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_RMBDEPOSIT_INVALID.equals(smsParam.getHrySmstype())) {
      // 汉字需要转码
      String code = smsParam.getHryCode();
      LogFactory.info("发送短信，接收到的请求参数HryCode（提现）：" + code);

      if ("BTC".equals(code)) {
        smsParam.setHryCode("比特币");
      } else if ("LTC".equals(code)) {
        smsParam.setHryCode("莱特币");
      } else if ("CRTC".equals(code)) {
        smsParam.setHryCode("联合学分");
      }
    }

    // 内部验证密码
    String smsKey = smsParam.getSmsKey();
    String hrySmstype = smsParam.getHrySmstype();
    // 获得sendId
    Long sendId = smsParam.getSendId();
    LogFactory.info("收到短信发送请求sendId=" + sendId);
    try {
      Thread.sleep(5000);// 休息5秒

      JsonResult jsonResult = new JsonResult();
      AppSmsSendService appSmsSendService = (AppSmsSendService) ContextUtil
          .getBean("appSmsSendService");
      AppSmsSend appSmsSend = new AppSmsSend();

      // 标记为已接收到些条记录
      appSmsSend.setReceiveStatus("1");
      // 判断密钥
      if (!StringUtils.isEmpty(smsKey) && "hurongyuseen".equals(smsKey)) {
        String code = smsParam.getHryCode();
        LogFactory.info("短信验证码为:" + code);
        //创建一个云片对象
        YunpianServiceImpl serviceImpl = new YunpianServiceImpl();
        //设置短信发送类型
        appSmsSend.setType(SmsSendUtil.getSendTypeValue(hrySmstype));
        appSmsSend.setPostParam(JSON.toJSONString(smsParam));
        boolean sendSms = serviceImpl.sendSms(appSmsSend, smsParam, phone, map);
        // 如果发送标记为成功,则标记为已发送
        if (sendSms) {
          appSmsSend.setSendStatus("1");
        }
        jsonResult.setSuccess(sendSms);
      } else {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("非法请求，密码不正确");
      }
      appSmsSendService.save(appSmsSend);


    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }


  public void sendsmsHai(String params, String phoneType, String phone, Map<String, Object> map) {

    String param = params;
    LogFactory.info("发送短信，接收到的请求参数：" + param);
    SmsParam smsParam = JSON.parseObject(param, SmsParam.class);

    if (SmsSendUtil.WITHDRAW_RMBORCOIN.equals(smsParam.getHrySmstype())
        || SmsSendUtil.WITHDRAW_RMBORCOIN_FRONT.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_RMBWITHDRAW_INVALID.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_COINWITHDRAW_INVALID.equals(smsParam.getHrySmstype())
        || SmsSendUtil.SMS_RMBDEPOSIT_INVALID.equals(smsParam.getHrySmstype())) {
      // 汉字需要转码
      String code = smsParam.getHryCode();
      LogFactory.info("发送短信，接收到的请求参数HryCode（提现）：" + code);

      if ("BTC".equals(code)) {
        smsParam.setHryCode("比特币");
      } else if ("LTC".equals(code)) {
        smsParam.setHryCode("莱特币");
      } else if ("CRTC".equals(code)) {
        smsParam.setHryCode("联合学分");
      }
    }

    // 内部验证密码
    String smsKey = smsParam.getSmsKey();
    // 获得sendId
    Long sendId = smsParam.getSendId();
    LogFactory.info("收到短信发送请求sendId=" + sendId);
    try {
      Thread.sleep(5000);// 休息5秒

      JsonResult jsonResult = new JsonResult();
      AppSmsSendService appSmsSendService = (AppSmsSendService) ContextUtil
          .getBean("appSmsSendService");
      AppSmsSend appSmsSend = new AppSmsSend();

      // 标记为已接收到些条记录
      appSmsSend.setReceiveStatus("1");
      // 判断密钥
      if (!StringUtils.isEmpty(smsKey) && "hurongyuseen".equals(smsKey)) {
        String code = smsParam.getHryCode();
        LogFactory.info("短信验证码为:" + code);
        YunpianServiceImpl serviceImpl = new YunpianServiceImpl();
        //设置短信发送类型
        appSmsSend.setType(SmsSendUtil.getSendTypeValue(smsParam.getHrySmstype()));
        appSmsSend.setPostParam(JSON.toJSONString(smsParam));
        boolean sendSmsHai = serviceImpl.sendSmsHai(appSmsSend, smsParam, phone, map);
        // 如果发送标记为成功,则标记为已发送
        if (sendSmsHai) {
          appSmsSend.setSendStatus("1");
        }
        jsonResult.setSuccess(sendSmsHai);
      } else {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("非法请求，密码不正确");
      }
      appSmsSendService.save(appSmsSend);


    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * @Description: 根据系统代码获取手机模板发送短信 syscode 系统代码  #替换字# map
   * @Author: zongwei
   * @CreateDate: 2018/6/11 11:48
   * @UpdateUser: zongwei
   * @UpdateDate: 2018/6/11 11:48
   * @UpdateRemark: 创建
   * @Version: 1.0
   */
  public void sendSmsInfo(String params, String phoneType, String phone, String syscode,
      Map<String, Object> map) {

    String param = params;
    LogFactory.info("发送短信，接收到的请求参数：" + param);
    SmsParam smsParam = JSON.parseObject(param, SmsParam.class);

    // 内部验证密码
    String smsKey = smsParam.getSmsKey();
    // 获得sendId
    Long sendId = smsParam.getSendId();
    LogFactory.info("收到短信发送请求sendId=" + sendId);
    try {
      Thread.sleep(5000);// 休息5秒

      JsonResult jsonResult = new JsonResult();
      AppSmsSendService appSmsSendService = (AppSmsSendService) ContextUtil
          .getBean("appSmsSendService");
      AppSmsSend appSmsSend = new AppSmsSend();

      // 标记为已接收到些条记录
      appSmsSend.setReceiveStatus("1");
      // 判断密钥
      if (!StringUtils.isEmpty(smsKey) && "hurongyuseen".equals(smsKey)) {
        String serviceName = "";
        SdkService sdkService;

        serviceName = PropertiesUtils.APP.getProperty("app.smsServiceName");

        if (!StringUtils.isEmpty(serviceName)) {
          sdkService = (SdkService) ContextUtil.getBean(serviceName);
        } else {
          sdkService = (SdkService) ContextUtil.getBean("sdkService");
        }
        //设置短信发送类型
        appSmsSend.setType(SmsSendUtil.getSendTypeValue(smsParam.getHrySmstype()));
        appSmsSend.setPostParam(JSON.toJSONString(smsParam));
        boolean sendSmsHai = sdkService.sendSmsInfo(appSmsSend, smsParam, phone, syscode, map);
        // 如果发送标记为成功,则标记为已发送
        if (sendSmsHai) {
          appSmsSend.setSendStatus("1");
        }
        jsonResult.setSuccess(sendSmsHai);
      } else {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("非法请求，密码不正确");
      }
      appSmsSendService.save(appSmsSend);


    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
