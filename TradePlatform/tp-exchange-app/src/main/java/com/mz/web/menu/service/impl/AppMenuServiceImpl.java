/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年10月13日 上午10:23:31
 */
package com.mz.web.menu.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.oauth.remote.user.RemoteAppResourceService;
import com.mz.oauth.user.model.AppUser;
import com.mz.util.QueryFilter;
import com.mz.util.SortList;
import com.mz.util.file.FileUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.web.menu.model.AppMenu;
import com.mz.web.menu.service.AppMenuService;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2015年10月13日 上午10:23:31 
 */
@Service("appMenuService")
public class AppMenuServiceImpl extends BaseServiceImpl<AppMenu, Long> implements AppMenuService {

  @Resource(name = "appMenuDao")
  @Override
  public void setDao(BaseDao<AppMenu, Long> dao) {
    super.dao = dao;
  }


  @Override
  public List<String> readStandbyFile() {

    List<String> list = new ArrayList<String>();

    URL url = this.getClass().getClassLoader().getResource("");
    String standbyPath = url.getFile() + FileUtil.standbyPath;
    File file = new File(standbyPath);
    File[] listFiles = file.listFiles();
    if (listFiles != null) {
      for (File f : listFiles) {
        list.add(f.getName());
      }
    }

    return list;
  }


  @Override
  public boolean createStandby() {
    boolean flag = false;
		
	/*	QueryFilter filter = new QueryFilter();
		filter.addFilter("Q_pid_EQ_String", "0");
		List<AppMenu> list1 = dao.find(filter);
		 
		Element element1  = null;
		Element element2  = null;
		Element element3  = null;
		
		if(list1!=null){
			URL url = this.getClass() .getClassLoader().getResource("");
			String standbyPath = url.getFile()+FileUtil.standbyPath;
			String dateToString = DateUtil.dateToString(new Date(),"yyyy-MM-dd_HH-mm-ss");
			String filePath = standbyPath+"/"+dateToString+"MENU.xml";
			Document doc = XMLUtil.createDocument();
			Element root = doc.addElement("root");
			
			for(AppMenu appMenu1 : list1){
				 element1 = root.addElement("menu");
				 element1.addAttribute("name", appMenu1.getName());
				 element1.addAttribute("url", appMenu1.getUrl());
				 element1.addAttribute("shiroUrl", appMenu1.getShiroUrl());
				 element1.addAttribute("isOpen", appMenu1.getIsOpen());
				 element1.addAttribute("isOutLink", appMenu1.getIsOutLink());
				 element1.addAttribute("orderNo", appMenu1.getOrderNo());
				 element1.addAttribute("appName", appMenu1.getAppName());
				 
				 filter = new QueryFilter();
				 filter.addFilter("Q_pid_EQ_String", appMenu1.getId()+"");
				 List<AppMenu> list2 = dao.find(filter);
				 if(list2!=null){
					 for(AppMenu appMenu2 : list2){
						 element2 = element1.addElement("menu");
						 element2.addAttribute("name", appMenu2.getName());
						 element2.addAttribute("url", appMenu2.getUrl());
						 element2.addAttribute("shiroUrl", appMenu2.getShiroUrl());
						 element2.addAttribute("isOpen", appMenu2.getIsOpen());
						 element2.addAttribute("isOutLink", appMenu2.getIsOutLink());
						 element2.addAttribute("orderNo", appMenu2.getOrderNo());
						 element2.addAttribute("appName", appMenu2.getAppName());
						 
						 filter = new QueryFilter();
						 filter.addFilter("Q_pid_EQ_String", appMenu2.getId()+"");
						 List<AppMenu> list3 = dao.find(filter);
						 for(AppMenu appMenu3 : list3){
							 element3 = element2.addElement("menu");
							 element3.addAttribute("name", appMenu3.getName());
							 element3.addAttribute("url", appMenu3.getUrl());
							 element3.addAttribute("shiroUrl", appMenu3.getShiroUrl());
							 element3.addAttribute("isOpen", appMenu3.getIsOpen());
							 element3.addAttribute("isOutLink", appMenu3.getIsOutLink());
							 element3.addAttribute("orderNo", appMenu3.getOrderNo());
							 element3.addAttribute("appName", appMenu3.getAppName());
						 }
						 
					 }
				 }
			}
			flag = XMLUtil.createXML(doc, filePath);
		}*/

    return flag;
  }

  /* (non-Javadoc)
   * @see com.mz.core.mvc.service.menu.AppMenuService#restoreStandby(java.lang.String)
   */
  @Override
  public boolean restoreStandby(String fileName) {
    return false;/*
		boolean flag = false;
		if(StringUtil.isNull(fileName)){
			URL url = this.getClass() .getClassLoader().getResource("");
			String standbyPath = url.getFile()+FileUtil.standbyPath;
			String filePath = standbyPath+"/"+fileName;
			
			File file = new File(filePath);
			if(file!=null&&file.exists()){
				 //清空菜单表
				 dao.executeSql(" delete FROM app_menu ");
				
				 AppMenu appMenu1 = null; 
				 AppMenu appMenu2 = null; 
				 AppMenu appMenu3 = null; 
				 
				 Document doc = XMLUtil.readXML(filePath);
				 Element rootElement = doc.getRootElement();
				 List<Element> list1 = rootElement.elements("menu");
				 if(list1!=null){
					 for(Element element1 : list1){
							 
						 appMenu1 = new AppMenu();
						 appMenu1.setName(element1.attributeValue("name"));
						 appMenu1.setUrl(element1.attributeValue("url"));
						 appMenu1.setShiroUrl(element1.attributeValue("shiroUrl"));
				//		 appMenu1.setPid("0");
						 appMenu1.setIsOpen(element1.attributeValue("isOpen"));
						 appMenu1.setIsOutLink(element1.attributeValue("isOutLink"));
						 appMenu1.setOrderNo(element1.attributeValue("orderNO"));
						 appMenu1.setAppName(element1.attributeValue("appName"));
						 dao.save(appMenu1);
						 
						 List<Element> list2 = element1.elements("menu");
						 if(list2!=null){
							 for(Element element2 :list2 ){
								 
								 appMenu2 = new AppMenu();
								 appMenu2.setName(element2.attributeValue("name"));
								 appMenu2.setUrl(element2.attributeValue("url"));
								 appMenu2.setShiroUrl(element2.attributeValue("shiroUrl"));
						//		 appMenu2.setPid(appMenu1.getId()+"");
								 appMenu2.setIsOpen(element2.attributeValue("isOpen"));
								 appMenu2.setIsOutLink(element2.attributeValue("isOutLink"));
								 appMenu2.setOrderNo(element2.attributeValue("orderNO"));
								 appMenu1.setAppName(element2.attributeValue("appName"));
								 dao.save(appMenu2);
								 
								 List<Element> list3 = element2.elements("menu");
								 if(list3!=null){
									 for(Element element3 :list3 ){
										 appMenu3 = new AppMenu();
										 appMenu3.setName(element3.attributeValue("name"));
										 appMenu3.setUrl(element3.attributeValue("url"));
										 appMenu3.setShiroUrl(element3.attributeValue("shiroUrl"));
								//		 appMenu3.setPid(appMenu2.getId()+"");
										 appMenu3.setIsOpen(element3.attributeValue("isOpen"));
										 appMenu3.setIsOutLink(element3.attributeValue("isOutLink"));
										 appMenu3.setOrderNo(element3.attributeValue("orderNO"));
										 appMenu3.setAppName(element3.attributeValue("appName"));
										 dao.save(appMenu3);
									 }
									 
								 }
							 }
						 }
					 }
				 }
				 
				 flag = true;
				 
			}
		}
		return flag;
	*/
  }

  //递归查找权限树
  public boolean loadSonShiro(AppMenu appMenu) {
    Subject subject = SecurityUtils.getSubject();
    //如果菜单权限URL不为空则直接判断
    if (!StringUtils.isEmpty(appMenu.getShiroUrl())) {
      boolean flag = subject.isPermitted(appMenu.getShiroUrl());
      if (flag) {
        return flag;
      }
    }

    //否侧查找子权限
//		QueryFilter filter = new QueryFilter();
//		filter.addFilter("Q_t.pkey_EQ_String", appMenu.getMkey());
//		List<AppMenu> find = dao.find(filter);

    QueryFilter filter = new QueryFilter(AppMenu.class);
    filter.addFilter("pkey=", appMenu.getMkey());
    List<AppMenu> find = dao.selectByExample(filter.getExample());

    for (AppMenu menu : find) {
      //	dao.evict(menu);
      return loadSonShiro(menu);
    }

    return false;

  }

  @Override
  public List<AppMenu> findSystemMenu(HttpServletRequest request) {

    String key = request.getParameter("key");

    //获得当前登录用户
    AppUser user = ContextUtil.getCurrentUser();
    //最终返回的菜单
    List<AppMenu> list = new ArrayList<AppMenu>();

    //如果是admin账户返回所有菜单
    if (PropertiesUtils.APP.getProperty("app.admin").equals(user.getUsername())) {
      //查找所有菜单

      QueryFilter filter = new QueryFilter(AppMenu.class);
      filter.addFilter("type=", "menu");
      filter.addFilter("isVisible!=", "1");
      filter.addFilter("appName_in", key);
      filter.setOrderby("orderNo");
      List<AppMenu> allList = find(filter);

      return allList;
    }

    //远程调用 查询用户拥有的权限资源
    RemoteAppResourceService remoteAppResourceService = (RemoteAppResourceService) ContextUtil
        .getBean("remoteAppResourceService");
    //获得登录用户的所有权限资源
    Set<String> allResourceSet = remoteAppResourceService.getAllResource(user);

    Iterator<String> iteratorAllResourceSet = allResourceSet.iterator();
    while (iteratorAllResourceSet.hasNext()) {
      QueryFilter filter2 = new QueryFilter(AppMenu.class);
      filter2.addFilter("mkey=", iteratorAllResourceSet.next());
      filter2.addFilter("isVisible!=", "1");
      filter2.addFilter("appName_in", key);
      filter2.setOrderby("orderNo");
      AppMenu appMenu = get(filter2);
      if (appMenu != null) {
        list.add(appMenu);
      }

    }

    //list排序
    SortList<AppMenu> sortList = new SortList<AppMenu>();
    sortList.Sort(list, "getId", null);

    return list;
  }


  @Override
  public List<AppMenu> listTree(HttpServletRequest request) {

    QueryFilter filter = new QueryFilter(AppMenu.class);
    filter.addFilter("pkey_notlike", "%\\_%");
    List<AppMenu> find = find(filter);
    for (AppMenu appMenu : find) {
      recursivefind(appMenu);
    }

    return find;

  }

  /**
   * 递归查询
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param appMenu
   * @return: void
   * @Date :          2016年5月26日 下午2:51:55
   * @throws:
   */
  public void recursivefind(AppMenu appMenu) {
    QueryFilter filter = new QueryFilter(AppMenu.class);
    //父key等于传进来的mkey
    filter.addFilter("pkey=", appMenu.getMkey());
    List<AppMenu> list = this.find(filter);
    if (list != null && list.size() > 0) {
      appMenu.setSubMenu(list);
      for (AppMenu item : list) {
        recursivefind(item);
      }
    }
  }


  @Override
  public JsonResult setVisible(Long id) {

    JsonResult jsonResult = new JsonResult();

    String status = "";
    AppMenu appMenu = this.get(id);
    if ("1".equals(appMenu.getIsVisible())) {
      status = "0";
    } else {
      status = "1";
    }
    appMenu.setIsVisible(status);
    this.update(appMenu);

    QueryFilter queryFilter = new QueryFilter(AppMenu.class);
    queryFilter.addFilter("pkey=", appMenu.getMkey());
    List<AppMenu> list = this.find(queryFilter);
    for (AppMenu appMenu2 : list) {
      appMenu2.setIsVisible(status);
      this.update(appMenu2);

      QueryFilter queryFilter2 = new QueryFilter(AppMenu.class);
      queryFilter2.addFilter("pkey=", appMenu2.getMkey());
      List<AppMenu> list2 = this.find(queryFilter2);
      for (AppMenu appMenu3 : list2) {
        appMenu3.setIsVisible(status);
        this.update(appMenu3);
      }
    }

    jsonResult.setSuccess(true);
    return jsonResult;
  }


}
