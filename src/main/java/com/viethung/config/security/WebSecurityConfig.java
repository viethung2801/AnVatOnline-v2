package com.viethung.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> {
                            request.requestMatchers("/my-profile").authenticated();
                            request.requestMatchers("/admin/**").hasAnyRole("ADMIN");
                            request.anyRequest().permitAll();
                        }
                ).formLogin(
                        loginForm ->
                                loginForm.loginPage("/login")
                                        .defaultSuccessUrl("/")
                                        .permitAll()

                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                )
                .exceptionHandling(exception -> exception.accessDeniedPage("/403"))
        ;
        return httpSecurity.build();
    }
}
