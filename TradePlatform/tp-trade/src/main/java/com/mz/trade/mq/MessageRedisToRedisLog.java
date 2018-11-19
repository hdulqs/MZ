package com.mz.trade.mq;

import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.util.sys.ContextUtil;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MessageRedisToRedisLog implements MessageListener {
	private Logger logger = Logger.getLogger(MessageRedisToRedisLog.class);
	private static ExecutorService executors = Executors.newFixedThreadPool(1);

	@Override
	public void onMessage(Message message) {
		if (((ThreadPoolExecutor)executors).getQueue().size() == 0) {
			executors.execute(new Runnable() {
				@Override
				public void run() {
					try {
						ExOrderInfoService exOrderInfoService = (ExOrderInfoService) ContextUtil.getBean("exOrderInfoService");
						exOrderInfoService.redisToredisLog();
					} catch (Exception e) {
						logger.error("redisToredisLog error: ", e);
					}
				}
			});
		}
	}

}
