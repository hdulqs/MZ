/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月12日 下午6:30:58
 */
package com.mz.web.menu.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

import com.mz.core.mvc.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <p> TODO</p>  左侧菜单栏
 * @author:         Liu Shilei 
 * @Date :          2015年10月12日 下午6:30:58 
 */
@Table(name = "app_menu")
public class AppMenu extends BaseModel {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name="mkey")
	private String mkey;  //KEY值
	
	@Column(name="pkey")
	private String pkey; //父类KEY值
	
	@Column(name="appName")
	private String appName; //应用系统名称
	
	@Column(name="name")
	private String name; //菜单名称
	
	@Column(name="url")
	private String url; //菜单URL
	
	@Column(name="shiroUrl")
	private String shiroUrl;//权限RUL	
	
	@Column(name="isOpen")
	private String isOpen;//是否展开  0不展开  1展开
	
	@Column(name="isOutLink")
	private String isOutLink;//是否是外部链接  0内部 1外部
	
	@Column(name="orderNo")
	private Integer orderNo; //排序号  优先级
	
	@Column(name="type")
	private String type;//xml类型
	
	@Column(name="isVisible")  //是否可见
	private String isVisible; //是否可见  0可见  1不可见  //是否可见
	
	@Transient
	private List<AppMenu> subMenu;   //子菜单集合
	
	

	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getId() {
		return id;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getName() {
		return name;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getUrl() {
		return url;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getShiroUrl() {
		return shiroUrl;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setShiroUrl(String shiroUrl) {
		this.shiroUrl = shiroUrl;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getIsOpen() {
		return isOpen;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getIsOutLink() {
		return isOutLink;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setIsOutLink(String isOutLink) {
		this.isOutLink = isOutLink;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Integer
	 */
	public Integer getOrderNo() {
		return orderNo;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Integer
	 */
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getAppName() {
		return appName;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getMkey() {
		return mkey;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setMkey(String mkey) {
		this.mkey = mkey;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getPkey() {
		return pkey;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPkey(String pkey) {
		this.pkey = pkey;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getType() {
		return type;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getIsVisible() {
		return isVisible;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setIsVisible(String isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * <p> TODO</p>
	 * @return:     List<AppMenu>
	 */
	public List<AppMenu> getSubMenu() {
		return subMenu;
	}

	/** 
	 * <p> TODO</p>
	 * @return: List<AppMenu>
	 */
	public void setSubMenu(List<AppMenu> subMenu) {
		this.subMenu = subMenu;
	}
	
	
	
	
	
	
}
