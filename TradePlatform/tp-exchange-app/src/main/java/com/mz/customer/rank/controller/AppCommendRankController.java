/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-12-14 15:06:35 
 */
package com.mz.customer.rank.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.rank.model.AppCommendRank;
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
 * @Date:        2017-12-14 15:06:35 
 */
@Controller
@RequestMapping("/rank/appcommendrank")
public class AppCommendRankController extends BaseController<AppCommendRank, Long> {
	
	@Resource(name = "appCommendRankService")
	@Override
	public void setService(BaseService<AppCommendRank, Long> service) {
		super.service = service;
	}
	
	
	@MethodName(name = "查看AppCommendRank")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppCommendRank see(@PathVariable Long id){
		AppCommendRank appCommendRank = service.get(id);
		return appCommendRank;
	}
	
	@MethodName(name="增加AppCommendRank")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppCommendRank appCommendRank){
		return super.save(appCommendRank);
	}
	
	@MethodName(name="修改AppCommendRank")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppCommendRank appCommendRank){
		return super.update(appCommendRank);
	}
	
	@MethodName(name="删除AppCommendRank")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppCommendRank")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppCommendRank.class,request);
		return super.findPage(filter);
	}
	
	
}
