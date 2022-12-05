package com.compilercharisma.chameleonbusinessstudio.config;

import com.compilercharisma.chameleonbusinessstudio.authorization.UserAuthorizationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
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
                .oauth2Login(Customizer.withDefaults())
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/api/v1/users").authenticated() // allow logged-in users to register themselves
                .pathMatchers(HttpMethod.POST, "/api/v1/appointments/book-me/{id}").authenticated() // allow any role to book-me
                .pathMatchers(HttpMethod.POST, "/api/v1/appointments/unbook-me/{id}").authenticated() // allow any role to unbook-me
                .pathMatchers(HttpMethod.POST, "/api/v1/notification-preferences/mine").authenticated() // all users can update their notification preferences
                .pathMatchers(HttpMethod.GET, "/api/v1/config/**").permitAll() // need to allow unauthenticated users
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
                        "/assets/images/**.svg")
                .permitAll()
                .pathMatchers(HttpMethod.GET, "/api/v1/**").authenticated() // by default, allow any logged-in user to GET
                .pathMatchers(HttpMethod.POST, "/api/v1/**").access(userAuthorizationManager) // by default, only authorized users can POST
                .pathMatchers(HttpMethod.PUT, "/api/v1/**").access(userAuthorizationManager) // by default, only authorized users can PUT
                .pathMatchers(HttpMethod.DELETE, "/api/v1/**").access(userAuthorizationManager) // by default, only authorized users can DELETE
                .anyExchange().authenticated()
                .and()
                .cors().configurationSource(cs -> new CorsConfiguration().applyPermitDefaultValues())
                .and()
                .csrf()
                .disable()
                .build(); // not sure what this does
    }
}
