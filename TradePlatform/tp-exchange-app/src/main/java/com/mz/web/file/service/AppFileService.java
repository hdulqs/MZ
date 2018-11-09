/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Gao Mimi
 * @version: V1.0
 * @Date: 2015年09月28日  18:10:04
 */
package com.mz.web.file.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.oauth.user.model.AppUser;
import com.mz.util.file.FtpUtil;
import com.mz.web.file.model.AppFile;
import com.mz.web.util.FileInfo;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p> TODO</p>
 * @author: Gao Mimi
 * @Date :      2015年09月28日  18:10:04
 */
public interface AppFileService extends BaseService<AppFile, Long> {

  public String[] uploadLocal(MultipartFile file, String mark, String fileLocalpath);

  public String[] uploadRemote(MultipartFile file, String mark, String fileRemotePath,
      FtpUtil ftpUtil);

  //public void delete(Long id);
  public List<AppFile> getListbyMark(String mark);


  /**
   * 维护文件关系表
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:
   * @return: void
   * @Date :          2016年6月21日 上午9:35:02
   * @throws:
   */
  public AppFile setFileRelation(AppUser user, FileInfo fileInfo);
}


