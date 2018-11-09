/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      denghf
 * @version:     V1.0 
 * @Date:        2017-08-18 09:58:28 
 */
package com.mz.ico.coin.service.impl;

import com.mz.ico.coin.model.AppIcoPay;
import com.mz.ico.coin.service.AppIcoPayService;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppIcoPayService </p>
 * @author:         denghf
 * @Date :          2017-08-18 09:58:28  
 */
@Service("appIcoPayService")
public class AppIcoPayServiceImpl extends BaseServiceImpl<AppIcoPay, Long> implements AppIcoPayService{
	
	@Resource(name="appIcoPayDao")
	@Override
	public void setDao(BaseDao<AppIcoPay, Long> dao) {
		super.dao = dao;
	}
	

}
