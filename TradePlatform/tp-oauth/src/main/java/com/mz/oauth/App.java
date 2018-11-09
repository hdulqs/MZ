package com.mz.oauth;

import com.alibaba.druid.support.http.StatViewServlet;

import com.mz.core.mvc.dao.base.BaseDao;
import net.bull.javamelody.MonitoringFilter;
import net.bull.javamelody.SessionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.util.IntrospectorCleanupListener;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by Frank on 2018/8/15.
 * <p>
 * Spring Boot入口
 */
@SpringBootApplication(scanBasePackages = {"com.mz.**.controller", "com.mz.redis.common",
    "com.mz.**.service", "com.mz.oauth.listener", "com.mz.shiro"})
@ImportResource({"classpath*:remote/*.xml", "classpath*:config/*.xml"})
@MapperScan(basePackages = {"com.mz.**.dao"}, markerInterface = BaseDao.class)
@EnableTransactionManagement
public class App extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(App.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public IntrospectorCleanupListener introspectorCleanupListener() {
    return new IntrospectorCleanupListener();
  }

  @Bean
  public RequestContextListener requestContextListener() {
    return new RequestContextListener();
  }

  @Bean
  public ServletListenerRegistrationBean<SessionListener> sessionListener() {
    ServletListenerRegistrationBean<SessionListener> slrBean = new ServletListenerRegistrationBean<>();
    slrBean.setListener(new SessionListener());
    return slrBean;
  }

  @Bean
  public FilterRegistrationBean servletListenerRegistrationBean() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(
        new MonitoringFilter());
    filterRegistrationBean.addInitParameter("monitoring-path", "/monitoring");
    filterRegistrationBean.addUrlPatterns("/*");
    return filterRegistrationBean;
  }

  @Bean
  public ServletRegistrationBean DruidStatViewServlet() {
    ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(
        new StatViewServlet(), "/druiFilterRegistrationBeand/*");
    servletRegistrationBean.addInitParameter("exclusions", "/jsLib/*");
    servletRegistrationBean.addInitParameter("sessionStatEnable", "/false/*");
    servletRegistrationBean.addInitParameter("loginUsername", "admin");
    servletRegistrationBean.addInitParameter("loginPassword", "123456");
    servletRegistrationBean.addInitParameter("resetEnable", "false");
    return servletRegistrationBean;
  }
}
