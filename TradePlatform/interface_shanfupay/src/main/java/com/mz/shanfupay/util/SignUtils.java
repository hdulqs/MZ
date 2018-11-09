package com.mz.shanfupay.util;

import com.mz.shanfupay.Shanfupay;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import org.jasig.cas.client.util.CommonUtils;

public class SignUtils {

	private static String encodingCharset = "UTF-8";

	/**
	 * 闪付请求的签名加密
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Zhang Lei
	 * @param: @param shanfu
	 * @param: @param keyValue 闪付提供的商户key
	 * @param: @return
	 * @return: String
	 * @Date : 2016年12月2日 下午4:31:34
	 * @throws:
	 */
	public static String getReqMd5Hmac(Shanfupay shanfu, String keyValue) {
		StringBuffer sValue = new StringBuffer();
		// 业务类型
		if (CommonUtils.isNotEmpty(shanfu.getP0_Cmd())) {
			sValue.append(shanfu.getP0_Cmd());
		}
		// 商户编号
		if (CommonUtils.isNotEmpty(shanfu.getP1_MerId())) {
			sValue.append(shanfu.getP1_MerId());
		}
		// 商户订单号
		if (CommonUtils.isNotEmpty(shanfu.getP2_Order())) {
			sValue.append(shanfu.getP2_Order());
		}
		// 支付金额
		if (CommonUtils.isNotEmpty(shanfu.getP3_Amt())) {
			sValue.append(shanfu.getP3_Amt());
		}
		// 交易币种
		if (CommonUtils.isNotEmpty(shanfu.getP4_Cur())) {
			sValue.append(shanfu.getP4_Cur());
		}
		// 商品名称
		if (CommonUtils.isNotEmpty(shanfu.getP5_Pid())) {
			sValue.append(shanfu.getP5_Pid());
		}
		// 商品种类
		if (CommonUtils.isNotEmpty(shanfu.getP6_Pcat())) {
			sValue.append(shanfu.getP6_Pcat());
		}
		// 商品描述
		if (CommonUtils.isNotEmpty(shanfu.getP7_Pdesc())) {
			sValue.append(shanfu.getP7_Pdesc());
		}
		// 商户接收支付成功数据的地址
		if (CommonUtils.isNotEmpty(shanfu.getP8_Url())) {
			sValue.append(shanfu.getP8_Url());
		}
		// 送货地址
		if (CommonUtils.isNotEmpty(shanfu.getP9_SAF())) {
			sValue.append(shanfu.getP9_SAF());
		}
		// 商户扩展信息
		if (CommonUtils.isNotEmpty(shanfu.getPa_MP())) {
			sValue.append(shanfu.getPa_MP());
		}
		// 银行编码
		if (CommonUtils.isNotEmpty(shanfu.getPd_FrpId())) {
			sValue.append(shanfu.getPd_FrpId());
		}
		// 应答机制
		if (CommonUtils.isNotEmpty(shanfu.getPr_NeedResponse())) {
			sValue.append(shanfu.getPr_NeedResponse());
		}

		String sNewString = null;

		System.out.println("请求签名初始串-----" + sValue);
		sNewString = hmacSign(sValue.toString(), keyValue);
		System.out.println("请求加密后的签名-------" + sNewString);
		return (sNewString);
	}
	
	
	/**
	 * 闪付响应的签名加密
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Zhang Lei
	 * @param: @param map  响应的参数集合
	 * @param: @param keyValue 闪付提供的商户key
	 * @param: @return
	 * @return: String
	 * @Date : 2016年12月2日 下午4:31:34
	 * @throws:
	 */
	public static String getResMd5Hmac(Map<String, Object> map, String keyValue) {
		StringBuffer sValue = new StringBuffer();
		// 商户编号
		if (map.containsKey("p1_MerId") && CommonUtils.isNotEmpty(map.get("p1_MerId").toString())) {
			sValue.append(map.get("p1_MerId").toString());
		}
		// 业务类型
		if (map.containsKey("r0_Cmd") && CommonUtils.isNotEmpty(map.get("r0_Cmd").toString())) {
			sValue.append(map.get("r0_Cmd").toString());
		}
		// 支付结果
		if (map.containsKey("r1_Code") && CommonUtils.isNotEmpty(map.get("r1_Code").toString())) {
			sValue.append(map.get("r1_Code").toString());
		}
		// 闪付支付交易流水号
		if (map.containsKey("r2_TrxId") && CommonUtils.isNotEmpty(map.get("r2_TrxId").toString())) {
			sValue.append(map.get("r2_TrxId").toString());
		}
		// 支付金额
		if (map.containsKey("r3_Amt") && CommonUtils.isNotEmpty(map.get("r3_Amt").toString())) {
			sValue.append(map.get("r3_Amt").toString());
		}
		// 交易币种
		if (map.containsKey("r4_Cur") && CommonUtils.isNotEmpty(map.get("r4_Cur").toString())) {
			sValue.append(map.get("r4_Cur").toString());
		}
		// 商品名称
		if (map.containsKey("r5_Pid") && CommonUtils.isNotEmpty(map.get("r5_Pid").toString())) {
			sValue.append(map.get("r5_Pid").toString());
		}
		// 商户订单号
		if (map.containsKey("r6_Order") && CommonUtils.isNotEmpty(map.get("r6_Order").toString())) {
			sValue.append(map.get("r6_Order").toString());
		}
		// 闪付支付会员ID
		if (map.containsKey("r7_Uid") && CommonUtils.isNotEmpty(map.get("r7_Uid").toString())) {
			sValue.append(map.get("r7_Uid").toString());
		}
		// 商户扩展信息
		if (map.containsKey("r8_MP") && CommonUtils.isNotEmpty(map.get("r8_MP").toString())) {
			sValue.append(map.get("r8_MP").toString());
		}
		// 交易结果返回类型
		if (map.containsKey("r9_BType") && CommonUtils.isNotEmpty(map.get("r9_BType").toString())) {
			sValue.append(map.get("r9_BType").toString());
		}
		
		String sNewString = null;
		
		System.out.println("相应签名初始串-----" + sValue);
		sNewString = hmacSign(sValue.toString(), keyValue);
		System.out.println("相应加密后的签名-------" + sNewString);
		return (sNewString);
	}

	/**
	 * 加密方法
	 * 
	 * @param aValue
	 * @param aKey
	 * @return
	 */
	public static String hmacSign(String aValue, String aKey) {
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		try {
			keyb = aKey.getBytes(encodingCharset);
			value = aValue.getBytes(encodingCharset);
		} catch (UnsupportedEncodingException e) {
			keyb = aKey.getBytes();
			value = aValue.getBytes();
		}

		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {

			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return toHex(dg);
	}

	public static String toHex(byte input[]) {
		if (input == null)
			return null;
		StringBuffer output = new StringBuffer(input.length * 2);
		for (int i = 0; i < input.length; i++) {
			int current = input[i] & 0xff;
			if (current < 16)
				output.append("0");
			output.append(Integer.toString(current, 16));
		}

		return output.toString();
	}

	/**
	 * 
	 * @param args
	 * @param key
	 * @return
	 */
	public static String getHmac(String[] args, String key) {
		if (args == null || args.length == 0) {
			return (null);
		}
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			str.append(args[i]);
		}
		return (hmacSign(str.toString(), key));
	}

	/**
	 * @param aValue
	 * @return
	 */
	public static String digest(String aValue) {
		aValue = aValue.trim();
		byte value[];
		try {
			value = aValue.getBytes(encodingCharset);
		} catch (UnsupportedEncodingException e) {
			value = aValue.getBytes();
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		return toHex(md.digest(value));

	}

	public static void main(String[] args) {
		// System.out.println(hmacSign("Buy26ccb1234561.0CNYddddd","JgHyeiSJfBXQkIYVN5aI7xMGbFFbnJTH"));
		System.out.println(CommonUtils.isNotEmpty(null));
	}

}
