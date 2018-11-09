package com.mz;


import com.mz.core.mvc.dao.base.BaseDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by Frank on 2018/8/15.
 * <p>
 * Spring Boot入口
 */
@SpringBootApplication(scanBasePackages = {"com.mz.**.controller", "com.mz.redis.common",
    "com.mz.**.service","com.mz.**.mvc"})
@MapperScan(basePackages = {"com.mz.**.dao"}, markerInterface = BaseDao.class)
@ImportResource("classpath*:remote/*.xml")
@EnableTransactionManagement
public class App extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(App.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
