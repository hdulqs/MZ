package com.mz.util;

import com.mz.redis.common.utils.RedisUtil;
import com.mz.trade.redis.model.AppAccountRedis;
import com.mz.trade.redis.model.ExDigitalmoneyAccountRedis;

public class UserRedisUtils {
	
	/**
	 * 获得redis账户 
	 * @return
	 */
	public static AppAccountRedis getAccount(String accountId){
		if(accountId!=null&&!"".equals(accountId) ){
			RedisUtil<AppAccountRedis>  a = new RedisUtil<AppAccountRedis>(AppAccountRedis.class);
			AppAccountRedis appAccount = a.get(accountId);
			return appAccount;
		}
		return null;
		
	}
	
	/**
	 * 获得redis币账户 
	 * @return
	 */
	public static ExDigitalmoneyAccountRedis getAccount(String exAccountId,String coinCode){
		if(exAccountId!=null&&!"".equals(exAccountId) ){
			RedisUtil<ExDigitalmoneyAccountRedis>  a = new RedisUtil<ExDigitalmoneyAccountRedis>(ExDigitalmoneyAccountRedis.class);
			ExDigitalmoneyAccountRedis dmAccount = a.get(exAccountId);
			return dmAccount;
		}
		return null;
	}
	
	
	

}
