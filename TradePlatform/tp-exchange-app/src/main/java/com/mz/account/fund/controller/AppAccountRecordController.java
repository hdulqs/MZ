/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年6月16日 上午11:27:54
 */
package com.mz.account.fund.controller;

import com.mz.account.fund.model.AppAccountRecord;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
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
 * @Date :          2016年6月16日 上午11:27:54 
 */
@Controller
@RequestMapping("/fund/appaccountrecord")
public class AppAccountRecordController extends
		BaseController<AppAccountRecord, Long> {

	@Override
	@Resource(name="appAccountRecordService")
	public void setService(BaseService<AppAccountRecord, Long> service) {
		super.service = service ;
	}
	
	@MethodName(name = "查询账户list")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
	    QueryFilter filter = new QueryFilter(AppAccountRecord.class, request);
	    filter.setOrderby("created asc");
	    PageResult findPage = super.findPage(filter);
	    return findPage;
	}
	
	
	

}
