/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年8月20日 下午5:08:04
 */
package com.mz.web.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppBanner;
import com.mz.web.cache.CacheManageCallBack;
import com.mz.web.cache.CacheManageService;
import com.mz.core.constant.StringConstant;
import com.mz.web.article.service.AppBannerService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年8月20日 下午5:08:04 
 */
@Service("appBannerService")
public class AppBannerServiceImpl extends BaseServiceImpl<AppBanner, Long>
implements AppBannerService, CacheManageService {

	@Autowired
	private RedisService redisService;
	@Resource(name = "appBannerDao")
	@Override
	public void setDao(BaseDao<AppBanner, Long> dao) {
		super.dao = dao;
	}

	@Override
	public void initCache(CacheManageCallBack callback) {
		QueryFilter filter=new QueryFilter(AppBanner.class);
		filter.addFilter("status=", 0);
		filter.addFilter("isShow=", 1);
		List<AppBanner> list=this.find(filter);
		if(list!=null&&list.size()>0){
			redisService.save(StringConstant.CACHE_BANNER, JSON.toJSONString(list));
		}
		callback.callback(AppBannerService.class, StringConstant.CACHE_BANNER, "banner缓存");
		
	}

}
