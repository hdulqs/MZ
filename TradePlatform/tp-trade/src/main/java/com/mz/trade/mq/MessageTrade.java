package com.mz.trade.mq;

import com.mz.trade.entrust.service.TradeService;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.mz.util.sys.ContextUtil;

public class MessageTrade implements MessageListener {
	private Logger logger = Logger.getLogger(MessageTrade.class);

	@Override
	public void onMessage(Message message) {
		TradeService tradeService = (TradeService) ContextUtil.getBean("tradeService");
		try {
			tradeService.matchExtrustToOrderQueue(new String(message.getBody(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
