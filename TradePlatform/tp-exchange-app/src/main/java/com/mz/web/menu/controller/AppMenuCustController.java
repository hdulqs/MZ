/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年10月13日 上午10:26:28
 */
package com.mz.web.menu.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.menu.model.AppMenuCust;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.web.menu.service.AppMenuCustService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Liu Shilei
 * @Date : 2015年10月13日 上午10:26:28
 */
@Controller
@RequestMapping("/menu/appmenucust")
public class AppMenuCustController extends BaseController<AppMenuCust, Long> {


  @Resource(name = "appMenuCustService")
  @Override
  public void setService(BaseService<AppMenuCust, Long> service) {
    super.service = service;
  }

  @MethodName(name = "返回客户的所有菜单和功能,不查权限,树")
  @RequestMapping("/findAll")
  @ResponseBody
  public List<AppMenuCust> findAll(HttpServletRequest request) {
    QueryFilter queryFilter = new QueryFilter(AppMenuCust.class, request);
    //queryFilter.addFilter("type!=", "function");
    return super.find(queryFilter);
  }

  @MethodName(name = "返回客户的所有菜单和功能,不查权限,dataTabel")
  @RequestMapping("/findAllPage")
  @ResponseBody
  public PageResult findAllPage(HttpServletRequest request) {
    QueryFilter queryFilter = new QueryFilter(AppMenuCust.class, request);
    queryFilter.addFilter("type!=", "function");
    return super.findPage(queryFilter);
  }


  @MethodName(name = "增加")
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult add(HttpServletRequest request, String ids) {

    return ((AppMenuCustService) service).add(ids);

  }


  @MethodName(name = "删除")
  @RequestMapping(value = "/remove", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult remove(HttpServletRequest request, String ids) {
    return ((AppMenuCustService) service).remove(ids);
  }


  @MethodName(name = "设置菜单显示与不显示")
  @RequestMapping(value = "/isVisible", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult isVisible(HttpServletRequest request, String ids) {
    return ((AppMenuCustService) service).isVisible(ids);
  }


}
