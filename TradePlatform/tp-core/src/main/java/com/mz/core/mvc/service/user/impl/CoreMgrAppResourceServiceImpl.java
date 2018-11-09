/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月21日 上午11:42:06
 */
package com.mz.core.mvc.service.user.impl;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.core.mvc.service.menu.CoreMgrAppMenuService;
import com.mz.manage.init.model.MgrAppResource;
import com.mz.core.mvc.service.user.CoreMgrAppResourceService;
import com.mz.util.QueryFilter;
import com.mz.util.ReadClassUtil;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年9月21日 上午11:42:06 
 */
@Service
public class CoreMgrAppResourceServiceImpl extends BaseServiceImpl<MgrAppResource, Long> implements CoreMgrAppResourceService{
	
	@Resource(name="coreMgrAppResourceDao")
	@Override
	public void setDao(BaseDao<MgrAppResource, Long> dao) {
		super.dao = dao;
		
	}
	
	@Autowired
	private CoreMgrAppMenuService coreMgrAppMenuService;
	
	
	/**
	 * 判断一个类中是否有权限标签
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param clazz
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2015年10月20日 下午2:37:58   
	 * @throws:
	 */
	private boolean classHasRequiresPermissions(Class clazz){
		Method[] methods = clazz.getDeclaredMethods();
		for(Method method : methods){
			MyRequiresPermissions myRequiresPermissions = method.getAnnotation(MyRequiresPermissions.class);
			if(myRequiresPermissions!=null){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * <p>递归创建权限目录结构</p>
	 * @author:         Liu Shilei
	 * @param:    @param appName  应用系统名称
	 * @param:    @param className  类名
	 * @param:    @param requestMapping  /user/appuser   路径数组{user,appuser}
	 * @param:    @param pid  父级ID
	 * @param:    @param index  从第一个标记开始
	 * @param:    @param AppResource  上一级权限目录对象
	 * @return: AppResource  返回最后一级目录  权限对象
	 * @Date :          2015年10月20日 上午11:29:35   
	 * @throws:
	 */
	private MgrAppResource initResourceCatalogue(String appName,Class clazz,String[] requestMapping,String pkey,int index,MgrAppResource AppResource){
		String appKey = PropertiesUtils.APP.getProperty("app.key");
		
		
		MgrAppResource _AppResource = null;
		QueryFilter filter = null;
		
		//如果这个类中没有权限标签则返回  不进行权限URL添加
		boolean classHasRequiresPermissions = classHasRequiresPermissions(clazz);
		if(!classHasRequiresPermissions){
			return _AppResource;
		}
		
		//如果   requestMapping = "/"
		if(requestMapping.length==0){
			//则创建到根目录中
			_AppResource = new MgrAppResource();
			_AppResource.setAppName(appName);
			_AppResource.setName(clazz.getSimpleName());
			_AppResource.setClassName(clazz.getName());
			_AppResource.setMethodName("");
			
			_AppResource.setPkey(pkey);
			_AppResource.setMkey(appKey+"/"+clazz.getSimpleName());
			
			_AppResource.setUrl(appKey+"/"+clazz.getSimpleName());
			_AppResource.setSysName(appKey+"/"+clazz.getSimpleName());
			_AppResource.setIsLock("1");
			_AppResource.setIsDelete("0");
		//	dao.save_NOSAAS(_AppResource);
			
			dao.insertSelective(_AppResource);
			
			return _AppResource;
		}
		
		if(index==requestMapping.length){//如果index越界了则跳出递归
			return AppResource;
		}
		String url = appKey+"/";
		//拼接目录url
		for(int i = 1 ; i < requestMapping.length; i++){
			if(i>index){
				break;
			}
			url += requestMapping[i]+"/";
		}
	//	filter = new QueryFilter();
//		filter.addFilter("Q_appName_EQ_String", appName);
//		filter.addFilter("Q_sysName_EQ_String", url);
//		filter.addFilter("Q_url_EQ_String", url);
//      _AppResource = dao.get_NOSAAS(filter);
		
		filter = new QueryFilter(MgrAppResource.class);
		filter.addFilter("appName=", appName);
		filter.addFilter("sysName=", url);
		filter.addFilter("url=", url);
		List<MgrAppResource> list = dao.selectByExample(filter.setNosaas().getExample());
		if(list.size()>0){
			_AppResource = list.get(0);
		}
		
		if(_AppResource==null){
			_AppResource = new MgrAppResource();
			_AppResource.setAppName(appName);
			_AppResource.setName(requestMapping[index]);
			_AppResource.setMethodName("");
			_AppResource.setPkey(pkey);
			_AppResource.setMkey(url);
			_AppResource.setUrl(url);
			_AppResource.setSysName(url);
			_AppResource.setIsLock("1");
			_AppResource.setIsDelete("0");
			//dao.save_NOSAAS(_AppResource);
			dao.insert(_AppResource);
		}
		pkey = _AppResource.getMkey();
		index++;
		AppResource = _AppResource;
		return initResourceCatalogue(appName,clazz,requestMapping,pkey,index,AppResource);
		
	}
	
	
	@Override
	public void init(String appName) {
		String appKey = PropertiesUtils.APP.getProperty("app.key");
		String methodNameName = "";
		
		//读取hry包下的所有类
		Set<Class<?>> set = ReadClassUtil.getClasses("com.mz");
		Iterator<Class<?>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Class<?> clazz = iterator.next();
			//如果是controller类则进行权限资源操作
			if(clazz.getName().contains(".controller.")){
				MgrAppResource paren = null;
				LogFactory.info("----------类名"+clazz.getSimpleName());
				//获得类的RequestMapping 注解
				RequestMapping requestMapping = clazz.getAnnotation(org.springframework.web.bind.annotation.RequestMapping.class);
				String currentViewPrefix = "";
				if (requestMapping != null && requestMapping.value().length > 0) {
					currentViewPrefix = requestMapping.value()[0];
					
					//如果第一个字符为"/" 进行创建权限结构
					if(currentViewPrefix.startsWith("/")){
						String[] split = currentViewPrefix.split("/");
						//递归生成权限目录
						paren =  initResourceCatalogue(appName,clazz,split,appKey,1,null);
					}
					
				}
				if (StringUtils.isEmpty(currentViewPrefix)) {
					currentViewPrefix = "";
				}
				
				//获得类中全部方法
				Method[] methods = clazz.getDeclaredMethods();
				
				//循环所有方法
				for(Method method : methods){
					LogFactory.info("          ---方法名"+method.getName());
					MyRequiresPermissions myRequiresPermissions = method.getAnnotation(MyRequiresPermissions.class);
					//判断当前方法是否存在这个权限
					if(myRequiresPermissions!=null){
						
						//获得方法的requestmapping
						RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
					//	String url = currentViewPrefix +methodRequestMapping.value()[0]+".do";
						String url = appKey+currentViewPrefix +methodRequestMapping.value()[0];
						
						MethodName methodName = method.getAnnotation(MethodName.class);
						if(methodName!=null){
							methodNameName = methodName.name();
						}
						
						/**
						 * 判断此方法的对应   数据库权限是否存在  
						 * 	如果存在则查下个
						 *  如果不存在则添加
						 */
//						QueryFilter filter = new QueryFilter();
//						filter.addFilter("Q_appName_EQ_String", appName);
//						filter.addFilter("Q_className_EQ_String", clazz.getName());
//						filter.addFilter("Q_methodName_EQ_String", method.getName());
//						MgrAppResource _AppResource = dao.get_NOSAAS(filter);
						
						QueryFilter filter = new QueryFilter(MgrAppResource.class);
						filter.addFilter("appName=", appName);
						filter.addFilter("className=", clazz.getName());
						filter.addFilter("methodName=", method.getName());
						List<MgrAppResource> list = dao.selectByExample(filter.setNosaas().getExample());
						MgrAppResource _AppResource = null;
						if(list.size()>0){
							_AppResource =  list.get(0);
						}
						
						if(_AppResource!=null){
							_AppResource.setAppName(appName);
							_AppResource.setName(methodNameName);
							_AppResource.setMkey(url);
							_AppResource.setPkey(paren==null?appKey:paren.getMkey());
							_AppResource.setUrl(url);
							_AppResource.setSysName(url);
							_AppResource.setClassName(clazz.getName());
							_AppResource.setMethodName(method.getName());
						//	dao.update(_AppResource);
							dao.updateByPrimaryKeySelective(_AppResource);
							continue ;
						}
						//如果不存在则添加
						MgrAppResource appResource = new MgrAppResource();
						appResource.setAppName(appName);
						appResource.setName(methodNameName);
						appResource.setMkey(url);
						appResource.setPkey(paren==null?appKey:paren.getMkey());
						appResource.setUrl(url);
						appResource.setSysName(url);
						appResource.setClassName(clazz.getName());
						appResource.setMethodName(method.getName());
						appResource.setIsLock("1");
						appResource.setIsDelete("0");
				//		dao.save_NOSAAS(appResource);
						dao.insertSelective(appResource);
						
					}else{
						//如果这个方法不存在权限了，查找数据库进行删除这个对应的方法权限
//						QueryFilter filter = new QueryFilter();
//						filter.addFilter("Q_appName_EQ_String", appName);
//						filter.addFilter("Q_className_EQ_String", clazz.getName());
//						filter.addFilter("Q_methodName_EQ_String", method.getName());
//						MgrAppResource _AppResource = dao.get_NOSAAS(filter);
						
						QueryFilter filter = new QueryFilter(MgrAppResource.class);
						filter.addFilter("appName=", appName);
						filter.addFilter("className=", clazz.getName());
						filter.addFilter("methodName=", method.getName());
						List<MgrAppResource> list = dao.selectByExample(filter.setNosaas().getExample());
						MgrAppResource _AppResource = null;
						if(list.size()>0){
							_AppResource = list.get(0);
						}
						if(_AppResource!=null){
							dao.delete(_AppResource);
						}
						
					}
					
				}
			}
		}
	}

	
}
