/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年5月17日 下午12:02:33
 */
package com.mz.web.article.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppFriendLink;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.web.article.service.AppFriendLinkService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Wu Shuiming
 * @Date :          2016年5月17日 下午12:02:33 
 */


@Controller      
@RequestMapping("/article/appfriendlink")
public class AppFriendLinkController extends BaseController<AppFriendLink, Long> {

	@Resource(name = "appFriendLinkService")
	@Override
	public void setService(BaseService<AppFriendLink, Long> service) {
		super.service = service ;
	}
	
	@MethodName(name = "修改一条文章类型数据")
	@RequestMapping("/modify")
	@ResponseBody
	public JsonResult modify(AppFriendLink appCategory) {
		return super.update(appCategory);
	}

	@MethodName(name = "增加一条文章类型数据")
	@RequestMapping("/add")
	@ResponseBody
	public JsonResult add(AppFriendLink appCategory) {
		return super.save(appCategory);
	}

	@MethodName(name = "删除一条文章类型数据")
	@RequestMapping("/remove/{ids}")
	@ResponseBody
	public JsonResult remove(@PathVariable String ids) {
		JsonResult result =null;
		AppFriendLinkService aservice = (AppFriendLinkService) service;
		  String[] id=ids.split(",");
		  for(int i=0;i<id.length;i++){
			  AppFriendLink friendLink = aservice.get(Long.valueOf(id[i]));
			  friendLink.setStatus(2);
			   result = super.update(friendLink);
		  }
		  return result;
		
		
	}

	@MethodName(name = "查看文章类型")
	@RequestMapping("/see/{id}")
	@ResponseBody
	public AppFriendLink see(@PathVariable String id) {
		AppFriendLinkService aservice = (AppFriendLinkService) service;
		AppFriendLink friendLink = aservice.get(Long.valueOf(id));
		return friendLink;
	}

	@MethodName(name = "加载所有文章类型列表数据")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(AppFriendLink.class, request);
		filter.addFilter("status_in", "0,1");
		return super.findPage(filter);
	}

	@MethodName(name = "加载所有文章类型列表数据")
	@RequestMapping("/selectlist")
	@ResponseBody
	public List<AppFriendLink> selectlist(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(AppFriendLink.class, request);
		filter.addFilter("state=", 0);
		List<AppFriendLink> list = super.find(filter);
		return list;
	}

}
