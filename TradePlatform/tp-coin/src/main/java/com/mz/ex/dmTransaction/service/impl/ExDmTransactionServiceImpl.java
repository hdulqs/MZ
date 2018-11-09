/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-11-08 11:13:05 
 */
package com.mz.ex.dmTransaction.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.ex.digitalmoneyAccount.service.ExDigitalmoneyAccountService;
import com.mz.ex.dmHotAccountRecord.service.ExDmHotAccountRecordService;
import com.mz.ex.dmTransaction.model.ExDmTransaction;
import com.mz.ex.dmTransaction.service.ExDmTransactionService;
import com.mz.util.QueryFilter;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> ExDmTransactionService </p>
 * @author:         shangxl
 * @Date :          2017-11-08 11:13:05  
 */
@Service("exDmTransactionService")
public class ExDmTransactionServiceImpl extends BaseServiceImpl<ExDmTransaction, Long> implements ExDmTransactionService{
	
	@Resource(name="exDigitalmoneyAccountService")
	private ExDigitalmoneyAccountService exDigitalmoneyAccountService;
	
	@Resource(name="exDmTransactionService")
	private ExDmTransactionService exDmTransactionService;
	
	@Resource(name="exDmHotAccountRecordService")
	private ExDmHotAccountRecordService exDmHotAccountRecordService;
	
	@Resource(name="exDmTransactionDao")
	@Override
	public void setDao(BaseDao<ExDmTransaction, Long> dao) {
		super.dao = dao;
	}

	@Override
	public ExDmTransaction getExDmTransactionByOrderNo(String orderNo) {
		QueryFilter filter=new QueryFilter(ExDmTransaction.class);
		filter.addFilter("orderNo=", orderNo);
		ExDmTransaction dmTransaction=this.get(filter);
		if(dmTransaction!=null){
			return dmTransaction;
		}
		return null;
	}
	
	/*@Override
	public String rechargeCoin(ExDmTransaction exTxs){
		StringBuffer result = new StringBuffer("{\"success\":\"true\",\"msg\":");
		try {
			Integer status = exTxs.getStatus();
			//未审核充币订单
			if(status==1){
				com.mz.ex.digitalmoneyAccount.coin.ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService.getExDigitalmoneyAccountByPublicKey(exTxs.getInAddress(),exTxs.getCoinCode());
				if(exDigitalmoneyAccount==null){
					LogFactory.info("未查询到钱包账户");
					return null;
				}
				BigDecimal hotMoney = exDigitalmoneyAccount.getHotMoney();
				BigDecimal transactionMoney = exTxs.getTransactionMoney();
				BigDecimal k = hotMoney.add(transactionMoney);
				exDigitalmoneyAccount.setHotMoney(k);
				// 修改可用余额
				exDigitalmoneyAccountService.update(exDigitalmoneyAccount);
				// 保存可用余额流水
				com.mz.ex.dmHotAccountRecord.coin.ExDmHotAccountRecord exDmHotAccountRecord = new com.mz.ex.dmHotAccountRecord.coin.ExDmHotAccountRecord();
				exDmHotAccountRecord.setAccountId(exDigitalmoneyAccount.getId());
				exDmHotAccountRecord.setCustomerId(exTxs.getCustomerId());
				exDmHotAccountRecord.setUserName(exDigitalmoneyAccount.getUserName());
				exDmHotAccountRecord.setRecordType(1);
				exDmHotAccountRecord.setTransactionMoney(exTxs.getTransactionMoney());
				exDmHotAccountRecord.setStatus(5);
				exDmHotAccountRecord.setRemark("可用余额流水已记录成功 ");
				exDmHotAccountRecord.setCurrencyType(exTxs.getCurrencyType());
				exDmHotAccountRecord.setCoinCode(exTxs.getCoinCode());
				exDmHotAccountRecord.setWebsite(exTxs.getWebsite());
				exDmHotAccountRecord.setTransactionNum(exTxs.getTransactionNum());
				exDmHotAccountRecord.setSaasId(RpcContext.getContext().getAttachment("saasId"));
				exDmHotAccountRecordService.save(exDmHotAccountRecord);
				// 修改订单
				exTxs.setStatus(2);
				exTxs.setUserId(exDigitalmoneyAccount.getCustomerId());
				exDmTransactionService.update(exTxs);
				result.append("\"充值记账成功\"");
			}else{
				result.append("\"该订单已通过\"");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.append("\"异常 :" + e.getMessage() + "  \"");
		} finally {
			result.append("}");
		}
		return result.toString();
	}*/

}
