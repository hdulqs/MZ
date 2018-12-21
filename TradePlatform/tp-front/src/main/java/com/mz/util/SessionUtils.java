package com.mz.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.sys.SpringContextUtil;
import com.mz.manage.remote.model.User;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 * SessionUtils.java
 * @author denghf
 * 2017年9月12日 下午6:20:17
 */
@Component
public class SessionUtils {
	
	public static Map<String,Object> map = new HashMap<String,Object>();
	
	
	/**
	 * 封装一下
	 * @param t
	 * @return
	 */
	public static User getUser(HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		if(user!=null){
			return user;
		}
		return null;
	}
	
	/**
	 * 退出
	 * @param tokenId
	 * @return
	 */
	public static boolean logout(HttpServletRequest request,HttpServletResponse response){
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return true;
		}
		RedisService redisService = SpringContextUtil.getBean("redisService");
		redisService.delete("mobile:"+user.getUuid());
		request.getSession().removeAttribute("user");
		return true;
	}
	
	/**
	 * 更新redis
	 * @param user
	 */
	public static void updateRedis(User user){
		//重新设置时常
		RedisService redisService = SpringContextUtil.getBean("redisService");
		redisService.save("mobile:"+user.getUuid(), "{\"mobile\":\""+user.getUsername()+"\",\"user\":"+JSON.toJSON(user).toString()+"}", 1800);
	}
	
	public static void main(String[] args) throws JsonProcessingException {
		String abc = "{\"mobile\":\"1602514484@qq.com\",\"user\":\"{\"accountPassWord\":\"\",\"customerId\":69433,\"customerType\":1,\"googleState\":0,\"isChange\":0,\"isDelete\":0,\"isLock\":0,\"isReal\":1,\"messIp\":\"10.154.92.230\",\"mobile\":\"1602514484@qq.com\",\"phone\":\"+86 13718730732\",\"phoneState\":1,\"saasId\":\"hurong_system\",\"salt\":\"74f5f29c530a52b91d09472a1a393769\",\"truename\":\"\",\"userCode\":\"8c3fa6d9aada49a185d580155508c63c\",\"username\":\"1602514484@qq.com\"}\"}";
		/*ObjectMapper  om  = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
		String aa = om.writeValueAsString(abc);*/
		//"phone":"+86 13718730732","customerType":1,"truename":"","userCode":"8c3fa6d9aada49a185d580155508c63c","accountPassWord":"","isDelete":0,"phoneState":1,"username":"1602514484@qq.com","customerId":69433,"isReal":1,"saasId":"hurong_system","isChange":0,"googleState":0,"salt":"74f5f29c530a52b91d09472a1a393769","mobile":"1602514484@qq.com","isLock":0,"messIp":"10.154.92.230"
		
		JSONObject jsonv = JSON.parseObject(abc);
		String userStr = jsonv.getString("user");
		System.out.println(userStr);
	}
}
