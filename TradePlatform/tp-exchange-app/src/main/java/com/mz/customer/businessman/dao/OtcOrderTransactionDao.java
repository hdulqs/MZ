/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: liushilei
 * @version: V1.0
 * @Date: 2017-12-07 14:06:38
 */
package com.mz.customer.businessman.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.customer.businessman.model.OtcOrderTransaction;
import java.util.List;
import java.util.Map;

/**
 * <p> C2cTransactionDao </p>
 * @author: ZONGWEI
 * @Date: 2018-05-22
 */
public interface OtcOrderTransactionDao extends BaseDao<OtcOrderTransaction, Long> {

  /**
   * 查询OTC交易记录查询
   * @param params
   * @return
   */
  List<OtcOrderTransaction> otcorderlistall(Map<String, String> params);


  /**
   * 查询OTC交易买记录查询
   * @param params
   * @return
   */
  List<OtcOrderTransaction> otcorderbuylist(Map<String, String> params);


  /**
   * 查询OTC交易卖记录查询
   * @param params
   * @return
   */
  List<OtcOrderTransaction> otcorderselllist(Map<String, String> params);


}
