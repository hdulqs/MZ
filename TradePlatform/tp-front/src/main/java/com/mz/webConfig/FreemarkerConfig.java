package com.mz.webConfig;

import com.mz.util.common.directive.HryTopOrFooterDirective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by Frank on 2018/8/20.
 */
@Configuration
public class FreemarkerConfig {
    @Autowired
    private freemarker.template.Configuration configuration;
    @Autowired
    HryTopOrFooterDirective hryTopOrFooterDirective;

    @PostConstruct
    public void setSharedVariable() {
        configuration.setSharedVariable("HryTopOrFooter", hryTopOrFooterDirective);
    }
}
