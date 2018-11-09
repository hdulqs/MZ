package com.mz.sms.utils.aliService;

import org.apache.commons.lang3.StringUtils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;

public class AliServiceUtils {
	
	/**
	 * 
	 * @param userName   用户名
	 * @param passWord   密码
	 * @param phoneNumber  接收手机号
	 * @param params   参数
	 * @param signName 签名
	 * @param templateCode  模板code
	 * @return
	 */
	public static boolean sample(String userName, String passWord, String phoneNumber, String params, String signName, String templateCode) {
		try {
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", userName, passWord);
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms", "sms.aliyuncs.com");
			IAcsClient client = new DefaultAcsClient(profile);
			SingleSendSmsRequest request = new SingleSendSmsRequest();
			request.setSignName(signName);// 控制台创建的签名名称
			request.setTemplateCode(templateCode);// 控制台创建的模板CODE
			request.setParamString(params);// 短信模板中的变量；数字需要转换为字符串；个人用户每个变量长度必须小于15个字符。"
			// request.setParamString("{}");
			request.setRecNum(phoneNumber);// 接收号码
			SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
			if(!StringUtils.isEmpty(httpResponse.getModel())){
				System.out.println(httpResponse.getModel());
				System.out.println(httpResponse.getRequestId());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return false;
	}

}
