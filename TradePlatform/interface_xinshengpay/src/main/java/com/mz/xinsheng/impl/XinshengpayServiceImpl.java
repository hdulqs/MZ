package com.mz.xinsheng.impl;

import com.mz.ThirdPayInterfaceService;
import com.mz.utils.CommonRequest;
import com.mz.xinsheng.XinshengpayInterfaceUtil;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
 * 新生支付
 * <p> TODO</p>
 * @author:         Zhang Lei 
 * @Date :          2017年5月3日 下午2:16:13
 */

public class XinshengpayServiceImpl implements ThirdPayInterfaceService {

	
    //充值
	public CommonRequest recharge(HttpServletResponse response,CommonRequest  request) {
		//这里需要进行具体充值方式的区分
		System.out.println("新生支付的充值方法："+request.getRequestUrl());
		CommonRequest ret=null;
		if(request.getRequestUrl()!=null && "weixin".equals(request.getRequestUrl())){//微信支付
			ret=XinshengpayInterfaceUtil.recharge_weixin(response,request);
		}else if(request.getRequestUrl()!=null && "zhifubao".equals(request.getRequestUrl())){
			ret=XinshengpayInterfaceUtil.recharge_zhifubao(response,request);
		}else if(request.getRequestUrl()!=null && "wangyin".equals(request.getRequestUrl())){
			ret=XinshengpayInterfaceUtil.recharge(response,request);
		}else{//默认有收银台模式
			ret=XinshengpayInterfaceUtil.recharge(response,request);
		}
		return ret;
	}

	
	//提现(接口暂未对接)
	@Override
	public CommonRequest withdraw(CommonRequest request) {
		CommonRequest ret=XinshengpayInterfaceUtil.withdraw(request);
		return ret;
	}

  
    //充值异步通知处理方法
	@Override
	public CommonRequest rechargeCallBack(Map<String, Object> map) {
		System.out.println("现代金控的充值异步通知处理方法。。。。。。。");
		CommonRequest request=null;
		//充值回调区分哪种方式
		String rechargeCallBackType=map.get("rechargeCallBackType")==null?"":map.get("rechargeCallBackType").toString();
		if("".equals(rechargeCallBackType)){
			System.out.println("未获取到充值回调方式rechargeCallBackType字段，请检查代码！！！！！！！！！！！！！！！！");
			return null;
		}else{
			if("havapage".equals(rechargeCallBackType)){//有收银台方式
				request=XinshengpayInterfaceUtil.rechargeCallBack(map);
			}else if("weixin".equals(rechargeCallBackType)){
				request=XinshengpayInterfaceUtil.rechargeCallBack_weixin(map);
				
			}else if("zhifubao".equals(rechargeCallBackType)){
				request=XinshengpayInterfaceUtil.rechargeCallBack_zhifubao(map);
				
			}else if("wangyin".equals(rechargeCallBackType)){
				request=XinshengpayInterfaceUtil.rechargeCallBack_wangyin(map);
				
			}
		}
		return request;
	}



	//提现异步通知处理方法
	@Override
	public CommonRequest withdrawCallBack(Map<String, Object> map) {
		return null;
	}


	//查询身份证信息
	@Override
	public CommonRequest checkIdentity(CommonRequest request) {
		return null;
	}

	//查询订单
	@Override
	public CommonRequest queryOrder(CommonRequest arg0) {
		return null;
	}


}
