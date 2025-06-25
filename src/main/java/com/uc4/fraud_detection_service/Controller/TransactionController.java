package com.uc4.fraud_detection_service.Controller;
import com.uc4.fraud_detection_service.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.uc4.fraud_detection_service.service.FraudDetectionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private FraudDetectionService fraudDetectionService;

    @PostMapping
    public void processTransaction(@RequestBody Transaction transaction) {
        logger.info("Received transaction for analysis: {}", transaction.getTransactionId());
        fraudDetectionService.analyzeTransaction(transaction);
        logger.info(("Transaction analysed"));
    }
}
