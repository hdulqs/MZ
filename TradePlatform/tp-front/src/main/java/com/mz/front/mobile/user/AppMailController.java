package com.mz.front.mobile.user;

import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.sys.SpringContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mobile/mail")
@Api(value = "绑定邮箱操作类")
public class AppMailController {

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
    @ApiOperation(value = "绑定邮箱", httpMethod = "POST", response = JsonResult.class, notes = "tokenId verifyCode：验证码 mail：邮箱")
    public JsonResult setMail(String codes, String savedSecret, HttpServletRequest request) {
        String verifyCode = request.getParameter("verifyCode");//短信验证码
        String mail = "";
        mail = request.getParameter("mail");
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        String tokenId = request.getParameter("tokenId");
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {

            String tel = JSONObject.parseObject(value).getString("mobile");
            User user = remoteManageService.selectByTel(tel);

            if (StringUtils.isEmpty(verifyCode)) {
                return new JsonResult().setMsg(SpringContextUtil.diff("邮箱验证码不能为空"));
            }

            String session_verifyCode = redisService.get("Mail:sendMail:" + mail);
            if (!verifyCode.equals(session_verifyCode)) {
                return new JsonResult().setMsg(SpringContextUtil.diff("邮箱验证码错误或已失效！"));
            }
            if (user != null) {

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
        return new JsonResult().setSuccess(false).setMsg("请登录或重新登录！");

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
    @ApiOperation(value = "邮箱取消绑定", httpMethod = "POST", response = JsonResult.class, notes = "tokenId verifyCode：验证码 mail：邮箱")
    public JsonResult cancelMail(String codes, String savedSecret, HttpServletRequest request) {
        String verifyCode = request.getParameter("verifyCode");//短信验证码
        String mail = "";
        mail = request.getParameter("mail");
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        String tokenId = request.getParameter("tokenId");
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {

            String tel = JSONObject.parseObject(value).getString("mobile");
            User user = remoteManageService.selectByTel(tel);

            if(user == null){
                return new JsonResult().setSuccess(false).setMsg("请登录或重新登录！");
            }

            if (StringUtils.isEmpty(verifyCode)) {
                return new JsonResult().setMsg(SpringContextUtil.diff("邮箱验证码不能为空"));
            }

            String session_verifyCode = redisService.get("Mail:sendMail:" + mail);
            if (!verifyCode.equals(session_verifyCode)) {
                return new JsonResult().setMsg(SpringContextUtil.diff("邮箱验证码错误或已失效！"));
            }




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
        return new JsonResult().setSuccess(false).setMsg("请登录或重新登录！");
    }


    @RequestMapping("setmailtwo")
    @ApiOperation(value = "下一步", httpMethod = "POST", response = JsonResult.class, notes = " tokenId verifyCode：验证码 mail：邮箱")
    @ResponseBody
    public JsonResult setmailtwo(HttpServletRequest request) {
        String verifyCode = request.getParameter("verifyCode");
        String mail = request.getParameter("mail");
        String tokenId = request.getParameter("tokenId");
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {
            String session_verifyCode = redisService.get("Mail:sendMail:" + mail);
            if (verifyCode.equals(session_verifyCode)) {
                return new JsonResult().setSuccess(true).setMsg("成功！");
            }
        }else {
            return new JsonResult().setSuccess(false).setMsg("请登录或重新登录！");
        }
        return new JsonResult().setSuccess(false).setMsg("邮箱验证码不正确！");
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
    @ApiOperation(value = "再次绑定邮箱", httpMethod = "POST", response = JsonResult.class, notes = "tokenId verifyCode：验证码 mail：邮箱")
    public JsonResult setMailtwo(HttpServletRequest request) {
        String verifyCode = request.getParameter("verifyCode");//短信验证码
        String mail = "";
        mail = request.getParameter("mail");
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        String tokenId = request.getParameter("tokenId");
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {

            String tel = JSONObject.parseObject(value).getString("mobile");
            User user = remoteManageService.selectByTel(tel);

            if (user == null) {
                return new JsonResult().setSuccess(false).setMsg("请登录或重新登录！");
            }

            if (StringUtils.isEmpty(verifyCode)) {
                return new JsonResult().setMsg(SpringContextUtil.diff("邮箱验证码不能为空"));
            }


            String session_verifyCode = redisService.get("Mail:sendMail:" + mail);
            if (!verifyCode.equals(session_verifyCode)) {
                return new JsonResult().setMsg(SpringContextUtil.diff("邮箱验证码错误或已失效！"));
            }

            if (user != null) {

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
        return new JsonResult().setSuccess(false).setMsg("请登录或重新登录！");
    }


}
