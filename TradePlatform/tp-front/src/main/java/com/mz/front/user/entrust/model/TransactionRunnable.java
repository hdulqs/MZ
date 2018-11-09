package com.mz.front.user.entrust.model;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.mz.util.sys.SpringContextUtil;;

public class TransactionRunnable implements Runnable {
	
	private final Logger log = Logger.getLogger(TransactionRunnable.class);
	
	//成交订单
	private List<EntrustInfo> infoList;
	
	//委托单
	private Entrust entrust;
	

	
	
	private  TransactionRunnable() {
	}
	
	public TransactionRunnable(List<EntrustInfo> infoList,Entrust entrust){
		this.infoList = infoList;
		this.entrust = entrust;
	}

	@Override
	public void run() {
		
		//总成交价钱
		BigDecimal total_price = new BigDecimal(0);
		//总成交数量
		BigDecimal total_amout = new BigDecimal(0);
		
		//如果成交单大于0,说明交易成功
		if(infoList.size()>0){
			for(int i = 0 ; i < infoList.size() ; i++){
				EntrustInfo info = infoList.get(i);
				if("buy".equals(info.getEntrust_type())){//主动买,以卖方为成交价计算
					
					BigDecimal bigDecimal = info.getSell_entrust_price().multiply(info.getDealAmout()).setScale(4, BigDecimal.ROUND_HALF_UP);					
					total_price = total_price.add(bigDecimal);
					total_amout = total_amout.add(info.getDealAmout());
				}else{//主动卖，以买方成交价计算
					
					BigDecimal bigDecimal = info.getBuy_entrust_price().multiply(info.getDealAmout()).setScale(4, BigDecimal.ROUND_HALF_UP);					
					total_price = total_price.add(bigDecimal);
					total_amout = total_amout.add(info.getDealAmout());
				}
			}
		}
		
		
		//买委托
		if("buy".equals(entrust.getEntrust_type())){
			if(total_price.compareTo(BigDecimal.ZERO)==1){
				log.info("交易成功：主委托方增加"+total_amout+"个币");
				log.info("交易成功：主委托方减少"+total_price+"人民币");
			}else{
				//queueSender.send("account.queue", "hello");
				//更新账户
				log.info("未交易成功：主委托方增加"+0+"个币");
				log.info("未交易成功：主委托方减少"+entrust.getEntrust_price().multiply(entrust.getEntrust_amout())+"人民币");
			}
			
		}else{//卖委托处理
			if(total_price.compareTo(BigDecimal.ZERO)==1){
				log.info("交易成功：主委托方增加"+total_price+"人民币");
				log.info("交易成功：主委托方减少"+total_amout+"个币");
			}else{
				log.info("未交易成功：主委托方增加"+0+"人民币");
				//更新账户
				log.info("未交易成功：主委托方减少"+entrust.getEntrust_amout()+"个币");
			}
		}
		
		
		
		
	}

}
