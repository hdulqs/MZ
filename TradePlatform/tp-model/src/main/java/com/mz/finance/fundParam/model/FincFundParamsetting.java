/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年11月25日  15:49:58
 */
package  com.mz.finance.fundParam.model;


import static javax.persistence.GenerationType.AUTO;
import com.mz.core.mvc.model.BaseModel;

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
 * @Date :        2015年11月25日  15:49:58
 */

@Entity
@Table(name="finc_fund_paramsetting")
@DynamicInsert(true)
@DynamicUpdate(true)
public class FincFundParamsetting extends BaseModel{

    @Id
	@GeneratedValue(strategy = AUTO)
	@Column(name = "paramsettingId", unique = true, nullable = false)
    protected Long paramsettingId;    //
    protected String paramsettingName;//方案的名称
    protected String paramsettingKey;//方案的名称
	protected String dateModel;   //third,notthird
	protected String repaymentMethod;   //还款方式singleInterest,sameprincipalsameInterest,sameprincipal,sameprincipalandInterest
	protected String repaymentCycle;   //还款周期monthPay,seasonPay,yearPay,dayPay,owerPay
	protected String repaymentDate;   //是固定日还款fixedDate,repaymentDateToDate
	protected String preposePayAccrual;   //是否前置付息
	protected String interestByOneTime;   //是否一次性
	protected String ccalculateFirstAndEnd;   //算头算尾
	protected String intentDateType;   //还款日是减一天还是不减
	
	protected String serviceMoneyWay;   //财务服务费【不随息，按本金计算，前置一次性付费】
	protected String consultationMoneyWay;   //咨询管理费【随息】
	protected String otherOneFundMoneyWay;   //预留给客户的收费1【不随息，按本金计算，后置一次性付费】
	protected String otherTwoFundMoneyWay;   //预留给客户的收费2【不随息，按本金计算，每期付费】
	protected String otherTreeFundMoneyWay;   //预留给客户的收费3



	protected Boolean isHiddenDateModel;   //third,notthird
	protected Boolean isHiddenRepaymentMethod;   //还款方式singleInterest,sameprincipalsameInterest,sameprincipal,sameprincipalandInterest
	protected Boolean isHiddenRepaymentCycle;   //还款周期monthPay,seasonPay,yearPay,dayPay,owerPay
	protected Boolean isHiddenRepaymentDate;   //是固定日还款fixedDate,repaymentDateToDate
	protected Boolean isHiddenPreposePayAccrual;   //是否前置付息
	protected Boolean isHiddenInterestByOneTime;   //是否一次性
	protected Boolean isHiddenCcalculateFirstAndEnd;   //算头算尾
	protected Boolean isHiddenIntentDateType;   //还款日是减一天还是不减
	
	protected Boolean isHiddenServiceMoneyWay;   //财务服务费【不随息，按本金计算，前置一次性付费】
	protected Boolean isHiddenConsultationMoneyWay;   //咨询管理费【随息】
	protected Boolean isHiddenOtherOneFundMoneyWay;   //预留给客户的收费1【不随息，按本金计算，后置一次性付费】
	protected Boolean isHiddenOtherTwoFundMoneyWay;   //预留给客户的收费2【不随息，按本金计算，每期付费】
	protected Boolean isHiddenOtherTreeFundMoneyWay;   //预留给客户的收费3
    
	protected Integer isDelete;//1已删除，0正常

	
	


	/**
	 * <p> TODO</p>
	 * @return:     Integer
	 */
	public Integer getIsDelete() {
		return isDelete;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Integer
	 */
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getParamsettingKey() {
		return paramsettingKey;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setParamsettingKey(String paramsettingKey) {
		this.paramsettingKey = paramsettingKey;
	}

	/**
	 * 	 * @return Long
       
     * @hibernate.id column="paramsettingId" type="java.lang.Long" generator-class="native"
	 */

	public Long getParamsettingId() {
		return this.paramsettingId;
	}
	
	/**
	 * Set the paramsettingId
	 */	
	public void setParamsettingId(Long aValue) {
		this.paramsettingId = aValue;
	}	

	/**
	 * <p> TODO</p>
	 * @return:     Boolean
	 */
	public Boolean getIsHiddenDateModel() {
		return isHiddenDateModel;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Boolean
	 */
	public void setIsHiddenDateModel(Boolean isHiddenDateModel) {
		this.isHiddenDateModel = isHiddenDateModel;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Boolean
	 */
	public Boolean getIsHiddenRepaymentMethod() {
		return isHiddenRepaymentMethod;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Boolean
	 */
	public void setIsHiddenRepaymentMethod(Boolean isHiddenRepaymentMethod) {
		this.isHiddenRepaymentMethod = isHiddenRepaymentMethod;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Boolean
	 */
	public Boolean getIsHiddenRepaymentCycle() {
		return isHiddenRepaymentCycle;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Boolean
	 */
	public void setIsHiddenRepaymentCycle(Boolean isHiddenRepaymentCycle) {
		this.isHiddenRepaymentCycle = isHiddenRepaymentCycle;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Boolean
	 */
	public Boolean getIsHiddenRepaymentDate() {
		return isHiddenRepaymentDate;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Boolean
	 */
	public void setIsHiddenRepaymentDate(Boolean isHiddenRepaymentDate) {
		this.isHiddenRepaymentDate = isHiddenRepaymentDate;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Boolean
	 */
	public Boolean getIsHiddenPreposePayAccrual() {
		return isHiddenPreposePayAccrual;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Boolean
	 */
	public void setIsHiddenPreposePayAccrual(Boolean isHiddenPreposePayAccrual) {
		this.isHiddenPreposePayAccrual = isHiddenPreposePayAccrual;
	}


	/**
	 * <p> TODO</p>
	 * @return:     Boolean
	 */
	public Boolean getIsHiddenInterestByOneTime() {
		return isHiddenInterestByOneTime;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Boolean
	 */
	public void setIsHiddenInterestByOneTime(Boolean isHiddenInterestByOneTime) {
		this.isHiddenInterestByOneTime = isHiddenInterestByOneTime;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Boolean
	 */
	public Boolean getIsHiddenCcalculateFirstAndEnd() {
		return isHiddenCcalculateFirstAndEnd;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Boolean
	 */
	public void setIsHiddenCcalculateFirstAndEnd(
			Boolean isHiddenCcalculateFirstAndEnd) {
		this.isHiddenCcalculateFirstAndEnd = isHiddenCcalculateFirstAndEnd;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Boolean
	 */
	public Boolean getIsHiddenIntentDateType() {
		return isHiddenIntentDateType;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Boolean
	 */
	public void setIsHiddenIntentDateType(Boolean isHiddenIntentDateType) {
		this.isHiddenIntentDateType = isHiddenIntentDateType;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Boolean
	 */
	public Boolean getIsHiddenServiceMoneyWay() {
		return isHiddenServiceMoneyWay;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Boolean
	 */
	public void setIsHiddenServiceMoneyWay(Boolean isHiddenServiceMoneyWay) {
		this.isHiddenServiceMoneyWay = isHiddenServiceMoneyWay;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Boolean
	 */
	public Boolean getIsHiddenConsultationMoneyWay() {
		return isHiddenConsultationMoneyWay;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Boolean
	 */
	public void setIsHiddenConsultationMoneyWay(Boolean isHiddenConsultationMoneyWay) {
		this.isHiddenConsultationMoneyWay = isHiddenConsultationMoneyWay;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Boolean
	 */
	public Boolean getIsHiddenOtherOneFundMoneyWay() {
		return isHiddenOtherOneFundMoneyWay;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Boolean
	 */
	public void setIsHiddenOtherOneFundMoneyWay(Boolean isHiddenOtherOneFundMoneyWay) {
		this.isHiddenOtherOneFundMoneyWay = isHiddenOtherOneFundMoneyWay;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Boolean
	 */
	public Boolean getIsHiddenOtherTwoFundMoneyWay() {
		return isHiddenOtherTwoFundMoneyWay;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Boolean
	 */
	public void setIsHiddenOtherTwoFundMoneyWay(Boolean isHiddenOtherTwoFundMoneyWay) {
		this.isHiddenOtherTwoFundMoneyWay = isHiddenOtherTwoFundMoneyWay;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Boolean
	 */
	public Boolean getIsHiddenOtherTreeFundMoneyWay() {
		return isHiddenOtherTreeFundMoneyWay;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Boolean
	 */
	public void setIsHiddenOtherTreeFundMoneyWay(
			Boolean isHiddenOtherTreeFundMoneyWay) {
		this.isHiddenOtherTreeFundMoneyWay = isHiddenOtherTreeFundMoneyWay;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getParamsettingName() {
		return paramsettingName;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setParamsettingName(String paramsettingName) {
		this.paramsettingName = paramsettingName;
	}

	/**
	 * third,notthird	 * @return String
	 * @hibernate.property column="dateModel" type="java.lang.String" length="100" not-null="false" unique="false"
	 */

	public String getDateModel() {
		return this.dateModel;
	}
	
	/**
	 * Set the dateModel
	 */	
	public void setDateModel(String aValue) {
		this.dateModel = aValue;
	}	

	/**
	 * 还款方式singleInterest,sameprincipalsameInterest,sameprincipal,sameprincipalandInterest	 * @return String
	 * @hibernate.property column="repaymentMethod" type="java.lang.String" length="100" not-null="false" unique="false"
	 */

	public String getRepaymentMethod() {
		return this.repaymentMethod;
	}
	
	/**
	 * Set the repaymentMethod
	 */	
	public void setRepaymentMethod(String aValue) {
		this.repaymentMethod = aValue;
	}	

	/**
	 * 还款周期monthPay,seasonPay,yearPay,dayPay,owerPay	 * @return String
	 * @hibernate.property column="repaymentCycle" type="java.lang.String" length="100" not-null="false" unique="false"
	 */

	public String getRepaymentCycle() {
		return this.repaymentCycle;
	}
	
	/**
	 * Set the repaymentCycle
	 */	
	public void setRepaymentCycle(String aValue) {
		this.repaymentCycle = aValue;
	}	

	/**
	 * 是固定日还款fixedDate,repaymentDateToDate	 * @return String
	 * @hibernate.property column="repaymentDate" type="java.lang.String" length="10" not-null="false" unique="false"
	 */

	public String getRepaymentDate() {
		return this.repaymentDate;
	}
	
	/**
	 * Set the repaymentDate
	 */	
	public void setRepaymentDate(String aValue) {
		this.repaymentDate = aValue;
	}	

	/**
	 * 是否前置付息	 * @return String
	 * @hibernate.property column="preposePayAccrual" type="java.lang.String" length="10" not-null="false" unique="false"
	 */

	public String getPreposePayAccrual() {
		return this.preposePayAccrual;
	}
	
	/**
	 * Set the preposePayAccrual
	 */	
	public void setPreposePayAccrual(String aValue) {
		this.preposePayAccrual = aValue;
	}	

	/**
	 * 是否一次性	 * @return String
	 * @hibernate.property column="interestByOneTime" type="java.lang.String" length="10" not-null="false" unique="false"
	 */

	public String getInterestByOneTime() {
		return this.interestByOneTime;
	}
	
	/**
	 * Set the interestByOneTime
	 */	
	public void setInterestByOneTime(String aValue) {
		this.interestByOneTime = aValue;
	}	

	/**
	 * 算头算尾	 * @return String
	 * @hibernate.property column="ccalculateFirstAndEnd" type="java.lang.String" length="10" not-null="false" unique="false"
	 */

	public String getCcalculateFirstAndEnd() {
		return this.ccalculateFirstAndEnd;
	}
	
	/**
	 * Set the ccalculateFirstAndEnd
	 */	
	public void setCcalculateFirstAndEnd(String aValue) {
		this.ccalculateFirstAndEnd = aValue;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getIntentDateType() {
		return intentDateType;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setIntentDateType(String intentDateType) {
		this.intentDateType = intentDateType;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getServiceMoneyWay() {
		return serviceMoneyWay;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setServiceMoneyWay(String serviceMoneyWay) {
		this.serviceMoneyWay = serviceMoneyWay;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getConsultationMoneyWay() {
		return consultationMoneyWay;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setConsultationMoneyWay(String consultationMoneyWay) {
		this.consultationMoneyWay = consultationMoneyWay;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOtherOneFundMoneyWay() {
		return otherOneFundMoneyWay;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOtherOneFundMoneyWay(String otherOneFundMoneyWay) {
		this.otherOneFundMoneyWay = otherOneFundMoneyWay;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOtherTwoFundMoneyWay() {
		return otherTwoFundMoneyWay;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOtherTwoFundMoneyWay(String otherTwoFundMoneyWay) {
		this.otherTwoFundMoneyWay = otherTwoFundMoneyWay;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOtherTreeFundMoneyWay() {
		return otherTreeFundMoneyWay;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOtherTreeFundMoneyWay(String otherTreeFundMoneyWay) {
		this.otherTreeFundMoneyWay = otherTreeFundMoneyWay;
	}	

	



}
