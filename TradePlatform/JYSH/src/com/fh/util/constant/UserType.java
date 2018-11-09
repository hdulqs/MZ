package com.fh.util.constant;

public enum UserType {
	COMMON_USER("普通用户","U1"),
	CHUANGYE_USER("创业者","U2"),
	TOUZI_USER("投资者","U3"),
	
	QUXIAN_AGENT("区县代理","U4"),
	DISHI_AGENT("地市代理","U5"),
	
	YUNYING_COMPANY("市场运营公司","U6"),
	Terminal_shop("终端店铺","U7"),
	supplier("供应商","U8"),
	Special_COMPANY("特约联盟商户","U9");
	
	
	private String name;
	private String type;
	private  UserType(String name,String type){
		this.name = name;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
