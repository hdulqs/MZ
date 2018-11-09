/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月23日 上午10:08:17
 */
package com.mz.thirdpay.remote;

import com.mz.remote.RemoteAppThirdPayConfigService;
import com.mz.util.QueryFilter;
import com.mz.thirdpay.biz.service.AppThirdPayConfigService;

import com.mz.thirdpay.AppThirdPayConfig;
import javax.annotation.Resource;

/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年7月23日 上午10:08:17 
 */
public class RemoteAppThirdPayConfigServiceImpl implements RemoteAppThirdPayConfigService {

	@Resource
	AppThirdPayConfigService appThirdPayConfigService;
	
	@Override
	public AppThirdPayConfig getCurrentThirdPay() {
		QueryFilter  filter=new QueryFilter(AppThirdPayConfig.class);
		filter.addFilter("currentThird=", "1");
		AppThirdPayConfig appThirdPayConfig=appThirdPayConfigService.get(filter);
		return appThirdPayConfig;
	}

}
