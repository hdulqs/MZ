/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2017-12-07 14:06:38 
 */
package com.mz.customer.businessman.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.businessman.model.AppBusinessman;
import com.mz.customer.businessman.model.C2cTransaction;
import com.mz.customer.businessman.service.AppBusinessmanService;
import com.mz.customer.businessman.service.C2cTransactionService;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.person.service.AppPersonInfoService;
import com.mz.oauth.user.model.AppUser;
import com.mz.util.QueryFilter;
import com.mz.account.fund.service.AppBankCardService;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright: 北京互融时代软件有限公司
 * 
 * @author: liushilei
 * @version: V1.0
 * @Date: 2017-12-07 14:06:38
 */
@Controller
@RequestMapping("/businessman/c2ctransaction")
public class C2cTransactionController extends BaseController<C2cTransaction, Long> {

	@Resource(name = "c2cTransactionService")
	@Override
	public void setService(BaseService<C2cTransaction, Long> service) {
		super.service = service;
	}

	@Resource
	private AppBusinessmanService appBusinessmanService;

	@Resource
	private AppPersonInfoService appPersonInfoService;

	@Resource
	private AppBankCardService appBankCardService;

	@MethodName(name = "查看C2cTransaction")
	@RequestMapping(value = "/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public JSONObject see(@PathVariable Long id) {
		C2cTransaction c2cTransaction = service.get(id);
		String c2cOrderDetail = ((C2cTransactionService)service).getC2cOrderDetail(c2cTransaction.getTransactionNum());
		return JSON.parseObject(c2cOrderDetail);
	}

	@MethodName(name = "增加C2cTransaction")
	@RequestMapping(value = "/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request, C2cTransaction c2cTransaction) {
		return super.save(c2cTransaction);
	}

	@MethodName(name = "修改C2cTransaction")
	@RequestMapping(value = "/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request, C2cTransaction c2cTransaction) {
		return super.update(c2cTransaction);
	}

	@MethodName(name = "删除C2cTransaction")
	@RequestMapping(value = "/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids) {
		return super.deleteBatch(ids);
	}

	/**
	 * 关闭交易
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/close/{ids}")
	@ResponseBody
	public JsonResult close(@PathVariable String ids,HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		AppUser appUser= (AppUser) request.getSession().getAttribute("user");
		String[] arr = ids.split(",");
		for (String id : arr) {
			jsonResult = ((C2cTransactionService) service).close(Long.valueOf(id),appUser);
		}

		return jsonResult;
	}
	
	
	/**
	 * 确认交易
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/confirm/{ids}")
	@ResponseBody
	public JsonResult confirm(@PathVariable String ids,HttpServletRequest request) {

		JsonResult jsonResult = new JsonResult();
		AppUser appUser= (AppUser) request.getSession().getAttribute("user");
		
		String[] arr = ids.split(",");
		for (String id : arr) {
			jsonResult = ((C2cTransactionService) service).confirm(Long.valueOf(id),appUser,request);
		}

		return jsonResult;
	}

	/**
	 * C2C订单确认前核算用户账
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/checkUserAccount/{ids}")
	@ResponseBody
	public JsonResult checkUserAccount(@PathVariable String ids,HttpServletRequest request) {

		JsonResult jsonResult = new JsonResult();
		AppUser appUser= (AppUser) request.getSession().getAttribute("user");

		String[] arr = ids.split(",");
		for (String id : arr) {
			jsonResult = ((C2cTransactionService) service).checkAccount(Long.valueOf(id),appUser,request);
		}

		return jsonResult;
	}

	@MethodName(name = "列表C2cTransaction")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		String trueName = request.getParameter("trueName_like");
		String surname = request.getParameter("surname_like");

		QueryFilter filter = new QueryFilter(C2cTransaction.class, request);

		//如果名和姓有一个存在的话我就要去查AppPersonInfo表
		if (!(StringUtils.isEmpty(trueName)&&StringUtils.isEmpty(surname))){
			QueryFilter filter1 = new QueryFilter(AppPersonInfo.class);
			//判断添加离线条件
			if (!StringUtils.isEmpty(trueName)&&!StringUtils.isEmpty(surname)){
				filter1.addFilter("trueName_like", "%" +trueName+"%");
				filter1.addFilter("surname_like", "%" +surname+"%");

			}else if (!StringUtils.isEmpty(trueName)){
				filter1.addFilter("trueName_like", "%" +trueName+"%");
				;
			}else if (!StringUtils.isEmpty(surname)){
				filter1.addFilter("surname_like", "%" +surname+"%");

			}
			List<AppPersonInfo> appPersonInfos = appPersonInfoService.find(filter1);
			//如果在AppPersonInfo中没有对应的用户
			if (appPersonInfos==null||appPersonInfos.size()==0){
				//添加一个查不到的条件
				filter.addFilter("id=","0000");
				/*PageResult pageResult = new PageResult();
				pageResult.setDraw(5);
				pageResult.setPage(1);
				pageResult.setPageSize(10);
				pageResult.setRecordsFiltered((long)0);
				pageResult.setRecordsTotal((long)0);
				pageResult.setTotalPage((long)0);
				ArrayList<Object> list = new ArrayList<>();
				pageResult.setRows(list);
				return  pageResult;*/
			}
			String ids = "";
			for (AppPersonInfo appPersonInfo:appPersonInfos) {
				Long id = appPersonInfo.getCustomerId();
				ids +=id+",";
			}
			if (!StringUtils.isEmpty(ids)){
				ids = ids.substring(0,ids.length()-1);
				filter.addFilter("customerId_in",ids);
			}
		}

			filter.setOrderby(" id desc");
			PageResult findPage = super.findPage(filter);

			List<C2cTransaction> rows = findPage.getRows();
		if (rows != null && rows.size() > 0) {
			for (C2cTransaction ct : rows) {
				//---start 新增 2018-4-25 后台添加字段使用 by gt
				Integer status = ct.getStatus();
				switch (status){
					case 1 :
						ct.setStatusByDes("待审核");
						break;
					case 2 :
						ct.setStatusByDes("交易成功");
						break;
					case 3 :
						ct.setStatusByDes("交易取消");
						break;
					default:
						ct.setStatusByDes("");
				}

				Integer type = ct.getTransactionType();
				switch (type) {
					case 1:
						ct.setTransactionTypeByDes("买");
						break;
					case 2:
						ct.setTransactionTypeByDes("卖");
						break;
					default:
						ct.setTransactionTypeByDes("");
				}
				if (ct.getHandleId()!=null&&ct.getHandleId()==0) {
					ct.setHandleId(null);
				}
				//查询卖家银行卡
//				AppBankCard appBankCard = null;
//				Long customerBankId = ct.getCustomerBankId();
//				if(customerBankId!=null&&ct.getTransactionType()==2){
//					QueryFilter q = new QueryFilter(AppBankCard.class);
//					q.addFilter("customerId=", ct.getCustomerId());
//					q.addFilter("isDelete=", 0);
//					appBankCard = appBankCardService.get(q);
//					if(appBankCard!=null){
//						ct.setCardBank(appBankCard.getCardBank());
//						ct.setSubBank(appBankCard.getSubBank());
//						ct.setCardNumber(appBankCard.getCardNumber());
//					}
//
//				}

				//--end 新增

				AppPersonInfo appPersonInfo =appPersonInfoService.getByCustomerId(ct.getCustomerId());
				if(null != appPersonInfo && null!=appPersonInfo.getTrueName() &&null != appPersonInfo.getSurname()){
					ct.setAllName(appPersonInfo.getSurname()+appPersonInfo.getTrueName());
				}


				AppBusinessman appBusinessman = appBusinessmanService.get(ct.getBusinessmanId());
				if (appBusinessman != null) {
					ct.setBusinessmanTrueName(ct.getBusinessman());
					if(null==ct.getBusinessmanTrueName() || "".equals(ct.getBusinessmanTrueName())){
						ct.setBusinessmanTrueName(appBusinessman.getTrueName());
					}
				}
			}
		}
		return findPage;
	}

}
