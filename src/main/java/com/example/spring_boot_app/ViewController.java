package com.example.spring_boot_app;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Map;

@Controller
public class ViewController {

    @GetMapping("/dashboard")
    public String dashboardView(OAuth2AuthenticationToken token, Model model) {
        model.addAttribute("name", token.getPrincipal().getAttribute("name"));
        model.addAttribute("email", token.getPrincipal().getAttribute("email"));
        return "dashboardView";
    }

    @GetMapping("/")
    public String loginpage() {

        return "loginpage";
    }
}
