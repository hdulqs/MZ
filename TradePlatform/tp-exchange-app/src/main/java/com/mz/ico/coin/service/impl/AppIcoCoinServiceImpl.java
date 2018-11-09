/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-08-17 18:22:21 
 */
package com.mz.ico.coin.service.impl;

import com.alibaba.fastjson.JSON;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.ico.coin.model.AppIcoCoin;
import com.mz.util.QueryFilter;
import com.mz.util.properties.PropertiesUtils;
import com.mz.ico.coin.service.AppIcoCoinService;
import com.mz.trade.entrust.ExchangeDataCache;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppIcoCoinService </p>
 * @author:         shangxl
 * @Date :          2017-08-17 18:22:21  
 */
@Service("appIcoCoinService")
public class AppIcoCoinServiceImpl extends BaseServiceImpl<AppIcoCoin, Long> implements AppIcoCoinService{
	
	@Resource(name="appIcoCoinDao")
	@Override
	public void setDao(BaseDao<AppIcoCoin, Long> dao) {
		super.dao = dao;
	}
	
	
	/**
	 * 初始化ico虚拟币缓存
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    
	 * @return: void 
	 * @Date :          2017年8月21日 上午10:06:50   
	 * @throws:
	 */
	@Override
	public void initIcoRedisCode(){
		QueryFilter queryFilter = new QueryFilter(AppIcoCoin.class);
		queryFilter.addFilter("issueState=", 1);
		queryFilter.setOrderby("sort");
		List<AppIcoCoin> list = super.find(queryFilter);
		ArrayList<String> codeList = new ArrayList<String>();
		for (AppIcoCoin l : list) {
			codeList.add(l.getCoinCode());
		}

		// 缓存到所有的站点中productListKey值
		Map<String, String> mapLoadWeb = PropertiesUtils.getLoadWeb();
		for (String Website : mapLoadWeb.keySet()) {
			ExchangeDataCache.setStringData(Website + ":coinList", JSON.toJSONString(codeList));
		}
	}
	

}
