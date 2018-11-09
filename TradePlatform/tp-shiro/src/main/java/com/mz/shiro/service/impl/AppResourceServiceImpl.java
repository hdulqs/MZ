/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年9月21日 上午11:42:06
 */
package com.mz.shiro.service.impl;

import com.mz.shiro.service.AppResourceService;
import com.mz.core.cache.StaticCache;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.oauth.user.model.AppResource;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2015年9月21日 上午11:42:06
 */
@Service("appResourceService")
public class AppResourceServiceImpl extends BaseServiceImpl<AppResource, Long> implements
    AppResourceService {

  @Resource(name = "appResourceDao")
  @Override
  public void setDao(BaseDao<AppResource, Long> dao) {
    super.dao = dao;

  }


  @Override
  public void initCache() {
    //	List<AppResource> find = dao.findAll();
    List<AppResource> find = dao.selectAll();
    for (AppResource AppResource : find) {
      StaticCache.BASE_APPRESOURCE_CACHE.put(AppResource.getUrl(), AppResource.getUrl());
    }
  }


}
