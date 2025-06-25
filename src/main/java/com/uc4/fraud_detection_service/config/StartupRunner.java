package com.uc4.fraud_detection_service.config;


import com.uc4.fraud_detection_service.service.FraudDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private FraudDetectionService fraudDetectionService;

    @Override
    public void run(String... args) {
        fraudDetectionService.getFraudRules(); // Load rules into cache
    }
}
