package com.mz.trade.model;

public enum AccountResultEnum {

    FAULT(0, "失败"),
    SUCCESS(1, "成功"),
    SAVE_FAULT(2, "保存失败"),
    ACCOUNT_NO_ALIVE(1000, "法币账户不存在"),
    ACCOUNT_HOT_MONEY_NEGATE(1001, "法币热钱将为负"),
    ACCOUNT_COLD_MONEY_NEGATE(1002, "法币冷钱将为负"),
    COIN_ACCOUNT_NOT_ALIVE(1010, "数字货币账户不存在"),
    COIN_ACCOUNT_HOT_MONEY_NEGATE(1011, "数字货币账户热钱将为负"),
    COIN_ACCOUNT_COLD_MONEY_NEGATE(1012, "数字货币账户冷钱将为负");

    private int code;
    private String message;

    AccountResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
