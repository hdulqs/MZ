/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月31日 下午6:55:57
 */
package com.mz.account.fund.controller;

import com.mz.account.fund.model.AppOurAccount;
import com.mz.account.fund.model.AppTransaction;
import com.mz.account.fund.model.FeeWithdrawalsRecord;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.util.http.HttpsRequest;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.account.fund.service.AppOurAccountService;
import com.mz.account.fund.service.AppTransactionService;
import com.mz.core.constant.StringConstant;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.exchange.account.service.FeeWithdrawalsRecordService;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月31日 下午6:55:57 
 */
@Controller
@RequestMapping("/fund/appouraccount")
public class AppOurAccountController extends BaseController<AppOurAccount, Long>{
	
	@Resource(name="appOurAccountService")
	@Override
	public void setService(BaseService<AppOurAccount, Long> service) {
		super.service = service;
	}
	
	@Resource
	private AppTransactionService appTransactionService;
	
    @MethodName(name = "查询卡号list")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
    	String coinCode = request.getParameter("coinCode");
    	String accountType = request.getParameter("accountType");
    	QueryFilter filter = new QueryFilter(AppOurAccount.class, request);
    	if(null != coinCode){
    		filter.addFilter("currencyType=", coinCode);
    	}
    	
    	if(null != accountType){
    		if(2 == Integer.valueOf(accountType)){
    			filter.addFilter("accountType_in", "0,2");
    		}
    	}
    	
    	String ico=request.getParameter("ico");
    	if("1".equals(ico)){
    		filter.addFilter("openAccountType_in", "2,3");
    	}else{
    		filter.addFilter("openAccountType_notin", "2,3");
    	}
    	PageResult page = super.findPage(filter);
    	//充值-提现
    	AppOurAccount appOurAccount = null;
    	List<AppOurAccount> listappOurAccount = (List<AppOurAccount>) page.getRows();
    	if(listappOurAccount.size()!=0){
    		for(int j=0;j<listappOurAccount.size();j++){
        		if(listappOurAccount.get(j).getIsShow()==1){
        			appOurAccount = listappOurAccount.get(j);
        		}
        	}
        	
        	if(appOurAccount!=null){
        		QueryFilter qf = new QueryFilter(AppTransaction.class);
            	qf.addFilter("transactionType_in", "3,4");
            	qf.addFilter("status=", "2");
            	qf.addFilter("ourAccountNumber=", appOurAccount.getAccountNumber());
            	List<AppTransaction> listtr = appTransactionService.find(qf);
            	
            	BigDecimal add = new BigDecimal(0);
            	BigDecimal subtract = new BigDecimal(0);
            	for(int i=0;i<listtr.size();i++){
            		if(listtr.get(i).getTransactionType()==3){
            			add = add.add(listtr.get(i).getTransactionMoney());
            		}
            		if(listtr.get(i).getTransactionType()==4){
            			subtract = subtract.add(listtr.get(i).getTransactionMoney());
            		}
            	}
            	BigDecimal sum = add.subtract(subtract);
            	appOurAccount.setAccountMoney(sum);
            	super.update(appOurAccount);
        	}
    	}
    	return page;
	}
	
    
	@MethodName(name = "查看")
	@RequestMapping(value="/see/{id}",method=RequestMethod.GET)
	@MyRequiresPermissions
	@ResponseBody
	public AppOurAccount see(@PathVariable Long id){
		AppOurAccount appOurAccount = service.get(id);
		return appOurAccount;
	}
    
    
	@MethodName(name="增加")
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppOurAccount appOurBank){
		
		AppOurAccountService appOurAccountService = (AppOurAccountService)service;
		JsonResult result = appOurAccountService.saveOurAccount(appOurBank,0);
		return result;
		
	}
	
	
	@MethodName(name="删除")
	@RequestMapping(value="/remove/{ids}",method=RequestMethod.DELETE)
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	
	@MethodName(name="修改")
	@RequestMapping(value="/modify", method = RequestMethod.POST)
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppOurAccount appOurAccount){
		
		AppOurAccountService appOurAccountService = (AppOurAccountService)service;
		JsonResult result = appOurAccountService.saveOurAccount(appOurAccount, 1);
		return result;
	}
	
	
	@RequestMapping("/coinServiceInfo")
	@MethodName(name = "钱包服务器币种信息")
	@ResponseBody
	public JsonResult coinServiceInfo() {
		JsonResult result=new JsonResult();
		try {
			QueryFilter filter = new QueryFilter(AppOurAccount.class);
			filter.addFilter("accountType=", "1");
			filter.addFilter("openAccountType=", "1");
			
			List<AppOurAccount> list = super.find(filter);
			BigDecimal coinTotalMoney=BigDecimal.ZERO;
			BigDecimal withdrawMoney=BigDecimal.ZERO;
			for(AppOurAccount account:list){
				coinTotalMoney=new BigDecimal(balance("",account.getCurrencyType()));
				withdrawMoney=new BigDecimal(balance(account.getAccountName(),account.getCurrencyType()));
				account.setWithdrawMoney(withdrawMoney);
				account.setCoinTotalMoney(coinTotalMoney);
			}
			
			result.setCode(StringConstant.SUCCESS);
			result.setMsg("查询成功");
			result.setObj(list);
			 result.setSuccess(true);
			
		} catch (Exception e) {
			result.setCode(StringConstant.FAIL);
			 result.setMsg("AppOurAccountController coinServiceInfo Err:"+e.getMessage());
			 result.setSuccess(false);
		}
		
			return result;
			
	  }
	

	private  String  balance(String account,String coinCode) {
		    String ss="0.00";
		try{
			String url=PropertiesUtils.APP.getProperty("app.coinip");
			ss=HttpsRequest.post(url+"/coin/coin/balance?userName="+account+"&type="+coinCode);
		}catch(Exception e){
			System.out.println("err:"+e.getMessage());
		}
		  return ss;
	}
	
	
	
	
	@RequestMapping("/feeWithdrawals")
	@MethodName(name = "手续费账户提现")
	@ResponseBody
	public JsonResult feeWithdrawals(HttpServletRequest req) {
    	JsonResult result =new JsonResult();
		Long id = Long.valueOf(req.getParameter("id"));
		BigDecimal money = new BigDecimal(req.getParameter("money"));
		if(id==null || money==null){
			result.setMsg("我方资金账户ID或提现金额为空！");
			result.setSuccess(false);
			return result;
		}
		
		//获取交易商账户
		AppOurAccount account=((AppOurAccountService)service).get(id);
		if(account!=null){
			BigDecimal rewardMoney=account.getAccountFee();
			if(money.compareTo(rewardMoney)>0){//派发金额大于可派发佣金金额
				result.setMsg("提现金额大于手续费账户可提现金额！");
				result.setSuccess(false);
				return result;
			}
			
			
			//手续费账户减去这笔钱
			account.setAccountFee(account.getAccountFee().subtract(money));
			//已提现手续费金额加上这笔钱
			account.setHasOutFee(account.getHasOutFee().add(money));
			
			//加上派发记录
			FeeWithdrawalsRecordService recordService=(FeeWithdrawalsRecordService) ContextUtil.getBean("feeWithdrawalsRecordService");
			FeeWithdrawalsRecord record=new FeeWithdrawalsRecord();
			record.setOurAccountId(id);
			record.setOurAccountNum(account.getAccountNumber());
			record.setWithdrawalsMoney(money);
			record.setStatus(0);//流水状态(0成功  1失败)
			record.setFailMsg("提现成功！");
			recordService.save(record);
			
			//更改
			((AppOurAccountService)service).update(account);
			result.setMsg("派发成功！");
			result.setSuccess(true);
		}else{
			result.setMsg("未查到代理商账户！");
			result.setSuccess(false);
		}
		
		return result;
	}
}	
