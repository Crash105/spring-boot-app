package com.example.spring_boot_app;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final TransactionService transactionService;

    public AdminController(TransactionService transactionService, UserRepository userRepository) {
        this.userRepository = userRepository;

        this.transactionService = transactionService;
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model, OAuth2AuthenticationToken token) {
        model.addAttribute("name", token.getPrincipal().getAttribute("name"));
        model.addAttribute("email", token.getPrincipal().getAttribute("email"));

        // Get all users
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        Map<Long, Double> balances = new HashMap<>();

        for (User user : users) {
            double balance = transactionService.getUserBalance(user.getId());
            balances.put(user.getId(), balance);
        }
        model.addAttribute("balances", balances);

        // Get all transactions
        //List<Transaction> transactions = transactionRepository.findAll();
        //model.addAttribute("transactions", transactions);

        return "adminDashboard";
    }
}
