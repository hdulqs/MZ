/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月31日 下午6:52:11
 */
package com.mz.account.fund.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mz.account.fund.model.AppColdAccountRecord;
import com.mz.account.fund.service.AppColdAccountRecordService;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月31日 下午6:52:11 
 */
@Service("appColdAccountRecordService")
public class AppColdAccountRecordServiceImpl extends BaseServiceImpl<AppColdAccountRecord, Long> implements AppColdAccountRecordService{
	
	@Resource(name="appColdAccountRecordDao")
	@Override
	public void setDao(BaseDao<AppColdAccountRecord, Long> dao) {
		super.dao = dao;
	}

}
