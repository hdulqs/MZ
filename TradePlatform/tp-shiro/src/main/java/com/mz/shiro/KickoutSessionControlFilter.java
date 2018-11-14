package com.mz.shiro;


import com.mz.util.sys.ContextUtil;
import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * 同一账号最多几个会话过滤器
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2015年9月25日 下午5:00:11
 */
public class KickoutSessionControlFilter extends AccessControlFilter {


  private String kickoutUrl; //踢出后到的地址
  private boolean kickoutAfter = false; //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
  private int maxSession = 1; //同一个帐号最大会话数 默认1

  private SessionManager sessionManager;
  private Cache<String, Deque<Serializable>> cache;

  public void setKickoutUrl(String kickoutUrl) {
    this.kickoutUrl = kickoutUrl;
  }

  public void setKickoutAfter(boolean kickoutAfter) {
    this.kickoutAfter = kickoutAfter;
  }

  public void setMaxSession(int maxSession) {
    this.maxSession = maxSession;
  }

  public void setSessionManager(SessionManager sessionManager) {
    this.sessionManager = sessionManager;
  }

  public void setCacheManager(CacheManager cacheManager) {
    this.cache = cacheManager.getCache("shiro-activeSessionCache");
  }

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
      Object mappedValue) throws Exception {
    return false;
  }

  //在线session  map集合
  private static final Map<String, Deque> map = new HashMap<String, Deque>();

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
      throws Exception {
    Subject subject = getSubject(request, response);
    Object object = subject.getSession().getAttribute("user");
    if (object == null) {
      //如果没有登录，直接进行之后的流程
      return true;
    }

    Session session = subject.getSession();
    String username = (String) subject.getPrincipal();
    Serializable sessionId = session.getId();

    //TODO 同步控制
    //  Deque<Serializable> deque = cache.get(username);
    Deque<Serializable> deque = map.get(username);
    if (deque == null) {
      deque = new LinkedList<Serializable>();
      //cache.put(username, deque);
      map.put(username, deque);
    }

    //如果队列里没有此sessionId，且用户没有被踢出；放入队列
    if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
      deque.push(sessionId);
    }

    //如果队列里的sessionId数超出最大会话数，开始踢人
    while (deque.size() > maxSession) {
      Serializable kickoutSessionId = null;
      if (kickoutAfter) { //如果踢出后者
        kickoutSessionId = deque.removeFirst();
        //踢出用户时,踢掉带user属性的session
        removeSession(kickoutSessionId);
      } else { //否则踢出前者
        kickoutSessionId = deque.removeLast();
        //踢出用户时,踢掉带user属性的session
        removeSession(kickoutSessionId);
      }
      try {
        Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
        if (kickoutSession != null) {
          //设置会话的kickout属性表示踢出了
          kickoutSession.setAttribute("kickout", true);
        }
      } catch (Exception e) {//ignore exception
      }
    }

    //如果被踢出了，直接退出，重定向到踢出后的地址
    if (session.getAttribute("kickout") != null) {
      //会话被踢出了
      try {
        subject.logout();
      } catch (Exception e) { //ignore
      }
      saveRequest(request);
      WebUtils.issueRedirect(request, response, kickoutUrl);
      return false;
    }

    return true;
  }

  /**
   * 踢人时把带有user的session给踢掉,防止统计在线人数人，重名情况
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @param kickoutSessionId
   * @return: void
   * @Date :          2015年10月10日 下午1:54:52
   * @throws:
   */
  public void removeSession(Serializable kickoutSessionId) {
    //踢出用户时,踢掉带user属性的session
    SessionDAO sessionDAO = (SessionDAO) ContextUtil.getBean("sessionDAO");
    Session readSession = null;
    try {
      readSession = sessionDAO.readSession(kickoutSessionId);
      readSession.removeAttribute("user");
    } catch (Exception e) {
      System.out.println("session已退出");
    }

  }

}
