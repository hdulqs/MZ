/**
 * 
 */
package com.mz.web.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.sys.ContextUtil;
import com.mz.web.app.dao.AppHolidayConfigDao;
import com.mz.web.app.model.AppHolidayConfig;
import com.mz.web.app.service.AppHolidayConfigService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author lvna
 *
 */       
@Service("appHolidayConfigService")
public class AppHolidayConfigServiceImpl extends
		BaseServiceImpl<AppHolidayConfig, Long> implements
    AppHolidayConfigService {

	@Resource(name="appHolidayConfigDao")
	@Override
	public void setDao(BaseDao<AppHolidayConfig, Long> dao) {
		super.dao= dao;
	}

	/**
	 * 
	 * 在日期区间true、不在false
	 */
	@Override
	public Boolean judgeHoliday(String date) {
		AppHolidayConfigDao appHolidayConfigDao = (AppHolidayConfigDao)dao;
		Integer judgeHoliday = appHolidayConfigDao.judgeHoliday(date);
		if(0 == judgeHoliday){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void initCache() {
		//存入缓存
		List<AppHolidayConfig> findAll = super.findAll();
		if(findAll!=null&&findAll.size()>0){
			RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
			redisService.save("appholidayConfig", JSON.toJSONString(findAll));
		}

	}
 

}
