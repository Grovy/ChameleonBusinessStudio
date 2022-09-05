package com.compilercharisma.chameleonbusinessstudio.config;

import com.compilercharisma.chameleonbusinessstudio.authentication.AuthenticationService;
import com.compilercharisma.chameleonbusinessstudio.entity.UserEntity;
import com.compilercharisma.chameleonbusinessstudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Autowired
    public OAuth2LoginSuccessHandler(AuthenticationService authenticationService, UserService userService){
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /**
     *
     * @param request HTTPServlet request to be
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
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
