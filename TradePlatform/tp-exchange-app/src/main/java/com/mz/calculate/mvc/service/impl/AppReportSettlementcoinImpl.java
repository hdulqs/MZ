/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年7月5日 上午10:42:10
 */
package com.mz.calculate.mvc.service.impl;

import com.mz.calculate.settlement.model.AppReportSettlementcoin;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.product.model.ExProduct;
import com.mz.util.sys.ContextUtil;
import com.mz.calculate.mvc.service.AppReportSettlementcoinService;
import com.mz.exchange.remote.account.RemoteExProductService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


/**
 * <p> TODO</p>
 * @author:         Wu Shuiming
 * @Date :          2016年7月5日 上午10:42:10 
 */
@Service("appReportSettlementcoinService")
public class AppReportSettlementcoinImpl extends BaseServiceImpl<AppReportSettlementcoin, Long> implements
		AppReportSettlementcoinService  {

	@Resource(name="appReportSettlementcoinDao")
	@Override
	public void setDao(BaseDao<AppReportSettlementcoin, Long> dao) {
		super.dao = dao;
	}
	
	@Override
	public List<ExProduct> getSelectProduct(){
		RemoteExProductService remoteExProductService = (RemoteExProductService)ContextUtil.getBean("remoteExProductService");
		List<ExProduct> list = remoteExProductService.getSelectProduct();
		return list;
	}
	
	
	
	
}	











