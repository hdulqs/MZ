/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0
 * @Date:        2015年10月13日 下午4:57:27
 */
package com.mz.shiro;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi
 * @Date :          2015年10月13日 下午4:57:27
 */

import com.mz.core.exception.IncorrectCaptchaException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {
  private static final Logger LOG = LoggerFactory.getLogger(CaptchaFormAuthenticationFilter.class);

  public CaptchaFormAuthenticationFilter() {
  }

  @Override
  protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
    String fallbackUrl = (String) getSubject(request, response).getSession().getAttribute("authc.fallbackUrl");
    if(StringUtils.isEmpty(fallbackUrl)) {
      fallbackUrl = getSuccessUrl();
    }
    HttpServletRequest req = (HttpServletRequest) request;
    String successUrl = req.getParameter("successUrl");
    //直接跳转主页
    if(StringUtils.isEmpty(successUrl)){
      WebUtils.issueRedirect(request, response, fallbackUrl);
    }else{
      WebUtils.issueRedirect(request, response, successUrl);
    }
  }

  @Override
  /**
   * 登录验证
   */
  protected boolean executeLogin(ServletRequest request,  ServletResponse response) throws Exception {
    CaptchaUsernamePasswordToken token = null;

    try {//抓空异常
      token = createToken(request, response);
    } catch (UnknownAccountException e) {
      LOG.info("用户名和密码不能为空-"+e);
      return onLoginFailure(token, e, request, response);
    }

    if (token == null) {
      String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
          "must be created in order to execute a login attempt.";
      throw new IllegalStateException(msg);
    }
    Subject subject = getSubject(request, response);
    try {
      /*图形验证码验证*/
      doCaptchaValidate((HttpServletRequest) request, token);
      subject.login(token);//正常验证
      PrincipalCollection principals = subject.getPrincipals();
      subject.getSession().setAttribute("loginUserName", principals.getPrimaryPrincipal());
      LOG.info(token.getUsername()+"登录成功");
      return onLoginSuccess(token, subject, request, response);
    }catch (AuthenticationException e) {
      subject.getSession().removeAttribute("loginUserName");
      LOG.info(token.getUsername()+"登录失败--"+e);
      return onLoginFailure(token, e, request, response);
    }
  }

  // 验证码校验
  protected void doCaptchaValidate(HttpServletRequest request,
      CaptchaUsernamePasswordToken token) {
    //session中的图形码字符串
    String captcha = (String) request.getSession().getAttribute(
        com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
    if("qwer".equals(token.getCaptcha())){//交易中心首页登录，默认验证码
      return ;
    }
    if (captcha != null && !captcha.equalsIgnoreCase(token.getCaptcha())) {
      throw new IncorrectCaptchaException("验证码错误！");
    }
  }

  @Override
  protected CaptchaUsernamePasswordToken createToken(ServletRequest request,
      ServletResponse response) {
    String username = getUsername(request);
    String password = getPassword(request);
    String captcha = getCaptcha(request);
    boolean rememberMe = isRememberMe(request);
    String host = getHost(request);
    String appuserprefix = getAppuserprefix(request);

    if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
      throw new UnknownAccountException("用户名和密码不能为空");//没找到帐号
    }

    return new CaptchaUsernamePasswordToken(username,password.toCharArray(), rememberMe, host, captcha,appuserprefix);
  }

  public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

  private String captchaParam = DEFAULT_CAPTCHA_PARAM;

  public String getCaptchaParam() {
    return captchaParam;
  }

  public void setCaptchaParam(String captchaParam) {
    this.captchaParam = captchaParam;
  }

  protected String getCaptcha(ServletRequest request) {
    return WebUtils.getCleanParam(request, getCaptchaParam());
  }

  protected String getAppuserprefix(ServletRequest request) {
    return WebUtils.getCleanParam(request, "appuserprefix");
  }
} 
