package com.mz.exchange.websocketclient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Test {

	public static void main(String[] args) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Calendar c1=Calendar.getInstance();  
        Calendar c2=Calendar.getInstance();  
        try {  
            c1.setTime(new Date());  
            c2.setTime(formatter.parse("2016-11-22 00:00:00"));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        int result=c1.compareTo(c2);  
        System.out.println(result);
	}

}
