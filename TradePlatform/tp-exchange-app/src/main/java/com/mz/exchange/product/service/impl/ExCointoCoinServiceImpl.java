/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      gaomimi
 * @version:     V1.0 
 * @Date:        2017-07-06 19:40:34 
 */
package com.mz.exchange.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.product.model.ExCointoCoin;
import com.mz.exchange.product.model.ExProduct;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.QueryFilter;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.core.constant.StringConstant;
import com.mz.exchange.product.dao.ExCointoCoinDao;
import com.mz.exchange.product.service.ExCointoCoinService;
import com.mz.exchange.product.service.ExProductService;
import com.mz.manage.remote.model.Coin;
import com.mz.trade.entrust.ExchangeDataCache;
import com.mz.web.app.service.AppConfigService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.util.StringUtil;

/**
 * <p> ExCointoCoinService </p>
 * @author:         gaomimi
 * @Date :          2017-07-06 19:40:34  
 */
@Service("exCointoCoinService")
public class ExCointoCoinServiceImpl extends BaseServiceImpl<ExCointoCoin, Long> implements ExCointoCoinService{
	
	@Resource
	private ExProductService exProductService;
	@Resource
	private AppConfigService appConfigService;
	@Resource(name="exCointoCoinDao")
	@Override
	public void setDao(BaseDao<ExCointoCoin, Long> dao) {
		super.dao = dao;
	}
	
	
	@Override
	public List<ExCointoCoin> getBylist(String toProductcoinCode, String fromProductcoinCode,Integer issueState) {
		QueryFilter filter = new QueryFilter(ExProduct.class);
		if(!StringUtil.isEmpty(toProductcoinCode)){
			filter.addFilter("coinCode=", toProductcoinCode);
		}
		if(!StringUtil.isEmpty(fromProductcoinCode)){
			filter.addFilter("fixPriceCoinCode=", fromProductcoinCode);
		}
		if(null!=issueState){
			filter.addFilter("state=", issueState);
		}
		
		String saasId = PropertiesUtils.APP.getProperty("app.saasId");
		filter.setSaasId(saasId);
		List<ExCointoCoin> list = super.find(filter);
		return list;
	}

	@Override
	public List<ExCointoCoin> getBylist(Integer state) {
		// TODO Auto-generated method stub
		ExCointoCoinDao exCointoCoinDao = (ExCointoCoinDao) dao;
		Map<String, Object> map = new HashMap<String, Object>();
		if(null!=state){
			map.put("state", state);
		}
		return exCointoCoinDao.getByfixPrice(map);
	}
	
	@Override
	public void initRedisCode() {
		QueryFilter queryFilter = new QueryFilter(ExCointoCoin.class);
		queryFilter.addFilter("state=", 1);
	//	queryFilter.setOrderby("sort");
		List<ExCointoCoin> list = super.find(queryFilter);
		//币详细信息list缓存
		//刷新产品列表缓存CoinInfoList
		
		ArrayList<String> codeList = new ArrayList<String>();
		
		List<Coin> listCoin=new ArrayList<Coin>();

		RedisService redisService = (RedisService) ContextUtil.getBean("redisService");


		for(ExCointoCoin eExCointoCoin : list){
			codeList.add(eExCointoCoin.getCoinCode()+"_"+eExCointoCoin.getFixPriceCoinCode());

			//组装coin对象
			JSONObject c=new JSONObject();
			QueryFilter filter=new QueryFilter(ExProduct.class);
			filter.addFilter("coinCode=", eExCointoCoin.getCoinCode());
			ExProduct product=exProductService.get(filter);

			String coinStr = redisService.get("cn:coinInfoList2");
			if(coinStr!=null){
				List<Coin> coins = JSONArray.parseArray(coinStr, Coin.class);
				for(Coin a : coins){
					if(eExCointoCoin.getCoinCode().equals(a.getCoinCode())&&eExCointoCoin.getFixPriceCoinCode().equals(a.getFixPriceCoinCode())) {
						c.put("yesterdayPrice", a.getYesterdayPrice());
					}
				}
			}



			JSONObject c2c = JSON.parseObject(JSON.toJSONString(eExCointoCoin));
			for (Map.Entry<String, Object> entry : c2c.entrySet()) {
				c.put(entry.getKey(), entry.getValue());
			}
			
			
			JSONObject p = JSON.parseObject(JSON.toJSONString(product));
			for (Map.Entry<String, Object> entry : p.entrySet()) {
				c.put(entry.getKey(), entry.getValue());
			}
			
			//如果是虚拟币
			if(eExCointoCoin.getFixPriceType()!=null&&eExCointoCoin.getFixPriceType()==1){
				QueryFilter queryProduct = new QueryFilter(ExProduct.class);
				queryProduct.addFilter("coinCode=", eExCointoCoin.getFixPriceCoinCode());
				ExProduct exProduct = exProductService.get(queryProduct);
				c.put("keepDecimalForCurrency",exProduct.getKeepDecimalForCoin() );
			}else{//真实货币
//				RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
				String config = redisService.get("configCache:all");
				if(!StringUtils.isEmpty(config)){
					JSONObject parseObject = JSONObject.parseObject(config);
					c.put("keepDecimalForCurrency",null==parseObject.get("keepDecimalForRmb")?"2":parseObject.get("keepDecimalForRmb") );
				}else{
					String  appConfigv  =	appConfigService.getBykeyfromDB("keepDecimalForRmb");
					c.put("keepDecimalForCurrency",null==appConfigv?"2":appConfigv);
					
				}
			}
			
			Coin coin=JSON.toJavaObject(c, Coin.class);
			listCoin.add(coin);
			
		}
		
		//缓存到所有的站点中productListKey值
		Map<String,String> mapLoadWeb=PropertiesUtils.getLoadWeb();
		for (String Website : mapLoadWeb.keySet()) {
		    ExchangeDataCache.setStringData(Website+":productFixList",JSON.toJSONString(codeList));
		    
		    ExchangeDataCache.setStringData(Website+":"+StringConstant.COININFOLIST,JSON.toJSONString(listCoin));
		}
	}
}
