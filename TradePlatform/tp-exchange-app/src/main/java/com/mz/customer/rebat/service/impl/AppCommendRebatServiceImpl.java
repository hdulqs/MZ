/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 17:40:59 
 */
package com.mz.customer.rebat.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.rebat.model.AppCommendRebat;
import com.mz.customer.rebat.service.AppCommendRebatService;
import com.mz.customer.rebat.dao.AppCommendRebatDao;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <p> AppCommendTradeService </p>
 * @author:         menwei
 * @Date :          2017-11-28 17:40:59  
 */
@Service("appCommendRebatService")
public class AppCommendRebatServiceImpl extends BaseServiceImpl<AppCommendRebat, Long> implements
    AppCommendRebatService {
	
	@Resource(name="appCommendRebatDao")
	@Override
	public void setDao(BaseDao<AppCommendRebat, Long> dao) {
		super.dao = dao;
	}
	@Resource(name="appCommendRebatDao")
	public AppCommendRebatDao appCommendRebatDao;
	
	private static final Logger log = LoggerFactory.getLogger(AppCommendRebatServiceImpl.class);

}
