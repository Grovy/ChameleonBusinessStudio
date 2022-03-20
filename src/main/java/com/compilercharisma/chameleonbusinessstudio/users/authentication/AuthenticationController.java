package com.compilercharisma.chameleonbusinessstudio.users.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Controller
@RequestMapping("auth")
public class AuthenticationController {
    
    /*
    we can use this to access values from application.properties 
     */
    private final Environment env;
    
    @Autowired
    public AuthenticationController(Environment env){
        this.env = env;
    }
    
    /**
     * the CrossOrigin annotation allows the Angular app to make get requests to
     * this method. We will not do a hard-coded allowed origin in the final
     * version, and I'm starting to think only Angular needs the google client
     * id anyway, so I could just store that in the Angular app instead
     * 
     * @return a JSON object containing {google_client_id: string}
     */
    @CrossOrigin(origins="http://localhost:4200")
    @GetMapping("/credentials")
    public @ResponseBody Map<String, Object> getCredentialsForAngular(){
        Map<String, Object> giveThisToNg = new HashMap<>();
        giveThisToNg.put("google_client_id", env.getProperty("google.client.id"));
        return giveThisToNg;
    }
    
    /**
     * this one is meant to be accessed via the Spring Boot port
     * 
     * @return signInWithGoogleExample.html with the credentials baked in
     */
    @GetMapping("/test")
    public @ResponseBody String test(){
        StringBuilder content = new StringBuilder();
        try(
            InputStream s = AuthenticationController.class.getResourceAsStream("/static/signInWithGoogleExample.html");
            InputStreamReader in = new InputStreamReader(s);
            BufferedReader buff = new BufferedReader(in);
        ){
            while(buff.ready()){
                content.append(buff.readLine());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return content.toString().replace(
            "$(google_client_id)", 
            env.getProperty("google.client.id"
        ));
    }
    
    /**
     * this method still needs to handle validation by passing to the Google
     * auth service
     * 
     * @param credential
     * @param csrfToken
     * 
     * @return test value for now 
     */
    @PostMapping("/login")
    public @ResponseBody String login(
        @RequestParam(name="credential") String credential,
        @RequestParam(name="g_csrf_token") String csrfToken
    ){
        StringBuilder sb = new StringBuilder();
        sb.append("{<br/>");
        sb.append(String.format("   credential: %s,<br/>", credential));
        sb.append(String.format("   csrf: %s<br/>", csrfToken));
        sb.append("}");
        return sb.toString();
    }
}
