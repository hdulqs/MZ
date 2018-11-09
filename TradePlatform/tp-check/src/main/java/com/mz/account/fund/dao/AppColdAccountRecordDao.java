/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月31日 下午6:50:05
 */
package com.mz.account.fund.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import java.util.List;
import java.util.Map;

import com.mz.account.fund.model.AppColdAccountRecord;


/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月31日 下午6:50:05 
 */
public interface AppColdAccountRecordDao extends BaseDao<AppColdAccountRecord,Long> {
    public  void insertRecord(List<AppColdAccountRecord> list);
    public List<AppColdAccountRecord> find(Map<String,Object> map);
    
}