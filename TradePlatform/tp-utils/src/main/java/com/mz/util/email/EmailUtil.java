/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年6月30日 下午3:11:50
 */
package com.mz.util.email;

import com.mz.util.properties.PropertiesUtils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * 
 * <p> TODO</p>
 * @author:         Wu Shuiming
 * @Date :          2016年6月30日 下午3:11:50
 * 
 *  第一个参数是接收人邮箱地址
 *  
 *  第二个参数是邮件标题 
 *  
 *  第三个参数是邮件正文 
 * 
 * 
 */
public class EmailUtil {
	
	/**
	 * receivUser ： 接收人
	 * mailTitle ： 邮件标题
	 * mailContent ： 邮件正文
	 * 
	 * @author:    Wu Shuiming
	 * @version:   V1.0 
	 * @date:      2016年8月15日 下午2:24:38
	 */
	public static Boolean sendMail(String receivUser,String mailTitle, String mailContent){
		
		PropertiesUtils.APP.getProperty("");
		//端口
		String port = PropertiesUtils.APP.getProperty("app.email.port");
		// 设置服务器
		String host = PropertiesUtils.APP.getProperty("app.email.host");
		// 同时通过认证
		String auth = PropertiesUtils.APP.getProperty("app.email.auth");
		// 发送协议
		String protocol = PropertiesUtils.APP.getProperty("app.email.protocol");
		// 发送用户
		String emailUser = PropertiesUtils.APP.getProperty("app.email.userName");
		// 认证用户
		String agreedUser = PropertiesUtils.APP.getProperty("app.email.agreedUser");
		// 认证密码
		String agreedPwd = PropertiesUtils.APP.getProperty("app.email.agreedPwd");
		// 公司名称
		// String companyName = PropertiesUtils.APP.getProperty("app.email.companyName");
		
		Properties props = new Properties(); // 可以加载一个配置文件
		// 使用smtp：简单邮件传输协议
		if(null==port){
			props.put("mail.smtp.host", host); 
		}
		props.put("mail.smtp.auth", auth);  
		props.put("mail.transport.protocol", protocol); 
		Session session = Session.getInstance(props);// 根据属性新建一个邮件会话
		MimeMessage message = new MimeMessage(session);// 由邮件会话新建一个消息对象
		try {
			
			message.setFrom(new InternetAddress(emailUser));
			
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(receivUser));// 设置收件人,并设置其接收类型为TO
			message.setSubject(mailTitle);// 设置标题
			// 设置信件内容
			message.setText(mailContent); // 发送 纯文本 邮件 todo
			message.setContent(mailContent, "text/html;charset=gbk"); // 发送HTML邮件，内容样式比较丰富
			message.setSentDate(new Date());// 设置发信时间
			message.saveChanges();// 存储邮件信息
			// 发送邮件
			Transport transport = session.getTransport();
			// 第一个参数  认证用户     第二个参数 认证密码
			if(null==port){
				transport.connect(agreedUser, agreedPwd);
			}else{
				transport.connect(host,Integer.valueOf(port), agreedUser,agreedPwd);  
			}
	        transport.sendMessage(message, message.getAllRecipients());// 发送邮件,其中第二个参数是所有已设好的收件人地址
	        transport.close();
		} catch (Exception e) {
			System.out.println("发送邮件报错 ---- 接收人为  ： "+receivUser+  "   邮件标题是 ： "+ mailTitle);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	// 测试方法 
	public static void main(String[] args) throws Exception {
		
		Boolean b = sendMail("your Email@qq.com", "Java Mail 测试邮件",
				"<a>点击确认</a>：<b>邮件内容。。。</b>");
		System.out.println(b);
	}
}