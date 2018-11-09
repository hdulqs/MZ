package com.mz.mq.producer;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.mz.util.sys.ContextUtil;
import com.mz.exchange.lend.ExDmPingNorService;

/**
 * 充币监听
 * @author CHINA_LSL
 *
 */
public class MessageLendRepay implements MessageListener {
	private Logger logger = Logger.getLogger(MessageLendRepay.class);

	@Override
	public void onMessage(Message message) {
		String str = new String(message.getBody());
		ExDmPingNorService exDmPingService = (ExDmPingNorService) ContextUtil.getBean("exDmPingNorService");
		exDmPingService.jobRunTimeRepayLendMoney();
		
	}

}