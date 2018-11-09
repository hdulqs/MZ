/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月29日 下午4:13:42
 */
package com.mz.core.dwrcomet;

import com.mz.oauth.user.model.AppUser;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Liu Shilei
 * @Date : 2015年9月29日 下午4:13:42
 */
public class DWRScriptSessionListener implements ScriptSessionListener {

		// 维护一个Map key为session的Id， value为ScriptSession对象
		public static final Map<String, ScriptSession> scriptSessionMap = new HashMap<String, ScriptSession>();
	
		/**
		 * ScriptSession创建事件
		 */
		@Override
		public void sessionCreated(ScriptSessionEvent event) {
			WebContext webContext = WebContextFactory.get();
	/*		Cookie[] cookies = webContext.getHttpServletRequest().getCookies();
			Cookie DWRSESSIONID = null;
			for(Cookie cookie : cookies){
				if("DWRSESSIONID".equals(cookie.getName())){
					DWRSESSIONID = cookie;
				}
			}*/
			HttpSession session = webContext.getSession();
			if(session==null){
				return ;
			}
			
			AppUser user = (AppUser) session.getAttribute("user");
			ScriptSession scriptSession = event.getSession();
			if(user==null){
				return ;
			}
			scriptSessionMap.put(user.getId().toString(), scriptSession); // 添加scriptSession
			scriptSession.setAttribute("userId", user.getId());
			scriptSession.setAttribute("sessionId", session.getId());
			
			System.out.println("session: " + session.getId());
			System.out.println("userId: " + user.getId());
			System.out.println("scriptSession: "+ scriptSession.getId() + "is created!");
		}
	
		/**
		 * ScriptSession销毁事件
		 */
		@Override
		public void sessionDestroyed(ScriptSessionEvent event) {
			WebContext webContext = WebContextFactory.get();
			HttpSession session = webContext.getSession();
			AppUser user = (AppUser) session.getAttribute("user");
			if(user==null){
				return;
			}
			ScriptSession scriptSession = scriptSessionMap.remove(user.getId()); // 移除scriptSession
			System.out.println("session: " + session.getId());
			if(scriptSession!=null){
				System.out.println("scriptSession: "+ scriptSession.getId() + "is destroyed!");
			}
		}
	
		/**
		 * 获取所有ScriptSession
		 */
		public static Collection<ScriptSession> getScriptSessions() {
			return scriptSessionMap.values();
		}

}
