package com.mz.front.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.sys.SpringContextUtil;;

public class HtmlInterceptor extends HandlerInterceptorAdapter {

	private static Logger log = Logger.getLogger(HtmlInterceptor.class);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
			
		//请求路径
		String requestUrl = request.getRequestURL().toString();
		requestUrl += HttpServletRequestUtils.getParamsMd5(request);
		//路径加密
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(requestUrl.getBytes());
		
		//获得静态页生成时间
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String htmlPath = redisService.get("static_html:"+md5DigestAsHex);
		if(!StringUtils.isEmpty(htmlPath)){
			request.getRequestDispatcher(htmlPath).forward(request, response);
			return false;
		}
		
		
		return true;
	}

}