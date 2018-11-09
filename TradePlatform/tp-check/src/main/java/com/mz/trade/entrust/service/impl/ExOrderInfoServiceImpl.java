/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:04:29
 */
package com.mz.trade.entrust.service.impl;

import com.github.pagehelper.util.StringUtil;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.util.properties.PropertiesUtils;
import com.mz.core.thread.ThreadPool;
import com.mz.exchange.product.dao.ExCointoCoinDao;
import com.mz.trade.entrust.dao.ExOrderInfoDao;
import com.mz.trade.entrust.service.ExEntrustService;
import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.trade.entrust.service.RemoveRecordDataRunable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年4月12日 下午4:45:50 
 */
@Service("exOrderInfoService")
public class ExOrderInfoServiceImpl extends BaseServiceImpl<ExOrderInfo, Long>
		implements ExOrderInfoService {

	@Resource(name = "exOrderInfoDao")
	@Override
	public void setDao(BaseDao<ExOrderInfo, Long> dao) {
		super.dao = dao;
	}
	@Resource
	private ExOrderInfoDao exOrderInfoDao;
	@Resource
	private ExCointoCoinDao exCointoCoinDao;
	@Resource
	private ExEntrustService exEntrustService;
	@Resource
	private ExOrderInfoService exOrderInfoService;
	@Override
	public void removeEntrustRobt() {
      long start=System.currentTimeMillis();
    	
		String autoExExEntrustUserName=PropertiesUtils.APP.getProperty("app.autoExExEntrustUserName");
		String[] rt=null;
		List<String> listusername=new ArrayList<>();
		if(!StringUtil.isEmpty(autoExExEntrustUserName)){
			 rt= autoExExEntrustUserName.split(",");
		}else{
			return ;
		}
		int a=0;
		while(a<rt.length){
			listusername.add(rt[a]);
			a++;
			
		}
	//	String autoExExEntrustUserNameaddyin="'"+autoExExEntrustUserName.replace(",", "','")+"'";
		
		
			//1、所有的节点先查出来
	   		Integer maxacar=exOrderInfoDao.getMaxACAR();
			Integer maxahar=exOrderInfoDao.getMaxAHAR();
			Integer maxedcar=exOrderInfoDao.getMaxEDCAR();
			Integer maxdhacr=exOrderInfoDao.getMaxEDHACR();
			
			Integer maxaexentrust=exOrderInfoDao.getMaxExEntrust();
			Integer maxexorderinfo=exOrderInfoDao.getMaxExOrderInfo();
			
			Map<String,Object> map=new HashMap<String,Object>();
	    	map.put("list", listusername);
	    	map.put("id", (null==maxaexentrust?0:maxaexentrust));
		    //2、移动exentrust，exordderinfo
			exOrderInfoDao.insertExEntrustHistory(map);
			map.put("id", (null== maxexorderinfo?0:maxexorderinfo));
			exOrderInfoDao.insertExOrderInfoHistory(map);
			
			//3删掉已经移走的exentrust，exordderinfo
			map.put("id", (null== maxaexentrust?0:maxaexentrust));
	    	exOrderInfoDao.deleteExEntrust(map);
	    	map.put("id", (null== maxexorderinfo?0:maxexorderinfo));
	    	exOrderInfoDao.deleteExorderInfo(map);
	    	//4、删掉所有的流水 ,这个可以走异步省的这个主线程花的时间太长
	    	RemoveRecordDataRunable removeRecordDataRunable=new RemoveRecordDataRunable(map,maxacar,maxahar,maxedcar,maxdhacr,exOrderInfoDao);
	    	ThreadPool.exe(removeRecordDataRunable);
	    	
		 long end=System.currentTimeMillis();
		 System.out.println("数据移除总耗时间="+(end-start));
		
	}
    
}
