/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月14日 上午11:48:37
 */
package com.mz.util;

import com.mz.util.sys.ContextUtil;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.management.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * <p>mybatis通用查询组件</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月14日 上午11:48:37 
 */
public class RemoteQueryFilter implements  Serializable{
	private Example example = null;
	private Criteria criteria = null;
	
	private Integer page;// 要查找第几页
	private Integer pageSize;// 每页显示多少条
	
	private Integer draw;//dataTable的请求标记数

	private HttpServletRequest request;
	
	private boolean nosaas = true;
	
	private String saasId;
	
	private Class<?> clazz;
	private HashMap<String, String> requestMap;
	
	private String orderBy ;
	
	
	private LinkedHashMap<String, Object> filterMap = new LinkedHashMap<String, Object>();

	/**
	 * <p> TODO</p>
	 * @return:     HashMap<String,String>
	 */
	public HashMap<String, String> getRequestMap() {
		return requestMap;
	}

	/** 
	 * <p>手动设置saasId   主要用在远程调用时</p>
	 * @return: String
	 */
	public void setSaasId(String saasId) {
		this.saasId = saasId;
	}

	/**
	 *  私有构造
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:
	 */
	private RemoteQueryFilter(){}
	
	/**
	 * 构造封装 Example 对象
	 * 
	 * QueryFilter 默认Saas模式
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param entityClass
	 */
	public RemoteQueryFilter(Class<?> entityClass){
		this.clazz = entityClass;
	}
	
	
	/**
	 * 带request的QueryFilter
	 * 
	 * 主要自动封装dataTable参数
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param entityClass
	 * @param:    @param request
	 */
	public RemoteQueryFilter(Class<?> entityClass,HttpServletRequest request){
		//给clazz附值
		this.clazz = entityClass;
		this.requestMap = new HashMap<String, String>();
		this.saasId = ContextUtil.getSaasId();

		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = StringUtils.trim(request.getParameter(name));
			
			if (StringUtils.isNotBlank(value)&&!name.equals("_dc")) {
				this.requestMap.put(name, value);
			}
		}
	}
	
	/**
	 * 生成QueryFilter
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @return
	 * @return: QueryFilter 
	 * @Date :          2016年5月9日 下午5:21:15   
	 * @throws:
	 */
	public QueryFilter getQueryFilter(){
		QueryFilter queryFilter = null;
		if(requestMap!=null){
			queryFilter = new QueryFilter(clazz,requestMap);
			queryFilter.setSaasId(saasId);
		}else{
			queryFilter = new QueryFilter(clazz);
			queryFilter.setSaasId(saasId);
			
			queryFilter.setPage(this.page);
			queryFilter.setPageSize(this.pageSize);
			queryFilter.setOrderby(this.orderBy);
			
		}
		//加载过滤参数
		if(filterMap!=null&&filterMap.size()>0){
			Set<Entry<String, Object>> entrySet = filterMap.entrySet();
			Iterator<Entry<String, Object>> iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> next = iterator.next();
				queryFilter.addFilter(next.getKey(), next.getValue());
			}
		}
		//设置orderBy
		if(!StringUtils.isEmpty(orderBy)){
			queryFilter.setOrderby(getOrderBy());
		}
		
		return queryFilter;
	}
	
	
	/**
	 * dataOrderBy
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param request
	 * @param:    @param name
	 * @param:    @param value
	 * @return: void 
	 * @Date :          2016年4月6日 下午2:59:18   
	 * @throws:
	 */
	private void orderBy(HttpServletRequest request,String name ,String value){
		if(name.contains("order")==true){
			if(name.contains("dir")){
				String num = request.getParameter(name.replace("dir", "column"));
				String data = request.getParameter("columns["+num+"]"+"[data]");
				if(data!=null&&!"".equals(data)){
					example.setOrderByClause(data+" " + value);
				}
			}
		}
	}
	
	/**
	 * or
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param condition
	 * @param:    @param value
	 * @param:    @return
	 * @return: QueryFilter 
	 * @Date :          2016年4月18日 下午7:33:31   
	 * @throws:
	 */
	public RemoteQueryFilter or(String condition,String value){
		Criteria newcriteria = example.createCriteria();
		example.or(addCriteria(condition, value,newcriteria));
		return this;
	}
	
	

	
	
	/**
	 * 添加条件
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param condition
	 * @param:    @param value
	 * @return: void 
	 * @Date :          2016年4月6日 下午2:57:28   
	 * @throws:
	 */
	private Criteria addCriteria(String condition, Object value,Criteria criteria){
		
		/*-------------------------------------like处理-------------------------------------------------*/
		if(condition.contains("_like")||condition.contains("_LIKE")){
			String[] split = condition.split("_");
			criteria.andLike(split[0], value+"");
		}
		/*-------------------------------------in处理-------------------------------------------------*/
		else if(condition.contains("_in")||condition.contains("_IN")){
			String[] split = condition.split("_");
			
			if(value instanceof String){
				String v = ((String) value).trim();
				String[] vArr = v.split(",");
				StringBuffer sb = new StringBuffer();
				for(int i = 0 ; i < vArr.length ; i++ ){
					sb.append("'"+vArr[i]+"'");
					if(i<vArr.length-1){
						sb.append(",");
					}
				}
				criteria.andCondition(" "+split[0]+" "+"in" +" ( " +sb.toString() + " )");
			}else{
				criteria.andIn(split[0], (List)value);
			}
		}
		/*-------------------------------------【_EQ】【_GT】【_LT】【_NEQ】处理-------------------------------------------------*/
		/**
		 *        【_EQ】              =
		 *        【_GT】              >
		 *        【_LT】             <
		 *        【_NEQ】          !=
		 */
		else if(condition.contains("_EQ")){
			String[] strArr = condition.split("_");
			criteria.andCondition(strArr[0]+"=", value);
		}else if(condition.contains("_GT")){
			String[] strArr = condition.split("_");
			criteria.andCondition(strArr[0]+">", value);
		}else if(condition.contains("_LT")){
			String[] strArr = condition.split("_");
			criteria.andCondition(strArr[0]+"<", value);
		}else if(condition.contains("_NEQ")){
			String[] strArr = condition.split("_");
			criteria.andCondition(strArr[0]+"!=", value);
		}
		/*-------------------------------------【=】【>】【<】【!=】处理-------------------------------------------------*/
		else{
			criteria.andCondition(condition, value);
		}
		
		return criteria;
		
	}
	
	
	
	
	/**
	 * 
	 *	手写左边条件，右边用value值
	 *  -------------------------------------------------------- 
	 *  等于 
	 * 	例：select * from app_user where id = 10
	 *  调用： addFilter("id=",10)
	 *  
	 *  -------------------------------------------------------- 
	 *  大于
	 * 	例：select * from app_user where id > 10
	 *  调用： addFilter("id>",10)
	 *  
	 *  -------------------------------------------------------- 
	 *  小于
	 * 	例：select * from app_user where id < 10
	 *  调用： addFilter("id<",10)
	 *  
	 *  -------------------------------------------------------- 
	 *  不等于
	 * 	例：select * from app_user where id != 10
	 *  调用： addFilter("id!=",10)
	 *  
	 *  -------------------------------------------------------- 
	 *  like
	 *  例：select * from app_user where username like '%小明%'
	 *  调用：addFilter("username_like",'%小明%');
	 *  调用：addFilter("username_like",'%小明');
	 *  调用：addFilter("username_like",'小明%');
	 *  
	 *  -------------------------------------------------------- 
	 *  in  两种参数  
	 *  	   一、   "1,2,3"  字符串
	 *  	   二    List<String>  list = new ArrayList<String>()      String  可  换Long,Integer等
	 *  	    list.add("1");
	 *  		list.add("2") 
	 *  例：select * from app_user where id in ("1,2,3")
	 *  调用：addFilter("id_in","1,2,3");
	 *  调用:addFilter("in_in",list);
	 *  
	 *  -------------------------------------------------------- 
	 *  
	 * @author:         Liu Shilei
	 * @param:    @return
	 * @return: QueryFilter 
	 * @Date :          2016年3月14日 下午1:21:20   
	 * @throws:
	 */
	public RemoteQueryFilter addFilter(String condition, Object value){
		//addCriteria(condition,value,criteria);
		filterMap.put(condition, value);
		return this;
	}
	
	
	/**
	 * <p> TODO</p>
	 * @return:     Integer
	 */
	public Integer getPage() {
		return page;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Integer
	 */
	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Integer
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Integer
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Integer
	 */
	public Integer getDraw() {
		return draw;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Integer
	 */
	public void setDraw(Integer draw) {
		this.draw = draw;
	}


	/**
	 * orderby  
	 * 		例：select * from app_user order by username asc
	 * 		
	 * 		调用：setOrderby("username asc");
	 * 
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param orderby
	 * @return: void 
	 * @Date :          2016年3月17日 上午11:32:10   
	 * @throws:
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	public String getOrderBy() {
		return orderBy;
	}



	/**
	 * <p> TODO</p>
	 * @return:     HttpServletRequest
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/** 
	 * <p> TODO</p>
	 * @return: HttpServletRequest
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Example
	 */
	public Example getExample() {
		if(this.nosaas){
			if(saasId!=null&&!"".equals(saasId)){
				criteria.andCondition("saasId=",saasId);
			}else{
				//增加saasId查询条件
				criteria.andCondition("saasId=", ContextUtil.getSaasId());
			}
		}
		return example;
	}
	
	/**
	 * 设置为nosaas
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @return
	 * @return: QueryFilter 
	 * @Date :          2016年3月15日 上午10:25:40   
	 * @throws:
	 */
	public RemoteQueryFilter setNosaas() {
		this.nosaas = false;
		return this;
	}

	
}
