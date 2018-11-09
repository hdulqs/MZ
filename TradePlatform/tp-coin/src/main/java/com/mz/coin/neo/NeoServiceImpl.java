package com.mz.coin.neo;

import com.alibaba.fastjson.JSONObject;
import com.mz.coin.BtsServer;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.coin.Wallet;
import com.mz.util.log.LogFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NeoServiceImpl implements BtsServer, NeoService {
	/**
	 * 根据指定的资产编号，返回钱包中对应资产的余额信息
	 * 
	 */
	@Override
	public BigDecimal getBalance(String accountName) {
		BigDecimal balance = BigDecimal.ZERO;
		String methodname = "getbalance";
		Object result = "";
		String amount_ = "";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List paramlist = new ArrayList<>();
			Map addressMap = new HashMap(16);
			String id="";
			addressMap = getaccountstate(accountName);
			if (null == addressMap) {
				LogFactory.info("未查询到账户资产信息");
				return balance;
			} else {
				id = addressMap.get("asset") + "";
			}
			if ("0".equals(id)) {
				LogFactory.info("未查询到账户资产信息");
				return balance;
			}
			paramlist.add('"' + id + '"');
			result = NeoRpcHttpClient.getHttpClient(methodname, paramlist);
			LogFactory.info(result + "");
			JSONObject json = JSONObject.parseObject(result + "");
			if (json == null) {

			} else {
				map = (Map<String, Object>) json.get("result");
				amount_ = map.get("balance") + "";
				balance = BigDecimal.valueOf(Double.valueOf(amount_));
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogFactory.error("生成neo钱包中对应资产的余额信息失败！");
		}

		return balance;
	}

	/**
	 * <p>
	 * 生成地址、备注
	 * </p>
	 * 
	 * @author: zzz
	 * @param: @param
	 *             userName
	 * @param: @return
	 * @return: String
	 * @Date : 2018年1月9日 下午2:57:08
	 * @throws:
	 */
	@Override
	public String getPublicKey(String userName) {
		String methodname = "getnewaddress";
		Object result = "";
		String address = "";
		List list = new ArrayList<>();
		try {
			List paramlist = new ArrayList<>();
			result = NeoRpcHttpClient.getHttpClient(methodname, paramlist);
			LogFactory.info(result + "");
			JSONObject json = JSONObject.parseObject(result + "");
			address = json.get("result") + "";
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogFactory.error("生成neo地址失败！");
		}
		return address;
	}

	@Override
	public boolean unlock(String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean lock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void getAccountHistory(String accountName, String count, String id, String password) {
		// TODO Auto-generated method stub
	}

	@Override
	public String transfer(String fromAccount, String toAccount, String amount, String symbol, String memo) {
		return null;
	}

	/**
	 * 根据账户地址，查询账户资产信息
	 * 
	 * @param address
	 * @return
	 */
	@Override
	public Map getaccountstate(String address) {
		Map resultMap = new HashMap();
		String asset = "";
		String methodname = "getaccountstate";
		Object result = "";
		List list = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List paramlist = new ArrayList<>();
			paramlist.add('"' + address + '"');
			result = NeoRpcHttpClient.getHttpClient(methodname, paramlist);
			LogFactory.info(result + "");
			JSONObject json = JSONObject.parseObject(result + "");
			map = (Map<String, Object>) json.get("result");
			List<Map> listresult = new <Map>ArrayList();
			listresult = (List<Map>) map.get("balances");
			if (listresult == null || listresult.size() == 0) {
				return resultMap;
			} else {
				resultMap = listresult.get(0);
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogFactory.error("查询账户资产信息失败！");
		}
		return resultMap;
	}

	/**
	 * 根据指定的索引，返回对应的区块信息
	 * 
	 * @param index 区块索引（区块高度） = 区块数 - 1。
	 * @return
	 */
	@Override
	public List<NeoEntity> getblock(String index) {
		List<NeoEntity> listNeo = new ArrayList<NeoEntity>();
		List<Map> listtx=new ArrayList<Map>();
		String methodname = "getblock";
		Object result = "";
		String asset = "";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List paramlist = new ArrayList<>();
			paramlist.add(index);
			paramlist.add(1);
			result = NeoRpcHttpClient.getHttpClient(methodname, paramlist);
			LogFactory.info(result + "");
			JSONObject json = JSONObject.parseObject(result + "");
			if(json!=null){
				map = (Map<String, Object>) json.get("result");
				listtx=(List<Map>) map.get("tx");
				if(null==listtx) {
					return listNeo;
				}else {
					for(int i=0;i<listtx.size();i++) {
						if("ContractTransaction".equals(listtx.get(i).get("type"))) {
							Map mapVin=new HashMap();
							mapVin=(Map) listtx.get(i).get("vin");
							if(null==mapVin) {
								
							}else {
								NeoEntity neoEntity=new NeoEntity();
								neoEntity.setN(mapVin.get("n")+"");
								neoEntity.setAsset(mapVin.get("asset")+"");
								neoEntity.setValue(mapVin.get("value")+"");
								neoEntity.setAddress(mapVin.get("address")+"");
							}
						}
					}
				}
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogFactory.error("生成neo钱包中对应资产的余额信息失败！");
		}

		return listNeo;
	}

	@Override
	public boolean validateAddress(String validateaddress) {
		boolean flag = false;
		String methodname = "validateaddress";
		Object result = "";
		List list = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List paramlist = new ArrayList<>();
			paramlist.add('"' + validateaddress + '"');
			result = NeoRpcHttpClient.getHttpClient(methodname, paramlist);
			LogFactory.info(result + "");
			JSONObject json = JSONObject.parseObject(result + "");
			map = (Map<String, Object>) json.get("result");
			if ((boolean) map.get("isvalid")) {
				flag = true;
			}
			;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogFactory.error("生成neo地址失败！");
		}
		return flag;
	}

	@Override
	public Map sendfrom(String asset_id, String from, String to, String value, String fee) {
		Map mapresult = new HashMap();
		// 手续费fee
		if (null == fee || "".equals(fee)) {
			fee = "0";
		} else {

		}
		String asset = "";
		BigDecimal balance = BigDecimal.ZERO;
		String methodname = "sendfrom";
		Object result = "";
		String amount_ = "";
		Map<String, Object> map = new HashMap<String, Object>();
		List list = new ArrayList<>();
		try {
			List paramlist = new ArrayList<>();
			paramlist.add('"' + asset_id + '"');
			paramlist.add('"' + from + '"');
			paramlist.add('"' + to + '"');
			paramlist.add(value);
			if (fee.equals("0")) {

			} else {
				paramlist.add(fee);
			}
			result = NeoRpcHttpClient.getHttpClient(methodname, paramlist);
			LogFactory.info(result + "");
			JSONObject json = JSONObject.parseObject(result + "");
			map = (Map<String, Object>) json.get("result");
			// amount_=map.get("balance")+"";
			// balance = new BigDecimal(amount_).divide(BigDecimal.valueOf(100000l), 8,
			// BigDecimal.ROUND_DOWN);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogFactory.error("生成neo钱包中对应资产的余额信息失败！");
		}

		return mapresult;
	}

	/**
	 * 获取主链中区块的数量
	 * 
	 * @return
	 */
	@Override
	public int getblockcount() {
		int index=0;
		List<NeoEntity> listNeo = new ArrayList<NeoEntity>();
		String methodname = "getblockcount";
		Object result = "";
		String asset = "";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List paramlist = new ArrayList<>();
			result = NeoRpcHttpClient.getHttpClient(methodname, paramlist);
			LogFactory.info(result + "");
			JSONObject json = JSONObject.parseObject(result + "");
			if(json!=null&&json.get("result")!=null){
				index =Integer.parseInt(json.get("result")+"");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			LogFactory.error("获取主链中区块的数量失败！");
		}

		return index;
	}

	@Override
	public JsonResult send2ColdAddress(String toAddress, String amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonResult sendFrom(String amount, String toAddress, String memo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Wallet getWalletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getRlativeAccountHistory(String accountName, String startnum, String count, String endnum) {
		// TODO Auto-generated method stub
	}

}
