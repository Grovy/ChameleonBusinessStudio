package com.compilercharisma.chameleonbusinessstudio.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    @GetMapping("/getPrincipal")
    public String getPrincipal(Authentication authentication) {
        return ((OAuth2AuthenticationToken) authentication).getPrincipal().getAttribute("email");
    }
}
