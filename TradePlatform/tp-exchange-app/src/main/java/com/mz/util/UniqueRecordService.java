package com.mz.util;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.UniqueRecord;

public interface UniqueRecordService extends BaseService<UniqueRecord, String> {

  boolean add(UniqueRecord unique);
}
