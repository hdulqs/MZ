/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0
 * @Date:        2015年9月16日 上午11:04:39
 */
package com.mz.core.interceptors;

import com.mz.core.constant.StringConstant;
import com.mz.core.mvc.model.AppConfig;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 设置通用数据的Interceptor
 * <p/>
 * 使用Filter时 文件上传时 getParameter时为null 所以改成Interceptor
 * <p/>
 * 1、ctx---->request.contextPath 2、currentURL---->当前地址
 * 
 * @author: Yuan Zhicheng
 * @version: V1.0
 * @Date: 2015年9月16日 上午11:04:39
 */
public class CommonDataInterceptor extends HandlerInterceptorAdapter {
	private static final String[] DEFAULT_EXCLUDE_PARAMETER_PATTERN = new String[] {
			"\\&\\w*page.pn=\\d+", "\\?\\w*page.pn=\\d+",
			"\\&\\w*page.size=\\d+" };
	private final PathMatcher pathMatcher = new AntPathMatcher();
	private String[] excludeParameterPatterns = DEFAULT_EXCLUDE_PARAMETER_PATTERN;
	private String[] excludeUrlPatterns = null;

	private static List<AppConfig> appConfigList = null;

	public void setExcludeParameterPatterns(String[] excludeParameterPatterns) {
		this.excludeParameterPatterns = excludeParameterPatterns;
	}

	public void setExcludeUrlPatterns(final String[] excludeUrlPatterns) {
		this.excludeUrlPatterns = excludeUrlPatterns;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (isExclude(request)) {
			return true;
		}

		/*
		 * base URL
		 */
		if (request.getAttribute(StringConstant.BASE_PATH) == null) {
			request.setAttribute(StringConstant.BASE_PATH, getBasePath(request));
		}

		/*
		 * 根路径
		 */
		if (request.getAttribute(StringConstant.CONTEXT_PATH) == null) {
			request.setAttribute(StringConstant.CONTEXT_PATH,
					request.getContextPath());
		}

		/*
		 * 静态资源 地址
		 */
		if (request.getAttribute(StringConstant.STATIC_CONTEXT_PATH) == null) {
			request.setAttribute(StringConstant.STATIC_CONTEXT_PATH,
					getStaticPath(StringConstant.STATIC_CONTEXT_PATH, request));
		}

		if (request.getAttribute(StringConstant.CURRENT_URL) == null) {
			request.setAttribute(StringConstant.CURRENT_URL,
					extractCurrentURL(request, true));
		}

		if (request.getAttribute(StringConstant.NO_QUERYSTRING_CURRENT_URL) == null) {
			request.setAttribute(StringConstant.NO_QUERYSTRING_CURRENT_URL,
					extractCurrentURL(request, false));
		}
		/*
		 * 返回的 url
		 */
		if (request.getAttribute(StringConstant.BACK_URL) == null) {
			request.setAttribute(StringConstant.BACK_URL,
					extractBackURL(request));
		}

		// 初始化 key 为request_开头的数据

		initAppConfig(request, "request_");

		return true;
	}

	private boolean isExclude(final HttpServletRequest request) {
		if (excludeUrlPatterns == null) {
			return false;
		}

		for (String pattern : excludeUrlPatterns) {
			if (pathMatcher.match(pattern, request.getServletPath())) {
				return true;
			}
		}

		return false;
	}

	private String extractCurrentURL(HttpServletRequest request,
			boolean needQueryString) {
		String url = request.getRequestURI();
		String queryString = request.getQueryString();

		if (!StringUtils.isEmpty(queryString)) {
			queryString = "?" + queryString;

			for (String pattern : excludeParameterPatterns) {
				queryString = queryString.replaceAll(pattern, "");
			}

			if (queryString.startsWith("&")) {
				queryString = "?" + queryString.substring(1);
			}
		}

		if (!StringUtils.isEmpty(queryString) && needQueryString) {
			url = url + queryString;
		}

		return getBasePath(request) + url;
	}

	/**
	 * 上一次请求的地址 1、先从request.parameter中查找BackURL 2、获取header中的 referer
	 *
	 * @param request
	 * @return
	 */
	private String extractBackURL(HttpServletRequest request) {
		String url = request.getParameter(StringConstant.BACK_URL);

		// 使用Filter时 文件上传时 getParameter时为null 所以改成Interceptor
		if (StringUtils.isEmpty(url)) {
			url = request.getHeader("Referer");
		}

		if (!StringUtils.isEmpty(url)
				&& (url.startsWith("http://") || url.startsWith("https://"))) {
			return url;
		}

		if (!StringUtils.isEmpty(url)
				&& url.startsWith(request.getContextPath())) {
			url = getBasePath(request) + url;
		}

		return url;
	}

	/**
	 *
	 * <p>
	 * 获取URL路径
	 * </p>
	 * 
	 * @author: Yuan Zhicheng
	 * @param: @param req
	 * @param: @return
	 * @return: String
	 * @Date : 2015年10月14日 下午2:18:16
	 * @throws:
	 */
	private String getBasePath(HttpServletRequest req) {
		StringBuffer baseUrl = new StringBuffer();
		String scheme = req.getScheme();
		int port = req.getServerPort();

		// String servletPath = req.getServletPath ();
		// String pathInfo = req.getPathInfo ();
		baseUrl.append(scheme); // http, https
		baseUrl.append("://");
		baseUrl.append(req.getServerName());

		if ((scheme.equals("http") && (port != 80))
				|| (scheme.equals("https") && (port != 443))) {
			baseUrl.append(':');
			baseUrl.append(req.getServerPort());
		}

		return baseUrl.toString();
	}

	/**
	 *
	 * <p>
	 * 获取Key 对应的静态资源的路径 ； 这里一般获取js css img 等静态资源的baseUrl,该url保存在数据库 中；
	 * 目的:实现静态文件的分离部署
	 * </p>
	 * 
	 * @author: Yuan Zhicheng
	 * @param: key
	 * @return: 静态资源URL 路径
	 * @Date : 2015年10月14日 下午2:19:34
	 * @throws:
	 */
	private String getStaticPath(String key, HttpServletRequest req) {
		String path = "";

		// 从数据库中获取
		// path=AppSetingService.findByKey(key);
		path = "http://localhost/hurong_static";
		if (!StringUtils.isEmpty(path)) {
			return path;
		}

		return req.getContextPath();
	}

	/**
	 * <p>
	 * 初始化appconfig
	 * </p>
	 * 
	 * @author: Yuan Zhicheng
	 * @param: @param req
	 * @param: @param filterKey 需要过滤的的key eg:request_ 代表 要放到作用域中的值
	 * @return: void
	 * @Date : 2015年11月4日 下午4:54:15
	 * @throws:
	 */
	private void initAppConfig(HttpServletRequest req, String filterKey) {/*
		// appConfigList 为NULL 时进行查询 提高查询效率
		if (StringUtils.isEmpty(appConfigList)) {
			AppConfigService appConfig = (AppConfigService) ContextUtil
					.getBean("appConfigService");
			
			QueryFilter filter = new QueryFilter(AppConfig.class);
			filter.addFilter("configkey=", filterKey);
			appConfigList = appConfig.find(filter.setNosaas());
			
		} else {
			for (AppConfig config : appConfigList) {
				req.setAttribute(config.getConfigkey(), config.getValue());
			}
		}
	*/}
}
