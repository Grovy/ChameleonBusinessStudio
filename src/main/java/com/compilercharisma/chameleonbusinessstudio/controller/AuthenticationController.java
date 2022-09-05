package com.compilercharisma.chameleonbusinessstudio.controller;

import com.compilercharisma.chameleonbusinessstudio.authentication.AuthenticationService;
import com.compilercharisma.chameleonbusinessstudio.exception.NoUserLoggedInException;
import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException;
import com.compilercharisma.chameleonbusinessstudio.entity.user.AbstractUser;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("auth/userinfo.email")
public class AuthenticationController {    
    private final AuthenticationService authService;
    
    @Autowired
    public AuthenticationController(AuthenticationService authService){
        this.authService = authService;
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
        } catch(NoUserLoggedInException ex){
            System.out.print("No user is logged in");
        } catch(UserNotRegisteredException ex){
            System.out.println("Current user isn't registered");
        }
        return user.getAttributes();
    }
}
