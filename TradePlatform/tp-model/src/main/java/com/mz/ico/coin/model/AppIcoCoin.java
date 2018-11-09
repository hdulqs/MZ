/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-08-17 18:22:20 
 */
package com.mz.ico.coin.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> AppIcoCoin </p>
 * @author:         shangxl
 * @Date :          2017-08-17 18:22:20  
 */
@Table(name="app_ico_coin")
public class AppIcoCoin extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "name")
	private String name;  //产品名称
	
	@Column(name= "issueTotalMoney")
	private BigDecimal issueTotalMoney;  //发行总额（人民币）
	
	@Column(name= "totalNum")
	private Long totalNum;  //发行总量
            
	
	@Column(name= "issuePrice")
	private BigDecimal issuePrice;  //发行价格（单价）
	
	@Column(name= "issueTime")
	private Date issueTime;  //发行时间
	
	@Column(name= "issueState")
	private Integer issueState;  //0 ：  准备状态 1 ：  正在发行 2 ：  停牌 3 ：  退市
	
	@Column(name= "coinCode")
	private String coinCode;  //币的代码 (股票代码)
	
	@Column(name= "splitMinCoin")
	private BigDecimal splitMinCoin;  //拆分最小份数
	
	@Column(name= "issueId")
	private Long issueId;  //发行方id(未来参考发行方表)
	
	@Column(name= "issueName")
	private String issueName;  //发行方名称
	
	@Column(name= "stock")
	private Long stock;  //产品存量
	
	@Column(name= "sort")
	private Integer sort;  //排序字段
	
	@Column(name= "arithmetic")
	private String arithmetic;  //算法说明
	
	@Column(name= "proveMode")
	private String proveMode;  //证明方式
	
	@Column(name= "productReferral")
	private String productReferral;  //产品介绍
	
	@Column(name= "pamState")
	private Integer pamState;  //产品的状态（0 表示没有  1 表示有用）
	
	@Column(name= "blockspeed")
	private String blockspeed;  //多少秒产生一个区块
	
	@Column(name= "buyFeeRate")
	private BigDecimal buyFeeRate;  //买方费率
	
	@Column(name= "sellFeeRate")
	private BigDecimal sellFeeRate;  //买方费率
	
	@Column(name= "buyMinMoney")
	private BigDecimal buyMinMoney;  //最小成交金额
	
	@Column(name= "sellMinCoin")
	private BigDecimal sellMinCoin;  //最小成交数量
	
	@Column(name= "minBlockSize")
	private String minBlockSize;  //区块大小最小值
	
	@Column(name= "maxBlockSize")
	private String maxBlockSize;  //区块大小最大值
	
	@Column(name= "walletDownload")
	private String walletDownload;  //钱包下载地址
	
	@Column(name= "soundDownload")
	private String soundDownload;  //源码下载地址
	
	@Column(name= "blockBrowser")
	private String blockBrowser;  //区块浏览器
	
	@Column(name= "officialWebsite")
	private String officialWebsite;  //官方网站
	
	@Column(name= "officialForum")
	private String officialForum;  //官方论坛
	
	@Column(name= "miningAddress")
	private String miningAddress;  //挖矿地址
	
	@Column(name= "isRecommend")
	private Integer isRecommend;  //是否置顶（ 0 表示不置顶 ， 1 表示置顶）
	
	@Column(name= "openingTime")
	private String openingTime;  //开市时间
	
	@Column(name= "closeTime")
	private String closeTime;  //闭市时间
	
	@Column(name= "picturePath")
	private String picturePath;  //产品图片路径
	
	@Column(name= "closePlateTime")
	private String closePlateTime;  //closePlateTime
	
	@Column(name= "openAndclosePlateTime")
	private String openAndclosePlateTime;  //openAndclosePlateTime
	
	@Column(name= "transactionType")
	private Integer transactionType;  //1竞价 --也就是正常的  2坐市
	
	@Column(name= "prepaidFeeRate")
	private BigDecimal prepaidFeeRate;  //充值手续费率
	
	@Column(name= "paceFeeRate")
	private BigDecimal paceFeeRate;  //提现手续费率
	
	@Column(name= "oneTimePaceNum")
	private BigDecimal oneTimePaceNum;  //每次提现的最大数量
	
	@Column(name= "oneDayPaceNum")
	private BigDecimal oneDayPaceNum;  //每天提现的最大数量
	
	@Column(name= "leastPaceNum")
	private BigDecimal leastPaceNum;  //最小提现 数量
	
	@Column(name= "circulation")
	private BigDecimal circulation;  //融币的手续费率
	
	@Column(name= "priceLimits")
	private BigDecimal priceLimits;  //文章正文
	
	@Column(name= "keepDecimalForCoin")
	private Integer keepDecimalForCoin;  //币的保留几位小数
	
	@Column(name= "keepDecimalForCurrency")
	private Integer keepDecimalForCurrency;  //钱的保留几位小数
	
	@Column(name= "openBell")
	private Integer openBell;  //1表示开市   2表示闭市
	
	@Column(name= "decline")
	private BigDecimal decline;  //跌幅
	
	@Column(name= "rose")
	private BigDecimal rose;  //涨幅
	
	@Column(name= "averagePrice")
	private BigDecimal averagePrice;  //均价(收盘价)
	
	@Column(name= "oneTimeOrderNum")
	private BigDecimal oneTimeOrderNum;  //每次下单的最大数量（个）
	
	
	
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>产品名称</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * <p>产品名称</p>
	 * @author:  shangxl
	 * @param:   @param name
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * <p>发行总额（人民币）</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getIssueTotalMoney() {
		return issueTotalMoney;
	}
	
	/**
	 * <p>发行总额（人民币）</p>
	 * @author:  shangxl
	 * @param:   @param issueTotalMoney
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setIssueTotalMoney(BigDecimal issueTotalMoney) {
		this.issueTotalMoney = issueTotalMoney;
	}
	
	
	/**
	 * <p>发行总量
            </p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public Long getTotalNum() {
		return totalNum;
	}
	
	/**
	 * <p>发行总量
            </p>
	 * @author:  shangxl
	 * @param:   @param totalNum
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}
	
	
	/**
	 * <p>发行价格（单价）</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getIssuePrice() {
		return issuePrice;
	}
	
	/**
	 * <p>发行价格（单价）</p>
	 * @author:  shangxl
	 * @param:   @param issuePrice
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setIssuePrice(BigDecimal issuePrice) {
		this.issuePrice = issuePrice;
	}
	
	
	/**
	 * <p>发行时间</p>
	 * @author:  shangxl
	 * @return:  Date 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public Date getIssueTime() {
		return issueTime;
	}
	
	/**
	 * <p>发行时间</p>
	 * @author:  shangxl
	 * @param:   @param issueTime
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}
	
	
	/**
	 * <p>0 ：  准备状态
            1 ：  正在发行
            2 ：  停牌
            3 ：  退市</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public Integer getIssueState() {
		return issueState;
	}
	
	/**
	 * <p>0 ：  准备状态
            1 ：  正在发行
            2 ：  停牌
            3 ：  退市</p>
	 * @author:  shangxl
	 * @param:   @param issueState
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setIssueState(Integer issueState) {
		this.issueState = issueState;
	}
	
	
	/**
	 * <p>币的代码 (股票代码)</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getCoinCode() {
		return coinCode;
	}
	
	/**
	 * <p>币的代码 (股票代码)</p>
	 * @author:  shangxl
	 * @param:   @param coinCode
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}
	
	
	/**
	 * <p>拆分最小份数</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getSplitMinCoin() {
		return splitMinCoin;
	}
	
	/**
	 * <p>拆分最小份数</p>
	 * @author:  shangxl
	 * @param:   @param splitMinCoin
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setSplitMinCoin(BigDecimal splitMinCoin) {
		this.splitMinCoin = splitMinCoin;
	}
	
	
	/**
	 * <p>发行方id(未来参考发行方表)</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public Long getIssueId() {
		return issueId;
	}
	
	/**
	 * <p>发行方id(未来参考发行方表)</p>
	 * @author:  shangxl
	 * @param:   @param issueId
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}
	
	
	/**
	 * <p>发行方名称</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getIssueName() {
		return issueName;
	}
	
	/**
	 * <p>发行方名称</p>
	 * @author:  shangxl
	 * @param:   @param issueName
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}
	
	
	/**
	 * <p>产品存量</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public Long getStock() {
		return stock;
	}
	
	/**
	 * <p>产品存量</p>
	 * @author:  shangxl
	 * @param:   @param stock
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setStock(Long stock) {
		this.stock = stock;
	}
	
	
	/**
	 * <p>排序字段</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public Integer getSort() {
		return sort;
	}
	
	/**
	 * <p>排序字段</p>
	 * @author:  shangxl
	 * @param:   @param sort
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	
	/**
	 * <p>算法说明</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getArithmetic() {
		return arithmetic;
	}
	
	/**
	 * <p>算法说明</p>
	 * @author:  shangxl
	 * @param:   @param arithmetic
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setArithmetic(String arithmetic) {
		this.arithmetic = arithmetic;
	}
	
	
	/**
	 * <p>证明方式</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getProveMode() {
		return proveMode;
	}
	
	/**
	 * <p>证明方式</p>
	 * @author:  shangxl
	 * @param:   @param proveMode
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setProveMode(String proveMode) {
		this.proveMode = proveMode;
	}
	
	
	/**
	 * <p>产品介绍</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getProductReferral() {
		return productReferral;
	}
	
	/**
	 * <p>产品介绍</p>
	 * @author:  shangxl
	 * @param:   @param productReferral
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setProductReferral(String productReferral) {
		this.productReferral = productReferral;
	}
	
	
	/**
	 * <p>产品的状态（0 表示没有  1 表示有用）</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public Integer getPamState() {
		return pamState;
	}
	
	/**
	 * <p>产品的状态（0 表示没有  1 表示有用）</p>
	 * @author:  shangxl
	 * @param:   @param pamState
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setPamState(Integer pamState) {
		this.pamState = pamState;
	}
	
	
	/**
	 * <p>多少秒产生一个区块</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getBlockspeed() {
		return blockspeed;
	}
	
	/**
	 * <p>多少秒产生一个区块</p>
	 * @author:  shangxl
	 * @param:   @param blockspeed
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setBlockspeed(String blockspeed) {
		this.blockspeed = blockspeed;
	}
	
	
	/**
	 * <p>买方费率</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getBuyFeeRate() {
		return buyFeeRate;
	}
	
	/**
	 * <p>买方费率</p>
	 * @author:  shangxl
	 * @param:   @param buyFeeRate
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setBuyFeeRate(BigDecimal buyFeeRate) {
		this.buyFeeRate = buyFeeRate;
	}
	
	
	/**
	 * <p>买方费率</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getSellFeeRate() {
		return sellFeeRate;
	}
	
	/**
	 * <p>买方费率</p>
	 * @author:  shangxl
	 * @param:   @param sellFeeRate
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setSellFeeRate(BigDecimal sellFeeRate) {
		this.sellFeeRate = sellFeeRate;
	}
	
	
	/**
	 * <p>最小成交金额</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getBuyMinMoney() {
		return buyMinMoney;
	}
	
	/**
	 * <p>最小成交金额</p>
	 * @author:  shangxl
	 * @param:   @param buyMinMoney
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setBuyMinMoney(BigDecimal buyMinMoney) {
		this.buyMinMoney = buyMinMoney;
	}
	
	
	/**
	 * <p>最小成交数量</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getSellMinCoin() {
		return sellMinCoin;
	}
	
	/**
	 * <p>最小成交数量</p>
	 * @author:  shangxl
	 * @param:   @param sellMinCoin
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setSellMinCoin(BigDecimal sellMinCoin) {
		this.sellMinCoin = sellMinCoin;
	}
	
	
	/**
	 * <p>区块大小最小值</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getMinBlockSize() {
		return minBlockSize;
	}
	
	/**
	 * <p>区块大小最小值</p>
	 * @author:  shangxl
	 * @param:   @param minBlockSize
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setMinBlockSize(String minBlockSize) {
		this.minBlockSize = minBlockSize;
	}
	
	
	/**
	 * <p>区块大小最大值</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getMaxBlockSize() {
		return maxBlockSize;
	}
	
	/**
	 * <p>区块大小最大值</p>
	 * @author:  shangxl
	 * @param:   @param maxBlockSize
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setMaxBlockSize(String maxBlockSize) {
		this.maxBlockSize = maxBlockSize;
	}
	
	
	/**
	 * <p>钱包下载地址</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getWalletDownload() {
		return walletDownload;
	}
	
	/**
	 * <p>钱包下载地址</p>
	 * @author:  shangxl
	 * @param:   @param walletDownload
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setWalletDownload(String walletDownload) {
		this.walletDownload = walletDownload;
	}
	
	
	/**
	 * <p>源码下载地址</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getSoundDownload() {
		return soundDownload;
	}
	
	/**
	 * <p>源码下载地址</p>
	 * @author:  shangxl
	 * @param:   @param soundDownload
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setSoundDownload(String soundDownload) {
		this.soundDownload = soundDownload;
	}
	
	
	/**
	 * <p>区块浏览器</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getBlockBrowser() {
		return blockBrowser;
	}
	
	/**
	 * <p>区块浏览器</p>
	 * @author:  shangxl
	 * @param:   @param blockBrowser
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setBlockBrowser(String blockBrowser) {
		this.blockBrowser = blockBrowser;
	}
	
	
	/**
	 * <p>官方网站</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getOfficialWebsite() {
		return officialWebsite;
	}
	
	/**
	 * <p>官方网站</p>
	 * @author:  shangxl
	 * @param:   @param officialWebsite
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setOfficialWebsite(String officialWebsite) {
		this.officialWebsite = officialWebsite;
	}
	
	
	/**
	 * <p>官方论坛</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getOfficialForum() {
		return officialForum;
	}
	
	/**
	 * <p>官方论坛</p>
	 * @author:  shangxl
	 * @param:   @param officialForum
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setOfficialForum(String officialForum) {
		this.officialForum = officialForum;
	}
	
	
	/**
	 * <p>挖矿地址</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getMiningAddress() {
		return miningAddress;
	}
	
	/**
	 * <p>挖矿地址</p>
	 * @author:  shangxl
	 * @param:   @param miningAddress
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setMiningAddress(String miningAddress) {
		this.miningAddress = miningAddress;
	}
	
	
	/**
	 * <p>是否置顶（ 0 表示不置顶 ， 1 表示置顶）</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public Integer getIsRecommend() {
		return isRecommend;
	}
	
	/**
	 * <p>是否置顶（ 0 表示不置顶 ， 1 表示置顶）</p>
	 * @author:  shangxl
	 * @param:   @param isRecommend
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setIsRecommend(Integer isRecommend) {
		this.isRecommend = isRecommend;
	}
	
	
	/**
	 * <p>开市时间</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getOpeningTime() {
		return openingTime;
	}
	
	/**
	 * <p>开市时间</p>
	 * @author:  shangxl
	 * @param:   @param openingTime
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setOpeningTime(String openingTime) {
		this.openingTime = openingTime;
	}
	
	
	/**
	 * <p>闭市时间</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getCloseTime() {
		return closeTime;
	}
	
	/**
	 * <p>闭市时间</p>
	 * @author:  shangxl
	 * @param:   @param closeTime
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}
	
	
	/**
	 * <p>产品图片路径</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getPicturePath() {
		return picturePath;
	}
	
	/**
	 * <p>产品图片路径</p>
	 * @author:  shangxl
	 * @param:   @param picturePath
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}
	
	
	/**
	 * <p>closePlateTime</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getClosePlateTime() {
		return closePlateTime;
	}
	
	/**
	 * <p>closePlateTime</p>
	 * @author:  shangxl
	 * @param:   @param closePlateTime
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setClosePlateTime(String closePlateTime) {
		this.closePlateTime = closePlateTime;
	}
	
	
	/**
	 * <p>openAndclosePlateTime</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public String getOpenAndclosePlateTime() {
		return openAndclosePlateTime;
	}
	
	/**
	 * <p>openAndclosePlateTime</p>
	 * @author:  shangxl
	 * @param:   @param openAndclosePlateTime
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setOpenAndclosePlateTime(String openAndclosePlateTime) {
		this.openAndclosePlateTime = openAndclosePlateTime;
	}
	
	
	/**
	 * <p>1竞价 --也就是正常的  2坐市</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public Integer getTransactionType() {
		return transactionType;
	}
	
	/**
	 * <p>1竞价 --也就是正常的  2坐市</p>
	 * @author:  shangxl
	 * @param:   @param transactionType
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}
	
	
	/**
	 * <p>充值手续费率</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getPrepaidFeeRate() {
		return prepaidFeeRate;
	}
	
	/**
	 * <p>充值手续费率</p>
	 * @author:  shangxl
	 * @param:   @param prepaidFeeRate
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setPrepaidFeeRate(BigDecimal prepaidFeeRate) {
		this.prepaidFeeRate = prepaidFeeRate;
	}
	
	
	/**
	 * <p>提现手续费率</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getPaceFeeRate() {
		return paceFeeRate;
	}
	
	/**
	 * <p>提现手续费率</p>
	 * @author:  shangxl
	 * @param:   @param paceFeeRate
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setPaceFeeRate(BigDecimal paceFeeRate) {
		this.paceFeeRate = paceFeeRate;
	}
	
	
	/**
	 * <p>每次提现的最大数量</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getOneTimePaceNum() {
		return oneTimePaceNum;
	}
	
	/**
	 * <p>每次提现的最大数量</p>
	 * @author:  shangxl
	 * @param:   @param oneTimePaceNum
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setOneTimePaceNum(BigDecimal oneTimePaceNum) {
		this.oneTimePaceNum = oneTimePaceNum;
	}
	
	
	/**
	 * <p>每天提现的最大数量</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getOneDayPaceNum() {
		return oneDayPaceNum;
	}
	
	/**
	 * <p>每天提现的最大数量</p>
	 * @author:  shangxl
	 * @param:   @param oneDayPaceNum
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setOneDayPaceNum(BigDecimal oneDayPaceNum) {
		this.oneDayPaceNum = oneDayPaceNum;
	}
	
	
	/**
	 * <p>最小提现 数量</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getLeastPaceNum() {
		return leastPaceNum;
	}
	
	/**
	 * <p>最小提现 数量</p>
	 * @author:  shangxl
	 * @param:   @param leastPaceNum
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setLeastPaceNum(BigDecimal leastPaceNum) {
		this.leastPaceNum = leastPaceNum;
	}
	
	
	/**
	 * <p>融币的手续费率</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getCirculation() {
		return circulation;
	}
	
	/**
	 * <p>融币的手续费率</p>
	 * @author:  shangxl
	 * @param:   @param circulation
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setCirculation(BigDecimal circulation) {
		this.circulation = circulation;
	}
	
	
	/**
	 * <p>文章正文</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getPriceLimits() {
		return priceLimits;
	}
	
	/**
	 * <p>文章正文</p>
	 * @author:  shangxl
	 * @param:   @param priceLimits
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setPriceLimits(BigDecimal priceLimits) {
		this.priceLimits = priceLimits;
	}
	
	
	/**
	 * <p>币的保留几位小数</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public Integer getKeepDecimalForCoin() {
		return keepDecimalForCoin;
	}
	
	/**
	 * <p>币的保留几位小数</p>
	 * @author:  shangxl
	 * @param:   @param keepDecimalForCoin
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setKeepDecimalForCoin(Integer keepDecimalForCoin) {
		this.keepDecimalForCoin = keepDecimalForCoin;
	}
	
	
	/**
	 * <p>钱的保留几位小数</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public Integer getKeepDecimalForCurrency() {
		return keepDecimalForCurrency;
	}
	
	/**
	 * <p>钱的保留几位小数</p>
	 * @author:  shangxl
	 * @param:   @param keepDecimalForCurrency
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setKeepDecimalForCurrency(Integer keepDecimalForCurrency) {
		this.keepDecimalForCurrency = keepDecimalForCurrency;
	}
	
	
	/**
	 * <p>1表示开市   2表示闭市</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public Integer getOpenBell() {
		return openBell;
	}
	
	/**
	 * <p>1表示开市   2表示闭市</p>
	 * @author:  shangxl
	 * @param:   @param openBell
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setOpenBell(Integer openBell) {
		this.openBell = openBell;
	}
	
	
	/**
	 * <p>跌幅</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getDecline() {
		return decline;
	}
	
	/**
	 * <p>跌幅</p>
	 * @author:  shangxl
	 * @param:   @param decline
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setDecline(BigDecimal decline) {
		this.decline = decline;
	}
	
	
	/**
	 * <p>涨幅</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getRose() {
		return rose;
	}
	
	/**
	 * <p>涨幅</p>
	 * @author:  shangxl
	 * @param:   @param rose
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setRose(BigDecimal rose) {
		this.rose = rose;
	}
	
	
	/**
	 * <p>均价(收盘价)</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getAveragePrice() {
		return averagePrice;
	}
	
	/**
	 * <p>均价(收盘价)</p>
	 * @author:  shangxl
	 * @param:   @param averagePrice
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setAveragePrice(BigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}
	
	
	/**
	 * <p>每次下单的最大数量（个）</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:22:20    
	 */
	public BigDecimal getOneTimeOrderNum() {
		return oneTimeOrderNum;
	}
	
	/**
	 * <p>每次下单的最大数量（个）</p>
	 * @author:  shangxl
	 * @param:   @param oneTimeOrderNum
	 * @return:  void 
	 * @Date :   2017-08-17 18:22:20   
	 */
	public void setOneTimeOrderNum(BigDecimal oneTimeOrderNum) {
		this.oneTimeOrderNum = oneTimeOrderNum;
	}
	
	

}
