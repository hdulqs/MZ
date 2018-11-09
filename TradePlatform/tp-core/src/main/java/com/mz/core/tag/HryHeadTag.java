/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月27日 下午6:12:06
 */
package com.mz.core.tag;

import com.mz.util.file.FileUtil;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年10月27日 下午6:12:06 
 */
public class HryHeadTag extends SimpleTagSupport {
	
	@Override
	public void doTag() throws JspException, IOException {
		
		String fileURL="http://47.75.200.109:6061/hurong_static/hryTag/layout/head.tag";
		String str = FileUtil.readRemoteFile2String(fileURL);
		getJspContext().getOut().print(str);
		
	}
	
	
}
