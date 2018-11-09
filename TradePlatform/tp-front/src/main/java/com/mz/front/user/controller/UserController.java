package com.mz.front.user.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.AppConfig;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.manage.remote.RemoteManageService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.model.Coin;
import com.mz.manage.remote.model.CoinAccount;
import com.mz.manage.remote.model.MyAccountTO;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.UserInfo;
import com.mz.manage.remote.model.commendCode;
import com.mz.util.FileType;
import com.mz.util.FileUpload;
import com.mz.util.SessionUtils;
import com.mz.util.sys.SpringContextUtil;;
import com.mz.util.filter.XssHttpServletRequestWrapper;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


@Controller("frontUserController")
@RequestMapping("/user")
public class UserController {

    private final static Logger log = Logger.getLogger(UserController.class);


    /**
     * 注册类型属性编辑器
     *
     * @param binder
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
        binder.registerCustomEditor(String.class, new StringPropertyEditorSupport(true, true));
    }


    @Autowired
    private RedisService redisService;


    /**
     * 个人中心主页
     *
     * @return
     */
    @RequestMapping("center")
    public ModelAndView center(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("base/user/center");
        Locale locale = LocaleContextHolder.getLocale();
        Cookie[] cos = request.getCookies();
        if (cos != null) {
            for (Cookie c : cos) {
                if ("tokenId".equals(c.getName())) {
                    mav.addObject("tokenId", c.getValue());
                }
            }
        }
        //是否开启语言切换
        RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil.getBean("remoteAppTransactionManageService");
        List<AppConfig> isOpenLanguage = remoteAppTransactionManageService.getConfigInfo("isOpenLanguage");
        if (isOpenLanguage.size() == 0) {
            mav.addObject("isOpenLanguage", "1");
        } else {
            String value = isOpenLanguage.get(0).getValue();
            mav.addObject("isOpenLanguage", value);
        }
        mav.addObject("locale", locale);
        return mav;
    }


    /**
     * 个人中心---账户中心
     *
     * @return
     */
    @RequestMapping("index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("front/user/index");
        User user = SessionUtils.getUser(request);
        //ThreadPool.exe(new UserRedisRunnable(user.getCustomerId().toString()));
        if (user != null) {
            Cookie[] cos = request.getCookies();
            if (cos != null) {
                for (Cookie c : cos) {
                    if ("tokenId".equals(c.getName())) {
                        mav.addObject("tokenId", c.getValue());
                    }
                }
            }

            //查询账户金额
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            MyAccountTO myAccount = remoteManageService.myAccount(user.getCustomerId());
            mav.addObject("myaccount", myAccount);

            //查询币账户
            List<CoinAccount> findCoinAccount = remoteManageService.findCoinAccount(user.getCustomerId());
            mav.addObject("coinAccountList", findCoinAccount);

            //从redis查图片路径
            if (findCoinAccount != null && findCoinAccount.size() > 0) {
                String string = redisService.get("cn:coinInfoList");
                List<Coin> coins = JSONArray.parseArray(string, Coin.class);

                for (Coin coin : coins) {
                    for (CoinAccount coinAccount : findCoinAccount) {
                        if (coin.getCoinCode().equals(coinAccount.getCoinCode())) {
                            coinAccount.setPicturePath(coin.getPicturePath());
                        }
                    }
                }
            }
        }
        String config = redisService.get("configCache:all");
        if (!StringUtils.isEmpty(config)) {
            JSONObject parseObject = JSONObject.parseObject(config);
            mav.addObject("isOpenLanguage", parseObject.get("isOpenLanguage"));
        }
        JSONObject parseObject = JSONObject.parseObject(config);
        mav.addObject("languageCode", parseObject.get("language_code"));//当前币种
        return mav;
    }

    /**
     * 获取个人账户资金
     *
     * @return
     */
    @RequestMapping("getAccountInfo")
    @ResponseBody
    public JsonResult getAccountInfo(HttpServletRequest request) {

        String coinCode = request.getParameter("coinCode");

        if (!StringUtils.isEmpty(coinCode) && coinCode.contains("_")) {
            User user = SessionUtils.getUser(request);
            if (user != null) {
                RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
                RemoteResult result = remoteManageService.getAccountInfo(coinCode, user.getCustomerId());
                return new JsonResult().setSuccess(true).setObj(result.getObj());
            } else {
                return new JsonResult().setSuccess(false);
            }
        }
        return new JsonResult();
    }

    /**
     * 实名成功页面信息
     *
     * @return
     */
    @RequestMapping("realinfo")
    public ModelAndView realinfo(HttpServletRequest request) {
        Locale locale = LocaleContextHolder.getLocale();
        ModelAndView mav = new ModelAndView();
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        User user = SessionUtils.getUser(request);
        User selectByTel = remoteManageService.selectByCustomerId(user.getCustomerId());
        // Integer states = (Integer) request.getSession().getAttribute("states");
        //未实名，往实名页跳
        if (selectByTel.getStates() != null && selectByTel.getStates() == 0) {
            mav.setViewName("front/user/identity");
            return mav;
        } else if (selectByTel.getStates() == 3) {
            mav.setViewName("front/user/realinfono");
            return mav;
        } else if (selectByTel.getStates() == 1) {//已实名=
            mav.setViewName("front/user/realinfo");
            try {
                RemoteResult result = remoteManageService.getPersonInfo(user.getUserCode());
                if (result != null && result.getSuccess()) {
                    UserInfo userInfo = (UserInfo) result.getObj();
                    if ("1".equals(userInfo.getType())) {
                        if ("en".equals(locale.toString())) {
                            userInfo.setPapersType("ID Card");
                        }
                    } else if ("2".equals(userInfo.getType())) {
                        if ("en".equals(locale.toString())) {
                            userInfo.setPapersType("Passport");
                        }
                    }
                    mav.addObject("info", result.getObj());
                }
            } catch (Exception e) {
                log.error("实名成功页面加载客户信息错误");
            }
            return mav;
        } else if (selectByTel.getStates() == 2) {
            mav.setViewName("front/user/realinfosu");
            try {
                RemoteResult result = remoteManageService.getPersonInfo(user.getUserCode());
                if (result != null && result.getSuccess()) {
                    UserInfo userInfo = (UserInfo) result.getObj();
                    if ("1".equals(userInfo.getType())) {
                        if ("en".equals(locale.toString())) {
                            userInfo.setPapersType("ID Card");
                        }
                    } else if ("2".equals(userInfo.getType())) {
                        if ("en".equals(locale.toString())) {
                            userInfo.setPapersType("Passport");
                        }
                    }
                    mav.addObject("info", result.getObj());
                }
            } catch (Exception e) {
                log.error("实名成功页面加载客户信息错误");
            }
            return mav;
        }
        return mav;
    }


    /**
     * 登录密码页面
     *
     * @return
     */
    @RequestMapping("loginPass")
    public ModelAndView loginPass(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        //未实名，往实名页跳
        mav.setViewName("front/user/setpw");
        return mav;
    }


    /**
     * 实名认证 提交
     *
     * @return
     */
    @RequestMapping("realname")
    @ResponseBody
    public JsonResult realname(HttpServletRequest request) {
        String language = (String) request.getAttribute("language");

        String trueName = request.getParameter("trueName");
        String country = request.getParameter("country");
        String cardType = request.getParameter("cardType");
        String cardId = request.getParameter("cardId");

        if (StringUtils.isEmpty(trueName)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("username_no_null"));
        }
        if (StringUtils.isEmpty(country)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("country_no_null"));
        }
        if (StringUtils.isEmpty(cardType)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("cardtype_no_null"));
        }
        if (StringUtils.isEmpty(cardId)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("card_no_null"));
        }

        User user = SessionUtils.getUser(request);

        try {
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult realname = remoteManageService.realname(user.getMobile(), trueName, "", country, cardType, cardId);
            if (realname != null) {
                if (realname.getSuccess()) {
                    //更新session状态
                    user.setIsReal(1);
                    user.setTruename(trueName);
                    request.getSession().setAttribute("user", user);
                    return new JsonResult().setSuccess(true);
                } else {
                    return new JsonResult().setMsg(SpringContextUtil.diff(realname.getMsg()));
                }
            }
        } catch (Exception e) {
            log.error("实名认证远程调用出错");
            e.printStackTrace();
            return new JsonResult().setMsg(SpringContextUtil.diff("yichang"));
        }


        return new JsonResult();

    }


    /**
     * 实名认证 提交2
     *
     * @return
     */
    @RequestMapping("identity")
    @ResponseBody
    public JsonResult identity(@RequestParam("file") MultipartFile[] files, HttpServletRequest request) {

        request = new XssHttpServletRequestWrapper(request);
        String type = request.getParameter("type");
        String trueName = request.getParameter("trueName");
        String sex = request.getParameter("sex");
        String surname = request.getParameter("surname");
        String country = request.getParameter("country");
        String cardType = request.getParameter("cardType");
        String cardId = request.getParameter("cardId");

        User user = SessionUtils.getUser(request);
        if (country.equals("") || country == null) {
            country = "+86";
        }
        try {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                String names=file.getOriginalFilename();
                if (names != null && (names.endsWith("jpg") || names.endsWith("png") || names.endsWith("gif") || names.endsWith("bmp"))) {
                } else {
                    return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("picture_error"));
                }

                InputStream inputStream = file.getInputStream();
                String fileType = FileType.getFileType(inputStream);
                if (fileType != null && (fileType.equals("jpg") || fileType.equals("png") || fileType.equals("gif") || fileType.equals("bmp"))) {
                } else {
                    return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("picture_error"));
                }
            }
            //上传图片
            // String[] pathImg = this.upload(files); 更换照片上传方式  modify by zongwei
            String[] pathImg = FileUpload.POSTFileQiniu(request, files);

            RedisService redisService = SpringContextUtil.getBean("redisService");
            String config = redisService.get("configCache:all");
            JSONObject parseObject = JSONObject.parseObject(config);
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult realname = remoteManageService.xstar(user.getUsername(), trueName, sex, surname, country, cardType, cardId, pathImg, type, parseObject.get("language_code").toString());
            if (realname != null) {
                if (realname.getSuccess()) {
                    user.setStates(1);
                    //北京处理跳号问题 -- 2018.4.21
                    //SessionUtils.updateRedis(user);
                    request.getSession().setAttribute("user", user);
                    //北京处理跳号问题 -- 2018.4.21

                    //更新session状态
					/*user.setIsReal(1);
					user.setTruename(trueName);
					request.getSession().setAttribute("states",1);
					request.getSession().setAttribute("user",user);*/
                } else {
                    return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff(realname.getMsg()));
                }
            }
        } catch (Exception e) {
            log.error("实名认证远程调用出错");
            e.printStackTrace();
        }
        return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("tijiaochenggongshenhe"));

    }

    /**
     * 上传图片
     *
     * @param
     * @return
     */
    public String[] upload(@RequestParam("file") MultipartFile[] files) {
        String[] pathImg = new String[3];
        try {
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    //获取文件名
                    String filename = file.getOriginalFilename();
                    //上传图片
                    if (file != null && filename != null && filename.length() > 0) {
                        //上传路径


                        String realPath = this.getRequest().getRealPath("/");
                        //生成hryfile路径
                        String rootPath = realPath.substring(0, org.apache.commons.lang3.StringUtils.lastOrdinalIndexOf(realPath, File.separator, 2) + 1);
                        System.out.println("rootPath" + rootPath);
                        //新图片名称
                        String newFileName = UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));
                        pathImg[i] = "hryfilefront" + File.separator + newFileName;
                        File secondFolder = new File(rootPath + "hryfilefront");
                        //存入本地
                        if (!secondFolder.exists()) {
                            secondFolder.mkdirs();
                        }
                        File newFile = new File(rootPath + "hryfilefront" + File.separator + newFileName);
                        file.transferTo(newFile);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pathImg;
    }


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
     * 实名认证图片上传
     *
     * @param
     * @return
     */
    @RequestMapping("upload")
    @ResponseBody
    public String upload111(@RequestParam("file") MultipartFile[] files) {
        try {
            if (files != null && files.length > 0) {
                //循环获取file数组中得文件
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    // 原始名称
                    String originalFilename = file.getOriginalFilename();

                    // 上传图片
                    if (file != null && originalFilename != null
                            && originalFilename.length() > 0) {
                        //获取web项目根路径
                        String realPath = this.getRequest().getRealPath("/");
                        //生成hryfile路径
                        String rootPath = realPath.substring(0, org.apache.commons.lang3.StringUtils.lastOrdinalIndexOf(realPath, File.separator, 2) + 1);
                        // 存储图片的物理路径
                        String pic_path = "F:\\develop\\upload\\temp\\";

                        // 新的图片名称
                        String newFileName = UUID.randomUUID()
                                + originalFilename.substring(originalFilename
                                .lastIndexOf("."));
                        // 新图片
                        File newFile = new File(pic_path + newFileName);

                        // 将内存中的数据写入磁盘
                        file.transferTo(newFile);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改登录密码
     *
     * @return
     */
    @RequestMapping("setpw")
    @ResponseBody
    public JsonResult setpw(HttpServletRequest request) {
        String language = (String) request.getAttribute("language");

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

        try {
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult result = remoteManageService.setpw(user.getUsername(), oldPassWord, newPassWord);

            if (result != null) {
                if (result.getSuccess()) {
                    if (user != null) {
                        user.setPassDate(new Date());
                        Object obj = result.getObj();
                        user.setPassword(obj.toString());
                        //北京处理跳号问题 -- 2018.4.21
                        //SessionUtils.updateRedis(user);
                        request.getSession().setAttribute("user", user);
                        //北京处理跳号问题 -- 2018.4.21
                    }
                    return new JsonResult().setSuccess(true);
                } else {
                    return new JsonResult().setMsg(SpringContextUtil.diff(result.getMsg()));
                }
            }
        } catch (Exception e) {
            log.error("修改登录密码远程调用出错");
            return new JsonResult().setMsg(SpringContextUtil.diff("yichang"));
        }


        return new JsonResult();

    }


    /**
     * 设置交易密码
     *
     * @return
     */
    @RequestMapping("setapw")
    public ModelAndView setapw(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        User user = SessionUtils.getUser(request);
        //未前置，前往设置页
        if (StringUtils.isEmpty(user.getAccountPassWord())) {
            mav.setViewName("front/user/setapw");
            return mav;
        } else {//已设置，前往重置页
            mav.setViewName("front/user/resetapw");
            return mav;
        }
    }

    /**
     * 设置交易密码提交
     *
     * @return
     */
    @RequestMapping("setapwsubmit")
    @ResponseBody
    public JsonResult setapwsubmit(HttpServletRequest request) {
        String language = (String) request.getAttribute("language");

        String accountPassWord = request.getParameter("accountPassWord");
        String reaccountPassWord = request.getParameter("reaccountPassWord");
        String accountpwSmsCode = request.getParameter("accountpwSmsCode");

        if (StringUtils.isEmpty(accountPassWord)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("jypwd_no_null"));
        }
        if (StringUtils.isEmpty(reaccountPassWord)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("okpwd_no_null"));
        }
        if (!accountPassWord.equals(reaccountPassWord)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("twopwd_is_diff"));
        }
        if (StringUtils.isEmpty(accountpwSmsCode)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_is_not_null"));
        }
        String session_accountpwSmsCode = (String) request.getSession().getAttribute("accountpwSmsCode");
        if (!accountpwSmsCode.equals(session_accountpwSmsCode)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
        }

        User user = SessionUtils.getUser(request);

        try {
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult result = remoteManageService.setapw(user.getMobile(), accountPassWord);
            if (result != null) {
                if (result.getSuccess()) {
                    //更新session状态
                    user.setAccountPassWord(result.getObj().toString());
                    request.getSession().setAttribute("user", user);
                    return new JsonResult().setSuccess(true);
                } else {
                    return new JsonResult().setMsg(result.getMsg());
                }
            }
        } catch (Exception e) {
            log.error("修改交易密码远程调用出错");
            return new JsonResult().setMsg(SpringContextUtil.diff("yichang"));
        }
        return new JsonResult();
    }

    /**
     * 重置交易密码提交
     *
     * @return
     */
    @RequestMapping("resetapwsubmit")
    @ResponseBody
    public JsonResult resetapwsubmit(HttpServletRequest request) {
        String language = (String) request.getAttribute("language");

        String passWord = request.getParameter("passWord");
        String accountPassWord = request.getParameter("accountPassWord");
        String reaccountPassWord = request.getParameter("reaccountPassWord");
        String accountpwSmsCode = request.getParameter("accountpwSmsCode");

        if (StringUtils.isEmpty(passWord)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("loginpwd_no_null"));
        }
        if (StringUtils.isEmpty(accountPassWord)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("jypwd_no_null"));
        }
        if (StringUtils.isEmpty(reaccountPassWord)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("okpwd_no_null"));
        }
        if (!accountPassWord.equals(reaccountPassWord)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("twopwd_is_diff"));
        }
        if (StringUtils.isEmpty(accountpwSmsCode)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_is_not_null"));
        }
        String session_accountpwSmsCode = (String) request.getSession().getAttribute("accountpwSmsCode");
        if (!accountpwSmsCode.equals(session_accountpwSmsCode)) {
            return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
        }

        User user = SessionUtils.getUser(request);

        try {
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult result = remoteManageService.resetapw(user.getMobile(), passWord, accountPassWord);
            if (result != null) {
                if (result.getSuccess()) {
                    //更新session状态
                    user.setAccountPassWord(result.getObj().toString());
                    request.getSession().setAttribute("user", user);
                    return new JsonResult().setSuccess(true);
                } else {
                    return new JsonResult().setMsg(SpringContextUtil.diff(result.getMsg()));
                }
            }
        } catch (Exception e) {
            log.error("重置交易密码远程调用出错");
            return new JsonResult().setMsg(SpringContextUtil.diff("yichang"));
        }
        return new JsonResult();
    }


    /**
     * 跳转到币账户页面
     *
     * @param request
     * @return
     */
    @RequestMapping("publickeylist")
    public ModelAndView publickeylist(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        User user = SessionUtils.getUser(request);
		/*//未实名，往实名页跳
	/*	if(user.getIsReal()==0){
			mav.setViewName("front/user/realname");
			return mav;
		}else{//已实名
*/
        mav.setViewName("front/user/publickeylist");
        return mav;
    }

    /**
     * 设置谷歌双重认证
     *
     * @return
     */
    @RequestMapping("setgoogle")
    public ModelAndView setgoogle(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        User user = SessionUtils.getUser(request);
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        User selectByTel = remoteManageService.selectByCustomerId(user.getCustomerId());
        //未前置，前往设置页
        if (selectByTel.getGoogleState() == 0) {
            mav.setViewName("front/user/setgoogle");
            return mav;
        } else if (selectByTel.getGoogleState() == 1) {//已设置，前往重置页
            mav.setViewName("front/user/setagoogle");
            return mav;
        }
        return mav;
    }

    /**
     * 设置手机认证
     *
     * @return
     */
    @RequestMapping("setphone")
    public ModelAndView setphone(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        User user = SessionUtils.getUser(request);
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        User selectByTel = remoteManageService.selectByCustomerId(user.getCustomerId());
        //未前置，前往设置页
        if (selectByTel.getPhoneState() == 0) {
            mav.setViewName("front/user/setphone");
            return mav;
        } else if (selectByTel.getPhoneState() == 1) {//已设置，前往重置页
            String phone = user.getPhone();
            phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            request.setAttribute("phone", phone);
            mav.setViewName("front/user/setaphone");
            return mav;
        }
        return mav;
    }

    /**
     * 绑定邮箱
     *
     * @return
     */
    @RequestMapping("setmail")
    public ModelAndView setmail(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        User user = SessionUtils.getUser(request);
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        User selectByTel = remoteManageService.selectByCustomerId(user.getCustomerId());
        //未前置，前往设置页
        if (selectByTel.getMailStates() == 0) {
            mav.setViewName("front/user/setmail");
            return mav;
        } else if (selectByTel.getMailStates() == 1) {//已设置，前往重置页
            String mail = user.getMail();
            mail = mail.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            request.setAttribute("mail", mail);
            mav.setViewName("front/user/setmail");
            return mav;
        }
        return mav;
    }

    /**
     * 设置手机登录认证(新增功能)
     *
     * @return
     * @author huangjia
     */
    @RequestMapping("setloginphone")
    public ModelAndView setloginphone(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        User user = SessionUtils.getUser(request);
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        User selectByTel = remoteManageService.selectByCustomerId(user.getCustomerId());
        //未前置，前往设置页
        //前往关闭手机登录认证的页面
        if ((selectByTel.getCheckStates() == null || selectByTel.getCheckStates() == 1) && selectByTel.getPhoneState() == 1) {
            mav.setViewName("front/user/setloginaphone");
            return mav;
        } else if (selectByTel.getCheckStates() == 0 && selectByTel.getPhoneState() == 1) {
            //前往开启手机登录认证的页面
            mav.setViewName("front/user/setloginphone");
            return mav;
        }
        mav.setViewName("front/user/setphone");
        return mav;
    }

    /**
     * @return
     */
    @RequestMapping("setcommend")
    public ModelAndView setcommend(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        //未前置，前往设置页
        User user = SessionUtils.getUser(request);
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        String property = PropertiesUtils.APP.getProperty("app.url");
        RemoteResult selectCommend = remoteManageService.selectCommend(user.getMobile(), property);
        mav.addObject("info", selectCommend.getObj());
        mav.setViewName("front/user/invite");
        return mav;
    }

    @RequestMapping("setcommendfind")
    @ResponseBody
    public JsonResult setcommendfind(HttpServletRequest request) {
        User user = SessionUtils.getUser(request);
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        List<commendCode> selectCommendfind = remoteManageService.selectCommendfind(user.getMobile());
        return new JsonResult().setSuccess(true).setObj(selectCommendfind);

    }

    /**
     * 设置gugerenzhens
     *
     * @return
     */
    @RequestMapping("setagoogle")
    public ModelAndView setagoogle(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        //User user = SessionUtils.getUser(request);
        //String googleKey = (String) request.getSession().getAttribute("googleKey");

	/*	//未前置，前往设置页
		if(StringUtils.isEmpty(googleKey)){
			mav.setViewName("front/user/setgoogle");
			return mav;
		}else{//已设置，前往重置页
			mav.setViewName("front/user/setagoogle");
			return mav;
		}*/
        mav.setViewName("front/user/setagoogle");
        return mav;
    }


    /**
     * 设置gugerenzhens
     *
     * @return
     */
    @RequestMapping("identitymav")
    public ModelAndView identitymav(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        User user = SessionUtils.getUser(request);
        Long customerId = user.getCustomerId();
        User selectByTel = remoteManageService.selectByCustomerId(customerId);
        if (selectByTel.getStates() != null && selectByTel.getStates() == 0) {
            mav.setViewName("front/user/identity");
            return mav;
        } else if (selectByTel.getStates() == 3) {
            mav.setViewName("front/user/realinfono");
            return mav;
        } else {//已实名
            RemoteResult result = remoteManageService.getPersonInfo(user.getUserCode());
            mav.addObject("info", result.getObj());
            mav.setViewName("front/user/realinfo");
            return mav;
        }
    }


    /**
     * 设置gugerenzhens
     *
     * @return
     */
    @RequestMapping("identitymavno")
    public ModelAndView identitymavno(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        User user = SessionUtils.getUser(request);
        mav.setViewName("front/user/identity");
        return mav;
    }


}
