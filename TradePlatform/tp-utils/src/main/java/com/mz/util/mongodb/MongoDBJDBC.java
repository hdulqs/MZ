/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月26日 下午5:00:29
 */
package com.mz.util.mongodb;

/*
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
*/

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年10月26日 下午5:00:29 
 */
public class MongoDBJDBC {
	
	/*public static void main(String args[]){
		MongoDatabase dataBase = getDataBase("com.mz");
		MongoCollection<Document> collection = dataBase.getCollection("user");
		
		for(int i = 1 ; i <= 100 ;i++){
			Document document = new Document();
			document.append("username", "小明"+i);
			document.append("age", i);
			collection.insertOne(document);
			
			System.out.println("---------小明"+i);
		} 
		
*//*		FindIterable<Document> find = collection.find();
		find.limit(10);
		find.limit(10);
		MongoCursor<Document> iterator = find.iterator();
		while (iterator.hasNext()) {
			Document next = iterator.next();
			System.out.println(next.get("username"));
		}*//*
		
	}	
	
	*//**
	 * 获得数据库
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param dbName
	 * @param:    @return
	 * @return: MongoDatabase 
	 * @Date :          2015年10月26日 下午5:35:37   
	 * @throws:
	 *//*
	public static MongoDatabase getDataBase(String dbName){
		MongoClient mongoClient = new MongoClient("47.75.200.109", 27017);
		MongoDatabase database = mongoClient.getDatabase(dbName);
		return database;
	}*/

}
