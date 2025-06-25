package com.uc4.fraud_detection_service.Controller;

import com.uc4.fraud_detection_service.model.UserActivity;
import com.uc4.fraud_detection_service.service.UserBehaviorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-activity")
public class UserActivityController {

    private static final Logger logger = LoggerFactory.getLogger(UserActivityController.class);
    @Autowired
    private UserBehaviorService userBehaviorService;

    @PostMapping("/record")
    public void recordActivity(@RequestBody UserActivity activity) {
        userBehaviorService.recordActivity(activity);
    }

    @GetMapping("/anomalous/{userId}")
    public boolean isAnomalous(@PathVariable String userId) {
        logger.info("Received transaction for analysis: {}", userId);
        return userBehaviorService.isAnomalous(userId);
    }
}
