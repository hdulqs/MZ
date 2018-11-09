/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年7月6日 下午6:09:23
 */
package com.mz.customer.agents.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.agents.model.AppTradeFactorage;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.agents.service.AppTradeFactorageService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author: Wu Shuiming
 * @Date :          2016年7月6日 下午6:09:23 
 */

@Controller
@RequestMapping("/agents/tradeFactorage")
public class AppTradeFactorageController extends BaseController<AppTradeFactorage, Long> {

  @Resource(name = "appTradeFactorageService")
  @Override
  public void setService(BaseService<AppTradeFactorage, Long> service) {
    super.service = service;
  }

  @MethodName(name = "查询佣金的列表")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppTradeFactorage.class,
        request);
    //	filter.addFilter("states!=", 1);
    PageResult page = super.findPage(filter);
    return page;
  }

  @MethodName(name = "添加一条佣金信息")
  @RequestMapping("/add")
  @ResponseBody
  public JsonResult add(AppTradeFactorage appTradeFactorage) {
    JsonResult result = super.save(appTradeFactorage);
    return result;
  }

  @MethodName(name = "修改代理参数表")
  @RequestMapping("/modified")
  @ResponseBody
  public JsonResult modified(AppTradeFactorage appTradeFactorage) {

    return super.update(appTradeFactorage);

  }

  @MethodName(name = "删除某个代理参数")
  @RequestMapping("/remove/{id}")
  @ResponseBody
  public JsonResult remove(@PathVariable Long id) {

    AppTradeFactorageService appTradeFactorageService = (AppTradeFactorageService) service;
    AppTradeFactorage tradeFactorage = appTradeFactorageService.get(id);
    // tradeFactorage.setStates(0);

    return super.update(tradeFactorage);

  }

  @MethodName(name = "根据id查看某个对象的方法")
  @RequestMapping(value = "/see/{id}", method = RequestMethod.GET)
  @ResponseBody
  public AppTradeFactorage see(@PathVariable Long id) {

    AppTradeFactorageService appTradeFactorageService = (AppTradeFactorageService) service;

    AppTradeFactorage tradeFactorage = appTradeFactorageService.get(id);

    return tradeFactorage;

  }

}
