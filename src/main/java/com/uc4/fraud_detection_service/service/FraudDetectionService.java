package com.uc4.fraud_detection_service.service;


import com.uc4.fraud_detection_service.Exception.FraudRuleException;
import com.uc4.fraud_detection_service.Exception.TransactionException;
import com.uc4.fraud_detection_service.model.AnomalyDetectedEvent;
import com.uc4.fraud_detection_service.model.FraudRule;
import com.uc4.fraud_detection_service.model.Transaction;
import com.uc4.fraud_detection_service.repository.FraudRuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Service
public class FraudDetectionService {

    private static final Logger logger = LoggerFactory.getLogger(FraudDetectionService.class);
    private static final int THREAD_POOL_SIZE = 10;

    @Autowired
    private FraudRuleRepository fraudRuleRepository;

    @Autowired
    private KafkaTemplate<String, AnomalyDetectedEvent> kafkaTemplate;

    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public void analyzeTransaction(Transaction transaction) {
        logger.info("Analyzing transaction: {}", transaction.getTransactionId());
        try {
            if (evaluateTransaction(transaction)) {
                AnomalyDetectedEvent event = new AnomalyDetectedEvent();
                event.setTransactionId(transaction.getTransactionId());
                event.setReason("Transaction flagged as fraudulent");
                kafkaTemplate.send("anomalies", event);
                logger.warn("Anomaly detected for transaction: {}", transaction.getTransactionId());
            } else {
                logger.info("Transaction {} is within normal limits.", transaction.getTransactionId());
            }
        } catch (FraudRuleException e) {
            logger.error("Fraud rule evaluation failed for transaction {}: {}", transaction.getTransactionId(), e.getMessage());
            throw new TransactionException("Error analyzing transaction", e);
        } catch (Exception e) {
            logger.error("Unexpected error analyzing transaction {}: {}", transaction.getTransactionId(), e.getMessage());
            throw new TransactionException("Unexpected error occurred", e);
        }
    }

    @Cacheable("fraudRules")
    public List<FraudRule> getFraudRules() {
        logger.info("Fetching fraud rules from repository");
        try {
            return fraudRuleRepository.findAll();
        } catch (Exception e) {
            logger.error("Error fetching fraud rules: {}", e.getMessage());
            throw new FraudRuleException("Failed to fetch fraud rules", e);
        }
    }

    public boolean evaluateTransaction(Transaction transaction) {
        logger.debug("Evaluating transaction: {}", transaction.getTransactionId());
        try {
            List<FraudRule> rules = getFraudRules();
            List<Callable<Boolean>> tasks = rules.stream()
                    .map(rule -> (Callable<Boolean>) () -> rule.evaluate(transaction))
                    .toList();

            List<Future<Boolean>> results = executorService.invokeAll(tasks);

            for (Future<Boolean> result : results) {
                if (result.get()) {
                    logger.info("Transaction {} failed a rule", transaction.getTransactionId());
                    return true;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error evaluating transaction {}: {}", transaction.getTransactionId(), e.getMessage());
            throw new FraudRuleException("Transaction evaluation failed", e);
        }
        return false;
    }
}
