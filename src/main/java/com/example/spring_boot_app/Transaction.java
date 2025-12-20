package com.example.spring_boot_app;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "transaction_time", insertable = false, updatable = false)
    private LocalDateTime transaction_time;

    @Column(name = "description")
    private String description;


    @Column(name = "user_id")
    private Long userId;


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setTransaction_time(LocalDateTime transaction_time) {
        this.transaction_time = transaction_time;

    }
    public LocalDateTime getTransaction_time() {
        return transaction_time;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;

    }

}
// Getters and setters

