package com.mz.sms.sdk.service.impl;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.redis.common.utils.RedisService;
import com.mz.sms.sdk.service.SdkService;
import com.mz.sms.send.model.AppSmsSend;
import com.mz.sms.utils.hx.HttpSend;
import com.mz.util.StringUtil;
import com.mz.util.file.Md5Util;
import com.mz.util.sys.ContextUtil;
import com.mz.web.remote.RemoteAppConfigService;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service("wuminService")
public class WuMingServiceImpl implements SdkService {
    //提交地址
    private static final String INTERNATIONAL_URL="http://47.107.152.184:8372/api/sendmsg";
    private static final String APP_ID = "LCskF9rQQsRqPCIDws4l3R1ncjKf3h";
    // private static final String APP_KEY = "I1UHQ3YcP4HbZPmGZSxB3awPL7jFwtjdiyVLqnuJ";

    private static final Logger log = Logger.getLogger(WuMingServiceImpl.class);

    @Override
    public boolean sendSms(AppSmsSend appSmsSend, SmsParam smsParam,String phone) {
        //根据短信类型获得短信模板
        RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
        String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
        String apiKey = remoteAppConfigService.getValueByKey("sms_apiKey");
        if("0".equals(smsOpen)){//接口是否开启
            String value = remoteAppConfigService.getValueByKey("sms_take_phone");
            return sendSMS(appSmsSend, smsParam, phone, value, APP_ID, apiKey);
        }else{
            appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
            return false;
        }
    }

    @Override
    public JsonResult checkCard(String name, String idCard) {
        return null;
    }

    @Override
    public boolean sendSmsHai(AppSmsSend appSmsSend, SmsParam smsParam, String phone) {
        //根据短信类型获得短信模板
        RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
        String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
        String apiKey = remoteAppConfigService.getValueByKey("sms_apiKey");
        if("0".equals(smsOpen)){//接口是否开启
            String templateid = remoteAppConfigService.getValueByKey(smsParam.getHrySmstype());
            return sendSMS(appSmsSend, smsParam, phone, templateid, APP_ID, apiKey);
        }else{
            appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
            return false;
        }
    }

    // private static void


    public static void main(String[] args) throws NoSuchAlgorithmException {
        Map<String, String> params = new HashMap<String, String>();
        String mobile = "0000000000000";
        String code = "1234";
        String templateid = "1";
        String app_key = "I1UHQ3YcP4HbZPmGZSxB3awPL7jFwtjdiyVLqnuJ";

        params.put("mobile", mobile);
        params.put("code", code);
        params.put("msgid", APP_ID);
        params.put("templateid", "1");
        params.put("sign", Md5Util.encryption(mobile+code+ templateid +app_key));
        String sendSms=HttpSend.yunpianPost(INTERNATIONAL_URL, params);
        System.out.println(sendSms);
    }

    private boolean sendSMS(AppSmsSend appSmsSend, SmsParam smsParam, String phone, String templateid, String api_id, String api_key) {
        Map<String, String> params = new HashMap<>();
        String sercretCode = smsParam.getHryCode();
        if (phone.startsWith("+")) {
            if (phone.length() > 11) {
                phone = phone.substring(phone.length() - 11);
            } else {
                return false;
            }
        }


        try {
            Integer.valueOf(templateid);
        } catch (NumberFormatException e) {
            appSmsSend.setReceiveStatus("422");
            appSmsSend.setSendContent("使用的短信模板id：<" + templateid + ">验证码:" + sercretCode);
            appSmsSend.setThirdPartyResult("模板错误，模板请使用数字");
            templateid = "1";
        }

        params.put("mobile", phone);
        params.put("code", sercretCode);
        params.put("msgid", api_id);
        params.put("templateid", templateid);
        try {
            params.put("sign", Md5Util.encryption(phone + sercretCode + templateid + api_key));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        String sendSms = HttpSend.yunpianPost(INTERNATIONAL_URL, params);

        //修改为您要发送的手机号
        if (sendSms != null) {
            System.out.println(sendSms);
            int statusCode = 0;
            String message = null;
            try {
                JSONObject myJsonObject = new JSONObject(sendSms);
                statusCode = myJsonObject.getInt("status_code");
                message = myJsonObject.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (statusCode == 600) {
                appSmsSend.setReceiveStatus("1");
                appSmsSend.setSendContent("使用的短信模板id：<" + templateid + ">验证码:" + sercretCode);
                appSmsSend.setThirdPartyResult(appSmsSend.getThirdPartyResult() + "发送成功！");
                return true;
            } else if (statusCode == 422) {
                appSmsSend.setReceiveStatus("" + statusCode);
                appSmsSend.setSendContent("使用的短信模板id：<" + templateid + ">验证码:" + sercretCode);
                appSmsSend.setThirdPartyResult(appSmsSend.getThirdPartyResult() + sendSms + "/" + message + "/调用API时发生错误，需要开发者进行相应的处理。");
                return false;
            }
        } else {
            appSmsSend.setThirdPartyResult("发送失败，调用接口异常！");
            appSmsSend.setSendContent("使用的短信模板id：<" + templateid + ">验证码:" + sercretCode);
            appSmsSend.setThirdPartyResult(appSmsSend.getThirdPartyResult() + sendSms + "/调用API时发生错误，需要开发者进行相应的处理。");
            return false;
        }
        return false;
    }

    /**
     * @Description:    根据系统代码获取短信模板
     * @Author:         zongwei
     * @CreateDate:     2018/6/11 10:35
     * @UpdateUser:    zongwei
     * @UpdateDate:     2018/6/11 10:35
     * @UpdateRemark:   创建
     * @Version:        1.0
     */
    public synchronized boolean sendSmsInfo(AppSmsSend appSmsSend, SmsParam smsParam, String phone,String syscode,Map<String, Object> map) {
        RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
        RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
        String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
        String apiKey = remoteAppConfigService.getValueByKey("sms_apiKey");
        if("0".equals(smsOpen)) {
            String value = remoteAppConfigService.getValueByKey(smsParam.getHrySmstype());

            return sendSMS(appSmsSend, smsParam, phone, value, APP_ID, apiKey);
        }
        appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
        return false;
    }

}
