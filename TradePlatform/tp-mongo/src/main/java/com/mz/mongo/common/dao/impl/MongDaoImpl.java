package com.mz.mongo.common.dao.impl;

import com.mz.util.reflect.ReflectUtil;
import com.mz.mongo.common.dao.MongoDao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.WriteResult;

@SuppressWarnings("unchecked") 
@Component("mongoDao")
public abstract class MongDaoImpl<T,PK> implements MongoDao<T,PK> {
	
	@Resource(name ="mongoTemplate")
	public MongoTemplate mongoTemplate;
	
	private final Class<T> clazz;
	public MongDaoImpl() {
		this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@Override
	public void save(T t) {
		String tableName = ReflectUtil.getTableName(t);
		mongoTemplate.save(t, tableName);
	}
	
	@Override
	public T get(PK pk) {
		 return mongoTemplate.findById(pk, clazz);
	}
	

	// 查询所有 (根据反射到制定的po才能查询)
	@Override
	public List<T> findAll() {
		List<T> list = mongoTemplate.findAll(clazz);
		return list;
	}
	

	// 根据po的id查询一个对象 。但是要保证id不能重复
	@Override
	public T findById(Serializable id) {
		T t = (T) mongoTemplate.findById(id, clazz);
		return t;
	}
	
	/**
	 * Criteria 是用于复杂查询 
	 * 
	 * @author:         Wu Shuiming
	 * @param:    @param property
	 * @param:    @param value
	 * @param:    @return
	 * @return: List<T> 
	 * @Date :          2016年3月18日 上午10:39:02   
	 * @throws:
	 */
	public List<T> findByString(String property, String value){
		Query query = new Query();
		query.addCriteria(Criteria.where(property).is(value));
		// List<T> list = mongoTemplate.find(query, entityClass);
		return null;
	}

	/**
	 * 根据id修改某一个属性的值 。
	 * 
	 * property 参数为属性名称
	 * 
	 * value 参数为 修改后的值(但是要注意该属性的类型)
	 * 
	 */
	@Override
	public WriteResult updateTree(Serializable id , String property, Object value) {
		Query query = new Query();
		Update update = Update.update(property, value);
		query.addCriteria(Criteria.where("id").is(id));
		WriteResult result = mongoTemplate.updateFirst(query,update,clazz);
		return null;
	}



	// 删除一个指定的对象
	@Override
	public void delById(T t) {
		mongoTemplate.remove(t);
	}

	// 删除整个集合相当于删除一张表 。
	@Override
	public void dopCollection() {
		mongoTemplate.dropCollection(clazz);
	}

	//动态修改的方法 
	public void updateByproperty(Object o , Object b ,Object c){
	// 	mongoTemplate.updateFirst(new Query(where(o).is (b)), update, entityClass)
	
	//	mongoOps.updateFirst(query(where("name").is("Joe")), update("age", 35), Person.class);	
	//	  p = mongoOps.findOne(query(where("name").is("Joe")), Person.class);
	
	}


}
