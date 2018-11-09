package com.mz.util;

import com.mz.core.mvc.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 唯一校验表
 */
@SuppressWarnings("serial")
@Table(name = "unique_record")
public class UniqueRecord extends BaseModel{
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "remark")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}