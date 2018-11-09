/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 16:07:54 
 */
package com.mz.customer.deploy.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.deploy.model.AppCommendDeploy;
import com.mz.util.QueryFilter;
import com.mz.web.menu.model.AppMenuTree;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.web.menu.service.AppMenuTreeService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 16:07:54 
 */
@Controller
@RequestMapping("/deploy/appcommenddeploy")
public class AppCommendDeployController extends BaseController<AppCommendDeploy, Long> {
	
	@Resource(name = "appCommendDeployService")
	@Override
	public void setService(BaseService<AppCommendDeploy, Long> service) {
		super.service = service;
	}
	@Resource
	private AppMenuTreeService appMenuTreeService;
	
	@MethodName(name = "查看AppCommendDeploy")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppCommendDeploy see(@PathVariable Long id){
		AppCommendDeploy appClommendDeploy = service.get(id);
		QueryFilter filter = new QueryFilter(AppMenuTree.class);
		filter.addFilter("okey=","customer_customer_AppCommendDeploy_editbuy");
		filter.or("okey=","customer_customer_AppCommendDeploy_editsell");
		return appClommendDeploy;
	}
	
	@MethodName(name="增加AppCommendDeploy")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppCommendDeploy appCommendDeploy){
		if(appCommendDeploy.getStates()==null){
			appCommendDeploy.setStates(1);
		}
		JsonResult rest=new JsonResult();
		QueryFilter qf=new QueryFilter(AppCommendDeploy.class);
		qf.addFilter("states=",appCommendDeploy.getStates());
		List<AppCommendDeploy> find = service.find(qf);
		if(find.size()>0){
			rest.setSuccess(false);
		}else{
			super.save(appCommendDeploy);
			rest.setSuccess(true);
		}
		return  rest;
				
	}
	
	@MethodName(name="修改AppCommendDeploy")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppCommendDeploy appCommendDeploy){
		AppCommendDeploy appcommend = service.get(appCommendDeploy.getId());
		JsonResult rest=new JsonResult();
		if(appcommend!=null){
			BeanUtils.copyProperties(appCommendDeploy,appcommend);
			QueryFilter filter = new QueryFilter(appCommendDeploy.getClass());
			filter.addFilter("id!=",appcommend.getId());
			filter.addFilter("states=",appcommend.getStates());
			List<AppCommendDeploy> appCommendDeploys = service.find(filter);
			if(null!=appCommendDeploys&&appCommendDeploys.size()>0){
				rest.setSuccess(false);
			}else{
				service.update(appcommend);
				rest.setSuccess(true);
			}
		}else{
			rest.setSuccess(false);
		}
		return  rest;
	}
	
	@MethodName(name="删除AppCommendDeploy")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppCommendDeploy")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppCommendDeploy.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
