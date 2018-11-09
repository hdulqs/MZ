/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Gao Mimi
 * @version: V1.0
 * @Date: 2015年10月10日 下午6:02:16
 */
package com.mz.web.util;

import com.mz.util.file.FtpUtil;
import com.mz.core.constant.CodeConstant;

/**
 * <p> TODO</p>
 * @author: Gao Mimi
 * @Date :          2015年10月10日 下午6:02:16 
 */
public class FtpUtils {

  private FtpUtil ftpUtil;

  public FtpUtils() {/*
		AppConfigService appConfigService=(AppConfigService)ContextUtil.getBean("appConfigService");
		String ftpip=appConfigService.getBykey("ftpip");
		String ftpuser=appConfigService.getBykey("ftpuser");
		String ftppsw=appConfigService.getBykey("ftppsw");
		String ftpport=appConfigService.getBykey("ftpport");
	    ftpUtil = new FtpUtil(ftpip, ftpuser,ftppsw,Integer.valueOf(ftpport));
	*/
  }

  public String ftpupload(String remotePath, String localPath, String filename) {

    try {
      ftpUtil.uploadFile(remotePath, localPath, filename);
      return CodeConstant.CODE_SUCCESS;
    } catch (Exception e) {
      e.printStackTrace();
      return CodeConstant.CODE_FAILED;
    }
  }

  /**
   * <p> TODO</p>
   * @return: FtpUtil
   */
  public FtpUtil getFtpUtil() {
    return ftpUtil;
  }

  /**
   * <p> TODO</p>
   * @return: FtpUtil
   */
  public void setFtpUtil(FtpUtil ftpUtil) {
    this.ftpUtil = ftpUtil;
  }

}
