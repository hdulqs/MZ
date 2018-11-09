/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2017-12-07 14:06:38 
 */
package com.mz.customer.businessman.controller;

import com.alibaba.fastjson.JSONObject;
import com.mz.account.fund.model.AppBankCard;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.businessman.model.OtcOrderTransaction;
import com.mz.customer.businessman.service.OtcOrderTransactionService;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.person.service.AppPersonInfoService;
import com.mz.util.QueryFilter;
import com.mz.account.fund.service.AppBankCardService;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.remote.RemoteAppCustomerService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.otc.OtcOrderTransactionMange;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:
 * 
 * @author: zongwei
 * @version: V1.0
 * @Date: 20180526
 */
@Controller
@RequestMapping("/businessman/otcOrdertransaction")
public class OtcOrderTransactionController extends BaseController<OtcOrderTransaction, Long> {

	@Resource(name = "otcOrderTransactionService")
	@Override
	public void setService(BaseService<OtcOrderTransaction, Long> service) {
		super.service = service;
	}


	@Resource
	private AppPersonInfoService appPersonInfoService;
	@Resource
	private  RemoteManageService remoteManageService;

	@Resource
	private RemoteAppCustomerService remoteAppCustomerService;

	@Resource
	private AppBankCardService    appBankCardService;

	@RequestMapping("/otcPayInfor")
	@ResponseBody
	public JSONObject otcPayInfor(HttpServletRequest request) {
		String transactionorderid = request.getParameter("id");
		JSONObject data =  new JSONObject();
		JSONObject userinfo =  new JSONObject();

		OtcOrderTransactionMange otcOrderTransactionMange =	remoteManageService.selectOtcOrderbyid(Integer.valueOf(transactionorderid).longValue());

		QueryFilter q = new QueryFilter(AppBankCard.class);
		q.addFilter("customerId=", otcOrderTransactionMange.getSellCustomId());
		q.addFilter("isDelete=", 0);
		AppBankCard  appBankCard = appBankCardService.get(q);

		User user = remoteManageService.selectByustomerId(otcOrderTransactionMange.getSellCustomId());



		userinfo.put("username",user.getUsername());
		userinfo.put("name",user.getSurname() + user.getTruename());
		userinfo.put("phone",user.getPhone());



				;
		data.put("userinfo", userinfo);
		data.put("Bankinfo", appBankCard);
		data.put("orderinfo", otcOrderTransactionMange);
		return data;
	}


	@RequestMapping("/applyArbitrationinfo")
	@ResponseBody
	public JSONObject applyArbitrationinfo(HttpServletRequest request) {
		String transactionorderid = request.getParameter("id");
		JSONObject data =  new JSONObject();

		OtcOrderTransactionMange otcOrderTransactionMange =	remoteManageService.selectOtcOrderbyid(Integer.valueOf(transactionorderid).longValue());

		User buyuser = remoteManageService.selectByustomerId(otcOrderTransactionMange.getBuyCustomId());
		User selluser = remoteManageService.selectByustomerId(otcOrderTransactionMange.getSellCustomId());
		User Appealuser = remoteManageService.selectByustomerId(otcOrderTransactionMange.getAppealCustomId());

		if(("Refuse").equals(otcOrderTransactionMange.getAppealHandle())){
			otcOrderTransactionMange.setAppealHandle("驳回申诉");
		}else if(("Colse").equals(otcOrderTransactionMange.getAppealHandle() )){
			otcOrderTransactionMange.setAppealHandle("订单关闭");
		}else if (("Finish").equals(otcOrderTransactionMange.getAppealHandle())){
			otcOrderTransactionMange.setAppealHandle("交易完成");
		}

		data.put("buyuser",buyuser);
		data.put("selluser",selluser);
		data.put("Appealuser", Appealuser);
		data.put("orderinfo", otcOrderTransactionMange);
		return data;
	}





	@MethodName(name = "列表otcTransaction")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(OtcOrderTransaction.class, request);
		filter.setOrderby(" id desc");
		PageResult findPage = super.findPage(filter);
		List<OtcOrderTransaction> rows = findPage.getRows();
		if (rows != null && rows.size() > 0) {
			for (OtcOrderTransaction ct : rows) {

				Integer status = ct.getStatus();
				switch (status){
					case 1 :
						ct.setStatusByDes("待付款");
						break;
					case 2 :
						ct.setStatusByDes("已付款");
						break;
					case 3 :
						ct.setStatusByDes("已取消");
						break;
					case 4 :
						ct.setStatusByDes("已完成");
						break;
					case 5 :
						ct.setStatusByDes("已关闭");
						break;
					case 6 :
						ct.setStatusByDes("申诉中");
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
				AppPersonInfo buyappPersonInfo =appPersonInfoService.getByCustomerId(ct.getBuyCustomId());
				ct.setBuyCustomname(buyappPersonInfo.getMobilePhone());
				if(null != buyappPersonInfo && null!=buyappPersonInfo.getTrueName() &&null != buyappPersonInfo.getSurname()){
					ct.setBuyallName(buyappPersonInfo.getSurname()+buyappPersonInfo.getTrueName());
				}

				AppPersonInfo sellappPersonInfo =appPersonInfoService.getByCustomerId(ct.getSellCustomId());
				ct.setSellCustomname(sellappPersonInfo.getMobilePhone());
				if(null != sellappPersonInfo && null!=sellappPersonInfo.getTrueName() &&null != sellappPersonInfo.getSurname()){
					ct.setSellallName(sellappPersonInfo.getSurname()+sellappPersonInfo.getTrueName());
				}

			}
		}
		return findPage;
	}


	@MethodName(name = "otcColse")
	@RequestMapping(value = "/otcColse")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult otcColse(HttpServletRequest request) {
		String id = request.getParameter("id");
		OtcOrderTransactionMange	otcOrderTransactionMange = new OtcOrderTransactionMange();
		otcOrderTransactionMange.setId(Integer.valueOf(id).longValue());
		JsonResult jsonResult = ((OtcOrderTransactionService) service).otcColse(otcOrderTransactionMange);
		return jsonResult;
	}

	@MethodName(name = "finishOtcOrder")
	@RequestMapping(value = "/finishOtcOrder")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult finishOtcOrder(HttpServletRequest request) {
		String id = request.getParameter("id");
		OtcOrderTransactionMange	otcOrderTransactionMange = new OtcOrderTransactionMange();
		otcOrderTransactionMange.setId(Integer.valueOf(id).longValue());
		JsonResult jsonResult = ((OtcOrderTransactionService) service).finishOtcOrder(otcOrderTransactionMange);
		return jsonResult;
	}


	@MethodName(name = "otcrefuse")
	@RequestMapping(value = "/otcrefuse")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult otcrefuse(HttpServletRequest request) {
		String id = request.getParameter("id");
		OtcOrderTransactionMange	otcOrderTransactionMange = new OtcOrderTransactionMange();
		otcOrderTransactionMange.setId(Integer.valueOf(id).longValue());
		JsonResult jsonResult = ((OtcOrderTransactionService) service).otcrefuse(otcOrderTransactionMange);
		return jsonResult;
	}



}
