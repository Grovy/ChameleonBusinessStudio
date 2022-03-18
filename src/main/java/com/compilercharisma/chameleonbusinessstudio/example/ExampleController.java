package com.compilercharisma.chameleonbusinessstudio.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * CONTROLLERS handle HTTP requests.
 * 
 * In this example, once you run the program on port 5000 for example. The
 * application is listening for HTTP requests on port 5000,
 *  http://localhost:5000
 * 
 * this controller handles requests to the 'example' route thanks to the 
 * RequestMapping annotation
 *  http://localhost:5000/example
 * 
 * and the getExample method of this class handles requests to the 'test' 
 * endpoint
 *  http://localhost:5000/example/test
 * 
 * You can see if it works by going to 
 *  http://localhost:5000/example/test?name=MyName
 * 
 * @author Matt Crow
 */

@Controller // "this class handles HTTP requests"
@RequestMapping(path="/example") // "handle requests to the example path"
public class ExampleController {
    private final ExampleService exampleService;
    
    /**
     * Spring looks at this ctor and thinks to itself,
     * "This guy needs an ExampleService. I'll make one for him."
     * 
     * @param exampleService the service this needs
     */
    @Autowired
    public ExampleController(ExampleService exampleService){
        this.exampleService = exampleService;
    }
    
    @GetMapping(path="/test") // handle requests to /example/test
    public @ResponseBody String getExample(@RequestParam("name") String name){
        ExampleEntity created = exampleService.createExampleEntity(name);
        return String.format("Create an ExampleEntity with ID of %d", created.getId());
    } 
}
