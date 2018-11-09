/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: zenghao
 * @version: V1.0
 * @Date: 2016-11-24 16:36:09
 */
package com.mz.exchange.subscription.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.subscription.model.ExBuybackRecord;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.exchange.subscription.service.ExBuybackRecordService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author: zenghao
 * @version: V1.0
 * @Date: 2016-11-24 16:36:09
 */
@Controller
@RequestMapping("/subscription/exbuybackrecord")
public class ExBuybackRecordController extends BaseController<ExBuybackRecord, Long> {

  @Resource(name = "exBuybackRecordService")
  @Override
  public void setService(BaseService<ExBuybackRecord, Long> service) {
    super.service = service;
  }

  @MethodName(name = "查看ExBuybackRecord")
  @RequestMapping(value = "/see/{id}")
  @MyRequiresPermissions
  @ResponseBody
  public ExBuybackRecord see(@PathVariable Long id) {
    ExBuybackRecord exBuybackRecord = service.get(id);
    return exBuybackRecord;
  }

  @MethodName(name = "增加ExBuybackRecord")
  @RequestMapping(value = "/add")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request, ExBuybackRecord exBuybackRecord) {
    return super.save(exBuybackRecord);
  }

  @MethodName(name = "修改ExBuybackRecord")
  @RequestMapping(value = "/modify")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request, ExBuybackRecord exBuybackRecord) {
    return super.update(exBuybackRecord);
  }

  @MethodName(name = "删除ExBuybackRecord")
  @RequestMapping(value = "/remove/{ids}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult remove(@PathVariable String ids) {
    return super.deleteBatch(ids);
  }

  @MethodName(name = "列表ExBuybackRecord")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(ExBuybackRecord.class, request);
    filter.setOrderby("created desc");
    return super.findPage(filter);
  }

  @MethodName(name = "通过回购ExBuybackRecord")
  @RequestMapping(value = "/passed/{id}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult passed(@PathVariable Long id) {
    ExBuybackRecordService exBuybackRecordService = (ExBuybackRecordService) service;
    return exBuybackRecordService.passedRecord(id);
  }

  @MethodName(name = "驳回回购ExBuybackRecord")
  @RequestMapping(value = "/reject", method = RequestMethod.POST)
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult reject(HttpServletRequest req) {
    ExBuybackRecordService exBuybackRecordService = (ExBuybackRecordService) service;
    return exBuybackRecordService.rejectRecord(req);
  }


}
