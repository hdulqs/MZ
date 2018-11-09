/**
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月6日 下午3:18:41
 */
package com.mz.remote;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.utils.CommonRequest;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 第三方支付远程调用接口(用于项目之间的远程调用)
 * 
 * @author: Zhang Xiaofang
 * @Date : 2016年7月6日 下午3:18:41
 */
public interface RemoteThirdPayInterfaceService {

	/**
	 * 
	 * <p>
	 * 充值
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: String
	 * @Date : 2016年7月6日 下午3:22:04
	 * @throws:
	 */

	public JsonResult recharge(HttpServletResponse response, CommonRequest request);

	/**
	 * 
	 * <p>
	 * 充值
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: String
	 * @Date : 2016年7月6日 下午3:22:04
	 * @throws:
	 */
	public JsonResult recharge(CommonRequest request);

	/**
	 * 
	 * <p>
	 * 提现
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: String
	 * @Date : 2016年7月6日 下午3:22:04
	 * @throws:
	 */
	public CommonRequest withdraw(CommonRequest request);

	/**
	 * 
	 * <p>
	 * 查询订单
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: String
	 * @Date : 2016年7月6日 下午3:22:04
	 * @throws:
	 */
	public JsonResult queryOrder(CommonRequest request);

	/**
	 * 
	 * <p>
	 * 充值回调
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: String
	 * @Date : 2016年7月6日 下午3:22:04
	 * @throws:
	 */

	public CommonRequest rechargeCallBack(Map<String, Object> map);

	/**
	 * 
	 * <p>
	 * 提现回调
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: String
	 * @Date : 2016年7月6日 下午3:22:04
	 * @throws:
	 */
	public CommonRequest withdrawCallBack(Map<String, Object> map);

	/**
	 * 查询身份证信息
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: CommonRequest
	 * @Date : 2016年9月21日 上午10:43:36
	 * @throws:
	 */
	public JsonResult checkIdentity(CommonRequest request);

	/**
	 * 
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Zhang Lei
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: JsonResult
	 * @Date : 2016年12月9日 下午8:39:39
	 * @throws:
	 */
	public JsonResult shanFuQueryOrder(CommonRequest request);

	/**
	 * 后台有开关功能 查询接口是否开启
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: CommonRequest
	 * @Date : 2016年9月21日 上午10:43:36
	 * @throws:
	 */
	public String getIsOpen(String key);

	/**
	 * 会员开立子账户
	 * 
	 * @param customerAccount
	 * @param customerName
	 * @param phone
	 * @param email
	 * 
	 * @return TxnReturnCode 000000成功 其他都是失败 CnsmrSeqNo 请求流水 TxnReturnMsg 返回消息
	 */
	JsonResult openCustAcctId(Long customerAccount, String customerName, Long phone, String email);

	/**
	 * 会员绑定提现账户-银联鉴权 绑定银行卡
	 * 
	 * @param SubAcctNo
	 *            子账户账号
	 * @param customerAccount
	 *            账号
	 * @param customerName
	 *            会员名称
	 * @param MemberGlobalType
	 *            会员证件类型
	 * @param MemberGlobalId
	 *            会员证件号码
	 * @param MemberAcctNo
	 *            会员账号
	 * @param BankType
	 *            银行类型
	 * @param AcctOpenBranchName
	 *            开户行名称
	 * @param EiconBankBranchId
	 *            超级网银号
	 * @param CnapsBranchId
	 *            大小额联行号
	 * @param Mobile
	 *            手机号 用来接收短信
	 * 
	 * @return TxnReturnCode 000000成功 其他都是失败 CnsmrSeqNo 请求流水 TxnReturnMsg 返回消息
	 * 
	 */
	Map<String, Object> bindRelateAcctUnionPay(Long SubAcctNo, Long customerAccount, String customerName, Integer MemberGlobalType, String MemberGlobalId, String MemberAcctNo, String BankType, String AcctOpenBranchName, String EiconBankBranchId, String CnapsBranchId, Long Mobile);

	/**
	 * 会员绑定提现账户-银联鉴权 绑定银行卡 回填短信验证码
	 * 
	 * @param TranNetMemberCode
	 *            账号
	 * @param SubAcctNo
	 *            会员子账号号码
	 * @param string
	 *            银行卡号
	 * @param MessageCheckCode
	 *            短信验证码
	 * 
	 * @return TxnReturnCode 000000成功 其他都是失败 CnsmrSeqNo 请求流水 TxnReturnMsg 返回消息
	 */
	Map<String, Object> bindRelateAccReUnionPay(Long SubAcctNo, Long customerAccount, String string, String MessageCheckCode);

	/**
	 * 会员解绑提现账户
	 * 
	 * @param TranNetMemberCode
	 *            账号
	 * @param SubAcctNo
	 *            会员子账号号码
	 * @param MemberAcctNo
	 *            银行卡号
	 * 
	 * @return TxnReturnCode 000000成功 其他都是失败 CnsmrSeqNo 请求流水 TxnReturnMsg 返回消息
	 */
	Map<String, Object> unbindRelateAcct(Long SubAcctNo, Long customerAccount, String MemberAcctNo);

	/**
	 * 充值 在途 MemberRecharge
	 * 
	 * @param transactionMoney
	 *            充值金额
	 * @param issInsCode
	 *            发卡机构代码
	 * @param customerAccount 
	 * 
	 * @return
	 */
	String[] memberRecharge(BigDecimal transactionMoney, String issInsCode, Long SubAccNo,String path, Long customerAccount);

	/**
	 * 提现 支持手续费
	 * 
	 * @param SubAcctNo
	 *            子账户账号
	 * @param customerAccount
	 *            会员账号
	 * @param customerName
	 *            会员名称
	 * @param TakeCashAcctNo
	 *            绑定银行卡号
	 * @param TakeCashAcctName
	 *            绑定银行卡开户人名称
	 * @param CashAmt
	 *            提现金额
	 * @param TakeCashCommission
	 *            手续费
	 * @return
	 */
	Map<String, Object> memberWithdrawCash(Long SubAcctNo, Long customerAccount, String customerName, String TakeCashAcctNo, String TakeCashAcctName, BigDecimal CashAmt, BigDecimal TakeCashCommission);

	/**
	 * 会员间交易(不验证)
	 * 
	 * @param OutSubAcctNo
	 *            付款方子账户
	 * @param OutMemberCode
	 *            付款方会员账户
	 * @param OutSubAcctName
	 *            付款方名称
	 * @param InSubAcctNo
	 *            收款方子账户
	 * @param InMemberCode
	 *            收款方会员账户
	 * @param InSubAcctName
	 *            收款方名称
	 * @param TranAmt
	 *            交易金额
	 * @param TranFee
	 *            交易手续费
	 */
	Map<String, Object> memberTransaction(Integer FunctionFlag,Long OutSubAcctNo, Long OutMemberCode, String OutSubAcctName, Long InSubAcctNo, Long InMemberCode, String InSubAcctName, BigDecimal TranAmt, BigDecimal TranFee);

	/**
	 * 查询单笔交易结果
	 * 
	 * @param FunctionFlag
	 *            2：会员间交易 3：提现 4：充值
	 * @param TranNetSeqNo
	 *            交易网流水号
	 * @param SubAcctNo
	 *            会员子账户
	 * @return
	 */
	Map<String, Object> singleTransactionStatusQuery(Integer FunctionFlag, String TranNetSeqNo, Long SubAcctNo);

	/**
	 * 查询会员账户信息
	 * 
	 * @param SubAcctNo
	 * @param PageNum
	 * @param QueryFlag
	 *            查询标志2：普通会员子账号 3：功能子账号
	 */
	Map<String, Object> custAcctIdBalanceQuery(Long SubAcctNo, Integer PageNum, Integer QueryFlag);

	/**
	 * 查询对账文件信息
	 * 
	 * @param fileDate
	 *            日期
	 * @param fileType
	 *            充值 CZ 提现 TX
	 * @return
	 */
	Map<String, Object> queryAccountFileInfo(String fileDate, String fileType);

	/**
	 * 下载对账文件
	 * @param remoteFile 对账文件 服务器路径
	 * @param localFile  本地路径
	 * @param privateAuth 提取码
	 * @param RandomPassword 随机密码  用来解密对账文件
	 */
	boolean downloadFile(String remoteFile, String localFile, String privateAuth, String RandomPassword);
	
	/**
	 * 查询平台余额
	 * @return
	 */
	Map<String, Object> supAcctIdBalanceQuery();
	
	void memberBindQuery();
	
	/**
	 *  查询银行清分状态
	 *  FunctionFlag 功能标志 1:全部，2：指定时间段  必填
	 *  StartDate    若是指定时间段查询，则必输，当查询全部时，不起作用  yyyyMMdd
	 *  EndDate	                 同上 
	 *  PageNum      页码 必填
	 */
	Map<String, Object> BankClearQuery(Integer FunctionFlag, String StartDate, String EndDate, Integer PageNum);
	
	
	/**
	 * 获取提现验证码
	 * @param SubAcctNo 会员子账号
	 * @param TranNetMemberCode 平台会员账号 
	 * @param TranAmt	交易金额
	 * @return 短信指令号	
	 */
	String applicationTextMsgDynamicCode(Long SubAcctNo, Long TranNetMemberCode, BigDecimal TranAmt);
	
	/**
	 * 冻结用户资金 不验证短信码
	 * @param FunctionFlag    	      功能标志          1：冻结（会员→担保）2：解冻（担保→会员）3：清分+冻结 4：见证+收单的冻结资金解冻
	 * @param customerAccount    平台账号
	 * @param SubAcctNo		                子账户    
	 * @param TranAmt			      冻结金额
	 * @param TranCommission     解冻时，将根据该金额收取手续费，若无手续费则送0。
	 * @param OrderContent       订单说明,不必填
	 */
	Map<String, Object> MembershipTrancheFreeze(Integer FunctionFlag, Long customerAccount, Long SubAcctNo, BigDecimal TranAmt, BigDecimal TranCommission, String OrderContent);


}
