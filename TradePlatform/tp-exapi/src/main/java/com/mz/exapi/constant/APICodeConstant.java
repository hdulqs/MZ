/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2016年5月12日 上午10:09:31
 */
package com.mz.exapi.constant;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年5月12日 上午10:09:31 
 */
public class APICodeConstant {
	


	/**
	 * 成功编码
	 */
	public static final String CODE_SUCCESS="8888";
	
	/**
	 * 失败编码
	 */
	public static final String CODE_FAILED="0000";
	/**
	 * 一般错误提示
	 */
	public static final String CODE_Error_Tips="1000";
	
	/**
	 * 内部错误
	 */
	public static final String CODE_Internal_Error="1001";
	/**
	 * 验证不通过
	 */
	public static final String CODE_Validate_No_Pass="1002";
	
	/**
	 * 用户不存在
	 */
	public static final String CODE_No_Such_User="1003";
	
	/**
	 * 人民币账户不存在
	 */
	public static final String CODE_No_Such_Account="1004";
	/**
	 * 币账户不存在
	 */
	public static final String CODE_No_Such_CoinAccount="1005";

	
	/**
	 * 无效的参数
	 */
	public static final String CODE_Invalid_Arguments="5000";
	/**
	 * 无效的IP或与绑定的IP不一致
	 */
	public static final String CODE_Invalid_Ip_Address="5001";
	/**
	 * 方法调错
	 */
	public static final String CODE_Method_Error="5002";
	/**
	 * 访问公钥已失效
	 */
	public static final String CODE_Key_Invalid="5003";
	/**
	 * 验签失败
	 */
	public static final String CODE_SignData_Invalid="5004";
	
	/**
	 * API接口被锁定或未启用
	 */
	public static final String CODE_API_Locked_Or_Not_Enabled="6000";
	/**
	 * 请求过于频繁
	 */
	public static final String CODE_Request_Too_Frequently="6001";


	
	/**
	 * 人民币账户余额不足
	 */
	public static final String CODE_CNY_Balance="2000";
	
	/**
	 * 币账户余额不足
	 */
	public static final String CODE_COIN_Balance="2001";
	/**
	 * 委托单没有找到
	 */
	public static final String CODE_Not_Found_Entrust="2002";
	/**
	 * 币种账号不存在
	 */
	/**
	 * 无效的金额
	 */
	public static final String CODE_Invalid_Money="2003";
	/**
	 * 无效的数量
	 */
	public static final String CODE_Invalid_Amount="2004";
	/**
	 * 人民币账户不存在
	 */
	public static final String CODE_No_Such_Product="2005";
	/**
	 * 提币地址不存在
	 */
	public static final String CODE_Not_Found_DM_RechangAdd="2006";
	/**
	 *提现地址不存在
	 */
	public static final String CODE_Not_Found_DM_WithAdd="2007";

}
