package com.compilercharisma.chameleonbusinessstudio;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;

/**
 * we will definitely want to rework this, as I'm not sure which paths we need
 * to authenticate and allow
 * 
 * @author Matt
 */
@Configuration
@EnableWebSecurity
public class TempSecurity extends WebSecurityConfigurerAdapter {
    
    @Override
    public void configure(WebSecurity security) throws Exception {
        security.ignoring().antMatchers("/resources/static/**");
    }
    
    @Override
    protected void configure(HttpSecurity security) throws Exception {
        /*
        we need to figure out what configuration this needs such that most of
        the Angular routes and Spring API resources are restricted, yet doesn't
        block all of our resources.
        Maybe put API under an /api route?
        */
        security
                .httpBasic()
                .disable();
        security
                .oauth2Login()
                    .permitAll()
                .and()
                .authorizeRequests()
                    .antMatchers(
                            "/", 
                            "/index", 
                            "/index.html",
                            "/oauth/**"
                    ).permitAll()
                    //.anyRequest().authenticated()//a -> a.antMatchers("/auth/credentials").permitAll().anyRequest().authenticated());
                .and()
                .cors().configurationSource(cs-> new CorsConfiguration().applyPermitDefaultValues());
        security.csrf().disable();
    }
}
