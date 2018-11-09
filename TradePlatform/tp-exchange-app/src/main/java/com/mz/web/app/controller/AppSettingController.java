/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Zhang Xiaofang
 * @version: V1.0
 * @Date: 2016年4月1日 下午12:57:03
 */
package com.mz.web.app.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppSetting;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author: Zhang Xiaofang
 * @Date :          2016年3月30日 上午11:43:28 
 */
@Controller
@RequestMapping("/app/appsetting")
public class AppSettingController extends BaseController<AppSetting, Long> {


  @Resource(name = "appSettingService")
  @Override
  public void setService(BaseService<AppSetting, Long> service) {
    super.service = service;
  }


  @MethodName(name = "分页查询AppSetting")
  @RequestMapping("/list")
  @ResponseBody
  @MyRequiresPermissions
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppSetting.class, request);
    return super.findPage(filter);
  }

  @MethodName(name = "删除一条AppSetting数据")
  @RequestMapping("/remove/{id}")
  @ResponseBody
  @MyRequiresPermissions
  public JsonResult remove(@PathVariable String id) {
    QueryFilter filter = new QueryFilter(AppSetting.class);
    if (null != id) {
      filter.addFilter("id_in", id);
      return super.delete(filter);
    }
    return null;


  }

  @MethodName(name = "添加一条AppSetting数据")
  @RequestMapping("/add")
  @ResponseBody
  @MyRequiresPermissions
  public JsonResult add(AppSetting clientSetting) {

    return super.save(clientSetting);

  }

  @MethodName(name = "修改AppSetting数据")
  @RequestMapping("/modify")
  @ResponseBody
  @MyRequiresPermissions
  public JsonResult modify(AppSetting clientSetting) {
    return super.update(clientSetting);
  }

  @MethodName(name = "查看一条AppSetting数据")
  @RequestMapping(value = "/see/{id}", method = RequestMethod.GET)
  @ResponseBody
  @MyRequiresPermissions
  public AppSetting see(@PathVariable Long id) {

    return service.get(id);
  }

  @MethodName(name = "查询所有AppSetting数据")
  @RequestMapping("/selectAll")
  @ResponseBody
  public List<AppSetting> selectAll() {

    return service.findAll();
  }

}