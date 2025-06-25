package com.uc4.fraud_detection_service.service;

import com.uc4.fraud_detection_service.Exception.TransactionException;
import com.uc4.fraud_detection_service.model.AnomalyDetectedEvent;
import com.uc4.fraud_detection_service.model.FraudRule;
import com.uc4.fraud_detection_service.model.Transaction;
import com.uc4.fraud_detection_service.repository.FraudRuleRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"anomalies"})
class FraudDetectionServiceIntegrationTest {

    @Autowired
    private FraudDetectionService fraudDetectionService;

    @Autowired
    private FraudRuleRepository fraudRuleRepository;

    @Autowired
    private KafkaTemplate<String, AnomalyDetectedEvent> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private KafkaConsumer<String, AnomalyDetectedEvent> consumer;

    @BeforeEach
    void setUp() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        consumerProps.put("key.deserializer", StringDeserializer.class);
        consumerProps.put("value.deserializer", StringDeserializer.class);
        consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList("anomalies"));
    }

    @Test
    void testAnalyzeTransaction_AnomalyDetected() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn123");
        transaction.setAmount(15000);

        assertDoesNotThrow(() -> fraudDetectionService.analyzeTransaction(transaction));

        ConsumerRecords<String, AnomalyDetectedEvent> records = KafkaTestUtils.getRecords(consumer);
        assertFalse(records.isEmpty());

        ConsumerRecord<String, AnomalyDetectedEvent> record = records.iterator().next();
        AnomalyDetectedEvent event = record.value();
        assertEquals("txn123", event.getTransactionId());
        assertEquals("Transaction flagged as fraudulent", event.getReason());
    }

    @Test
    void testGetFraudRules() {
        FraudRule rule = new FraudRule() {
            @Override
            public boolean evaluate(Transaction transaction) {
                return false;
            }

            @Override
            public String getRuleName() {
                return "Test Rule";
            }
        };
        fraudRuleRepository.save(rule);

        List<FraudRule> rules = fraudDetectionService.getFraudRules();

        assertNotNull(rules);
        assertEquals(1, rules.size());
        assertEquals("Test Rule", rules.get(0).getRuleName());
    }

    @Test
    void testEvaluateTransaction_ExceptionHandling() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn123");
        transaction.setAmount(15000);

        assertThrows(TransactionException.class, () -> {
            fraudDetectionService.evaluateTransaction(transaction);
        });
    }
}