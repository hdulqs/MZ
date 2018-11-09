/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年4月13日 上午11:45:27
 */
package com.mz.mongo.util;

import com.mongodb.MongoTimeoutException;
import com.mz.mongo.constant.MongoDBInfo;
import com.mz.util.UUIDUtil;
import com.mz.util.reflect.ReflectUtil;
import com.mz.util.sys.ContextUtil;
import com.mz.core.mvc.model.page.PageResult;
import java.util.List;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

/**
 * <p>
 * 不通过继承调用的mongo工具类
 * </p>
 * 
 * @author: Liu Shilei
 * @Date : 2016年4月13日 上午11:45:27
 */
public class MongoUtil<T, PK> {

	private Class<T> clazz;
	private String tableName;
	public MongoTemplate mongoTemplate;

	/**
	 * 自动获得@Table name属性做为collection名称
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param clazz
	 */
	public MongoUtil(Class<T> clazz) {
		this.clazz = clazz;
		this.tableName = ReflectUtil.getTableName(clazz);
		this.mongoTemplate = (MongoTemplate) ContextUtil.getBean("mongoTemplate");
	}

	/**
	 * 自定义collection名称
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param clazz
	 * @param: @param tableName mongodb collection名称
	 */
	public MongoUtil(Class<T> clazz, String tableName) {
		this.clazz = clazz;
		this.tableName = tableName;
		this.mongoTemplate = (MongoTemplate) ContextUtil.getBean("mongoTemplate");
	}

	/**
	 * id自增长
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @return
	 * @return: Long
	 * @Date : 2016年4月18日 上午10:18:22
	 * @throws:
	 */
	public Long autoincrementId() {

		// 先查
		MongoUtil<MongoID, String> mongoUtil = new MongoUtil<MongoID, String>(MongoID.class);
		MongoQueryFilter mongoQueryFilter = new MongoQueryFilter();
		mongoQueryFilter.addFilter("tableName=", tableName);
		mongoQueryFilter.setOrderby("tableId", "desc");
		MongoID mongoID = mongoUtil.get(mongoQueryFilter);

		if (mongoID != null) {
			MongoID newMongoId = new MongoID();
			newMongoId.setId(UUIDUtil.getUUID());
			newMongoId.setTableName(tableName);
			newMongoId.setTableId(mongoID.getTableId() + 1);
			mongoUtil.save(newMongoId);
			return newMongoId.getTableId();
		} else {
			MongoID newMongoId = new MongoID();
			newMongoId.setId(UUIDUtil.getUUID());
			newMongoId.setTableName(tableName);
			newMongoId.setTableId(Long.valueOf(1));
			mongoUtil.save(newMongoId);
			return newMongoId.getTableId();
		}
	}
	
	/**
	 * 清除mongodb表
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    
	 * @return: void 
	 * @Date :          2016年10月26日 下午2:45:02   
	 * @throws:
	 */
	public void dropCollection(){
		mongoTemplate.dropCollection(this.tableName);
	}
	/**
	 * 保存 和 更新方法
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param t
	 * @return: void
	 * @Date : 2016年4月14日 上午10:56:13
	 * @throws:
	 */
	public void save(T t) {
		// 设置saasId created modified
		ReflectUtil.save(t);
		try {
			if(MongoDBInfo.mongoDbStatus){
				mongoTemplate.save(t, tableName);
			}
		} catch (DataAccessResourceFailureException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (MongoTimeoutException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据id获得对象
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param pk
	 * @param: @return
	 * @return: T
	 * @Date : 2016年4月14日 上午10:56:33
	 * @throws:
	 */
	public T get(PK pk) {
		try {
			if(MongoDBInfo.mongoDbStatus){
			return mongoTemplate.findById(pk, clazz, tableName);
			}
		} catch (DataAccessResourceFailureException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (MongoTimeoutException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 根据MongoQueryFilter 查出一个对象
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param mongoQueryFilter
	 * @param: @return
	 * @return: T
	 * @Date : 2016年4月14日 下午3:03:33
	 * @throws:
	 */
	public T get(MongoQueryFilter mongoQueryFilter) {
		try {
			if(MongoDBInfo.mongoDbStatus){
			return mongoTemplate.findOne(mongoQueryFilter.getQuery(), clazz, tableName);
			}
		} catch (DataAccessResourceFailureException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (MongoTimeoutException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询全部
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @return
	 * @return: List<T>
	 * @Date : 2016年4月14日 下午3:04:45
	 * @throws:
	 */
	public List<T> findAll() {
		try {
			if(MongoDBInfo.mongoDbStatus){
				return mongoTemplate.findAll(clazz, tableName);
			}
		} catch (DataAccessResourceFailureException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (MongoTimeoutException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 按条件查询
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param mongoQueryFilter
	 * @param: @return list
	 * @return: List<T>
	 * @Date : 2016年4月14日 下午3:06:07
	 * @throws:
	 */
	public List<T> find(MongoQueryFilter mongoQueryFilter) {
		try {
			if(MongoDBInfo.mongoDbStatus){
				return mongoTemplate.find(mongoQueryFilter.getQuery(), clazz, tableName);
			}
		} catch (DataAccessResourceFailureException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (MongoTimeoutException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	
	
	/**
	 * 根据对象删除
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param pk
	 * @param: @return
	 * @return: boolean
	 * @Date : 2016年4月14日 下午3:52:44
	 * @throws:
	 */
	public boolean deleteObject(T t) {
		try {
			if(MongoDBInfo.mongoDbStatus){
				if (t != null) {
					mongoTemplate.remove(t, tableName);
				}
				return true;
			}
		} catch (DataAccessResourceFailureException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (MongoTimeoutException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 根据id删除
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param pk
	 * @param: @return
	 * @return: boolean
	 * @Date : 2016年4月14日 下午3:52:44
	 * @throws:
	 */
	public boolean delete(PK pk) {
		try {
			if(MongoDBInfo.mongoDbStatus){
				T t = mongoTemplate.findById(pk, clazz, tableName);
				if (t != null) {
					mongoTemplate.remove(t, tableName);
				}
				return true;
			}
		} catch (DataAccessResourceFailureException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (MongoTimeoutException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据条件删除
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param mongoQueryFilter
	 * @param: @return
	 * @return: boolean
	 * @Date : 2016年4月14日 下午3:53:18
	 * @throws:
	 */
	public boolean delete(MongoQueryFilter mongoQueryFilter) {

		try {
			if(MongoDBInfo.mongoDbStatus){
				mongoTemplate.findAndRemove(mongoQueryFilter.getQuery(), clazz, tableName);
				return true;
			}
		} catch (DataAccessResourceFailureException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (MongoTimeoutException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * <p>
	 * 查询总数
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param mongoQueryFilter
	 * @param: @return
	 * @return: Long
	 * @Date : 2016年4月14日 下午5:30:22
	 * @throws:
	 */
	public Long count(MongoQueryFilter mongoQueryFilter) {
		try {
			if(MongoDBInfo.mongoDbStatus){
				return mongoTemplate.count(mongoQueryFilter.getQuery(), clazz, tableName);
			}
		} catch (DataAccessResourceFailureException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (MongoTimeoutException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询分页List
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param mongoQueryFilter
	 * @param: @return
	 * @return: List<T>
	 * @Date : 2016年4月15日 上午11:32:17
	 * @throws:
	 */
	private List<T> findPageList(MongoQueryFilter mongoQueryFilter) {
		Query query = mongoQueryFilter.getQuery();
		query.skip((mongoQueryFilter.getPage() - 1) * mongoQueryFilter.getPageSize());
		query.limit(mongoQueryFilter.getPageSize());
		try {
			if(MongoDBInfo.mongoDbStatus){
				return mongoTemplate.find(query, clazz, tableName);
			}
		} catch (DataAccessResourceFailureException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (MongoTimeoutException e) {
			MongoDBInfo.setMongoDbStatus(false);
			System.out.println("mongo连接超时");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	public PageResult findPage(MongoQueryFilter mongoQueryFilter) {

		// 复制一个mongoQueryFilter对象
		MongoQueryFilter countFilter = (MongoQueryFilter) mongoQueryFilter.clone();

		PageResult p = new PageResult();

		List<T> list = this.findPageList(mongoQueryFilter);
		Long count = this.count(countFilter);

		// 设置分页数据
		p.setRows(list);
		// 设置总记录数
		p.setRecordsTotal(count);

		p.setDraw(mongoQueryFilter.getDraw());
		p.setPage(mongoQueryFilter.getPage());
		p.setPageSize(mongoQueryFilter.getPageSize());

		return p;
	}

}
