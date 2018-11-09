/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年4月14日 上午10:55:52
 */
package com.mz.mongo.util;

import java.io.Serializable;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * <p>MongoQueryFilter 实现序列化，和克隆接口
 * 	  因为mongo每查询完成时会销毁Query对象，导致Query不能重用 
 * </p>
 * @author:         Liu Shilei 
 * @Date :          2016年4月14日 上午10:55:52 
 */
public class MongoQueryFilter implements  Serializable,Cloneable   {
	
	
	private Query query = null;
	
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
	 * 构造封装 Example 对象
	 * 
	 * QueryFilter 默认Saas模式
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param entityClass
	 */
	public MongoQueryFilter(){
		//初始化Query对象
		query = new Query();
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
					
					if("desc".equals(value)){
						this.query.with(new Sort(Direction.DESC, data));
					}else{
						this.query.with(new Sort(Direction.ASC, data));
					}
					
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
	public MongoQueryFilter(HttpServletRequest request){
		//初始化Query对象
		query = new Query();
		
		this.request = request;
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = StringUtils.trim(request.getParameter(name));
			
			if (StringUtils.isNotBlank(value)) {
				
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
				//其它正常处理
				else if(name.contains("_")){
					addCriteria(name, value);
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
	private void addCriteria(String condition, Object value){
		/*-------------------------------------like处理-------------------------------------------------*/
		if(condition.contains("_like")||condition.contains("_LIKE")){
			String[] split = condition.split("_");
			query.addCriteria(Criteria.where(split[0]).regex(value+""));
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
				//criteria.andCondition(" "+split[0]+" "+"in" +" ( " +sb.toString() + " )");
				
				//query.addCriteria(Criteria.where(split[0]).in());
				
			}else{
				//criteria.andIn(split[0], (List)value);
			}
		}
		/*-------------------------------------【_EQ】【_GT】【_LT】【_NEQ】处理-------------------------------------------------*/
		/**
		 *        【_EQ】              =
		 *        【_GT】              >
		 *        【_LT】             <
		 *        【_NEQ】          !=
		 *        
		 *        【=】【>】【<】【!=】处理-
		 */
		else if(condition.contains("_EQ")){
			String[] strArr = condition.split("_");
			query.addCriteria(Criteria.where(strArr[0]).is(value+""));
		}else if(condition.contains("=")){
			query.addCriteria(Criteria.where(condition.replace("=", "")).is(value+""));
		}else if(condition.contains("_GT")){
			String[] strArr = condition.split("_");
			query.addCriteria(Criteria.where(strArr[0]).gt(value+""));
		}else if(condition.contains(">")){
			query.addCriteria(Criteria.where(condition.replace(">", "")).is(value+""));
		}else if(condition.contains("_LT")){
			String[] strArr = condition.split("_");
			query.addCriteria(Criteria.where(strArr[0]).lt(value+""));
		}else if(condition.contains("<")){
			query.addCriteria(Criteria.where(condition.replace("<", "")).is(value+""));
		}else if(condition.contains("_NEQ")){
			String[] strArr = condition.split("_");
			query.addCriteria(Criteria.where(strArr[0]).ne(value+""));
		}else if(condition.contains("!=")){
			query.addCriteria(Criteria.where(condition.replace("!=", "")).is(value+""));
		}
		
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
	public MongoQueryFilter addFilter(String condition, Object value){
		addCriteria(condition,value);
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
	 * 		调用：setOrderby("username","asc");
	 * 
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param orderby
	 * @return: void 
	 * @Date :          2016年3月17日 上午11:32:10   
	 * @throws:
	 */
	public void setOrderby(String column,String sort) {
		if("desc".equals(sort)){
			this.query.with(new Sort(Direction.DESC, column));
		}else{
			this.query.with(new Sort(Direction.ASC, column));
		}
		
	}
	
	/**
	 * limit
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param start
	 * @param:    @param end
	 * @return: void 
	 * @Date :          2016年4月19日 上午11:30:49   
	 * @throws:
	 */
	public void limit(int start,int end){
		this.query.skip(start);
		this.query.limit(end);
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
	 * 设置为nosaas
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @return
	 * @return: QueryFilter 
	 * @Date :          2016年3月15日 上午10:25:40   
	 * @throws:
	 */
	public MongoQueryFilter setNosaas() {
		this.nosaas = false;
		return this;
	}

	/**
	 * <p>获得Query</p>
	 * @author:         Liu Shilei
	 * @param:    @return
	 * @return: Query 
	 * @Date :          2016年4月14日 下午2:00:46   
	 * @throws:
	 */
	public Query getQuery() {
//	     去掉saasId   查询条件   add by liu shilei 2016/07/04
/*		try {
		    if(this.nosaas){
				if(saasId!=null&&!"".equals(saasId)){
					query.addCriteria(Criteria.where("saasId").is(saasId));   //手动设置
				}else{
					//增加saasId查询条件  ,自动从session中获取
					query.addCriteria(Criteria.where("saasId").is(ContextUtil.getSaasId()));
				}
			}
		} catch (InvalidMongoDbApiUsageException e) {
		}*/
		return query;
		
	}
	
	
	
	@Override  
    public Object clone()
    {  
        try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}  
        return null;
    } 
	
	
}
