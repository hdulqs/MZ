/**
 * Copyright:   北京互融时代软件有限公司
 * @author:       Liu Shilei 
 * @version:      V1.0 
 * @Date:        2015年9月28日10:59:57
 */
package com.mz.core.mvc.controller.config;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * <p> TODO</p>     缓存配置
 * @author:         Liu Shilei 
 * @Date :          2015年9月28日 上午10:59:13
 */
@Controller
@RequestMapping("/cacheController")
public class CacheController  {
	
	@MethodName(name="配置用户登录错误过多后的锁定时间")
	@RequestMapping("/configLoginLock")
	@ResponseBody
	public JsonResult configLoginLock(Long secondes,Model Model){
		
		 CacheManager cacheManager = CacheManager.getInstance();
		 Cache cache = cacheManager.getCache("passwordRetryCache"); 
	     CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
	     cacheConfiguration.setTimeToIdleSeconds(secondes);
	   //cache.disableDynamicFeatures();
	     
	     JsonResult jsonResult = new JsonResult();
	     jsonResult.setSuccess(true);
	     jsonResult.setMsg("设置成功");
	     return jsonResult;
	     
	}
	
	


}
