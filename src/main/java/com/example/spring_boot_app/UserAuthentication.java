package com.example.spring_boot_app;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserAuthentication extends SimpleUrlAuthenticationSuccessHandler {


    private final UserRepository userRepository;


    public UserAuthentication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {



        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String googleId = oAuth2User.getAttribute("sub");
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");


        // Check if the user already exists
        User user = userRepository.findByGoogleId(googleId).orElseGet(() -> {
            User newUser = new User();
            newUser.setGoogleId(googleId);
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setRole("USER");
            return userRepository.save(newUser);
        });




        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        OAuth2User updatedUser = new DefaultOAuth2User(
                authorities,
                oAuth2User.getAttributes(),
                "sub"
        );



        OAuth2AuthenticationToken newAuth = new OAuth2AuthenticationToken(
                updatedUser,
                authorities,
                "google"
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        // Debug — print UPDATED authorities
        System.out.println("Updated Authorities: " + newAuth.getAuthorities());





        response.sendRedirect("/dashboard");
    }
}
