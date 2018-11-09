/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月28日 下午7:00:10
 */
package com.mz.exchange.transaction.service.impl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.azazar.krotjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mz.account.fund.model.AppAccountRecord;
import com.mz.account.fund.model.AppOurAccount;
import com.mz.account.remote.RemoteAppOurAccountService;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageFactory;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.user.model.AppCustomer;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.account.service.ExAmineOrderService;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.coin.model.Transaction;
import com.mz.exchange.lend.model.ExDmLend;
import com.mz.exchange.product.model.ExProduct;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.util.QueryFilter;
import com.mz.util.RemoteQueryFilter;
import com.mz.util.StringUtil;
import com.mz.util.UniqueRecord;
import com.mz.util.date.DateUtil;
import com.mz.util.idgenerate.IdGenerate;
import com.mz.util.idgenerate.NumConstant;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.customer.remote.RemoteAppCustomerService;
import com.mz.customer.user.service.AppCustomerService;
import com.mz.exchange.product.service.ExProductService;
import com.mz.exchange.purse.CoinInterfaceUtil;
import com.mz.exchange.transaction.dao.ExDmTransactionDao;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import com.mz.manage.remote.model.ExDmTransactionManage;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.mq.producer.service.MessageProducer;
import com.mz.trade.redis.model.Accountadd;
import com.mz.util.UniqueRecordService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午7:00:10
 */
@Service("exDmTransactionService")
public class ExDmTransactionServiceImpl extends
    BaseServiceImpl<ExDmTransaction, Long> implements
    ExDmTransactionService {

  @Resource
  public ExDigitalmoneyAccountService exDigitalmoneyAccountService;

  @Resource
  private MessageProducer messageProducer;

  @Resource
  private AppCustomerService appCustomerService;

  @Resource
  private UniqueRecordService uniqueRecordService;


  @Resource(name = "exDmTransactionDao")
  @Override
  public void setDao(BaseDao<ExDmTransaction, Long> dao) {
    super.dao = dao;
  }


  @Override
  public PageResult findPageBySql(QueryFilter filter) {

    //----------------------分页查询头部外壳------------------------------
    //创建PageResult对象
    PageResult pageResult = new PageResult();
    Page<ExDmLend> page = null;
    if (filter.getPageSize().compareTo(Integer.valueOf(-1)) == 0) {
      //pageSize = -1 时
      //pageHelper传pageSize参数传0查询全部
      page = PageHelper.startPage(filter.getPage(), 0);
    } else {
      page = PageHelper.startPage(filter.getPage(), filter.getPageSize());
    }
    //----------------------分页查询头部外壳------------------------------

    //----------------------查询开始------------------------------

    String customerName = filter.getRequest().getParameter("customerName_like");
    String coinCode = filter.getRequest().getParameter("coinCode");
    String status = filter.getRequest().getParameter("status");
    String createdG = filter.getRequest().getParameter("created_GT");
    String createdL = filter.getRequest().getParameter("created_LT");
    String inAddress = filter.getRequest().getParameter("inAddress_like");
    String outAddress = filter.getRequest().getParameter("outAddress_like");
    String transactionNum = filter.getRequest().getParameter("transactionNum_like");
    String transactionType = filter.getRequest().getParameter("type");
    String currencyType = filter.getRequest().getParameter("currencyType");
    String trueName = filter.getRequest().getParameter("trueName_like");
    Map<String, Object> map = new HashMap<String, Object>();
    if (!StringUtils.isEmpty(customerName)) {
      map.put("customerName", "%" + customerName + "%");
    }

    if (!StringUtils.isEmpty(trueName)) {
      map.put("trueName", "%" + trueName + "%");
    }

    if (!StringUtils.isEmpty(inAddress)) {
      map.put("inAddress", "%" + inAddress + "%");
    }

    if (!StringUtils.isEmpty(outAddress)) {
      map.put("outAddress", "%" + outAddress + "%");
    }

    if (!StringUtils.isEmpty(transactionNum)) {
      map.put("transactionNum", "%" + transactionNum + "%");
    }

    if (!StringUtils.isEmpty(transactionType)) {
      map.put("transactionType", transactionType);
    }

    if (!StringUtils.isEmpty(currencyType)) {
      map.put("currencyType", currencyType);
    }
    if (!StringUtils.isEmpty(coinCode)) {
      if (!coinCode.equals("all")) {
        map.put("coinCode", coinCode);
      }

    }

    int[] s = new int[3];
    if (!StringUtils.isEmpty(status)) {
      if (!status.equals("all")) {
        String[] str = status.split(",");
        if (str.length >= 1) {

        }
        map.put("status", "(" + status + ")");
      }

    } else {
      s[0] = 2;
      map.put("status", "2");
    }
    if (!StringUtils.isEmpty(createdG)) {
      map.put("createdG", createdG);
    }
    if (!StringUtils.isEmpty(createdL)) {

      map.put("createdL", createdL);
    }
    map.put("orderby", "app.created desc");
    ((ExDmTransactionDao) dao).findPageBySql(map);
    //设置分页数据
    pageResult.setRows(page.getResult());
    //设置总记录数
    pageResult.setRecordsTotal(page.getTotal());
    pageResult.setDraw(filter.getDraw());
    pageResult.setPage(filter.getPage());
    pageResult.setPageSize(filter.getPageSize());

    return pageResult;
  }


  @Override
  public ExDmTransaction findLastTrasaction() {

    ExDmTransactionDao exDmTransactionDao = (ExDmTransactionDao) dao;
    ExDmTransaction lastTrasaction = exDmTransactionDao.findLastTrasaction();

    return lastTrasaction;
  }


  /**
   * 使用用户名查询某种币他在某种币的提现或充值的数量
   *
   * type 字符串类型  表示类型  ---- 1 表示充值   2 表示提现   (1,2)
   */
  @Override
  public BigDecimal findTransactionByCustomer(String customer, String coinCode, String type) {

    ExDmTransactionDao exDmTransactionDao = (ExDmTransactionDao) dao;

    BigDecimal totalNum = exDmTransactionDao.findGetNumByCustomer(customer, coinCode, type);

    if (null == totalNum) {
      return BigDecimal.ZERO;
    }

    return totalNum;
  }


  //之前用来刷新钱包记录方法 ，但由于每次查记录只查前10条，就会有漏掉的数据，所以这个方法暂时不用了。换成了下面的recordAll();
  @Override
  public Map<String, String> record() {
    QueryFilter f = new QueryFilter(ExProduct.class);
    f.addFilter("issueState=", "1");
    Map<String, String> map = new HashMap<String, String>();
    ExProductService exProductService = (ExProductService) ContextUtil.getBean("exProductService");
    List<ExProduct> l = exProductService.find(f);
    StringBuffer sf = new StringBuffer();
    for (ExProduct product : l) {
      //总查询记录数
      int txTotal = 0;
      //保存记录总数
      int saveTxTotal = 0;

      try {
        //http 请求获取交易数据
        String txStr = CoinInterfaceUtil.list(product.getCoinCode());
        if (!"".equals(txStr) && null != txStr) {
          @SuppressWarnings("unchecked")
          List<String> txList = (List<String>) JSON.parse(txStr);

          String lastTxid = null;
          RemoteAppCustomerService remoteAppCustomerService = (RemoteAppCustomerService) ContextUtil
              .getBean("remoteAppCustomerService");
          ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService) ContextUtil
              .getBean("exDigitalmoneyAccountService");
          ExDmTransactionService exDmTransactionService = (ExDmTransactionService) ContextUtil
              .getBean("exDmTransactionService");
          ExDmTransaction lastTx = exDmTransactionService.findLastTrasaction();

          // 是否是最后一条数据
          boolean isLast = false;
          if (lastTx == null) {
            lastTxid = "";
            isLast = true;
          } else {
            lastTxid = lastTx.getOrderNo();
          }
          //查询总记录数
          txTotal = txList.size();

          for (String txs : txList) {
            //转换为map
            Map<String, Object> tx2map = StringUtil.str2map(txs);
            String json = com.alibaba.fastjson.JSON.toJSONString(tx2map);
            json = json.replaceAll(" ", "");
            Transaction tx = com.alibaba.fastjson.JSON
                .parseObject(json, Transaction.class);

            //从系统中记录的最后一条开始记录
            if (tx.getTxId().equals(lastTxid)) {
              isLast = true;
              continue;
            }

            if (isLast) {
              String name = tx.getAccount();
              String address = tx.getAddress();
              QueryFilter queryFilter = new QueryFilter(ExDigitalmoneyAccount.class);
              queryFilter.addFilter("publicKey=", address);
              ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService
                  .get(queryFilter);
              if (null != exDigitalmoneyAccount) {
                //站点 中国站/国际站
                String webSite = exDigitalmoneyAccount.getWebsite();
                //用户持币code
                String userCode = exDigitalmoneyAccount.getCoinCode();
                String currencyType = exDigitalmoneyAccount.getCurrencyType();

                RemoteQueryFilter remoteQueryFilter = new RemoteQueryFilter(AppCustomer.class);
                remoteQueryFilter.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
                remoteQueryFilter.addFilter("userName=", name);

                AppCustomer appCustomer = remoteAppCustomerService
                    .getByQueryFilter(remoteQueryFilter);
                if (null != appCustomer) {
                  String category = tx.getCategory();//交易类型
                  //confirmations
                  String confirmations = String.valueOf(tx.getConfirmations());//确认节点数
                  String amount = String.valueOf(tx.getAmount());
                  String blocktime = "";
                  if (!confirmations.equals("0")) {
                    blocktime = tx.getBlockTime().toString();//区块时间
                    blocktime = DateUtil.stampToDate(blocktime + "000");

                  }

                  String txid = tx.getTxId();//交易单号
                  String time = tx.getTime();
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
                  //记录 收入的金额
                  if (category.equals("receive")) {
                    QueryFilter filter = new QueryFilter(ExDmTransaction.class);
                    filter.addFilter("transactionNum=", txid);

                    ExDmTransaction transaction = exDmTransactionService.get(filter);
                    if (null == transaction) {
                      ExDmTransaction exDmTransaction = new ExDmTransaction();
                      exDmTransaction.setAccountId(exDigitalmoneyAccount.getId());
                      exDmTransaction.setCoinCode(userCode);
                      exDmTransaction.setCreated(new Date());
                      exDmTransaction.setCurrencyType(currencyType);
                      exDmTransaction.setCustomerId(appCustomer.getId());
                      exDmTransaction.setCustomerName(appCustomer.getUserName());
                      exDmTransaction.setTime(time);
                      exDmTransaction.setTimereceived(timereceived);
                      exDmTransaction.setInAddress(address);
                      exDmTransaction.setConfirmations(confirmations);
                      exDmTransaction.setBlocktime(blocktime);
                      exDmTransaction.setFee(new BigDecimal(0));
                      exDmTransaction.setTransactionMoney(new BigDecimal(amount));

                      exDmTransaction.setStatus(1);
                      exDmTransaction.setOrderNo(txid);
                      exDmTransaction.setTransactionNum(
                          IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
                      //充币
                      exDmTransaction.setTransactionType(1);
                      exDmTransaction.setUserId(appCustomer.getId());
                      exDmTransaction.setWebsite(webSite);

                      exDmTransactionService.save(exDmTransaction);
                      int num = Integer.valueOf(confirmations);
                      if (num >= 2) {
                        QueryFilter fil = new QueryFilter(ExDmTransaction.class);
                        filter.addFilter("transactionNum=", txid);

                        ExDmTransaction tran = exDmTransactionService.get(fil);
                        ExAmineOrderService examineOrderService = (ExAmineOrderService) ContextUtil
                            .getBean("examineOrderService");

                        String s = examineOrderService.pasePutOrder(tran.getId());
                        if (s.equals("OK")) {

                        } else {

                        }
                      }

                      saveTxTotal++;
                    }

                  }

                }
              }

            }

          }
          sf.append("币种:" + product.getCoinCode() + "  " + "查询的总记录数:" + txTotal + "  " + "保存的记录数:"
              + saveTxTotal);
          map.put("code", "success");
          map.put("msg", "刷新成功");
        } else {
          sf.append("币种:" + product.getCoinCode() + "查询的记录为空");
        }

      } catch (Exception e) {
        e.printStackTrace();
        map.put("code", "err");
        sf.append("异常:" + e.getMessage());

      }
      LogFactory.info(sf);
    }

    map.put("msg", sf.toString());

    return map;
  }


  //更新记录。把数据库中未审核的充币 提币订单通过订单号调用钱包接口查询，如果确认节点数不小于2就更新订单状态并更新余额。
  @Override
  public Map<String, String> updateStatus() {
    ExDmTransactionService exDmTransactionService = (ExDmTransactionService) ContextUtil
        .getBean("exDmTransactionService");
    RemoteAppOurAccountService remoteAppOurAccountService = (RemoteAppOurAccountService) ContextUtil
        .getBean("remoteAppOurAccountService");

    //查询数据库中充值订单状态为未审核的数据。
    QueryFilter filter = new QueryFilter(ExDmTransaction.class);
    //再次修改之后  把提币状态为4的给去掉了 但由于项目已经用了一段 所以还保留对4的查询 之后可以去掉
    filter.addFilter("status_in", "1,4");
    List<ExDmTransaction> list = exDmTransactionService.find(filter);
    int total = list.size();
    int addTotal = 0;
    int number = 0;
    if (null != list) {
      for (ExDmTransaction exDmTransaction : list) {
        String orderNum = exDmTransaction.getOrderNo();
        String coinCode = exDmTransaction.getCoinCode();
        //  调用钱包接口查询订单详情。
        String txStr = CoinInterfaceUtil.row(orderNum, coinCode);
        if (null != txStr && !"".equals(txStr)) {
          txStr = txStr.replace(" ", "");
          Map<String, Object> tx2map = StringUtil.str2map(txStr);
          String confirmations = tx2map.get("confirmations").toString();
          Long num = Long.valueOf(confirmations);
          //确认节点数大于1时  更新订单状态和账户余额
          if (num > 1) {
            //充币
            if (exDmTransaction.getTransactionType() == 1) {
              number++;
              ExAmineOrderService examineOrderService = (ExAmineOrderService) ContextUtil
                  .getBean("examineOrderService");
              String s = examineOrderService.pasePutOrder(exDmTransaction.getId());
              if (s.equals("OK")) {
                addTotal++;
              }
            }
            //提币 只查询状态为4时
            else if (exDmTransaction.getTransactionType() == 2) {
              if (exDmTransaction.getStatus() == 4) {
                number++;
                ExAmineOrderService examineOrderService = (ExAmineOrderService) ContextUtil
                    .getBean("examineOrderService");

                String s = examineOrderService.pasePutOrder(exDmTransaction.getId());
                if (s.equals("OK")) {
                  addTotal++;

                }

                Object feeobj = tx2map.get("fee");
                String fee = "";
                if (null != feeobj) {
                  fee = String.valueOf(feeobj);
                  fee = fee.substring(1, fee.length());
                }

                //查询我方提币账户
                AppOurAccount our = remoteAppOurAccountService
                    .findAppOurAccount(exDmTransaction.getWebsite(),
                        exDmTransaction.getCurrencyType(), 1);
                //保存流水并更新我方提币账户余额(手续费)
                AppAccountRecord withdrawRecord = new AppAccountRecord();
                withdrawRecord.setAppAccountId(our.getId());
                withdrawRecord.setAppAccountNum(our.getAccountNumber());
                withdrawRecord.setRecordType(1);
                withdrawRecord.setSource(0);
                withdrawRecord.setTransactionMoney(new BigDecimal(fee));
                withdrawRecord
                    .setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
                withdrawRecord.setStatus(5);
                withdrawRecord.setRemark("钱包收取的提币手续费");
                withdrawRecord.setCurrencyName(exDmTransaction.getCurrencyType());
                withdrawRecord.setCurrencyType(exDmTransaction.getCurrencyType());
                withdrawRecord.setRemark(exDmTransaction.getOrderNo() + "-purseFee");
                boolean c = remoteAppOurAccountService.updateAccountBalance(our, withdrawRecord);
              }
            }

          }

        }

      }
    }
    return null;
  }


  //把用户充值进来的币转到平台维护的充币地址和提币地址。
  @Override
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
	*/
  }


  /**
   * 手动刷新币账户的方法
   */
  @Override
  public Map<String, String> recordAll() {

    return null;

//				try {
//					
//				Map<String, String> map = new HashMap<String, String>();
//				//查询已实名的用户
//				RemoteAppCustomerService remoteAppCustomerService = (RemoteAppCustomerService) ContextUtil.getBean("remoteAppCustomerService");
//			    List<AppCustomer>	customerList=remoteAppCustomerService.getRealNameCustomer();
//			   
//			    //查询所有产品
//			    QueryFilter  f=new QueryFilter(ExProduct.class);
//				f.addFilter("issueState=", "1");
//				ExProductService exProductService = (ExProductService) ContextUtil.getBean("exProductService");
//			    List<ExProduct>	l=exProductService.find(f);
//			    
//			    ExDigitalmoneyAccountService    exDigitalmoneyAccountService=(ExDigitalmoneyAccountService)ContextUtil.getBean("exDigitalmoneyAccountService");
//
//				ExDmTransactionService   exDmTransactionService=(ExDmTransactionService)ContextUtil.getBean("exDmTransactionService");
//				
//				StringBuffer  sf=new StringBuffer();
//				
//				LogFactory.info("===  "+customerList.size());
//				
//			    for(AppCustomer customer:customerList){
//			    	
//			    	LogFactory.info("1=======  "+customer.getUserName());
//			    	
//		//	    	if("13967443365".equals(customer.getUserName())){
//			    	
//			    	List<ExDigitalmoneyAccount>   digitalmoneyAccountList=exDigitalmoneyAccountService.getlistByCustomerId(customer.getId());
//			    	LogFactory.info("=2.1=======  "+customer.getUserName());
//			    	if(null != digitalmoneyAccountList&& digitalmoneyAccountList.size()>0){
//			    		LogFactory.info("=2.2=======  "+customer.getUserName());	
//			    	for(ExDigitalmoneyAccount digitalmoneyAccount:digitalmoneyAccountList){
//			    		LogFactory.info("2=======  "+customer.getUserName());
//			    		String txStr="";
//			    		//国际站
//			    		if (digitalmoneyAccount.getWebsite().equals(ContextUtil.EN)) {
//				    	      txStr=   CoinInterfaceUtil.listByCustomerName(digitalmoneyAccount.getCoinCode(),customer.getUserName()+"-USD");
//                        
//			    		}else{
//			    			 LogFactory.info("线程等待1500毫秒");
//			    		     Thread currentThread = Thread.currentThread();
//			    		 //    currentThread.sleep(1500);
//			    		
//				    		 txStr=   CoinInterfaceUtil.listByCustomerName(digitalmoneyAccount.getCoinCode(),customer.getUserName());
//			    		}
//    		
//			    		 System.out.println(txStr);
//			    		 LogFactory.info("3=======  "+customer.getUserName());
//			    		if(!"".equals(txStr)&&null!=txStr){
//			    			
//			    			 LogFactory.info("1==="+txStr+"===");
//			    				@SuppressWarnings("unchecked")
//								List<String> txList=(List<String>) JSON.parse(txStr);
//			    			LogFactory.info("2==="+txStr+"===");	
//			    				for(String txs:txList){
//			    					LogFactory.info("4=======  "+customer.getUserName());
//			    					//转换为map
//									Map<String, Object> tx2map=StringUtil.str2map(txs);
//									String json= com.alibaba.fastjson.JSON.toJSONString(tx2map);
//									json=json.replaceAll(" ", "");
//						            com.mz.exchange.coin.model.Transaction tx= com.alibaba.fastjson.JSON.parseObject(json,com.mz.exchange.coin.model.Transaction.class);
//						                String  name=customer.getUserName();
//							            String  address=tx.getAddress();
//							            QueryFilter queryFilter=new QueryFilter(ExDigitalmoneyAccount.class);
//							            queryFilter.addFilter("publicKey=", address);
//							            queryFilter.addFilter("userName=", name);
//							            ExDigitalmoneyAccount exDigitalmoneyAccount=exDigitalmoneyAccountService.get(queryFilter);
//							         if(null!=exDigitalmoneyAccount){
//							        	 //站点 中国站/国际站
//							        	 String  webSite=exDigitalmoneyAccount.getWebsite();
//							        	 //用户持币code
//							        	 String userCode=exDigitalmoneyAccount.getCoinCode();
//							        	 String currencyType=exDigitalmoneyAccount.getCurrencyType();
//							           
//							        
//								    	   String  category=tx.getCategory();//交易类型
//								    	   if(category.equals("receive")){
//						                    //confirmations
//								    	   String  confirmations=String.valueOf(tx.getConfirmations());//确认节点数
//								    	   String  amount=String.valueOf(tx.getAmount());
//								    	   String  blocktime="";
//								    	   if(!confirmations.equals("0")){
//								    		   blocktime=tx.getBlockTime();//区块时间
//								    		   blocktime= DateUtil.stampToDate(blocktime+"000");
//								    	   }
//								    	 
//								    	   String  txid=tx.getTxId();//交易单号
//								    	   String  time=tx.getTime().toString();
//								    	   if(null!=time&&!"".equals(time)){
//								    		   time= DateUtil.stampToDate(time+"000");
//								    	   }
//								    	   String  timereceived=tx.getTimeReceived();
//									    	 if (null!=timereceived&&!"".equals(timereceived)) {
//									    		 timereceived= DateUtil.stampToDate(timereceived+"000");
//											}
//								    	   Object  feeobj=tx.getFee();
//								    	   String fee="";
//								    	   if (null!=feeobj) {
//								    		   fee=String.valueOf(feeobj);
//										   }
//								    	   //记录 收入的金额
//								    	   LogFactory.info("5=======  "+customer.getUserName());
//								    		   QueryFilter filter = new QueryFilter(ExDmTransaction.class);
//								    		   filter.addFilter("orderNo=", txid);
//								    		  
//								    		   ExDmTransaction   transaction=exDmTransactionService.get(filter);
//								    		   if(null == transaction || customer.getUserName()!=transaction.getCustomerName()){
//								    			   ExDmTransaction exDmTransaction=new ExDmTransaction();
//										    	   exDmTransaction.setAccountId(exDigitalmoneyAccount.getId());
//										    	   exDmTransaction.setCoinCode(userCode);
//										    	   exDmTransaction.setCreated(new Date());
//										    	   exDmTransaction.setCurrencyType(currencyType);
//										    	   exDmTransaction.setCustomerId(customer.getId());
//										    	   exDmTransaction.setCustomerName(customer.getUserName());
//										    	   exDmTransaction.setTime(time);
//										    	   exDmTransaction.setTimereceived(timereceived);
//										    	   exDmTransaction.setInAddress(address);
//										    	   exDmTransaction.setConfirmations(confirmations);
//										    	   exDmTransaction.setBlocktime(blocktime);
//										    	   exDmTransaction.setFee(new BigDecimal(0));
//										    	   exDmTransaction.setTransactionMoney(new BigDecimal(amount));
//										    	   exDmTransaction.setTrueName(exDigitalmoneyAccount.getTrueName());
//										    	   exDmTransaction.setStatus(1);
//										    	   exDmTransaction.setOrderNo(txid);
//										    	   exDmTransaction.setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
//										    	  //充币
//										    	   exDmTransaction.setTransactionType(1);
//										    	   exDmTransaction.setUserId(customer.getId());
//										    	   exDmTransaction.setWebsite(webSite);
//										    	
//										    	   exDmTransactionService.save(exDmTransaction);
//										    	  
//										    	   long num=Long.valueOf(confirmations);
//										    	   if(num>=2){ 
//										    		  
//										    		   QueryFilter fil = new QueryFilter(ExDmTransaction.class);
//										    		   fil.addFilter("orderNo=", txid);
//										    		  
//										    		   ExDmTransaction   tran=exDmTransactionService.get(fil);
//										    		   ExAmineOrderService examineOrderService = (ExAmineOrderService) ContextUtil.getBean("examineOrderService");
//					
//										    		   String s = examineOrderService.pasePutOrder(tran.getId());
// 										    	   }
//								    		   }
//								    	  }
//							         }
//							     }
//			    			}
//			    	   }
//			    }else{
//			    	LogFactory.info("用户的虚拟账户为空   ");
//			    }
//			    	
//			    	
////			    	}else{
////			    		LogFactory.info("不是此用户=====  ");
////			    	}
//			    		
//			    	
//			}
//				
//			    return map;
//		
//				} catch (Exception e) {
//					return null ;
//				}
  }


  public static void main(String[] args) {
    String time = "1474183126";
    String res;
    res = DateUtil.stampToDate(time + "000");

    System.out.println(res);

  }


  //这个方式暂时不用
  @Override
  public Map<String, String> recordAllWithdraw() {
    return null;
  }


  /**
   * 撤销成功记录
   */
  @Override
  public synchronized JsonResult cancelTransaction(Long id) {
    JsonResult jsonResult = new JsonResult();
    jsonResult.setSuccess(false);

    ExDmTransaction dmTransaction = super.get(id);
    if (dmTransaction.getStatus() == 2) {

      String code = dmTransaction.getCoinCode();
      BigDecimal money = dmTransaction.getTransactionMoney();
      BigDecimal accountMoney = BigDecimal.ZERO;
      AppOurAccount appOurAccount;

      Long customerId = dmTransaction.getCustomerId();
      ExDigitalmoneyAccount list = exDigitalmoneyAccountService
          .getByCustomerIdAndType(customerId, code, dmTransaction.getCurrencyType(),
              dmTransaction.getWebsite());

      RemoteAppOurAccountService remoteAppOurAccountService = (RemoteAppOurAccountService) ContextUtil
          .getBean("remoteAppOurAccountService");
      AppOurAccount appOurAccount2 = remoteAppOurAccountService
          .findAppOurAccount(dmTransaction.getWebsite(), code, 1);

      if (null != appOurAccount2) {
        appOurAccount = appOurAccount2;
        accountMoney = appOurAccount.getAccountMoney();
        if (accountMoney.compareTo(money) < 0) {
          jsonResult.setMsg("我方账户余额不足");
          return jsonResult;
        }

      } else {
        jsonResult.setMsg("我方账户不能为空");
        return jsonResult;
      }

      if (null != list) {
        ExDigitalmoneyAccount digitalmoneyAccount = list;

        BigDecimal hotMoney = digitalmoneyAccount.getHotMoney();

        if (hotMoney.compareTo(money) >= 0) {

          // 我方账户  减少一笔钱
          remoteAppOurAccountService
              .changeBitForOurAccount(dmTransaction.getTransactionMoney(), appOurAccount);
          // 给自己的钱包减一笔钱
          exDigitalmoneyAccountService
              .payFromHotAccount(digitalmoneyAccount.getId(), dmTransaction.getTransactionMoney(),
                  dmTransaction.getTransactionNum(), "成功驳回订单", null, null);
          //修改订单状态
          dmTransaction.setStatus(3);

          super.update(dmTransaction);
          jsonResult.setSuccess(true);
          jsonResult.setMsg("驳回成功  请手动对这个用户转币操作");
          return jsonResult;

//					String rechargeResult=CoinInterfaceUtil.sendTo(appOurAccount.getAccountNumber(),dmTransaction.getInAddress(), dmTransaction.getTransactionMoney().toString(), dmTransaction.getCoinCode());
//					Map<String, Object> map = StringUtil.str2map(rechargeResult);
//						String msg=map.get("msg").toString();
//						String codes=map.get("code").toString();
//						if(codes.equals("8")){
//							// 从我方账户减一笔钱到用户账户
//							jsonResult.setMsg("撤销成功");
//							jsonResult.setSuccess(true);
//							return jsonResult;
//						}

        } else {
          jsonResult.setMsg("用户账户可用币不足！");
        }
        return jsonResult;
      } else {
        jsonResult.setMsg("用户币账户为空");
      }
      return jsonResult;
    }
    jsonResult.setMsg("此订单已驳回了 ");
    return jsonResult;

  }

  /**
   *
   * @param params
   * @return
   */
  public FrontPage findExdmtransaction(Map<String, String> params) {
    Page page = PageFactory.getPage(params);
    List<ExDmTransactionManage> list = ((ExDmTransactionDao) dao).findExdmtransaction(params);
    if (list == null) {
      return new FrontPage(list, 0, 0, 0);
    }
    return new FrontPage(list, page.getTotal(), page.getPages(), page.getPageSize());
  }

  @Override
  public boolean sendOurCustomer(ExDmTransaction exDmTransaction,
      ExDigitalmoneyAccount exDigitalmoneyAccount) {
    //插入校验表
    UniqueRecord uniqueRecord = new UniqueRecord();
    uniqueRecord.setRemark("内部转币订单");
    uniqueRecord.setId("内部转币订单:" + exDmTransaction.getTransactionNum());
    uniqueRecord.setCreated(new Date());
    Boolean checkUnique = uniqueRecordService.add(uniqueRecord);
    if (checkUnique) {
      //充币
      AppCustomer appCustomer = appCustomerService.get(exDigitalmoneyAccount.getCustomerId());
      ExDmTransaction ex = new ExDmTransaction();
      ex.setCustomerId(exDigitalmoneyAccount.getCustomerId());
      ex.setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
      ex.setAccountId(exDigitalmoneyAccount.getId());
      ex.setTransactionType(1);
      ex.setTransactionMoney(
          exDmTransaction.getTransactionMoney().subtract(exDmTransaction.getFee()));
      ex.setCustomerName(exDigitalmoneyAccount.getUserName());
      ex.setTrueName(exDigitalmoneyAccount.getTrueName());
      ex.setSurname(exDigitalmoneyAccount.getSurname());
      ex.setStatus(2);
      ex.setSaasId(RpcContext.getContext().getAttachment(
          "saasId"));
      ex.setCoinCode(exDigitalmoneyAccount.getCoinCode());
      ex.setCurrencyType("CNY");
      ex.setFee(new BigDecimal(0));
      ex.setInAddress(exDigitalmoneyAccount.getPublicKey());
      ex.setOrderNo(exDmTransaction.getTransactionNum());
      ex.setRemark("内部互转");
      // 保存订单
      super.save(ex);

      //热账户增加
      Accountadd accountadd3 = new Accountadd();
      accountadd3.setAccountId(ex.getAccountId());
      accountadd3.setMoney(ex.getTransactionMoney());
      accountadd3.setMonteyType(1);
      accountadd3.setAcccountType(1);
      accountadd3.setRemarks(31);
      accountadd3.setTransactionNum(ex.getTransactionNum());

      List<Accountadd> list2 = new ArrayList<Accountadd>();
      list2.add(accountadd3);
      messageProducer.toAccount(com.alibaba.fastjson.JSON.toJSONString(list2));

      //----提币
      //冷账户减少
      Accountadd accountadd2 = new Accountadd();
      accountadd2.setAccountId(exDmTransaction.getAccountId());
      accountadd2.setMoney(exDmTransaction.getTransactionMoney().subtract(exDmTransaction.getFee())
          .multiply(new BigDecimal(-1)));
      accountadd2.setMonteyType(2);
      accountadd2.setAcccountType(1);
      accountadd2.setRemarks(33);
      accountadd2.setTransactionNum(exDmTransaction.getTransactionNum());

      //手续费 -- 冷
      Accountadd accountaddf1 = new Accountadd();
      accountaddf1.setAccountId(exDmTransaction.getAccountId());
      accountaddf1.setMoney(exDmTransaction.getFee().multiply(new BigDecimal(-1)));
      accountaddf1.setMonteyType(2);
      accountaddf1.setAcccountType(1);
      accountaddf1.setRemarks(34);
      accountaddf1.setTransactionNum(exDmTransaction.getTransactionNum());

      List<Accountadd> list = new ArrayList<Accountadd>();
      list.add(accountadd2);
      list.add(accountaddf1);

      // 修改订单
      exDmTransaction.setStatus(2);
      super.update(exDmTransaction);
      RemoteAppOurAccountService remoteAppOurAccountService = (RemoteAppOurAccountService) ContextUtil
          .getBean("remoteAppOurAccountService");
      //我方提币账户
      AppOurAccount ourAccount = remoteAppOurAccountService
          .findAppOurAccount(ContextUtil.getWebsite(), exDmTransaction.getCoinCode(),
              Integer.valueOf("1"));
      remoteAppOurAccountService
          .changeCountToOurAccoun(ourAccount, exDmTransaction, exDmTransaction.getOutAddress(),
              "提币记录", "");
      remoteAppOurAccountService
          .changeCountToOurAccoun(ourAccount, exDmTransaction, exDmTransaction.getOutAddress(),
              "提币手续费记录", "fee");

      exDmTransaction.setStatus(2);
      super.update(exDmTransaction);
      //----发送mq消息----end
      messageProducer.toAccount(com.alibaba.fastjson.JSON.toJSONString(list));
      return true;
    }
    return false;
  }


}




