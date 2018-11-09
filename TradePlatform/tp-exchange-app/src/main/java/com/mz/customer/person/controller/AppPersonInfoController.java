/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      zhangcb
 * @version:     V1.0 
 * @Date:        2016-11-22 18:25:52 
 */
package com.mz.customer.person.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.user.model.AppCustomer;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.change.remote.account.RemoteExDigitalmoneyAccountService;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.person.service.AppPersonInfoService;
import com.mz.customer.user.service.AppCustomerService;
import com.mz.web.remote.RemoteAppConfigService;
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
 * Copyright:   北京互融时代软件有限公司
 * @author:      zhangcb
 * @version:     V1.0 
 * @Date:        2016-11-22 18:25:52 
 */
@Controller
@RequestMapping("/person/apppersoninfo")
public class AppPersonInfoController extends BaseController<AppPersonInfo, Long> {
	
	@Resource(name = "appCustomerService")
	public AppCustomerService appCustomerService;
	
	@Resource(name = "appPersonInfoService")
	@Override
	public void setService(BaseService<AppPersonInfo, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppPersonInfo")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppPersonInfo see(@PathVariable Long id){
    	AppPersonInfoService appPersonInfoService =(AppPersonInfoService)service;
    	List<AppPersonInfo> list=appPersonInfoService.getById(id);
    	return list.size()>0?list.get(0):null;
    			
	}
	
	@MethodName(name="增加AppPersonInfo")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppPersonInfo appPersonInfo){
		return super.save(appPersonInfo);
	}
	
	@MethodName(name="修改AppPersonInfo")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppPersonInfo appPersonInfo){
		return super.update(appPersonInfo);
	}
	
	@MethodName(name="删除AppPersonInfo")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppPersonInfo")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
    	QueryFilter filter = new QueryFilter(AppPersonInfo.class, request);
    	PageResult findPageBySql = ((AppPersonInfoService)service).findPageBySql(filter);
    	return findPageBySql;
	}
	
	/**
	 * 金科新加用户资金报表
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param request
	 * @param:    @return
	 * @return: PageResult 
	 * @Date :          2017年4月12日 上午11:21:19   
	 * @throws:
	 */
	@MethodName(name = "列表listPersonReport")
	@RequestMapping("/listPersonReport")
	@ResponseBody
	public PageResult listPersonReport(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppPersonInfo.class, request);
		PageResult findPageBySql = ((AppPersonInfoService)service).findPageBySql1(filter);
		return findPageBySql;
	}
	
	/**
	 * 注册用户审核
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param ids
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2016年11月25日 上午11:29:32   
	 * @throws:
	 */
	@MethodName(name = "注册用户审核")
	@RequestMapping(value="/updateExamine",method=RequestMethod.POST)
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult updateExamine(AppPersonInfo appPersonInfo){
		JsonResult result = new JsonResult();
		AppPersonInfoService appPersonInfoService = (AppPersonInfoService)service;
		if(appPersonInfoService.updateExamine(appPersonInfo)&&appPersonInfo.getIsExamine()==1){
			AppCustomer appCustomer = appCustomerService.get(appPersonInfo.getCustomerId());
			appCustomer.setIsReal(Integer.valueOf(1));//实名认证
			appCustomerService.update(appCustomer);
			result.setSuccess(true);
			result.setMsg("审核成功");
		}else if(appPersonInfoService.updateExamine(appPersonInfo)&&appPersonInfo.getIsExamine()==2){
			result.setSuccess(true);
			result.setMsg("拒绝成功");
		}
		return result;
	}
	
	
	@MethodName(name = "查看AppPersonInfo")
	@RequestMapping(value="/seeByCustomerId/{id}")
	@ResponseBody
	public AppPersonInfo seeByCustomerId(@PathVariable Long id){
		QueryFilter filter = new QueryFilter(AppPersonInfo.class);
		filter.addFilter("customerId=", id);
		AppPersonInfo info=((AppPersonInfoService)service).get(filter);
		return info;
		
	}
	
	/**
	 * 获取所有代理商的列表
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param request
	 * @param:    @return
	 * @return: PageResult 
	 * @Date :          2017年3月14日 上午10:16:27   
	 * @throws:
	 */
	@MethodName(name = "代理商申请列表")
	@RequestMapping("/agentApplyList")
	@ResponseBody
	public PageResult agentApplyList(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppPersonInfo.class, request);
		PageResult findPageBySql = ((AppPersonInfoService)service).findAgentApplyList(filter);
		return findPageBySql;
	}
	
	
	/***
	 * 代理商申请审核通过
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param request
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2017年3月14日 上午10:16:45   
	 * @throws:
	 */
	@MethodName(name = "代理商申请审核通过")
	@RequestMapping("/agentApplyPass")
	@ResponseBody
	public JsonResult agentApplyPass(HttpServletRequest request){
		JsonResult result = new JsonResult();
		String id=request.getParameter("id");
		if(id==null || "".equals(id)){
			result.setSuccess(false);
			result.setMsg("参数不合法或者未查到该客户信息！");
			return result;
		}
		AppPersonInfo person = ((AppPersonInfoService)service).get(Long.valueOf(id));
		//获取该用户得状态
		int jkAgentType=person.getJkAgentType();
		if(jkAgentType!=0){//不是会员
			result.setSuccess(false);
			result.setMsg("该客户已经是代理商了！");
			return result;
		}
		
		//获取该客户要申请得信息
		int applyState=person.getJkApplyState();
		if(applyState!=0 && applyState!=2){
			result.setSuccess(false);
			result.setMsg("该客户申请状态不正确！");
			return result;
		}
		
		//判断申请的该地区的代理是否已存在
		boolean isAgentExist=((AppPersonInfoService)service).isAgentExist(person.getJkApplyType()+"",
				person.getJkApplyAgentProvinceCode(),person.getJkApplyAgentCityCode(),person.getJkApplyAgentCountyCode());
		if(isAgentExist){
			result.setSuccess(false);
			result.setMsg("申请的代理上在该地区已存在！请直接驳回!");
			return result;
		}
		
		
		
		
		
		//获取客户得申请信息,把正式信息更新上
		person.setJkAgentType(person.getJkApplyType());
		
		//获取名字
		person.setJkAgentName(getAgentName(person));
		person.setJkAgentProvince(person.getJkApplyAgentProvince());
		person.setJkAgentProvinceCode(person.getJkApplyAgentProvinceCode());
		person.setJkAgentCity(person.getJkApplyAgentCity());
		person.setJkAgentCityCode(person.getJkApplyAgentCityCode());
		person.setJkAgentCounty(person.getJkApplyAgentCounty());
		person.setJkAgentCountyCode(person.getJkApplyAgentCountyCode());
		
		//金科的申请代理商状态   -1 无申请  0申请中  1审核成功  2 审核拒绝
		person.setJkApplyState(1);
		((AppPersonInfoService)service).update(person);
		
		result.setSuccess(true);
		result.setMsg("审核通过成功！");
		return result;
	}


	/***
	 * 代理商申请审核拒绝
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param request
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2017年3月14日 上午10:16:45   
	 * @throws:
	 */
	@MethodName(name = "代理商申请审核拒绝")
	@RequestMapping("/agentApplyRefuse")
	@ResponseBody
	public JsonResult agentApplyRefuse(HttpServletRequest request){
		JsonResult result = new JsonResult();
		//先获取申请的代理需要的币数量
		BigDecimal needCoinNum=null;
		
		String id=request.getParameter("id");
		if(id==null || "".equals(id)){
			result.setSuccess(false);
			result.setMsg("参数不合法或者未查到该客户信息！");
			return result;
		}
		
		AppPersonInfo person = ((AppPersonInfoService)service).get(Long.valueOf(id));
	
		
		//申请的类型
		int agentLevel=person.getJkApplyType();
		if(agentLevel==3){//省代
			needCoinNum=new BigDecimal(getConfigValue("provinceAgentNeedCoinNum"));
		}else if(agentLevel==2){//市代
			needCoinNum=new BigDecimal(getConfigValue("cityAgentNeedCoinNum"));
		}else if(agentLevel==1){//区代
			needCoinNum=new BigDecimal(getConfigValue("countyAgentNeedCoinNum"));
		}
		//币的数量不正确，未获取到
		if(needCoinNum==null){
			result.setSuccess(false);
			result.setMsg("申请代理所需币的数量未从配置中查询到！！！");
			return result;
		}
		//然后冻结解冻
		//判断该申请人的币账户是否有足够的币,有的话冻结
		RemoteExDigitalmoneyAccountService remoteExDigitalmoneyAccountService=(RemoteExDigitalmoneyAccountService) ContextUtil.getBean("remoteExDigitalmoneyAccountService");
		ExDigitalmoneyAccount coinAccount=remoteExDigitalmoneyAccountService.getByCustomerIdAndType(person.getCustomerId(), "JKC","cny", "cn");
		if(coinAccount==null || coinAccount.getColdMoney().compareTo(needCoinNum)<0){
			//不够
			result.setSuccess(false);
			result.setMsg("未查到币账户或该用户币账户冻结余额不足，可能账户异常！需要数量"+needCoinNum);
			return result;
		}
		
		
		//金科的申请代理商状态   -1 无申请  0申请中  1审核成功  2 审核拒绝
		person.setJkApplyState(2);
		((AppPersonInfoService)service).update(person);
		
		//冻结解冻
		coinAccount.setHotMoney(coinAccount.getHotMoney().add(needCoinNum));
		coinAccount.setColdMoney(coinAccount.getColdMoney().subtract(needCoinNum));
		remoteExDigitalmoneyAccountService.updateAccount(coinAccount);
		System.out.println("申请代理商审核拒绝冻结币解冻成功！");
		
		result.setSuccess(true);
		result.setMsg("审核拒绝成功,冻结币解冻成功！");
		return result;
	}
	
	
	/**
	 * 获取代理商代理商类型名称（例：山东省省代，洛阳市市代）
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param person
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年3月14日 上午10:18:39   
	 * @throws:
	 */
	private String getAgentName(AppPersonInfo person) {
		//金科的申请成为代理商类型   0 会员 1 区代（县代）  2 市代   3省代
		String agentName="";
		int jkApplyType=person.getJkApplyType();
		if(jkApplyType==1){
			//获取区的名字
			String jkApplyAgentCounty=person.getJkApplyAgentCounty();
			if(jkApplyAgentCounty.endsWith("区")){
				agentName=jkApplyAgentCounty+"区代";
			}else if(jkApplyAgentCounty.endsWith("县")){
				agentName=jkApplyAgentCounty+"县代";
			}else{
				agentName=jkApplyAgentCounty+"代理";
			}
		}else if(jkApplyType==2){
			//获取市的名字
			String jkApplyAgentCity=person.getJkApplyAgentCity();
			if(jkApplyAgentCity.endsWith("市")){
				agentName=jkApplyAgentCity+"市代";
			}else{
				agentName=jkApplyAgentCity+"代理";
			}
		}else if(jkApplyType==3){
			//获取省的名字
			String jkApplyAgentprovince=person.getJkApplyAgentProvince();
			if(jkApplyAgentprovince.endsWith("省")){
				agentName=jkApplyAgentprovince+"省代";
			}else{
				agentName=jkApplyAgentprovince+"代理";
			}
		}
		return agentName;
	}
	
	
	
	/**
	 * 获取app_config配置
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param type
	 * @param:    @return
	 * @return: String 
	 * @Date :          2016年12月8日 上午10:50:01   
	 * @throws:
	 */
	public static String  getConfigValue(String type) {
		RemoteAppConfigService remoteAppConfigService  = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String value= remoteAppConfigService.getValueByKey(type);
		return  value;
	}
}
