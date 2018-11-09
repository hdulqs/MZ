/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月14日 上午11:48:37
 */
package com.mz.util;



import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * <p>mybatis通用查询组件</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月14日 上午11:48:37 
 */
public class QueryFilter implements  Serializable{
	private Example example = null;
	private Criteria criteria = null;
	
	private Integer page;// 要查找第几页
	private Integer pageSize;// 每页显示多少条
	
	private Integer draw;//dataTable的请求标记数

	private HttpServletRequest request;
	
	private boolean nosaas = true;
	
	private String saasId;
	

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
	private QueryFilter(){}
	
	/**
	 * 构造封装 Example 对象
	 * 
	 * QueryFilter 默认Saas模式
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param entityClass
	 */
	public QueryFilter(Class<?> entityClass){
		//初始化example对象
		example = new Example(entityClass);
		//创建查询条件对象
		criteria = example.createCriteria();
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
//      dataTable 参数示例:
//		columns[0][data]	id
//		columns[0][name]	
//		columns[0][orderable]	true
//		columns[0][search][regex]	false
//		columns[0][search][value]	
//		columns[0][searchable]	true
		/*order[0][column]	5
		order[0][dir]	desc*/
		
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
	public QueryFilter or(String condition,String value){
		Criteria newcriteria = example.createCriteria();
		example.or(addCriteria(condition, value,newcriteria));
		return this;
	}
	
	
	/**
	 * Remote调用时封装的QueryFilter
	 * 
	 * 主要自动封装dataTable参数
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param entityClass
	 * @param:    @param request
	 */
	public QueryFilter(Class<?> entityClass,HashMap<String, String> requestMap){
		//初始化example对象
		this.example = new Example(entityClass);
		//创建查询条件对象
		this.criteria = example.createCriteria();
		
		if(requestMap!=null&&requestMap.size()>0){
			
			Set<Entry<String, String>> entrySet = requestMap.entrySet();
			Iterator<Entry<String, String>> iterator = entrySet.iterator();
			
			while (iterator.hasNext()) {
				Entry<String, String> next = iterator.next();
				String name = next.getKey();
				String value = next.getValue();
				
				if (StringUtils.isNotBlank(value)) {

				//	addFilter(name, value);
					
					/**
					 * dataTable分页数据
					 */
					if (name.equalsIgnoreCase("start")) {
						Integer start=Integer.parseInt(value);
						String length = requestMap.get("length");
						this.setPage(start/Integer.parseInt(length)+1);
					}
					else if (name.equalsIgnoreCase("length")) {
						this.setPageSize(Integer.parseInt(value));
					}
					//dataTable 请求次数
					else if(name.equalsIgnoreCase("draw")){
						this.setDraw(Integer.parseInt(value));
					}
					//dataTable 自动like查询
					else if(name.contains("_like")||name.contains("_LIKE")){
						String[] strArr = name.split("_");
						try {
							criteria.andLike(strArr[0], "%"+value+"%");
						} catch (RuntimeException e) {
						}
					}
					//其它正常处理
					else if(name.contains("_")){
						try {
							addCriteria(name, value,criteria);
						} catch (RuntimeException e) {
						}
					}
					
					
					//dataTable 自动按列排序
				//	orderBy(request, name, value);
					
					
				}
			}
			
		}
		
	
		
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
	public QueryFilter(Class<?> entityClass,HttpServletRequest request){
		this.request = request;
		//初始化example对象
		this.example = new Example(entityClass);
		//创建查询条件对象
		this.criteria = example.createCriteria();
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = StringUtils.trim(request.getParameter(name));
			
			if (StringUtils.isNotBlank(value)) {

			//	addFilter(name, value);
				
				/**
				 * dataTable分页数据
				 */
				if (name.equalsIgnoreCase("start")) {
					Integer start=Integer.parseInt(value);
					String length = request.getParameter("length");
					this.setPage(start/Integer.parseInt(length)+1);
				}
				else if (name.equalsIgnoreCase("length")) {
					this.setPageSize(Integer.parseInt(value));
				}
				//dataTable 请求次数
				else if(name.equalsIgnoreCase("draw")){
					this.setDraw(Integer.parseInt(value));
				}
				//dataTable 自动like查询
				else if(name.contains("_like")||name.contains("_LIKE")){
					
					String[] strArr = name.split("_");
					try {
						criteria.andLike(strArr[0], "%"+value+"%");
					} catch (RuntimeException e) {
					}
				}
				//其它正常处理
				else if(name.contains("_")){
					try {
						addCriteria(name, value,criteria);
					} catch (RuntimeException e) {
					}
				}
				
				
				//dataTable 自动按列排序
				orderBy(request, name, value);
				
				
			}
		}
	
		
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
		/*-------------------------------------notlike处理-------------------------------------------------*/
		if(condition.contains("_notlike")||condition.contains("_NOTLIKE")){
			String[] split = condition.split("_");
			criteria.andNotLike(split[0], value+"");
		}
		/*-------------------------------------like处理-------------------------------------------------*/
		else if(condition.contains("_like")||condition.contains("_LIKE")){
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
			
		}/*-------------------------------------not in处理-------------------------------------------------*/
		else if(condition.contains("_notin")||condition.contains("_NOTIN")){
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
				criteria.andCondition(" "+split[0]+" "+"not in" +" ( " +sb.toString() + " )");
			}else{
				criteria.andNotIn(split[0], (List)value);
			}
		}/*-------------------------------------is null处理-------------------------------------------------*/
		else if(condition.contains("_isnull")||condition.contains("_ISNULL")){
			String[] split = condition.split("_");
			criteria.andIsNull(split[0]);
		}/*-------------------------------------is not null处理-------------------------------------------------*/
		else if(condition.contains("_isnotnull")||condition.contains("_ISNOTNULL")){
			String[] split = condition.split("_");
			criteria.andIsNotNull(split[0]);
		}
		/*-------------------------------------【_EQ】【_GT】【_LT】【_NEQ】处理-------------------------------------------------*/
		/**
		 *        【_EQ】              =
		 *        【_GT】              >
		 *        【_LT】             <
		 *        【_NEQ】          !=
		 *        【_GEQ】          >=
		 *        【_LEQ】          <=
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
		}else if(condition.contains("_GET")){
			String[] strArr = condition.split("_");
			criteria.andCondition(strArr[0]+">=", value);
		}else if(condition.contains("_LET")){
			String[] strArr = condition.split("_");
			criteria.andCondition(strArr[0]+"<=", value);
		}
		/*-------------------------------------【=】【>】【<】【!=】【>=】【<=】处理-------------------------------------------------*/
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
	public QueryFilter addFilter(String condition, Object value){
		addCriteria(condition,value,criteria);
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
	public void setOrderby(String orderby) {
		this.example.setOrderByClause(orderby);
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
	public QueryFilter setNosaas() {
		this.nosaas = false;
		return this;
	}

	
}
