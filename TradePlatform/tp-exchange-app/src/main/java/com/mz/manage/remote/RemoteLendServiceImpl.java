package com.mz.manage.remote;

import com.github.pagehelper.Page;
import com.mz.core.mvc.model.page.PageFactory;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.exchange.lend.model.ExDmLend;
import com.mz.exchange.lend.model.ExDmLendIntent;
import com.mz.exchange.lend.model.ExDmLendTimes;
import com.mz.exchange.product.model.ExProduct;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.core.constant.CodeConstant;
import com.mz.customer.person.service.AppPersonInfoService;
import com.mz.exchange.lend.service.ExDmLendIntentService;
import com.mz.exchange.lend.service.ExDmLendService;
import com.mz.exchange.lend.service.ExDmLendTimesService;
import com.mz.exchange.lend.service.ExDmPingService;
import com.mz.exchange.lend.service.LendCoinAccountService;
import com.mz.exchange.product.service.ExCointoCoinService;
import com.mz.exchange.product.service.ExProductService;
import com.mz.front.redis.model.UserRedis;
import com.mz.manage.remote.model.Lend;
import com.mz.manage.remote.model.LendCanCoinCode;
import com.mz.manage.remote.model.LendIntent;
import com.mz.manage.remote.model.LendTimes;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.trade.redis.model.AppAccountRedis;
import com.mz.trade.redis.model.ExDigitalmoneyAccountRedis;
import com.mz.trade.redis.model.ExchangeDataCacheRedis;
import com.mz.web.remote.RemoteAppConfigService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import tk.mybatis.mapper.util.StringUtil;
import com.mz.util.UserRedisUtils;

public class RemoteLendServiceImpl implements RemoteLendService {

	@Resource
	private ExDmLendIntentService exDmLendIntentService;
	@Resource
	private ExDmPingService exDmPingService;
	@Resource
	private ExDmLendService exDmLendService;
	@Resource
	private LendCoinAccountService lendCoinAccountService;
	@Resource
	private ExProductService exProductService;
	@Resource
	private AppPersonInfoService appPersonInfoService;
	@Resource
	private ExDmLendTimesService exDmLendTimesService;
	@Resource
	private ExCointoCoinService exCointoCoinService;
	@Override
	public FrontPage findExDmLend(Map<String, String> map) {
		// return exDmLendService.listPage(filter);
		return exDmLendService.findFrontPage(map);
	}

	@Override
	public Map<String, BigDecimal> getLengCenter(Long customerId, String coinCode) {
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		// 净资产
		BigDecimal coinNetAsset = lendCoinAccountService.getCoinNetAsset(customerId,null, coinCode, "cny", "cn");

		// 已借资产

		// 还能借多少

		// 总资产
		map.put("coinNetAsset", coinNetAsset);
		return null;
	}

	public String[] repayPrincipal(Long lendId, BigDecimal repayCount) {
		String[] relt = new String[2];
		ExDmLend exDmLend = exDmLendService.get(lendId);
		exDmLend.setRepayLendCount(exDmLend.getRepayLendCount().add(repayCount));
		if (exDmLend.getLendCount().equals(exDmLend.getRepayLendCount())) {
			exDmLend.setStatus(2);
		} else {
			exDmLend.setStatus(1);
		}
		exDmLendService.update(exDmLend);
		return relt;

	}

	@Override
	public BigDecimal netAsseToLend(Long customerId, String currencyType, String website) {
		// TODO Auto-generated method stub
		return lendCoinAccountService.netAsseToLend(customerId,null, currencyType, website);
	}

	@Override
	public String repaymentInfo(Long lendId) {
		ExDmLend exDmLend = exDmLendService.get(lendId);
		RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
		UserRedis userRedis = redisUtil.get(exDmLend.getCustomerId().toString());
		BigDecimal balance = null;
		String coinCode="";
		if (exDmLend.getLendCoinType().equals("1")) {
			AppAccountRedis account = UserRedisUtils.getAccount(userRedis.getAccountId().toString());
			balance = account.getHotMoney();
			coinCode=getRMBCode();
		} else {
			ExDigitalmoneyAccountRedis exaccount = UserRedisUtils.getAccount(userRedis.getDmAccountId(exDmLend.getLendCoin()).toString(), exDmLend.getLendCoin());
			balance = exaccount.getHotMoney();
			coinCode=exaccount.getCoinCode();
		}
		StringBuffer sb = new StringBuffer("{\"LendCount\":" + exDmLend.getLendCount()); // 借款总金额
		sb.append(",\"notRepayLendCount\":" + exDmLend.getLendCount().subtract(exDmLend.getRepayLendCount()));
		sb.append(",\"balance\":" + balance);
		sb.append(",\"coinCode\":\"" + coinCode+"\"");
		sb.append(",\"notInterest\":" + exDmLend.getInterestCount().subtract(exDmLend.getRepayInterestCount()) + "}");
		return sb.toString();
	}

	@Override
	public String[] repayment(Long id, String type, BigDecimal repaymentMoney) {
		return exDmLendService.repayment(id, type, repaymentMoney);

	}

	@Override
	public FrontPage fildIntentPage(Map<String, String> params) {
		// TODO Auto-generated method stub
		return exDmLendIntentService.listIntentPage(params);
	}

	@Override
	public Boolean isPinging(Long customerId, String userCode, String currencyType, String website) {
		// TODO Auto-generated method stub
		return exDmPingService.isPinging(customerId, userCode, currencyType, website);
	}

	@Override
	public List<Lend> list(Long customerId, String userCode, String currencyType, String website, String lendCoin) {
		/*
		 * QueryFilter filter=new QueryFilter(ExDmLend.class);
		 * if(null!=customerId&&!"".equals(customerId)){
		 * filter.addFilter("customerId=", customerId); }
		 * 
		 * filter.addFilter("currencyType=", currencyType);
		 * filter.addFilter("website=", website);
		 * if(null!=lendCoin&&!"".equals(lendCoin)){
		 * filter.addFilter("lendCoin=", lendCoin); } return
		 * exDmLendService.find(filter);
		 */
		return null;
	}

	@Override
	public List<Lend> find(Map<String, String> map) {
		// return exDmLendService.find(remoteQueryFilter.getQueryFilter());
		return null;
	}

	@Override
	public List<LendIntent> findintent(Map<String, String> map) {
		// return
		// exDmLendIntentService.find(remoteQueryFilter.getQueryFilter());

		return null;

	}

	@Override
	public String[] saveExDmLend(Lend lend) {
		ExDmLend exDmLend = new ExDmLend();
		exDmLend.setCustomerId(lend.getCustomerId());
		exDmLend.setUserCode(lend.getUserCode());
		exDmLend.setUserName(lend.getUserName());
		exDmLend.setLendCount(lend.getLendCount());
		exDmLend.setLendCoin(lend.getLendCoin());
		exDmLend.setLendCoinType(lend.getLendCoinType());
		exDmLend.setCurrencyType("cny");
		exDmLend.setWebsite("cn");
		if(lend.getLendCoinType().equals("1")){
			exDmLend.setLendCoin(getRMBCode());
		}
		return exDmLendService.saveExDmLend(exDmLend);
	}

	public static String getRMBCode() {
		RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String language_code = remoteAppConfigService.getFinanceByKey("language_code");
		return language_code;
	}
	public static String getConfigByKey(String key) {
		RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String language_code = remoteAppConfigService.getFinanceByKey(key);
		return language_code;
	}

    public  Map<String, String> getAllCener(Long customerId,String coinCode,String coinCodeForRmb){
    	RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
    	
		Map<String, String> map = new HashMap<String, String>();
		// 虚拟货币
		
		// 净资产
		BigDecimal coinNetAsset = lendCoinAccountService.getCoinNetAsset(customerId, coinCodeForRmb, coinCode, "cny", "cn");
		// 总负债
		BigDecimal rMBLendMoneyed = lendCoinAccountService.getCodeLendMoneySum(customerId, coinCodeForRmb,coinCode, "cny", "cn");
		// 还能借入
		BigDecimal canLendMoney = lendCoinAccountService.getCanLendMoney(customerId, coinCodeForRmb, coinCode, "cny", "cn");

		// 总资产
		BigDecimal rMBSum = coinNetAsset.add(rMBLendMoneyed);
		int keepDecimalForRmb=lendCoinAccountService.culDecimal(coinCode);
	
		
		if (coinNetAsset.compareTo(new BigDecimal("0")) == 0) {
			map.put("coinNetAsset", coinNetAsset.setScale(keepDecimalForRmb, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros().toPlainString());
		} else {
			map.put("coinNetAsset", coinNetAsset.setScale(keepDecimalForRmb, BigDecimal.ROUND_HALF_DOWN).toString());
		}
		if (rMBLendMoneyed.compareTo(new BigDecimal("0")) == 0) {
			map.put("rMBLendMoneyed", rMBLendMoneyed.setScale(keepDecimalForRmb, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros().toPlainString());
		} else {
			map.put("rMBLendMoneyed", rMBLendMoneyed.setScale(keepDecimalForRmb, BigDecimal.ROUND_HALF_DOWN).toString());
		}
		if (canLendMoney.compareTo(new BigDecimal("0")) == 0) {
			map.put("canLendMoney", canLendMoney.setScale(keepDecimalForRmb, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros().toPlainString());
		} else {
			map.put("canLendMoney", canLendMoney.setScale(keepDecimalForRmb, BigDecimal.ROUND_HALF_DOWN).toString());
		}
		if (rMBSum.compareTo(new BigDecimal("0")) == 0) {
			map.put("rMBSum", rMBSum.setScale(keepDecimalForRmb, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros().toPlainString());
		} else {
			map.put("rMBSum", rMBSum.setScale(keepDecimalForRmb, BigDecimal.ROUND_HALF_DOWN).toString());
		}
		map.put("coinCodeForRmb", coinCodeForRmb);
		BigDecimal netAsseToLend=lendCoinAccountService.netAsseToLend(customerId,coinCodeForRmb, null, null);
		map.put("netAsseToLend", netAsseToLend.toString());
		String lendRates = remoteAppConfigService.getFinanceLendByKey("lendRate");
		map.put("lendRates", lendRates);

		AppPersonInfo appPersonInfo = appPersonInfoService.getByCustomerId(customerId);
		BigDecimal times = new BigDecimal("3");
		if (null != appPersonInfo.getLendTimes()) {
			times = appPersonInfo.getLendTimes();
		} else {
			String lendTimestr = remoteAppConfigService.getFinanceLendByKey("lendTimes");
			if (!StringUtil.isEmpty(lendTimestr)) {
				times = new BigDecimal(lendTimestr);
			}
		}
		map.put("lendTimes", times.toString());

		return map;

	
    }
    //融资
	@Override
	public Map<String, String> getLengCoinCodeForRmbCenter(Long customerId) {
//		String coinCodeForRmb = getRMBCode();
//        String crmb = getConfigByKey("coinCodeForRmb");
		RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String crmb = remoteAppConfigService.getFinanceByKey("coinCodeForRmb");
        Map<String, String> map=   getAllCener(customerId,crmb,crmb);
		map.put("coinCodeForRmb", crmb);
		return map;
	}
   //融币
	@Override
	public Map<String, String> getLengCoinCodeCenter(Long customerId, String lendcoinCode) {
		RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String coinCodeForRmb = remoteAppConfigService.getFinanceByKey("coinCodeForRmb");
		Map<String, String> map=  new HashMap<String, String>();
		if(null==lendcoinCode){
			List<LendCanCoinCode> list=getLendCanCoinCode();
			if(null!=list&&list.size()>0){
				lendcoinCode=list.get(0).getCoinCode();
				map= getAllCener(customerId,lendcoinCode,coinCodeForRmb);
			}else{
				map= getLengCoinCodeForRmbCenter(customerId);
			}
		}else{
			map= getAllCener(customerId,lendcoinCode,coinCodeForRmb);
		}
		
		map.put("coinCodeForRmb", lendcoinCode);
		return map;
		
	}

	@Override
	public List<LendCanCoinCode> getLendCanCoinCode() {
		List<LendCanCoinCode> listcan=new ArrayList<LendCanCoinCode>();
		RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String lendCodelist = remoteAppConfigService.getFinanceLendByKey("lendCodelist");
		if(!StringUtil.isEmpty(lendCodelist)){
			String[] ar=lendCodelist.split(",");
			for(int i=0;i<ar.length;i++){
				LendCanCoinCode lendCanCoinCode=new LendCanCoinCode();
	    		lendCanCoinCode.setCoinCode(ar[i]);
	    		listcan.add(lendCanCoinCode);
			}
		}
		
		
		
		
		
		
	/*	for(ExCointoCoin e:list){
			LendCanCoinCode lendCanCoinCode=new LendCanCoinCode();
    		lendCanCoinCode.setCoinCode(e.getCoinCode());
    		listcan.add(lendCanCoinCode);
		    if(e.getFixPriceCoinCode().equals(coinCodeForRmb)){
		    	BigDecimal currentExchangPrice=getCurrentExchangPrice(e.getCoinCode(),e.getFixPriceCoinCode());
		    	if(null!=currentExchangPrice){
		    		LendCanCoinCode lendCanCoinCode=new LendCanCoinCode();
		    		lendCanCoinCode.setCoinCode(e.getCoinCode());
		    		listcan.add(lendCanCoinCode);
		    	}
		    }
			
		}*/
		return listcan;
	}
	public static BigDecimal getCurrentExchangPrice(String coinCode,String fixPriceCoinCode) {
		
		String header = coinCode + "_" + fixPriceCoinCode;
		String key=header + ":" + ExchangeDataCacheRedis.CurrentExchangPrice;	
		RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
		String v = redisService.get(key);
		if(StringUtil.isEmpty(v)||new BigDecimal(v).compareTo(new BigDecimal("0"))==0){
			return null;
		}else{
			return new BigDecimal(v);
		}

	}

	@Override
	public List<LendIntent> fildIntentlist(Long lendId) {
		QueryFilter filter = new QueryFilter(ExProduct.class);
		filter.addFilter("lendId=", lendId);
		List<ExDmLendIntent> list= exDmLendIntentService.find(filter);
		List<LendIntent> liste=new ArrayList<LendIntent>();
		for(ExDmLendIntent l:list){
			LendIntent le=new LendIntent();
			le.setIntentType(l.getIntentType());
			le.setLendCoin(l.getLendCoin());
			le.setRepayCount(l.getRepayCount());
			le.setFactTime(l.getFactTime());
			liste.add(le);
			
		}
		return liste;
	}

	@Override
	public Map<String, String> getCoinCodeForRmb() {
		Map<String, String> map = new HashMap<String, String>();
		RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String coinCodeForRmbType = remoteAppConfigService.getFinanceByKey("coinCodeForRmbType");
		if (coinCodeForRmbType.equals("1")) { // 真实货币

			String language_code = remoteAppConfigService.getFinanceByKey("language_code");
			map.put("coinCodeForRmbType", "1");
			map.put("coinCodeForRmb", language_code);

		} else {
			String coinCodeForRmbsss = remoteAppConfigService.getFinanceByKey("coinCodeForRmb");
			map.put("coinCodeForRmbType", "2");
			map.put("coinCodeForRmb", coinCodeForRmbsss);
		}
		return map;

	}

	@Override
	public String[] lendTimesApplyAdd(LendTimes LendTimes) {
		String[] relt = new String[2];
		QueryFilter personFilter = new QueryFilter(ExDmLendTimes.class);
		personFilter.addFilter("status=", 1);
		personFilter.addFilter("customerId=",LendTimes.getCustomerId());
		ExDmLendTimes exDmLendTimesing = exDmLendTimesService.get(personFilter);


		RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String lengPings = remoteAppConfigService.getFinanceLendByKey("lendTimesConfig");
        String lendTimes1 = LendTimes.getLendTimes().toString();
        boolean flag= false;
        if(lengPings!=null) {
            String[] lendTimes = lengPings.split(",");
            for (String lend : lendTimes) {
                if (lend.equals(lendTimes1)) {
                    flag=true;
                    break;
                }
            }
        }

        if(!flag){
            relt[0] = CodeConstant.CODE_FAILED;
            relt[1] = "notExistingMultiple";
        }else if (null != exDmLendTimesing) {
			relt[0] = CodeConstant.CODE_FAILED;
			relt[1] = "existingMultiple";
		} else {
			ExDmLendTimes exDmLendTimes = new ExDmLendTimes();
			exDmLendTimes.setCustomerId(LendTimes.getCustomerId());
			exDmLendTimes.setUserCode(LendTimes.getUserCode());
			exDmLendTimes.setUserName(LendTimes.getUserName());
			exDmLendTimes.setLendTimes(LendTimes.getLendTimes());
			exDmLendTimes.setApplyTime(new Date());
			exDmLendTimes.setStatus(1);

			exDmLendTimesService.save(exDmLendTimes);
			relt[0] = CodeConstant.CODE_SUCCESS;
		}
		return relt;

	}

	@Override
	public FrontPage lendTimesApplyList(Map<String, String> map) {
		String customerId = map.get("customerId");
		QueryFilter personFilter = new QueryFilter(ExDmLendTimes.class);
		personFilter.addFilter("customerId=", customerId);
		personFilter.setOrderby(" applyTime desc ");
		List<ExDmLendTimes> list = exDmLendTimesService.find(personFilter);
		List<LendTimes> listLendTimes=new ArrayList<LendTimes>();
		for(ExDmLendTimes s:list){
			LendTimes lendTimes=new LendTimes();
			lendTimes.setApplyTime(s.getApplyTime());
			lendTimes.setLendTimes(s.getLendTimes());
			lendTimes.setStatus(s.getStatus());
			lendTimes.setLendTimesStatus(s.getLendTimesStatus());
			listLendTimes.add(lendTimes);
		}
		Page page = PageFactory.getPage(map);
		// 查询方法
		return new FrontPage(listLendTimes, page.getTotal(), page.getPages(), page.getPageSize());
	}

}
