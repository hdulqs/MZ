/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月31日 下午6:55:57
 */
package com.mz.account.fund.controller;

import com.mz.account.fund.model.AppBankCard;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.user.model.AppCustomer;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.user.service.AppCustomerService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月31日 下午6:55:57 
 */
@Controller
@RequestMapping("/fund/appbankcard")
public class AppBankCardController extends BaseController<AppBankCard, Long>{
	@Resource(name="appCustomerService")
	public AppCustomerService appCustomerService;


	@Resource(name="appBankCardService")
	@Override
	public void setService(BaseService<AppBankCard, Long> service) {
		super.service = service;
	}
	
    @MethodName(name = "查询卡号list")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
    	QueryFilter filter = new QueryFilter(AppBankCard.class, request);
		PageResult findPage = super.findPage(filter);
		List<AppBankCard> rows = findPage.getRows();
		for(AppBankCard appBankCard:rows){
			AppCustomer appCustomer = appCustomerService.getByCustomerId(appBankCard.getUserName());
			if(appCustomer!=null&&appCustomer.getPhone()!=null) {
				appBankCard.setMobile(appCustomer.getPhone());
			}
		}
    	return findPage;
	}
	
	
	
	
	
	
}
