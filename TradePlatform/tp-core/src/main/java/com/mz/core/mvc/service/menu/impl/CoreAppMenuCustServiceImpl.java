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
import com.mz.util.BeanUtil;
import com.mz.util.QueryFilter;
import com.mz.web.menu.model.AppMenu;
import com.mz.web.menu.model.AppMenuCust;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Liu Shilei
 * @Date : 2015年10月13日 上午10:23:31
 */
@Service("coreAppMenuCustService")
public class CoreAppMenuCustServiceImpl extends BaseServiceImpl<AppMenuCust, Long> implements CoreAppMenuCustService {

	@Resource(name = "coreAppMenuCustDao")
	@Override
	public void setDao(BaseDao<AppMenuCust, Long> dao) {
		super.dao = dao;
	}
	
	@Resource
	private CoreAppMenuService coreAppMenuService;
	
	@Override
	public void init(List<AppMenu> appMenuList,String appName) {

		List<AppMenu> listAll = appMenuList;
		// ---------------------------------AppMenuCustList 和 AppMenuList比较-----------------------------------------------------
		QueryFilter filter = new QueryFilter(AppMenu.class);
		filter.addFilter("appName=", appName);

		// 数据中原有的appmenucust
		List<AppMenuCust> oldList = this.find(filter);

		//更新过的所有key值
		List<String> updateKey = new ArrayList<String>();
		
		//更新过的menuKey值
		List<String> menuKey = new ArrayList<String>();
		
		/**
		 * 说明
		 * 	  一丶遍历全部的appMenuCust
		 *   二丶比较appMeunCust 与appMenu  mkey值想匹配则更新
		 *   三丶如果appMenuCust在 appMenu list中找不到则删除appMenuCust
		 */
		for (AppMenuCust appMenuCust : oldList) {
			boolean isHas = false;
			for (int i = 0; i < listAll.size(); i++) {
				//如果appMenuCust的mkey 与新增的appMenu匹配上 ---------更新
				AppMenu appMenu = listAll.get(i);
				if (appMenuCust.getMkey().equals(appMenu.getMkey())) {
					isHas = true;
					appMenu.setId(null);
					//isVisible不被更新
					String isVisible = appMenuCust.getIsVisible();
					BeanUtil.copyNotNullProperties(appMenu, appMenuCust);
					appMenuCust.setIsVisible(isVisible);
					this.update(appMenuCust);
					//记录被更新过的key值
					updateKey.add(appMenu.getMkey());
					//记录被更新过的menu  key值
					if("menu".equals(appMenuCust.getType())){
						menuKey.add(appMenuCust.getMkey());
					}
					break;
				}
			}
			// 如果数据库中的和menu.xml中的key值匹配不上----------- 执行删除
			if (!isHas) {
				// 此处注释，防止多人同时开发同一个项目时menu不对称
				dao.deleteByPrimaryKey(appMenuCust.getId());
			}
		}
		
		/**
		 * 遍历所有更新过key值
		 *    更新每个menu下的function
		 */
		for(String key : menuKey){
			QueryFilter queryFilter = new QueryFilter(AppMenu.class);
			queryFilter.addFilter("pkey=", key);
			List<AppMenu> amList = coreAppMenuService.find(queryFilter);
			
			QueryFilter queryFilter2 = new QueryFilter(AppMenuCust.class);
			queryFilter2.addFilter("pkey=", key);
			List<AppMenuCust> amcList = super.find(queryFilter2);
			
			for(AppMenu appMenu : amList){
				boolean isHas = false;//标记AppMenuCust中是否拥有
				for(AppMenuCust appMenuCust : amcList){
					if(appMenu.getMkey().equals(appMenuCust.getMkey())){
						isHas = true;
					}
				}
				
				//如果isHas = false 则添加至AppMenuCust
				if(!isHas){
					AppMenuCust  _appMenuCust = new AppMenuCust();
					BeanUtil.copyNotNullProperties(appMenu, _appMenuCust);
					_appMenuCust.setId(null);
					_appMenuCust.setIsVisible("1");//新增的默认设置为客户应用菜单不可见
					super.save(_appMenuCust);
				}
			}
			
		}
		


	}

}
