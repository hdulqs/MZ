package com.mz.coin.enums;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.mz.Constant;
import com.mz.coin.utils.RedisUtil;

public  enum Coin {
	
	ETC(Constant.ETC,"EtcService","createAddress","sendFrom"),ETZ(Constant.ETZ,"EtzService","createAddress","123");
	 
	 private String coinName;
	 private String service;
	 private String method;
	 private String parameter;
	
     public static Map<String,Coin> map = new LinkedHashMap<String,Coin>(Coin.values().length);
	 
     
	 static {
		 for (Coin c : Coin.values()) {
			 map.put(c.getCoinName().toUpperCase(),c);
		 }
	 }
	 
	 
	 Coin(String coinName,String service, String method,String parameter) {
		
		    this.coinName = coinName;
	        this.service = service;
	        this.method = method;
	        this.parameter = parameter;
	    }

	 
	/* public static Map<String,Coin> getCoinNames() {
		
		 Map<String,Coin> map = new LinkedHashMap<String,Coin>(Coin.values().length);
		 for (Coin c : Coin.values()) {
			 RedisUtil.setEnums(c.getCoinName(),c);
			}
			return map;
	 }
	 */
	 
	 
	public String getCoinName() {
		return coinName;
	}


	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}


	public Class<?> getValue() throws ClassNotFoundException {
	        return Class.forName(service);
	 }
	 
	 public String getCoin(String tyep) {
			return service;
	}
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	 
	 
	 
	 
}
