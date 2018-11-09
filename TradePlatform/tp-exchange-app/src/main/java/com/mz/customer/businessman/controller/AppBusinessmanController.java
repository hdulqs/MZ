/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2017-12-06 18:43:30 
 */
package com.mz.customer.businessman.controller;

import com.alibaba.fastjson.JSONObject;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.businessman.model.AppBusinessman;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2017-12-06 18:43:30 
 */
@Controller
@RequestMapping("/businessman/appbusinessman")
public class AppBusinessmanController extends BaseController<AppBusinessman, Long> {
	
	@Resource(name = "appBusinessmanService")
	@Override
	public void setService(BaseService<AppBusinessman, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppBusinessman")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppBusinessman see(@PathVariable Long id){
		AppBusinessman appBusinessman = service.get(id);
		return appBusinessman;
	}
	
	@MethodName(name="增加AppBusinessman")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppBusinessman appBusinessman){
		return super.save(appBusinessman);
	}
	
	@MethodName(name="修改AppBusinessman")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppBusinessman appBusinessman){
		return super.update(appBusinessman);
	}
	
	@MethodName(name="删除AppBusinessman")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppBusinessman")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppBusinessman.class,request);
		return super.findPage(filter);
	}
	
	
	@RequestMapping(value="/changerule")
	@ResponseBody
	public JsonResult changerule(HttpServletRequest request){
		
		/**
		 * 0指定匹配
		 * 1随机匹配
		 * 2价格匹配
		 */
		String c2crule = request.getParameter("c2crule");
		/**
		 * 指定匹配中的A类,B类商户
		 */
		String businessmanType = request.getParameter("businessmanType");
		/**
		 * 价格匹配中的A类，B类商户
		 */
		String businessmanType2 = request.getParameter("businessmanType2");//价格匹配商户类型
		JSONObject obj = new JSONObject();
		obj.put("c2crule", c2crule);
		if(!StringUtils.isEmpty(businessmanType)){
			obj.put("businessmanType", businessmanType);
		}
		if(!StringUtils.isEmpty(businessmanType2)){
			obj.put("businessmanType2", businessmanType2);
		}
		RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
		redisService.save("cn:c2crule", obj.toJSONString());
		
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		
		return jsonResult;
	}
	
	
	@RequestMapping(value="/loadchangerule")
	@ResponseBody
	public JsonResult loadchangerule(HttpServletRequest request){
		
		
		JsonResult jsonResult = new JsonResult();
		RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
		String str = redisService.get("cn:c2crule");
		if(!StringUtils.isEmpty(str)){
			jsonResult.setObj(JSONObject.parseObject(str));
		}
		jsonResult.setSuccess(true);
		return jsonResult;
	}
	
	
	
	@RequestMapping("/findall")
	@ResponseBody
	public List<AppBusinessman> findall(HttpServletRequest request){
		return super.findAll();
	}
	
	
}
