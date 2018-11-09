/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年7月5日 上午10:42:10
 */
package com.mz.calculate.mvc;

import com.alibaba.fastjson.JSON;
import com.mz.account.fund.model.AppOurAccount;
import com.mz.account.fund.model.AppPlatformSettlementRecord;
import com.mz.account.remote.RemoteAppAccountSureoldService;
import com.mz.calculate.mvc.dao.AppReportSettlementDao;
import com.mz.calculate.mvc.po.OperationAccountFundInfoLog;
import com.mz.calculate.settlement.model.AppReportSettlement;
import com.mz.customer.user.model.AppCustomer;
import com.mz.exchange.product.model.ExCointoCoin;
import com.mz.exchange.product.model.ExProduct;
import com.mz.redis.common.utils.RedisService;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.util.QueryFilter;
import com.mz.util.RemoteQueryFilter;
import com.mz.util.date.DateUtil;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.serialize.Mapper;
import com.mz.util.sys.ContextUtil;
import com.mz.account.fund.service.AppOurAccountService;
import com.mz.account.fund.service.AppPlatformSettlementRecordService;
import com.mz.calculate.mvc.service.AppReportSettlementCulService;
import com.mz.calculate.mvc.service.AppReportSettlementService;
import com.mz.calculate.mvc.service.AppReportSettlementcoinService;
import com.mz.change.remote.exEntrust.RemoteExEntrustService;
import com.mz.change.remote.exEntrust.RemoteExExOrderService;
import com.mz.change.remote.lend.RemoteExDmLendService;
import com.mz.core.constant.StringConstant;
import com.mz.customer.remote.RemoteAppCustomerService;
import com.mz.exchange.product.service.ExCointoCoinService;
import com.mz.exchange.remote.account.RemoteExProductService;
import com.mz.trade.entrust.ExchangeDataCache;
import com.mz.trade.entrust.service.ExEntrustService;
import com.mz.trade.entrust.service.ExOrderInfoService;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


/**
 * <p> TODO</p>
 * @author:         Wu Shuiming
 * @Date :          2016年7月5日 上午10:42:10 
 */
@Service("appReportSettlementNoTrService")
public class AppReportSettlementNorTrService    {

	
	@Resource(name = "remoteAppCustomerService")
	public RemoteAppCustomerService appCustomerService;
	
	@Resource(name = "appReportSettlementcoinService")
	public AppReportSettlementcoinService appReportSettlementcoinService;
	@Resource
	public RemoteExEntrustService remoteExEntrustService;
	@Resource
	public RemoteExDmLendService remoteExDmLendService;
	@Resource
	private  RedisService redisService;
	@Resource
	public RemoteAppAccountSureoldService remoteAppAccountSureoldService;
	@Resource
	public AppReportSettlementCulService appReportSettlementCulService;
	@Resource
	public AppReportSettlementService appReportSettlementService;
	@Resource
	public AppReportSettlementDao appReportSettlementDao;
	@Resource
	public RemoteExExOrderService remoteExExOrderService;
	@Resource
	public ExCointoCoinService exCointoCoinService;
	@Resource
	public ExEntrustService  exEntrustService;
	public String productListsb(String website){
		
		String productListStr = ExchangeDataCache.getStringData(website+":productList");
	    List<String> productList = JSON.parseArray(productListStr,String.class);
	    StringBuffer  productListsb=new StringBuffer();
	    if(null!=productList&&productList.size()>0){
	    	int a=0;
	    	while(a<productList.size()){
	    		productListsb.append(productList.get(a));
                 if(a<productList.size()-1){
                	 productListsb.append(",");
	    		}
	    		a++;
	    	}
	    	}
	    return productListsb.toString();
	}

	/**
	 * 闭市所有用户生成结算单
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    
	 * @return: void 
	 * @Date :          2017年4月7日 下午5:04:40   
	 * @throws:
	 */
	public void settlement() {
		RemoteQueryFilter filter=new RemoteQueryFilter(AppCustomer.class);
		filter.addFilter("isDelete=", 0);
		filter.addFilter("isReal=", 1);
		List<AppCustomer> list=appCustomerService.find(filter);
		System.out.println("开始生成结算单啦");
	
		
		Date comeDate=new Date();
	    Map<String,String> mapLoadWeb=PropertiesUtils.getLoadWeb();
		for (String website : mapLoadWeb.keySet()) {
		    String currencyType=mapLoadWeb.get(website);

		    for(AppCustomer customer:list){
		    	Date nowDate=new Date();
		    	AppReportSettlement last=getLastSettlementByCustomerId(customer.getId(),currencyType,website);
		    	Date endDate=null;
				if(null==last){
					endDate=DateUtil.addDay(nowDate, -10);
				}else{
					endDate=last.getEndSettleDate();
				}
				appReportSettlementService.settlementByCustomerId(customer,comeDate, nowDate, endDate, currencyType, website);
		    	
		    } 
		}
		
		System.out.println("结算生成结算单啦");
	}
    

	/**
	 * 获取用户的最后一条结算单对象
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param customerId
	 * @param:    @param currencyType
	 * @param:    @param website
	 * @param:    @return
	 * @return: AppReportSettlement 
	 * @Date :          2017年4月7日 下午5:05:09   
	 * @throws:
	 */
	public AppReportSettlement getLastSettlementByCustomerId(Long customerId,
			String currencyType, String website) {
		List<AppReportSettlement> list=appReportSettlementDao.getEndDate(customerId,currencyType,website);
		if(null!=null || list.size()!=0){
			return list.get(0);
		}else{
			return null;
		}
		
	}
	//最后一次收盘之后
	public void closePlateDeal(){
		//1，计算均价   (改为凌晨1点的定时任务  2017/04/19)
		averagePrice();
		//对所有可撤（未成和部成）的委托全部撤单（此时所有的冻结数据都归零）
	//	remoteExEntrustService.cancelAllExEntrust();
		//数据库用“沉淀”的成交明细刷新一次。
		
		//清除一个月以前得mongodb的日志数据
//		deleteLogAmonthAgo();
		
		//闭盘计算当日的收入总额和手续费收入总额
		calculationTodayMoney();
	}
	
	/**
	 * 闭盘计算上个结算时间到现在的收入总额和手续费收入总额
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    
	 * @return: void 
	 * @Date :          2017年4月7日 下午1:50:14   
	 * @throws:
	 */
	private void calculationTodayMoney() {
		System.out.println("闭盘计算上个结算时间到现在的收入总额和手续费收入总额");
		AppOurAccountService accountService=(AppOurAccountService) ContextUtil.getBean("appOurAccountService");
		AppPlatformSettlementRecordService recordService=(AppPlatformSettlementRecordService) ContextUtil.getBean("appPlatformSettlementRecordService");
		
		//需要查询,获取主账户
		AppOurAccount account=accountService.getAppOurAccount("cn", "cny", 0);
		if(account==null){
			System.out.println("未查询到主资金账户！!!!!!!!");
		}else{
			//先获取开始结束时间，查到上次结算数据时就取上次结算的时间
			//没查到就取两天前
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date nowDate=new Date();
			String beginDate="";
			String endDate=format.format(nowDate);
			QueryFilter qf=new QueryFilter(AppPlatformSettlementRecord.class);
			qf.setOrderby("created desc");
			AppPlatformSettlementRecord record=recordService.get(qf);
			if(record==null){
				System.out.println("未查询到平台结算对象，日期取两天前就行");
				beginDate=getAgoTwoDayString();
			}else{
				System.out.println("查询到了上次平台结算对象，日期为："+record.getSettlementDay());
				beginDate=format.format(record.getSettlementDay());//上次计算时间
			}
			//计算当日的收入总额，充值总金额-提现总金额
			//充值总金额
			BigDecimal rechargeMoney=recordService.getRechargeMoney(beginDate,endDate);
			//提现总金额
			BigDecimal withdrawalsMoney=recordService.getWithdrawalsMoney(beginDate,endDate);
			//计算当日的手续费收入总额，交易买手续费+交易卖手续费+充值手续费+提现手续费
			//交易买手续费+交易卖手续费
			BigDecimal tradeFeeMoney=recordService.getTradeFeeMoney(beginDate,endDate);
			//充值手续费+提现手续费
			BigDecimal tranFeeMoney=recordService.getTranFeeMoney(beginDate,endDate);
			System.out.println(endDate+"结算，查询到的上次结算数据时就取上次结算的时间的，充值总金额："+rechargeMoney
					+",提现总金额:"+withdrawalsMoney+",交易买卖手续费总金额:"+tradeFeeMoney+",充值提现手续费总金额:"+tradeFeeMoney);
			
			//保存结算记录信息
			AppPlatformSettlementRecord settlementRecord=new AppPlatformSettlementRecord();
			settlementRecord.setSettlementDay(nowDate);
			settlementRecord.setAccountMoneyNewBefore(account.getAccountMoneyNew());//结算前
			if(rechargeMoney!=null&&!"".equals(rechargeMoney)&&withdrawalsMoney!=null&&!"".equals(withdrawalsMoney)&&tradeFeeMoney!=null&&!"".equals(tradeFeeMoney)){
			settlementRecord.setAccountMoneyNewAfter(account.getAccountMoneyNew().add(rechargeMoney).subtract(withdrawalsMoney));//结算后
			settlementRecord.setRechargeMoneyOneSettlement(rechargeMoney);
			
			settlementRecord.setWithdrawalsMoneyOneSettlement(withdrawalsMoney);
			settlementRecord.setTransactionFeeOneSettlement(tradeFeeMoney);
			settlementRecord.setRechargeOrWithdrawalsFeeOneSettlement(tranFeeMoney);
			recordService.save(settlementRecord);
			
			//查完以后需要保存，更新账户总额，此次结算新增账户额（充值-提现），新增手续费额
			account.setAccountMoneyNew(account.getAccountMoneyNew().add(rechargeMoney).subtract(withdrawalsMoney));
			account.setTodayAddedMoney(rechargeMoney.subtract(withdrawalsMoney));
			account.setTodayAddedFee(tradeFeeMoney.add(tranFeeMoney));
			account.setAccountFee(account.getAccountFee().add(tradeFeeMoney.add(tranFeeMoney)));
			accountService.update(account);
			}
		}
	}
	
	
	/**
	 * 清除一个月以前得mongodb的日志数据
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    
	 * @return: void 
	 * @Date :          2017年4月6日 上午11:34:40   
	 * @throws:
	 */
	private void deleteLogAmonthAgo() {
		System.out.println("此方法已暂时去掉，目前无效");
		/*
		System.out.println("闭市删除一个月前日志deleteLogAmonthAgo");
		String lastAmonthDay=getLastAmonthDayString();
		Date lastAmonth=getLastAmonthDay();
		//删除一个月前登陆日志
		MongoUtil<AppLogLogin, Long> mongoUtil = new MongoUtil<AppLogLogin, Long>(AppLogLogin.class);
		MongoQueryFilter mongoQueryFilter = new MongoQueryFilter();
		mongoQueryFilter.addFilter("loginTime_LT", lastAmonthDay);
		List<AppLogLogin> loginLogList=mongoUtil.find(mongoQueryFilter);
		for (AppLogLogin appLogLogin : loginLogList) {
			mongoUtil.deleteObject(appLogLogin);
		}
		System.out.println("删除"+lastAmonthDay+"前登录日志【"+loginLogList==null?0:loginLogList.size()+"】条");
		
		//删除一个月前异常日志
		MongoUtil<AppException, Long> mongUtil1 = new MongoUtil<AppException, Long>(AppException.class);
    	MongoQueryFilter mongoQueryFilter1 = new MongoQueryFilter();
    	mongoQueryFilter1.setOrderby("created", "asc");
		List<AppException> exceptionList=mongUtil1.find(mongoQueryFilter1);
		List<AppException> exceptionList2=exceptionList;
		int num=0;
		for (AppException appException : exceptionList2) {
			Date date=appException.getCreated();
			if (date.getTime()>lastAmonth.getTime()) {
				num++;
				mongUtil1.deleteObject(appException);
			}else{
				break;
			}
		}
		System.out.println("删除"+lastAmonthDay+"前异常日志【"+num+"】条");
		
		
		//删除一个月前操作日志
		MongoUtil<AppLog, Long> mongoUtil2 = new MongoUtil<AppLog, Long>(AppLog.class);
    	MongoQueryFilter mongoQueryFilter2 = new MongoQueryFilter();
    	mongoQueryFilter2.addFilter("systemTime_LT", lastAmonthDay);
    	List<AppLog> controllerLogList=mongoUtil2.find(mongoQueryFilter2);
    	for (AppLog appLog : controllerLogList) {
    		mongoUtil2.deleteObject(appLog);
		}
		System.out.println("删除"+lastAmonthDay+"前操作日志【"+controllerLogList==null?0:controllerLogList.size()+"】条");
	*/}
	
	
	/**
	 * 获取两天前的时间
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param s
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年4月6日 下午5:18:44   
	 * @throws:
	 */
    public static String getAgoTwoDayString(){
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Calendar c = Calendar.getInstance();
        //前两天
        c.setTime(new Date());
        c.add(Calendar.DATE, -2);
        Date m = c.getTime();
        String mon = format.format(m);
        return mon;
    }
    /**
     * 
     * <p> TODO</p>
     * @author:         Zhang Lei
     * @param:    @param s
     * @param:    @return
     * @return: String 
     * @Date :          2017年4月6日 下午5:18:44   
     * @throws:
     */
    public static String getLastAmonthDayString(){
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Calendar c = Calendar.getInstance();
    	//过去一月
    	c.setTime(new Date());
    	c.add(Calendar.MONTH, -1);
    	Date m = c.getTime();
    	String mon = format.format(m);
    	return mon;
    }
    /**
     * 一个月前的日期
     * <p> TODO</p>
     * @author:         Zhang Lei
     * @param:    @param s
     * @param:    @return
     * @return: String 
     * @Date :          2017年4月6日 下午5:18:44   
     * @throws:
     */
    public static Date getLastAmonthDay(){
    	Calendar c = Calendar.getInstance();
    	//过去一月
    	c.setTime(new Date());
    	c.add(Calendar.MONTH, -1);
    	Date m = c.getTime();
    	return m;
    }
	
	/**
	 * <p> TODO</p>
	 * @author:         Zeng Hao
	 * 					金科取最后一笔成交价为明日开盘价
	 * 					存入币种的priceLimits字段中
	 * @param:    
	 * @return: void 
	 * @Date :          2016年12月1日 下午3:58:22   
	 * @throws:
	 */
	public void averagePrice(){
		System.out.println("取最后一笔成交价为明日开盘价存入币种的AveragePrice字段中");
		List<ExCointoCoin> listExCointoCoin=exCointoCoinService.getBylist(null,null,1);
				for (ExCointoCoin exCointoCoin : listExCointoCoin) {
					  String header =exEntrustService.getHeader("cn", "cny", exCointoCoin.getCoinCode(), exCointoCoin.getFixPriceCoinCode());
					  String currentExchangPrice=ExchangeDataCache.getStringData(header+":"+ExchangeDataCache.CurrentExchangPrice);
					 if(null!=currentExchangPrice){
						 BigDecimal currentExchangPricebig=new BigDecimal(currentExchangPrice);
						 exCointoCoin.setAveragePrice(currentExchangPricebig);
						 exCointoCoinService.update(exCointoCoin);
					   	//更新开盘价
						System.out.println("更新开盘价");
						ExchangeDataCache.setStringData(header+":"+ExchangeDataCache.OpenedExchangPrice,currentExchangPricebig.toString());
	
						 }
				}
		}
		
		
		
	public void closePlateDealExEntrust() {
		closePlateDeal();
		//settlement();
	}
	
	
	
	//=======================金科币凌晨1点定时计算================================================================
	/**
	 * 自动计算最后一笔成交价和开盘价
	 */
	public void autoCalculationOpenprice() {
		LogFactory.info("金科系统改造，非标准版");
		System.out.println("定时任务自动计算最后一笔成交价和开盘价："+new Date());
		String productListStr = ExchangeDataCache.getStringData("cn:productList");
		if (!StringUtils.isEmpty(productListStr)) {
			System.out.println("定时任务产品的str:"+productListStr);
				List<String> productList = JSON.parseArray(productListStr, String.class);
				for (String coinCode : productList) {
					ExOrderInfoService exOrderService = (ExOrderInfoService) ContextUtil.getBean("exOrderInfoService");
					ExOrderInfo exOrderInfo =  exOrderService.getAveragePriceYesterday(coinCode);
					
					System.out.println("定时任务币的种类："+coinCode+",查询到的昨天最后一笔流水为："+exOrderInfo);
					//保存成交价
					if(null!=exOrderInfo&&null!=exOrderInfo.getTransactionPrice()){
						RemoteExProductService remoteExProductService = (RemoteExProductService) ContextUtil.getBean("remoteExProductService");
						ExProduct prodect=remoteExProductService.findByCoinCode(coinCode, null);
						
						if(null!=exOrderInfo&&null!=exOrderInfo.getTransactionPrice()){
							 List<ExCointoCoin> list=exCointoCoinService.getBylist(exOrderInfo.getCoinCode(), exOrderInfo.getFixPriceCoinCode(),null);
							if(null!=list&list.size()>0){
								ExCointoCoin exCointoCoin=list.get(0);
								 exCointoCoin.setAveragePrice(exOrderInfo.getTransactionPrice());
								 exCointoCoinService.update(exCointoCoin);
							}
						}
						//更新开盘价
						System.out.println("定时任务更新开盘价");
						ExchangeDataCache.setStringData("cn:cny:"+coinCode+":openedExchangPrice", exOrderInfo.getTransactionPrice().setScale(5, BigDecimal.ROUND_HALF_UP).toString());
					}
			}
		}
	}
	
	//=======================start核算====================
	
	public List<Map<String,Object>>  culSureOldAccountAllCustomerErrorInfo(Integer days) {
		long start=System.currentTimeMillis();
		System.out.println(start);
		/*MongoTemplate mongoTemplate = (MongoTemplate) ContextUtil.getBean("mongoTemplate");
		mongoTemplate.dropCollection("user_fund_check_all");*/
		Map<String,Object> mapp =new HashMap<String,Object>();
		mapp.put("endTime", DateUtil.dateToString(new Date(), StringConstant.DATE_FORMAT_FULL));
		if(days==null){
			mapp.put("beginTime", "2016-01-01 18:24:48");
		}else{
			mapp.put("beginTime", DateUtil.dateToString(DateUtil.addDay(new Date(),(0-days)), StringConstant.DATE_FORMAT_FULL));
		}
		
		List<AppCustomer> list=appCustomerService.getFundChangeCustomers(mapp);
		System.out.println("计算条数=="+(null!=list?list.size():"0"));
	    Map<String,String> mapLoadWeb=PropertiesUtils.getLoadWeb();
	    List<Map<String,Object>>  listErrorInfo=new ArrayList<Map<String,Object>>();
	    int i=1;
	

		    for(AppCustomer l:list){
		    	System.out.println("到第"+i+"条了");
		    	i++;
		    	long start1=System.currentTimeMillis();
		    	Map<String,Object> map=appReportSettlementCulService.culAccountByCustomer(l.getId(),null, null,false,false); 
		    	long start2=System.currentTimeMillis();
		    	System.out.println("一人耗时=="+(start2-start1));
		    	if(null!=map){
		    		map.put("customerId", l.getId());
		    		map.put("createTime", new Date());
		    		listErrorInfo.add(map);
		    	}
		    	
		     
		}
		
		long end=System.currentTimeMillis();
		System.out.println(end);
		System.out.println("余额核算时间=="+(end-start));
		//List<String> list1=new ArrayList<String>();
		redisService.setList("user_fund_check_all", listErrorInfo);
	//	mongoTemplate.insert(listErrorInfo, "user_fund_check_all");
		
		
	
		return listErrorInfo;
	}	
	public void culAccountAllCustomer() {
		List<Map<String,Object>>  listErrorInfo=new ArrayList<Map<String,Object>>();
		long start=System.currentTimeMillis();
		System.out.println(start);
		List<AppCustomer> list=new ArrayList<AppCustomer>();
		List<Map<String,Object>> listcheckall=redisService.getList1("user_fund_check_all");
	    for(Map<String,Object> a:listcheckall){
	    	Long customerId=(Long) a.get("customerId");
	    	AppCustomer appCustomer =new AppCustomer();
	    	appCustomer.setId(customerId);
	    	list.add(appCustomer);
	    }
	    System.out.println("核算到数据库一共条数list.size()=="+list.size());
	    for(AppCustomer l:list){
	    	 Map<String,Object> map=appReportSettlementCulService.culAccountByCustomer(l.getId(),null, null,true,false);
	    	 if(null!=map){
		    		listErrorInfo.add(map);
		    	}
	    } 
		if(listErrorInfo.size()>0){
			//保存操作日志
			OperationAccountFundInfoLog operationAccountFundInfoLog=new OperationAccountFundInfoLog();
			operationAccountFundInfoLog.setWebsite("cn");
			operationAccountFundInfoLog.setCurrencyType("cny");
			operationAccountFundInfoLog.setOperatorName(ContextUtil.getCurrentUser().getUsername());
			operationAccountFundInfoLog.setUserName("all");
			operationAccountFundInfoLog.setContext(Mapper.objectToJson(listErrorInfo));
			operationAccountFundInfoLog.setCreatDate(new Date());
			List<OperationAccountFundInfoLog> operationAccountFundInfoLoglist = JSON.parseArray(redisService.get("operation_accountfundinfo_log"), OperationAccountFundInfoLog.class);
			if (operationAccountFundInfoLoglist == null) {
				operationAccountFundInfoLoglist = new ArrayList<OperationAccountFundInfoLog>();
			}
			operationAccountFundInfoLoglist.add(0, operationAccountFundInfoLog);
			redisService.save("operation_accountfundinfo_log", JSON.toJSONString(operationAccountFundInfoLoglist));
		}
		
		long end=System.currentTimeMillis();
		System.out.println("全部余额核算到数据库耗时："+(end-start));
}
	//========================end核算=======================
	/*public List<Map<String,Object>>  culSureOldAccountAllCustomerErrorInfo(Integer days) {
	long start=System.currentTimeMillis();
	System.out.println(start);
	MongoTemplate mongoTemplate = (MongoTemplate) ContextUtil.getBean("mongoTemplate");
	mongoTemplate.dropCollection("user_fund_check_all");
	Map<String,Object> mapp =new HashMap<String,Object>();
	mapp.put("endTime", DateUtil.dateToString(new Date(), StringConstant.DATE_FORMAT_FULL));
	if(days==null){
		mapp.put("beginTime", "2016-01-01 18:24:48");
	}else{
		mapp.put("beginTime", DateUtil.dateToString(DateUtil.addDay(new Date(),(0-days)), StringConstant.DATE_FORMAT_FULL));
	}
	
	List<AppCustomer> list=appCustomerService.getFundChangeCustomers(mapp);
	System.out.println("计算条数=="+(null!=list?list.size():"0"));
    Map<String,String> mapLoadWeb=PropertiesUtils.getLoadWeb();
    List<Map<String,Object>>  listErrorInfo=new ArrayList<Map<String,Object>>();
	for (String website : mapLoadWeb.keySet()) {
	    String currencyType=mapLoadWeb.get(website);

	    for(AppCustomer l:list){
	    	Map<String,Object> map=appReportSettlementCulService.culAccountByCustomer(l.getId(),currencyType, website,false); 
	    	if(null!=map){
	    		map.put("customerId", l.getId());
	    		map.put("createTime", new Date());
	    		listErrorInfo.add(map);
	    	}
	    	
	     } 
	}
	
	long end=System.currentTimeMillis();
	System.out.println(end);
	System.out.println("余额核算时间=="+(end-start));
	mongoTemplate.insert(listErrorInfo, "user_fund_check_all");
	return listErrorInfo;
}	*/

/*public void culAccountAllCustomer() {
	List<Map<String,Object>>  listErrorInfo=new ArrayList<Map<String,Object>>();
	long start=System.currentTimeMillis();
	System.out.println(start);
	RemoteQueryFilter filter=new RemoteQueryFilter(AppCustomer.class);
	filter.addFilter("isDelete=", 0);
	filter.addFilter("isReal=", 1);
	List<AppCustomer> list=appCustomerService.find(filter);
	MongoTemplate mongoTemplate = (MongoTemplate) ContextUtil.getBean("mongoTemplate");
    DBCollection course = mongoTemplate.getDb().getCollection("user_fund_check_all");
    DBObject query1 = new BasicDBObject();  
    DBCursor cursor = course.find(query1);
    List<AppCustomer> list=new ArrayList<AppCustomer>();
    while(cursor.hasNext()) {
    	   DBObject document = cursor.next(); 
    	   System.out.println(document.toString());
    	//   Map<String,List<AccountFundInfo>> map= (Map<String,List<AccountFundInfo>>) document;
    	   AppCustomer appCustomer=new AppCustomer();
    	   appCustomer.setId(Long.valueOf(document.get("customerId").toString()));
    	   list.add(appCustomer);
    }
    Map<String,String> mapLoadWeb=PropertiesUtils.getLoadWeb();
	for (String website : mapLoadWeb.keySet()) {
	    String currencyType=mapLoadWeb.get(website);

	    for(AppCustomer l:list){
	    	 Map<String,Object> map=appReportSettlementCulService.culAccountByCustomer(l.getId(),currencyType, website,true);
	    	 if(null!=map){
		    		listErrorInfo.add(map);
		    	}
	    } 
	}
	
	//保存操作日志
	MongoUtil<OperationAccountFundInfoLog, Long> mongoUtil = new MongoUtil<OperationAccountFundInfoLog, Long>(
			OperationAccountFundInfoLog.class,"operation_accountfundinfo_log");
	OperationAccountFundInfoLog operationAccountFundInfoLog=new OperationAccountFundInfoLog();
	operationAccountFundInfoLog.setWebsite("cn");
	operationAccountFundInfoLog.setCurrencyType("cny");
	operationAccountFundInfoLog.setOperatorName(ContextUtil.getCurrentUser().getUsername());
	operationAccountFundInfoLog.setUserName("all");
	operationAccountFundInfoLog.setContext(Mapper.objectToJson(listErrorInfo));
	operationAccountFundInfoLog.setCreatDate(new Date());
	mongoUtil.save(operationAccountFundInfoLog);
	long end=System.currentTimeMillis();
	System.out.println(end);
}*/


}
