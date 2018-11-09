package com.mz.customer.businessman.model;

import com.mz.core.mvc.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

@Table(name="otc_account_record")
public class OtcAccountRecord extends BaseModel {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;  //id

    @Column(name= "customId")
    private Long customId;  //客户id

    @Column(name= "accountId")
    private Long accountId;

    @Column(name= "money")
    private BigDecimal money = new BigDecimal(0);

    @Column(name= "monteyType")
    private  Integer monteyType;//1热账户，2，冷账号

    @Column(name= "acccountType")
    private  Integer acccountType ;//0资金账号，1币账户

    @Column(name= "transactionNum")
    private String transactionNum;// 单号

    @Column(name= "remarks")
    private String remarks;//备注


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomId() {
        return customId;
    }

    public void setCustomId(Long customId) {
        this.customId = customId;
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

    public Integer getAcccountType() {
        return acccountType;
    }

    public void setAcccountType(Integer acccountType) {
        this.acccountType = acccountType;
    }

    public String getTransactionNum() {
        return transactionNum;
    }

    public void setTransactionNum(String transactionNum) {
        this.transactionNum = transactionNum;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
