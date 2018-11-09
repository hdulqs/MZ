/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2015年11月20日 下午2:38:50
 */
package com.mz.factoring.project.model;

import static javax.persistence.GenerationType.AUTO;
import com.mz.core.mvc.model.ProjectBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2015年11月20日 下午2:38:50 
 */
@Entity
@Table(name="factoring_project")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TestFactoringProject extends ProjectBase{

    @Id
	@GeneratedValue(strategy = AUTO)
	@Column(name = "factoringProjectId", unique = true, nullable = false)
     public Long factoringProjectId;

	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getFactoringProjectId() {
		return factoringProjectId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setFactoringProjectId(Long factoringProjectId) {
		this.factoringProjectId = factoringProjectId;
	}
     
}
