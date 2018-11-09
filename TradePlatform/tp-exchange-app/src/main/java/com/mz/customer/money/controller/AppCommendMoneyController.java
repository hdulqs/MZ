/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-29 10:05:55 
 */
package com.mz.customer.money.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.money.model.AppCommendMoney;
import com.mz.customer.money.service.AppCommendMoneyService;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-29 10:05:55 
 */
@Controller
@RequestMapping("/money/appcommendmoney")
public class AppCommendMoneyController extends BaseController<AppCommendMoney, Long> {
	
	@Resource(name = "appCommendMoneyService")
	@Override
	public void setService(BaseService<AppCommendMoney, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppCommendMoney")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppCommendMoney see(@PathVariable Long id){
		AppCommendMoney appCommendMoney = service.get(id);
		return appCommendMoney;
	}
	
	@MethodName(name="增加AppCommendMoney")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppCommendMoney appCommendMoney){
		return super.save(appCommendMoney);
	}
	
	@MethodName(name="修改AppCommendMoney")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppCommendMoney appCommendMoney){
		return super.update(appCommendMoney);
	}
	
	@MethodName(name="删除AppCommendMoney")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppCommendMoney")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppCommendMoney.class,request);
		filter.addFilter("paidMoney=", new BigDecimal(0));
		filter.addFilter("moneyNum!=", new BigDecimal(0));
		filter.setOrderby("custromerId desc");
		//filter.addFilter("order by custromerId desc", "");
		PageResult findPage = super.findPage(filter);
//		List rows = findPage.getRows();
//		List rows1 = new ArrayList();
//		Long RecordsTotal = Long.valueOf(0);
//		for (Object object : rows) {
//			//将获取的object向下转型
//			AppCommendMoney acm = (AppCommendMoney) object;
//			 BigDecimal moneyNum = acm.getMoneyNum();
//			 BigDecimal paidMoney = acm.getPaidMoney();
//			 //判断是否派发
//			if (moneyNum.compareTo(paidMoney)!=0) {
//				rows1.add(object);
//				RecordsTotal++;
//			}
//		}
//		findPage.setRows(rows1);
//		findPage.setRecordsTotal(RecordsTotal);
//		Long totalPageNum = (RecordsTotal  - 1) / findPage.getPageSize() + 1;
//		findPage.setTotalPage(totalPageNum);
		return findPage;
	}
	
	
	@MethodName(name = "查询平台用户的详细信息")
	@RequestMapping("/findTotalAccountForReport")
	@ResponseBody
	public List<AppCommendMoney> findTotalAccountForReport(HttpServletRequest req){
		
		AppCommendMoneyService appCommendMoneyService = (AppCommendMoneyService)service;
		List<AppCommendMoney> selectCommend = appCommendMoneyService.selectCommend();
		
		return selectCommend;
	}
	
	

	@MethodName(name = "查询平台用户的详细信息")
	@RequestMapping(value="/findMoneyDetail/{ids}")
	@ResponseBody
	public AppCommendMoney findMoneyDetail(@PathVariable String ids){
		AppCommendMoneyService appCommendMoneyService = (AppCommendMoneyService)service;
		QueryFilter AppCommendMoney = new QueryFilter(AppCommendMoney.class);
		AppCommendMoney.addFilter("id=", ids);
		AppCommendMoney appCommendMoney2 = appCommendMoneyService.get(AppCommendMoney);
		Long custromerId = appCommendMoney2.getCustromerId();
		String fixPriceCoinCode = appCommendMoney2.getFixPriceCoinCode();
	/*	AppCommendMoney AppCommendMoney = appCommendMoneyService.get(AppCommendMoney);*/
		BigDecimal findOne = appCommendMoneyService.findOne(custromerId.toString(),fixPriceCoinCode);
		BigDecimal findTwo = appCommendMoneyService.findTwo(custromerId.toString(),fixPriceCoinCode);
		BigDecimal findThree = appCommendMoneyService.findThree(custromerId.toString(),fixPriceCoinCode);
		BigDecimal findLater =appCommendMoneyService.findLater(custromerId.toString(),fixPriceCoinCode);
		AppCommendMoney appCommendMoney=new AppCommendMoney();
		appCommendMoney.setCommendOne(findOne);
		appCommendMoney.setCommendTwo(findTwo);
		appCommendMoney.setCommendThree(findThree);
		appCommendMoney.setCommendLater(findLater);
		
		return appCommendMoney;
	}
	
	
	
	
	@MethodName(name="根据id查看某个对象的方法")
	@RequestMapping(value="/postMoney",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult postMoney(HttpServletRequest req){
		JsonResult result = new JsonResult();
		String ids=req.getParameter("id");
		String[] split = ids.split(",");
		int a = 0;
		int b = 0;
		for (int i = 0; i < split.length; i++) {
			
		Long id = Long.valueOf(split[i]);
		//Long id1 = Long.valueOf(req.getParameter("id"));
		//String fixPriceCoinCode = req.getParameter("fixPriceCoinCode");
	  //  Integer money = Integer.valueOf(req.getParameter("money"));
	    
		//如果佣金结算金额不一致，以最大值为准（暂时注掉）
	    //BigDecimal count = commissionDeployService.getStandardMoney();
		
		AppCommendMoney appCommendMoney = service.get(id);
		
		AppCommendMoneyService appCommendMoneyService = (AppCommendMoneyService)service;
	
		result = appCommendMoneyService.postMoneyById(id, appCommendMoney.getMoneyNum(),appCommendMoney.getFixPriceCoinCode());
	
		if(result.getSuccess()==false){
			a++;
		}
		if(result.getSuccess()==true){
			b++;
		}
		}
		result.setMsg("派送成功"+b+","+"派送失败"+a);
		return result;
	}
	
	
	@MethodName(name="一键派发")
	@RequestMapping(value="/oneKey",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult oneKey(HttpServletRequest req){
		JsonResult result = new JsonResult();
		int a = 0;
		int b = 0;
		ArrayList<Long> arrayList = new ArrayList<Long>();
		AppCommendMoneyService appCommendMoneyService = (AppCommendMoneyService)service;
		List<AppCommendMoney> allCommend=appCommendMoneyService.selectAllCommend();
		for (AppCommendMoney appCommendMoney : allCommend) {
			
			Long id = appCommendMoney.getId();
			//AppCommendMoney appCommendMoneyH = service.get(id);
		
			result = appCommendMoneyService.postMoneyById(id, appCommendMoney.getMoneyNum(),appCommendMoney.getFixPriceCoinCode());
		
			if(result.getSuccess()==false){
				a++;
			}
			if(result.getSuccess()==true){
				b++;
			}
		}
		result.setMsg("派送成功"+b+","+"派送失败"+a);
		return result;
	}
	
}
