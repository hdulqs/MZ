/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0 
 * @Date:        2015年9月16日 上午11:04:39
 */
package com.mz.core.mvc.controller.generator;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 代码生成控制器
 * 
 * 
 * 
 * @author Yuan Zhicheng
 *
 */
@Controller
@RequestMapping("/generatorController")
public class GeneratorController {

	@MethodName(name = "生成项目文件")
	@RequestMapping("/generator")
	@ResponseBody
	public JsonResult generator(String generatorPath, String modelPath, String daoPackageName, String servicePackageName, String controllerPackageName) {
		JsonResult j = new JsonResult();
		try {
			/*Boolean b = new GeneratorUtil(generatorPath, modelPath, daoPackageName, servicePackageName, controllerPackageName).generator();
			if (b) {
				j.setMsg("生成完毕，请查看【" + generatorPath + "】目录");
				j.setSuccess(true);
			} else {
				j.setMsg("生成失败！请再次确认【" + modelPath + "】目录是否正确");
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("生成失败");
		}
		return j;
	}
}
