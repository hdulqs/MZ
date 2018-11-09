package com.mz.core.util.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @description:
 * @author denghuifan
 * @create_time：2017年7月17日17:58:00
 * @version V1.0.0
 *
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
// Loads the config beans required by the framework
@ComponentScan(basePackages = {"com.mz.front.mobile"})
public class MySwaggerConfig extends WebMvcConfigurationSupport
{
	
	 @Bean
    public Docket customDocket() {
        //
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mz.front.mobile"))
                .paths(PathSelectors.any())
                .build();
    }

    //private SpringSwaggerConfig springSwaggerConfig;

    /**
     * Required to autowire SpringSwaggerConfig
     */
    @Autowired
   /* public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig)
    {
        this.springSwaggerConfig = springSwaggerConfig;
    }*/

    /**
     * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc
     * framework - allowing for multiple swagger groups i.e. same code base
     * multiple swagger resource listings.
     */
    /*@Bean
    public SwaggerSpringMvcPlugin customImplementation()
    {
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo()).includePatterns(".*?");
    }*/

    private ApiInfo apiInfo()
    {
    	return new ApiInfoBuilder()  
                .title("Spring 中使用Swagger2构建RESTful APIs")  
                .termsOfServiceUrl("http://blog.csdn.net/he90227")  
                .contact("denghf")  
                .version("1.1")  
                .build();  
        /*return new ApiInfo("API接口管理",//大标题 title
                "Blog前台API接口",//小标题
                "0.0.1",//版本
                "www.fangshuoit.com",//termsOfServiceUrl
                contact,//作者
                "Blog",//链接显示文字
                "https://cc520.me"//网站链接
        );*/
        //return apiInfo;
    }
}