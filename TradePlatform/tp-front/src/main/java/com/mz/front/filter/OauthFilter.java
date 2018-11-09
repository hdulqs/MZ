package com.mz.front.filter;

import com.alibaba.fastjson.JSONObject;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.sys.ContextUtil;
import com.mz.manage.remote.model.User;
import com.mz.util.PropertiesUtils;
import com.mz.util.SessionUtils;
import com.mz.util.sys.SpringContextUtil;;
import java.io.IOException;
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
 * 权限过虑器
 *
 * @author CHINA_LSL
 */
public class OauthFilter implements Filter {

  public FilterConfig config;

  //权限标签
  private String oauthStr;

  @Override
  public void destroy() {
    this.config = null;
  }

  /**
   * 如果包含返回true
   */
  public static boolean isContains(String container, String[] regx) {
    for (int i = 0; i < regx.length; i++) {
      if (container.indexOf(regx[i]) != -1) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest hrequest = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(
        (HttpServletResponse) response);

    //String allowFilter = config.getInitParameter("allowFilter"); // 过滤资源后缀参数
    String allowFilter = oauthStr; // 过滤资源后缀参数
    String redirectPath =
        ContextUtil.getAppPath(hrequest) + config.getInitParameter("redirectPath");// 没有登陆转向页面
    String disabletestfilter = config.getInitParameter("disabletestfilter");// 过滤器是否有效
    RedisService redisService = SpringContextUtil.getBean("redisService");

    //请求路径
    String uri = hrequest.getRequestURI();

    String contextPath = hrequest.getContextPath();
    String replace = uri.replace(contextPath, "");

    if (uri.contains("/v2/api-docs")) {
      chain.doFilter(request, response);
      return;
    }

    if (uri.contains("/mobile")) {
      String tokenId = request.getParameter("tokenId");
      if (tokenId != null) {
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {
          String username = JSONObject.parseObject(value).getString("mobile");
          redisService.save("online:username:" + username, "1", 1800);
        }
      }
      chain.doFilter(request, response);
      return;
    }

    if ("/".equals(replace)) {
      wrapper.sendRedirect(redirectPath);
      return;
    }

    if (disabletestfilter.toUpperCase().equals("Y")) { // 过滤无效
      chain.doFilter(request, response);
      return;
    }

    String[] allowList = allowFilter.split(",");
    String[] newList = new String[allowList.length];

    //封装请求全路径
    String url = ContextUtil.getAppPath(hrequest) + replace;
    for (int i = 0; i < allowList.length; i++) {
      newList[i] = ContextUtil.getAppPath(hrequest) + allowList[i];
    }

    //全路径对比
    if (this.isContains(url, newList)) {// 只对指定过滤参数后缀进行过滤

      //start ---北京处理跳号问题 2018.4.21---
			/*User user = null;
			String tokenId = "";
			Cookie[] cos = hrequest.getCookies();
			if(cos!=null){
				for(Cookie c : cos){
					if("tokenId".equals(c.getName())){
						tokenId = c.getValue();
					}
				}
			}
			user = (User)SessionUtils.getUser(hrequest);*/
      User user = SessionUtils.getUser(hrequest);
      //end ---北京处理跳号问题 2018.4.21---

      if (user != null) {
        hrequest.setAttribute("phone", user.getPhone());
        hrequest.setAttribute("passDate", user.getPassDate());
        hrequest.setAttribute("user", user);
        hrequest.setAttribute("googleKey", user.getGoogleKey());
        hrequest.setAttribute("googleState", user.getGoogleState());
        hrequest.setAttribute("userName", user.getUsername());
        hrequest.setAttribute("trueName", user.getTruename());
        hrequest.setAttribute("states", user.getStates());
        hrequest.setAttribute("isDelete", user.getIsDelete());
        hrequest.setAttribute("phoneState", user.getPhoneState());

        //add by zongwei 20180709 加 隐藏中间的手机号码
        if (user.getPhone() != null) {
          String phone = user.getPhone();
          phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
          hrequest.setAttribute("phoneHide", phone);
        }

        redisService.save("online:username:" + user.getUsername(), "1", 1800);

        //重新设置时常
        //redisService.save("mobile:"+tokenId, "{\"mobile\":\""+user.getUsername()+"\",\"user\":"+JSON.toJSON(user).toString()+"}", 10800);

      }
      if (user == null) {
        String XRequested = hrequest.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(XRequested)) {
          resp.addHeader("nologin", "true");
          resp.getWriter().write("{\"loginUrl\":\"" + redirectPath
              + "\",\"market\":\"nomsg\",\"nologinkey\":\"hrynologinkeye993f9d7322e4fcab554fe3741d46aca\"}");
        } else {
          wrapper.sendRedirect(redirectPath);
        }

        return;
      } else {
        chain.doFilter(request, response);
        return;
      }

    }
    chain.doFilter(request, response);
    return;


  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    config = filterConfig;
    oauthStr = PropertiesUtils.getOuathStr();
  }

}
