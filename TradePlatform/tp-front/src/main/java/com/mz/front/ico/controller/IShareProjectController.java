/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年8月25日 下午3:21:39
 */
package com.mz.front.ico.controller;

import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.manage.remote.ico.RemoteIcoService;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.util.SessionUtils;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ico业务前台方法
 * <p> TODO</p>
 * @author:         Shangxl 
 * @Date :          2017年7月14日 下午3:47:15
 */
@Controller
@RequestMapping("/iShareProject")
public class IShareProjectController {
	@Resource
	private RemoteIcoService remoteIcoService;//ico远程调用接口
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class, new DateTimePropertyEditorSupport());
		/**
		 * 防止XSS攻击，并且带去左右空格功能
		 */
		binder.registerCustomEditor(String.class, new StringPropertyEditorSupport(true, false));
	}
	
	
	/**
	 * 查询我分享的项目list
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param request
	 * @param:    @return
	 * @return: FrontPage 
	 * @Date :          2017年7月19日 下午8:25:59   
	 * @throws:
	 */
	@RequestMapping("/list")
	@ResponseBody
	public FrontPage list(HttpServletRequest request) {
		Map<String, String> params = HttpServletRequestUtils.getParams(request);
		User user=SessionUtils.getUser(request);
		if(user!=null){
			params.put("customerId=",user.getCustomerId().toString());
			return remoteIcoService.listIshareProject(params);
		}
		return null;
	
	}
}
