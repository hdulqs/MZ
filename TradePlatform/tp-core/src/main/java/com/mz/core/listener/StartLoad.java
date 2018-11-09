/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年5月11日 下午7:19:58
 */
package com.mz.core.listener;

import com.mz.core.annotation.NoLogin;
import com.mz.web.menu.model.AppMenu;
import com.mz.core.mvc.service.menu.CoreAppMenuCustService;
import com.mz.core.mvc.service.menu.CoreAppMenuService;
import com.mz.core.mvc.service.menu.CoreAppMenuTreeService;
import com.mz.util.QueryFilter;
import com.mz.util.ReadClassUtil;
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>基础启动项</p>
 * @author:         Liu Shilei 
 * @Date :          2016年5月11日 下午7:19:58 
 */
public class StartLoad {
	
	public final static String STARTUPMANAGE_P = "com.mz.";
	public final static String STARTUPMANAGE_S = ".listener.StartupManage";
	public final static String START="start";
	
	/**
	 * 反射加载
	 */
	public static void reflectLoad(String loadApp){
		if(StringUtils.isEmpty(loadApp)){
		}else{
			String[] appNames = StringUtils.split(loadApp,",");
			String appName=null;
			for (int i = 0; i < appNames.length; i++) {
				appName = appNames[i];
				Class clazz = null;
				try {
					clazz = Class.forName(STARTUPMANAGE_P+appName+STARTUPMANAGE_S);
					if (clazz != null) {
						LogFactory.info("服务启动中..............................正在加载"+appName+"应用");
						Object object = clazz.newInstance();
						Method method = clazz.getDeclaredMethod(START);
						method.invoke(object);
						LogFactory.info("服务启动中..............................成功加载"+appName+"应用");
					}else{
						LogFactory.info("服务启动中..............................加载"+appName+"应用不存在");
					}
				} catch (Exception e) {
				}
			}
		}
		
	}
	
	
	/**
	 * 读menu.xml加载菜单和权限
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param appName
	 * @param:    @param saasId
	 */
	public static void loadBase(String appName,String appKey ,String saasId){
		/**
		 * -------------------加载数据到当前平台---------------------
		 * --没有saasId情况下的appMenu加载--
		 * 2016年3月25日14:55:41
		 */
		//初始化左侧菜单栏
		CoreAppMenuService coreAppMenuService = (CoreAppMenuService)ContextUtil.getBean("coreAppMenuService"); 
		coreAppMenuService.init(appName,appKey);
		//初始化权限列表  权限已改造到app_menu  app_menu_cust  app_menu_tree
		
		//根据appMenu List 初始化权限
		QueryFilter filter = new QueryFilter(AppMenu.class);
		filter.setSaasId(saasId);
		List<AppMenu> appMenuList = coreAppMenuService.find(filter);
		
		//初始化权限列表   权限已改造到app_menu  app_menu_cust  app_menu_tree
		
		//appMenuCust对应的修改
		CoreAppMenuCustService coreAppMenuCustService = (CoreAppMenuCustService) ContextUtil.getBean("coreAppMenuCustService");
		coreAppMenuCustService.init(appMenuList,appName);
		
		//appMenutree对应的修改
		CoreAppMenuTreeService  coreAppMenuTreeService = (CoreAppMenuTreeService) ContextUtil.getBean("coreAppMenuTreeService");
		coreAppMenuTreeService.init(appMenuList,appName);
		
		/**
		 * ---------------------加载数据到当前平台end-------------------------------------
		 */
	}
	
	
	
	/**
	 * 加载所有带有NoLogin注解的Controller方法
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param noLoginSet
	 * @return: void 
	 * @Date :          2016年5月12日 上午9:35:51   
	 * @throws:
	 */
	public static void loadNoLoginAnnotations(Set<String> noLoginSet,String appKey){
		
		//读取hry.exmain包下的所有类
		Set<Class<?>> set = ReadClassUtil.getClasses("com.mz."+appKey);
		Iterator<Class<?>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Class<?> clazz = iterator.next();
			//如果是controller类则进行权限资源操作
			if(clazz.getName().contains(".controller.")){
				//获得类的RequestMapping 注解
				RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
				String currentViewPrefix = "";
				if (requestMapping != null && requestMapping.value().length > 0) {
					currentViewPrefix = requestMapping.value()[0];
				}
				
				//如果为/  则置空  防止双//
				if("/".equals(currentViewPrefix)){
					currentViewPrefix = "";
				}
				
				//获得类中全部方法
				Method[] methods = clazz.getDeclaredMethods();
				//循环所有方法
				for(Method method : methods){
					
					NoLogin noLoginAnnotation = method.getAnnotation(NoLogin.class);
					if(noLoginAnnotation!=null){
						RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
						String[] value = methodRequestMapping.value();
						LogFactory.info("加载NoLogin:"+value[0]);
						if(value[0].contains("{")){
							noLoginSet.add(currentViewPrefix+value[0].substring(0, value[0].indexOf("{")));
						}else{
							noLoginSet.add(currentViewPrefix+value[0]);
						}
					}
					
				}
			}
		}
	
		
	}
	
	
	/**
	 * 读menu.xml加载前台菜单
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param appName
	 * @param:    @param saasId
	 */
	public static void loadFrontMenu(String appName,String appKey ,String saasId){
		/**
		 * -------------------加载数据到当前平台---------------------
		 * --没有saasId情况下的appMenu加载--
		 * 2016年3月25日14:55:41
		 */
		//初始化左侧菜单栏
		CoreAppMenuService coreAppMenuService = (CoreAppMenuService)ContextUtil.getBean("coreAppMenuService"); 
		coreAppMenuService.init(appName,appKey);
	}
	
	
}
