package com.mz.webConfig;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by Frank on 2018/8/20.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

  /**
   * 指定FastJson为JSON处理器
   */
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    super.configureMessageConverters(converters);
    FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
    fastConverter.setFastJsonConfig(fastJsonConfig);
    converters.add(fastConverter);
  }

  /**
   * 静态资源配置
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**")
        .addResourceLocations("file:static/")
        .addResourceLocations("classpath:/static/");
    super.addResourceHandlers(registry);
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("forward:/index");
    registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    super.addViewControllers(registry);
  }
}
