/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年11月20日 下午3:45:38
 */
package com.mz.web.remote;

import com.mz.manage.init.model.MgrAppMenu;
import com.mz.tenant.user.model.SaasUser;
import com.mz.util.BeanUtil;
import com.mz.util.QueryFilter;
import com.mz.web.menu.dao.AppMenuDao;
import com.mz.web.menu.model.AppMenu;
import com.mz.web.menu.service.AppMenuService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2015年11月20日 下午3:45:38 
 */
public class RemoteAppMenuServiceImpl implements RemoteAppMenuService {

  @Resource
  private AppMenuDao appMenuDao;
  @Resource
  private AppMenuService appMenuService;

  @Override
  public Serializable save_NOSAAS(AppMenu appMenu) {
    return appMenuService.save(appMenu);
  }

  @Override
  public List<AppMenu> findBySaasId(String saasId) {

    QueryFilter filter = new QueryFilter(AppMenu.class);
    filter.addFilter("Q_t.saasId_=_String", saasId);
    return appMenuService.find(filter);

  }

  @Override
  public void update(AppMenu appMenu) {
    appMenuService.update(appMenu);
  }

  @Override
  public boolean init(SaasUser saasUser, List<MgrAppMenu> list) {

    try {
      //重新插入初始化菜单
      for (MgrAppMenu mgrAppMenu : list) {
        AppMenu _appMenu = new AppMenu();
        BeanUtil.copyProperties(mgrAppMenu, _appMenu);
        _appMenu.setId(null);
        _appMenu.setSaasId(saasUser.getSaasId());
        appMenuService.save(_appMenu);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean updateinit(SaasUser saasUser, List<MgrAppMenu> list) {
    try {

      QueryFilter filter = new QueryFilter(AppMenu.class);
      filter.addFilter("saasId=", saasUser.getSaasId());
      //业务平台AppMenu
      List<AppMenu> listAll = appMenuService.find(filter);

      //更新过的key值
      List<String> updateKey = new ArrayList<String>();
      //--------------------------------比较第一步    业务平台的AppMenu 和管理平台的menu
      for (AppMenu appMenu : listAll) {
        boolean isHas = false;
        for (int i = 0; i < list.size(); i++) {
          //业务平台的menu存在管理平台的menu中key值匹配上 ---------更新
          if (appMenu.getMkey().equals(list.get(i).getMkey())) {
            isHas = true;
            Long id = appMenu.getId();
            BeanUtil.copyNotNullProperties(list.get(i), appMenu);
            appMenu.setId(id);
            appMenuService.update(appMenu);

            //被更新过的key值
            updateKey.add(list.get(i).getMkey());

            break;
          }
        }
        //业务平台的menu不存在管理平台的menu中key值匹配不上 ----------- 执行删除
        if (!isHas) {
          appMenuDao.delete(appMenu);
        }
      }

      //--------------------------------比较第二步    menu.xml中的数据      没有被更新过的执行新增
      for (MgrAppMenu mgrAppMenu : list) {
        //是否被更新过
        boolean isUpdate = false;
        for (String str : updateKey) {
          if (mgrAppMenu.getMkey().equals(str)) {
            isUpdate = true;//表示更新过
            break;
          }
        }

        if (!isUpdate) {//如果没有更新过-------执行新增
          AppMenu _appMenu = new AppMenu();
          BeanUtil.copyProperties(mgrAppMenu, _appMenu);
          _appMenu.setId(null);
          _appMenu.setSaasId(saasUser.getSaasId());
          appMenuService.save(_appMenu);
        }

      }


    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;

  }


  @Override
  public List<AppMenu> findByIds(String ids) {
    QueryFilter filter = new QueryFilter(AppMenu.class);
    filter.addFilter("id_in", ids);
    return appMenuService.find(filter.setNosaas());
  }

  @Override
  public void restore(SaasUser saasUser, List<MgrAppMenu> list) {
    QueryFilter filter = new QueryFilter(AppMenu.class);
    filter.addFilter("saasId=", saasUser.getSaasId());
    List<AppMenu> find = appMenuService.find(filter);
    if (find != null) {
      for (AppMenu appMenu : find) {
        appMenuDao.delete(appMenu);
      }
    }
  }


}
