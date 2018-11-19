package com.mz.trade.mq.service;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {
	private Logger logger = Logger.getLogger(MessageProducer.class);

    @Autowired
    private AmqpTemplate amqpTemplate;  


    public void toTrade(Object message)  {  
    	try {  
    		amqpTemplate.convertAndSend("toTradeKey", message);  
		} catch (Exception e) {
			e.printStackTrace();
		}
    }  
    
    public void toAccount(Object message)  {  
    	try {  
    		amqpTemplate.convertAndSend("toAccountKey", message);  
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }  
    public void redisToRedisLog(Object message)  {
    	try {  
    		amqpTemplate.convertAndSend("redisToRedisLogKey", message);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }  
    public void redisToMysql(Object message)  {
    	try {  
    		amqpTemplate.convertAndSend("redisToMysqlKey", message);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }  
}
