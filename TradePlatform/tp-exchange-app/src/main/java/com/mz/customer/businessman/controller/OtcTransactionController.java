/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: liushilei
 * @version: V1.0
 * @Date: 2017-12-07 14:06:38
 */
package com.mz.customer.businessman.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.businessman.model.OtcTransaction;
import com.mz.customer.businessman.service.AppBusinessmanService;
import com.mz.customer.businessman.service.OtcTransactionService;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.person.service.AppPersonInfoService;
import com.mz.util.QueryFilter;
import com.mz.account.fund.service.AppBankCardService;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright: 北京互融时代软件有限公司
 *
 * @author: liushilei
 * @version: V1.0
 * @Date: 2017-12-07 14:06:38
 */
@Controller
@RequestMapping("/businessman/otctransaction")
public class OtcTransactionController extends BaseController<OtcTransaction, Long> {

  @Resource(name = "otcTransactionService")
  @Override
  public void setService(BaseService<OtcTransaction, Long> service) {
    super.service = service;
  }

  @Resource
  private AppBusinessmanService appBusinessmanService;

  @Resource
  private AppPersonInfoService appPersonInfoService;

  @Resource
  private AppBankCardService appBankCardService;


  @MethodName(name = "增加otcTransaction")
  @RequestMapping(value = "/add")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request, OtcTransaction otcTransaction) {
    return super.save(otcTransaction);
  }

  @MethodName(name = "修改otcTransaction")
  @RequestMapping(value = "/modify")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request, OtcTransaction otcTransaction) {
    return super.update(otcTransaction);
  }

  @MethodName(name = "删除otcTransaction")
  @RequestMapping(value = "/remove/{ids}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult remove(@PathVariable String ids) {
    return super.deleteBatch(ids);
  }

  /**
   * 关闭交易
   */
  @RequestMapping(value = "/close/{ids}")
  @ResponseBody
  public JsonResult close(@PathVariable String ids) {
    JsonResult jsonResult = new JsonResult();

    String[] arr = ids.split(",");
    for (String id : arr) {
      jsonResult = ((OtcTransactionService) service).OtcListclose(Long.valueOf(id));
    }

    return jsonResult;
  }


  /**
   * 确认交易
   */
  @RequestMapping(value = "/confirm/{ids}")
  @ResponseBody
  public JsonResult confirm(@PathVariable String ids) {

    JsonResult jsonResult = new JsonResult();

    String[] arr = ids.split(",");
    for (String id : arr) {
      jsonResult = ((OtcTransactionService) service).confirm(Long.valueOf(id));
    }

    return jsonResult;
  }

  @MethodName(name = "列表otcTransaction")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(OtcTransaction.class, request);
    filter.setOrderby(" id desc");
    PageResult findPage = super.findPage(filter);
    List<OtcTransaction> rows = findPage.getRows();
    if (rows != null && rows.size() > 0) {
      for (OtcTransaction ct : rows) {

        Integer status = ct.getStatus();
        switch (status) {
          case 1:
            ct.setStatusByDes("待成交");
            break;
          case 2:
            ct.setStatusByDes("已完成");
            break;
          case 3:
            ct.setStatusByDes("交易取消");
            break;
          case 4:
            ct.setStatusByDes("部分交易");
            break;
          case 5:
            ct.setStatusByDes("全部交易");
            break;
          case 6:
            ct.setStatusByDes("部分完成");
            break;
          default:
            ct.setStatusByDes("");
        }

        Integer type = ct.getTransactionType();
        switch (type) {
          case 1:
            ct.setTransactionTypeByDes("买");
            break;
          case 2:
            ct.setTransactionTypeByDes("卖");
            break;
          default:
            ct.setTransactionTypeByDes("");
        }

        AppPersonInfo appPersonInfo = appPersonInfoService.getByCustomerId(ct.getCustomerId());
        if (null != appPersonInfo && null != appPersonInfo.getTrueName() && null != appPersonInfo
            .getSurname()) {
          ct.setAllName(appPersonInfo.getSurname() + appPersonInfo.getTrueName());
        }


      }
    }
    return findPage;
  }

}
