/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-08-17 18:22:21
 */
package com.mz.ico.coin.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.ico.coin.model.AppIcoCoin;

/**
 * <p> AppIcoCoinService </p>
 * @author: shangxl
 * @Date :          2017-08-17 18:22:21  
 */
public interface AppIcoCoinService extends BaseService<AppIcoCoin, Long> {

  /**
   * ico初始化产品code
   * <p> TODO</p>
   * @author: Shangxl
   * @param:
   * @return: void
   * @Date :          2017年8月21日 上午10:10:34
   * @throws:
   */
  public void initIcoRedisCode();
}
