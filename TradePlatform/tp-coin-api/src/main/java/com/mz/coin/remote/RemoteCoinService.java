package com.mz.coin.remote;

import java.math.BigDecimal;

public interface RemoteCoinService {
	/**
	 * 创建以太坊钱包地址
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param password
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年9月14日 下午10:17:12   
	 * @throws:
	 */
	public String createAddress(String password);
	
	/**
	 * 
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param from
	 * @param:    @param to
	 * @param:    @param password
	 * @param:    @param amount
	 * @param:    @return
	 * @return: String[] arg0 8888(成功)/0000(失败) arg1 message:  hash/错误信息
	 * @Date :          2017年9月14日 下午10:24:20   
	 * @throws:
	 */
	public String tradeCoin(String from,String to,String password,BigDecimal amount);
}
