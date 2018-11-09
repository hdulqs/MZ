/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年7月6日 下午6:09:23
 */
package com.mz.customer.agents.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.agents.model.CustomerAsAgents;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Wu Shuiming
 * @Date :          2016年7月6日 下午6:09:23 
 */

@Controller
@RequestMapping("/agents/customerAsAgents")
public class CustomerAsAgentsController extends BaseController<CustomerAsAgents, Long> {

	@Resource(name="customerAsAgentsService")
	@Override
	public void setService(BaseService<CustomerAsAgents, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "分页查询代理商用户 ")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(CustomerAsAgents.class,
				request);
		PageResult page = super.findPage(filter);
		return page;
	}

}
