/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 17:40:59 
 */
package com.mz.customer.agents.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.customer.rebat.model.AppCommendRebat;
import java.util.List;
import java.util.Map;

/**
 * <p> AppCommendTradeDao </p>
 * @author:         menwei
 * @Date :          2017-11-28 17:40:59  
 */
public interface AppCommendRebatDao extends BaseDao<AppCommendRebat, Long> {
   List<AppCommendRebat> culApoCmmendRebat(Map<String,Object> map);
}
