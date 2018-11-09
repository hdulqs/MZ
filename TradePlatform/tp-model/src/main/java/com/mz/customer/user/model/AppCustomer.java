/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年3月24日 下午2:59:48
 */
package com.mz.customer.user.model;

import static javax.persistence.GenerationType.IDENTITY;

import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>客户基础表---登录账号信息</p>
 *
 * @author: Liu Shilei
 * @Date :          2016年3月24日 下午2:59:48
 */
@Table(name = "app_customer")
@Getter
@Setter
public class AppCustomer extends BaseModel {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "userName")
  private String userName;   //用户名

  @Column(name = "passWord")
  private String passWord;  //密码

  @Column(name = "accountPassWord")
  private String accountPassWord;  //交易密码

  @Column(name = "isLock")
  private Integer isLock;  //是否锁定   0没锁定  1锁定

  @Column(name = "lockTime")
  private Date lockTime;  //锁定时间

  @Column(name = "isChange")
  private Integer isChange;  //是否能交易  0可以交易1不能交易

  @Column(name = "isDelete")
  private Integer isDelete;  //是否禁用  0没有禁用 1禁用

  @Column(name = "isReal")
  private Integer isReal;  //是否实名  0没有实名  1实名

  @Column(name = "isRealUsd")
  private Integer isRealUsd; //是否国际站实名  0 没有实名  1实名

  @Column(name = "salt")
  private String salt;  //盐

  @Column(name = "userCode")
  private String userCode;  //用户唯一ID标识 系统生成

  @Column(name = "integral")
  private Integer integral;  //积分

  @Column(name = "type")
  private String type;  //integral  积分用户

  @Transient   //不与数据库映射的字段
  private Object appPersonInfo;

  // 注册码
  @Column(name = "referralCode")
  private String referralCode;

  // 用户是否注册邮箱
  @Column(name = "hasEmail")
  public Integer hasEmail;

  @Column(name = "googleKey")
  private String googleKey;

  @Column(name = "googleState")
  private Integer googleState;//谷歌认证状态(0否，1是)


  @Column(name = "googleDate")
  private Date googleDate;//停用时间


  @Column(name = "isProving")
  private Integer isProving;//二次验证是否开启（0 否,1是）

  @Column(name = "messIp")
  private String messIp;//二次验证是否开启（0 否,1是）


  @Column(name = "passDate")
  private Date passDate;//二次验证是否开启（0 否,1是）


  @Column(name = "phone")
  private String phone;

  @Column(name = "phoneState")
  private Integer phoneState;//谷歌认证状态(0否，1是)


  @Column(name = "openPhone")
  private Integer openPhone;//谷歌认证状态(0否，1是)

  // 状态(  0 未实名 1 待审核 2 已实名 3 已拒绝)
  @Column(name = "states")
  private Integer states;

  @Column(name = "uuid")
  private String uuid;

  @Column(name = "commendCode")
  private String commendCode;

  @Transient
  private Long customerId;

  @Transient
  private String truename;

  @Transient
  private String surname;


  @Transient
  private int oneNumber;  //sid

  @Transient
  private int twoNumber;  //sid

  @Transient
  private int threeNumber;  //sid

  @Transient
  private int laterNumber;  //sid

  //新增
  @Column(name = "checkStates")
  private Integer checkStates;

  //第三方用户名
  @Column(name = "thirdUserName")
  private String thirdUserName;

  //第三方平台
  @Column(name = "platform")
  private String platform;

  //第三方用户密码
  @Column(name = "thirdUserPw")
  private String thirdUserPw;

  //otc冻结标识
  @Column(name = "otcFrozenFlag")
  private String otcFrozenFlag;

  //otc冻结日期
  @Column(name = "otcFrozenDate")
  private Date otcFrozenDate;

  @Column(name = "otcFrozenCout")
  private BigDecimal otcFrozenCout;

  //OTC最新取消日期
  @Column(name = "otcCancelDate")
  private Date otcCancelDate;

  //打开otc   0 关闭 1 打开
  @Column(name = "openOtcStates")
  private Integer openOtcStates;

  @Column(name = "mailStates")
  private Integer mailStates; //绑定邮箱标识 0 未绑定 1 绑定

  @Column(name = "mail")
  private String mail; //绑定的邮箱

  @Column(name = "memberPoint")
  private BigDecimal memberPoint; //会员积分

  @Transient
  private String company;
}
