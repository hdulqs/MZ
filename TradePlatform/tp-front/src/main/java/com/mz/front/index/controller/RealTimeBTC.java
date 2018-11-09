package com.mz.front.index.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.mz.redis.common.utils.RedisService;
import com.mz.util.WebClient;
import com.mz.util.sys.SpringContextUtil;;

/**
 * 定时拉取BTC价格
 * RealTimeBTC.java
 * @author denghf
 * 2017年9月13日 下午7:01:26
 */
@Component
@EnableScheduling
public class RealTimeBTC {
	
	@Scheduled(fixedRate = 50000)
	public void updateBTC(){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
/*		RedisService redisService = SpringContextUtil.getBean("redisService");
		String sendUrl = "";
		try {
			sendUrl = WebClient.getWebContentByPost("https://www.bitstamp.net/api/v2/ticker/btcusd", "utf-8",60000);
			JSONObject jsonv = JSON.parseObject(sendUrl);
			String last = jsonv.getString("last");
			redisService.save("bitstamp_btc_price", last);
			System.out.println("-------------------"+sd.format(new Date())+"-----当前BTC价格："+last+"美元----------------------------");
		} catch (Exception e) {
			System.out.println("访问b网出错");
		}*/
	}
	
	@Scheduled(fixedRate = 50000)
	public void updateETH(){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
/*		RedisService redisService = SpringContextUtil.getBean("redisService");
		String sendUrl = "";
		try {
			sendUrl = WebClient.getWebContentByPost("https://www.bitstamp.net/api/v2/ticker/ethusd", "utf-8",60000);
			JSONObject jsonv = JSON.parseObject(sendUrl);
			String last = jsonv.getString("last");
			redisService.save("bitstamp_eth_price", last);
			System.out.println("-------------------"+sd.format(new Date())+"-----当前ETH价格："+last+"美元----------------------------");
		} catch (Exception e) {
			System.out.println("访问b网出错");
		}*/
	}
}
