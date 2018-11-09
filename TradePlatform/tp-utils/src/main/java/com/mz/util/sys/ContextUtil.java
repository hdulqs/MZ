/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Gao Mimi
 * @version: V1.0
 * @Date: 2015年10月9日 下午12:04:20
 */
package com.mz.util.sys;

import com.mz.customer.user.model.AppCustomer;
import com.mz.oauth.user.model.AppUser;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.rpc.RpcContext;

/**
 * <p>
 * 全局的系统层面的工具类，供业务系统调用
 * </p>
 *
 * @author: Gao Mimi
 * @Date : 2015年10月10日 下午2:06:00
 */

public class ContextUtil {

  /**
   * //国际站  中国站区别
   */
  public static final String CN = "cn";  //中国站
  public static final String EN = "en";  //国际站

  public static final String CNY = "cny";//人民币账户
  public static final String USD = "usd";//美民币账户

  public static final String VIP = "vip";//现货会员体系，后台登录用户的appuserprefix类型


  /**
   * 获得远程调用时的currencyType
   */
  public static String getRemoteCurrencyType() {
    return RpcContext.getContext().getAttachment("currencyType");
  }

  /**
   * 获得远程调用时的website
   */
  public static String getRemoteWebsite() {
    return RpcContext.getContext().getAttachment("website");
  }

  /**
   * 设置远程调用时传的账户类型参数
   */
  public static void setRemoteCurrencyType() {
    RpcContext.getContext().setAttachment("currencyType", getCurrencyType());
  }

  /**
   * 设置远程调用时传的账户类型参数
   */
  public static void setRemoteWebsite() {
    RpcContext.getContext().setAttachment("website", getWebsite());
  }

  /**
   * 获得默认交易账户类型
   */
  public static String getCurrencyType() {
    if (CN.equals(getWebsite())) {
      return CNY;
    }
    return USD;
  }

  /**
   * 获得站点类型
   */
  public static String getWebsite() {
    try {
      HttpServletRequest request = getRequest();
      if (request != null) {
        String host = request.getHeader("HOST");
        if (!StringUtils.isEmpty(host)) {
          //如果host不为空并且host包含app.website_en的值，则说明访问的是国际站
          if (host.contains(PropertiesUtils.APP.getProperty("app.website_en"))) {
            return EN;
          } else {
            return CN;
          }
        } else {
          return CN;
        }
      }
      return CN;
    } catch (Exception e) {
    }
    return CN;
  }

  /**
   * 获得saasId
   * <p>
   * TODO
   * </p>
   *
   * @author: Liu Shilei
   * @param: @return
   * @return: String
   * @Date : 2015年11月24日 下午5:47:28
   * @throws:
   */
  public static String getSaasId() {
    String systemSaasId = "hurong_system";
    try {
      Subject subject = SecurityUtils.getSubject();
      if (subject != null) {
        Session session = subject.getSession();
        if (session != null) {
          String saasId = (String) session.getAttribute("saasId");
          if (saasId != null) {
            return saasId;
          }
        }
      }
    } catch (Exception e) {

    }
    return systemSaasId;

    // return (String)
    // SecurityUtils.getSubject().getSession().getAttribute("saasId");
  }

  /**
   * 判断是否有用户登录
   * <p>
   * TODO
   * </p>
   *
   * @author: Liu Shilei
   * @param: @return
   * @return: boolean
   * @Date : 2015年11月25日 上午10:40:35
   * @throws:
   */
  public static boolean isLogin() {
    try {
      AppUser user = (AppUser) SecurityUtils.getSubject().getSession()
          .getAttribute("user");
      if (user == null) {
        return false;
      }
      return true;
    } catch (UnavailableSecurityManagerException e) {
      return false;
    }
  }

  /**
   * 获得session
   * <p>
   * TODO
   * </p>
   *
   * @author: Liu Shilei
   * @param: @return
   * @return: Session
   * @Date : 2015年12月3日 下午6:48:21
   * @throws:
   */
  public static Session getSession() {
    return SecurityUtils.getSubject().getSession();
  }


  /**
   * 前台获得登录用户
   * <p>
   * TODO
   * </p>
   *
   * @author: Liu Shilei
   * @param: @return
   * @return: AppCustomer
   * @Date : 2016年3月29日 上午11:57:48
   * @throws:
   */
  public static AppCustomer getCurrentAppCustomer() {
    AppCustomer user = (AppCustomer) SecurityUtils.getSubject().getSession().getAttribute("user");
    return user;
  }

  /**
   * 从上下文取得当前用户
   */
  public static AppUser getCurrentUser() {
    try {
      AppUser user = (AppUser) SecurityUtils.getSubject().getSession().getAttribute("user");
      return user;
    } catch (Exception e) {
      LogFactory.info("没找到后台用户");
    }
    return null;
  }


  /**
   * 获取appuser appuserprefix值 默认返回hry
   */
  public static String getCurrentUserType() {
    AppUser currentUser = getCurrentUser();
    if (currentUser != null) {
      return currentUser.getAppuserprefix();
    } else {
      return "com.mz";
    }
  }

  /**
   * 从上下文取得当前用户名
   */
  public static String getCurrentUserName() {
    try {
      Object principal = SecurityUtils.getSubject().getPrincipal();
      return (String) principal;
    } catch (Exception e) {
      LogFactory.error("未找到当前用户");
    }
    return "";
  }

  /**
   * 从上下文取得当前用户id
   */
  public static Long getCurrentUserId() {
    AppUser curUser = getCurrentUser();
    if (curUser != null) {
      return curUser.getId();
    }
    return null;
  }

  /**
   * 获取注入对象
   */
  public static Object getBean(String name) {
    if (name.contains("remote")) {//如果获得的是远程接口，先尝试获取本地接口
      Object bean = SpringContextUtil.getBean(name + "Impl");
      if (bean != null) {
        return bean;
      }
    }
    return SpringContextUtil.getApplicationContext().getBean(name);
  }


  /**
   * 取得应用程序的绝对路径
   */
  public static String getAppAbsolutePath() {
    return AppUtils.getAppAbsolutePath();
  }

  /**
   * 获得httpservletrequest
   * <p>
   * TODO
   * </p>
   *
   * @author: Liu Shilei
   * @param: @return
   * @return: HttpServletRequest
   * @Date : 2015年10月27日 下午7:02:55
   * @throws:
   */
  public static HttpServletRequest getRequest() {
    try {
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
          .getRequestAttributes()).getRequest();
      return request;
    } catch (Exception e) {
    }
    return null;

  }

  /**
   * 获得HttpServletResponse
   * <p>
   * TODO
   * </p>
   *
   * @author: Liu Shilei
   * @param: @return
   * @return: HttpServletResponse
   * @Date : 2015年10月27日 下午7:02:55
   * @throws:
   */
  public static HttpServletResponse getResponse() {
    HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes()).getResponse();
    return response;
  }

  /**
   * 获得IP地址
   */
  public static String getIp(HttpServletRequest request) {

    String ip = request.getHeader("X-Forwarded-For");
    if (!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      //多次反向代理后会有多个ip值，第一个ip才是真实ip
      int index = ip.indexOf(",");
      if (index != -1) {
        return ip.substring(0, index);
      } else {
        return ip;
      }
    }
    ip = request.getHeader("X-Real-IP");
    if (!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      return ip;
    }
    return request.getRemoteAddr();

  }

  /**
   * 获得域名加应用数据
   * @param request
   * @return
   */
  public static String getAppPath(HttpServletRequest request){
    String path = request.getContextPath();
    String basePath = getBasePath(request) + path;
    return basePath;
  }

  /**
   * 获得域名路径
   * @param request
   * @return
   */
  public static String getBasePath(HttpServletRequest request){
    String scheme = request.getScheme();
    String serverName = request.getServerName();
    int port = request.getServerPort();
    String basePath = scheme + "://" + serverName + ":" + port ;
    return basePath;
  }
}
