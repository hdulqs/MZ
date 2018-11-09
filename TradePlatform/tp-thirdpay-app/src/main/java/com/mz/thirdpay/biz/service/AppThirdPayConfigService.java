/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Zhang Xiaofang
 * @version: V1.0
 * @Date: 2016年7月8日 下午6:31:40
 */
package com.mz.thirdpay.biz.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.thirdpay.AppThirdPayConfig;
import java.util.List;

/**
 * <p> TODO</p>
 *
 * @author: Zhang Xiaofang
 * @Date :          2016年7月8日 下午6:31:40
 */
public interface AppThirdPayConfigService extends BaseService<AppThirdPayConfig, Long> {

  /**
   * <p> TODO</p>
   *
   * @author: Zhang Xiaofang
   * @param: @return
   * @return: List<AppThirdPayConfig>
   * @Date :          2016年7月16日 下午4:32:36
   * @throws:
   */
  List<AppThirdPayConfig> findType();


  AppThirdPayConfig findCurrentType();


}
