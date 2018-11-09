/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月26日 下午4:09:36
 */
package com.mz.thirdpay.biz.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.thirdpay.AppLogThirdPay;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年7月26日 下午4:09:36 
 */

@RequestMapping("/pay/applogthirdpay")
@Controller
public class AppLogThirdPayController extends BaseController<AppLogThirdPay, Long>{

	
	@Resource(name = "appLogThirdPayService")
	@Override
	public void setService(BaseService<AppLogThirdPay, Long> service) {
		super.service = service;
	}

	
	@ResponseBody
	@RequestMapping("/list")
	@MethodName(name="查询第三方支付的日志")
	public PageResult list(HttpServletRequest request) {
		QueryFilter  filter=new QueryFilter(AppLogThirdPay.class,request);
		  filter.setOrderby("created  desc");
		return super.findPage(filter);
    }
	
	
	
	
	
}
