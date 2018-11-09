/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年1月7日 上午10:15:50
 */
package com.mz.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.sql.Criteria;

/**
 * <p> TODO</p>  QueryFilter  和SqlFilter共同接口
 * @author:         Liu Shilei 
 * @Date :          2016年1月7日 上午10:15:50 
 */
public interface Filter  {


	public HttpServletRequest getRequest();

	public void setRequest(HttpServletRequest request);
	
	public String[] getJoinFetch();

	public void setJoinFetch(String[] joinFetch);

	public String[] getJoin() ;

	public void setJoin(String[] join);

	public void setParams(List<Object[]> params) ;

	public String getSort() ;

	public void setSort(String sort) ;
	
	public Integer getDraw();

	public void setDraw(Integer draw);

	public Integer getPage();

	public void setPage(Integer page);

	public Integer getPageSize();

	public void setPageSize(Integer pageSize);
	
	public String getOrderby();
	
	public void setOrderby(String orderby);

	
	
	public void orderBy(HttpServletRequest request,String name ,String value);
	
	/**
	 * 添加一个条件过滤
	 * 
	 * 如果name是Q_开头，并且参数可以分为3段或者4段，才添加过滤
	 * 
	 * 第二段是要过滤的属性，第三段是操作符，第四段是参数类型
	 * 
	 * @param name
	 * @param value
	 */
	public void addFilter(String name, String value) ;

	public List<Object[]> getParams() ;

	public Criteria getCondition();


}
