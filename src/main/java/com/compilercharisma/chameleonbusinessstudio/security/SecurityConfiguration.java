package com.compilercharisma.chameleonbusinessstudio.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;

/**
 * we will definitely need to rework this, as I'm not sure which paths we need
 * to authenticate and allow
 *
 * @author Matt
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    public SecurityConfiguration(OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        /*
        We need to figure out what configuration this needs such that most of
        the Angular routes and Spring API resources are restricted, yet doesn't
        block all of our resources.
        Maybe put API under an /api route?
        */
        security
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
                .permitAll()// non-logged in users need access to log in page
                .and()
                .logout()
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/index",
                        "/index.html",
                        "/styles.**.css", // Angular files
                        "/runtime.**.js",
                        "/polyfills.**.js",
                        "/main.**.js",
                        "/oauth/**", // needed for login
                        "/custom/**/*",
                        "/**/*.ico", // angular icon
                        "/site-header",
                        "/assets/images/**.svg"
                ).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .cors().configurationSource(cs-> new CorsConfiguration().applyPermitDefaultValues()); // not sure what this does
        
        /*
        https://stackoverflow.com/questions/58195990/how-to-disable-logout-confirmation-in-spring-security-using-xml
        
        can maybe remove this line once we have a post request for logout,
        assuming it doesn't prompt 'are you sure you want to log out?'
        */
        security.csrf().disable();
    }

}
