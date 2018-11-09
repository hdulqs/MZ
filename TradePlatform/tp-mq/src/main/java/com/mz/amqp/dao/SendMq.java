/**
 * Copyright:   北京互融时代软件有限公司
 * @author:        Wu Shuiming
 * @version:      V1.0 
 * @Date :    2016年3月9日  上午10:01:29
 */
package com.mz.amqp.dao;


/**
 *类描述：
 *@author: wu shuiming
 *@date： 日期：2016年3月9日        时间：上午10:01:29
 *@version 1.0
 */
public interface SendMq {
	
	public static final String SEND_NAME = "hy.mq.rabbitmq.send.impl.SendImpl";
	
	public void sendString(String exchange, String key, String massage);
	
	public void sendLog(String massage);
	
}
