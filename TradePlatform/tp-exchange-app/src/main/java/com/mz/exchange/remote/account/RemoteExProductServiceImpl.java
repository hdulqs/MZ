/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年4月6日 下午4:21:15
 */
package com.mz.exchange.remote.account;

import com.mz.account.fund.model.AppAccount;
import com.mz.account.remote.RemoteAppAccountService;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.customer.agents.model.AppAgentsCustromer;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.user.model.AppCustomer;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.product.model.ExCointoCoin;
import com.mz.exchange.product.model.ExProduct;
import com.mz.exchange.product.service.ExCointoCoinService;
import com.mz.exchange.product.service.ExProductService;
import com.mz.ico.coin.model.AppIcoCoin;
import com.mz.ico.coinAccount.model.AppIcoCoinAccount;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.util.QueryFilter;
import com.mz.util.RemoteQueryFilter;
import com.mz.util.UUIDUtil;
import com.mz.util.idgenerate.IdGenerate;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.customer.remote.RemoteAppAgentsService;
import com.mz.ico.coin.service.AppIcoCoinService;
import com.mz.ico.coinAccount.service.AppIcoCoinAccountService;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年4月6日 下午4:21:15
 */
public class RemoteExProductServiceImpl implements RemoteExProductService {
	
	private final Logger log = Logger.getLogger(RemoteExProductServiceImpl.class);

	@Resource
	public ExProductService exPproductService;

	@Resource
	public ExDigitalmoneyAccountService exDigitalmoneyAccountService;

	@Resource
	RemoteAppAccountService remoteAppAccountService;
	@Resource
  ExCointoCoinService exCointoCoinService;
	@Resource
	private AppIcoCoinService appIcoCoinService;
	@Resource
	private AppIcoCoinAccountService appIcoCoinAccountService;
	@Override
	public List<ExProduct> findByIssueState(Integer i) {
		QueryFilter filter = new QueryFilter(ExProduct.class);
		filter.addFilter("issueState=", i);
		filter.setOrderby("isRecommend DESC");
		List<ExProduct> list = exPproductService.find(filter);
		return list;
	}

	@Override
	public List<ExProduct> findByIssueState(RemoteQueryFilter rfilter) {
		QueryFilter filter = rfilter.getQueryFilter();
		List<ExProduct> list = exPproductService.find(filter);
		return list;
	}

	@Override
	public boolean findByCode(String s) {
		boolean b = exPproductService.findByCoinCode(s);
		return b;
	}

	@Override
	public ExProduct findByCoinCode(String c, String sassId) {
		return exPproductService.findByCoinCode(c, sassId);
	}

	@Override
	public ExProduct findByCoinCode(RemoteQueryFilter rfilter) {
		QueryFilter filter = rfilter.getQueryFilter();
		ExProduct product = exPproductService.get(filter);
		return product;
	}

	/**
	 * 保存代理商
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param agentsCustromer
	 * @param: @return
	 * @return: boolean
	 * @Date : 2016年10月17日 下午6:54:10
	 * @throws:
	 */
	public boolean saveAppAgents(AppAgentsCustromer agentsCustromer) {
		try {
			RemoteAppAgentsService remoteAppAgentsService = (RemoteAppAgentsService) ContextUtil.getBean("remoteAppAgentsService");
			JsonResult result = remoteAppAgentsService.saveAgents(agentsCustromer);
			return true;
		} catch (Exception e) {
			return false;
		}
	}



	@Override
	public JsonResult openDmAccount(AppCustomer appCustomer, AppPersonInfo appPersonInfo, AppAgentsCustromer agentsCustromer, String website, String currencyType) {
		JsonResult jsonResult = new JsonResult();
		//判断有没有ico系统
		if("true".equals(PropertiesUtils.APP.getProperty("app.hasico"))){
			//开通ICO虚拟币账户
			try {
				QueryFilter filter=new QueryFilter(AppIcoCoin.class);
				filter.addFilter("issueState=", Integer.valueOf(1));
				List<AppIcoCoin> coins=appIcoCoinService.find(filter);
				for (AppIcoCoin l:coins) {
					QueryFilter f=new QueryFilter(AppIcoCoinAccount.class);
					f.addFilter("customerId=", appCustomer.getId());
					f.addFilter("coinCode=", l.getCoinCode());
					List<AppIcoCoinAccount> cs=appIcoCoinAccountService.find(f);
					if(cs==null||cs.size()==0){
						AppIcoCoinAccount coinAccount=new AppIcoCoinAccount();
						coinAccount.setCurrencyType(currencyType);
						coinAccount.setWebsite(website);
						coinAccount.setSaasId(appCustomer.getSaasId());
						coinAccount.setAccountNum(IdGenerate.accountNum(appCustomer.getId().intValue(), l.getCoinCode()));
						coinAccount.setCustomerId(appCustomer.getId());
						coinAccount.setUserName(appCustomer.getUserName());
						coinAccount.setTrueName(appPersonInfo.getTrueName());
						coinAccount.setSurname(appPersonInfo.getSurname());
						coinAccount.setCoinCode(l.getCoinCode());
						coinAccount.setPublicKey("");
						coinAccount.setCoinName(l.getName());
						coinAccount.setPrivateKey(UUIDUtil.getUUID());
						coinAccount.setStatus(Integer.valueOf(1));
						// 保存
						appIcoCoinAccountService.save(coinAccount);
						//异步调用ico钱包接口生成正式钱包地址
						//ThreadPool.exe(new OpenIcoCoin(website, l.getCoinCode(), appCustomer.getUserName(), coinAccount.getId()));
					}
				}
			} catch (Exception e) {
				log.error("开通ICO币账户失败！");
			}
		}
		// 查出全部发行的产品例表
		List<ExProduct> listProducts = findByIssueState(Integer.valueOf(1));
		// 便利开通账户号
		log.info("【进入开通虚拟币账户】");
		for (ExProduct exProduct : listProducts) {
			QueryFilter filter = new QueryFilter(AppAccount.class);
			filter.addFilter("customerId=", appCustomer.getId());
			filter.addFilter("website=", website);
			filter.addFilter("currencyType=", currencyType);
			filter.addFilter("coinCode=", exProduct.getCoinCode());
			log.info("产品总条:" + listProducts.size());
			log.info("查询" + exProduct.getCoinCode());
			ExDigitalmoneyAccount _digitalmoneyAccount = exDigitalmoneyAccountService.get(filter);
			if (_digitalmoneyAccount == null) {
				log.info("币账户币不存在-开始开通虚拟币账户" + appCustomer.getUserName() + "|" + exProduct.getCoinCode());
				ExDigitalmoneyAccount digitalmoneyAccount = new ExDigitalmoneyAccount();
				digitalmoneyAccount.setCurrencyType(currencyType);
				digitalmoneyAccount.setWebsite(website);
				digitalmoneyAccount.setSaasId(appCustomer.getSaasId());
				digitalmoneyAccount.setAccountNum(IdGenerate.accountNum(appCustomer.getId().intValue(), exProduct.getCoinCode()));
				digitalmoneyAccount.setCustomerId(appCustomer.getId());
				digitalmoneyAccount.setUserName(appCustomer.getUserName());
				digitalmoneyAccount.setTrueName(appPersonInfo.getTrueName());
				digitalmoneyAccount.setSurname(appPersonInfo.getSurname());
				digitalmoneyAccount.setCoinCode(exProduct.getCoinCode());
				digitalmoneyAccount.setPublicKey("");
				digitalmoneyAccount.setCoinName(exProduct.getName());
				digitalmoneyAccount.setPrivateKey(UUIDUtil.getUUID());
				digitalmoneyAccount.setStatus(Integer.valueOf(1));
				//digitalmoneyAccount.setHotMoney(exProduct.getGiveCoin());//注册送币
				
				// 保存
				exDigitalmoneyAccountService.save(digitalmoneyAccount);
				
				//记录流水
				//exDigitalmoneyAccountService.saveRecord(digitalmoneyAccount);
				
				
				
				log.info("初始化虚拟币账户缓存---------->>>:"+digitalmoneyAccount.getId().toString());
				RedisUtil<ExDigitalmoneyAccount> redisUtil = new RedisUtil<ExDigitalmoneyAccount>(ExDigitalmoneyAccount.class);
				redisUtil.put(digitalmoneyAccount, digitalmoneyAccount.getId().toString());
				
				//异步开通钱包地址
				//ThreadPool.exe(new OpenCoin(website, exProduct.getCoinCode(), appCustomer.getUserName(), digitalmoneyAccount.getId()));
			}else  if(_digitalmoneyAccount!=null&&"".equals(_digitalmoneyAccount.getPublicKey())){
				//异步开通钱包地址
				//ThreadPool.exe(new OpenCoin(website, exProduct.getCoinCode(), appCustomer.getUserName(), _digitalmoneyAccount.getId()));
			}else{
				log.info("币账户已存在    用户名：" + appCustomer.getUserName()+"--币账户："+_digitalmoneyAccount.getAccountNum()+"-coinCode:"+_digitalmoneyAccount.getCoinCode());
			}
		}
		return jsonResult;
	}

	/**
	 * 
	 * 通过用户的 id 返回用户跟用户有关系的所有币的对象
	 * 
	 * @author: Wu Shuiming
	 * @version: V1.0
	 * @date: 2016年7月26日 下午7:04:19
	 */
	@Override
	public List<ExProduct> findProduct(Long id) {
		List<ExProduct> list = exPproductService.findProductByCustomerId(id);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * RemoteExProductService#findByIssueState(java
	 * .lang.Integer, java.lang.String)
	 */
	@Override
	public List<ExProduct> findByIssueState(Integer i, String saasId) {
		// TODO Auto-generated method stub
		return exPproductService.findByIssueState(i, saasId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * RemoteExProductService#get(com.mz.core.util.
	 * RemoteQueryFilter)
	 */
	@Override
	public ExProduct get(RemoteQueryFilter filter) {
		return exPproductService.get(filter.getQueryFilter());
	}
	
	
	@Override
	public List<ExProduct> getSelectProduct(){
		List<ExProduct> list = exPproductService.findByIssueState(1, "");
		return list;
	}

	@Override
	public void addAveragePrice(ExProduct exProduct) {
		 exPproductService.update(exProduct);
	}

	
	/**
	 * 手动生成比地址
	 */
	@Override
	public void handToCoinAdress(String coin) {
		System.out.println("进入后台：手动生成比地址方法。。。。。。。。注释啦！！！！！！！");
		/*System.out.println("进入后台：手动生成比地址方法。。。。。。。。");
		QueryFilter filter=new QueryFilter(ExDigitalmoneyAccount.class);
		filter.addFilter("coinCode=", coin);
		List<ExDigitalmoneyAccount> list=exDigitalmoneyAccountService.find(filter);
		for (ExDigitalmoneyAccount exDigitalmoneyAccount : list) {
			if(exDigitalmoneyAccount.getPublicKey()!=null && !"".equals(exDigitalmoneyAccount.getPublicKey())){
			}else{
				System.out.println(exDigitalmoneyAccount.getUserName()+"开始生成");
				ThreadPool.exe(new OpenCoin("cn", coin, exDigitalmoneyAccount.getUserName(), exDigitalmoneyAccount.getId()));
			}
		}*/
	}

	@Override
	public List<ExCointoCoin> getExCointoCoinlist(String toProductcoinCode, String fromProductcoinCode, Integer issueState) {
		List<ExCointoCoin> list=exCointoCoinService.getBylist(toProductcoinCode, fromProductcoinCode, issueState);
		return list;
	}
}
