package com.mz.amqp.common;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Frank on 2018/11/5.
 */
@Configuration
public class AmqpConfig {

  public static final String HOST = "192.168.1.102";
  public static final String USER_NAME = "qq";
  public static final String PASSWORD = "1234";
  public static final String VIRTUAL_HOST = "/qq";
  public static final String EXCHAGE_NAME = "exchange";

  @Bean
  public ConnectionFactory connectionFactory(){
    CachingConnectionFactory cachingConnectionFactory=new CachingConnectionFactory();
    cachingConnectionFactory.setHost(HOST);
    cachingConnectionFactory.setUsername(USER_NAME);
    cachingConnectionFactory.setPassword(PASSWORD);
    cachingConnectionFactory.setVirtualHost(VIRTUAL_HOST);
    return cachingConnectionFactory;
  }

  @Bean
  public TopicExchange topicExchange() {
    return new TopicExchange(EXCHAGE_NAME, true, true);
  }

  @Bean
  RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
    rabbitTemplate.setExchange(EXCHAGE_NAME);
    return rabbitTemplate;
  }

  @Bean
  public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

}
