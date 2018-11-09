/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0
 * @Date:        2017-12-07 14:06:38 
 */
package com.mz.customer.businessman.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.mz.account.fund.model.AppBankCard;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageFactory;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.customer.businessman.model.OtcCoin;
import com.mz.customer.businessman.model.OtcOrderTransaction;
import com.mz.customer.businessman.model.OtcTransaction;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.person.service.AppPersonInfoService;
import com.mz.customer.user.model.AppCustomer;
import com.mz.customer.user.service.AppCustomerService;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.util.QueryFilter;
import com.mz.util.date.DateUtil;
import com.mz.util.idgenerate.IdGenerate;
import com.mz.util.idgenerate.NumConstant;
import com.mz.util.sys.ContextUtil;
import com.mz.account.fund.service.AppBankCardService;
import com.mz.change.remote.account.RemoteExDigitalmoneyAccountService;
import com.mz.customer.businessman.dao.OtcOrderTransactionDao;
import com.mz.customer.businessman.service.OtcAccountRecordService;
import com.mz.customer.businessman.service.OtcOrderTransactionService;
import com.mz.customer.businessman.service.OtcTransactionService;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import com.mz.front.redis.model.UserRedis;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.manage.remote.model.otc.OtcOrderTransactionMange;
import com.mz.mq.producer.service.MessageProducer;
import com.mz.trade.redis.model.Accountadd;
import com.mz.trade.redis.model.ExDigitalmoneyAccountRedis;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.mz.util.UserRedisUtils;


/**
 * <p>
 * OtcTransactionService
 * </p>
 * 
 * @author: liushilei
 * @Date : 2017-12-07 14:06:38
 */
@Service("otcOrderTransactionService")
public class OtcOrderTransactionServiceImpl extends BaseServiceImpl<OtcOrderTransaction, Long> implements OtcOrderTransactionService {

	private static Object lock = new Object();
	private static final Logger log = Logger.getLogger(OtcOrderTransactionServiceImpl.class);

	@Resource
	private MessageProducer messageProducer;

	@Resource
	private ExDmTransactionService exDmTransactionService;

	@Resource
	private ExDigitalmoneyAccountService exDigitalmoneyAccountService;

	@Resource
	private OtcTransactionService otcTransactionService;

	@Resource
	private AppBankCardService appBankCardService;

	@Resource
	private AppCustomerService appCustomerService;

	@Resource
	private OtcAccountRecordService otcAccountRecordService;

	@Resource
	private AppPersonInfoService appPersonInfoService;

	@Resource(name = "otcOrderTransactionDao")
	@Override
	public void setDao(BaseDao<OtcOrderTransaction, Long> dao) {
		super.dao = dao;
	}

	/**
	 * 查询otc从清单中购买
	 * @author zongwei
	 * @param id  清单id
	 * @param customerId  操作者id
	 * @time  2018-05-08
	 * @return
	 */
	@Override
	public synchronized JsonResult createOrderTransaction(Long customerId, Long id,BigDecimal transactioncount) {

		JsonResult jsonResult = new JsonResult();
		OtcOrderTransaction otcOrderTransaction = new OtcOrderTransaction();

		synchronized (lock) {
			AppCustomer appCustomer = appCustomerService.get(customerId);
			if(appCustomer.getOtcFrozenDate() != null) {
				String otcFrozenString = DateUtil.dateToString(appCustomer.getOtcFrozenDate(), "yyyy-MM-dd HH:mm");
				Date date = new Date();
				//Date startDate = DateUtil.addMinToDate(date, 2880);
				String startDateString = DateUtil.dateToString(date, "yyyy-MM-dd HH:mm");
				String otcFrozenStringto = DateUtil.dateToString(DateUtil.addMinToDate(appCustomer.getOtcFrozenDate(), 2880), "yyyy-MM-dd HH:mm");
				if (otcFrozenStringto.compareTo(startDateString) > 0) {
					jsonResult.setSuccess(false);
					jsonResult.setMsg("你已被冻结到：" + otcFrozenStringto);
					return jsonResult;
				}
				if(appCustomer.getOtcFrozenCout().compareTo(new BigDecimal(3) ) >= 0) {
					appCustomer.setOtcFrozenDate(null);
					appCustomer.setOtcFrozenCout(new BigDecimal(0));
					appCustomerService.updateNull(appCustomer);
				}
			}




			OtcTransaction otcTransaction = otcTransactionService.get(id);
			if (customerId.equals(otcTransaction.getCustomerId())){
				jsonResult.setSuccess(false);
				jsonResult.setMsg("不能买卖自己的订单！");
				return jsonResult;
			}

			if (otcTransaction.getStatus().intValue() == 3) {
				jsonResult.setSuccess(false);
				jsonResult.setMsg("交易已关闭");
				return jsonResult;
			}
			if (otcTransaction.getStatus().intValue() == 2) {
				jsonResult.setSuccess(false);
				jsonResult.setMsg("交易已完成");
				return jsonResult;
			}
			RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
			String OtcCoinList = redisService.get("cn:OtcCoinList");
			BigDecimal fee = new BigDecimal(0);
			BigDecimal min = new BigDecimal(1);
			BigDecimal max = new BigDecimal(100);
			boolean   CoinIsOpen = false ;
			if (!StringUtils.isEmpty(OtcCoinList)) {
				List<OtcCoin> parseArray = JSON.parseArray(OtcCoinList, OtcCoin.class);
				for (OtcCoin OtcCoin : parseArray) {
					if (otcTransaction.getCoinCode().equals(OtcCoin.getCoinCode())) {
						if(OtcCoin.getIsOpen() == 0){
							jsonResult.setSuccess(false);
							jsonResult.setMsg(OtcCoin.getCoinCode() + "未开启！");
							return jsonResult;
						}
						CoinIsOpen = true;
						min = OtcCoin.getMinCount();
						max = OtcCoin.getMaxCount();

						if(("Definite").equals(OtcCoin.getPoundage_type())){
							fee = OtcCoin.getPoundage();
						}else{
							fee =  OtcCoin.getPoundage().multiply(transactioncount);
						}
					}
				}

			}else {
				jsonResult.setSuccess(false);
				jsonResult.setMsg("OTC配置错误！");
				return jsonResult;
			}

			if(CoinIsOpen == false){
				jsonResult.setSuccess(false);
				jsonResult.setMsg(otcTransaction.getCoinCode() + "未开启！");
				return jsonResult;
			}


			if (otcTransaction.getSurplusQuantity().compareTo(transactioncount) < 0) {
				jsonResult.setSuccess(false);
				jsonResult.setMsg("请刷新重试，剩余数量为:" + otcTransaction.getSurplusQuantity().setScale(2, BigDecimal.ROUND_HALF_DOWN));
				return jsonResult;

			}
			//修改otc委托剩余数量
			otcTransaction.setSurplusQuantity(otcTransaction.getSurplusQuantity().subtract(transactioncount));
			otcTransaction.setBusinessQuantity(otcTransaction.getBusinessQuantity().add(transactioncount));
			otcTransaction.setStatus(4);
			if(otcTransaction.getBusinessQuantity().equals(otcTransaction.getTransactionCount())){
				otcTransaction.setStatus(5);
				otcTransaction.setBusinessFlag("N");
			}

			if(otcTransaction.getSurplusQuantity().compareTo(new BigDecimal(0)) < 0){
				jsonResult.setSuccess(false);
				jsonResult.setMsg("交易失败！");
				return jsonResult;
			}


			if (transactioncount.compareTo(min) < 0) {
				jsonResult.setSuccess(false);
				jsonResult.setMsg("最小下单数为:" + min.setScale(2, BigDecimal.ROUND_HALF_DOWN));
				return jsonResult;
			}
			if (transactioncount.compareTo(max) > 0) {
				jsonResult.setSuccess(false);
				jsonResult.setMsg("最大下单数为:" + max.setScale(2, BigDecimal.ROUND_HALF_DOWN));
				return jsonResult;
			}

			if (otcTransaction.getTransactionType() == 1) {//如果是买，则操作者是 otc卖方
				AppBankCard appBankCard = null;
				QueryFilter filter = new QueryFilter(AppBankCard.class);
				filter.addFilter("customerId=", customerId);
				filter.addFilter("isDelete=", 0);
				appBankCard = appBankCardService.get(filter);
				if (appBankCard == null) {
					jsonResult.setMsg("请到个人中心添加银行卡后再操作");
					jsonResult.setSuccess(false);
					return jsonResult;
				}

				otcOrderTransaction.setBuyCustomId(otcTransaction.getCustomerId());
				otcOrderTransaction.setSellCustomId(customerId);
				otcOrderTransaction.setCancelFlag("N");


				// 卖方  判断账户是否大于要卖的币
				RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
				UserRedis userRedis = redisUtil.get(customerId.toString());
				Long sellAccountId = userRedis.getDmAccountId(otcTransaction.getCoinCode());
				ExDigitalmoneyAccountRedis dmAccount = UserRedisUtils.getAccount(sellAccountId.toString(), otcTransaction.getCoinCode());
				if (dmAccount.getHotMoney().compareTo(transactioncount.add(fee)) < 0) {
					jsonResult.setMsg(otcTransaction.getCoinCode() + "不足!");
					return jsonResult;
				}
				//买方
				RemoteExDigitalmoneyAccountService remoteExDigService=(RemoteExDigitalmoneyAccountService) ContextUtil.getBean("remoteExDigitalmoneyAccountService");
				ExDigitalmoneyAccount buyexDigitalmoneyAccount = remoteExDigService.findByCustomerType(otcTransaction.getCustomerId(), otcTransaction.getCoinCode(), "USD", "cn");
				Long buyAccountId = buyexDigitalmoneyAccount.getId();

				otcTransaction.setFee(fee);
				otcOrderTransaction.setFee(fee);



				otcOrderTransaction =common(otcOrderTransaction,otcTransaction,transactioncount);
				otcOrderTransaction.setBuyAccountId(buyAccountId);
				otcOrderTransaction.setSellAaccountId(sellAccountId);
				otcOrderTransaction.setCreateBy(customerId);
				//保存事物
				super.save(otcOrderTransaction);

				//冻结币
				sellCoin(otcOrderTransaction,customerId,sellAccountId, transactioncount);

				jsonResult.setSuccess(true);

			} else if (otcTransaction.getTransactionType() == 2) {//如果是卖，则操作者是 otc买方

				QueryFilter filter = new QueryFilter(OtcOrderTransaction.class);
				filter.addFilter("buyCustomId=", customerId);
				filter.addFilter("status=", 1);
				List<OtcOrderTransaction> list = super.find(filter);
				if(list.size() > 1){
					jsonResult.setMsg("已经有两笔未付款订单，请付款！");
					jsonResult.setSuccess(false);
					return jsonResult;
				}

				otcOrderTransaction.setSellCustomId(otcTransaction.getCustomerId());
				otcOrderTransaction.setBuyCustomId(customerId);
				otcOrderTransaction =common(otcOrderTransaction,otcTransaction,transactioncount);

				otcOrderTransaction.setCancelFlag("N");

				// 买方
				RedisUtil<UserRedis> buyredisUtil = new RedisUtil<UserRedis>(UserRedis.class);
				UserRedis buyuserRedis = buyredisUtil.get(customerId.toString());
				Long buyAccountId = buyuserRedis.getDmAccountId(otcTransaction.getCoinCode());

				//卖方
				RemoteExDigitalmoneyAccountService remoteExDigService=(RemoteExDigitalmoneyAccountService) ContextUtil.getBean("remoteExDigitalmoneyAccountService");
				ExDigitalmoneyAccount sellexDigitalmoneyAccount = remoteExDigService.findByCustomerType(otcTransaction.getCustomerId(), otcTransaction.getCoinCode(), "USD", "cn");
				Long sellAccountId = sellexDigitalmoneyAccount.getId();

				otcOrderTransaction.setBuyAccountId(buyAccountId);
				otcOrderTransaction.setSellAaccountId(sellAccountId );
				//如果是固定费率
				if(("Definite").equals(otcTransaction.getPoundage_type())) {
					otcOrderTransaction.setFee(otcTransaction.getFee());
				}else{
					otcOrderTransaction.setFee(transactioncount.multiply(otcTransaction.getPoundage()));
				}
				otcOrderTransaction.setCreateBy(customerId);

				super.save(otcOrderTransaction);
				jsonResult.setSuccess(true);
			}
			if(jsonResult.getSuccess()){
				//如果订单数量小于最小数量 交易标识改为N 不能挂单
				if (otcTransaction.getSurplusQuantity().compareTo(min) < 0) {
					otcTransaction.setBusinessFlag("N");
					otcTransaction.setFrozenQuantity(otcTransaction.getSurplusQuantity());

				}
				otcTransactionService.update(otcTransaction);
				//如果是卖操作 发送短信通知买方付款
				if (otcTransaction.getTransactionType() == 1) {
					AppCustomer customer = appCustomerService.get(otcTransaction.getCustomerId());
					String  mobile = customer.getPhone();
					if(mobile != null && mobile != "") {
						SmsParam smsParam = new SmsParam();
						smsParam.setHryMobilephone(customer.getPhone());
						smsParam.setHryCode(otcTransaction.getCoinCode());
						Map<String, Object> map = new HashMap<>();
						map.put("coincode", otcTransaction.getCoinCode());
					    String 	address = mobile.substring(0, mobile.indexOf(" "));
						if (address.equals("+86")) {
							SmsSendUtil.sendSmsInfo(smsParam, null, mobile, "OTC_SMS_BUY_ZH", map);
						}else{
							SmsSendUtil.sendSmsInfo(smsParam, null, mobile, "OTC_SMS_BUY_EN", map);
						}
					}
				}
			}

		}


		return jsonResult;
	}

	private synchronized void sellCoin(OtcOrderTransaction otcOrderTransaction,Long customerId,Long dmAccountId ,BigDecimal transactioncount) {



		// 热账户减少
		Accountadd accountadd = new Accountadd();
		accountadd.setAccountId(dmAccountId);
		accountadd.setMoney((transactioncount.add(otcOrderTransaction.getFee())).multiply(new BigDecimal(-1)));
		accountadd.setMonteyType(1);
		accountadd.setAcccountType(1);
		accountadd.setRemarks(52);

		accountadd.setTransactionNum(otcOrderTransaction.getTransactionNum());

		//保存币账户操作记录
		otcAccountRecordService.saveRecord(customerId, accountadd.getAccountId(), accountadd.getMoney(),accountadd.getMonteyType(), accountadd.getAcccountType(), accountadd.getTransactionNum(), "OTC在市场挂单卖出");


		// 冷账户增加
		Accountadd accountadd2 = new Accountadd();
		accountadd2.setAccountId(dmAccountId);
		accountadd2.setMoney((transactioncount.add(otcOrderTransaction.getFee())));
		accountadd2.setMonteyType(2);
		accountadd2.setAcccountType(1);
		accountadd2.setRemarks(52);
		accountadd2.setTransactionNum(otcOrderTransaction.getTransactionNum());

		//保存币账户操作记录
		otcAccountRecordService.saveRecord(customerId, accountadd2.getAccountId(), accountadd2.getMoney(),accountadd2.getMonteyType(), accountadd2.getAcccountType(), accountadd2.getTransactionNum(), "OTC在市场挂单卖出");


		List<Accountadd> list = new ArrayList<Accountadd>();
		list.add(accountadd);
		list.add(accountadd2);
		messageProducer.toAccount(JSON.toJSONString(list));
	}





	private OtcOrderTransaction common(OtcOrderTransaction otcOrderTransaction, OtcTransaction otcTransaction,BigDecimal transactionCount){

		otcOrderTransaction.setCustomerBankId(otcTransaction.getCustomerBankId());

		otcOrderTransaction.setTransactionId(otcTransaction.getId());
	// 币种
		otcOrderTransaction.setCoinCode(otcTransaction.getCoinCode());
	// 订单号
		otcOrderTransaction.setTransactionNum(otcTransaction.getTransactionNum());
	// 交易类型
		otcOrderTransaction.setTransactionType(otcTransaction.getTransactionType());
	// 交易数量
		otcOrderTransaction.setTransactionCount(transactionCount);
	// 交易价格
		otcOrderTransaction.setTransactionPrice(otcTransaction.getTransactionPrice());
	// 交易金额
		otcOrderTransaction.setTransactionMoney(transactionCount.multiply(otcTransaction.getTransactionPrice()).setScale(2, BigDecimal.ROUND_DOWN));
	// 待审核
		otcOrderTransaction.setStatus(Integer.valueOf(1));
	// 未支付
		otcOrderTransaction.setStatus2(Integer.valueOf(1));
	// 用户名
		otcOrderTransaction.setUserName(otcTransaction.getUserName());
		//交易订单号
		otcOrderTransaction.setTransactionOrderNum(UUID.randomUUID().toString().replace("-", ""));
		//随机码
		//生成随机码
		String code = RandomStringUtils.random(6, false, true);
		otcOrderTransaction.setRandomNum(code);

		return  otcOrderTransaction;
	}
	/**
	 * 完成付款
	 * @param otcOrderTransactionMange
	 * @return
	 */
	@Override
	public synchronized JsonResult otcPayment(OtcOrderTransactionMange otcOrderTransactionMange) {

		JsonResult jsonResult = new JsonResult();

		synchronized (lock) {
			OtcOrderTransaction OrderTransaction = super.get(otcOrderTransactionMange.getId());
			if(OrderTransaction.getStatus() != 1){
				jsonResult.setSuccess(false);
				jsonResult.setMsg("状态已变更，请刷新！");
				return jsonResult;
			}
			OrderTransaction.setStatus(2);
			OrderTransaction.setImg1(otcOrderTransactionMange.getImg1());
			OrderTransaction.setImg2(otcOrderTransactionMange.getImg2());
			OrderTransaction.setImg3(otcOrderTransactionMange.getImg3());
			OrderTransaction.setPaymentType(otcOrderTransactionMange.getPaymentType());
			OrderTransaction.setPaymentTime(otcOrderTransactionMange.getPaymentTime());
			super.update(OrderTransaction);
			jsonResult.setSuccess(true);

			AppCustomer customer = appCustomerService.get(OrderTransaction.getSellCustomId());
			     String  mobile = customer.getPhone();
				if(mobile != null && mobile != "") {
					SmsParam smsParam = new SmsParam();
					smsParam.setHryMobilephone(customer.getPhone());
					smsParam.setHryCode(OrderTransaction.getCoinCode());
					Map<String, Object> map = new HashMap<>();
					map.put("coincode", OrderTransaction.getCoinCode());
					String 	address = mobile.substring(0, mobile.indexOf(" "));
					if (address.equals("+86")) {
						SmsSendUtil.sendSmsInfo(smsParam, null, mobile, "OTC_SMS_SELL_ZH", map);
					}else{
						SmsSendUtil.sendSmsInfo(smsParam, null, mobile, "OTC_SMS_SELL_EN", map);
					}
				}

			return jsonResult;
		}
	}

	private OtcOrderTransactionMange  common(OtcOrderTransaction otcOrderTransaction){
		OtcOrderTransactionMange otcOrderTransactionMange = new OtcOrderTransactionMange();
		otcOrderTransactionMange.setId(otcOrderTransaction.getId());
		otcOrderTransactionMange.setBuyCustomId(otcOrderTransaction.getBuyCustomId());
		otcOrderTransactionMange.setSellCustomId(otcOrderTransaction.getSellCustomId());
		otcOrderTransactionMange.setBuyAccountId(otcOrderTransaction.getBuyAccountId());
		otcOrderTransactionMange.setSellAaccountId(otcOrderTransaction.getSellAaccountId());
		otcOrderTransactionMange.setImg1(otcOrderTransaction.getImg1());
		otcOrderTransactionMange.setImg2(otcOrderTransaction.getImg2());
		otcOrderTransactionMange.setImg3(otcOrderTransaction.getImg3());
		otcOrderTransactionMange.setImg4(otcOrderTransaction.getImg4());
		otcOrderTransactionMange.setImg5(otcOrderTransaction.getImg5());
		otcOrderTransactionMange.setImg6(otcOrderTransaction.getImg6());
		otcOrderTransactionMange.setAppealReason(otcOrderTransaction.getAppealReason());
		otcOrderTransactionMange.setPaymentType(otcOrderTransaction.getPaymentType());
		otcOrderTransactionMange.setAppealFlag(otcOrderTransaction.getAppealFlag());
		otcOrderTransactionMange.setPaymentTime(otcOrderTransaction.getPaymentTime());
		otcOrderTransactionMange.setCoinCode(otcOrderTransaction.getCoinCode());
		otcOrderTransactionMange.setFee(otcOrderTransaction.getFee());
		otcOrderTransactionMange.setStatus(otcOrderTransaction.getStatus());
		otcOrderTransactionMange.setRandomNum(otcOrderTransaction.getRandomNum());
		otcOrderTransactionMange.setTransactionType(otcOrderTransaction.getTransactionType());
		otcOrderTransactionMange.setTransactionMoney(otcOrderTransaction.getTransactionMoney());
		otcOrderTransactionMange.setTransactionCount(otcOrderTransaction.getTransactionCount());
		otcOrderTransactionMange.setTransactionPrice(otcOrderTransaction.getTransactionPrice());
		otcOrderTransactionMange.setAppealCustomId(otcOrderTransaction.getAppealCustomId());
		otcOrderTransactionMange.setAppealCustomName(otcOrderTransaction.getAppealCustomName());
		otcOrderTransactionMange.setAppealHandle(otcOrderTransaction.getAppealHandle());
		otcOrderTransactionMange.setTransactionNum(otcOrderTransaction.getTransactionNum());
		otcOrderTransactionMange.setCreated(otcOrderTransaction.getCreated());
		otcOrderTransactionMange.setModified(otcOrderTransaction.getModified());
		otcOrderTransactionMange.setSaasId(otcOrderTransaction.getSaasId());
		otcOrderTransactionMange.setStatus2(otcOrderTransaction.getStatus2());
		otcOrderTransactionMange.setUserName(otcOrderTransaction.getUserName());
		otcOrderTransactionMange.setTransactionOrderNum(otcOrderTransaction.getTransactionOrderNum());
        otcOrderTransactionMange.setCreateBy(otcOrderTransaction.getCreateBy());
		return  otcOrderTransactionMange;
	}

	/**
	 * 获交易订单列表
	 * @param
	 * @return
	 */
	public FrontPage otcorderlistall(Map<String, String> params){
		Page page = PageFactory.getPage(params);
		List<OtcOrderTransaction> list2 = ((OtcOrderTransactionDao) dao).otcorderlistall(params);
		ArrayList<OtcOrderTransactionMange> resultList = new ArrayList<OtcOrderTransactionMange>();
		for(int i =0;i < list2.size() ;i++){
			if (list2.get(i).getBuyCustomId().equals(Integer.valueOf( params.get("customId")).longValue()) ){
				if (list2.get(i).getSellCustomId().equals(Integer.valueOf( params.get("customId")).longValue()) ){
					list2.get(i).setTransactionType(3);
				}else {
					list2.get(i).setTransactionType(1);
				}
			}else if (list2.get(i).getSellCustomId().equals(Integer.valueOf( params.get("customId")).longValue()) ){
				if (list2.get(i).getBuyCustomId().equals(Integer.valueOf( params.get("customId")).longValue())  ){
					list2.get(i).setTransactionType(3);
				}else {
					list2.get(i).setTransactionType(2);
				}
			} else{
				list2.get(i).setTransactionType(2);
			}
			resultList.add(common(list2.get(i)));
		}

		return new FrontPage(resultList, page.getTotal(), page.getPages(), page.getPageSize());
	}


	/**
	 * 获卖交易订单列表
	 * @param
	 * @return
	 */
	public FrontPage otcorderselllist(Map<String, String> params){
		Page page = PageFactory.getPage(params);
		List<OtcOrderTransaction> list2 = ((OtcOrderTransactionDao) dao).otcorderselllist(params);
		ArrayList<OtcOrderTransactionMange> resultList = new ArrayList<OtcOrderTransactionMange>();
		for(int i =0;i < list2.size() ;i++){
				list2.get(i).setTransactionType(2);
			resultList.add(common(list2.get(i)));
		}
		return new FrontPage(resultList, page.getTotal(), page.getPages(), page.getPageSize());
	}


	/**
	 * 获买交易订单列表
	 * @param
	 * @return
	 */
	public FrontPage otcorderbuylist(Map<String, String> params){
		Page page = PageFactory.getPage(params);
		List<OtcOrderTransaction> list2 = ((OtcOrderTransactionDao) dao).otcorderbuylist(params);
		ArrayList<OtcOrderTransactionMange> resultList = new ArrayList<OtcOrderTransactionMange>();
		for(int i =0;i < list2.size() ;i++){
			list2.get(i).setTransactionType(1);
			resultList.add(common(list2.get(i)));
		}
		return new FrontPage(resultList, page.getTotal(), page.getPages(), page.getPageSize());
	}



	/**
	 * otc申诉
	 * @param otcOrderTransactionMange
	 * @return
	 */
	@Override
	public synchronized JsonResult confirmotcApplyArbitration(OtcOrderTransactionMange otcOrderTransactionMange) {

		JsonResult jsonResult = new JsonResult();

		synchronized (lock) {
			OtcOrderTransaction OrderTransaction = super.get(otcOrderTransactionMange.getId());
			if(OrderTransaction.getStatus() != 2 && OrderTransaction.getStatus() != 1 && OrderTransaction.getStatus() != 3){
				jsonResult.setSuccess(false);
				jsonResult.setMsg("状态已变更，请刷新！");
				return jsonResult;
			}
			//记录申诉前状态，更改当前状态
			OrderTransaction.setStatus2(OrderTransaction.getStatus());
			OrderTransaction.setStatus(6);
			OrderTransaction.setImg4(otcOrderTransactionMange.getImg4());
			OrderTransaction.setImg5(otcOrderTransactionMange.getImg5());
			OrderTransaction.setImg6(otcOrderTransactionMange.getImg6());
			OrderTransaction.setAppealCustomId(otcOrderTransactionMange.getAppealCustomId());
			OrderTransaction.setAppealCustomName(otcOrderTransactionMange.getAppealCustomName());
			OrderTransaction.setAppealFlag("Y");
			OrderTransaction.setAppealReason(otcOrderTransactionMange.getAppealReason());
			super.update(OrderTransaction);
			jsonResult.setSuccess(true);
			return jsonResult;
		}
	}
    /*
    /  获取手续费
     */
	private BigDecimal getFee(String CoinCode,BigDecimal transactioncount ){
		BigDecimal fee = new BigDecimal(0);
		RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
		String OtcCoinList = redisService.get("cn:OtcCoinList");
		if (!StringUtils.isEmpty(OtcCoinList)) {
			List<OtcCoin> parseArray = JSON.parseArray(OtcCoinList, OtcCoin.class);
			for (OtcCoin OtcCoin : parseArray) {
				if (CoinCode.equals(OtcCoin.getCoinCode())) {
					if(("Definite").equals(OtcCoin.getPoundage_type())){
						fee = OtcCoin.getPoundage();
					}else{
						fee =  OtcCoin.getPoundage().multiply(transactioncount);
					}
				}
			}

		}
		return  fee;
	}

	/**
	 * OTC交易完成  买方账号热账户增加，卖方冷账户减少
	 * @param
	 * @return
	 */
	private synchronized void finishsellCoin(OtcOrderTransaction otcOrderTransaction,BigDecimal transactioncount,BigDecimal fee) {


		// 买方热账户增加
		Accountadd accountadd = new Accountadd();
		accountadd.setAccountId(otcOrderTransaction.getBuyAccountId());
		accountadd.setMoney(transactioncount);
		accountadd.setMonteyType(1);
		accountadd.setAcccountType(1);
		accountadd.setRemarks(52);

		accountadd.setTransactionNum(otcOrderTransaction.getTransactionNum());

		//保存币账户操作记录
		//otcAccountRecordService.saveRecord(otcOrderTransaction.getBuyCustomId(), accountadd.getAccountId(), accountadd.getMoney(),accountadd.getMonteyType(), accountadd.getAcccountType(), accountadd.getTransactionNum(), "OTC_finish");


		// 卖方冷账户减少
		Accountadd accountadd2 = new Accountadd();
		accountadd2.setAccountId(otcOrderTransaction.getSellAaccountId());
		accountadd2.setMoney((transactioncount.add(fee)).multiply(new BigDecimal(-1)));
		accountadd2.setMonteyType(2);
		accountadd2.setAcccountType(1);
		accountadd2.setRemarks(52);
		accountadd2.setTransactionNum(otcOrderTransaction.getTransactionNum());

		//保存币账户操作记录
		//otcAccountRecordService.saveRecord(otcOrderTransaction.getSellCustomId(), accountadd2.getAccountId(), accountadd2.getMoney(),accountadd2.getMonteyType(), accountadd2.getAcccountType(), accountadd2.getTransactionNum(), "OTC_finish");
		otcAccountRecordService.saveRecord(otcOrderTransaction.getSellCustomId(), accountadd2.getAccountId(), accountadd2.getMoney(),accountadd2.getMonteyType(), accountadd2.getAcccountType(), accountadd2.getTransactionNum(), "OTC_finish");
		otcAccountRecordService.saveRecord(otcOrderTransaction.getBuyCustomId(), accountadd2.getAccountId(), accountadd.getMoney(),accountadd.getMonteyType(), accountadd.getAcccountType(), accountadd.getTransactionNum(), "OTC_finish");

		List<Accountadd> list = new ArrayList<Accountadd>();
		list.add(accountadd);
		list.add(accountadd2);
		messageProducer.toAccount(JSON.toJSONString(list));
	}

    /**
     * OTC取消  卖方热账户增加 卖方冷账户减少
     * @param
     * @return
     */
    private synchronized void UndoCoin(OtcOrderTransaction otcOrderTransaction,BigDecimal transactioncount) {


        // 卖方热账户增加
        Accountadd accountadd = new Accountadd();
        accountadd.setAccountId(otcOrderTransaction.getSellAaccountId());
        accountadd.setMoney(transactioncount.add(otcOrderTransaction.getFee()));
        accountadd.setMonteyType(1);
        accountadd.setAcccountType(1);
        accountadd.setRemarks(52);

        accountadd.setTransactionNum(otcOrderTransaction.getTransactionNum());

		//保存币账户操作记录
		otcAccountRecordService.saveRecord(otcOrderTransaction.getSellCustomId(), accountadd.getAccountId(), accountadd.getMoney(),accountadd.getMonteyType(), accountadd.getAcccountType(), accountadd.getTransactionNum(), "OTC交易订单取消");


		// 卖方冷账户减少
        Accountadd accountadd2 = new Accountadd();
        accountadd2.setAccountId(otcOrderTransaction.getSellAaccountId());
        accountadd2.setMoney((transactioncount.add(otcOrderTransaction.getFee())).multiply(new BigDecimal(-1)));
        accountadd2.setMonteyType(2);
        accountadd2.setAcccountType(1);
        accountadd2.setRemarks(52);
        accountadd2.setTransactionNum(otcOrderTransaction.getTransactionNum());

		//保存币账户操作记录
		otcAccountRecordService.saveRecord(otcOrderTransaction.getSellCustomId(), accountadd2.getAccountId(), accountadd2.getMoney(),accountadd2.getMonteyType(), accountadd2.getAcccountType(), accountadd2.getTransactionNum(), "OTC交易订单取消");


		List<Accountadd> list = new ArrayList<Accountadd>();
        list.add(accountadd);
        list.add(accountadd2);
        messageProducer.toAccount(JSON.toJSONString(list));
    }

    /**
    * @Description:    创建交易记录
    * @Author:         zongwei
    * @CreateDate:     2018/6/14 14:03
    * @UpdateUser:    zongwei
    * @UpdateDate:     2018/6/14 14:03
    * @UpdateRemark:   创建
    * @Version:        1.0
    */
	private synchronized void create_exDmTransaction(OtcOrderTransaction otcOrderTransaction,BigDecimal fee) {


		// 买方
		ExDigitalmoneyAccount buyaccount = exDigitalmoneyAccountService.get(otcOrderTransaction.getBuyAccountId());
		ExDmTransaction exDmTransaction = new ExDmTransaction();
		exDmTransaction.setAccountId(buyaccount.getId());
		exDmTransaction.setCoinCode(buyaccount.getCoinCode());
		exDmTransaction.setCreated(new Date());
		exDmTransaction.setCurrencyType(buyaccount.getCurrencyType());
		exDmTransaction.setWebsite(buyaccount.getWebsite());
		exDmTransaction.setCustomerId(buyaccount.getCustomerId());
		exDmTransaction.setCustomerName(buyaccount.getUserName());
		exDmTransaction.setSurname(buyaccount.getSurname());
		exDmTransaction.setTrueName(buyaccount.getTrueName());
		exDmTransaction.setFee(new BigDecimal(0));
		exDmTransaction.setTransactionMoney(otcOrderTransaction.getTransactionCount());
		exDmTransaction.setStatus(2);
		exDmTransaction.setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
		// 充币
		exDmTransaction.setTransactionType(1);
		exDmTransaction.setUserId(ContextUtil.getCurrentUserId());
		exDmTransaction.setRemark("OTC购买");
		// 保存关联关系
		exDmTransactionService.save(exDmTransaction);

		//保存币账户操作记录



		// 卖方
		ExDigitalmoneyAccount sellaccount = exDigitalmoneyAccountService.get(otcOrderTransaction.getSellAaccountId());
		ExDmTransaction exDmTransaction2 = new ExDmTransaction();
		exDmTransaction2.setAccountId(sellaccount.getId());
		exDmTransaction2.setCoinCode(sellaccount.getCoinCode());
		exDmTransaction2.setCreated(new Date());
		exDmTransaction2.setCurrencyType(sellaccount.getCurrencyType());
		exDmTransaction2.setWebsite(sellaccount.getWebsite());
		exDmTransaction2.setCustomerId(sellaccount.getCustomerId());
		exDmTransaction2.setCustomerName(sellaccount.getUserName());
		exDmTransaction2.setSurname(sellaccount.getSurname());
		exDmTransaction2.setTrueName(sellaccount.getTrueName());
		exDmTransaction2.setFee(fee);
		exDmTransaction2.setTransactionMoney(otcOrderTransaction.getTransactionCount());
		exDmTransaction2.setStatus(2);
		exDmTransaction2.setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
		// 提币
		exDmTransaction2.setTransactionType(2);
		exDmTransaction2.setUserId(ContextUtil.getCurrentUserId());
		exDmTransaction2.setRemark("OTC卖币");
		exDmTransactionService.save(exDmTransaction2);
	}
    /**
    * @Description:    记录用户完成otc单
    * @Author:         zongwei
    * @CreateDate:     2018/6/23 14:58
    * @UpdateUser:    zongwei
    * @UpdateDate:     2018/6/23 14:58
    * @UpdateRemark:   创建
    * @Version:        1.0
    */
	private synchronized void update_user_otc_count(OtcOrderTransaction otcOrderTransaction) {
		  //卖方
           AppPersonInfo sellappPersonInfo = appPersonInfoService.getByCustomerId(otcOrderTransaction.getSellCustomId());
           if(sellappPersonInfo.getOtcFinishCount() == null ){
			   sellappPersonInfo.setOtcFinishCount(new BigDecimal(1));
		   }else{
			   sellappPersonInfo.setOtcFinishCount(sellappPersonInfo.getOtcFinishCount().add(new BigDecimal(1)));
		   }
		   appPersonInfoService.update(sellappPersonInfo);
		//买方
		   AppPersonInfo buyappPersonInfo = appPersonInfoService.getByCustomerId(otcOrderTransaction.getBuyCustomId());
		    if(buyappPersonInfo.getOtcFinishCount() == null ){
			   buyappPersonInfo.setOtcFinishCount(new BigDecimal(1));
		     }else{
			   buyappPersonInfo.setOtcFinishCount(buyappPersonInfo.getOtcFinishCount().add(new BigDecimal(1)));
		    }
		    appPersonInfoService.update(buyappPersonInfo);
	}


	/**
	 * otc确认到账
	 * @param otcOrderTransactionMange
	 * @return
	 */
	@Override
	public synchronized JsonResult finishOtcOrder(OtcOrderTransactionMange otcOrderTransactionMange) {

		JsonResult jsonResult = new JsonResult();

		synchronized (lock) {
			OtcOrderTransaction OrderTransaction = super.get(otcOrderTransactionMange.getId());
			if(OrderTransaction.getStatus() != 2 && OrderTransaction.getStatus() != 6){
				jsonResult.setSuccess(false);
				jsonResult.setMsg("状态已变更，请刷新！");
				return jsonResult;
			}

			//更新完成状态

			OrderTransaction.setStatus(4);
            OrderTransaction.setAppealHandle("Finish");
			OrderTransaction.setFinishTime(new Date());
			super.update(OrderTransaction);
			OtcTransaction otcTransaction = otcTransactionService.get(OrderTransaction.getTransactionId());
			BigDecimal fee = new BigDecimal(0);
            if(("Definite").equals(otcTransaction.getPoundage_type())) {
			    if(otcTransaction.getDealQuantity().compareTo(fee) > 0 ){
					fee = new BigDecimal(0);
				}else{
					fee =   otcTransaction.getFee();
				}
			}else{
				fee =   OrderTransaction.getFee();
			}
			otcTransaction.setDealQuantity(otcTransaction.getDealQuantity().add(OrderTransaction.getTransactionCount()));
			otcTransaction.setStatus(6);
			if (otcTransaction.getTransactionCount().equals(otcTransaction.getDealQuantity().add(otcTransaction.getFrozenQuantity())) ){
				otcTransaction.setStatus(2);
				if(otcTransaction.getTransactionType() ==2){
					//退还多余的币种
					this.operationCoin(otcTransaction,OrderTransaction);
					otcTransaction.setReturnQuantity(otcTransaction.getFrozenQuantity());
					if(("Definite").equals(otcTransaction.getPoundage_type())) {
						otcTransaction.setReturnQuantity(otcTransaction.getFrozenQuantity());
						otcTransaction.setFrozenQuantity(new BigDecimal(0));
					}else{
						otcTransaction.setReturnQuantity(otcTransaction.getFrozenQuantity().multiply(otcTransaction.getPoundage().add(new BigDecimal(1))));
						otcTransaction.setFrozenQuantity(new BigDecimal(0));
					}
				}
			}
			otcTransactionService.update(otcTransaction);
			//统计otc完成笔数
			this.update_user_otc_count(OrderTransaction);
			//币账号操作
			this.create_exDmTransaction(OrderTransaction, fee);
			this.finishsellCoin(OrderTransaction,OrderTransaction.getTransactionCount(),fee);
			jsonResult.setSuccess(true);
            //通知买家交易完成
			AppCustomer customer = appCustomerService.get(OrderTransaction.getBuyCustomId());
			String  mobile = customer.getPhone();
			if(mobile != null && mobile != "") {
				SmsParam smsParam = new SmsParam();
				smsParam.setHryMobilephone(customer.getPhone());
				smsParam.setHryCode(otcTransaction.getCoinCode());
				Map<String, Object> map = new HashMap<>();
				map.put("coincode", otcTransaction.getCoinCode());
				String 	address = mobile.substring(0, mobile.indexOf(" "));
				if (address.equals("+86")) {
					SmsSendUtil.sendSmsInfo(smsParam, null, mobile, "OTC_FINISH_SMS_ZH", map);
				}else{
					SmsSendUtil.sendSmsInfo(smsParam, null, mobile, "OTC_FINISH_SMS_EN", map);
				}
			}
			return jsonResult;
		}
	}
    /*
    * 剩余数量返还
    *
    * *
     */
	private synchronized void operationCoin(OtcTransaction OtcTransaction,OtcOrderTransaction OrderTransaction) {

		// 热账户增加
		Accountadd accountadd = new Accountadd();
		accountadd.setAccountId(OtcTransaction.getAccountId());
		if(("Definite").equals(OtcTransaction.getPoundage_type())) {
			accountadd.setMoney(OtcTransaction.getFrozenQuantity());
		}else{
			if(OtcTransaction.getTransactionType() == 1){
				accountadd.setMoney(OtcTransaction.getFrozenQuantity().add(OrderTransaction.getFee()));
			}else {
				accountadd.setMoney(OtcTransaction.getFrozenQuantity().multiply(OtcTransaction.getPoundage().add(new BigDecimal(1))));
			}

		}
		accountadd.setMonteyType(1);
		accountadd.setAcccountType(1);
		accountadd.setRemarks(52);

		accountadd.setTransactionNum(OtcTransaction.getTransactionNum());

		//保存币账户操作记录
		otcAccountRecordService.saveRecord(OtcTransaction.getCustomerId(), accountadd.getAccountId(), accountadd.getMoney(),accountadd.getMonteyType(), accountadd.getAcccountType(), accountadd.getTransactionNum(), "剩余数量返还");


		// 冷账户减少
		Accountadd accountadd2 = new Accountadd();
		accountadd2.setAccountId(OtcTransaction.getAccountId());
		if(("Definite").equals(OtcTransaction.getPoundage_type())) {
			accountadd2.setMoney(OtcTransaction.getFrozenQuantity().multiply(new BigDecimal(-1)));
		}else{
			if(OtcTransaction.getTransactionType() == 1){
				accountadd2.setMoney(OtcTransaction.getFrozenQuantity().add(OrderTransaction.getFee()).multiply(new BigDecimal(-1)));
			}else {
				accountadd2.setMoney(OtcTransaction.getFrozenQuantity().multiply(OtcTransaction.getPoundage().add(new BigDecimal(1))).multiply(new BigDecimal(-1)));
			}

		}
		accountadd2.setMonteyType(2);
		accountadd2.setAcccountType(1);
		accountadd2.setRemarks(52);
		accountadd2.setTransactionNum(OtcTransaction.getTransactionNum());

		//保存币账户操作记录
		otcAccountRecordService.saveRecord(OtcTransaction.getCustomerId(), accountadd2.getAccountId(), accountadd2.getMoney(),accountadd2.getMonteyType(), accountadd2.getAcccountType(), accountadd2.getTransactionNum(), "剩余数量返还");


		List<Accountadd> list = new ArrayList<Accountadd>();
		list.add(accountadd);
		list.add(accountadd2);
		messageProducer.toAccount(JSON.toJSONString(list));
	}

    /**
     * otc撤销
     * @param otcOrderTransactionMange
     * @return
     */
    @Override
    public synchronized JsonResult otcUndo(OtcOrderTransactionMange otcOrderTransactionMange) {

        JsonResult jsonResult = new JsonResult();

        synchronized (lock) {
            OtcOrderTransaction OrderTransaction = super.get(otcOrderTransactionMange.getId());
            if(OrderTransaction.getStatus() != 1){
                jsonResult.setSuccess(false);
                jsonResult.setMsg("状态已变更，请刷新！");
                return jsonResult;
            }

			AppCustomer appCustomer = appCustomerService.get(otcOrderTransactionMange.getCancelBy());
			Date date = new Date();
			String toDateString = DateUtil.dateToString(date, "yyyy-MM-dd");
			String otcCancelDateString =  DateUtil.dateToString(appCustomer.getOtcCancelDate(), "yyyy-MM-dd");
            if(appCustomer.getOtcFrozenCout() == null) {
				appCustomer.setOtcFrozenCout(new BigDecimal(1));
			}else {
            	if(toDateString.equals(otcCancelDateString)) {
					appCustomer.setOtcFrozenCout(appCustomer.getOtcFrozenCout().add(new BigDecimal(1)));
				}else {
					appCustomer.setOtcFrozenCout(new BigDecimal(1));
				}
			}
			appCustomer.setOtcCancelDate(new Date());
			if(appCustomer.getOtcFrozenCout().compareTo(new BigDecimal(3)) >= 0 ){
				appCustomer.setOtcFrozenDate(new Date());
			}
			appCustomerService.update(appCustomer);

            //更新撤销状态
            OrderTransaction.setStatus(3);
            OrderTransaction.setCancelBy(otcOrderTransactionMange.getCancelBy());
            OrderTransaction.setCancelFlag("Y");
            OrderTransaction.setCancelDate(new Date());
            super.update(OrderTransaction);

            //回写委托订单
			OtcTransaction otcTransaction = otcTransactionService.get(OrderTransaction.getTransactionId());
			otcTransaction.setBusinessQuantity(otcTransaction.getBusinessQuantity().subtract(OrderTransaction.getTransactionCount()));
			otcTransaction.setSurplusQuantity(otcTransaction.getSurplusQuantity().add(OrderTransaction.getTransactionCount()));
			otcTransaction.setStatus(1);
			otcTransaction.setBusinessFlag("Y");
			otcTransaction.setFrozenQuantity(new BigDecimal(0));
			//如果成交量大于0 状态改为部分成交
			if (otcTransaction.getDealQuantity().compareTo(new BigDecimal(0)) > 0 ){
				otcTransaction.setStatus(6);
			}else{
				if (otcTransaction.getBusinessQuantity().compareTo(new BigDecimal(0)) > 0 ) {
					otcTransaction.setStatus(4);
				}
			}
			otcTransactionService.update(otcTransaction);
			//如果委托订单是买，则冻结在交易订单
			if(otcTransaction.getTransactionType() == 1){
				this.UndoCoin(OrderTransaction,OrderTransaction.getTransactionCount());
			}
            jsonResult.setSuccess(true);
            return jsonResult;
        }
    }

	/**
	 * otc关闭
	 * @param otcOrderTransactionMange
	 * @return
	 */
	public synchronized JsonResult otcColse(OtcOrderTransactionMange otcOrderTransactionMange){
		JsonResult jsonResult = new JsonResult();

		synchronized (lock) {
			OtcOrderTransaction OrderTransaction = super.get(otcOrderTransactionMange.getId());
			if(OrderTransaction.getStatus() != 6){
				jsonResult.setSuccess(false);
				jsonResult.setMsg("状态已变更，请刷新！");
				return jsonResult;
			}

			//更新完成状态
			OrderTransaction.setStatus(5);
            OrderTransaction.setAppealHandle("Colse");

			super.update(OrderTransaction);
			//回写委托订单
			OtcTransaction otcTransaction = otcTransactionService.get(OrderTransaction.getTransactionId());
			otcTransaction.setBusinessQuantity(otcTransaction.getBusinessQuantity().subtract(OrderTransaction.getTransactionCount()));
			otcTransaction.setSurplusQuantity(otcTransaction.getSurplusQuantity().add(OrderTransaction.getTransactionCount()));
			otcTransaction.setStatus(1);
			otcTransaction.setBusinessFlag("Y");
			otcTransaction.setFrozenQuantity(new BigDecimal(0));
			//如果成交量大于0 状态改为部分成交
			if (otcTransaction.getDealQuantity().compareTo(new BigDecimal(0)) > 0 ){
				otcTransaction.setStatus(6);
			}else{
				if (otcTransaction.getBusinessQuantity().compareTo(new BigDecimal(0)) > 0 ) {
					otcTransaction.setStatus(4);
				}
			}
			otcTransactionService.update(otcTransaction);
			jsonResult.setSuccess(true);

            //币账号操作
            if(otcTransaction.getTransactionType() == 1) {
                this.UndoCoin(OrderTransaction, OrderTransaction.getTransactionCount());
            }
			return jsonResult;
		}
	}


	/**
	 * otc自动关闭（30分钟没付款自动关闭）
	 * @param otcOrderTransactionMange
	 * @return
	 */
	public synchronized JsonResult autoOtcColse(OtcOrderTransactionMange otcOrderTransactionMange){
		JsonResult jsonResult = new JsonResult();

		synchronized (lock) {
			OtcOrderTransaction OrderTransaction = super.get(otcOrderTransactionMange.getId());


			//更新完成状态
			OrderTransaction.setStatus(5);

			super.update(OrderTransaction);
			//回写委托订单
			OtcTransaction otcTransaction = otcTransactionService.get(OrderTransaction.getTransactionId());
			otcTransaction.setBusinessQuantity(otcTransaction.getBusinessQuantity().subtract(OrderTransaction.getTransactionCount()));
			otcTransaction.setSurplusQuantity(otcTransaction.getSurplusQuantity().add(OrderTransaction.getTransactionCount()));
			otcTransaction.setStatus(1);
			otcTransaction.setBusinessFlag("Y");
			otcTransaction.setFrozenQuantity(new BigDecimal(0));
			//如果成交量大于0 状态改为部分成交
			if (otcTransaction.getDealQuantity().compareTo(new BigDecimal(0)) > 0 ){
				otcTransaction.setStatus(6);
			}else{
				if (otcTransaction.getBusinessQuantity().compareTo(new BigDecimal(0)) > 0 ) {
					otcTransaction.setStatus(4);
				}
			}
			otcTransactionService.update(otcTransaction);
			jsonResult.setSuccess(true);
            //币账号操作
            if(otcTransaction.getTransactionType() == 1) {
                this.UndoCoin(OrderTransaction, OrderTransaction.getTransactionCount());
            }
			return jsonResult;
		}
	}


	/**
	 * otc拒绝
	 * @param otcOrderTransactionMange
	 * @return
	 */
	public synchronized JsonResult otcrefuse(OtcOrderTransactionMange otcOrderTransactionMange){
		JsonResult jsonResult = new JsonResult();

		synchronized (lock) {
			OtcOrderTransaction OrderTransaction = super.get(otcOrderTransactionMange.getId());
			if(OrderTransaction.getStatus() != 6){
				jsonResult.setSuccess(false);
				jsonResult.setMsg("状态已变更，请刷新！");
				return jsonResult;
			}
			//更新状态
            OrderTransaction.setAppealHandle("Refuse");
			OrderTransaction.setStatus(OrderTransaction.getStatus2());
			super.update(OrderTransaction);
			jsonResult.setSuccess(true);
			return jsonResult;
		}
	}

	@Override
	public void timeout() {
		log.info("定时设置Otc超时");
		BigDecimal fee = new BigDecimal(0);
		RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
		String OtcCoinList = redisService.get("cn:OtcCoinList");
		if (!StringUtils.isEmpty(OtcCoinList)) {
			List<OtcCoin> parseArray = JSON.parseArray(OtcCoinList, OtcCoin.class);
			for (OtcCoin OtcCoin : parseArray) {
				int time = 30;
				if (!StringUtils.isEmpty(OtcCoin.getMaxTradeTime())) {
					time = OtcCoin.getMaxTradeTime().intValue();
				}

				Date date = new Date();
				Date startDate = DateUtil.addMinToDate(date, -time);
				String startDateString = DateUtil.dateToString(startDate, "yyyy-MM-dd HH:mm");

				QueryFilter filter = new QueryFilter(OtcOrderTransaction.class);
				filter.addFilter("created<", startDateString);
				filter.addFilter("status=", 1);
				filter.addFilter("coinCode=", OtcCoin.getCoinCode());
				List<OtcOrderTransaction> list = super.find(filter);
				log.info("Otc超时" + list.size() + "条,超时界线" + startDateString);
				if (list != null && list.size() > 0) {
					for (OtcOrderTransaction otcOrderTransaction : list) {
						OtcOrderTransactionMange otcOrderTransactionMange = new OtcOrderTransactionMange();
						otcOrderTransactionMange.setId(otcOrderTransaction.getId());
						autoOtcColse(otcOrderTransactionMange);
					}
				}

			}
		}
	}



}
