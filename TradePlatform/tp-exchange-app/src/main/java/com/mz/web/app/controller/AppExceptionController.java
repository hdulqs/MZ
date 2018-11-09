/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Gao Mimi
 * @version: V1.0
 * @Date: 2015年11月04日  18:23:33
 */
package com.mz.web.app.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.log.AppException;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.core.mvc.service.log.AppExceptionService;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
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
 * @author: Yuan Zhicheng
 * @Date :   2015年11月04日  18:23:33     
 */

@Controller
@RequestMapping("/log/appexception")
public class AppExceptionController extends BaseController<AppException, Long> {

  @Resource(name = "appExceptionService")
  @Override
  public void setService(BaseService<AppException, Long> service) {
    super.service = service;
  }

  private AppExceptionService appExceptionService;

  /**
   *AppExceptionController自己的initBinder
   *
   * @param binder
   */
  @InitBinder
  public void initBinderDemoUser(ServletRequestDataBinder binder) {

  }

  @MyRequiresPermissions
  @MethodName(name = "加载一条异常数据")
  @RequestMapping(value = "/load/{id}", method = RequestMethod.GET)
  @ResponseBody
  public AppException load(@PathVariable Long id) {
    AppException appException = service.get(id);
    return appException;
  }

  @MethodName(name = "批量加载异常数据")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    appExceptionService = (AppExceptionService) service;
    QueryFilter filter = new QueryFilter(AppException.class, request);
    return appExceptionService.findPageResult(filter);
//    	MongoUtil<AppException, Long> mongUtil = new MongoUtil<AppException, Long>(AppException.class);
//    	MongoQueryFilter mongoQueryFilter = new MongoQueryFilter(request);
//    	return mongUtil.findPage(mongoQueryFilter);
  }

}
