package com.fh.util;

import org.springframework.context.ApplicationContext;
/**
 * 项目名称：
 * @author:fh
 * 
*/
public class Const {
	public static final String SESSION_SECURITY_CODE = "sessionSecCode";
	public static final String SESSION_USER = "sessionUser";
	public static final String SESSION_ROLE_RIGHTS = "sessionRoleRights";
	public static final String SESSION_menuList = "menuList";			//当前菜单
	public static final String SESSION_allmenuList = "allmenuList";		//全部菜单
	public static final String SESSION_QX = "QX";
	public static final String SESSION_userpds = "userpds";			
	public static final String SESSION_USERROL = "USERROL";				//用户对象
	public static final String SESSION_USERNAME = "USERNAME";			//用户名
	public static final String TRUE = "T";
	public static final String FALSE = "F";
	public static final String LOGIN = "/login_toLogin.do";				//登录地址
	public static final String SYSNAME = "admin/config/SYSNAME.txt";	//系统名称路径
	public static final String PAGE	= "admin/config/PAGE.txt";			//分页条数配置路径
	public static final String EMAIL = "admin/config/EMAIL.txt";		//邮箱服务器配置路径
	public static final String SMS1 = "admin/config/SMS1.txt";			//短信账户配置路径1
	public static final String SMS2 = "admin/config/SMS2.txt";			//短信账户配置路径2
	public static final String FWATERM = "admin/config/FWATERM.txt";	//文字水印配置路径
	public static final String IWATERM = "admin/config/IWATERM.txt";	//图片水印配置路径
	public static final String WEIXIN	= "admin/config/WEIXIN.txt";	//微信配置路径
	public static final String WEBSOCKET = "admin/config/WEBSOCKET.txt";//WEBSOCKET配置路径
	public static final String FILEPATHIMG = "uploadFiles/uploadImgs/";	//图片上传路径
	public static final String FILEPATHFILE = "uploadFiles/file/";		//文件上传路径
	public static final String FILESVERSION = "uploadFiles/version/";		//APK版本
	public static final String FILESCICON = "uploadFiles/scicon/";		//商城分类图标
	public static final String FILEPATHTWODIMENSIONCODE = "uploadFiles/twoDimensionCode/"; //二维码存放路径
	public static final String NO_INTERCEPTOR_PATH = ".*/((login)|(logout)|(code)|(app)|(weixin)|(static)|(main)|(websocket)).*";	//不对匹配该值的访问路径拦截（正则）
	public static ApplicationContext WEB_APP_CONTEXT = null; //该值会在web容器启动时由WebAppContextListener初始化
	public static final String YMIOSAPPKEY = "5adee59d8f4a9d70f000008f";						//友盟IOS:KEY
	public static final String YMIOSAPPMASTERSECRET = "2m6qqp2flvfrfnxwj5eyh2fbqxou8g8e";		//友盟IOS:App Master Secret
	public static final String YMANDROIDAPPKEY = "5aa372f2f29d98334300051d";					//友盟ANDROID:KEY
	public static final String YMANDROIDAPPMASTERSECRET = "2u36ir6qicfr0dai3lswedfy136zzffx";	//友盟ANDROID:App Master Secret
	
	
	
	public static boolean VERIFICATIONCODE = true;															//登录验证码（本地为true、服务器false）
//	public static String VERIFYLICAIORDER = "http://www.hilison.com/nntb/verifyLiCaiOrder.htm";				//理财订单接口正式地址
//	public static String VERIFYCHONGZHIORDER = "http://www.hilison.com/nntb/verifyChongZhiOrder.htm";		//充值金币审核接口正式地址
	public static boolean ISXJN = false;		//小金牛系统OR牛牛通宝系统
	public static String VERIFYLICAIORDER = "http://120.78.57.242:80/nntb/verifyLiCaiOrder.htm";				//理财订单接口测试地址
	public static String VERIFYCHONGZHIORDER = "http://120.78.57.242:80/nntb/verifyChongZhiOrder.htm";		//充值金币审核接口测试地址

	/**
	 * APP Constants
	 */
	//app注册接口_请求协议参数)
	public static final String[] APP_REGISTERED_PARAM_ARRAY = new String[]{"countries","uname","passwd","title","full_name","company_name","countries_code","area_code","telephone","mobile"};
	public static final String[] APP_REGISTERED_VALUE_ARRAY = new String[]{"国籍","邮箱帐号","密码","称谓","名称","公司名称","国家编号","区号","电话","手机号"};
	
	//app根据用户名获取会员信息接口_请求协议中的参数
	public static final String[] APP_GETAPPUSER_PARAM_ARRAY = new String[]{"USERNAME"};
	public static final String[] APP_GETAPPUSER_VALUE_ARRAY = new String[]{"用户名"};
	

	

	
}
