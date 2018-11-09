/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年09月28日  18:10:04
 */
package com.mz.web.file.service.impl;


import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.web.file.model.AppFileRelation;
import com.mz.web.file.dao.AppFileRelationDao;
import com.mz.web.file.service.AppFileRelationService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年6月20日 下午3:36:27
 */
@Service("appFileRelationService")
public class AppFileRelationServiceImpl extends BaseServiceImpl<AppFileRelation, Long> implements AppFileRelationService{
	
	@Resource(name = "appFileRelationDao")
	@Override
	public void setDao(BaseDao<AppFileRelation, Long> dao) {
		super.dao = dao;
	}

	@Override
	public List<AppFileRelation> findGroup() {
		return ((AppFileRelationDao)dao).findGroup();
	}

	@Override
	public List<AppFileRelation> findByOrgId(HttpServletRequest request) {
		String orgId = request.getParameter("orgId");
		if(!StringUtils.isEmpty(orgId)){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("orgId", orgId);
			
			String fileType = request.getParameter("fileType");
			String fileName = request.getParameter("fileName");
			if(!StringUtils.isEmpty(fileType)){
				//fileType转成对应的文件类型
				if("img".equals(fileType)){
					map.put("fileType", "image");
				}else if("file".equals(fileType)){
					map.put("fileType", "NOLIKE_image");
				}
			}
			if(!StringUtils.isEmpty(fileName)){
				map.put("fileName", fileName);
			}
			
			return ((AppFileRelationDao)dao).findByOrgId(map);
		}
		return null;
	}


	

	
}