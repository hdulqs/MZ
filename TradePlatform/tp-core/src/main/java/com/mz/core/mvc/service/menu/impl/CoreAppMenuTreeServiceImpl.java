/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月13日 上午10:23:31
 */
package com.mz.core.mvc.service.menu.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.core.mvc.service.menu.CoreAppMenuCustService;
import com.mz.core.mvc.service.menu.CoreAppMenuService;
import com.mz.core.mvc.service.menu.CoreAppMenuTreeService;
import com.mz.core.mvc.service.menu.CoreAppRoleMenuTreeService;
import com.mz.util.QueryFilter;
import com.mz.oauth.user.model.AppRoleMenuTree;
import com.mz.web.menu.model.AppMenu;
import com.mz.web.menu.model.AppMenuTree;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年10月13日 上午10:23:31 
 */
@Service("coreAppMenuTreeService")
public class CoreAppMenuTreeServiceImpl extends BaseServiceImpl<AppMenuTree, Long>  implements CoreAppMenuTreeService{

	@Resource(name = "coreAppMenuTreeDao")
	@Override
	public void setDao(BaseDao<AppMenuTree, Long> dao) {
		super.dao = dao;
	}
	
	@Resource
	private CoreAppRoleMenuTreeService coreAppRoleMenuTreeService;
	
	@Autowired
	private CoreAppMenuService coreAppMenuService;
	
	@Autowired
	private CoreAppMenuCustService coreAppMenuCustService;
	
	@Override
	public void init(List<AppMenu> appMenuList , String appName) {


		List<AppMenu> listAll = appMenuList;
		// ---------------------------------AppMenuCustList 和 AppMenuList比较-----------------------------------------------------

		// 数据中原有的AppMenuTree
		List<AppMenuTree> oldList = this.findAll();

		// 更新过的key值
		List<String> updateKey = new ArrayList<String>();
		
		//更新过的menu的key值集合
		List<String> menuKey = new ArrayList<String>();
		
		// --------------------遍历所有appMenuTree与appMenu进行比较，如果相同则更新，不存在删除
		for (AppMenuTree appMenuTree : oldList) {
			//如果appMenuTree Type =root 或menus 不进行比较操作
			if("root".equals(appMenuTree.getType())||"menus".equals(appMenuTree.getType())){
				continue;
			}
			//标记是否被更新过
			boolean isHas = false;
			for (int i = 0; i < listAll.size(); i++) {
				// 如果数据库中的和menu.xml中的key值匹配上 ---------更新
				AppMenu appMenu = listAll.get(i);
				if(appMenuTree.getOkey()==null){
					isHas = true;
					break;
				}
				if (appMenuTree.getOkey().equals(appMenu.getMkey())) {
					isHas = true;
					
					//appmenuTree只更新shiroUrl和Url
					appMenuTree.setShiroUrl(appMenu.getShiroUrl());
					appMenuTree.setUrl(appMenu.getUrl());
					this.update(appMenuTree);

					// 被更新过的key值
					updateKey.add(listAll.get(i).getMkey());
					
					//记录被更新过的menu  key值
					if("menu".equals(appMenu.getType())){
						menuKey.add(appMenu.getMkey());
					}
					
					break;
				}
			}
			// 如果数据库中的和menu.xml中的key值匹配不上----------- 执行删除
			if (!isHas) {
				//删除自身
				// 此处注释，防止多人同时开发同一个项目时menu不对称
				long menuId = appMenuTree.getId();
				dao.deleteByPrimaryKey(appMenuTree.getId());
				//再删除角色关系表
				QueryFilter queryFilter = new QueryFilter(AppRoleMenuTree.class);
				queryFilter.addFilter("menuTreeId=", menuId);
				coreAppRoleMenuTreeService.delete(queryFilter);
			}
		}
	}

}
