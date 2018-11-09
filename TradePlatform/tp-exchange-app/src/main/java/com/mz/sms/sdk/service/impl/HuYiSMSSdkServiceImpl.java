/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年5月18日 下午2:25:52
 */
package com.mz.sms.sdk.service.impl;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.sms.utils.hx.HttpSend;
import com.mz.util.RemoteQueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.remote.RemoteAppPersonInfoService;
import com.mz.sms.sdk.service.SdkService;
import com.mz.sms.send.model.AppSmsSend;
import com.mz.sms.utils.juhe.JuheUtils;
import com.mz.web.remote.RemoteAppConfigService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * <p> TODO</p>
 * @author:         Yuan Zhicheng
 * @Date :          2016年5月18日 下午2:25:52 
 */
@Service("HuYiSMSSdkServiceImpl")
public class HuYiSMSSdkServiceImpl implements SdkService{
	
	private static final String INTERNATIONAL_URL="http://api.isms.ihuyi.cn/webservice/isms.php";
	
	@Override
	public boolean sendSms(AppSmsSend appSmsSend,SmsParam smsParam,String phone) {
		
		//根据短信类型获得短信模板
		// 国际短信 城市号  +" "+手机号
		RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
		String hxrt_username = remoteAppConfigService.getValueByKey("sms_username");
		String hxrt_password = remoteAppConfigService.getValueByKey("sms_password");
		if("0".equals(smsOpen)){
			String value = remoteAppConfigService.getValueByKey(smsParam.getHrySmstype());
			String sendContent = SmsSendUtil.replaceKey(value, smsParam);
			
			//String sendSms = SmsHxUtils.sendSms(hxrt_username,hxrt_password,smsParam.getHryMobilephone(),smsParam.getHryCode(),sendContent);
			String encodedContent = null;
			
			try {
				encodedContent = URLEncoder.encode(sendContent, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return false;
			}
			
			if(!smsParam.getHryMobilephone().startsWith("0")){
				
				RemoteAppPersonInfoService remoteAppPersonInfoService = (RemoteAppPersonInfoService) ContextUtil.getBean("remoteAppPersonInfoService");
				RemoteQueryFilter remoteQueryFilter = new RemoteQueryFilter(AppPersonInfo.class);
				remoteQueryFilter.addFilter("mobilePhone=", smsParam.getHryMobilephone());
				AppPersonInfo appPersonInfo = remoteAppPersonInfoService.get(remoteQueryFilter);
				smsParam.setHryMobilephone(appPersonInfo.getCountry()+" "+smsParam.getHryMobilephone());
			}
			
			StringBuffer strBuf = new StringBuffer();
			strBuf.append("account=").append(hxrt_username);
			strBuf.append("&password=").append(hxrt_password);
			strBuf.append("&mobile=").append(smsParam.getHryMobilephone());
			strBuf.append("&content=").append(encodedContent);
			strBuf.append("&method=").append("Submit");
			System.out.println("intersms==="+strBuf.toString());
			String sendSms= HttpSend.getSend(INTERNATIONAL_URL, strBuf.toString());
			System.out.println("短信返回=="+sendSms);	
			

			Document doc = null;
			try {
				doc = DocumentHelper.parseText(sendSms);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			Element root = doc.getRootElement();


			String code = root.elementText("code");	
			String msg = root.elementText("msg");	
			String ismsid = root.elementText("ismsid");	
			
			
			System.out.println(code);
			System.out.println(msg);
			System.out.println(ismsid);
						
			 if("2".equals(code)){
				
				System.out.println("短信提交成功");
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
		
/*		String checkCard = "{"
		+    "\"reason\": \"成功1\", "
		+    "    \"result\": {"
		+    "        \"realname\": \"董好帅\","
		+    "        \"idcard\": \"330329199001020012\","
		+    "        \"res\": 2 "
		+    "    },"
		+    "    \"error_code\": 0"
		+    "}";*/
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
	public boolean sendSmsHai(AppSmsSend appSmsSend, SmsParam smsParam, String Phone) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendSmsInfo(AppSmsSend appSmsSend,SmsParam smsParam,String Phone,String syscode,Map<String, Object> map){
		// TODO Auto-generated method stub
		return false;
	}
	
}
