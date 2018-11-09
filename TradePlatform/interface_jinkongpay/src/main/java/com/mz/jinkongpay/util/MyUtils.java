package com.mz.jinkongpay.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

public class MyUtils {
      
	/*** 
     * MD5加密 生成32位md5码 （线程安全）
     * @param 待加密字符串
     * @return 返回32位md5码
     */
    public static String md5Encode(String inStr,String unicode) throws Exception {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes(unicode);
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
    
    /**
     * 根据传来的时间格式获取相同格式的字符串
     * @param format
     * @return
     */
    public static String getTime(String format){
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	return sdf.format(new Date());
    }
    
    
    
    
    public static String getOrderID(){
    	String format="yyyyMMddHHmmss";
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	String time= sdf.format(new Date());
    	int num = (int)(Math.random()*100);
    	return time+String.valueOf(num);
    }
    
    /**
     * 将map转成string
     * <p> TODO</p>
     * @author:         Zhang Lei
     * @param:    @param sortedMap
     * @param:    @return
     * @return: String 
     * @Date :          2017年2月3日 下午3:29:32
     * @throws:
     */
    public static String mapToStringAndTrim(SortedMap<String, String> sortedMap){
    	 StringBuffer sb = new StringBuffer();
    	  Iterator it =	sortedMap.entrySet().iterator();
    	
           while (it.hasNext()) {
               Map.Entry entry = (Map.Entry)it.next();
               String key = entry.getKey().toString().trim();
               if(entry.getValue()==null){
            	   continue;
               }
               String value = entry.getValue().toString().trim();
               if (!"".equals(value) && value!=null) {
            	   sb.append(key+"="+value+"&");
               }
           }
    	return sb.substring(0,sb.length()-1);
    }
    
    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param,String unicode) {
        OutputStreamWriter out = null;
        BufferedReader read = null;
        String result = "";
        try {
        	
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection  conn = (HttpURLConnection )realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)"); 
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", unicode);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(param.length()));
            
            
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(),unicode);
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            read = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),unicode));
            String line;
            while ((line = read.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(read!=null){
                	read.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    
}
