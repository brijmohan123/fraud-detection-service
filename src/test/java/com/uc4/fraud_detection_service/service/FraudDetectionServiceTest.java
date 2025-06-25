package com.uc4.fraud_detection_service.service;

import com.uc4.fraud_detection_service.model.AnomalyDetectedEvent;
import com.uc4.fraud_detection_service.model.FraudRule;
import com.uc4.fraud_detection_service.model.Transaction;
import com.uc4.fraud_detection_service.repository.FraudRuleRepository;
import com.uc4.fraud_detection_service.service.FraudDetectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FraudDetectionServiceTest {

    @Mock
    private FraudRuleRepository fraudRuleRepository;

    @Mock
    private KafkaTemplate<String, AnomalyDetectedEvent> kafkaTemplate;

    @InjectMocks
    private FraudDetectionService fraudDetectionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAnalyzeTransaction_AnomalyDetected() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn123");
        transaction.setAmount(15000);

        fraudDetectionService.analyzeTransaction(transaction);

        ArgumentCaptor<AnomalyDetectedEvent> eventCaptor = ArgumentCaptor.forClass(AnomalyDetectedEvent.class);
        verify(kafkaTemplate, times(1)).send(eq("anomalies"), eventCaptor.capture());

        AnomalyDetectedEvent capturedEvent = eventCaptor.getValue();
        assertEquals("txn123", capturedEvent.getTransactionId());
        assertEquals("Transaction amount exceeds $10,000", capturedEvent.getReason());
    }

    @Test
    void testGetFraudRules() {
        FraudRule rule = new FraudRule();
        rule.setRuleName("Test Rule");
        when(fraudRuleRepository.findAll()).thenReturn(Collections.singletonList(rule));

        List<FraudRule> rules = fraudDetectionService.getFraudRules();

        assertNotNull(rules);
        assertEquals(1, rules.size());
        assertEquals("Test Rule", rules.get(0).getRuleName());
    }
}
