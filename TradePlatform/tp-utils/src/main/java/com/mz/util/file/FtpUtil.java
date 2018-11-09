/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2015年10月10日 上午11:26:25
 */
package com.mz.util.file;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p> ftp文件上传</p>
 * @author:         Gao Mimi 
 * @Date :          2015年10月10日 上午11:26:25 
 */

/**
 * 
 * 在FTP服务端设定为被动模式，且客户端和服务端不在同一个网段的情况下，
 * sun.net.ftp.FtpClient会出现上传文件失败（进程僵死，服务端接收到的文件大小为0）的情况，
 * 此为该类的一个BUG，建议使用java ftp时不要使用sun.net.ftp.FtpClient，
 * 而用APACHE基金会的commons-net.jar中的org.apache.commons.net.ftp.FTPClient来实现
 */
public class FtpUtil {
	String ip;
	int port;
	String user;
	String pwd;
	String remotePath;
	String localPath;
	FTPClient ftpClient;

	public FtpUtil(String ip, String user, String pwd,int port) {
		this.ip = ip;
		this.user = user;
		this.pwd = pwd;
		this.port=port;
	}

	/**
	 * 连接ftp服务器
	 * 
	 * @param ip
	 * @param port
	 * @param user
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public boolean connectServer(String ip, int port, String user, String pwd)
			throws Exception {
		boolean isSuccess = false;
		try {
			ftpClient = new FTPClient();
			ftpClient.connect(ip);
			isSuccess=ftpClient.login(user, pwd);
		} catch (Exception e) {
			
			throw new Exception("Connect ftp server error:" + e.getMessage());
		}
		if(isSuccess==false){
			
			System.out.println("密码或用户名错误");
		}else{
			
			System.out.println("ftp服务器连接成功");
		}
		return isSuccess;
	}

	public void uploadFile(String remotePath, String localPath, String filename)
			throws Exception {
		try {
			if (connectServer(getIp(), getPort(), getUser(), getPwd())) {
				if (remotePath.length() != 0){
					long startT=System.currentTimeMillis(); //记录开始前的时间
				
					ftpClient.changeWorkingDirectory(remotePath);// 改变工作目录
					ftpClient.enterLocalPassiveMode();// 更改客户端为passive模式
					ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);// 设置二进制传输方式

					// System.out.println(ftpClient.getResponseStrings());

					File file_in = new File(localPath + File.separator + filename);
					System.out.println("file_in====" + file_in.getAbsolutePath());
					FileInputStream is = new FileInputStream(file_in);
					
					ftpClient.storeFile(filename, is);
					is.close();
					ftpClient.disconnect();
					
					
					long endT=System.currentTimeMillis();//记录结束后的时间
					long totT=endT-startT;// 耗时
					System.out.println("上传耗时："+totT+"ms。");
			  }
			}
		} catch (Exception ex) {
			throw new Exception("ftp upload file error:" + ex.getMessage());
		}
	}
	public void uploadFile(String remotePath, MultipartFile file, String filename)
			throws Exception {
		try {
			if (connectServer(getIp(), getPort(), getUser(), getPwd())) {
				if (remotePath.length() != 0){
					long startT=System.currentTimeMillis(); //记录开始前的时间
				
				
                    //建立文件夹 
					
					ftpClient.makeDirectory(remotePath);
					
					ftpClient.changeWorkingDirectory(remotePath);// 改变工作目录
					ftpClient.enterLocalPassiveMode();// 更改客户端为passive模式
					ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);// 设置二进制传输方式
					
					
					//数据开始传输
					InputStream is=file.getInputStream();
					ftpClient.storeFile(filename, file.getInputStream());
					is.close();
					ftpClient.disconnect();
					
					
					long endT=System.currentTimeMillis();//记录结束后的时间
					long totT=endT-startT;// 耗时
					System.out.println("上传耗时："+totT+"ms。");
			  }
			}
		} catch (Exception ex) {
			throw new Exception("ftp upload file error:" + ex.getMessage());
		}
	}
	
	
	
	public static void main(String[] args) throws Exception {
		/*
		 * args[0]="172.16.8.142"; args[1]="ftpuser"; args[2]="123456";
		 * args[3]="/home/user"; args[4]="C:";
		 * args[5]="file.jsp";
		 */
		FtpUtil ftpTest = new FtpUtil("47.75.200.109", "lvne", "mike@123456789",21);
		ftpTest.uploadFile("/home/user", "E:","aaa.txt");

	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

}
