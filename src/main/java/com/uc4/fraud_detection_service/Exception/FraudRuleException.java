
package com.uc4.fraud_detection_service.Exception;

public class FraudRuleException extends RuntimeException {
    public FraudRuleException(String message) {
        super(message);
    }

    public FraudRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
