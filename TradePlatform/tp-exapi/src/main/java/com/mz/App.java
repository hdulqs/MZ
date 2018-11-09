package com.mz;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.exapi.filter.ApiFilter;
import java.util.Locale;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
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
    "com.mz.redis.common", "com.mz.util"})
@MapperScan(basePackages = {"com.mz.**.dao"}, markerInterface = BaseDao.class)
@PropertySource({"classpath:app.properties"})
@ImportResource({"classpath*:remote/*.xml"})
public class App extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(App.class);
  }

  public static void main(String[] args) {
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
  public FilterRegistrationBean servletListenerRegistrationBean() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(
        new ApiFilter());
    filterRegistrationBean.addUrlPatterns("/api/*");
    return filterRegistrationBean;
  }
}
