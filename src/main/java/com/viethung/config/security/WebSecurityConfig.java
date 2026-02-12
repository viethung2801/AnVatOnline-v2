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
                            // Use explicit AntPathRequestMatcher to avoid ambiguity when there are multiple servlets
                            request.requestMatchers(new AntPathRequestMatcher("/my-profile")).authenticated();
                            request.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAnyRole("ADMIN");
                            // Allow H2 console access when embedded (H2 registers its own servlet)
                            request.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll();
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
                // Allow frames from same origin so H2 console can render in a frame
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
        ;
        return httpSecurity.build();
    }
}
