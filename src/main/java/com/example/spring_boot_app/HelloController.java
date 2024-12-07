package com.example.spring_boot_app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping
    public String helloWorld() {
        return "Hello World from Spring Boot";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}
