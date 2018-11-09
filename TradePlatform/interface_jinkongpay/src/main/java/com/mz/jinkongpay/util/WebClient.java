package com.mz.jinkongpay.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

/**
 * 模拟web提交
 */
@SuppressWarnings("all")
public class WebClient {
	/**
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String[] operateParameter(HttpServletResponse response,String url, Map<String, String> params, 
							String charset)throws IOException {
		String[] ret = new String[2];
		if (url != null && !"".equals(url)) {
			if (charset != null && !"".equals(charset)) {
				StringBuffer sb = new StringBuffer();
				String parameterUtil = getParams(params, charset);
				sb.append("<html>");
				sb.append("<head>");
				sb.append("<script type=\"text/javascript\">");
				sb.append("function redirectUrl() {");
				sb.append("document.form0.submit();");
				sb.append("}");
				sb.append("</script>");
				sb.append("</head>");
				sb.append("<body onload=\"redirectUrl()\">");
				sb.append("<form name=\"form0\" action=\"" + url
						+ "\" method=\"post\">");
				if (parameterUtil != null) {
					sb.append(parameterUtil);
				}
				sb.append("</form>");
				sb.append("</body>");
				sb.append("</html>");
				ret[0] = "SUCCESS";
				ret[1] = sb.toString();
				System.out.println("向现代金控请求的html:" + sb.toString());
				response.setContentType("text/html; charset=utf-8");
				
				OutputStream outputStream = response.getOutputStream();//获取OutputStream输出流
				byte[] dataByteArr = sb.toString().getBytes("UTF-8");//将字符转换成字节数组，指定以UTF-8编码进行转换
				outputStream.write(dataByteArr);//使用OutputStream流向客户端输出字节数组
				
				outputStream.flush();
				outputStream.close();
				
				//使用以下输出流时会抛异常：getWriter() has already been called for this response
//				PrintWriter pw = response.getWriter();
//				pw.write(sb.toString());
//				pw.flush();
//				pw.close();
				
			} else {
				ret[0] = "FAILD";
				ret[1] = "form表单编码方式不存在";
			}
		} else {
			ret[0] = "FALID";
			ret[1] = "第三方url不存在";
		}
		return ret;
	}

	/**
	 * 获取中间页面的form表单参数
	 * 
	 * @param params
	 * @return
	 */
	private static String getParams(Map<String, String> params, String charset) {
		String htmlParamss = null;
		try {
			if (params != null) {
				StringBuffer sb = new StringBuffer();
				Iterator iter = params.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					Object key = entry.getKey();
					Object val = entry.getValue();
					sb.append("<input type=\"hidden\" name='" + key.toString()
							+ "\' value='" + val.toString() + "' />");
				}
				htmlParamss = sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return htmlParamss;
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
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 设置是否向connection输出，因为这个是post请求，参数要放在 http正文内，因此需要设为true
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		// Post 请求不能使用缓存
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=" + charset + "");
		// 增加报头，模拟浏览器，防止屏蔽
		connection.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 8.0; Windows vista)");
		// 只接受text/html类型，当然也可以接受图片,pdf,*/*任意
		connection.setRequestProperty("Accept", "*/*");// text/html
		connection.setConnectTimeout(timeout);
		connection.connect();
		DataOutputStream out = new DataOutputStream(
				connection.getOutputStream());
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
				connection.getInputStream(), "gbk"));
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
	 * 将map对象参数转换成String=String&方式
	 * @param sortedMap
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String generateParams(SortedMap<String, String> sortedMap,String charset) throws UnsupportedEncodingException{
		int flag=0;
		StringBuffer ret=new StringBuffer();
		Iterator iter = sortedMap.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    Object key = entry.getKey(); 
		    Object val = entry.getValue(); 
		    if(val!=null){
		    if(flag==0){
		    	ret.append(key);
		    	ret.append("=");
		    	if(charset!=null&&!charset.equals("")){
		    	ret.append(URLEncoder.encode(val.toString(), charset));
		    	}else{
		    		ret.append(val.toString());
		    	}
		    	flag++;
		    }else{
		    	ret.append("&");
		    	ret.append(key);
		    	ret.append("=");
		    	if(charset!=null&&!charset.equals("")){
			    	ret.append(URLEncoder.encode(val.toString(), charset));
			    	}else{
			    		ret.append(val.toString());
			    	}
		    }
		    }
		} 
		return ret.toString();
	}
	
	
	/**
	 * 微信支付提交方式
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param map
	 * @param:    @param reqUrl
	 * @param:    @return
	 * @param:    @throws UnsupportedEncodingException
	 * @return: String 
	 * @Date :          2017年2月9日 上午9:52:16   
	 * @throws:
	 */
	public static Map<String,String> httpPost(SortedMap<String, String> map,String reqUrl) throws UnsupportedEncodingException{
		if(reqUrl.startsWith("https")){
			try {
				ignoreSsl();
			} catch (Exception e) {
				System.out.println("Java 信任所有SSL证书(解决PKIX path building failed问题)异常！！！");
				e.printStackTrace();
			}
		}
		HttpPost httpPost = new HttpPost(reqUrl);
		CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        String res = "";
        StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
        System.out.println("微信提交entityParams:" + XmlUtils.parseXML(map));
        httpPost.setEntity(entityParams);
        
        entityParams.setContentType("application/json");

        httpPost.setHeader("Content-Type", "text/xml;charset=utf-8");
        client = HttpClients.createDefault();
        try {
			response = client.execute(httpPost);
		} catch (Exception e1) {
			System.out.println("微信支付报文发送异常！！！！！！！！");
			e1.printStackTrace();
		}
        
        if(response != null && response.getEntity() != null){
        	
        	HttpEntity entity = response.getEntity();  
            System.out.println("----------------------------------------");  
            System.out.println(response.getStatusLine());  
            String payResult = null;
            System.out.println(12);
            try {
	            if (entity != null) {  
	                System.out.println("Response content length: " + entity.getContentLength());  
					payResult = EntityUtils.toString(entity);
	                System.out.println(payResult); 
	                System.out.println(1);
	            }  
	            System.out.println(2);
	        	Map<String,String> resultMap = XmlUtils.toMap(payResult.getBytes(), "utf-8");
	        	System.out.println(3);
	            System.out.println("请求结果：" + resultMap.size());
	            return resultMap;
            } catch (Exception e) {
				e.printStackTrace();
			} 
        }
        return null;
	}
	
	
	
	/**
	 * 支付宝支付提交方式
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param map
	 * @param:    @param reqUrl
	 * @param:    @return
	 * @param:    @throws UnsupportedEncodingException
	 * @return: String 
	 * @Date :          2017年2月9日 上午9:52:16   
	 * @throws:
	 */
	public static Map<String,String> httpPost1(SortedMap<String, String> map,String reqUrl) throws UnsupportedEncodingException{
		if(reqUrl.startsWith("https")){
			try {
				ignoreSsl();
			} catch (Exception e) {
				System.out.println("Java 信任所有SSL证书(解决PKIX path building failed问题)异常！！！");
				e.printStackTrace();
			}
		}
		HttpPost httpPost = new HttpPost(reqUrl);
		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;
		String res = "";
		StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
		System.out.println("支付宝提交entityParams:" + XmlUtils.parseXML(map));
		httpPost.setEntity(entityParams);
		
		entityParams.setContentType("application/json");
		
		httpPost.setHeader("Content-Type", "text/xml;charset=utf-8");
		client = HttpClients.createDefault();
		try {
			response = client.execute(httpPost);
		} catch (Exception e1) {
			System.out.println("支付宝支付报文发送异常！！！！！！！！");
			e1.printStackTrace();
		}
		
		if(response != null && response.getEntity() != null){
			
			HttpEntity entity = response.getEntity();  
			System.out.println("----------------------------------------");  
			System.out.println(response.getStatusLine());  
			String payResult = null;
			System.out.println(12);
			try {
				if (entity != null) {  
					System.out.println("Response content length: " + entity.getContentLength());  
					payResult = EntityUtils.toString(entity);
					System.out.println(payResult); 
					System.out.println(1);
				}  
				System.out.println(2);
				Map<String,String> resultMap = (Map)JSON.parse(payResult);  
				System.out.println("请求结果：" + resultMap.size());
				return resultMap;
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return null;
	}

	
	
	/**
	 * 将中间页面写成文件
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param truePath
	 * @param:    @param rechargeurl_wangyin
	 * @param:    @param sortedMap
	 * @param:    @param string
	 * @return: void 
	 * @Date :          2017年2月14日 下午4:59:15   
	 * @throws:
	 */
	public static void writePage(String truePath, String rechargeurl_wangyin,
			SortedMap<String, String> params, String charset) {
		StringBuffer sb = new StringBuffer();
		String parameterUtil = getParams(params, charset);
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<script type=\"text/javascript\">");
		sb.append("function redirectUrl() {");
		sb.append("document.form0.submit();");
		sb.append("}");
		sb.append("</script>");
		sb.append("</head>");
		sb.append("<body onload=\"redirectUrl()\">");
		sb.append("<form name=\"form0\" action=\"" + rechargeurl_wangyin
				+ "\" method=\"post\">");
		if (parameterUtil != null) {
			sb.append(parameterUtil);
		}
		sb.append("</form>");
		sb.append("</body>");
		sb.append("</html>");
		
		File file=new File(truePath);
		try {
			 FileWriter fw = new FileWriter(file.getAbsoluteFile());
			   BufferedWriter bw = new BufferedWriter(fw);
			   bw.write(sb.toString());
			   bw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 忽略HTTPS请求的SSL证书，必须在openConnection之前调用
	 * @throws Exception
	 */
	public static void ignoreSsl() throws Exception{
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
				return true;
			}
		};
		trustAllHttpsCertificates();
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}
	
	private static void trustAllHttpsCertificates() throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[1];
		TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}
	static class miTM implements TrustManager,X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
		public boolean isServerTrusted(X509Certificate[] certs) {
			return true;
		}
		public boolean isClientTrusted(X509Certificate[] certs) {
			return true;
		}
		public void checkServerTrusted(X509Certificate[] certs, String authType)
				throws CertificateException {
			return;
		}
		public void checkClientTrusted(X509Certificate[] certs, String authType)
				throws CertificateException {
			return;
		}
	}
}
