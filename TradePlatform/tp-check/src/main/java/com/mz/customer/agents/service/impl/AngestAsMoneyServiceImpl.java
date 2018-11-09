package com.mz.customer.agents.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.agents.model.AngestAsMoney;
import com.mz.customer.agents.service.AngestAsMoneyService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("angestAsMoneyService")
public class AngestAsMoneyServiceImpl extends BaseServiceImpl<AngestAsMoney,Long> implements AngestAsMoneyService {

	@Resource(name="angestAsMoneyDao")
	@Override
	public void setDao(BaseDao<AngestAsMoney, Long> dao) {
		super.dao = dao;
	}


	
}






