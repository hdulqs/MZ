/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年11月17日  16:57:27
 */
package  com.mz.finance.fund.model;


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
 * @Date :        2015年11月17日  16:57:27
 */

@Entity
@Table(name="finc_borrower_fund")
@DynamicInsert(true)
@DynamicUpdate(true)
public class FincBorrowerFund extends BaseModel{

    @Id
	@GeneratedValue(strategy = AUTO)
	@Column(name = "fundIntentId", unique = true, nullable = false)
    protected Long fundIntentId;    //
	protected String fundType;   //款项类型
	protected Integer payintentPeriod;   //款项期数
	protected java.util.Date intentDate;   //计划还款日
	protected java.util.Date interestStarDate;   //计息开始日
	protected java.util.Date interestEndDate;   //计息结束日
	protected java.math.BigDecimal payMoney;   //支出金额
	protected java.math.BigDecimal incomeMoney;   //收入金额
	protected java.util.Date factDate;   //实际到帐日/实际还款日
	protected java.math.BigDecimal afterMoney;   //已对帐金额
	protected java.math.BigDecimal notMoney;   //未对帐金额
	protected Short isCheck;   //0有效2刚生成但无效4被划掉已经无效
	protected String remark;   //备注
	protected Long projectId;   //项目id
	protected String businessType;   //业务类型
	protected String projectName;   //项目名称
	protected String projectNumber;   //项目编号
	protected Long preceptId;   //方案Id
	protected Long bidPlanId;   //标Id
	protected Short isFlat;   //是否平帐0为平账1已平账
	protected java.math.BigDecimal flatMoney;   //平帐金额
	protected java.math.BigDecimal punishRate;   //罚息利率
	protected java.math.BigDecimal punishMoney;   //罚息总额
	protected Integer punishDays;   //罚息天数
	protected Long fincSuperviseRecordId;   //
	protected Long fincEarlyRepaymentId;   //
	protected Long fincAlteraccrualRecordId;   //




    

	/**
	 * 	 * @return Long
       
     * @hibernate.id column="fundIntentId" type="java.lang.Long" generator-class="native"
	 */

	public Long getFundIntentId() {
		return this.fundIntentId;
	}
	
	/**
	 * Set the fundIntentId
	 */	
	public void setFundIntentId(Long aValue) {
		this.fundIntentId = aValue;
	}	

	/**
	 * 款项类型	 * @return String
	 * @hibernate.property column="fundType" type="java.lang.String" length="50" not-null="false" unique="false"
	 */

	public String getFundType() {
		return this.fundType;
	}
	
	/**
	 * Set the fundType
	 */	
	public void setFundType(String aValue) {
		this.fundType = aValue;
	}	

	/**
	 * 款项期数	 * @return Integer
	 * @hibernate.property column="payintentPeriod" type="java.lang.Integer" length="10" not-null="false" unique="false"
	 */

	public Integer getPayintentPeriod() {
		return this.payintentPeriod;
	}
	
	/**
	 * Set the payintentPeriod
	 */	
	public void setPayintentPeriod(Integer aValue) {
		this.payintentPeriod = aValue;
	}	

	/**
	 * 计划还款日	 * @return java.util.Date
	 * @hibernate.property column="intentDate" type="java.util.Date" length="19" not-null="false" unique="false"
	 */

	public java.util.Date getIntentDate() {
		return this.intentDate;
	}
	
	/**
	 * Set the intentDate
	 */	
	public void setIntentDate(java.util.Date aValue) {
		this.intentDate = aValue;
	}	

	
	/**
	 * <p> TODO</p>
	 * @return:     java.util.Date
	 */
	public java.util.Date getInterestStarDate() {
		return interestStarDate;
	}

	/** 
	 * <p> TODO</p>
	 * @return: java.util.Date
	 */
	public void setInterestStarDate(java.util.Date interestStarDate) {
		this.interestStarDate = interestStarDate;
	}

	/**
	 * <p> TODO</p>
	 * @return:     java.util.Date
	 */
	public java.util.Date getInterestEndDate() {
		return interestEndDate;
	}

	/** 
	 * <p> TODO</p>
	 * @return: java.util.Date
	 */
	public void setInterestEndDate(java.util.Date interestEndDate) {
		this.interestEndDate = interestEndDate;
	}

	/**
	 * 支出金额	 * @return java.math.BigDecimal
	 * @hibernate.property column="payMoney" type="java.math.BigDecimal" length="20" not-null="false" unique="false"
	 */

	public java.math.BigDecimal getPayMoney() {
		return this.payMoney;
	}
	
	/**
	 * Set the payMoney
	 */	
	public void setPayMoney(java.math.BigDecimal aValue) {
		this.payMoney = aValue;
	}	

	/**
	 * 收入金额	 * @return java.math.BigDecimal
	 * @hibernate.property column="incomeMoney" type="java.math.BigDecimal" length="20" not-null="false" unique="false"
	 */

	public java.math.BigDecimal getIncomeMoney() {
		return this.incomeMoney;
	}
	
	/**
	 * Set the incomeMoney
	 */	
	public void setIncomeMoney(java.math.BigDecimal aValue) {
		this.incomeMoney = aValue;
	}	

	/**
	 * 实际到帐日/实际还款日	 * @return java.util.Date
	 * @hibernate.property column="factDate" type="java.util.Date" length="19" not-null="false" unique="false"
	 */

	public java.util.Date getFactDate() {
		return this.factDate;
	}
	
	/**
	 * Set the factDate
	 */	
	public void setFactDate(java.util.Date aValue) {
		this.factDate = aValue;
	}	

	/**
	 * 已对帐金额	 * @return java.math.BigDecimal
	 * @hibernate.property column="afterMoney" type="java.math.BigDecimal" length="20" not-null="false" unique="false"
	 */

	public java.math.BigDecimal getAfterMoney() {
		return this.afterMoney;
	}
	
	/**
	 * Set the afterMoney
	 */	
	public void setAfterMoney(java.math.BigDecimal aValue) {
		this.afterMoney = aValue;
	}	

	/**
	 * 未对帐金额	 * @return java.math.BigDecimal
	 * @hibernate.property column="notMoney" type="java.math.BigDecimal" length="20" not-null="false" unique="false"
	 */

	public java.math.BigDecimal getNotMoney() {
		return this.notMoney;
	}
	
	/**
	 * Set the notMoney
	 */	
	public void setNotMoney(java.math.BigDecimal aValue) {
		this.notMoney = aValue;
	}	

	/**
	 * 0有效2刚生成但无效4被划掉已经无效	 * @return Short
	 * @hibernate.property column="isCheck" type="java.lang.Short" length="5" not-null="false" unique="false"
	 */

	public Short getIsCheck() {
		return this.isCheck;
	}
	
	/**
	 * Set the isCheck
	 */	
	public void setIsCheck(Short aValue) {
		this.isCheck = aValue;
	}	

	/**
	 * 备注	 * @return String
	 * @hibernate.property column="remark" type="java.lang.String" length="255" not-null="false" unique="false"
	 */

	public String getRemark() {
		return this.remark;
	}
	
	/**
	 * Set the remark
	 */	
	public void setRemark(String aValue) {
		this.remark = aValue;
	}	

	/**
	 * 项目id	 * @return Long
	 * @hibernate.property column="projectId" type="java.lang.Long" length="19" not-null="false" unique="false"
	 */

	public Long getProjectId() {
		return this.projectId;
	}
	
	/**
	 * Set the projectId
	 */	
	public void setProjectId(Long aValue) {
		this.projectId = aValue;
	}	

	/**
	 * 业务类型	 * @return String
	 * @hibernate.property column="businessType" type="java.lang.String" length="30" not-null="false" unique="false"
	 */

	public String getBusinessType() {
		return this.businessType;
	}
	
	/**
	 * Set the businessType
	 */	
	public void setBusinessType(String aValue) {
		this.businessType = aValue;
	}	

	/**
	 * 项目名称	 * @return String
	 * @hibernate.property column="projectName" type="java.lang.String" length="250" not-null="false" unique="false"
	 */

	public String getProjectName() {
		return this.projectName;
	}
	
	/**
	 * Set the projectName
	 */	
	public void setProjectName(String aValue) {
		this.projectName = aValue;
	}	

	/**
	 * 项目编号	 * @return String
	 * @hibernate.property column="projectNumber" type="java.lang.String" length="50" not-null="false" unique="false"
	 */

	public String getProjectNumber() {
		return this.projectNumber;
	}
	
	/**
	 * Set the projectNumber
	 */	
	public void setProjectNumber(String aValue) {
		this.projectNumber = aValue;
	}	

	/**
	 * 方案Id	 * @return Long
	 * @hibernate.property column="preceptId" type="java.lang.Long" length="19" not-null="false" unique="false"
	 */

	public Long getPreceptId() {
		return this.preceptId;
	}
	
	/**
	 * Set the preceptId
	 */	
	public void setPreceptId(Long aValue) {
		this.preceptId = aValue;
	}	

	/**
	 * 标Id	 * @return Long
	 * @hibernate.property column="bidPlanId" type="java.lang.Long" length="19" not-null="false" unique="false"
	 */

	public Long getBidPlanId() {
		return this.bidPlanId;
	}
	
	/**
	 * Set the bidPlanId
	 */	
	public void setBidPlanId(Long aValue) {
		this.bidPlanId = aValue;
	}	

	/**
	 * 是否平帐0为平账1已平账	 * @return Short
	 * @hibernate.property column="isFlat" type="java.lang.Short" length="5" not-null="false" unique="false"
	 */

	public Short getIsFlat() {
		return this.isFlat;
	}
	
	/**
	 * Set the isFlat
	 */	
	public void setIsFlat(Short aValue) {
		this.isFlat = aValue;
	}	

	/**
	 * 平帐金额	 * @return java.math.BigDecimal
	 * @hibernate.property column="flatMoney" type="java.math.BigDecimal" length="20" not-null="false" unique="false"
	 */

	public java.math.BigDecimal getFlatMoney() {
		return this.flatMoney;
	}
	
	/**
	 * Set the flatMoney
	 */	
	public void setFlatMoney(java.math.BigDecimal aValue) {
		this.flatMoney = aValue;
	}	

	/**
	 * 罚息利率	 * @return java.math.BigDecimal
	 * @hibernate.property column="punishRate" type="java.math.BigDecimal" length="20" not-null="false" unique="false"
	 */

	public java.math.BigDecimal getPunishRate() {
		return this.punishRate;
	}
	
	/**
	 * Set the punishRate
	 */	
	public void setPunishRate(java.math.BigDecimal aValue) {
		this.punishRate = aValue;
	}	

	/**
	 * 罚息总额	 * @return java.math.BigDecimal
	 * @hibernate.property column="punishMoney" type="java.math.BigDecimal" length="20" not-null="false" unique="false"
	 */

	public java.math.BigDecimal getPunishMoney() {
		return this.punishMoney;
	}
	
	/**
	 * Set the punishMoney
	 */	
	public void setPunishMoney(java.math.BigDecimal aValue) {
		this.punishMoney = aValue;
	}	

	/**
	 * 罚息天数	 * @return Integer
	 * @hibernate.property column="punishDays" type="java.lang.Integer" length="10" not-null="false" unique="false"
	 */

	public Integer getPunishDays() {
		return this.punishDays;
	}
	
	/**
	 * Set the punishDays
	 */	
	public void setPunishDays(Integer aValue) {
		this.punishDays = aValue;
	}	

	/**
	 * 	 * @return Long
	 * @hibernate.property column="fincSuperviseRecordId" type="java.lang.Long" length="19" not-null="false" unique="false"
	 */

	public Long getFincSuperviseRecordId() {
		return this.fincSuperviseRecordId;
	}
	
	/**
	 * Set the fincSuperviseRecordId
	 */	
	public void setFincSuperviseRecordId(Long aValue) {
		this.fincSuperviseRecordId = aValue;
	}	

	/**
	 * 	 * @return Long
	 * @hibernate.property column="fincEarlyRepaymentId" type="java.lang.Long" length="19" not-null="false" unique="false"
	 */

	public Long getFincEarlyRepaymentId() {
		return this.fincEarlyRepaymentId;
	}
	
	/**
	 * Set the fincEarlyRepaymentId
	 */	
	public void setFincEarlyRepaymentId(Long aValue) {
		this.fincEarlyRepaymentId = aValue;
	}	

	/**
	 * 	 * @return Long
	 * @hibernate.property column="fincAlteraccrualRecordId" type="java.lang.Long" length="19" not-null="false" unique="false"
	 */

	public Long getFincAlteraccrualRecordId() {
		return this.fincAlteraccrualRecordId;
	}
	
	/**
	 * Set the fincAlteraccrualRecordId
	 */	
	public void setFincAlteraccrualRecordId(Long aValue) {
		this.fincAlteraccrualRecordId = aValue;
	}	

	




}
