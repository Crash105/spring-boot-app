package com.example.spring_boot_app;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class HelloController {

    /*
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World from Spring Boot";

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Hello Admin Endpoint";
    }


    @RequestMapping("/user")
    public Principal user(Principal principal) {

        return principal;
    }
    */


}