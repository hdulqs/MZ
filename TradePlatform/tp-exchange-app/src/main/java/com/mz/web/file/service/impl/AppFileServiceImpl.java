/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Gao Mimi
 * @version: V1.0
 * @Date: 2015年09月28日  18:10:04
 */
package com.mz.web.file.service.impl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.oauth.remote.company.RemoteAppOrganizationService;
import com.mz.oauth.user.model.AppOrganization;
import com.mz.oauth.user.model.AppUser;
import com.mz.oauth.user.model.AppUserOrganization;
import com.mz.util.RemoteQueryFilter;
import com.mz.util.file.FileUtil;
import com.mz.util.file.FtpUtil;
import com.mz.util.file.Md5Util;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.web.file.model.AppFile;
import com.mz.web.file.model.AppFileRelation;
import com.mz.web.util.FileInfo;
import com.mz.core.constant.CodeConstant;
import com.mz.core.constant.StringConstant;
import com.mz.core.exception.HryError;
import com.mz.core.exception.HryException;
import com.mz.web.file.service.AppFileRelationService;
import com.mz.web.file.service.AppFileService;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p> TODO</p>
 * @author: Gao Mimi
 * @Date :    2015年09月28日  18:10:04     
 */
@Service("appFileService")
public class AppFileServiceImpl extends BaseServiceImpl<AppFile, Long> implements AppFileService {

  @Resource(name = "appFileDao")
  @Override
  public void setDao(BaseDao<AppFile, Long> dao) {
    super.dao = dao;
  }


  @Resource
  private AppFileRelationService appFileRelationService;

  /**
   * 文件上传到本地接口
   * return String[2]  【0】成功或失败，【1】成功返回id，失败为0
   */
  @Override
  public String[] uploadLocal(MultipartFile file, String mark, String fileLocalpath) {
    String[] reslut = new String[2];
    try {
      // 文件是否为空
      if (file.getSize() == 0 || file.isEmpty()) {
        throw new HryException(HryError.FILE_EMPTY);
      }
      String path = ContextUtil.getAppAbsolutePath() + StringConstant.ATTACH_FILES + fileLocalpath;
      FileUtil.mkDirectory(path);
      String ori_fileName = file.getOriginalFilename();
      String md5_fileName = Md5Util.getFileMD5String(file.getInputStream()) + "." + FileUtil
          .getExtensionName(ori_fileName);
      md5_fileName = FileUtil.checkFileName(md5_fileName, path);
      File targetFile = new File(path, md5_fileName);
      if (!targetFile.exists()) {
        file.transferTo(targetFile);

      }
      AppFile appFile = createAppFile(mark, BigDecimal.valueOf(file.getSize()),
          fileLocalpath + "\\" + md5_fileName, null,
          FileUtil.getExtensionName(ori_fileName), FileUtil.getFileNameNoEx(ori_fileName));
      super.save(appFile);
      reslut[0] = CodeConstant.CODE_SUCCESS;
      reslut[1] = appFile.getFileid().toString();
    } catch (Exception e) {
      reslut[0] = CodeConstant.CODE_FAILED;
      reslut[1] = "0";
      System.out.println(e);

    }
    return reslut;
  }

  private AppFile createAppFile(String mark, BigDecimal filesize, String fileLocalpath,
      String fileRemotePath, String extendname, String filename) {
    AppFile appFile = new AppFile();
    appFile.setMark(mark);
    appFile.setFilesize(filesize);
    appFile.setFileLocalpath(fileLocalpath);
    appFile.setFileRemotePath(fileRemotePath);
    appFile.setExtendname(extendname);
    appFile.setFilename(filename);
    appFile.setCreatorid(ContextUtil.getCurrentUser().getId());
    return appFile;
  }

  /**
   * 文件上传到到远程ftp接口口
   */
  @Override
  public String[] uploadRemote(MultipartFile file, String mark, String fileRemotePath,
      FtpUtil ftpUtil) {
    String[] reslut = new String[2];
    try {
      // 文件是否为空
      if (file.getSize() == 0 || file.isEmpty()) {
        throw new HryException(HryError.FILE_EMPTY);
      }
      String ori_fileName = file.getOriginalFilename();
      String md5_fileName = Md5Util.getFileMD5String(file.getInputStream()) + "." + FileUtil
          .getExtensionName(ori_fileName);
      ftpUtil.uploadFile(fileRemotePath, file, md5_fileName);
      AppFile appFile = createAppFile(mark, BigDecimal.valueOf(file.getSize()), null,
          fileRemotePath,
          FileUtil.getExtensionName(ori_fileName), FileUtil.getFileNameNoEx(ori_fileName));
      super.save(appFile);
      reslut[0] = CodeConstant.CODE_SUCCESS;
      reslut[1] = appFile.getFileid().toString();
    } catch (Exception e) {
      reslut[0] = CodeConstant.CODE_FAILED;
      reslut[1] = "0";
      System.out.println(e);

    }
    return reslut;
  }

  /**
   * 文件删除接口
   */
//	@Override
//	public void delete(Long id) {
//		QueryFilter filter = new QueryFilter(AppFile.class);
//		if (id != null) {
//			filter.addFilter("Q_t.id_=_Long", "" + id);
//		}
//		AppFile appFile=super.get(id);
//		 super.delete(appFile);
//	}

  /**
   * 根据唯一标识mark得到文件列表
   */
  @Override
  public List<AppFile> getListbyMark(String mark) {
	/*	QueryFilter filter = new QueryFilter();
		if (mark != null) {
			filter.addFilter("Q_t.mark_=_String",mark);
		}
		List<AppFile> list=super.dao.find(filter);*/
    return null;
    // return list;
  }


  public void saveAppFile() {
  }

  @Override
  public AppFile setFileRelation(AppUser user, FileInfo fileInfo) {

    //保存文件对象
    AppFile appFile = new AppFile();
    appFile.setMark("");
    appFile.setFilesize(new BigDecimal(fileInfo.getFilesize()));
    appFile.setFileLocalpath(fileInfo.getFileLocalPath());
    appFile.setFileRemotePath(fileInfo.getFileRemotePath());
    appFile.setFileWebPath(fileInfo.getFileWebPath());
    appFile.setExtendname(fileInfo.getExtendname());
    appFile.setFilename(fileInfo.getFileName());
    appFile.setFileTrueName(fileInfo.getFileTrueName());
    appFile.setCreatorid(user.getId());
    super.save(appFile);

    if (!PropertiesUtils.APP.getProperty("app.admin").equals(user.getUsername())) {
      //查询用户所属的所有组织机构
      RemoteAppOrganizationService remoteAppOrganizationService = (RemoteAppOrganizationService) ContextUtil
          .getBean("remoteAppOrganizationService");
      RpcContext.getContext().setAttachment("saasId", ContextUtil.getSaasId());
      List<AppUserOrganization> list = remoteAppOrganizationService.findUserOrgByUid(user.getId());

      if (list != null && list.size() > 0) {
        for (int i = 0; i < list.size(); i++) {
          AppUserOrganization appUserOrganization = list.get(i);
          AppFileRelation appFileRelation = new AppFileRelation();
          appFileRelation.setFileId(appFile.getFileid());
          appFileRelation.setOrgId(appUserOrganization.getOrganizationId());
          appFileRelation.setUserId(appUserOrganization.getUserId());
          appFileRelation.setMkey("org" + appUserOrganization.getOrganizationId());

          if (i == 0) {//如果i==0  则pkey == 空
            appFileRelation.setPkey("");
          } else {//如果i>0则pkey 等于前一个对象的对象名加Id
            appFileRelation.setPkey("org" + list.get(i - 1).getOrganizationId());
          }
          appFileRelationService.save(appFileRelation);
        }
      }
    } else {
      AppFileRelation root = saveRoot(appFile.getFileid(), user);
    }

    return appFile;

  }

  /**
   * 保存文件关系表 root对象
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param fileId
   * @param:    @param userId
   * @param:    @return
   * @return: AppFileRelation
   * @Date :          2016年6月21日 上午10:14:58
   * @throws:
   */
  public AppFileRelation saveRoot(Long fileId, AppUser user) {

    RemoteAppOrganizationService remoteAppOrganizationService = (RemoteAppOrganizationService) ContextUtil
        .getBean("remoteAppOrganizationService");
    RemoteQueryFilter queryFilter = new RemoteQueryFilter(AppOrganization.class);
    queryFilter.setSaasId(user.getSaasId());
    queryFilter.addFilter("type=", "root");
    AppOrganization appOrganization = remoteAppOrganizationService.get(queryFilter);
    AppFileRelation appFileRelation = new AppFileRelation();
    appFileRelation.setFileId(fileId);
    appFileRelation.setOrgId(appOrganization.getId());
    appFileRelation.setUserId(user.getId());
    appFileRelation.setMkey("org" + appOrganization.getId());
    appFileRelation.setPkey("");
    appFileRelationService.save(appFileRelation);
    return appFileRelation;

  }


}