/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月25日 上午11:22:44
 */
package com.mz.coin.third.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Zhang Xiaofang
 * @Date : 2016年7月25日 上午11:22:44
 */
@RequestMapping("/coin/coin")
@Controller("thirdCoinController")
public class CoinController {

	@ResponseBody
	@RequestMapping("/list")
	@MethodName(name = "查看钱包配置")
	public JsonResult list() {
		JsonResult jsonResult = new JsonResult();
		try {
			// 读取原来配置文件到beforeP
			InputStream in = this.getClass().getClassLoader()
					.getResourceAsStream("/coinConfig/bitCoinConfig.properties");
			Properties p = new Properties();
			p.load(in);
			in.close();
			Object jString = JSONObject.toJSON(p);
			System.out.println("-----" + jString);
			jsonResult.setObj(jString);

		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonResult;
	}

}
