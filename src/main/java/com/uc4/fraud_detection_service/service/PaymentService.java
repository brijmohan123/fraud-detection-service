package com.uc4.fraud_detection_service.service;

import com.uc4.fraud_detection_service.model.PaymentRequest;
import com.uc4.fraud_detection_service.model.Transaction;
import com.uc4.fraud_detection_service.repository.TransactionRepository;
import com.uc4.fraud_detection_service.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;

@Service
public class PaymentService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void processPayment(PaymentRequest paymentRequest) throws Exception {
        // Encrypt card details
        String encryptedCardNumber = EncryptionUtil.encrypt(paymentRequest.getCardNumber(), secretKey);
        String encryptedCardCvv = EncryptionUtil.encrypt(paymentRequest.getCardCvv(), secretKey);

        // Simulate payment processing
        Transaction transaction = paymentRequest.getTransaction();
        transaction.setStatus("SUCCESS");
        transactionRepository.save(transaction);

        // Publish event
        publishTransactionCompletedEvent(paymentRequest);
    }

    private void publishTransactionCompletedEvent(PaymentRequest paymentRequest) {
        String message = "Transaction completed for user ID: " + paymentRequest.getUserId() + ", Amount: " + paymentRequest.getTransaction().getAmount();
        kafkaTemplate.send("transaction-completed-topic", message);
    }
}
