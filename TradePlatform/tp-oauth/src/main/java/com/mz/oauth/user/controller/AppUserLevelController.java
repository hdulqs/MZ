/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年2月17日 下午3:05:37
 */
package com.mz.oauth.user.controller;

import com.mz.shiro.service.AppUserLevelService;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.oauth.user.model.AppUserLevel;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年2月17日 下午3:05:37 
 */
@Controller
@RequestMapping("/user/appuserlevel")
public class AppUserLevelController extends BaseController<AppUserLevel, Long>{
	
	@Resource(name = "appUserLevelService")
	@Override
	public void setService(BaseService<AppUserLevel, Long> service) {
		super.service = service;
	}
	
	@MethodName(name="增加")
	@RequestMapping(value="/addBatch", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult addBatch(@RequestBody List<AppUserLevel> users){
		return ((AppUserLevelService)service).addBatch(users);
	}
	
	@MethodName(name="配置页面查询上下级结构树")
	@RequestMapping(value="/list", method = RequestMethod.POST)
	@ResponseBody
	public List<AppUserLevel> list(){
		return ((AppUserLevelService)service).saveForlist();
	}
	
	@MethodName(name="人关系图 ")
	@RequestMapping(value="/loadTree")
	@ResponseBody
	public List<AppUserLevel> loadTree(){
		return service.findAll();
	}

}
