/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年6月21日 下午7:13:05
 */
package com.mz.account.remote;

import java.math.BigDecimal;

/**
 * <p> TODO</p>
 * @author:         Wu Shuiming
 * @Date :          2016年6月21日 下午7:13:05 
 */
public interface RemoteTopAndDepositParameterService {

	public BigDecimal getTopAndDeposit(String code,BigDecimal money,Integer type);
}
