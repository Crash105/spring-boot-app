package com.example.spring_boot_app.controller;

import com.example.spring_boot_app.*;
import com.example.spring_boot_app.entities.Transaction;
import com.example.spring_boot_app.entities.User;
import com.example.spring_boot_app.repository.TransactionRepository;
import com.example.spring_boot_app.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;



@Controller
public class ViewController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final AIService aiService;

    public ViewController(UserRepository userRepository,
                          TransactionRepository transactionRepository, TransactionService transactionService, AIService aiService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
        this.aiService = aiService;




    }

    @GetMapping("/dashboard")
    public String dashboardView(OAuth2AuthenticationToken token, Model model) {

        String googleId = token.getPrincipal().getAttribute("sub");

        // But in YOUR database, user_id references google_authentication.id (BIGINT)
        User user = userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        double balance = transactionService.getUserBalance(user.getId());
        model.addAttribute("balance", balance);
        model.addAttribute("name", token.getPrincipal().getAttribute("name"));
        model.addAttribute("email", token.getPrincipal().getAttribute("email"));
        return "dashboardView";
    }

    @GetMapping("/companies")
    public String companies(Model model) {
        // Service returns List<Company>, not JSON string - this is the correct Spring pattern
        // The view layer (Thymeleaf) will handle rendering the list
        List<Company> companies = aiService.generateCompanyNames();
        model.addAttribute("companies", companies);
        return "InvestmentView";
    }


    @PostMapping(value = "/transaction", consumes = "application/x-www-form-urlencoded")
    public String handleTransaction(
            @RequestParam("amount") Integer amount,
            @RequestParam("type") String type,
            OAuth2AuthenticationToken token
    ) {
        String googleId = token.getPrincipal().getAttribute("sub");

        // But in YOUR database, user_id references google_authentication.id (BIGINT)
        User user = userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("User not found"));



        // Save transaction
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(amount);
        newTransaction.setDescription(type);
        newTransaction.setUserId(user.getId());
        newTransaction.setPerformed_by(user.getId());
        transactionRepository.save(newTransaction);
        transactionService.clearUserBalanceCache(user.getId());

        return "redirect:/dashboard";

    }
    @PostMapping(value = "/transaction", consumes = "application/json")
    public ResponseEntity<?> handleAdminTransaction(OAuth2AuthenticationToken token, @RequestBody Map<String, Object> json) {

        Long userId = Long.valueOf(json.get("userId").toString());
        Integer amount = Integer.valueOf(json.get("amount").toString());
        String type = json.get("type").toString();

        String googleId = token.getPrincipal().getAttribute("sub");

        // But in YOUR database, user_id references google_authentication.id (BIGINT)
        User user = userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(amount);
        newTransaction.setDescription(type);
        newTransaction.setUserId(userId);
        newTransaction.setPerformed_by(user.getId());

        transactionRepository.save(newTransaction);
        transactionService.clearUserBalanceCache(userId);

        return ResponseEntity.ok("OK");
    }

    @GetMapping("/")
    public String loginpage() {

        return "loginpage";
    }
}
