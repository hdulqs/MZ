package com.mz.remote;

import com.mz.manage.remote.model.AppLogThirdInterfaceDTO;
import com.mz.manage.remote.model.LmcTransfer;

/**
 * 第三方通用接口
 * 1.LMC
 * <p> TODO</p>
 * @author:         Shangxl 
 * @Date :          2017年7月27日 下午2:48:46
 */
public interface RemoteThirdInterfaceService {
	
	/**
	 * LMC项目创建钱包地址
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param account_id 用编号
	 * @param:    @param symbol 币种类型
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年7月27日 下午2:52:41   
	 * @throws:
	 */
	public String createCoinAddress(String account_id,String symbol);
	
	/**
	 * LMC 钱包-钱包转账
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param transfer
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2017年7月31日 下午3:21:20   
	 * @throws:
	 */
	public String[] lmcTransfer(LmcTransfer transfer);
	
	/**
	 * LMC查询账单总和
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param transfer
	 * @param:    @return
	 * @return: String[] 
	 * @Date :          2017年7月31日 下午5:44:54   
	 * @throws:
	 */
	public String [] walletTransferSum(LmcTransfer transfer);
	
	/**
	 * LMC 获取账单列表
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param transfer
	 * @param:    @return
	 * @return: String[] 
	 * @Date :          2017年7月31日 下午6:19:49   
	 * @throws:
	 */
	public String[] listwalletTransfer(LmcTransfer transfer);
	
	/**
	 * 保存或更新接口调用log
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param logDTO
	 * @return: AppLogThirdInterfaceDTO 
	 * @Date :          2017年7月31日 下午7:54:39   
	 * @throws:
	 */
	public AppLogThirdInterfaceDTO saveLog(AppLogThirdInterfaceDTO logDTO);
	
}
