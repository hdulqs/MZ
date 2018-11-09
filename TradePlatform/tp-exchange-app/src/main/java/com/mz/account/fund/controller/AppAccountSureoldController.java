/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: GaoMimi
 * @version: V1.0
 * @Date: 2016-10-11 14:37:42
 */
package com.mz.account.fund.controller;

import com.mz.account.fund.model.AppAccountSureold;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
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
 * @author: GaoMimi
 * @version: V1.0
 * @Date: 2016-10-11 14:37:42
 */
@Controller
@RequestMapping("/fund/appaccountsureold")
public class AppAccountSureoldController extends BaseController<AppAccountSureold, Long> {

  @Resource(name = "appAccountSureoldService")
  @Override
  public void setService(BaseService<AppAccountSureold, Long> service) {
    super.service = service;
  }

  @MethodName(name = "查看AppAccountSureold")
  @RequestMapping(value = "/see/{id}")
  @MyRequiresPermissions
  @ResponseBody
  public AppAccountSureold see(@PathVariable Long id) {
    AppAccountSureold appAccountSureold = service.get(id);
    return appAccountSureold;
  }

  @MethodName(name = "增加AppAccountSureold")
  @RequestMapping(value = "/add")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request, AppAccountSureold appAccountSureold) {
    return super.save(appAccountSureold);
  }

  @MethodName(name = "修改AppAccountSureold")
  @RequestMapping(value = "/modify")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request, AppAccountSureold appAccountSureold) {
    return super.update(appAccountSureold);
  }

  @MethodName(name = "删除AppAccountSureold")
  @RequestMapping(value = "/remove/{ids}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult remove(@PathVariable String ids) {
    return super.deleteBatch(ids);
  }

  @MethodName(name = "列表AppAccountSureold")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppAccountSureold.class, request);
    return super.findPage(filter);
  }

}
