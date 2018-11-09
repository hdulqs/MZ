/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年5月24日 上午9:36:36
 */
package com.mz.trade.entrust.service.impl;
import com.mz.trade.redis.model.Accountadd;

import java.util.List;

/**
 * 
 * @author gaomm
 *
 */
public class AccountAddToRedisRunable implements Runnable {
	
	
	private List<Accountadd> aadds;
	public AccountAddToRedisRunable(List<Accountadd> aadds){
		this.aadds=aadds;
	}
	
	@Override
	public void run() {
	//	TradeRedis.putAccountaddlist(aadds);
		
	}
	
}
