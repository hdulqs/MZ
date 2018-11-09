/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Wu shuiming
 * @version:   V1.0 
 * @Date:      2015年09月28日  18:10:04
 */
package com.mz.web.message.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.web.app.model.AppMessage;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.oauth.user.model.AppUser;
import com.mz.web.message.service.AppMessageService;
import com.mz.web.message.service.MessageAsCustomerService;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * </p>
 * 
 * @author: Wu shuiming
 * @Date : 2015年09月28日 18:10:04
 */

@Controller
@RequestMapping("/message/appmessage")
public class AppMessageController extends BaseController<AppMessage, Long> {
	
	@Resource(name = "appMessageService")
	@Override
	public void setService(BaseService<AppMessage, Long> service) {
		super.service = service;
	}
	
	@Resource(name = "messageAsCustomerService")
	public MessageAsCustomerService messageAsCustomerService ;


	@MethodName(name = "修改一条消息")
	@RequestMapping("/modify/{type}")
	@ResponseBody
	// BindingResult result
	public JsonResult modify(@PathVariable int type ,AppMessage appMessage,
			@RequestParam(value = "receiveUserIdList[]") Long[] arry){
		                          
		AppUser user = ContextUtil.getCurrentUser();
		if(type == 1){
			// 保存发送人
			appMessage.setSendDate(new Date());
			appMessage.setSendUserName(user.getName());
			appMessage.setIsSend(type);
		}
		AppMessageService appMessageService = (AppMessageService)service;
		/*JsonResult result = appMessageService.updateMessage(appMessage, arry);*/
		return null;
	}

	
	// type等于1的时候是保存并发送 type等于2的时候只保存
	@MethodName(name = "增加一条消息")
	@RequestMapping("/add/{type}")
	@ResponseBody
	public JsonResult add(@PathVariable int type ,
			AppMessage appMessage,
			@RequestParam(value = "receiveUserIdList[]") String[] userNames) {
		
		AppUser user = ContextUtil.getCurrentUser();
		appMessage.setOperator(user.getUsername());
		if(type == 1){
			// 保存发送人
			appMessage.setSendDate(new Date());
			appMessage.setSendUserName(user.getUsername());
		}
		appMessage.setIsSend(type);
		AppMessageService messageService = (AppMessageService)service;
		JsonResult result = messageService.saveMessage(appMessage,userNames);
		return result;
	}
	// type等于1的时候是保存并发送 type等于2的时候只保存
	@MethodName(name = "增加一条消息")
	@RequestMapping("/adds/{type}")
	@ResponseBody
	public JsonResult adds(@PathVariable int type ,
			AppMessage appMessage,
			@RequestParam(value = "receiveUserNames") String receiveUserNames ) {
		AppUser user = ContextUtil.getCurrentUser();
		appMessage.setOperator(user.getUsername());
		if(type == 1){
			// 保存发送人
			appMessage.setSendDate(new Date());
			appMessage.setSendUserName(user.getUsername());
			appMessage.setIsSend(type);
		}
		AppMessageService messageService = (AppMessageService)service;
		JsonResult result = messageService.saveMessageName(appMessage,receiveUserNames);
		return result;
	}
	
	@MethodName(name = "发送一条消息")
	@RequestMapping("/send")
	@ResponseBody
	public JsonResult send(@RequestParam(value = "ids[]") Long[] messageIds) {
		
		AppMessageService appMessageService = (AppMessageService)service;
		JsonResult result = appMessageService.sendMessage(messageIds);
		return result;
	}
	

	@MethodName(name = "删除一条消息")
	@RequestMapping("/remove/{ids}")
	@ResponseBody
	public JsonResult remove(@PathVariable Long[] ids) {
		AppMessageService appMessageService = (AppMessageService)service;
		JsonResult result = appMessageService.removeMessage(ids);
		return result;
	}

	@MethodName(name = "查看一条消息")
	@RequestMapping("/see/{id}")
	@ResponseBody
	public AppMessage see(@PathVariable Long id) {
		return service.get(id);
	}

	@MethodName(name = "加载一条消息")
	@RequestMapping("/list/{state}")
	@ResponseBody
	public PageResult list(@PathVariable int state , HttpServletRequest request) {
		
		QueryFilter filter = new QueryFilter(AppMessage.class,request);
		
		AppMessageService appMessageService = (AppMessageService) service;
		
		PageResult result = appMessageService.selectMessageListVoByList(filter,state);
		
		return result ;
	}
	
	
	
	@MethodName(name = "分页查询所有的已发送的消息")
	@RequestMapping("/listMessageVo")
	@ResponseBody
	public PageResult listMessageVo(HttpServletRequest request) {
		
		QueryFilter filter = new QueryFilter(AppMessage.class,request);
		
		AppMessageService appMessageService = (AppMessageService) service;
		
		PageResult result = appMessageService.selectMessageVoByPage(filter);
		
		return result ;
	}
	
	
	
	
	
	
	
	
	
	
	@MethodName(name = "加载一条消息")
	@RequestMapping("/messageList/{state}")
	@ResponseBody
	public PageResult messageList(@PathVariable int state , HttpServletRequest request) {
		
		QueryFilter filter = new QueryFilter(AppMessage.class,request);
		
		filter.addFilter("isSend=", state);
		
		PageResult page = super.findPage(filter);
		
		return page;

	}
	
	
	
	
	
	
	
	
	
	
	
}
