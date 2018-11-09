package com.fh.util;

 
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;


@Controller
public class SuiXingFuService{

	
	
	private static String encrypt(String encodeType,String json, String sxfPublic) {
		try {
			String resData = PaymentUtils.encrypt(json, sxfPublic);
			return resData;
		} catch (Exception e) {
		}
		return "";
	}
	
	
	public  boolean doQuery(String orderNo ,Double amt) {
		try {
			return doQuery1(  orderNo ,  amt) ;
		} catch (Exception e) {
			 return false;
		}
	}
	
	
	String url = "https://api.suixingpay.com/onlinepay/queryOrder";
	String tranCd = "1002";
	String version = "1.0";
	String encodeType = "RSA#RSA";
	
	String mercNo = "600000000000592";
	String mercPrivateKey="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCXN2iTaRFNizn8sWsMH3D1Wrm2SpCHYO7hECfVlOckLK9oI4v3OJi2Hhplut8dgQXtTabD5EtpgXVNBOrbBOtK4CofeFIuy9MG4f+ta0Gut7aGwkc0s7tWEBV8cFx4gdDIKUs1WqB1hGLPETSRgY0BtvdpvhkyE+xy//HZkGht/hRgttzzWas/c/VV6Z1xlwZXyG3gWC5Qm9G8yORIXtCyCW7mpmBPcklZiYrXEwPkverQmNLWVd+t/V4BXNI7h38ZLtxB0rdzR6DbCJkNcKhj7POfg3hewaQG0mbI3RZn9N6/jh+165fH53fgs121HZ5IAx8gANR2P0AwlDw2pv51AgMBAAECggEAQg3+LIZ0/H6Y8f0+WalSWpsrDKo9wCVjtASuoenNnku2w7LjbTbX3pX/yfh7hhPaVvLYqjGLj6bxapd/T/UyIx8Qx3Zzz+VRZWxLl6GGq5+sp+kt2aO5bbrQ5+7dNfbfENCY/dF8zljqILHPWNBSnDEkmphjwTBa1VFggD1IoOL0dLWow5FK20fl5BVAhma58vTldSPdkmuFe+05rOG4mI6EiKcAXKS4o0DYUQgajPWX3DfxuEXQEqhi2zdoC1ptvSyA4k+zAY2BGABQhLdbBjsIhq2IXVcuAQPBzqSP8jLPnq2qj/kGr+YLYqVG5ZvoAHmlaS6bVmQro2KqofWuAQKBgQDGxeNYRqbiN9QWPU4NXOV9Q2pfDYoDE2ME704h1Mo+E5K3YQmSSyV+JFynTxxdWBusGFlzxEO3paSbFzoyV1GzKS4ywbnttd8pPUJLyIRD+vcpSJ35FLPiveUzHWOiXcTiCuenfyP6mIP6f8YmvNgdOr8gFT2WDzwOV9XzPZPL4QKBgQDCwHjm2Zsn65e0o2ZVNTbhPEvP2futxrV02ecpYFeJjI0iaFuEVeE5ctQBmKDO7FnOGiZHo8x5lQXVu5ROPdNZn24rPuuQZAQuvbQyrvrqtYYF2BOKbG6WifmWqE44kRWJlk53YGsyzJ+WoB/1F8t7JYCbr40VpwIILfXsgQflFQKBgGUI8SZNw0X5WrRzNNlV/tBNFCR8nUoy8IHxoaVQo/wptsm/aT4TNFuJ4A6rB5/qySYQumc9u639ZE3w4IP2HnES3YivL/LEH0q2+JvMsG5dgoRZhPK2szjtjTMOHTbKsuiy4k0hMwiNvYf2qqHGG5O/6KZGJafA1qXzv6yEADwBAoGBAKMP9usLPk8Rgqc7vR4Q/U9fZfa1hHY/D4W0QUmyofjIVw99I0tNgNPSIlLn0ZUP7i2UVYLcUgNm1Ejd01IjkAfVZrAM8b2nEJ12Nx/MxMiqjnnsLohTO9EN243ahAv25nqaAD8R2w/gpqq+jaNwde45O1KfjViYyinBGc0ZSFv1AoGAHxBzxpAY70RA1pF4eB1zEw3FqA6QIBpU5ukMo0qd1x1JMEqtU0m8RWVJwGPh4oYq7JYT9Ys3JKPjrP9YNpIX7Lvs87y6BnZ0a2kxdrP1KhCYE3Dx92OvFQzgC0QxU7ZaFdZHkIIjotndMaORluHTBndiFf+aftHZ7SCG7eZxag4=";
	String sxfPublic="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh02TjjtEaaCIrPLtq+8E2GoTVg08R4i47/jl0TqRK7hcR11WbairVUudLP0439y5sw8LObZ+RTICClXt04RkVLZkXWkEBsvYN3DiXEUFXaXu3n8Dlq1F2SWBuyGZvnuzO9L3nxirnEMb42MGf2YBDcJJ2HdlXftF50ymWqBnA7Rx639Cdo/fL3DqnPsj+ImOqNOXSH/HduYoj2Vii4SDCh1dlg7NuJmzltMhnZ8tqJweS/6M1a1Oateu1c/u4GWReDPchSdqTnQZQlMZ/LPhhPV7kHK/UAiT/w6q9HJ3BU+kOJ4Dsd1O7+ryplRdyPT6mT4goEsMUqUW+boIXZJUvQIDAQAB";

	
	public  boolean doQuery1(String orderNo ,Double amt) throws Exception  {
		// 获取参数
		net.sf.json.JSONObject order = new net.sf.json.JSONObject();
		order.put("orderNo", orderNo);
		// 利用工具类加密
		String data = encrypt(encodeType, order.toString(), sxfPublic);
		net.sf.json.JSONObject main = new net.sf.json.JSONObject();
		main.put("mercNo", mercNo);
		main.put("tranCd", tranCd);
		main.put("version", version);
		main.put("reqData", data);
		main.put("ip", "59.56.66.3");
		// 加签名，注意参数顺序
		String sign = PaymentUtils.sign(main.toString(), mercPrivateKey);
		net.sf.json.JSONObject json = new net.sf.json.JSONObject();
		json.put("mercNo", mercNo);
		json.put("tranCd", tranCd);
		json.put("version", version);
		json.put("reqData", data);
		json.put("ip", "59.56.66.3");
		json.put("sign", sign);
		json.put("encodeType", encodeType );
		HttpClient httpClient		= new HttpClient();
		PostMethod postMethod		= new PostMethod(url);
		NameValuePair[] datas		= {
				new NameValuePair("_t", json.toString()),
		};
		postMethod.setRequestBody(datas);
		int statusCode			= httpClient.executeMethod(postMethod);
		byte[] responseByte		= postMethod.getResponseBody();
		String responseBody		= new String(responseByte, "UTF-8");
		System.out.println(statusCode);
		System.out.println(responseBody);
		@SuppressWarnings("unchecked")
		Map<String, Object> jsonMap	= JSON.parseObject(responseBody, TreeMap.class);
		String rmercNo = (String)jsonMap.get("mercNo");
		String resCode = (String)jsonMap.get("resCode");
		if("000000".equals(resCode) && mercNo.equals(rmercNo)){
			String resData = (String)jsonMap.get("resData");
			String retdata = PaymentUtils.decrypt(resData, mercPrivateKey);
			//{"orderNo":"R14975040204235","tranAmt":198,"bankWay":"BOC","tranSts":"F","orderId":"736fa95bd5a74109bf88db72c9a745a7","bankId":"","endTime":"20170615132617","desc":"","payChannel":"4"}
			net.sf.json.JSONObject dataJson = net.sf.json.JSONObject.fromObject(retdata);
			String retOrderNo = dataJson.getString("orderNo");
			String tranSts = dataJson.getString("tranSts");
			Double tranAmt = dataJson.getDouble("tranAmt");
			if("S".equals(tranSts) && amt==tranAmt.doubleValue() && orderNo.equals(retOrderNo)){
				 return true ;
			}
			
		}
		return false;
	}
	

}
