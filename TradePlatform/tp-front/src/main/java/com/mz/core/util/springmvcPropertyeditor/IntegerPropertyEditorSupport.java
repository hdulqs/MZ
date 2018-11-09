/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2015年9月16日 下午5:26:49
 */
package com.mz.core.util.springmvcPropertyeditor;

import java.beans.PropertyEditorSupport;

/**
 * <p> 转换前台的字符串格式的Integer值给后台使用</p>
 * @author:         Gao Mimi 
 * @Date :          2015年9月16日 下午5:26:49 
 */
public class IntegerPropertyEditorSupport extends PropertyEditorSupport {

	 public String getAsText() {
	  Integer value = (Integer) getValue();
	  if(null == value){
	   value = new Integer(0);
	  }
	  return value.toString();
	 }

	 public void setAsText(String text) throws IllegalArgumentException {
	  Integer value = null;
	  if(null != text && !text.equals("")){
	   value = Integer.valueOf(text);
	  }
	  setValue(value);
	 }
	}
