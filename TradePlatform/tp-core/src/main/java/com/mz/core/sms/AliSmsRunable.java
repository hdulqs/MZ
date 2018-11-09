/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年5月24日 上午9:36:36
 */
package com.mz.core.sms;

import com.mz.core.sms.SmsParam;
import com.mz.sms.send.model.AppSmsSend;
import com.mz.core.mvc.service.AppSmsSend.AppSmsSendService;
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;

/**
 * 
 * <p> TODO</p>
 * @author:         Shangxl 
 * @Date :          2017年2月17日 下午5:57:15
 */
public class AliSmsRunable implements Runnable {
	
	private AppSmsSend appSmsSend;//短信发送记录
	
	private SmsParam smsParam; //请求参数
	
	private String result ;  //返回值
	
	
	public String getResult() {
		return result;
	}
	
	public AliSmsRunable(AppSmsSend appSmsSend, SmsParam smsParam){
		this.appSmsSend = appSmsSend;
		this.smsParam = smsParam;
	}
	
	@Override
	public void run() {
//		MongoUtil<AppSmsSend, Long> mongoUtil = new MongoUtil<AppSmsSend, Long>(AppSmsSend.class);
		AppSmsSend appSmsSend = new AppSmsSend();
//		appSmsSend.setId(mongoUtil.autoincrementId());
		appSmsSend.setType(SmsSendUtil.getSendTypeValue(smsParam.getHrySmstype()));
		appSmsSend.setServerUrl(null);
		
		AppSmsSendService sendService=(AppSmsSendService) ContextUtil.getBean("appSmsSendService");
		sendService.save(appSmsSend);
//		mongoUtil.save(appSmsSend);
		smsParam.setSendId(appSmsSend.getId());
		LogFactory.info("发送短信：请求参数【"+smsParam.toJson()+"】");
		LogFactory.info(result);
	}

}
