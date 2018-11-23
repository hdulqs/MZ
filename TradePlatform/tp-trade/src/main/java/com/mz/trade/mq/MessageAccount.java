package com.mz.trade.mq;

import com.alibaba.fastjson.JSON;
import com.mz.core.mvc.model.log.AppException;
import com.mz.core.mvc.service.log.AppExceptionService;
import com.mz.trade.entrust.service.RedisAccountService;
import com.mz.trade.model.AccountResultEnum;
import com.mz.trade.redis.model.Accountadd;
import com.rabbitmq.client.Channel;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageAccount implements ChannelAwareMessageListener {

    private Logger logger = Logger.getLogger(MessageAccount.class);

    @Autowired
    private AppExceptionService appExceptionService;

    @Autowired
    private RedisAccountService redisAccountService;

    @Override
    public void onMessage(Message message, Channel channel) {
        AccountResultEnum flag = null;
        try {
            String messageBody = new String(message.getBody());
            flag = redisAccountService.accountChange(JSON.parseArray(messageBody, Accountadd.class), false);
            if (flag == AccountResultEnum.SUCCESS) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                AppException exceptionLog = new AppException();
                exceptionLog.setName(flag.getMessage());
                appExceptionService.save(exceptionLog);
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        } catch (IOException e) {
            AppException exceptionLog = new AppException();
            exceptionLog.setName("channel 响应失败");
            appExceptionService.save(exceptionLog);
        } catch (Exception e) {
            try {
                AppException exceptionLog = new AppException();
                if (flag == null) {
                    exceptionLog.setName("mq==消息报错体2==");
                } else {
                    exceptionLog.setName(flag.getMessage());
                }
                appExceptionService.save(exceptionLog);
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException a) {
                a.printStackTrace();
            }
            e.printStackTrace();
        }
    }

}
