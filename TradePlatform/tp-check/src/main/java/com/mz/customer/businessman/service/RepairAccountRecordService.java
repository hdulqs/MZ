/**
 * Copyright:   风云科技
 * @author:      zongwei
 * @version:     V1.0 
 * @Date:        20180502
 */
package com.mz.customer.businessman.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.businessman.model.RepairAccountRecord;
import java.math.BigDecimal;
import java.util.Map;


/**
 * <p> OtcAccountRecordService </p>
 * @author:         zongwei
 * @Date :          20180502
 */
public interface RepairAccountRecordService extends BaseService<RepairAccountRecord, Long>{

    public Map<String, BigDecimal> getRepairAccountRecordcount(Long accountId, String beginDateString) ;
}
