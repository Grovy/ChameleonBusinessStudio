package com.compilercharisma.chameleonbusinessstudio.controller;

import com.compilercharisma.chameleonbusinessstudio.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@Slf4j
@RequestMapping(path="/api/v2/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService){
        this.userService = userService;
    }

    /**
     * Gets the currently logged-in user's email from the Authentication object
     *
     * @param authentication Method of Authentication, e.g. Google OAuth2.0
     * @return the email of the user
     */
    @GetMapping("/getPrincipal")
    public String getPrincipal(Authentication authentication) {
        return ((OAuth2AuthenticationToken) authentication).getPrincipal().getAttribute("email");
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
        var email = ((OAuth2AuthenticationToken) authentication).getPrincipal().getAttribute("email");
        log.info("Checking to see if user with email [{}] is registered in Vendia", email);
        return Mono.just(Objects.requireNonNull(email))
                .flatMap(s -> userService.isRegistered((String) s))
                .map(bool -> new ResponseEntity<>(bool, HttpStatus.OK));
    }

}
