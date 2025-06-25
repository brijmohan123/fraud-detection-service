package com.uc4.fraud_detection_service.repository;

import com.uc4.fraud_detection_service.model.FraudRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraudRuleRepository extends JpaRepository<FraudRule, Long> {
}
