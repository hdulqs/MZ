package com.mz.util.common.directive;

import com.mz.core.mvc.model.AppConfig;
import com.mz.util.sys.SpringContextUtil;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.model.User;
import com.mz.util.common.BaseConfUtil;
import com.mz.util.common.Constant;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Shangxl
 * @Date : 2017年7月15日 下午2:36:30
 */
@Component
public class HryTopOrFooterDirective implements TemplateDirectiveModel {

  @Autowired
  FreeMarkerConfigurer freeMarkerConfigurer;

  @Override
  public void execute(Environment env, Map map, TemplateModel[] models,
      TemplateDirectiveBody directiveBody) throws TemplateException, IOException {
    Locale locale = LocaleContextHolder.getLocale();

    Map<String, Object> root = new HashMap<String, Object>();

    /*FreeMarkerConfigurer freeMarkerConfigurer = (FreeMarkerConfigurer) SpringContextUtil.getBean("freemarkerConfig");*/
    Configuration cfg = freeMarkerConfigurer.getConfiguration();
    String url = map.get("url").toString();
    if (!StringUtils.isEmpty(url)) {
      root = BaseConfUtil.getConfigByKey(Constant.baseConfig);
    }
    if ("zh_CN".equals(locale.toString())) {
      String companyAdress = root.get("companyAdress").toString();
      root.put("companyAdress", companyAdress != null ? companyAdress.split("~")[0] : "");

      String siteCopyright = root.get("siteCopyright").toString();
      root.put("siteCopyright", siteCopyright != null ? siteCopyright.split("~")[0] : "");

      String siteName = root.get("siteName").toString();
      root.put("siteName", siteName != null ? siteName.split("~")[0] : "");
    } else if ("en".equals(locale.toString())) {
      String companyAdress = root.get("companyAdress").toString();
      root.put("companyAdress",
          companyAdress != null && companyAdress.split("~").length > 1 ? companyAdress.split("~")[1]
              : "");

      String siteCopyright = root.get("siteCopyright").toString();
      root.put("siteCopyright",
          siteCopyright != null && siteCopyright.split("~").length > 1 ? siteCopyright.split("~")[1]
              : "");

      String siteName = root.get("siteName").toString();
      root.put("siteName",
          siteCopyright != null && siteName.split("~").length > 1 ? siteName.split("~")[1] : "");
    } else {
      String companyAdress = root.get("companyAdress").toString();
      root.put("companyAdress", companyAdress != null ? companyAdress.split("~")[0] : "");

      String siteCopyright = root.get("siteCopyright").toString();
      root.put("siteCopyright", siteCopyright != null ? siteCopyright.split("~")[0] : "");

      String siteName = root.get("siteName").toString();
      root.put("siteName", siteName != null ? siteName.split("~")[0] : "");
    }

    //QQ
    String serviceQQ = root.get("serviceQQ").toString();
    root.put("serviceQQ", serviceQQ);

    Template t = cfg.getTemplate(url);
    Writer out = env.getOut();

    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
        .currentRequestAttributes()).getRequest();
    HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
        .currentRequestAttributes()).getResponse();
    if (request.getSession().getAttribute("hasico") != null) {
      root.put("hasico", request.getSession().getAttribute("hasico"));
    }
    root.put("springMacroRequestContext",
        new RequestContext(request, response, request.getServletContext(), root));
    root.put("locale", locale);
    root.put("showColor", request.getParameter("showColor"));
    User user = (User) request.getSession().getAttribute("user");
    if (user != null) {
      root.put("user", user);
    }

    //是否开启建议
    RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil
        .getBean("remoteAppTransactionManageService");
    List<AppConfig> configInfo = remoteAppTransactionManageService.getConfigInfo("isProposal");
    if (configInfo.size() == 0) {
      root.put("isProposal", "1");
    } else {
      String value = configInfo.get(0).getValue();
      root.put("isProposal", value);
    }
    //建议代码块
    List<AppConfig> write_proposal = remoteAppTransactionManageService
        .getConfigInfo("write_proposal");
    if (write_proposal.size() == 0) {
      root.put("write_proposal", "");
    } else {
      String value = write_proposal.get(0).getValue();
      root.put("write_proposal", value);
    }
    //是否开启语言切换
    List<AppConfig> isOpenLanguage = remoteAppTransactionManageService
        .getConfigInfo("isOpenLanguage");
    if (isOpenLanguage.size() == 0) {
      root.put("isOpenLanguage", "1");
    } else {
      String value = isOpenLanguage.get(0).getValue();
      root.put("isOpenLanguage", value);
    }
    t.process(root, out);
    out.flush();
  }

}
