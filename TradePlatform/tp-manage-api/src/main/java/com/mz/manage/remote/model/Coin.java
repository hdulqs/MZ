package com.mz.manage.remote.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class Coin implements Serializable {
	/**
	 * @Fields : TODO
	 */
	
	private static final long serialVersionUID = 352272460201788717L;

	private Long id;
	@ApiModelProperty(value = "币种名称", required = false)
	private String name;// 币种名称
	
	@ApiModelProperty(value = "币代码", required = false)
	private String coinCode;// 币代码

	@ApiModelProperty(value = "交易币种", required = false)
	private String fixPriceCoinCode;// 交易币种

	@ApiModelProperty(value = "发布总金额", required = false)
	private BigDecimal issueTotalMoney;

	@ApiModelProperty(value = "发布单价", required = false)
	private BigDecimal issuePrice;

	@ApiModelProperty(value = "时间", required = false)
	private Date issueTime;

	// 产品的状态 0 ： 准备状态
	// 1 ： 正在发行
	// 2 ： 停牌
	// 3 ： 退市'z
	@ApiModelProperty(value = "产品的状态 0 ： 准备状态 1 ： 正在发行 2 ： 停牌 3 ： 退市'z", required = false)
	private int issueState;

	// 发行方id
	@ApiModelProperty(value = "发行方id", required = false)
	private Long issueId;

	// 发行方名称
	@ApiModelProperty(value = "发行方名称", required = false)
	private String issueName;
	// 买方手续费 率
	
	@ApiModelProperty(value = "买方手续费 率", required = false)
	private BigDecimal buyFeeRate;

	// 卖方手续费 率
	@ApiModelProperty(value = "卖方手续费 率", required = false)
	private BigDecimal sellFeeRate;

	// 买方 的成交 金额
	@ApiModelProperty(value = "买方 的成交 金额", required = false)
	private BigDecimal buyMinMoney;

	// 卖方 最小的成交 数量
	@ApiModelProperty(value = "卖方 最小的成交 数量", required = false)
	private BigDecimal sellMinCoin;

	// 最小拆分的数量
	@ApiModelProperty(value = "最小拆分的数量", required = false)
	private BigDecimal splitMinCoin;

	// 产品的存量
	@ApiModelProperty(value = "产品的存量", required = false)
	private Long stock;

	// 产品的排序字段
	@ApiModelProperty(value = "产品的排序字段", required = false)
	private Integer sort;

	// 价格限制
	@ApiModelProperty(value = "价格限制", required = false)
	private BigDecimal priceLimits;

	// 每次下单的最小数据
	@ApiModelProperty(value = "每次下单的最小数据", required = false)
	private BigDecimal oneTimeOrderNumMin;
	//每次下单的最大数量
	@ApiModelProperty(value = "每次下单的最小数据", required = false)
	private BigDecimal oneTimeOrderNum;
	
	//涨幅 针对开盘价的涨幅
	@ApiModelProperty(value = "涨幅 针对开盘价的涨幅", required = false)
	private BigDecimal rose;
	
	//跌幅
	@ApiModelProperty(value = "跌幅", required = false)
	private BigDecimal decline;
	
	//均价（收盘价）
	@ApiModelProperty(value = "均价（收盘价）", required = false)
	private BigDecimal averagePrice;
			

	// 产品参数状态
	@ApiModelProperty(value = "产品参数状态", required = false)
	private Integer pamState;

	// 融资手续费率
	@ApiModelProperty(value = "融资手续费率", required = false)
	private BigDecimal circulation;

	// '1表示开市 2表示闭市'
	@ApiModelProperty(value = "1表示开市 2表示闭市", required = false)
	private Integer openBell;
	
	@ApiModelProperty(value = "0真实货币1虚拟币", required = false)
	private Integer fixPriceType;  //0真实货币1虚拟币
	
	@ApiModelProperty(value = "交易币的保留几位小数", required = false)
	private Integer keepDecimalForCoin; //交易币的保留几位小数
	
	@ApiModelProperty(value = "定价币的保留几位小数", required = false)
	private Integer keepDecimalForCurrency; //定价币的保留几位小数
	
	// 产品的状态 0 ： 准备状态
		// 1 ： 可以交易
	@ApiModelProperty(value = "产品的状态 0 ： 准备状态 1 ： 可以交易", required = false)
	private Integer state;
	
	
	private String picturePath;
	@ApiModelProperty(value = "提现手续费率", required = false)
	private BigDecimal paceFeeRate;//提现手续费率
	
	@ApiModelProperty(value = "注册送币数量", required = false)
	private BigDecimal giveCoin;//注册送币数量
	
	
	
	// 是否置顶显示
	@ApiModelProperty(value = "是否置顶显示", required = false)
	private Integer isRecommend;
	
	private String yesterdayPrice; 
	
	
	
	public BigDecimal getPaceFeeRate() {
		return paceFeeRate;
	}

	public void setPaceFeeRate(BigDecimal paceFeeRate) {
		this.paceFeeRate = paceFeeRate;
	}

	public Integer getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(Integer isRecommend) {
		this.isRecommend = isRecommend;
	}

	public BigDecimal getOneTimeOrderNumMin() {
		return oneTimeOrderNumMin;
	}

	public void setOneTimeOrderNumMin(BigDecimal oneTimeOrderNumMin) {
		this.oneTimeOrderNumMin = oneTimeOrderNumMin;
	}

	public BigDecimal getOneTimeOrderNum() {
		return oneTimeOrderNum;
	}

	public void setOneTimeOrderNum(BigDecimal oneTimeOrderNum) {
		this.oneTimeOrderNum = oneTimeOrderNum;
	}

	public BigDecimal getRose() {
		return rose;
	}

	public void setRose(BigDecimal rose) {
		this.rose = rose;
	}

	public BigDecimal getDecline() {
		return decline;
	}

	public void setDecline(BigDecimal decline) {
		this.decline = decline;
	}

	public BigDecimal getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(BigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public Integer getFixPriceType() {
		return fixPriceType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFixPriceType(Integer fixPriceType) {
		this.fixPriceType = fixPriceType;
	}


	public Integer getKeepDecimalForCoin() {
		return keepDecimalForCoin;
	}

	public void setKeepDecimalForCoin(Integer keepDecimalForCoin) {
		this.keepDecimalForCoin = keepDecimalForCoin;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public BigDecimal getIssueTotalMoney() {
		return issueTotalMoney;
	}

	public void setIssueTotalMoney(BigDecimal issueTotalMoney) {
		this.issueTotalMoney = issueTotalMoney;
	}

	public BigDecimal getIssuePrice() {
		return issuePrice;
	}

	public void setIssuePrice(BigDecimal issuePrice) {
		this.issuePrice = issuePrice;
	}

	public Date getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}

	public int getIssueState() {
		return issueState;
	}

	public void setIssueState(int issueState) {
		this.issueState = issueState;
	}

	public Long getIssueId() {
		return issueId;
	}

	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}

	public String getIssueName() {
		return issueName;
	}

	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}

	public BigDecimal getBuyFeeRate() {
		return buyFeeRate;
	}

	public void setBuyFeeRate(BigDecimal buyFeeRate) {
		this.buyFeeRate = buyFeeRate;
	}

	public BigDecimal getSellFeeRate() {
		return sellFeeRate;
	}

	public void setSellFeeRate(BigDecimal sellFeeRate) {
		this.sellFeeRate = sellFeeRate;
	}

	public BigDecimal getBuyMinMoney() {
		return buyMinMoney;
	}

	public void setBuyMinMoney(BigDecimal buyMinMoney) {
		this.buyMinMoney = buyMinMoney;
	}

	public BigDecimal getSellMinCoin() {
		return sellMinCoin;
	}

	public void setSellMinCoin(BigDecimal sellMinCoin) {
		this.sellMinCoin = sellMinCoin;
	}

	public BigDecimal getSplitMinCoin() {
		return splitMinCoin;
	}

	public void setSplitMinCoin(BigDecimal splitMinCoin) {
		this.splitMinCoin = splitMinCoin;
	}

	public Long getStock() {
		return stock;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public BigDecimal getPriceLimits() {
		return priceLimits;
	}

	public void setPriceLimits(BigDecimal priceLimits) {
		this.priceLimits = priceLimits;
	}

	public Integer getPamState() {
		return pamState;
	}

	public void setPamState(Integer pamState) {
		this.pamState = pamState;
	}

	public BigDecimal getCirculation() {
		return circulation;
	}

	public void setCirculation(BigDecimal circulation) {
		this.circulation = circulation;
	}

	public Integer getOpenBell() {
		return openBell;
	}

	public void setOpenBell(Integer openBell) {
		this.openBell = openBell;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}

	public String getFixPriceCoinCode() {
		return fixPriceCoinCode;
	}

	public void setFixPriceCoinCode(String fixPriceCoinCode) {
		this.fixPriceCoinCode = fixPriceCoinCode;
	}

	public Integer getKeepDecimalForCurrency() {
		return keepDecimalForCurrency;
	}

	public void setKeepDecimalForCurrency(Integer keepDecimalForCurrency) {
		this.keepDecimalForCurrency = keepDecimalForCurrency;
	}

	public BigDecimal getGiveCoin() {
		return giveCoin;
	}

	public void setGiveCoin(BigDecimal giveCoin) {
		this.giveCoin = giveCoin;
	}

	public String getYesterdayPrice() {
		return yesterdayPrice;
	}

	public void setYesterdayPrice(String yesterdayPrice) {
		this.yesterdayPrice = yesterdayPrice;
	}
	
	
	
	
	

}
