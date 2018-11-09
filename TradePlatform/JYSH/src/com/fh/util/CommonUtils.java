package com.fh.util;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * @Discription TODO
 * @author chenjie
 * @time 2017年4月28日 下午6:05:19
 *
 */
public class CommonUtils {

	public static String parseString(String src) {
		if (src == null) {
			return "";
		} else {
			return src.trim();
		}
	}

	/**
	 * 判断是否为空字符�?
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isEmptyString(String src) {

		return src == null || src.trim().length() < 1;

	}

	/**
	 * 判断是否整数
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isInteger(String src) {

		try {

			Integer.parseInt(src);

		} catch (Exception e) {

			return false;

		}

		return true;
	}

	/**
	 * 判断是否Long�?
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isLong(String src) {

		try {

			Long.parseLong(src);

		} catch (Exception e) {

			return false;

		}

		return true;
	}

	/**
	 * 将字符串转换为Int�?
	 * 
	 * 此方法将不会抛出NumberFormatException和NullPointerException，出现无法转换时�?则返�?1
	 * 
	 * @param s
	 * @return
	 */
	public static int parseInt(String s) {

		return parseInt(s, -1);

	}

	public static int parseInt(String s, int defaultValue) {

		try {

			return Integer.parseInt(s);

		} catch (Exception e) {

			return defaultValue;

		}
	}
	

	public static float parseFloat(String s, float defaultValue) {

		try {

			return Float.parseFloat(s);

		} catch (Exception e) {

			return defaultValue;

		}
	}

	/**
	 * 将字符串转换为Long�?
	 * 
	 * 此方法将不会抛出NumberFormatException和NullPointerException，出现无法转换时�?则返�?1
	 * 
	 * @param s
	 * @return
	 */
	public static long parseLong(String s) {

		return parseLong(s, -1l);

	}

	/**
	 * 将字符串转换为Long�?
	 * 
	 * 此方法将不会抛出NumberFormatException和NullPointerException，出现无法转换时�?
	 * 则返回defaultValue
	 * 
	 * @param s
	 * @param defaultValue
	 * @return
	 */
	public static long parseLong(String s, long defaultValue) {

		try {

			return Long.parseLong(s);

		} catch (Exception e) {

			return defaultValue;

		}
	}

	

	public static String getRandomString() {
		return getRandomString(12);
	}

	/**
	 * 获取�?��类BASE64编码的随机字符串
	 * 
	 * @param num
	 * @return
	 */
	public static String getRandomString(int num) {
		Random rd = new Random();
		StringBuilder content = new StringBuilder(num);

		for (int i = 0; i < num; i++) {
			int n;
			while (true) {
				n = rd.nextInt('z' + 1);
				if (n >= '0' && n <= '9')
					break;
				if (n >= 'a' && n <= 'z')
					break;
				if (n >= 'A' && n <= 'Z')
					break;
			}
			content.append((char) n);
		}
		return content.toString();
	}

	/**
	 * 获取�?��从min到max的随机整�?
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomNumber(int min, int max) {
		Random rd = new Random();
		int ret = rd.nextInt(max);

		while (ret < min)
			ret += rd.nextInt(max - min);

		return ret;
	}

	/**
	 * 判断Email (Email由帐号@域名组成，格式为xxx@xxx.xx)<br>
	 * 帐号由英文字母�?数字、点、减号和下划线组成，<br>
	 * 只能以英文字母�?数字、减号或下划线开头和结束�?br>
	 * 域名由英文字母�?数字、减号�?点组�?br>
	 * www.net.cn的注册规则为：只提供英文字母、数字�?减号。减号不能用作开头和结尾�?中文域名使用太少，暂不�?�?<br>
	 * 实际查询�?12.com已被注册�?br>
	 * 以下是几大邮箱极限数据测试结�?br>
	 * 163.com为字母或数字�?��和结束�?<br>
	 * hotmail.com为字母开头，字母、数字�?减号或下划线结束�?br>
	 * live.cn为字母�?数字、减号或下划线开头和结束。hotmail.com和live.cn不允许有连续的句号�?
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {

		return isEmptyString(email) ? false
				: PatternUtils
						.regex("^[\\w_-]+([\\.\\w_-]*[\\w_-]+)?@[\\w-]+\\.[a-zA-Z0-9]+(\\.[a-zA-Z]+)?$",
								email.trim(), true);
	}
	
	/**
	 * 获取邮箱域名
	 * @param email
	 * @return
	 */
	public static String getEmailDomain(String email){
		try{
			return email.substring(email.lastIndexOf("@")+1).toLowerCase();
		}catch(Exception e){
			return "";
		}
	}

	/**
	 * 从输入字符串中截取EMAIL
	 * 
	 * @param input
	 * @return
	 */
	public static String parseEmail(String input) {

		String regex = "[\\s\\p{Punct}]*([\\w_-]+([\\.\\w_-]*[\\w_-]+)?@[\\w-]+\\.[a-zA-Z]+(\\.[a-zA-Z]+)?)[\\s\\p{Punct}]*";

		return PatternUtils.parseStr(input, regex, 1);
	}

	/**
	 * 判断是否为手机号
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {

		return isEmptyString(mobile) ? false : PatternUtils.regex(
				"^(\\+86(\\s)?)?0?1(3|4|5|7|8)\\d{9}$", mobile, true);

	}

	/**
	 * 将带有区号的手机号进行标准格式转�?
	 * 
	 * @param mobile
	 * @return
	 */
	public static String getPhoneNumber(String phoneNumber, boolean mobileOnly) {

		if (isEmptyString(phoneNumber))
			return "";

		phoneNumber = phoneNumber.replaceAll("[^\\d]", "");
		if (phoneNumber.startsWith("86"))
			phoneNumber = phoneNumber.replaceFirst("86", "");

		String ret = PatternUtils.parseStr(phoneNumber.replaceAll("\\s*", ""),
				"0?(1(3|4|5|8)\\d{9})", 1);

		return isMobile(ret) ? phoneNumber.startsWith("0") ? phoneNumber
				.replaceFirst("0", "") : phoneNumber : mobileOnly ? "" : ret;

	}

	/**
	 * 判断半角标点符号(�?US-ASCII)
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isPunct(String src) {

		String regex = "\\p{Punct}";

		return PatternUtils.regex(regex, src, false);
	}

	/**
	 * String数组转化成以','分割的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String arrayToString(String[] strArray) {

		if (strArray == null || strArray.length < 1)
			return "";

		String s = Arrays.toString(strArray).replaceFirst("\\[", "");

		if (isEmptyString(s))
			return "";

		return s.substring(0, s.length() - 1);

	}

	/**
	 * List转化成以','分割的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static <E> String collectionToString(Collection<E> collection) {

		if (collection == null || collection.isEmpty())
			return "";

		String s = collection.toString().replaceFirst("\\[", "");

		if (isEmptyString(s))
			return "";

		return s.substring(0, s.length() - 1);

	}
	
	
	/**
	 * List转化成以'',''分割的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static <E> String collectionToSQTString(Collection<String> collection) {
		if(collection.isEmpty())
			return "";
		
		StringBuffer sber = new StringBuffer();
		int i = 0;
		for(String str : collection ){
			i++;
			sber.append("'").append(str).append("'");
			if(i<collection.size())
				sber.append(",");
		}
		return sber.toString();

	}

	/**
	 * 末尾�?��截取字符�?
	 * 
	 * @param str
	 * @return
	 */
	public static String subString(String str, int len) {

		if (CommonUtils.isEmptyString(str))
			return "";

		if (str.length() < len)
			throw new IllegalArgumentException(
					"The input string's length must>" + len);

		return str.substring(str.length() - len);

	}

	/**
	 * 按指定长度截取字符串
	 * 
	 * @param src
	 * @param length
	 * @return
	 */
	public static String intercept(String src, int length) {

		if (isEmptyString(src))
			return "";

		int len = src.length();
		int lng = src.getBytes().length;

		return lng < length ? src : src.substring(0,
				(int) ((length - 6) * len / lng)) + "";
	}

	/**
	 * 获取传入小数的货币表现形�?
	 * 
	 * @param money
	 * @return
	 */
	public static String formatMoney(double money) {

		if ((int) money == money)
			return Integer.toString((int) money);

		return formatMoney("0.00", money);

	}

	/**
	 * 获取传入小数的货币表现形�?
	 * 
	 * @param format
	 *            指定的表现形�?
	 * @param money
	 * @return
	 */
	public static String formatMoney(String format, double money) {

		DecimalFormat decimalFormat = new DecimalFormat(format);

		return decimalFormat.format(money);

	}

	

	

	

	/**
	 * 判断是否为电话号�?
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {

		return PatternUtils
				.regex("(?s)\\+?(\\s*(\\(\\d+\\)|\\d+)\\s*)*(\\d-?)+\\d+",
						phone, true);

	}
	
	
	public static int div(int one,int two){
		if(two==0)
			return 0;
		else
			return one%two==0?one/two:(one/two+1);
	}



	// TEST
	public static void main(String args[]) {
		System.out.println(parseFloat("9.5", 0));
	}
}
