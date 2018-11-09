package com.mz.front.filter;

import com.mz.util.PropertiesUtils;
import com.mz.util.common.BaseConfUtil;
import com.mz.util.common.Constant;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 网站开关过滤器
 * 
 * @author CHINA_LSL
 *
 */
public class SiteSwitchFilter implements Filter {

	public FilterConfig config;
	
	//权限标签
	private String fixUrl;

	@Override
	public void destroy() {
		this.config = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest hrequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse=(HttpServletResponse)response;
		
		
		String fix = fixUrl; // 过滤资源后缀参数
		String disabletestfilter = config.getInitParameter("disabletestfilter");// 过滤器是否有效
		String noFilter = config.getInitParameter("noFilter"); // 过滤资源后缀参数
		String[] allowList = noFilter.split(",");
		//请求路径
		String uri = hrequest.getRequestURI();
		//获取网站开关
		Map<String,Object> baseMap=BaseConfUtil.getConfigByKey(Constant.baseConfig);
		String website_switch=(String) baseMap.get(Constant.WEBSITESWITCH);

		//过滤器作废或者如果网站开关打开，则直接放行
		if("Y".equals(disabletestfilter)||"0".equals(website_switch)){
			chain.doFilter(request, response);
			return;
		}else{
			for(String l:allowList){
				if(uri.contains(l)){//如果在允许数组中，则放行
					chain.doFilter(request, response);
					return;
				}
			}
			//如果没有再放行队列，则拦截,重定向
			servletResponse.sendRedirect(fix);
			return;
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		config = filterConfig;
		fixUrl = PropertiesUtils.getfixUrl();
	}

}
