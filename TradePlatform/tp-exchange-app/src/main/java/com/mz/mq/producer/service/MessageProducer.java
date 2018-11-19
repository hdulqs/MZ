package com.mz.mq.producer.service;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

  private Logger logger = Logger.getLogger(MessageProducer.class);

  @Autowired
  private AmqpTemplate amqpTemplate;


  /**
   * 发送账户操作信息
   */
  public void toAccount(Object message) {
    try {
      amqpTemplate.convertAndSend("toAccountKey", message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 发送委托信息
   */
  public void toTrade(Object message) {
    try {
      amqpTemplate.convertAndSend("toTradeKey", message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 发送委托信息
   */
  public void toLendRepay(Object message) {
    try {
      amqpTemplate.convertAndSend("toLendRepayKey", message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 发送委托信息
   */
  public void toLendPing(Object message) {
    try {
      amqpTemplate.convertAndSend("toLendPingKey", message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 发送委托信息
   */
  public void syncKline(Object message) {
    try {
      amqpTemplate.convertAndSend("syncKlineKey", message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void sendChongbiCallBack(Object message) {
    try {
      logger.info("to send message:{}" + message);
      amqpTemplate.convertAndSend("chongbiCallBackKey", message);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
