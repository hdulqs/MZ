/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月6日 下午3:12:38
 */
package com.mz.gopay.impl;

import com.mz.ThirdPayInterfaceService;
import com.mz.utils.CommonRequest;
import com.mz.gopay.GopayInterfaceUtil;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年7月6日 下午3:12:38 
 */


public class GopayServiceImpl implements ThirdPayInterfaceService {

	
 
	//充值
	public CommonRequest recharge(HttpServletResponse response,CommonRequest  request) {
		CommonRequest ret=GopayInterfaceUtil.recharge(response,request);
	
		
		return ret;
	}

	
	//提现(目前并未使用接口，提现是通过线下打款)
	@Override
	public CommonRequest withdraw(CommonRequest request) {
		CommonRequest req=GopayInterfaceUtil.withdraw(request);
		return req;
	}


	//查询订单
   @Override
	public CommonRequest queryOrder(CommonRequest request) {
		
		return null;
	}


    //充值回调
	@Override
	public CommonRequest rechargeCallBack(Map<String, Object> map) {
		CommonRequest request=GopayInterfaceUtil.rechargeCallBack(map);
		return request;
	}



	//提现回调
	@Override
	public CommonRequest withdrawCallBack(Map<String, Object> map) {
		CommonRequest request=GopayInterfaceUtil.withdrawCallBack(map);
		return request;
	}



	//查询身份证信息(暂未对接)
	@Override
	public CommonRequest checkIdentity(CommonRequest request) {
		// TODO Auto-generated method stub
		return null;
	}


}
