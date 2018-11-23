/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2016年5月3日 上午11:00:12
 */
package com.mz.trade.MQmanager;


import com.mz.trade.entrust.service.RedisAccountService;
import com.mz.trade.model.AccountResultEnum;
import com.mz.trade.mq.service.MessageProducer;
import java.util.List;

import com.mz.util.serialize.Mapper;
import com.mz.util.sys.ContextUtil;
import com.mz.trade.redis.model.Accountadd;
import com.mz.trade.redis.model.EntrustTrade;
import com.mz.util.sys.SpringContextUtil;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年5月3日 上午11:00:12 
 */
public class MQEnter {
	/**
	 * 
	 * <p>
	 * 放进匹配队列
	 * </p>
	 * 
	 * @author: Gao Mimi
	 * @param: @param exEntrust
	 * @return: void
	 * @Date : 2016年4月22日 下午4:44:47
	 * @throws:
	 */
	public static void pushExEntrustMQ(EntrustTrade exEntrust) {
		MessageProducer messageProducer =(MessageProducer)ContextUtil.getBean("messageProducer");
		String exentrust=Mapper.objectToJson(exEntrust);
		messageProducer.toTrade(exentrust);
	}
	/**
	 * 放进资金处理队列
	 * @param aaddlists
	 */
	public static void pushDealFundMQ(List<Accountadd> aaddlists) {
		RedisAccountService redisAccountService = SpringContextUtil.getBean(RedisAccountService.class);
		AccountResultEnum result = redisAccountService.accountChange(aaddlists, false);
		if (result == AccountResultEnum.SUCCESS) {
			return;
		} else {
			MessageProducer messageProducer =(MessageProducer)ContextUtil.getBean("messageProducer");
			messageProducer.toAccount(Mapper.objectToJson(aaddlists));
		}
	}

}