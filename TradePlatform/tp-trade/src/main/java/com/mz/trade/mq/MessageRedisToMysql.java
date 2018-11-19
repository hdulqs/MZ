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
	// 当且仅当使用一个线程来保持后台的redisToMysql保持运行
	private static ExecutorService executors = Executors.newFixedThreadPool(1);

	@Override
	public void onMessage(Message message) {
		if (((ThreadPoolExecutor)executors).getQueue().size() == 0) {
			executors.execute(() -> {
				try {
					ExOrderInfoService exOrderInfoService = (ExOrderInfoService) ContextUtil.getBean("exOrderInfoService");
					exOrderInfoService.redisToMysql();
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("redisToMysql error", e);
				}
			});
		}
	}

}
