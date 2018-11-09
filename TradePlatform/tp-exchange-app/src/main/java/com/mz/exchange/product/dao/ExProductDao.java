/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Wu Shuiming
 * @version:   V1.0 
 * @Date:      2015年11月06日  14:57:13
 */
package com.mz.exchange.product.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.exchange.product.model.ExProduct;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * <p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午1:34:18
 */
public interface ExProductDao extends BaseDao<ExProduct, Long> {
	
	public List<ExProduct> selectProductByCustomerId(@Param(value = "customerId") Long customerId);

}
