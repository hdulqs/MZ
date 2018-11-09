/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月28日 下午6:00:18
 */
package com.mz.exchange.transaction.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.manage.remote.model.ExDmTransactionManage;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午6:00:18
 */
public interface ExDmTransactionDao extends BaseDao<ExDmTransaction, Long> {


  List<ExDmTransaction> findPageBySql(Map<String, Object> map);

  public ExDmTransaction findLastTrasaction();

  /**
   * 根据用户的账号 查询他当天充值或提现的总数量
   *
   * @author: Wu Shuiming
   * @version: V1.0
   * @date: 2016年8月31日 下午3:31:27
   */
  public BigDecimal findGetNumByCustomer(@Param(value = "customerName") String customer,
      @Param(value = "coinCode") String coinCode, @Param(value = "type") String type);

  public List<ExDmTransactionManage> findExdmtransaction(Map<String, String> map);

}
