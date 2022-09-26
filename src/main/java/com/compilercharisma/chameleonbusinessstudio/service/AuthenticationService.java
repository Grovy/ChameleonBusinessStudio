//package com.compilercharisma.chameleonbusinessstudio.service;
//
//import com.compilercharisma.chameleonbusinessstudio.dto.User;
//import com.compilercharisma.chameleonbusinessstudio.exception.NoUserLoggedInException;
//import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException;
//import com.compilercharisma.chameleonbusinessstudio.repository.UserRepository;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
///**
// * Use this class to get the logged in user.
// *
// * Note that there are two types of user that can log in:
// * - Google Users (unregistered)
// * - OUR Users (registered)
// *
// * Keep that in mind when using this class
// *
// * @author Matt Crow <mattcrow19@gmail.com>
// */
//@Service
//public class AuthenticationService {
//
//    private final UserRepository userRepository;
//
//    public AuthenticationService(UserRepository userRepository){
//        this.userRepository = userRepository;
//    }
//
//    /**
//     * will throw exceptions if the user isn't registered
//     * @return the logged in user, using OUR representation of them
//     */
//    public Mono<User> getLoggedInUser(){
//        if(!isLoggedInUserRegistered()){
//            throw new UserNotRegisteredException();
//        }
//        OAuth2User googleUser = getLoggedInGoogleUser();
//        return userRepository.findId(googleUser.getAttribute("email"));
//    }
//
//    public OAuth2User getLoggedInGoogleUser(){
//        if(!isUserLoggedIn()){
//            throw new NoUserLoggedInException();
//        }
//        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if(!(p instanceof OAuth2User)){
//            throw new RuntimeException();
//        }
//        return (OAuth2User)p;
//    }
//
//    public boolean isUserLoggedIn(){
//        Authentication authStatus = SecurityContextHolder.getContext().getAuthentication();
//        return authStatus != null
//                && authStatus.isAuthenticated()
//                && authStatus.getPrincipal() != null
//                && !(authStatus instanceof AnonymousAuthenticationToken);
//    }
//
//    public boolean isLoggedInUserRegistered(){
//        OAuth2User googleUser = getLoggedInGoogleUser();
//        return userRepository.isUserRegistered(googleUser.getAttribute("email"));
//    }
//}