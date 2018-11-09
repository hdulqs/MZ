/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年09月28日  18:10:04
 */
package com.mz.web.file.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.web.file.model.AppFileRelation;
import java.util.List;
import java.util.Map;

/**
 * 
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年6月20日 下午3:34:00
 */
public interface AppFileRelationDao extends BaseDao<AppFileRelation,Long>{

	/**
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @return
	 * @return: List<AppFileRelation> 
	 * @Date :          2016年6月21日 下午5:11:31   
	 * @throws:
	 */
	List<AppFileRelation> findGroup();

	/**
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param map
	 * @param:    @return
	 * @return: List<AppFileRelation> 
	 * @Date :          2016年6月23日 上午11:47:01   
	 * @throws:
	 */
	List<AppFileRelation> findByOrgId(Map<String, Object> map);
	
}