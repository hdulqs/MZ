package com.mz.sms.sdk.service.impl;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.sms.send.model.AppSmsSend;
import com.mz.sms.utils.aliService.AliServiceUtils;
import com.mz.sms.utils.hx.AliHttpUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.sms.sdk.service.SdkService;
import com.mz.web.remote.RemoteAppConfigService;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

/**
 * 阿里短信接口
 * @author CHINA_LSL
 *
 */
@Service("aliSmsService")
public class AliSmsServiceImpl implements SdkService{

	@Override
	public boolean sendSms(AppSmsSend appSmsSend, SmsParam smsParam,String phone) {
		
		//根据短信类型获得短信模板
		RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
		String hxrt_username = remoteAppConfigService.getValueByKey("sms_username");
		String hxrt_password = remoteAppConfigService.getValueByKey("sms_password");
		if("0".equals(smsOpen)){
			boolean sample = AliServiceUtils
          .sample(hxrt_username, hxrt_password,smsParam.getHryMobilephone() , "{\"name\":\""+smsParam.getHryCode()+"\"}", "51数字资产", "SMS_39200288");
			appSmsSend.setThirdPartyResult(new Boolean(sample).toString());
			if(sample){
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
		return null;
	}
	
	
	public static void main(String[] args) {
		 AliServiceUtils.sample("LTAISTm2KIizOjqV", "gZI3sKfNqM3LsD0wtNMp1xm85u6bpb","18201202026" , "{\"name\":\""+RandomStringUtils.random(6, false, true)+"\"}", "51数字资产", "SMS_39200288");
	}
	
	/**
	 * 云市场
	 */
	public static void market(){

	    String host = "http://sms.market.alicloudapi.com";
	    String path = "/singleSendSms";
	    String method = "GET";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE 45d08b89c6904825b0684");
	    
	    
//	    headers.put("X-Ca-Key", "LTAISTm2KIizOjqV");
//	    headers.put("X-Ca-Signature", "gZI3sKfNqM3LsD0wtNMp1xm85u6bpb");
	    Map<String, String> querys = new HashMap<String, String>();
	    querys.put("ParamString", "{\"no\":\"1234567\"}");
	    querys.put("RecNum", "18201202026");
	    querys.put("SignName", "个人签名");
	    querys.put("TemplateCode", "SMS_46725102");


	    try {
	    	/**
	    	* 重要提示如下:
	    	* HttpUtils请从
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* 下载
	    	*
	    	* 相应的依赖请参照
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	HttpResponse response = AliHttpUtils.doGet(host, path, method, headers, querys);
	    	System.out.println(response.toString());
	    	//获取response的body
	    	System.out.println(EntityUtils.toString(response.getEntity()));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	
	}

	@Override
	public boolean sendSmsHai(AppSmsSend appSmsSend, SmsParam smsParam, String Phone) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendSmsInfo(AppSmsSend appSmsSend,SmsParam smsParam,String Phone,String syscode,Map<String, Object> map){
		return false;
	}
	
}
