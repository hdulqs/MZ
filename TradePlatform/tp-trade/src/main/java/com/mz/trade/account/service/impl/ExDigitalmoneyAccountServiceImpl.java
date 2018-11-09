/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午6:12:49
 */
package com.mz.trade.account.service.impl;

import com.alibaba.fastjson.JSON;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.account.model.ExDmColdAccountRecord;
import com.mz.exchange.account.model.ExDmHotAccountRecord;
import com.mz.trade.account.service.AppColdAccountRecordService;
import com.mz.trade.account.service.AppHotAccountRecordService;
import com.mz.trade.account.service.ExDigitalmoneyAccountService;
import com.mz.trade.account.service.ExDmColdAccountRecordService;
import com.mz.trade.account.service.ExDmHotAccountRecordService;
import com.mz.trade.model.AccountRemarkEnum;
import com.mz.trade.model.TradeRedis;
import com.mz.trade.redis.model.ExDigitalmoneyAccountRedis;
import java.math.BigDecimal;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午6:12:49
 */
@Service("exDigitalmoneyAccountService")
public class ExDigitalmoneyAccountServiceImpl extends
		BaseServiceImpl<ExDigitalmoneyAccount, Long> implements
		ExDigitalmoneyAccountService {

	
	
	@Resource(name = "exDigitalmoneyAccountDao")
	@Override
	public void setDao(BaseDao<ExDigitalmoneyAccount, Long> dao) {
		super.dao = dao;
	}
	
	private ExDigitalmoneyAccountService exDigitalmoneyAccountService;
	@Resource
	private ExDmColdAccountRecordService  exDmColdAccountRecordService;
	@Resource
	private ExDmHotAccountRecordService  exDmHotAccountRecordService;
	@Resource
	private AppColdAccountRecordService  appColdAccountRecordService;
	@Resource
	private AppHotAccountRecordService  appHotAccountRecordService;
	
	

	@Override
	public ExDmColdAccountRecord createColdRecord(Integer recordType,ExDigitalmoneyAccount exDigitalmoneyAccount, BigDecimal freezeMoney,String transactionNum,Integer remark){
		//资金池5(完成)，托管0(处理中)，将来通过配置文件来获取判断
		Integer status=5;
		
		ExDmColdAccountRecord coldAccountRecord=new ExDmColdAccountRecord();
		coldAccountRecord.setAccountId(exDigitalmoneyAccount.getId());
		coldAccountRecord.setCurrencyType(exDigitalmoneyAccount.getCurrencyType());
		coldAccountRecord.setWebsite(exDigitalmoneyAccount.getWebsite());
		coldAccountRecord.setCoinCode(exDigitalmoneyAccount.getCoinCode());
		coldAccountRecord.setCustomerId(exDigitalmoneyAccount.getCustomerId());
		coldAccountRecord.setSaasId(exDigitalmoneyAccount.getSaasId());
		coldAccountRecord.setUserName(exDigitalmoneyAccount.getUserName());
		coldAccountRecord.setTrueName(exDigitalmoneyAccount.getTrueName());
		coldAccountRecord.setTransactionNum(transactionNum);
		coldAccountRecord.setRecordType(recordType);
		coldAccountRecord.setStatus(status);
		coldAccountRecord.setTransactionMoney(freezeMoney);
		coldAccountRecord.setRemark(AccountRemarkEnum.getName(remark));
		return coldAccountRecord;
		
	}
	@Override
	public ExDmHotAccountRecord createHotRecord(Integer recordType,ExDigitalmoneyAccount account, BigDecimal freezeMoney,String transactionNum,Integer remark){
		//资金池5(完成)，托管0(处理中)将来通过配置文件来获取判断
		Integer status=5;
		ExDmHotAccountRecord hotAccountRecord=new ExDmHotAccountRecord();
		hotAccountRecord.setAccountId(account.getId());
		hotAccountRecord.setCurrencyType(account.getCurrencyType());
		hotAccountRecord.setWebsite(account.getWebsite());
		hotAccountRecord.setCoinCode(account.getCoinCode());
		
		hotAccountRecord.setCustomerId(account.getCustomerId());
		hotAccountRecord.setSaasId(account.getSaasId());
		hotAccountRecord.setUserName(account.getUserName());
		hotAccountRecord.setTrueName(account.getTrueName());
		hotAccountRecord.setTransactionNum(transactionNum);
		hotAccountRecord.setRecordType(recordType);
		hotAccountRecord.setStatus(status);
		hotAccountRecord.setTransactionMoney(freezeMoney);
		hotAccountRecord.setRemark(AccountRemarkEnum.getName(remark));
		return hotAccountRecord;
		
	}



	@Override
	public ExDigitalmoneyAccountRedis getExDigitalmoneyAccountByRedis(String id) {
		Object obj=TradeRedis.redisUtilExDigitalmoneyAccount.get(id);
		if(null==obj){
			// 去数据拿
			ExDigitalmoneyAccount account =this.get(Long.valueOf(id));
			ExDigitalmoneyAccountRedis accountr=(ExDigitalmoneyAccountRedis)JSON.parseObject(JSON.toJSONString( account),ExDigitalmoneyAccountRedis.class);
			return accountr;
		}else{
			ExDigitalmoneyAccountRedis accountr=(ExDigitalmoneyAccountRedis)obj;
			return accountr;
		}
		
	
	}


	@Override
	public void setExDigitalmoneyAccounttoRedis(ExDigitalmoneyAccountRedis exDigitalmoneyAccount) {
		TradeRedis.redisUtilExDigitalmoneyAccount.put(exDigitalmoneyAccount, exDigitalmoneyAccount.getId().toString());
	}




	
}
