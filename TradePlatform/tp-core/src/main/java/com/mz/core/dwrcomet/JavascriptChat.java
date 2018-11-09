/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月9日10:23:02
 */
package com.mz.core.dwrcomet;


/**
 * 聊天发送引擎
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年10月9日 上午10:23:33
 */
public class JavascriptChat {/*
	
	*//**
	 * 页面初始化推送scriptsession  初始化当前的sessionId到scriptsession属性中
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param sendUserId
	 * @return: void 
	 * @Date :          2015年10月8日 下午7:23:01   
	 * @throws:
	 *//*
	public void onPageLoad(String sendUserId) {
		// 获取当前的ScriptSession
		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
		scriptSession.setAttribute("sendUserId", sendUserId);
		// 为每个user 创建一个聊天记录Map
		System.out.println("初始化发送者聊天记录map=userId"+sendUserId);
	}
	
	*//**
	 * 封装消息记录
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param chatmessage  发送内容
	 * @param:    @param sendUserId   发送者
	 * @param:    @param receiveUserId  接收者
	 * @param:    @return
	 * @return: LinkedList<Message> 
	 * @Date :          2015年10月9日 上午10:32:31   
	 * @throws:
	 *//*
	private List<AppMessage> setMessage(String chatmessage, String sendUserId ,String receiveUserId){
		Date date = new Date();
		
//		AppMessage appMessage = new AppMessage();
//		appMessage.setDate(date);
//		appMessage.setSendUserId(sendUserId);
//		appMessage.setReceiveUserId(receiveUserId);
//		appMessage.setText(chatmessage);
//		
//		AppMessage appMessage2 = new AppMessage();
//		appMessage2.setDate(date);
//		appMessage2.setSendUserId(receiveUserId);
//		appMessage2.setReceiveUserId(sendUserId);
//		appMessage2.setText(chatmessage);
//		
//		//保存两条聊天记录
//		MongoUtil<AppMessage, Long> mongoUtil = new MongoUtil<AppMessage, Long>(AppMessage.class);
//		appMessage.setId(mongoUtil.autoincrementId());
//		mongoUtil.save(appMessage);
//		
//		appMessage2.setId(mongoUtil.autoincrementId());
//		mongoUtil.save(appMessage2);
//		
//	    AppMessageService appMessageService = (AppMessageService)ContextUtil.getBean("appMessageService");
//		List<AppMessage> list = appMessageService.find(sendUserId,receiveUserId);
		
		MongoQueryFilter mongoQueryFilter = new MongoQueryFilter();
		mongoQueryFilter.addFilter("sendUserId=", sendUserId);
		mongoQueryFilter.addFilter("receiveUserId=", receiveUserId);
//		List<AppMessage> list = mongoUtil.find(mongoQueryFilter);
		
		return null;
	}
	
	
	*//**
	 * 发送消息
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param chatmessage 发送消息
	 * @param:    @param sendUserId 发送人ID
	 * @param:    @param receiveUserId 接收人ID
	 * @return: void 
	 * @Date :          2015年10月8日 下午4:52:10   
	 * @throws:
	 *//*
	public void addMessage(String chatmessage, String sendUserId ,String receiveUserId) {
		List<AppMessage> linkedList = setMessage(chatmessage, sendUserId, receiveUserId);
		Runnable run = new ChatRunnable(linkedList,sendUserId,receiveUserId);
		// 执行推送
		Browser.withAllSessions(run);
	}
	
	*//**
	 * 挤出前一个登录人时的推送消息方法
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param chatmessage
	 * @param:    @param receiveUserId
	 * @return: void 
	 * @Date :          2015年10月10日 下午2:40:39   
	 * @throws:
	 *//*
	public void kitOutMessage(String chatmessage,String receiveUserId){
		KickOutRunnable run = new KickOutRunnable(receiveUserId);
		// 执行推送
		Browser.withAllSessions(run);
	}
	
	
	*//**
	 * 过滤实现重写
	 * <p> TODO</p>
	 * @author:         Liu Shilei 
	 * @Date :          2015年10月5日 下午5:09:54
	 *//*
	class ScriptSessionFilterWrap implements ScriptSessionFilter {

		private String userId = null;

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public ScriptSessionFilterWrap() {
		};

		public ScriptSessionFilterWrap(String userId) {
			this.userId = userId;
		}

		@Override
		public boolean match(ScriptSession session) {
			String session_userId = (String) session.getAttribute("sendUserId");
			System.out.println(session_userId);
			
			System.out.println("两者比较下"+userId.equals(session_userId));
			return userId.equals(session_userId);
		}

	}
	
	class KickOutRunnable implements Runnable{
		//接收人ID
		private String kickoutSessionId = null;
		//script消息
		private ScriptBuffer script = new ScriptBuffer();
		
		public KickOutRunnable(String kickoutSessionId){
			this.kickoutSessionId = kickoutSessionId;
		}

		@Override
		public void run() {
			// 设置要调用的 js及参数
			script.appendCall("kitOutMessage", "你已在别出登录");
			// 得到所有ScriptSession
			Collection<ScriptSession> sessions = DWRScriptSessionListener.getScriptSessions();
			// 遍历每一个ScriptSession
			for (ScriptSession scriptSession : sessions) {
				String sessionId = (String) scriptSession.getAttribute("sessionId");
				if(kickoutSessionId.equals(sessionId)){
					scriptSession.addScript(script);
				}
			}
		}

	}
	
	
	*//**
	 * 推送聊天记录线程重写
	 * <p> TODO</p>
	 * @author:         Liu Shilei 
	 * @Date :          2015年10月5日 下午5:10:17
	 *//*
	class ChatRunnable implements Runnable {
		
		//推送的消息队列
		private List<AppMessage> messages = null;
		//接收人ID
		private String receiveUserId = null;
		//发送人ID
		private String sendUserId = null;
		//script消息
		private ScriptBuffer script = new ScriptBuffer();
		
		public ChatRunnable(){};
		
		public ChatRunnable(List<AppMessage> messages,String sendUserId,String receiveUserId){
			if(messages.size()>8){
				this.messages = messages.subList(messages.size()-8, messages.size());
			}else{
				this.messages = messages;
			}
			this.receiveUserId = receiveUserId;
			this.sendUserId = sendUserId;
		}
		
		public ChatRunnable(List<AppMessage> messages){
			this.messages = messages;
		}


		@Override
		public void run() {
			// 设置要调用的 js及参数
			script.appendCall("receiveMessages", messages);
			// 得到所有ScriptSession
			Collection<ScriptSession> sessions = DWRScriptSessionListener.getScriptSessions();
			// 遍历每一个ScriptSession
			for (ScriptSession scriptSession : sessions) {
				String session_userId = (String) scriptSession.getAttribute("sendUserId");
				if(receiveUserId.equals(session_userId)||sendUserId.equals(session_userId)){
					scriptSession.addScript(script);
				}
			}
			
		}

	}

*/}
