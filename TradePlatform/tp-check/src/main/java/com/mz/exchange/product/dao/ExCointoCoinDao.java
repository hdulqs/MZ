/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      gaomimi
 * @version:     V1.0 
 * @Date:        2017-07-06 19:40:34 
 */
package com.mz.exchange.product.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.exchange.product.model.ExCointoCoin;
import java.util.List;

/**
 * <p> ExCointoCoinDao </p>
 * @author:         gaomimi
 * @Date :          2017-07-06 19:40:34  
 */
public interface ExCointoCoinDao extends BaseDao<ExCointoCoin, Long> {
	public List<ExCointoCoin> getExCointoCoinAutoUsername();
}
