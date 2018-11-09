/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:09:25
 */
package com.mz.exchange.entrust.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.product.service.ProductCommonService;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.manage.remote.model.Coin;
import com.mz.trade.entrust.service.ExEntrustService;
import com.mz.trade.redis.model.EntrustTrade;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.util.StringUtil;


/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午2:09:25
 */
@Controller
@RequestMapping("/entrust/exentrust")
public class ExEntrustController extends BaseController<ExEntrust, Long> {

	@Resource(name = "exEntrustService")
	@Override
	public void setService(BaseService<ExEntrust, Long> service) {
		super.service = service;
	}
	@Resource
	private ExEntrustService entrustService;
	@Resource
	private ProductCommonService productCommonService;
	@MethodName(name = "查询product")
	@RequestMapping("/list")
	@ResponseBody
	/*@MyRequiresPermissions(value="/entrust/exentrust/list/ed,/entrust/exentrust/list/ing,"
			+"/entrust/exentrust/list/canel,/entrust/exentrust/list/file,/entrust/exentrust/list/part,")*/
	public PageResult list(HttpServletRequest request) {
		System.out.println("Q_type_eq_Integer=="+request.getParameter("type="));
		QueryFilter filter = new QueryFilter(ExEntrust.class,request);
		String type=request.getParameter("type");
		if(type.equals("listnow")){
			filter.addFilter("status_in", "0,1");
			
		}else if(type.equals("listhistory")){
			filter.addFilter("status_in", "2,3,4");
			
		}else if(type.equals("listing")){
			filter.addFilter("status=", 0);
			
		}else if(type.equals("listpart")){
			filter.addFilter("status=", 1);
			
		}else if(type.equals("listed")){
			filter.addFilter("status=", 2);
			
		}else if(type.equals("listcancelpart")){
			filter.addFilter("status=", 3);
			
		}else if(type.equals("listcancel")){
			filter.addFilter("status=", 4);
			
		}
		String entrustType=request.getParameter("entrustType");
		String entrusStatus=request.getParameter("entrusStatus");
		String entrustWay=request.getParameter("entrustWay");
		String coinCode=request.getParameter("coinCode");
		String lentrustTime=request.getParameter("lentrustTime");
		String gentrustTime=request.getParameter("gentrustTime");
		String fixPriceCoinCode=request.getParameter("fixPriceCoinCode");
		/*String source=request.getParameter("source");
		if(!StringUtil.isEmpty(source)){
			filter.addFilter("source=",source);
		}*/
		if(!StringUtil.isEmpty(entrustType)){
			filter.addFilter("entrusStatus=",entrusStatus);
		}
		if(!StringUtil.isEmpty(entrustWay)){
			filter.addFilter("entrustWay=",entrustWay);
		}
		/*if(!StringUtil.isEmpty(userName)){
			filter.addFilter("userName_like",userName);
		}*/
		if(!StringUtil.isEmpty(coinCode)){
			filter.addFilter("coinCode=",coinCode);
		}
		if(!StringUtil.isEmpty(lentrustTime)){
			filter.addFilter("entrustTime>=",lentrustTime);
		}
		if(!StringUtil.isEmpty(gentrustTime)){
			filter.addFilter("entrustTime<=",gentrustTime);
		}
		if(!StringUtil.isEmpty(fixPriceCoinCode)){
			filter.addFilter("fixPriceCoinCode=",fixPriceCoinCode);
		}
	/*	if(!StringUtil.isEmpty(entrustNum)){
			filter.addFilter("entrustNum_like",entrustNum);
		}*/
		filter.setOrderby("entrustTime desc");
		PageResult findPage = super.findPage(filter);
		List<ExEntrust> list=(List<ExEntrust>)findPage.getRows();
		
		for(ExEntrust l:list){
	/*		RpcContext.getContext().setAttachment("saasId",ContextUtil.getSaasId());
			RemoteAppPersonInfoService remoteAppPersonInfoService = (RemoteAppPersonInfoService) ContextUtil.getBean("remoteAppPersonInfoService");
			AppPersonInfo ac=remoteAppPersonInfoService.getByCustomerId(l.getCustomerId());
			l.setTrueName(ac.getTrueName());*/
			Coin productCommon = productCommonService.getProductCommon(l.getCoinCode(),l.getFixPriceCoinCode());
			if(null!=productCommon){
				int keepDecimalForCoin=productCommon.getKeepDecimalForCoin();
				int keepDecimalForCurrency=productCommon.getKeepDecimalForCurrency();
				l.setEntrustPrice(l.getEntrustPrice().setScale(keepDecimalForCurrency,BigDecimal.ROUND_HALF_UP));
				l.setEntrustCount(l.getEntrustCount().setScale(keepDecimalForCoin,BigDecimal.ROUND_HALF_UP));
			    l.setSurplusEntrustCount((l.getEntrustCount().subtract(l.getSurplusEntrustCount())).setScale(keepDecimalForCoin, BigDecimal.ROUND_HALF_UP));
				l.setKeepDecimalForCoin(keepDecimalForCoin); 
			    l.setKeepDecimalForCurrency(keepDecimalForCurrency);
			}
			l.setEntrustSum(l.getEntrustSum().setScale(2, BigDecimal.ROUND_HALF_UP));
			
			l.setTransactionFee(l.getTransactionFee().setScale(2, BigDecimal.ROUND_HALF_UP));
			//l.setCurrencyType(l.getCurrencyType().toUpperCase());
		}
		// System.out.println(findPage);
		return findPage;
	}
	@MethodName(name = "查询订单匹配详情")
	@RequestMapping(value = "/orderFindByentrustNum")
	@ResponseBody
	public  List<ExOrderInfo> orderFindByentrustNum(HttpServletRequest request) {
		String entrustNum = request.getParameter("entrustNum").toString();
		String coinCode = request.getParameter("coinCode").toString();
		List<ExOrderInfo> list=entrustService.getMatchDetail(entrustNum, coinCode);
		 for(ExOrderInfo l:list){
				Coin productCommon = productCommonService.getProductCommon(l.getCoinCode(),l.getFixPriceCoinCode());
				if(null!=productCommon){
					int keepDecimalForCoin=(productCommon.getKeepDecimalForCoin());
					int keepDecimalForCurrency=(productCommon.getKeepDecimalForCurrency());
					l.setTransactionPrice(l.getTransactionPrice().setScale(keepDecimalForCurrency,BigDecimal.ROUND_HALF_UP));
					l.setTransactionCount(l.getTransactionCount().setScale(keepDecimalForCoin,BigDecimal.ROUND_HALF_UP));
					l.setKeepDecimalForCoin(keepDecimalForCoin); 
					l.setKeepDecimalForCurrency(keepDecimalForCurrency);
				}
			 l.setTransactionSum(l.getTransactionSum().setScale(2, BigDecimal.ROUND_HALF_DOWN));
		 }
		 return list;
	}
	@MethodName(name = "撤销委托订单")
	@RequestMapping(value = "/cancelExEntrust")
	@ResponseBody
	public JsonResult cancelExEntrust(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		String entrustNums = request.getParameter("entrustNums").toString();
		String[] id=entrustNums.split(",");
		for(int i=0;i<id.length;i++){
			EntrustTrade entrustTrade= new EntrustTrade();
			QueryFilter filter = new QueryFilter(ExEntrust.class);
			filter.addFilter("entrustNum_in", entrustNums);
			List<ExEntrust> list = this.find(filter);
			if(null!=list&&list.size()>0){
				entrustTrade.setEntrustNum(list.get(i).getEntrustNum());
				entrustTrade.setCoinCode(list.get(i).getCoinCode());
				entrustTrade.setType(list.get(i).getType());
				entrustTrade.setFixPriceCoinCode(list.get(i).getFixPriceCoinCode());
				entrustTrade.setEntrustPrice(list.get(i).getEntrustPrice());
				entrustService.cancelExEntrust(entrustTrade, "平台手动取消");
			}
		
			}
	//	entrustService.cancelExEntrust(id[i].toString(),null,"平台手动取消");
		jsonResult.setSuccess(true);
		jsonResult.setMsg("撤销成功");
		 // }
		return jsonResult;

	}
	@MethodName(name = "撤销委托订单--buykeyseel没有了撤销不了")
	@RequestMapping(value = "/cancenokeylExEntrust")
	@ResponseBody
	public JsonResult cancenokeylExEntrust(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		String entrustNums = request.getParameter("entrustNums").toString();
		String[] id=entrustNums.split(",");
		for(int i=0;i<id.length;i++){
			EntrustTrade entrustTrade= new EntrustTrade();
			QueryFilter filter = new QueryFilter(ExEntrust.class);
			filter.addFilter("entrustNum_in", entrustNums);
			List<ExEntrust> list = this.find(filter);
			if(null!=list&&list.size()>0){
				String[] rt=entrustService.cancelExEntrustnokey(list.get(i), "平台手动取消---nokey");
				jsonResult.setSuccess(true);
				jsonResult.setMsg(rt[1]);
				 // }
				return jsonResult;
			}
		
			}
	//	entrustService.cancelExEntrust(id[i].toString(),null,"平台手动取消");
		jsonResult.setSuccess(true);
		jsonResult.setMsg("撤销成功");
		 // }
		return jsonResult;

	}
}
