package com.compilercharisma.chameleonbusinessstudio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

/**
 * we will definitely need to rework this, as I'm not sure which paths we need
 * to authenticate and allow
 *
 * @author Matt
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity security) throws Exception {
        return security
                .oauth2Login()
                .and()
                .logout()
                .and()
                .authorizeExchange()
                .pathMatchers(
                        "/",
                        "/index",
                        "/index.html",
                        "/styles.**.css", // Angular files
                        "/runtime.**.js",
                        "/polyfills.**.js",
                        "/main.**.js",
                        "/oauth/**", // needed for login
                        "/custom/**",
                        "/**.ico", // angular icon
                        "/site-header",
                        "/assets/images/**.svg")
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .cors().configurationSource(cs-> new CorsConfiguration().applyPermitDefaultValues())
                .and()
                .csrf()
                .disable().build(); // not sure what this does

        /*
        https://stackoverflow.com/questions/58195990/how-to-disable-logout-confirmation-in-spring-security-using-xml

        can maybe remove this line once we have a post request for logout,
        assuming it doesn't prompt 'are you sure you want to log out?'
        */

    }

}
