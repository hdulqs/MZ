/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年4月29日 下午3:40:00
 */
package com.mz.web.article.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.web.app.model.AppCategory;
import com.mz.web.app.model.AppCategoryVo;
import java.util.List;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年4月29日 下午3:40:00
 */
public interface AppCategoryDao extends BaseDao<AppCategory, Long> {
	
	public List<AppCategoryVo> findFooterCategory();
	
	
}
