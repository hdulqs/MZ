package com.mz.exapi.filter;

import com.mz.core.annotation.RequestLimit2;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.IpUtil;
import com.mz.util.sys.SpringContextUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.manage.remote.RemoteApiService;
import com.mz.manage.remote.model.ApiExApiApply;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tk.mybatis.mapper.util.StringUtil;

public class ApiFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // TODO Auto-generated method stub

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    //访问次数限制
    RequestLimit2 limit = new RequestLimit2();
    //访问次数
    limit.count = Integer.parseInt(PropertiesUtils.APP.getProperty("count"));
    //访问时间(秒)
    limit.time = Integer.parseInt(PropertiesUtils.APP.getProperty("time"));
    ;

    HttpServletRequest hrequest = (HttpServletRequest) request;
    HttpServletResponse hresponse = (HttpServletResponse) response;
    //请求路径
    String uri = hrequest.getRequestURI();
    //交易身份状态
    boolean info = true;
    //访问次数限制状态
    boolean timeinfo = true;

    if (uri.contains("/api")) {
      timeinfo = requestLimit(hresponse, hrequest, limit);
    }

    //交易身份验证
    if (uri.contains("/api/trade")) {
      info = verification(hrequest, hresponse);
    }
    //获取用户数据
    if (uri.contains("/api/user")) {
      info = verification(hrequest, hresponse);
    }
    if (info && timeinfo) {
      chain.doFilter(request, response);
    }
  }

  //校验身份
  private boolean verification(HttpServletRequest request, HttpServletResponse response) {
    try {
      JsonResult jsonResult = new JsonResult();
      //请求ip
      String ip = IpUtil.getIp(request);
      //请求key
      String accesskey = request.getParameter("accesskey");
      if (accesskey == null || "".equals(accesskey)) {
        response.setContentType("text/html;charset=UTF-8");// 解决中文乱码
        response.getWriter().write("{\"success\":false,\"msg\":\"accesskey为空\",\"code\":\"1003\"}");
        response.getWriter().flush();
        response.getWriter().close();
        return false;
      }
      if (ip == null || "".equals(ip)) {
        response.setContentType("text/html;charset=UTF-8");// 解决中文乱码
        response.getWriter().write("{\"success\":false,\"msg\":\"ip地址无效\",\"code\":\"1003\"}");
        response.getWriter().flush();
        response.getWriter().close();
        return false;
      }
      RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
      ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey, ip);
      if (exApiApply == null || exApiApply.equals("")) {
        response.setContentType("text/html;charset=UTF-8");// 解决中文乱码
        response.getWriter()
            .write("{\"success\":false,\"msg\":\"accesskeyip地址匹配错误\",\"code\":\"1003\"}");
        response.getWriter().flush();
        response.getWriter().close();
        return false;
      }
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }

    return true;
  }

  //访问次数限制
  public boolean requestLimit(HttpServletResponse response, HttpServletRequest request,
      RequestLimit2 limit) {
    RedisService redisService = SpringContextUtil.getBean("redisService");
    try {

      String ip = IpUtil.getIp(request);
      String url = request.getRequestURL().toString();
      String key = "req_limit_".concat(url).concat(ip);
      String value = redisService.get(key);
      if (StringUtil.isEmpty(value)) {
        redisService.save(key, "1", limit.time);
      } else {
        Long count = Long.valueOf(value);
        if (count == limit.count) {
          System.out.println("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count + "]");
          response.setContentType("text/html;charset=UTF-8");// 解决中文乱码
          response.getWriter().write(
              "{\"success\":false,\"msg\":\"用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数["
                  + limit.count + "]\",\"code\":\"1003\"}");
          response.getWriter().flush();
          response.getWriter().close();
          return false;
        }
        count++;
        String countstr = count.toString();
        redisService.save(key, countstr, limit.time);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;


  }

  @Override
  public void destroy() {
    // TODO Auto-generated method stub

  }


}
