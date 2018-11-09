package com.mz.exapi.util;

import com.mz.util.EmailUtil;
import com.mz.util.PropertiesUtils;
import java.util.Locale;
import org.apache.log4j.Logger;

/**
 * 登录后更新用户账户id保存到redis中	
 * @author CHINA_LSL
 *
 */
public class EmailRunnable implements Runnable{
	
	private final Logger log = Logger.getLogger(EmailRunnable.class);
	
	private String email;
	
	
	
	private Locale locale;
	
	
	private String type;
	
	
	private String sb ;

	
	
/*	public EmailRunnable(String email,String password,String sb,String type){
		this.email = email;
		this.sb=sb;
		this.password=password;
		this.type=type;
	}*/
	
	public EmailRunnable(String email,String sb,String type){
		this.email = email;
		this.sb=sb;
		this.type=type;
	}
	

	public EmailRunnable(String email, String string, String type, Locale locale) {
		// TODO Auto-generated constructor stub
		this.email = email;
		this.sb=string;
		this.type=type;
		this.locale=locale;
	}

	@Override
	public void run() {
		String emailaddress = PropertiesUtils.APP.getProperty("app.email.emailaddress");
		String newip = PropertiesUtils.APP.getProperty("app.email.newip");
		String passwordreset = PropertiesUtils.APP.getProperty("app.email.passwordreset");
		String emailaddresscn = PropertiesUtils.APP.getProperty("app.email.emailaddresscn");
		String passwordresetcn = PropertiesUtils.APP.getProperty("app.email.passwordresetcn");
		
		
		if("zh_CN".equals(locale.toString())){
			if(type.equals("1")){
				
				EmailUtil.sendMail(email, emailaddresscn, sb);
			}else if(type.equals("2")){
				
				EmailUtil.sendMail(email, newip, sb);
			}else if(type.equals("3")){
				
				EmailUtil.sendMail(email, passwordresetcn, sb);
			}
		}else{
			if(type.equals("1")){
				
				EmailUtil.sendMail(email, emailaddress, sb);
			}else if(type.equals("2")){
				
				EmailUtil.sendMail(email, newip, sb);
			}else if(type.equals("3")){
				
				EmailUtil.sendMail(email, passwordreset, sb);
			}
		}
	
	}
}
