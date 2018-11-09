/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年4月6日 下午3:40:23
 */
package com.mz.account.remote;

import com.mz.account.fund.model.AppHotAccountRecord;
import com.mz.account.fund.service.AppHotAccountRecordService;
import javax.annotation.Resource;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年4月6日 下午3:40:23 
 */
public class RemoteAppHotAccountRecordServiceImpl implements RemoteAppHotAccountRecordService{
	
	@Resource
	private AppHotAccountRecordService appHotAccountRecordService;
	
	@Override
	public void save(AppHotAccountRecord appHotAccountRecord) {
		appHotAccountRecordService.save(appHotAccountRecord);
	}

}
