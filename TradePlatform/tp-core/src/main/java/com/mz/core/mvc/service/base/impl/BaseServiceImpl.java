/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0 
 * @Date:        2015年9月16日 上午11:04:39
 */
package com.mz.core.mvc.service.base.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.util.reflect.ReflectUtil;
import com.mz.redis.common.utils.RedisUtil;
import java.io.Serializable;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 基础service实现类
 * 
 * 实现此service的类必须提供泛型dao
 * 
 * 
 * 
 * @author Yuan Zhicheng
 *
 * @param <T>
 * @param <PK>
 */
@Service("baseService")
public abstract class BaseServiceImpl<T extends Serializable, PK extends Serializable> implements BaseService<T, PK> {

	protected BaseDao<T, PK> dao;

	/**
	 * 抽象方法，需要子类提供dao
	 * 
	 * @param dao
	 */
	public abstract void setDao(BaseDao<T, PK> dao);

	@Override
	public Serializable save(T t) {
		 ReflectUtil.save(t);
		 return this.dao.insertSelective(t);//添加时使用数据默认值
	}
	
	@Override
	public void saveAll(List<T> l) {
		for(T t : l){
			save(t);
		}
	}
	@Override
	public boolean delete(PK pk){
		try {
			dao.deleteByPrimaryKey(pk);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean deleteBatch(String ids){
		try {
			if(!StringUtils.isEmpty(ids)){
				String[] split = org.apache.commons.lang3.StringUtils.split(ids, ",");
				for(String id  : split){
					dao.deleteByPrimaryKey(Long.valueOf(id));
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean deleteBatch(List<Long> ids){
		try {
			if(ids!=null&&ids.size()>0){
				for(Long id  : ids){
					dao.deleteByPrimaryKey(Long.valueOf(id));
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

	@Override
	public boolean delete(QueryFilter filter) {
		
		try {
			dao.deleteByExample(filter.getExample());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

	@Override
	public void update(T t) {
		ReflectUtil.save(t);
		this.dao.updateByPrimaryKeySelective(t);//null值不会更新
		if(
//		BaseService.App_account.equals(t.getClass().getName())||
//		BaseService.Ex_account.equals(t.getClass().getName())||
		BaseService.Ex_product.equals(t.getClass().getName())
	    ){
			RedisUtil<T> redisUtil = new RedisUtil(t.getClass());
			//默认主键为id
			redisUtil.put(t,null);
		}
	}
	

	@Override
	public void updateNull(T t) {
		ReflectUtil.save(t);
		this.dao.updateByPrimaryKey(t);//null值会更新
	}


	@Override
	public T get(PK pk) {
		return this.dao.selectByPrimaryKey(pk);
	}

	@Override
	public T get(QueryFilter filter) {
		try {
			List<T> list = dao.selectByExample(filter.getExample());
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public List<T> findAll() {
		return dao.selectAll();
	}
	
	
	@Override
	public List<T> find(QueryFilter filter) {
		return this.dao.selectByExample(filter.getExample());
	}
	

	@Override
	public Long count(T t) {
		 int count = this.dao.selectCount(t);
		 return Integer.valueOf(count).longValue();
	}
	

	@Override
	public Long count(QueryFilter filter) {
		int count = dao.selectCountByExample(filter.getExample());
		return Integer.valueOf(count).longValue();
		
	}
	
	
	
	/**  单表分页方法
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param filter
	 * @return: void 
	 * @Date :          2016年3月17日 上午10:46:29   
	 * @throws:
	 */
	public Page<T> findPage(QueryFilter filter){
		Page<T> p = null;
		if(Integer.valueOf(-1).compareTo(filter.getPageSize())==0){
			//pageSize = -1 时  
			//pageHelper传pageSize参数传0查询全部
			p = PageHelper.startPage(filter.getPage(), 0);
		}else{
			p = PageHelper.startPage(filter.getPage(), filter.getPageSize());
		}
		
		dao.selectByExample(filter.getExample());
		
		return p;
	}
	
	
	
	/**  单表分页方法 ,返回pageResult
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param filter
	 * @return: void 
	 * @Date :          2016年3月17日 上午10:46:29   
	 * @throws:
	 */
	public PageResult findPageResult(QueryFilter filter){
		PageResult p = new PageResult();
		Page<T> page = findPage(filter);
		//设置分页数据
		p.setRows(page.getResult());
		//设置总记录数
		p.setRecordsTotal(page.getTotal());
		p.setDraw(filter.getDraw());
		p.setPage(filter.getPage());
		p.setPageSize(filter.getPageSize());
		return p;
	}

}
