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
import com.mz.core.exception.IncorrectCaptchaException;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.oauth.user.model.AppUser;
import com.mz.oauth.util.DrawPictureUtil;
import com.mz.shiro.service.AppLogLoginService;
import com.mz.util.UUIDUtil;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.ContextUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2015年9月18日 上午10:32:03
 */
@Controller
@RequestMapping("/")
public class LoginController extends BaseController<AppUser, Long> {

  Logger logger = LoggerFactory.getLogger(LoginController.class);

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
  public String index(HttpServletRequest request) throws Exception {
    Subject subject = SecurityUtils.getSubject();
    String indexHtml = request.getScheme() + "://" + request.getServerName() + "/admin/#/index";
    String loginHtml = request.getScheme() + "://" + request.getServerName() + "/admin/login.html";
    if (subject.isAuthenticated()) {
      return indexHtml;
    }
    return loginHtml;
  }

  @GetMapping("/login")
  public String loginGet(HttpServletRequest request) {
    Subject subject = SecurityUtils.getSubject();
    String indexHtml = request.getScheme() + "://" + request.getServerName() + "/admin/#/index";
    String loginHtml = request.getScheme() + "://" + request.getServerName() + "/admin/login.html";
    if (subject.isAuthenticated()) {
      return "redirect:" + indexHtml;
    }
    return "redirect:" + loginHtml;
  }

  @PostMapping("/login")
  public String loginPost(HttpServletRequest request) {
    Subject subject = SecurityUtils.getSubject();
    String indexHtml = request.getScheme() + "://" + request.getServerName() + "/admin/#/index";
    if (subject.isAuthenticated()) {
      return "redirect:" + indexHtml;
    }
    String shiroLoginFailure = (String) request.getAttribute("shiroLoginFailure");
    String loginHtml = request.getScheme() + "://" + request.getServerName() + "/admin/login.html";
    String url = "", error = "";
    if (UnknownAccountException.class.getName().equals(shiroLoginFailure)) {
      url = loginHtml + "?error=1";
    } else if (IncorrectCredentialsException.class.getName().equals(shiroLoginFailure)) {
      url = loginHtml + "?error=1";
    } else if (ExcessiveAttemptsException.class.getName().equals(shiroLoginFailure)) {
      url = loginHtml + "?error=3";
    } else if (IncorrectCaptchaException.class.getName().equals(shiroLoginFailure)) {
      url = loginHtml + "?error=2";
    } else if (shiroLoginFailure != null) {
      url = loginHtml + "?error=1";
    }
    request.setAttribute("message", error);
    if (request.getParameter("forceLogout") != null) {
      request.setAttribute("message", "您已经被管理员强制退出，请重新登录");
      url = loginHtml;
    }
    return "redirect:" + url;
  }
}
