/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年8月11日 下午8:57:08
 */
package com.mz.exchange.purse.controller;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.user.model.AppCustomer;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.exchange.purse.CoinInterfaceUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年8月11日 下午8:57:08 
 */
@Controller
@RequestMapping("purse/cointransaction")
public class CoinTransactionController extends BaseController<AppCustomer, Long>{

	
	@Override
	public void setService(BaseService<AppCustomer, Long> service) {
		// TODO Auto-generated method stub
		
	}

	
	@RequestMapping("/list")
	@ResponseBody
	public String list() {
		
		String ss=CoinInterfaceUtil.list("LTC");
        //System.out.println(ss);
		return  ss;
	}
	
	
}
