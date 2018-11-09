package com.mz.trade.mq;

import com.mz.trade.entrust.service.ExOrderInfoService;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.mz.util.sys.ContextUtil;

public class MessageReidsToMysql implements MessageListener {
	private Logger logger = Logger.getLogger(MessageReidsToMysql.class);

	@Override
	public void onMessage(Message message) {
		ExOrderInfoService exOrderInfoService = (ExOrderInfoService) ContextUtil.getBean("exOrderInfoService");
		exOrderInfoService.reidsToMysql();
	}

}
