package com.mz.manage;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.util.sys.AppUtils;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by Frank on 2018/8/15.
 * <p>
 * Spring Boot入口
 */
@SpringBootApplication(scanBasePackages = {"com.mz.redis.common", "com.mz.**.controller",
    "com.mz.**.service", "com.mz.**.mvc", "com.mz.util.sys", "com.mz.manage.listener",
    "com.mz.exchange.lend", "com.mz.shiro"})
@PropertySource({"classpath:app.properties"})
@ImportResource({"classpath*:remote/*.xml", "classpath*:mqapp/*.xml",
    "classpath*:rabbitmq/*.xml", "classpath*:spring/*.xml"})
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
  protected ServletContextListener listener() {
    return new ServletContextListener() {
      @Override
      public void contextInitialized(ServletContextEvent sce) {
        AppUtils.init(sce.getServletContext());
      }

      @Override
      public final void contextDestroyed(ServletContextEvent sce) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
          Driver driver = drivers.nextElement();
          try {
            DriverManager.deregisterDriver(driver);
          } catch (SQLException e) {
          }
        }
      }
    };
  }
}
