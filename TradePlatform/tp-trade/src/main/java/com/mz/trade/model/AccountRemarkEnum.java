package com.mz.trade.model;

public enum  AccountRemarkEnum {
	 TYPE1("委托下单", 1),
	 TYPE2("交易成功，-买家解冻总金额转给卖家", 2),
	 TYPE3("买家市价，-完成还剩一点金额给解冻到自己账户", 3), 
	 TYPE4("买家市价，+完成还剩一点金额给解冻到自己账户", 4),  
	 TYPE5("买的价格向下浮动，-要退钱", 5),  
	 TYPE6("买的价格向下浮动，+要退钱", 6),  
	 TYPE7("交易成功，+卖家收入金额", 7),  
	 TYPE8("交易成功，-卖家手续费", 8),  
	 TYPE9("交易成功，+买家收入币", 9),  
	 TYPE10("交易成功，-买家手续费", 10),
	 TYPE11("交易成功，-卖家虚拟币解冻转买家", 11),
	 TYPE12("撤销委托，解冻到自己钱包", 12),
	 TYPE21("充值确认", 21),
	 TYPE22("确认提现", 22),
	 TYPE23("手动充值", 23),
	 TYPE24("手动充值又撤销", 24),
	 TYPE25("手动提现", 25),
	 TYPE31("充币", 31),
	 TYPE32("人民币提现", 32),
	 TYPE33("提币审核成功", 33),
	 TYPE34("提币手续费审核成功", 34),
	 TYPE35("申请币提现", 35),
	TYPE36("申请币提现手续费", 36),
	TYPE37("提币已驳回", 37),
	TYPE38("人民币提现驳回", 38),
	
	TYPE40("佣金派发", 40),
	
	TYPE50("ctc冻结", 50),
	TYPE51("ctc解冻", 51),
	TYPE52("OTC交易", 52),
	TYPE53("币账户修复", 53);
    private String name;  
    private int index;  
    private AccountRemarkEnum(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  
    public static String getName(int index) {  
        for (AccountRemarkEnum c : AccountRemarkEnum.values()) {  
            if (c.getIndex() == index) {  
                return c.name;  
            }  
        }  
        return String.valueOf(index);  
    }  
    // get set 方法  
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public int getIndex() {  
        return index;  
    }  
    public void setIndex(int index) {  
        this.index = index;  
    }  
}
