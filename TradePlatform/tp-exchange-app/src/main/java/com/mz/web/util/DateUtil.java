package com.mz.web.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String getNewDate() {

		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		return time;
		
	}
	/**
	 * 判断日期是否周末  周六：1、周日：2、不是周末：0
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param date
	 * @param:    @return
	 * @return: Integer 
	 * @Date :          2016年12月10日 下午12:52:58   
	 * @throws:
	 */
	public static Integer isWeekend(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
			return Integer.valueOf(1);
		}else if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
			return Integer.valueOf(2);
		}
		return Integer.valueOf(0);
	}
}
