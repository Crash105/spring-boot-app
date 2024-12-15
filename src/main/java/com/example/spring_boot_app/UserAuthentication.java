package com.example.spring_boot_app;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import org.springframework.stereotype.Service;

import java.io.IOException;

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
        userRepository.findByGoogleId(googleId).orElseGet(() -> {
            User user = new User();
            user.setGoogleId(googleId);
            user.setName(name);
            user.setEmail(email);
            return userRepository.save(user);
        });

        response.sendRedirect("/dashboard");
    }
}
