/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: liushilei
 * @version: V1.0
 * @Date: 2016-10-19 17:40:12
 */
package com.mz.spotchange.user.model;

import static javax.persistence.GenerationType.IDENTITY;

import com.mz.core.mvc.model.BaseModel;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> SpVipUser </p>
 * @author: liushilei
 * @Date :          2016-10-19 17:40:12  
 */
@Table(name = "sp_vip_user")
public class SpVipUser extends BaseModel {


  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;  //id

  @Column(name = "userName")
  private String userName;  //登录用户名

  @Column(name = "passWord")
  private String passWord;  //密码

  @Column(name = "appUserId")
  private Long appUserId;  //账号ID


  @Column(name = "vipType")
  private String vipType;  //会员:vip,代理商:agent

  @Column(name = "vipName")
  private String vipName;  //会员名称 ,代理商名称

  @Column(name = "vipNumber")
  private String vipNumber;  //会员编号（登录账号）

  @Column(name = "superiorVipNumber")
  private String superiorVipNumber;  //上级会员编号，代理商的上级会员编号

  @Column(name = "contacts")
  private String contacts;  //联系人

  @Column(name = "mobilePhone")
  private String mobilePhone;  //联系电话

  @Column(name = "cardType")
  private String cardType;  //证件类型

  @Column(name = "cardId")
  private String cardId;  //证件号码

  @Column(name = "openTime")
  private String openTime;  //签约时间

  @Column(name = "openBank")
  private String openBank;  //开户银行

  @Column(name = "openBankKey")
  private String openBankKey;  //开户银行

  @Column(name = "bankCard")
  private String bankCard;  //银行卡号

  @Column(name = "province")
  private String province;  //所在省

  @Column(name = "provinceKey")
  private String provinceKey;  //所在省Key

  @Column(name = "city")
  private String city;  //所在市

  @Column(name = "cityKey")
  private String cityKey;  //所在市Key

  @Column(name = "isDelete")
  private Integer isDelete;  //0没有注销，1注销


  /**
   * <p> TODO</p>
   * @return: Integer
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
   * @return: String
   */
  public String getOpenBankKey() {
    return openBankKey;
  }

  /**
   * <p> TODO</p>
   * @return: String
   */
  public void setOpenBankKey(String openBankKey) {
    this.openBankKey = openBankKey;
  }

  /**
   * <p> TODO</p>
   * @return: String
   */
  public String getSuperiorVipNumber() {
    return superiorVipNumber;
  }

  /**
   * <p> TODO</p>
   * @return: String
   */
  public void setSuperiorVipNumber(String superiorVipNumber) {
    this.superiorVipNumber = superiorVipNumber;
  }

  /**
   * <p> TODO</p>
   * @return: String
   */
  public String getProvinceKey() {
    return provinceKey;
  }

  /**
   * <p> TODO</p>
   * @return: String
   */
  public void setProvinceKey(String provinceKey) {
    this.provinceKey = provinceKey;
  }

  /**
   * <p> TODO</p>
   * @return: String
   */
  public String getCityKey() {
    return cityKey;
  }

  /**
   * <p> TODO</p>
   * @return: String
   */
  public void setCityKey(String cityKey) {
    this.cityKey = cityKey;
  }


  /**
   * <p>id</p>
   * @author: liushilei
   * @return: Long
   * @Date :   2016-10-19 17:40:12
   */
  public Long getId() {
    return id;
  }

  /**
   * <p>id</p>
   * @author: liushilei
   * @param:   @param id
   * @return: void
   * @Date :   2016-10-19 17:40:12
   */
  public void setId(Long id) {
    this.id = id;
  }


  /**
   * <p>会员名称</p>
   * @author: liushilei
   * @return: String
   * @Date :   2016-10-19 17:40:12
   */
  public String getVipName() {
    return vipName;
  }

  /**
   * <p>会员名称</p>
   * @author: liushilei
   * @param:   @param vipName
   * @return: void
   * @Date :   2016-10-19 17:40:12
   */
  public void setVipName(String vipName) {
    this.vipName = vipName;
  }


  /**
   * <p>会员编号</p>
   * @author: liushilei
   * @return: String
   * @Date :   2016-10-19 17:40:12
   */
  public String getVipNumber() {
    return vipNumber;
  }

  /**
   * <p>会员编号</p>
   * @author: liushilei
   * @param:   @param vipNumber
   * @return: void
   * @Date :   2016-10-19 17:40:12
   */
  public void setVipNumber(String vipNumber) {
    this.vipNumber = vipNumber;
  }


  /**
   * <p>联系人</p>
   * @author: liushilei
   * @return: String
   * @Date :   2016-10-19 17:40:12
   */
  public String getContacts() {
    return contacts;
  }

  /**
   * <p>联系人</p>
   * @author: liushilei
   * @param:   @param contacts
   * @return: void
   * @Date :   2016-10-19 17:40:12
   */
  public void setContacts(String contacts) {
    this.contacts = contacts;
  }


  /**
   * <p>联系电话</p>
   * @author: liushilei
   * @return: String
   * @Date :   2016-10-19 17:40:12
   */
  public String getMobilePhone() {
    return mobilePhone;
  }

  /**
   * <p>联系电话</p>
   * @author: liushilei
   * @param:   @param mobilePhone
   * @return: void
   * @Date :   2016-10-19 17:40:12
   */
  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }


  /**
   * <p>证件类型</p>
   * @author: liushilei
   * @return: String
   * @Date :   2016-10-19 17:40:12
   */
  public String getCardType() {
    return cardType;
  }

  /**
   * <p>证件类型</p>
   * @author: liushilei
   * @param:   @param cardType
   * @return: void
   * @Date :   2016-10-19 17:40:12
   */
  public void setCardType(String cardType) {
    this.cardType = cardType;
  }


  /**
   * <p>证件号码</p>
   * @author: liushilei
   * @return: String
   * @Date :   2016-10-19 17:40:12
   */
  public String getCardId() {
    return cardId;
  }

  /**
   * <p>证件号码</p>
   * @author: liushilei
   * @param:   @param cardId
   * @return: void
   * @Date :   2016-10-19 17:40:12
   */
  public void setCardId(String cardId) {
    this.cardId = cardId;
  }


  /**
   * <p>签约时间</p>
   * @author: liushilei
   * @return: String
   * @Date :   2016-10-19 17:40:12
   */
  public String getOpenTime() {
    return openTime;
  }

  /**
   * <p>签约时间</p>
   * @author: liushilei
   * @param:   @param openTime
   * @return: void
   * @Date :   2016-10-19 17:40:12
   */
  public void setOpenTime(String openTime) {
    this.openTime = openTime;
  }


  /**
   * <p>开户银行</p>
   * @author: liushilei
   * @return: String
   * @Date :   2016-10-19 17:40:12
   */
  public String getOpenBank() {
    return openBank;
  }

  /**
   * <p>开户银行</p>
   * @author: liushilei
   * @param:   @param openBank
   * @return: void
   * @Date :   2016-10-19 17:40:12
   */
  public void setOpenBank(String openBank) {
    this.openBank = openBank;
  }


  /**
   * <p>银行卡号</p>
   * @author: liushilei
   * @return: String
   * @Date :   2016-10-19 17:40:12
   */
  public String getBankCard() {
    return bankCard;
  }

  /**
   * <p>银行卡号</p>
   * @author: liushilei
   * @param:   @param bankCard
   * @return: void
   * @Date :   2016-10-19 17:40:12
   */
  public void setBankCard(String bankCard) {
    this.bankCard = bankCard;
  }


  /**
   * <p>所在省</p>
   * @author: liushilei
   * @return: String
   * @Date :   2016-10-19 17:40:12
   */
  public String getProvince() {
    return province;
  }

  /**
   * <p>所在省</p>
   * @author: liushilei
   * @param:   @param province
   * @return: void
   * @Date :   2016-10-19 17:40:12
   */
  public void setProvince(String province) {
    this.province = province;
  }


  /**
   * <p>所在市</p>
   * @author: liushilei
   * @return: String
   * @Date :   2016-10-19 17:40:12
   */
  public String getCity() {
    return city;
  }

  /**
   * <p>所在市</p>
   * @author: liushilei
   * @param:   @param city
   * @return: void
   * @Date :   2016-10-19 17:40:12
   */
  public void setCity(String city) {
    this.city = city;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "SpVipUser [id=" + id + ",  vipName=" + vipName + ", vipNumber=" + vipNumber
        + ", contacts=" + contacts + ", mobilePhone=" + mobilePhone + ", cardType=" + cardType
        + ", cardId=" + cardId + ", openTime=" + openTime + ", openBank=" + openBank + ", bankCard="
        + bankCard + ", province=" + province + ", city=" + city + "]";
  }

  /**
   * <p> TODO</p>
   * @return: String
   */
  public String getUserName() {
    return userName;
  }

  /**
   * <p> TODO</p>
   * @return: String
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * <p> TODO</p>
   * @return: String
   */
  public String getPassWord() {
    return passWord;
  }

  /**
   * <p> TODO</p>
   * @return: String
   */
  public void setPassWord(String passWord) {
    this.passWord = passWord;
  }

  /**
   * <p> TODO</p>
   * @return: Long
   */
  public Long getAppUserId() {
    return appUserId;
  }

  /**
   * <p> TODO</p>
   * @return: Long
   */
  public void setAppUserId(Long appUserId) {
    this.appUserId = appUserId;
  }

  /**
   * <p> TODO</p>
   * @return: String
   */
  public String getVipType() {
    return vipType;
  }

  /**
   * <p> TODO</p>
   * @return: String
   */
  public void setVipType(String vipType) {
    this.vipType = vipType;
  }


}
