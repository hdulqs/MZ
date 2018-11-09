package com.mz.front.user.entrust.controller;

import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.Entrust;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.trade.redis.model.EntrustByUser;
import com.mz.trade.redis.model.EntrustTrade;
import com.mz.util.SessionUtils;
import com.mz.util.SortList;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.SpringContextUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/user/entrust")
public class EntrustController {

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
	 * 查询交易记录
	 * 
	 * @return
	 */
	@RequestMapping("list")
	@ResponseBody
	public FrontPage list(HttpServletRequest request) {

		String querypath = request.getParameter("querypath");

		// 交易类型
		String typeone = request.getParameter("typeone");
		User user = SessionUtils.getUser(request);
//		if (user.getIsReal() != 1) {
//			return new FrontPage(null, 0, 1, 5);
//		}
		RemoteManageService service = SpringContextUtil.getBean("remoteManageService");
		Map<String, String> params = HttpServletRequestUtils.getParams(request);
		// 全部
		if ("0".equals(typeone)) {
			params.put("typeone", null);
		}
		params.put("customerId", user.getCustomerId().toString());
		if (!"center".equals(querypath)) {// 如果是个人中心查询，加上coinCode查询
			String coinCode = request.getParameter("coinCode");
			if (StringUtils.isEmpty(coinCode) || !coinCode.contains("_")) {
				return null;
			}
			if (!StringUtils.isEmpty(coinCode)) {
				String[] split = coinCode.split("_");
				params.put("coinCode", split[0]);
				params.put("fixPriceCoinCode", split[1]);
			}
		}

		FrontPage findTrades = service.findEntrust(params);
		List<Entrust> list = findTrades.getRows();
		for(int i=0;i<list.size();i++){
			Entrust entrust = list.get(i);
			entrust.setCoin(list.get(i).getCoin());
			entrust.setEntrustTime_long(entrust.getEntrustTime().getTime());
			entrust.setCoinCode(list.get(i).getCoinCode() + "-" + list.get(i).getFixPriceCoinCode());
		}
		return findTrades;
	}

	/**
	 * 查询redis中的交易记录
	 * 
	 * @return
	 */
	@RequestMapping("rlist")
	@ResponseBody
	public List<EntrustTrade> rlist(HttpServletRequest request) {

		String type = request.getParameter("type");
		String coinCode = request.getParameter("coinCode");

		// 交易类型
		User user = SessionUtils.getUser(request);

		RedisUtil<EntrustByUser> redisUtil = new RedisUtil<EntrustByUser>(EntrustByUser.class);
		EntrustByUser entrustByUser = redisUtil.get(user.getCustomerId().toString());
		if (entrustByUser != null) {
			
			if ("current".equals(type)) {
				Map<String, List<EntrustTrade>> entrustingmap = entrustByUser.getEntrustingmap();
				if (entrustingmap != null) {
					List<EntrustTrade> list = entrustingmap.get(coinCode);
					SortList<EntrustTrade> sort = new SortList<EntrustTrade>();
					if (list != null) {
						
						for(EntrustTrade et:list){
							et.setEntrustTime_long(et.getEntrustTime().getTime());
						}
						
						sort.SortStringDate(list, "getEntrustTime", "desc");
					}
					return list;
				}
			} else {
				Map<String, List<EntrustTrade>> entrustedmap = entrustByUser.getEntrustedmap();
				if (entrustedmap != null) {
					List<EntrustTrade> list = entrustedmap.get(coinCode);
					SortList<EntrustTrade> sort = new SortList<EntrustTrade>();
					if (list != null) {
						for(EntrustTrade et:list){
							et.setEntrustTime_long(et.getEntrustTime().getTime());
						}
						sort.SortStringDate(list, "getEntrustTime", "desc");
					}
					return list;
				}
			}
		}
		return null;
	}

}
