/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0 
 * @Date:        2015年9月16日 上午11:04:39
 */
package com.mz.util;

import java.sql.Connection;

import javax.sql.DataSource;

import org.nutz.dao.ConnCallback;
import org.nutz.dao.impl.DaoRunner;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * spring整合nutz的dao，让spring控制dao的事物
 * 
 * @author:      Yuan Zhicheng
 *
 */
public class SpringDaoRunner implements DaoRunner {
	@Override
	public void run(DataSource dataSource, ConnCallback callback) {
		Connection con = DataSourceUtils.getConnection(dataSource);
		try {
			callback.invoke(con);
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e);
			}
		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
	}
}
