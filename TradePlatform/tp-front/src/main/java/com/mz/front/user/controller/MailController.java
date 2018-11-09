package com.mz.front.user.controller;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.redis.common.utils.RedisService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.util.SessionUtils;
import com.mz.util.sys.SpringContextUtil;;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/mail")
public class MailController {

  /**
   * @Description: 绑定邮箱
   * @Author: zongwei
   * @CreateDate: 2018/7/7 13:44
   * @UpdateUser: zongwei
   * @UpdateDate: 2018/7/7 13:44
   * @UpdateRemark: 创建
   * @Version: 1.0
   */
  @RequestMapping("/setMail")
  @ResponseBody
  public JsonResult setMail(String codes, String savedSecret, HttpServletRequest request) {
    String verifyCode = request.getParameter("verifyCode");//短信验证码
    String mail = "";
    mail = request.getParameter("mail");
    User user = SessionUtils.getUser(request);

    if (StringUtils.isEmpty(verifyCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("邮箱验证码不能为空"));
    }

    RedisService redisService = SpringContextUtil.getBean("redisService");
    String session_verifyCode = redisService.get("Mail:sendMail:" + mail);
    if (!verifyCode.equals(session_verifyCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("邮箱验证码错误或已失效！"));
    }
    if (user != null) {
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");

      RemoteResult setmail = remoteManageService.setMail(user.getCustomerId(), mail);

      if (setmail != null && setmail.getSuccess()) {
        user.setMail(mail);
        user.setMailStates(1);
        //北京处理跳号问题 -- 2018.4.21
        request.getSession().setAttribute("user", user);
        return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff(setmail.getMsg()));

      } else {
        return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff(setmail.getMsg()));
      }
    } else {
      return new JsonResult().setSuccess(false).setMsg("请登录或重新登录！");
    }

  }


  /**
   * @Description: 邮箱取消绑定
   * @Author: zongwei
   * @CreateDate: 2018/7/7 13:44
   * @UpdateUser: zongwei
   * @UpdateDate: 2018/7/7 13:44
   * @UpdateRemark: 创建
   * @Version: 1.0
   */
  @RequestMapping("/cancelMail")
  @ResponseBody
  public JsonResult cancelMail(String codes, String savedSecret, HttpServletRequest request) {
    String verifyCode = request.getParameter("verifyCode");//短信验证码
    String mail = "";
    mail = request.getParameter("mail");
    User user = SessionUtils.getUser(request);

    if (StringUtils.isEmpty(verifyCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("邮箱验证码不能为空"));
    }

    RedisService redisService = SpringContextUtil.getBean("redisService");
    String session_verifyCode = redisService.get("Mail:sendMail:" + mail);
    if (!verifyCode.equals(session_verifyCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("邮箱验证码错误或已失效！"));
    }

    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");

    RemoteResult setmail = remoteManageService.cancelMail(user.getCustomerId(), mail);

    if (setmail != null && setmail.getSuccess()) {
      user.setMail(null);
      user.setMailStates(0);
      //北京处理跳号问题 -- 2018.4.21
      request.getSession().setAttribute("user", user);
      return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff(setmail.getMsg()));

    } else {
      return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff(setmail.getMsg()));
    }

  }


  @RequestMapping("setmailtwo")
  public ModelAndView setmailtwo(HttpServletRequest request) {
    String verifyCode = request.getParameter("verifyCode");
    String mail = request.getParameter("mail");
    ModelAndView mav = new ModelAndView();

    RedisService redisService = SpringContextUtil.getBean("redisService");
    String session_verifyCode = redisService.get("Mail:sendMail:" + mail);
    if (verifyCode.equals(session_verifyCode)) {
      mav.setViewName("front/user/setmailtwo");
      // 默认生成html静态页，配置false不生成
      mav.addObject("CREATE_HTML", false);

      mav.addObject("a", "3");

      mav.addObject("verifyCode", "verifyCode");
      ;
    } else {
      mav.setViewName("front/user/setmailone");
      // 默认生成html静态页，配置false不生成
      mav.addObject("CREATE_HTML", false);

      mav.addObject("a", "3");
      mav.addObject("meg", "邮箱验证码不对");
    }

    return mav;
  }


  /**
   * @Description: 重新绑定邮箱
   * @Author: zongwei
   * @CreateDate: 2018/7/7 13:44
   * @UpdateUser: zongwei
   * @UpdateDate: 2018/7/7 13:44
   * @UpdateRemark: 创建
   * @Version: 1.0
   */
  @RequestMapping("/setMailagain")
  @ResponseBody
  public JsonResult setMailagain(String codes, String savedSecret, HttpServletRequest request) {
    String verifyCode = request.getParameter("verifyCode");//短信验证码
    String mail = "";
    mail = request.getParameter("mail");
    User user = SessionUtils.getUser(request);

    if (StringUtils.isEmpty(verifyCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("邮箱验证码不能为空"));
    }

    RedisService redisService = SpringContextUtil.getBean("redisService");
    String session_verifyCode = redisService.get("Mail:sendMail:" + mail);
    if (!verifyCode.equals(session_verifyCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("邮箱验证码错误或已失效！"));
    }

    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");

    RemoteResult setmail = remoteManageService.setMail(user.getCustomerId(), mail);

    if (setmail != null && setmail.getSuccess()) {
      user.setMail(mail);
      user.setMailStates(1);
      //北京处理跳号问题 -- 2018.4.21
      request.getSession().setAttribute("user", user);
      return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff(setmail.getMsg()));

    } else {
      return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff(setmail.getMsg()));
    }

  }


  /**
   * @Description: 再次绑定邮箱
   * @Author: zongwei
   * @CreateDate: 2018/7/7 13:44
   * @UpdateUser: zongwei
   * @UpdateDate: 2018/7/7 13:44
   * @UpdateRemark: 创建
   * @Version: 1.0
   */
  @RequestMapping("/setMailtwo")
  @ResponseBody
  public JsonResult setMailtwo(String codes, String savedSecret, HttpServletRequest request) {
    String verifyCode = request.getParameter("verifyCode");//短信验证码
    String mail = "";
    mail = request.getParameter("mail");
    User user = SessionUtils.getUser(request);

    if (StringUtils.isEmpty(verifyCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("邮箱验证码不能为空"));
    }

    RedisService redisService = SpringContextUtil.getBean("redisService");
    String session_verifyCode = redisService.get("Mail:sendMail:" + mail);
    if (!verifyCode.equals(session_verifyCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("邮箱验证码错误或已失效！"));
    }

    if (user != null) {
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");

      RemoteResult setmail = remoteManageService.setMail(user.getCustomerId(), mail);

      if (setmail != null && setmail.getSuccess()) {
        user.setMail(mail);
        user.setMailStates(1);
        //北京处理跳号问题 -- 2018.4.21
        request.getSession().setAttribute("user", user);
        return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff(setmail.getMsg()));

      } else {
        return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff(setmail.getMsg()));
      }

    } else {
      return new JsonResult().setSuccess(false).setMsg("请登录或重新登录！");
    }

  }


}
