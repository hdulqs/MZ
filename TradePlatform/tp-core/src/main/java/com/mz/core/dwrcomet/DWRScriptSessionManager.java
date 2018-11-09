/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月29日 下午4:17:38
 */
package com.mz.core.dwrcomet;

import org.directwebremoting.impl.DefaultScriptSessionManager;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年9月29日 下午4:17:38 
 */
public class DWRScriptSessionManager extends DefaultScriptSessionManager {
	
    public DWRScriptSessionManager(){
           //绑定一个ScriptSession增加销毁事件的监听器
          this.addScriptSessionListener( new DWRScriptSessionListener());
          System. out.println( "bind DWRScriptSessionListener");
    }
}
