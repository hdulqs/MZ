/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年3月24日 上午11:46:46
 */
package com.mz.exchange.account.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.account.model.AppFundAccount;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年3月24日 上午11:46:46 
 */

@Controller   
@RequestMapping("/account/appfundaccount")
public  class AppFundAccountController extends BaseController<AppFundAccount, Long>{

	@Resource(name="appFundAccountService")
	@Override
	public void setService(BaseService<AppFundAccount, Long> service) {
		super.service=service;
		
	}
	
	@MethodName(name = "分页查询")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter=new QueryFilter(AppFundAccount.class,request);
		System.out.println("-----"+findPage(filter).getRecordsTotal());
		return  super.findPage(filter);	
	}
	
	 @MethodName(name = "删除数据")
     @RequestMapping("/remove/{id}")
     @ResponseBody
     public JsonResult remove(@PathVariable String id ){
    	 QueryFilter filter=new QueryFilter(AppFundAccount.class);
    			  if(null!=id){
                    	 filter.addFilter("id_in", id);
                    	 return super.delete(filter);
                     }
                     return null;
                     

     }
	 
     @MethodName(name = "添加数据")
     @RequestMapping("/add")
     @ResponseBody
     public JsonResult add(AppFundAccount appFundAccount){
    	 appFundAccount.setCoinType("234");
    	 appFundAccount.setCustomId(Long.valueOf("2"));
    	 System.out.println("-------");
    	 return super.save(appFundAccount);
      
     }
     
     @MethodName(name = "修改数据")
     @RequestMapping("/modify")
     @ResponseBody
     public JsonResult modify(AppFundAccount appFundAccount){
    	 return super.update(appFundAccount);
     }
     
     @MethodName(name="查看一条AppFundAccount数据")
     @RequestMapping(value="/see/{id}",method=RequestMethod.GET)
     @ResponseBody
     public  AppFundAccount  see(@PathVariable  Long  id){
    	 System.out.println("----"+service.get(id).getAddressRemark());
    	 return  service.get(id);
     }
}