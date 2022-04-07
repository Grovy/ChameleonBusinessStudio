package com.compilercharisma.chameleonbusinessstudio.angular;

import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.PathResourceResolver;

/** 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class AngularResourceResolver extends PathResourceResolver {
    @Override
    protected Resource getResource(String resourcePath, Resource location) throws IOException {
        Resource requested = location.createRelative(resourcePath); // in this case, /static/$(resourcePath)
        
        if(requested.exists() && requested.isReadable()){
            /*
            this clause prevents requests for valid files from being sent to
            Angular
            */
            return requested;
        }
        // not a request for a file? Give it to Angular.
        return new ClassPathResource("/static/index.html");
    }
}
