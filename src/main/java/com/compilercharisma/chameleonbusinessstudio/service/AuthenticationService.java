package com.compilercharisma.chameleonbusinessstudio.service;

import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.exception.NoUserLoggedInException;
import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
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
     * This method may replace some of our other methods, as it sounds like
     * SecurityContextHolder is unreliable in Webflux: 
     * https://stackoverflow.com/q/70050719
     * 
     * @return a mono containing the logged in user, or an error if no user is
     *  logged in
     */
    public Mono<User> getLoggedInUserReactive(){
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
            .flatMap(this::googleUserToOurUser);           
    }

    private boolean isAuthenticatedUser(Authentication auth){
        return auth != null 
            && auth.isAuthenticated() 
            && auth.getPrincipal() != null 
            && !(auth instanceof AnonymousAuthenticationToken);
    }

    private OAuth2User principalToGoogleUser(Object principal){
        if(!(principal instanceof OAuth2User)){
            throw new RuntimeException();
        }
        return (OAuth2User)principal;
    }

    private Mono<User> googleUserToOurUser(OAuth2User googleUser){
        String email = googleUser.getAttribute("email");
        return users.get(email)
            .handle((maybeUser, sink)->{
                if(maybeUser.isPresent()){
                    sink.next(maybeUser.get());
                } else {
                    sink.error(new UserNotRegisteredException(email));
                }
            });
    }
}