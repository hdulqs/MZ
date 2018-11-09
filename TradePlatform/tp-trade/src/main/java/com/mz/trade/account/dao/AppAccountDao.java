/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月31日 下午6:50:05
 */
package com.mz.trade.account.dao;

import com.mz.account.fund.model.AppAccount;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.trade.redis.model.AppAccountRedis;
import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月31日 下午6:50:05 
 */
public interface AppAccountDao extends BaseDao<AppAccount,Long> {

	
    public void  updateAppAccount(List<AppAccountRedis> list);
}
