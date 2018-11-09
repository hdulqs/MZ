/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Wu Shuiming
 * @version:   V1.0 
 * @Date:      2015年11月06日  14:57:13
 */
package com.mz.trade.entrust.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.trade.redis.model.EntrustTrade;
import java.util.List;
import java.util.Map;

/**
 * 
 * <p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午1:34:18
 */
public interface ExEntrustDao extends BaseDao<ExEntrust, Long> {
	
	public ExEntrust getExEntrustByentrustNum(String entrustNum);
    public void  insertExEntrust(List<EntrustTrade> list);
    public void  updateExEntrust(List<ExEntrust> list);
    public  List<ExEntrust>   getExEntrustListByNums(List<EntrustTrade> list);
    List<ExEntrust> getExEdBycustomerId(Map<String,Object> map);
    public  List<ExEntrust>   getExEntrustListByNumstoMysql(List<EntrustTrade> list);
    public  List<ExEntrust>   getEntrustingByCustomerId(Map<String,Object> map);
}
