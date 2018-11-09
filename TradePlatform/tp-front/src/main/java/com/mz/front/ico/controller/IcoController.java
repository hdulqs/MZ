/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年8月25日 下午3:21:39
 */
package com.mz.front.ico.controller;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.ContextUtil;
import com.mz.util.sys.SpringContextUtil;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.ico.RemoteIcoService;
import com.mz.manage.remote.ico.model.AppIcoCoinAccountDTO;
import com.mz.manage.remote.ico.model.AppIcoEvaluateDTO;
import com.mz.manage.remote.ico.model.AppIcoProjectDTO;
import com.mz.manage.remote.ico.model.AppIcoProjectHomePageDTO;
import com.mz.manage.remote.ico.model.AppIcoProjectRepayDTO;
import com.mz.manage.remote.ico.model.AppIcoProjectSuportDTO;
import com.mz.manage.remote.ico.model.AppPersonInfoDTO;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.util.SessionUtils;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

;

/**
 * ico业务前台方法
 * <p> TODO</p>
 * @author:         Shangxl 
 * @Date :          2017年7月14日 下午3:47:15
 */
@Controller
@RequestMapping("/ico")
public class IcoController {
	@Resource
	private RemoteIcoService remoteIcoService;//ico远程调用接口
	private final static Logger log = Logger.getLogger(IcoController.class);
	
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
	
	//发起项目
	@RequestMapping(value="/publishProject")
	@ResponseBody
	public JsonResult publishProject(HttpServletRequest request,AppIcoProjectDTO project){
		JsonResult jsonResult=new JsonResult();
		
		return jsonResult;
	}
	
	//查询项目
	@RequestMapping(value="/getProject")
	public ModelAndView getProject(HttpServletRequest request,Long id){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/front/ico/temp/detail");
		Map<String,Object> map=new HashMap<String, Object>();
		String projectId=request.getParameter("id");
		if(StringUtils.isNotEmpty(projectId)){
			map.put("id=", projectId);
		}
		//项目主页
		AppIcoProjectDTO appIcoProjectDTO=remoteIcoService.getProject(map);
		if(appIcoProjectDTO!=null){
			if(appIcoProjectDTO.getStartTime().compareTo(new Date())==0 || (appIcoProjectDTO.getStartTime()).compareTo(new Date())<0){
				int days = (int) ((appIcoProjectDTO.getEndTime().getTime() - (new Date()).getTime()) / (1000*3600*24));
				appIcoProjectDTO.setDaysRemaining(days+"");
			}else{
				appIcoProjectDTO.setDistanceStartDate(((appIcoProjectDTO.getStartTime().getTime() - (new Date()).getTime()) / (1000*3600*24))+"");
			}
		}
		//项目讨论
		List<AppIcoEvaluateDTO> listeval = remoteIcoService.listEvaluate(Long.valueOf(projectId));
		modelAndView.addObject("listeval", listeval);
		
		//支持者
		Map<String,Object> mapk=new HashMap<String, Object>();
		mapk.put("projectId=", projectId);
		List<AppIcoProjectSuportDTO> listProjectSuport=remoteIcoService.listProjectSuport(map);
		modelAndView.addObject("listProjectSuport", listProjectSuport);
		
		modelAndView.addObject("appIcoProjectDTO", appIcoProjectDTO);
		return modelAndView;
	}
	
	//获取项目主页
	@RequestMapping(value="/getProjectHomePage")
	@ResponseBody
	public ModelAndView getProjectHomePage(HttpServletRequest request){
		ModelAndView view=new ModelAndView();
		String projectId=request.getParameter("projectId");
		Map<String,Object> map=new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(projectId)){
			map.put("projectId=", projectId);	
		}
		AppIcoProjectHomePageDTO homePageDTO=remoteIcoService.getProjectHomePageDTO(map);
		view.addObject("homePageDTO", homePageDTO);
		view.setViewName("/front/ico/projectHomePage");
		return view;
	}
	
	//项目支持者
	@RequestMapping(value="/listProjectSuport")
	@ResponseBody
	public JsonResult listProjectSuport(HttpServletRequest request){
		JsonResult jsonResult=new JsonResult();
		ModelAndView view=new ModelAndView();
		Map<String,Object> map=new HashMap<String, Object>();
		String projectId=request.getParameter("projectId");
		if(StringUtils.isNotEmpty(projectId)){
			map.put("projectId=", projectId);
		}
		List<AppIcoProjectSuportDTO> list=remoteIcoService.listProjectSuport(map);
		jsonResult.setObj(list);
		jsonResult.setSuccess(true);
		return jsonResult;
	}
	
	//list项目
	@RequestMapping(value="/listProject")
	public ModelAndView listProject(HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		String isDisabled = request.getParameter("isDisabled");
		//全部
		List<AppIcoProjectDTO> dtoList = dtoList("");
		if(dtoList==null){
			mv.addObject("sizeAll", 0);
		}else{
			mv.addObject("sizeAll", dtoList.size());
			if("all".equals(isDisabled)){
				mv.addObject("list", dtoList);
				mv.addObject("class", "class");
			}
		}
		
		//即将开始
		List<AppIcoProjectDTO> dtoList3 = dtoList("3");
		if(dtoList3==null){
			mv.addObject("sizeJjks", 0);
		}else{
			mv.addObject("sizeJjks", dtoList3.size());
			if("jjks".equals(isDisabled)){
				mv.addObject("list", dtoList3);
				mv.addObject("class3", "class");
			}
		}
		
		//进行中
		List<AppIcoProjectDTO> dtoList4 = dtoList("4");
		if(dtoList4==null){
			mv.addObject("sizeJxz", 0);
		}else{
			mv.addObject("sizeJxz", dtoList4.size());
			if("jxz".equals(isDisabled)){
				mv.addObject("list", dtoList4);
				mv.addObject("class4", "class");
			}
		}
		
		//已完成
		List<AppIcoProjectDTO> dtoList5 = dtoList("5");
		if(dtoList5==null){
			mv.addObject("sizeYwc", 0);
		}else{
			mv.addObject("sizeYwc", dtoList5.size());
			if("ywc".equals(isDisabled)){
				mv.addObject("list", dtoList5);
				mv.addObject("class5", "class");
			}
		}
		
		//项目库
		List<AppIcoProjectDTO> dtoListn = dtoList("");
		if(dtoListn==null){
			mv.addObject("sizeAll", 0);
		}else{
			mv.addObject("sizeXmk", dtoListn.size());
			if("xmk".equals(isDisabled)){
				mv.addObject("list", dtoListn);
				mv.addObject("classn", "class");
			}
		}
		mv.setViewName("/front/ico/temp/list");
		return mv;
	}
	
	public List<AppIcoProjectDTO> dtoList(String status){
		List<AppIcoProjectDTO> list = new ArrayList<AppIcoProjectDTO>();
		Map<String,Object> map=new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(status)){
			map.put("status=", status);
			list = remoteIcoService.browseProject(map);
		}else{
			map.put("status _in", "3,4,5");
			list = remoteIcoService.browseProject(map);
		}
		//剩余天数
		if(list!=null){
			for(AppIcoProjectDTO dto:list){
				if(dto.getStartTime().compareTo(new Date())==0 || (dto.getStartTime()).compareTo(new Date())<0){
					int days = (int) ((dto.getEndTime().getTime() - (new Date()).getTime()) / (1000*3600*24));
					dto.setDaysRemaining(days+"");
				}else{
					dto.setDistanceStartDate(((dto.getStartTime().getTime() - (new Date()).getTime()) / (1000*3600*24))+"");
				}
			}
		}
		return list;
	}
	
	/**
	 * 发布项目、修改项目
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param request
	 * @param:    @return
	 * @return: ModelAndView 
	 * @Date :          2017年7月18日 下午3:57:21   
	 * @throws:
	 */
	@RequestMapping("launch")
	@ResponseBody
	public ModelAndView launch(HttpServletRequest request){
		ModelAndView view=new ModelAndView();
		//获取当前登录用户
		User user=(User) ContextUtil.getRequest().getSession().getAttribute("user");
		if(user!=null){
			int isReal=user.getIsReal();
			//if(isReal==1){
				view.setViewName("front/ico/launch");
				String id=request.getParameter("projectId");
				if(StringUtils.isNotEmpty(id)){
					view.addObject("id", id);
					//获取项目信息
					this.getProjectInfoByStep(view,id);
				}else{
					this.getProjectInfoByStep(view,null);
				}
			}else{
				view.setViewName("front/user/realname");
			}
		/*}else{
			view.setViewName("index");
		}*/
//		view.setViewName("front/ico/temp/pay");
		return view;
	}
	
	
	
	/**
	 * 查询登录用户发起的项目
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param request
	 * @param:    @return
	 * @return: ModelAndView 
	 * @Date :          2017年7月19日 下午6:07:14   
	 * @throws:
	 */
	@RequestMapping(value="/iLaunchProject",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView iLaunchProject(HttpServletRequest request){
		ModelAndView view=new ModelAndView("/front/ico/iLaunchProject");
		return view;
	}
	
	
	/**
	 * 查询登录用户支持的项目
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param request
	 * @param:    @return
	 * @return: ModelAndView 
	 * @Date :          2017年7月19日 下午6:07:14   
	 * @throws:
	 */
	@RequestMapping(value="/iSuportProject",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView iSuportProject(HttpServletRequest request){
		ModelAndView view=new ModelAndView("/front/ico/iSuportProject");
		return view;
	}
	
	
	/**
	 * 查询登录用户分享的项目
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param request
	 * @param:    @return
	 * @return: ModelAndView 
	 * @Date :          2017年7月19日 下午6:07:14   
	 * @throws:
	 */
	@RequestMapping(value="/iShareProject",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView iShareProject(HttpServletRequest request){
		ModelAndView view=new ModelAndView("/front/ico/iShareProject");
		return view;
	}
	
	//获取项目信息
	public void getProjectInfoByStep(ModelAndView view,String projectId){
		//获取当前登录用户
		User user=(User)ContextUtil.getRequest().getSession().getAttribute("user");
		if(projectId!=null){
			Map<String, Object>  m= new HashMap<String, Object>();
			m.put("id=", projectId);
			AppIcoProjectDTO projectDTO = remoteIcoService.getProject(m);
			int step=projectDTO.getStep();
			view.addObject("step", step);
			switch (step) {
				case 4:// 提交审核（查询项目）
					view.addObject("project", projectDTO);
				case 3:// 设置投资回报
					Map<String, String> map3 = new HashMap<String, String>();
					map3.put("projectId=", projectId);
					List<AppIcoProjectRepayDTO> projectRepayDTOs = remoteIcoService.listProjectRepayDTO(map3);
					if (projectRepayDTOs != null && projectRepayDTOs.size() > 0) {
						view.addObject("projectRepay", projectRepayDTOs.get(0));
					}
				case 2:// 项目详细介绍
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.put("projectId=", projectId);
					AppIcoProjectHomePageDTO homePageDTO = remoteIcoService.getProjectHomePageDTO(map2);
					view.addObject("homePageDTO", homePageDTO);
				case 1:// 项目基本信息（查询项目）
					view.addObject("project", projectDTO);
				case 0:// 个人信息展示
					Map<String, String> map = new HashMap<String, String>();
					map.put("customerId=", user.getCustomerId().toString());
					List<AppPersonInfoDTO> listperson = remoteIcoService.getPersonInfo(map);
					if (listperson != null && listperson.size() > 0) {
						AppPersonInfoDTO appPersonInfoDTO = listperson.get(0);
						view.addObject("appPersonInfoDTO", appPersonInfoDTO);
					}
			default:
				break;
			}
		}else{
			Map<String, String> map = new HashMap<String, String>();
			map.put("customerId=", user.getCustomerId().toString());
			List<AppPersonInfoDTO> listperson = remoteIcoService.getPersonInfo(map);
			if (listperson != null && listperson.size() > 0) {
				AppPersonInfoDTO appPersonInfoDTO = listperson.get(0);
				view.addObject("appPersonInfoDTO", appPersonInfoDTO);
			}
		}
		
	}
	
	/**
	 * 订单第一步
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/payJump")
	public ModelAndView payJump(HttpServletRequest request){
		User user=(User)ContextUtil.getRequest().getSession().getAttribute("user");
		ModelAndView mv = new ModelAndView();
		String projectId = request.getParameter("projectId");
		if(user!=null){
			if(user.getIsReal()!=0){
				String getMoney = request.getParameter("getMoney");
				
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("id=", projectId);
				AppIcoProjectDTO appIcoProjectDTO=remoteIcoService.getProject(map);
				mv.addObject("appIcoProjectDTO", appIcoProjectDTO);
				mv.addObject("getMoney", getMoney);
				mv.setViewName("/front/ico/temp/pay");
			}else{
				mv.addObject("msg", "please realname");//StringEscapeUtils.escapeJava("请先进行实名认证!")
				mv.setViewName("redirect:/ico/getProject.do?id="+projectId);
			}
			return mv;
		}else{
			mv.setViewName("/login");
			return mv;
		}
	}
	/**
	 * 订单第二步
	 * @return
	 */
	@RequestMapping(value="/payJump2")
	public ModelAndView payJump2(HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		User user = SessionUtils.getUser(request);
		if(user != null){
			String projectId = request.getParameter("projectId");
			String coinType = request.getParameter("coinType");
			String money = request.getParameter("money");
			String proportions = request.getParameter("proportions");
			RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
			user = remoteManageService.selectByTel(user.getMobile());
			
			mv.addObject("user", user);
			mv.addObject("projectId", projectId);
			mv.addObject("coinType", coinType);
			mv.addObject("money", money);
			mv.addObject("proportions", proportions);
			mv.setViewName("/front/ico/temp/pay2");
		}else{
			mv.setViewName("/login");
		}
		return mv;
	}
	/**
	 * 订单第三步
	 * @return
	 */
	@RequestMapping(value="/payJump3")
	public ModelAndView payJump3(HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		User user = SessionUtils.getUser(request);
		if(user != null){
			String projectId = request.getParameter("projectId");
			String coinType = request.getParameter("coinType");
			String money = request.getParameter("money");
			String proportions = request.getParameter("proportions");
			
			AppIcoCoinAccountDTO appIcoCoinAccountDTO = remoteIcoService.appIcoCoinAccountDTO(user.getCustomerId());
			
			mv.addObject("getMoney", money);
			mv.addObject("coinType", coinType);
			mv.addObject("projectId", projectId);
			mv.addObject("proportions", proportions);
			mv.addObject("appIcoCoinAccountDTO", appIcoCoinAccountDTO);
			mv.setViewName("/front/ico/temp/pay3");
		}else{
			mv.setViewName("/login");
		}
		return mv;
	}
	
	/**
	 * 立即支付
	 * @return
	 */
	@RequestMapping(value="/immediatePayment")
	@ResponseBody
	public JsonResult immediatePayment(HttpServletRequest request){
		JsonResult js = new JsonResult();
		User user = SessionUtils.getUser(request);
		if(user != null){
			String projectId = request.getParameter("projectId");
			String money = request.getParameter("money");
			String proportions = request.getParameter("proportions");
			RemoteResult remote = remoteIcoService.immediatePayment(user, Long.valueOf(projectId), new BigDecimal(money), proportions);
			if(remote.getSuccess()){
				return js.setSuccess(true).setObj(remote.getObj());
			}else{
				return js.setSuccess(false).setMsg(remote.getMsg());
			}
		}else{
			return js.setSuccess(false).setMsg("登录超时");
		}
	}
	
	public static void main(String[] args) throws ParseException, UnsupportedEncodingException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
        Date date = format.parse("2017-08-01 11:14:48");
		int days = (int) ((new Date().getTime() - date.getTime()) / (1000*3600*24));
		//System.out.println(days);
		String str = "";
		String name = "请先进行实名认证!";
		System.out.println(StringEscapeUtils.escapeJava(name));
	}
}

