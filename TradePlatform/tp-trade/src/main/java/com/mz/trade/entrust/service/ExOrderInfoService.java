/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月24日 下午1:45:20
 */
package com.mz.trade.entrust.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.trade.entrust.model.ExOrder;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.trade.redis.model.EntrustTrade;

import java.math.BigDecimal;

/**
 * <p> TODO</p>
 * @author: Gao Mimi
 * @Date :          2016年4月12日 下午4:45:50 
 */
public interface ExOrderInfoService extends BaseService<ExOrderInfo, Long> {

    public ExOrderInfo createExOrderInfo(Integer type, EntrustTrade buyExEntrust, EntrustTrade sellentrust, BigDecimal tradeCount, BigDecimal tradePrice);

    public void redisToMysql();

    public void redisToredisLog();

    public void redisToMysqlmq();

    public void redisToredisLogmq();
}
