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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * this class is responsible for handling the "login with Google" aspect of the
 * project.
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
    private final AuthenticationService authService;
    private final UserService users;
    
    @Autowired
    public AuthenticationController(Environment env, GoogleAuthenticator auth, AuthenticationService authService, UserService users){
        this.env = env;
        this.auth = auth;
        this.authService = authService;
        this.users = users;
    }
    
    /**
     * allows the Angular app to get the google client id, as it does not have
     * direct access to the property file
     * 
     * note that this method should not require authentication, as it's OK for
     * the Google client ID to be visible, JUST NOT THE CLIENT SECRET
     * 
     * @return a JSON object containing {google_client_id: string}
     */
    @GetMapping("/credentials")
    public @ResponseBody Map<String, Object> getCredentialsForAngular(){
        Map<String, Object> giveThisToNg = new HashMap<>();
        giveThisToNg.put("google_client_id", env.getProperty("spring.security.oauth2.client.registration.google.clientId"));
        return giveThisToNg;
    }
    
    /**
     * test method so we can see what attributes the Google login provides.
     * 
     * @param user automatically set by Spring if the user is logged in
     * 
     * @return JSON data of the user 
     */
    @GetMapping(path="/principal")
    public @ResponseBody Map<String, Object> user(@AuthenticationPrincipal OAuth2User user){
        try {
            AbstractUser model = authService.getLoggedInUser();
            System.out.println(model);
        } catch(UserNotRegisteredException ex){
            System.out.println("Current user isn't registered");
        }
        return user.getAttributes();
    }
    
    /** 
     * Currently unused, as I'm using the default Spring Security Oauth2 to
     * handle authentication
     * 
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
    
    // unused for now
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
     * test example showing how the manual implementation of signin with google
     * looks.
     * 
     * @return signInWithGoogleExample.html with the credentials baked in
     */
    @GetMapping("/test")
    public @ResponseBody String test(){
        StringBuilder content = new StringBuilder();
        try(
            InputStream s = AuthenticationController.class.getResourceAsStream("/signinWithGoogleManual.html");
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
            env.getProperty("spring.security.oauth2.client.registration.google.clientId"
        ));
    }
}
