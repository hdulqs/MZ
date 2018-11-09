/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年3月31日 下午6:17:33
 */
package com.mz.account.fund.model;

import static javax.persistence.GenerationType.IDENTITY;

import com.mz.core.mvc.model.BaseModel;
import com.mz.core.mvc.model.ModelUtil;


import javax.persistence.*;

/**
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2016年3月31日 下午6:17:33
 */
@Table(name = "app_bank_card")
public class AppBankCard extends BaseModel {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "accountId")
    private Long accountId;  //账户ID

    @Column(name = "customerId")
    private Long customerId;   //用户id

    @Column(name = "userName")
    private String userName;  //用户名

    @Column(name = "trueName")
    private String trueName;

    @Column(name = "currencyType")
    private String currencyType;  //货币类型

    @Column(name = "cardName")
    private String cardName; // 银行卡持有人

    @Column(name = "cardNumber")
    private String cardNumber; //银行卡号

    @Column(name = "cardBank")
    private String cardBank;  //开户银行

    @Column(name = "bankAddress")
    private String bankAddress;  //开户行所在地  开户市

    @Column(name = "subBank")
    private String subBank;  //开户支行

    @Column(name = "subBankNum")
    private String subBankNum;  //开户支行银行机构代码


    @Column(name = "website")
    private String website;//站点类别默认cn

    @Column(name = "bankProvince")
    private String bankProvince;  //开户省份


    @Column(name = "signBank")
    private String signBank;  //签约银行通道


    @Column(name = "surname")
    private String surname;


    @Column(name = "isDelete")
    private int isDelete;

    @Transient
    private String mobile;    //手机 2018.4.25新加 by gt


    @Column(name = "weChatPicture")
    private String weChatPicture;  //微信照片     add by zongwei  20180511

    @Column(name = "alipayPicture")
    private String alipayPicture;  //支付宝照片     add by zongwei 20180511

    @Column(name = "weChat")
    private String weChat;  //微信照片     add by zongwei 20180511

    @Column(name = "alipay")
    private String alipay;  //支付宝照片     add by zongwei 20180511

    @Column(name = "bankProvinceKey")
    private String bankProvinceKey;  //开户省份key  add by zongwei 20180514


    public String getBankProvinceKey() {
        return bankProvinceKey;
    }

    public void setBankProvinceKey(String bankProvinceKey) {
        this.bankProvinceKey = bankProvinceKey;
    }

    public String getWeChatPicture() {
        return weChatPicture;
    }

    public void setWeChatPicture(String weChatPicture) {
        this.weChatPicture = weChatPicture;
    }

    public String getAlipayPicture() {
        return alipayPicture;
    }

    public void setAlipayPicture(String alipayPicture) {
        this.alipayPicture = alipayPicture;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSubBankNum() {
        return subBankNum;
    }

    public void setSubBankNum(String subBankNum) {
        this.subBankNum = subBankNum;
    }

    public String getSignBank() {
        return signBank;
    }

    public void setSignBank(String signBank) {
        this.signBank = signBank;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public String getBankProvince() {
        return bankProvince;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince;
    }

    /**
     * <p> TODO</p>
     *
     * @return: Long
     */
    public Long getId() {
        return id;
    }

    /**
     * <p> TODO</p>
     *
     * @return: Long
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <p> TODO</p>
     *
     * @return: Long
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * <p> TODO</p>
     *
     * @return: Long
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public String getCurrencyType() {
        return currencyType;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    /**
     * <p> TODO</p>
     *
     * @return: Long
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * <p> TODO</p>
     *
     * @return: Long
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }


    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public String getCardBank() {
        return cardBank;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public void setCardBank(String cardBank) {
        this.cardBank = cardBank;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public String getBankAddress() {
        return bankAddress;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public String getSubBank() {
        return subBank;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public void setSubBank(String subBank) {
        this.subBank = subBank;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public String getUserName() {
        return userName;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


}
