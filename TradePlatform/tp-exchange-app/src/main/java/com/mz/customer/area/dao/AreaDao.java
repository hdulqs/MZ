package com.mz.customer.area.dao;

import com.mz.area.model.Area;
import com.mz.core.mvc.dao.base.BaseDao;
import java.util.List;


public interface AreaDao extends BaseDao<Area, Long> { // 查询省

  public List<Area> findProvince();

  // 查询市
  public List<Area> findCity(Long provinceId);

  // 查询县区
  public List<Area> findCounty(Long cityId);
}
