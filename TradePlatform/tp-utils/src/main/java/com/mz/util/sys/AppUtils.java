/**
 * 
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2015年10月9日 下午12:04:20
 */
package com.mz.util.sys;
 
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * <p>此类不供业务系统系统调用， 初始化应用程序全局对象servletContext</p>
 * @author:         Gao Mimi 
 * @Date :          2015年10月10日 下午2:05:44
 */

 
public class AppUtils {
	
	private static Log logger=LogFactory.getLog(AppUtils.class);
	
	/**
	 * 应用程序全局对象
	 */
	private static ServletContext servletContext=null;
	
	/**
	 * 取得应用程序的绝对路径
	 * @return
	 */
	public static String getAppAbsolutePath(){
		return servletContext.getRealPath("/");
	}

	/**
	 * 应用程序启动时调用
	 * @param servletContext
	 */
	 public static void init(ServletContext in_servletContext){
	    	servletContext=in_servletContext;
	  }
	 
}
