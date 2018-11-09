/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年10月25日 下午6:50:40
 */
package com.mz.exchange.transaction;

import com.azazar.krotjson.JSON;
import com.mz.account.fund.model.AppTransaction;
import com.mz.account.remote.RemoteAppOurAccountService;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.customer.user.model.AppCustomer;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.account.service.ExAmineOrderService;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.coin.model.Transaction;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import com.mz.util.QueryFilter;
import com.mz.util.StringUtil;
import com.mz.util.date.DateUtil;
import com.mz.util.idgenerate.IdGenerate;
import com.mz.util.idgenerate.NumConstant;
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import com.mz.customer.remote.RemoteAppCustomerService;
import com.mz.exchange.purse.CoinInterfaceUtil;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Liu Shilei
 * @Date : 2016年10月25日 下午6:50:40
 */
public class HxbCoinQuart {



	public void recordAll() {

		try {

			Map<String, String> map = new HashMap<String, String>();
			// 查询已实名的用户
			RemoteAppCustomerService remoteAppCustomerService = (RemoteAppCustomerService) ContextUtil.getBean("remoteAppCustomerService");
			List<AppCustomer> customerList = remoteAppCustomerService.getRealNameCustomer();

			ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService) ContextUtil.getBean("exDigitalmoneyAccountService");

			ExDmTransactionService exDmTransactionService = (ExDmTransactionService) ContextUtil.getBean("exDmTransactionService");

			StringBuffer sf = new StringBuffer();
			for (AppCustomer customer : customerList) {
					String txStr = "";
			
					LogFactory.info("线程等待1500毫秒");
					Thread currentThread = Thread.currentThread();
					currentThread.sleep(1500);
					txStr = CoinInterfaceUtil.listByCustomerName("HXB", customer.getUserName());

					System.out.println(txStr);
					if (!"".equals(txStr) && null != txStr) {
						@SuppressWarnings("unchecked")
						List<String> txList = (List<String>) JSON.parse(txStr);

						for (String txs : txList) {
							// 转换为map
							Map<String, Object> tx2map = StringUtil.str2map(txs);
							String json = com.alibaba.fastjson.JSON.toJSONString(tx2map);
							json = json.replaceAll(" ", "");
							Transaction tx = com.alibaba.fastjson.JSON.parseObject(json, Transaction.class);

							String name = customer.getUserName();
							String address = tx.getAddress();
							QueryFilter queryFilter = new QueryFilter(ExDigitalmoneyAccount.class);
							queryFilter.addFilter("publicKey=", address);
							queryFilter.addFilter("userName=", name);
							ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService.get(queryFilter);
							if (null != exDigitalmoneyAccount) {
								// 站点 中国站/国际站
								String webSite = exDigitalmoneyAccount.getWebsite();
								// 用户持币code
								String userCode = exDigitalmoneyAccount.getCoinCode();
								String currencyType = exDigitalmoneyAccount.getCurrencyType();

								String category = tx.getCategory();// 交易类型
								if (category.equals("receive")) {
									// confirmations
									String confirmations = String.valueOf(tx.getConfirmations());// 确认节点数
									String amount = String.valueOf(tx.getAmount());
									String blocktime = "";
									if (!confirmations.equals("0")) {
										blocktime = tx.getBlockTime();// 区块时间
										blocktime = DateUtil.stampToDate(blocktime + "000");
									}

									String txid = tx.getTxId();// 交易单号
									String time = tx.getTime().toString();
									if (null != time && !"".equals(time)) {
										time = DateUtil.stampToDate(time + "000");
									}
									String timereceived = tx.getTimeReceived();
									if (null != timereceived && !"".equals(timereceived)) {
										timereceived = DateUtil.stampToDate(timereceived + "000");
									}
									Object feeobj = tx.getFee();
									String fee = "";
									if (null != feeobj) {
										fee = String.valueOf(feeobj);
									}
									// 记录 收入的金额

									QueryFilter filter = new QueryFilter(AppTransaction.class);
									filter.addFilter("orderNo=", txid);

									ExDmTransaction transaction = exDmTransactionService.get(filter);
									if (null == transaction) {
										ExDmTransaction exDmTransaction = new ExDmTransaction();
										exDmTransaction.setAccountId(exDigitalmoneyAccount.getId());
										exDmTransaction.setCoinCode(userCode);
										exDmTransaction.setCreated(new Date());
										exDmTransaction.setCurrencyType(currencyType);
										exDmTransaction.setCustomerId(customer.getId());
										exDmTransaction.setCustomerName(customer.getUserName());
										exDmTransaction.setTrueName(exDigitalmoneyAccount.getTrueName());
										exDmTransaction.setTime(time);
										exDmTransaction.setTimereceived(timereceived);
										exDmTransaction.setInAddress(address);
										exDmTransaction.setConfirmations(confirmations);
										exDmTransaction.setBlocktime(blocktime);
										exDmTransaction.setFee(new BigDecimal(0));
										exDmTransaction.setTransactionMoney(new BigDecimal(amount));

										exDmTransaction.setStatus(1);
										exDmTransaction.setOrderNo(txid);
										exDmTransaction.setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
										// 充币
										exDmTransaction.setTransactionType(1);
										exDmTransaction.setUserId(customer.getId());
										exDmTransaction.setWebsite(webSite);

										exDmTransactionService.save(exDmTransaction);

										long num = Long.valueOf(confirmations);
										if (num >= 2) {

											QueryFilter fil = new QueryFilter(ExDmTransaction.class);
											fil.addFilter("orderNo=", txid);

											ExDmTransaction tran = exDmTransactionService.get(fil);
											ExAmineOrderService examineOrderService = (ExAmineOrderService) ContextUtil.getBean("examineOrderService");

											String s = examineOrderService.pasePutOrderToAppAccount(tran.getId());

										}

									}

								}

							}

						}

					}

				}


		} catch (Exception e) {

		}

	}
	//更新记录。把数据库中未审核的充币 提币订单通过订单号调用钱包接口查询，如果确认节点数不小于2就更新订单状态并更新余额。
	public Map<String, String> updateStatus() {
		ExDmTransactionService   exDmTransactionService=(ExDmTransactionService)ContextUtil.getBean("exDmTransactionService");
	    RemoteAppOurAccountService remoteAppOurAccountService = (RemoteAppOurAccountService) ContextUtil.getBean("remoteAppOurAccountService");

		//查询数据库中充值订单状态为未审核的数据。
		QueryFilter filter=new QueryFilter(ExDmTransaction.class);
		//再次修改之后  把提币状态为4的给去掉了 但由于项目已经用了一段 所以还保留对4的查询 之后可以去掉
		filter.addFilter("status_in", "1,4");
		List<ExDmTransaction>  list=exDmTransactionService.find(filter);
		int total=list.size();
		int addTotal=0;
		int number=0;
		if(null!=list){
		      for(ExDmTransaction  exDmTransaction:list){
		    	  String  orderNum=exDmTransaction.getOrderNo();
		    	  String coinCode=exDmTransaction.getCoinCode();
		    	    //  调用钱包接口查询订单详情。
					String txStr=CoinInterfaceUtil.row(orderNum, coinCode);
					if(null!=txStr&&!"".equals(txStr)){
					txStr=txStr.replace(" ", "");
					Map<String, Object> tx2map=StringUtil.str2map(txStr);
					String confirmations=	tx2map.get("confirmations").toString();
                    Long num=Long.valueOf(confirmations);
                     //确认节点数大于1时  更新订单状态和账户余额
	                if(num>=2){
	                	  //充币
	                   if(exDmTransaction.getTransactionType()==1){
	                	   number++;
			    		   ExAmineOrderService examineOrderService = (ExAmineOrderService) ContextUtil.getBean("examineOrderService");
			    		   String s = examineOrderService.pasePutOrderToAppAccount(exDmTransaction.getId());
			    		   if (s.equals("OK")) {
			    			   addTotal++;
				   			} 
	                   }
	                   //提币 只查询状态为4时   
	                   else if(exDmTransaction.getTransactionType()==2){
	                	   System.out.println("定时提币");
	                	   
	                	   /*
	                	   if(exDmTransaction.getStatus()==4){
	                		  number++;
			    		   ExAmineOrderService examineOrderService = (ExAmineOrderService) ContextUtil.getBean("examineOrderService");
	
			    		   String s = examineOrderService.pasePutOrder(exDmTransaction.getId());
			    		   if (s.equals("OK")) {
			    			   addTotal++;
			    			   
				   			} 
			    		   
			    		   Object feeobj=	tx2map.get("fee");
			    		   String fee="";
				    	   if (null!=feeobj) {
				    		   fee=String.valueOf(feeobj);
				    		   fee=fee.substring(1, fee.length());
						   }
				    	   
				    	   //查询我方提币账户
				    	   AppOurAccount our=remoteAppOurAccountService.findAppOurAccount(exDmTransaction.getWebsite(), exDmTransaction.getCurrencyType(), 1);
				             //保存流水并更新我方提币账户余额(手续费)
					          AppAccountRecord withdrawRecord=new AppAccountRecord();
					           withdrawRecord.setAppAccountId(our.getId());
					    	   withdrawRecord.setAppAccountNum(our.getAccountNumber());
					    	   withdrawRecord.setRecordType(1);
					    	   withdrawRecord.setSource(0);
					    	   withdrawRecord.setTransactionMoney(new BigDecimal(fee));
					    	   withdrawRecord.setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
					    	   withdrawRecord.setStatus(5);
					    	   withdrawRecord.setRemark("钱包收取的提币手续费");
					    	   withdrawRecord.setCurrencyName(exDmTransaction.getCurrencyType());
					    	   withdrawRecord.setCurrencyType(exDmTransaction.getCurrencyType());
					    	   withdrawRecord.setRemark(exDmTransaction.getOrderNo()+"-purseFee");
					    	   boolean c=remoteAppOurAccountService.updateAccountBalance(our,withdrawRecord);
	                    }
	                   */}
	                
	                }
					
					}
			
		      }
		
		}
		//LogFactory.info("本次查询数据库中审核中的数据有:"+total+" 确认节点数超过2的:"+number+"  修改记录:"+addTotal);

		
		return null;
	}

	//把用户充值进来的币转到平台维护的充币地址和提币地址。
	public JsonResult sendToOurRecharge() {
		return null;
		/*
		
		System.out.println("进入转币操作===");
		JsonResult jsonResult=new JsonResult();
		Map<String,BigDecimal> map = new HashMap<String, BigDecimal>();
		//		Map<String,Map<String,BigDecimal>> statesMap = new HashMap<String,Map<String,BigDecimal>>(); 
		
		try {
		// 查询已实名的用户
		RemoteAppCustomerService remoteAppCustomerService = (RemoteAppCustomerService) ContextUtil.getBean("remoteAppCustomerService");
	    List<AppCustomer> customerList=remoteAppCustomerService.getRealNameCustomer();
		
	    ExDigitalmoneyAccountService exDigitalmoneyAccountService=(ExDigitalmoneyAccountService)ContextUtil.getBean("exDigitalmoneyAccountService");
	    
    	RemoteAppOurAccountService remoteAppOurAccountService = (RemoteAppOurAccountService) ContextUtil.getBean("remoteAppOurAccountService");

    	QueryFilter  f=new QueryFilter(ExProduct.class);
		f.addFilter("issueState=", "1");

		StringBuffer sf=new StringBuffer();
		for(AppCustomer customer:customerList){
	    	// 查询某个人的所有币 账户 
	    	List<ExDigitalmoneyAccount> digitalmoneyAccountList=exDigitalmoneyAccountService.getlistByCustomerId(customer.getId());

	    	// 循环所有的币账户  
	    	for(ExDigitalmoneyAccount digitalmoneyAccount:digitalmoneyAccountList){
	    		
	    		String txStr="";
	    		
	    		String coinCode = digitalmoneyAccount.getCoinCode();
		    	String website = digitalmoneyAccount.getWebsite();
		    	String mapKey = coinCode+"-"+website;
		    	
	    		// 查询站点中我方币种账户的余额 
	    		BigDecimal bigDecimal = map.get(mapKey);
	    		      
	    		List<AppOurAccount> list = remoteAppOurAccountService.getOurAccounts(digitalmoneyAccount.getWebsite(),digitalmoneyAccount.getCoinCode());
	    		// 我方提币账户 
				AppOurAccount withdrawAccount=null;
				// 我方充币账户
				AppOurAccount rechagreAccount=null;
				// 需要转入我方提币账户的币的数量
				BigDecimal transferWithdrawNum=new BigDecimal("0.00");
				// 需要转入我方充币账户的币的数量
				BigDecimal transferRechargeNum=new BigDecimal("0.00");
	    		
				// 应该转到提币地址的币的数量
			    for(AppOurAccount account:list){
			    	 // 我方充币地址账户
			    	 if(account.getOpenAccountType().equals("0")){
			    		 rechagreAccount=account;
			    	 }
			    	 // 我方提币地址账户                                                                                
			    	 if(account.getOpenAccountType().equals("1")){
			    		withdrawAccount=account;
			    		  // 查询提币地址的数量
			    		if(null == bigDecimal){
			    			String withdrawAccountBalance=CoinInterfaceUtil.balance(withdrawAccount.getAccountName(), digitalmoneyAccount.getCoinCode());
			    			BigDecimal withdrawBalance=new BigDecimal(withdrawAccountBalance);
			    			transferWithdrawNum=withdrawAccount.getRetainsMoney().subtract(withdrawBalance);
			    			// 如果没有保存 起来 
			    			map.put(mapKey, transferWithdrawNum);
			    		}else{
			    			transferWithdrawNum=bigDecimal;
			    		}
			    	}
			     }
			    
	    		
	    		//查询用户余额
	    		//国际站
	    		if (digitalmoneyAccount.getWebsite().equals(ContextUtil.EN)) {
		    	   // 查询某个人的某个币种有多少余额
		    		   txStr= CoinInterfaceUtil.balance(customer.getUserName()+"-USD", digitalmoneyAccount.getCoinCode());
				}else{
				  // 查询某个人的某个币种有多少余额
		    		   txStr= CoinInterfaceUtil.balance(customer.getUserName(), digitalmoneyAccount.getCoinCode());
				}
	    		
	    		System.out.println("转币str="+txStr);

	    		if(!"".equals(txStr)&&null!=txStr){
	    			BigDecimal userBalance=new BigDecimal(txStr);
	    			// 判断用户的余额不为 0 
	    			if(userBalance.compareTo(new BigDecimal("0.00"))>0){
	    				
	    			//查询钱包余额
	    			String purse=CoinInterfaceUtil.balance("", digitalmoneyAccount.getCoinCode());
	    			BigDecimal purseBalance=new BigDecimal(purse);
	    			// 钱包服务器上的币的数量大于用户账户的币的数量
	    			if(purseBalance.compareTo(userBalance)>0){
	    				
	    				//用户账户预留0.1个币 作为旷工费
	    				userBalance=userBalance.subtract(new BigDecimal("0.1"));
	    			
	    				// 根据币的代码查找我方账户
	    				// List<AppOurAccount> list=remoteAppOurAccountService.getOurAccounts(digitalmoneyAccount.getWebsite(),digitalmoneyAccount.getCoinCode());
	    				
	    			     if(null!=withdrawAccount){
	    					    
	    					  // 用户转入平台提币地址的币的数量
	    			    	  BigDecimal hh=new BigDecimal("0.00");
	    			    	     // 查询提币地址的数量
	    		    			String withdrawAccountBalance=CoinInterfaceUtil.balance(withdrawAccount.getAccountName(), digitalmoneyAccount.getCoinCode());
	    		    			BigDecimal withdrawBalance=new BigDecimal(withdrawAccountBalance);
	    		    			
	    		    			BigDecimal retainsMoney = withdrawAccount.getRetainsMoney();
	    		    			BigDecimal subtract = withdrawBalance.subtract(retainsMoney);
	    		    			
	    		    			// 提币账户的币的数量小于提币账户保留金额时 转币到提币账户
	    		    			if (BigDecimal.ZERO.compareTo(subtract)>0) {
	    		    				
	    		    			//  应该转到提币地址的币的数量
	    		    			//	transferWithdrawNum=withdrawAccount.getRetainsMoney().subtract(withdrawBalance);
		    		    			
	    		    				    if (userBalance.compareTo(transferWithdrawNum)>=0) {
	    		    				    	
	 	    								//调用钱包接口转币到我方提币地址
	 	    								String withdrawResult=CoinInterfaceUtil.sendTo(digitalmoneyAccount.getPublicKey(),withdrawAccount.getAccountNumber(), transferWithdrawNum.toString(), withdrawAccount.getCurrencyType(),null);
	 	    								if(null!=withdrawResult&&!"".equals(withdrawResult)){
	 	    									Map<String, Object> tx2map=StringUtil.str2map(withdrawResult);
	 	    									String order=tx2map.get("msg").toString();
	 	    									String code=tx2map.get("code").toString();
	 	    									if(code.equals("8")){
	 	    										BigDecimal decimal = map.get(mapKey);
	 	    										BigDecimal subtract2 = decimal.subtract(transferWithdrawNum);
	 	    										map.put(mapKey, subtract2);
	 	    										sf.append(" 转入成功 ");
	 	    								         //保存流水并更新我方提币账户余额
	 	    								          AppAccountRecord withdrawRecord=new AppAccountRecord();
	 	    								           withdrawRecord.setAppAccountId(withdrawAccount.getId());
	 	    								    	   withdrawRecord.setAppAccountNum(withdrawAccount.getAccountNumber());
	 	    								    	   withdrawRecord.setRecordType(1);
	 	    								    	   withdrawRecord.setSource(0);
	 	    								    	   withdrawRecord.setTransactionMoney(transferWithdrawNum);
	 	    								    	   withdrawRecord.setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
	 	    								    	   withdrawRecord.setStatus(5);
	 	    								    	   withdrawRecord.setRemark("从用户账户转币到我方提币账户");
	 	    								    	   withdrawRecord.setCurrencyName(withdrawAccount.getCurrencyType());
	 	    								    	   withdrawRecord.setCurrencyType(withdrawAccount.getCurrencyType());
	 	    								    	   withdrawRecord.setRemark(order);
	 	    								    	   withdrawRecord.setCustomerId(customer.getId());
	 	    								    	   withdrawRecord.setCustomerName(customer.getUserName());
	 	    								    	   withdrawRecord.setWebsite(digitalmoneyAccount.getWebsite());
	 	    								    	   boolean c=remoteAppOurAccountService.updateWithdrawAccountBalance(withdrawAccount,withdrawRecord);
	 	    								    	   if(c){
	 	    								    		   
	 	    								    	   }
	 	    									}
	 	    						
	 	    								}
	    		    			
	    		    				    }
	    		    		
	    		    			}
	    		    	
	    			     }
	    			  
	    			    if(null!=rechagreAccount){
	    			    	  //转入充币账户的数量
	    			    	  transferRechargeNum=userBalance.subtract(transferWithdrawNum);
								//调用钱包接口转币到我方充币地址
								String rechargeResult=CoinInterfaceUtil.sendTo(digitalmoneyAccount.getPublicKey(),rechagreAccount.getAccountNumber(), transferRechargeNum.toString(), rechagreAccount.getCurrencyType(),null);
								
								if(null!=rechargeResult&&!"".equals(rechargeResult)){
									
									Map<String, Object> m=StringUtil.str2map(rechargeResult);
									String or=m.get("msg").toString();
									String co=m.get("code").toString();
									if(co.equals("8")){
										  
								    	   //保存流水并更新我方充币账户余额
								           AppAccountRecord rechargeRecord=new AppAccountRecord();
								           rechargeRecord.setAppAccountId(rechagreAccount.getId());
								    	   rechargeRecord.setAppAccountNum(rechagreAccount.getAccountNumber());
								    	   rechargeRecord.setRecordType(1);
								    	   rechargeRecord.setSource(0);
								    	   rechargeRecord.setTransactionMoney(transferRechargeNum);
								    	   rechargeRecord.setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
								    	   rechargeRecord.setStatus(5);
								    	   rechargeRecord.setRemark("从用户账户转币到我方充值账户");
								    	   rechargeRecord.setCurrencyName(rechagreAccount.getCurrencyType());
								    	   rechargeRecord.setCurrencyType(rechagreAccount.getCurrencyType());
								    	   rechargeRecord.setRemark(or);
								    	   rechargeRecord.setCustomerId(customer.getId());
								    	   rechargeRecord.setCustomerName(customer.getUserName());
								    	   rechargeRecord.setWebsite(digitalmoneyAccount.getWebsite());
								    	   boolean b=remoteAppOurAccountService.updateAccountBalance(rechagreAccount,rechargeRecord);

									}
							
								}
						 
	    			    }
	    			
	    			}
	    		
	    			}
	    		
	    		}
	    	  
	    	   }
	   
	    }

		} catch (Exception e) {
			
			return null ;
		}
	
		
		jsonResult.setSuccess(true);
		return jsonResult;
	*/}

}
