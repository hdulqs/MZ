/**
 * 
 */
package com.mz.core.exception;

/**
 * @author TANFUCHAO
 * 注意：提示信息不要加逗号（，） 目前贷款可用错误码段为：7000-7400
 * 
 */
public enum HryError {
	
//	框架错误代码 start   7001 - 7099
	FILE_ERROR("0004", "不支持此类型文件格式"),
	FILE_TOBIG("0003", "文件太大"),
	FILE_EMPTY("0002","文件为空"),
	UPLOAD_FILED("0001", "上传失败"),
	UPLOAD_SUCCESS("0000", "成功");

	HryError(String code, String reason) {
		if ("9002".equals(code) || "0030".equals(code) || "0031".equals(code)
				|| "0023".equals(code) || "0040".equals(code)) {
			code = "011007";
			reason = "处理超时";
		}
		this.code = code;
		this.reason = reason;
	}

	String code, reason;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
