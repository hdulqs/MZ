package com.mz.coin.eth.client;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.mz.Constant;
import com.mz.util.log.LogFactory;
import com.mz.utils.Properties;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
/**
 * jsonrpc调用
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2017年11月20日 下午3:43:58
 */
public class RpcHttpClient {
	private static String  protocol= Properties.appcoinMap().get(Constant.ETHER.toLowerCase()+"_protocol");
	private static String  ip=Properties.appcoinMap().get(Constant.ETHER.toLowerCase()+"_ip");
	private static String  port=Properties.appcoinMap().get(Constant.ETHER.toLowerCase()+"_port");
	private RpcHttpClient() {
		
	}

	private volatile static JsonRpcHttpClient jsonrpcClient;

	public static JsonRpcHttpClient getClient() {
		if (jsonrpcClient == null) {
			synchronized (RpcHttpClient.class) {
				if (jsonrpcClient == null) {
					try {
	                	String url=protocol+"://"+ip+":"+port;
	                	LogFactory.info("获取eth代币的jsonrpc连接---"+"url="+url);
						jsonrpcClient = new JsonRpcHttpClient(new URL(url));
						Map<String,String> headers=new HashMap<>();
						headers.put("Content-Type", "application/json");
						jsonrpcClient.setHeaders(headers);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return jsonrpcClient;
	}
	
	/**
	 * 根据参数建立rpc链接
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param protocol
	 * @param:    @param ip
	 * @param:    @param port
	 * @param:    @return
	 * @return: JsonRpcHttpClient 
	 * @Date :          2018年3月2日 下午1:33:46   
	 * @throws:
	 */
	public static JsonRpcHttpClient getClientByParam(String protocol,String ip,String port) {
		if (jsonrpcClient == null) {
			synchronized (RpcHttpClient.class) {
				if (jsonrpcClient == null) {
					try {
	                	String url=protocol+"://"+ip+":"+port;
	                	LogFactory.info("获取eth代币的jsonrpc连接---"+"url="+url);
						jsonrpcClient = new JsonRpcHttpClient(new URL(url));
						Map<String,String> headers=new HashMap<>();
						headers.put("Content-Type", "application/json");
						jsonrpcClient.setHeaders(headers);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return jsonrpcClient;
	}
}