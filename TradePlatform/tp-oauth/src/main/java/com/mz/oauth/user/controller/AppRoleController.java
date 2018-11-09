/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月21日 上午11:30:01
 */
package com.mz.oauth.user.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.service.log.AppExceptionService;
import com.mz.shiro.service.AppResourceService;
import com.mz.shiro.service.AppRoleService;
import com.mz.shiro.service.AppUserService;
import com.mz.util.QueryFilter;
import com.mz.util.SetPropertyEditorSupport;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.oauth.user.model.AppResource;
import com.mz.oauth.user.model.AppRole;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年9月21日 上午11:30:01 
 */
@Controller
@RequestMapping("/user/approle")
public class AppRoleController extends BaseController<AppRole, Long> {
	
	@Resource(name = "appRoleService")
	@Override
	public void setService(BaseService<AppRole, Long> service) {
		super.service = service;
	}
	
	@Resource
	private AppResourceService appResourceService;
	
	
	/**
	 * User自己的initBinder
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinderDemoUser(ServletRequestDataBinder binder) {
		// 由于用户里面的角色信息是个Set类型，所以这地方要转一下才行，前台传递过来的是个一个字符串，类似"1,2,3"这种，后台要自动转成Set
		binder.registerCustomEditor(Set.class, "appResourceSet", new SetPropertyEditorSupport(
        AppResource.class));
		
	}
	
	
	@MethodName(name = "查看")
	@RequestMapping(value="/see/{id}",method=RequestMethod.GET)
	@MyRequiresPermissions 
	@ResponseBody
	public AppRole see(@PathVariable Long id){
		AppRole appRole = service.get(id);
		appRole.setAppResourceSet(((AppRoleService)service).getAppResourceSet(appRole));
		return appRole;
	}
	
	@MethodName(name="增加")
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppRole appRole){
		return ((AppRoleService) service).add(request,appRole);
	}
	
	@MethodName(name="修改")
	@RequestMapping(value="/modify", method = RequestMethod.POST)
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppRole appRole){
		return ((AppRoleService) service).modify(request,appRole);
	}
	
	@MethodName(name="删除")
	@RequestMapping(value="/remove/{ids}",method=RequestMethod.DELETE)
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String[] ids){
		return ((AppRoleService) service).remove(ids);
	}
	
	@MethodName(name = "列表")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppRole.class,request);
		filter.addFilter("type!=", "subcompany");
		return super.findPage(filter);
	}
	
	@MethodName(name = "查找roles列表,返回json,用于userAdd.jsp页面")
	@RequestMapping("/findToJsonOnUserAdd")
	@ResponseBody
	public List	findToJsonOnUserAdd(HttpServletRequest request){
		return super.service.findAll();
	}
	
	
	@MethodName(name = "查找角色拥有的权限和未拥有的权限")
	@RequestMapping("/findMyResourceAndNohasResource")
	@ResponseBody
	public Map<String,List<AppResource>> findMyResourceAndNohasResource(String id){
		List<AppResource> has = null;
		AppRole appRole = service.get(Long.valueOf(id));
		has = new ArrayList<AppResource>(((AppRoleService)service).getAppResourceSet(appRole));
		List<AppResource> allResource = appResourceService.findAll();
		
		Map<String,List<AppResource>> map = new HashMap<String,List<AppResource>>();
		map.put("has", has);
		map.put("all", allResource);
		
		return map;
		
	}
	
	@Resource
	private AppExceptionService appExceptionService;
	
	@Resource
	private AppUserService appUserService;
	
	@MethodName(name = "testAdd")
	@RequestMapping("/testAdd")
	@ResponseBody
	public JsonResult  testAdd(HttpServletRequest request){
		
		return super.get(Long.valueOf("20"));
		
//		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//		System.out.println("报时:"+format.format(new Date()));
//		
//		List<AppUser> findAll = appUserService.findAll();
//		List<AppException> list = appExceptionService.findAll();
//		System.out.println(list.size());
//		JsonResult jsonResult = new JsonResult();
//		System.out.println("报时:"+format.format(new Date()));
//		
//		return  jsonResult;
		
	}
	
	public boolean compare(BigDecimal money) {
		if(money.compareTo(new BigDecimal("1000"))<0){
			
			Thread thread = Thread.currentThread();
			try {
				thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			return true;
		}
		return false;
	}
	
	
	//------------------------------------

	
	
	
}
