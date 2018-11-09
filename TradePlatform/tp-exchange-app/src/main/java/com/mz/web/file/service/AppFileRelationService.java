/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Gao Mimi
 * @version: V1.0
 * @Date: 2015年09月28日  18:10:04
 */
package com.mz.web.file.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.web.file.model.AppFileRelation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2016年6月20日 下午3:35:32
 */
public interface AppFileRelationService extends BaseService<AppFileRelation, Long> {

  /**
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @return
   * @return: List<AppFileRelation>
   * @Date :          2016年6月21日 下午5:10:46
   * @throws:
   */
  List<AppFileRelation> findGroup();

  /**
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param request
   * @param:    @return
   * @return: List<AppFileRelation>
   * @Date :          2016年6月23日 上午11:45:01
   * @throws:
   */
  List<AppFileRelation> findByOrgId(HttpServletRequest request);
}


