package com.mz.coin.tv;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.Constant;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.coin.Wallet;
import com.mz.util.log.LogFactory;
import com.mz.utils.Properties;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2017年3月14日 上午9:45:12
 */
public class TvUtil {
	private static TvClientFactory btsClientFactory = TvClientFactory.getSingleton();

	/**
	 * serverAddr 钱包地址 port 钱包端口 rpcUsername RPC用户名 rpcPassword RPC 密码 walleName
	 * 钱包用户名 wallePassword 钱包密码 openTime 钱包打开时间
	 */
	private static String serverAddr = Properties.appcoinMap().get(Constant.TV + "_ip");
	private static int port = Integer.valueOf(Properties.appcoinMap().get(Constant.TV + "_port"));
	private static String rpcUsername = Properties.appcoinMap().get(Constant.TV + "_rpcuser");
	private static String rpcPassword = Properties.appcoinMap().get(Constant.TV + "_rpcpassword");
	private static String walleName = Properties.appcoinMap().get(Constant.TV + "_walletName");
	private static String wallePassword = Properties.appcoinMap().get(Constant.TV + "_walletPassword");
	public static String withdAccount = Properties.appcoinMap().get(Constant.TV + "_withdAccount");
	public static String coldAddress_memo = Properties.appcoinMap().get(Constant.TV + "_coldAddress_memo");
	private static int openTime = 10;
	/**
	 * 精度
	 */
	public static int PRECISION=5;
	/**
	 * 旷工费比率
	 */
	private static BigDecimal FEERATIO=BigDecimal.valueOf(0.002);
	/**
	 * 最小旷工费
	 */
	private static BigDecimal MIN_FEE=BigDecimal.valueOf(0.1);
	/**
	 * 单位转化比例
	 */
	public static BigDecimal RATE=new BigDecimal("100000");
	
	/**
	 * 
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param method
	 * @param:    @param params
	 * @param:    @return
	 * @return: String 
	 * @Date :          2018年4月10日 下午2:17:16   
	 * @throws:
	 */
	public static String clientWallet(String method, List<Object> params) {
		return btsClientFactory.send(method, params, serverAddr, port, rpcUsername, rpcPassword);
	}

	/**
	 * 打开钱包
	 * 
	 * @return
	 */
	public static boolean openAndUnlockWalle() {
		boolean flag = false;
		try {
			// 打开指定钱包
			String result = "";
			JSONObject info = null;
			List<Object> openParams = new ArrayList<>();
			openParams.add(walleName);
			result = clientWallet("wallet_open", openParams);
			info = JSONObject.parseObject(result);
			if (info != null && info.containsKey("error")) {
				System.out.println("钱包开启失败");
				return flag;
			}
			// 解锁钱包
			List<Object> unlockParams = new ArrayList<>();
			unlockParams.add(openTime);
			unlockParams.add(wallePassword);
			result = clientWallet("wallet_unlock", unlockParams);
			info = JSONObject.parseObject(result);
			if (info == null || info.containsKey("error")) {
				System.out.println("解锁失败");
				return flag;
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 *
	 * @param account
	 *            钱包账户
	 * @return 钱包余额
	 */
	public static BigDecimal getbalance(String account) {
		BigDecimal balance = BigDecimal.ZERO;
		try {
			if (openAndUnlockWalle()) {
				List<Object> params = new ArrayList<>();
				params.add(account);
				String result = clientWallet("wallet_account_balance", params);
				JSONObject info = JSONObject.parseObject(result);
				if (info == null || info.containsKey("error")) {
					System.out.println("获取余额失败");
					return balance;
				}
				JSONArray array = info.getJSONArray("result");
				if (array.size() > 0) {
					JSONArray tempArr = array.getJSONArray(0).getJSONArray(1).getJSONArray(0);
					balance = new BigDecimal(tempArr.get(1).toString()).divide(RATE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return balance;
	}

	/**
	 *
	 * @param account
	 *            转账账户
	 * @param coinType
	 *            货币类型
	 * @param amount
	 *            转账数量
	 * @param coinAddr
	 *            转账地址(toAddress)
	 * @return
	 */
	public static String sendCoinToAddr(String account, String coinType, String amount, String coinAddr, String memo) {
		String entryId = "";
		try {
			if (openAndUnlockWalle()) {
				List<Object> sendParams = new ArrayList<>();
				sendParams.add(amount);
				sendParams.add(coinType);
				sendParams.add(account);
				sendParams.add(coinAddr);
				sendParams.add(memo);
				sendParams.add("");
				String resultinfo = clientWallet("wallet_transfer_to_address", sendParams);
				JSONObject obj = JSONObject.parseObject(resultinfo);
				if (obj == null || obj.containsKey("error")) {
					System.out.println("转账失败:" + resultinfo);
					return entryId;
				}
				entryId = obj.getJSONObject("result").getString("entry_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entryId;
	}

	/**
	 *
	 * @param account
	 *            账户
	 * @return 地址
	 */
	public static String createAccount(String account) {
		String addr = "";
		try {
			if (openAndUnlockWalle()) {
				List<Object> sendParams = new ArrayList<>();
				sendParams.add(account);
				String resultinfo = clientWallet("wallet_account_create", sendParams);
				JSONObject obj = JSONObject.parseObject(resultinfo);
				if (obj.containsKey("error")) {
					System.out.println("创建地址失败:" + resultinfo);
					return addr;
				}
				addr = obj.getString("result");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addr;
	}

	/**
	 * 
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @return
	 * @return: Long 
	 * @Date :          2018年1月16日 下午2:16:50   
	 * @throws:
	 */
	public static Long getBlockCount() {
		long blockCount = 0L;
		try {
			List<Object> sendParams = new ArrayList<>();
			String resultinfo = clientWallet("blockchain_get_block_count", sendParams);
			JSONObject obj = JSONObject.parseObject(resultinfo);
			if (obj == null || obj.containsKey("error")) {
				System.out.println("获取区块失败:" + resultinfo);
				return blockCount;
			}
			blockCount = obj.getLongValue("result");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blockCount;
	}

	public static String getAccountPublicAddress(String accountName) {
		try {
			List<Object> sendParams = new ArrayList<>();
			sendParams.add(accountName);
			String resultinfo = clientWallet("wallet_get_account_public_address", sendParams);
			JSONObject obj = JSONObject.parseObject(resultinfo);
			if (obj.containsKey("result") && StringUtils.isNotEmpty(obj.getString("result"))) {
				return obj.getString("result");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param startBlookNum
	 * @param:    @param endBlookNum
	 * @param:    @param account
	 * @param:    @return
	 * @return: JSONArray 
	 * @Date :          2018年1月16日 下午2:16:45   
	 * @throws:
	 */
	public static JSONArray getTransactionDetail(long startBlookNum, long endBlookNum, String account) {
		startBlookNum = 0L;
		try {
			if (openAndUnlockWalle()) {
				List<Object> sendParams = new ArrayList<>();
				sendParams.add(account);
				sendParams.add(Constant.TV.toUpperCase());
				sendParams.add(0);// 返回的交易数量上限，0代表没有限制
				sendParams.add(startBlookNum);
				sendParams.add(endBlookNum);
				String resultinfo = clientWallet("wallet_account_transaction_history", sendParams);
				JSONObject obj = JSONObject.parseObject(resultinfo);
				if (obj.containsKey("error")) {
					System.out.println("获取交易信息失败:" + resultinfo);
				}
				JSONArray arr = obj.getJSONArray("result");
				if (arr.size() > 0) {
					// System.out.println("TV获取交易记录:"+arr.toJSONString());
					return arr;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 验证一个用户名或者地址是否有效
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             address
	 * @param: @return
	 * @return: boolean
	 * @Date : 2017年12月27日 下午4:07:30
	 * @throws:
	 */
	public static boolean walletCheckAddress(String address) {
		boolean result = false;
		if (openAndUnlockWalle()) {
			List<Object> sendParams = new ArrayList<>();
			sendParams.add(address);
			String resultinfo = clientWallet("wallet_check_address", sendParams);
			JSONObject obj = JSONObject.parseObject(resultinfo);
			result = obj.getBooleanValue("result");
		}
		return result;
	}

	/**
	 * <p>
	 * 转入冷钱包
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @return
	 * @return: JsonResult
	 * @Date : 2018年1月16日 下午2:08:58
	 * @throws:
	 */
	public static JsonResult send2ColdAddress(String toAddress, String amount) {
		JsonResult result = new JsonResult();
		String fromAddress = TvUtil.withdAccount;
		String memo = TvUtil.coldAddress_memo;
		BigDecimal fee=getTxFee(new BigDecimal(amount));
		BigDecimal need = new BigDecimal(amount).add(fee);
		BigDecimal waccountHotMoney = TvUtil.getbalance(fromAddress);
		if (waccountHotMoney.compareTo(need) >= 0) {
			if (StringUtils.isNotEmpty(memo)) {
				String entryId = TvUtil.sendCoinToAddr(fromAddress, Constant.TV.toUpperCase(), amount, toAddress, memo);
				if(StringUtils.isNotEmpty(entryId)){
					result.setSuccess(true);
					result.setMsg(entryId);
				}else{
					result.setMsg("TV钱包接口ERROR");
				}
			} else {
				result.setMsg("TV冷钱包备注不能为空");
			}
		} else {
			result.setMsg("提币账户可用余额不足");
		}
		return result;
	}
	
	
	/**
	 * <p> 查询钱包总额、提币地址余额</p>
	 * @author:         shangxl
	 * @param:    @return
	 * @return: Wallet
	 * @Date :          2018年1月16日 下午3:07:24   
	 * @throws:
	 */
	public static Wallet getWalletInfo(){
		Wallet wallet=new Wallet();
		String withdrawalsAddress=TvUtil.withdAccount;
		//提币地址余额
		BigDecimal withAddressMoneyDecimal=getbalance(withdrawalsAddress);
		//总账户余额
		BigDecimal totalMoneyDecimal=getWalletTotalBalance();
		String totalMoney=totalMoneyDecimal.toString();
		String withAddressMoney = withAddressMoneyDecimal.toString();
		String coldwalletAddress=Properties.appcoinMap().get(Constant.TV + Properties.COLDADDERSS);
		wallet.setCoinCode(Constant.TV.toUpperCase());
		wallet.setColdwalletAddress(coldwalletAddress==null?"":coldwalletAddress);
		wallet.setWithdrawalsAddress(withdrawalsAddress==null?"":withdrawalsAddress);
		wallet.setTotalMoney(totalMoney);
		wallet.setWithdrawalsAddressMoney(withAddressMoney);
		return wallet;
	}
	
	/**
	 * 获取钱包中所有资产总和
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @return
	 * @return: BigDecimal 
	 * @Date :          2018年4月10日 下午1:45:41   
	 * @throws:
	 */
	public static BigDecimal getWalletTotalBalance(){
		BigDecimal total=BigDecimal.ZERO;
		try {
			if (openAndUnlockWalle()) {
				List<Object> params = new ArrayList<>();
				String result = clientWallet("wallet_account_balance", params);
				JSONObject info = JSONObject.parseObject(result);
				if (info == null || info.containsKey("error")) {
					System.out.println("获取余额失败");
				}
				JSONArray array = info.getJSONArray("result");
				if(array.size()>0){
					for(Object l:array){
						String balanceStr =JSON.parseArray(JSON.toJSONString(l)).getJSONArray(1).getJSONArray(0).get(1).toString();
						if(StringUtils.isNotEmpty(balanceStr)){
							total=total.add(new BigDecimal(balanceStr));
						}
					}
					//单位转化
					if(total.compareTo(BigDecimal.ZERO)>0){
						total=total.divide(RATE);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}
	
	
	/**
	 * <p>提币</p>
	 * @author:         shangxl
	 * @param:    @param amount
	 * @param:    @param toAddress
	 * @param:    @param memo
	 * @param:    @param transactionNum
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2018年1月16日 下午4:09:25   
	 * @throws:
	 */
	public static JsonResult sendFrom(String amount,String toAddress,String memo,String transactionNum){
		JsonResult result=new JsonResult();
		//从提币地址转出
		//判断提币账户余额是否足够
		String fromAccount=TvUtil.withdAccount;
		BigDecimal fee=getTxFee(new BigDecimal(amount));
		BigDecimal need=new BigDecimal(amount).add(fee);
		BigDecimal waccountHotMoney=TvUtil.getbalance(fromAccount);
		if(waccountHotMoney.compareTo(need)>=0){
			//每次交易手续费为0.01
			//判断地址是否有效
			if(walletCheckAddress(toAddress)){
				String txid=TvUtil.sendCoinToAddr(fromAccount, Constant.TV.toUpperCase(), amount, toAddress, memo);
				if(StringUtils.isNotEmpty(txid)){
					result.setSuccess(true);
					result.setMsg(txid);
				}else{
					String message="订单"+transactionNum+"处理失败,返回为空";
					LogFactory.info(message);
					result.setMsg(message);
				}
			}else{
				result.setMsg("地址无效");
			}
		}else{
			result.setMsg("提币账户可用余额不足");
		}
		return result;
	}
	
	/**
	 * 计算旷工费：0.2%,但最小为0.1TV
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @return
	 * @return: BigDecimal 
	 * @Date :          2018年4月4日 下午6:36:35   
	 * @throws:
	 */
	public static BigDecimal getTxFee(BigDecimal amount){
		BigDecimal fee=amount.multiply(FEERATIO);
		if(fee.compareTo(MIN_FEE)<0){
			fee=MIN_FEE;
		}
		return fee;
	}
	
	public static void main(String[] args) {
		String str="{\"id\":1,\"result\":[[\"tv00026199600868\",[[0,2687]]],[\"tvwtaccount\",[[0,117414607]]]]}";
		JSONObject info=JSON.parseObject(str);
		JSONArray array = info.getJSONArray("result");
		if(array.size()>0){
			for(Object l:array){
				String balanceStr =JSON.parseArray(JSON.toJSONString(l)).getJSONArray(1).getJSONArray(0).get(1).toString();
				System.out.println(new BigDecimal(balanceStr));
			}
		}
	}
}
