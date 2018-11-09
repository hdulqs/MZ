package com.mz.customer.user.model;


import com.mz.core.mvc.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "app_member_point")
public class AppMemberPoint extends BaseModel {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;
    @Column(name = "isDelete")
    private String isDelete;
    @Column(name = "remark")
    private String remark;
    @Column(name = "recomm_customerId")
    private Long recomm_customerId;
    @Column(name = "customerId")
    private Long customerId;
    @Column(name = "member_point")
    private BigDecimal member_Point;

    @Column(name = "recomm_point")
    private BigDecimal recomm_Point;

    @Column(name = "content")
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getRecomm_customerId() {
        return recomm_customerId;
    }

    public void setRecomm_customerId(Long recomm_customerId) {
        this.recomm_customerId = recomm_customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getMember_Point() {
        return member_Point;
    }

    public void setMember_Point(BigDecimal member_Point) {
        this.member_Point = member_Point;
    }

    public BigDecimal getRecomm_Point() {
        return recomm_Point;
    }

    public void setRecomm_Point(BigDecimal recomm_Point) {
        this.recomm_Point = recomm_Point;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
