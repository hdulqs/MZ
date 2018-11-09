/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年09月28日  18:10:04
 */
package com.mz.web.file.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.file.model.AppFileRelation;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.web.file.service.AppFileRelationService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年6月21日 下午5:08:43
 */
@Controller
@RequestMapping("/file/appfilerelation")
public class AppFileRelationController extends BaseController<AppFileRelation, Long> {
	@Resource(name = "appFileRelationService")
	@Override
	public void setService(BaseService<AppFileRelation, Long> service) {
		super.service = service;
	}

	@MethodName(name = "列表")
	@RequestMapping("/findGroup")
	@ResponseBody
	public List<AppFileRelation> findGroup(HttpServletRequest request) {
		return ((AppFileRelationService)service).findGroup();
	}
	
	@MethodName(name = "list查询")
	@RequestMapping("/list")
	@ResponseBody
	public List<AppFileRelation> list(HttpServletRequest request){
		String orgId = request.getParameter("orgId");
		if(!StringUtils.isEmpty(orgId)){
			QueryFilter filter = new QueryFilter(AppFileRelation.class);
			filter.addFilter("orgId=", Long.valueOf(orgId));
			return super.find(filter);
		}
		return null;
	} 
	
	
	@MethodName(name = "list查询")
	@RequestMapping("/findByOrgId")
	@ResponseBody
	public List<AppFileRelation> findByOrgId(HttpServletRequest request){
		return ((AppFileRelationService)service).findByOrgId(request);
	} 
	
	
	
}
