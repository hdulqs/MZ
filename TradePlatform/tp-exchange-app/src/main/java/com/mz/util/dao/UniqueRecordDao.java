package com.mz.util.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.util.UniqueRecord;

public interface UniqueRecordDao extends BaseDao<UniqueRecord,String> {
    int add(UniqueRecord uniqueRecord);
}
