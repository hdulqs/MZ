package com.mz.exchange.lend.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.exchange.lend.model.ExDmPing;
import java.util.List;
import java.util.Map;

public interface ExDmLendPingDao extends BaseDao<ExDmPing,Long> {

    public List<ExDmPing> see(Map<String,String> map);
}
