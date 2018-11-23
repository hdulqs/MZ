package com.mz;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.front.filter.MobileLoginFilter;
import com.mz.front.filter.OauthFilter;
import com.mz.util.filter.XssFilter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by Frank on 2018/8/15.
 * <p>
 * Spring Boot入口
 */
@SpringBootApplication(scanBasePackages = {"com.mz.**.controller", "com.mz.**.service",
    "com.mz.redis.common", "com.mz.util.sys", "com.mz.util.common", "com.mz.webConfig",
    "com.mz.**.mvc", "com.mz.**.mobile"})
@PropertySource({"classpath:app.properties"})
@ImportResource({"classpath*:remote/*.xml"})
@MapperScan(basePackages = {"com.mz.**.dao"}, markerInterface = BaseDao.class)
@EnableTransactionManagement
public class App extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(App.class);
  }

  public static void main(String[] args) {
    System.setProperty("config.devtools.restart.enabled", "false");
    SpringApplication.run(App.class, args);
  }


  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
    sessionLocaleResolver.setDefaultLocale(new Locale("zh", "CN"));
    return sessionLocaleResolver;
  }

  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setBasenames("messages/message");
    messageSource.setCacheSeconds(10);
    return messageSource;
  }

  @Bean
  public CookieLocaleResolver cookieLocaleResolver() {
    return new CookieLocaleResolver();
  }

  @Bean
  public FilterRegistrationBean oauthFilter() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(new OauthFilter());
    registration.addUrlPatterns("/*");
    registration.setName("oauthFilter");
    Map<String, String> initParams = new HashMap<>();
    initParams.put("redirectPath", "/index");
    initParams.put("disabletestfilter", "N");
    registration.setInitParameters(initParams);
    return registration;
  }

  @Bean
  public FilterRegistrationBean mobileLoginFilter() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(new MobileLoginFilter());
    registration.addUrlPatterns("/*");
    registration.setName("mobileLoginFilter");
    return registration;
  }

  @Bean
  public FilterRegistrationBean xssFilter() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(new XssFilter());
    registration.addUrlPatterns("/*");
    registration.setName("XssFilter");
    return registration;
  }
}
