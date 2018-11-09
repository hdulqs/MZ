/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月24日 下午2:09:25
 */
package com.mz.exchange.entrust.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.trade.entrust.service.ExOrderInfoService;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.util.StringUtil;


/**
 * <p> TODO</p>
 * @author: Gao Mimi
 * @Date :          2016年4月12日 下午4:45:50 
 */
@Controller
@RequestMapping("/entrust/exorderinfo")
public class ExOrderInfoController extends BaseController<ExOrderInfo, Long> {

  @Resource(name = "exOrderInfoService")
  @Override
  public void setService(BaseService<ExOrderInfo, Long> service) {
    super.service = service;
  }

  @Resource
  private ExOrderInfoService exorderInfoService;

  @MethodName(name = "查询订单")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(ExOrderInfo.class, request);
    String fixPriceCoinCode = request.getParameter("fixPriceCoinCode");
    if (!StringUtil.isEmpty(fixPriceCoinCode)) {
      filter.addFilter("fixPriceCoinCode=", fixPriceCoinCode);
    }
    filter.setOrderby("transactionTime desc");

    //	filter.addFilter("orderNum_groubby", "orderNum");
    PageResult findPage = super.findPage(filter);
    List<ExOrderInfo> list = (List<ExOrderInfo>) findPage.getRows();
    return findPage;
  }

  @MethodName(name = "查询订单平台收取费用台账")
  @RequestMapping("/listfees")
  @ResponseBody
  public PageResult listfees(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(ExOrderInfo.class, request);
    filter.setOrderby("transactionTime desc");
    PageResult findPage = super.findPage(filter);
    List<ExOrderInfo> list = (List<ExOrderInfo>) findPage.getRows();
		/*Map<String, String> params = new HashMap<String, String>();
		params.put("offset", filter.getPage() - 1 +"");
		params.put("limit", "10");
		FrontPage frontPage = exorderInfoService.selectFee(params);
		List<ExOrderInfo> list=(List<ExOrderInfo>)frontPage.getRows();*/
    for (ExOrderInfo l : list) {
      if (null != l.getTransactionFee()) {
        l.setTransactionFee(l.getTransactionFee().setScale(2, BigDecimal.ROUND_HALF_UP));
      }
      if (null != l.getTransactionPrice()) {
        l.setTransactionPrice(l.getTransactionPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
      }
      if (null != l.getTransactionCount()) {
        l.setTransactionCount(l.getTransactionCount().setScale(4, BigDecimal.ROUND_HALF_UP));
      }
      if (null != l.getTransactionSum()) {
        l.setTransactionSum(l.getTransactionSum().setScale(2, BigDecimal.ROUND_HALF_UP));
      }
      if (l.getTransactionFeeRate() == null) {
        l.setTransactionFeeRate(new BigDecimal(0));
      }
      if (l.getTransactionFee() == null) {
        l.setTransactionFee(new BigDecimal(0));
      }

    }
    return findPage;
  }
}
