/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: liushilei
 * @version: V1.0
 * @Date: 2017-12-18 16:55:04
 */
package com.mz.customer.businessman.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.businessman.model.AppBusinessman;
import com.mz.customer.businessman.model.AppBusinessmanBank;
import com.mz.customer.businessman.service.AppBusinessmanService;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author: liushilei
 * @version: V1.0
 * @Date: 2017-12-18 16:55:04
 */
@Controller
@RequestMapping("/businessman/appbusinessmanbank")
public class AppBusinessmanBankController extends BaseController<AppBusinessmanBank, Long> {

  @Resource(name = "appBusinessmanBankService")
  @Override
  public void setService(BaseService<AppBusinessmanBank, Long> service) {
    super.service = service;
  }

  @Resource
  private AppBusinessmanService appBusinessmanService;


  @MethodName(name = "查看AppBusinessmanBank")
  @RequestMapping(value = "/see/{id}")
  @MyRequiresPermissions
  @ResponseBody
  public AppBusinessmanBank see(@PathVariable Long id) {
    AppBusinessmanBank appBusinessmanBank = service.get(id);
    return appBusinessmanBank;
  }

  @MethodName(name = "增加AppBusinessmanBank")
  @RequestMapping(value = "/add")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request, AppBusinessmanBank appBusinessmanBank) {

    QueryFilter filter2 = new QueryFilter(AppBusinessman.class);
    filter2.addFilter("bankcard=", appBusinessmanBank.getBankcard());
    AppBusinessmanBank appBusinessmanBank2 = service.get(filter2);
    if (appBusinessmanBank2 != null) {
      JsonResult jsonResult = new JsonResult();
      jsonResult.setMsg("银行卡号不能重复");
      return jsonResult;
    }

    QueryFilter filter = new QueryFilter(AppBusinessmanBank.class);
    filter.addFilter("businessmanId=", appBusinessmanBank.getBusinessmanId());
    List<AppBusinessmanBank> list = service.find(filter);
    if (list != null && list.size() > 0) {
      JsonResult jsonResult = new JsonResult();
      jsonResult.setMsg("只能绑定一张银行卡");
      return jsonResult;
    }

    return super.save(appBusinessmanBank);
  }

  @MethodName(name = "修改AppBusinessmanBank")
  @RequestMapping(value = "/modify")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request, AppBusinessmanBank appBusinessmanBank) {

    QueryFilter filter = new QueryFilter(AppBusinessmanBank.class);
    filter.addFilter("businessmanId=", appBusinessmanBank.getBusinessmanId());
    List<AppBusinessmanBank> list = service.find(filter);
    if (list != null && list.size() > 0) {
      if (appBusinessmanBank.getId().compareTo(list.get(0).getId()) != 0) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setMsg("只能绑定一张银行卡");
        return jsonResult;

      }
    }

    return super.update(appBusinessmanBank);
  }

  @MethodName(name = "删除AppBusinessmanBank")
  @RequestMapping(value = "/remove/{ids}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult remove(@PathVariable String ids) {
    return super.deleteBatch(ids);
  }

  @MethodName(name = "列表AppBusinessmanBank")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppBusinessmanBank.class, request);
    PageResult page = super.findPage(filter);
    List<AppBusinessmanBank> rows = page.getRows();
    if (rows != null && rows.size() > 0) {
      for (AppBusinessmanBank bank : rows) {
        if (bank.getBusinessmanId() != null) {
          AppBusinessman appBusinessman = appBusinessmanService.get(bank.getBusinessmanId());
          if (appBusinessman != null) {
            bank.setBusinessmanTrueName(appBusinessman.getTrueName());
          }
        }
      }
    }

    return page;
  }


}
