package com.mz.amqp.common;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Frank on 2018/11/5.
 */
@Component
public class AmqpUtils {

  @Autowired
  ConnectionFactory connectionFactory;

  /**
   * 新增队列、消费者及绑定
   */
  public void addQueue(String queueName, String routeKey) {
    RabbitAdmin admin = new RabbitAdmin(connectionFactory);
    Queue queue = new Queue(queueName);
    admin.declareQueue(queue);
    TopicExchange topicExchange = new TopicExchange(AmqpConfig.EXCHAGE_NAME);
    admin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(routeKey));
    Consumer consumer = new Consumer();
    MessageListenerAdapter adapter = new MessageListenerAdapter(consumer);
    adapter.setDefaultListenerMethod("onMessage");
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(new String[]{queueName});
    container.setMessageListener(adapter);
    container.setMessageConverter(new Jackson2JsonMessageConverter());
    container.start();
  }
}
