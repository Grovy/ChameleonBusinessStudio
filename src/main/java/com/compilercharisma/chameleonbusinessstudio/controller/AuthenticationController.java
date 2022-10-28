package com.compilercharisma.chameleonbusinessstudio.controller;

import com.compilercharisma.chameleonbusinessstudio.service.AuthenticationService;
import com.compilercharisma.chameleonbusinessstudio.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Communicates logged-in user details to the Angular front-end.
 */
@RestController
@Slf4j
@RequestMapping(path="/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authService;
    private final UserService userService;

    public AuthenticationController(
            AuthenticationService authService, 
            UserService userService
    ){
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * Gets the currently logged-in user's email from the Authentication object
     *
     * @param authentication Method of Authentication, e.g. Google OAuth2.0
     * @return the email of the user
     */
    @GetMapping("/principal")
    public String getPrincipal(Authentication authentication) {
        return authService.getEmailFrom(authentication);
    }

    /**
     * Returns true if the user is properly authenticated
     *
     * @param authentication Method of Authentication, e.g. Google OAuth2.0
     * @return true if the user is authenticated, false otherwise
     */
    @GetMapping("/isAuthenticated")
    public boolean isAuthenticated(Authentication authentication) {
        return authentication.isAuthenticated();
    }

    /**
     * Returns true if the currently authenticated user has a user in Vendia,
     * else returns false
     *
     * @param authentication Authentication object (has the OAuth2.0 token)
     * @return {@link Boolean}
     */
    @GetMapping("/isUserRegistered")
    public Mono<ResponseEntity<Boolean>> isUserRegistered(Authentication authentication){
        var email = authService.getEmailFrom(authentication);
        log.info("Checking to see if user with email [{}] is registered in Vendia", email);
        return Mono.just(Objects.requireNonNull(email))
                .flatMap(userService::isRegistered)
                .map(bool -> new ResponseEntity<>(bool, HttpStatus.OK));
    }
}
