/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:09:25
 */
package com.mz.exchange.lend.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.lend.model.ExDmLendIntent;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.exchange.lend.service.ExDmLendIntentService;
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
 * @Date : 2016年3月24日 下午2:09:25
 */
@Controller
@RequestMapping("/lend/exdmLendIntent")
public class ExDmLendIntentController extends BaseController<ExDmLendIntent, Long> {

	@Resource(name = "exDmLendIntentService")
	@Override
	public void setService(BaseService<ExDmLendIntent, Long> service) {
		super.service = service;
	}

	@MyRequiresPermissions
	@RequestMapping("/find")
	@MethodName(name = "分页查询ExDmLend")
	@ResponseBody
	public PageResult find(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(ExDmLendIntent.class,request);
	    ExDmLendIntentService exDmLendIntentService = (ExDmLendIntentService)service;
	 	PageResult pageResult = exDmLendIntentService.findPageBySql(filter);
		return pageResult;
	}

}
