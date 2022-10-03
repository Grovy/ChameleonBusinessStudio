package com.compilercharisma.chameleonbusinessstudio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

/*
 * java.lang.IllegalStateException: No current ServletRequestAttributes
 * it looks like Spring data is pretty tightly coupled to Servlets, and thus
 * does not work in WebFlux
 * 
 * @EnableSpringDataWebSupport // add support for PagedResourcesAssembler
 */

@Configuration
public class PaginationConfiguration implements WebFluxConfigurer {
    
    /**
     * adds support for pagination in WebFlux
     * https://stackoverflow.com/a/64887583
     * 
     * @param config automatically supplied by Spring Boot
     */
    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer config){
        config.addCustomResolver(new ReactivePageableHandlerMethodArgumentResolver());
    }
}
