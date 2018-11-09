/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年11月25日 下午4:49:39
 */
package com.mz.oauth.remote.user;


import com.mz.manage.init.model.MgrAppMenu;
import com.mz.oauth.user.model.AppResource;
import com.mz.oauth.user.model.AppUser;

import com.mz.tenant.user.model.SaasUser;
import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2015年11月25日 下午4:49:39
 */
public interface RemoteAppResourceService {

  /**
   * 增加appResource
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @param appMenu
   * @param: @return
   * @return: Serializable
   * @Date :          2015年11月25日 下午2:20:00
   * @throws:
   */
  public Serializable save_NOSAAS(AppResource appResource);

  /**
   * <p>查询SaasId用户下的所有AppResource</p>
   *
   * @author: Liu Shilei
   * @param: @param saasId
   * @param: @return
   * @return: List<AppResource>
   * @Date :          2015年11月27日 上午11:28:17
   * @throws:
   */
  public List<AppResource> findBySaasId(String saasId);

  /**
   * <p> TODO</p> 返回当前用户下的所有权限资源key
   *
   * @author: Liu Shilei
   * @param: @param user
   * @param: @return
   * @return: Set<String>
   * @Date :          2015年12月10日 上午10:27:45
   * @throws:
   */
  public Set<String> getAllResource(AppUser user);


  /**
   * <p> TODO</p>  初始化业务平台权限
   *
   * @author: Liu Shilei
   * @param: @param saasId
   * @param: @param list
   * @return: void
   * @Date :          2015年12月23日 下午4:24:46
   * @throws:
   */
  public void init(SaasUser saasUser, List<MgrAppMenu> list);

  /**
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @param list
   * @return: void
   * @Date :          2015年12月23日 下午4:58:08
   * @throws:
   */
  public void restore(SaasUser saasUser, List<MgrAppMenu> list);

  /**
   * <p> TODO</p> 更新业务平台数据
   *
   * @author: Liu Shilei
   * @param: @param saasUser
   * @param: @param list
   * @return: void
   * @Date :          2015年12月23日 下午5:49:43
   * @throws:
   */
  public void updateinit(SaasUser saasUser, List<MgrAppMenu> list);


}
