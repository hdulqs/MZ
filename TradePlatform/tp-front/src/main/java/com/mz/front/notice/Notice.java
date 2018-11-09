package com.mz.front.notice;

import com.mz.front.index.controller.EmailRunnable;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.manage.remote.model.User;
import com.mz.core.thread.ThreadPool;
;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Notice {

    public static boolean  setphoneNotice(User user){
        if(user.getPhoneState() == 1){
           String mobile = user.getPhone();
            String mobi = mobile.replace(" ", "");
            // 手机号
            String phone = mobile.substring(mobile.lastIndexOf(" ") + 1);

            // 地区截取
            String address = mobile.substring(0, mobile.indexOf(" "));
            if (address.equals("+86")) {
                SmsParam smsParam = new SmsParam();
                smsParam.setHryMobilephone(mobile);
                smsParam.setHryCode("set_phone");
                Map<String, Object> map = new HashMap<>();
                SmsSendUtil.sendSmsInfo(smsParam, null, mobile, "SYS_SETPHONE_EN", map);
            }else{
                SmsParam smsParam = new SmsParam();
                smsParam.setHryMobilephone(mobile);
                smsParam.setHryCode("set_phone");
                Map<String, Object> map = new HashMap<>();
                SmsSendUtil.sendSmsInfo(smsParam, null, mobile, "SYS_SETPHONE_US", map);
            }
        }
        if(user.getMailStates() == 1){
            String email = user.getMail();
            Locale locale = LocaleContextHolder.getLocale();
            // 发送邮件
            StringBuffer sb = new StringBuffer();
            sb.append("<a '>" + "您手机号码已更换，如非本人操作，请注意！" + "</a>");
            sb.append("<br><br>");
            String type = "4";
            System.out.println("发送邮件报错 ---- 接收人为  ： "+email);
            ThreadPool.exe(new EmailRunnable(email, sb.toString(), type, locale));
        }
        return  true;
    }

}
