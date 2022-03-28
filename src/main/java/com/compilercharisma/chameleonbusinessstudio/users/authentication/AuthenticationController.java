package com.compilercharisma.chameleonbusinessstudio.users.authentication;

import com.compilercharisma.chameleonbusinessstudio.users.AbstractUser;
import com.compilercharisma.chameleonbusinessstudio.users.UserService;
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
 * this class is responsible for handling the "login with Google" aspect of the
 * project.
 * 
 * It still has yet to implement the redirects and setting the logged in user
 * upon validating the user's Google account
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Controller
@RequestMapping("auth")
public class AuthenticationController {
    /*
    we can use this to access values from application.properties 
     */
    private final Environment env;
    private final GoogleAuthenticator auth;
    private final UserService users;
    
    @Autowired
    public AuthenticationController(Environment env, GoogleAuthenticator auth, UserService users){
        this.env = env;
        this.auth = auth;
        this.users = users;
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
     * each param is provided by the Google Sign In library
     * @param cookiesCsrfToken 
     * @param credential
     * @param csrfToken
     * 
     * @return test value for now 
     */
    @PostMapping("/login")
    public @ResponseBody String handleSignInWithGoogle(
        @CookieValue(name="g_csrf_token") String cookiesCsrfToken,
        @RequestParam(name="credential") String credential,
        @RequestParam(name="g_csrf_token") String csrfToken
    ){
        StringBuilder sb = new StringBuilder();
        sb.append("{<br/>");
        sb.append(String.format("   cookie g_csrf_token: %s,<br/>", cookiesCsrfToken));
        sb.append(String.format("   credential: %s,<br/>", credential));
        sb.append(String.format("   g_csrf_token: %s<br/>", csrfToken));
        sb.append("}<br/>");
        
        if(cookiesCsrfToken.equals(csrfToken)){
            GoogleAccountDetails details = auth.validate(credential);
            if(details == null){
                sb.append("invalid");
            } else {
                sb.append(details.getEmail());
                handleGoogleLogin(details.getEmail());
            }
        } else {
            sb.append("BAD TOKEN");
        }
        
        return sb.toString();
    }
    
    private void handleGoogleLogin(String email){
        try {
            AbstractUser user = users.get(email);
            System.out.printf("%s should be logged into our site as %s\n", email, user.toString());
        } catch(Exception ex){
            ex.printStackTrace();
            System.out.printf("%s does not have an account here yet\n", email);
        }
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
}
