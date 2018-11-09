package com.mz.account.fund.service;

import com.mz.account.fund.model.AppAgencyTranAwardRecord;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;

/**
 * <p> TODO</p>
 *
 * @author: Zhang Lei
 * @Date :          2017年3月9日 下午1:45:43
 */
public interface AppAgencyTranAwardRecordService extends
    BaseService<AppAgencyTranAwardRecord, Long> {

  PageResult findPageBySql(QueryFilter filter);

}
