package com.compilercharisma.chameleonbusinessstudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.exception.NoUserLoggedInException;
import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException;

import reactor.core.publisher.Mono;

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
     * Migrating towards using this method instead of others, as it is both
     * mockable and does not rely on any static contexts
     * 
     * @param token an authentication token
     * @return the user denoted by the token, or nothing if such a user does not
     *  exist.
     */
    public Mono<User> getLoggedInUserFrom(Authentication token){
        var email = getEmailFrom(token);
        return users.get(email)
            .map(opt -> opt.orElseThrow(()->new UserNotRegisteredException(email)));
    }

    /**
     * Migrating towards using this method instead of others, as it is mockable,
     * supports polymorphism, and doesn't rely on static contexts.
     * 
     * @param token an authentication token
     * 
     * @return the email contained in the given token
     */
    public String getEmailFrom(Authentication token){
        if(!isAuthenticatedUser(token)){
            throw new NoUserLoggedInException();
        }

        if(!(token instanceof OAuth2AuthenticationToken)){
            throw new RuntimeException("Unsupported token type: " + token.getClass().getName());
        }

        var principal = ((OAuth2AuthenticationToken)token).getPrincipal();
        var email = principal.getAttribute("email");

        if(email == null){
            throw new RuntimeException("Principle does not contain email");
        }

        return email.toString();
    }

    private boolean isAuthenticatedUser(Authentication auth){
        return auth != null 
            && auth.isAuthenticated() 
            && auth.getPrincipal() != null 
            && !(auth instanceof AnonymousAuthenticationToken);
    }
}