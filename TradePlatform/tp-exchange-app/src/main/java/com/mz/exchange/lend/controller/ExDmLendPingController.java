package com.mz.exchange.lend.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.lend.model.ExDmPing;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.exchange.lend.service.ExDmLendPingService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/lend/exDmLendPing")
public class ExDmLendPingController extends BaseController<ExDmPing, Long> {

  @Resource(name = "exDmLendPingService")
  @Override
  public void setService(BaseService<ExDmPing, Long> service) {
    super.service = service;
  }

  @MethodName(name = "列表ExDmLendPing")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(ExDmPing.class, request);
    filter.setOrderby("created desc");
    PageResult see = ((ExDmLendPingService) service).see(filter);
    return see;
  }

}
