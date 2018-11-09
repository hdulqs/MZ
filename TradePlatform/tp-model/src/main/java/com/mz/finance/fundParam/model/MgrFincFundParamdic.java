/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年11月25日  16:34:41
 */
package  com.mz.finance.fundParam.model;


import static javax.persistence.GenerationType.AUTO;
import com.mz.core.mvc.model.MgrBaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * <p> TODO</p>
 * @author:       Gao Mimi    
 * @Date :        2015年11月25日  16:34:41
 */

@Entity
@Table(name="mgr_finc_fund_paramdic")
@DynamicInsert(true)
@DynamicUpdate(true)
public class MgrFincFundParamdic extends MgrBaseModel{

    @Id
	@GeneratedValue(strategy = AUTO)
	@Column(name = "paramDicId", unique = true, nullable = false)
    protected Long paramDicId;    //
	protected Long pid;   //父节点ID
	protected String pparamDicKey;   //父节点key
	protected String paramDicKey;   //节点key
	protected String text;   //标题
	protected Integer orderNo;   //排序
	protected Integer isOld;   //是否删除0正常，1过期
	protected String paramDicDescrption;   //描述




    

	/**
	 * 	 * @return Long
       
     * @hibernate.id column="paramDicId" type="java.lang.Long" generator-class="native"
	 */

	public Long getParamDicId() {
		return this.paramDicId;
	}
	
	/**
	 * Set the paramDicId
	 */	
	public void setParamDicId(Long aValue) {
		this.paramDicId = aValue;
	}	

	/**
	 * 父节点ID	 * @return Long
	 * @hibernate.property column="pid" type="java.lang.Long" length="19" not-null="false" unique="false"
	 */

	public Long getPid() {
		return this.pid;
	}
	
	/**
	 * Set the pid
	 */	
	public void setPid(Long aValue) {
		this.pid = aValue;
	}	

	/**
	 * 父节点key	 * @return String
	 * @hibernate.property column="pparamDicKey" type="java.lang.String" length="50" not-null="false" unique="false"
	 */

	public String getPparamDicKey() {
		return this.pparamDicKey;
	}
	
	/**
	 * Set the pparamDicKey
	 */	
	public void setPparamDicKey(String aValue) {
		this.pparamDicKey = aValue;
	}	

	/**
	 * 节点key	 * @return String
	 * @hibernate.property column="paramDicKey" type="java.lang.String" length="50" not-null="false" unique="false"
	 */

	public String getParamDicKey() {
		return this.paramDicKey;
	}
	
	/**
	 * Set the paramDicKey
	 */	
	public void setParamDicKey(String aValue) {
		this.paramDicKey = aValue;
	}	

	/**
	 * 标题	 * @return String
	 * @hibernate.property column="text" type="java.lang.String" length="50" not-null="false" unique="false"
	 */

	public String getText() {
		return this.text;
	}
	
	/**
	 * Set the text
	 */	
	public void setText(String aValue) {
		this.text = aValue;
	}	

	/**
	 * 排序	 * @return Integer
	 * @hibernate.property column="orderNo" type="java.lang.Integer" length="10" not-null="false" unique="false"
	 */

	public Integer getOrderNo() {
		return this.orderNo;
	}
	
	/**
	 * Set the orderNo
	 */	
	public void setOrderNo(Integer aValue) {
		this.orderNo = aValue;
	}	

	/**
	 * 是否删除0正常，1过期	 * @return Integer
	 * @hibernate.property column="isOld" type="java.lang.Integer" length="10" not-null="false" unique="false"
	 */

	public Integer getIsOld() {
		return this.isOld;
	}
	
	/**
	 * Set the isOld
	 */	
	public void setIsOld(Integer aValue) {
		this.isOld = aValue;
	}	

	/**
	 * 描述	 * @return String
	 * @hibernate.property column="paramDicDescrption" type="java.lang.String" length="255" not-null="false" unique="false"
	 */

	public String getParamDicDescrption() {
		return this.paramDicDescrption;
	}
	
	/**
	 * Set the paramDicDescrption
	 */	
	public void setParamDicDescrption(String aValue) {
		this.paramDicDescrption = aValue;
	}	

	




}
