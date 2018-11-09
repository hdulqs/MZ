/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月28日 下午2:17:01
 */
package com.mz.core.mvc.controller.chat;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.dwrcomet.Message;
import com.mz.core.mvc.service.log.AppExceptionService;
import com.mz.core.mvc.model.log.AppException;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Liu Shilei
 * @Date : 2015年9月28日 下午2:17:01
 */
@Controller
@RequestMapping("/chat")
public class ChatController {
	
	
	
	@Resource
	private AppExceptionService appExceptionService;
	
	@MethodName(name="加载聊天记录")
	@RequestMapping("/loadChatLog")
	@ResponseBody
	public List<Message> loadChatLog(String receiveUserId) {
		
		
		AppException appException = new AppException();
		appException.setCreated(new Date());
		appException.setModified(new Date());
		appException.setName("大家好");
	//	appExceptionService.save(appException);
		
//		AppException selectOne1 = appExceptionService.selectOne1();
	//	AppException appException2 = appExceptionService.get(Long.valueOf(5));
		
	//	appException2.setName("大家不好");
		
		return null;/*
		AppUser user = ContextUtil.getCurrentUser();
		Map<String, LinkedList<Message>> sendMap = JavascriptChat.chatMap.get(user.getId().toString());
		
		//一丶为send  to receive 创建list
		LinkedList<Message> linkedList = sendMap.get(receiveUserId);
		if(linkedList==null){//第一次加载时，为null初始化一个list到map中
			linkedList = new LinkedList<Message>();
			sendMap.put(receiveUserId, linkedList);
		}
		
		//二丶检查receive to send 方的map list是否存在
		Map<String, LinkedList<Message>> receiveMap = JavascriptChat.chatMap.get(receiveUserId);
		if(receiveMap==null){
			receiveMap = new HashMap<String, LinkedList<Message>> ();
			JavascriptChat.chatMap.put(receiveUserId, receiveMap);
		}else{
			LinkedList<Message> linkedList2 = receiveMap.get(user.getId().toString());
			if(linkedList2==null){
				receiveMap.put(user.getId().toString(), new LinkedList<Message>());
				JavascriptChat.chatMap.put(receiveUserId, receiveMap);
			}
		}
		
		if(linkedList.size()>8){
			List<Message> subList = linkedList.subList(linkedList.size()-8, linkedList.size());
			return subList;
		}
		return linkedList;
	*/}
	
	
	
	
	
		
}
