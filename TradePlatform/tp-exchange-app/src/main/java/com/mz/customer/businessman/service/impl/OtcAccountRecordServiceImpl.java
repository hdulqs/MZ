/**
 * Copyright:   风云科技
 *
 * @author: zongwei
 * @Date :          20180502
 * @Date: 2017-12-07 14:06:38
 */
package com.mz.customer.businessman.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.businessman.model.OtcAccountRecord;
import com.mz.util.QueryFilter;
import com.mz.customer.businessman.service.OtcAccountRecordService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> OtcAccountRecordService </p>
 * @author: zongwei
 * @Date :          20180502
 */
@Service("otcAccountRecordService")
public class OtcAccountRecordServiceImpl extends BaseServiceImpl<OtcAccountRecord, Long> implements
    OtcAccountRecordService {

  @Resource(name = "otcAccountRecordDao")
  @Override
  public void setDao(BaseDao<OtcAccountRecord, Long> dao) {
    super.dao = dao;
  }

  public synchronized void saveRecord(Long customId, Long accountId, BigDecimal money,
      Integer monteyType, Integer acccountType, String transactionNum, String remarks) {
    OtcAccountRecord otcAccountRecord = new OtcAccountRecord();
    otcAccountRecord.setAcccountType(acccountType);
    otcAccountRecord.setMonteyType(monteyType);
    otcAccountRecord.setAccountId(accountId);
    otcAccountRecord.setCustomId(customId);
    otcAccountRecord.setMoney(money);
    otcAccountRecord.setTransactionNum(transactionNum);
    otcAccountRecord.setRemarks(remarks);
    super.save(otcAccountRecord);
  }

  public synchronized Map<String, BigDecimal> getOtcAccountRecordcount(Long accountId,
      String beginDateString) {
    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    // 提币2审核中1，4所有记录
    QueryFilter filter = new QueryFilter(OtcAccountRecord.class);
    filter.addFilter("accountId=", accountId);
    if (null != beginDateString) {
      filter.addFilter("modified>=", beginDateString);
    }

    // withdrawedtfilter.addFilter("website=", website);
    List<OtcAccountRecord> list = super.find(filter);
    BigDecimal otcoldcount = new BigDecimal("0");
    BigDecimal otchotcount = new BigDecimal("0");
    for (OtcAccountRecord at : list) {
      if (at.getMonteyType() == 1) {
        otcoldcount = otcoldcount.add(at.getMoney());
      } else {
        otchotcount = otchotcount.add(at.getMoney());
      }
    }
    map.put("otcoldcount", otcoldcount);
    map.put("otchotcount", otchotcount);
    return map;
  }


}
