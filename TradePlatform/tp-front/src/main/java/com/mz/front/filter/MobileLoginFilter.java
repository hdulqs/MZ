package com.mz.front.filter;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mz.redis.common.utils.RedisService;
import com.mz.util.sys.SpringContextUtil;;

public class MobileLoginFilter implements Filter{
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;  
        HttpServletResponse httpResponse = (HttpServletResponse) response;  
        RedisService redisService = (RedisService) SpringContextUtil.getBean("redisService");
        
        String path = httpRequest.getRequestURI();
        if(path.contains("/mobile/nouser")){
        	chain.doFilter(request, response);
        	return ;
        }
        
        // app退出问题
        String uuid = httpRequest.getParameter("tokenId");
        String str = redisService.get("mobile:"+uuid);
        if(str!=null){
        	redisService.save("mobile:" + uuid,str,86400);  
        	chain.doFilter(request, response);
        }else{
        	if(!path.contains("/mobile/user")){
        		chain.doFilter(request, response);
            	return ;
        	}else{
        		try {
            		httpResponse.setContentType("text/html;charset=UTF-8");// 解决中文乱码
                	httpResponse.getWriter().write("{\"success\":false,\"msg\":\"请先登录\"}");
                	httpResponse.getWriter().flush();
                	httpResponse.getWriter().close();
    			} catch (Exception e) {
    				// TODO: handle exception
    				e.printStackTrace();
    			}
        	}
        }
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
