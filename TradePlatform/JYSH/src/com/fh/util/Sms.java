package com.fh.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.fh.util.Const;
import com.fh.util.Tools;

public class Sms {
	
	static String key ="71c7e5b2d010de9ddd99046613197f57";
	static String url = "http://v.juhe.cn/sms/send";

	public void send(String mobile) {
			
		String PostData = "";
		try {
			PostData = "key=" + key + "&tpl_id=32694" +  "&mobile=" + mobile + "&tpl_value=" + URLEncoder.encode("#namee#=代理", "utf-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("短信提交失败");
		}
		// System.out.println(PostData);
		String ret = SMS(PostData, url);
		System.out.println(ret);
		

	}
	
	//管理员登录验证
	public void sends(String mobile,String code) {
		
		String PostData = "";
		try {
			PostData = "key=" + key + "&tpl_id=66827" +  "&mobile=" + mobile + "&tpl_value=" + URLEncoder.encode("#code#="+code,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("短信提交失败");
		}
		 //System.out.println(code);
		String ret = SMS(PostData, url);
		//System.out.println(ret);
		

	}
	
	
	//小金牛--充值审核验证
	public void xjnchonzhi_send(String mobile,String code,String str) {
		
		String PostData = "";
		try {
			PostData = "key=" + key + "&tpl_id=46073" +  "&mobile=" + mobile + "&tpl_value=" + URLEncoder.encode("#P#="+code+"&#M#="+str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("短信提交失败");
		}
		 //System.out.println(code);
		String ret = SMS(PostData, url);
		//System.out.println(ret);
		

	}
	
	
	
	
	
	
	//新增投资交易验证
	public void tzsend(String mobile,String code) {
		
		String PostData = "";
		try {
			PostData = "key=" + key + "&tpl_id=31816" +  "&mobile=" + mobile + "&tpl_value=" + URLEncoder.encode("#P#="+code+"&#M#=新增投资交易","UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("短信提交失败");
		}
		 //System.out.println(code);
		String ret = SMS(PostData, url);
		//System.out.println(ret);
		

	}
	
	
	public static String SMS(String postData, String postUrl) {
		try {
			// 发送POST请求
			URL url = new URL(postUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Length", "" + postData.length());
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			out.write(postData);
			out.flush();
			out.close();
			// 获取响应状态
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out.println("connect failed!");
				return "";
			}
			// 获取响应内容体
			String line, result = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
			in.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return "";
	}

}
