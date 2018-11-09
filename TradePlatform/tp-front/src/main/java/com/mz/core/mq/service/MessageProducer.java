package com.mz.core.mq.service;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("frontMessageProducer")
public class MessageProducer {
	private Logger logger = Logger.getLogger(MessageProducer.class);

    @Autowired
    private AmqpTemplate amqpTemplate;  


    
    public void sendChongbiCallBack(Object message)  {  
    	try {  
    		logger.info("to send message:{}"+message);  
    		amqpTemplate.convertAndSend("chongbiCallBackKey", message);  
			
		} catch (Exception e) {
			e.printStackTrace();
		}
     
    } 
    
}
