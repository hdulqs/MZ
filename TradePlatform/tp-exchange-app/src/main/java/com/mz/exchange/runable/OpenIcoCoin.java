package com.mz.exchange.runable;

import com.alibaba.fastjson.JSONObject;
import com.mz.ico.coinAccount.model.AppIcoCoinAccount;
import com.mz.util.http.HttpsRequest;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.ico.coinAccount.service.AppIcoCoinAccountService;
import org.springframework.util.StringUtils;

public class OpenIcoCoin implements Runnable{
	
	private String website;
	private String coinCode;
	private String userName;
	private Long coinAccountId;
    
    public OpenIcoCoin(String website,String coinCode,String userName, Long coinAccountId){
    	this.website = website;
    	this.coinCode = coinCode;
    	this.userName = userName;
    	this.coinAccountId = coinAccountId;
    }
	
	@Override
	public void run() {
		try {
			if (ContextUtil.EN.equals(website)) {
				userName=userName+"ICO-USD";
			} else {
				userName=userName+"ICO";
			}
			//获取ico业务tomcat服务器地址
			String url = PropertiesUtils.APP.getProperty("app.coinip");
			LogFactory.info("开通币ICO钱包地址：" + url + "/coin/icoCoin/create?userName=" + userName + "&type=" + coinCode);
			String result = HttpsRequest.post(url + "/coin/icoCoin/create?userName=" + userName + "&type=" + coinCode);
			LogFactory.info("ICO钱包调用返回值=" + result);
			JSONObject parseObject = null;
			if (!StringUtils.isEmpty(result)) {
				parseObject = JSONObject.parseObject(result);
			}
			String address = "";
			if (parseObject != null) {
				address = parseObject.get("address").toString();
			}

			if (!address.equals("")) {
				AppIcoCoinAccountService appIcoCoinAccountService  = (AppIcoCoinAccountService) ContextUtil.getBean("appIcoCoinAccountService");
				AppIcoCoinAccount coinAccount = appIcoCoinAccountService.get(coinAccountId);
				coinAccount.setPublicKey(address);
				appIcoCoinAccountService.update(coinAccount);
				LogFactory.info("手机号为："+coinAccount.getUserName()+",币地址："+address);
			} else {
				LogFactory.info("开通ICO币"+coinCode+"钱包出错");
			}

		} catch (Exception e) {
			LogFactory.error("远程调用开通ico钱包失败");
			e.printStackTrace();
		}
	}
}
