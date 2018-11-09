/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月6日 下午3:12:38
 */
package com.mz.huicaopay.impl;

import com.mz.ThirdPayInterfaceService;
import com.mz.utils.CommonRequest;
import com.mz.huicaopay.HuicaopayInterfaceUtil;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年7月6日 下午3:12:38 
 */


public class HuiCaoServiceImpl implements ThirdPayInterfaceService {

	
    //充值
	public CommonRequest recharge(HttpServletResponse response,CommonRequest  request) {
		CommonRequest ret=HuicaopayInterfaceUtil.recharge(response,request);
		return ret;
	}

	
	//提现(接口暂未对接，目前使用的是先下打款到用户的账户)
	@Override
	public CommonRequest withdraw(CommonRequest request) {
		CommonRequest req=HuicaopayInterfaceUtil.withdraw(request);
		return req;
	}


	//查询订单信息(暂未对接)
   @Override
	public CommonRequest queryOrder(CommonRequest request) {
		
		return null;
	}


  
    //充值异步通知处理方法
	@Override
	public CommonRequest rechargeCallBack(Map<String, Object> map) {
		CommonRequest request=HuicaopayInterfaceUtil.rechargeCallBack(map);
		return request;
	}



	//提现异步通知处理方法
	@Override
	public CommonRequest withdrawCallBack(Map<String, Object> map) {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see ThirdPayInterfaceService#checkIdentity(CommonRequest)
	 */
	@Override
	public CommonRequest checkIdentity(CommonRequest request) {
		// TODO Auto-generated method stub
		return null;
	}



}
