/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年6月21日 下午7:34:25
 */
package com.mz.web.util;

/**
 * <p>文件的信息</p>
 * @author:         Liu Shilei 
 * @Date :          2016年6月21日 下午7:34:25 
 */
public class FileInfo {
	
	private String fileName;     //系统显示的名称,下载的名称
	private String fileTrueName; //文件保存的uuid名称
	private String fileWebPath;  //文件的web路径  存放于web服务器下的路径
	private String fileLocalPath; //文件本地存放的路径
	private String fileRemotePath; //文件远程存放的路径
	private String extendname;//文件扩展名
	private Long filesize;//文件大小
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getFileName() {
		return fileName;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getFileTrueName() {
		return fileTrueName;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setFileTrueName(String fileTrueName) {
		this.fileTrueName = fileTrueName;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getFileLocalPath() {
		return fileLocalPath;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setFileLocalPath(String fileLocalPath) {
		this.fileLocalPath = fileLocalPath;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getFileRemotePath() {
		return fileRemotePath;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setFileRemotePath(String fileRemotePath) {
		this.fileRemotePath = fileRemotePath;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getExtendname() {
		return extendname;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setExtendname(String extendname) {
		this.extendname = extendname;
	}
	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getFilesize() {
		return filesize;
	}
	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getFileWebPath() {
		return fileWebPath;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setFileWebPath(String fileWebPath) {
		this.fileWebPath = fileWebPath;
	}
	
	
	
}
