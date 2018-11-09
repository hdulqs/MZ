/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午4:21:38
 */
package com.mz.customer.user.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.user.dao.AppCustomerDao;
import com.mz.customer.user.model.AppCustomer;
import com.mz.customer.user.model.AppCustomerSimple;
import com.mz.customer.user.service.AppCustomerService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.QueryFilter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月24日 下午4:21:38 
 */
@Service("appCustomerService")
public class AppCustomerServiceImpl extends BaseServiceImpl<AppCustomer, Long> implements
    AppCustomerService {
	
	@Resource(name = "appCustomerDao")
	@Override
	public void setDao(BaseDao<AppCustomer, Long> dao) {
		super.dao = dao;
	}
	@Autowired
	private RedisService redisService;
	@Override
	public PageResult findPageBySql(QueryFilter filter) {
		
		//----------------------分页查询头部外壳------------------------------
		//创建PageResult对象
		PageResult pageResult = new PageResult();
		Page<AppCustomer> page = null;
		if(filter.getPageSize().compareTo(Integer.valueOf(-1))==0){
			//pageSize = -1 时  
			//pageHelper传pageSize参数传0查询全部
			page= PageHelper.startPage(filter.getPage(), 0);
		}else{
			page = PageHelper.startPage(filter.getPage(), filter.getPageSize());
		}
		//----------------------分页查询头部外壳------------------------------
		
		//----------------------查询开始------------------------------
		
		String userName = filter.getRequest().getParameter("userName_like");
		String trueName = filter.getRequest().getParameter("trueName_like");
		String cardId = filter.getRequest().getParameter("cardId_like");
		String referralCode = (String) filter.getRequest().getAttribute("referralCode");
		String type = (String) filter.getRequest().getAttribute("type");
		String userName_in=filter.getRequest().getParameter("userName_in");
		String isDelete = filter.getRequest().getParameter("isDelete");
		String isReal = filter.getRequest().getParameter("isReal");
		String created_GT = filter.getRequest().getParameter("created_GT");
		String created_LT = filter.getRequest().getParameter("created_LT");
		String phone = filter.getRequest().getParameter("phone_like");
		Map<String,Object> map = new HashMap<String,Object>();
		if(!StringUtils.isEmpty(userName)){
			map.put("userName", "%"+userName+"%");
		}
		if(!StringUtils.isEmpty(trueName)){
			map.put("trueName", "%"+trueName+"%");
		}
		if(!StringUtils.isEmpty(cardId)){
			map.put("cardId", "%"+cardId+"%");
		}
		if(!StringUtils.isEmpty(referralCode)){
			map.put("referralCode", "%"+referralCode+"%");
		}
		if(!StringUtils.isEmpty(userName_in)){
			map.put("userName_in", userName_in.split(","));
		}
		if(!StringUtils.isEmpty(type)){
			map.put("type", "%"+type+"%");
		}
		if(!StringUtils.isEmpty(isDelete)){
			map.put("isDelete", isDelete);
		}
		if(!StringUtils.isEmpty(created_GT)){
			map.put("created_GT", created_GT);
		}
		if(!StringUtils.isEmpty(created_LT)){
			map.put("created_LT", created_LT);
		}
		if(!StringUtils.isEmpty(phone)){
			map.put("phone", "%"+phone+"%");
		}

		if(!StringUtils.isEmpty(isReal)){
			
			switch (isReal){
			 case "0": map.put("states", '0'); break;
			 case "1": map.put("states", 1); break;
			 case "2": map.put("states", 2); break;
			 case "3": map.put("states", 3); break;
			}
		}
		
		((AppCustomerDao)dao).findPageBySql(map);
		//----------------------查询结束------------------------------
		
		//----------------------分页查询底部外壳------------------------------
		//设置分页数据
		pageResult.setRows(page.getResult());
		//设置总记录数
		pageResult.setRecordsTotal(page.getTotal());
		pageResult.setDraw(filter.getDraw());
		pageResult.setPage(filter.getPage());
		pageResult.setPageSize(filter.getPageSize());
		//----------------------分页查询底部外壳------------------------------
		
		return pageResult;
	}

	@Override
	public List<AppCustomerSimple> findAppCustomerSimple(HttpServletRequest request) {
		return ((AppCustomerDao)dao).findAppCustomerSimple(null);
	}

	//禁用某个用户方法
	@Override
	public JsonResult storpCustomer(Long id,boolean type) {
		JsonResult jsonResult = new JsonResult();
		AppCustomer customer = super.get(id);
		Integer delete = customer.getIsDelete();
		if(type){
			if(delete == 0){
				customer.setIsDelete(1);
				super.update(customer);
				String uuid = customer.getUuid();
				
				redisService.delete("mobile:"+uuid);
				jsonResult.setMsg("已成功禁用此用户");
				jsonResult.setSuccess(true);
				return jsonResult;
			}else{
				jsonResult.setMsg("此用户以被禁用");
				jsonResult.setSuccess(false);
				return jsonResult;
			}
		}else{
			
			if(delete == 0){
			
				jsonResult.setMsg("此用户未被禁用");
				jsonResult.setSuccess(false);
				return jsonResult;
			}else{
				customer.setIsDelete(0);
				super.update(customer);
				jsonResult.setMsg("解除禁用成功");
				jsonResult.setSuccess(true);
				return jsonResult;
			}
		}
		
		
	}

	@Override
	public JsonResult lockCustomer(Date time, Long id,boolean type) {
		JsonResult result = new JsonResult();
		
		AppCustomer customer = super.get(id);
		Integer delete = customer.getIsDelete();
		if(type){
			if(delete !=1){
				customer.setLockTime(time);
				customer.setIsLock(1);
				super.update(customer);
				result.setMsg("锁定该用户成功");
				result.setSuccess(true);
				return result;
			}
			result.setMsg("该用户已被禁用不能锁定");
			result.setSuccess(false);
		}else{
			if(delete !=1){
				  if(customer.getIsLock()==0){
					   result.setMsg("该用户未锁定");
						result.setSuccess(false);
						return result;
				  }else{
					  customer.setIsLock(0);
						super.update(customer);
						result.setMsg("解锁该用户成功");
						result.setSuccess(true);
						return result;
				  }
				
			}
			result.setMsg("该用户已被禁用不能解锁");
			result.setSuccess(false);
		}
		
		return result;
	}

	
	
	@Override
	public List<AppCustomer> findById(Long id) {
		
		Map<String, Object> map=new HashMap<String, Object>();
		
		
		if(null!=id&&!"".equals(id)){
			map.put("id", id);
			List<AppCustomer> list=	((AppCustomerDao)dao).findById(map);
			return list;
		}
		
		return null;
	}


	
	@Override
	public List<AppCustomer> getRealNameCustomer() {
		
		List<AppCustomer> list=	((AppCustomerDao)dao).getRealNameCustomer();
		return list;
	}

	
	@Override
	public List<AppCustomer> getFundChangeCustomers(Map<String, Object> map) {
		return ((AppCustomerDao)dao).getFundChangeCustomers(map);
	}

	@Override
	public int getHasAuthNum() {
		return ((AppCustomerDao)dao).getHasAuthNum();
	}


	@Override
	public AppCustomer getByCustomerId(String username) {
		// TODO Auto-generated method stub
		Map<String, Object> map=new HashMap<String, Object>();
		if(null!=username && !"".equals(username)){
			map.put("username", username);
			List<AppCustomer> list=((AppCustomerDao)dao).getByCustomerId(map);
			return list==null?null:list.get(0);
		}
		return null;
	}

	@Override
	public AppCustomer getByPhone(String mobile) {
		// TODO Auto-generated method stub
				Map<String, Object> map=new HashMap<String, Object>();
				if(null!=mobile && !"".equals(mobile)){
					map.put("phone", mobile);
					List<AppCustomer> list=((AppCustomerDao)dao).getByPhone(map);
					return list==null?null:list.get(0);
				}
				return null;
	}

	@Override
	public int commendCount(String userName) {
		// TODO Auto-generated method stub
		return ((AppCustomerDao)dao).commendCount(userName);

	}

}
