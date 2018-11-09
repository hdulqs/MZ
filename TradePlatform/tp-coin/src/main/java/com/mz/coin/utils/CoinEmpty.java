package com.mz.coin.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.mz.coin.eth.service.impl.EtherService;
import com.mz.utils.Properties;

public class CoinEmpty {
	
	private static Map<String, String> properties = Properties.appcoinMap();
	
	public static boolean coinisNotEmpty(String coinCode) {
		
		String protocol = properties.get(coinCode.toLowerCase() + "_protocol");
		  String ip = properties.get(coinCode.toLowerCase() + "_ip");
		  String port = properties.get(coinCode.toLowerCase() + "_port");
		  
		  if(EtherService.isSmartContractCoin(coinCode)) {
			  return true;
		  }
		  
		  if(!StringUtils.isNotEmpty(ip)||!StringUtils.isNotEmpty(port)||!StringUtils.isNotEmpty(protocol)) {
			  System.out.println(coinCode+"地址为空");
			  return false;
		  }
		      return true;
	}
}
