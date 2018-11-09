package com.fh.service.mall.mall_brandcategory;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;


@Service("mall_brandcategoryService")
public class Mall_brandcategoryService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("Mall_brandcategoryMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("Mall_brandcategoryMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("Mall_brandcategoryMapper.edit", pd);
	}
	
	/*
	* 修改品牌推荐
	*/
	public void editrecommend(PageData pd)throws Exception{
		dao.update("Mall_brandcategoryMapper.editrecommend", pd);
	}
	
	
	
	/*
	*列表
	*/
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("Mall_brandcategoryMapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Mall_brandcategoryMapper.listAll", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Mall_brandcategoryMapper.findById", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("Mall_brandcategoryMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

