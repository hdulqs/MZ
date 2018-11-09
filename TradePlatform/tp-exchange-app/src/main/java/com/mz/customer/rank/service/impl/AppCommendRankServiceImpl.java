/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-12-14 15:06:35 
 */
package com.mz.customer.rank.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.rank.model.AppCommendRank;
import com.mz.customer.rank.service.AppCommendRankService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppCommendRankService </p>
 * @author:         menwei
 * @Date :          2017-12-14 15:06:35  
 */
@Service("appCommendRankService")
public class AppCommendRankServiceImpl extends BaseServiceImpl<AppCommendRank, Long> implements AppCommendRankService{
	
	@Resource(name="appCommendRankDao")
	@Override
	public void setDao(BaseDao<AppCommendRank, Long> dao) {
		super.dao = dao;
	}
	

}
