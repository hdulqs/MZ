/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年5月6日 上午11:15:40
 */
package com.mz.web.app.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.AppConfig;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.sys.ContextUtil;
import com.mz.web.cache.CacheManageCallBack;
import com.mz.web.cache.CacheManageService;
import com.mz.core.constant.StringConstant;
import com.mz.core.mvc.controller.base.BaseController;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Zhang Xiaofang
 * @Date : 2016年5月6日 上午11:15:40
 */
@Controller
@RequestMapping("/app/appcache")
public class AppCacheController extends BaseController<AppConfig, Long> {

	@Autowired
	private RedisService redisService;

	@Override
	public void setService(BaseService<AppConfig, Long> service) {

	}

	@MethodName(name = "查询缓存信息")
	@RequestMapping("/list")
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult list() {
		JsonResult jsonResult = new JsonResult();
		String str = redisService.get(StringConstant.CACHE_TYPE);
		if (null != str && !"".equals(str)) {
			jsonResult.setObj(str);
		}

		return jsonResult;
	}

	@MethodName(name = "查询数据并更新缓存")
	@RequestMapping("/update")
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult modify(HttpServletRequest request) {
		JsonResult result = new JsonResult();
		String val = request.getParameter("types");
		if (null != val && !"".equals(val)) {
			String[] types = val.split("\\,");
			for (int i = 0; i < types.length; i++) {
				if (null != types[i] && !"".equals(types[i])) {
					CacheManageCallBack cacheManageCallBack = (CacheManageCallBack) ContextUtil
							.getBean("cacheManageCallBack");
					CacheManageService cacheManageService = (CacheManageService) ContextUtil.getBean(types[i]);
					cacheManageService.initCache(cacheManageCallBack);
					result.setSuccess(true);
				}
			}
		}
		return result;
	}
}
