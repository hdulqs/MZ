/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年6月16日 下午6:39:57
 */
package com.mz.oauth.remote.user;

import com.mz.oauth.user.model.AppRoleMenuTree;
import com.mz.shiro.service.AppRoleMenuTreeService;
import java.util.List;

import com.mz.util.QueryFilter;

import javax.annotation.Resource;

import com.alibaba.dubbo.rpc.RpcContext;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年6月16日 下午6:39:57 
 */
public class RemoteAppRoleMenuTreeServiceImpl implements RemoteAppRoleMenuTreeService {
	
	@Resource
	private AppRoleMenuTreeService appRoleMenuTreeService;
	
	@Override
	public boolean delete(List<Long> ids) {
		
		String saasId = RpcContext.getContext().getAttachment("saasId");
		if(ids!=null&&ids.size()>0){
			for(Long id : ids){
				QueryFilter queryFilter = new QueryFilter(AppRoleMenuTree.class);
				queryFilter.setSaasId(saasId);
				queryFilter.addFilter("menuTreeId=", id);
				appRoleMenuTreeService.delete(queryFilter);
			}
		}
		return true;
	}

}
