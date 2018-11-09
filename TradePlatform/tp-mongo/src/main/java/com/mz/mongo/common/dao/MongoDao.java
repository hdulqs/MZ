/**
 * Copyright:   北京互融时代软件有限公司
 * @author:        Wu Shuiming
 * @version:      V1.0 
 * @Date :    2016年3月7日  下午6:35:36
 */
package com.mz.mongo.common.dao;

import java.io.Serializable;
import java.util.List;

import com.mongodb.WriteResult;

/**
 *类描述：
 *@author: wu shuiming
 *@date： 日期：2016年3月7日        时间：下午6:35:36
 *@version 1.0
 */
public interface MongoDao<T,PK> {
	
	/**
	 * 保存方法
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param t
	 * @return: void 
	 * @Date :          2016年4月12日 下午5:14:18   
	 * @throws:
	 */
	public void save(T t);
	
	/**
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param valueOf
	 * @return: void 
	 * @Date :          2016年4月13日 上午11:16:14   
	 * @throws:
	 */
	public T get(PK pk);
	
	
	public List<T> findAll();

	public T findById(Serializable id);

	public WriteResult updateTree(Serializable id , String property,  Object value);

	public void dopCollection();

	void delById(T t);



	
}
