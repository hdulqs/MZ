/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年12月28日 下午7:10:15
 */
package com.mz.util.sys;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年12月28日 下午7:10:15 
 */
public class CookiesUtils {
	
	/**
	 * 获取cookie值
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param request
	 * @param:    @param name
	 * @param:    @return
	 * @return: String 
	 * @Date :          2015年12月28日 下午7:15:19   
	 * @throws:
	 */
	public static  String getValue(HttpServletRequest request,String name){
		Cookie[] cookies = request.getCookies();
		try {
			for(Cookie cookie : cookies){
				if(name.equals(cookie.getName())){
					return cookie.getValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
}
