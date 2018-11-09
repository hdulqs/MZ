package com.mz.account.fund.controller;

import com.mz.account.fund.model.AppCoinRewardRecord;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.user.model.AppCustomer;
import com.mz.util.QueryFilter;
import com.mz.account.fund.service.AppCoinRewardRecordService;
import com.mz.core.mvc.controller.base.BaseController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 奖励币的记录
 * <p> TODO</p>
 * @author:         Zhang Lei 
 * @Date :          2017年3月10日 下午3:37:12
 */
@Controller
@RequestMapping("/fund/appCoinRewardRecord")
public class AppCoinRewardRecordController extends BaseController<AppCoinRewardRecord, Long>{
	
	@Resource(name="appCoinRewardRecordService")
	@Override
	public void setService(BaseService<AppCoinRewardRecord, Long> service) {
		super.service = service;
	}
	
    @MethodName(name = "查询奖励币的记录list")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
    	QueryFilter filter = new QueryFilter(AppCustomer.class, request);
    	PageResult findPageBySql = ((AppCoinRewardRecordService)service).findPageBySql(filter);
    	return findPageBySql;
	}
	
    
	@MethodName(name = "查看")
	@RequestMapping(value="/see/{id}",method=RequestMethod.GET)
	@MyRequiresPermissions 
	@ResponseBody
	public AppCoinRewardRecord see(@PathVariable Long id){
		AppCoinRewardRecord appCoinRewardRecord = service.get(id);
		return appCoinRewardRecord;
	}
    
    
	
	
	@MethodName(name="删除")
	@RequestMapping(value="/remove/{ids}",method=RequestMethod.DELETE)
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	
	
	
	
}	
