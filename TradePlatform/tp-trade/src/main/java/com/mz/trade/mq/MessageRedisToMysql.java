package com.mz.trade.mq;

import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.util.sys.ContextUtil;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MessageRedisToMysql implements MessageListener {
	private Logger logger = Logger.getLogger(MessageRedisToMysql.class);

	@Override
	public void onMessage(Message message) {
		ExOrderInfoService exOrderInfoService = (ExOrderInfoService) ContextUtil.getBean("exOrderInfoService");
		exOrderInfoService.redisToMysql();
	}

}
