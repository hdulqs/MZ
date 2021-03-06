/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年11月17日  16:57:27
 */
package  com.mz.finance.fund.model.mergePeriod;
import java.math.BigDecimal;
import java.util.Date;
public abstract class FundIntentPeriod {
//	public final static Comparator comparator= new FundIntentPeriodComparator();//用来给期数排序
	
	public FundIntentPeriod(){
		
	}
	protected BigDecimal periodTotal = new BigDecimal(0);//一期各种费用的综合//totalIncomeMoney
	protected Integer payintentPeriod;//期数
	protected Date intentDate;
	protected Date factDate;////最新还款日   此日期为最新还款日，各个款项对账有优先级，取各自最新的 factDate，因为 每次对账时，最优先对罚息对账，那么
	public Long projectId;
	public String businessType;
	public String projectNumber;//项目编号
	public String oppositeName;//对方主体的名称
	public Long oppositeID;//对方主体的名称
	protected String oppositeType;
	protected Integer punishDays; //罚息天数
	public BigDecimal getPeriodTotal() {
		return periodTotal;
	}
	public void setPeriodTotal(BigDecimal periodTotal) {
		this.periodTotal = periodTotal;
	}
	public Integer getPayintentPeriod() {
		return payintentPeriod;
	}
	public void setPayintentPeriod(Integer payintentPeriod) {
		this.payintentPeriod = payintentPeriod;
	}
	public Date getIntentDate() {
		return intentDate;
	}
	public void setIntentDate(Date intentDate) {
		this.intentDate = intentDate;
	}
	public Date getFactDate() {
		return factDate;
	}
	public void setFactDate(Date factDate) {
		this.factDate = factDate;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String getOppositeName() {
		return oppositeName;
	}
	public void setOppositeName(String oppositeName) {
		this.oppositeName = oppositeName;
	}
	public Long getOppositeID() {
		return oppositeID;
	}
	public void setOppositeID(Long oppositeID) {
		this.oppositeID = oppositeID;
	}
	public Integer getPunishDays() {
		return punishDays;
	}
	public void setPunishDays(Integer punishDays) {
		this.punishDays = punishDays;
	}
	public String getOppositeType() {
		return oppositeType;
	}
	public void setOppositeType(String oppositeType) {
		this.oppositeType = oppositeType;
	}

	

}
