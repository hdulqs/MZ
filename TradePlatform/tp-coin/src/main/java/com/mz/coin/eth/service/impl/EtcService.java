package com.mz.coin.eth.service.impl;

import com.alibaba.fastjson.JSON;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.mz.Constant;
import com.mz.coin.coin.model.AppCoinTransaction;
import com.mz.coin.coin.service.AppCoinTransactionService;
import com.mz.coin.eth.client.AdminClient;
import com.mz.coin.eth.client.RpcHttpClient;
import com.mz.coin.utils.RedisUtil;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.coin.Wallet;
import com.mz.ex.digitalmoneyAccount.service.ExDigitalmoneyAccountService;
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import com.mz.utils.CommonUtil;
import com.mz.utils.Properties;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.nutz.json.Json;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.admin.JsonRpc2_0Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

/**
 * 
 * <p>
 * TODO
 * </p>
 * 
 * @author: Shangxl
 * @Date : 2017年9月5日 下午6:57:46
 */
public class EtcService {
	// 配置信息
	private static String coinType = Constant.ETC;
	private static Map<String, String> properties = Properties.appcoinMap();
	private static String protocol = properties.get(coinType.toLowerCase() + "_protocol");
	private static String ip = properties.get(coinType.toLowerCase() + "_ip");
	private static String port = properties.get(coinType.toLowerCase() + "_port");
	public static String PASSWORD = properties.get(coinType.toLowerCase() + "_password");

	public static JsonRpc2_0Admin admin = AdminClient.getClientByParam(protocol, ip, port);
	public static JsonRpcHttpClient client = RpcHttpClient.getClientByParam(protocol, ip, port);
	public static String ACCOUNTS = coinType.toLowerCase() + "_accounts";

	/**
	 * 查询钱包所有地址
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @return
	 * @return: List<String>
	 * @Date : 2017年11月2日 下午8:11:23
	 * @throws:
	 */
	public static List<String> listAccount() {
		try {
			EthAccounts accounts = admin.ethAccounts().send();
			return accounts.getAccounts();
		} catch (Exception e) {
			LogFactory.info(coinType + "-listAccount 钱包接口异常");
		}
		return new ArrayList<>();
	}

	/**
	 * 创建新的账户
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             password
	 * @param: @return
	 * @return: String
	 * @Date : 2017年11月2日 下午8:17:30
	 * @throws:
	 */
	public static String createAddress() {
		String address = null;
		NewAccountIdentifier account = null;
		try {
			account = admin.personalNewAccount(PASSWORD).send();
			address = account.getAccountId();
			System.out.println("ETC链接：" + address);
			String eth_accounts_str = RedisUtil.getValue(ACCOUNTS);
			if (StringUtils.isNotEmpty(address) && StringUtils.isNotEmpty(eth_accounts_str)) {
				List<String> eth_accounts = JSON.parseArray(eth_accounts_str, String.class);
				if (!eth_accounts.contains(address)) {
					eth_accounts.add(address);
					RedisUtil.setValue(ACCOUNTS, Json.toJson(eth_accounts));
				}
			}
		} catch (NoRouteToHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return address;
	}

	/**
	 * 获得账号的余额(wei)
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             address
	 * @param: @return
	 * @return: BigDecimal
	 * @Date : 2017年11月2日 下午8:20:37
	 * @throws:
	 */
	public static BigInteger getBalance(String address) {
		BigInteger balance = BigInteger.ZERO;
		try {
			EthGetBalance ethBalance = admin.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
			balance = ethBalance.getBalance();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoRouteToHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return balance;
	}

	/**
	 * 查询GasPrice
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @return
	 * @return: BigInteger
	 * @Date : 2017年11月2日 下午8:29:23
	 * @throws:
	 */
	public static BigInteger getGasPrice() {
		BigInteger gasPrice = BigInteger.ZERO;
		try {
			EthGasPrice ethGasPrice = admin.ethGasPrice().send();
			gasPrice = ethGasPrice.getGasPrice();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoRouteToHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gasPrice;
	}

	/**
	 * 查询GasLimit
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @return
	 * @return: BigInteger
	 * @Date : 2017年11月2日 下午8:39:17
	 * @throws:
	 */
	public static BigInteger getGasLimit(Transaction t) {
		BigInteger gasLimit = BigInteger.ZERO;
		try {
			System.out.println("gasLimit日志信息   "+JSON.toJSON(t));
			gasLimit = admin.ethEstimateGas(t).send().getAmountUsed();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoRouteToHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gasLimit;
	}

	/**
	 * 解锁账户
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             address
	 * @param: @param
	 *             password
	 * @param: @return
	 * @return: boolean
	 * @Date : 2017年11月3日 上午10:10:24
	 * @throws:
	 */
	public static Boolean unlockAccount(String address, String password) {
		Boolean result = false;
		try {
			PersonalUnlockAccount account = admin.personalUnlockAccount(address, password).send();
			result = account.accountUnlocked();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoRouteToHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 转币
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             fromAddress
	 * @param: @param
	 *             toAddress
	 * @param: @param
	 *             amount(wei)
	 * @param: @param
	 *             password
	 * @param: @return
	 * @return: String
	 * @Date : 2017年11月2日 下午8:42:15
	 * @throws:
	 */
	public static String sendTransaction(Transaction t, String password) {
		String result = null;
		try {
			System.out.println(t.getFrom()+"_"+password);
			boolean unlock=admin.personalUnlockAccount(t.getFrom(), password, BigInteger.valueOf(20L)).send().getResult();
			if(unlock){
				result=admin.ethSendTransaction(t).send().getTransactionHash();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoRouteToHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * 根据blockNumber查询历史交易记录（暂无合适应用场景--误删）
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param:
	 * @return: void
	 * @Date : 2017年11月3日 上午11:50:54
	 * @throws:
	 */
	public static void replayTransactionsObservable() {
		admin.replayTransactionsObservable(DefaultBlockParameter.valueOf(new BigInteger("3914")), DefaultBlockParameterName.LATEST).subscribe(tx -> {
			// System.out.println(JSON.toJSONString(tx));
		});
	}


	/**
	 * 将所有用户的币都转入主钱包
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param:
	 * @return: void
	 * @Date : 2017年11月3日 下午5:59:49
	 * @throws:
	 */
	public static void send2coinBaseJob() {
		try {
			LogFactory.info(coinType + "归集到主钱包");
			ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService) ContextUtil
					.getBean("exDigitalmoneyAccountService");
			List<String> list = exDigitalmoneyAccountService.listPublicKeyByCoinCode(coinType);
			String fromAddress = null;
			String toAddress = getBasecoin();
			for (String l : list) {
				fromAddress = l;
				if (StringUtils.isNotEmpty(fromAddress) && (!fromAddress.equalsIgnoreCase(toAddress))) {
					// wei
					BigInteger totalMoney = getBalance(fromAddress);
					BigInteger price = getGasPrice();
					Transaction t = new Transaction(fromAddress, null, price, null, toAddress, totalMoney, null);
					BigInteger gas = getGasLimit(t);
					BigInteger fee = price.multiply(gas);
					BigInteger hotMoney = totalMoney.subtract(fee);
					if (hotMoney.compareTo(BigInteger.ZERO) > 0) {
						t = new Transaction(fromAddress, null, price, gas, toAddress, hotMoney, null);
						String txHash = sendTransaction(t, PASSWORD);
						LogFactory.info("转入冷钱包：" + txHash);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogFactory.info("程序错误");
		}
	}

	/**
	 * 
	 * <p>
	 * 从提币地址转出到冷钱包地址
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             toAddress
	 * @param: @param
	 *             amount
	 * @param: @return
	 * @return: JsonResult
	 * @Date : 2018年1月16日 下午2:25:08
	 * @throws:
	 */
	public static JsonResult send2ColdWallet(String toAddress, String amount) {
		JsonResult result = new JsonResult();
		String fromAddress = getBasecoin();
		BigDecimal money = Convert.toWei(new BigDecimal(amount), Unit.ETHER);
		Transaction t = new Transaction(fromAddress, null, null, null, toAddress, money.toBigInteger(), null);
		String hash = sendTransaction(t, PASSWORD);
		if (StringUtils.isNotEmpty(hash)) {
			result.setSuccess(true);
			result.setMsg(hash);
		} else {
			result.setMsg("以太坊钱包接口ERROR");
		}
		return result;
	}

	/**
	 * 获取主账户地址
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @return
	 * @return: String
	 * @Date : 2017年11月13日 下午5:34:45
	 * @throws:
	 */
	public static String getBasecoin() {
		String coinbase = null;
		try {
			coinbase = admin.ethCoinbase().send().getAddress();
		} catch (NoRouteToHostException e) {
			LogFactory.info("etc-getBasecoin 钱包接口不通");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return coinbase;
	}

	/**
	 * 查询合约币的余额
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             fromAddress
	 * @param: @param
	 *             contractAddress
	 * @param: @return
	 * @return: BigDecimal
	 * @Date : 2017年11月22日 下午1:36:43
	 * @throws:
	 */
	public static BigInteger getBalanceofContract(String fromAddress, String contractAddress) {
		BigInteger balance = BigInteger.ZERO;
		if (StringUtils.isNotEmpty(fromAddress) && StringUtils.isNotEmpty(contractAddress)) {
			Function fn = new Function("balanceOf", Arrays.asList(new Address(fromAddress)), Collections.<TypeReference<?>> emptyList());
			String data = FunctionEncoder.encode(fn);
			Map<String, String> map = new HashMap<String, String>();
			map.put("to", contractAddress);
			map.put("data", data);
			try {
				String methodName = "eth_call";
				Object[] params = new Object[] { map, "latest" };
				String result = client.invoke(methodName, params, Object.class).toString();
				if (StringUtils.isNotEmpty(result)) {
					balance = Numeric.decodeQuantity(result);
				}
			} catch (Throwable e) {
				e.printStackTrace();
				LogFactory.info("查询接口ERROR");
			}
		}
		return balance;
	}

	/**
	 * 以太坊代币转账
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             fromAddress
	 * @param: @param
	 *             toAddress
	 * @param: @param
	 *             contractAddress
	 * @param: @param
	 *             amount
	 * @param: @return
	 * @return: String
	 * @Date : 2017年11月22日 下午1:46:43
	 * @throws:
	 */
	public static String sendTransactionofContract(String fromAddress, String toAddress, String contractAddress, BigInteger amount) {
		Boolean unlock = unlockAccount(fromAddress, PASSWORD);
		if (unlock) {
			Function fn = new Function("transfer", Arrays.asList(new Address(toAddress), new Uint256(amount)), Collections.<TypeReference<?>> emptyList());
			String data = FunctionEncoder.encode(fn);
			String to = contractAddress;
			Map<String, String> map = new HashMap<String, String>();
			map.put("from", fromAddress);
			map.put("to", to);
			map.put("data", data);
			try {
				String methodName = "eth_sendTransaction";
				Object[] params = new Object[] { map };
				String result = client.invoke(methodName, params, Object.class).toString();
				if (StringUtils.isNotEmpty(result)) {
					return result;
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else {
			LogFactory.info("解锁提币账户失败");
		}
		return null;
	}

	/**
	 * 验证地址有效
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             address
	 * @param: @return
	 * @return: boolean
	 * @Date : 2017年12月27日 下午4:35:59
	 * @throws:
	 */
	public static boolean isAddress(String address) {
		// 未找到如何调用钱包 web3.isAddress的方法，暂使用此方法
		boolean result = false;
		if (address.length() == 42 && address.startsWith("0x")) {
			result = true;
		}
		return result;
	}

	/**
	 * 查询以以太坊的同步情况
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @return
	 * @return: String
	 * @Date : 2018年1月11日 下午4:04:24
	 * @throws:
	 */
	public static String syncing() {
		try {
			String methodName = "eth_syncing";
			Map<String, String> map = new HashMap<String, String>();
			Object[] params = new Object[] { map };
			String result = client.invoke(methodName, params, Object.class).toString();
			if (StringUtils.isNotEmpty(result)) {
				return result;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * 查询钱包总额、提币地址余额
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @return
	 * @return: Wallet
	 * @Date : 2018年1月16日 下午3:07:24
	 * @throws:
	 */
	public static Wallet getEtherWalletInfo() {
		Wallet wallet = new Wallet();
		String address = getBasecoin();
		BigInteger moneyInteger = getBalance(address);
		if (moneyInteger != null) {
			BigDecimal money = Convert.fromWei(String.valueOf(moneyInteger), Unit.ETHER);
			// 提币账户
			String toMoney = money.toString();
			// 查询钱包上所有账户的余额
			List<String> listAccounts = listAccount();
			// 总余额
			BigInteger totalMoney = BigInteger.ZERO;
			for (String account : listAccounts) {
				BigInteger accoutMoney = getBalance(account);
				totalMoney = totalMoney.add(accoutMoney);
			}
			BigDecimal totalMoney_ = Convert.fromWei(String.valueOf(totalMoney), Unit.ETHER);
			String total = totalMoney_.toString();

			String coldAddress = Properties.appcoinMap().get(coinType.toLowerCase() + Properties.COLDADDERSS);
			wallet.setCoinCode(coinType);
			wallet.setWithdrawalsAddress(address);
			wallet.setColdwalletAddress(coldAddress == null ? "" : coldAddress);
			wallet.setWithdrawalsAddressMoney(toMoney);
			wallet.setTotalMoney(total);
		}
		return wallet;
	}

	/**
	 * <p>
	 * 提币
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             amount
	 * @param: @param
	 *             toAddress
	 * @param: @return
	 * @return: JsonResult
	 * @Date : 2018年1月16日 下午4:05:43
	 * @throws:
	 */
	public static JsonResult sendFrom(String amount, String toAddress) {
		JsonResult result = new JsonResult();

		String fromAddress = getBasecoin();
		// 单位转化为wei
		BigDecimal money = Convert.toWei(new BigDecimal(amount), Unit.ETHER);
		BigInteger value = money.toBigInteger();
		// 转出地址余额wei
		BigInteger fromAddressMoney = getBalance(fromAddress);
		BigInteger price = getGasPrice();
		org.web3j.protocol.core.methods.request.Transaction transaction = new org.web3j.protocol.core.methods.request.Transaction(fromAddress, BigInteger.valueOf(0), price, BigInteger.valueOf(0), toAddress, value, "0x0");
//		BigInteger gas = getGasLimit(transaction);
		BigInteger gas=null;
		// 判断钱包余额是否大于等于提币金额
		// 区块链交易消耗由用户支付（note：提币1个实际到账可能0.995）
//		BigInteger needfunds = price.multiply(gas).add(value);
		BigDecimal needMoneyDecimal = Convert.toWei(new BigDecimal("0.001"), Unit.ETHER);
		BigInteger needMoney = needMoneyDecimal.toBigInteger();
		if (fromAddressMoney != null && fromAddressMoney.compareTo(needMoney) >= 0) {
			transaction = new org.web3j.protocol.core.methods.request.Transaction(fromAddress, null, price, gas, toAddress, value, "");
			String txHash = sendTransaction(transaction, PASSWORD);
			if (StringUtils.isNotEmpty(txHash)) {
				result.setSuccess(true);
				result.setMsg(txHash);
			} else {
				result.setMsg("提币失败");
			}
		} else {
			result.setMsg("ETC提币账户余额不足,至少需要" + Convert.fromWei(new BigDecimal(needMoney.toString()), Unit.ETHER));
		}
		return result;
	}

	/**
	 * <p>
	 * 合约币提币
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             type
	 * @param: @param
	 *             amount
	 * @param: @param
	 *             toAddress
	 * @param: @return
	 * @return: JsonResult
	 * @Date : 2018年1月16日 下午4:39:23
	 * @throws:
	 */
	public static JsonResult smartContract_sendFrom(String type, String amount, String toAddress) {
		JsonResult result = new JsonResult();
		type = type.toUpperCase();
		String unit = properties.get(type + Constant.DECIMALS);
		String contractAddress = properties.get(type);
		String fromAddress = getBasecoin();
		if (StringUtils.isNotEmpty(unit) && StringUtils.isNotEmpty(contractAddress) && StringUtils.isNotEmpty(fromAddress)) {
			// 查询提币地址余额
			BigInteger rawFromAddressMoney = getBalanceofContract(fromAddress, contractAddress);
			BigDecimal fromAddressMoney = new BigDecimal(rawFromAddressMoney.toString());
			// 设置手续费
			BigDecimal fee = BigDecimal.valueOf(0.005d);
			fromAddressMoney = fromAddressMoney.subtract(fee);
			// 单位转化为wei
			BigDecimal money = com.mz.utils.Convert.toWei(new BigDecimal(amount), com.mz.utils.Convert.Unit.fromString(unit));
			if (fromAddressMoney.compareTo(money) >= 0) {
				String hash = sendTransactionofContract(fromAddress, toAddress, contractAddress, money.toBigInteger());
				if (StringUtils.isNotEmpty(hash)) {
					result.setSuccess(true);
					result.setMsg(hash);
				} else {
					result.setMsg("合约币提币失败");
				}
			} else {
				result.setMsg("提币地址余额不足");
			}
		} else {
			result.setMsg("合约币提币失败,配置错误");
		}
		return result;
	}

	/**
	 * <p>
	 * 合约币查询余额
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             coinCode
	 * @param: @return
	 * @return: String[]
	 * @Date : 2018年1月18日 上午11:13:50
	 * @throws:
	 */
	public static Wallet smartContractGetWalletInfo(String coinCode) {
		coinCode = coinCode.toUpperCase();
		Wallet wallet = new Wallet();
		String unit = properties.get(coinCode + Constant.DECIMALS);
		String contractAddress = properties.get(coinCode);
		String address = getBasecoin();
		BigInteger moneyInteger = getBalanceofContract(address, contractAddress);
		if (moneyInteger != null) {
			BigDecimal money = com.mz.utils.Convert.fromWei(String.valueOf(moneyInteger), com.mz.utils.Convert.Unit.fromString(unit));
			// 提币账户
			String toMoney = money.toString();
			// 查询钱包上所有账户的余额
			List<String> listAccounts = listAccount();
			// 总余额
			BigInteger totalMoney = BigInteger.ZERO;
			for (String account : listAccounts) {
				BigInteger accoutMoney = getBalanceofContract(account, contractAddress);
				totalMoney = totalMoney.add(accoutMoney);
			}
			BigDecimal totalMoney_ = com.mz.utils.Convert.fromWei(String.valueOf(totalMoney), com.mz.utils.Convert.Unit.fromString(unit));
			String total = totalMoney_.toString();
			String coldAddress = Properties.appcoinMap().get(Constant.ETHER.toLowerCase() + Properties.COLDADDERSS);

			wallet.setCoinCode(coinCode);
			wallet.setColdwalletAddress(coldAddress == null ? "" : coldAddress);
			wallet.setWithdrawalsAddress(address);
			wallet.setWithdrawalsAddressMoney(toMoney);
			wallet.setTotalMoney(total);
		}
		return wallet;
	}

	/**
	 * 判断是否是合约币
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @return
	 * @return: boolean
	 * @Date : 2018年1月22日 下午1:49:52
	 * @throws:
	 */
	public static boolean isSmartContractCoin(String coinType) {
		System.out.println(coinType.toUpperCase());
		String value = Properties.appcoinMap().get(coinType.toUpperCase());
		System.out.println(value);
		if (value != null && value.startsWith(CommonUtil.HEXPREFIX)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取钱包同步的区块高度
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @return
	 * @return: BigInteger
	 * @Date : 2018年1月26日 下午6:47:29
	 * @throws:
	 */
	public static BigInteger getBlockNumber() {
		try {
			return admin.ethBlockNumber().send().getBlockNumber();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return BigInteger.ZERO;
	}

	/**
	 * 根据区块高度查询记录
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             blockNumber
	 * @param: @param
	 *             isdetail
	 * @return: void
	 * @Date : 2018年1月26日 下午6:48:39
	 * @throws:
	 */
	public static Block GetBlockByNumbe(BigInteger number, boolean isdetail) {
		DefaultBlockParameter blockNumber = DefaultBlockParameter.valueOf(number);
		try {
			return admin.ethGetBlockByNumber(blockNumber, isdetail).send().getBlock();
		} catch (IOException e) {
			LogFactory.info(coinType + "-geth连接错误");
			// e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 产生以太经典充币记录
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param:
	 * @return: void
	 * @Date : 2018年1月26日 下午7:02:27
	 * @throws:
	 */
	public static void productionTx() {
		// 可能会漏掉，需要再次考虑逻辑
		BigInteger blockNumber = getBlockNumber().subtract(BigInteger.valueOf(15L));
		System.out.println(coinType + "-" + blockNumber);
		BigInteger count = BigInteger.valueOf(15L);
		for (int i = 1; i < count.intValue(); i++) {
			blockNumber = blockNumber.add(BigInteger.ONE);
			Block block = GetBlockByNumbe(blockNumber, true);
			if (block != null) {
				List<TransactionResult> transactions = block.getTransactions();
				for (TransactionResult l : transactions) {
					org.web3j.protocol.core.methods.response.Transaction tx = (org.web3j.protocol.core.methods.response.Transaction) l;
					recordGethTx(tx);
				}
			}
		}

	}

	/**
	 * 根据has值获取交易明细
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             hash
	 * @param: @return
	 * @return: org.web3j.protocol.core.methods.response.Transaction
	 * @Date : 2018年1月29日 上午10:51:51
	 * @throws:
	 */
	public static org.web3j.protocol.core.methods.response.Transaction ethGetTransactionByHash(String hash) {
		try {
			return admin.ethGetTransactionByHash(hash).send().getTransaction().get();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 重新production geth充值记录
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param:
	 * @return: void
	 * @Date : 2018年1月29日 上午10:53:48
	 * @throws:
	 */
	public static void recaptureGethTx(String hash) {
		org.web3j.protocol.core.methods.response.Transaction tx = ethGetTransactionByHash(hash);
		LogFactory.info("重新刷新geth-" + coinType + "充币记录：" + hash + "------json:" + Json.toJson(tx));
		recordGethTx(tx);
	}

	/**
	 * 记录以太坊及代币的充值记录
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param:
	 * @return: void
	 * @Date : 2018年1月29日 上午11:25:05
	 * @throws:
	 */
	public static void recordGethTx(org.web3j.protocol.core.methods.response.Transaction tx) {
		if (tx != null) {
			AppCoinTransactionService txService = (AppCoinTransactionService) ContextUtil.getBean("appCoinTransactionService");
			String to = tx.getTo();
			String from = tx.getFrom();
			String eth_accounts_str = RedisUtil.getValue(ACCOUNTS);
			if (to != null && eth_accounts_str.contains(to) && txService.existNumber(tx.getHash()) == 0) {// 以太坊
				AppCoinTransaction t = new AppCoinTransaction();
				t.setHash_(tx.getHash());
				t.setTransactionIndex(tx.getTransactionIndex().toString());
				t.setBlockNumber(tx.getBlockNumber().toString());
				t.setBlockHash(tx.getBlockHash());
				t.setFrom_(from);
				t.setTo_(to);
				t.setCoinType(coinType);
				t.setAmount(Convert.fromWei(tx.getValue().toString(), Unit.ETHER));
				t.setGas(new BigDecimal(tx.getGas().toString()));
				t.setGasPrice(new BigDecimal(tx.getGasPrice().toString()));
				t.setIsconsume(0);
				txService.save(t);
			}
		}
	}

	/**
	 * 记录etc充币记录
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             tx
	 * @return: void
	 * @Date : 2018年3月2日 下午1:39:34
	 * @throws:
	 */
	public static void recordEtcTx(org.web3j.protocol.core.methods.response.Transaction tx) {
		if (tx != null) {
			AppCoinTransactionService txService = (AppCoinTransactionService) ContextUtil.getBean("appCoinTransactionService");
			String to = tx.getTo();
			String from = tx.getFrom();
			String ACCOUNTS_str = RedisUtil.getValue(ACCOUNTS);
			if (to != null && ACCOUNTS_str.contains(to) && txService.existNumber(tx.getHash()) == 0&&tx.getValue()!=null&&BigInteger.ZERO.compareTo(tx.getValue())<0) {
				AppCoinTransaction t = new AppCoinTransaction();
				t.setHash_(tx.getHash());
				t.setTransactionIndex(tx.getTransactionIndex().toString());
				t.setBlockNumber(tx.getBlockNumber().toString());
				t.setBlockHash(tx.getBlockHash());
				t.setFrom_(from);
				t.setTo_(to);
				t.setCoinType(coinType);
				t.setAmount(Convert.fromWei(tx.getValue().toString(), Unit.ETHER));
				t.setGas(new BigDecimal(tx.getGas().toString()));
				t.setGasPrice(new BigDecimal(tx.getGasPrice().toString()));
				t.setIsconsume(0);
				txService.save(t);
			}
		}
	}

	/**
	 * 加载ACCOUNTS到redis
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param:
	 * @return: void
	 * @Date : 2018年1月29日 下午12:22:20
	 * @throws:
	 */
	public static void loadAccounts2Redis() {
		ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService) ContextUtil.getBean("exDigitalmoneyAccountService");
		List<String> listPublicKey = exDigitalmoneyAccountService.listPublicKeyByCoinCode(coinType);
		RedisUtil.setValue(ACCOUNTS, Json.toJson(listPublicKey));
	}

}
