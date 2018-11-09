/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Gao Mimi
 * @version: V1.0
 * @Date: 2015年10月27日 下午6:17:27
 */
package com.mz.web.dictionary.service.impl;

import com.alibaba.fastjson.JSON;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.QueryFilter;
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import com.mz.web.app.model.AppDictionary;
import com.mz.web.cache.CacheManageCallBack;
import com.mz.web.cache.CacheManageService;
import com.mz.web.dictionary.model.AppDicMultilevel;
import com.mz.web.dictionary.model.AppDicOnelevel;
import com.mz.core.constant.CodeConstant;
import com.mz.core.constant.StringConstant;
import com.mz.web.dictionary.service.AppDicMultilevelService;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author: Gao Mimi
 * @Date :          2015年10月27日 下午6:17:27 
 */
@Service("appDicMultilevelService")
public class AppDicMultilevelServiceImpl extends BaseServiceImpl<AppDicMultilevel, Long> implements
    AppDicMultilevelService, CacheManageService {

  @Resource(name = "appDicMultilevelDao")
  @Override
  public void setDao(BaseDao<AppDicMultilevel, Long> dao) {
    super.dao = dao;
  }

  @Autowired
  private RedisService redisService;

  @Override
  public JsonResult removeDic(String id) {

    JsonResult jsonResult = new JsonResult();

    if (StringUtils.isEmpty(id)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("id不能为空");
    } else {
      diguiRemove(Long.valueOf(id));
      jsonResult.setSuccess(true);
    }

    return jsonResult;
  }

  public void diguiRemove(Long id) {
    AppDicMultilevel t = get(id);
    t.setIsOld(1);
    this.update(t);
    dicToredis(t.getpDicKey());//缓存到redis
    QueryFilter queryFilter = new QueryFilter(AppDicMultilevel.class);
    queryFilter.addFilter("pDicKey=", t.getDicKey());
    List<AppDicMultilevel> list = find(queryFilter);
    if (list != null && list.size() > 0) {
      for (AppDicMultilevel appDictionary : list) {
        diguiRemove(appDictionary.getId());
      }
    }
  }

  /* 添加数据字典
   *
   */
  @Override
  public String addDic(AppDicMultilevel appDictionary) {

    appDictionary.setIsOld(0);
    appDictionary.setLeaf(1);
    if ("root".equals(appDictionary.getpDicKey())) {
      appDictionary.setPath("root");
      appDictionary.setpDicKey("root");
      appDictionary.setIsOld(0);
      appDictionary.setOrderNo(1);
      save(appDictionary);
    } else {
      //排序
      List<AppDicMultilevel> listr = findListBypDicKey(appDictionary.getpDicKey());
      if (null != listr && listr.size() > 0) {
        for (int i = 0; i < listr.size(); i++) {
          AppDicMultilevel ado = listr.get(i);
          ado.setOrderNo(i + 1);
          super.update(ado);
        }
        appDictionary.setOrderNo(listr.size() + 1);
      } else {
        appDictionary.setOrderNo(1);
      }

      AppDicMultilevel pappDictionary = getParent(appDictionary.getpDicKey());
      appDictionary.setPath(pappDictionary.getPath() + "." + pappDictionary.getDicKey());
      pappDictionary.setLeaf(0);
      super.save(appDictionary);
      super.update(pappDictionary);

    }
    dicToredis(appDictionary.getpDicKey());//缓存到redis
    return CodeConstant.CODE_SUCCESS;
  }

  @Override
  public void dicToredis(String pDicKey) {

    QueryFilter filter = new QueryFilter(AppDicMultilevel.class);
    filter.addFilter("pDicKey=", pDicKey);
    filter.addFilter("isOld=", "0");
    filter.addFilter("saasId=", ContextUtil.getSaasId());
    filter.setOrderby("orderNo asc");
    List<AppDicMultilevel> listr = this.find(filter);
    String jsonString = JSON.toJSONString(listr);
    redisService.save("DIC:" + pDicKey, jsonString);
  }

  @Override
  public JsonResult move(String id, String type) {

    JsonResult jsonResult = new JsonResult();
    if (StringUtils.isEmpty(id)) {
      jsonResult.setSuccess(false);
    } else if (StringUtils.isEmpty(type)) {
      jsonResult.setSuccess(false);
    } else {
      AppDicMultilevel appDictionary1 = this.get(Long.valueOf(id));

      QueryFilter filter = new QueryFilter(AppDictionary.class);
      filter.addFilter("pDicKey=", appDictionary1.getpDicKey());
      if ("up".equals(type)) {
        filter.addFilter("orderNo<", appDictionary1.getOrderNo());
        filter.setOrderby("orderNo desc");
      } else {
        filter.addFilter("orderNo>", appDictionary1.getOrderNo());
        filter.setOrderby("orderNo asc");
      }
      AppDicMultilevel appDictionary2 = get(filter);

      if (appDictionary2 != null) {
        int orderNo = appDictionary1.getOrderNo();
        int orderNo2 = appDictionary2.getOrderNo();

        appDictionary1.setOrderNo(orderNo2);
        appDictionary2.setOrderNo(orderNo);
        update(appDictionary1);
        update(appDictionary2);
        jsonResult.setSuccess(true);
      }
      dicToredis(appDictionary1.getpDicKey());//缓存到redis
    }

    return jsonResult;
  }

  @Override
  public List<AppDicMultilevel> findListBypDicKey(String pDicKey) {
    QueryFilter filter = new QueryFilter(AppDictionary.class);
    filter.addFilter("isOld=", "0");
    filter.addFilter("pDickey=", pDicKey);
    filter.setOrderby("orderNo asc");
    List<AppDicMultilevel> list = super.find(filter);
    return list;
  }

  @Override
  public AppDicMultilevel getParent(String pDicKey) {
    QueryFilter qf = new QueryFilter(AppDicOnelevel.class);
    qf.addFilter("dicKey=", pDicKey);
    return super.get(qf);
  }

  @Override
  public void initCache(CacheManageCallBack callback) {
    List<AppDicMultilevel> list = this.findListBypDicKey("bank");
    String data = JSON.toJSONString(list);
    redisService.save(StringConstant.CACHE_BANK, data);
    callback.callback(AppDicMultilevelService.class, StringConstant.CACHE_BANK, "银行信息缓存");
    LogFactory.info("=====" + JSON.toJSONString(data));
  }
}
