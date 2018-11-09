/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年5月18日 下午2:25:52
 */
package com.mz.sms.sdk.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.redis.common.utils.RedisService;
import com.mz.sms.send.model.AppSmsSend;
import com.mz.sms.utils.hx.SmsHxUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.sms.sdk.service.SdkService;
import com.mz.sms.utils.juhe.JuheUtils;
import com.mz.web.remote.RemoteAppConfigService;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年5月18日 下午2:25:52 
 */
@Service("sdkService")
public class SdkServiceImpl implements SdkService{
	

	
	@Override
	public boolean sendSms(AppSmsSend appSmsSend,SmsParam smsParam,String phone) {
		
		//根据短信类型获得短信模板
		RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
		String hxrt_username = remoteAppConfigService.getValueByKey("sms_username");
		String hxrt_password = remoteAppConfigService.getValueByKey("sms_password");
		if("0".equals(smsOpen)){
			String value = remoteAppConfigService.getValueByKey(smsParam.getHrySmstype());
			String sendContent = SmsSendUtil.replaceKey(value, smsParam);
			String sendSms = SmsHxUtils
          .sendSms(hxrt_username,hxrt_password,phone,smsParam.getHryCode(),sendContent);
			appSmsSend.setThirdPartyResult(sendSms);
			appSmsSend.setSendContent(sendContent);
			if(sendSms.contains("message=短信发送成功")){
				return true;
			}else{
				return false;
			}
		}else{
			appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
			return false;
		}
	}

	
	
	@Override
	public boolean sendSmsHai(AppSmsSend appSmsSend,SmsParam smsParam,String Phone) {
		
		//根据短信类型获得短信模板
		RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
		String hxrt_username = remoteAppConfigService.getValueByKey("sms_username");
		String hxrt_password = remoteAppConfigService.getValueByKey("sms_password");
		if("0".equals(smsOpen)){
			String value = remoteAppConfigService.getValueByKey(smsParam.getHrySmstype());
			String sendContent = SmsSendUtil.replaceKey(value, smsParam);
			String sendSms = SmsHxUtils.sendSms(hxrt_username,hxrt_password,Phone,smsParam.getHryCode(),sendContent);
			appSmsSend.setThirdPartyResult(sendSms);
			appSmsSend.setSendContent(sendContent);
			if(sendSms.contains("message=短信发送成功")){
				return true;
			}else{
				return false;
			}
		}else{
			appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
			return false;
		}
	}
	
	
	@Override
	public JsonResult checkCard(String name, String idCard) {
		JsonResult jsonResult = new JsonResult();
		RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String juhe_cardKey = remoteAppConfigService.getValueByKey("juhe_cardKey");
		String juhe_cardUrl = remoteAppConfigService.getValueByKey("juhe_cardUrl");
		String checkCard = JuheUtils.checkCard(juhe_cardUrl, juhe_cardKey, idCard, name);
		
		jsonResult.setMsg(checkCard);
		try {
			JSONObject parseObject = JSON.parseObject(checkCard);
			JSONObject result = (JSONObject) parseObject.get("result");
			Integer res = (Integer) result.get("res");
			if(1==res.intValue()){/*1：匹配 2：不匹配*/
				jsonResult.setSuccess(true);
			}
		} catch (Exception e) {
		}
		return jsonResult;
	}

	@Override
	public boolean sendSmsInfo(AppSmsSend appSmsSend,SmsParam smsParam,String Phone,String syscode,Map<String, Object> map) {

		//根据短信类型获得短信模板
		RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
		String hxrt_username = remoteAppConfigService.getValueByKey("sms_username");
		String hxrt_password = remoteAppConfigService.getValueByKey("sms_password");
		if("0".equals(smsOpen)){
			RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
			String sysCodeValueLists = redisService.get("cn:SysCodeValueList");
			String value = null;
			if (!StringUtils.isEmpty(sysCodeValueLists)) {
				List<com.mz.spotchange.model.SysCodeValue> sysCodeValuelist = JSONArray.parseArray(sysCodeValueLists, com.mz.spotchange.model.SysCodeValue.class);
				for (com.mz.spotchange.model.SysCodeValue sysCodeValue : sysCodeValuelist) {
					if(sysCodeValue.getCode().equals(syscode)){
						value =  sysCodeValue.getValue();
					}
				}
			}
			if(value == null) {
				appSmsSend.setReceiveStatus("0");
				appSmsSend.setThirdPartyResult("系统代码获取不到短信模板！");
				return false;
			}
			String sendContent = this.replaceKey(value, map);
			String sendSms = SmsHxUtils.sendSms(hxrt_username,hxrt_password,Phone,smsParam.getHryCode(),sendContent);
			appSmsSend.setThirdPartyResult(sendSms);
			appSmsSend.setSendContent(sendContent);
			if(sendSms.contains("message=短信发送成功")){
				return true;
			}else{
				return false;
			}
		}else{
			appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
			return false;
		}
	}

	/**
	 * 替换模板里面的值
	 * @param value
	 * @param map
	 * @return
	 */
	public static String replaceKey(String value,Map<String, Object> map){
		String[] split = value.split("#");
		String string = "";
		if (map!=null) {
			for (int i = 0; i < split.length; i++) {
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if (split[i].equals(entry.getKey())) {
						split[i]=(String) entry.getValue();
					}

				}
				string+=split[i];
			}
		}
		return string;
	}
	
}
