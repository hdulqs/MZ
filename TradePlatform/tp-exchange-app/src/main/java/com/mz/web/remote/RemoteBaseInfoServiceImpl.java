package com.mz.web.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.customer.user.model.AppCustomerCollection;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppHolidayConfig;
import com.mz.web.app.service.AppHolidayConfigService;
import com.mz.core.constant.StringConstant;
import com.mz.customer.user.service.AppCustomerCollectionService;
import com.mz.manage.remote.RemoteBaseInfoService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

public class RemoteBaseInfoServiceImpl implements RemoteBaseInfoService {
	
	@Resource
	private AppHolidayConfigService appHolidayConfigService;
	@Autowired
	private RedisService redisService;
	
	@Resource
	private AppCustomerCollectionService appCustomerCollectionService;
	
	@Override
	public List<AppHolidayConfig> listAppHolidayConfig() {
		String str=JSON.toJSONString(appHolidayConfigService.findAll());
		return JSON.parseArray(str, AppHolidayConfig.class);
	}
	
	@Override
	public String getFinanceByKey(String key) {
		String val="";
		String string=redisService.get(StringConstant.CONFIG_CACHE+":financeConfig");
		JSONArray obj=JSON.parseArray(string);
		for(Object o:obj){
		JSONObject	 oo=JSON.parseObject(o.toString());
		if(oo.getString("configkey").equals(key)){
			val=oo.getString("value");
		}
		}
		return val;
	}
	
	@Override
	public boolean addCustomerCollection(Long customerId, String coinCode) {
		
		QueryFilter filter = new QueryFilter(AppCustomerCollection.class);
		filter.addFilter("customerId=", customerId);
		filter.addFilter("coinCode=", coinCode);
		AppCustomerCollection model = appCustomerCollectionService.get(filter);
		if(model==null){
			AppCustomerCollection appCustomerCollection = new AppCustomerCollection();
			appCustomerCollection.setCustomerId(customerId);
			appCustomerCollection.setCoincode(coinCode);
			appCustomerCollectionService.save(appCustomerCollection);
		}
		
		return true;
	}
	
	@Override
	public boolean deleteCustomerCollection(Long customerId, String coinCode) {
		
		QueryFilter filter = new QueryFilter(AppCustomerCollection.class);
		filter.addFilter("customerId=", customerId);
		filter.addFilter("coinCode=", coinCode);
		appCustomerCollectionService.delete(filter);
		return true;
	}
	
	@Override
	public List<String> findCustomerCollection(Long customerId) {
		
		QueryFilter filter = new QueryFilter(AppCustomerCollection.class);
		filter.addFilter("customerId=", customerId);
		List<AppCustomerCollection> list = appCustomerCollectionService.find(filter);
		if(list!=null&&list.size()>0){
			ArrayList<String> coins = new ArrayList<String>();
			for(AppCustomerCollection c : list){
				coins.add(c.getCoincode());
			}
			return coins;
		}
		
		return null;
	}
	
}
