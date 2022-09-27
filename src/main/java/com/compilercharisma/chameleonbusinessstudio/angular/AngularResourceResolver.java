package com.compilercharisma.chameleonbusinessstudio.angular;

import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.resource.PathResourceResolver;

import reactor.core.publisher.Mono;

/** 
 * https://www.javadoc.io/doc/org.springframework/spring-webflux/5.2.7.RELEASE/org/springframework/web/reactive/resource/PathResourceResolver.html
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class AngularResourceResolver extends PathResourceResolver {
    @Override
    protected Mono<Resource> getResource(String resourcePath, Resource location) {
        
        Resource requested = null;
        try {
            requested = location.createRelative(resourcePath); // in this case, /static/$(resourcePath)
        } catch(IOException ex){
            /*
             * superclass method signature does not contain a throws clause, so
             * this method cannot throw a check exception, so throw an unchecked
             * exception instead
             */
            throw new RuntimeException(ex);
        }
        
        if(requested.exists() && requested.isReadable()){
            /*
            this clause prevents requests for valid files from being sent to
            Angular
            */
            return Mono.just(requested);
        }
        // not a request for a file? Give it to Angular.
        return Mono.just(new ClassPathResource("/static/index.html"));
    }
}
