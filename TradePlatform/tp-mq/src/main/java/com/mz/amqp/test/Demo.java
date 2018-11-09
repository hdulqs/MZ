/**
 * Copyright:   北京互融时代软件有限公司
 * @author:        Wu Shuiming
 * @version:      V1.0 
 * @Date :    2016年3月9日  下午6:15:25
 */
package com.mz.amqp.test;

import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *类描述：
 *@author: wu shuiming
 *@date： 日期：2016年3月9日        时间：下午6:15:25
 *@version 1.0
 */
public class Demo {

	@Test
	public void test01() throws InterruptedException{
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:rabbitmq/spring-rabbitmq.xml");
		// RabbitMQ模板
		RabbitTemplate template = ctx.getBean(RabbitTemplate.class);

		// 发送消息
		template.convertAndSend("logExchange","hy.web.req", "喝喝");
		Thread.sleep(5000);// 休眠1秒
		ctx.destroy(); // 容器销毁
	}
}
