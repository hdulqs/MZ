/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:      V1.0 
 * @Date:        2017年6月11日 下午6:18:26
 */
package com.mz.coin.coin.controller;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin.Transaction;
import com.azazar.krotjson.JSON;
import com.mz.Constant;
import com.mz.coin.CoinService;
import com.mz.coin.Wallet;
import com.mz.coin.bds.BdsServerImpl;
import com.mz.coin.bds.BtsServerImpl;
import com.mz.coin.bds.GxsServerImpl;
import com.mz.coin.coin.service.AppCoinTransactionService;
import com.mz.coin.coin.service.CoinTransactionService;
import com.mz.coin.enums.Coin;
import com.mz.coin.eth.service.impl.EtcService;
import com.mz.coin.eth.service.impl.EtherService;
import com.mz.coin.eth.service.impl.EtzService;
import com.mz.coin.htl.service.HtlServiceImpl;
import com.mz.coin.neo.NeoServiceImpl;
import com.mz.coin.tea.service.TEAService;
import com.mz.coin.tv.TvUtil;
import com.mz.coin.utils.CoinEmpty;
import com.mz.coin.utils.CoinServiceImpl;
import com.mz.coin.utils.JsonrpcClient;
import com.mz.coin.utils.MyCoinService;
import com.mz.coin.utils.RedisUtil;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.constant.StringConstant;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.ex.digitalmoneyAccount.model.ExDigitalmoneyAccount;
import com.mz.ex.digitalmoneyAccount.service.ExDigitalmoneyAccountService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.StringUtil;
import com.mz.util.log.LogFactory;
import com.mz.util.security.Check;
import com.mz.util.sys.ContextUtil;
import com.mz.utils.CommonUtil;
import com.mz.utils.Properties;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

/**
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2017年7月30日 下午5:05:34 
 */
@Controller
@RequestMapping("/coin")
public class CoinController {

	@Resource(name="redisService")
	private RedisService redisService;
	@Resource(name="exDigitalmoneyAccountService")
	private ExDigitalmoneyAccountService exDigitalmoneyAccountService;
	
	@Resource(name="appCoinTransactionService")
	private AppCoinTransactionService appCoinTransactionService;

	@Autowired
	TEAService teaService;
	/**
	 * 创建钱包地址 传入用户名
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping("/create")
	@ResponseBody
	public Map<String, String> create(HttpServletRequest req) {
		String userName = req.getParameter("userName");
		String type = req.getParameter("type");
		String accountNumber=req.getParameter("accountNumber");
		Map<String, String> map = new HashMap<String, String>(16);
		String address = null;
		if(StringUtils.isNotEmpty(userName)&&StringUtils.isNotEmpty(type)&&StringUtils.isNotEmpty(accountNumber)){
			try {
				type = type.toUpperCase();
				// 以太经典
				if (type.equalsIgnoreCase(Constant.ETC)) {
					address = EtcService.createAddress();
				} else if (type.equalsIgnoreCase(Constant.ETZ)) {
					address = EtzService.createAddress();
				} else if (type.equalsIgnoreCase(Constant.ETHER) || EtherService.isSmartContractCoin(type)) {
					String result = exDigitalmoneyAccountService.getEthPublicKeyByparams(userName);
					// 判断此用户已经创建过以太坊地址则返回以太坊地址
					if (StringUtils.isNotEmpty(result)) {
						address = result;
						// 判断此用户未创建过以太坊地址则创建新地址
					} else {
						address = EtherService.createAddress();
					}
				} else if (type.equalsIgnoreCase(Constant.TV)) {
					address = TvUtil.createAccount(accountNumber);
				} else if (type.equalsIgnoreCase(Constant.ZCASH)) {
					CoinService coinService = new CoinServiceImpl(type);
					address = coinService.createNewAddress(null);
				} else if (type.equalsIgnoreCase(Constant.BDS)) {
					BdsServerImpl bdsServerImpl = new BdsServerImpl();
					address = bdsServerImpl.getPublicKey(accountNumber);
				} else if (type.equalsIgnoreCase(Constant.BTS)) {
					BtsServerImpl btsServerImpl = new BtsServerImpl();
					address = btsServerImpl.getPublicKey(accountNumber);
				} else if (type.equalsIgnoreCase(Constant.GXS)) {
					GxsServerImpl gxsServerImpl = new GxsServerImpl();
					address = gxsServerImpl.getPublicKey(accountNumber);
				} else if (type.equalsIgnoreCase(Constant.NEO)) {
					NeoServiceImpl neoServer = new NeoServiceImpl();
					address = neoServer.getPublicKey(accountNumber);
				} else if (type.equalsIgnoreCase(com.mz.coin.utils.Constant.HTL)) {
					HtlServiceImpl htlServer = new HtlServiceImpl();
					address = htlServer.getPublicKey(accountNumber);
				}else if (type.equalsIgnoreCase("TEA")) {
					address = teaService.getnewaddress();
				}else {
					CoinService coinService = new CoinServiceImpl(type);
					address = coinService.createNewAddress(accountNumber);
				}
				map.put("address", address);
				map.put("code", "success");
			} catch (Exception e) {
				LogFactory.info("创建失败，请求参数---账户名：" + accountNumber + "   " + "币种：" + type);
				e.printStackTrace();
				map.put("address", address);
				map.put("code", "error");
			}
		}else{
			LogFactory.info("创建币地址参数信息：userName="+userName+"    type="+type+"      accountNumber="+accountNumber);
			map.put("address", address);
			map.put("code", "请求参数无效请检查");
		}
		return map;
	}
	
	
	/**
	 * 创建钱包地址 传入用户名
	 * cjz
	 * @param req
	 * @return
	 */
	public Map<String, String> create1(HttpServletRequest req) {
		String userName = req.getParameter("userName");
		String type = req.getParameter("type");
		String accountNumber=req.getParameter("accountNumber");
		Map<String, String> map = new HashMap<String, String>(16);
		String address = null;
		if(StringUtils.isNotEmpty(userName)&&StringUtils.isNotEmpty(type)&&StringUtils.isNotEmpty(accountNumber)){
			try {
				type = type.toUpperCase();
				Map<String, Coin> coinMap = Coin.map;
				if(coinMap.containsKey(type)) {
					Coin coin = coinMap.get(type);
					Class<?> service = coin.getValue();
					Object object = service.newInstance();
					//将所有调用的方法重载成map
					Method method = service.getMethod(coin.getMethod(),Map.class);
					address = (String) method.invoke(object,accountNumber);
					map.put("address", address);
					map.put("code", "success");
				}
			} catch (Exception e) {
				LogFactory.info("创建失败，请求参数---账户名：" + accountNumber + "   " + "币种：" + type);
				e.printStackTrace();
				map.put("address", address);
				map.put("code", "error");
			}
		}else{
			LogFactory.info("创建币地址参数信息：userName="+userName+"    type="+type+"      accountNumber="+accountNumber);
			map.put("address", address);
			map.put("code", "请求参数无效请检查");
		}
		return map;
	}

	/**
	 * 查所某个用户账户余额 (一个用户对应一个账户)
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping("/balance")
	@ResponseBody
	public double balance(HttpServletRequest req) {
		String type = req.getParameter("type");
		String userName = req.getParameter("userName");
		double d = 0;
		try {
			CoinService coinService = new CoinServiceImpl(type);
			if (null != userName && !"".equals(userName)) {
				d = coinService.getBalance(userName);
			} else {
				d = coinService.getBalance();
			}
			return d;
		} catch (Exception e) {
			LogFactory.info("查询余额异常");
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 转账接口 成功返回 订单号 以及一个code 失败返回 {code=1,msg='地址错误'}
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value="/sendFrom",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult sendFrom(HttpServletRequest request) {
		//在安全组设置白名单
		//判断重复问题，根据订单号，查询是否已经处理
		JsonResult result=new JsonResult();
		String toAddress = request.getParameter("toAddress");
		String type = request.getParameter("type");
		String transactionNum=request.getParameter("transactionNum");
		String amount = request.getParameter("amount");
		String getcode = request.getParameter("auth_code");
		String keepDecimalForCoin=request.getParameter("keepDecimalForCoin");
		String memo=request.getParameter("memo");
		System.out.println("转账接口：toAddress:"+toAddress+",type:"+type+",transactionNum:"+transactionNum+",amount:"+amount+",auth_code:"+getcode+",keepDecimalForCoin:"+keepDecimalForCoin+",memo:"+memo);
		LogFactory.info("收到订单："+transactionNum+"_的提币请求");
		String[] params = { toAddress, type, amount,transactionNum,keepDecimalForCoin,memo};
		// 判断是否有空参数
		if (CommonUtil.isNohashEmptyInArr(params)) {
			//进行安全校验
			String authcode = Check.authCode(params);
			System.out.println("coin_authcode="+authcode);
			System.out.println("exchange_authcode="+getcode);
			if (StringUtils.isNotEmpty(authcode) && getcode.equals(authcode)) {
				if(StringUtils.isNotEmpty(type)){
					//提币数量四舍五入
					Integer digit=Integer.valueOf(keepDecimalForCoin);
					if(digit!=0){
						amount=CommonUtil.strRoundDown(amount, digit);
					}
					type=type.toUpperCase();
					LogFactory.info("提币数量： digit="+digit+" amount="+amount+" type="+type);
					if(type.equalsIgnoreCase(Constant.ETC)){
						result=EtcService.sendFrom(amount, toAddress);
					}else if(type.equalsIgnoreCase(Constant.ETZ)){
						result=EtzService.sendFrom(amount, toAddress);
					}else if (type.equalsIgnoreCase(Constant.ETHER)) {
						result=EtherService.sendFrom(amount, toAddress);
					}else if(EtherService.isSmartContractCoin(type)){//以太坊代币
						result=EtherService.smartContractWithdraw(type, amount, toAddress);
					}else if(type.equalsIgnoreCase(Constant.TV)){
						result=TvUtil.sendFrom(amount, toAddress, memo, transactionNum);
					}else if(type.equalsIgnoreCase(Constant.USDT)){
						JsonrpcClient client=MyCoinService.getClient(type);
						result=client.omniSendFrom(amount, toAddress,Integer.parseInt(Constant.PROPERTYID_USDT));
					}else if(type.equalsIgnoreCase(Constant.BDS)){
						BdsServerImpl bdsServerImpl=new BdsServerImpl();
						result=bdsServerImpl.sendFrom(amount, toAddress, memo);
					}else if(type.equalsIgnoreCase(Constant.BTS)){
						BtsServerImpl btsServerImpl=new BtsServerImpl();
						result=btsServerImpl.sendFrom(amount, toAddress, memo);
					}else if(type.equalsIgnoreCase(Constant.GXS)){
						GxsServerImpl gxsServerImpl=new GxsServerImpl();
						result=gxsServerImpl.sendFrom(amount, toAddress, memo);
					}else if(type.equalsIgnoreCase(Constant.NEO)){
						NeoServiceImpl neoServiceImpl=new NeoServiceImpl();
						result=neoServiceImpl.sendFrom(amount, toAddress, memo);
					}else if(type.equalsIgnoreCase(com.mz.coin.utils.Constant.HTL)){
						HtlServiceImpl htlServiceImpl=new HtlServiceImpl();
						result=htlServiceImpl.sendFrom(amount, toAddress, memo);
					} else if (type.equalsIgnoreCase("TEA")) {
						result = teaService.sendassetfrom(null, toAddress, new BigDecimal(amount));
					}else {
						CoinService coinService = new CoinServiceImpl(type);
						result=coinService.sendFrom(amount, toAddress);
					}
				}
			}else{
				LogFactory.info("提币安全校验失败，请求参数："+request.getParameterMap());
				result.setMsg("安全校验失败");
			}
		} else {
			LogFactory.info("提币参数包含空参，请求参数："+request.getParameterMap());
			result.setMsg("包含空参或参数有误，请检查");
		}
		LogFactory.info(com.alibaba.fastjson.JSON.toJSON(result));
		return result;
	}


	/**
	 * 根据用户名返回用户的地址
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping("/getAddressesByAccount")
	@ResponseBody
	public String getAddressesByAccount(HttpServletRequest req) {
		String result = "";
		String type = req.getParameter("type");
		String account = req.getParameter("account");
		if (null != type && "" != type) {
			CoinService coinService = new CoinServiceImpl(type);
			if (null != account && "" != account) {
				List<String> list = coinService.getAddressesByAccount(account);
				result = JSON.stringify(list);
				return result;
			}
		}
		return result;
	}

	
	/**
	 * 查询币的总账户和提币账户
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param req
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年11月14日 下午8:18:29   
	 * @throws:
	 */
	@RequestMapping("/listBalance")
	@ResponseBody
	public String listBalance(HttpServletRequest req) {
		try {
			List<String> list=new ArrayList<>();
			String type=req.getParameter("type");
			String address=req.getParameter("address");
			if(StringUtil.isNull(type)&&StringUtil.isNull(address)){
				if(type.toUpperCase().equals(Constant.ETHER)){
					list.add("0");
					//提币地址余额
					address=EtherService.getBasecoin();
					BigInteger money=EtherService.getBalance(address);
					list.add(Convert.fromWei(money.toString(), Unit.ETHER).toString());
				}else{
					CoinService coinService = new CoinServiceImpl(type);
					list.add(String.valueOf(coinService.getBalance()));
					list.add(String.valueOf(coinService.getBalance("")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 获取钱包余额（提币余额和总余额）
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param req
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年11月29日 上午10:22:54   
	 * @throws:
	 */
	@RequestMapping("/listWalletBalance1")
	@ResponseBody
	public List<Wallet> listWalletBalance(HttpServletRequest req) {
		List<Wallet> list = new ArrayList<>();
		List<String> coins = RedisUtil.listcoin();
		int i = 0;
		for (String coinCode : coins) {
			LogFactory.info("币名称:"+coinCode);
			Wallet wallet=new Wallet();
			wallet.setCoinCode(coinCode);
			if (coinCode.equalsIgnoreCase(Constant.ETHER)) {
				wallet=EtherService.getEtherWalletInfo();
			}else if (coinCode.equalsIgnoreCase(Constant.ETC)) {
				wallet=EtcService.getEtherWalletInfo();
			}else if (coinCode.equalsIgnoreCase(Constant.ETZ)) {
				wallet=EtzService.getEtherWalletInfo();
			//以太坊代币
			}else if(EtherService.isSmartContractCoin(coinCode)){
				wallet=EtherService.smartContractGetWalletInfo(coinCode);
			}else if(coinCode.equalsIgnoreCase(Constant.TV)){
				wallet=TvUtil.getWalletInfo();
			}else if(coinCode.equalsIgnoreCase(Constant.USDT)){
				wallet=MyCoinService.omniGetWalletInfo(coinCode);
			}else if(coinCode.equalsIgnoreCase(Constant.BDS)){
				BdsServerImpl bdsServerImpl=new BdsServerImpl();
				wallet=bdsServerImpl.getWalletInfo();
			}else if(coinCode.equalsIgnoreCase(Constant.BTS)){
				BtsServerImpl btsServerImpl=new BtsServerImpl();
				wallet=btsServerImpl.getWalletInfo();
			}else if(coinCode.equalsIgnoreCase(Constant.GXS)){
				GxsServerImpl gxsServerImpl=new GxsServerImpl();
				wallet=gxsServerImpl.getWalletInfo();
			}else if(coinCode.equalsIgnoreCase(Constant.NEO)){
				NeoServiceImpl neoServiceImpl=new NeoServiceImpl();
				wallet=neoServiceImpl.getWalletInfo();
			}else if (coinCode.equalsIgnoreCase("TEA")) {
				wallet = teaService.listWalletBalance();
			}else {
				CoinService coinService = new CoinServiceImpl(coinCode);
				wallet=coinService.getWalletInfo(coinCode);
			}
				wallet.setId(++i);
				list.add(wallet);
			
		}
		return list;
	}
	
	/**
	 * 获取钱包余额（提币余额和总余额）
	 * <p> TODO</p>
	 * @author:         changjz
	 * @param:    @param req
	 * @param:    @return
	 * @return: String 
	 * @Date :          2018年4月3日 上午10:22:54   
	 * @throws:
	 */
	@RequestMapping("/listWalletBalance")
	@ResponseBody
	public List<Wallet> listWalletBalance1(HttpServletRequest req) {
		String allwalletlist = redisService.get("AllWalletList");
		if(allwalletlist!=null){
			List<Wallet> l = com.alibaba.fastjson.JSON.parseArray(allwalletlist,Wallet.class);
            System.out.println("读redis里面的钱包数据");
			return l;
		}else {
			List<Wallet> list = new ArrayList<>();
			List<String> coins = RedisUtil.listcoin();
			int i = 0;
			for (String coinCode : coins) {
				coinCode = coinCode.toUpperCase();
				LogFactory.info("币名称:" + coinCode);
				if (!CoinEmpty.coinisNotEmpty(coinCode)) {
					continue;
				}
				Wallet wallet = new Wallet();
				wallet.setCoinCode(coinCode);
				//以太坊代币
				if (EtherService.isSmartContractCoin(coinCode)) {
					wallet = EtherService.smartContractGetWalletInfo(coinCode);
					wallet.setId(++i);
					list.add(wallet);

					continue;
				}


				switch (coinCode) {
					case Constant.ETHER:
						wallet = EtherService.getEtherWalletInfo();
						wallet.setId(++i);
						list.add(wallet);
						continue;
					case Constant.ETC:
						wallet = EtcService.getEtherWalletInfo();
						wallet.setId(++i);
						list.add(wallet);
						continue;
					case Constant.ETZ:
						wallet = EtzService.getEtherWalletInfo();
						wallet.setId(++i);
						list.add(wallet);
						continue;
					case "TV":
						wallet = TvUtil.getWalletInfo();
						wallet.setId(++i);
						list.add(wallet);
						continue;
//					case Constant.USDT:
//						wallet = MyCoinService.omniGetWalletInfo(coinCode);
//						wallet.setId(++i);
//						list.add(wallet);
//						continue;
					case Constant.BDS:
						BdsServerImpl bdsServerImpl = new BdsServerImpl();
						wallet = bdsServerImpl.getWalletInfo();
						wallet.setId(++i);
						list.add(wallet);
						continue;
					case Constant.BTS:
						BtsServerImpl btsServerImpl = new BtsServerImpl();
						wallet = btsServerImpl.getWalletInfo();
						wallet.setId(++i);
						list.add(wallet);
						continue;
					case Constant.GXS:
						GxsServerImpl gxsServerImpl = new GxsServerImpl();
						wallet = gxsServerImpl.getWalletInfo();
						wallet.setId(++i);
						list.add(wallet);
						continue;
					case Constant.NEO:
						NeoServiceImpl neoServiceImpl = new NeoServiceImpl();
						wallet = neoServiceImpl.getWalletInfo();
						wallet.setId(++i);
						list.add(wallet);
						continue;
					case com.mz.coin.utils.Constant.HTL:
						HtlServiceImpl htlServiceImpl = new HtlServiceImpl();
						wallet = htlServiceImpl.getWalletInfo();
						wallet.setId(++i);
						list.add(wallet);
						continue;
					case "TEA":
						wallet = teaService.listWalletBalance();
						wallet.setId(++i);
						list.add(wallet);
						continue;
					default:
						CoinService coinService = new CoinServiceImpl(coinCode);
						wallet = coinService.getWalletInfo(coinCode);
						wallet.setId(++i);
						list.add(wallet);
						continue;
				}


			}
			return list;
		}
	}


	/**
	 * 将用户账户可用币转到冷钱包
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param req
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年11月14日 下午8:18:51   
	 * @throws:
	 */
	@RequestMapping("/toColdAccount")
	@ResponseBody
	public JsonResult toColdAccount(HttpServletRequest req) {
		JsonResult result = new JsonResult();
		Map<String, String> map = new HashMap<String, String>(16);
		String type = req.getParameter("type");
		String amount = req.getParameter("amount");
		String fromAddress = "-";
		if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(amount)) {
			type = type.toUpperCase();
			BigDecimal money = new BigDecimal(amount);
			String toAddress = Properties.appcoinMap().get(type.toLowerCase() + "_coldAddress");
			boolean isValidAddress = true;
			if (StringUtils.isNotEmpty(toAddress)) {
				if (isValidAddress) {
					if (type.equalsIgnoreCase(Constant.ETC)) {
						result = EtcService.send2ColdWallet(toAddress, amount);
					}else if (type.equalsIgnoreCase(Constant.ETZ)) {
						result = EtzService.send2ColdWallet(toAddress, amount);
					} else if (type.equalsIgnoreCase(Constant.ETHER)) {
						result = EtherService.send2ColdWallet(toAddress, amount);
					} else if (EtherService.isSmartContractCoin(type)) {
						result=EtherService.smartContractSend2ColdWallet(type, amount,toAddress);
					} else if (type.equalsIgnoreCase(Constant.TV)) {
						result = TvUtil.send2ColdAddress(toAddress, amount);
					} else if (type.equalsIgnoreCase(Constant.USDT)) {

						JsonrpcClient client=MyCoinService.getClient(type);
						result=client.omnisendColdWallet(amount, toAddress,Integer.parseInt(Constant.PROPERTYID_USDT));

					} else if (type.equalsIgnoreCase(Constant.BDS)) {
						BdsServerImpl bdsServerImpl = new BdsServerImpl();
						result = bdsServerImpl.send2ColdAddress(toAddress, amount);
					} else if (type.equalsIgnoreCase(Constant.BTS)) {
						BtsServerImpl btsServerImpl = new BtsServerImpl();
						result = btsServerImpl.send2ColdAddress(toAddress, amount);
					} else if (type.equalsIgnoreCase(Constant.GXS)) {
						GxsServerImpl gxsServerImpl = new GxsServerImpl();
						result = gxsServerImpl.send2ColdAddress(toAddress, amount);
					} else if (type.equalsIgnoreCase(Constant.NEO)) {
						NeoServiceImpl neoServiceImpl = new NeoServiceImpl();
						result = neoServiceImpl.send2ColdAddress(toAddress, amount);
					} else if (type.equalsIgnoreCase(com.mz.coin.utils.Constant.HTL)) {
						HtlServiceImpl htlServiceImpl = new HtlServiceImpl();
						result = htlServiceImpl.send2ColdAddress(toAddress, amount);
					}else if (type.equalsIgnoreCase("TEA")) {
						result = teaService.sendassetfrom(null, toAddress, new BigDecimal(amount));
					}else {
						CoinService coinService = new CoinServiceImpl(type);
						result = coinService.sendtoAddress(toAddress, money.doubleValue());
					}
					// 转币成功
					if (result.getSuccess() && StringUtils.isNotEmpty(result.getMsg())) {
						map.put("toAddress", toAddress);
						map.put("fromAddress", fromAddress);
						map.put("txHash", result.getMsg());
						result.setObj(map);
					} else {
						LogFactory.info("转币冷钱包失败：" + com.alibaba.fastjson.JSON.toJSONString(result));
					}
				}else{
					result.setMsg(type + "_冷钱包地址无效，请检查");
				}
			} else {
				result.setMsg(type + "_冷钱包地址为空,请检查");
			}
		} else {
			result.setMsg("参数无效，请检查--type=" + type + "   amount=" + amount);
		}
		// 打印错误日志
		if (!result.getSuccess()) {
			LogFactory.info("转入冷钱包操作失败：" + result.getMsg());
		}
		return result;
	}
	
	
	/**
	 * 
	 * 查询单个用户的所有流水记录
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public String list(HttpServletRequest req) {
		String type = req.getParameter("type");
		String userName = req.getParameter("userName");
		String count = req.getParameter("count");
		String startWith = req.getParameter("start");
		List<Transaction> list = null;
		String result = "";
		try {
			CoinService coinService = new CoinServiceImpl(type);
			if (null != count && !"".equals(count)) {
				list = coinService.listTransactions(userName, Integer.valueOf(count));
				result = JSON.stringify(list);
				if (null != startWith && !"".equals(startWith)) {
					list = coinService.listTransactions(userName, Integer.valueOf(count), Integer.valueOf(startWith));
					result = JSON.stringify(list);
				} else {
					list = coinService.listTransactions(userName, Integer.valueOf(count));
					result = JSON.stringify(list);
				}
			} else {
				list = coinService.listTransactions(userName,null);
				result = JSON.stringify(list);
			}
			System.out.println("====交易记录返回" + result);
			return result;
		} catch (Exception e) {
			return "Call interface error";
		}

	}
	
	
	/**
	 * 查询某个币的前10条交易数据 传币的类型
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping("/allList")
	@ResponseBody
	public String allList(HttpServletRequest req) {
		String type = req.getParameter("type");
		List<Transaction> list = null;
		String result = "";
		try {
			CoinService coinService = new CoinServiceImpl(type);
			list = coinService.listTransactions(null,null);
			result = JSON.stringify(list);
			System.out.println("====" + list.toString());
		} catch (Exception e) {
			System.out.println("err:" + e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 查询订单 根据订单号查询具体的订单 orderNo 币服务器上的订单
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping("/row")
	@ResponseBody
	public String row(HttpServletRequest req) {
		String orderNO = req.getParameter("orderNo");
		String type = req.getParameter("type");
		String row = "";
		try {
			CoinService coinService = new CoinServiceImpl(type);
			row = coinService.getRawTransaction(orderNO);
		} catch (Exception e) {
			e.printStackTrace();
			row = "";
		}
		System.out.println("查询详细信息：" + row);
		return row;
	}

	/**
	 * 查询某种币的所有账户
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping("/listAccounts")
	@ResponseBody
	public Map<String, Number> listAccounts(HttpServletRequest req) {
		String type = req.getParameter("type");
		if (null != type && "" != type) {
			CoinService coinService = new CoinServiceImpl(type);
			Map<String, Number> map = coinService.listaccounts();
			return map;
		}
		return null;
	}
	
	/**
	 * 查询某个币的所有用户
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping("/getAllUsers")
	@ResponseBody
	public String getAllUsers(HttpServletRequest req) {
		String result = "";
		String type = req.getParameter("type");
		if (null != type && "" != type) {
			CoinService coinService = new CoinServiceImpl(type);
			Map<String, Number> map = coinService.listaccounts();
			result = JSON.stringify(map);
			return result;
		}
		return result;
	}

	@RequestMapping("/recordTransaction")
	@ResponseBody
	public void recordTransaction() {
		// 币种类型
		String type = "dsc";
		CoinTransactionService txService = (CoinTransactionService) ContextUtil.getBean("coinTransactionService");

		JsonResult ret = txService.recordTransaction(type, null, null);
		System.out.println("ret==" + ret);
	}

	
	
	@RequestMapping("/refreshUserCoin")
	@MethodName(name = "单个用户刷币")
	@ResponseBody
	public JsonResult refreshUserCoin(HttpServletRequest request) {
		JsonResult ret = new JsonResult();
		String coinCode = request.getParameter("coinCode");
		String account = request.getParameter("account");
		String countstr = request.getParameter("count");
		int count = Integer.valueOf(countstr);
		ExDigitalmoneyAccountService accountService = (ExDigitalmoneyAccountService) ContextUtil.getBean("exDigitalmoneyAccountService");
		ExDigitalmoneyAccount coinAccount=accountService.getAccountByAccountNumber(account);
		if (StringUtils.isNotEmpty(coinCode) && StringUtils.isNotEmpty(account) && StringUtils.isNotEmpty(countstr)) {
			String[] arrcoin = Properties.listCoinBasedBtc();// 基于比特币币种类型
			if (arrcoin != null && arrcoin.length > 0 && Json.toJson(arrcoin).contains(coinCode.toUpperCase())) {
				CoinTransactionService txService = (CoinTransactionService) ContextUtil.getBean("coinTransactionService");
				if(coinCode.equalsIgnoreCase(Constant.USDT)){

					if(coinAccount!=null&&StringUtils.isNotEmpty(coinAccount.getPublicKey())){
						ret = txService.omniRecordTransaction(coinCode,coinAccount.getPublicKey(), 0, 0, 0, 0);
					}else{
						ret.setCode(StringConstant.FAIL);
					}
				}else if (coinCode.equalsIgnoreCase("TEA")) {
					if(coinAccount!=null&&StringUtils.isNotEmpty(coinAccount.getPublicKey())) {
						int skip = -1;
						ret = teaService.refreshUserCoin(coinAccount.getPublicKey(), count, skip);
					}
				}else{
					ret = txService.recordTransaction(coinCode, account, count);
				}
			} else {
				// 其他的币种
				ret.setCode(StringConstant.FAIL);
				ret.setMsg("网络错误");
			}
		} else {
			ret.setMsg("参数不正确");
			LogFactory.info("参数：" + Json.toJson(request.getParameterMap()));
		}
		System.out.println("刷币执行结果："+com.alibaba.fastjson.JSON.toJSON(ret));
		return ret;
	}
	
	
	@RequestMapping("/validateaddress")
	@MethodName(name = "验证钱包地址是否有效")
	@ResponseBody
	public JsonResult validateaddress(HttpServletRequest request) {
		JsonResult result = new JsonResult();
		String coinCode = request.getParameter("coinCode");
		String address = request.getParameter("address");
		if (StringUtils.isNotEmpty(coinCode) && StringUtils.isNotEmpty(address)) {
			boolean success=false;
			if (coinCode.equalsIgnoreCase(Constant.ETHER)||EtherService.isSmartContractCoin(coinCode)) {
				success=EtherService.isAddress(address);
			} else if (coinCode.equalsIgnoreCase(Constant.TV)) {
				success=TvUtil.walletCheckAddress(address);
			} else {
				CoinService coinService = new CoinServiceImpl(coinCode);
				String validate = coinService.validateAddress(address);
				validate = validate.replace(" ", "");
				Map<String, Object> map = StringUtil.str2map(validate);
				success = Boolean.parseBoolean(map.get("isvalid").toString());
			}
			result.setSuccess(success);
		} else {
			result.setMsg("验证失败");
		}
		if (result.getSuccess()) {
			result.setMsg("验证成功");
		}else{
			LogFactory.info("请求参数如下："+request.getParameterMap());
		}
		return result;
	}
	
	
	/**
	 * 重新接受以太坊及代币充值记录
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param req
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年11月17日 下午8:12:02   
	 * @throws:
	 */
	@RequestMapping("/recaptureGethTx")
	@ResponseBody
	public JsonResult recaptureGethTx(HttpServletRequest req) {
		JsonResult result=new JsonResult();
		String hash=req.getParameter("hash");
		String type=req.getParameter("type");
		if(StringUtils.isNotEmpty(hash)&&hash.length()==66){
			if(type.equalsIgnoreCase(Constant.ETC)){
				EtcService.recaptureGethTx(hash);
			}else if(type.equalsIgnoreCase(Constant.ETHER)){
				EtherService.recaptureGethTx(hash);
			}else if(type.equalsIgnoreCase(Constant.ETZ)){
				EtzService.recaptureGethTx(hash);
			}
			result.setSuccess(true);
		}else{
			result.setMsg("hash不正确");
		}
		return result;
	}
	
	/**
	 * 查询代币地址余额(以太坊代币、usdt)
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param request
	 * @param:    @return
	 * @return: JsonResult
	 * @Date :          2018年3月29日 上午11:00:19   
	 * @throws:
	 */
	@RequestMapping(value="/listTokenAddressAssets",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult listTokenAddressAssets(HttpServletRequest request) {
		JsonResult result=new JsonResult();
		String coinType=request.getParameter("coinType");
		if(Constant.USDT.equalsIgnoreCase(coinType)){
			JsonrpcClient client=MyCoinService.getClient(Constant.USDT);
			result=client.listOmniAssetByPropertyid(Integer.parseInt(Constant.PROPERTYID_USDT));
		}else{
			result=EtherService.listTokenAddressAssets(coinType);
		}
		return result;
	}
	
	/**
	 * 充值旷工费
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param request
	 * @param:    @return
	 * @return: JsonResult
	 * @Date :          2018年3月29日 上午11:00:43   
	 * @throws:
	 */
	@RequestMapping(value="/rechargeTxFee2TokenAddress",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult rechargeTxFee2TokenAddress(HttpServletRequest request) {
		System.out.println("进入coin充值旷工费");
		JsonResult result=new JsonResult();
		String address=request.getParameter("address");
		String amount=request.getParameter("amount");
		String coinType=request.getParameter("coinType");
		String authcode=request.getParameter("authcode");
		String[] params = {address,amount,coinType};
		String checkcode = Check.authCode(params);
		// 判空
		if (CommonUtil.isNohashEmptyInArr(params)&&checkcode.equals(authcode)) {
			//以太坊代币
			if(EtherService.isSmartContractCoin(coinType)){
				result= EtherService.sendFrom(amount, address);
			}else if(Constant.USDT.equalsIgnoreCase(coinType)){
				JsonrpcClient client=MyCoinService.getClient(Constant.USDT);
				result=client.omniRechargeTxFee(address, amount, Integer.parseInt(Constant.PROPERTYID_USDT));
			}
		}else{
			LogFactory.info(request.getParameterMap());
		}
		return result;
	}
	

	/**
	 * <p>归集代币</p>
	 * @author:         shangxl
	 * @param:    @param request
	 * @param:    @return
	 * @return: JsonResult
	 * @Date :          2018年3月29日 上午11:00:43   
	 * @throws:
	 */
	@RequestMapping(value="/tokenCollect",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult tokenCollect(HttpServletRequest request) {
		JsonResult result=new JsonResult();
		String message="";
		String address=request.getParameter("address");
		String amount=request.getParameter("amount");
		String coinType=request.getParameter("coinType");
		if(StringUtils.isNotEmpty(address)&&StringUtils.isNotEmpty(coinType)){
			if(Constant.USDT.equalsIgnoreCase(coinType)){
				JsonrpcClient client=MyCoinService.getClient(Constant.USDT);
				result=client.omniAssetCollect(address, amount, Integer.parseInt(Constant.PROPERTYID_USDT));
			}else{
				result=EtherService.smartContractSend2CoinBase(address,amount, coinType);
			}
		}else{
			message="参数异常";
			result.setMsg(message);
		}
		if(!result.getSuccess()){
			LogFactory.info(message+"="+request.getParameterMap());
		}
		return result;
	}
	
	/**
	 * 测试http调用
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param req
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年11月17日 下午8:12:02   
	 * @throws:
	 */
	@RequestMapping("/test")
	@ResponseBody
	public JsonResult test(HttpServletRequest req) {
		return null;
	}

	/**
	 * 定位ETH区块高度，开始刷币
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/refreshETH",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult refreshETH(HttpServletRequest request) {
		JsonResult result=new JsonResult();
		String mount=request.getParameter("mount");
		EtherService.etherProductionRefresh(Integer.valueOf(mount));
		return result;
	}

	/**
	 * 定位USDT区块高度，开始刷币
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/refreshUSDT",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult refreshUSDT(HttpServletRequest request) {
		JsonResult result=new JsonResult();
		String mount=request.getParameter("blockNum");
		int i = Integer.parseInt(mount);
		String address=request.getParameter("address");
		CoinTransactionService txService = (CoinTransactionService) ContextUtil.getBean("coinTransactionService");
		txService.omniRecordTransactionByOneBlock("USDT",address,0,0,i,i);
		return result;
	}
}
