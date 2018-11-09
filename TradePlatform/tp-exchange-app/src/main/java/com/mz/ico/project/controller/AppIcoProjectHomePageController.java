/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-07-21 16:51:55
 */
package com.mz.ico.project.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.ico.project.model.AppIcoProjectHomePage;
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
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-07-21 16:51:55
 */
@Controller
@RequestMapping("/project/appicoprojecthomepage")
public class AppIcoProjectHomePageController extends BaseController<AppIcoProjectHomePage, Long> {

  @Resource(name = "appIcoProjectHomePageService")
  @Override
  public void setService(BaseService<AppIcoProjectHomePage, Long> service) {
    super.service = service;
  }

  @MethodName(name = "查看AppIcoProjectHomePage")
  @RequestMapping(value = "/see/{id}")
  @MyRequiresPermissions
  @ResponseBody
  public AppIcoProjectHomePage see(@PathVariable Long id) {
    AppIcoProjectHomePage appIcoProjectHomePage = service.get(id);
    return appIcoProjectHomePage;
  }

  @MethodName(name = "增加AppIcoProjectHomePage")
  @RequestMapping(value = "/add")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request, AppIcoProjectHomePage appIcoProjectHomePage) {
    return super.save(appIcoProjectHomePage);
  }

  @MethodName(name = "修改AppIcoProjectHomePage")
  @RequestMapping(value = "/modify")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request,
      AppIcoProjectHomePage appIcoProjectHomePage) {
    return super.update(appIcoProjectHomePage);
  }

  @MethodName(name = "删除AppIcoProjectHomePage")
  @RequestMapping(value = "/remove/{ids}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult remove(@PathVariable String ids) {
    return super.deleteBatch(ids);
  }

  @MethodName(name = "列表AppIcoProjectHomePage")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppIcoProjectHomePage.class, request);
    return super.findPage(filter);
  }


}
