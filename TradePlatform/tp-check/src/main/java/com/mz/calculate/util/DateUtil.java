/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Wu Shuiming
 * @version:   V1.0 
 * @Date:      2016年9月5日 下午3:22:58
 */
package com.mz.calculate.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Wu shuiming
 * @date 2016年9月5日 下午3:22:58
 */
public class DateUtil {

	/**
	 * 
	 * 将String类型 转成 java.Sql 类型 
	 * 
	 * 
	 * @author:    Wu Shuiming
	 * @version:   V1.0 
	 * @date:      2016年9月5日 下午3:30:06
	 */
	public static Date getDateToString(String date){
		
		SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date sqlDate = new Date(sim.parse(date).getTime());
			return sqlDate;
		} catch (ParseException e) {
			e.printStackTrace();
			Date sqlDate2 = new Date(new java.util.Date().getTime());
			return sqlDate2;
		}
		
	}
	
	/**
	 * 将当前时间转成字符串类型   
	 * 
	 * @return
	 */
	public static String getNowDate(){
		SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
		String newDate = sim.format(new java.util.Date().getTime());
		return newDate ;
		
	}
	
	/**
	 * 时间类型的字符串转换成date 
	 * 
	 * @param date
	 * @return
	 */
	public static java.util.Date StringToDate(String date){
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date date2 = sim.parse(date);
			return date2;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 时间格式的字符你串转换成时间类型的字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String StringDateToString(String date){
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date date2 = sim.parse(date);
			String format = sim.format(date2);
			return format;
		} catch (ParseException e) {
			e.printStackTrace();
			return getNowDate();
		}
	}
	
	
	
	public static void main(String[] args) {
//		SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			java.util.Date date = sim.parse("2016-09-05 21:21:21");
//			System.out.println(date);
//		} catch (ParseException e) {
//			System.out.println("出错");
//			e.printStackTrace();
//		}
		
		String date = "2016-9-9";
		java.util.Date date2 = StringToDate(date);
		System.out.println(date2);
		SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
		String format = sim.format(date2);
		System.out.println(format);
		
		
	}
	
}
