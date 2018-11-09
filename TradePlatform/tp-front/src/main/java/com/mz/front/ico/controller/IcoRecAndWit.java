package com.mz.front.ico.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.ico.RemoteIcoService;
import com.mz.manage.remote.ico.model.AppIcoCoinAccountDTO;
import com.mz.manage.remote.model.ExDmCustomerPublicKeyManage;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.util.SessionUtils;
import com.mz.util.sys.SpringContextUtil;;

@Controller
@RequestMapping("/ico/recandwit")
public class IcoRecAndWit {

	@Resource
	private RemoteIcoService remoteIcoService;//ico远程调用接口
	
	/**
	 * 充币页面
	 * @return
	 */
	@RequestMapping("/listAccount")
	public ModelAndView listAccount(HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		User user = SessionUtils.getUser(request);
		if(user != null){
			List<AppIcoCoinAccountDTO> listAccount = remoteIcoService.listAccount(user.getCustomerId());
			if(listAccount!=null && listAccount.size()>0){
				mv.addObject("firstHot", listAccount.get(0).getHotMoney());
				mv.addObject("firstCold", listAccount.get(0).getColdMoney());
				mv.addObject("publicKey", listAccount.get(0).getPublicKey());
				mv.addObject("coincode", listAccount.get(0).getCoinCode());
				mv.addObject("listAccount", listAccount);
			}
			mv.setViewName("/front/ico/temp/icobtcpost");
		}else{
			mv.setViewName("/login");
		}
		return mv;
	}
	
	/**
	 * 查询充值币的记录
	 * 交易类型(1充币 ，2提币)'
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public FrontPage list(HttpServletRequest request) {
		//User user = (User)request.getSession().getAttribute("user");
		User user = SessionUtils.getUser(request);
		String status = request.getParameter("status");
		String transactionType = request.getParameter("transactionType");
		Map<String, String> params = HttpServletRequestUtils.getParams(request);
		params.put("customerId", user.getCustomerId().toString());
		params.put("transactionType", transactionType);
		params.put("status", status);
		if("0".equals(status)){//0查全部
			params.put("status",null);
		}
		return remoteIcoService.findIcotransaction(params);
	}
	
	/**
	 * 跳转到提币页面
	 * @return
	 */
	@RequestMapping("/icojump")
	public ModelAndView icojump(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		User user = SessionUtils.getUser(request);
		//未实名，往实名页跳
/*		if(user.getIsReal()==0){
			mav.setViewName("front/user/realname");
			return mav;
		}else{//已实名
*/			if(user!=null){
				List<AppIcoCoinAccountDTO> list = remoteIcoService.listAccount(user.getCustomerId());
				
				if(list!=null && list.size()>0){
					mav.addObject("list", list);
					mav.addObject("firstHot", list.get(0).getHotMoney());
					mav.addObject("firstCold", list.get(0).getColdMoney());
					mav.addObject("publicKey", list.get(0).getPublicKey());
					mav.addObject("coinType", list.get(0).getCoinCode());
					mav.addObject("currencyType", list.get(0).getCurrencyType());
					mav.addObject("coincode", list.get(0).getCoinCode());
					mav.addObject("paceFeeRate", list.get(0).getPaceFeeRate());
					mav.addObject("leastPaceNum", list.get(0).getLeastPaceNum());
					mav.addObject("oneDayPaceNum", list.get(0).getOneDayPaceNum());
					
					RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
					List<ExDmCustomerPublicKeyManage> list2 = remoteAppBankCardManageService.getList(user.getCustomerId(),list.get(0).getCoinCode());
					if(list2!=null && list2.size()>0){
						mav.addObject("list2", list2);
					}
				}
			}
			mav.setViewName("front/ico/temp/icobtcget");
			return mav;
		//}
	}
	
	/**
	 * 提币ico
	 * @return
	 */
	@RequestMapping("/getIco")
	@ResponseBody
	public JsonResult getIco(HttpServletRequest request){
		String accountPassWord = request.getParameter("accountPassWord");
		String withdrawCode = request.getParameter("withdrawCode");
		String coinType = request.getParameter("coinType");
		String btcNum = request.getParameter("btcNum");
		String btcKey = request.getParameter("btcKey");
		
		String accounCoinSmsCode = (String) request.getSession().getAttribute("accounCoinSmsCode");
		if(withdrawCode==null || "".equals(withdrawCode)){
			return new JsonResult().setSuccess(false).setMsg("交易密码不能为空");
		}
		if(!withdrawCode.equals(accounCoinSmsCode)){
			return new JsonResult().setSuccess(false).setMsg("交易密码不正确");
		}
		
		JsonResult js = new JsonResult();
		User user = SessionUtils.getUser(request);
		//未实名，往实名页跳
		if(user.getIsReal()==0){
			js.setSuccess(false).setMsg("请先进行实名认证");
			return js;
		}else{//已实名 提币
			remoteIcoService.getIco(user, coinType, new BigDecimal(btcNum), accountPassWord,btcKey);
			js.setSuccess(true).setMsg("提币成功");
			return js;
		}
	}
}
