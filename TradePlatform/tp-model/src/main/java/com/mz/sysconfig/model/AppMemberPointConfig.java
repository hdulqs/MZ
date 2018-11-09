package com.mz.spotchange.model;


import com.mz.core.mvc.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "app_member_point_config")
public class AppMemberPointConfig extends BaseModel {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "isopen")
    private Integer isopen;

    @Column(name = "remark")
    private String remark;

    @Column(name = "recomm_point")
    private BigDecimal recomm_point;

    @Column(name = "recomm_isopen")
    private long recomm_isopen;

    @Column(name = "customerId")
    private long customerId;

    @Column(name = "regist_point")
    private BigDecimal regist_point;

    @Column(name = "regist_isopen")
    private Integer regist_isopen;

    @Column(name = "content")
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getIsopen() {
        return isopen;
    }

    public void setIsopen(Integer isopen) {
        this.isopen = isopen;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getRecomm_point() {
        return recomm_point;
    }

    public void setRecomm_point(BigDecimal recomm_point) {
        this.recomm_point = recomm_point;
    }

    public long getRecomm_isopen() {
        return recomm_isopen;
    }

    public void setRecomm_isopen(long recomm_isopen) {
        this.recomm_isopen = recomm_isopen;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getRegist_point() {
        return regist_point;
    }

    public void setRegist_point(BigDecimal regist_point) {
        this.regist_point = regist_point;
    }

    public Integer getRegist_isopen() {
        return regist_isopen;
    }

    public void setRegist_isopen(Integer regist_isopen) {
        this.regist_isopen = regist_isopen;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
