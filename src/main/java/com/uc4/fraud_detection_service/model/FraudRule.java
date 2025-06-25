package com.uc4.fraud_detection_service.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class FraudRule {

    private static final double THRESHOLD = 10000.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ruleName;
    private String ruleDescription;

    public boolean evaluate(Transaction transaction) {
        return transaction.getAmount() > THRESHOLD;
    }
}
