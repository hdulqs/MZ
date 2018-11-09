package com.fh.util;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

public class WanDianService {
	
	static String url = "http://万店通联.中国/mobile/index.php";
 
	public static void doAction(String username,BigDecimal Money,String requestId){
		
//		try {
//			String content = "act=nncai&op=register_sync&data=";
//			String str = "act=nncai&op=register_sync&type=bank&Username="+username+"&Money="+Money.toString()+"&code=" + requestId;
//			
//			//System.out.println("加密明文：" + str);
//			String enStr = DES.encrypt(str);
//			//System.out.println("加密密文：" + enStr);
//			
//			//System.out.print("url：");
//			//System.out.println(url + "?" + content + enStr);
//			
//			
//			//String result = "";
//			String result = HttpRequestUtil.sendGet(url, content + enStr);
//			
//			System.out.println("WanDianService 返回：" + result);
//			
//			 
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
		
		
	}
	
	public static void main(String[] args) {
		
		doAction("18973898598",new BigDecimal("80000"),System.currentTimeMillis()+"");
		//String s = "{\"code\":\"21\",\"msg\":\"\u91cd\u590d\u63d0\u4ea4\"}";
		//System.out.println(s);
		//System.out.println(s.substring(s.indexOf("{")));
		
		
	}
 
 
}
