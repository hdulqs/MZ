package com.mz.front.mail.controller;


import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.IpUtil;
import com.mz.core.thread.ThreadPool;
import com.mz.util.sys.SpringContextUtil;
import com.mz.front.index.controller.EmailRunnable;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mobile/sendmail")
@Api(value = "App邮箱操作类", description = "邮箱验证码发送")
public class mobilesendMail {

  String mail = "mail:";

  private JsonResult verificationOrder(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String ip = IpUtil.getIp(request);
    RedisService redisService = (RedisService) SpringContextUtil.getBean("redisService");
    Integer telTime = 5;
    Integer telCount = 3;
    String telValue = redisService.get(mail + ip);
    if (telValue == null || "".equals(telValue)) {
      redisService.save(mail + ip, "1", telTime);
    } else {
      Integer num = Integer.valueOf(telValue);
      if (num >= telCount) {
        jsonResult.setCode("0000");
        jsonResult.setMsg(SpringContextUtil.diff("sms_tooMuch"));
        jsonResult.setSuccess(false);
        return jsonResult;
      }
      num++;
      redisService.save(mail + ip, String.valueOf(num), telTime);
    }

    return jsonResult.setSuccess(true);

  }


  @RequestMapping(value = "/mailcode", method = RequestMethod.POST)
  @ResponseBody
  @ApiOperation(value = "发送邮箱验证码", httpMethod = "POST", response = JsonResult.class, notes = "tokenId")
  public JsonResult MailCode(HttpServletRequest request) {

    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }
    JsonResult jsonResult = new JsonResult();
    String email = request.getParameter("mail");
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = (RedisService) SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      String code = RandomStringUtils.random(6, false, true);
      if (user != null) {
        Locale locale = LocaleContextHolder.getLocale();
        // 发送邮件
        StringBuffer sb = new StringBuffer();
        sb.append("<a '>" + "您的验证码是【" + code + "】。如非本人操作，请忽略本短信！" + "</a>");
        sb.append("<br><br>");
        String type = "5";
        System.out.println("发送邮件报错 ---- 接收人为  ： " + email);
        ThreadPool.exe(new EmailRunnable(email, sb.toString(), type, locale));
        redisService.save("Mail:sendMail:" + email, code, 120);
        jsonResult.setMsg("发送成功！");
        jsonResult.setSuccess(true);
        return jsonResult;
      } else {
        jsonResult.setSuccess(false).setMsg("发送失败！");
      }
    }

    return jsonResult.setSuccess(false).setMsg("请登录或重新登录！");
  }
}
