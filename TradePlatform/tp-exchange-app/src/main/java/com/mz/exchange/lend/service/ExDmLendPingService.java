package com.mz.exchange.lend.service;

import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.lend.model.ExDmPing;
import com.mz.util.QueryFilter;

public interface ExDmLendPingService extends BaseService<ExDmPing,Long> {

    public PageResult see(QueryFilter filter);

    boolean stopeAlllistByapply(Long customerId, String currencyType, String website);

    boolean stopeMoneylistByapply(Long customerId, String currencyType, String website);
}
