/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月13日 上午10:23:31
 */
package com.mz.core.mvc.service.menu.impl;


import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.core.mvc.service.menu.CoreAppMenuService;
import com.mz.util.BeanUtil;
import com.mz.util.QueryFilter;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.xml.XMLUtil;
import com.mz.web.menu.model.AppMenu;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年10月13日 上午10:23:31 
 */
@Service("coreAppMenuService")
public class CoreAppMenuServiceImpl extends BaseServiceImpl<AppMenu, Long>  implements CoreAppMenuService{

	@Resource(name = "coreAppMenuDao")
	@Override
	public void setDao(BaseDao<AppMenu, Long> dao) {
		super.dao = dao;
	}
	
	/**
	 * 去重menu.xml的key值
	 * 如果发现有重复的抛出运行时异常，不让项目启动
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param appKey  
	 * @param:    @param map
	 * @param:    @param appMenu
	 * @return: void 
	 * @Date :          2015年12月3日 上午10:36:11   
	 * @throws:
	 */
	public void isRepeat(String appKey,Map<String,Integer> map ,AppMenu appMenu){
		
		Integer integer = map.get(appMenu.getMkey());
		if(integer!=null&&integer!=0){
			LogFactory.error("菜单初始化异常");
			throw new RuntimeException("\r\n"
									  +"---------------------------------------菜单初始化异常------------------------------------------\r\n"
									  +"-----------------menu.xml中key值【"+appMenu.getMkey().replace(appKey+"_", "")+"】重复了------------\r\n"
									  +"--------------------------请更换不重复的key值,谢谢合作！-------------------------------------------\r\n"
								);
		}else{
			if(!StringUtils.isEmpty(appMenu.getMkey())){
				map.put(appMenu.getMkey(), 1); //put mkey
			}
			if(!StringUtils.isEmpty(appMenu.getShiroUrl())){
				map.put(appMenu.getShiroUrl(), 1);//put shiorUrl
			}
		}
		
	}
	
	/**
	 * 递归查找menu.xml中的元素
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param appName  项目名称
	 * @param:    @param appKey   项目KEY
	 * @param:    @param map      menu key值重复容器
	 * @param:    @param listAll  全部数据
	 * @param:    @param element  XML
	 * @param:    @param pkey  父级key值
	 * @return: void 
	 * @Date :          2015年12月3日 上午10:46:49   
	 * @throws:
	 */
	public void findMeun(String appName,String appKey,Map<String,Integer> map,List<AppMenu> listAll,Element element,String pkey){
		List<Element> elements = element.elements();
		if(elements!=null){
			 //父级key值
			 String key = element.attributeValue("key");
			 if(!StringUtils.isEmpty(key)){
				 pkey = appKey+"_"+key;
			 }
			
			 for(Element element1 : elements){
				 if(element1.getName().equals("menu")){
					 AppMenu mgrAppMenu = new AppMenu();
					 mgrAppMenu.setName(element1.attributeValue("name"));
					 if(StringUtils.isEmpty(element1.attributeValue("url"))){
						 mgrAppMenu.setUrl(null);
					 }else{
						 mgrAppMenu.setUrl(appKey+element1.attributeValue("url"));
					 }
					 if(StringUtils.isEmpty(element1.attributeValue("shiroUrl"))){
						 mgrAppMenu.setShiroUrl(null);
					 }else{
						 mgrAppMenu.setShiroUrl(appKey+element1.attributeValue("shiroUrl"));
					 }
					 mgrAppMenu.setMkey(appKey+"_"+element1.attributeValue("key"));
					 mgrAppMenu.setPkey(pkey);
					 mgrAppMenu.setIsOpen(element1.attributeValue("isOpen"));
					 mgrAppMenu.setIsOutLink(element1.attributeValue("isOutLink"));
					 if(!org.apache.commons.lang3.StringUtils.isEmpty(element1.attributeValue("orderNo"))){
						 mgrAppMenu.setOrderNo(Integer.valueOf(element1.attributeValue("orderNo")));
					 }
					 mgrAppMenu.setAppName(appName);
					 mgrAppMenu.setType("menu");
					 isRepeat(appKey,map, mgrAppMenu);
					 listAll.add(mgrAppMenu);	
					 
				 }else if(element1.getName().equals("function")){
					 AppMenu mgrAppMenu = new AppMenu();
					 mgrAppMenu.setName(element1.attributeValue("name"));
					 mgrAppMenu.setShiroUrl(appKey+element1.attributeValue("shiroUrl"));
					 mgrAppMenu.setMkey(appKey+"_"+element1.attributeValue("key"));
					 mgrAppMenu.setPkey(pkey);
					 mgrAppMenu.setAppName(appName);
					 mgrAppMenu.setType("function");
					 isRepeat(appKey,map, mgrAppMenu);
					 listAll.add(mgrAppMenu);
				 }
				 
				 findMeun(appName, appKey, map, listAll, element1,pkey);
			 }
		}
		
	}
	
	
	@Override
	public void init(String appName,String appKey) {
		 LogFactory.info("正在加载/menu-"+appKey+".xml");
		 InputStream resourceAsStream = this.getClass() .getClassLoader().getResourceAsStream("menu-"+appKey+".xml");
		 Document doc = XMLUtil.readIS(resourceAsStream);
		 if(doc==null){
			 LogFactory.info("没找到----menu-"+appKey+".xml");
			 return ;
		 }
		 Element rootElement = doc.getRootElement();
		 if(rootElement==null){
			 return;
		 }
		 
		 //xml中最新的menu
		 List<AppMenu>  listAll = new LinkedList<AppMenu>();
		 Map<String,Integer>  map = new HashMap<String,Integer>();
		 
		 //递归查找list
		 findMeun(appName, appKey, map, listAll, rootElement,appKey);
		 
		 //---------------------------------数据库和menu.xml中数据比较-----------------------------------------------------
		 
		 QueryFilter filter = new QueryFilter(AppMenu.class);
		 filter.addFilter("appName=", appName);
		 
		 //数据中原有的appmenu
		 List<AppMenu> oldList = dao.selectByExample(filter.setNosaas().getExample());
		 
		 //更新过的key值
		 List<String> updateKey = new ArrayList<String>();
		 //--------------------------------比较第一步    比较数据库中的数据         如果存在menu.xml中更新，不存在删除
		 for(AppMenu  mgrAppMenu: oldList){
			 boolean isHas = false;
			 for(int i = 0 ; i < listAll.size() ; i++ ){
				 //如果数据库中的和menu.xml中的key值匹配上 ---------更新
				 if(mgrAppMenu.getMkey().equals(listAll.get(i).getMkey())){
					 isHas = true;
					 BeanUtil.copyNotNullProperties(listAll.get(i), mgrAppMenu);
					 super.update(mgrAppMenu);
					 //被更新过的key值
					 updateKey.add(listAll.get(i).getMkey());
					 break;
				 }
			 }
			 //如果数据库中的和menu.xml中的key值匹配不上----------- 执行删除
			 if(!isHas){
				 //此处注释，防止多人同时开发同一个项目时menu不对称
				 dao.deleteByPrimaryKey(mgrAppMenu.getId());
			 }
		 }
		 
		//--------------------------------比较第二步    menu.xml中的数据      没有被更新过的执行新增
		 for(AppMenu mgrAppMenu : listAll){
			 //是否被更新过
			 boolean isUpdate = false;
			 for(String str :updateKey){
				 if(mgrAppMenu.getMkey().equals(str)){
					 isUpdate = true;//表示更新过
					 break;
				 }
			 }
			 
			 if(!isUpdate){//如果没有更新过-------执行新增
				 mgrAppMenu.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
				 super.save(mgrAppMenu);
			 }
			 
		 }
	}

}
