package com.mz.coin.bds;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.mz.Constant;
import com.mz.coin.BtsServer;
import com.mz.coin.coin.model.CoinTransaction;
import com.mz.coin.coin.service.CoinTransactionService;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.coin.Wallet;
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import com.mz.utils.Properties;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
/**
 * 
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2018年3月13日 下午8:13:38
 */
public class BdsServerImpl implements BtsServer {

	private JsonRpcHttpClient client = BdsRpcHttpClient.getClient();
	private String coinType=Constant.BDS;

	@Override
	public BigDecimal getBalance(String accountName) {
		BigDecimal balance = BigDecimal.ZERO;
		String methodName = "list_account_balances";
		List<String> list = new ArrayList<>();
		List<Map<String,Object>> result = null;
		list.add(accountName);
		try {
			result = client.invoke(methodName, list, List.class);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (result != null && !result.isEmpty()) {
			Map<String, Object> map = (Map<String, Object>) result.get(0);
			String amountStr = map.get("amount").toString();
			balance = new BigDecimal(amountStr).divide(BigDecimal.valueOf(100000L), 8, BigDecimal.ROUND_DOWN);
		} else {
			LogFactory.info("未查询到数据" + "accountName=" + accountName);
		}
		return balance;
	}

	@Override
	public String getPublicKey(String accountNum) {
		String chargeAccount = BdsRpcHttpClient.CHARGE_ACCOUNT;
		String memo = accountNum;
		return "ACCOUNT:" + chargeAccount + ",MEMO:" + memo;
	}

	@Override
	public boolean unlock(String password) {
		boolean result = false;
		// 需要先解锁钱包，否则看不到memo
		String unlockMethodName = "unlock";
		List<String> unlockparams = new ArrayList<>();
		unlockparams.add(password);
		try {
			Object unlockResult = client.invoke(unlockMethodName, unlockparams, Object.class);
			if (unlockResult == null) {
				result = true;
			}
		} catch (ConnectException e) {
			LogFactory.info("unlock-bds钱包拒绝连接");
		} catch (Throwable e) {
			LogFactory.info("unlock-bds钱包接口error");
		}
		return result;
	}

	@Override
	public boolean lock() {
		boolean result = false;
		// 需要先解锁钱包，否则看不到memo
		String unlockMethodName = "lock";
		List<String> unlockparams = new ArrayList<>();
		try {
			Object unlockResult = client.invoke(unlockMethodName, unlockparams, Object.class);
			if (unlockResult == null) {
				result = true;
			}
		} catch (ConnectException e) {
			LogFactory.info("lock-bds钱包拒绝连接");
		} catch (Throwable e) {
			LogFactory.info("lock-bds钱包接口error");
		}
		return result;
	}

	@Override
	public void getAccountHistory(String accountName, String count, String id, String password) {
		CoinTransactionService coinTransactionService=(CoinTransactionService) ContextUtil.getBean("coinTransactionService");
		// 查询历史记录
		if (this.unlock(password)) {
			String methodName = "get_account_history1";
			List<String> list = new ArrayList<>();
			list.add(accountName);
			list.add(count);
			list.add(id);
			List<Map> history = new ArrayList<>();
			try {
				history = client.invoke(methodName, list, List.class);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			for (Map<String, Object> l : history) {
				String memo = l.get("memo").toString();
				if (StringUtils.isNotEmpty(memo)) {
					Map<String, Object> op = (Map<String, Object>) l.get("op");
					if (op != null) {
						int blockNum = (int) op.get("block_num");
						String txId = op.get("id").toString();
						List<Map<String, Object>> opsub = (List<Map<String, Object>>) op.get("op");
						if (opsub != null && opsub.get(1) != null) {
							Map<String, Object> data = opsub.get(1);
							Map<String, Object> feeMap = (Map<String, Object>) data.get("fee");
							Map<String, Object> amountMap = (Map<String, Object>) data.get("amount");

							String feeStr = feeMap.get("amount").toString();
							String amountStr = amountMap.get("amount").toString();
							Double fee = Double.valueOf(feeStr) / 100000;
							Double amount = Double.valueOf(amountStr) / 100000;
							int blockNumber = blockNum;
							String address = this.getPublicKey(memo);
							String time = System.currentTimeMillis() + "";
							time = time.substring(0, time.length() - 3);
							String txIdType = txId + "_transfer";
							CoinTransaction transaction = new CoinTransaction();
							transaction.setTime(time);
							transaction.setTimeReceived(time);
							transaction.setBlockTime(time);
							transaction.setAddress(address);
							transaction.setCoinType(coinType);
							transaction.setConfirmations(1);
							transaction.setTxId(txId);
							transaction.setTxIdType(txIdType);
							transaction.setFee(fee);
							transaction.setAmount(amount);
							transaction.setBlockIndex(blockNumber);
							if(coinTransactionService.isExists(txIdType)==0){
								coinTransactionService.save(transaction);
							}
						}
					} else {
						LogFactory.info(coinType+"-op未查询到数据");
					}
				} else {
					LogFactory.info(coinType+"没有备注的交易");
				}
			}
		} else {
			LogFactory.info("解锁失败");
		}
		this.lock();
	}

	@Override
	public String transfer(String fromAccount, String toAccount, String amount, String symbol, String memo) {
		String txId = null;
		String password = BdsRpcHttpClient.WALLET_PASSWORD;
		if (this.unlock(password)) {
			String methodname = "transfer2";
			List<Object> list = new ArrayList<>();
			list.add(fromAccount);
			list.add(toAccount);
			list.add(amount);
			list.add(symbol);
			list.add(memo);
			try {
				Object result = client.invoke(methodname, list, Object.class);
				// 转账有个确认机制、一定需要注意
				List<String> data = (List<String>) result;
				if (data != null && !data.isEmpty()) {
					txId = data.get(0);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else {
			LogFactory.info("解锁失失败");
		}
		this.lock();
		return txId;
	}

	@Override
	public JsonResult send2ColdAddress(String toAddress, String amount) {
		JsonResult result = new JsonResult();
		String memo = BdsRpcHttpClient.memo;
		String coldAccount = toAddress;
		String fromAccount = BdsRpcHttpClient.CHARGE_ACCOUNT;
		String txHash = this.transfer(fromAccount, coldAccount, amount, coinType, memo);
		if (StringUtils.isNotEmpty(txHash)) {
			result.setSuccess(true);
			result.setMsg(txHash);
		} else {
			result.setMsg("BDS转币接口ERROR");
		}
		return result;
	}

	@Override
	public JsonResult sendFrom(String amount, String toAddress, String memo) {
		System.out.println(this.coinType);
		JsonResult result = new JsonResult();

		String fromAccount = BdsRpcHttpClient.CHARGE_ACCOUNT;
		BigDecimal money = BigDecimal.valueOf(Double.valueOf(amount));
		// 旷工费暂时设置为0.5
		BigDecimal fee = BigDecimal.valueOf(0.5d);
		BigDecimal chargeAccountMoney = this.getBalance(fromAccount).subtract(fee);
		if (chargeAccountMoney.compareTo(money) >= 0) {
			String txId = this.transfer(fromAccount, toAddress, amount, coinType, memo);
			if (StringUtils.isNotEmpty(txId)) {
				result.setSuccess(true);
				result.setMsg(txId);
			} else {
				result.setMsg("错误、提币账户余额不足");
			}
		} else {
			result.setMsg("提币账户可用余额不足");
		}
		return result;
	}

	@Override
	public Wallet getWalletInfo() {
		Wallet wallet=new Wallet();
		String chargeAccount=BdsRpcHttpClient.CHARGE_ACCOUNT;
		String total = this.getBalance(chargeAccount).toString();
		String toMoney = total;
		String coldAddress=Properties.appcoinMap().get(coinType.toLowerCase() + Properties.COLDADDERSS);
		
		wallet.setCoinCode(coinType);
		wallet.setColdwalletAddress(coldAddress==null?"":coldAddress);
		wallet.setWithdrawalsAddress(chargeAccount==null?"":chargeAccount);
		wallet.setWithdrawalsAddressMoney(toMoney);
		wallet.setTotalMoney(total);
		return wallet;
	}

	@Override
	public void getRlativeAccountHistory(String accountName, String startnum, String count, String endnum) {}
}
