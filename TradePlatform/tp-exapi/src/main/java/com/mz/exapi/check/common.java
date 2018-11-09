/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2016年5月12日 下午1:53:35
 */
package com.mz.exapi.check;

import javax.servlet.http.HttpServletRequest;

import tk.mybatis.mapper.util.StringUtil;
import com.mz.core.constant.CodeConstant;
import com.mz.exapi.constant.APICodeConstant;
import com.mz.exapi.util.APIBaseModel;
import com.mz.exapi.util.ModelValidate;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年5月12日 下午1:53:35 
 */
public class common {
	public static  String[] check(Object object,String selfmethod,HttpServletRequest request){
		   APIBaseModel aPIBaseModel=(APIBaseModel)object;
	    String[] relt=new String[2]; 
  		String method=aPIBaseModel.getMethod();
  		//（1）方法调用对错的检查
  		relt=common.commoncheck(method, selfmethod);
  		if(relt[0].equals(CodeConstant.CODE_FAILED)){
  			return relt;
  		}
		//（2）一般性检查，空，长度，格式之类的检查
		String result = ModelValidate.validateModel(object);// 返回的验证结果，验证结果就是一个字符串，如果有错误的话则该字符串的长度大于0  
		if(!StringUtil.isEmpty(result)){
			relt[0]=CodeConstant.CODE_FAILED;
			relt[1]=APICodeConstant.CODE_Error_Tips;
  			return relt;
  		}
		//（3）身份检验
        //relt=SignData.sign(object, request);
        return relt;
	}
	
	public static String[] commoncheck(String method,String selfmethod){
		String[] relt=new String[2]; 
		if(null==method){
			relt[0]=CodeConstant.CODE_FAILED;
			relt[1]=APICodeConstant.CODE_Error_Tips;
			return relt;
			
		}else{
			if(!method.equals(selfmethod)){
				relt[0]=CodeConstant.CODE_FAILED;
				relt[1]=APICodeConstant.CODE_Method_Error;
				return relt;
			}
			
		}
			
			relt[0]=CodeConstant.CODE_SUCCESS;
			relt[1]=APICodeConstant.CODE_SUCCESS;
			return relt;
		}
	}

