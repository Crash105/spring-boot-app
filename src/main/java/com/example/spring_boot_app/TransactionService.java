package com.example.spring_boot_app;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository repo) {
        this.transactionRepository = repo;
    }

    @Cacheable(value = "userBalance", key = "#userId")
    public double getUserBalance(Long userId) {

        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        double total = 0.0;
        for (Transaction tx : transactions) {
            if ("add".equalsIgnoreCase(tx.getDescription())) {
                total += tx.getAmount().doubleValue();
            } else if ("subtract".equalsIgnoreCase(tx.getDescription())) {
                total -= tx.getAmount().doubleValue();
            }
        }

        return total;
    }
    @CacheEvict(value = "userBalance", key = "#userId")
    public void clearUserBalanceCache(Long userId) {}

}
