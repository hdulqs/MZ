package com.mz.trade.entrust.service;

import com.mz.trade.entrust.dao.ExOrderInfoDao;
import java.util.Map;

public class RemoveRecordDataRunable implements Runnable {
	
	
	private Map<String,Object> map;
	private Integer maxacar;
	private Integer maxahar;
	private Integer maxedcar;
	private Integer maxdhacr;
	private ExOrderInfoDao exOrderInfoDao;
	public RemoveRecordDataRunable(Map<String,Object> map,Integer maxacar,
			Integer maxahar,Integer maxedcar,Integer maxdhacr,ExOrderInfoDao exOrderInfoDao){
		this.map=map;
		this.maxacar=maxacar;
		this.maxahar=maxahar;
		this.maxedcar=maxedcar;
		this.maxdhacr=maxdhacr;
		this.exOrderInfoDao=exOrderInfoDao;
	}
	@Override
	public void run() {	//4、删掉所有的流水 
		long start=System.currentTimeMillis();
    	map.put("id",  (null==maxacar?0:maxacar));
    	exOrderInfoDao.batchDeleteExenrustAcar(map);
    	map.put("id",  (null==maxahar?0:maxahar));
    	exOrderInfoDao.batchDeleteExenrustAhar(map);
    	map.put("id",  (null==maxedcar?0:maxedcar));
    	exOrderInfoDao.batchDeleteExenrustEdcar(map);
    	map.put("id", (null== maxdhacr?0:maxdhacr));
    	exOrderInfoDao.batchDeleteExenrustEdhar(map);
    	 long end=System.currentTimeMillis();
    	 System.out.println("数据资金流水移除总耗时间="+(end-start));
	
	}
	
	
}
