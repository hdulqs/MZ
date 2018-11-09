/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:     V1.0 
 * @Date:        2016-10-17 14:19:49 
 */
package com.mz.spotchange.product.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> SpProductParameter </p>
 * @author:         Wu Shuiming
 * @Date :          2016-10-17 14:19:49  
 */
@Table(name="sp_product_parameter")
public class SpProductParameter extends BaseModel {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name= "id")
	private Long id;  //主键id
	
	@Column(name= "productId")
	private Long productId;  //产品id
	
	@Column(name= "belongType")
	private Integer belongType;  //所属类（1表示pc交易   2表示微盘交易）
	
	@Column(name= "createFeeRate")
	private BigDecimal createFeeRate;  //建仓手续费
	
	@Column(name= "sellFeeRate")
	private BigDecimal sellFeeRate;  //卖方手续费
	
	@Column(name= "buyFeeRate")
	private BigDecimal buyFeeRate;  //买方手续费
	
	@Column(name= "marginThan")
	private BigDecimal marginThan;  //保证金比例
	
	@Column(name= "deleteFeeRate")
	private BigDecimal deleteFeeRate;  //平仓手续费
	
	@Column(name= "delayFeeRate")
	private BigDecimal delayFeeRate;  //延期收费 （手/天）
	
	@Column(name= "stopLoss")
	private BigDecimal stopLoss;  //止盈止损点
	
	@Column(name= "someBad")
	private Integer someBad;  //点差
	
	@Column(name= "smallSingleNum")
	private BigDecimal smallSingleNum;  //单笔最小成交量
	
	@Column(name= "maxSingleNum")
	private BigDecimal maxSingleNum;  //单笔最大的成交量
	
	@Column(name= "maxPositionNum")
	private BigDecimal maxPositionNum;  //最大持有多少手
	
	@Column(name= "productCode")
	private String productCode; // 产品代码
	
	@Column(name= "states")
	private Integer states; //状态  1表示可用    0表示不可用 
	
	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	/**
	 * <p>主键id</p>
	 * @author:  Wu Shuiming
	 * @return:  Long 
	 * @Date :   2016-10-17 14:19:49    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>主键id</p>
	 * @author:  Wu Shuiming
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2016-10-17 14:19:49   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>产品id</p>
	 * @author:  Wu Shuiming
	 * @return:  Long 
	 * @Date :   2016-10-17 14:19:49    
	 */
	public Long getProductId() {
		return productId;
	}
	
	/**
	 * <p>产品id</p>
	 * @author:  Wu Shuiming
	 * @param:   @param productId
	 * @return:  void 
	 * @Date :   2016-10-17 14:19:49   
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	
	/**
	 * <p>所属类（1表示pc交易   2表示微盘交易）</p>
	 * @author:  Wu Shuiming
	 * @return:  Integer 
	 * @Date :   2016-10-17 14:19:49    
	 */
	public Integer getBelongType() {
		return belongType;
	}
	
	/**
	 * <p>所属类（1表示pc交易   2表示微盘交易）</p>
	 * @author:  Wu Shuiming
	 * @param:   @param belongType
	 * @return:  void 
	 * @Date :   2016-10-17 14:19:49   
	 */
	public void setBelongType(Integer belongType) {
		this.belongType = belongType;
	}
	
	
	/**
	 * <p>建仓手续费</p>
	 * @author:  Wu Shuiming
	 * @return:  BigDecimal 
	 * @Date :   2016-10-17 14:19:49    
	 */
	public BigDecimal getCreateFeeRate() {
		return createFeeRate;
	}
	
	/**
	 * <p>建仓手续费</p>
	 * @author:  Wu Shuiming
	 * @param:   @param createFeeRate
	 * @return:  void 
	 * @Date :   2016-10-17 14:19:49   
	 */
	public void setCreateFeeRate(BigDecimal createFeeRate) {
		this.createFeeRate = createFeeRate;
	}
	
	
	/**
	 * <p>卖方手续费</p>
	 * @author:  Wu Shuiming
	 * @return:  BigDecimal 
	 * @Date :   2016-10-17 14:19:49    
	 */
	public BigDecimal getSellFeeRate() {
		return sellFeeRate;
	}
	
	/**
	 * <p>卖方手续费</p>
	 * @author:  Wu Shuiming
	 * @param:   @param sellFeeRate
	 * @return:  void 
	 * @Date :   2016-10-17 14:19:49   
	 */
	public void setSellFeeRate(BigDecimal sellFeeRate) {
		this.sellFeeRate = sellFeeRate;
	}
	
	
	/**
	 * <p>买方手续费</p>
	 * @author:  Wu Shuiming
	 * @return:  BigDecimal 
	 * @Date :   2016-10-17 14:19:49    
	 */
	public BigDecimal getBuyFeeRate() {
		return buyFeeRate;
	}
	
	/**
	 * <p>买方手续费</p>
	 * @author:  Wu Shuiming
	 * @param:   @param buyFeeRate
	 * @return:  void 
	 * @Date :   2016-10-17 14:19:49   
	 */
	public void setBuyFeeRate(BigDecimal buyFeeRate) {
		this.buyFeeRate = buyFeeRate;
	}
	
	
	/**
	 * <p>保证金比例</p>
	 * @author:  Wu Shuiming
	 * @return:  BigDecimal 
	 * @Date :   2016-10-17 14:19:49    
	 */
	public BigDecimal getMarginThan() {
		return marginThan;
	}
	
	/**
	 * <p>保证金比例</p>
	 * @author:  Wu Shuiming
	 * @param:   @param marginThan
	 * @return:  void 
	 * @Date :   2016-10-17 14:19:49   
	 */
	public void setMarginThan(BigDecimal marginThan) {
		this.marginThan = marginThan;
	}
	
	
	/**
	 * <p>平仓手续费</p>
	 * @author:  Wu Shuiming
	 * @return:  BigDecimal 
	 * @Date :   2016-10-17 14:19:49    
	 */
	public BigDecimal getDeleteFeeRate() {
		return deleteFeeRate;
	}
	
	/**
	 * <p>平仓手续费</p>
	 * @author:  Wu Shuiming
	 * @param:   @param deleteFeeRate
	 * @return:  void 
	 * @Date :   2016-10-17 14:19:49   
	 */
	public void setDeleteFeeRate(BigDecimal deleteFeeRate) {
		this.deleteFeeRate = deleteFeeRate;
	}
	
	
	/**
	 * <p>延期收费 （手/天）</p>
	 * @author:  Wu Shuiming
	 * @return:  BigDecimal 
	 * @Date :   2016-10-17 14:19:49    
	 */
	public BigDecimal getDelayFeeRate() {
		return delayFeeRate;
	}
	
	/**
	 * <p>延期收费 （手/天）</p>
	 * @author:  Wu Shuiming
	 * @param:   @param delayFeeRate
	 * @return:  void 
	 * @Date :   2016-10-17 14:19:49   
	 */
	public void setDelayFeeRate(BigDecimal delayFeeRate) {
		this.delayFeeRate = delayFeeRate;
	}
	
	
	/**
	 * <p>止盈止损点</p>
	 * @author:  Wu Shuiming
	 * @return:  BigDecimal 
	 * @Date :   2016-10-17 14:19:49    
	 */
	public BigDecimal getStopLoss() {
		return stopLoss;
	}
	
	/**
	 * <p>止盈止损点</p>
	 * @author:  Wu Shuiming
	 * @param:   @param stopLoss
	 * @return:  void 
	 * @Date :   2016-10-17 14:19:49   
	 */
	public void setStopLoss(BigDecimal stopLoss) {
		this.stopLoss = stopLoss;
	}
	
	
	/**
	 * <p>点差</p>
	 * @author:  Wu Shuiming
	 * @return:  Integer 
	 * @Date :   2016-10-17 14:19:49    
	 */
	public Integer getSomeBad() {
		return someBad;
	}
	
	/**
	 * <p>点差</p>
	 * @author:  Wu Shuiming
	 * @param:   @param someBad
	 * @return:  void 
	 * @Date :   2016-10-17 14:19:49   
	 */
	public void setSomeBad(Integer someBad) {
		this.someBad = someBad;
	}
	
	
	/**
	 * <p>单笔最小成交量</p>
	 * @author:  Wu Shuiming
	 * @return:  BigDecimal 
	 * @Date :   2016-10-17 14:19:49    
	 */
	public BigDecimal getSmallSingleNum() {
		return smallSingleNum;
	}
	
	/**
	 * <p>单笔最小成交量</p>
	 * @author:  Wu Shuiming
	 * @param:   @param smallSingleNum
	 * @return:  void 
	 * @Date :   2016-10-17 14:19:49   
	 */
	public void setSmallSingleNum(BigDecimal smallSingleNum) {
		this.smallSingleNum = smallSingleNum;
	}
	
	
	/**
	 * <p>单笔最大的成交量</p>
	 * @author:  Wu Shuiming
	 * @return:  BigDecimal 
	 * @Date :   2016-10-17 14:19:49    
	 */
	public BigDecimal getMaxSingleNum() {
		return maxSingleNum;
	}
	
	/**
	 * <p>单笔最大的成交量</p>
	 * @author:  Wu Shuiming
	 * @param:   @param maxSingleNum
	 * @return:  void 
	 * @Date :   2016-10-17 14:19:49   
	 */
	public void setMaxSingleNum(BigDecimal maxSingleNum) {
		this.maxSingleNum = maxSingleNum;
	}
	
	
	/**
	 * <p>最大持有多少手</p>
	 * @author:  Wu Shuiming
	 * @return:  BigDecimal 
	 * @Date :   2016-10-17 14:19:49    
	 */
	public BigDecimal getMaxPositionNum() {
		return maxPositionNum;
	}
	
	/**
	 * <p>最大持有多少手</p>
	 * @author:  Wu Shuiming
	 * @param:   @param maxPositionNum
	 * @return:  void 
	 * @Date :   2016-10-17 14:19:49   
	 */
	public void setMaxPositionNum(BigDecimal maxPositionNum) {
		this.maxPositionNum = maxPositionNum;
	}
	
	

}
