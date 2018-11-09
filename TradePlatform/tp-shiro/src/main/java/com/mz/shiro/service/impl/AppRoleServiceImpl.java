/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年9月21日 上午11:28:30
 */
package com.mz.shiro.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.log.AppException;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.core.mvc.service.log.AppExceptionService;
import com.mz.oauth.user.model.AppResource;
import com.mz.oauth.user.model.AppRole;
import com.mz.oauth.user.model.AppRoleMenuTree;
import com.mz.oauth.user.model.AppUserRole;
import com.mz.shiro.dao.AppResourceDao;
import com.mz.shiro.dao.AppUserRoleDao;
import com.mz.shiro.service.AppRoleMenuTreeService;
import com.mz.shiro.service.AppRoleService;
import com.mz.util.BeanUtil;
import com.mz.util.QueryFilter;
import com.mz.util.date.DateUtil;
import com.mz.util.sys.ContextUtil;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2015年9月21日 上午11:28:30 
 */
@Service("appRoleService")
public class AppRoleServiceImpl extends BaseServiceImpl<AppRole, Long> implements AppRoleService {

  @Resource(name = "appRoleDao")
  @Override
  public void setDao(BaseDao<AppRole, Long> dao) {
    super.dao = dao;
  }

  @Resource
  private AppResourceDao appResourceDao;
  @Resource
  private AppUserRoleDao appUserRoleDao;

  @Resource
  private AppRoleMenuTreeService appRoleMenuTreeService;

  @Override
  public JsonResult add(HttpServletRequest request, AppRole appRole) {
    String menuTreeIds = request.getParameter("menuTreeIds");

    JsonResult jsonResult = new JsonResult();
    //判空
    if (org.apache.commons.lang3.StringUtils.isEmpty(menuTreeIds)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("权限不能为空");
      return jsonResult;
    } else {
      //保存角色
      this.save(appRole);

      String[] ids = org.apache.commons.lang3.StringUtils.split(menuTreeIds, ",");
      for (String id : ids) {
        AppRoleMenuTree appRoleMenuTree = new AppRoleMenuTree();
        appRoleMenuTree.setRoleId(appRole.getId());
        appRoleMenuTree.setMenuTreeId(Long.valueOf(id));
        appRoleMenuTreeService.save(appRoleMenuTree);
      }

      jsonResult.setSuccess(true);
      return jsonResult;
    }
  }

  @Override
  public JsonResult remove(String[] ids) {
    JsonResult jsonResult = new JsonResult();
    //判空
    if (StringUtils.isEmpty(ids)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("id不能为空");
      return jsonResult;
    } else {
      String[] strArr = ids;
      //保存关联信息
      for (String id : strArr) {
        AppRole appRole = this.get(Long.valueOf(id));

        List<AppUserRole> findByAppRole = appUserRoleDao.findByAppRole(appRole.getId());
        if (findByAppRole != null && findByAppRole.size() > 0) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("【" + appRole.getName() + "】角色已有账号，请先删除账号");
          return jsonResult;
        }
        //删除角色对应的权限
        QueryFilter queryFilter = new QueryFilter(AppRoleMenuTree.class);
        queryFilter.addFilter("roleId=", appRole.getId());
        appRoleMenuTreeService.delete(queryFilter);
        //删除角色
        dao.delete(appRole);
      }

      jsonResult.setSuccess(true);
      return jsonResult;
    }

  }

  @Override
  public Set<AppResource> getAppResourceSet(AppRole appRole) {
    return new HashSet<AppResource>(
        appResourceDao.findByAppRole(appRole.getId(), ContextUtil.getSaasId()));
  }

  @Override
  public JsonResult modify(HttpServletRequest request, AppRole appRole) {
    String menuTreeIds = request.getParameter("menuTreeIds");
    JsonResult jsonResult = new JsonResult();
    //判空
    if (org.apache.commons.lang3.StringUtils.isEmpty(menuTreeIds)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("权限不能为空");
      return jsonResult;
    } else {

      AppRole _appRole = this.get(appRole.getId());
      BeanUtil.copyNotNullProperties(appRole, _appRole);
      //修改角色
      this.update(_appRole);
      //删除所有的关联关系
      QueryFilter queryFilter = new QueryFilter(AppRoleMenuTree.class);
      queryFilter.addFilter("roleId=", appRole.getId());
      appRoleMenuTreeService.delete(queryFilter);
      //保存新的关联关系
      String[] ids = org.apache.commons.lang3.StringUtils.split(menuTreeIds, ",");
      for (String id : ids) {
        AppRoleMenuTree appRoleMenuTree = new AppRoleMenuTree();
        appRoleMenuTree.setRoleId(appRole.getId());
        appRoleMenuTree.setMenuTreeId(Long.valueOf(id));
        appRoleMenuTreeService.save(appRoleMenuTree);
      }

      jsonResult.setSuccess(true);
      return jsonResult;
    }

  }

  @Resource
  private AppExceptionService appExceptionService;

  @Override
  public JsonResult testAdd(HttpServletRequest request) {

    Thread thread = Thread.currentThread();
    try {
      String money = request.getParameter("money");

      //	AppRole appRole = dao.get(Long.valueOf(22));

      AppRole appRole = dao.selectByPrimaryKey(Long.valueOf(22));

      if (compare(appRole.getMoney())) {
        BigDecimal add = appRole.getMoney().add(new BigDecimal(money));
        if (compare(add)) {
          appRole.setMoney(add);
          //	dao.save(appRole);
          dao.insertSelective(appRole);

          System.out.println("扣" + money + "成功,标金额：【" + appRole.getMoney() + "】  操作个人账户--等待5秒");
          //休息5秒
          //	thread.sleep(5000);

          AppException appException = new AppException();
          appException.setName("投标成功" + DateUtil.dateToString(new Date()));
          appException.setNotes("投标成功" + DateUtil.dateToString(new Date()));
          appExceptionService.save(appException);

          JsonResult jsonResult = new JsonResult();
          jsonResult.setSuccess(true);
          jsonResult.setMsg("投标成功" + DateUtil.dateToString(new Date()));
          return jsonResult;
        } else {
          JsonResult jsonResult = new JsonResult();
          jsonResult.setSuccess(true);
          jsonResult.setMsg("投标失败" + DateUtil.dateToString(new Date()));
          return jsonResult;
        }


      } else {
        System.out.println("没抢到标,退款" + money + "--等待5秒");
        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(true);
        jsonResult.setMsg("投标失败,没抢到标" + DateUtil.dateToString(new Date()));
        return jsonResult;
        //thread.sleep(5000);
      }
    } catch (Exception e) {
    }

    JsonResult jsonResult = new JsonResult();
    jsonResult.setSuccess(true);
    jsonResult.setMsg("投标失败,没抢到标2" + DateUtil.dateToString(new Date()));
    return jsonResult;

  }

  public boolean compare(BigDecimal money) {
    if (money.compareTo(new BigDecimal("1000")) < 0) {

      Thread thread = Thread.currentThread();
      try {
        //thread.sleep(5000);
      } catch (Exception e) {
        e.printStackTrace();
      }

      return true;
    }
    return false;
  }


}
