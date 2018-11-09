/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年4月29日 下午5:02:03
 */
package com.mz.web.article.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppBanner;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.web.article.service.AppBannerService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年4月29日 下午5:02:03
 */

@Controller
@RequestMapping("/article/appbanner")
public class AppBannerController extends BaseController<AppBanner, Long> {

  @Resource(name = "appBannerService")
  @Override
  public void setService(BaseService<AppBanner, Long> service) {
    super.service = service;
  }


  @MethodName(name = "修改banner")
  @RequestMapping("/modify")
  @ResponseBody
  // BindingResult result
  public JsonResult modify(AppBanner appBanner) {
    if (appBanner.getRemark2() == null) {
      appBanner.setRemark2("");
    }
    JsonResult jsoreult = super.update(appBanner);
    return jsoreult;
  }

  @MethodName(name = "上传banner")
  @RequestMapping("/add")
  @ResponseBody
  public JsonResult add(AppBanner appBanner) {

    JsonResult jsonResult = super.save(appBanner);

    return jsonResult;
  }

  @MethodName(name = "删除banner")
  @RequestMapping("/remove")
  @ResponseBody
  public JsonResult remove(@RequestParam(value = "ids[]") Long[] ids) {
    AppBannerService aservice = (AppBannerService) service;
    JsonResult jsonResult = new JsonResult();
    //boolean s = aservice.delete(ids[0]);
    boolean s = true;
    for (int i = 0; i < ids.length; i++) {
      s = aservice.delete(ids[i]);
    }
    if (s) {
      jsonResult.setSuccess(true);
      jsonResult.setMsg("删除成功");
    } else {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("删除失败");
    }
    return jsonResult;
  }

  @MethodName(name = "查banner")
  @RequestMapping("/see/{id}")
  @ResponseBody
  public AppBanner see(@PathVariable Long id) {
    return service.get(id);
  }

  @MethodName(name = "加载banner列表")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppBanner.class, request);
    filter.setOrderby("sort asc");

    return super.findPage(filter);
  }


}
