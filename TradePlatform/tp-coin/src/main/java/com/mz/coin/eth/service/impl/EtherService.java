package com.mz.coin.eth.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.mz.Constant;
import com.mz.coin.Wallet;
import com.mz.coin.coin.model.AppCoinTransaction;
import com.mz.coin.coin.service.AppCoinTransactionService;
import com.mz.coin.eth.client.AdminClient;
import com.mz.coin.eth.client.RpcHttpClient;
import com.mz.coin.utils.RedisUtil;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.dto.model.Token;
import com.mz.ex.digitalmoneyAccount.service.ExDigitalmoneyAccountService;
import com.mz.util.http.Httpclient;
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import com.mz.utils.CommonUtil;
import com.mz.utils.Properties;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.nutz.json.Json;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.exceptions.MessageDecodingException;
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
import org.web3j.protocol.core.methods.response.TransactionReceipt;
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
public class EtherService {
	public static JsonRpc2_0Admin admin = AdminClient.getClient();
	public static JsonRpcHttpClient client = RpcHttpClient.getClient();
	public static Map<String, String> coinMap = Properties.appcoinMap();
	public static String PASSWORD = coinMap.get("eth_password");
	/**
	 * 精度
	 */
	public static String DECIMALS = "_DECIMALS";

	public static String ETH_ACCOUNTS = "eth_accounts";

	public static String BLOCKNUMBER = "blockNumber";
	
	/**
	 * 大单位-代币归集旷工费
	 */
	public static final BigDecimal TOKEN_COLLECT_TXFEE_ETHER=BigDecimal.valueOf(0.001D);
	
	/**
	 * 小单位-代币归集旷工费
	 */
	public static final BigInteger TOKEN_COLLECT_TXFEE=Convert.toWei(TOKEN_COLLECT_TXFEE_ETHER, Unit.ETHER).toBigInteger();
	
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
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			LogFactory.info("eth-listAccount 钱包接口异常");
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
		try {
			if (admin != null) {
				NewAccountIdentifier account = admin.personalNewAccount(PASSWORD).send();
				address = account.getAccountId();
				String eth_accounts_str = RedisUtil.getValue(ETH_ACCOUNTS);
				if (StringUtils.isNotEmpty(address) && StringUtils.isNotEmpty(eth_accounts_str)) {
					List<String> eth_accounts = JSON.parseArray(eth_accounts_str, String.class);
					if (!eth_accounts.contains(address)) {
						eth_accounts.add(address);
						RedisUtil.setValue(ETH_ACCOUNTS, Json.toJson(eth_accounts));
					}
				}
			} else {
				LogFactory.info("admin is null");
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
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
			if (admin != null) {
				EthGetBalance ethBalance = admin.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
				balance = ethBalance.getBalance();
			} else {
				LogFactory.info("admin is null");
			}
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
			if (admin != null) {
				EthGasPrice ethGasPrice = admin.ethGasPrice().send();
				gasPrice = ethGasPrice.getGasPrice();
			} else {
				LogFactory.info("admin is null");
			}
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
			if (admin != null) {
				gasLimit = admin.ethEstimateGas(t).send().getAmountUsed();
			} else {
				LogFactory.info("admin is null");
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoRouteToHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessageDecodingException e) {
			LogFactory.error("钱包同步异常");
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
			if (admin != null) {
				PersonalUnlockAccount account = admin.personalUnlockAccount(address, password).send();
				result = account.accountUnlocked();
			} else {
				LogFactory.info("admin is null");
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
			if (admin != null) {
				result = admin.personalSendTransaction(t, password).send().getTransactionHash();
			} else {
				LogFactory.info("admin is null");
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
	 * 根据钱包地址查询所有的交易记录(免费第三方接口、不稳定)
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Shangxl
	 * @param: @param
	 *             address
	 * @param: @return
	 * @return: List<com.mz.exchange.coin.model.Transaction>
	 * @Date : 2017年9月12日 下午2:37:20
	 * @throws:
	 */
	@Deprecated
	public static List<AppCoinTransaction> listTxByaddress(Map<String, String> map, String url) throws RuntimeException {
		String result = Httpclient.get(url, map);
		JSONObject data = JSON.parseObject(result);
		if (data != null && data.get("message").equals("OK")) {
			List<AppCoinTransaction> list = JSON.parseArray(data.getString("result"), AppCoinTransaction.class);
			return list;
		}
		return null;
	}

	/**
	 * 订阅历史交易记录和最新的交易记录（观察者模式） 初次启动会显示历史记录，有新增再通知
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param:
	 * @return: void
	 * @Date : 2017年11月3日 上午11:34:52
	 * @throws:
	 */
	@Deprecated // 废弃原因：性能太差
	public static void catchUpToLatestAndSubscribeToNewTransactionsObservable() {

		String ETH = Constant.ETHER;
		LogFactory.info("开始订阅geth交易记录");
		AppCoinTransactionService txService = (AppCoinTransactionService) ContextUtil.getBean("appCoinTransactionService");
		DefaultBlockParameter startblockNumber = DefaultBlockParameterName.EARLIEST;
		// 如果数据库中已经存在历史记录，则用历史记录blockNumber作为开始块号
		BigInteger lastestBlockNumber = null;
		try {
			lastestBlockNumber = txService.getLastestBlock();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果数据为空则用配置文件中的块号
		if (lastestBlockNumber != null) {
			startblockNumber = DefaultBlockParameter.valueOf(lastestBlockNumber);
		} else {
			startblockNumber = DefaultBlockParameter.valueOf(getBlockNumber());
		}

		if (admin != null) {
			// 每次都查询接口，需要考虑性能问题、放在外面新注册用户又不能获取充值记录
			List<String> eth_accounts = listAccount();
			RedisUtil.setValue(ETH_ACCOUNTS, Json.toJson(eth_accounts));
			admin.catchUpToLatestAndSubscribeToNewTransactionsObservable(startblockNumber).subscribe(tx -> {
				System.out.println(tx.getBlockNumber());
				String to = tx.getTo();
				String from = tx.getFrom();

				String eth_accounts_str = RedisUtil.getValue(ETH_ACCOUNTS);
				if (eth_accounts_str.contains(to) && txService.existNumber(tx.getHash()) == 0) {// 以太坊
					AppCoinTransaction t = new AppCoinTransaction();
					t.setHash_(tx.getHash());
					t.setTransactionIndex(tx.getTransactionIndex().toString());
					t.setBlockNumber(tx.getBlockNumber().toString());
					t.setBlockHash(tx.getBlockHash());
					t.setFrom_(from);
					t.setTo_(to);
					t.setCoinType(ETH);
					t.setAmount(Convert.fromWei(tx.getValue().toString(), Unit.ETHER));
					t.setGas(new BigDecimal(tx.getGas().toString()));
					t.setGasPrice(new BigDecimal(tx.getGasPrice().toString()));
					t.setIsconsume(0);
					txService.save(t);
				} else if (coinMap.containsKey(to)) {// 平台的代币
					String input = tx.getInput();
					if (StringUtils.isNotEmpty(input)) {
						String data = input.substring(0, 9);
						data = data + input.substring(17, input.length());
						Function function = new Function("transfer", Arrays.asList(), Arrays.asList(new TypeReference<Address>() {
						}, new TypeReference<Uint256>() {
						}));
						List<Type> params = FunctionReturnDecoder.decode(data, function.getOutputParameters());
						// 充币地址
						String toAddress = params.get(0).getValue().toString();
						String amount = params.get(1).getValue().toString();
						if (eth_accounts_str.contains(toAddress) && StringUtils.isNotEmpty(amount) && txService.existNumber(tx.getHash()) == 0) {
							// 币种
							String coinCode = coinMap.get(to);
							// 精度
							String unit = coinMap.get(coinCode + DECIMALS);

							if (StringUtils.isNotEmpty(coinCode) && StringUtils.isNotEmpty(unit)) {
								AppCoinTransaction t = new AppCoinTransaction();
								t.setHash_(tx.getHash());
								t.setInput(tx.getInput());
								t.setTransactionIndex(tx.getTransactionIndex().toString());
								t.setBlockNumber(tx.getBlockNumber().toString());
								t.setBlockHash(tx.getBlockHash());
								t.setFrom_(from);
								t.setTo_(toAddress);
								t.setCoinType(coinCode);
								t.setAmount(com.mz.utils.Convert.fromWei(amount, com.mz.utils.Convert.Unit.fromString(unit)));
								t.setGas(new BigDecimal(tx.getGas().toString()));
								t.setGasPrice(new BigDecimal(tx.getGasPrice().toString()));
								t.setIsconsume(0);
								txService.save(t);
							} else {
								LogFactory.info("代币配置错误，请检查：  coinCode=" + coinCode + "  unit=" + unit);
							}
						}
					}
				}

			});
		} else {
			LogFactory.info("geth钱包rpc连接ERROR");
		}

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
		try {
			admin.replayTransactionsObservable(DefaultBlockParameter.valueOf(new BigInteger("3914")), DefaultBlockParameterName.LATEST).subscribe(tx -> {
				System.out.println(JSON.toJSONString(tx));
			});
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
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
			LogFactory.info("以太坊资金归集到主钱包-01");
			// 首先查询昨天所有的充值记录
			ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService) ContextUtil.getBean("exDigitalmoneyAccountService");
			List<String> list = exDigitalmoneyAccountService.listPublicKeyByCoinCode(Constant.ETHER);
			String fromAddress = null;
			String toAddress = getBasecoin();
			for (String l : list) {
				fromAddress = l;
				if (StringUtils.isNotEmpty(fromAddress) && (!fromAddress.equalsIgnoreCase(toAddress))) {
					// wei(小单位)
					BigInteger totalMoney = getBalance(fromAddress);
					BigInteger price = getGasPrice();
					Transaction t = new Transaction(fromAddress, null, price, null, toAddress, totalMoney, null);
					BigInteger gas = getGasLimit(t);
					BigInteger fee = price.multiply(gas);
					//减去预留的代币的旷工费
					BigInteger hotMoney = totalMoney.subtract(TOKEN_COLLECT_TXFEE.add(fee));
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
		}catch (ConnectException e) {
			LogFactory.info("geth连接错误");
		} catch (NoRouteToHostException e) {
			LogFactory.info("eth-getBasecoin 钱包接口不通");
		}catch (IOException e) {
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
	 * 以太坊代币转账接口
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
			if (listAccounts != null && listAccounts.size() > 0) {
				for (String account : listAccounts) {
					BigInteger accoutMoney = getBalance(account);
					totalMoney = totalMoney.add(accoutMoney);
				}
			}
			BigDecimal totalMoney_ = Convert.fromWei(String.valueOf(totalMoney), Unit.ETHER);
			String total = totalMoney_.toString();

			String coldAddress = Properties.appcoinMap().get(Constant.ETHER.toLowerCase() + Properties.COLDADDERSS);
			wallet.setCoinCode(Constant.ETHER);
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
	public synchronized static JsonResult sendFrom(String amount, String toAddress) {
		JsonResult result = new JsonResult();
		System.out.println("进入coin提币旷工费");
		String fromAddress = getBasecoin();
		if(StringUtils.isNotEmpty(fromAddress)){
			// 单位转化为wei
			BigDecimal money = Convert.toWei(new BigDecimal(amount), Unit.ETHER);
			BigInteger value = money.toBigInteger();
			// 转出地址余额wei
			BigInteger fromAddressMoney = getBalance(fromAddress);
			BigInteger price = getGasPrice();
			org.web3j.protocol.core.methods.request.Transaction transaction = new org.web3j.protocol.core.methods.request.Transaction(fromAddress, BigInteger.valueOf(0), price, BigInteger.valueOf(0), toAddress, value, "");
			BigInteger gas = getGasLimit(transaction);
			// 判断钱包余额是否大于等于提币金额
			// 区块链交易消耗由用户支付（note：提币1个实际到账可能0.995）
			BigInteger needfunds = price.multiply(gas).add(value);
			if (fromAddressMoney != null && fromAddressMoney.compareTo(needfunds) >= 0) {
				transaction = new org.web3j.protocol.core.methods.request.Transaction(fromAddress, null, price, gas, toAddress, value, "");
				String txHash = sendTransaction(transaction, PASSWORD);
				if (StringUtils.isNotEmpty(txHash)) {
					
					result.setSuccess(true);
					result.setMsg(txHash);
				} else {
					System.out.println(txHash);
					result.setMsg("提币失败");
				}
			} else {
				result.setMsg("提币账户余额不足,至少需要" + Convert.fromWei(new BigDecimal(needfunds.toString()), Unit.ETHER));
			}
		}else{
			result.setMsg("钱包错误，请检查");
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
	public static JsonResult smartContractSendFrom(String type, String amount,String fromAddress, String toAddress) {
		JsonResult result = new JsonResult();
		type = type.toUpperCase();
		String unit = coinMap.get(type + DECIMALS);
		String contractAddress = coinMap.get(type);
		if (StringUtils.isNotEmpty(unit) && StringUtils.isNotEmpty(contractAddress) && StringUtils.isNotEmpty(fromAddress)) {
			// 查询提币地址余额
			
			BigInteger rawFromAddressMoney = getBalanceofContract(fromAddress, contractAddress);
			BigDecimal fromAddressMoney = new BigDecimal(rawFromAddressMoney.toString());
			
			System.out.println("fromAddress="+fromAddress+" contractAddress="+contractAddress+" fromAddressMoney="+fromAddressMoney+" rawFromAddressMoney="+rawFromAddressMoney);
			
			/*// 设置手续费
			Double feeDouble=Double.valueOf(0.005D);
			BigDecimal fee=com.mz.utils.Convert.toWei(BigDecimal.valueOf(feeDouble), com.mz.utils.Convert.Unit.fromString(unit));
			fromAddressMoney = fromAddressMoney.subtract(fee);*/
			
			
			// 单位转化为wei
			BigDecimal money = com.mz.utils.Convert.toWei(new BigDecimal(amount), com.mz.utils.Convert.Unit.fromString(unit));
			LogFactory.info("fromAddressMoney="+fromAddressMoney+"   money="+money);
			if (fromAddressMoney.compareTo(money) >= 0) {
				//判断以太坊旷工费是否足够
				BigInteger fromAddressEthBalance=getBalance(fromAddress);
				LogFactory.info("fromAddressEthBalance="+fromAddressEthBalance+"   txFee="+TOKEN_COLLECT_TXFEE);
				if(fromAddressEthBalance.compareTo(TOKEN_COLLECT_TXFEE)>=0){
					String hash = sendTransactionofContract(fromAddress, toAddress, contractAddress, money.toBigInteger());
					if (StringUtils.isNotEmpty(hash)) {
						result.setSuccess(true);
						result.setMsg(hash);
					} else {
						result.setMsg("合约币提币失败");
					}
				}else{
					result.setMsg("旷工费不足，至少需要"+TOKEN_COLLECT_TXFEE_ETHER+"eth");
				}
			} else {
				result.setMsg("转出地址余额不足");
			}
		} else {
			result.setMsg("合约币提币失败,配置错误");
		}
		return result;
	}
	
	/**
	 * 代币提现接口
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param type
	 * @param:    @param amount
	 * @param:    @param toAddress
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2018年3月22日 下午3:22:17   
	 * @throws:
	 */
	public static JsonResult smartContractWithdraw(String type,String amount,String toAddress){
		JsonResult result=new JsonResult();
		String fromAddress=getBasecoin();
		result=smartContractSendFrom(type,amount,fromAddress,toAddress);
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
		String unit = coinMap.get(coinCode + DECIMALS);
		String contractAddress = coinMap.get(coinCode);
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
			String coldAddress = Properties.appcoinMap().get(coinCode.toLowerCase() + Properties.COLDADDERSS);

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
		String value = Properties.appcoinMap().get(coinType.toUpperCase());
		if (value != null && value.startsWith(CommonUtil.HEXPREFIX)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取以太坊所有的token
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @return
	 * @return: Map<String,String>
	 * @Date :          2018年3月19日 下午5:39:19   
	 * @throws:
	 */
	public static Map<String,String> tokenMap(){
		Map<String,String> tokens=new HashMap<String,String>(200);
		Map<String,String> properties=Properties.appcoinMap();
		Set<Entry<String, String>> sets=properties.entrySet();
		Iterator<Entry<String, String>> it=sets.iterator();
		while(it.hasNext()){
			Entry<String, String> entry=it.next();
			String key=entry.getKey();
			if(key != null && key.startsWith(CommonUtil.HEXPREFIX)){
				tokens.put(entry.getValue(),entry.getKey());
			}
		}
		return tokens;
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
		}catch (ConnectException e) {
			LogFactory.info("geth连接错误");
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		if(number.compareTo(BigInteger.ONE)<0){
			number=BigInteger.ONE;
		}
		DefaultBlockParameter blockNumber = DefaultBlockParameter.valueOf(number);
		try {
			return admin.ethGetBlockByNumber(blockNumber, isdetail).send().getBlock();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			LogFactory.info("geth连接错误");
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * 产生geth充币记录
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
	public static void etherProductionTxOld() {
		// 可能会漏掉，需要再次考虑逻辑
		BigInteger blockNumber = getBlockNumber().subtract(BigInteger.valueOf(15L));
		System.out.println(blockNumber);
		BigInteger count = BigInteger.valueOf(15L);
		for (int i = 1; i < count.intValue(); i++) {
			blockNumber = blockNumber.add(BigInteger.ONE);
			Block block = GetBlockByNumbe(blockNumber, true);
			if (block != null) {
				List<TransactionResult> transactions = block.getTransactions();
				for (TransactionResult l : transactions) {
					org.web3j.protocol.core.methods.response.Transaction tx = (org.web3j.protocol.core.methods.response.Transaction) l;
					tx.getHash();
					recordGethTx(tx);
				}
			}
		}

	}


	/**
	 * 修改过的产生geth充币记录
	 */
	public static void etherProductionTx() {
		//redis查找上次更新的区块
		String lastUpdateBlock = RedisUtil.getValue("eth_lastUpdateBlock");
		if(lastUpdateBlock!=null) {
			long bt = System.currentTimeMillis();
			Long b = Long.valueOf(lastUpdateBlock);
			//从哪一个区块开始
			BigInteger begin = BigInteger.valueOf(b);
			//当前区块高度
			BigInteger blockNumber = getBlockNumber();
			//遍历次数
			BigInteger count = blockNumber.subtract(begin);
			System.out.println("当前区块为：" + blockNumber);
			System.out.println("从第：" + begin + "块高度开始计算");
			System.out.println("遍历次数：" + count);


			for (int i = 1; i < count.intValue(); i++)
			{
				begin = begin.add(BigInteger.ONE);
				Block block = GetBlockByNumbe(begin, true);
				if (block != null) {
					List<TransactionResult> transactions = block.getTransactions();
					for (TransactionResult l : transactions) {
						org.web3j.protocol.core.methods.response.Transaction tx = (org.web3j.protocol.core.methods.response.Transaction) l;
						recordGethTx(tx);
					}
				}

				RedisUtil.setValue("eth_lastUpdateBlock", begin.toString());
			}
			long et = System.currentTimeMillis();
			System.out.println("时间："+(bt-et) /1000 + "s");

		}else {
			//初始化eth_lastUpdateBlock
			if (lastUpdateBlock==null){
				RedisUtil.setValue("eth_lastUpdateBlock","1");
			}
			//读不到redis的上次更新的区块，就还是走之前的方法
			System.out.print("还是走老方法吧");
			etherProductionTxOld();
		}
	}


	public static void etherProductionRefresh(Integer c) {
			long bt = System.currentTimeMillis();
			//从哪一个区块开始
			BigInteger begin = BigInteger.valueOf(c);
			//当前区块高度
			BigInteger blockNumber = getBlockNumber();
			//遍历次数
			BigInteger count = blockNumber.subtract(begin);
			System.out.println("当前区块为：" + blockNumber);
			System.out.println("从第：" + begin + "块高度开始计算");
			System.out.println("遍历次数：" + count);


			for (int i = 1; i < count.intValue(); i++)
			{
				begin = begin.add(BigInteger.ONE);
				Block block = GetBlockByNumbe(begin, true);
				if (block != null) {
					List<TransactionResult> transactions = block.getTransactions();
					for (TransactionResult l : transactions) {
						org.web3j.protocol.core.methods.response.Transaction tx = (org.web3j.protocol.core.methods.response.Transaction) l;
						recordGethTx(tx);
					}
				}
				System.out.println("执行到了区块高度为："+begin);
			}
			long et = System.currentTimeMillis();
			System.out.println("时间："+(bt-et) /1000 + "s");

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
		LogFactory.info("重新刷新geth充币记录：" + hash + "------json:" + Json.toJson(tx));
		recordGethTx(tx);
	}

	/**
	 * 记录geth的充值记录
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
			String eth_accounts_str = RedisUtil.getValue(ETH_ACCOUNTS);
			//提币地址
			String coinBase=getBasecoin();
			if (to != null && eth_accounts_str.contains(to) && txService.existNumber(tx.getHash()) == 0&&tx.getValue()!=null&&BigInteger.ZERO.compareTo(tx.getValue())<0&&!from.equalsIgnoreCase(coinBase)) {// 以太坊
				AppCoinTransaction t = new AppCoinTransaction();
				t.setHash_(tx.getHash());
				t.setTransactionIndex(tx.getTransactionIndex().toString());
				t.setBlockNumber(tx.getBlockNumber().toString());
				t.setBlockHash(tx.getBlockHash());
				t.setFrom_(from);
				t.setTo_(to);
				t.setCoinType(Constant.ETHER);
				t.setAmount(Convert.fromWei(tx.getValue().toString(), Unit.ETHER));
				t.setGas(new BigDecimal(tx.getGas().toString()));
				t.setGasPrice(new BigDecimal(tx.getGasPrice().toString()));
				t.setIsconsume(0);
				txService.save(t);
			// 平台的代币
			} else if (coinMap.containsKey(to)) {
				String input = tx.getInput();
				if (StringUtils.isNotEmpty(input) && input.length() >= 138) {
					String data = input.substring(0, 9);
					data = data + input.substring(17, input.length());
					Function function = new Function("transfer", Arrays.asList(), Arrays.asList(new TypeReference<Address>() {
					}, new TypeReference<Uint256>() {
					}));
					List<Type> params = FunctionReturnDecoder.decode(data, function.getOutputParameters());
					// 充币地址
					String toAddress = params.get(0).getValue().toString();
					String amount = params.get(1).getValue().toString();
					if (eth_accounts_str.contains(toAddress) && StringUtils.isNotEmpty(amount) && txService.existNumber(tx.getHash()) == 0) {
						// 币种
						String coinCode = coinMap.get(to);
						// 精度
						String unit = coinMap.get(coinCode + DECIMALS);

						if (StringUtils.isNotEmpty(coinCode) && StringUtils.isNotEmpty(unit)) {
							//判断合约方法执行是否成功，如果Event logs为空则说明合约方法没有执行成功
							TransactionReceipt receipt=getTransactionReceipt(tx.getHash());
							if(receipt!=null&&receipt.getLogs()!=null&&receipt.getLogs().size()>0){
								AppCoinTransaction t = new AppCoinTransaction();
								t.setHash_(tx.getHash());
								t.setInput(tx.getInput());
								t.setTransactionIndex(tx.getTransactionIndex().toString());
								t.setBlockNumber(tx.getBlockNumber().toString());
								t.setBlockHash(tx.getBlockHash());
								t.setFrom_(from);
								t.setTo_(toAddress);
								t.setCoinType(coinCode);
								t.setAmount(com.mz.utils.Convert.fromWei(amount, com.mz.utils.Convert.Unit.fromString(unit)));
								t.setGas(new BigDecimal(tx.getGas().toString()));
								t.setGasPrice(new BigDecimal(tx.getGasPrice().toString()));
								t.setIsconsume(0);
								txService.save(t);
							}else{
								LogFactory.info("合约错误,执行失败,hash:"+tx.getHash());
							}
						} else {
							LogFactory.info("代币配置错误，请检查：  coinCode=" + coinCode + "  unit=" + unit);
						}
					}
				}
			}
		}
	}

	/**
	 * @Desc 		根据transactionHash获取交易收据
	 * @author:     SHANGXL
	 * @param:    	@param transactionHash
	 * @param:    	@return
	 * @return: 	TransactionReceipt
	 * @Date :  	2018年5月28日 上午11:49:28
	 */
	public static TransactionReceipt getTransactionReceipt(String transactionHash){
		TransactionReceipt receipt=new TransactionReceipt();
		try {
			receipt=admin.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt().get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return receipt;
	}
	/**
	 * 加载eth_accounts到redis
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
	public static void loadEthAccounts2Redis() {
		ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService) ContextUtil.getBean("exDigitalmoneyAccountService");
		List<String> listPublicKey = exDigitalmoneyAccountService.listEtherpublickey();
		RedisUtil.setValue(ETH_ACCOUNTS, Json.toJson(listPublicKey));
	}

	/**
	 * 转载数据库blockNumber到redis(暂时未用到，勿删)
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param:
	 * @return: void
	 * @Date : 2018年1月26日 下午7:46:45
	 * @throws:
	 */
	public static void loadBlockNumber2Redis() {
		AppCoinTransactionService txService = (AppCoinTransactionService) ContextUtil.getBean("appCoinTransactionService");
		BigInteger lastestBlockNumber = txService.getLastestBlock();
		BigInteger blockNumber = null;
		try {
			lastestBlockNumber = txService.getLastestBlock();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (lastestBlockNumber != null) {
			blockNumber = lastestBlockNumber;
		} else {
			blockNumber = getBlockNumber().subtract(BigInteger.TEN);
		}
		if (blockNumber != null) {
			RedisUtil.setValue(BLOCKNUMBER, Json.toJson(blockNumber));
		}
	}
	
	/**
	 * 以太坊代币地址的代币资产全部归集到提币地址
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param fromAddress
	 * @param:    @param coinType
	 * @return: JsonResult 
	 * @Date :          2018年3月19日 下午5:02:30   
	 * @throws:
	 */
	public static JsonResult smartContractSend2CoinBase(String fromAddress,String amount,String coinType){
		JsonResult result=new JsonResult();
		String toAddress=getBasecoin();
		if(StringUtils.isNotEmpty(toAddress)){
			result=smartContractSendFrom(coinType, amount, fromAddress, toAddress);
		}else{
			result.setMsg("钱包出错请检查");
		}
		return result;
	}
	
	
	/**
	 * 以太坊token转币线下冷钱包
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param type
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2018年3月22日 下午3:02:27   
	 * @throws:
	 */
	public static JsonResult smartContractSend2ColdWallet(String type,String amount,String toAddress){
		JsonResult result=new JsonResult();
		String message=null;
		String fromAddress=getBasecoin();
		if(StringUtils.isNotEmpty(type)&&StringUtils.isNotEmpty(amount)&&StringUtils.isNotEmpty(toAddress)){
			return smartContractSendFrom(type, amount, fromAddress, toAddress);
		}else{
			message="参数异常，请检查";
			LogFactory.info(message+"type="+type+" amount="+amount+" toAddress="+toAddress);
			result.setMsg(message);
		}
		return result;
	}
	
	/**
	 * 根据以太坊代币类型查询地址上的资产信息
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param coinType
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2018年3月29日 上午10:56:59   
	 * @throws:
	 */
	public static JsonResult listTokenAddressAssets(String coinType) {
		JsonResult result = new JsonResult();
		String contractAddress = coinMap.get(coinType);
		String unit = coinMap.get(coinType + DECIMALS);
		if (StringUtils.isNotEmpty(contractAddress) && StringUtils.isNotEmpty(unit)) {
			AppCoinTransactionService appCoinTransactionService = (AppCoinTransactionService) ContextUtil.getBean("appCoinTransactionService");
			List<String> listAddress = appCoinTransactionService.listAddressGroupByAddress(coinType);
			if (listAddress != null && listAddress.size() > 0) {
				// 判断以太坊rpc接口是否通
				if (getBasecoin() != null) {
					List<Token> listToken = new ArrayList<Token>();
					int id=0;
					for (String address : listAddress) {
						id++;
						// 合约币余额
						BigInteger rawtokenAssets = getBalanceofContract(address, contractAddress);
						if(rawtokenAssets.compareTo(BigInteger.ZERO)>0){
							BigDecimal tokenAssets = com.mz.utils.Convert.fromWei(String.valueOf(rawtokenAssets), com.mz.utils.Convert.Unit.fromString(unit));
							// 以太坊余额
							BigInteger rawEtherAssets = getBalance(address);
							BigDecimal etherAssets = Convert.fromWei(String.valueOf(rawEtherAssets), Unit.ETHER);
							// 是否可归集
							Boolean abledCollect = etherAssets.compareTo(TOKEN_COLLECT_TXFEE_ETHER) >= 0;
							//填充对象
							listToken.add(new Token(id, address, tokenAssets, etherAssets, abledCollect));
						}
						//根据资产排序
						Collections.sort(listToken);
					}
					result.setSuccess(true);
					result.setObj(listToken);
				} else {
					result.setMsg("以太坊钱包异常请检查");
				}
			}
		} else {
			result.setMsg("合约币配置有误，未查询到记录");
		}
		return result;
	}
}
