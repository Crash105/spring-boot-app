package com.example.spring_boot_app;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserAuthentication extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

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

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
