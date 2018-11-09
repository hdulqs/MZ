/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 15:30:38 
 */
package com.mz.customer.commend.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.commend.model.AppCommendUser;
import com.mz.customer.user.model.AppCustomer;
import com.mz.util.QueryFilter;
import com.mz.customer.commend.dao.AppCommendUserDao;
import com.mz.customer.commend.service.AppCommendUserService;
import com.mz.customer.user.service.AppCustomerService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppCommendUserService </p>
 * @author:         menwei
 * @Date :          2017-11-28 15:30:38  
 */
@Service("appCommendUserService")
public class AppCommendUserServiceImpl extends BaseServiceImpl<AppCommendUser, Long> implements AppCommendUserService{
	
	@Resource(name="appCommendUserDao")
	@Override
	public void setDao(BaseDao<AppCommendUser, Long> dao) {
		super.dao = dao;
	}
	@Resource(name="appCommendUserDao")
	private AppCommendUserDao appCommendUserDao;
	
	@Resource(name="appCustomerService")
	public AppCustomerService appCustomerService;
	@Resource(name="appCommendUserService")
	public AppCommendUserService appCommendUserService;
	
	StringBuffer s = new StringBuffer();
	@Override
	public void saveObj(AppCustomer customer) {
		s = new StringBuffer("");
		AppCustomer AppCustomer =null;
		QueryFilter filter = new QueryFilter(AppCustomer.class);
		//filter.addFilter("referralCode=", customer.getCommendCode()); 
		if(customer.getCommendCode()!=null&&!customer.getCommendCode().equals("")){
		//filter.addFilter("commendCode=", customer.getReferralCode()); 
			filter.addFilter("referralCode=", customer.getCommendCode()); 
		 AppCustomer = appCustomerService.get(filter);
		
		if(AppCustomer!=null){
		AppCustomer.setOneNumber(AppCustomer.getOneNumber()+1);
		}
		addSid(AppCustomer);
		}
		if(s==null||s.equals("")){
			AppCommendUser appCommend=new AppCommendUser();
			appCommend.setUid(customer.getId());
			appCommend.setUsername(customer.getUserName());
			appCommend.setPname(AppCustomer.getUserName());
			appCommend.setPid(AppCustomer.getId());
			appCommend.setIsFrozen(0);
			appCommend.setReceCode(customer.getReferralCode());
			s.toString().lastIndexOf(",", 1);
			System.out.println(AppCustomer.getId());
			appCommendUserService.save(appCommend);
		}else{
			QueryFilter filteru = new QueryFilter(AppCommendUser.class);
			filteru.addFilter("uid=", customer.getId());
			AppCommendUser appCommendUser = appCommendUserService.get(filteru);
			if(appCommendUser!=null){
				appCommendUser.setSid(s.toString().substring(0, s.length()-1));
				String str = s.toString().substring(0, s.length()-1);
				appCommendUser.setSid(str);
				int one = str.lastIndexOf(",");
				String maxid = str.substring((one+1),str.length());
				appCommendUser.setMaxId(Long.valueOf(maxid));
				appCommendUserService.update(appCommendUser);
			}else{
				AppCommendUser appcommend=new AppCommendUser();
				if(s.toString()!=null&&!s.toString().equals("")){
				String str = s.toString().substring(0, s.length()-1);
				appcommend.setSid(str);
				int one = str.lastIndexOf(",");
				String maxid = str.substring((one+1),str.length());
				appcommend.setMaxId(Long.valueOf(maxid));
				}
				appcommend.setUid(customer.getId());
				appcommend.setUsername(customer.getUserName());
				appcommend.setIsFrozen(0);
				appcommend.setReceCode(customer.getReferralCode());
				if(AppCustomer!=null){
				appcommend.setPname(AppCustomer.getUserName());
				appcommend.setPid(AppCustomer.getId());
				}
				appCommendUserService.save(appcommend);
			}
		}
		
	}
	
	//递归文本路径
	public String addSid(AppCustomer customer){  
		AppCustomer appCustomer = null;
        if(customer!=null){  
        	//s.insert(0, customer.getId()+",");
        	s.append( customer.getId()+",");
         }else{
        	 return null;
         }
    	QueryFilter filter = new QueryFilter(AppCustomer.class);
    	if(customer.getCommendCode()!=null&&!"".equals(customer.getCommendCode())){
    		
    		filter.addFilter("referralCode=", customer.getCommendCode()); 		
    		 appCustomer = appCustomerService.get(filter);
    	}else{
    		return null;
    	}
        return  addSid(appCustomer);  
        
    }  
	
	
	
	@Override
	public List<AppCommendUser> saveAppTradeFactor(AppCustomer buyCustomer, BigDecimal transactionBuyFee,
			String fixPriceCoinCode, Integer fixPriceType) {
		// TODO Auto-generated method stub
		return null;
	}
/*	public static void main(String[] args) {
		StringBuffer s=new StringBuffer();
		String s2="13333,";
		  System.out.println(s2.substring(0, s2.length()-1));
	}*/

	@Override
	public int findLen(String  id) {
		// TODO Auto-generated method stub
		return appCommendUserDao.findLen(id);
	}

	@Override
	public JsonResult forzen(String ids) {
		// TODO Auto-generated method stub
		return appCommendUserDao.forzen(ids);
	}

	@Override
	public JsonResult noforzen(String ids) {
		// TODO Auto-generated method stub
		return appCommendUserDao.noforzen(ids);
	}

	@Override
	public List<AppCommendUser> findLikeBySid(String pid) {
		Map<String,String> map = new HashMap<>();
		map.put("pid1",pid);
		map.put("pid2",pid);
		return appCommendUserDao.findLikeBySid(map);
	}

	@Override
	public List<AppCommendUser> findByAloneMoneyIsNotZero(AppCommendUser appCommendUser) {
        String lastSid;
        String sid = appCommendUser.getSid();
        if(sid!=null){
            String[] sids = sid.split(",");
            lastSid = sids[sids.length-1];
        }else{
            lastSid = appCommendUser.getUid().toString();
        }
        Map<String,String> map = new HashMap<>();
        map.put("uid",appCommendUser.getUid().toString());
        map.put("sid",lastSid);
        return appCommendUserDao.findByAloneMoneyIsNotZero(map);
	}


	public static void main(String[] args) {
		String s="1,2,3,4,";
		String str = s.toString().substring(0, s.length()-1);
		int one = str.lastIndexOf(",");
		String maxid = str.substring((one+1),str.length());
		System.out.println(str.substring((one+1),str.length()));
	}

	//新增方法
	@Override
	public int findLen2(String sid) {
		// TODO Auto-generated method stub
		return appCommendUserDao.findLen2(sid);
	}
}
