package com.mz.sms.sdk.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.redis.common.utils.RedisService;
import com.mz.sms.send.model.AppSmsSend;
import com.mz.util.sys.ContextUtil;
import com.yourbit.common.sms.InfobipSms;
import com.mz.sms.sdk.service.SdkService;
import com.mz.web.remote.RemoteAppConfigService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 国际通道短信方
 * GuoJiTongDaoServiceImpl.java
 * @author denghf
 * 2017年10月17日 上午9:53:42
 */
@Service("guoJiTongDaoService")
public class GuoJiTongDaoServiceImpl implements SdkService {

	@Override
	public boolean sendSms(AppSmsSend appSmsSend, SmsParam smsParam, String phone) {
		//根据短信类型获得短信模板
		RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
		if("0".equals(smsOpen)){//接口是否开启
			String value = remoteAppConfigService.getValueByKey(smsParam.getHrySmstype());
			String sendContent = SmsSendUtil.replaceKey(value, smsParam);
			
			try {
				int code = InfobipSms.sendSslSms("Test", "+86"+phone , sendContent,InfobipSms.USERNAME,InfobipSms.PASSWD);
				System.out.println("国际通道短信方发送状态吗："+code);
				appSmsSend.setReceiveStatus("1");
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
				appSmsSend.setThirdPartyResult("【系统短信功能异常】");
			}
		}else{
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
	public boolean sendSmsHai(AppSmsSend appSmsSend, SmsParam smsParam, String Phone) throws IOException {
		//根据短信类型获得短信模板
		RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
		if("0".equals(smsOpen)){//接口是否开启
			String value = remoteAppConfigService.getValueByKey(smsParam.getHrySmstype());
			String sendContent = SmsSendUtil.replaceKey(value, smsParam);
			
			try {
				int code = InfobipSms.sendSslSms("Test", Phone , sendContent,InfobipSms.USERNAME,InfobipSms.PASSWD);
				System.out.println("国际通道短信方发送状态吗："+code);
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
				appSmsSend.setThirdPartyResult("【系统短信功能异常】");
			}
		}else{
			appSmsSend.setThirdPartyResult("【系统短信功能未开启】");
			return false;
		}
		return false;
	}

	@Override
	public boolean sendSmsInfo(AppSmsSend appSmsSend,SmsParam smsParam,String Phone,String syscode,Map<String, Object> map){
		//根据短信类型获得短信模板
		RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String smsOpen = remoteAppConfigService.getValueByKey("smsOpen");
		if("0".equals(smsOpen)){//接口是否开启
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

			try {
				int code = InfobipSms.sendSslSms("Test", Phone , sendContent,InfobipSms.USERNAME,InfobipSms.PASSWD);
				System.out.println("国际通道短信方发送状态吗："+code);
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
				appSmsSend.setThirdPartyResult("【系统短信功能异常】");
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
