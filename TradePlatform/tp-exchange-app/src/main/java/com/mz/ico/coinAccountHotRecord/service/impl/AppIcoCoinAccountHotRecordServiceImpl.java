/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-08-18 14:06:56 
 */
package com.mz.ico.coinAccountHotRecord.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.ico.coinAccountHotRecord.model.AppIcoCoinAccountHotRecord;
import com.mz.ico.coinAccountHotRecord.service.AppIcoCoinAccountHotRecordService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppIcoCoinAccountHotRecordService </p>
 * @author:         shangxl
 * @Date :          2017-08-18 14:06:56  
 */
@Service("appIcoCoinAccountHotRecordService")
public class AppIcoCoinAccountHotRecordServiceImpl extends BaseServiceImpl<AppIcoCoinAccountHotRecord, Long> implements AppIcoCoinAccountHotRecordService{
	
	@Resource(name="appIcoCoinAccountHotRecordDao")
	@Override
	public void setDao(BaseDao<AppIcoCoinAccountHotRecord, Long> dao) {
		super.dao = dao;
	}
	

}
