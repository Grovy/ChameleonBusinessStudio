package com.compilercharisma.chameleonbusinessstudio.Security;

import com.compilercharisma.chameleonbusinessstudio.authentication.AuthenticationService;
import com.compilercharisma.chameleonbusinessstudio.users.UserEntity;
import com.compilercharisma.chameleonbusinessstudio.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
Author: Daniel E. Villavicencio Mena
This class is a custom-made OAuth2 success handler that has two dependencies:
One on authenticationService and another on userService
This handler gets called in SecurityConfiguration after a Google User
successfully authenticates itself using Google.
 */
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Autowired
    public OAuth2LoginSuccessHandler(AuthenticationService authenticationService, UserService userService){
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /*
    Business Logic on what to do after a successful Google-sign in
    Get the OAuth2User from the AuthenticationService, get its details (name, email) and build
    a new UserEntity from them.
    Store in the UserRepository.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User googleUser = authenticationService.getLoggedInGoogleUser();
        UserEntity ourUser = new UserEntity();
        ourUser.setEmail(googleUser.getAttribute("email"));
        ourUser.setDisplayName(googleUser.getAttribute("name"));
        ourUser.setRole("Participant");
        if(!(userService.isRegistered(ourUser.getEmail()))){
            userService.registerUser(ourUser);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
