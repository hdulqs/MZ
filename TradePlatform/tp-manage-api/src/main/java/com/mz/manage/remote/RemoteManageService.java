
package com.mz.manage.remote;

import com.mz.manage.remote.model.AppAccountManage;
import com.mz.manage.remote.model.AppOurAccountManage;
import com.mz.manage.remote.model.CoinAccount;
import com.mz.manage.remote.model.MyAccountTO;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.c2c.C2cOrder;
import com.mz.manage.remote.model.commendCode;
import com.mz.util.UniqueRecord;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.manage.remote.model.otc.OtcOrderTransactionMange;
import com.mz.manage.remote.model.otc.OtcTransactionOrder;
import com.mz.trade.redis.model.EntrustTrade;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RemoteManageService {


	
	/**
	 * 查找开启提币的币种(对外接口新增)
	 * @return
	 */
	public RemoteResult getAddress(Long customerId,String coinCode);

	/**
	 * 查找开启提币的币种
	 * @return
	 */
	public List<String> findOpenTibi();
	
	/**
	 * 
	 */
	public boolean setc2cTransactionStatus(String transactionNum, int status2,String remark);
	
	/**
	 * 生成c2c订单
	 * @param c2cOrder
	 * @return
	 */
	public RemoteResult createC2cOrder(C2cOrder c2cOrder);
	
	/**
	 * 查询账户ID和虚拟币账户ID
	 * @param username
	 * @param coinCode
	 * @return
	 */
	public Map<String, Object> selectRechargeCoin(String username,String coinCode);
	/**
	 * 获得c2c订单汇款信息
	 * @param c2cOrder
	 * @return
	 */
	public String getC2cOrderDetail(String transactionNum );
	/**
	 * 查所有交易记录
	 * @param params
	 * @return
	 */
	public FrontPage findAllEntrust(Map<String, String> params) ;
	/**
	 * 获得c2c最新10条记录
	 * @param coinCode
	 */
	public List<C2cOrder> c2c10Order(Long customerId,String coinCode);
	
	public RemoteResult appgetAccountInfo(String cionCode,String fixCode);
	
	/**
	 * 生成钱包地址
	 * @param exdmaccountId
	 * @return
	 */
	public RemoteResult createPublicKey(Long exdmaccountId);

    /*
    * add by  zongwei
    * Otc创建
    * */
    RemoteResult createOtcOrder(OtcTransactionOrder otcTransactionOrder);

    /**
	 * 获得币对应的保留小数
	 * @param exdmaccountId
	 * @return
	 */
	public Integer getKeepDecimalForCoin(String CoinCode);
	/**
	 * 获得钱包地址
	 * @param exdmaccountId
	 * @return
	 */
	public RemoteResult getPublicKey(Long exdmaccountId);
	
	public void cancelAllExEntrust(EntrustTrade entrustTrade);
	/**
	 * 查询当前用户今天取了多少钱
	 * @param customerId
	 * @param created 
	 * @return
	 */
	public BigDecimal getOldMoney(Long customerId,String created );
	
	/**
	 * 登录接口
	 * @param username  用户名
	 * @param password  密码
	 * @return  RemoteResult.obj  返回user
	 */
	public RemoteResult login(String username,String password,String uuid);
	
	/**
	 * 手机登录接口
	 * @param username  用户名
	 * @param password  密码
	 * @return  RemoteResult.obj  返回user
	 */
	public RemoteResult login(String username,String password,String country,String uuid);
	
	/**
	 * 退出
	 * @param user
	 * @return
	 */
	public RemoteResult logout(User user);
	
	
	/**
	 * 注册接口
	 * @param username
	 * @param password
	 * @return
	 */
	public RemoteResult regist(String username, String password,String referralCode,String language);
	
	/**
	 * 查询交易记录
	 * @param params
	 */
	public FrontPage findTrades(Map<String, String> params);

	/**
	 * 查询委托记录
	 * @param params
	 * @return
	 */
	public FrontPage findEntrust(Map<String, String> params);
	/**
	 * 通customerId获取User对象
	 * @param customerId
	 * @return
	 */
	public User selectByustomerId(Long customerId);
	
	/**
	 * 实名认证
	 * @param userCode 用户标识
	 * @param trueName 姓名
	 * @param country  国家
	 * @param cardType 证件类型
	 * @param cardId   证件号
	 * @return
	 */
	public RemoteResult realname(String userCode, String trueName,String surName, String country, String cardType, String cardId);

	/**
	 * 查询personinfo信息
	 * @param userCode
	 * @return
	 */
	public RemoteResult getPersonInfo(String userCode);

	/**
	 * 重置登录密码
	 * @param username 用户名
	 * @param oldPassWord  旧密码
	 * @param newPassWord  新密码
	 * @return
	 */
	public RemoteResult setpw(String username,String oldPassWord, String newPassWord);
	

	/**
	 * 设置交易密码
	 * @param username  用户名
	 * @param accountPassWord  交易密码
	 * @return
	 */
	public RemoteResult setapw(String username, String accountPassWord);

	
	/**
	 * 重置交易密码
	 * @param username  用户名
	 * @param passWord  登录密码
	 * @param accountPassWord  交易密码
	 * @return
	 */
	public RemoteResult resetapw(String username, String passWord, String accountPassWord);


	public RemoteResult getAccountInfo(String coinCode,Long customerId);


	/**
	 * 获取虚拟账户
	 * @param customerId
	 * @param currencyType
	 * @param website
	 * @return
	 */
	public AppAccountManage getByCustomerIdAndType(Long customerId,String currencyType,String website);
	
	/**
	 * 判断申请的该地区的代理是否已存在
	 * 
	 */
	public boolean isAgentExist(String agentLevel, String provinceId,String cityId, String countyId);
	
	/**
	 * 获取一个我方账户对象
	 * @param website
	 * @param currencyType
	 * @param accountType
	 * @return
	 */
	public AppOurAccountManage getOurAccount(String website,String currencyType,String accountType);

	//start交易
	/**
	 * 下单
	 * @param exEntrust
	 * @return
	 */
	public String[] addEntrust(EntrustTrade exEntrust); 
	/**
	 * 下单检查
	 * @param exEntrust
	 * @return
	 */
	public String[] addEntrustCheck(EntrustTrade exEntrust);
	/**
	 * 
	 * <p>
	 * 撤销委托
	 * </p>
	 * 
	 * @author: Gao Mimi
	 * @param: @param entrustNums
	 * @return: void
	 * @Date : 2016年4月19日 下午4:42:22
	 * @throws:
	 */
	public String[] cancelExEntrust(EntrustTrade entrustTrade);
	//end交易

	/**
	 * 查询发行的币种
	 * @return
	 */
	public RemoteResult finaCoins();


	
	/**
	 * 获取资金账户
	 * @param mobile
	 */
	public MyAccountTO myAccount(Long customerId);
	
	/**
	 * 查询手机号是否存在
	 * @param telphone
	 * @return
	 */
	public RemoteResult selectPhone(String telphone);
	/**
	 * 查询币账户
	 * @param customerId
	 * @return
	 */
	public List<CoinAccount> findCoinAccount(Long customerId);
	/**
	 * 修改密码
	 * @param pwd
	 * @param tel
	 * @return
	 */
	public RemoteResult updatepwd(String pwd,String tel);
	
	/**
	 * 通过手机号获取User对象
	 * @param tel
	 * @return
	 */
	public User selectByTel(String tel);
	
	/**
	 * app端修改交易密码
	 * @param username
	 * @param accountPassWord
	 * @return
	 */
	public RemoteResult appresetapw(String username, String accountPassWord);
	
	/**
	 * 获取人民币虚拟账户
	 * @param id
	 * @return
	 */
	public CoinAccount getAppaccount(Long id);
	/**
	 * test
	 * @param id
	 * @return
	 */
	public RemoteResult testAppCustomer(String username);


	//public RemoteResult setgoogle(String mobile, String savedSecret);


	RemoteResult sendgoogle(String mobile, String savedSecret);

	
	/**
	 * 查询登录用户的人民币账户id和币账户 id
	 * @param id
	 * @return
	 */
	public Map<String,Long> findAllAccountId(String id);

	/**
	 * 用户的委托单记录
	 * @param id
	 * @return
	 */
	public  List<Map<String, List<EntrustTrade>>> findExEntrustBycust(Long customerId);
	
	
	RemoteResult jcgoogle(String mobile, String savedSecret);

	/**
	 * 激活注册账号
	 * @param codeaa
	 * @return
	 */
	public RemoteResult activation(String code);
	
	
	RemoteResult savaIp(String mobile,String messIp);


	RemoteResult setPhone(String mobile, String username);
	
	/**
	 * 实名认证2
	 */
	public RemoteResult xstar(String userCode, String trueName, String sex, String surname, String country, String cardType, String cardId, String[] pathImg, String type,String language);

	RemoteResult setvail(String username, String oldPassWord);


	RemoteResult offPhone(String mobile, String username);


	RemoteResult setpwvail(String username, String oldPassWord, String newPassWord);


	public RemoteResult regphone(String mobile);

	public void cancelCustAllExEntrust(EntrustTrade entrustTrade);
	
	public RemoteResult updatepwdemail(String passwd,String username);


	public RemoteResult forget(String email, String password);


	public RemoteResult emailvail(String code);


	public BigDecimal myBtcCount(Long customerId, String code);

	/**
	 * 查询虚拟账户
	 * @param customerId
	 * @return
	 */
	public AppAccountManage getAppAccountManage(Long customerId);

	/**
	 * 查询代理商账户
	 * @param customerId
	 * @return
	 */
	public RemoteResult selectAgent(String referralCode);

	RemoteResult selectCommend(String username, String property);

	List<commendCode> selectCommendfind(String username);

	public List<commendCode> selectCommendRanking();


	public List<C2cOrder> c2cNewBuyOrder();

	public List<C2cOrder> c2cNewSellOrder();

    User selectByCustomerId(Long username);
    
	/**
	 * 设置c2c订单  status2字段 的状态
	 * @param transactionNum
	 * @param status2
	 */
	public boolean setc2cTransactionStatus2(String transactionNum, int status2,String remark);

    String[] checkPing(Long customerId);
    
	/**
	 * 查询c2c订单
	 * @param params
	 * @return
	 */
	public FrontPage c2clist(Map<String, String> params);
	
	/**
	 * 手机注册 
	 * @param mobile
	 * @param password
	 * @param referralCode
	 * @param string
	 * @return
	 */
	public RemoteResult registMobile(String mobile, String password, String referralCode, String country,String string);

    /**
     * 判断用户的币账户和资金账户是否有负数
     * @param customerId
     * @return
     */
    public RemoteResult canTakeMoney(String customerId);

    /**
     * 判断取消登录验证
     * @param customerId
     * @return
     */
	public RemoteResult offLoginPhone(String mobile, String username);

	/**
     * 新增判断添加登录验证
     * @param customerId
     * @return
     */
	public RemoteResult setLoginPhone(String mobile, String username);
	
	/**
	 * 查询手机号是否存在
	 * @author zongwei
	 * @param telphone
	 * @time  2018-04-27
	 * @return
	 */
	public RemoteResult checknoPhone(String telphone,String country);
	
	  /**
		 * 查询otc事物
		 * @author zongwei
		 * @param telphone
		 * @time  2018-05-08
		 * @return
		 */
	public RemoteResult  getOtcTransactionAll(String transactionType ,String status, String status2);
	
	/*
	 * 查询字典表
	 */
	public Map<String, String> getappDictionary(String key);

	public RemoteResult updateThird(User user);


	/**
	 * 查询otc从清单中购买
	 * @author zongwei
	 * @param id  清单id
	 * @param customerId  操作者id
	 * @time  2018-05-08
	 * @return
	 */
	public RemoteResult  createOrderTransaction(Long customerId,Long id,BigDecimal transactioncount);


	public RemoteResult getOtcTransaction(String transactionType);

	public RemoteResult getOtclist(String transactionType,String coinCode,String OrderByClause);


	public FrontPage otcorderlistall(Map<String, String> params);


	/**
	 * 获卖OTC交易订单列表
	 * @param
	 * @return
	 */
	public FrontPage otcorderselllist(Map<String, String> params);

	/**
	 * 获买OTC交易订单列表
	 * @param
	 * @return
	 */
	public FrontPage otcorderbuylist(Map<String, String> params);
	

	/**
	 * 新增用户
	 * @param
	 * @return
	 */
	RemoteResult registMobile1(String mobile, String password, String referralCode, String country, String language);


	/**
	 * 获买OTC交易订单列表
	 * @param
	 * @return
	 */
	public RemoteResult getOtcbyid(Long id);




	/**
	 * OTC完成付款
	 * @param otcOrderTransactionMange
	 * @return
	 */
	public RemoteResult otcPayment(OtcOrderTransactionMange otcOrderTransactionMange);



	/**
	 * OTC申诉
	 * @param otcOrderTransactionMange
	 * @return
	 */
	public RemoteResult confirmotcApplyArbitration(OtcOrderTransactionMange otcOrderTransactionMange);


	/**
	 * OTC确认到账
	 * @param otcOrderTransactionMange
	 * @return
	 */
	public RemoteResult finishOtcOrder(OtcOrderTransactionMange otcOrderTransactionMange);


    /**
     * OTC撤销
     * @param otcOrderTransactionMange
     * @return
     */
    public RemoteResult otcUndo(OtcOrderTransactionMange otcOrderTransactionMange);



	public FrontPage getOtclists(Map<String, String> params);


	public RemoteResult OtcListclose(Long id);


	public OtcOrderTransactionMange selectOtcOrderbyid(Long id);

	/**
	 * 校验唯一
	 * @param unique
	 * @return
	 */
	public Boolean checkUnique(UniqueRecord unique);


	public RemoteResult setMail (Long customerId,String mail);



	public RemoteResult  cancelMail (Long customerId,String mail);
	

	//修改前台用户返佣显示状态
	public FrontPage rakebake(Map<String, String> params);



}
