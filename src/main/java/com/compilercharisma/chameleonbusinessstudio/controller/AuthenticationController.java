package com.compilercharisma.chameleonbusinessstudio.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

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
}
