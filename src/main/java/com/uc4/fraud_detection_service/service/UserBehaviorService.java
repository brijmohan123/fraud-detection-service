package com.uc4.fraud_detection_service.service;

import com.uc4.fraud_detection_service.model.UserActivity;
import com.uc4.fraud_detection_service.repository.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserBehaviorService {

    @Autowired
    private UserActivityRepository userActivityRepository;

    public void recordActivity(UserActivity activity) {
        userActivityRepository.save(activity);
    }

    public boolean isAnomalous(String userId) {
        List<UserActivity> activities = userActivityRepository.findByUserId(userId);
        // Analyze activities for anomalies
        return detectAnomalies(activities);
    }

    private boolean detectAnomalies(List<UserActivity> activities) {
        // Example logic for detecting anomalies
        // Check for rapid login attempts, unusual locations, etc.
        return false;
    }
}
