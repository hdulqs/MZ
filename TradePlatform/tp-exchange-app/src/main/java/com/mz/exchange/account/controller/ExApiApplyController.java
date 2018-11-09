/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年5月12日 上午10:57:39
 */
package com.mz.exchange.account.controller;

import com.alibaba.dubbo.rpc.RpcContext;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.user.model.AppCustomer;
import com.mz.exchange.account.model.ExApiApply;
import com.mz.util.QueryFilter;
import com.mz.util.date.DateUtil;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.ContextUtil;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.remote.RemoteAppCustomerService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年5月12日 上午10:57:39 
 */
@Controller
@RequestMapping("/account/exapiapply")
public class ExApiApplyController  extends BaseController<ExApiApply, Long>{
	
	
	
	
	@Resource(name="exApiApplyService")
	@Override
	public void setService(BaseService<ExApiApply, Long> service) {
		super.service = service;
	}
	
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class,
				new DateTimePropertyEditorSupport());

		/**
		 * 防止XSS攻击，并且带去左右空格功能
		 */
		binder.registerCustomEditor(String.class,
				new StringPropertyEditorSupport(true, false));
	}
	
	

	
	
	
	@MethodName(name = "查询api的key")
	@RequestMapping(value = "/list")
	@ResponseBody
   public PageResult list(HttpServletRequest request){
		
		QueryFilter filter = new QueryFilter(ExApiApply.class, request);
    	filter.setOrderby("created desc");
    	PageResult findPage = super.findPage(filter);
		List<ExApiApply> list=(List<ExApiApply>)findPage.getRows();
		
		for(ExApiApply l:list){
			RpcContext.getContext().setAttachment("saasId",ContextUtil.getSaasId());
			RemoteAppCustomerService remoteExEntrustService = (RemoteAppCustomerService) ContextUtil.getBean("remoteAppCustomerService");
			AppCustomer ac=remoteExEntrustService.getById(l.getCustomerId());
			l.setUserName(ac.getUserName());
			if(null==l.getIpAddress()||"".equals(l.getIpAddress())){
        		l.setExpiryDate(90);
        		int days=90-DateUtil.getDaysBetweenDate(l.getCreated(),new Date());
            	l.setExpiryDate(days);
            	if(days==0){
            		l.setState(1);
            	}
        	}
		}
    	return findPage;
	 
	   
   }
	
	
	
	
	@MethodName(name = "删除一个访问api的key")
	@RequestMapping(value = "/delete")
	@ResponseBody
   public JsonResult deleteKey(HttpServletRequest req){
	   
	   JsonResult result=new JsonResult();
	  String id=req.getParameter("id");
	  if(null!=id&&!"".equals(id)){
		  String ids[]=id.split(",");
		if(ids.length>0){
			for(String s:ids){
				result=super.delete(Long.valueOf(s));
			}
		}
			
		 
	  }
	   
	   return result;
	   
   }
	
}
