/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      zenghao
 * @version:     V1.0 
 * @Date:        2016-11-21 15:48:49 
 */
package com.mz.exchange.subscription.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.product.model.ExProduct;
import com.mz.exchange.product.service.ExProductService;
import com.mz.exchange.subscription.model.ExSubscriptionPlan;
import com.mz.util.QueryFilter;
import com.mz.util.date.DateUtil;
import com.mz.core.mvc.controller.base.BaseController;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      zenghao
 * @version:     V1.0 
 * @Date:        2016-11-21 15:48:49 
 */
@Controller
@RequestMapping("/subscription/exsubscriptionplan")
public class ExSubscriptionPlanController extends BaseController<ExSubscriptionPlan, Long> {
	@Resource
	private ExProductService exProductService;
	@Resource(name = "exSubscriptionPlanService")
	@Override
	public void setService(BaseService<ExSubscriptionPlan, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看ExSubscriptionPlan")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public ExSubscriptionPlan see(@PathVariable Long id){
		ExSubscriptionPlan exSubscriptionPlan = service.get(id);
		return exSubscriptionPlan;
	}
	
	@MethodName(name="增加ExSubscriptionPlan")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,ExSubscriptionPlan exSubscriptionPlan){
		JsonResult jr = new JsonResult();
		try {
			int result=DateUtil.compareDate(exSubscriptionPlan.getStartTime(), new Date(), 1);
			if(result>0){
				ExProduct prduct =  exProductService.get(Long.valueOf(exSubscriptionPlan.getCoinCode()));
				if(prduct!=null){
					QueryFilter filter = new QueryFilter(ExSubscriptionPlan.class);
					List<ExSubscriptionPlan> planlist = service.find(filter);
					if(planlist.size()==0){
						exSubscriptionPlan.setPeriod(1);
					}else{
						QueryFilter qfilter = new QueryFilter(ExSubscriptionPlan.class);
						qfilter.setOrderby("period desc");
						List<ExSubscriptionPlan> explanlist = service.find(filter);
						exSubscriptionPlan.setPeriod(explanlist.get(0).getPeriod()+1);
					}
					exSubscriptionPlan.setCoinCode(prduct.getCoinCode());
					exSubscriptionPlan.setCoinName(prduct.getName());
					exSubscriptionPlan.setPurchaseNumber(new BigDecimal(0));
					exSubscriptionPlan.setSurplusNumber(exSubscriptionPlan.getOpenNumber());
					super.save(exSubscriptionPlan);
					jr.setSuccess(true);
				}else{
					jr.setMsg("币种信息错误");
					jr.setSuccess(false);
				}
			}else{
				jr.setMsg("开始认购时间不允许小于当前时间");
				jr.setSuccess(false);
			}
		} catch (Exception e) {
			jr.setSuccess(false);
		}
		return jr;
	}
	
	@MethodName(name="修改ExSubscriptionPlan")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,ExSubscriptionPlan exSubscriptionPlan){
		return super.update(exSubscriptionPlan);
	}
	
	@MethodName(name="删除ExSubscriptionPlan")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		JsonResult jr = new JsonResult();
		String str[] = ids.split(",");
		int successNum = 0;
		int faileNum = 0;
		try {
			for(int i=0;i<str.length;i++){
				ExSubscriptionPlan exSubscriptionPlan = service.get(Long.valueOf(str[i]));
				if(exSubscriptionPlan!=null){
					//未发布和未认购的可以删除
					if(exSubscriptionPlan.getState()==0||exSubscriptionPlan.getState()==1){
						//判断当前时间是否大于等于认购开始时间
						/*int result=DateUtil.compareDate(exSubscriptionPlan.getStartTime(), new Date(), 1);
				        if(result>=0){
				        	exSubscriptionPlan.setState(2);
							super.update(exSubscriptionPlan);
							faileNum++;
				        }else{*/
				        	super.deleteBatch(str[i]);
				        	successNum++;
				       // }
					}else{
						faileNum++;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		jr.setMsg("删除成功："+successNum+"条,失败："+faileNum+"条");
		return jr;
	}
	@MethodName(name = "发布ExSubscriptionPlan")
	@RequestMapping(value="/release/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult release(@PathVariable Long id){
		JsonResult jr = new JsonResult();
		try {
			ExSubscriptionPlan exSubscriptionPlan = service.get(id);
			//判断是否有已经发布和正在认购的计划
			QueryFilter filter = new QueryFilter(ExSubscriptionPlan.class);
			filter.addFilter("state=", "2");
			ExSubscriptionPlan notPaln = service.get(filter);
			if(notPaln!=null){
				jr.setMsg("已有计划在认购中，请稍后发布");
				jr.setSuccess(false);
				return jr;
			}
			if(exSubscriptionPlan.getState()==0){
				//判断认购前是否已经制定过了
				QueryFilter qfilter = new QueryFilter(ExSubscriptionPlan.class);
				qfilter.addFilter("period=", exSubscriptionPlan.getPeriod());
				ExSubscriptionPlan periodPlan = service.get(qfilter);
				if(periodPlan!=null&&periodPlan.getState()!=0){
					jr.setMsg("认购期数有误，请重新核对");
					jr.setSuccess(false);
					return jr;
				}
				//判断起始认购价是否大于上一期交易最高价
				if(exSubscriptionPlan.getPeriod()>1){
					QueryFilter queryfilter = new QueryFilter(ExSubscriptionPlan.class);
					queryfilter.addFilter("period=", exSubscriptionPlan.getPeriod()-1);
					ExSubscriptionPlan explan = service.get(queryfilter);
					if(null!=explan&&exSubscriptionPlan.getInitialPrice().compareTo(explan.getTransactionPrice())<0){
						jr.setMsg("起始认购价不能低于上期交易最高价:"+explan.getTransactionPrice()+"元");
						jr.setSuccess(false);
						return jr;
					}
				}
				
				//判断当前时间是否大于等于认购开始时间
				int result=DateUtil.compareDate(exSubscriptionPlan.getStartTime(), new Date(), 1);
		        if(result<0){
		        	exSubscriptionPlan.setState(2);
		        }else{
		        	exSubscriptionPlan.setState(1);
		        }
				super.update(exSubscriptionPlan);
				jr.setSuccess(true);
			}else{
				jr.setMsg("只能选择未发布的记录");
				jr.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jr;
	}	
	@MethodName(name = "列表ExSubscriptionPlan")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(ExSubscriptionPlan.class,request);
		PageResult planList =super.findPage(filter);
		//判断0未发布，1已发布认购时间是否已经超过当前时间
		if(planList.getRows().size()>0){
			for(int i=0;i<planList.getRows().size();i++){
				ExSubscriptionPlan plan = (ExSubscriptionPlan)planList.getRows().get(i);
				if(plan.getState()==0||plan.getState()==1){
					int result=DateUtil.compareDate(plan.getStartTime(), new Date(), 1);
			        if(result<0){
			        	plan.setState(2);
			        	super.update(plan);
			        	planList.getRows().set(i, plan);
			        }
				}
			}
		}
		return planList;
	}
}
