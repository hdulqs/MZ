/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月13日 上午10:22:53
 */
package com.mz.web.menu.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;

import com.mz.web.menu.model.AppMenu;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年10月13日 上午10:22:53 
 */
public interface AppMenuService extends BaseService<AppMenu, Long> {
	


	/**
	 * <p> TODO</p> 读取菜单备份文件列表
	 * @author:         Liu Shilei
	 * @param:    
	 * @return: void 
	 * @Date :          2015年10月19日 上午11:46:17   
	 * @throws:
	 */
	List<String> readStandbyFile();


	/**
	 * <p> TODO</p> 创建菜单备份
	 * @author:         Liu Shilei
	 * @param:    
	 * @return: void 
	 * @Date :          2015年10月19日 下午2:29:14   
	 * @throws:
	 */
	boolean createStandby();


	/**
	 * <p> TODO</p> 还原菜单备份
	 * @author:         Liu Shilei
	 * @param:    @param fileName
	 * @return: void 
	 * @Date :          2015年10月19日 下午3:37:57   
	 * @throws:
	 */
	boolean restoreStandby(String fileName);


	/**
	 * <p> TODO</p>   加载左侧菜单
	 * @author:         Liu Shilei
	 * @param:    
	 * @return: void 
	 * @Date :          2015年10月23日 上午9:46:32   
	 * @throws:
	 */
	List<AppMenu> findSystemMenu(HttpServletRequest request);


	/**
	 * <p>菜单管理功能,查出一颗菜单树</p>
	 * @author:         Liu Shilei
	 * @param:    @param request
	 * @param:    @return
	 * @return: List<AppMenu> 
	 * @Date :          2016年5月26日 下午12:20:09   
	 * @throws:
	 */
	List<AppMenu> listTree(HttpServletRequest request);


	/**
	 * <p>设置菜单是否可见,如果当前可见，则改为不可见，如果当前不可见，则改为可见。。
	 * 	      菜单包括级联连带效果,即父不可见，子孙也不可见
	 * </p>
	 * @author:         Liu Shilei
	 * @param:    @param id
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2016年5月26日 下午4:06:27   
	 * @throws:
	 */
	JsonResult setVisible(Long id);

}
