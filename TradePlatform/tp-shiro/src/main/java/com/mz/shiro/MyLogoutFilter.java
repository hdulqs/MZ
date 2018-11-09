/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年12月28日 下午5:05:32
 */
package com.mz.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

/** 
 * 重写退出方法
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年12月28日 下午5:05:32 
 */
public class MyLogoutFilter extends LogoutFilter{
	
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response)throws Exception {
        Subject subject = getSubject(request, response);
        subject.getSession().removeAttribute("user");
        String id = subject.getSession().getId().toString();
/*    	try {
			ShardedJedisPool shardedJedisPool = (ShardedJedisPool) ContextUtil.getBean("shardedJedisPool");
			ShardedJedis jedis = null;
			jedis = shardedJedisPool.getResource();
			jedis.del(id);
		} catch (Exception e) {
			LogFactory.info("停止会话，删除session出错");
		}*/
    	
        
        String redirectUrl = getRedirectUrl(request, response, subject);
        //退出后重定向地址
        String backUrl = request.getParameter("backUrl");
        try {
            subject.logout();
            
            //String appuserprefix = CookiesUtils.getValue((HttpServletRequest)request, "appuserprefix");
            
            //如果重定向地址为空  跳转权限中心默认的地址
            if(StringUtils.isEmpty(backUrl)){
            	issueRedirect(request, response, redirectUrl);
            }else{//否则跳到backUrl
            	issueRedirect(request, response, backUrl);
            }
            
        } catch (SessionException ise) {
        }
    
        
        return false;
    }
	
}
