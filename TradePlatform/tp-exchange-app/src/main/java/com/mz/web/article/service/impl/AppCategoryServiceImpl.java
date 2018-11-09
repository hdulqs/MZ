package com.mz.web.article.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppCategory;
import com.mz.web.article.service.AppCategoryService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service("appCategoryService")
public class AppCategoryServiceImpl extends BaseServiceImpl<AppCategory, Long>
		implements AppCategoryService {

	@Resource(name = "appCategoryDao")
	@Override
	public void setDao(BaseDao<AppCategory, Long> dao) {
		super.dao = dao;
	}

	@Override
	public List<AppCategory> selectlist(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(AppCategory.class, request);
		filter.addFilter("state=", 0);
		String website = request.getParameter("website");
		filter.addFilter("website=", website);
		List<AppCategory> find = this.find(filter);
		if(find!=null&&find.size()>0){
			for(AppCategory ac : find){
				digui(ac);
			}
		}
		return find;
	}
	
	public void digui(AppCategory ac){
		
		if(ac.getPreateId().intValue()!=0){
				AppCategory parent = this.get(ac.getPreateId());
				if(parent!=null){
					ac.setName(parent.getName()+"--"+ac.getName());
				}
			
		}
		
	}
	

}
