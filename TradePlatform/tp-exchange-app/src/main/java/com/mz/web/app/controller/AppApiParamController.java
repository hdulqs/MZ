/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年4月29日 下午5:02:03
 */
package com.mz.web.app.controller;


import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppApi;
import com.mz.web.app.model.AppApiParam;
import com.mz.web.app.service.AppApiParamService;
import com.mz.web.app.service.AppApiService;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * <p> TODO</p>
 * @author: Zhang Xiaofang
 * @Date :          2016年8月22日 下午4:49:45
 */
@Controller
@RequestMapping("/app/appapiparam")
public class AppApiParamController extends BaseController<AppApiParam, Long> {

  @Resource(name = "appApiParamService")
  @Override
  public void setService(BaseService<AppApiParam, Long> service) {
    super.service = service;
  }


  @Resource(name = "appApiService")
  private AppApiService appApiService;


  @MethodName(name = "修改api参数")
  @RequestMapping("/modify")
  @ResponseBody
  // BindingResult result
  public JsonResult modify(AppApiParam appApiParam) {
    JsonResult jsoreult = super.update(appApiParam);
    return jsoreult;
  }

  @MethodName(name = "添加api参数")
  @RequestMapping("/add")
  @ResponseBody
  public JsonResult add(AppApiParam appApiParam) {

    JsonResult jsonResult = super.save(appApiParam);

    return jsonResult;
  }

  @MethodName(name = "删除api参数")
  @RequestMapping("/remove")
  @ResponseBody
  public JsonResult remove(@RequestParam(value = "ids[]") Long[] ids) {
    AppApiParamService aservice = (AppApiParamService) service;
    JsonResult jsonResult = new JsonResult();
    boolean s = aservice.delete(ids[0]);
    if (s) {
      jsonResult.setSuccess(true);
      jsonResult.setMsg("删除成功");
    } else {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("删除失败");
    }
    return jsonResult;
  }


  @MethodName(name = "查看api接口的参数")
  @RequestMapping("/see/{id}")
  @ResponseBody
  public AppApiParam see(@PathVariable Long id) {
    return service.get(id);
  }


  @MethodName(name = "加载api接口列表")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    String apiId = request.getParameter("apiId");

    //if(null!=apiId&&!"".equals(apiId)){
    QueryFilter filter = new QueryFilter(AppApiParam.class, request);
    filter.setOrderby("sort asc");
    filter.addFilter("apiId=", apiId);
    return super.findPage(filter);
	/*	}
		return null;*/

  }


  @MethodName(name = "加载api接口列表")
  @RequestMapping("/findlist")
  @ResponseBody
  public List<AppApi> findlist(HttpServletRequest request) {

    QueryFilter filter = new QueryFilter(AppApi.class, request);
    filter.setOrderby("sort asc");
    return appApiService.find(filter);

  }


}
