/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年9月18日 上午10:32:03
 */
package com.mz.oauth.login.controller;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.Constants;
import com.mz.core.annotation.NoLogin;
import com.mz.core.annotation.base.MethodName;
import com.mz.oauth.util.DrawPictureUtil;
import com.mz.shiro.PasswordHelper;
import com.mz.shiro.service.AppLogLoginService;
import com.mz.util.QueryFilter;
import com.mz.util.UUIDUtil;
import com.mz.util.httpRequest.IpUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.ContextUtil;
import com.mz.core.exception.IncorrectCaptchaException;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.log.AppLogLogin;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.oauth.user.model.AppUser;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2015年9月18日 上午10:32:03
 */
@Controller
@RequestMapping("/")
public class LoginController extends BaseController<AppUser, Long> {

  @InitBinder
  public void initBinder(ServletRequestDataBinder binder) {
    /**
     * 自动转换日期类型的字段格式
     */
    binder.registerCustomEditor(Date.class, new DateTimePropertyEditorSupport());

    /**
     * 防止XSS攻击，并且带去左右空格功能
     */
    binder.registerCustomEditor(String.class, new StringPropertyEditorSupport(true, false));
  }

  @RequestMapping("/kaptcha")
  public void kaptcha(HttpServletRequest request, HttpServletResponse response,
      HttpSession session) {
    DrawPictureUtil drawPictureUtil = new DrawPictureUtil(Constants.KAPTCHA_SESSION_KEY,
        100, 30);
    drawPictureUtil.darw(request, response, session);
  }

  @RequestMapping("/getUser")
  public void getUser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
      throws IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    //增加cookie  ID  干扰攻击者
    //sdfer  为了给报表做免登录
    Cookie cookie1 = new Cookie("sdfer", request.getSession().getId());
    cookie1.setPath("/");
    response.addCookie(cookie1);

//		Cookie cookie2 = new Cookie("JSESSIONID", UUIDUtil.getPrototypeUUID());
//		cookie2.setPath("/");
//		response.addCookie(cookie2);

    Cookie cookie3 = new Cookie("eraare", UUIDUtil.getPrototypeUUID());
    cookie3.setPath("/");
    response.addCookie(cookie3);
    AppUser user = ContextUtil.getCurrentUser();
    if (user != null) {
      out.print(JSON.toJSONString(user));
    }
    out.close();
  }

  @Resource(name = "appUserService")
  @Override
  public void setService(BaseService<AppUser, Long> service) {
    super.service = service;
  }

  @Resource(name = "appLogLoginService")
  private AppLogLoginService appLogLoginService;

  @RequestMapping("/index")
  public String index(HttpServletRequest request, HttpServletResponse response) {
    Subject subject = SecurityUtils.getSubject();
    if (subject.isAuthenticated()) {
      return "/";
    } else {
      return "/login.html";
    }
  }

  @MethodName(name = "测试")
  @RequestMapping("/test")
  @NoLogin
  public void test(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setStatus(301);
    response.sendRedirect("http://www.hurongbi.cn");
  }


  @NoLogin
  @MethodName(name = "用户登录效验")
  @RequestMapping("/login")
  @ResponseBody
  public JsonResult login(HttpServletRequest request, HttpSession session) {
    JsonResult jsonResult = new JsonResult();
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String captcha = request.getParameter("captcha");
    System.out.print("验证码为" + captcha);
    if (StringUtils.isEmpty(username)) {
      jsonResult.setMsg("用户名/密码错误");
    } else if (StringUtils.isEmpty(password)) {
      jsonResult.setMsg("用户名/密码错误");
    } else if (StringUtils.isEmpty(captcha)) {
      jsonResult.setMsg("验证码错误");
    } else {
      String session_captcha = (String) request.getSession()
          .getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
      System.out.print("后台验证码为：" + session_captcha + "，用户输入验证码为" + captcha);
      if (!captcha.equalsIgnoreCase(session_captcha)) {
        jsonResult.setMsg("验证码错误");
      } else {
        QueryFilter queryFilter = new QueryFilter(AppUser.class);
        queryFilter.addFilter("username=", username);
        AppUser appUser = service.get(queryFilter);
        if (appUser == null) {
          jsonResult.setMsg("用户名/密码错误");
        } else {
          AppLogLogin appLogLogin = new AppLogLogin();
          appLogLogin.setCreated(new Date());
          appLogLogin.setModified(new Date());
          appLogLogin.setIp(IpUtil.getIp());
          String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
          appLogLogin.setLoginTime(dateStr);
          appLogLogin.setSaasId("hurong_system");
          appLogLogin.setType("1");
          appLogLogin.setUserId(appUser.getId());
          appLogLogin.setUserName(appUser.getUsername());
          if ("1".equals(appUser.getIsDelete())) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg("此账号已被注销");
            //登录失败
            appLogLogin.setStatus("2");
            return jsonResult;
          } else {
            PasswordHelper passwordHelper = new PasswordHelper();
            //密码加密
            String encryString = passwordHelper
                .encryString(password, appUser.getSalt() + appUser.getAppuserprefix());
            if (encryString.equals(appUser.getPassword())) {
              //登录成功
              appLogLogin.setStatus("1");
              jsonResult.setMsg(encryString);
              jsonResult.setSuccess(true);
            } else {
              //登录失败
              appLogLogin.setStatus("2");
              jsonResult.setMsg("用户名/密码错误");
            }
            appLogLoginService.save(appLogLogin);

          }
        }
      }
    }
    return jsonResult;
  }

  @MethodName(name = "用户登录操作")
  @RequestMapping("/loginService")
  public String loginService(AppUser appUser, HttpServletRequest request,
      HttpServletResponse response) {

    JsonResult jsonResult = new JsonResult();
    Subject subject = SecurityUtils.getSubject();
    String backUrl = PropertiesUtils.APP.getProperty("app.url");
    String loginHtml = backUrl + "/login.html";
    String indexHtml = backUrl + "/#/index";
    String url = "";

    //如果认证信息存在，则直接返回主页，防止认证后再次返回登录页登录不成功的问题
    if (subject.isAuthenticated()) {
      jsonResult.setSuccess(true);
      jsonResult.setMsg("已经登录了");
      url = indexHtml;
    }
    String exceptionClassName = null;
    exceptionClassName = (String) request.getAttribute("shiroLoginFailure");

    String error = null;
    if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("用户名/密码错误");
      url = loginHtml + "&error=1";
    } else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("用户名/密码错误");
      url = loginHtml + "&error=1";
    } else if (ExcessiveAttemptsException.class.getName().equals(exceptionClassName)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("登录次数过多,请1分钟后再试");
      url = loginHtml + "&error=3";
    } else if (IncorrectCaptchaException.class.getName().equals(exceptionClassName)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("验证码错误");
      url = loginHtml + "&error=2";
    } else if (exceptionClassName != null) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("用户名/密码错误");
      url = loginHtml + "&error=1";
    }
    request.setAttribute("message", error);
    if (request.getParameter("forceLogout") != null) {
      request.setAttribute("message", "您已经被管理员强制退出，请重新登录");
      url = loginHtml;
    }

    if (StringUtils.isEmpty(url)) {
      url = loginHtml;
    }

    if (!jsonResult.getSuccess()) {/*//登录失败时，增加失败日志
        	MongoUtil<AppLogLogin, String> mongoUtil = new MongoUtil<AppLogLogin, String>(AppLogLogin.class);
        	AppLogLogin appLogLogin = AppLogLoginFactory.getAppLogLogin(appUser, false,request);
        	appLogLogin.setId(mongoUtil.autoincrementId());
    		mongoUtil.save(appLogLogin);
        */
    }
    return "redirect:" + url;
  }

  protected String getAppuserprefix(ServletRequest request) {
    return WebUtils.getCleanParam(request, "appuserprefix");
  }
}
