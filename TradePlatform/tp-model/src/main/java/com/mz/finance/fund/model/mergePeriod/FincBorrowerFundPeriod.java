package com.mz.finance.fund.model.mergePeriod;
import com.mz.finance.fund.model.FincBorrowerFund;

import java.math.BigDecimal;
import java.util.List;



public  class FincBorrowerFundPeriod extends FundIntentPeriod {
	
	public FincBorrowerFundPeriod(){
		
	}
	
	protected String ids;
	
	
	protected Long fundProjectId;
	protected Long planId;
	protected String bidPlanName;
	protected String bidPlanProjectNum;
	protected Long borrowId;//借款人Id
	protected String borrowName;//借款人姓名
	protected Long ptpborrowId;//招标项目借款人P2P账号表主键Id
	protected String ptpborrowName;//招标项目借款人p2p账号表登陆名

	//设置初始值的原因，如果principalRepayment为null那么gson中 principalRepayment的属性不会显示，所以出示一个都为null的飞空对象，通过isNull属性来判断是否为新加对象
	//也可以通过principalRepayment==SlFundIntent.slFundIntentNull；来判断
	@FundType(name="principalRepayment",prior=FundType.priorLevel.One)
	public FincBorrowerFund principalRepaymentMoney = new FincBorrowerFund();//本金偿还
	
	@FundType(name="loanInterestMoney",prior=FundType.priorLevel.Two)
	public FincBorrowerFund loanInterestMoney = new FincBorrowerFund();//利息
	
	@FundType(name="consultationMoney",prior=FundType.priorLevel.Three)
	public FincBorrowerFund consultationMoney = new FincBorrowerFund();//咨询服务费

	@FundType(name="serviceMoney",prior=FundType.priorLevel.Four)
	public FincBorrowerFund serviceMoney = new FincBorrowerFund();
	
	@FundType(name="otherOneFundMoney",prior=FundType.priorLevel.Five)
	public FincBorrowerFund otherOneFundMoney = new FincBorrowerFund();//利息
	
	@FundType(name="otherTwoFundMoney",prior=FundType.priorLevel.Six)
	public FincBorrowerFund otherTwoFundMoney = new FincBorrowerFund();//咨询服务费

	@FundType(name="otherTreeFundMoney",prior=FundType.priorLevel.Seven)
	public FincBorrowerFund otherTreeFundMoney = new FincBorrowerFund();
	
	
	@FundType(name="principalLendingMoney",prior=FundType.priorLevel.None)
	public FincBorrowerFund principalLendingMoney = new FincBorrowerFund();//本金放款 1条记录

	public void initFincBorrowerFund(List<FincBorrowerFund> bpfundlist){
		this.ids="";
		principalRepaymentMoney.setIncomeMoney(new BigDecimal("0"));
		principalRepaymentMoney.setAfterMoney(new BigDecimal("0"));
		principalRepaymentMoney.setPunishMoney(new BigDecimal("0"));
		
		loanInterestMoney.setIncomeMoney(new BigDecimal("0"));
		loanInterestMoney.setAfterMoney(new BigDecimal("0"));
		loanInterestMoney.setPunishMoney(new BigDecimal("0"));
		
		consultationMoney.setIncomeMoney(new BigDecimal("0"));
		consultationMoney.setAfterMoney(new BigDecimal("0"));
		consultationMoney.setPunishMoney(new BigDecimal("0"));
		
		serviceMoney.setIncomeMoney(new BigDecimal("0"));
		serviceMoney.setAfterMoney(new BigDecimal("0"));
		serviceMoney.setPunishMoney(new BigDecimal("0"));
		
		otherOneFundMoney.setIncomeMoney(new BigDecimal("0"));
		otherOneFundMoney.setAfterMoney(new BigDecimal("0"));
		otherOneFundMoney.setPunishMoney(new BigDecimal("0"));
		
		otherTwoFundMoney.setIncomeMoney(new BigDecimal("0"));
		otherTwoFundMoney.setAfterMoney(new BigDecimal("0"));
		otherTwoFundMoney.setPunishMoney(new BigDecimal("0"));
		
		otherTreeFundMoney.setIncomeMoney(new BigDecimal("0"));
		otherTreeFundMoney.setAfterMoney(new BigDecimal("0"));
		otherTreeFundMoney.setPunishMoney(new BigDecimal("0"));
		
		principalLendingMoney.setPayMoney(new BigDecimal("0"));

		int i=0;
		while(i<bpfundlist.size()){
			FincBorrowerFund fp=bpfundlist.get(i);
			if(i==0){
				this.intentDate=fp.getIntentDate();
			}
			
			if(fp.getFundType().equals("principalLendingMoney")){
				principalLendingMoney.setPayMoney(principalLendingMoney.getPayMoney().add(fp.getPayMoney()));
				
			}else if(fp.getFundType().equals("principalRepaymentMoney")){
				principalRepaymentMoney.setIncomeMoney(principalRepaymentMoney.getIncomeMoney().add(fp.getIncomeMoney()));
				principalRepaymentMoney.setAfterMoney(principalRepaymentMoney.getAfterMoney().add(fp.getAfterMoney()));
				principalRepaymentMoney.setPunishMoney(principalRepaymentMoney.getPunishMoney().add(fp.getPunishMoney()));
			}else if(fp.getFundType().equals("loanInterestMoney")){
				loanInterestMoney.setIncomeMoney(loanInterestMoney.getIncomeMoney().add(fp.getIncomeMoney()));
				loanInterestMoney.setAfterMoney(loanInterestMoney.getAfterMoney().add(fp.getAfterMoney()));
				loanInterestMoney.setPunishMoney(loanInterestMoney.getPunishMoney().add(fp.getPunishMoney()));
			}else if(fp.getFundType().equals("consultationMoney")){
				consultationMoney.setIncomeMoney(consultationMoney.getIncomeMoney().add(fp.getIncomeMoney()));
				consultationMoney.setAfterMoney(consultationMoney.getAfterMoney().add(fp.getAfterMoney()));
				consultationMoney.setPunishMoney(consultationMoney.getPunishMoney().add(fp.getPunishMoney()));
			}else if(fp.getFundType().equals("serviceMoney")){
				serviceMoney.setIncomeMoney(serviceMoney.getIncomeMoney().add(fp.getIncomeMoney()));
				serviceMoney.setAfterMoney(serviceMoney.getAfterMoney().add(fp.getAfterMoney()));
				serviceMoney.setPunishMoney(serviceMoney.getPunishMoney().add(fp.getPunishMoney()));
			}else if(fp.getFundType().equals("otherOneFundMoney")){
				otherOneFundMoney.setIncomeMoney(otherOneFundMoney.getIncomeMoney().add(fp.getIncomeMoney()));
				otherOneFundMoney.setAfterMoney(otherOneFundMoney.getAfterMoney().add(fp.getAfterMoney()));
				otherOneFundMoney.setPunishMoney(otherOneFundMoney.getPunishMoney().add(fp.getPunishMoney()));
			}else if(fp.getFundType().equals("otherTwoFundMoney")){
				otherTwoFundMoney.setIncomeMoney(otherTwoFundMoney.getIncomeMoney().add(fp.getIncomeMoney()));
				otherTwoFundMoney.setAfterMoney(otherTwoFundMoney.getAfterMoney().add(fp.getAfterMoney()));
				otherTwoFundMoney.setPunishMoney(otherTwoFundMoney.getPunishMoney().add(fp.getPunishMoney()));
			}else if(fp.getFundType().equals("otherTreeFundMoney")){
				otherTreeFundMoney.setIncomeMoney(otherTreeFundMoney.getIncomeMoney().add(fp.getIncomeMoney()));
				otherTreeFundMoney.setAfterMoney(otherTreeFundMoney.getAfterMoney().add(fp.getAfterMoney()));
				otherTreeFundMoney.setPunishMoney(otherTreeFundMoney.getPunishMoney().add(fp.getPunishMoney()));
			}
			i++;
		
		}
		
		
		
	}
	
	public BigDecimal allIncomeMoney;
	public BigDecimal allPunishMoney;
	public void initMoney(){
		
       this.allIncomeMoney=principalRepaymentMoney.getIncomeMoney().
    		   add(loanInterestMoney.getIncomeMoney()).
    		   add(consultationMoney.getIncomeMoney()).
    		   add(serviceMoney.getIncomeMoney()).
    		   add(otherOneFundMoney.getIncomeMoney()).
    		   add(otherTwoFundMoney.getIncomeMoney()).
    		   add(otherTreeFundMoney.getIncomeMoney());
       this.allPunishMoney=principalRepaymentMoney.getIncomeMoney().
    		   add(loanInterestMoney.getPunishMoney()).
    		   add(consultationMoney.getPunishMoney()).
    		   add(serviceMoney.getPunishMoney()).
    		   add(otherOneFundMoney.getPunishMoney()).
    		   add(otherTwoFundMoney.getPunishMoney()).
    		   add(otherTreeFundMoney.getPunishMoney());
		
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getIds() {
		return ids;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}
	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getFundProjectId() {
		return fundProjectId;
	}
	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setFundProjectId(Long fundProjectId) {
		this.fundProjectId = fundProjectId;
	}
	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getPlanId() {
		return planId;
	}
	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getBidPlanName() {
		return bidPlanName;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setBidPlanName(String bidPlanName) {
		this.bidPlanName = bidPlanName;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getBidPlanProjectNum() {
		return bidPlanProjectNum;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setBidPlanProjectNum(String bidPlanProjectNum) {
		this.bidPlanProjectNum = bidPlanProjectNum;
	}
	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getBorrowId() {
		return borrowId;
	}
	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setBorrowId(Long borrowId) {
		this.borrowId = borrowId;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getBorrowName() {
		return borrowName;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}
	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getPtpborrowId() {
		return ptpborrowId;
	}
	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setPtpborrowId(Long ptpborrowId) {
		this.ptpborrowId = ptpborrowId;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getPtpborrowName() {
		return ptpborrowName;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPtpborrowName(String ptpborrowName) {
		this.ptpborrowName = ptpborrowName;
	}
	/**
	 * <p> TODO</p>
	 * @return:     FincBorrowerFund
	 */
	public FincBorrowerFund getPrincipalRepaymentMoney() {
		return principalRepaymentMoney;
	}
	/** 
	 * <p> TODO</p>
	 * @return: FincBorrowerFund
	 */
	public void setPrincipalRepaymentMoney(FincBorrowerFund principalRepaymentMoney) {
		this.principalRepaymentMoney = principalRepaymentMoney;
	}
	/**
	 * <p> TODO</p>
	 * @return:     FincBorrowerFund
	 */
	public FincBorrowerFund getLoanInterestMoney() {
		return loanInterestMoney;
	}
	/** 
	 * <p> TODO</p>
	 * @return: FincBorrowerFund
	 */
	public void setLoanInterestMoney(FincBorrowerFund loanInterestMoney) {
		this.loanInterestMoney = loanInterestMoney;
	}
	/**
	 * <p> TODO</p>
	 * @return:     FincBorrowerFund
	 */
	public FincBorrowerFund getConsultationMoney() {
		return consultationMoney;
	}
	/** 
	 * <p> TODO</p>
	 * @return: FincBorrowerFund
	 */
	public void setConsultationMoney(FincBorrowerFund consultationMoney) {
		this.consultationMoney = consultationMoney;
	}
	/**
	 * <p> TODO</p>
	 * @return:     FincBorrowerFund
	 */
	public FincBorrowerFund getServiceMoney() {
		return serviceMoney;
	}
	/** 
	 * <p> TODO</p>
	 * @return: FincBorrowerFund
	 */
	public void setServiceMoney(FincBorrowerFund serviceMoney) {
		this.serviceMoney = serviceMoney;
	}
	/**
	 * <p> TODO</p>
	 * @return:     FincBorrowerFund
	 */
	public FincBorrowerFund getOtherOneFundMoney() {
		return otherOneFundMoney;
	}
	/** 
	 * <p> TODO</p>
	 * @return: FincBorrowerFund
	 */
	public void setOtherOneFundMoney(FincBorrowerFund otherOneFundMoney) {
		this.otherOneFundMoney = otherOneFundMoney;
	}
	/**
	 * <p> TODO</p>
	 * @return:     FincBorrowerFund
	 */
	public FincBorrowerFund getOtherTwoFundMoney() {
		return otherTwoFundMoney;
	}
	/** 
	 * <p> TODO</p>
	 * @return: FincBorrowerFund
	 */
	public void setOtherTwoFundMoney(FincBorrowerFund otherTwoFundMoney) {
		this.otherTwoFundMoney = otherTwoFundMoney;
	}
	/**
	 * <p> TODO</p>
	 * @return:     FincBorrowerFund
	 */
	public FincBorrowerFund getOtherTreeFundMoney() {
		return otherTreeFundMoney;
	}
	/** 
	 * <p> TODO</p>
	 * @return: FincBorrowerFund
	 */
	public void setOtherTreeFundMoney(FincBorrowerFund otherTreeFundMoney) {
		this.otherTreeFundMoney = otherTreeFundMoney;
	}
	/**
	 * <p> TODO</p>
	 * @return:     FincBorrowerFund
	 */
	public FincBorrowerFund getPrincipalLendingMoney() {
		return principalLendingMoney;
	}
	/** 
	 * <p> TODO</p>
	 * @return: FincBorrowerFund
	 */
	public void setPrincipalLendingMoney(FincBorrowerFund principalLendingMoney) {
		this.principalLendingMoney = principalLendingMoney;
	}
	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getAllIncomeMoney() {
		return allIncomeMoney;
	}
	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setAllIncomeMoney(BigDecimal allIncomeMoney) {
		this.allIncomeMoney = allIncomeMoney;
	}
	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getAllPunishMoney() {
		return allPunishMoney;
	}
	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setAllPunishMoney(BigDecimal allPunishMoney) {
		this.allPunishMoney = allPunishMoney;
	}



	
	
	
	
}
