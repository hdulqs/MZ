package com.fh.util.constant;
/**
 * Http的Post和Get请求方式 
 * @author Administrator
 *
 */
import java.io.BufferedReader;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStreamWriter;  
import java.net.HttpURLConnection;  
import java.net.URL;  
import java.util.Map; 
public class HttpRequest {
	 /** 
     * http get请求方式 
     * @param urlStr 
     * @param params 
     * 
    **/  
    public static String get(String urlStr,Map<String,String> params){  
        InputStream is = null;  
        BufferedReader reader = null;  
        String resultStr ="";  
        try {  
            String paramsStr = "";  
            for(String param : params.keySet()){  
                paramsStr += "&" + param + "=" + params.get(param);  
            }  
            if(!paramsStr.isEmpty()){  
                paramsStr = paramsStr.substring(1);  
                urlStr += "?" + paramsStr;  
            }  
            URL url = new URL(urlStr);  
            HttpURLConnection httpCon = (HttpURLConnection) url  
                    .openConnection();  
            httpCon.setRequestMethod("GET");  
            is = httpCon.getInputStream();  
   
            reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));  
            StringBuilder sb = new StringBuilder();  
            String line = null;  
            while ((line = reader.readLine()) != null) {     
                sb.append(line);     
            }  
            resultStr = sb.toString();  
           
        } catch (Exception e) {  
            e.printStackTrace();  
               
        } finally {  
            try {  
                if(reader!=null)reader.close();  
                if(is!=null)is.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return resultStr;  
    }  
      
    /** 
     * http post请求方式 
     * @param urlStr 
     * @param params 
     * 
    **/  
    public static String post(String urlStr,Map<String,String> params){  
         URL connect;  
         StringBuffer data = new StringBuffer();    
         try {    
             connect = new URL(urlStr);    
             HttpURLConnection connection = (HttpURLConnection)connect.openConnection();    
             connection.setRequestMethod("POST");    
             connection.setDoOutput(true);   
             connection.setDoInput(true);  
             connection.setRequestProperty("accept", "*/*");  
             connection.setRequestProperty("connection", "Keep-Alive");  
             connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)"); 
             //conn.setRequestProperty("Cookie", cookiesAll.toString()); 设置cookie  若需要登录操作  
             OutputStreamWriter paramout = new OutputStreamWriter(    
                     connection.getOutputStream(),"UTF-8");   
            String paramsStr = "";  
            for(String param : params.keySet()){  
                paramsStr += "&" + param + "=" + params.get(param);  
            }  
            if(!paramsStr.isEmpty()){  
                paramsStr = paramsStr.substring(1);  
            }  
             paramout.write(paramsStr);    
             paramout.flush();    
             BufferedReader reader = new BufferedReader(new InputStreamReader(    
                     connection.getInputStream(), "UTF-8"));    
             String line;                
             while ((line = reader.readLine()) != null) {            
                 data.append(line);              
             }    
             paramout.close();    
             reader.close();    
         } catch (Exception e) {    
             e.printStackTrace();    
         }    
        return data.toString();  
    }  
}
