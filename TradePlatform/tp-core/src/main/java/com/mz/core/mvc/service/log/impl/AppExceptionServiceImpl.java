/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年9月17日 上午11:32:10
 */
package com.mz.core.mvc.service.log.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.dao.log.AppExceptionDao;
import com.mz.core.mvc.model.log.AppException;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.core.mvc.service.log.AppExceptionService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2015年9月17日 上午11:32:10 
 */
@Service("appExceptionService")
public class AppExceptionServiceImpl extends BaseServiceImpl<AppException, Long> implements
    AppExceptionService {

  @Resource(name = "appExceptionDao")
  @Override
  public void setDao(BaseDao<AppException, Long> dao) {
    super.dao = dao;
  }

  @Resource
  private AppExceptionDao appExceptionDao;

  @Override
  public AppException selectOne1() {
    AppException selectByPrimaryKey = appExceptionDao.selectByPrimaryKey(Long.valueOf(1));

    Example example = new Example(AppException.class);
    Criteria criteria = example.createCriteria();
    //	criteria.andCondition("name like "," 大家 ");
    criteria.andLike("name", "%大家%");

    List<AppException> list = super.dao.selectByExample(example);
    System.out.println(list.get(0).getName());
    return list.isEmpty() ? null : list.get(0);
  }

}
