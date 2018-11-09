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
import com.mz.customer.agents.model.AngestAsMoney;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.agents.service.AngestAsMoneyService;
import com.mz.customer.agents.service.CommissionDeployService;
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
@RequestMapping("/agents/angestasmoney")
public class AngestAsMoneyController extends BaseController<AngestAsMoney, Long> {

  @Resource(name = "angestAsMoneyService")
  @Override
  public void setService(BaseService<AngestAsMoney, Long> service) {
    super.service = service;
  }

  @Resource(name = "commissionDeployService")
  public CommissionDeployService commissionDeployService;

  @MethodName(name = "查询佣金比例配置的信息")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AngestAsMoney.class,
        request);
    filter.addFilter("states=", 1);
    PageResult page = super.findPage(filter);
    return page;
  }


  @MethodName(name = "添加一条佣金信息")
  @RequestMapping("/add")
  @ResponseBody
  public JsonResult add(AngestAsMoney angestAsMoney) {
    JsonResult result = super.save(angestAsMoney);
    return result;
  }

  @MethodName(name = "修改代理参数表")
  @RequestMapping("/modified")
  @ResponseBody
  public JsonResult modified(AngestAsMoney angestAsMoney) {

    return super.update(angestAsMoney);

  }


  @MethodName(name = "根据id查看某个对象的方法")
  @RequestMapping(value = "/see/{id}", method = RequestMethod.GET)
  @ResponseBody
  public AngestAsMoney see(@PathVariable Long id) {

    AngestAsMoneyService angestAsMoneyService = (AngestAsMoneyService) service;

    AngestAsMoney angestAsMoney = angestAsMoneyService.get(id);

    return angestAsMoney;

  }


  @MethodName(name = "根据id查看某个对象的方法")
  @RequestMapping(value = "/postMoney", method = RequestMethod.GET)
  @ResponseBody
  public JsonResult postMoney(HttpServletRequest req) {
    Long id = Long.valueOf(req.getParameter("id"));
    String fixPriceCoinCode = req.getParameter("fixPriceCoinCode");
    //  Integer money = Integer.valueOf(req.getParameter("money"));

    //如果佣金结算金额不一致，以最大值为准（暂时注掉）
    //BigDecimal count = commissionDeployService.getStandardMoney();

    AngestAsMoneyService angestAsMoneyService = (AngestAsMoneyService) service;

    JsonResult result = angestAsMoneyService.postMoneyById(id, null, fixPriceCoinCode);

    return result;

  }


}
