package com.mz.amqp.common;

import java.io.UnsupportedEncodingException;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * Created by Frank on 2018/11/5.
 */
public class Consumer implements MessageListener {

  @Override
  public void onMessage(Message message)  {
    try {
      System.out.println(new String(message.getBody(), CharEncoding.UTF_8));
      //在这里可以通过message里的交易类型不同到交易处理类
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
}
