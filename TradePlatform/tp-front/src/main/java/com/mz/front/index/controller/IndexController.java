package com.mz.front.index.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.ContextUtil;
import com.mz.util.sys.SpringContextUtil;
import com.mz.web.app.model.AppBanner;
import com.mz.core.constant.StringConstant;
import com.mz.manage.remote.RemoteAppArticleService;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteBaseInfoService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.Article;
import com.mz.manage.remote.model.Coin;
import com.mz.manage.remote.model.FriendLink;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.commendCode;
import com.mz.util.GoogleAuthenticatorUtil;
import com.mz.util.IpUtil;
import com.mz.util.SessionUtils;
import com.mz.core.thread.ThreadPool;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

@Controller
@RequestMapping("/")
public class IndexController {

  @Autowired
  UserRedisRunnable userRedisRunnable;

  // 基础缓存
  public static final String baseConfig = "configCache:baseConfig";
  private final static Logger log = Logger.getLogger(IndexController.class);

  @Autowired
  private RedisService redisService;

  /**
   * 注册类型属性编辑器
   */
  @InitBinder
  public void initBinder(ServletRequestDataBinder binder) {

    // 系统注入的只能是基本类型，如int，char，String

    /**
     * 自动转换日期类型的字段格式
     */
    binder.registerCustomEditor(Date.class, new DateTimePropertyEditorSupport());

    /**
     * 防止XSS攻击，并且带去左右空格功能
     */
    binder.registerCustomEditor(String.class, new StringPropertyEditorSupport(true, false));
  }

  /**
   * 加载banner 修改banner随语言切换(huang)
   */
  @RequestMapping(value = "banner", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ResponseBody
  public JsonResult banner() {

    String language = "";
    Locale locale = LocaleContextHolder.getLocale();
    JsonResult jsonresult = new JsonResult();
    List<AppBanner> list = this.listBanner();
    if (locale.toString() != null && !"".equals(locale.toString())) {
      if ("zh_CN".equals(locale.toString())) {
        language = "cn";
        List<AppBanner> list2 = new ArrayList<AppBanner>();
        for (AppBanner appBanner : list) {
          Integer type = appBanner.getApplicationType();
          String remark1 = appBanner.getRemark1();
          int a = Integer.parseInt(remark1);
          if (appBanner.getSort() == null) {
            appBanner.setSort(0);
          }
          if (type == 0 && a == 0) {
            list2.add(appBanner);
          }
          Collections.sort(list2);
          jsonresult.setSuccess(true).setObj(list2);
        }

      } else if ("en".equals(locale.toString())) {
        language = "en";
        List<AppBanner> list2 = new ArrayList<AppBanner>();
        for (AppBanner appBanner : list) {
          Integer type = appBanner.getApplicationType();
          String remark1 = appBanner.getRemark1();
          int a = Integer.parseInt(remark1);
          if (appBanner.getSort() == null) {
            appBanner.setSort(0);
          }
          if (type == 0 && a == 1) {
            list2.add(appBanner);
          }
          Collections.sort(list2);
          jsonresult.setSuccess(true).setObj(list2);
        }
      } else if ("tn".equals(locale.toString())) {
        language = "tn";
        List<AppBanner> list2 = new ArrayList<AppBanner>();
        for (AppBanner appBanner : list) {
          Integer type = appBanner.getApplicationType();
          String remark1 = appBanner.getRemark1();
          int a = Integer.parseInt(remark1);
          if (appBanner.getSort() == null) {
            appBanner.setSort(0);
          }
          if (type == 0 && a == 2) {
            list2.add(appBanner);
          }
          Collections.sort(list2);
          jsonresult.setSuccess(true).setObj(list2);
        }
      }
    } else {
      language = "cn";
      List<AppBanner> list2 = new ArrayList<AppBanner>();
      for (AppBanner appBanner : list) {
        Integer type = appBanner.getApplicationType();
        String remark1 = appBanner.getRemark1();
        int a = Integer.parseInt(remark1);
        if (appBanner.getSort() == null) {
          appBanner.setSort(0);
        }
        if (type == 0 && a == 0) {
          list2.add(appBanner);
        }
        Collections.sort(list2);
        jsonresult.setSuccess(true).setObj(list2);
      }
    }
    return jsonresult;
  }

  /**
   * 主页
   */
  @RequestMapping("index")
  public ModelAndView index(HttpServletRequest request) {
    String tokenId = request.getParameter("tokenId");
    User user = null;

    Locale locale = LocaleContextHolder.getLocale();
    ModelAndView mav = new ModelAndView("index");
    String hasico = redisService.get(ContextUtil.getWebsite() + ":hasico");
    if ("true".equals(hasico)) {
      request.getSession().setAttribute("hasico", true);
    } else {
      request.getSession().removeAttribute("hasico");
    }
    //RedisService redisService = SpringContextUtil.getBean("redisService");
    String config = redisService.get("configCache:all");
    if (!StringUtils.isEmpty(config)) {
      JSONObject parseObject = JSONObject.parseObject(config);
      mav.addObject("isOpenLanguage", parseObject.get("isOpenLanguage"));
    }
    mav.addObject("locale", locale);
    mav.addObject("user", user);
    mav.addObject("tokenId", tokenId);
    mav.addObject("showColor", "1");
    String basePath =
        request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + request.getContextPath() + "/";
    //System.out.println("basePath==" + basePath);
    return mav;
  }

  // 最新动态
  @RequestMapping("article")
  @ResponseBody
  public JsonResult article(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String type = request.getParameter("type");

    String language = "";
    Locale locale = LocaleContextHolder.getLocale();
    if (locale.toString() != null && !"".equals(locale.toString())) {
      if ("zh_CN".equals(locale.toString())) {
        language = "cn";
      } else if ("en".equals(locale.toString())) {
        language = "en";

        // 最新动态
        if ("4".equals(type)) {
          type = "65";
        } else if ("5".equals(type)) {
          type = "64";
        } else if ("6".equals(type)) {
          type = "66";
        }
      }
    } else {
      language = "cn";
    }
    // 新闻
    RemoteAppArticleService remoteAppArticleService = SpringContextUtil.getBean("remoteAppArticleService");
    List<Article> listArticle = remoteAppArticleService
        .findArticListByIdLimit(Long.valueOf(type), 6, language);// 最新动态
    return jsonResult.setSuccess(true).setObj(listArticle);
  }

  // 友情链接
  @RequestMapping("friend")
  @ResponseBody
  public JsonResult friend() {
    JsonResult jsonResult = new JsonResult();
    // 友情链接
    RemoteAppArticleService remoteFriendLinkService = SpringContextUtil.getBean("remoteAppArticleService");
    List<FriendLink> listFriendLink = remoteFriendLinkService.findAllFriendLink();
    return jsonResult.setSuccess(true).setObj(listFriendLink);
  }

  /**
   * 通用的返回静态资源方法,用户菜单点击时切换页面
   */
  @RequestMapping("v")
  public ModelAndView v(String u, HttpServletRequest request) {
    ModelAndView mav = new ModelAndView();
    if (!StringUtils.isEmpty(u)) {
      String[] str = u.split("\\?");
      if (str.length > 1) {
        mav.setViewName(str[0]);
      } else {
        mav.setViewName(u);
      }
    } else {
      mav.setViewName("index");
    }
    // 默认生成html静态页，配置false不生成
    mav.addObject("CREATE_HTML", false);

    mav.addObject("a", "3");

    // 国际化
    // mav.addObject("springMacroRequestContext", new
    // RequestContext(request, request.getServletContext()));

    return mav;
  }

  /**
   * 跳转到登录页面
   */
  @RequestMapping("login")
  public ModelAndView login(HttpServletRequest request) {
    Locale locale = LocaleContextHolder.getLocale();
    ModelAndView mav = new ModelAndView("login");
    String market = request.getParameter("market");
    // 基础信息
    // 查询后台参数配置
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String config = redisService.get("configCache:all");
    if (!StringUtils.isEmpty(config)) {
      JSONObject parseObject = JSONObject.parseObject(config);
      mav.addObject("isOpenLanguage", parseObject.get("isOpenLanguage"));
    }
    mav.addObject("market", market);
    mav.addObject("locale", locale);
    return mav;
  }

  /**
   * 跳转到登录页面
   */
  @RequestMapping("findPwdTip")
  public ModelAndView findPwdTip(HttpServletRequest request) {
    ModelAndView mav = new ModelAndView("findPwdTip");
    // 基础信息
    return mav;
  }

  /**
   * 跳转到注册页面
   */
  @RequestMapping("reg")
  public ModelAndView reg(HttpServletRequest request) {
    Locale locale = LocaleContextHolder.getLocale();
    ModelAndView mav = new ModelAndView("reg");
    mav.addObject("locale", locale);

    // 查询后台参数配置
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String config = redisService.get("configCache:all");
    if (!StringUtils.isEmpty(config)) {
      JSONObject parseObject = JSONObject.parseObject(config);
      if ("zh_CN".equals(locale.toString())) {
        mav.addObject("regreg", parseObject.get("reg_cn"));
      } else if ("en".equals(locale.toString())) {
        mav.addObject("regreg", parseObject.get("reg_en"));
      }
      mav.addObject("isOpenLanguage", parseObject.get("isOpenLanguage"));
    }
    return mav;
  }

  /**
   * 邀请链接
   */
  @RequestMapping("regcode/{code:.+}")
  public ModelAndView regcode(@PathVariable String code) {
    Locale locale = LocaleContextHolder.getLocale();
    ModelAndView mav = new ModelAndView("reg");
    mav.addObject("locale", locale);

    // 查询后台参数配置
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String config = redisService.get("configCache:all");
    if (!StringUtils.isEmpty(config)) {
      JSONObject parseObject = JSONObject.parseObject(config);
      if ("zh_CN".equals(locale.toString())) {
        mav.addObject("regreg", parseObject.get("reg_cn"));
      } else if ("en".equals(locale.toString())) {
        mav.addObject("regreg", parseObject.get("reg_en"));
      }
      mav.addObject("commendCode", code);
      mav.addObject("isOpenLanguage", parseObject.get("isOpenLanguage"));
    }
    return mav;

  }

  /**
   * 关于我们页面
   */
  @RequestMapping("about")
  public ModelAndView about(HttpServletRequest request) {
    ModelAndView mav = new ModelAndView("about");
    return mav;
  }

  /**
   * 共用 在login的service里面新增判断来区分新用户和老用户
   *
   * @author huangjia
   */
  @SuppressWarnings("unused")
  @RequestMapping("sencodvail")
  @ResponseBody
  public JsonResult sencodvail(HttpServletRequest request, HttpServletResponse response) {
    String language = (String) request.getAttribute("language");

    String type = request.getParameter("type");
    if (type.equals("1")) {

      String username = request.getParameter("username").toLowerCase();
      String password = request.getParameter("password");
      if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
        return new JsonResult().setMsg(SpringContextUtil.diff("login_nameorpwd_erro"));
      }
      try {
        // 生成tokenid
        UUID uuid = UUID.randomUUID();
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        RemoteResult login = remoteManageService.login(username, password, uuid.toString());
        if (login != null && login.getSuccess()) {
          User user = (User) login.getObj();
          userRedisRunnable.process(user.getCustomerId().toString());
          user.setTokenId(uuid.toString());
          redisService.save("mobile:" + uuid,
              "{\"mobile\":\"" + username + "\",\"user\":" + JSON.toJSON(login.getObj()).toString()
                  + "}", 1800);
          log.info("UUID=" + uuid);
          Cookie cookie = new Cookie("tokenId", uuid.toString());
          cookie.setMaxAge(1800);
          cookie.setPath("/");
          response.addCookie(cookie);
          if (user != null) {
            return new JsonResult().setSuccess(true).setObj(user);
          } else {
            return new JsonResult().setSuccess(false).setObj(user);

          }
        }
        return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff(login.getMsg()));

      } catch (Exception e) {
        e.printStackTrace();
        // TODO: handle exception
      }
      return new JsonResult().setMsg(SpringContextUtil.diff("login_nameorpwd_erro"));
    } else if (type.equals("2")) {
      String oldPassWord = request.getParameter("oldPassWord");
      String newPassWord = request.getParameter("newPassWord");
      String reNewPassWord = request.getParameter("reNewPassWord");
      String pwSmsCode = request.getParameter("pwSmsCode");

      if (StringUtils.isEmpty(oldPassWord)) {
        return new JsonResult().setMsg(SpringContextUtil.diff("oldpwd_no_null"));
      }
      if (StringUtils.isEmpty(newPassWord)) {
        return new JsonResult().setMsg(SpringContextUtil.diff("newpwd_no_null"));
      }
      if (oldPassWord.equals(newPassWord)) {
        return new JsonResult().setMsg(SpringContextUtil.diff("newandold_no_null"));
      }
      if (StringUtils.isEmpty(reNewPassWord)) {
        return new JsonResult().setMsg(SpringContextUtil.diff("repeatpwd_no_null"));
      }
      if (!newPassWord.equals(reNewPassWord)) {
        return new JsonResult().setMsg(SpringContextUtil.diff("twopwd_is_diff"));
      }
      User user = SessionUtils.getUser(request);
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult result = remoteManageService
          .setpwvail(user.getUsername(), oldPassWord, newPassWord);
      if (result != null) {
        if (result.getSuccess()) {
          if (user != null) {
            return new JsonResult().setSuccess(true).setObj(user);
          } else {
            return new JsonResult().setSuccess(false).setObj(user);

          }
        } else {
          return new JsonResult().setMsg(SpringContextUtil.diff(result.getMsg()));
        }
      }

    } else if (type.equals("3")) {
      // 提币交易
      // user,code,accountpwSmsCode,sessionAccountpwSmsCode,btcNum,type,username,btcKey
      User user = SessionUtils.getUser(request);

      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      User selectByTel = remoteManageService.selectByCustomerId(user.getCustomerId());
      String sessionAccountpwSmsCode = (String) request.getSession()
          .getAttribute("accounCoinSmsCode");
      String code = request.getParameter("coinType");
      String accountpwSmsCode = request.getParameter("withdrawCode");
      String curType = request.getParameter("currencyType");// HTC
      String btcNum = request.getParameter("btcNum");// jine
      String btcKey = request.getParameter("btcKey");// 钱包地址
      /*
       * Date passDate = user.getPassDate(); SimpleDateFormat formatter = new
       * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       * if(passDate!=null&&!passDate.equals("")){ String passDate1 =
       * formatter.format(passDate); if(checkDate(passDate1)){ }else{ return new
       * JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("24h_jinzhitibi")); } }
       */
      if (selectByTel != null) {
        try {
          String username = selectByTel.getUsername();
          RemoteAppTransactionManageService transactionManageService = (RemoteAppTransactionManageService) SpringContextUtil
              .getBean("remoteAppTransactionManageService");
          RemoteResult remoteResult = transactionManageService
              .getOrdervail(user, code, accountpwSmsCode, sessionAccountpwSmsCode, btcNum, curType,
                  username, btcKey);
          if (remoteResult.getSuccess()) {
            return new JsonResult().setSuccess(true).setObj(selectByTel);
          } else {
            return new JsonResult().setSuccess(false)
                .setMsg(SpringContextUtil.diff(remoteResult.getMsg()));
          }
        } catch (Exception e) {
          // TODO: handle exception
          return new JsonResult().setSuccess(false);
        }
      }

      return new JsonResult().setSuccess(false);
    } else if (type.equals("4")) {
      String pwSmsCode = request.getParameter("verifyCode");// 短信验证码
      String mobile = request.getParameter("mobile");
      // 地区截取
      User user = SessionUtils.getUser(request);
      RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult regist = manageService.regphone(mobile);
      if (!regist.getSuccess()) {
        return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("phonechongfu"));

      }
      if (StringUtils.isEmpty(pwSmsCode)) {
        return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_is_not_null"));
      }
      // String session_pwSmsCode = (String)
      // request.getSession().getAttribute("accountpwSmsCode");
      RedisService redisService = SpringContextUtil.getBean("redisService");
      String session_pwSmsCode = redisService.get("SMS:smsphone:" + mobile);
      if (!pwSmsCode.equals(session_pwSmsCode)) {
        return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
      }
      return new JsonResult().setSuccess(true).setObj(user);

    } else if (type.equals("5")) {
      User user = SessionUtils.getUser(request);
      String GoogleKey = user.getGoogleKey();
      String codes = request.getParameter("codes");
      String PassWord = request.getParameter("PassWord");
      if (StringUtils.isEmpty(codes)) {
        return new JsonResult().setMsg(SpringContextUtil.diff("gugeyanzhengusnull"));
      }
      if (StringUtils.isEmpty(PassWord)) {
        return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
      }
      // 查看密码
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult result = remoteManageService.setvail(user.getMobile(), PassWord);

      if (!result.getSuccess()) {
        return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("mimacuowu"));

      }
      long code = Long.parseLong(codes);
      System.out.println("cococooc=" + code);
      System.out.println("savedSecret=" + GoogleKey);

      long t = System.currentTimeMillis();
      GoogleAuthenticatorUtil ga = new GoogleAuthenticatorUtil();
      // ga.setWindowSize(15); // should give 5 * 30 seconds of grace...
      boolean r = ga.check_code(GoogleKey, code, t);
      if (!r) {
        return new JsonResult().setMsg(SpringContextUtil.diff("gugeyanzhengshibai"));
      }
      return new JsonResult().setSuccess(true).setObj(user);
    } else if (type.equals("6")) {
      User user = SessionUtils.getUser(request);
      return new JsonResult().setSuccess(true).setObj(user);

    }

    return null;
  }

  /**
   * 判断是否超过24小时
   */

  public static boolean checkDate(String date1) throws Exception {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date start = formatter.parse(date1);
    String format = formatter.format(new Date());
    Date end = formatter.parse(format);
    long cha = end.getTime() - start.getTime();
    double result = cha * 1.0 / (1000 * 60 * 60);
    if (result <= 24) {
      // System.out.println("不可用");
      return false;
    } else {
      // System.out.println("可用");
      return true;
    }
  }

  /**
   * 共用
   */
  @RequestMapping("sencodgo")
  @ResponseBody
  public JsonResult sencodgo(HttpServletRequest request) {
    String language = (String) request.getAttribute("language");
    User user = SessionUtils.getUser(request);
    /*
     * Integer googleState = (Integer)
     * request.getSession().getAttribute("googleState"); Integer phoneState =
     * (Integer) request.getSession().getAttribute("phoneState");
     * user.setGoogleState(googleState); user.setPhoneState(phoneState);
     */
    if (user != null) {
      return new JsonResult().setSuccess(true).setObj(user);
    } else {
      return new JsonResult().setSuccess(false).setObj(user);

    }

  }

  /**
   * 登录ajax方法
   */
  @RequestMapping("loginService")
  @ResponseBody
  public JsonResult loginService(HttpServletRequest request, HttpServletResponse response) {
    StringBuffer sb = new StringBuffer();
    String username = request.getParameter("username");
    if (!StringUtils.isEmpty(username)) {
      username = username.toLowerCase();
    }
    String mobile = request.getParameter("mobile");
    String country = request.getParameter("country");
    String password = request.getParameter("password");
    if (StringUtils.isEmpty(username) && StringUtils.isEmpty(mobile)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("login_nameorpwd_erro"));
    }

    try {
      // 生成tokenid
      UUID uuid = UUID.randomUUID();
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult login = null;
      if (!StringUtils.isEmpty(country) && !StringUtils.isEmpty(mobile)) {
        login = remoteManageService.login(mobile, password, country, uuid.toString());
      } else {
        login = remoteManageService.login(username, password, uuid.toString());
      }
      if (login != null && login.getSuccess()) {
        User user = (User) login.getObj();
        //此处为半解绑改动将phoneState换成CheckStates
        if (user.getGoogleState() == 1 || user.getCheckStates() == 1) {
          Object object = request.getSession().getAttribute("isAuthentication");
          if (object != null) {
            String isAuthentication = object.toString();
            if (!isAuthentication.equals("1")) {
              request.getSession().removeAttribute("isAuthentication");
              return new JsonResult().setSuccess(false)
                  .setMsg(SpringContextUtil.diff("isAuthenticationshibai"));
            }
          } else {
            request.getSession().removeAttribute("isAuthentication");
            return new JsonResult().setSuccess(false)
                .setMsg(SpringContextUtil.diff("isAuthenticationshibai"));
          }

        }
        request.getSession().removeAttribute("isAuthentication");
        request.getSession().setAttribute("user", user);
        User user1 = (User) request.getSession().getAttribute("user");

        log.info(username + "|登录成功!");
        userRedisRunnable.process(user.getCustomerId().toString());

        RedisService redisService = SpringContextUtil.getBean("redisService");
        user.setTokenId(uuid.toString());
        redisService.save("mobile:" + uuid,
            "{\"mobile\":\"" + username + "\",\"user\":" + JSON.toJSON(login.getObj()).toString()
                + "}", 1800);
        log.info("UUID=" + uuid);
        Cookie cookie = new Cookie("tokenId", uuid.toString());

        cookie.setMaxAge(1800);
        cookie.setPath("/");
        response.addCookie(cookie);
        String ip = IpUtil.getIp(request);
        System.out.println("当前登录IP=" + ip);
        String ipnumber = ip.substring(0, ip.indexOf("."));
        /*
         * if (user.getMessIp() != null) { String messIp = user.getMessIp(); String
         * ipnumb = messIp.substring(0, messIp.indexOf(".")); String addres =
         * AddressesUtil.getAddres(ip); SimpleDateFormat formatter = new
         * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String dateString =
         * formatter.format(new Date()); if (!ipnumber.equals(ipnumb)) { // 发送异地登录邮件
         * String email=username; String type="2";
         * sb.append("Dear "+email+"<br><br>"+SpringContextUtil.diff("ipdirst")+ip+"<br>");
         * sb.append(SpringContextUtil.diff("iptwo")+"<br><br>");
         * //sb.append(SpringContextUtil.diff("Resetendemail")+"<br>"+SpringContextUtil.diff(
         * "Resetendem")); //ThreadPool.exe(new
         * EmailRunnable(email,sb.toString(),type)); } return new
         * JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff(login.getMsg())).setObj(
         * uuid); } else {
         */
        RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
        manageService.savaIp(user.getMobile(), ip);
        // }
        return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff(login.getMsg()))
            .setObj(uuid);

      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error("登录方法远程调用出错");
    }
    return new JsonResult().setMsg(SpringContextUtil.diff("login_nameorpwd_erro"));
  }

  @RequestMapping("resetPassword/{code}/{language}")
  public ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response,
      @PathVariable String code, @PathVariable String language) {
    String[] split = language.split("_");
    if (split.length > 1) {
      resolver.setLocale(request, response, new Locale(split[0], split[1]));
    } else {
      resolver.setLocale(request, response, new Locale(split[0]));
    }
    //邮箱账号
    String email = redisService.get("forget:" + code);
    // 判断redis有没有code，如果没有则超时
    if (StringUtils.isEmpty(email)) {
      return new ModelAndView("/forgetpwd1");
    }
    RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
    RemoteResult regist = manageService.emailvail(code);
    ModelAndView mav = new ModelAndView("/resetPassword");
    if (regist != null && !"".equals(regist)) {
      if (regist.getSuccess() == null || !regist.getSuccess()) {
        return null;
      }
      if (regist.getSuccess()) {
        request.getSession().setAttribute("forgetEmail", email);
        return mav;
      }
    }
    return null;

  }

  @RequestMapping("activation/{email}/{code}")
  public ModelAndView activation(@PathVariable String email, @PathVariable String code) {
    ModelAndView mav = new ModelAndView("/activation");

    try {
      RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult regist = manageService.activation(code);
      if (regist != null) {
        if (regist.getSuccess()) {
          mav.addObject("message", SpringContextUtil.diff("jihuochenggong"));
          return mav;
        }
      }
    } catch (Exception e) {
      log.error("注册方法远程调用出错");
    }

    mav.addObject("message", SpringContextUtil.diff("jihuoshibai"));

    return mav;
  }

  /**
   * 手机注册ajax方法
   */
  @RequestMapping("registService2")
  @ResponseBody
  public JsonResult registService2(HttpServletRequest request, HttpServletResponse response) {
    String language = (String) request.getAttribute("language");
    Locale locale = LocaleContextHolder.getLocale();

    // 手机号
    String mobile = request.getParameter("mobile");
    // 国家
    String country = request.getParameter("country");
    // 密码
    String password = request.getParameter("password");
    // 图形验证码
    String registCode = request.getParameter("registCode");
    // 手机验证码
    String registSmsCode = request.getParameter("registSmsCode");
    // 邀请码
    String referralCode = request.getParameter("referralCode");

    if (StringUtils.isEmpty(mobile)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("telphone_is_not_null"));
    } else {// 去空格
      mobile = mobile.trim().replace(" ", "");
    }
    if (StringUtils.isEmpty(password)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
    }
    if (StringUtils.isEmpty(registCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tx_is_not_null"));
    }
    // session图形验证码
    String session_registcode = (String) request.getSession().getAttribute("registCode");
    if (!registCode.equalsIgnoreCase(session_registcode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tx_error"));
    }
    // session手机验证码
    String session_registSmsCode = (String) request.getSession()
        .getAttribute("registSmsCode" + mobile);
    if (!registSmsCode.equalsIgnoreCase(session_registSmsCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
    }

    if (!"".equals(referralCode) && referralCode != null) {
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult selectPhone = remoteManageService.selectAgent(referralCode);
      if (!selectPhone.getSuccess()) {
        return new JsonResult().setMsg(SpringContextUtil.diff("dailisbucunzai"));
      }
    }
    try {
      RedisService redisService = SpringContextUtil.getBean("redisService");
      String config = redisService.get("configCache:all");
      JSONObject parseObject = JSONObject.parseObject(config);
      RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult regist = manageService.registMobile(mobile, password, referralCode, country,
          parseObject.get("language_code").toString());

      if (regist != null) {
        if (regist.getSuccess()) {
          // 删除验证码
          request.getSession().removeAttribute("registCode");
          return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("reg_success"));
        } else {
          return new JsonResult().setMsg(SpringContextUtil.diff(regist.getMsg()));
        }
      }
      return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("reg_success"));
    } catch (Exception e) {
      log.error("注册方法远程调用出错");
    }

    return new JsonResult();

  }

  /**
   * 注册ajax方法 在service层新增设置checkstates的状态为了区分新老用户。
   *
   * @author huangjia
   */
  @RequestMapping("registService")
  @ResponseBody
  public JsonResult registService(HttpServletRequest request, HttpServletResponse response) {
    String language = (String) request.getAttribute("language");
    Locale locale = LocaleContextHolder.getLocale();

    // 邮箱
    String email = request.getParameter("email").toLowerCase();
    // 密码
    String password = request.getParameter("password");
    // 图形验证码
    String registCode = request.getParameter("registCode");
    // 邀请码
    String referralCode = request.getParameter("referralCode");

    if (StringUtils.isEmpty(email)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tel_is_not_null"));
    }
    if (StringUtils.isEmpty(password)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
    }
    if (StringUtils.isEmpty(registCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tx_is_not_null"));
    }
    // session图形验证码
    String session_registcode = (String) request.getSession().getAttribute("registCode");
    if (!registCode.equalsIgnoreCase(session_registcode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tx_error"));
    }
    if (!"".equals(referralCode) && referralCode != null) {
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult selectPhone = remoteManageService.selectAgent(referralCode);
      if (!selectPhone.getSuccess()) {
        return new JsonResult().setMsg(SpringContextUtil.diff("dailisbucunzai"));
      }
    }
    try {
      RedisService redisService = SpringContextUtil.getBean("redisService");
      String config = redisService.get("configCache:all");
      JSONObject parseObject = JSONObject.parseObject(config);
      RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult regist = manageService
          .regist(email, password, referralCode, parseObject.get("language_code").toString());

      if (regist != null) {
        if (regist.getSuccess()) {
          String url =
              PropertiesUtils.APP.getProperty("app.url") + "/activation/" + email + "/" + regist
                  .getObj();
          // 发送邮件
          StringBuffer sb = new StringBuffer();
          sb.append(
              SpringContextUtil.diff("dear") + " " + email + "<br><br>" + SpringContextUtil.diff("regestone")
                  + "<br><br>" + SpringContextUtil.diff("regesttwo") + "<br><br>" + SpringContextUtil
                  .diff("browseropen") + "<br><br>");
          sb.append("<a href='" + url + "'>" + url + "</a>");
          sb.append("<br><br>");

          // sb.append("/activation/"+email+"/"+regist.getObj()+"<br><br>"+"<input
          // type='button' value='提交'
          // onClick='location.href="+PropertiesUtils.APP.getProperty("app.url")+"/activation/"+email+"/"+regist.getObj()+"'
          // />");
          // sb.append(SpringContextUtil.diff("Resetendemail")+"<br>"+SpringContextUtil.diff("Resetendem"));
          String type = "1";
          ThreadPool.exe(new EmailRunnable(email, sb.toString(), type, locale));
          return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("reg_success"));
        } else {
          return new JsonResult().setMsg(SpringContextUtil.diff(regist.getMsg()));
        }
      }
      return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("reg_success"));
    } catch (Exception e) {
      log.error("注册方法远程调用出错：" + e.getMessage());
    }

    return new JsonResult().setMsg(SpringContextUtil.diff("zhuceshibai"));

  }

  /**
   *
   *
   * @param request
   * @param response
   */
  @RequestMapping("forgetService")
  @ResponseBody
  public JsonResult forgetService(HttpServletRequest request, HttpServletResponse response) {

    String ip = IpUtil.getIp(request);
    log.info("找回密码ip地址" + ip);
    System.out.println("找回密码ip地址" + ip);

    // 邮箱
    String email = request.getParameter("email");
    if (StringUtils.isEmpty(email)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tel_is_not_null"));
    }

    int count = 0;
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String val = redisService.get("ip:forpwd:" + ip);
    if (!StringUtils.isEmpty(val)) {
      String num = redisService.get("ip:forpwd:" + ip);
      count = Integer.parseInt(num);
    } else {
      redisService.save("ip:forpwd:" + ip, "1", 60 * 60 * 24);
    }

    if (count <= 5) {

      String language = (String) request.getAttribute("language");
      Locale locale = LocaleContextHolder.getLocale();
      if (StringUtils.isEmpty(email)) {
        return new JsonResult().setMsg(SpringContextUtil.diff("tel_is_not_null"));
      }
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");

      User selectByTel = remoteManageService.selectByTel(email);
      if (StringUtils.isEmpty(selectByTel.getCustomerId())) {
        return new JsonResult().setMsg(SpringContextUtil.diff("userisnull"));

      }
      RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult regist = manageService.forget(email, "");
      if (regist != null) {
        if (regist.getSuccess()) {
          StringBuffer sb = new StringBuffer();
          sb.append(SpringContextUtil.diff("dear") + " " + email + "<br><br>" + SpringContextUtil
              .diff("Resetfirstemail") + "<br><br>" + SpringContextUtil.diff("Resettwoemail")
              + "<br><br>");
          sb.append(PropertiesUtils.APP.getProperty("app.url"));
          sb.append(
              "/resetPassword/" + "/" + regist.getObj() + "/" + locale.toString() + "<br><br>");
          String type = "3";
          ThreadPool.exe(new EmailRunnable(email, sb.toString(), type, locale));
          // 发送邮件
          // 计时，time秒后 找回密码链接失效
          String config = redisService.get("configCache:all");
          JSONObject parseObject = JSONObject.parseObject(config);
          Integer time =
              parseObject.get("resetPwdTime") == null ? 60 : parseObject.getInteger("resetPwdTime");
          redisService.save("forget:" + regist.getObj().toString(), email, time * 60);

          redisService.save("ip:forpwd:" + ip, count + 1 + "", 60 * 60 * 24);

          return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("reg_success"));
        }
      } else {
        return new JsonResult().setSuccess(false).setMsg("失败");

      }
      return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("reg_success"));
    }
    return new JsonResult().setSuccess(false).setMsg("找回加次数过多，请一天后再试");

  }

  /**
   * 注销方法
   */
  @RequestMapping("logout")
  public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
    // request.getSession().removeAttribute("user");
    Locale locale = LocaleContextHolder.getLocale();
    SessionUtils.logout(request, response);
    model.addAttribute("user", null);
    model.addAttribute("locale", locale);
    String config = redisService.get("configCache:all");
    if (!StringUtils.isEmpty(config)) {
      JSONObject parseObject = JSONObject.parseObject(config);
      model.addAttribute("isOpenLanguage", parseObject.get("isOpenLanguage"));
    }
    return "index";
  }

  /**
   * 跳转到登录交易大厅页面
   */
  @RequestMapping("market")
  public ModelAndView market(HttpServletRequest request, HttpServletResponse response) {
    ModelAndView mav = new ModelAndView("market");

    User user = null;
    String tokenId = request.getParameter("tokenId");

    String symbol = request.getParameter("symbol");
    // 重定向到默认币种
    if (StringUtils.isEmpty(symbol) || !symbol.contains("_")) {
      String str = redisService.get("cn:coinInfoList");
      if (!StringUtils.isEmpty(str)) {
        List<Coin> coins = JSON.parseArray(str, Coin.class);
        if (coins != null && coins.size() > 0) {
          Coin coin = coins.get(0);
          mav.addObject("defaultCoin", coin.getCoinCode() + "_" + coin.getFixPriceCoinCode());
          try {

            response.sendRedirect(
                "market?symbol=" + coin.getCoinCode() + "_" + coin.getFixPriceCoinCode());

            return null;
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }

    if (!StringUtils.isEmpty(symbol)) {
      String[] split = symbol.split("_");
      String str = redisService.get("cn:coinInfoList");

      // 查交易币的位数
      if (!StringUtils.isEmpty(str)) {
        List<Coin> coins = JSON.parseArray(str, Coin.class);
        if (coins != null && coins.size() > 0) {
          for (Coin coin : coins) {
            if (split[0].equals(coin.getCoinCode()) && split[1]
                .equals(coin.getFixPriceCoinCode())) {
              mav.addObject("keepDecimalForCoin", coin.getKeepDecimalForCoin());
              mav.addObject("keepDecimalForCurrency", coin.getKeepDecimalForCurrency());
            }
          }
        }
      }
    }

    return mav;
  }

  // 查询查询系统基础配置
  public List<AppBanner> listBanner() {
    List<AppBanner> list = new ArrayList<AppBanner>();
    String text = redisService.get(StringConstant.CACHE_BANNER);
    if (!StringUtils.isEmpty(text)) {
      list = JSON.parseArray(text, AppBanner.class);
    }
    return list;
  }

  @Autowired
  private CookieLocaleResolver resolver;

  /**
   * 语言切换
   */
  @RequestMapping("language")
  public ModelAndView language(HttpServletRequest request, HttpServletResponse response) {
    String language = request.getParameter("language").toLowerCase();
    String split_ = request.getParameter("split");

    if (language == null || language.equals("")) {
      return new ModelAndView("redirect:/");
    } else {
      String[] split = language.split("_");
      if (split.length > 1) {
        resolver.setLocale(request, response, new Locale(split[0], split[1]));
      } else {
        resolver.setLocale(request, response, new Locale(split[0]));
      }
      return new ModelAndView("redirect:" + split_);
    }
  }

  @RequestMapping("ico2exchange")
  @ResponseBody
  public ModelAndView ico2exchange(HttpServletRequest request, HttpServletResponse response) {
    ModelAndView view = new ModelAndView("index");
    if (request.getSession().getAttribute("hasico") != null) {
      request.getSession()
          .setAttribute("hasico", !(boolean) request.getSession().getAttribute("hasico"));
    } else {
      request.getSession().setAttribute("hasico", true);
    }
    return view;
  }

  // 邀请排行榜
  @RequestMapping("selectCommendRanking")
  @ResponseBody
  public JsonResult selectCommendRanking(HttpServletRequest request, HttpServletResponse response) {

    JsonResult jsonResult = new JsonResult();
    RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");

    List<commendCode> selectCommendRanking = manageService.selectCommendRanking();
    jsonResult.setObj(selectCommendRanking);
    return jsonResult;

  }

  /**
   * 添加收藏
   */
  @RequestMapping("addCustomerCollection")
  @ResponseBody
  public JsonResult addCustomerCollection(HttpServletRequest request) {

    JsonResult jsonResult = new JsonResult();
    String coinCode = request.getParameter("coinCode");
    User user = (User) request.getSession().getAttribute("user");
    if (user != null) {
      RemoteBaseInfoService remoteBaseInfoService = SpringContextUtil.getBean("remoteBaseInfoService");
      remoteBaseInfoService.addCustomerCollection(user.getCustomerId(), coinCode);
      return jsonResult.setSuccess(true).setMsg(SpringContextUtil.diff("success"));
    }

    return jsonResult.setMsg(SpringContextUtil.diff("before_login"));

  }

  /**
   * 移除收藏
   */
  @RequestMapping("removeCustomerCollection")
  @ResponseBody
  public JsonResult removeCustomerCollection(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String coinCode = request.getParameter("coinCode");
    User user = (User) request.getSession().getAttribute("user");
    if (user != null) {
      RemoteBaseInfoService remoteBaseInfoService = SpringContextUtil.getBean("remoteBaseInfoService");
      remoteBaseInfoService.deleteCustomerCollection(user.getCustomerId(), coinCode);
      return jsonResult.setSuccess(true).setMsg(SpringContextUtil.diff("success"));
    }

    return jsonResult.setMsg(SpringContextUtil.diff("before_login"));
  }

}
