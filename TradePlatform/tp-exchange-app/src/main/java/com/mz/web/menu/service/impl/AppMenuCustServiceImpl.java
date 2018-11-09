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
import com.mz.util.BeanUtil;
import com.mz.util.QueryFilter;
import com.mz.web.menu.model.AppMenu;
import com.mz.web.menu.model.AppMenuCust;
import com.mz.web.menu.service.AppMenuCustService;
import com.mz.web.menu.service.AppMenuService;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2015年10月13日 上午10:23:31
 */
@Service("appMenuCustService")
public class AppMenuCustServiceImpl extends BaseServiceImpl<AppMenuCust, Long> implements
    AppMenuCustService {

  @Resource(name = "appMenuCustDao")
  @Override
  public void setDao(BaseDao<AppMenuCust, Long> dao) {
    super.dao = dao;
  }

  @Resource
  private AppMenuService appMenuService;

  @Override
  public JsonResult add(String ids) {
    JsonResult jsonResult = new JsonResult();
    if (!StringUtils.isEmpty(ids)) {
      String[] split = StringUtils.split(ids, ",");
      for (String id : split) {
        //保存功能
        AppMenu appMenu = appMenuService.get(Long.valueOf(id));
        AppMenuCust appMenuCust = new AppMenuCust();
        BeanUtil.copyProperties(appMenu, appMenuCust);
        appMenuCust.setId(null);
        super.save(appMenuCust);

        //保存菜单下的权限
        QueryFilter queryFilter = new QueryFilter(AppMenu.class);
        queryFilter.addFilter("pkey=", appMenu.getMkey());
        queryFilter.addFilter("type=", "function");
        List<AppMenu> list = appMenuService.find(queryFilter);
        if (list != null && list.size() > 0) {
          for (AppMenu smenu : list) {
            AppMenuCust smenuCust = new AppMenuCust();
            BeanUtil.copyProperties(smenu, smenuCust);
            smenuCust.setId(null);
            super.save(smenuCust);
          }
        }

      }
    }
    jsonResult.setSuccess(true);

    return jsonResult;
  }

  @Override
  public JsonResult remove(String ids) {

    JsonResult jsonResult = new JsonResult();
    if (!StringUtils.isEmpty(ids)) {
      String[] split = StringUtils.split(ids, ",");
      for (String id : split) {
        AppMenuCust appMenuCust = super.get(Long.valueOf(id));
        super.delete(appMenuCust.getId());

        //删除菜单下的权限
//				QueryFilter queryFilter = new QueryFilter(AppMenu.class);
//				queryFilter.addFilter("pkey=", appMenuCust.getMkey());
//				List<AppMenuCust> list = super.find(queryFilter);
//				if(list!=null&&list.size()>0){
//					for(AppMenuCust smenu : list){
//						super.delete(smenu.getId());
//					}
//				}

      }
    }

    jsonResult.setSuccess(true);

    return jsonResult;
  }


  @Override
  public JsonResult isVisible(String ids) {

    JsonResult jsonResult = new JsonResult();
    if (!StringUtils.isEmpty(ids)) {
      String[] split = StringUtils.split(ids, ",");
      for (String id : split) {
        AppMenuCust appMenuCust = super.get(Long.valueOf(id));
        if ("function".equals(appMenuCust.getType())) {
          if ("1".equals(appMenuCust.getIsVisible())) {
            appMenuCust.setIsVisible("0");//设置客户展示
          } else {
            //设置该客户不展示
            appMenuCust.setIsVisible("1");
          }
          super.update(appMenuCust);
        }

      }
    }

    jsonResult.setSuccess(true);

    return jsonResult;
  }


}
