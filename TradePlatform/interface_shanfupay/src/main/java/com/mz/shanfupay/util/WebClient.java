package com.mz.shanfupay.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
				System.out.println("向闪付请求的html:" + sb.toString());
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
		return URLDecoder.decode(sb.toString(), "utf-8");
	}
	/**
	 * 将map对象参数转换成String=String&方式
	 * @param params
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String generateParams(Map<String, String> params,String charset) throws UnsupportedEncodingException{
		int flag=0;
		StringBuffer ret=new StringBuffer();
		Iterator iter = params.entrySet().iterator(); 
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
}
