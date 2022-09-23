package com.compilercharisma.chameleonbusinessstudio.service;

import com.compilercharisma.chameleonbusinessstudio.entity.user.AbstractUser;
import com.compilercharisma.chameleonbusinessstudio.exception.NoUserLoggedInException;
import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * Use this class to get the logged in user.
 * 
 * Note that there are two types of user that can log in:
 * - Google Users (unregistered)
 * - OUR Users (registered)
 * 
 * Keep that in mind when using this class
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Service
public class AuthenticationService {

    private final UserService users;

    @Autowired
    public AuthenticationService(UserService users){
        this.users = users;
    }

    /**
     * will throw exceptions if the user isn't registered
     * @return the logged in user, using OUR representation of them 
     */
    public AbstractUser getLoggedInUser(){
        if(!isLoggedInUserRegistered()){
            throw new UserNotRegisteredException();
        }
        OAuth2User googleUser = getLoggedInGoogleUser();
        return users.get(googleUser.getAttribute("email"));
    }

    public OAuth2User getLoggedInGoogleUser(){
        if(!isUserLoggedIn()){
            throw new NoUserLoggedInException();
        }
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!(p instanceof OAuth2User)){
            throw new RuntimeException();
        }
        return (OAuth2User)p;
    }
    
    public boolean isUserLoggedIn(){
        Authentication authStatus = SecurityContextHolder.getContext().getAuthentication();
        return authStatus != null 
                && authStatus.isAuthenticated() 
                && authStatus.getPrincipal() != null 
                && !(authStatus instanceof AnonymousAuthenticationToken);
    }
    
    public boolean isLoggedInUserRegistered(){
        OAuth2User googleUser = getLoggedInGoogleUser();
        return users.isRegistered(googleUser.getAttribute("email"));
    }
}