package com.mz.front.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.mz.manage.remote.model.User;

public class SessionListener implements HttpSessionListener ,HttpSessionAttributeListener {
	
    public final static String USER_NAME = "user";

    //用户与session的关系
    private static final Map<String,HttpSession> SESSIONS = new HashMap<String,HttpSession>();

	public static final Map<String,String> ONLINE_USER = new HashMap<String,String>();
    
    //sessionid 与用户的关系
    public static final Map<String, String> SESSIONID_USER = new HashMap<String, String>();
	
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		if(USER_NAME.equals(event.getName())){
			//获得用户
			User user = (User) event.getValue();
			//绑定sessionId和用户名
			SESSIONID_USER.put(event.getSession().getId(), user.getUsername());
			ONLINE_USER.put(user.getUsername(),"1");
			//获得之前的session存在不存在
			HttpSession httpSession = SESSIONS.get(user.getUsername());
			//如果存在强制下线
			if(httpSession!=null){
				httpSession.removeAttribute(USER_NAME);
			}
			//把新登录的session加入
			SESSIONS.put(user.getUsername(), event.getSession());
			System.out.println("当前在线人数【attributeAdded】:"+SESSIONS.size());
		}
		
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		if(USER_NAME.equals(event.getName())){
			 String sessionId = event.getSession().getId();  
			 String userName = SESSIONID_USER.get(sessionId);
			 SESSIONID_USER.remove(sessionId);
			 SESSIONS.remove(userName);
			 ONLINE_USER.remove(userName);
			 //System.out.println("当前在线人数【attributeRemoved】:"+SESSIONS.size());
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		HttpSession session = event.getSession();
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		User user = (User) session.getAttribute(USER_NAME);
		if(user != null){
			ONLINE_USER.put(user.getUsername(),"1");
		}
		//System.out.println("在线:"+userName);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		
		 String sessionId = se.getSession().getId();  
		 String userName = SESSIONID_USER.get(sessionId);
		 SESSIONID_USER.remove(sessionId);
		 SESSIONS.remove(userName);
		 ONLINE_USER.remove(userName);
		 //System.out.println("当前在线人数【sessionDestroyed】:"+SESSIONS.size());
		
	}

}
