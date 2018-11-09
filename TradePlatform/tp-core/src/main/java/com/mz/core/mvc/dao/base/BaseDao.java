/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月10日 上午10:24:13
 */
package com.mz.core.mvc.dao.base;



import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月10日 上午10:24:13 
 */
public interface BaseDao<T,PK> extends BaseMapper<T>,Mapper<T>{

	
}
