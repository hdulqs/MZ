/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0 
 * @Date:        2015年9月16日 上午11:04:39
 */
package com.mz.core.mvc.controller.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.mz.core.mvc.service.log.AppExceptionService;
import com.mz.core.constant.Tip;
import com.mz.core.mvc.model.log.AppException;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.JsonPropertyFilter;
import com.mz.util.QueryFilter;
import com.mz.util.date.DateUtil;
import com.mz.util.httpRequest.IpUtil;
import com.mz.util.log.LogFactory;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.ContextUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.UnauthorizedException;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.mapl.Mapl;
import org.springframework.http.HttpStatus;
import org.springframework.orm.hibernate4.HibernateOptimisticLockingFailureException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * 基础控制器
 * 
 * 其他控制器继承此控制器获得日期字段类型转换和防止XSS攻击的功能
 * 
 * 此类还有一个序列化json的方法，可以返回你需要的属性
 * 
 * 
 * 
 * @author Yuan Zhicheng
 *
 */
public abstract class BaseController<T extends Serializable, PK extends Serializable> {

	public String[] getName() {
		return new String[] { "a" };
	}

	private final Class<T> entityClass;

	protected BaseService<T, PK> service;

	public abstract void setService(BaseService<T, PK> service);

	// 视图前缀
	private String viewPrefix;

	public String getViewPrefix() {
		return viewPrefix;
	}

	@SuppressWarnings("unchecked")
	public BaseController() {
		this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * 注册类型属性编辑器
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {

		// 系统注入的只能是基本类型，如int，char，String

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
	 * 权限异常拦截 返回到没有权限页面
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param request
	 * @param: @param e
	 * @param: @return
	 * @return: ModelAndView
	 * @Date : 2015年9月21日 下午4:58:26
	 * @throws:
	 */
	@ExceptionHandler({ UnauthorizedException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ModelAndView processUnauthenticatedException(HttpServletRequest request, HttpServletResponse response, UnauthorizedException exception) {

		// 获取请求头
		String requestType = request.getHeader("X-Requested-With");
		// 根据请求头区分是同步请求还是异步请求 XMLHttpRequest
		// 异步请求
		if (true) {
			PrintWriter writer = null;
			try {
				response.setHeader("Content-Type", "text/html;charset=UTF-8");
				writer = response.getWriter();
				writer.write("{\"success\":false,\"msg\":\"对不起你没有权限 \"}");
				// writer.print("<script>alert('对不起你没有权限')</script>");
				writer.flush();
				writer.close();
			} catch (Exception e) {
				writer.close();
				e.printStackTrace();
			} finally {
				if (writer != null) {
					writer.close();
				}
			}
		}
		return null;

		// return mv;
	}

	@ExceptionHandler({ HibernateOptimisticLockingFailureException.class })
	@ResponseBody
	public JsonResult hightConcurrenceException(HttpServletRequest request, HttpServletResponse response, Exception exception) {

		System.out.println("异常捕获");
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		System.out.println(sw.toString().substring(0, sw.toString().indexOf("Exception")));

		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(false);
		jsonResult.setMsg("退款成功：异常返回时间：" + DateUtil.dateToString(new Date()) + sw.toString().substring(0, sw.toString().indexOf("Exception")));
		return jsonResult;

	}

	/**
	 * 异常处理
	 * 
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler
	@ResponseBody
	public JsonResult exception(HttpServletRequest request, HttpServletResponse response, Exception exception) {

		exception.printStackTrace();
		LogFactory.error("异常捕获");
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);

		JsonResult jsonResult = new JsonResult();
		exception.printStackTrace(pw);

		AppException exceptionLog = new AppException();
		//异常名称
		exceptionLog.setName(sw.toString().substring(0, sw.toString().indexOf("Exception")));
		//异常内容
		exceptionLog.setNotes(sw.toString());
		//异常ip
		exceptionLog.setIp(IpUtil.getIp(request));
		//异常请求方法
		exceptionLog.setRequestmethod(request.getMethod());

		Enumeration enu = request.getParameterNames();
		StringBuffer sb = new StringBuffer("");
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			sb.append(paraName + "=【" + request.getParameter(paraName) + "】  ");
		}

		exceptionLog.setParameter(sb.toString());
		exceptionLog.setRequestaddress(request.getRequestURL().toString());

		// 获取请求头 
		String requestType = request.getHeader("X-Requested-With");
		// 根据请求头区分是同步请求还是异步请求 XMLHttpRequest
		// 异步请求
		if (requestType != null) {
			exceptionLog.setType("async");
		}{
			exceptionLog.setType("sync");
			request.setAttribute("error", sw.toString());
		}
		AppExceptionService appExceptionService=(AppExceptionService) ContextUtil.getBean("appExceptionService");
		appExceptionService.save(exceptionLog);
//		LogFactory.error("记录异常日志到mongo中");
//		LogFactory.error("异常名称：【"+exceptionLog.getName()+"】");
//		MongoUtil<AppException, Long>  mongoUtil = new MongoUtil<AppException, Long>(AppException.class);
//		exceptionLog.setId(mongoUtil.autoincrementId());
//		mongoUtil.save(exceptionLog);
		
		jsonResult.setSuccess(false);
		jsonResult.setMsg("异常名称:【" + exceptionLog.getName()+"】");
		return jsonResult;
	}

	/**
	 * 根据ID删除一条记录
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param pk
	 * @param: @return
	 * @return: JsonResult
	 * @Date : 2016年3月18日 下午6:28:48
	 * @throws:
	 */
	public JsonResult delete(PK pk) {
		JsonResult j = new JsonResult();
		boolean delete = service.delete(pk);
		if (delete) {
			j.setSuccess(true);
			j.setMsg(Tip.SUCCESS_DELETE);
		} else {
			j.setSuccess(false);
			j.setMsg(Tip.FAILED_DELETE);
		}
		return j;

	}

	/**
	 * 支持批量/单条删除
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param filter
	 * @param: @return
	 * @return: JsonResult
	 * @Date : 2015年10月12日 下午2:56:13
	 * @throws:
	 */
	public JsonResult delete(QueryFilter filter) {
		JsonResult j = new JsonResult();
		boolean delete = service.delete(filter);
		if (delete) {
			j.setSuccess(true);
			j.setMsg(Tip.SUCCESS_DELETE);
		} else {
			j.setSuccess(false);
			j.setMsg(Tip.FAILED_DELETE);
		}
		return j;
	}

	/**
	 * ------------------------------------------------------------------------
	 * -------------------------------
	 *
	 * 根据条件返回一个对象
	 * 
	 * @author: Liu Shilei
	 * @param: @param filter
	 * @param: @return
	 * @return: JsonResult
	 * @Date : 2016年3月17日 下午4:14:28
	 * @throws:
	 */
	public JsonResult get(QueryFilter filter) {
		JsonResult j = new JsonResult();
		try {
			T t = service.get(filter);
			j.setSuccess(true);
			j.setObj(t);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg(Tip.FAILED_SELECT);
		}

		return j;
	}

	/**
	 * 根据主键返回一个对象,封装成JsonResult
	 * 
	 * @author: Liu Shilei
	 * @param: @param valueOf
	 * @return: void
	 * @Date : 2016年3月18日 下午5:16:44
	 * @throws:
	 */
	public JsonResult get(PK pk) {
		JsonResult j = new JsonResult();
		try {
			T t = service.get(pk);
			j.setSuccess(true);
			j.setObj(t);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg(Tip.FAILED_SELECT);
		}
		return j;
	}

	/**
	 * 更新对象
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param t
	 * @param: @return
	 * @return: JsonResult
	 * @Date : 2016年3月17日 下午4:56:18
	 * @throws:
	 */
	public JsonResult update(T t) {
		JsonResult j = new JsonResult();
		try {
			service.update(t);
			j.setSuccess(true);
			j.setObj(t);
			j.setMsg(Tip.SUCCESS_UPDATE);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg(Tip.SUCCESS_UPDATE);
		}
		return j;
	}

	/**
	 * <p>
	 * 正常保存
	 * </p>
	 * 
	 * @author: Yuan Zhicheng
	 * @param: @param t
	 * @param: @return
	 * @return: JsonResult
	 * @Date : 2015年11月11日 下午7:30:46
	 * @throws:
	 */
	public JsonResult save(T t) {
		JsonResult j = new JsonResult();
		try {
			service.save(t);
			j.setMsg(Tip.SUCCESS_SAVE);
			j.setSuccess(true);
			j.setObj(t);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(Tip.FAILED_SAVE);
			j.setSuccess(false);
		}
		return j;
	}

	/**
	 * 单表分页方法
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param filter
	 * @param: @return
	 * @return: PageResult
	 * @Date : 2016年3月17日 上午10:44:51
	 * @throws:
	 */
	public PageResult findPage(QueryFilter filter) {

		PageResult p = new PageResult();

		Page<T> page = service.findPage(filter);

		// 设置分页数据
		p.setRows(page.getResult());
		// 设置总记录数
		p.setRecordsTotal(page.getTotal());

		p.setDraw(filter.getDraw());
		p.setPage(filter.getPage());
		p.setPageSize(filter.getPageSize());

		return p;
	}

	/**
	 * 查询list
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param filter
	 * @param: @return
	 * @return: List<T>
	 * @Date : 2016年3月17日 下午4:19:30
	 * @throws:
	 */
	public List<T> find(QueryFilter filter) {
		return service.find(filter);
	}

	/**
	 * 查询全部返回list
	 * 
	 * @author: Liu Shilei
	 * @param:
	 * @return: void
	 * @Date : 2016年3月18日 下午6:25:12
	 * @throws:
	 */
	public List<T> findAll() {
		return service.findAll();
	}

	/**
	 * 序列化json并返回到前台
	 * 
	 * @param object
	 * @param includesProperties
	 *            要序列化的属性
	 * @param response
	 */
	public void returnJsonByIncludeProperties(Object object, String[] includesProperties, HttpServletResponse response) {
		try {
			JsonPropertyFilter filter = new JsonPropertyFilter();// 用了这个过滤器，妈妈再也不用担心序列化JSON时hibernate懒加载异常了
			// System.out.println("对象转JSON：要排除的属性[" + excludesProperties +
			// "]要包含的属性[" + includesProperties + "]");
			String json;
			// 使用SerializerFeature.WriteDateUseDateFormat特性来序列化日期格式的类型为yyyy-MM-dd
			// HH24:mi:ss
			// 使用SerializerFeature.DisableCircularReferenceDetect特性关闭引用检测和生成
			json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);

			// 使用SerializerFeature.BrowserCompatible特性会把所有的中文都会序列化为\\uXXXX这种格式，字节数会多一些，但是能兼容IE6
			// json = JSON.toJSONString(object, filter,
			// SerializerFeature.WriteDateUseDateFormat,
			// SerializerFeature.DisableCircularReferenceDetect,
			// SerializerFeature.BrowserCompatible);
			// System.out.println("转换后的JSON字符串：" + json);

			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(Json.toJson(Mapl.includeFilter(Json.fromJson(json), Lang.array2list(includesProperties))));
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>批量删除</p>
	 * @author:         Liu Shilei
	 * @param:    @param ids
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2016年6月6日 下午5:59:33   
	 * @throws:
	 */
	public JsonResult deleteBatch(String ids) {

		JsonResult j = new JsonResult();
		boolean delete = service.deleteBatch(ids);
		if (delete) {
			j.setSuccess(true);
			j.setMsg(Tip.SUCCESS_DELETE);
		} else {
			j.setSuccess(false);
			j.setMsg(Tip.FAILED_DELETE);
		}
		return j;
	}
	
	
	/**
	 * <p>批量删除</p>
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param ids
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2016年6月16日 下午7:10:59   
	 * @throws:
	 */
	public JsonResult deleteBatch(List<Long> ids) {

		JsonResult j = new JsonResult();
		boolean delete = service.deleteBatch(ids);
		if (delete) {
			j.setSuccess(true);
			j.setMsg(Tip.SUCCESS_DELETE);
		} else {
			j.setSuccess(false);
			j.setMsg(Tip.FAILED_DELETE);
		}
		return j;
	}

}
