package com.uc4.fraud_detection_service.Controller;

import com.uc4.fraud_detection_service.model.PaymentRequest;
import com.uc4.fraud_detection_service.model.Transaction;
import com.uc4.fraud_detection_service.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public void processPayment(@RequestBody PaymentRequest paymentRequest) throws Exception {
        logger.info("processing payment ....");
        paymentService.processPayment(paymentRequest);
        logger.info("Payment processed with transactionId: {}", paymentRequest.getTransaction().getTransactionId());
    }
}
