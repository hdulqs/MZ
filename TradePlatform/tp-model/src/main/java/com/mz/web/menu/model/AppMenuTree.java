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
 * <p> TODO</p>  应用菜单树
 * @author:         Liu Shilei 
 * @Date :          2015年10月12日 下午6:30:58 
 */
@Table(name = "app_menu_tree")
public class AppMenuTree extends BaseModel {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name="okey")  //原始key值
	private String okey;
	
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

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOkey() {
		return okey;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOkey(String okey) {
		this.okey = okey;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appName == null) ? 0 : appName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isOpen == null) ? 0 : isOpen.hashCode());
		result = prime * result + ((isOutLink == null) ? 0 : isOutLink.hashCode());
		result = prime * result + ((isVisible == null) ? 0 : isVisible.hashCode());
		result = prime * result + ((mkey == null) ? 0 : mkey.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((okey == null) ? 0 : okey.hashCode());
		result = prime * result + ((orderNo == null) ? 0 : orderNo.hashCode());
		result = prime * result + ((pkey == null) ? 0 : pkey.hashCode());
		result = prime * result + ((shiroUrl == null) ? 0 : shiroUrl.hashCode());
		result = prime * result + ((subMenu == null) ? 0 : subMenu.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppMenuTree other = (AppMenuTree) obj;
		if (appName == null) {
			if (other.appName != null)
				return false;
		} else if (!appName.equals(other.appName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isOpen == null) {
			if (other.isOpen != null)
				return false;
		} else if (!isOpen.equals(other.isOpen))
			return false;
		if (isOutLink == null) {
			if (other.isOutLink != null)
				return false;
		} else if (!isOutLink.equals(other.isOutLink))
			return false;
		if (isVisible == null) {
			if (other.isVisible != null)
				return false;
		} else if (!isVisible.equals(other.isVisible))
			return false;
		if (mkey == null) {
			if (other.mkey != null)
				return false;
		} else if (!mkey.equals(other.mkey))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (okey == null) {
			if (other.okey != null)
				return false;
		} else if (!okey.equals(other.okey))
			return false;
		if (orderNo == null) {
			if (other.orderNo != null)
				return false;
		} else if (!orderNo.equals(other.orderNo))
			return false;
		if (pkey == null) {
			if (other.pkey != null)
				return false;
		} else if (!pkey.equals(other.pkey))
			return false;
		if (shiroUrl == null) {
			if (other.shiroUrl != null)
				return false;
		} else if (!shiroUrl.equals(other.shiroUrl))
			return false;
		if (subMenu == null) {
			if (other.subMenu != null)
				return false;
		} else if (!subMenu.equals(other.subMenu))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	

	
	
	
}
