package com.mz.thirdpay.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;



/**
 * 模拟web提交
 */
public class WebClient {
	public static void SendByUrl(HttpServletResponse response,String url,String charset) {
		if(response!=null){
			try {
				//response.setContentType("text/html;charset="+charset+"");
				response.setContentType("text/html;charset=utf-8");
				response.setCharacterEncoding(charset);
				response.sendRedirect(url);
				//response.getWriter().write("<script language=JavaScript>window.open('" + url + "' ,'_blank','');</script>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 
	/**
	 * get方式
	 */
	public static String getWebContentByGet(String urlString, final String charset,  
            int timeout) throws IOException {  
        if (urlString == null || urlString.length() == 0) {  
            return null;  
        }  
        urlString = (urlString.startsWith("http://") || urlString  
                .startsWith("https://")) ? urlString : ("http://" + urlString)  
                .intern();  
        URL url = new URL(urlString);  
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
        conn.setRequestMethod("GET");  
        // 增加报头，模拟浏览器，防止屏蔽  
        conn.setRequestProperty(  
                        "User-Agent",  
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");  
        // 只接受text/html类型，当然也可以接受图片,pdf,*/*任意，就是tomcat/conf/web里面定义那些  
        conn.setRequestProperty("Accept", "*/*");  
        conn.setConnectTimeout(timeout);  
        try {  
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {  
                return null;  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
            return null;  
        }  
        InputStream input = conn.getInputStream();  
        BufferedReader reader = new BufferedReader(new InputStreamReader(input,charset));  
        String line = null;  
        StringBuffer sb = new StringBuffer();  
        while ((line = reader.readLine()) != null) {  
            sb.append(line).append("\r\n");  
        }  
        if (reader != null) {  
            reader.close();  
        }  
        if (conn != null) {  
            conn.disconnect();  
        }  
        return sb.toString();  
  
    }  
  /**
   * post 方式
   */
	public static String getWebContentByPost(String urlString, String data,
			final String charset, int timeout) throws IOException {
		if (urlString == null || urlString.length() == 0) {
			return null;
		}
		urlString = (urlString.startsWith("http://") || urlString
				.startsWith("https://")) ? urlString : ("http://" + urlString)
				.intern();
		URL url = new URL(urlString);
		System.out.println("url=="+url);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 设置是否向connection输出，因为这个是post请求，参数要放在 http正文内，因此需要设为true
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		// Post 请求不能使用缓存
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		// connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset="+charset+"");
		// 增加报头，模拟浏览器，防止屏蔽
		connection.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 8.0; Windows vista)");
		// 只接受text/html类型，当然也可以接受图片,pdf,*/*任意
		connection.setRequestProperty("Accept", "*/*");// text/html
		connection.setConnectTimeout(timeout);
		connection.connect();
		DataOutputStream out = new DataOutputStream(connection
				.getOutputStream());
		// String content = data;//+URLEncoder.encode("中文 ", "utf-8");
		// out.writeBytes(content);
		byte[] content = data.getBytes(charset);// +URLEncoder.encode("中文 ",
		out.write(content);
		out.flush();
		out.close();
		try {
			// 必须写在发送数据的后面
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream(), charset));
		String line;
		StringBuffer sb = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\r\n");
		}
		if (reader != null) {
			reader.close();
		}
		if (connection != null) {
			connection.disconnect();
		}
		return sb.toString();
	}
	
	/**
	 * HttpClient    模拟post提交 并完成重定向
	 * @param response
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String sendUrl(HttpServletResponse response,String url,Map<String, String> params ,String charset){
		try{
			HttpClient client =new HttpClient();
			String responseString="";
			PostMethod xmlpost;
			int statusCode = 0;
			//client.setConnectionTimeout(new Integer(1200).intValue());
	 	    xmlpost = new PostMethod(url);
	 	    client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
	 	    List<NameValuePair> list = new ArrayList<NameValuePair>(); 
	 	    Iterator<Entry<String, String>> iter = params.entrySet().iterator(); 
	 	    while (iter.hasNext()) {
	 	    	 Map.Entry<String,String> entry = (Map.Entry<String,String>) iter.next(); 
	 	    	 System.out.println("entry.getKey().toString()==="+entry.getKey().toString()+"    entry.getValue().toString()==="+entry.getValue().toString());
	 		     NameValuePair nvp=new NameValuePair(entry.getKey().toString(),entry.getValue().toString());
	 		     list.add(nvp);
	 	    }
	 	   NameValuePair[] nvps=new NameValuePair[list.size()];
	 	   xmlpost.setRequestBody(list.toArray(nvps)); 
	 	   statusCode = client.executeMethod(xmlpost);
	 	 int statusCodes = xmlpost.getStatusCode();
	 	  System.out.println("statusCode=="+statusCodes);	
	 	 System.out.println("responseString:"+ responseString); 
	       if(statusCode<HttpURLConnection.HTTP_OK || statusCode>=HttpURLConnection.HTTP_MULT_CHOICE){
	           System.err.println("失败返回码[" + statusCode + "]");
	           throw new Exception("请求接口失败，失败码[ "+ statusCode +" ]");
	       }else if(statusCode== 302){
	    	   String tmpString = xmlpost.getResponseHeader("location").getValue();
	    	   GetMethod redirect = new GetMethod(tmpString);
               client.executeMethod(redirect);
                System.out.println("Redirect:"+ redirect.getStatusLine().toString()); 
                redirect.releaseConnection();
	       }
		}catch(Exception e){
			e.printStackTrace();
		}
		
 	   
		return null;
	}
	
	
	/**
	 * 准备中间页面所需参数
	 * add by linyan  2014-9-22
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String[] operateParameter(String url,Map<String, String> params,String charset){
		String[] ret=new String[2];
		if(url!=null&&!"".equals(url)){
			if(charset!=null&&!"".equals(charset)){
				StringBuffer sb=new StringBuffer();
				String parameterUtil=getParams(params,charset);
				sb.append("<html>");
				sb.append("<head>");
				sb.append("<script type=\"text/javascript\">");
				sb.append("function redirectUrl() {");
				sb.append("document.form0.submit();");
				sb.append("}");
				sb.append("</script>");
				sb.append("</head>");
				sb.append("<body onload=\"redirectUrl()\">");
				sb.append("<form name=\"form0\" action=\""+url+"\" method=\"post\">");
				if(parameterUtil!=null){
					sb.append(parameterUtil);
				}
				sb.append("</form>");
				sb.append("</body>");
				sb.append("</html>");
				ret[0]="SUCCESS";
				ret[1]=sb.toString();
				System.out.println(sb.toString());
		//		reponseWrite(sb.toString(),charset);
			}else{
				ret[0]="FAILD";
				ret[1]="form表单编码方式不存在";
			}
		}else{
			ret[0]="FALID";
			ret[1]="第三方url不存在";
		}
		return ret;
	}
	/**
	 * 获取中间页面的form表单参数
	 * @param params
	 * @return
	 */
	private static String getParams(Map<String, String> params,String charset) {
		// TODO Auto-generated method stub
		String htmlParamss=null;
		try{
			if(params!=null){
				StringBuffer sb=new StringBuffer();
				Iterator<Entry<String, String>> iter = params.entrySet().iterator(); 
				while (iter.hasNext()) { 
				    Map.Entry<String,String> entry = (Map.Entry<String,String>) iter.next(); 
				    Object key = entry.getKey(); 
				    Object val = entry.getValue(); 
				    sb.append("<input type=\"hidden\" name='"+key.toString()+"\' value='"+val.toString()+"' />");
				} 
				htmlParamss=sb.toString();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return htmlParamss;
	}
 
	public static void reponseWrite(String htmlParamss,String charset) {
	/*	try {
			HttpServletRequest request =((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			ServletWebRequest servletWebRequest=new ServletWebRequest(request);
			HttpServletResponse response=servletWebRequest.getResponse();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=utf-8");
			PrintWriter pw;
			pw = response.getWriter();
			pw.write(htmlParamss) ;
			pw.flush() ;
			pw.close() ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	} 
}


