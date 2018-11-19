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

  List<ExCointoCoin> getExCointoCoinValid();

    /**
     * 获取需要自动交易的交易对，因为自动交易用户需要验证，所有需要取出来
     * @return
     */
    List<ExCointoCoin> getSratAutoExCointoCoin();
	/**
	 * 根据交易币种和定价币种获取相关信息
	 * @param coinCode 交易币种
	 * @param fixPriceCoinCode 定价币种
	 * @return
	 */
	 List<ExCointoCoin> getExCointoCoinByCoinCode(@Param(value="coinCode")String coinCode, @Param(value="fixPriceCoinCode")String fixPriceCoinCode);
}
