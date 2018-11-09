/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Gao Mimi
 * @version: V1.0
 * @Date: 2015年09月28日  18:10:04
 */
package com.mz.web.message.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppMessageCategory;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.web.message.service.AppMessageCategoryService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Gao Mimi
 * @Date : 2015年09月28日 18:10:04
 */

@Controller
@RequestMapping("/message/appmessagecategory")
public class AppMessageCategoryController extends BaseController<AppMessageCategory, Long> {

  @Resource(name = "appMessageCategoryService")
  @Override
  public void setService(BaseService<AppMessageCategory, Long> service) {
    super.service = service;
  }

  @MethodName(name = "修改一条文章数据")
  @RequestMapping("/modify")
  @ResponseBody
  public JsonResult modify(AppMessageCategory appMessageCategory) {
    JsonResult jsoreult = super.update(appMessageCategory);
    return jsoreult;
  }

  @MethodName(name = "增加一条文章数据")
  @RequestMapping("/add")
  @ResponseBody
  public JsonResult add(AppMessageCategory appMessageCategory) {

    JsonResult jsonResult = super.save(appMessageCategory);

    return jsonResult;
  }

  @MethodName(name = "删除一条文章数据数据")
  @RequestMapping("/remove/{ids}")
  @ResponseBody
  public JsonResult remove(@PathVariable Long[] ids) {
    AppMessageCategoryService appMessageCategoryService = (AppMessageCategoryService) service;
    JsonResult result = appMessageCategoryService.removeCategory(ids);
    return result;
  }

  @MethodName(name = "查看一篇文章")
  @RequestMapping("/see/{id}")
  @ResponseBody
  public AppMessageCategory see(@PathVariable String id) {
    return service.get(Long.valueOf(id));
  }

  @MethodName(name = "加载文章列表数据")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppMessageCategory.class, request);
    filter.setOrderby("modified  desc");
    filter.addFilter("state=", 1);
    PageResult page = super.findPage(filter);
    return page;
  }


  @MethodName(name = "加载文章列表数据")
  @RequestMapping("/selectList")
  @ResponseBody
  public List<AppMessageCategory> selectList(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppMessageCategory.class, request);
    filter.addFilter("state=", 1);
    List<AppMessageCategory> list = super.find(filter);
    return list;
  }

}
