package com.compilercharisma.chameleonbusinessstudio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity security) {
        return security
                .authorizeExchange()
                .pathMatchers("/api/appointments/**")
                .permitAll()
                .and()
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
    }

}
