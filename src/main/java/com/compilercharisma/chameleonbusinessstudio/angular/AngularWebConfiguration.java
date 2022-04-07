package com.compilercharisma.chameleonbusinessstudio.angular;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * https://stackoverflow.com/a/46854105
 * 
 * Since Angular serves as an SPA, yet uses URL paths to provide routing, poor
 * Spring Boot gets confused by requests to its routes! This resolves the
 * problem by changing the default behavior for handling requests to routes not
 * associated with a Spring Boot controller.
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Configuration
public class AngularWebConfiguration implements WebMvcConfigurer {

    /**
     * Configures Spring Boot with out custom static resource handler
     * 
     * @param reg handles requests to static resources
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry reg){
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/util/pattern/PathPattern.html
        reg.addResourceHandler("/*/**") // match any path.
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new AngularResourceResolver());
    }
}
