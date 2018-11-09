/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年5月24日 上午9:36:36
 */
package com.mz.core.sms;

import com.mz.manage.remote.RemoteSmsService;
import org.apache.log4j.Logger;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
* @Description:    发送短信线程
* @Author:         zongwei
* @CreateDate:     2018/6/11 12:20
* @UpdateUser:    zongwei
* @UpdateDate:     2018/6/11 12:20
* @UpdateRemark:   创建
* @Version:        1.0
*/
public class SmsInfoRunable implements Runnable {

	@Autowired
	RemoteSmsService remoteSmsService;

	private final static Logger log = Logger.getLogger(SmsInfoRunable.class);

	private String url;  //请求地址
	private SmsParam smsParam; //请求参数

	private String phoneType;

	private String result ;  //返回值

	private String phone;

	private String syscode;

	private Map<String, Object> map;



	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getResult() {
		return result;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public SmsInfoRunable(String url, SmsParam smsParam, String phoneType, String phone, String syscode, Map<String, Object> map){
		this.url = url;
		this.smsParam = smsParam;
		this.phoneType=phoneType;
		this.phone=phone;
		this.syscode =syscode;
		this.map = map;
	}

	@Override
	public void run() {
		log.info("发送短信：请求地址【"+url+"】");
		log.info("发送短信：请求参数【"+smsParam.toJson()+"】");
		remoteSmsService.sendSmsInfo(smsParam.toJson(),phoneType,phone,syscode,map);
		log.info(result);
	}
}
