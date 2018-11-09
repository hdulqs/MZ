package com.mz.utils;

import com.mz.core.mvc.model.page.JsonResult;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2017年3月13日 下午7:37:57
 */
public class CommonUtil {

	/**
	 * 16进制前缀
	 */
	public static final String HEXPREFIX = "0x";

	/**
	 * 判断string list中是否有无效元素
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             list
	 * @param: @return
	 * @return: boolean
	 * @Date : 2017年11月3日 上午9:42:19
	 * @throws:
	 */
	public static boolean isNoHasEmptyInListstr(List<String> list) {
		for (String l : list) {
			if (l == null || "".equals(l.trim())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断string 数组是否有空
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             arr
	 * @param: @return
	 * @return: boolean
	 * @Date : 2017年11月13日 下午4:59:56
	 * @throws:
	 */
	public static boolean isNohashEmptyInArr(String[] arr) {
		for (String l : arr) {
			if (l == null || "".equals(l.trim())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             str
	 * @param: @param
	 *             digit
	 * @param: @return
	 * @return: String
	 * @Date : 2017年12月21日 下午7:35:26
	 * @throws:
	 */
	public static String strRoundDown(String str, int digit) {
		BigDecimal a = new BigDecimal(str);
		a = a.setScale(digit, BigDecimal.ROUND_DOWN);
		return a.toString();
	}

	/**
	 * 
	 * <p>
	 * 解析比特币异常
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             error
	 * @param: @return
	 * @return: JsonResult
	 * @Date : 2018年1月15日 下午4:32:51
	 * @throws:
	 */
	public static JsonResult analysisBitcoinException(String msg) {
		JsonResult result = new JsonResult();
		int start = msg.indexOf("{");
		int end = msg.lastIndexOf("}") + 1;
		String str = msg.substring(start, end);
		Map<String, Object> map = com.alibaba.fastjson.JSON.parseObject(str, Map.class);
		String error = map.get("error").toString();
		Map<String, Object> data = com.alibaba.fastjson.JSON.parseObject(error, Map.class);
		result.setSuccess(false);
		result.setCode(data.get("code").toString());
		result.setMsg(data.get("message").toString());
		return result;
	}

}
