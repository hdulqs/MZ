/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:09:25
 */
package com.mz.exchange.product.controller;

import com.alibaba.fastjson.JSON;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.AppConfig;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.product.model.ExProduct;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.QueryFilter;
import com.mz.util.message.MessageConstant;
import com.mz.util.message.MessageUtil;
import com.mz.util.sys.ContextUtil;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.exchange.entrust.StartupEntrust;
import com.mz.exchange.product.service.ExCointoCoinService;
import com.mz.exchange.product.service.ExProductService;
import com.mz.trade.entrust.DifCustomer;
import com.mz.web.app.service.AppConfigService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午2:09:25
 */
@Controller       
@RequestMapping("/product/exproduct")
public class ExProductController extends BaseController<ExProduct, Long> {

	@Resource(name = "exProductService")
	@Override
	public void setService(BaseService<ExProduct, Long> service) {
		super.service = service;
	}
	
	@Resource
	private ExCointoCoinService exCointoCoinService;
	@Resource
	private AppConfigService appConfigService;
	
	@MethodName(name = "查询product所有的")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		
		String page = request.getParameter("page");
		QueryFilter filter = new QueryFilter(ExProduct.class, request);
		if("product".equals(page)){
			filter.addFilter("issueState!=",4);
		}if("parameter".equals(page)){
			filter.addFilter("issueState=", 1);
		}
		PageResult findPage = super.findPage(filter);
		return findPage;
	}

	@MethodName(name = "下拉框查询product所有")
	@RequestMapping("/selectlist")
	@ResponseBody
	public List<ExProduct> selectlist(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(ExProduct.class, request);
		filter.addFilter("issueState=",1);
		List<ExProduct> list = super.find(filter);
		return list;
	}
	
	
	@MethodName(name = "下拉框查询product所有")
	@RequestMapping("/selectlisttype")
	@ResponseBody
	public List<ExProduct> selectlisttype(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(ExProduct.class, request);
		filter.addFilter("paceType=",1);
		List<ExProduct> list = super.find(filter);
		return list;
	}

	@MethodName(name = "根据id删除数据(将IssueState设置成3)")
	@RequestMapping("/remove/{id}")
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult remove(@PathVariable Long id) {
		ExProductService exProductService = (ExProductService)service;
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(false);
		try {
			ExProduct exProduct = service.get(id);
			// 4 表示删除 
			exProduct.setIssueState(4);
			exProduct.setPamState(0);
			service.update(exProduct);
			jsonResult.setSuccess(true);
			
			// 更新缓存
			exProductService.updateProductToRedis("");
			
			return jsonResult;
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResult;
		}
	}

	@MethodName(name = "添加数据")
	@RequestMapping("/add")
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult add(ExProduct exProduct) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(false);

		String coinCode = exProduct.getCoinCode();
		ExProductService exProductService = (ExProductService) service;
		boolean b = exProductService.findByCoinCode(coinCode);
		if (!b) {
			int i = exProduct.getIssueState();
			if (i == 1) {
				exProduct.setIssueTime(new Date());
			}
			// 设置发行方id
			exProduct.setIssueId(88l);
			
			// 更新缓存
			exProductService.updateProductToRedis(coinCode);
			
			return super.save(exProduct);
		}
		jsonResult.setMsg(MessageUtil.getText(MessageConstant.INCORRECT_CODE));
		return jsonResult;
	}
	
	
	

	@MethodName(name = "添加交易数据")
	@RequestMapping("/adddeal")
	@ResponseBody
	public JsonResult addDeal(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(false);

		String productId = request.getParameter("productId");
		
		String sort = request.getParameter("sort");

		ExProductService exservice = (ExProductService) service;
		ExProduct product = exservice.get(Long.valueOf(productId));
		try {
			
			product.setSort(Integer.valueOf(sort));

		} catch (Exception e) {
			return jsonResult;
		}

		JsonResult result = super.update(product);
		
		// 更新缓存
		exservice.updateProductToRedis("");

		return result;
	}

	@MethodName(name = "修改数据")
	@RequestMapping("/modify")
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult modify(ExProduct exProduct) {
		
		ExProductService exProductService = (ExProductService) service;
		ExProduct product = exProductService.findByCoinCode(exProduct.getCoinCode(), "");
		
		boolean b = exProductService.findByCoinCode(exProduct.getCoinCode());
		if(!b){
			JsonResult jResult = new JsonResult();
			jResult.setSuccess(false);
			jResult.setMsg("币的代码不能修改");
			return jResult;
		}
		JsonResult jsonResult = super.update(exProduct);
		/*String key = "currecyType:" +exProduct.getCoinCode();
		String key2 = "paceCurrecy:" +exProduct.getCoinCode();

		RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
		redisService.save(key, exProduct.getPaceType());
		redisService.save(key2, exProduct.getPaceCurrecy());*/

		/*if(exProduct.getPaceType().equals("2")){
			redisService.save(key2, exProduct.getPaceCurrecy());

		}else{
			redisService.save(key2, exProduct.getPaceFeeRate().toString());

		}*/
		// 更新缓存
		exProductService.updateProductToRedis("");
		exCointoCoinService.initRedisCode();
		
		if(jsonResult.getSuccess()){
			if(product != null && product.getTransactionType() != exProduct.getTransactionType()){
				DifCustomer.clearOnlyentrustNum(exProduct.getCoinCode());
			}
		}
		return jsonResult;
	}
	
	
	
	
	
	@MethodName(name = "修改数据")
	@RequestMapping("/modifytwo")
	@ResponseBody
	public JsonResult modifytwo(ExProduct exProduct) {
		
		ExProductService exProductService = (ExProductService) service;
		ExProduct product = exProductService.findByCoinCode(exProduct.getCoinCode(), "");
		
		boolean b = exProductService.findByCoinCode(exProduct.getCoinCode());
		if(!b){
			JsonResult jResult = new JsonResult();
			jResult.setSuccess(false);
			jResult.setMsg("币的代码不能修改");
			return jResult;
		}
		JsonResult jsonResult = super.update(exProduct);
		String key = "currecyType:" +exProduct.getCoinCode();
		String key2 = "paceCurrecy:" +exProduct.getCoinCode();

		RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
		redisService.save(key, exProduct.getPaceType());
		redisService.save(key2, exProduct.getPaceCurrecy());

		/*if(exProduct.getPaceType().equals("2")){
			redisService.save(key2, exProduct.getPaceCurrecy());

		}else{
			redisService.save(key2, exProduct.getPaceFeeRate().toString());

		}*/
		// 更新缓存
		exProductService.updateProductToRedis("");
		exCointoCoinService.initRedisCode();
		
		//发布所有缓存
		StartupEntrust.Entrustinit();
		exProductService.updateProductToRedis("");
		
		if(jsonResult.getSuccess()){
			if(product != null && product.getTransactionType() != exProduct.getTransactionType()){
				DifCustomer.clearOnlyentrustNum(exProduct.getCoinCode());
			}
		}
		return jsonResult;
	}

	@MethodName(name = "查看一条AppFundAccount数据")
	@RequestMapping(value = "/see/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ExProduct see(@PathVariable Long id) {
		return service.get(id);
	}
	
	
	@MethodName(name = "发布一个产品   并且同步给所有的用户")
	@RequestMapping(value = "/publishProduct/{ids}", method = RequestMethod.GET)
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult publishProduct(@PathVariable Long[] ids) {
		
		QueryFilter filter = new QueryFilter(AppConfig.class);
		filter.addFilter("configkey=", "language_code");
		AppConfig appConfig = appConfigService.get(filter);
		
		
		ExProductService exProductService = (ExProductService)service;
		
		JsonResult result = exProductService.publishProduct(ids,appConfig.getValue());	
		//发布所有缓存
		StartupEntrust.Entrustinit();
		exProductService.updateProductToRedis("");
	
		return result ;
	}
	
	
	@MethodName(name = "将一个产品退市   并且同步给所有的用户虚拟币账号")
	@RequestMapping(value = "/delistProduct/{ids}", method = RequestMethod.GET)
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult delistProduct(@PathVariable Long[] ids) {
		
		ExProductService exProductService = (ExProductService)service;
		
		JsonResult result = exProductService.delishProduct(ids);

		return result ;
	}
	
	
	@MethodName(name="产品下拉框中的")
	@RequestMapping(value="/selectProduct", method = RequestMethod.POST)
	@ResponseBody
	public List<ExProduct> selectProduct(HttpServletRequest req){
		String state = req.getParameter("states");
		
		QueryFilter filter = new QueryFilter(ExProduct.class);
		if(null != state){
			Integer i = Integer.valueOf(state);
			if(i==1){
				filter.addFilter("issueState=", 1);
			}
		}
		List<ExProduct> list = super.find(filter);
		return list;
	}
	
	
	@MethodName(name="检查币账户没有publicKey的 ，重新生成")
	@RequestMapping(value="/detection")
	@ResponseBody
	public JsonResult detection(HttpServletRequest req){
		JsonResult jsonResult = new JsonResult();
		ExProductService exProductService = (ExProductService)service;
		String detection = exProductService.detection();
		jsonResult.setSuccess(true);
		jsonResult.setMsg(detection);
		return jsonResult;
	}
	
	@MethodName(name="打开一个币的交易")
	@RequestMapping(value="/openProduct/{id}")
	@ResponseBody
	public JsonResult openProduct(@PathVariable Long id){
		ExProductService exProductService = (ExProductService)service;
		JsonResult jsonResult = exProductService.endProduct(id,1);
		return jsonResult;
	}
	
	@MethodName(name="关闭一个币交易")
	@RequestMapping(value="/endProduct/{id}")
	@ResponseBody
	public JsonResult endProduct(@PathVariable Long id){
		ExProductService exProductService = (ExProductService)service;
		JsonResult jsonResult = exProductService.endProduct(id,2);
		return jsonResult;
	}
	@MethodName(name="设置币的状态为 开启1")
	@RequestMapping(value="/openTransaction/{id}")
	@ResponseBody
	public JsonResult openTransaction(@PathVariable Long id){
		ExProductService exProductService = (ExProductService)service;
		//发布所有缓存
		StartupEntrust.Entrustinit();
		JsonResult jsonResult = exProductService.setCoinStatus(id,1);
		return jsonResult;
	}
	
	@MethodName(name="设置币的状态为关闭 0")
	@RequestMapping(value="/closeTransaction/{id}")
	@ResponseBody
	public JsonResult closeTransaction(@PathVariable Long id){
		ExProductService exProductService = (ExProductService)service;
		//发布所有缓存
		StartupEntrust.Entrustinit();
		JsonResult jsonResult = exProductService.setCoinStatus(id,0);
		return jsonResult;
	}
	
	@MethodName(name="开启C2C")
	@RequestMapping(value="/openc2c/{id}")
	@ResponseBody
	public JsonResult openc2c(@PathVariable Long id){
		ExProduct exProduct = service.get(id);
		exProduct.setOpen_c2c(1);
		service.update(exProduct);
		
		QueryFilter filter = new QueryFilter(ExProduct.class);
		filter.addFilter("open_c2c=", 1);
		List<ExProduct> list = service.find(filter);
		if(list!=null&&list.size()>0){
			ArrayList<String> c2cs = new ArrayList<String>();
			for(ExProduct ex : list){
				c2cs.add(ex.getCoinCode());
			}
			RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
			redisService.save("cn:c2clist", JSON.toJSONString(c2cs));
		}
		
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		return jsonResult;
	}
	
	@MethodName(name="关闭C2C")
	@RequestMapping(value="/closec2c/{id}")
	@ResponseBody
	public JsonResult closec2c(@PathVariable Long id){
		ExProduct exProduct = service.get(id);
		exProduct.setOpen_c2c(0);
		service.update(exProduct);
		
		QueryFilter filter = new QueryFilter(ExProduct.class);
		filter.addFilter("open_c2c=", 1);
		List<ExProduct> list = service.find(filter);
		if(list!=null&&list.size()>0){
			ArrayList<String> c2cs = new ArrayList<String>();
			for(ExProduct ex : list){
				c2cs.add(ex.getCoinCode());
			}
			RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
			redisService.save("cn:c2clist", JSON.toJSONString(c2cs));
		}
		
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		return jsonResult;
	}
	
	
}	
