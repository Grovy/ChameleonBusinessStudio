package com.compilercharisma.chameleonbusinessstudio.config;

import com.compilercharisma.chameleonbusinessstudio.authorization.UserAuthorizationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity security, UserAuthorizationManager userAuthorizationManager) {
        return security
                .oauth2Login()
                .and()
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/api/v1/users").authenticated()                // this will be used for the log in flow after a user is authenticated
                .pathMatchers(HttpMethod.GET, "/api/v1/**").authenticated()                   // by default, allow any logged-in user to GET
                .pathMatchers(HttpMethod.POST, "/api/v1/**").access(userAuthorizationManager)  // by default, only authorized users can POST
                .pathMatchers(HttpMethod.PUT, "/api/v1/**").access(userAuthorizationManager)  // by default, only authorized users can PUT
                .pathMatchers(HttpMethod.DELETE, "/api/v1/**").access(userAuthorizationManager)  // by default, only authorized users can DELETE
                .pathMatchers(HttpMethod.POST, "/api/v1/appointments/book-me").authenticated() // allow any role to book-me
                .pathMatchers(HttpMethod.GET, "/api/v1/auth/principal").permitAll()           // not sure if this will crash?
                .pathMatchers(HttpMethod.GET, "/api/v1/auth/isAuthenticated").permitAll()     // crash?
                .pathMatchers(HttpMethod.GET, "/api/v1/auth/isUserRegistered").permitAll()    // crash?
                .pathMatchers(HttpMethod.GET, "/api/v1/config/**").permitAll()                // need to allow unauthenticated users
                .pathMatchers(
                        "/",
                        "/index",
                        "/index.html",
                        "/styles.**.css", // Angular files
                        "/runtime.**.js",
                        "/polyfills.**.js",
                        "/main.**.js",
                        "/oauth/**", // needed for login
                        "/**.ico", // angular icon
                        "/site-header",
                        "/assets/images/**.svg")
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .cors().configurationSource(cs -> new CorsConfiguration().applyPermitDefaultValues())
                .and()
                .csrf()
                .disable().build(); // not sure what this does
    }
}
