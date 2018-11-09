/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年8月25日 下午3:21:39
 */
package com.mz.front.ico.controller;

import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.util.BeanUtil;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.ContextUtil;
import com.mz.manage.remote.ico.RemoteIcoService;
import com.mz.manage.remote.ico.model.AppIcoProjectDTO;
import com.mz.manage.remote.ico.model.AppIcoProjectHomePageDTO;
import com.mz.manage.remote.ico.model.AppIcoProjectRepayDTO;
import com.mz.manage.remote.ico.model.AppPersonInfoDTO;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * ico业务前台方法
 * <p> TODO</p>
 * @author:         Shangxl 
 * @Date :          2017年7月14日 下午3:47:15
 */
@Controller
@RequestMapping("/iLaunchProject")
public class ILaunchProjectController {
	@Resource
	private RemoteIcoService remoteIcoService;//ico远程调用接口
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class, new DateTimePropertyEditorSupport());
		/**
		 * 防止XSS攻击，并且带去左右空格功能
		 */
		binder.registerCustomEditor(String.class, new StringPropertyEditorSupport(true, false));
	}
	
	
	/**
	 * 查询我发起的项目list
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param request
	 * @param:    @return
	 * @return: FrontPage 
	 * @Date :          2017年7月19日 下午8:25:59   
	 * @throws:
	 */
	@RequestMapping("/list")
	@ResponseBody
	public FrontPage list(HttpServletRequest request) {
		Map<String, String> params = HttpServletRequestUtils.getParams(request);
		return remoteIcoService.listProject(params);
				
	}
	
	
	@RequestMapping(value="/projectStep",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView projectStep(HttpServletRequest request){
		//使用load2div局部刷新页面到指定的地方
		ModelAndView view=new ModelAndView();
		String projectId=request.getParameter("projectId");
		String step=request.getParameter("step");
		if(StringUtils.isNotEmpty(projectId)&&StringUtils.isNotEmpty(step)){
			view.setViewName("/front/ico/step/step"+step);
			this.getProjectInfoByStep(view, step, projectId);
		}else if(StringUtils.isEmpty(projectId)&&StringUtils.isEmpty(step)){//未保存
			view.setViewName("/front/ico/step/step0");
			this.getProjectInfoByStep(view, "0", null);
		}
		return view;
	}
	
	
	/**
	 * 根据项目step获取项目相关信息
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param view
	 * @param:    @param step
	 * @param:    @param projectId
	 * @param:    @return
	 * @return: ModelAndView 
	 * @Date :          2017年7月20日 下午2:29:44   
	 * @throws:
	 */
	public ModelAndView getProjectInfoByStep(ModelAndView view,String step,String projectId){
		//获取当前登录用户
		User user=(User) ContextUtil.getRequest().getSession().getAttribute("user");
		switch (step) {
		case "0"://个人信息展示
			Map<String,String> map=new HashMap<String, String>();
			map.put("customerId=", user.getCustomerId().toString());
			List<AppPersonInfoDTO> listperson=remoteIcoService.getPersonInfo(map);
			if(listperson!=null&&listperson.size()>0){
				AppPersonInfoDTO appPersonInfoDTO=listperson.get(0);
				view.addObject("appPersonInfoDTO", appPersonInfoDTO);
			}
			break;
		case "1"://项目基本信息
			Map<String,Object> map1=new HashMap<String, Object>();
			map1.put("id=",projectId);
			AppIcoProjectDTO projectDTO=remoteIcoService.getProject(map1);
			view.addObject("project", projectDTO);
			break;
		case "2"://项目详细介绍
			Map<String,Object> map2=new HashMap<String, Object>();
			map2.put("projectId=", projectId);
			AppIcoProjectHomePageDTO homePageDTO=remoteIcoService.getProjectHomePageDTO(map2);
			view.addObject("homePageDTO", homePageDTO);
			break;
		case "3"://设置投资回报
			Map<String,String> map3=new HashMap<String, String>();
			map3.put("projectId=", projectId);
			List<AppIcoProjectRepayDTO> projectRepayDTOs=remoteIcoService.listProjectRepayDTO(map3);
			if(projectRepayDTOs!=null&&projectRepayDTOs.size()>0){
				view.addObject("projectRepay", projectRepayDTOs.get(0));
			}
			break;
		case "4"://提交审核
			Map<String,Object> map4=new HashMap<String, Object>();
			map4.put("id=",projectId);
			AppIcoProjectDTO projectDTO4=remoteIcoService.getProject(map4);
			view.addObject("project", projectDTO4);
			break;
		default:
			break;
		}
		return view;
	}
	
	/**
	 * 保存完善项目
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2017年7月20日 下午6:28:46   
	 * @throws:
	 */
	@RequestMapping(value="/saveProjectStep",method = RequestMethod.POST)
	@ResponseBody
	public JsonResult saveProjectStep(HttpServletRequest request, AppIcoProjectDTO projectDTO,AppIcoProjectRepayDTO projectRepayDTO) {
		JsonResult result = new JsonResult();
		//保存详细地址
		if (projectDTO.getStep() == 0) {
			User user = (User) ContextUtil.getRequest().getSession().getAttribute("user");
			// 属性值复制，这里直接使用set不用对象复制
			projectDTO.setWebsite(ContextUtil.getWebsite());
			projectDTO.setCustomerId(user.getCustomerId());
			projectDTO.setTrueName(user.getTruename());
			projectDTO.setLinkman(user.getTruename());
			projectDTO.setPhone(user.getMobile());
		}else if (projectDTO.getStep() == 2) {
			// 保存项目详细（主页）信息
			String projectHomePage = request.getParameter("projectHomePage");
			if (StringUtils.isNotEmpty(projectHomePage)) {
				AppIcoProjectHomePageDTO homePageDTO = new AppIcoProjectHomePageDTO();
				homePageDTO.setWebsite(ContextUtil.getWebsite());
				homePageDTO.setProjectId(projectDTO.getId());
				homePageDTO.setContent(projectHomePage);
				remoteIcoService.saveProjectHomePage(homePageDTO);
			}
		} else if (projectDTO.getStep() == 3) {
			//保存项目回报
			projectRepayDTO.setProjectId(projectDTO.getId());
			projectRepayDTO.setWebsite(ContextUtil.getWebsite());
			remoteIcoService.saveProjectRepay(projectRepayDTO);
		}
		//保存项目信息
		if (projectDTO != null &&projectDTO.getId()!=null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id=", projectDTO.getId());
			AppIcoProjectDTO oldproject = remoteIcoService.getProject(map);
			if (oldproject!=null) {
				if(oldproject.getStep()>projectDTO.getStep()){
					projectDTO.setStep(oldproject.getStep());
				}
				projectDTO=(AppIcoProjectDTO) BeanUtil.convert(oldproject, projectDTO);
			}
		}
		result.setObj(remoteIcoService.saveProjectStep(projectDTO));
		result.setSuccess(true);
		return result;
	}
	
	@RequestMapping(value="/remoteProject",method = RequestMethod.POST)
	@ResponseBody
	public JsonResult remoteProject(HttpServletRequest request){
		JsonResult jsonResult=new JsonResult();
		String projectId=request.getParameter("projectId");
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id=", projectId);
		if(StringUtils.isNotEmpty(projectId)){
			jsonResult.setSuccess(remoteIcoService.remoteProject(map));
		}
		return jsonResult;
	}
}
