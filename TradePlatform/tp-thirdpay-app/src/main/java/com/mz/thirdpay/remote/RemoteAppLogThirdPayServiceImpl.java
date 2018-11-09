/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月26日 下午8:13:40
 */
package com.mz.thirdpay.remote;

import com.mz.remote.RemoteAppLogThirdPayService;
import com.mz.thirdpay.AppLogThirdPay;
import com.mz.thirdpay.biz.service.AppLogThirdPayService;
import javax.annotation.Resource;


/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年7月26日 下午8:13:40 
 */
public class RemoteAppLogThirdPayServiceImpl implements RemoteAppLogThirdPayService {

	
	@Resource
	AppLogThirdPayService appLogThirdPayService;
	
	@Override
	public void save(AppLogThirdPay appLogThirdPay) {
		
		appLogThirdPayService.save(appLogThirdPay);
	}

}
