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
import com.mz.customer.businessman.model.RepairAccountRecord;
import com.mz.util.QueryFilter;
import com.mz.customer.businessman.service.RepairAccountRecordService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> RepairAccountRecordService </p>
 *
 * @author: zongwei
 * @Date :          20180502
 */
@Service("repairAccountRecordService")
public class RepairAccountRecordServiceImpl extends
    BaseServiceImpl<RepairAccountRecord, Long> implements RepairAccountRecordService {

  @Resource(name = "repairAccountRecordDao")
  @Override
  public void setDao(BaseDao<RepairAccountRecord, Long> dao) {
    super.dao = dao;
  }


  public synchronized Map<String, BigDecimal> getRepairAccountRecordcount(Long accountId,
      String beginDateString) {
    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    // 提币2审核中1，4所有记录
    QueryFilter filter = new QueryFilter(RepairAccountRecord.class);
    filter.addFilter("accountId=", accountId);
    if (null != beginDateString) {
      filter.addFilter("modified>=", beginDateString);
    }

    // withdrawedtfilter.addFilter("website=", website);
    List<RepairAccountRecord> list = super.find(filter);
    BigDecimal Repairoldcount = new BigDecimal("0");
    BigDecimal Repairhotcount = new BigDecimal("0");
    for (RepairAccountRecord at : list) {
      if (at.getMonteyType() == 1) {
        Repairhotcount = Repairhotcount.add(at.getMoney());
      } else {
        Repairoldcount = Repairoldcount.add(at.getMoney());
      }
    }
    map.put("Repairoldcount", Repairoldcount);
    map.put("Repairhotcount", Repairhotcount);
    return map;
  }


}
