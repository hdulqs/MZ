/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      gaomimi
 * @version:     V1.0 
 * @Date:        2017-07-06 19:40:34 
 */
package com.mz.exchange.product.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.user.model.AppCustomer;
import com.mz.customer.user.service.AppCustomerService;
import com.mz.exchange.product.model.ExCointoCoin;
import com.mz.exchange.product.model.ExProduct;
import com.mz.exchange.product.service.ExCointoCoinService;
import com.mz.exchange.product.service.ExProductService;
import com.mz.manage.remote.model.Coin2;
import com.mz.mq.producer.service.MessageProducer;
import com.mz.trade.entrust.ExchangeDataCache;
import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.web.app.service.AppConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      gaomimi
 * @version:     V1.0 
 * @Date:        2017-07-06 19:40:34 
 */
@Controller
@RequestMapping("/product/excointocoin")
public class ExCointoCoinController extends BaseController<ExCointoCoin, Long> {
	
	@Resource(name = "exCointoCoinService")
	@Override
	public void setService(BaseService<ExCointoCoin, Long> service) {
		super.service = service;
	}
	@Resource(name = "exCointoCoinService")
	public ExCointoCoinService exCointoCoinService;
	@Resource(name = "exProductService")
	public ExProductService exProductService;
	@Resource(name = "appCustomerService")
	public AppCustomerService appCustomerService;
	@Resource(name = "exOrderInfoService")
	public ExOrderInfoService exOrderInfoService;
	@Resource
	public AppConfigService appConfigService;
	
	@MethodName(name="增加ExCointoCoin")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,ExCointoCoin exCointoCoin){
		List<ExCointoCoin>list=exCointoCoinService.getBylist(exCointoCoin.getCoinCode(), exCointoCoin.getFixPriceCoinCode(), null);
		if(list.size()==0){
			return super.save(exCointoCoin);
		}else{
			JsonResult jsonResult=new JsonResult();
			jsonResult.setSuccess(false);
			jsonResult.setMsg("已经存在");
			return jsonResult;
		}
		
	}
	@MethodName(name="修改ExCointoCoin")
	@RequestMapping(value="/modifyauto")
	@ResponseBody
	public JsonResult modifyauto(HttpServletRequest request,ExCointoCoin exCointoCoin){
		JsonResult jsonResult=new JsonResult();
		List<ExCointoCoin>list=exCointoCoinService.getBylist(exCointoCoin.getCoinCode(), exCointoCoin.getFixPriceCoinCode(), null);
		ExCointoCoin ex=null;
		if(list.size()!=0){
			 ex=list.get(0);
			 /* if(null!=ex.getAutoUsername()&&!ex.getAutoUsername().equals(exCointoCoin.getAutoUsername())){
				jsonResult.setMsg("自动交易账号不能修改");
				jsonResult.setSuccess(false);
				return jsonResult;
		    	
		    }*/
		}
		if(null!=exCointoCoin.getAutoUsername()){
			QueryFilter filter=new QueryFilter(AppCustomer.class);
				filter.addFilter("userName=", exCointoCoin.getAutoUsername());
				AppCustomer customer=appCustomerService.get(filter);
				if(null==customer){
					jsonResult.setMsg("自动交易账号有问题");
					jsonResult.setSuccess(false);
					return jsonResult;
				}
				ex.setAutoCount(exCointoCoin.getAutoCount());
				ex.setAutoCountFloat(exCointoCoin.getAutoCountFloat());
				ex.setAutoPrice(exCointoCoin.getAutoPrice());
				ex.setAutoPriceFloat(exCointoCoin.getAutoPriceFloat());
				ex.setAutoUsername(exCointoCoin.getAutoUsername());
				ex.setIsSratAuto(exCointoCoin.getIsSratAuto());
				ex.setAtuoPriceType(exCointoCoin.getAtuoPriceType());
				ex.setIsHedge(exCointoCoin.getIsHedge());
				if(ex.getAtuoPriceType().intValue()==1){
					ex.setUpFloatPer(null);
					if(null==exCointoCoin.getAutoPrice()||exCointoCoin.getAutoPrice().compareTo(new BigDecimal(0))==0){
						jsonResult.setMsg("基准价格不能为0");
						jsonResult.setSuccess(false);
						return jsonResult;
					}
				}else if(ex.getAtuoPriceType().intValue()==2){
					BigDecimal upFloatPer=(exCointoCoin.getAutoPriceFloat().add(new BigDecimal(100))).multiply(new BigDecimal(100).subtract(exCointoCoin.getAutoPriceFloat()));
					ex.setUpFloatPer(new BigDecimal(10000).divide(upFloatPer,10,BigDecimal.ROUND_DOWN));
					ex.setAutoPrice(new BigDecimal(0));
				}
				JsonResult update = super.update(ex);
				return update;
		}else{
			jsonResult.setMsg("自动交易账号不能为空");
			jsonResult.setSuccess(false);
			return jsonResult;
		}
		
		
	//	exCointoCoinService.initRedisCode();
	
		
	}	
	@MethodName(name="修改ExCointoCoin")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,ExCointoCoin exCointoCoin){
		JsonResult update = super.update(exCointoCoin);
		
		exCointoCoinService.initRedisCode();
		
		String coinsKey  = "cn:coinInfoList2";
		String conStr = ExchangeDataCache.getStringData(coinsKey);
		if(!StringUtils.isEmpty(conStr)){
			String code = exCointoCoin.getCoinCode()+exCointoCoin.getFixPriceCoinCode();
			List<Coin2> list = JSONObject.parseArray(conStr, Coin2.class);
			boolean flag = false;
			for(Coin2 c : list){
				if(code.equals(c.getCoinCode()+c.getFixPriceCoinCode())){
					c.setYesterdayPrice(exCointoCoin.getYesterdayPrice());
					flag = true;
				}
			}
			if(!flag){
				Coin2 coin2 = new Coin2();
				coin2.setCoinCode(exCointoCoin.getCoinCode());
				coin2.setFixPriceCoinCode(exCointoCoin.getFixPriceCoinCode());
				coin2.setYesterdayPrice(exCointoCoin.getYesterdayPrice());
				list.add(coin2);
			}
			ExchangeDataCache.setStringData(coinsKey, JSON.toJSONString(list));
		}else{
			List<Coin2>  list = new ArrayList<Coin2>();

			Coin2 coin2 = new Coin2();
			coin2.setCoinCode(exCointoCoin.getCoinCode());
			coin2.setFixPriceCoinCode(exCointoCoin.getFixPriceCoinCode());
			coin2.setYesterdayPrice(exCointoCoin.getYesterdayPrice());
		
			list.add(coin2);
			ExchangeDataCache.setStringData(coinsKey, JSON.toJSONString(list));
			
		}
		
		
		return update;
	}
	@MethodName(name="开启交易")
	@RequestMapping(value="/open/{id}")
	@ResponseBody
	public JsonResult open(@PathVariable Long id){
		ExCointoCoin exCointoCoin=this.service.get(id);
		exCointoCoin.setState(1);
		JsonResult jsonResult=super.update(exCointoCoin);
		exCointoCoinService.initRedisCode();
		return jsonResult;
	}
	@MethodName(name="关闭交易")
	@RequestMapping(value="/close/{id}")
	@ResponseBody
	public JsonResult close(@PathVariable Long id){
		ExCointoCoin exCointoCoin=this.service.get(id);
		exCointoCoin.setState(0);
		JsonResult jsonResult=super.update(exCointoCoin);
		exCointoCoinService.initRedisCode();
		return jsonResult;
	}

	@MethodName(name="开启操盘")
	@RequestMapping(value="/openOperate/{id}")
	@ResponseBody
	public JsonResult openOperate(@PathVariable Long id){
		ExCointoCoin exCointoCoin=this.service.get(id);
		exCointoCoin.setIsOperate(1);
		JsonResult jsonResult=super.update(exCointoCoin);
		exCointoCoinService.initRedisCode();
		return jsonResult;
	}

	@MethodName(name="关闭操盘")
	@RequestMapping(value="/closeOperate/{id}")
	@ResponseBody
	public JsonResult closeOperate(@PathVariable Long id){
		ExCointoCoin exCointoCoin=this.service.get(id);
		exCointoCoin.setIsOperate(0);
		JsonResult jsonResult=super.update(exCointoCoin);
		exCointoCoinService.initRedisCode();
		return jsonResult;
	}

	@MethodName(name="删除ExCointoCoin")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表ExCointoCoin")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(ExCointoCoin.class,request);
		return super.findPage(filter);
	}

	@MethodName(name = "从火币导入火币的K线")
	@RequestMapping("/importKline/{id}")
	@ResponseBody
	public JsonResult importKlineFromHuobi(@PathVariable Long id) {
        ExCointoCoin exCointoCoin=this.service.get(id);
        String coinCode  = exCointoCoin.getCoinCode() + "_" + exCointoCoin.getFixPriceCoinCode();
        MessageProducer messageProducer = (MessageProducer) ContextUtil.getBean("messageProducer");
        messageProducer.syncKline(coinCode);
        JsonResult jsonResult=super.update(exCointoCoin);
        exCointoCoinService.initRedisCode();
        return jsonResult;
	}

	@RequestMapping("/selectlistFixPriceCoinCode")
	@MethodName(name = "查询")
	@ResponseBody
	public  List<ExCointoCoin> selectlistFixPriceCoinCode(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(ExCointoCoin.class, request);
		List<ExCointoCoin> list = super.find(filter);
		List<ExCointoCoin> result = new ArrayList<ExCointoCoin>();
		List<String> tmpList=new ArrayList<String>();
        for(ExCointoCoin e:list){
        	if(!tmpList.contains(e.getFixPriceCoinCode())){
        		tmpList.add(e.getFixPriceCoinCode());
        		e.setCoinCode(e.getFixPriceCoinCode());
        		result.add(e);
        	}
        }
		return result;
	}
	@RequestMapping("/getFixPriceType")
	@MethodName(name = "得到定价币种类型")
	@ResponseBody
	public  List<ExCointoCoin> getFixPriceType(HttpServletRequest request) {
		 List<ExCointoCoin> list=new  ArrayList<ExCointoCoin>();
		 ExCointoCoin exCointoCoin0=new  ExCointoCoin();
		 exCointoCoin0.setCoinCode("真实货币");
		 exCointoCoin0.setFixPriceType(0);
		 list.add(exCointoCoin0);
		 ExCointoCoin exCointoCoin1=new  ExCointoCoin();
		 exCointoCoin1.setCoinCode("虚拟货币");
		 exCointoCoin1.setFixPriceType(1);
		 list.add(exCointoCoin1);
		return list;
	  }
	@RequestMapping(value="/getFixPriceCoinList")
	@MethodName(name = "得到定价币种类型")
	@ResponseBody
	public  List<ExProduct> getFixPriceCoinList(HttpServletRequest request) {
		String fixPriceType=request.getParameter("fixPriceType"); 
		if(null!=fixPriceType&&fixPriceType.equals("0")){
			List<ExProduct> list = new ArrayList<ExProduct>();
			String bykey = appConfigService.getBykey("language_code");
			ExProduct exProduct=new ExProduct();
			exProduct.setCoinCode(bykey);
			list.add(exProduct);
			return list;
		}else{
			QueryFilter filter = new QueryFilter(ExProduct.class, request);
			filter.addFilter("issueState!=",3);
			List<ExProduct> list = exProductService.find(filter);
			return list;
		}
		
	  }
	@MethodName(name="编辑ExCointoCoin")
	@RequestMapping(value="/modify/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(ExCointoCoin exCointoCoin){
		
		return 	 super.update(exCointoCoin);
	}
	@MethodName(name = "查看ExCointoCoin")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public ExCointoCoin see(@PathVariable Long id){
		ExCointoCoin exCointoCoin = service.get(id);
		
		
		String coinsKey  = "cn:coinInfoList2";
		String conStr = ExchangeDataCache.getStringData(coinsKey);
		if(!StringUtils.isEmpty(conStr)){
			
			String code = exCointoCoin.getCoinCode()+exCointoCoin.getFixPriceCoinCode();
			
			List<Coin2> list = JSONObject.parseArray(conStr, Coin2.class);
			for(Coin2 c : list){
				if(code.equals(c.getCoinCode()+c.getFixPriceCoinCode())){
					exCointoCoin.setYesterdayPrice(c.getYesterdayPrice());
				}
			}
			
		}
	
		return exCointoCoin;
	}
	@MethodName(name = "列表ExCointoCoin")
	@RequestMapping("/listauto")
	@ResponseBody
	public PageResult listauto(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(ExCointoCoin.class,request);
		filter.addFilter("state=", 1);
		return super.findPage(filter);
	}
}
