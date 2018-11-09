/**
 * Copyright:   风云科技
 *
 * @author: zongwei
 * @version: V1.0
 * @Date: 20180502
 */
package com.mz.customer.businessman.service;

import com.mz.core.mvc.service.base.BaseService;

import com.mz.customer.businessman.model.OtcAccountRecord;
import java.math.BigDecimal;
import java.util.Map;


/**
 * <p> OtcAccountRecordService </p>
 * @author: zongwei
 * @Date :          20180502
 */
public interface OtcAccountRecordService extends BaseService<OtcAccountRecord, Long> {

  public void saveRecord(Long customId, Long accountId, BigDecimal money, Integer monteyType,
      Integer acccountType, String transactionNum, String remarks);

  public Map<String, BigDecimal> getOtcAccountRecordcount(Long accountId, String beginDateString);
}
