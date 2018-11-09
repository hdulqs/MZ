package com.mz.amqp.common;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Frank on 2018/11/5.
 */
@Component
public class Producer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  public void handle(Message message, String routeKey) {
    rabbitTemplate.convertAndSend(AmqpConfig.EXCHAGE_NAME, routeKey, message);
  }

}
