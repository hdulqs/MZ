/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年5月17日 上午11:21:53
 */
package com.mz.web.article.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.web.app.model.AppFriendLink;
import com.mz.web.article.service.AppFriendLinkService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author:         Wu Shuiming
 * @Date :          2016年5月17日 上午11:21:53 
 */
@Service("appFriendLinkService")
public class AppFriendLinkServiceImpl extends BaseServiceImpl<AppFriendLink, Long> implements AppFriendLinkService{

	@Resource(name = "appFriendLinkDao")
	@Override
	public void setDao(BaseDao<AppFriendLink, Long> dao) {
		super.dao = dao;
	}

}
