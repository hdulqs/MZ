/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月27日 下午6:12:06
 */
package com.mz.core.tag;

import com.mz.util.sys.ContextUtil;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年10月27日 下午6:12:06 
 */
public class HryTokenTag extends SimpleTagSupport {
	
	@Override
	public void doTag() throws JspException, IOException {
		String uuid = UUID.randomUUID().toString();
		ContextUtil.getRequest().getSession().setAttribute("token", uuid);
		getJspContext().getOut().print("<input  type=\"hidden\" name=\"token\" value=\""+uuid+"\" />");
	}
	
	
}
