package com.mz.coin.neo;

import java.util.List;
import java.util.Map;

public interface NeoService {
	
	/**验证地址是否是正确的 NEO 地址
	 * 
	 * @param validateaddress
	 * @return
	 */
	public boolean validateAddress(String validateaddress);
	
	/**
	 * 根据账户地址，查询账户资产信息
	 * 
	 * @param address
	 * @return
	 */
	public Map getaccountstate(String address);
	
	/**
	 * 根据指定的索引，返回对应的区块信息
	 * 
	 * @param index 区块索引（区块高度） = 区块数 - 1。
	 * @return
	 */
	public List<NeoEntity> getblock(String index);
	
	
	/**
	 * 获取主链中区块的数量
	 * 
	 * @return
	 */
	public int getblockcount();
	
	
	/**
	 * 从指定地址，向指定地址转账。
	 * @param asset_id 资产id
	 * @param from 转账地址
	 * @param to 收款地址
	 * @param value 转账金额
	 * @param fee 手续费，可选参数，默认为 0
	 * @return
	 */
	public Map sendfrom(String asset_id,String from,String to,String value,String fee);

}
