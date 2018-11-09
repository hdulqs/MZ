/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年8月23日 下午9:13:09
 */
package com.mz.calculate.mvc.service;

import com.mz.calculate.settlement.model.AppReportSettlementcoin;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.product.model.ExProduct;
import java.util.List;

/**
 * @author Wu shuiming
 * @date 2016年8月23日 下午9:13:09
 */
public interface AppReportSettlementcoinService extends BaseService<AppReportSettlementcoin, Long> {

  /**
   * 查询下拉框中的产品
   *
   * @return
   */
  public List<ExProduct> getSelectProduct();


}
