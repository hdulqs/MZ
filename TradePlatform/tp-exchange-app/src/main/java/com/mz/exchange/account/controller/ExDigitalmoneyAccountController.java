/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午7:08:15
 */
package com.mz.exchange.account.controller;

import com.alibaba.fastjson.JSON;
import com.mz.account.fund.model.AppAccount;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.account.util.ExcelUtil;
import com.mz.exchange.purse.CoinInterfaceUtil;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import com.mz.util.QueryFilter;
import com.mz.util.idgenerate.IdGenerate;
import com.mz.util.idgenerate.NumConstant;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.core.constant.CodeConstant;
import com.mz.core.constant.StringConstant;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.mq.producer.service.MessageProducer;
import com.mz.trade.redis.model.Accountadd;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午7:08:15
 */
@Controller
@RequestMapping("/account/exdigitalmoneyaccount")
public class ExDigitalmoneyAccountController extends
		BaseController<ExDigitalmoneyAccount, Long> {

	@Resource(name = "exDigitalmoneyAccountService")
	public void setService(BaseService<ExDigitalmoneyAccount, Long> service) {
		super.service = service;
	}
	
	@Resource(name = "exDmTransactionService")
	public ExDmTransactionService transactionService;
	
	@Resource
	private MessageProducer messageProducer;

	@MethodName(name = "分页查询DigitalmoneyAccount")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
    	QueryFilter filter = new QueryFilter(AppAccount.class, request);
    	  PageResult findPageBySql = ((ExDigitalmoneyAccountService)service).findPageBySql(filter);
//    	PageResult findPage = super.findPage(filter);
    	return findPageBySql;
	}
	
	
	@RequestMapping("/see/{id}")
	@MethodName(name = "通过虚拟币id查询其所对应的订单数量")
	@ResponseBody
	public List<ExDmTransaction> see(@PathVariable Long id) {
		
		QueryFilter filter = new QueryFilter(ExDmTransaction.class);
		filter.addFilter("accountId=", id);
		List<ExDmTransaction> list = transactionService.find(filter);
		return list;
	}

	@RequestMapping("/selectlistCurrenyType")
	@MethodName(name = "查询")
	@ResponseBody
	public String selectlistCurrenyType() {
		StringBuffer sb=new StringBuffer("[");
		 Map<String,String> mapLoadWeb=PropertiesUtils.getLoadWeb();
		    int i=1;
			for (String website : mapLoadWeb.keySet()) {
				 String currencyType=mapLoadWeb.get(website);
				 sb.append("{\"currencyType\":\""+currencyType+"\"}");
				
				 if(i< mapLoadWeb.keySet().size()){
				   sb.append(",");
				 }
				i++;
			}
			sb.append("]");
			return sb.toString();
			
	  }
	@MethodName(name="禁用")
	@RequestMapping(value="/disable", method = RequestMethod.POST)
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult disable(HttpServletRequest req){
		ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService)service;
		return exDigitalmoneyAccountService.disableMoney(Long.valueOf(req.getParameter("id")),req.getParameter("disableMoney"));
	}
	 
	
	@RequestMapping("/recharge")
	@MethodName(name = "手动充值")
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult recharge(HttpServletRequest request) {
		String id = request.getParameter("id");
		String number = request.getParameter("number");
		JsonResult jsonResult = new JsonResult();
		if(!StringUtils.isEmpty(id)&&!StringUtils.isEmpty(number)){
			if(new BigDecimal(number).compareTo(new BigDecimal("0"))<0){
				jsonResult.setMsg("充值金额不能为负数");
				jsonResult.setSuccess(false);
				return  jsonResult;
			}
			ExDigitalmoneyAccount account = service.get(Long.valueOf(id));
			ExDmTransaction exDmTransaction = new ExDmTransaction();
			exDmTransaction.setAccountId(account.getId());
			exDmTransaction.setCoinCode(account.getCoinCode());
			exDmTransaction.setCreated(new Date());
			exDmTransaction.setCurrencyType(account.getCurrencyType());
			exDmTransaction.setWebsite(account.getWebsite());
			exDmTransaction.setCustomerId(account.getCustomerId());
			exDmTransaction.setCustomerName(account.getUserName());
			exDmTransaction.setTrueName(account.getTrueName());
			exDmTransaction.setFee(new BigDecimal(0));
			exDmTransaction.setTransactionMoney(new BigDecimal(number));
			exDmTransaction.setStatus(1);
			exDmTransaction.setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
			// 充币
			exDmTransaction.setTransactionType(1);
			exDmTransaction.setUserId(ContextUtil.getCurrentUserId());
			transactionService.save(exDmTransaction);
			
			//String[] inComeToHotAccount = ((ExDigitalmoneyAccountService)service).inComeToHotAccount(exDmTransaction.getAccountId(), exDmTransaction.getTransactionMoney(), exDmTransaction.getTransactionNum(), "手动充币", null,null);
			exDmTransaction.setStatus(Integer.valueOf(2));//充值成功
			transactionService.update(exDmTransaction);
			jsonResult.setMsg("充值成功");
			jsonResult.setSuccess(true);
			
			//发送mq通知缓存
			Accountadd accountadd = new Accountadd();
			accountadd.setAccountId(exDmTransaction.getAccountId());
			accountadd.setMoney(exDmTransaction.getTransactionMoney());
			accountadd.setMonteyType(1);
			accountadd.setAcccountType(1);
			accountadd.setRemarks(23);
			accountadd.setTransactionNum(exDmTransaction.getTransactionNum());
			List<Accountadd> list = new ArrayList<Accountadd>();
			list.add(accountadd);
			messageProducer.toAccount(JSON.toJSONString(list));
			
			
		
			
		}else{
			jsonResult.setMsg("参数不能为空");
		}
		
		
		return jsonResult;
	}



	@RequestMapping("/tibi")
	@MethodName(name = "手动提币")
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult tibi(HttpServletRequest request) {
		String id = request.getParameter("id");
		String number = request.getParameter("number");
		JsonResult jsonResult = new JsonResult();
		if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(number)) {
			ExDigitalmoneyAccount account = service.get(Long.valueOf(id));
			ExDmTransaction exDmTransaction = new ExDmTransaction();
			exDmTransaction.setAccountId(account.getId());
			exDmTransaction.setCoinCode(account.getCoinCode());
			exDmTransaction.setCreated(new Date());
			exDmTransaction.setCurrencyType(account.getCurrencyType());
			exDmTransaction.setWebsite(account.getWebsite());
			exDmTransaction.setCustomerId(account.getCustomerId());
			exDmTransaction.setCustomerName(account.getUserName());
			exDmTransaction.setTrueName(account.getTrueName());
			exDmTransaction.setSurname(account.getSurname());
			exDmTransaction.setFee(new BigDecimal(0));
			exDmTransaction.setTransactionMoney(new BigDecimal(number));
			exDmTransaction.setStatus(1);
			exDmTransaction.setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
			// 提币
			exDmTransaction.setTransactionType(2);
			exDmTransaction.setRemark("手动提币");
			exDmTransaction.setUserId(ContextUtil.getCurrentUserId());
			transactionService.save(exDmTransaction);

			/*account.setHotMoney(account.getHotMoney().subtract(new BigDecimal(number)));
			service.update(account);*/

			exDmTransaction.setStatus(Integer.valueOf(2));//提币成功
			transactionService.update(exDmTransaction);
			jsonResult.setMsg("充值成功");
			jsonResult.setSuccess(true);

			//发送mq通知缓存
			Accountadd accountadd = new Accountadd();
			accountadd.setAccountId(exDmTransaction.getAccountId());
			accountadd.setMoney(exDmTransaction.getTransactionMoney().multiply(new BigDecimal(-1)));
			accountadd.setMonteyType(1);
			accountadd.setAcccountType(1);
			accountadd.setRemarks(25);
			accountadd.setTransactionNum(exDmTransaction.getTransactionNum());
			List<Accountadd> list = new ArrayList<Accountadd>();
			list.add(accountadd);
			messageProducer.toAccount(JSON.toJSONString(list));


		} else {
			jsonResult.setMsg("参数不能为空");
		}


		return jsonResult;
	}
	
	
	
	
	
	
	@RequestMapping("rechargeList")
	@MethodName(name = "（导入excel）")
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult rechargeList(HttpServletRequest request) throws Exception {
		JsonResult jsonResult = new JsonResult();
		StringBuffer sb=new StringBuffer("");
		
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator iter = multiRequest.getFileNames();

			while (iter.hasNext()) {
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next()
						.toString());
				if (file != null) {
					// HSSFWorkbook:是操作Excel2003以前（包括2003）的版本，扩展名是.xls
					// XSSFWorkbook:是操作Excel2007的版本，扩展名是.xlsx
					System.out.println("文件名：：：：：：" + file.getOriginalFilename());
					String[][] result = null;
					if (file.getOriginalFilename().endsWith("xls")) {
						result = ExcelUtil.getData2003(file.getInputStream(), 0);
					} else if (file.getOriginalFilename().endsWith("xlsx")) {
						result = ExcelUtil.getData2007(file.getInputStream(), 0);
					}
					int rowLength = result.length;
					A:for (int i = 1; i < rowLength; i++) {
							// 用户账号 真实姓名 币种名称 转入钱包地址 转入个数 金额 单价
							String cellphone = result[i][0];
							String name = result[i][1];
							String coinName = result[i][2];
							String coinAdds = result[i][3];
							String number = result[i][4];

							if (!StringUtils.isEmpty(cellphone)&& !StringUtils.isEmpty(number)) {
								QueryFilter filter = new QueryFilter(ExDigitalmoneyAccount.class);
								filter.addFilter("userName=", cellphone.trim());
								filter.addFilter("publicKey=", coinAdds.trim());
								ExDigitalmoneyAccount account = service.get(filter);
								if(account==null){
									sb.append((i+1)+"行,"+name + "未查询到该用户"+"\r\n");
									continue A;
								}

								ExDmTransaction exDmTransaction = new ExDmTransaction();
								exDmTransaction.setAccountId(account.getId());
								exDmTransaction.setCoinCode(account.getCoinCode());
								exDmTransaction.setCreated(new Date());
								exDmTransaction.setInAddress(account.getPublicKey());
								exDmTransaction.setCurrencyType(account.getCurrencyType());
								exDmTransaction.setWebsite(account.getWebsite());
								exDmTransaction.setCustomerId(account.getCustomerId());
								exDmTransaction.setCustomerName(account.getUserName());
								exDmTransaction.setTrueName(account.getTrueName());
								exDmTransaction.setFee(new BigDecimal(0));
								exDmTransaction.setTransactionMoney(new BigDecimal(number));
								exDmTransaction.setStatus(1);
								exDmTransaction.setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
								// 充币
								exDmTransaction.setTransactionType(1);
								exDmTransaction.setUserId(ContextUtil.getCurrentUserId());
								transactionService.save(exDmTransaction);

								String[] inComeToHotAccount = ((ExDigitalmoneyAccountService) service).inComeToHotAccount(exDmTransaction.getAccountId(),exDmTransaction.getTransactionMoney(),	exDmTransaction.getTransactionNum(),"手动充币", null,null);
								if (CodeConstant.CODE_SUCCESS.equals(inComeToHotAccount[0])) {
									exDmTransaction.setStatus(Integer.valueOf(2));// 充值成功
									transactionService.update(exDmTransaction);
									//sb.append((i+1)+"行,"+name + "充值成功"+"\r\n");
								} else {
									transactionService.delete(exDmTransaction.getId());
									sb.append((i+1)+"行,"+name + "充值失败"+"\r\n");
									System.out.println(name + "充值失败");
								}

							} else {
								sb.append((i+1)+"行,"+name + "参数有误");
								jsonResult.setMsg("参数不能为空");
							}
						}
				}

			}
			
		}
		jsonResult.setCode(StringConstant.SUCCESS);
		jsonResult.setMsg(sb.toString());
		jsonResult.setSuccess(true);
		return jsonResult;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping("/refreshUserCoin")
	@MethodName(name = "单个用户刷币")
	@ResponseBody
	public JsonResult refreshUserCoin(HttpServletRequest request) {
		JsonResult result=new JsonResult();
		try {
			String id = request.getParameter("id");
			ExDigitalmoneyAccount exDigitalmoneyAccount = service.get(Long.valueOf(id));
			//币种code
			 String coinCode=request.getParameter("coinCode").toString();
			 //帐号
			 String account=request.getParameter("account").toString();
			 //数量
			 String count=request.getParameter("count").toString();
			 
			String s= CoinInterfaceUtil
          .refreshUserCoin(coinCode, exDigitalmoneyAccount.getAccountNum().toLowerCase(), count);
			 result.setCode(StringConstant.SUCCESS);
			 result.setMsg("刷新成功，请等待到账！");
			 result.setObj(s);
			 result.setSuccess(true);
			 
		} catch (Exception e) {
			result.setCode(StringConstant.FAIL);
			 result.setMsg("ExDigitalmoneyAccountController refreshUserCoin Err:"+e.getMessage());
			 result.setSuccess(false);
		}
		
			return result;
			
	  }
	@MethodName(name="拨币")
	@RequestMapping(value="/coinPoking", method = RequestMethod.POST)
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult coinPoking(HttpServletRequest req){
		JsonResult result=new JsonResult();
		String id = req.getParameter("id");
		String coinPokingNumber = req.getParameter("coinPokingNumber");
		if(null!=coinPokingNumber&&null!=id){
			ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService)service;
			return exDigitalmoneyAccountService.coinPoking(Long.valueOf(id), coinPokingNumber);
		}else{
			 result.setMsg("拨币失败");
			 result.setSuccess(true);
			 return result;
		}
	}

}
