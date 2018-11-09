/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月6日 上午11:49:50
 */
package com.mz.gopay.utils;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年7月6日 上午11:49:50 
 */
public class HttpClientUtils {
	
	/**
	 * HttpClientUtils.getInstance().httpPost(
				"http://localhost:8080/sshmysql/register", params);

		// GET 同步方法
		HttpClientUtils.getInstance().httpGet(
				"http://wthrcdn.etouch.cn/weather_mini?city=北京");
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	
	
	
	/**
	 * get请求
	 * 
	 * @param url
	 * @return
	 */
	public String httpGet(String url) {
		return httpGet(url, null);
	}
	
	
	
	
	public  static String httpPost(String url, Map<String, String> paramsMap) {
		return   httpPost(url, paramsMap, null);
	}
	/**
	 * http get请求
	 * 
	 * @param url
	 * @return
	 */
	public    String httpGet(String url, Map<String, String> headMap) {
		String responseContent = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			setGetHead(httpGet, headMap);
			try {
				System.out.println(response1.getStatusLine());
				HttpEntity entity = response1.getEntity();
				InputStream is = entity.getContent();
				StringBuffer strBuf = new StringBuffer();
				byte[] buffer = new byte[4096];
				int r = 0;
				while ((r = is.read(buffer)) > 0) {
					strBuf.append(new String(buffer, 0, r, "UTF-8"));
				}
				responseContent = strBuf.toString();
				System.out.println("debug:" + responseContent);
				EntityUtils.consume(entity);
			} finally {
				response1.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}

	
	
	/**
	 * http的post请求
	 * 
	 * @param url
	 * @param paramsMap
	 * @return
	 */
	public static String httpPost(String url, Map<String, String> paramsMap,
			Map<String, String> headMap) {
		String responseContent = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);
			setPostHead(httpPost, headMap);
			setPostParams(httpPost, paramsMap);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				System.out.println(response.getStatusLine());
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				StringBuffer strBuf = new StringBuffer();
				byte[] buffer = new byte[4096];
				int r = 0;
				while ((r = is.read(buffer)) > 0) {
					strBuf.append(new String(buffer, 0, r, "UTF-8"));
				}
				responseContent = strBuf.toString();
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("responseContent = " + responseContent);
		return responseContent;
	}

	
	/**
	 * 设置POST的参数
	 * 
	 * @param httpPost
	 * @param paramsMap
	 * @throws Exception
	 */
	private static   void setPostParams(HttpPost httpPost, Map<String, String> paramsMap)
			throws Exception {
		if (paramsMap != null && paramsMap.size() > 0) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Set<String> keySet = paramsMap.keySet();
			for (String key : keySet) {
				nvps.add(new BasicNameValuePair(key, paramsMap.get(key)));
			}
			System.err.println("param----"+nvps);
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		}
	}

	/**
	 * 设置http的HEAD
	 * 
	 * @param httpPost
	 * @param headMap
	 */
	private  static  void setPostHead(HttpPost httpPost, Map<String, String> headMap) {
		if (headMap != null && headMap.size() > 0) {
			Set<String> keySet = headMap.keySet();
			for (String key : keySet) {
				httpPost.addHeader(key, headMap.get(key));
			}
		}
	}

	/**
	 * 设置http的HEAD
	 * 
	 * @param httpGet
	 * @param headMap
	 */
	private static  void setGetHead(HttpGet httpGet, Map<String, String> headMap) {
		if (headMap != null && headMap.size() > 0) {
			Set<String> keySet = headMap.keySet();
			for (String key : keySet) {
				httpGet.addHeader(key, headMap.get(key));
			}
		}
	}
	
	
	
	  /**
     * POST方式发起http请求
     */
   
    public  void requestByPostMethod(){
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpPost post = new HttpPost("https://gatewaymer.gopay.com.cn/Trans/WebClientAction.do");          //这里用上本机的某个工程做测试
            //创建参数列表
            List<NameValuePair> list = new ArrayList<NameValuePair>();
           
            list.add(new BasicNameValuePair("version", "1.1"));
            list.add(new BasicNameValuePair("customerId", "0000002642"));
            list.add(new BasicNameValuePair("tranDateTime", "20160829181614"));
            list.add(new BasicNameValuePair("recvBankAcctNum", "1212121212121221212"));
            list.add(new BasicNameValuePair("merOrderNum", "01160829181613996190"));
            list.add(new BasicNameValuePair("recvBankName", "招商银行"));
            list.add(new BasicNameValuePair("merchantEncode", "1"));
            list.add(new BasicNameValuePair("recvBankCity", "北京"));
            list.add(new BasicNameValuePair("recvBankProvince", "北京"));
            list.add(new BasicNameValuePair("corpPersonFlag", "2"));
            list.add(new BasicNameValuePair("recvBankAcctName", "吴水明"));
            list.add(new BasicNameValuePair("recvBankBranchName", "北京分行"));
            list.add(new BasicNameValuePair("signValue", "09306e732cd7cc3861f7b1ce3866c251"));
            list.add(new BasicNameValuePair("tranAmt", "9900.00"));
            list.add(new BasicNameValuePair("approve", "1"));
            list.add(new BasicNameValuePair("merURL", "http://localhost/static/pay/thirdpayconfig/withdraw"));
            list.add(new BasicNameValuePair("tranCode", "4025"));
            list.add(new BasicNameValuePair("payAcctId", "0000000002000000293"));
            //url格式编码
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list,"GBK");
            post.setEntity(uefEntity);
            System.out.println("POST 请求...." + post.getURI());
            //执行请求
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            try{
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity){
                    System.out.println("-------------------------------------------------------");
                    System.out.println(EntityUtils.toString(uefEntity));
                    System.out.println("-------------------------------------------------------");
                }
                String responseContent="";
            	InputStream is = entity.getContent();
				StringBuffer strBuf = new StringBuffer();
				byte[] buffer = new byte[4096];
				int r = 0;
				while ((r = is.read(buffer)) > 0) {
					strBuf.append(new String(buffer, 0, r, "GBK"));
				}
				responseContent = strBuf.toString();
				
				System.out.println(responseContent);
            } finally{
                httpResponse.close();
            }
             
        } catch( UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try{
               // closeHttpClient(httpClient);                
            } catch(Exception e){
                e.printStackTrace();
            }
        }
         
    }
     
    
    
    public static String post(String url,Map<String,String> map,String code) {
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpPost post = new HttpPost(url);        
            //创建参数列表
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (String key:map.keySet()) {
				
				 list.add(new BasicNameValuePair(key, map.get(key)));
			}
            
            //url格式编码
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list,code);
            post.setEntity(uefEntity);
           
            //执行请求
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            try{
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity){
             /*       System.out.println("-------------------------------------------------------");
                    System.out.println(EntityUtils.toString(uefEntity));
                    System.out.println("-------------------------------------------------------");*/
                }
                String responseContent="";
            	InputStream is = entity.getContent();
				StringBuffer strBuf = new StringBuffer();
				byte[] buffer = new byte[4096];
				int r = 0;
				while ((r = is.read(buffer)) > 0) {
					strBuf.append(new String(buffer, 0, r, code));
				}
				responseContent = strBuf.toString();
				return responseContent;
            } finally{
                httpResponse.close();
            }
             
        } catch( UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try{
               // closeHttpClient(httpClient);                
            } catch(Exception e){
                e.printStackTrace();
            }
        }
         return "";
    }
    private static CloseableHttpClient getHttpClient(){
        return HttpClients.createDefault();
    }
	   public static void main(String[] args) {
		   HttpClientUtils h=new HttpClientUtils();
		   h.requestByPostMethod();
	}
}
