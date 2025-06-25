package com.uc4.fraud_detection_service.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class AnomalyDetectedEvent {
    private String transactionId;
    private String reason;
}