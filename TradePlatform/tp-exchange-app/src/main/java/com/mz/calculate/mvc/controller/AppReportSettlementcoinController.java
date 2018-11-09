/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Wu Shuiming
 * @version:   V1.0 
 * @Date:      2016年9月6日 下午6:52:17
 */
package com.mz.calculate.mvc.controller;

import com.mz.calculate.settlement.model.AppReportSettlementcoin;
import com.mz.calculate.util.DateUtil;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.product.model.ExProduct;
import com.mz.util.QueryFilter;
import com.mz.calculate.mvc.service.AppReportSettlementcoinService;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Wu shuiming
 * @date 2016年9月6日 下午6:52:17
 */                  
@RequestMapping(value="/appReportSettlementcoin")
@Controller
public class AppReportSettlementcoinController extends BaseController<AppReportSettlementcoin, Long>{

	@Resource(name="appReportSettlementcoinService")
	@Override
	public void setService(BaseService<AppReportSettlementcoin, Long> service) {
		super.service = service;
		
	}
	
	
	@MethodName(name = "查询AppReportSettlementcoin所有的")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		                                        
		String modified = request.getParameter("modified");
		String coinCode = request.getParameter("coinCode");
		QueryFilter filter = new QueryFilter(AppReportSettlementcoin.class, request);
		if(null == modified){
			String nowDate = DateUtil.getNowDate();
			filter.addFilter("modified_like", nowDate+"%");
		}else{
			filter.addFilter("modified_like", DateUtil.StringDateToString(modified)+"%");
		}
		if(null != coinCode && ""!= coinCode){
			filter.addFilter("coinCode=", coinCode);
		}
		filter.setOrderby("modified desc"); 
		PageResult findPage = super.findPage(filter);
		return findPage;
	}
	
	/**
	 * 查询下拉框中的所有产品
	 * 
	 * @return
	 */
	@MethodName(name = "查询下拉框中所有的产品")
	@RequestMapping("/getSelectProduct")
	@ResponseBody
	public List<ExProduct> getSelectProduct(){
		AppReportSettlementcoinService appReportSettlementcoinService = (AppReportSettlementcoinService)service;
		List<ExProduct> list = appReportSettlementcoinService.getSelectProduct();
		return list;
	}
	


}
