/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年7月6日 下午6:02:18
 */
package com.mz.customer.agents.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.agents.model.AngestAsMoney;
import java.math.BigDecimal;

/**
 * <p> TODO</p>
 * @author: Wu Shuiming
 * @Date :          2016年7月6日 下午6:02:18 
 */
public interface AngestAsMoneyService extends BaseService<AngestAsMoney, Long> {

  /**
   * 通过代理商id派发佣金
   *
   * @author: Wu Shuiming
   * @version: V1.0
   * @date: 2016年8月4日 下午4:38:59
   */
  //public JsonResult postMoneyById(Long id,BigDecimal money);

  JsonResult postMoneyById(Long id, BigDecimal money, String FixPriceCoinCode);


}                                                                                             
