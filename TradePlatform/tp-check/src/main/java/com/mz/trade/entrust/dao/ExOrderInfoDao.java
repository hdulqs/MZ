/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Wu Shuiming
 * @version:   V1.0 
 * @Date:      2015年11月06日  14:57:13
 */
package com.mz.trade.entrust.dao;


import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.trade.entrust.model.ExOrderInfo;
import java.util.Map;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年4月12日 下午4:45:50 
 */
public interface ExOrderInfoDao extends BaseDao<ExOrderInfo, Long> {
	
	
	 public void batchDeleteExenrustAcar(Map<String,Object> map);
	 public void batchDeleteExenrustAhar(Map<String,Object> map);
	 public void batchDeleteExenrustEdcar(Map<String,Object> map);
	 public void batchDeleteExenrustEdhar(Map<String,Object> map);
	 
	 public Integer getMaxExEntrust();
	 public Integer getMaxExOrderInfo();
	 public Integer getMaxEDCAR();
	 public Integer getMaxEDHACR();
	 public Integer getMaxACAR();
	 public Integer getMaxAHAR();
	 public void insertExEntrustHistory(Map<String,Object> map);
	 public void insertExOrderInfoHistory(Map<String,Object> map);
	 
	 public void deleteExEntrust(Map<String,Object> map);
	 public void deleteExorderInfo(Map<String,Object> map);
	
}
