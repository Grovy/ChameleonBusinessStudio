package com.compilercharisma.chameleonbusinessstudio;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class allows us to forward requests to the Angular application
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AngularWebConfiguration implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("forward:index.html");
    }
}
