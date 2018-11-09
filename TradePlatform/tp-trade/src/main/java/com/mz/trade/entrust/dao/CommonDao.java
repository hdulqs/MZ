/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2015年11月06日  14:57:13
 */
package com.mz.trade.entrust.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.customer.user.model.AppCustomer;
import com.mz.exchange.product.model.ExCointoCoin;
import com.mz.trade.redis.model.EntrustTrade;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 *
 * <p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午1:34:18
 */
public interface CommonDao extends BaseDao<EntrustTrade, Long> {

  AppCustomer getAppUserByuserName(@Param(value = "userName") String userName);

  List<ExCointoCoin> getExointocoinValid();

  List<AppCustomer> getAppUserAll();
}
