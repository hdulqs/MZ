package com.mz.trade.redis.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Accountadd implements Serializable {

    // 账户id
    private Long accountId;

    private Integer acccountType;//0资金账号，1币账户
    private Integer monteyType;//1热账户，2，冷账号
    private BigDecimal money = BigDecimal.ZERO;
    private String transactionNum;// 单号
    /**
     * see {@link com.mz.trade.model.AccountRemarkEnum}
     */
    private Integer remarks;

    public Integer getRemarks() {
        return remarks;
    }

    public void setRemarks(Integer remarks) {
        this.remarks = remarks;
    }

    public Integer getAcccountType() {
        return acccountType;
    }

    public void setAcccountType(Integer acccountType) {
        this.acccountType = acccountType;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getMonteyType() {
        return monteyType;
    }

    public void setMonteyType(Integer monteyType) {
        this.monteyType = monteyType;
    }

    public String getTransactionNum() {
        return transactionNum;
    }

    public void setTransactionNum(String transactionNum) {
        this.transactionNum = transactionNum;
    }


}
