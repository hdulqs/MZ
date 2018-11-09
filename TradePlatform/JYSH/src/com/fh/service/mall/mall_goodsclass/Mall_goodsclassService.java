package com.fh.service.mall.mall_goodsclass;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;


@Service("mall_goodsclassService")
public class Mall_goodsclassService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("Mall_goodsclassMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("Mall_goodsclassMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("Mall_goodsclassMapper.edit", pd);
	}
	
	/*
	*列表
	*/
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("Mall_goodsclassMapper.datalistPage", page);
	}
	
	/*
	*列表
	*/
	public List<PageData> ztreelist(Page page)throws Exception{
		return (List<PageData>)dao.findForList("Mall_goodsclassMapper.ztreelistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Mall_goodsclassMapper.listAll", pd);
	}
	
	/*
	*列表(查询分类)
	*/
	public List<PageData> fenleilistAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Mall_goodsclassMapper.fenleilistAll", pd);
	}
	
	
	/*
	*列表(查询等级1的分类)
	*/
	public List<PageData> level1listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Mall_goodsclassMapper.level1listAll", pd);
	}
	
	/*
	*列表(查询等级1的分类)
	*/
	public List<PageData> DATA_IDSlistAll(String[] ArrayDATA_IDS)throws Exception{
		return (List<PageData>)dao.findForList("Mall_goodsclassMapper.DATA_IDSlistAll", ArrayDATA_IDS);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Mall_goodsclassMapper.findById", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("Mall_goodsclassMapper.deleteAll", ArrayDATA_IDS);
	}
	
	
	/*
	* 修改
	*/
	public void editParent_id(PageData pd)throws Exception{
		dao.update("Mall_goodsclassMapper.editParent_id", pd);
	}
	
}

