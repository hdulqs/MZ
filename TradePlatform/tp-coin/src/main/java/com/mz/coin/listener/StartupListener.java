/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0 
 * @Date:        2015年9月16日 上午11:04:39
 */
package com.mz.coin.listener;

import com.mz.Constant;
import com.mz.coin.eth.service.impl.EtcService;
import com.mz.coin.eth.service.impl.EtherService;
import com.mz.coin.eth.service.impl.EtzService;
import com.mz.coin.quart.service.CoinQuart;
import com.mz.core.quartz.QuartzJob;
import com.mz.core.quartz.QuartzManager;
import com.mz.core.quartz.ScheduleJob;
import com.mz.util.sys.AppUtils;
import com.mz.utils.Properties;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import org.springframework.web.context.ContextLoaderListener;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Gao Mimi
 * @Date : 2015年10月10日 下午5:20:10
 */
public class StartupListener extends ContextLoaderListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		// 初始化应用程序工具类
		AppUtils.init(event.getServletContext());
		String suffixIp="_ip";
		
		//基于bitcoin的充币记录获取保存至coin_transaction
		ScheduleJob productionTx = new ScheduleJob();
		productionTx.setBeanClass(CoinQuart.class.getName());
		productionTx.setMethodName("productionTx");
		QuartzManager.addJob("productionTx", productionTx, QuartzJob.class, "0 0/2 * * * ?");
		
		//消费coin_transaction中未消费的数据
		ScheduleJob consumeTx = new ScheduleJob();
		consumeTx.setBeanClass(CoinQuart.class.getName());
		consumeTx.setMethodName("consumeTx");
		QuartzManager.addJob("consumeTx", consumeTx, QuartzJob.class, "0 0/5 * * * ?");

		//查询所有币种数量记录到redis中去 5分钟
		ScheduleJob walletInfo = new ScheduleJob();
		walletInfo.setBeanClass(CoinQuart.class.getName());
		walletInfo.setMethodName("recordWalletInfoToRedis");
		QuartzManager.addJob("recordWalletInfoToRedis", walletInfo, QuartzJob.class, "0 0/5 * * * ?");
		
		//coins.properties配置文件读取
		Map<String,String> properties=Properties.appcoinMap();
		
		//以太坊、代币、etc业务
		if(properties.containsKey(Constant.ETHER.toLowerCase()+suffixIp)||properties.containsKey(Constant.ETC.toLowerCase()+suffixIp)){
			//eth、代币充币和归集
			if (properties.containsKey(Constant.ETHER.toLowerCase()+suffixIp)) {
				EtherService.loadEthAccounts2Redis();
				//以太坊、代币交易记录生产
				ScheduleJob etherProductionTx = new ScheduleJob();
				etherProductionTx.setBeanClass(CoinQuart.class.getName());
				etherProductionTx.setMethodName("etherProductionTx");
				QuartzManager.addJob("etherProductionTx", etherProductionTx, QuartzJob.class, "0 0/10 * * * ?");
				
				
				//资金转入主钱包用来提币，定时器
				ScheduleJob etherSend2coinBaseJob = new ScheduleJob();
				etherSend2coinBaseJob.setBeanClass(EtherService.class.getName());
				etherSend2coinBaseJob.setMethodName("send2coinBaseJob");
				QuartzManager.addJob("etherSend2coinBaseJob", etherSend2coinBaseJob, QuartzJob.class, "0 0 0/2 * * ?");
			}
			
			//etc充币和归集
			if (properties.containsKey(Constant.ETC.toLowerCase()+suffixIp)) {
				//将所有的ETC地址加载到redis中
				EtcService.loadAccounts2Redis();
				//以太经典交易记录生产
				ScheduleJob etcproductionTx = new ScheduleJob();
				etcproductionTx.setBeanClass(CoinQuart.class.getName());
				etcproductionTx.setMethodName("etcproductionTx");
				QuartzManager.addJob("etcproductionTx", etcproductionTx, QuartzJob.class, "0 0/1 * * * ?");
				
				
				//资金转入主钱包用来提币，定时器
				ScheduleJob etcSend2coinBaseJob = new ScheduleJob();
				etcSend2coinBaseJob.setBeanClass(EtcService.class.getName());
				etcSend2coinBaseJob.setMethodName("send2coinBaseJob");
				QuartzManager.addJob("etcSend2coinBaseJob", etcSend2coinBaseJob, QuartzJob.class, "0 0 0/2 * * ?");
			}
			
			//etz充币和归集
			if (properties.containsKey(Constant.ETZ.toLowerCase()+suffixIp)) {
				//将所有的ETZ地址加载到redis中
				EtzService.loadAccounts2Redis();
				//以太经典交易记录生产
				ScheduleJob etzProductionTx = new ScheduleJob();
				etzProductionTx.setBeanClass(CoinQuart.class.getName());
				etzProductionTx.setMethodName("etzProductionTx");
				QuartzManager.addJob("etzProductionTx", etzProductionTx, QuartzJob.class, "0 0/1 * * * ?");
				
				
				//资金转入主钱包用来提币，定时器
				ScheduleJob etzSend2coinBaseJob = new ScheduleJob();
				etzSend2coinBaseJob.setBeanClass(EtzService.class.getName());
				etzSend2coinBaseJob.setMethodName("send2coinBaseJob");
				QuartzManager.addJob("etzSend2coinBaseJob", etzSend2coinBaseJob, QuartzJob.class, "0 0 0/2 * * ?");
			}
			
			//以太坊、代币、etc、etz充币记录消费
			ScheduleJob gethConsumeTx = new ScheduleJob();
			gethConsumeTx.setBeanClass(CoinQuart.class.getName());
			gethConsumeTx.setMethodName("gethConsumeTx");
			QuartzManager.addJob("gethConsumeTx", gethConsumeTx, QuartzJob.class, "0 0/4 * * * ?");
		}
		
		
		//tv业务
		if (properties.containsKey(Constant.TV+suffixIp)) {
			//充币记录生产
			ScheduleJob tvProduceTx = new ScheduleJob();
			tvProduceTx.setBeanClass(CoinQuart.class.getName());
			tvProduceTx.setMethodName("tvProduceTx");
			QuartzManager.addJob("tvProduceTx", tvProduceTx, QuartzJob.class, "0 0/2 * * * ?");
			
			//充币记录消费
			ScheduleJob tvConsumeTx = new ScheduleJob();
			tvConsumeTx.setBeanClass(CoinQuart.class.getName());
			tvConsumeTx.setMethodName("tvConsumeTx");
			QuartzManager.addJob("tvConsumeTx", tvConsumeTx, QuartzJob.class, "0 0/5 * * * ?");
			
			
			//归集提币地址
			ScheduleJob tvSend2coinbase = new ScheduleJob();
			tvSend2coinbase.setBeanClass(CoinQuart.class.getName());
			tvSend2coinbase.setMethodName("tvSend2coinbase");
			QuartzManager.addJob("tvSend2coinbase", tvSend2coinbase, QuartzJob.class, "0 0 0/2 * * ?");
		}
		
		
		//bds业务
		if (properties.containsKey(Constant.BDS.toLowerCase()+suffixIp)) {
			//充币定时器5分钟一次
			ScheduleJob bdsConsumeTx = new ScheduleJob();
			bdsConsumeTx.setBeanClass(CoinQuart.class.getName());
			bdsConsumeTx.setMethodName("bdsConsumeTx");
			QuartzManager.addJob("bdsConsumeTx", bdsConsumeTx, QuartzJob.class, "0 0/5 * * * ?");
		}
		
		//bts业务
		if (properties.containsKey(Constant.BTS.toLowerCase()+suffixIp)) {
			//充币定时器5分钟一次
			ScheduleJob btsConsumeTx = new ScheduleJob();
			btsConsumeTx.setBeanClass(CoinQuart.class.getName());
			btsConsumeTx.setMethodName("btsConsumeTx");
			QuartzManager.addJob("btsConsumeTx", btsConsumeTx, QuartzJob.class, "0 0/5 * * * ?");
		}
		
		//gxs业务
		if (properties.containsKey(Constant.GXS.toLowerCase()+suffixIp)) {
			//充币定时器5分钟一次
			ScheduleJob gxsConsumeTx = new ScheduleJob();
			gxsConsumeTx.setBeanClass(CoinQuart.class.getName());
			gxsConsumeTx.setMethodName("gxsConsumeTx");
			QuartzManager.addJob("gxsConsumeTx", gxsConsumeTx, QuartzJob.class, "0 0/3 * * * ?");
		}
		
		//neo业务
		if (properties.containsKey(Constant.NEO.toLowerCase()+suffixIp)) {
			//充币定时器5分钟一次
			ScheduleJob neoConsumeTx = new ScheduleJob();
			neoConsumeTx.setBeanClass(CoinQuart.class.getName());
			neoConsumeTx.setMethodName("neoConsumeTx");
			QuartzManager.addJob("neoConsumeTx", neoConsumeTx, QuartzJob.class, "0 0/5 * * * ?");
		}

		//htl业务
		if (properties.containsKey(com.mz.coin.utils.Constant.HTL.toLowerCase()+suffixIp)) {
			//充币定时器5分钟一次
			ScheduleJob htlConsumeTx = new ScheduleJob();
			htlConsumeTx.setBeanClass(CoinQuart.class.getName());
			htlConsumeTx.setMethodName("htlConsumeTx");
			htlConsumeTx.setIsConcurrent(ScheduleJob.CONCURRENT_IS);
			QuartzManager.addJob("htlConsumeTx", htlConsumeTx, QuartzJob.class, "0 0/20 * * * ?");
		}
		//tea
		if (properties.containsKey("tea" + suffixIp)) {
			//归集
			ScheduleJob teaCollectionTx = new ScheduleJob();
			teaCollectionTx.setBeanClass(CoinQuart.class.getName());
			teaCollectionTx.setMethodName("teaCollection");
			QuartzManager.addJob("teaCollection", teaCollectionTx, QuartzJob.class, "0 0 0/2 * * ?");

			ScheduleJob teaSycnTx = new ScheduleJob();
			teaSycnTx.setBeanClass(CoinQuart.class.getName());
			teaSycnTx.setMethodName("teaCoinSync");
			QuartzManager.addJob("teaCoinSync", teaSycnTx, QuartzJob.class, "0 0/5 * * * ?");
		}
	}
}
