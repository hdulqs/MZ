package com.mz.util.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.util.UniqueRecord;
import com.mz.util.UniqueRecordService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("uniqueRecordService")
public class UniqueRecordServiceImpl extends BaseServiceImpl<UniqueRecord, String> implements
    UniqueRecordService {

  @Resource(name = "uniqueRecordDao")
  @Override
  public void setDao(BaseDao<UniqueRecord, String> dao) {
    super.dao = dao;
  }

  @Override
  public boolean add(UniqueRecord unique) {
    int insert = dao.insert(unique);
    return insert > 0 ? true : false;
  }
}
