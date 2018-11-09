/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年4月27日 下午5:58:33
 */
package hurong_mongo;

import com.mz.mongo.util.MongoUtil;
import com.mz.oauth.user.model.AppUser;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年4月27日 下午5:58:33 
 */
public class TestJava {
	
	public void test1(){
		MongoUtil<AppUser, Long> mongoUtil = new MongoUtil<AppUser, Long>(AppUser.class);
	}
}
