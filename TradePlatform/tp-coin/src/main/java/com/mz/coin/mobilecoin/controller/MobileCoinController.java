package com.mz.coin.mobilecoin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/mobilecoin")
public class MobileCoinController {/*
	*//**
	 * 虚拟币手机钱包
	 * 方法1：
	 * 	   注册功能，手机端会有一个密码传过来，然后我这边调用钱包生成地址返回
	 * 			还要生成币账户，初始化余额为0
	 * 方法2：
	 * 	    登陆功能，使用地址和密码登陆功能，手机端传入币账户地址和密码，我这边进行校验
	 * 方法3：
	 * 	  查询余额功能，传入币账户，我这边返回余额（查数据库）
	 * 方法4：
	 * 	  转币功能，手机端输入他人钱包地址转币，我这边调用交易功能（有个自动刷币，就是轮询请求
	 * 	  钱包然后获取交易流水，跟数据库比对然后有新交易就更新余额）
	 * 
	 * 
	 *  需要加上定时刷交易记录的功能，针对充值，
	 * 	都要重新写一下
	 *//*
	
	*//**
	 * 新加数据库表
	 * 1，账户表:id，币地址，密码，交易密码，余额
	 * 2，交易表:id，币地址，交易类型（转入或转出），交易对象币地址，交易数量，交易结果
	 * 3，日志表:id，操作类型（注册，登陆，交易），(该表暂时不加)
	 * 
	 * 
	 *//*
	
	*//**
	 * 注册，根据密码创建一个地址
	 * 这里用的是手机唯一标识做用户名创建比币地址
	 * 会出现用户用同一个手机重复注册的情况，
	 * 可以将 手机标识+时间戳 当作用户名
	 * 
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param req
	 * @param:    @return
	 * @return: Map<String,String> 
	 * @Date :          2017年2月14日 上午10:35:36   
	 * @throws:
	 *//*
	@RequestMapping("/create")
	@ResponseBody
	public Map<String, String> create(HttpServletRequest req) {
		Map<String, String> map = new HashMap<String, String>();
		String password = req.getParameter("password");
		String type = req.getParameter("type");
		String mobileId = req.getParameter("mobileId");
		System.out.println("创建币的地址 注册密码为：" + password + "   " + "币的类型：" + type);
		//判断是否有空
		if (password==null || "".equals(password.trim())) {
			map.put("msg", "密码不能为空！");
			map.put("code", "error");
			return map;
		}else if (type==null || "".equals(type.trim())) {
			map.put("msg", "币类型不能为空！");
			map.put("code", "error");
			return map;
		}else if (mobileId==null || "".equals(mobileId.trim())) {
			map.put("msg", "手机标识不能为空！");
			map.put("code", "error");
			return map;
		}
		//币用户名 = 手机标识  + 时间戳(保证唯一就行了)
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String accountName="mobile_"+mobileId+"_"+sdf.format(new Date());
		
		try {
			CoinService coinService = new CoinServiceImpl(type);
			//用手机唯一标识生成币的地址
			String address = coinService.createNewAddress(accountName);
			System.out.println("创建成功 地址为：" + address);
			//保存到数据库
			MobileCustomerServcie mobileCustomerServcie = (MobileCustomerServcie) ContextUtil.getBean("mobileCustomerServcie");
			MobileCustomer customer=new MobileCustomer();
			customer.setCoinAddr(address);//地址
			customer.setPassWord(password);//密码
			customer.setMobileId(mobileId);//手机唯一标识
			customer.setAccountName(accountName);//生成币的用户名
			customer.setCoinType(type);
			mobileCustomerServcie.save(customer);
			
			map.put("code", "success");
			map.put("address", address);
		} catch (Exception e) {
			System.out.println("创建失败 异常为：" + e.getMessage());
			e.printStackTrace();
			map.put("msg", "创建失败出现异常");
			map.put("code", "error");
		}
		return map;
	}
	
	
	*//**
	 * 登陆，根据币地址和密码进行登陆
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param req
	 * @param:    @return
	 * @return: Map<String,String> 
	 * @Date :          2017年2月14日 上午10:35:36   
	 * @throws:
	 *//*
	@RequestMapping("/login")
	@ResponseBody
	public Map<String, String> login(HttpServletRequest req) {
		Map<String, String> map = new HashMap<String, String>();
		String password = req.getParameter("password");
		String type = req.getParameter("type");
		String address = req.getParameter("address");
		//判断是否有空
		if (password==null || "".equals(password.trim())) {
			map.put("msg", "密码不能为空！");
			map.put("code", "error");
			return map;
		}else if (type==null || "".equals(type.trim())) {
			map.put("msg", "币类型不能为空！");
			map.put("code", "error");
			return map;
		}else if (address==null || "".equals(address.trim())) {
			map.put("msg", "币地址不能为空！");
			map.put("code", "error");
			return map;
		}
		
		
		try {
			MobileCustomerServcie mobileCustomerServcie = (MobileCustomerServcie) ContextUtil.getBean("mobileCustomerServcie");
			QueryFilter filter=new QueryFilter(MobileCustomer.class);
			filter.addFilter("coinAddr=", address);
			filter.addFilter("coinType=", type);
			filter.addFilter("passWord=", password);
			MobileCustomer customer=mobileCustomerServcie.get(filter);
			if(customer!=null){
				map.put("code", "success");
				//map.put("address", address);
			}else{
				map.put("code", "error");
				map.put("msg", "未查询到该用户或者密码不正确！");
			}
		} catch (Exception e) {
			System.out.println("创建失败 异常为：" + e.getMessage());
			map.put("msg", "用户登录出现异常");
			map.put("code", "error");
		}
		return map;
	}
	
	
	*//**
	 * 查所某个用户账户余额 (一个用户对应一个账户)
	 * 不能查询钱包的余额（每日会定时转入充币地址）
	 * @param req
	 * @return
	 *//*
	@RequestMapping("/balance")
	@ResponseBody
	public Map<String, String> balance(HttpServletRequest req) {
		Map<String, String> map = new HashMap<String, String>();
		BigDecimal bal = new BigDecimal("0");
		
		String type = req.getParameter("type");
		String address = req.getParameter("address");
		if (type==null || "".equals(type.trim())) {
			map.put("code", "error");
			map.put("msg", "币种类型为空！");
			return map;
		}else if (address==null || "".equals(address.trim())) {
			map.put("code", "error");
			map.put("msg", "币地址不能为空！");
			return map;
		}
		
		try {
			//根据币的地址去数据库查币的对象，然后查询余额
			//CoinService coinService = new CoinServiceImpl(type);
			MobileCustomerServcie mobileCustomerServcie = (MobileCustomerServcie) ContextUtil.getBean("mobileCustomerServcie");
			QueryFilter filter=new QueryFilter(MobileCustomer.class);
			filter.addFilter("coinAddr=", address);
			filter.addFilter("coinType=", type);
			MobileCustomer customer=mobileCustomerServcie.get(filter);
			if(customer!=null){
				//bal = coinService.getBalance(customer.getMobileId());
				bal=customer.getHotCurrency();//获取可用余额
				map.put("code", "success");
				map.put("balance", (new BigDecimal(bal+"")).setScale(4, BigDecimal.ROUND_DOWN)+"");
			}else{
				map.put("code", "error");
				map.put("msg", "该地址未查询到相关用户！");
			}
		} catch (Exception e) {
			map.put("code", "error");
			map.put("msg", "查所某个用户账户余额 异常！");
			System.out.println("查所某个用户账户余额 异常。。。。。。。。"+e.getMessage());
		}
		return map;
	}
	
	*//**
	 * 查询单个用户的所有流水记录
	 * 
	 * @param req
	 * @return
	 *//*
	@RequestMapping("/list")
	@ResponseBody
	public String list(HttpServletRequest req) {
		JSONObject json = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		String type = req.getParameter("type");
		String address = req.getParameter("address");
		String count = req.getParameter("count");//查询数量
		String startWith = req.getParameter("start");//查询起始数
		
		//参数非空判断
		if (type==null || "".equals(type.trim())) {
			json.put("code", "error");
			json.put("msg", "币种类型为空！");
			return json.toString();
			map.put("code", "error");
			map.put("msg", "币种类型为空！");
			return map;
		}else if (address==null || "".equals(address.trim())) {
			json.put("code", "error");
			json.put("msg", "币地址不能为空！");
			return json.toString();
			map.put("code", "error");
			map.put("msg", "币地址不能为空！");
			return map;
		}
		
		String result = "";
		try {
			MobileCustomerServcie mobileCustomerServcie = (MobileCustomerServcie) ContextUtil.getBean("mobileCustomerServcie");
			QueryFilter filter=new QueryFilter(MobileCustomer.class);
			filter.addFilter("coinAddr=", address);
			filter.addFilter("coinType=", type);
			MobileCustomer customer=mobileCustomerServcie.get(filter);
			if(customer==null){
				json.put("code", "error");
				json.put("msg", "该地址未查询到相关用户！");
				return json.toString();
			}
			
			//查询交易记录（需要查询mobile_coin_transcation）
			//钱包那边记录的并不是真正的改用户转的，都是由提币账户转
			
			MobileCoinTransactionServcie mobileCoinTransactionServcie=(MobileCoinTransactionServcie) ContextUtil.getBean("mobileCoinTransactionServcie");
			QueryFilter filter1=new QueryFilter(MobileCoinTransaction.class);
			filter1.addFilter("accountName=", customer.getAccountName());
			List<MobileCoinTransaction> list=mobileCoinTransactionServcie.find(filter1);
			
			
			
			
			JSONArray json1=new JSONArray();
            for(MobileCoinTransaction tran : list){
                JSONObject jo = new JSONObject();
                //（send为发送  receive为接收）
                // 交易类型(1充币 ，2提币)
                int transactionType=tran.getTransactionType();
                if(transactionType==1){
                	jo.put("category", "receive");
                }else{
                	jo.put("category", "send");
                }
                java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.0000");
                String money = myformat.format(tran.getTransactionMoney());   
                jo.put("amount",money);
                SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                jo.put("timereceived",sim.format(tran.getCreated()));
                json1.put(jo);
            }
			
			System.out.println("====交易记录返回" + json1.toString());
			map.put("code", "success");
			map.put("msg", json1);
			return json1.toString();
		} catch (Exception e) {
			
			map.put("code", "error");
			map.put("msg", "查询单个用户的所有流水记录异常");
			return map;
			System.out.println("查询单个用户的所有流水记录异常。。。。。。。"+e.getMessage());
			json.put("code", "error");
			json.put("msg", "查询单个用户的所有流水记录异常");
			return json.toString();
		}
	}
	
	
	*//**
	 * 转账接口 成功返回 订单号 以及一个code 失败返回 {code=1,msg='地址错误'}
	 * 一般是转出
	 * 这时需要从提币账户进行转账
	 * 
	 * @param req
	 * @return
	 *//*
	@RequestMapping("/sendFrom")
	@ResponseBody
	public Map<String, String> sendFrom(HttpServletRequest req) {
		Map<String, String> map = new HashMap<String, String>();
		String fromAddress = req.getParameter("fromAddress"); // 发款人
		String toAddress = req.getParameter("toAddress"); // 接收人
		String type = req.getParameter("type"); // 币的类型
		String amount = req.getParameter("amount"); // 数量
		//判断是否有空
		if (fromAddress==null || "".equals(fromAddress.trim())) {
			map.put("msg", "发款人地址不能为空！");
			map.put("code", "error");
			return map;
		}else if (toAddress==null || "".equals(toAddress.trim())) {
			map.put("msg", "接收人地址不能为空！");
			map.put("code", "error");
			return map;
		}else if (type==null || "".equals(type.trim())) {
			map.put("msg", "币类型不能为空！");
			map.put("code", "error");
			return map;
		}else if (amount==null || "".equals(amount.trim())) {
			map.put("msg", "转币数量不能为空！");
			map.put("code", "error");
			return map;
		}
		
		
		String result = "";
		try {
			CoinService coinService = new CoinServiceImpl(type);  
			MobileCustomerServcie mobileCustomerServcie = (MobileCustomerServcie) ContextUtil.getBean("mobileCustomerServcie");
			QueryFilter filter=new QueryFilter(MobileCustomer.class);
			filter.addFilter("coinAddr=", fromAddress);
			filter.addFilter("coinType=", type);
			MobileCustomer customer=mobileCustomerServcie.get(filter);
			if(customer==null){
				map.put("msg", "该地址未查询到相关用户！");
				map.put("code", "error");
				return map;
			}
			
			//判断币的余额是否足够
			if (customer.getHotCurrency().compareTo(new BigDecimal(amount))<0) {//不够
				map.put("msg", "币账户余额不足！");
				map.put("code", "error");
				return map;
			}
			
			
			//进行转币（注意手续费）
			//查询提币账户余额
			double purseBalance=coinService.getBalance();
			double withdrawBalance=coinService.getBalance(type.toLowerCase()+"withdraw");
			BigDecimal pursewithdrawBalance=new BigDecimal(withdrawBalance);
				
			//获取手续费
			BigDecimal feeRate=productWithdrawFeeRate(type);
	        BigDecimal  fee=new BigDecimal(amount).multiply(feeRate).divide(new BigDecimal(100));
	        
	        BigDecimal needSendMoney=new BigDecimal(amount).subtract(fee).setScale(4, BigDecimal.ROUND_HALF_UP);
			if(pursewithdrawBalance.compareTo(needSendMoney)>0){
				if(new BigDecimal(purseBalance).compareTo(needSendMoney)>0){
					MobileCoinTransactionServcie mobileCoinTransactionServcie = (MobileCoinTransactionServcie) ContextUtil.getBean("mobileCoinTransactionServcie");
					
					MobileCoinTransaction exDmTransaction = new MobileCoinTransaction();
					String transactionNum = NumConstant.Ex_Dm_Transaction;
					exDmTransaction.setTransactionNum(IdGenerate.transactionNum(transactionNum));
					exDmTransaction.setAccountId(customer.getId());
					exDmTransaction.setAccountName(customer.getAccountName());
					// 2表示提现
					exDmTransaction.setTransactionType(2);
					exDmTransaction.setTransactionMoney(new BigDecimal(amount));
					exDmTransaction.setCoinCode(type);
					exDmTransaction.setSaasId(RpcContext.getContext().getAttachment("saasId"));
					exDmTransaction.setFee(fee);
					exDmTransaction.setOurAccountNumber("");
					exDmTransaction.setInAddress(toAddress);
					exDmTransaction.setOutAddress(fromAddress);
					
					
					//调用钱包接口执行转币
					//应该是从提币账户进行转币的（为btcwithdraw）,从app_our_account中获取
					result = coinService.sendFrom(getOurWithdrawAddress(type), toAddress,(new BigDecimal(amount).subtract(fee)).doubleValue());
					if(result.contains("code=1")){
						//保存失败流水  状态 1待审核 --2已完成 -- 3以否决
						exDmTransaction.setStatus(3);
						mobileCoinTransactionServcie.save(exDmTransaction);
						
						map.put("msg", "转币地址错误或异常!");
						map.put("code", "error");
						return map;
					}else{
						//保存成功流水，扣除币余额
						//成功扣除余额
						//保存失败流水  状态 1待审核 --2已完成 -- 3以否决
						exDmTransaction.setStatus(2);
						mobileCoinTransactionServcie.save(exDmTransaction);
						
						//扣除币余额
						customer.setHotCurrency(customer.getHotCurrency().subtract(new BigDecimal(amount)));
	        			mobileCustomerServcie.update(customer);
	        			
						map.put("msg", "转币成功!");
						map.put("code", "success");
						return map;
					}
				}else{
					map.put("msg", "钱包总账户余额不足!");
					map.put("code", "error");
					return map;
				}
			}else{
				map.put("msg", "平台货币账户余额不足！");
				map.put("code", "error");
				return map;
			}
		} catch (Exception e) {
			System.out.println("转币出现异常！"+e.getMessage());
			map.put("msg", "转币出现异常!");
			map.put("code", "error");
			return map;
		}
	
		//暂时用不到，用到时候在放开
	return null;	
	}
	
	*//**
	 * 从数据库中获取
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param type
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年3月22日 上午9:30:32   
	 * @throws:
	 *//*
	private String getOurWithdrawAddress(String type) {
		RemoteAppOurAccountService remoteAppOurAccountService=(RemoteAppOurAccountService)ContextUtil.getBean("remoteAppOurAccountService");
		AppOurAccount account=remoteAppOurAccountService.getOurAccount("cn", type.toUpperCase(), "1");
		return account.getAccountNumber();
	}


	*//**
	 * 获取配置文件中的我方币的充币账户
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param coinCode
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年2月24日 下午5:05:31   
	 * @throws:
	 *//*
	private String getOurAddress(String coinCode){
		 Properties pros = new Properties();
		 String value="";
		 InputStream in = MobileCoinController.class.getResourceAsStream("/coinConfig/bitCoinConfig.properties");
		 try {
			 pros.load(in);
			 in.close();
			 value=(String) pros.get(coinCode+"_ourAddress");
		} catch (IOException e) {
			System.out.println("获取币配置文件中的数据抛了异常。。。");
			e.printStackTrace();
		}
		 return value;
	 }
	
	*//**
	 * 币的提现手续费
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param coinCode
	 * @param:    @return
	 * @return: BigDecimal 
	 * @Date :          2017年2月24日 下午5:13:23   
	 * @throws:
	 *//*
	public BigDecimal productWithdrawFeeRate(String coinCode){
		BigDecimal rate=new BigDecimal(0);
		RemoteExProductService remoteExProductService = (RemoteExProductService) ContextUtil
				.getBean("remoteExProductService");
		
		RemoteQueryFilter filter = new RemoteQueryFilter(ExProduct.class);
		filter.addFilter("coinCode=", coinCode.toUpperCase());
		filter.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
		ExProduct product = remoteExProductService.findByCoinCode(filter);
		rate=product.getPaceFeeRate();
		return rate;
	}
	
	

*/}
