package com.mz.amqp.common;

import com.mz.App;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Frank on 2018/11/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class})
public class AmqpUtilsTest {

  @Autowired
  AmqpUtils amqpUtils;
  @Autowired
  Producer producer;

  @Test
  public void addQueue() {
    String tradeType = "BTC";
    Message message = new Message();
    message.setKey("1");
    message.setTradeMoney(new BigDecimal(100));
    message.setTradeType(tradeType);
    producer.handle(message, tradeType);
    amqpUtils.addQueue(tradeType, tradeType);
  }
}