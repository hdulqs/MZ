/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-08-18 14:07:27 
 */
package com.mz.ico.coinAccountColdRecord.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.ico.coinAccountColdRecord.model.AppIcoCoinAccountColdRecord;
import com.mz.ico.coinAccountColdRecord.service.AppIcoCoinAccountColdRecordService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppIcoCoinAccountColdRecordService </p>
 * @author:         shangxl
 * @Date :          2017-08-18 14:07:27  
 */
@Service("appIcoCoinAccountColdRecordService")
public class AppIcoCoinAccountColdRecordServiceImpl extends BaseServiceImpl<AppIcoCoinAccountColdRecord, Long> implements AppIcoCoinAccountColdRecordService{
	
	@Resource(name="appIcoCoinAccountColdRecordDao")
	@Override
	public void setDao(BaseDao<AppIcoCoinAccountColdRecord, Long> dao) {
		super.dao = dao;
	}
	

}
