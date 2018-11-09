/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月31日 下午6:50:05
 */
package com.mz.account.fund.dao;

import com.mz.account.fund.model.AppAccount;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月31日 下午6:50:05 
 */
public interface AppAccountDao extends BaseDao<AppAccount,Long>{
	public int updateByVersion(BigDecimal cold,BigDecimal hot,BigDecimal lendMoney,Integer newversion,Long customerId,String currencyType,Integer version);

	/**
	 * <p>后台分页查询</p>
	 * @author:         Liu Shilei
	 * @param:    @param map
	 * @return: void 
	 * @Date :          2016年8月13日 下午4:59:14   
	 * @throws:
	 */
	public List<AppAccount> findPageBySql(Map<String, Object> map);
	
	
	/**
	 * 查询用户的资金账户  
	 * 
	 * @param userName
	 * @param website
	 * @return
	 */
	public List<AppAccount> findListAccount(@Param(value="userName")String userName,@Param(value="website")String website);
	
	
	public List<ExDigitalmoneyAccount> findDigitalmoneyAccount(@Param(value="userName")String userName,@Param(value="website")String website);

	/**
	 * 金科添加：查找所有的代理商账户
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param map
	 * @param:    @return
	 * @return: List<AppAccount> 
	 * @Date :          2017年3月11日 上午10:31:29   
	 * @throws:
	 */
	public List<AppAccount> findAgentPageBySql(Map<String, Object> map);
	

}
