/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月31日 下午6:52:11
 */
package com.mz.account.fund.service.impl;

import com.mz.account.fund.model.AppHotAccountRecord;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.account.fund.service.AppHotAccountRecordService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月31日 下午6:52:11 
 */
@Service("appHotAccountRecordService")
public class AppHotAccountRecordServiceImpl extends BaseServiceImpl<AppHotAccountRecord, Long> implements AppHotAccountRecordService{
	
	@Resource(name="appHotAccountRecordDao")
	@Override
	public void setDao(BaseDao<AppHotAccountRecord, Long> dao) {
		super.dao = dao;
	}
	

	
	

}
