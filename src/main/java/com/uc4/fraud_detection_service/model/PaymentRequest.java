package com.uc4.fraud_detection_service.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
public class PaymentRequest {

    @NotNull
    private String userId;

    @NotNull
    private String cardNumber;

    @NotNull
    private String cardExpiry;

    @NotNull
    private String cardCvv;

    private Transaction transaction;
}
