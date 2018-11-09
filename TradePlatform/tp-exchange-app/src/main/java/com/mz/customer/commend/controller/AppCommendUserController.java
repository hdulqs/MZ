/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: menwei
 * @version: V1.0
 * @Date: 2017-11-28 15:30:38
 */
package com.mz.customer.commend.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.commend.model.AppCommendUser;
import com.mz.customer.money.service.AppCommendMoneyService;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.commend.service.AppCommendUserService;
import com.mz.customer.user.service.AppCustomerService;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Copyright:   北京互融时代软件有限公司
 * @author: menwei
 * @version: V1.0
 * @Date: 2017-11-28 15:30:38
 */
@Controller
@RequestMapping("/commend/appcommenduser")
public class AppCommendUserController extends BaseController<AppCommendUser, Long> {

  @Resource(name = "appCommendUserService")
  @Override
  public void setService(BaseService<AppCommendUser, Long> service) {
    super.service = service;
  }

  @Resource
  private AppCustomerService appCustomerService;


  @Resource(name = "appCommendMoneyService")
  private AppCommendMoneyService appCommendMoneyService;

  @Resource(name = "appCommendUserService")
  private AppCommendUserService appCommendUserService;

  @MethodName(name = "查看AppCommendUser")
  @RequestMapping(value = "/see/{id}")
  @MyRequiresPermissions
  @ResponseBody
  public AppCommendUser see(@PathVariable Long id) {
    AppCommendUser appCommendUser = service.get(id);
    return appCommendUser;
  }

  @MethodName(name = "增加AppCommendUser")
  @RequestMapping(value = "/add")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request, AppCommendUser appCommendUser) {
    return super.save(appCommendUser);
  }

  @MethodName(name = "修改AppCommendUser")
  @RequestMapping(value = "/modify")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request, AppCommendUser appCommendUser) {
    return super.update(appCommendUser);
  }

  @MethodName(name = "删除AppCommendUser")
  @RequestMapping(value = "/remove/{ids}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult remove(@PathVariable String ids) {
    return super.deleteBatch(ids);
  }

  @MethodName(name = "列表AppCommendUser")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {

    QueryFilter filter = new QueryFilter(AppCommendUser.class, request);
    return super.findPage(filter);
  }

  public JsonResult addCommend(Long id) {
    JsonResult result = new JsonResult();
    AppCommendUser appCommendUser = appCommendUserService.get(id);
    if (appCommendUser != null) {
      QueryFilter queryFilter = new QueryFilter(AppCommendUser.class);
      Long pid = appCommendUser.getPid();
      queryFilter.addFilter("uid=", pid);
      AppCommendUser find = appCommendUserService.get(queryFilter);
      if (find != null) {
        BigDecimal aloneMoney = find.getAloneMoney();
        if (aloneMoney != null) {
          long longValue = aloneMoney.longValue();
          if (find.getAloneMoney() != null && longValue != 0) {
            result.setSuccess(false);
          } else {
            addCommend(find.getId());
          }
        } else {
          result.setSuccess(true);
        }
      }
      result.setSuccess(true);
    }

    return result;

  }


  @RequestMapping("/updateRewaMoney")
  @MethodName(name = "个人佣金设置")
  @ResponseBody
  public JsonResult updateRewaMoney(HttpServletRequest req) {
    JsonResult result = new JsonResult();
    Long id = Long.valueOf(req.getParameter("id"));
    BigDecimal money = new BigDecimal(req.getParameter("money"));
    AppCommendUser appCommendUser = appCommendUserService.get(id);
    if (money.compareTo(new BigDecimal(0)) == 0) {
      appCommendUser.setAloneMoney(money);
      appCommendUserService.update(appCommendUser);
      result.setSuccess(true);
      result.setMsg("设置成功");
      return result;
    }
    List<AppCommendUser> appCommendUsers = appCommendUserService
        .findByAloneMoneyIsNotZero(appCommendUser);
    if (appCommendUsers == null || appCommendUsers.size() == 0) {
      appCommendUser.setAloneMoney(money);
      appCommendUserService.update(appCommendUser);
      result.setSuccess(true);
      result.setMsg("设置成功");
    } else {
      result.setSuccess(false);
      result.setMsg("设置失败，已有上级设置");
    }

    return result;
  }
	
	
/*	int lent=1;
	//递归文本路径
	public JsonResult findPid(Long pid){  
    	JsonResult result =new JsonResult();

		QueryFilter queryFilter = new QueryFilter(AppCommendUser.class);
		queryFilter.addFilter("uid", pid);
		AppCommendUser appCommendUser = appCommendUserService.get(queryFilter);
		if(appCommendUser!=null){
		if(appCommendUser.getAloneMoney()!=null&&!"".equals(appCommendUser.getAloneMoney())){
			findPid(appCommendUser.getPid());
		}else{
			//上级已设置个人特殊佣金
			 result.setSuccess(false);
		}
		}else{
			result.setSuccess(true);
		}
		return result;
		
    }*/

  @MethodName(name = "列表AppCommendUser")
  @RequestMapping("/findLen/{id}")
  @ResponseBody
  public AppCommendUser findLen(@PathVariable String id) {
    int two = 0;
    int three = 0;
    AppCommendUser appCommendUser = new AppCommendUser();
    List<AppCommendUser> find3 = null;
	/*	QueryFilter appCommendMoney = new QueryFilter(AppCommendMoney.class);
		appCommendMoney.addFilter("id=", id);
		AppCommendMoney appuser = appCommendMoneyService.get(appCommendMoney);*/

    AppCommendUserService appCommendUserService = (AppCommendUserService) service;
    QueryFilter AppCommendUser = new QueryFilter(AppCommendUser.class);
    AppCommendUser.addFilter("id=", id);
    AppCommendUser appuser = appCommendUserService.get(AppCommendUser);
    QueryFilter appUser = new QueryFilter(AppCommendUser.class);
    appUser.addFilter("pid=", appuser.getUid());
    List<AppCommendUser> find = appCommendUserService.find(appUser);
    //AppCommendUser.setOne(find.size());
    System.out.println("o=" + find.size());
    if (find.size() > 0) {
      for (AppCommendUser appCommendUser2 : find) {
        QueryFilter AppCommendUser1 = new QueryFilter(AppCommendUser.class);
        AppCommendUser1.addFilter("pid=", appCommendUser2.getUid());
        List<AppCommendUser> find2 = appCommendUserService.find(AppCommendUser1);
        two += find2.size();

        for (AppCommendUser appCommendUser3 : find2) {
          QueryFilter AppCommendUser2 = new QueryFilter(AppCommendUser.class);
          AppCommendUser2.addFilter("pid=", appCommendUser3.getUid());
          find3 = appCommendUserService.find(AppCommendUser2);
          three += find3.size();
        }
      }
      appCommendUser.setOneNumber(find.size());
      appCommendUser.setTwoNumber(two);
      appCommendUser.setThreeNumber(three);
      int selectAll = appCommendUserService.findLen(appuser.getUid().toString());
      int conut = appCommendUserService.findLen2(appuser.getUid().toString());
      appCommendUser.setSelectAll(selectAll);
      appCommendUser.setLaterNumber(selectAll - find.size() - two - three);
      appCommendUser.setConut(conut);
      System.out.println("tt=" + two);
      System.out.println("oo=" + three);
    }
    return appCommendUser;
  }


  @MethodName(name = "冻结AppCommendUser")
  @RequestMapping(value = "/forzen/{ids}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult forzen(@PathVariable String ids) {

    JsonResult result = new JsonResult();

    QueryFilter filter = new QueryFilter(AppCommendUser.class);
    filter.addFilter("id=", ids);
    AppCommendUser appCommendUser = appCommendUserService.get(filter);
    appCommendUser.setIsFrozen(1);
    appCommendUserService.update(appCommendUser);
    result.setSuccess(true);
    return result;
  }


  @MethodName(name = "取消冻结AppCommendUser")
  @RequestMapping(value = "/noforzen/{ids}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult noforzen(@PathVariable String ids) {
    JsonResult result = new JsonResult();
    QueryFilter filter = new QueryFilter(AppCommendUser.class);
    filter.addFilter("id=", ids);
    AppCommendUser appCommendUser = appCommendUserService.get(filter);
    appCommendUser.setIsFrozen(0);
    appCommendUserService.update(appCommendUser);
    result.setSuccess(true);
    return result;
  }


  @MethodName(name = "详情列表AppCommendUser")
  @RequestMapping("/findReferees/{id}")
  @ResponseBody
  public AppCommendUser findReferees(@PathVariable String id) {
    //QueryFilter filter = new QueryFilter(App.class);
    AppCommendUser appCommendUser = new AppCommendUser();
    AppCommendUserService appCommendUserService = (AppCommendUserService) service;

    QueryFilter AppCommendUser = new QueryFilter(AppCommendUser.class);
    AppCommendUser.addFilter("id=", id);
    AppCommendUser appuser = appCommendUserService.get(AppCommendUser);
    QueryFilter appUser = new QueryFilter(AppCommendUser.class);
    appUser.addFilter("pid=", appuser.getUid());
    List<AppCommendUser> find = appCommendUserService.find(appUser);
    String username = "";
    if (find != null) {
      for (AppCommendUser appCommendUser2 : find) {
        username = username + appCommendUser2.getUsername() + "</br>";
      }
      if (username.length() > 1) {
        username = username.substring(0, username.length() - 1);
        appCommendUser.setUsername(username);
        return appCommendUser;
      }
    }
    appCommendUser.setUsername(username);
    return appCommendUser;
  }


}
