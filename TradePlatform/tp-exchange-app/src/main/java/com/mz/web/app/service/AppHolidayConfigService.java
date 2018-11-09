/**
 *
 */
package com.mz.web.app.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.web.app.model.AppHolidayConfig;

/**
 * @author lvna
 */
public interface AppHolidayConfigService extends
    BaseService<AppHolidayConfig, Long> {

  public Boolean judgeHoliday(String date);

  /**
   * 初始化节假日缓存
   */
  public void initCache();


}
