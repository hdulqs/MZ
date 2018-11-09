package com.mz.coin.bds;

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
public class BdsRpcHttpClient {
	private static String coinType = Constant.BDS;
	private static Map<String, String> properties = Properties.appcoinMap();
	private static String  protocol=properties.get(coinType.toLowerCase()+"_protocol");
	private static String  ip=properties.get(coinType.toLowerCase()+"_ip");
	private static String  port=properties.get(coinType.toLowerCase()+"_port");
	public static  String CHARGE_ACCOUNT=properties.get(coinType.toLowerCase()+"_chargeAccount");
	public static  String WALLET_PASSWORD=properties.get(coinType.toLowerCase()+"_walletPassword");
	public static  String id=properties.get(coinType.toLowerCase()+"_id");
	public static  String memo=properties.get(coinType.toLowerCase()+"_coldMemo");
	private BdsRpcHttpClient() {
		
	}
	/**
	 * 只用于bds连接建立，后续会整合到clientMap中去
	 */
	private volatile static JsonRpcHttpClient jsonrpcClient;
	private static Map<String,JsonRpcHttpClient> clientMap;
	

	public static JsonRpcHttpClient getClient() {
		if (jsonrpcClient == null) {
			synchronized (BdsRpcHttpClient.class) {
				if (jsonrpcClient == null) {
					try {
						LogFactory.info("获取基于bds币的rpc连接成功");
	                	String url=protocol+"://"+ip+":"+port;
						jsonrpcClient = new JsonRpcHttpClient(new URL(url));
					} catch (MalformedURLException e) {
//						e.printStackTrace();
						LogFactory.info("bds钱包接口连接失败");
					}
				}
			}
		}
		return jsonrpcClient;
	}
	
	/**
	 * 根据参数建立rpc连接
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param protocol
	 * @param:    @param ip
	 * @param:    @param port
	 * @param:    @return
	 * @return: JsonRpcHttpClient 
	 * @Date :          2018年3月5日 上午11:09:57   
	 * @throws:
	 */
	public static JsonRpcHttpClient getClientByparam(String protocol,String ip,String port) {
		String url=protocol+"://"+ip+":"+port;
		//url充当key
		if(clientMap==null||clientMap.get(url)==null){
			if(clientMap==null){
				clientMap=new HashMap<String,JsonRpcHttpClient>(16);
			}
			synchronized (BdsRpcHttpClient.class) {
				try {
					clientMap.put(url, new JsonRpcHttpClient(new URL(url)));
					LogFactory.info("获取基于bts币的rpc连接成功");
				} catch (MalformedURLException e) {
					LogFactory.info("钱包rpc接口连接失败");
				}
			}
		}
		return clientMap.get(url);
	}
}