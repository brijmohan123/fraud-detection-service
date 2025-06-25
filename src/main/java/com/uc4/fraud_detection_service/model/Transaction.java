package com.uc4.fraud_detection_service.model;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
public class Transaction {
    private String transactionId;
    @Positive
    private double amount;
    private String accountId;
    private String timestamp;
    private String status; // e.g., SUCCESS, FAILED
}
