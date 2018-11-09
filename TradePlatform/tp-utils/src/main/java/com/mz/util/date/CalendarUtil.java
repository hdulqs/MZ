/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2015年10月9日 下午12:04:20
 */
package com.mz.util.date;

import java.util.Calendar;

public class CalendarUtil {
	
	public static  Calendar cal = Calendar.getInstance();
	
	public static int getDay()
	{
		return cal.get(Calendar.DATE);
	}
	public static  int getMonth()
	{
		return cal.get(Calendar.MONTH)+1;	
	}
	
	public static int getYear()
	{
		return cal.get(Calendar.YEAR);
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(1/0);
		} catch (Exception e) {
			System.out.println(111);
		}finally{
			System.out.println(222);
		}
		
	}
	

}
