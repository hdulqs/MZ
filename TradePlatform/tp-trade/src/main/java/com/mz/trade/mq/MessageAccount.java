package com.mz.trade.mq;

import com.mz.core.mvc.model.log.AppException;
import com.mz.core.mvc.service.log.AppExceptionService;
import com.mz.trade.entrust.service.TradeService;
import com.mz.util.sys.ContextUtil;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

public class MessageAccount implements ChannelAwareMessageListener {

  private Logger logger = Logger.getLogger(MessageAccount.class);

  @Override
  public void onMessage(Message message, Channel channel) {
    TradeService tradeService = (TradeService) ContextUtil.getBean("tradeService");
    try {
      String messageBody = new String(message.getBody());
      if (messageBody.equals("null")) {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        return;
      }
      Boolean flag = tradeService.accountaddQueue(messageBody);
      if (!flag) {
        AppException exceptionLog = new AppException();
        exceptionLog.setName("mq==消息报错体==");
        AppExceptionService appExceptionService = (AppExceptionService) ContextUtil
            .getBean("appExceptionService");
        appExceptionService.save(exceptionLog);
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);


      }
    } catch (Exception e) {

      try {
        AppException exceptionLog = new AppException();
        exceptionLog.setName("mq==消息报错体2==");
        AppExceptionService appExceptionService = (AppExceptionService) ContextUtil
            .getBean("appExceptionService");
        appExceptionService.save(exceptionLog);
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
      } catch (IOException a) {
        // TODO Auto-generated catch block
        a.printStackTrace();
      }
    }

    try {
      channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    } catch (IOException e1) {
      logger.info("mq==channel.basicAck==确认失败");
      e1.printStackTrace();
    }


  }

}
