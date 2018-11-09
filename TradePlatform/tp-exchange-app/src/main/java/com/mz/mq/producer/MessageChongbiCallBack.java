package com.mz.mq.producer;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.alibaba.fastjson.JSON;

import com.mz.util.sys.ContextUtil;
import com.mz.manage.remote.RemoteInterfaceCallbackService;
import com.mz.manage.remote.model.LmcTransfer;

/**
 * 充币监听
 * @author CHINA_LSL
 *
 */
public class MessageChongbiCallBack implements MessageListener {
	private Logger logger = Logger.getLogger(MessageChongbiCallBack.class);

	@Override
	public void onMessage(Message message) {
		String str = new String(message.getBody());
		logger.info("@@@receives chongbi rabbitmq message======="+str);
		
		LmcTransfer lmcTransfer = JSON.parseObject(str, LmcTransfer.class);
		RemoteInterfaceCallbackService remoteInterfaceCallbackService = (RemoteInterfaceCallbackService) ContextUtil.getBean("remoteInterfaceCallbackService");
		remoteInterfaceCallbackService.lmcTransferCallBack(lmcTransfer);
		
	}

}