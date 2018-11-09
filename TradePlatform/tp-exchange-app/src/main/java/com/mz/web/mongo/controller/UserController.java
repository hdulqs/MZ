/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月18日 上午10:32:03
 */
package com.mz.web.mongo.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.PageResult;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年9月18日 上午10:32:03 
 */
@Controller
@RequestMapping("/mongo/user")
public class UserController {
	
	//-------------------------跳转---------------------------------
	@MethodName(name="跳转用户列表页")
	@RequestMapping("/listView")  
	public String listView(){
		return "/mongo/user";
	}
	
	@MethodName(name="跳转用户列表页")
	@RequestMapping("/list") 
	@ResponseBody
	public PageResult list(HttpServletRequest request){
	//	QueryFilter filter = new QueryFilter(request);
		PageResult p = new PageResult();
	/*
		MongoDatabase dataBase = MongoDBJDBC.getDataBase("db");
		MongoCollection<Document> collection = dataBase.getCollection("user");
		FindIterable<Document> find = collection.find();
		System.out.println(filter.getPage());
		System.out.println(filter.getPageSize());
		find.skip((filter.getPage()-1)*filter.getPageSize());
		find.limit(filter.getPageSize());
		MongoCursor<Document> iterator = find.iterator();
		
		
		System.out.println(find.first().size());
		
		List<User> list = new ArrayList<User>();
		while (iterator.hasNext()) {
			Document next = iterator.next();
			User user = new User();
			user.setUsername(next.getString("username"));
			user.setAge(next.getInteger("age").toString());
			list.add(user);
		}
		Long count = Long.valueOf("100000");
		
		p.setDraw(filter.getDraw());
		p.setRows(list);
		
		p.setRecordsTotal(count);
		p.setRecordsFiltered(count);
		p.setPage(filter.getPage());
		p.setPageSize(filter.getPageSize());*/
		
		return p;
	}
	

	
	
	
	//----------------------------------------------------
	

}
