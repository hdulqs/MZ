package com.mz.customer.area.controller;

import com.mz.area.model.Area;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.area.service.AreaService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/area")
public class AreaController extends BaseController<Area, Long> {
	@Resource(name = "areaService")
	public AreaService areaService;
	
	
	@Resource(name = "areaService")
	@Override
	public void setService(BaseService<Area, Long> service) {
		super.service = service;
	}
	
	/***  
     * 省份查询  
     * */  
    @RequestMapping("/findProvince")  
    @ResponseBody  
    public List<Area> findProvince(HttpServletRequest request) {  
    	List<Area> listDept = areaService.findProvince();  
        System.out.println(listDept);  
        return listDept;  
    }  
  
    /**  
     * 城市查询  
     * **/  
    @RequestMapping("/findCity")  
    @ResponseBody  
    public List<Area> findCity(HttpServletRequest request) {  
    	String provinceId=request.getParameter("provinceId");
    	List<Area> listCity=null;
    	if(provinceId!=null && !"".equals(provinceId)){
    		listCity = areaService.findCity(Long.valueOf(provinceId));  
    	}
		return listCity;  
    }  
  
    /**  
     * 县城查询  
     * */  
    @RequestMapping("/findCounty")  
    @ResponseBody  
    public List<Area> findCounty(HttpServletRequest request) {  
    	String cityId=request.getParameter("cityId");
    	List<Area> listCity=null;
    	if(cityId!=null && !"".equals(cityId)){
    		listCity = areaService.findCounty(Long.valueOf(cityId));  
    	}
		return listCity; 
    }  
}
