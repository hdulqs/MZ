/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-11-08 11:44:10 
 */
package com.mz.ex.dmHotAccountRecord.service.impl;

import com.mz.ex.dmHotAccountRecord.model.ExDmHotAccountRecord;
import com.mz.ex.dmHotAccountRecord.service.ExDmHotAccountRecordService;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> ExDmHotAccountRecordService </p>
 * @author:         shangxl
 * @Date :          2017-11-08 11:44:10  
 */
@Service("exDmHotAccountRecordService")
public class ExDmHotAccountRecordServiceImpl extends BaseServiceImpl<ExDmHotAccountRecord, Long> implements ExDmHotAccountRecordService{
	
	@Resource(name="exDmHotAccountRecordDao")
	@Override
	public void setDao(BaseDao<ExDmHotAccountRecord, Long> dao) {
		super.dao = dao;
	}
	

}
