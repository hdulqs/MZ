/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年12月9日 下午3:18:07
 */
package com.mz.oauth.remote.company;

import com.alibaba.dubbo.rpc.RpcContext;
import com.mz.oauth.user.model.AppOrganization;
import com.mz.oauth.user.model.AppUserOrganization;
import com.mz.shiro.dao.AppOrganizationDao;
import com.mz.shiro.service.AppOrganizationService;
import com.mz.shiro.service.AppUserOrganizationService;
import com.mz.tenant.user.model.SaasUser;
import com.mz.util.QueryFilter;
import com.mz.util.RemoteQueryFilter;
import java.util.List;
import javax.annotation.Resource;

/**
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2015年12月9日 下午3:18:07 
 */
public class RemoteAppOrganizationServiceImpl implements RemoteAppOrganizationService {

  @Resource
  private AppOrganizationDao appOrganizationDao;

  @Resource
  private AppOrganizationService appOrganizationService;

  @Resource
  private AppUserOrganizationService appUserOrganizationService;

  @Override
  public boolean initRootOrganization(SaasUser saasUser) {
    try {
      AppOrganization appOrganization = new AppOrganization();
      appOrganization.setSaasId(saasUser.getSaasId());
      appOrganization.setType("root");
      appOrganization.setName(saasUser.getCompany());
      appOrganization.setLogoPath("static/lib/_con/images/logo.png");
      //	appOrganizationDao.save_NOSAAS(appOrganization);

      appOrganizationDao.insertSelective(appOrganization);

      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean restoreRootOrganization(SaasUser saasUser) {
    try {
      //	QueryFilter filter = new QueryFilter();

      QueryFilter filter = new QueryFilter(SaasUser.class);

      //	filter.addFilter("Q_t.type_=_String", "root");

      filter.addFilter("type=", "root");

      //	filter.addFilter("Q_t.saasId_=_String", saasUser.getSaasId());

      filter.addFilter("saasId=", saasUser.getSaasId());

      //	List<AppOrganization> find_NOSAAS = appOrganizationDao.find_NOSAAS(filter);

      List<AppOrganization> find_NOSAAS = appOrganizationDao
          .selectByExample(filter.setNosaas().getExample());

      if (find_NOSAAS != null) {
        for (AppOrganization appOrganization : find_NOSAAS) {
          appOrganizationDao.delete(appOrganization);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public List<AppUserOrganization> findUserOrgByUid(Long userId) {

    QueryFilter queryFilter = new QueryFilter(AppUserOrganization.class);
    queryFilter.addFilter("userId=", userId);
    queryFilter.setSaasId(RpcContext.getContext().getAttachment("saasId"));
    return appUserOrganizationService.find(queryFilter);

  }

  @Override
  public AppOrganization get(RemoteQueryFilter remoteQueryFilter) {
    return appOrganizationService.get(remoteQueryFilter.getQueryFilter());
  }

}
