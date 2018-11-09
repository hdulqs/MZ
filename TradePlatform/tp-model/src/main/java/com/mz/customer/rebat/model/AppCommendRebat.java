package com.mz.customer.rebat.model;

import com.mz.core.mvc.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author <a href="mailto:17610171876@163.com">Mr_He</a>
 * @Copyright (c)</ b> 何川<br/>
 * @createTime 2018/2/8 10:56
 * @Description:
 */
@Table(name="app_commend_rebat")
public class AppCommendRebat extends BaseModel{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name= "customerId")
    private Integer customerId;

    @Column(name= "trueName")
    private String trueName;

    @Column(name= "rebatMoney")
    private BigDecimal rebatMoney;

    @Column(name= "coinCode")
    private String coinCode;

    @Column(name = "status")
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public BigDecimal getRebatMoney() {
        return rebatMoney;
    }

    public void setRebatMoney(BigDecimal rebatMoney) {
        this.rebatMoney = rebatMoney;
    }

    public String getCoinCode() {
        return coinCode;
    }

    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }
}
