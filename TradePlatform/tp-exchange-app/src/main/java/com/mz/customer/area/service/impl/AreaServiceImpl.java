package com.mz.customer.area.service.impl;

import com.mz.area.model.Area;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.area.dao.AreaDao;
import com.mz.customer.area.service.AreaService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("areaService")
public class AreaServiceImpl extends BaseServiceImpl<Area, Long> implements AreaService {

  @Resource(name = "areaDao")
  @Override
  public void setDao(BaseDao<Area, Long> dao) {
    super.dao = dao;
  }

  /**
   * 查所有的省
   * <p> TODO</p>
   *
   * @author: Zhang Lei
   * @param: @return
   * @return: List<Area>
   * @Date :          2017年3月17日 上午11:57:21
   * @throws:
   */
  @Override
  public List<Area> findProvince() {
    return ((AreaDao) dao).findProvince();
  }

  @Override
  public List<Area> findCity(Long provinceId) {
    return ((AreaDao) dao).findCity(provinceId);
  }

  @Override
  public List<Area> findCounty(Long cityId) {
    return ((AreaDao) dao).findCounty(cityId);
  }


}