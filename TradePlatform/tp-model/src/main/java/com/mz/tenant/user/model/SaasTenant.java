/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年11月10日 下午3:05:29
 */
package com.mz.tenant.user.model;

import static javax.persistence.GenerationType.IDENTITY;

import com.mz.core.mvc.model.MgrBaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * <p> TODO</p>  saas租户表
 *
 * @author: Liu Shilei
 * @Date :          2015年11月10日 下午3:05:29
 */
@Entity
@Table(name = "saas_tenant")
@DynamicInsert(true)
@DynamicUpdate(true)
public class SaasTenant extends MgrBaseModel {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "saasId", unique = true, nullable = false)
  private String saasId;

  @Column(name = "name")
  private String name; //姓名

  @Column(name = "email")
  private String email;//email

  @Column(name = "mobile")
  private String mobile;//手机号

  @Column(name = "company")
  private String company;//公司

  //是否开通saas前台账户，0没开通，1开通
  @Column(name = "isOpenSaasUser")
  private String isOpenSaasUser = "0";


  /**
   * <p> TODO</p>
   *
   * @return: String
   */
  public String getSaasId() {
    return saasId;
  }

  /**
   * <p> TODO</p>
   *
   * @return: String
   */
  public void setSaasId(String saasId) {
    this.saasId = saasId;
  }

  /**
   * <p> TODO</p>
   *
   * @return: String
   */
  public String getName() {
    return name;
  }

  /**
   * <p> TODO</p>
   *
   * @return: String
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * <p> TODO</p>
   *
   * @return: String
   */
  public String getEmail() {
    return email;
  }

  /**
   * <p> TODO</p>
   *
   * @return: String
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * <p> TODO</p>
   *
   * @return: String
   */
  public String getMobile() {
    return mobile;
  }

  /**
   * <p> TODO</p>
   *
   * @return: String
   */
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  /**
   * <p> TODO</p>
   *
   * @return: String
   */
  public String getCompany() {
    return company;
  }

  /**
   * <p> TODO</p>
   *
   * @return: String
   */
  public void setCompany(String company) {
    this.company = company;
  }

  /**
   * <p> TODO</p>
   *
   * @return: String
   */
  public String getIsOpenSaasUser() {
    return isOpenSaasUser;
  }

  /**
   * <p> TODO</p>
   *
   * @return: String
   */
  public void setIsOpenSaasUser(String isOpenSaasUser) {
    this.isOpenSaasUser = isOpenSaasUser;
  }


}
