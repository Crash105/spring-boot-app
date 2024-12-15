package com.example.spring_boot_app;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final UserAuthentication userAuthentication;


 public SecurityConfig(UserAuthentication userAuthentication) {
     this.userAuthentication = userAuthentication;
 }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        System.out.println("Hello");
        return http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/").permitAll();
            auth.anyRequest().authenticated();
        })  .oauth2Login(oauth2Login -> {
                    oauth2Login.successHandler(userAuthentication);
                })
                .logout(logout -> logout
                        .logoutSuccessUrl("/")  // Redirect to home page after logout
                        .permitAll()
                )
                .build();
    }
}
