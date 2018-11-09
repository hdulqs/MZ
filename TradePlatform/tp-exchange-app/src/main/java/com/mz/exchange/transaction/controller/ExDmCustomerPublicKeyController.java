/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0
 * @Date:        2016年3月28日 下午7:13:29
 */
package com.mz.exchange.transaction.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.transaction.model.ExDmCustomerPublicKey;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午7:13:29
 */
@Controller
@RequestMapping("/transaction/exdmcustomerpublickey")
public class ExDmCustomerPublicKeyController extends
		BaseController<ExDmCustomerPublicKey, Long> {

	@Resource(name = "exDmCustomerPublicKeyService")
	@Override
	public void setService(BaseService<ExDmCustomerPublicKey, Long> service) {
		super.service = service;
	}

	@RequestMapping("/list")
	@MethodName(name = "分页查询ExDmCustomerPublicKey")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(ExDmCustomerPublicKey.class,
				request);
		PageResult page = super.findPage(filter);
		return page;
	}



}
