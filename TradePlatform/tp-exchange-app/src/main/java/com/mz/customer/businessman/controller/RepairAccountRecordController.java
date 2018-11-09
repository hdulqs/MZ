/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2017-12-07 14:06:38 
 */
package com.mz.customer.businessman.controller;

import com.alibaba.fastjson.JSON;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.businessman.model.RepairAccountRecord;
import com.mz.customer.businessman.service.RepairAccountRecordService;
import com.mz.oauth.user.model.AppUser;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.mq.producer.service.MessageProducer;
import com.mz.trade.redis.model.Accountadd;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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
@RequestMapping("/businessman/repairaccountrecord")
public class RepairAccountRecordController extends BaseController<RepairAccountRecord, Long> {

	private final static Logger log = Logger.getLogger(RepairAccountRecordController.class);
	/**
	 * 注册类型属性编辑器
	 *
	 * @param binder
	 */
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {

		// 系统注入的只能是基本类型，如int，char，String

		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class, new DateTimePropertyEditorSupport());
		/**
		 * 防止XSS攻击，并且带去左右空格功能
		 */
		binder.registerCustomEditor(String.class, new StringPropertyEditorSupport(true, false));
	}

	@Resource(name = "repairAccountRecordService")
	@Override
	public void setService(BaseService<RepairAccountRecord, Long> service) {
		super.service = service;
	}

	@Resource
	private RepairAccountRecordService repairAccountRecordService;


	@Resource
	private  MessageProducer messageProducer;



	/**
	 * 修复账号
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/repairaccount")
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult repairaccount(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		String customerId = request.getParameter("customerId");
		String AccountId = request.getParameter("AccountId");
		String hotmoney = request.getParameter("hotmoney");
		String coldmoney = request.getParameter("coldmoney");

		AppUser appUser= (AppUser) request.getSession().getAttribute("user");
		if(null == appUser ) {

			// 热账户操作
			Accountadd accountadd = new Accountadd();
			accountadd.setAccountId(Integer.valueOf(AccountId).longValue());
			accountadd.setMoney(new BigDecimal(hotmoney));
			accountadd.setMonteyType(1);
			accountadd.setAcccountType(1);
			accountadd.setRemarks(53);

			accountadd.setTransactionNum("XIU");

			//保存币账户操作记录
			repairAccountRecordService.saveRecord(Integer.valueOf(customerId).longValue(), accountadd.getAccountId(), accountadd.getMoney(), accountadd.getMonteyType(), accountadd.getAcccountType(), accountadd.getTransactionNum(), "币账户修复");


			// 冷账户增加
			Accountadd accountadd2 = new Accountadd();
			accountadd2.setAccountId(Integer.valueOf(AccountId).longValue());
			accountadd2.setMoney(new BigDecimal(coldmoney));
			accountadd2.setMonteyType(2);
			accountadd2.setAcccountType(1);
			accountadd2.setRemarks(53);
			accountadd2.setTransactionNum("XIU");

			//保存币账户操作记录
			repairAccountRecordService.saveRecord(Integer.valueOf(customerId).longValue(), accountadd2.getAccountId(), accountadd2.getMoney(), accountadd2.getMonteyType(), accountadd2.getAcccountType(), accountadd2.getTransactionNum(), "币账户修复");


			List<Accountadd> list = new ArrayList<Accountadd>();
			list.add(accountadd);
			list.add(accountadd2);

			messageProducer.toAccount(JSON.toJSONString(list));
		}
		return jsonResult;
	}
	


}
