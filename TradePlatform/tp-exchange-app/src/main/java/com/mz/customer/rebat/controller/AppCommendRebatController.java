/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 17:40:59 
 */
package com.mz.customer.rebat.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.rebat.model.AppCommendRebat;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 17:40:59 
 */
@Controller
@RequestMapping("/rebat/appcommendrebat")
public class AppCommendRebatController extends BaseController<AppCommendRebat, Long> {
	
	@Resource(name = "appCommendRebatService")
	@Override
	public void setService(BaseService<AppCommendRebat, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppCommendRebat")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppCommendRebat see(@PathVariable Long id){
		AppCommendRebat appCommendRebat = service.get(id);
		return appCommendRebat;
	}
	
	@MethodName(name = "列表AppCommendRebat")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppCommendRebat.class,request);
		filter.setOrderby("created desc");
		PageResult page = super.findPage(filter);
		return page;
	}
	
	
	
	
}
