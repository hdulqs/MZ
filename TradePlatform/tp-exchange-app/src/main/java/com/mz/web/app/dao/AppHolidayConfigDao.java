/**
 *
 */
package com.mz.web.app.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.web.app.model.AppHolidayConfig;
import org.apache.ibatis.annotations.Param;

/**
 * @author lvna
 */
public interface AppHolidayConfigDao extends BaseDao<AppHolidayConfig, Long> {

  public Integer judgeHoliday(@Param(value = "date") String date);


}
