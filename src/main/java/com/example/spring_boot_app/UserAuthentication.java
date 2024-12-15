package com.example.spring_boot_app;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;

@Service
public class UserAuthentication extends SimpleUrlAuthenticationSuccessHandler {


    private final UserRepository userRepository;


    public UserAuthentication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("Hello 2");

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


        String role = user.getRole();  // Assuming 'user' object has a 'role' field

        // Create a list of granted authorities (roles)
        var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

        // Create the OAuth2AuthenticationToken with the correct parameters
        OAuth2AuthenticationToken updatedAuthentication = new OAuth2AuthenticationToken(
                oAuth2User,                        // The OAuth2User object representing the authenticated user
                authorities,                       // Authorities (roles) granted to the user
                "google"                           // The client registration ID (could be 'google' or another provider)
        );

        // Optionally, set this updated authentication in the security context if needed
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);


        response.sendRedirect("/dashboard");
    }
}
