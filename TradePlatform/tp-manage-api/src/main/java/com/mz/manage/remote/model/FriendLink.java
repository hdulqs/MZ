package com.mz.manage.remote.model;


import java.io.Serializable;


public class FriendLink implements  Serializable{
	
	private Long id;
	// 友情链接名称
	private String name;
	// 友情链接地址
	private String linkUrl;
	// 图片地址
	private String picturePath;
	
	// 状态
	private Integer status;
	// 是否是图片
	private Integer isPicture;
	
	// 区分中国站(cn表示中国站  en表示国际站)
	private String website;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsPicture() {
		return isPicture;
	}

	public void setIsPicture(Integer isPicture) {
		this.isPicture = isPicture;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

}
