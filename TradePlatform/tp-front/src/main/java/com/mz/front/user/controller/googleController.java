package com.mz.front.user.controller;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.customer.user.model.AppCustomer;
import com.mz.manage.remote.RemoteManageService;
import com.mz.util.properties.PropertiesUtils;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.util.GoogleAuthenticatorUtil;
import com.mz.util.SessionUtils;
import com.mz.util.sys.SpringContextUtil;;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/google")
public class googleController {

  /**
   *
   * @param codes
   * @param savedSecret
   * @param request
   * @return
   */

  @RequestMapping("/yzgoogle")
  @ResponseBody
  public JsonResult yzgoogle(String codes, String savedSecret, HttpServletRequest request) {
    // enter the code shown on device. Edit this and run it fast before the
    // code expires!
    long code = Long.parseLong(codes);
    System.out.println("cococooc=" + code);
    System.out.println("savedSecret=" + savedSecret);

    long t = System.currentTimeMillis();
    GoogleAuthenticatorUtil ga = new GoogleAuthenticatorUtil();
    //  ga.setWindowSize(0); // should give 5 * 30 seconds of grace...
    boolean r = ga.check_code(savedSecret, code, t);
    System.out.println("rrrr=" + r);
    User user = SessionUtils.getUser(request);
    if (!r) {
      return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("gugeyanzhengshibai"));
    } else {
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      System.out.println("user" + user.getMobile());
      String usname = "";
      if (user.getMobile() != null) {
        usname = user.getMobile();
      } else {
        usname = user.getUsername();
      }
      RemoteResult result = remoteManageService.sendgoogle(usname, savedSecret);
      if (result.getSuccess()) {
        user.setGoogleState(1);
        user.setGoogleKey(savedSecret);
        //北京处理跳号问题 -- 2018.4.21
        //SessionUtils.updateRedis(user);
        request.getSession().setAttribute("user", user);
        //北京处理跳号问题 -- 2018.4.21
      }
      return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("gugeyanzhengsuccess"));
    }
  }


  /**
   * 生成谷歌认证码
   */
  @RequestMapping("/sendgoogle")
  @ResponseBody
  public JsonResult sendgoogle(HttpServletRequest request) {
    String secret = GoogleAuthenticatorUtil.generateSecretKey();
    User user = SessionUtils.getUser(request);
    System.out.println("ssss=" + secret);
    String qrBarcodeURL = GoogleAuthenticatorUtil.getQRBarcodeURL("testuser",
        "testhost", secret);
    System.out.println("ffff=" + qrBarcodeURL);
    String company = PropertiesUtils.APP.getProperty("app.googleCompany");
    AppCustomer a = new AppCustomer();
    a.setGoogleKey(secret);
    a.setCompany(company);
    a.setUserName(user.getUsername());
    return new JsonResult().setSuccess(true).setObj(a);

  }


  /**
   *
   * @param codes
   * @param request
   * @return
   */

  @RequestMapping("/jcgoogle")
  @ResponseBody
  public JsonResult jcgoogle(String codes, HttpServletRequest request) {
    // enter the code shown on device. Edit this and run it fast before the
    // code expires!
    //String GoogleKey = (String) request.getSession().getAttribute("googleKey");
    User user = SessionUtils.getUser(request);
    String GoogleKey = user.getGoogleKey();
    String PassWord = request.getParameter("PassWord");
    if (StringUtils.isEmpty(PassWord)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("loginpwd_no_null"));
    }
    //查看密码
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    RemoteResult result = remoteManageService.setvail(user.getMobile(), PassWord);

    if (!result.getSuccess()) {
      return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("mimacuowu"));

    }
    long code = Long.parseLong(codes);
    long t = System.currentTimeMillis();
    GoogleAuthenticatorUtil ga = new GoogleAuthenticatorUtil();
    // ga.setWindowSize(0); // should give 5 * 30 seconds of grace...
    boolean r = ga.check_code(GoogleKey, code, t);
    if (!r) {
      return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("gugejiechuerror"));

    } else {
      RemoteResult jcgoogle = remoteManageService.jcgoogle(user.getMobile(), GoogleKey);

      if (jcgoogle.getSuccess()) {
        user.setGoogleState(0);
        user.setPassDate(new Date());
        user.setGoogleKey(GoogleKey);
        //北京处理跳号问题 -- 2018.4.21
        //SessionUtils.updateRedis(user);
        request.getSession().setAttribute("user", user);
        //北京处理跳号问题 -- 2018.4.21
      }

      return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("gugejiechusuccess"));
    }
  }


  /**
   *
   * @param codes
   * @param 谷歌二次验证
   * @param request
   * @return
   */

  @RequestMapping("/googleAuth")
  @ResponseBody
  public JsonResult googleAuth(String codes, String savedSecret, HttpServletRequest request) {
    //String GoogleKey = (String) request.getSession().getAttribute("googleKey");
    User user = null;
    String username = request.getParameter("username");
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    user = remoteManageService.selectByTel(username);
    if (user.getGoogleKey() == null) {
      user = SessionUtils.getUser(request);
    }
    long code = Long.parseLong(codes);
    long t = System.currentTimeMillis();
    GoogleAuthenticatorUtil ga = new GoogleAuthenticatorUtil();
    //ga.setWindowSize(0); // should give 5 * 30 seconds of grace...
    boolean r = ga.check_code(user.getGoogleKey(), code, t);
    if (!r) {

      return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("gugeyanzhengshibai"));
    } else {
      request.getSession().setAttribute("isAuthentication", "1");
      return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("gugeyanzhengsuccess"));
    }
  }


}
