package com.resourcemgmt.usermgmt.activities;

import com.resourcemgmt.usermgmt.controller.UsersController;
import com.resourcemgmt.usermgmt.dto.ActivityLogDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ActivityLogService {

    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "activityLogService", fallbackMethod = "activityLogFallback")
    public void logActivity(String action, String performedBy, String role, String module, String details) {
        ActivityLogDTO log = new ActivityLogDTO();
        log.setAction(action);
        log.setPerformedBy(performedBy);
        log.setRole(role);
        log.setModule(module);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());

        String url = "http://localhost:8080/api/activity";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(UsersController.TOKEN);

        HttpEntity<ActivityLogDTO> requestEntity = new HttpEntity<>(log, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
    }

    public void activityLogFallback(String action, String performedBy, String role, String module, String details, Throwable throwable) {
        // Log the fallback error or handle it as needed
        System.err.println("Failed to log activity: " + action + " due to " + throwable.getMessage());
    }

}