package com.mz.sms.sdk.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.redis.common.utils.RedisService;
import com.mz.sms.send.model.AppSmsSend;
import com.mz.sms.utils.hx.HttpSend;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.sms.sdk.service.SdkService;
import com.mz.web.remote.RemoteAppConfigService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
/**
 * 云片短信接口
 * <p> TODO</p>
 * @author:         Zhang Lei 
 * @Date :          2017年2月27日 上午10:25:11
 */
@Service("yunPianService")
public class YunpianServiceImpl implements SdkService{
	//提交地址
	private static final String INTERNATIONAL_URL="http://sms.yunpian.com/v2/sms/single_send.json";

    private static final Logger log = Logger.getLogger(YunpianServiceImpl.class);
	
	@Override
	public boolean sendSms(AppSmsSend appSmsSend, SmsParam smsParam,String phone) {
		//根据短信类型获得短信模板
		RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
		String yunpianKey = remoteAppConfigService.getValueByKey("sms_apiKey");
		if("0".equals(smsOpen)){//接口是否开启
			String value = remoteAppConfigService.getValueByKey("sms_take_phone");
			String sendContent = SmsSendUtil.replaceKey(value, smsParam);
			/*String encodedContent = null;
			try {
				encodedContent = URLEncoder.encode(sendContent, "utf-8");
			} catch (UnsupportedEncodingException e) {
				System.out.println("云片短信接口,获取替换短信内容异常！！！");
				e.printStackTrace();
				return false;
			}*/
			
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println("sdk===="+yunpianKey);
	        params.put("apikey", yunpianKey);
	        params.put("text", sendContent);
	        params.put("mobile", phone.replace(" ", ""));
	        String sendSms= HttpSend.yunpianPost(INTERNATIONAL_URL, params);
			
			//修改为您要发送的手机号
			if(sendSms!=null){
				System.out.println(sendSms);
				int code = 0;
				try {
					JSONObject myJsonObject = new JSONObject(sendSms);
					code = myJsonObject.getInt("code");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(code==0){
					appSmsSend.setReceiveStatus("1");
					appSmsSend.setThirdPartyResult("发送成功！");
					appSmsSend.setSendContent(sendContent);
					return true;
				}else if(code > 0){
					appSmsSend.setReceiveStatus("0");
					appSmsSend.setThirdPartyResult(sendSms);
					appSmsSend.setThirdPartyResult("调用API时发生错误，需要开发者进行相应的处理。");
                    log.error("调用短信发送错误："+sendSms);
					appSmsSend.setSendContent(sendContent);
					return false;
				}else if(-50<code && code<=-1){
					appSmsSend.setReceiveStatus("0");
					appSmsSend.setThirdPartyResult(sendSms);
					appSmsSend.setThirdPartyResult("权限验证失败，需要开发者进行相应的处理。");
					appSmsSend.setSendContent(sendContent);
					return false;
				}else if(code <= -50){
					appSmsSend.setReceiveStatus("0");
					appSmsSend.setThirdPartyResult(sendSms);
					appSmsSend.setThirdPartyResult("系统内部错误，请联系技术支持，调查问题原因并获得解决方案。");
					appSmsSend.setSendContent(sendContent);
					return false;
				}
			}else{
				appSmsSend.setThirdPartyResult("发送失败，调用接口异常！");
				appSmsSend.setSendContent(sendContent);
				return false;
			}
		}else{
			appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
			return false;
		}
		return false;
	}

	@Override
	public JsonResult checkCard(String name, String idCard) {
		return null;
	}

	/*@Override
	public boolean sendSmsHai(AppSmsSend appSmsSend, SmsParam smsParam, String Phone) throws IOException {
		//根据短信类型获得短信模板
				RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
				String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
				String hxrt_username = remoteAppConfigService.getValueByKey("sms_username");
				String hxrt_password = remoteAppConfigService.getValueByKey("sms_password");
				if("0".equals(smsOpen)){
					String value = remoteAppConfigService.getValueByKey(smsParam.getHrySmstype());
					String sendContent = SmsSendUtil.replaceKey(value, smsParam);
					String sendSms = YunPianUtils.sendSms(hxrt_username,hxrt_password,Phone,smsParam.getHryCode(),sendContent);
					//String sendSms = SmsHxUtils.sendSms(hxrt_username,hxrt_password,smsParam.getHryMobilephone().replaceAll(" ", ""),smsParam.getHryCode(),sendContent);
					appSmsSend.setThirdPartyResult(sendSms);
					appSmsSend.setSendContent(sendContent);
					if(sendSms.equals("0")){
						return true;
					}else{
						return false;
					}
				}else{
					appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
					return false;
				}
	}
	
	*/
	
	
	@Override
	public boolean sendSmsHai(AppSmsSend appSmsSend, SmsParam smsParam, String Phone) {/*
		// TODO Auto-generated method stu
		RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
		String apikey = remoteAppConfigService.getValueByKey("sms_apiKey");
		if("0".equals(smsOpen)){
				String value = remoteAppConfigService.getValueByKey(smsParam.getHrySmstype());
				String sendContent = SmsSendUtil.replaceKey(value, smsParam);
	
				String sendSms = YunPianUtils.sendSms(apikey, sendContent, Phone.replace(" ", ""));
				appSmsSend.setThirdPartyResult(sendSms);
				appSmsSend.setSendContent(sendContent);
				Map<String, String> map = new HashMap<String, String>();
				sendSms = sendSms.substring(1, sendSms.length()-1);//去掉前后括号
				String[] arraydata = sendSms.split(",");//按“，”将其分为字符数组
				String trim = arraydata[1].trim();
				int start = trim.indexOf(":");
				String name = trim.substring(start);
				String addressqu = name.substring(name.lastIndexOf(":")+1);
				String replace = addressqu.replace("\"","");
				if(replace.equals("发送成功")){
					appSmsSend.setReceiveStatus("1");
					appSmsSend.setThirdPartyResult("发送成功！");
					return true;
				}else{
					appSmsSend.setReceiveStatus("0");
					appSmsSend.setThirdPartyResult(sendSms);
					appSmsSend.setThirdPartyResult("系统内部错误，请联系技术支持，调查问题原因并获得解决方案。");
					appSmsSend.setSendContent(sendContent);
					return false;
			    }
		   }else{
				appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
				return false;
		  }
	  */

		//根据短信类型获得短信模板
		RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
		String yunpianKey = remoteAppConfigService.getValueByKey("sms_apiKey");
		if("0".equals(smsOpen)){//接口是否开启
			String value = remoteAppConfigService.getValueByKey(smsParam.getHrySmstype());
			String sendContent = SmsSendUtil.replaceKey(value, smsParam);
			/*String encodedContent = null;
			try {
				encodedContent = URLEncoder.encode(sendContent, "utf-8");
			} catch (UnsupportedEncodingException e) {
				System.out.println("云片短信接口,获取替换短信内容异常！！！");
				e.printStackTrace();
				return false;
			}*/
			
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println("sdk===="+yunpianKey);
	        params.put("apikey", yunpianKey);
	        params.put("text", sendContent);
	        params.put("mobile", Phone.replace(" ", ""));
	        String sendSms=HttpSend.yunpianPost(INTERNATIONAL_URL, params);
			
			//修改为您要发送的手机号
			if(sendSms!=null){
				System.out.println(sendSms);
				int code = 0;
				try {
					JSONObject myJsonObject = new JSONObject(sendSms);
					code = myJsonObject.getInt("code");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(code==0){
					appSmsSend.setReceiveStatus("1");
					appSmsSend.setThirdPartyResult("发送成功！");
					appSmsSend.setSendContent(sendContent);
					return true;
				}else if(code > 0){
					appSmsSend.setReceiveStatus("0");
					appSmsSend.setThirdPartyResult(sendSms);
					appSmsSend.setThirdPartyResult("调用API时发生错误，需要开发者进行相应的处理。");
                    log.error("调用短信发送错误："+sendSms);
					appSmsSend.setSendContent(sendContent);
					return false;
				}else if(-50<code && code<=-1){
					appSmsSend.setReceiveStatus("0");
					appSmsSend.setThirdPartyResult(sendSms);
					appSmsSend.setThirdPartyResult("权限验证失败，需要开发者进行相应的处理。");
					appSmsSend.setSendContent(sendContent);
					return false;
				}else if(code <= -50){
					appSmsSend.setReceiveStatus("0");
					appSmsSend.setThirdPartyResult(sendSms);
					appSmsSend.setThirdPartyResult("系统内部错误，请联系技术支持，调查问题原因并获得解决方案。");
					appSmsSend.setSendContent(sendContent);
					return false;
				}
			}else{
				appSmsSend.setThirdPartyResult("发送失败，调用接口异常！");
				appSmsSend.setSendContent(sendContent);
				return false;
			}
		}else{
			appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
			return false;
		}
		return false;
		}

	public static void main(String[] args) {
		Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", "8d309dc5792d93f986b6ca2819209195");
        params.put("text", "【微笑国际】您申请的USDT提币审核通过");
        params.put("mobile", "+8618813929463".replace(" ", ""));//+1 2029962812
        String sendSms=HttpSend.yunpianPost(INTERNATIONAL_URL, params);
        System.out.println(sendSms);
		
	}

	
	public boolean sendSmsHai(AppSmsSend appSmsSend, SmsParam smsParam, String phone,Map<String, Object> map) {
				//根据短信类型获得短信模板
				RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
				String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
				String yunpianKey = remoteAppConfigService.getValueByKey("sms_apiKey");
				if("0".equals(smsOpen)){//接口是否开启
					String value = remoteAppConfigService.getValueByKey(smsParam.getHrySmstype());
					String sendContent = SmsSendUtil.replaceKey(value, smsParam);
					
					
					Map<String, String> params = new HashMap<String, String>();
					System.out.println("sdk===="+yunpianKey);
			        params.put("apikey", yunpianKey);
			        params.put("text", sendContent);
			        params.put("mobile", phone.replace(" ", ""));
			        String sendSms=HttpSend.yunpianPost(INTERNATIONAL_URL, params);
					
					//修改为您要发送的手机号
					if(sendSms!=null){
						System.out.println(sendSms);
						int code = 0;
						try {
							JSONObject myJsonObject = new JSONObject(sendSms);
							code = myJsonObject.getInt("code");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if(code==0){
							appSmsSend.setReceiveStatus("1");
							appSmsSend.setThirdPartyResult("发送成功！");
							appSmsSend.setSendContent(sendContent);
							return true;
						}else if(code > 0){
							appSmsSend.setReceiveStatus("0");
							appSmsSend.setThirdPartyResult(sendSms);
							appSmsSend.setThirdPartyResult("调用API时发生错误，需要开发者进行相应的处理。");
                            log.error("调用短信发送错误："+sendSms);
							appSmsSend.setSendContent(sendContent);
							return false;
						}else if(-50<code && code<=-1){
							appSmsSend.setReceiveStatus("0");
							appSmsSend.setThirdPartyResult(sendSms);
							appSmsSend.setThirdPartyResult("权限验证失败，需要开发者进行相应的处理。");
							appSmsSend.setSendContent(sendContent);
							return false;
						}else if(code <= -50){
							appSmsSend.setReceiveStatus("0");
							appSmsSend.setThirdPartyResult(sendSms);
							appSmsSend.setThirdPartyResult("系统内部错误，请联系技术支持，调查问题原因并获得解决方案。");
							appSmsSend.setSendContent(sendContent);
							return false;
						}
					}else{
						appSmsSend.setThirdPartyResult("发送失败，调用接口异常！");
						appSmsSend.setSendContent(sendContent);
						return false;
					}
				}else{
					appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
					return false;
				}
				return false;
	}

	public boolean sendSms(AppSmsSend appSmsSend, SmsParam smsParam, String phone,Map<String, Object> map) {
		RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
		String yunpianKey = remoteAppConfigService.getValueByKey("sms_apiKey");
		if("0".equals(smsOpen)){//接口是否开启
			String hrySmstype = smsParam.getHrySmstype();
			//从配置文件中获取模板
			String value = PropertiesUtils.APP.getProperty("app."+hrySmstype);
			//替换模板中的值
			String sendContent = replaceKey(value,map);
			
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println("sdk===="+yunpianKey);
	        params.put("apikey", yunpianKey);
	        params.put("text", sendContent);
	        params.put("mobile", phone.replace(" ", ""));
	        String sendSms=HttpSend.yunpianPost(INTERNATIONAL_URL, params);
			
			//修改为您要发送的手机号
			if(sendSms!=null){
				System.out.println(sendSms);
				int code = 0;
				try {
					JSONObject myJsonObject = new JSONObject(sendSms);
					code = myJsonObject.getInt("code");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(code==0){
					appSmsSend.setReceiveStatus("1");
					appSmsSend.setThirdPartyResult("发送成功！");
					appSmsSend.setSendContent(sendContent);
					return true;
				}else if(code > 0){
					appSmsSend.setReceiveStatus("0");
					appSmsSend.setThirdPartyResult(sendSms);
					appSmsSend.setThirdPartyResult("调用API时发生错误，需要开发者进行相应的处理。");
                    log.error("调用短信发送错误："+sendSms);
					appSmsSend.setSendContent(sendContent);
					return false;
				}else if(-50<code && code<=-1){
					appSmsSend.setReceiveStatus("0");
					appSmsSend.setThirdPartyResult(sendSms);
					appSmsSend.setThirdPartyResult("权限验证失败，需要开发者进行相应的处理。");
					appSmsSend.setSendContent(sendContent);
					return false;
				}else if(code <= -50){
					appSmsSend.setReceiveStatus("0");
					appSmsSend.setThirdPartyResult(sendSms);
					appSmsSend.setThirdPartyResult("系统内部错误，请联系技术支持，调查问题原因并获得解决方案。");
					appSmsSend.setSendContent(sendContent);
					return false;
				}
			}else{
				appSmsSend.setThirdPartyResult("发送失败，调用接口异常！");
				appSmsSend.setSendContent(sendContent);
				return false;
			}
		}else{
			appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
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
		String yunpianKey = remoteAppConfigService.getValueByKey("sms_apiKey");
		if("0".equals(smsOpen)){//接口是否开启

			//从系统代码中获取模板
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
			//替换模板中的值
			String sendContent = this.replaceKey(value,map);


			Map<String, String> params = new HashMap<String, String>();
			System.out.println("sdk===="+yunpianKey);
			String mobile = phone.replace(" ", "");
			//mobile  =  mobile.replace("+", "");
			System.out.println("发送短信号码===="+mobile);
			System.out.println("发送内容===="+sendContent);
			params.put("apikey", yunpianKey);
			params.put("text", sendContent);
			params.put("mobile", mobile);
			String sendSms=HttpSend.yunpianPost(INTERNATIONAL_URL, params);

			//修改为您要发送的手机号
			if(sendSms!=null){
				System.out.println(sendSms);
				int code = 0;
				try {
					JSONObject myJsonObject = new JSONObject(sendSms);
					code = myJsonObject.getInt("code");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(code==0){
					appSmsSend.setReceiveStatus("1");
					appSmsSend.setThirdPartyResult("发送成功！");
					appSmsSend.setSendContent(sendContent);
					return true;
				}else if(code > 0){
					appSmsSend.setReceiveStatus("0");
					appSmsSend.setThirdPartyResult(sendSms);
					appSmsSend.setThirdPartyResult("调用API时发生错误，需要开发者进行相应的处理。");
                    log.error("调用短信发送错误："+sendSms);
					appSmsSend.setSendContent(sendContent);
					return false;
				}else if(-50<code && code<=-1){
					appSmsSend.setReceiveStatus("0");
					appSmsSend.setThirdPartyResult(sendSms);
					appSmsSend.setThirdPartyResult("权限验证失败，需要开发者进行相应的处理。");
					appSmsSend.setSendContent(sendContent);
					return false;
				}else if(code <= -50){
					appSmsSend.setReceiveStatus("0");
					appSmsSend.setThirdPartyResult(sendSms);
					appSmsSend.setThirdPartyResult("系统内部错误，请联系技术支持，调查问题原因并获得解决方案。");
					appSmsSend.setSendContent(sendContent);
					return false;
				}
			}else{
				appSmsSend.setThirdPartyResult("发送失败，调用接口异常！");
				appSmsSend.setSendContent(sendContent);
				return false;
			}
		}else{
			appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
			return false;
		}
		return false;
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
