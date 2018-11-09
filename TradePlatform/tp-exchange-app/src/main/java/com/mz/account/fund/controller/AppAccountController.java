/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月31日 下午6:55:57
 */
package com.mz.account.fund.controller;

import com.mz.account.fund.model.AppAccount;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.account.fund.service.AppAccountService;
import com.mz.core.mvc.controller.base.BaseController;
import java.math.BigDecimal;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月31日 下午6:55:57 
 */
@Controller
@RequestMapping("/fund/appaccount")
public class AppAccountController extends BaseController<AppAccount, Long>{
	
	@Resource(name="appAccountService")
	@Override
	public void setService(BaseService<AppAccount, Long> service) {
		super.service = service;
	}
	
	
    @MethodName(name = "查询账户list")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
    	QueryFilter filter = new QueryFilter(AppAccount.class, request);
    	PageResult findPageBySql = super.findPage(filter);
    	return findPageBySql;
	}
	
    
    
    /**
     * 查询代理商的list
     * <p> TODO</p>
     * @author:         Zhang Lei
     * @param:    @param request
     * @param:    @return
     * @return: PageResult 
     * @Date :          2017年3月11日 上午10:22:21   
     * @throws:
     */
    @MethodName(name = "查询账户list")
	@RequestMapping("/agentList")
	@ResponseBody
	public PageResult agentList(HttpServletRequest request) {
    	QueryFilter filter = new QueryFilter(AppAccount.class, request);
    	PageResult findPageBySql = ((AppAccountService)service).findAgentPageBySql(filter);
    	return findPageBySql;
	}
	
    @MethodName(name="派发代理商佣金")
	@RequestMapping(value="/postMoney",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult postMoney(HttpServletRequest req){
    	JsonResult result =new JsonResult();
		Long id = Long.valueOf(req.getParameter("id"));
		BigDecimal money = new BigDecimal(req.getParameter("money"));
		if(id==null || money==null){
			result.setMsg("代理商ID或派发金额为空！");
			result.setSuccess(false);
			return result;
		}
		
		//获取交易商账户
		AppAccount account=((AppAccountService)service).get(id);
		if(account!=null){
			BigDecimal rewardMoney=account.getRewardMoney();
			if(money.compareTo(rewardMoney)>0){//派发金额大于可派发佣金金额
				result.setMsg("派发金额大于可派发佣金金额！");
				result.setSuccess(false);
				return result;
			}
			
			
			//可用账户加上这笔钱
			account.setHotMoney(account.getHotMoney().add(money));
			//已派发佣金账户加上这笔钱
			account.setHasRewardMoney(account.getHasRewardMoney().add(money));
			//佣金账户减去这笔钱
			account.setRewardMoney(account.getRewardMoney().subtract(money));
			//更新
			((AppAccountService)service).update(account);
			
			result.setMsg("派发成功！");
			result.setSuccess(true);
		}else{
			result.setMsg("未查到代理商账户！");
			result.setSuccess(false);
		}
		
		return result;
	}
}
