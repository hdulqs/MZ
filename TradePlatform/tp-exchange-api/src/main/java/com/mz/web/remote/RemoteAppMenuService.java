/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年11月11日 下午5:07:22
 */
package com.mz.web.remote;


import com.mz.manage.init.model.MgrAppMenu;
import com.mz.tenant.user.model.SaasUser;
import com.mz.web.menu.model.AppMenu;
import java.io.Serializable;
import java.util.List;


/**
 *
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2015年11月20日 下午3:43:58
 */
public interface RemoteAppMenuService {


  /**
   * 增加AppMenu
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param appMenu
   * @param:    @return
   * @return: Serializable
   * @Date :          2015年11月25日 下午2:20:00
   * @throws:
   */
  public Serializable save_NOSAAS(AppMenu appMenu);


  /**
   * 更新AppMenu
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param appMenu
   * @param:    @return
   * @return: void
   * @Date :          2015年12月3日 下午3:32:33
   * @throws:
   */
  public void update(AppMenu appMenu);


  /**
   * <p>查询SAAS用户下的所有AppMenu</p>
   * @author: Liu Shilei
   * @param:    @param saasId
   * @return: void
   * @Date :          2015年11月27日 上午11:18:39
   * @throws:
   */
  public List<AppMenu> findBySaasId(String saasId);


  /**
   * <p> TODO</p>  业务平台初始化菜单权限----管理平台调用些方法
   * @author: Liu Shilei
   * @param:    @param saasId
   * @param:    @param list
   * @return: void
   * @Date :          2015年12月3日 下午3:40:42
   * @throws:
   */
  public boolean init(SaasUser saasUser, List<MgrAppMenu> list);

  /**
   * <p> TODO</p>  还原appMenu
   * @author: Liu Shilei
   * @param:    @param saasUser
   * @param:    @param list
   * @return: void
   * @Date :          2015年12月23日 下午5:02:00
   * @throws:
   */
  public void restore(SaasUser saasUser, List<MgrAppMenu> list);

  /**
   * <p> TODO</p> 根据ids  查询列表
   * @author: Liu Shilei
   * @param:    @param appResourceSet
   * @return: List<AppMenu>
   * @Date :          2015年12月7日 下午4:49:44
   * @throws:
   */
  public List<AppMenu> findByIds(String ids);


  /**
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param saasUser
   * @param:    @param list
   * @param:    @return
   * @return: boolean
   * @Date :          2015年12月23日 下午5:38:10
   * @throws:
   */
  public boolean updateinit(SaasUser saasUser, List<MgrAppMenu> list);


}
