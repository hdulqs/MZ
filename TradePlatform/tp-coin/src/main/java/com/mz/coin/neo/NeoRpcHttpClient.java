package com.mz.coin.neo;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.mz.Constant;
import com.mz.coin.bds.BdsRpcHttpClient;
import com.mz.util.log.LogFactory;
import com.mz.utils.Properties;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class NeoRpcHttpClient {
	private static String protocol = Properties.appcoinMap().get(Constant.NEO.toLowerCase() + "_protocol");
	private static String ip = Properties.appcoinMap().get(Constant.NEO.toLowerCase() + "_ip");
	private static String port = Properties.appcoinMap().get(Constant.NEO.toLowerCase() + "_port");
	public static String getHttpClient(String methodname, List list) {
		String url = "";
		try {
			url = protocol + "://" + ip + ":" + port + "?jsonrpc=2.0&id=1&method=" + methodname + "&params="
					+ (URLEncoder.encode(list + "", "UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String result = "";
		HttpClient client = new DefaultHttpClient();
		HttpGet get;
		try {
			get = new HttpGet(url);
		
		get.setHeader("charset", "UTF-8");
			HttpResponse response = client.execute(get);
			if (200 == response.getStatusLine().getStatusCode()) {
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			} else {

			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private NeoRpcHttpClient() {

	}

	private volatile static JsonRpcHttpClient jsonrpcClient;

	public static JsonRpcHttpClient getClient() {
		if (jsonrpcClient == null) {
			synchronized (BdsRpcHttpClient.class) {
				if (jsonrpcClient == null) {
					try {
						LogFactory.info("获取bds币的rpc连接成功");
						String url = protocol + "://" + ip + ":" + port;
						jsonrpcClient = new JsonRpcHttpClient(new URL(url));
					} catch (MalformedURLException e) {
						// e.printStackTrace();
						LogFactory.info("bds钱包接口连接失败");
					}
				}
			}
		}
		return jsonrpcClient;
	}
}
