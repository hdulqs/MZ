/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年5月13日 下午1:43:55
 */
package com.mz.web.initBinder;

import com.mz.web.app.model.AppArticle;
import org.springframework.beans.propertyeditors.PropertiesEditor;

/**
 * <p> TODO</p>
 * @author:         Wu Shuiming
 * @Date :          2016年5月13日 下午1:43:55 
 */
public class StringToArtic extends PropertiesEditor {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		AppArticle article = new AppArticle();
		setValue(article);
	}

}
