 /**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年11月04日  18:23:33
 */
package com.mz.web.app.controller;

 import com.mz.core.annotation.MyRequiresPermissions;
 import com.mz.core.annotation.base.MethodName;
 import com.mz.core.mvc.model.log.AppLog;
 import com.mz.core.mvc.model.page.PageResult;
 import com.mz.core.mvc.service.base.BaseService;
 import com.mz.core.mvc.service.log.AppLogService;
 import com.mz.util.QueryFilter;
 import com.mz.core.mvc.controller.base.BaseController;
 import java.util.List;
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
 * @author:  Yuan Zhicheng        
 * @Date :   2015年11月04日  18:23:33     
 */

@Controller
@RequestMapping("/log/applog")
public class AppLogController extends BaseController<AppLog, Long> {
	
	@Resource(name = "appLogService")
	@Override
	public void setService(BaseService<AppLog, Long> service) {
		super.service = service;
	}
	private AppLogService appLogService ;
	/**
	 *AppLogController自己的initBinder
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinderDemoUser(ServletRequestDataBinder binder) {
		
	}
	
	
	 @MyRequiresPermissions
	 @MethodName(name = "加载一条操作日志")
	 @RequestMapping(value="/load/{id}",method=RequestMethod.GET)
	 @ResponseBody
	 public AppLog load(@PathVariable Long id) {
		 AppLog AppLog = service.get(id);
	        return AppLog;
	    }
	
    @MethodName(name = "批量加载操作日志")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
//    	MongoUtil<AppLog, Long> mongoUtil = new MongoUtil<AppLog, Long>(AppLog.class);
//    	MongoQueryFilter mongoQueryFilter = new MongoQueryFilter(request);
//    	mongoQueryFilter.addFilter("", "%get%");
//    	mongoQueryFilter.setOrderby("systemTime", "desc");
    	appLogService=(AppLogService)service;
    	QueryFilter filter=new QueryFilter(AppLog.class,request);
    	filter.setOrderby("systemTime desc");
    	return appLogService.findPageResult(filter);
	}
	
    
    @MethodName(name = "批量加载异常数据")
	@RequestMapping("/findAll")
	@ResponseBody
    public List<AppLog>  findAll(){
    	QueryFilter filter = new QueryFilter(AppLog.class);
    	filter.addFilter("id_in", "18645,18646");
    	return find(filter);
    }
    
}
