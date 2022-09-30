package com.compilercharisma.chameleonbusinessstudio.service;

import com.compilercharisma.chameleonbusinessstudio.entity.UserEntity;
import com.compilercharisma.chameleonbusinessstudio.entity.user.AbstractUser;
import com.compilercharisma.chameleonbusinessstudio.exception.NoUserLoggedInException;
import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
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
        return googleUserToAbstractUser(googleUser);
    }

    public OAuth2User getLoggedInGoogleUser(){
        if(!isUserLoggedIn()){
            throw new NoUserLoggedInException();
        }
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principalToGoogleUser(p);
    }

    /**
     * This method may replace some of our other methods, as it sounds like
     * SecurityContextHolder is unreliable in Webflux: 
     * https://stackoverflow.com/q/70050719
     * 
     * @return a mono containing the logged in user, or an error if no user is
     *  logged in
     */
    public Mono<UserEntity> getLoggedInUserReactive(){
        return ReactiveSecurityContextHolder
            .getContext()
            .map(SecurityContext::getAuthentication)
            .handle((auth, sink)->{
                // https://stackoverflow.com/a/53596358
                if(isAuthenticatedUser(auth)){
                    sink.next(auth.getPrincipal());
                } else {
                    sink.error(new NoUserLoggedInException());
                }
            })
            .map(this::principalToGoogleUser)
            .map(this::googleUserToAbstractUser)
            .map(this::abstractUserToUserEntity);            
    }

    public Mono<Boolean> isReactiveUserLoggedIn(){
        return ReactiveSecurityContextHolder
            .getContext()
            .map(SecurityContext::getAuthentication)
            .map(this::isAuthenticatedUser);
    }

    private boolean isAuthenticatedUser(Authentication auth){
        return auth != null 
            && auth.isAuthenticated() 
            && auth.getPrincipal() != null 
            && !(auth instanceof AnonymousAuthenticationToken);
    }
    
    public boolean isUserLoggedIn(){
        Authentication authStatus = SecurityContextHolder.getContext().getAuthentication();
        return isAuthenticatedUser(authStatus);
    }
    
    public boolean isLoggedInUserRegistered(){
        OAuth2User googleUser = getLoggedInGoogleUser();
        return users.isRegistered(googleUser.getAttribute("email"));
    }

    private OAuth2User principalToGoogleUser(Object principal){
        if(!(principal instanceof OAuth2User)){
            throw new RuntimeException();
        }
        return (OAuth2User)principal;
    }

    private AbstractUser googleUserToAbstractUser(OAuth2User googleUser){
        return users.get(googleUser.getAttribute("email"));
    }

    private UserEntity abstractUserToUserEntity(AbstractUser user){
        return user.getAsEntity();
    }
}