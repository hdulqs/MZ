/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年10月27日  17:57:57
 */
package com.mz.web.dictionary.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Gao Mimi
 * @Date : 2015年10月27日 17:57:57
 */

@SuppressWarnings("serial")
@Table(name = "app_dic_onelevel")
public class AppDicOnelevel extends AppDicBase {
	 @Column(name="itemValue")
	 protected String itemValue;

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getItemValue() {
		return itemValue;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	
	
}
