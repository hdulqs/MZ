package com.mz.inpay.impl;

import com.mz.ThirdPayInterfaceService;
import com.mz.utils.CommonRequest;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

public class InpayServiceImpl implements ThirdPayInterfaceService {

	@Override
	public CommonRequest recharge(HttpServletResponse response, CommonRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonRequest withdraw(CommonRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonRequest queryOrder(CommonRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonRequest rechargeCallBack(Map<String, Object> map) {
		System.out.println(map.toString());
		
		
		CommonRequest commonRequest = new CommonRequest();
		commonRequest.setRequestThirdPay("inpay");
		String merOrderNum = map.get("merOrderId").toString();// 订单号系统
		commonRequest.setQueryOrderNo(map.get("tranNo").toString()); //创新支付存入的第三方的订单号
		commonRequest.setRequestNo(merOrderNum);
		commonRequest.setResponseObj(map.toString());
		

		if (map.containsKey("tradeStatus")) {
			String status = map.get("tradeStatus").toString();

			if ("02".equals(status)) {
				System.out.println("订单号为:"+merOrderNum+" 充值成功");
				commonRequest.setResponseCode("success");
				commonRequest.setResponseMsg("充值成功");
				commonRequest.setResponseObj(map.toString());
			} else if("01".equals(status)){
				System.out.println("订单号为:"+merOrderNum+" 处理中");
				commonRequest.setResponseCode("apply");
				commonRequest.setResponseMsg("充值处理中");
			} else {
				System.out.println("订单号为:"+merOrderNum+" 充值失败");
				commonRequest.setResponseCode("faile");
				commonRequest.setResponseMsg("充值失败");
			}
		}
		return commonRequest;
	}

	@Override
	public CommonRequest withdrawCallBack(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonRequest checkIdentity(CommonRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
