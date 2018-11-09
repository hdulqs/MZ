package com.mz.front.user.entrust.controller;

import com.alibaba.fastjson.JSON;
import com.mz.core.mq.service.MessageProducer;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.front.user.entrust.model.Entrust;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.SpringContextUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {

	/**
	 * 注册类型属性编辑器
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {

		// 系统注入的只能是基本类型，如int，char，String

		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class, new DateTimePropertyEditorSupport());

		/**
		 * 防止XSS攻击，并且带去左右空格功能
		 */
		binder.registerCustomEditor(String.class, new StringPropertyEditorSupport(true, false));
	}

	
	
	@Resource 
	private MessageProducer messageProducer;
	
	@RequestMapping("save")
	@ResponseBody
	public JsonResult save(HttpServletRequest request, HttpServletResponse response) {
		
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String str = redisService.get("lhz_buy_entrust");
		List<Entrust> list = new ArrayList<Entrust>();
		if(!StringUtils.isEmpty(str)){
			list = com.alibaba.fastjson.JSONArray.parseArray(str, Entrust.class);
		}
//		for(int i = 0; i < 100000 ; i++){
//			Entrust e = new Entrust();
//			e.setAccount_id("1");
//			e.setCoin_code("BTC");
//			e.setEntrust_amout(new BigDecimal(100));
//			e.setEntrust_number(UUID.randomUUID().toString());
//			e.setEntrust_price(new BigDecimal(100));
//			e.setEntrust_time(new Date());
//			list.add(e);
//		}
		long s =System.nanoTime();
		String str1 =JSON.toJSONString(list);
		long a = System.nanoTime();
		System.out.println(a-s);
		redisService.save("lhz_buy_entrust", str1);
		long e = System.nanoTime();
		System.out.println(e-a);
		
		
		return  new JsonResult();
		
	}
	
	

	@RequestMapping("buy")
	@ResponseBody
	public JsonResult put(HttpServletRequest request, HttpServletResponse response) {

		JsonResult jr = new JsonResult();
		String entrust_price = request.getParameter("p");
		String entrust_amount = request.getParameter("a");
		
//		Runnable runnable = new Runnable() {  
//            public void run() {  
//                while (true) {  
//                    // ------- code for task to run  
//                	  int max=2000;
//                      int min=10;
//                      Random random = new Random();
//                      int p = random.nextInt(max)%(max-min+1) + min;
//                      
//                      
//                      int maxa=20;
//                      int mina=10;
//                      Random r = new Random();
//                      
//                      int a = r.nextInt(maxa)%(maxa-mina+1) + mina;
//                	
//                     System.err.println("生成委托买单");
//                	entrustService.addEntrust("abcde12345","account12345", "buy", "BTC", new BigDecimal(p), new BigDecimal(a));
//                	
//                    // ------- ends here  
//                    try {  
//                        Thread.sleep(1);  
//                    } catch (InterruptedException e) {  
//                        e.printStackTrace();  
//                    }  
//                }  
//            }  
//        };  
//        ThreadPool.exe(runnable);
		
//		entrustService.addEntrust("abcde12345","account12345", "buy", "BTC", new BigDecimal(entrust_price), new BigDecimal(entrust_amount));
		
		//发送mq消息
		//queueSender.send("test.queue", "user12345,account12345,buy,BTC,"+entrust_price+","+entrust_amount);
		
		
		return jr.setSuccess(false).setMsg("委托成功");

	}
	
	@Test
	public void aa(){
		long s = System.nanoTime();
		long e = System.nanoTime();
		System.out.println(e-s);
		
	}
	
	@RequestMapping("sell")
	@ResponseBody
	public JsonResult sell(HttpServletRequest request, HttpServletResponse response) {

		JsonResult jr = new JsonResult();
		String entrust_price = request.getParameter("p");
		String entrust_amount = request.getParameter("a");
		
		RedisService  redisService = SpringContextUtil.getBean("redisService");
		redisService.keys("");
		
//		Runnable runnable = new Runnable() {  
//            public void run() {  
//                while (true) {  
//                    // ------- code for task to run  
//                	  int max=2000;
//                      int min=10;
//                      Random random = new Random();
//                      int p = random.nextInt(max)%(max-min+1) + min;
//                      
//                      int maxa=20;
//                      int mina=10;
//                      Random r = new Random();
//                      
//                      int a = r.nextInt(maxa)%(maxa-mina+1) + mina;
//                	
//                      System.err.println("生成委托卖单");
//                	entrustService.addEntrust("abcde12345","account12345", "sell", "BTC", new BigDecimal(p), new BigDecimal(a));
//                	
//                    // ------- ends here  
//                    try {  
//                        Thread.sleep(5000);  
//                    } catch (InterruptedException e) {  
//                        e.printStackTrace();  
//                    }  
//                }  
//            }  
//        };  
//        ThreadPool.exe(runnable);
//		entrustService.addEntrust("abcde12345","account12345", "sell", "BTC", new BigDecimal(entrust_price), new BigDecimal(entrust_amount));
	
		
		//发送mq消息
		//queueSender.send("test.queue", "user12345,DC_account12345,sell,BTC,"+entrust_price+","+entrust_amount);
		
		
		return jr.setSuccess(false).setMsg("委托成功");

	}

	

}
