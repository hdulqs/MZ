/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午5:55:59
 */
package com.mz.exchange.account.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.account.model.vo.DigitalmoneyAccountAndProduct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午5:55:59
 */
public interface ExDigitalmoneyAccountDao extends
		BaseDao<ExDigitalmoneyAccount, Long> {
	public int updateByVersion(BigDecimal cold,BigDecimal hot,BigDecimal lend,Integer newversion,Long customerId,String coinCode,String currencyType,String website , Integer version);
		
	/**
	 * 
	 * 通过站内星息 查询用户的虚拟币账户信息 包括产品
	 * 
	 * @author:    Wu Shuiming
	 * @version:   V1.0 
	 * @date:      2016年8月1日 下午6:54:13
	 */
	public List<DigitalmoneyAccountAndProduct> findNewProductByCustomer(@Param(value="website")String website,@Param(value="customerName")String customerName,@Param(value="isMarket")Integer isMarket);

	/**
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param map
	 * @return: void 
	 * @Date :          2016年8月13日 下午6:07:29   
	 * @throws:
	 */
	public List<ExDigitalmoneyAccount> findPageBySql(Map<String, Object> map);	

}
