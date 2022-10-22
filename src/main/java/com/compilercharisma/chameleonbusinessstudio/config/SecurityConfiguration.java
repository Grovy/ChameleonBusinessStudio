package com.compilercharisma.chameleonbusinessstudio.config;

import com.compilercharisma.chameleonbusinessstudio.authorization.UserAuthorizationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

//    @Bean
//    ReactiveClientRegistrationRepository clientRegistrations() {
//        ClientRegistration clientRegistration = ClientRegistrations
//                .fromOidcIssuerLocation("https://accounts.google.com")
//                .clientId("621729563946-2bnana80rude278barj2a3eft2l27n3c.apps.googleusercontent.com")
//                .clientSecret("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")
//                .build();
//        return new InMemoryReactiveClientRegistrationRepository(clientRegistration);
//    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity security, UserAuthorizationManager userAuthorizationManager) {
        return security
                .oauth2Login(Customizer.withDefaults())
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/api/appointments/**").access(userAuthorizationManager)
                .pathMatchers(HttpMethod.DELETE, "/api/appointments/**").access(userAuthorizationManager)
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
