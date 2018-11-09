/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0 
 * @Date:        2015年9月16日 上午11:04:39
 */
package com.mz.core.mvc.service.base;

import com.github.pagehelper.Page;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.util.QueryFilter;
import java.io.Serializable;
import java.util.List;

/**
 * 基础Service
 * 
 * 
 * 
 * @author Yuan Zhicheng
 *
 * @param <T>
 * @param <PK>
 */
public interface BaseService<T extends Serializable, PK extends Serializable> {
	
	//人民币账户
	public final static String  App_account = "com.mz.account.fund.model.AppAccount";
	//数据货币账户 
	public final static String Ex_account = "com.mz.exchange.account.model.ExDigitalmoneyAccount";
	//产品表
	public final static String Ex_product = "com.mz.exchange.product.model.ExProduct";
	
	


	/**
	 * 保存一个对象
	 * 
	 * @param t
	 * @return
	 */
	public Serializable save(T t);

	/**
	 * 保存一批对象
	 * 
	 * @param l
	 */
	public void saveAll(List<T> l);
	
	
	/**
	 * 根据主键删除对象
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param t
	 * @return: void 
	 * @Date :          2016年3月17日 下午5:19:44   
	 * @throws:
	 */
	public boolean delete(PK pk);
	
	
	/**
	 * 按条件删除
	 * 删除对象
	 * 
	 * @param filter
	 */
	public boolean delete(QueryFilter filter); 

	/**
	 * 修改一个对象,不更新null值
	 * 
	 * @param t
	 */
	public void update(T t);
	
	/**
	 * 修改一个对象,更新null值
	 * 
	 * @param t
	 */
	public void updateNull(T t);

	/**
	 * 获得一个对象
	 * 
	 * @param pk
	 *            主键
	 * @return
	 */
	public T get(PK pk);

	/**
	 * 获得第一个对象
	 * 
	 * @param filter
	 * @return
	 */
	public T get(QueryFilter filter);

	

	/**
	 * 查找所有
	 * 
	 * @return
	 */
	public List<T> findAll();
	

	/**
	 * 查找所有符合条件的
	 * 
	 * @param filter
	 * @return
	 */
	public List<T> find(QueryFilter filter);
	
	

	/**
	 * 统计所有
	 * 
	 * @return
	 */
	public Long count(T t);
	

	/**
	 * 统计符合条件的
	 * 
	 * @param filter
	 * @return
	 */
	public Long count(QueryFilter filter);
	
	
	/**  单表分页方法
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param filter
	 * @return: void 
	 * @Date :          2016年3月17日 上午10:46:29   
	 * @throws:
	 */
	public Page<T> findPage(QueryFilter filter);
	
	
	/**  单表分页方法 ,返回pageResult
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param filter
	 * @return: void 
	 * @Date :          2016年3月17日 上午10:46:29   
	 * @throws:
	 */
	public PageResult findPageResult(QueryFilter filter);

	/**
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param ids
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2016年6月6日 下午6:01:17   
	 * @throws:
	 */
	public boolean deleteBatch(String ids);

	/**
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param ids
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2016年6月16日 下午7:08:48   
	 * @throws:
	 */
	public boolean deleteBatch(List<Long> ids);

	
	

}
