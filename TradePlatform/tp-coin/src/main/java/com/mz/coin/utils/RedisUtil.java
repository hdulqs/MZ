package com.mz.coin.utils;

import com.alibaba.fastjson.JSON;
import com.mz.coin.enums.Coin;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.sys.ContextUtil;
import java.util.List;


public class RedisUtil {
	/**
	 * 获取btc rpc_server
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Shangxl
	 * @param: @return
	 * @return: List<String>
	 * @Date : 2017年9月20日 下午1:44:43
	 * @throws:
	 */
	public static List<String> listcoin() {
		RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
		if (redisService != null) {
			String listcoinstr = redisService.get(ContextUtil.getWebsite() + ":productList");
			if (listcoinstr != null) {
				return JSON.parseArray(listcoinstr, String.class);
			} else {
				System.out.println("未从redis中查到coins");
				return null;
			}
		} else {
			System.out.println("redis连接错误");
			return null;
		}
	}
	
	/**
	 * 设置参数
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param key
	 * @param:    @param val
	 * @return: void 
	 * @Date :          2018年1月26日 下午5:50:58   
	 * @throws:
	 */
	public static void setValue(String key,String val){
		RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
		redisService.save(key,val);
	}
	
	
	/**
	 * 保存枚举
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param key
	 * @param:    @param val
	 * @return: void 
	 * @Date :          2018年1月26日 下午5:50:58   
	 * @throws:
	 */
	public static void setEnums(String key,Coin c){
		RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
		String cJson = com.alibaba.fastjson.JSONObject.toJSONString(c);
		redisService.save("CoinJson:"+key, cJson);
	}
	/**
	 * 获取value
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param key
	 * @param:    @return
	 * @return: String 
	 * @Date :          2018年1月26日 下午5:54:06   
	 * @throws:
	 */
	public static String getValue(String key){
		RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
		return redisService.get(key);
	}
}
