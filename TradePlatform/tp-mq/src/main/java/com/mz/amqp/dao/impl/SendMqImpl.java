/**
 * Copyright:   北京互融时代软件有限公司
 * @author:        Wu Shuiming
 * @version:      V1.0 
 * @Date :    2016年3月9日  上午10:01:29
 */
package com.mz.amqp.dao.impl;


import com.mz.amqp.dao.SendMq;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


/**
 *类描述：
 *@author: wu shuiming
 *@date： 日期：2016年3月9日        时间：上午10:01:29
 *@version 1.0
 */
@Component("sendMq")
public class SendMqImpl implements SendMq{
	
	@Resource(name = "amqpTemplate")
	private RabbitTemplate mqTemplate;
	
	// 发送自定义信息到交换机 自己制定key 和交换机名称(但是需要在配置文件里配置)
	public void sendString(String exchange, String key, String massage){
		
		mqTemplate.convertAndSend(exchange, key, massage);
		
	}
	
	// 发送日志的方法固定的交换机和key 
	public void sendLog(String massage){
		
		mqTemplate.convertAndSend("logExchange","hy.web.req", massage);
		
	}
	
	
	
	
	
}
